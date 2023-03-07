package euler.converter;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import euler.AbstractDiagram;

public class TwitterDiagramReader {

	// all these one per diagram
	ArrayList<String> fileNames = new ArrayList<>();
	ArrayList<HashMap<String,Integer>> labelMapList = new ArrayList<>();
	ArrayList<String> abstractDiagrams = new ArrayList<>();
	ArrayList<HashMap<String,Integer>> zoneWeightsList = new ArrayList<>();

	public static void main(String[] args) {


		// the following recreates the zoneList directory from the twitter directories, randomly reassigning names

		String directory = "twitterData";
		TwitterDiagramReader r = new TwitterDiagramReader();
		r.loadAbstractDiagrams(directory);
	
	}

	
	/**
 	 * Converts the directory/zoneList .zones files into abstract descriptions and zoneWeights
	 */
	public void loadAbstractDiagrams(String directory) {
		
		abstractDiagrams = new ArrayList<>();
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

		fileNames.add(shortName);
		
		String[] splitZones = zoneText.split("\n");
		
		ArrayList<String> contours = new ArrayList<>();
		
System.out.println("ZZZ "+zoneText);
		for(int i = 0; i < splitZones.length; i++) {
System.out.print("XXX "+i+" "+splitZones[i]+" ");

String z = AbstractDiagram.orderZone(zoneString); 

		}
System.out.println();

		
	}




}
