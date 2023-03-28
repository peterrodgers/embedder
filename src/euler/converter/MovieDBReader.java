package euler.converter;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;

import euler.AbstractDiagram;
import euler.ConcreteDiagram;
import euler.DiagramPanel;
import euler.DualGraph;
import euler.GeneralConcreteDiagram;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.PlanarForceLayout;
import euler.simplify.GenerateJson;
import euler.simplify.Simplify;
import euler.utilities.*;
import pjr.graph.Edge;
import pjr.graph.GraphPanel;
import pjr.graph.Node;
import pjr.graph.drawers.BasicSpringEmbedder;

public class MovieDBReader {

	// input parsing
	HashMap<String,ArrayList<String>> actorListMap = new HashMap<>(); // actor list per title
	HashMap<String,ArrayList<String>> directorListMap = new HashMap<>(); // cinematographer per title
	HashMap<String,ArrayList<String>> cinematographerListMap = new HashMap<>(); // cinematographer per title
	HashMap<String,ArrayList<String>> genreListMap = new HashMap<>(); // genre list per title
	HashMap<String,Integer> yearMap = new HashMap<>(); // year per title
	
	
	// all these one per diagram
	ArrayList<HashMap<String,String>> labelMapList = new ArrayList<>();
	ArrayList<AbstractDiagram> abstractDiagramList = new ArrayList<>();
	ArrayList<HashMap<String,Integer>> zoneWeightsList = new ArrayList<>();
	
	// used to generate the above
	HashMap<String,String> labelMap;
	AbstractDiagram abstractDiagram;
	HashMap<String,Integer> zoneWeights;
	
	ArrayList<String> directorList = new ArrayList<>();
	
	protected static Random random = new Random(System.currentTimeMillis());
	
	public static void main(String[] args) {

		String fileLocation = "movieDB"+FileSystems.getDefault().getSeparator()+"moviedb.xml";
//directory = "tmp";
		MovieDBReader r = new MovieDBReader();
		r.loadAbstractDiagrams(fileLocation);
		
/*
ArrayList<String> movies = new ArrayList<>();
movies.add("Trapped (2002)");
movies.add("Woodsman, The (2004)");
movies.add("Distorted (2006)");
movies.add("Film Trix 2004 (2004)");
movies.add("In the Cut (2003)");
r.formAbstractDiagram(movies);
*/

//	r.createDiagramsByYearAndGenre(30);

	r.createDiagramsByDirector(50);

System.out.println("number of diagrams:|"+r.abstractDiagramList.size());


		for(int i = 0; i < r.abstractDiagramList.size(); i++) {
			
//if(i<16883) {continue;}
//if(!r.directorList.get(i).equals("Feig, Paul")) {continue;}
System.out.println(i+" "+r.directorList.get(i)+" "+r.abstractDiagramList.get(i));

			AbstractDiagram ad = r.abstractDiagramList.get(i);
//System.out.println("Abstract Diagram: "+ad.getZoneList());

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

/*			
DiagramDrawerPlanar.timeOutMillis = 2000;
//DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
boolean drawn = DiagramDrawerPlanar.layoutGraph(simplify.getDualGraph());
if(!drawn) {
	// exit if the planar layout fails. The current planar layout is not always successful. 
	System.out.println("Cannot generate a planar embedding");
	System.exit(0);

}

PlanarForceLayout pfl = new PlanarForceLayout(simplify.getDualGraph());
pfl.drawGraph();
simplify.getDualGraph().fitInRectangle(100,100,400,400);
*/

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
// this director fails to generate an original layout Harding, C.B. 0 a b c d
//if(r.directorList.get(i).equals("Harding, C.B.")) {
//	continue;
//}
/*
try {
		originalLayout(ad,r.directorList.get(i));
	}
	catch(Exception e) {
		try {
			System.out.println("RETRY "+r.directorList.get(i)+" "+ad);
			originalLayout(ad,r.directorList.get(i));
		}
		catch(Exception e2) {
			System.out.println("FAILED "+r.directorList.get(i)+" "+ad);
		}
	}
*/

if(p!=0 || c!=0) {
	System.out.println("SUMMARY start abstract diagram:|"+ad+"|planarity:|"+p+"|concurrency:|"+c+"|"+startText+"|total time:|"+simplify.totalTime+"|director:|"+r.directorList.get(i));
}

		}

		
	
	}

	
	
	/**
	 * @param sizeLimit creates only those diagrams under the limit
	 */
	public void createDiagramsByYearAndGenre(int sizeLimit) {

		HashMap<String,ArrayList<String>> movieMap = new HashMap<>();
		
		for(String movie : yearMap.keySet()) {
			Integer year = yearMap.get(movie);
			ArrayList<String> genreList = genreListMap.get(movie);
			if(genreList == null) {
				continue;
			}
			
			for(String genre : genreList) {
				String key = year+" "+genre;
				ArrayList<String> mappedMovies = movieMap.get(key);
				if(mappedMovies == null) {
					mappedMovies = new ArrayList<String>();
				}
				mappedMovies.add(movie);
				movieMap.put(key,mappedMovies);
			}
		}

		// create the globals
		labelMapList = new ArrayList<>();
		abstractDiagramList = new ArrayList<>();
		zoneWeightsList = new ArrayList<>();

		for(String k: movieMap.keySet()) {
			ArrayList<String> mappedMovies = movieMap.get(k);
			if(mappedMovies.size() > sizeLimit) {
				continue;
			}

			formAbstractDiagram(mappedMovies);
			
			labelMapList.add(labelMap);
			abstractDiagramList.add(abstractDiagram);
			zoneWeightsList.add(zoneWeights);
		}
		
		
	}

	/**
	 * @param sizeLimit creates only those diagrams under the limit
	 */
	public void createDiagramsByDirector(int sizeLimit) {

		HashMap<String,ArrayList<String>> movieMap = new HashMap<>();
		
		for(String movie : directorListMap.keySet()) {
			ArrayList<String> directorList = directorListMap.get(movie);
			
			for(String director : directorList) {
				ArrayList<String> mappedMovies = movieMap.get(director);
				if(mappedMovies == null) {
					mappedMovies = new ArrayList<String>();
				}
				mappedMovies.add(movie);
				movieMap.put(director,mappedMovies);
			}
		}

		// create the globals
		labelMapList = new ArrayList<>();
		abstractDiagramList = new ArrayList<>();
		zoneWeightsList = new ArrayList<>();

//int j = 0;

		directorList = new ArrayList<>();
		for(String director: movieMap.keySet()) {
			
			directorList.add(director);
			ArrayList<String> mappedMovies = movieMap.get(director);
			if(mappedMovies.size() > sizeLimit) {
				continue;
			}

			formAbstractDiagram(mappedMovies);
			
			labelMapList.add(labelMap);
			abstractDiagramList.add(abstractDiagram);
			zoneWeightsList.add(zoneWeights);
//j++;
//System.out.println(j+" "+director);
//System.out.println(j+" "+abstractDiagram);
//System.out.println(j+" "+zoneWeights);
		}
		
		
	}


	/**
 	 * Converts the directory/zoneList .zones files into abstract descriptions and zoneWeights
	 */
	public void loadAbstractDiagrams(String fileLocation) {
		
		abstractDiagramList = new ArrayList<>();
		zoneWeightsList = new ArrayList<>();
		
		String fileText = "";

		File movieFile = new File(fileLocation);
		try {
			Scanner scan = new Scanner(movieFile);  
			scan.useDelimiter("\\Z");
			if(scan.hasNext()) {
				fileText = scan.next();
			} else {
				fileText = "";
			}
			
			scan.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		
		String[] splitFileText = fileText.split("\n");
		
		String movieTitle = "";
		for(int i = 0; i < splitFileText.length; i++) {
			String fileLine = splitFileText[i];
			int startTitle = fileLine.indexOf("title=\"");
			if(startTitle != -1) {
				String endLine = fileLine.substring(startTitle);
				String[] splitLine = endLine.split("\"");
				movieTitle = splitLine[1];
//System.out.println(movieTitle+" "+endLine);
			}
			
			
			int startActor = fileLine.indexOf("<actor");
			if(startActor != -1) {
				String endLine = fileLine.substring(startActor);
				String[] splitLine = endLine.split(">");
				String[] splitLine2 = splitLine[1].split("<");
				String name = splitLine2[0];
				if(actorListMap.get(movieTitle) == null) {
					ArrayList<String> list = new ArrayList<>();
					list.add(name);
					actorListMap.put(movieTitle, list);
				} else {
					ArrayList<String> list = actorListMap.get(movieTitle);
					if(!list.contains(name)) {
						list.add(name);
					}
				}
			}
			
			
			
			int startDirector = fileLine.indexOf("<director");
			if(startDirector != -1) {
				String endLine = fileLine.substring(startDirector);
				String[] splitLine = endLine.split(">");
				String[] splitLine2 = splitLine[1].split("<");
				String name = splitLine2[0];
				if(directorListMap.get(movieTitle) == null) {
					ArrayList<String> list = new ArrayList<>();
					list.add(name);
					directorListMap.put(movieTitle, list);
				} else {
					ArrayList<String> list = directorListMap.get(movieTitle);
					if(!list.contains(name)) {
						list.add(name);
					}
				}
			}
			
			
			int startCinematographer = fileLine.indexOf("<cinematographer");
			if(startCinematographer != -1) {
				String endLine = fileLine.substring(startCinematographer);
				String[] splitLine = endLine.split(">");
				String[] splitLine2 = splitLine[1].split("<");
				String name = splitLine2[0];
				if(cinematographerListMap.get(movieTitle) == null) {
					ArrayList<String> list = new ArrayList<>();
					list.add(name);
					cinematographerListMap.put(movieTitle, list);
				} else {
					ArrayList<String> list = cinematographerListMap.get(movieTitle);
					if(!list.contains(name)) {
						list.add(name);
					}
				}
			}
			
			
			int startGenre = fileLine.indexOf("<genre");
			if(startGenre != -1) {
				String endLine = fileLine.substring(startGenre);
				String[] splitLine = endLine.split(">");
				String[] splitLine2 = splitLine[1].split("<");
				String name = splitLine2[0];
				if(genreListMap.get(movieTitle) == null) {
					ArrayList<String> list = new ArrayList<>();
					list.add(name);
					genreListMap.put(movieTitle, list);
				} else {
					ArrayList<String> list = genreListMap.get(movieTitle);
					if(!list.contains(name)) {
						list.add(name);
					}
				}
			}
			
			
			int startYear = fileLine.indexOf("year=\"");
			if(startYear != -1) {
				String endLine = fileLine.substring(startYear);
				String[] splitLine = endLine.split("\"");
				String[] splitLine2 = splitLine[1].split("\"");
				String yearString = splitLine2[0];
				Integer yearInt = Integer.parseInt(yearString);
				if(yearMap.get(movieTitle) == null) {
					yearMap.put(movieTitle, yearInt);
				} else {
System.out.println("ERROR DUPLICATE YEAR FOR MOVIE: "+movieTitle+" year "+yearMap);

				}
			}
			
			
			
		}

		/*
int j = 0;
for(String k : year.keySet()) {
	j++;
	System.out.println(j+" "+k+" "+year.get(k));
}
*/
		
			
//System.out.println(shortName);
//System.out.println(zoneText);
		
	}



	/**
	 * Return a simplify using the abstract diagram formed from only the movies in the list,
	 * with zone weights based on the actors sharing movies. A weight of 0 means no zone.
	 * 
	 * creates the globals
	 * 	HashMap<String,String> labelMap;
	 *  AbstractDiagram abstractDiagram;
	 *  HashMap<String,Integer> zoneWeights;
	 * 
	 * @param movieList
	 * @return
	 */
	public void formAbstractDiagram(ArrayList<String> movieList) {
		
		labelMap = new HashMap<>();
		zoneWeights = new HashMap<>();
		
		HashMap<String,String> reverseLabelMap = new HashMap<>();

		char c = 'a';
		for(String movie : movieList) {
			String letter = Character.toString(c);
			labelMap.put(movie,letter);
			reverseLabelMap.put(movie,letter);
			c++;
		}

		ArrayList<String> actors = findActorList(movieList);

		HashMap<String,String> zoneMap = new HashMap<>();

		zoneWeights.put("",0);
		for(String actor : actors) {
			String zone = "";
			for(String movie : movieList) {
				String contour = labelMap.get(movie);
				ArrayList<String> actorsInMovie = actorListMap.get(movie);
				
				if(actorsInMovie.contains(actor)) {
					zone += contour;
				}
			}
			zone = AbstractDiagram.orderZone(zone);

			zoneMap.put(actor,zone);
			
			Integer weight = zoneWeights.get(zone);
			if(weight == null) {
				weight = 0;
			}
			weight++;
			zoneWeights.put(zone,weight);
		}
		
		ArrayList<String> zoneList = new ArrayList<>();
		for(String zone : zoneWeights.keySet()) {
			zoneList.add(zone);
		}
		abstractDiagram = new AbstractDiagram(zoneList);

	}


	
	/**
	 * Finds all the actors in the movieList
	 * @param movieList
	 * @return
	 */
	private ArrayList<String> findActorList(ArrayList<String> movieList) {
		
		ArrayList<String> ret = new ArrayList<>();
		for(String movie : movieList) {
			ArrayList<String> actors = actorListMap.get(movie);
			for(String actor : actors){
				if(!ret.contains(actor)) {
					ret.add(actor);
				}
			}
		}
		return ret;
	}
	
	
	
	/**
	 * Take the abstract diagram
	 * - create a superdual
	 * - connect up any disconnected dual graph components
	 * - remove edges to find a planar dual
	 * @return concrete dual or null if abstract diagram is not atomic. 
	 */
	public static DualGraph generalDualFromAbstract(AbstractDiagram ad) {

		random = new Random(111);
			
		DualGraph dg = new DualGraph(ad);
		
		// connect up a disconnected dual graph
		ArrayList<ArrayList<Node>> subgraphs = dg.findDisconnectedSubGraphs(null);
		ArrayList<Node> firstSubgraph = subgraphs.get(0);
		subgraphs.remove(0);
		for(ArrayList<Node> subgraph : subgraphs) {
			Edge e = new Edge(firstSubgraph.get(0),subgraph.get(0));
			dg.addEdge(e);
		}
		
		// find a planar layout with minimal edges removed
		// aiming to maintain well-connectedness
		if(!dg.checkConnectivity()) {
			DualGraph newDual = dg.findWellformedPlanarGraph();
			boolean planar = DiagramDrawerPlanar.planarLayout(dg);
			if(planar) {
				newDual = dg;
			} else if(newDual == null) {
				//System.out.println("can not find a wellformed planar graph after edge removing");
				BasicSpringEmbedder se = new BasicSpringEmbedder();
				se.setGraphPanel(new GraphPanel(dg, new Frame()));
				se.layout();
				DualGraph temp = null;
				while(temp == null) {
					temp = DualGraph.findNonWellformedPlanarGraph(dg);
				}
				newDual = temp;
			}
				 
				// nasty fix in case of non-planar result
			planar = DiagramDrawerPlanar.planarLayout(dg);
			while(!planar) {
System.out.println("finding planar layout by random edge removal");
					
				dg.removeEdge(dg.getEdges().get(random.nextInt(dg.getEdges().size())));
				subgraphs = dg.findDisconnectedSubGraphs(null);
				firstSubgraph = subgraphs.get(0);
				subgraphs.remove(0);
				for(ArrayList<Node> subgraph : subgraphs) {
System.out.println("Adding Edge ");
					Edge e = new Edge(firstSubgraph.get(0),subgraph.get(0));
					dg.addEdge(e);
				}
				planar = DiagramDrawerPlanar.planarLayout(dg);

			}

			dg = newDual;
		}
		
		// if the planar graph is not well-connected
		// make it as well-connected as possible by
		// adding the smallest parallel edges
		dg.connectDisconnectedComponents();
		
		DiagramDrawerPlanar.planarLayout(dg);

		// draw the graph nicely before triangulating
		PlanarForceLayout pfl = new PlanarForceLayout(dg);
		pfl.setAnimateFlag(false);
		pfl.drawGraph();
		if(dg.findEdgeCrossings().size() > 0) {
System.out.println("STANDARD PLANAR FORCE LAYOUT FAILED to generate nice layout failed, restoring planar embedding");
			// here the nice layout algorithm fails, so restore planar embedding
			DiagramDrawerPlanar.planarLayout(dg);
		}

		
		// DRAWING OF HOLES AND DUPLICATE CONTOURS by may be done here by
		// renaming or done in the contour layout process, probably
		// not here

		// draw the graph

		// Lay the dual graph out nicely
		
		// split any faces that need splitting
		// dg.addAllFaceSplits();
		
		return dg;
		
	}

	
	

	/**
	 * Data from original layout of DG
	 * @param startDG
	 */
	public static void originalLayout(AbstractDiagram ad, String director) {
		
		DualGraph dg = generalDualFromAbstract(ad);
		
//System.out.println(dg);		
/*		
		GeneralConcreteDiagram concreteDiagram = new GeneralConcreteDiagram(dg);
		concreteDiagram.generateContours();
		concreteDiagram.setConcurrentOffset(ConcreteDiagram.CONCURRENT_OFFSET);
		concreteDiagram.setOptimizeContourAngles(true);
		concreteDiagram.setOptimizeMeetingPoints(true);
		concreteDiagram.setFitCircles(true);
		concreteDiagram.routeContours();
*/		
//System.out.println(startDG);
		DualGraphWindow dw = new DualGraphWindow(dg);
		dw.getDiagramPanel().setShowGraph(true);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(false);
		dw.getDiagramPanel().setShowContourLabel(false);
		dw.getDiagramPanel().setShowTriangulation(false);
		dw.getDiagramPanel().setJiggleLabels(false);
		
		
		dw.getDiagramPanel().setForceNoRedraw(true);
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
	 	ddp.layout();
	 	
		PlanarForceLayout pfl = new PlanarForceLayout(dw.getDiagramPanel());
		pfl.setAnimateFlag(false);
		pfl.setIterations(50);
		pfl.drawGraph();
		dw.getDiagramPanel().fitGraphInPanel();

		
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());
		
		DiagramPanel panel = dw.getDiagramPanel();
		
		panel.setShowGraph(true);
		panel.setShowRegion(false);
		panel.setShowContour(true);
		panel.setShowTriangulation(true);
		
		panel.setShowEdgeLabel(true);
		panel.setShowContourLabel(true);
		panel.setShowContourAreas(false);
		panel.setOptimizeContourAngles(true);
		panel.setOptimizeMeetingPoints(true);
		panel.setFitCircles(false);
		
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());

		
int concurrency = Simplify.countConcurrency(dg);
int startContourCount = ad.getContours().size();
int endContourCount = dg.findAbstractDiagram().getContours().size();
int extraContourCount = endContourCount - startContourCount;
System.out.println("ORIGINAL LAYOUT|"+ad+"|concurrency:|"+concurrency+"|duplicate curves:|"+extraContourCount+"|director:|"+director);
		dw.dispose();
	}




}

