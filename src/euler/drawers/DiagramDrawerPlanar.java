package euler.drawers;

import euler.*;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.awt.event.*;
import java.awt.geom.*;
import pjr.graph.*;
import pjr.graph.drawers.*;


/**
 * This class tries to draw an graph without edge crossing.
 * Firstly the planarity of the graph is checked to make sure that 
 * there is a planar embedding for the graph. If the graph pass the 
 * planarity test, an planar embedding will be provided.
 *
 */
public class DiagramDrawerPlanar extends DiagramDrawer {


	/** Trivial constructor. */
	public DiagramDrawerPlanar(DiagramPanel dp) {
		super(KeyEvent.VK_P, "Planar");
		this.setDiagramPanel(dp);
	}
	/** Trivial constructor. */
	public DiagramDrawerPlanar(int key, String s, DiagramPanel dp) {
		super(key, s);
		this.setDiagramPanel(dp);
	}
	/** Trivial constructor. */
	public DiagramDrawerPlanar(int key, String s, int mnemonic, DiagramPanel dp) {
		super(key, s, mnemonic);
		this.setDiagramPanel(dp);
	}
	


	/** Draws the graph.
	 * 1. check the planarity of the graph
	 * 2. if the graph is planar, find the planar embedding of the graph
	 *	else print out error message
	 */
	public void layout() {
		
	 DiagramPanel dp = getDiagramPanel();
		
		DualGraph dg = dp.getDualGraph();
//		dg.printAll();
		if(dg!=null){
			boolean planar = planarLayout(dg);
			getDiagramPanel().setDualGraph(dg);
			if(dp != null && planar) {
				dp.fitGraphInPanel();
				dp.update(dp.getGraphics());				
			} 
			else{
				System.out.println("dp == null or not planar" );
			}
		}		
	}

	
	static int left = 20;
	static int top = 20;
	static int right = 200;
	static int bottom = 200;


	
	public static boolean planarLayout(DualGraph dg) {
		
		if(dg == null) {
			return false;
		}
		
		ArrayList<Node> nodes = dg.getNodes();
		ArrayList<Edge> edges = dg.getEdges();
		
		if(nodes.size() == 0) {
			return true;
		}
		
		if(nodes.size() == 1) {
			nodes.get(0).setX(left);
			nodes.get(0).setY(top);
		}
		
		if(nodes.size() == 2) {
			nodes.get(0).setX(left);
			nodes.get(0).setY(top);
			nodes.get(1).setX(right);
			nodes.get(1).setY(top);
			return true;
		}
		
		if(nodes.size() == 3) {
			nodes.get(0).setX(left);
			nodes.get(0).setY(top);
			nodes.get(1).setX(right);
			nodes.get(1).setY(top);
			nodes.get(2).setX(right);
			nodes.get(2).setY(bottom);
			return true;
		}
		
		
		if(!isPlanar(dg)) {
			return false;
		}
		
		layoutComplexGraph(dg);
		
		return true;
	}
	
	
	private static void layoutComplexGraph(DualGraph dg) {
		
		
		ArrayList<Node> nodes = dg.getNodes();
		ArrayList<Edge> edges = dg.getEdges();
		

		Node mostConnectedN = null;
		int maxDegree = -1;
		for(Node n : nodes) {
			if(n.degree() > maxDegree) {
				maxDegree = n.degree();
				mostConnectedN = n;
			}
		}
		
		Node secondMostConnectedN = null;
		maxDegree = -1;
		for(Node n : nodes) {
			if(n == mostConnectedN) {
				continue;
			}
			if(n.degree() > maxDegree) {
				maxDegree = n.degree();
				secondMostConnectedN = n;
			}
		}
		
		Node thirdMostConnectedN = null;
		maxDegree = -1;
		for(Node n : nodes) {
			if(n == mostConnectedN || n == secondMostConnectedN) {
				continue;
			}
			if(n.degree() > maxDegree) {
				maxDegree = n.degree();
				thirdMostConnectedN = n;
			}
		}
		
		mostConnectedN.setX(left);
		mostConnectedN.setY(top);
		secondMostConnectedN.setX(right);
		secondMostConnectedN.setY(top);
		thirdMostConnectedN.setX(right);
		thirdMostConnectedN.setY(bottom);

		
	}
		
		
/*		
		ArrayList<Node> nodes = dg.getNodes();
		
		if(nodes.size()== 0){
			return true;
		}
		
		boolean planar = true;
	
		// The node buffer. This holds copies of node locations
		DrawCoordCollection nodeBuffer = new DrawCoordCollection();
		
		int emptyNodeIndex = 0;
		for(int j = 0 ; j < nodes.size(); j++){
			if(nodes.get(j).getLabel().compareTo("")==0||nodes.get(j).getLabel().compareTo("0")==0)
				emptyNodeIndex = j;
				//System.out.println("empty node index " + emptyNodeIndex);			
		}

		nodeBuffer.setUpNodes(nodes);		
		double coor[] = dg.getOGDFNodesCoor();
		int edgesIndex[] = dg.getOGDFEdgesIndex();
		double [] newCoor = euler.drawers.OGDFPlanar.planarEmbedding(coor, edgesIndex, nodes.size(), dg.getEdges().size(),emptyNodeIndex);
		
		if( newCoor!= null){
			planar = true;

			ArrayList<Point2D.Double> newCentres = new ArrayList<Point2D.Double>();
			int idx1 = 0;
			
			for(int i = 0; i <nodes.size(); i ++){
				double x = newCoor[idx1];
				idx1++;
				double y = newCoor[idx1];
				idx1++;				
				Point2D.Double point = new Point2D.Double(x, y);
				newCentres.add(point);				
			}		
			embed(newCentres,nodeBuffer);
			nodeBuffer.switchOldCentresToNode();
		} else {
			planar = false;
		}
		return true;
	}
*/		
	
	
	
	
	public static boolean isPlanar(DualGraph dg) {
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dg);
		boolean ret = isPlanar(jGraph);
		return ret;
	}

	
	private static boolean isPlanar(Graph<String, DefaultEdge> jGraph) {
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
		boolean ret = planarityInspector.isPlanar();
		return ret;
	}

/*	
	public static boolean isPlanar(DualGraph dg) {
		
		org.jgrapht.Graph<String, DefaultEdge> jGraph = buildJGraphT(dg);
		
		ArrayList<Node> nodes = dg.getNodes();
		if(dg != null && nodes.size()>0){
			int emptyNodeIndex = 0;			
			for(int j = 0 ; j < nodes.size(); j++){
				if(nodes.get(j).getLabel().compareTo("")==0
						||nodes.get(j).getLabel().compareTo("0")==0)
				emptyNodeIndex = j;
				//System.out.println("empty node index " + emptyNodeIndex);
			}		
			double coor[] = dg.getOGDFNodesCoor();
			int edgesIndex[] = dg.getOGDFEdgesIndex();
//			double [] newCoor = euler.drawers.OGDFPlanar.planarEmbedding(coor, edgesIndex, nodes.size(), dg.getEdges().size(),emptyNodeIndex);
			
			if( newCoor!= null){
				return true;
			}
			else{
					return false;
			}
		}
		else{
			System.out.println("dg == null");
			return false;
		}
	}
*/	
	
	private static Graph<String, DefaultEdge> buildJGraphT(DualGraph dg) {
		Graph<String, DefaultEdge> jGraph = new SimpleGraph<>(DefaultEdge.class);
		
		HashMap<Node,Integer> nodeIndexMap = new HashMap<>(dg.getNodes().size());
		
		for(int i = 0; i < dg.getNodes().size(); i++) {
			String nString = Integer.toString(i);
			jGraph.addVertex(nString);
			Node n = dg.getNodes().get(i);
			nodeIndexMap.put(n,i);
		}
		
		for(Edge e : dg.getEdges()) {
			int n1Int = nodeIndexMap.get(e.getFrom());
			int n2Int = nodeIndexMap.get(e.getTo());
			String n1String = Integer.toString(n1Int);
			String n2String = Integer.toString(n2Int);
			jGraph.addEdge(n1String, n2String);
		}
		
System.out.println(jGraph.toString());

		
		return jGraph;
		
	}
	
	
	
	
	
	/**
	 * Embeds the graph without edge crossing
	 **/	
	public static void embed( ArrayList<Point2D.Double> centres, DrawCoordCollection nodeBuffer) {
		
		if(centres.size()!= nodeBuffer.getBufferedNodes().size()){
			System.out.println("error, fail to find new centres for nodes");
		}
		for(int i = 0 ; i <nodeBuffer.getBufferedNodes().size(); i++){
			
			DrawCoord nb = nodeBuffer.getBufferedNodes().get(i);
			Point2D.Double point = centres.get(i);
			nb.setNewCentre(point);		
		}

		nodeBuffer.switchNewCentresToOld();
	
	}	
		 
	
}

