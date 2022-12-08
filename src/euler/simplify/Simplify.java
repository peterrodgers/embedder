package euler.simplify;

import java.awt.event.KeyEvent;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import euler.AbstractDiagram;
import euler.DualGraph;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;
import pjr.graph.Edge;
import pjr.graph.Node;


/**
 * 
 * Code based on the simplification ideas from Dagstuhl Seminar 22462.
 * 
 * @author Peter Rodgers
 */

public class Simplify {
	
	private static HashMap<Node,String> nodeVertexMap = null;
	private static HashMap<String,Node> vertexNodeMap = null;
	
	AbstractDiagram abstractDiagram = null;
	DualGraph dualGraph = null;

	/** Earliest first. Merged pairs, first of the pair is the merged label */
	ArrayList<String[]> setMergeHistory = new ArrayList<>();
	/** Earliest first */
	ArrayList<AbstractDiagram> abstractDiagramMergeHistory = new ArrayList<>();
	/** Earliest first */
	ArrayList<String> typeMergeHistory = new ArrayList<>();
	
	public static void main(String[] args) {

		
		//AbstractDiagram ad = new AbstractDiagram("0 a b c d e f abcdef abe cde adbe ade abc abce");
		//AbstractDiagram ad = new AbstractDiagram("0 a c bc bd acd");
		//AbstractDiagram ad = AbstractDiagram.VennFactory(5);
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(6);
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(7);
		
		//AbstractDiagram ad = new AbstractDiagram("0 ae be bce bde abcde");
			
		
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(4, true, 0.2);
		AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(5, true, 0.2);
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(6, true, 0.2);
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(7, true, 0.2);
		
		//AbstractDiagram ad = new AbstractDiagram("0 a b c abc abd af");
		
		Simplify simplify = new Simplify(ad);
		
		simplify.simplifyUntilPlanar();
		
		//simplify.simplifyUntilNoConcurrency();


		DualGraphWindow dw = new DualGraphWindow(simplify.getDualGraph());
		dw.getDiagramPanel().setShowGraph(true);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(false);
		dw.getDiagramPanel().setShowContourLabel(true);
		dw.getDiagramPanel().setShowTriangulation(false);
		dw.getDiagramPanel().setJiggleLabels(false);
		dw.getDiagramPanel().setForceNoRedraw(true);
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
	 	ddp.layout();
		dw.getDiagramPanel().fitGraphInPanel();
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());

		
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

	}

	
	public AbstractDiagram getAbstractDiagram() {return abstractDiagram;}
	public DualGraph getDualGraph() {return dualGraph;}
	public ArrayList<String[]> getSetMergeHistory() {return setMergeHistory;}
	public ArrayList<AbstractDiagram> getAbstractDiagramMergeHistory() {return abstractDiagramMergeHistory;}
	public ArrayList<String> getTypeMergeHistory() {return typeMergeHistory;}


	public Simplify(AbstractDiagram ad) {
		super();
		this.abstractDiagram = ad;
		dualGraph = formDualGraph(ad);
	}

	/**
	 * This reuses and so potentially changes the given dualGraph
	 * @param ad
	 */
	public Simplify(DualGraph dg) {
		super();
		this.dualGraph = dg;
		this.abstractDiagram = dg.findAbstractDiagram();
	}

	
	/**
	 * Form the dualGrapah adding edges between nodes with 1 contour difference and adding edges to ensure connected contours.
	 */
	private DualGraph formDualGraph(AbstractDiagram ad) {

		DualGraph dg = new DualGraph(ad);
		dg.connectDiscconnectedContours();
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
			AbstractDiagram ad = new AbstractDiagram(abstractDiagram);
			Graph<String, DefaultEdge> nonPlanarSubgraph = planarityInspector.getKuratowskiSubdivision();
			
			ArrayList<String> nonPlanarContours = findContoursInJGrpah(nonPlanarSubgraph);
			
			// find the set merge that removes most concurrency and apply it.
			String[] concurrentPair = findHighestConcurrencyContours(ad,nonPlanarContours);

			abstractDiagram = mergeSetsInAbstractDiagram(concurrentPair[0], concurrentPair[1]);
			dualGraph = formDualGraph(abstractDiagram);
			
			jGraph = buildJGraphT(dualGraph);
			planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
			planar = planarityInspector.isPlanar();
			
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
		
		ArrayList<String> contours = abstractDiagram.getContours();
		// find the set merge that removes most concurrency and apply it.
		String[] concurrentPair = findHighestConcurrencyContours(abstractDiagram,contours);
		
		mergeSetsInDualGraph(concurrentPair[0],concurrentPair[1]);

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
	private ArrayList<String> findContoursInJGrpah(Graph<String, DefaultEdge> jGraph) {

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
				
				AbstractDiagram mergeAD = mergeSets(ad,c1,c2);
				DualGraph dg = formDualGraph(mergeAD);
				int concurrencyCount = countConcurrency(dg);
				
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
	public AbstractDiagram mergeSetsInAbstractDiagram(String set1, String set2) {
		
		AbstractDiagram ret = mergeSets(abstractDiagram,set1,set2);
		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		setMergeHistory.add(setPair);
		abstractDiagramMergeHistory.add(abstractDiagram);
		typeMergeHistory.add("mergeSetsInAbstractDiagram, probably planarity");
		
		return ret;
	}

	
	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set. Returns the new abstract diagram.
	 * 
	 * @param ad the abstract diagram to change
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 * @return an abstract diagram with set1 and set2 merged, with the merged set taking set1 label.
	 */
	public static AbstractDiagram mergeSets(AbstractDiagram ad, String set1, String set2) {
		
		ArrayList<String> retZones = new ArrayList<>();
		
		for(String z : ad.getZoneList()) {
			String zNew = z;
			if(z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,"");
			}
			if(!z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,set1);
				zNew = AbstractDiagram.orderZone(zNew); 
			}
			if(!retZones.contains(zNew)) {
				retZones.add(zNew);
			}
		}

		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		
		AbstractDiagram ret = new AbstractDiagram(retZones);
		return ret;
	
	}
	
	
	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set.
	 * 
	 * Results in new dualGraph and abstractDiagram.
	 * 
	 * Stores the merge in the merge history
	 * 
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 */
	public void mergeSetsInDualGraph(String set1, String set2) {

		AbstractDiagram adNew = mergeSets(abstractDiagram, set1,set2);
		
		DualGraph dgNew = formDualGraph(adNew);
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
		typeMergeHistory.add("mergeSetsInDualGraph, probably concurrency");

		dualGraph = dgNew;
		abstractDiagram = adNew;
		
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
	
	


}
