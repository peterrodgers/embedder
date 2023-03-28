package euler.converter;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;

import euler.AbstractDiagram;
import euler.DualGraph;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.PlanarForceLayout;
import euler.simplify.GenerateJson;
import euler.simplify.Simplify;

public class TwitterDiagramReader {

	// all these one per diagram
	ArrayList<String> fileNameList = new ArrayList<>();
	ArrayList<HashMap<String,String>> labelMapList = new ArrayList<>();
	ArrayList<AbstractDiagram> abstractDiagramList = new ArrayList<>();
	ArrayList<HashMap<String,Integer>> zoneWeightsList = new ArrayList<>();
	
	
	public static void main(String[] args) {

		String directory = "twitterData";
//directory = "tmp";
		TwitterDiagramReader r = new TwitterDiagramReader();
		r.loadAbstractDiagrams(directory);
		
System.out.println("number of files:|"+r.fileNameList.size());

		for(int i = 0; i < r.abstractDiagramList.size(); i++) {
			

			AbstractDiagram ad = r.abstractDiagramList.get(i);
//System.out.println("Abstract Diagram: "+ad.getZoneList());
			
System.out.println(i+" "+r.fileNameList.get(i)+" "+r.abstractDiagramList.get(i));


			HashMap<String,Integer> zoneWeights = r.zoneWeightsList.get(i);
			HashMap<String,String> labelMap = r.labelMapList.get(i);
//System.out.println("zoneWeights: "+zoneWeights);

			if(ad.getContours().size() < 3) {
				continue;
			}
			
			Simplify simplify = new Simplify(ad);
String startText = "|start number of sets:|"+simplify.getAbstractDiagram().getContours().size()+"|start number of nodes:|"+simplify.getDualGraph().getNodes().size()+"|start number of edges:|"+simplify.getDualGraph().getEdges().size()+"|start zone weights:|"+zoneWeights+"|start label mapping:|"+labelMap+"|concurrency count:|"+Simplify.countConcurrency(simplify.getDualGraph());
			simplify.setZoneWeights(zoneWeights);

			simplify.simplifyUntilPlanar();
			

			// find planar embedding of the dual graph
//			boolean drawn = DiagramDrawerPlanar.layoutGraph(simplify.getDualGraph());
//			if(!drawn) {
				// exit if the planar layout fails. The current planar layout is not always successful. 
//				System.out.println("Cannot generate a planar embedding");
//				continue;
//			} else {
//				System.out.println();
//			}
			
			//spring embed for nice layout. Further simplification will be based on this layout
//			PlanarForceLayout pfl = new PlanarForceLayout(simplify.getDualGraph());
//			pfl.drawGraph();
//			simplify.getDualGraph().fitInRectangle(100,100,400,400);

			// json output of first planar graph
//			GenerateJson gs = new GenerateJson(simplify);
//			System.out.println(gs.jsonOutput());

			// iterate to remove concurrency, show the json at each stage
			while(simplify.getDualGraph().hasConcurrentEdges()) {
				simplify.reduceConcurrencyInDualGraph();
//				System.out.println(gs.jsonOutput());
			}

			
//System.out.println(simplify.getTypeMergeHistory());

int p = 0;
int c = 0;
for(String s : simplify.getTypeMergeHistory()) {
	if(s.equals(Simplify.PLANARITY_TYPE)) {
		p++;
	}
	if(s.equals(Simplify.CONCURRENCY_TYPE)) {
		c++;
	}
}

if(p!=0 || c!=0) {
	System.out.println("SUMMARY start abstract diagram:|"+ad+"|planarity:|"+p+"|concurrency:|"+c+"|"+startText+"|total time:|"+simplify.totalTime+"|file:|"+r.fileNameList.get(i)+".zones");
}


try {
Simplify.originalLayout(ad,r.fileNameList.get(i),true);
} catch (Exception e) {
	System.out.println("FAILED "+ad+" "+r.fileNameList.get(i));
}


		}

	}

	
	/**
 	 * Converts the directory/zoneList .zones files into abstract descriptions and zoneWeights
	 */
	public void loadAbstractDiagrams(String directory) {
		
		abstractDiagramList = new ArrayList<>();
		zoneWeightsList = new ArrayList<>();

		String zonePath = directory+FileSystems.getDefault().getSeparator()+"zoneList"; 
		File folder = new File(zonePath);
		File[] listOfFiles = folder.listFiles(); 
		ArrayList<String> fileNames = new ArrayList<String>();
		 
		for (int i = 0; i < listOfFiles.length; i++) {
	 
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (fileName.endsWith(".zones")) {
					int index = fileName.indexOf('.');
					String shortName = fileName.substring(0,index);
					fileNames.add(shortName);
				}
			}
		}
		
		for(String shortName : fileNames) {
			File zoneFile = new File(folder, shortName+".zones");
			String zoneText = "";
			try {
				Scanner scan = new Scanner(zoneFile);  
				scan.useDelimiter("\\Z");
				if(scan.hasNext()) {
					zoneText = scan.next();
				} else {
					zoneText = "";
				}
				
				scan.close();
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
			
			findAbstractDiagram(shortName,zoneText);
			
//System.out.println(shortName);
//System.out.println(zoneText);
		}
		
	}


	public void findAbstractDiagram(String shortName, String zoneText) {

		
		String[] splitZones = zoneText.split("\n");
		
		ArrayList<String> contours = new ArrayList<>();
		HashMap<String,String> labelMap = new HashMap<>();
		HashMap<String,Integer> zoneWeightMap = new HashMap<>();

		ArrayList<ArrayList<String>> longZones = new ArrayList<>();
		HashMap<ArrayList<String>,Integer> longWeightMap = new HashMap<>();

		for(int i = 0; i < splitZones.length; i++) {
			ArrayList<String> longZone = new ArrayList<>();
			String zoneString = splitZones[i].trim();
			String[] splitContours = zoneString.split(" ");

			if(zoneString.equals("")) { // empty zone
				continue;
			}

			for(int j = 0; j < splitContours.length-1; j++) {
				String contour = splitContours[j];
				longZone.add(contour);
				if(!contours.contains(contour)) {
					contours.add(contour);
				}
			}
			Collections.sort(longZone);
			longZones.add(longZone);
			String weightString = splitContours[splitContours.length-1];

			int weight = Integer.parseInt(weightString);
			longWeightMap.put(longZone,weight);

		}
		
		Collections.sort(contours);
		// populate the label mapping
		
		char c = 'a';
		for(String contour : contours) {
			String letter = Character.toString(c);
			labelMap.put(contour,letter);
			c++;
		}

		String adString = "0";
		zoneWeightMap.put("",1); // no empty set entry for weights, so set it to 1 as a default
		for(ArrayList<String> longZone : longZones) {
			String zone = "";
			for(String longContour : longZone) {
				String contour = labelMap.get(longContour);
				zone = zone+contour;
			}
			int weight = longWeightMap.get(longZone);
			zoneWeightMap.put(zone,weight);
			adString = adString +" "+zone;
		}
		
		
		AbstractDiagram ad = new AbstractDiagram(adString);
		
		// populate the globals
		fileNameList.add(shortName);
		labelMapList.add(labelMap);
		abstractDiagramList.add(ad);
		zoneWeightsList.add(zoneWeightMap);
		
	}




}

