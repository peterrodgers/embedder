package euler.data;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/** 
 * Convert twitter ego circles from snap into area proportional .zone files
 * It reads and writes various files and directories in twitterData directory.
 * Only needs to run once.
 * @author pjr
 *
 */
public class TwitterConverter {
	
	Random random = new Random(System.currentTimeMillis());

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> circles = new ArrayList<String>();
	ArrayList<String> nodes = new ArrayList<String>();
	ArrayList<ArrayList<String>> circleNodes = new ArrayList<ArrayList<String>>();
	ArrayList<String> lists1 = new ArrayList<String>();
	ArrayList<String> lists2 = new ArrayList<String>();



	public static void main(String[] args) {


		// the following recreates the zoneList directory from the twitter directories, randomly reassigning names

		String directory = "twitterData";
		TwitterConverter c = new TwitterConverter();
		c.convert(directory);
	
	}
	
	
	public TwitterConverter() {
	}


	public void convert(String directory) {
		String path = directory+FileSystems.getDefault().getSeparator()+"twitter"; 
		String newPath = directory+FileSystems.getDefault().getSeparator()+"renamedTwitter"; 
		String zonePath = directory+FileSystems.getDefault().getSeparator()+"zoneList"; 
		String zoneDataText = "file\tNumber Of Intersections\tNumber Of Zones\n";
		String sizeDataText = "file\tnumber Of Sets\tNumber Of Zones\tNumber Of Nodes\n";

		
		ArrayList<String> fileNames = new ArrayList<String>();
	 
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles(); 
	 
		for (int i = 0; i < listOfFiles.length; i++) {
	 
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (fileName.endsWith(".circles")) {
					int index = fileName.indexOf('.');
					String shortName = fileName.substring(0,index);
					fileNames.add(shortName);
				}
			}
		}

		for(String shortName : fileNames) {
			File circleFile = new File(folder, shortName+".circles");
			String circleText = "";
			try {
				Scanner scan = new Scanner(circleFile);  
				scan.useDelimiter("\\Z");
				if(scan.hasNext()) {
					circleText = scan.next();
				} else {
					circleText = "";
				}
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
				
			}
			
/*			File edgeFile = new File(shortName+".edges");
			String edgeText = "";
			try {
				Scanner scan = new Scanner(edgeFile);  
				scan.useDelimiter("\\Z");
				edgeText = scan.next();
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
*/			
			findNodes(circleText);
			
			if(nodes.size() >= 400) {
				System.out.println("Ignoring file "+circleFile+" as there are "+nodes.size()+" nodes");
				continue;
			}
			if(circles.size() >= 15) {
				System.out.println("Ignoring file "+circleFile+" "+circles.size()+" circles (sets)");
				continue;
			}
			
			
			findNames(directory);
			findlists(directory);

			if(nodes.size() > names.size()) {
				continue;
			}
			for(int i = 0; i < nodes.size(); i++) {
				String node = nodes.get(i);
				String name = names.get(i);
//System.out.println(shortName+" "+nodes.get(i)+" "+names.get(i));
				
				circleText = circleText.replace(node,name);
//				edgeText = edgeText.replace(node, name);
			}

			// backwards to account for 10 being replaced by 1 and 0
			for(int i = circles.size()-1; i >= 0 ; i--) {
				String circle = circles.get(i);
				String list = "";
				if(i < lists1.size()) {
					 list = lists1.get(i);
				} else {
					list = lists2.get(i-lists1.size());
				}
				
				circleText = circleText.replace(circle,list);
			}
			
			
//System.out.println(shortName+"\tsets\t"+circles.size()+"\tnodes\t"+nodes.size());			
//this stuff creates the new files
			try {
				BufferedWriter b = new BufferedWriter(new FileWriter(newPath+"//"+shortName+"-edit.circles"));
				b.append(circleText);
				b.close();
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();

			}

/*			try {
				BufferedWriter b = new BufferedWriter(new FileWriter("newPath//"+shortName+"-edit.edges"));
				b.append(edgeText);
				b.close();
			} catch(Exception e) {
				System.out.println(e);
			}
*/

			findNodes(circleText); // run this again as the text has been renamed
System.out.println("\n"+circleText+"\n-------");

			// form the zones

			HashMap<ArrayList<String>,Integer> zoneCardinalities = new HashMap<ArrayList<String>,Integer>();
			ArrayList<ArrayList<String>> zones = new ArrayList<ArrayList<String>>();
			for(String node : nodes) {
				ArrayList<String> zone = new ArrayList<String>();
				for(int i=0; i< circles.size();i++) {
					String circle = circles.get(i);
					ArrayList<String> members = circleNodes.get(i);
					if(members.contains(node)) {
						zone.add(circle);
					}
				}
				if(!zones.contains(zone)) {
					zones.add(zone);
					zoneCardinalities.put(zone, 1);
				} else {
					Integer cardinality = zoneCardinalities.get(zone);
					cardinality++;
					zoneCardinalities.put(zone, cardinality);

				}
			}
			
			
			String zoneText = "";
			
			for(ArrayList<String> z : zones) {
				for(String person : z) {
					zoneText += person+" ";
				}
				Integer cardinality = zoneCardinalities.get(z);
				zoneText += cardinality;
				zoneText += "\n";
			}

			// generate stats
			HashMap<Integer, Integer> sizeMap = new HashMap<Integer, Integer>(); // number of intersections, number of zones
			int max = 0;
			for(ArrayList<String> zone : zones) {
				int numberOfIntersections = zone.size();
				Integer size = sizeMap.get(numberOfIntersections);
				if(size == null) {
					size = 1;
				} else {
					size++;
				}
				sizeMap.put(numberOfIntersections,size);
				if(max < numberOfIntersections) {
					max = numberOfIntersections;
				}
			}
			for(int i = 0; i<= max; i++) {
				Integer size = sizeMap.get(i);
				if(size == null) {
					sizeMap.put(i, 0);
				}
			}
			
//System.out.print(zoneText);
//System.out.println("file\tnumber Of Intersections\tnumber Of Zones");
			for(int i : sizeMap.keySet()) {
				zoneDataText += shortName+"\t"+i+"\t"+sizeMap.get(i)+"\n";
			}

//System.out.print(zoneText);
System.out.println("file\tnumber Of Sets\tnumber Of Zones\tnumber of Nodes");
			int count = 0;
			for(ArrayList<String> zone : zones) {
				count += zone.size();
			}
			sizeDataText += shortName+"\t"+circles.size()+"\t"+zones.size()+"\t"+nodes.size()+"\n";
System.out.println(shortName+"\t"+circles.size()+"\t"+zones.size()+"\t"+nodes.size());


			//this stuff creates the new files
			try {
				BufferedWriter b = new BufferedWriter(new FileWriter(zonePath+"//"+shortName+".zones"));
				b.append(zoneText);
				b.close();
			} catch(Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		
		//output summary data for files
		try {
//System.out.println(zoneDataText);
			BufferedWriter b = new BufferedWriter(new FileWriter(zonePath+"//zoneData.txt"));
			b.append(zoneDataText);
			b.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(zonePath+"//sizeData.txt"));
			b.append(sizeDataText);
			b.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}


	private void findNames(String directory) {
		
		names = new ArrayList<String>();
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(directory+FileSystems.getDefault().getSeparator()+"names.txt"));
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}
				
				names.add(line);
				
				line = b.readLine();

			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}
		
		names = shuffle(names);
		
	}


	private void findlists(String directory) {
		
		lists1 = new ArrayList<String>();
		lists2 = new ArrayList<String>();
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(directory+FileSystems.getDefault().getSeparator()+"lists.txt"));
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}

				lists1.add(line);
				line = b.readLine();

			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}
		
		lists1 = shuffle(lists1);
		
		
		// remove duplicate start letters
		String startLetters = "";
		ArrayList<String> remove = new ArrayList<String>();
		for(int i = 0; i < lists1.size(); i++) {
			char startLetter = Character.toUpperCase(lists1.get(i).charAt(0));
			if(startLetters.indexOf(startLetter) == -1) {
				startLetters += startLetter;
			} else {
				remove.add(lists1.get(i));
			}
		}
		
		for(String s : remove) {
			lists1.remove(s);
			lists2.add(s);
		}

		
	}


	public ArrayList<String> shuffle(ArrayList<String> strings) {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		while(strings.size() > 0) {
			int index = random.nextInt(strings.size());
			
			String s = strings.remove(index);
			ret.add(s);
		}
		
		return ret;
		
	}


	private void findNodes(String circleText) {
		
		nodes = new ArrayList<String>();
		circles = new ArrayList<String>();
		circleNodes = new ArrayList<ArrayList<String>>();
		
		String[] lines = circleText.split("\n");

		for(int i = 0; i < lines.length; i++) {
			ArrayList<String> nodeList = new ArrayList<String>();
			String line = lines[i];
			String[] nodeArray = line.split("\t");
			circles.add(nodeArray[0]);
			for(int j = 1; j < nodeArray.length; j++) { // first element is circle id
				String node = nodeArray[j]; 
				nodes.add(node);
				nodeList.add(node);
			}
			circleNodes.add(nodeList);
		}

	}

	
	
}
