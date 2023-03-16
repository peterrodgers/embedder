package euler.converter;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;

import euler.AbstractDiagram;
import euler.DualGraph;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.PlanarForceLayout;
import euler.simplify.GenerateJson;
import euler.simplify.Simplify;

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
			AbstractDiagram ad = r.abstractDiagramList.get(i);
//System.out.println("Abstract Diagram: "+ad.getZoneList());

			HashMap<String,Integer> zoneWeights = r.zoneWeightsList.get(i);
//System.out.println("zoneWeights: "+zoneWeights);
			
			if(ad.getContours().size() == 0) {
				continue;
			}
			Simplify simplify = new Simplify(ad);
			
String startText = "|start number of sets:|"+simplify.getAbstractDiagram().getContours().size()+"|start number of nodes:|"+simplify.getDualGraph().getNodes().size()+"|start number of edges:|"+simplify.getDualGraph().getEdges().size();
			
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
	System.out.println("SUMMARY start abstract diagram:|"+ad+"|planarity:|"+p+"|concurrency:|"+c+"|"+startText+"|total time:|"+simplify.totalTime);
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

		for(String director: movieMap.keySet()) {
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
	
	
	


}

