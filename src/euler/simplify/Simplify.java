package euler.simplify;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import euler.AbstractDiagram;
import euler.ConcreteDiagram;
import euler.DiagramPanel;
import euler.DualGraph;
import euler.GeneralConcreteDiagram;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.DiagramDrawerSpringEmbedder;
import euler.drawers.PlanarForceLayout;
import pjr.graph.Edge;
import pjr.graph.GraphPanel;
import pjr.graph.Node;
import pjr.graph.drawers.BasicSpringEmbedder;


/**
 * 
 * Code based on the simplification ideas from Dagstuhl Seminar 22462.
 * 
 * @author Peter Rodgers
 */

public class Simplify {
	
	public boolean outputDataFlag = true;
	public double totalTime = 0.0;
	
	private static final int DEFAULT_WEIGHT = -1;
	
	private static HashMap<Node,String> nodeVertexMap = null;
	private static HashMap<String,Node> vertexNodeMap = null;
	
	private AbstractDiagram abstractDiagram = null;
	private DualGraph dualGraph = null;
	private HashMap<String,Integer> zoneWeights = new HashMap<>();

	/** Earliest first. Previous history of dual graphs */
	private ArrayList<DualGraph> dualGraphHistory = new ArrayList<>();
	/** Earliest first. Merged pairs, first of the pair is the merged label */
	private ArrayList<String[]> setMergeHistory = new ArrayList<>();
	/** Earliest first */
	private ArrayList<AbstractDiagram> abstractDiagramMergeHistory = new ArrayList<>();
	/** Earliest first */
	public ArrayList<String> typeMergeHistory = new ArrayList<>();
	
	public static String PLANARITY_TYPE = "Planarity";
	public static String CONCURRENCY_TYPE = "Concurrency";
	
	protected static Random random = new Random(System.currentTimeMillis());
	

	
	public static void main(String[] args) {
		AbstractDiagram ad = null;
		ad = new AbstractDiagram("0 a b c d e bcd cde abcde"); //Zhmutski, Peter
		// an example set system
		//ad = new AbstractDiagram("0 a b c d e f ab ac abe cde ade abc adbe abcd abce bcdf abcdef");
		//ad = new AbstractDiagram("0 abd abh adg cfg acdh acfg bceg bdef cdef defg abcef abcfh acdfh bcefh bdfgh cdefg acdefg bcdegh abcdefg");
		//ad = new AbstractDiagram("0 f ad bd dg adf bdg bef bgh cfh dgh abch bcdg cdfg cfgh degh efgh acdeh bdefh abdefh");
		//ad = new AbstractDiagram("0 hi ik fhi cdeg cefh efgh eghi ghil hijl abcefgh acdefgh ghjkl hijlmn ghijlmn abcdefhi bcdefghi fgijklmn");
		// comment out the above and use the below for random diagrams
		//ad = AbstractDiagram.randomDiagramFactory(6,true,0.15);
		// the below results in a bug, needs investigating
		//ad = new AbstractDiagram("0 def dfh abce abcf abef bceg bcgh cefg cfgh abdeg abdfg abfgh acdeg acdfh bcdef abcdfh acdefg acefgh");
		// create a Simplify to allow the simplification of the dual graph
		Simplify simplify = new Simplify(ad);
		simplify.randomizeWeights(1,10);

		// original layout
		AbstractDiagram abstractDiagram = new AbstractDiagram("0 a b c d e bcd cde abcde"); //Zhmutski, Peter
		DualGraph dg = Simplify.originalLayout(abstractDiagram,"Zhmutski, Peter", false);
		String jsonDiagram = GenerateJson.jsonOutputOrginalEmbedder(abstractDiagram,dg);
		System.out.println(jsonDiagram);

		
		// simplify the dual graph till it has a planar layout
/*		simplify.simplifyUntilPlanar();

		// find planar embedding of the dual graph
		DiagramDrawerPlanar.timeOutMillis = 2000;
		boolean drawn = DiagramDrawerPlanar.layoutGraph(simplify.getDualGraph());
		if(!drawn) {
			// exit if the planar layout fails. The current planar layout is not always successful. 
			System.out.println("Cannot generate a planar embedding");
			System.exit(0);

		}

		PlanarForceLayout pfl = new PlanarForceLayout(simplify.getDualGraph());
		pfl.drawGraph();
		simplify.getDualGraph().fitInRectangle(100,100,400,400);
		// json output of first planar graph
		GenerateJson gs = new GenerateJson(simplify);
		System.out.println(gs.jsonOutput());

	// iterate to remove concurrency, show the json at each stage
		while(simplify.getDualGraph().hasConcurrentEdges()) {
			simplify.reduceConcurrencyInDualGraph();
			System.out.println(gs.jsonOutput());
		}
*/	
		//uncomment for display
/*		DualGraphWindow dw = new DualGraphWindow(simplify.getDualGraph());
		dw.getDiagramPanel().setShowGraph(true);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(true);
		dw.getDiagramPanel().setShowContourLabel(true);
		dw.getDiagramPanel().setShowTriangulation(true);
*/		
		// uncomment for merge history
/*		
		for(DualGraph dg : simplify.dualGraphHistory) {
			System.out.println("dual graph history: "+dg);		
		}
		for(String type : simplify.typeMergeHistory) {
			System.out.println("type merge history: "+type);		
		}
		for(String[] h : simplify.setMergeHistory) {
			System.out.println("set merge history: "+h[0]+" "+h[1]);		
		}
		for(AbstractDiagram h : simplify.abstractDiagramMergeHistory) {
			System.out.println("abstract diagram history: "+h);		
		}
		if(simplify.abstractDiagramMergeHistory.size() != 0) {
			System.out.println("current abstract diagram: "+simplify.abstractDiagram);
		}
*/	

	}

	
	public AbstractDiagram getAbstractDiagram() {return abstractDiagram;}
	public DualGraph getDualGraph() {return dualGraph;}
	public HashMap<String,Integer> getZoneWeights() {return zoneWeights;}
	public ArrayList<DualGraph> getDualGraphHistory() {return dualGraphHistory;}
	public ArrayList<String[]> getSetMergeHistory() {return setMergeHistory;}
	public ArrayList<AbstractDiagram> getAbstractDiagramMergeHistory() {return abstractDiagramMergeHistory;}
	public ArrayList<String> getTypeMergeHistory() {return typeMergeHistory;}

	public void setZoneWeights(HashMap<String,Integer> inZoneWeights) {zoneWeights = inZoneWeights;}
	public void setAbstractDiagram(AbstractDiagram ad) {abstractDiagram = ad;}
	public void setDualGraph(DualGraph dg) {dualGraph = dg;}


	public Simplify(AbstractDiagram ad) {
		super();
		this.totalTime = 0.0;
		this.abstractDiagram = ad;
		fillWeights(DEFAULT_WEIGHT);
		dualGraph = formDualGraph(ad);
	}

	/**
	 * This reuses and so potentially changes the given dualGraph
	 * @param dg
	 */
	public Simplify(DualGraph dg) {
		super();
		this.totalTime = 0.0;
		this.dualGraph = dg;
		this.abstractDiagram = dg.findAbstractDiagram();
		fillWeights(DEFAULT_WEIGHT);
	}

	
	/**
	 * Form the dualGrapah adding edges between nodes with 1 contour difference and adding edges to ensure connected contours.
	 */
	private DualGraph formDualGraph(AbstractDiagram ad) {

		DualGraph dg = new DualGraph(ad);
		dg.connectDiscconnectedContours();
		
		// add weights
		for(Node n : dg.getNodes()) {
			String label = n.getLabel();
			int weight = zoneWeights.get(label);
			n.setScore(weight);
		}

		
		Node outsideNode = dg.firstNodeWithLabel("");
		ArrayList<Edge> edgesToOutside = outsideNode.connectingEdges();
		if(edgesToOutside.size() == 0) { // can't leave the outside disconnected
			
			String smallestLabel = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";
			Node smallestLabelNode = null;
			for(Node n : dg.getNodes()) {
				String label = n.getLabel();
				if(label.equals("")) { // don't form a loop
					continue; 
				}
				if(label.length() < smallestLabel.length()) {
					smallestLabel = label;
					smallestLabelNode = n;
				}
			}
			
			Edge e = new Edge (outsideNode,smallestLabelNode,smallestLabelNode.getLabel());
			dg.addEdge(e);

		}

		
		return dg;
	}
	
	
	/**
	 * merge sets until the dualGraph is planar.
	 */
	public void simplifyUntilPlanar() {
		
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dualGraph);
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
		boolean planar = planarityInspector.isPlanar();
		
		while(!planar) {
long startTime = System.currentTimeMillis();
			// keep a copy of the current dual in the history
			DualGraph clonedDualGraph = dualGraph.clone();
			dualGraphHistory.add(clonedDualGraph);
			
			AbstractDiagram ad = new AbstractDiagram(abstractDiagram);
			Graph<String, DefaultEdge> nonPlanarSubgraph = planarityInspector.getKuratowskiSubdivision();
			
			ArrayList<String> nonPlanarContours = findContoursInJGrpah(nonPlanarSubgraph);
			
			// find the set merge that removes most concurrency and apply it.
			String[] concurrentPair = findHighestConcurrencyContours(ad,nonPlanarContours);
			
if(outputDataFlag) {
	System.out.print("planarity,simplifyUntilPlanar|start abstractDiagram:|"+abstractDiagram);
}

			mergeSetsInAbstractDiagram(concurrentPair[0], concurrentPair[1]);
			dualGraph = formDualGraph(abstractDiagram);
if(outputDataFlag) {
	System.out.print("|end abstractDiagram:|"+abstractDiagram+"|sets merged:|"+concurrentPair[0]+","+concurrentPair[1]+"|number of sets:|"+abstractDiagram.getContours().size()+"|number of nodes:|"+dualGraph.getNodes().size()+"|number of edges:|"+dualGraph.getEdges().size()+"|zone weights:|"+zoneWeights+"|||concurrency count:|"+countConcurrency(getDualGraph())+"||");
}

			jGraph = buildJGraphT(dualGraph);
			planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
			planar = planarityInspector.isPlanar();
			
long endTime = System.currentTimeMillis();
double timeTaken = endTime;
timeTaken -= startTime;
timeTaken /= 1000;
totalTime += timeTaken;
if(outputDataFlag) {
	System.out.print("|time (s):|"+timeTaken);
	System.out.println();

}
		}

	}

	/**
	 * merge sets until the dual Graph is planar. Attempts to maintain layout.
	 */
	public void simplifyUntilNoConcurrency() {
		
		boolean concurrency = dualGraph.hasConcurrentEdges();

		while(concurrency) {
			reduceConcurrencyInDualGraph();
			concurrency = dualGraph.hasConcurrentEdges();
		}
	}

		


	/**
	 * One iteration of the set merging to reduce concurrency. Results in a new dualGraph and abstractDiagram.
	 * Attempts to keep the dualGraph layout
	 */
	public void reduceConcurrencyInDualGraph() {
		
long startTime = System.currentTimeMillis();

		
		DualGraph clonedDualGraph = dualGraph.clone();
		dualGraphHistory.add(clonedDualGraph);
		
		ArrayList<String> contours = abstractDiagram.getContours();
		// find the set merge that removes most concurrency and apply it.
		String[] concurrentPair = findHighestConcurrencyContours(abstractDiagram,contours);
		
if(outputDataFlag) {
	System.out.print("concurrency,mergeSetsInDualGraph|start abstractDiagram:|"+abstractDiagram);
}
		
		mergeSetsInDualGraph(concurrentPair[0],concurrentPair[1]);
		
if(outputDataFlag) {
	System.out.print("|end abstractDiagram:|"+abstractDiagram+"|sets merged:|"+concurrentPair[0]+","+concurrentPair[1]+"|number of sets:|"+abstractDiagram.getContours().size()+"|number of nodes:|"+dualGraph.getNodes().size()+"|number of edges:|"+dualGraph.getEdges().size()+"|zone weights:|"+zoneWeights+"|||concurrency count:|"+countConcurrency(getDualGraph()));
}

long endTime = System.currentTimeMillis();
double timeTaken = endTime;
timeTaken -= startTime;
timeTaken /= 1000;
totalTime+=timeTaken;
if(outputDataFlag) {
	System.out.print("|time (s):|"+timeTaken);
	System.out.println();

}



	}


	/**
	 * check if the dualGraph is planar.
	 * @return true if planar, false if not.
	 */
	public boolean isPlanar() {
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dualGraph);
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
		boolean planar = planarityInspector.isPlanar();
		return planar;
	}


	/**
	 * 
	 * @param jGraph
	 * @return all the contours in the mapped node labels
	 */
	public ArrayList<String> findContoursInJGrpah(Graph<String, DefaultEdge> jGraph) {

		ArrayList<String> contourList = new ArrayList<>();
		
		Set<String> vertices = jGraph.vertexSet();
		
		for (String v : vertices) {
			Node n = vertexNodeMap.get(v);
			ArrayList<String> labelList = AbstractDiagram.findContourList(n.getLabel());
			contourList.addAll(labelList);
		}
		Collections.sort(contourList);
		AbstractDiagram.removeDuplicatesFromSortedList(contourList);
		return contourList;
	}


	/**
	 * Find the pair of contours in the argument list, that when removed reduce concurrency by the greatest amount.
	 * 
	 * At present, this merges each possible pair and counts the concurrency.
	 * 
	 * @return pair of concurrent contours to merge
	 */
	private String[] findHighestConcurrencyContours(AbstractDiagram ad, ArrayList<String> contours) {

		String[] ret = new String[2];
		
		int minConcurrency = Integer.MAX_VALUE;
		
		ArrayList<String> testedContours = new ArrayList<>(contours.size());
		for(String c1 : contours) {
			testedContours.add(c1);
			for(String c2 : contours) {
				if(testedContours.contains(c2)) {
					continue; // stop the reverse test, so that if a b has been tested, dont test b a
				}
				
				mergeSets(ad,c1,c2);
				
				AbstractDiagram oldAbstractDiagram = abstractDiagram;
				HashMap<String,Integer> oldZoneWeights = zoneWeights;
				
				abstractDiagram = newAbstractDiagram;
				zoneWeights = newZoneWeights;
				
				DualGraph dg = formDualGraph(abstractDiagram);
				int concurrencyCount = countConcurrency(dg);
				
				abstractDiagram = oldAbstractDiagram;
				zoneWeights = oldZoneWeights;

				
				if(concurrencyCount < minConcurrency) {
					minConcurrency = concurrencyCount;
					ret[0] = c1;
					ret[1] = c2;
				}
			}
		}

		return ret;

	}


	/**
	 * 
	 * @param dg
	 * @return a count of the amount of concurrency, that is the number and size of concurrent edges.
	 */
	static public int countConcurrency(DualGraph dg) {

		int ret = 0;
		for(Edge e : dg.getEdges()) {
			String label = e.getLabel();
			int labelSize = label.length();
			ret += labelSize-1;
		}
		
		return ret;

	}


	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set. Does not modify this abstract description, returns the new abstract description.
	 * 
	 * Stores the merge in the merge history
	 * 
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 * @return an abstract diagram with set1 and set2 merged, with the merged set taking set1 label.
	 */
	public void mergeSetsInAbstractDiagram(String set1, String set2) {
		
		mergeSets(abstractDiagram,set1,set2);
		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		setMergeHistory.add(setPair);
		abstractDiagramMergeHistory.add(abstractDiagram);
		typeMergeHistory.add(PLANARITY_TYPE);
		
		abstractDiagram = newAbstractDiagram;
		zoneWeights = newZoneWeights;
	}

	
	AbstractDiagram newAbstractDiagram = null;
	HashMap<String,Integer> newZoneWeights = null;
	
	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set. Returns the new abstract diagram.
	 * 
	 * @param ad the abstract diagram to change
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 * @return an abstract diagram with set1 and set2 merged, with the merged set taking set1 label.
	 */
	public void mergeSets(AbstractDiagram ad, String set1, String set2) {
		
		ArrayList<String> newZones = new ArrayList<>();
		
		HashMap<String,Integer> newWeights = new HashMap<>();

		for(String z : ad.getZoneList()) {
			String zNew = z;
			if(z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,"");
			}
			if(!z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,set1);
				zNew = AbstractDiagram.orderZone(zNew); 
			}
			if(!newZones.contains(zNew)) {
				newZones.add(zNew);
				int oldWeight = zoneWeights.get(z);
				newWeights.put(zNew,oldWeight);
			} else {
				int oldWeight = zoneWeights.get(z);
				int newWeight = newWeights.get(zNew)+oldWeight;
				newWeights.put(zNew,newWeight);
			}
		}

		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		
		newAbstractDiagram = new AbstractDiagram(newZones);
		newZoneWeights = newWeights;
		
	}
	
	
	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set.
	 * 
	 * Chooses sets to merge based on dualGraph, merges them in the abstract diagram, then creates a new dualGraph.
	 * 
	 * Results in new dualGraph and abstractDiagram.
	 * 
	 * Stores the merge in the merge history
	 * 
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 */
	public void mergeSetsInDualGraph(String set1, String set2) {
		mergeSets(abstractDiagram, set1,set2);
		
		abstractDiagram = newAbstractDiagram;
		zoneWeights = newZoneWeights;
		
		DualGraph dgNew = formDualGraph(abstractDiagram);
		for(Node n : dgNew.getNodes()) {
			
			Node oldNode = dualGraph.firstNodeWithLabel(n.getLabel()); // no change in label
			if(oldNode == null) { // here we look for old nodes found by replacing the deleted set with the new one
				String oldLabel = n.getLabel();
				oldLabel = oldLabel.replace(set1,set2);
				oldLabel = AbstractDiagram.orderZone(oldLabel); 
				oldNode = dualGraph.firstNodeWithLabel(oldLabel);
			}
			if(oldNode == null) {  // here we look for old nodes found by adding the deleted set back
				String oldLabel = n.getLabel();
				oldLabel = oldLabel+set2;
				oldLabel = AbstractDiagram.orderZone(oldLabel); 
				oldNode = dualGraph.firstNodeWithLabel(oldLabel);
			}

			n.setX(oldNode.getX());
			n.setY(oldNode.getY());
		}
		
		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		setMergeHistory.add(setPair);
		abstractDiagramMergeHistory.add(abstractDiagram);
		typeMergeHistory.add(CONCURRENCY_TYPE);

		dualGraph = dgNew;
	
	}


	
	
	public static Graph<String, DefaultEdge> buildJGraphT(DualGraph dg) {
		Graph<String, DefaultEdge> jGraph = new SimpleGraph<>(DefaultEdge.class);

		nodeVertexMap = new HashMap<>(dg.getNodes().size());
		vertexNodeMap = new HashMap<>(dg.getNodes().size());
		
		for(int i = 0; i < dg.getNodes().size(); i++) {
			String nString = Integer.toString(i);
			jGraph.addVertex(nString);
			Node n = dg.getNodes().get(i);
			nodeVertexMap.put(n,nString);
			vertexNodeMap.put(nString,n);
		}
		
		for(Edge e : dg.getEdges()) {
			String v1 = nodeVertexMap.get(e.getFrom());
			String v2 = nodeVertexMap.get(e.getTo());
			jGraph.addEdge(v1, v2);
		}
		
	
		return jGraph;
		
	}
	
	
	/**
	 * 
	 * Make the node weights random values between min and max, inclusive.
	 * 
	 * @param min
	 * @param max
	 */
	public void randomizeWeights(int min, int max) {
		
		Random r = new Random(System.currentTimeMillis());
		for(String z : abstractDiagram.getZoneList()) {
			int weight = r.nextInt(min,max+1);
			zoneWeights.put(z,weight);
		}
		
	}


	/**
	 * 
	 * Make the node weights the value.
	 * 
	 * @param value
	 */
	public void fillWeights(int value) {
		
		for(String z : abstractDiagram.getZoneList()) {
			zoneWeights.put(z,value);
		}
		
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
	public static DualGraph originalLayout(AbstractDiagram ad, String director, boolean outputFlag) {
		
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

if(outputFlag) {		
int concurrency = Simplify.countConcurrency(dg);
int startContourCount = ad.getContours().size();
int endContourCount = dg.findAbstractDiagram().getContours().size();
int extraContourCount = endContourCount - startContourCount;
System.out.println("ORIGINAL LAYOUT|"+ad+"|concurrency:|"+concurrency+"|duplicate curves:|"+extraContourCount+"|director:|"+director);
}
//		dw.dispose();
		
		return dg;
	}





}
