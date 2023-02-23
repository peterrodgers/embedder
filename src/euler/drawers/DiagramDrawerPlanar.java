package euler.drawers;

import euler.*;
import euler.display.DualGraphWindow;
import euler.simplify.GenerateJson;
import euler.simplify.Simplify;

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

	private static int minX = 20;
	private static int maxX = 220;

	private static int minY = 20;
	private static int maxY = 220;

	private static long timeOutMillis = 2000;
	
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
	

	public static void main(String[] args) {

		
		DualGraph dg = new DualGraph();

		// create nodes and add them to the graph
		Node n1 = new Node("");
		Node n2 = new Node("a");
		Node n3 = new Node("b");
		Node n4 = new Node("ab");
		dg.addNode(n1);
		dg.addNode(n2);
		dg.addNode(n3);
		dg.addNode(n4);
		
		// create edges between nodes and add them to the graph
		Edge e1 = new Edge(n1,n2,"a");
		Edge e2 = new Edge(n1,n3,"b");
		Edge e3 = new Edge(n2,n4,"b");
		Edge e4 = new Edge(n3,n4,"a");
		dg.addEdge(e1);
		dg.addEdge(e2);
		dg.addEdge(e3);
		dg.addEdge(e4);

		// draw a graph with a planar layout and then apply force directed prettification, finally fit in a sensible rectangle
		boolean drawn = DiagramDrawerPlanar.layoutGraph(dg);
		if(!drawn) {
			// exit if the planar layout fails. The current planar layout is not always successful. 
			System.out.println("Cannot generate a planar embedding");
			System.exit(0);
		}
		PlanarForceLayout pfl = new PlanarForceLayout(dg);
		pfl.drawGraph();
		dg.fitInRectangle(100,100,400,400);

		// json output
		Simplify simplify = new Simplify(dg);
		GenerateJson gs = new GenerateJson(simplify);
		System.out.println(gs.jsonOutput());

		// show the graph in a window
		DualGraphWindow dw = new DualGraphWindow(dg);
		dw.getDiagramPanel().setShowGraph(true);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);


	}
	

	

	public void layout() {
		DiagramPanel dp = getDiagramPanel();
		DualGraph dg = dp.getDualGraph();
		boolean drawn = layoutGraph(dg);
		if(drawn) {
			dp.fitGraphInPanel();
			dp.update(dp.getGraphics());				
		}
	}
	
	
	/** Draws the graph.
	 * 1. check the planarity of the graph
	 * 2. if the graph is planar, find the planar embedding of the graph
	 *	else print out error message
	 */
	public static boolean layoutGraph(DualGraph dg) {

		boolean drawn = false;
		if(dg!=null) {
			boolean planar = isPlanar(dg);
			if(!planar) {
				System.out.println("not planar");
			} else {
				drawn = planarLayout(dg);
				if(!drawn) {
					System.out.println("no layout");
				}
			}
		}
		return drawn;
	}

	
	public static boolean planarLayout(DualGraph dg) {
		
		if(dg == null) {
			return false;
		}
		
		ArrayList<Node> nodes = dg.getNodes();
		
		if(nodes.size() == 0) {
			return true;
		}
		
		if(nodes.size() == 1) {
			nodes.get(0).setX(minX);
			nodes.get(0).setY(minY);
		}
		
		if(nodes.size() == 2) {
			nodes.get(0).setX(minX);
			nodes.get(0).setY(minY);
			nodes.get(1).setX(minX);
			nodes.get(1).setY(maxY);
			return true;
		}
		
		if(nodes.size() == 3) {
			nodes.get(0).setX(minX);
			nodes.get(0).setY(minY);
			nodes.get(1).setX(minX);
			nodes.get(1).setY(maxY);
			nodes.get(1).setX(maxX);
			nodes.get(1).setY(maxY);
			return true;
		}
		
		boolean planar = layoutComplexGraph(dg);
		
		if(!planar) {
			return false;
		}
		

		
		return true;
	}
	


/*
	private static boolean layoutComplexGraph(DualGraph dg) {
		
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dg);
		
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);

		if(!planarityInspector.isPlanar()) {
			return false;
		}
		
		ArrayList<Node> nodes = dg.getNodes();
		
		int minX = 20;
		int maxX = 220;

		int minY = 20;
		int maxY = 220;

		Node outsideNode = dg.firstNodeWithLabel("");
		String outsideVertex = nodeVertexMap.get(outsideNode);

		// find a face including the outside node
		Embedding<String, DefaultEdge> embedding = planarityInspector.getEmbedding();
		ArrayList<Node> face = new ArrayList<>(nodes.size());	
		String currentVertex = outsideVertex;
		while(!currentVertex.equals(outsideVertex) || face.size() == 0) {
			List<DefaultEdge> edgeList = embedding.getEdgesAround(currentVertex);
			
			DefaultEdge e = edgeList.get(0);
			String source = jGraph.getEdgeSource(e);
			String target = jGraph.getEdgeTarget(e);
			String otherEndVertex = source;
			if(source.equals(currentVertex)) {
				otherEndVertex = target;
			}
			currentVertex = otherEndVertex;
			Node currentNode = vertexNodeMap.get(currentVertex);
			face.add(currentNode);
System.out.println(face);
		}
		
System.out.println(face);

		return true;
	}
		
*/


	/** 
	 * The graph to draw must have more than 3 nodes
	 * 
	 * @param dg dualGraph to draw
	 * @param jGraph JGraphT formed from the dg by using buildJGraphT
	 * 
	 * @return true if planar, false if not.
	 */
	private static boolean layoutComplexGraph(DualGraph dg) {
		
		Graph<String, DefaultEdge> jGraph = Simplify.buildJGraphT(dg);
		
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);

		if(!planarityInspector.isPlanar()) {
			return false;
		}
		
		
		ArrayList<Node> nodes = dg.getNodes();
		
		Node n1 = dg.firstNodeWithLabel(""); // start with empty node on outside
		
		Node n2 = null;
		int maxDegree = -1;
		for(Node n : nodes) {
			if(n == n1) {
				continue;
			}
			if(n.degree() > maxDegree) {
				maxDegree = n.degree();
				n2 = n;
			}
		}
		
		Node n3 = null;
		maxDegree = -1;
		for(Node n : nodes) {
			if(n == n1 || n == n2) {
				continue;
			}
			if(n.degree() > maxDegree) {
				maxDegree = n.degree();
				n3 = n;
			}
		}
				
		n1.setX((maxX-minX)/2);
		n1.setY(minY);

		n2.setX(minX);
		n2.setY(maxY);

		n3.setX(maxX);
		n3.setY(maxY);

		int crossingCount = -1;
		
		int iteration = 1;
		int firstSeed = 7;

		long startTime = System.currentTimeMillis();
		
		while(crossingCount != 0) {

			Random r = new Random(firstSeed*iteration);
			
			for(Node n : nodes) {
				if(n == n1 || n == n2 || n == n3) {
					continue;
				}
				int x = r.nextInt(maxX-minX)+minX;
				int y = r.nextInt(maxY-minY)+minY;
				n.setX(x);
				n.setY(y);
			}
			
			ArrayList<Edge[]> crossings = dg.findEdgeCrossings();
			
			crossingCount = crossings.size();
//System.out.println(iteration+" "+crossingCount);
			iteration++;
			
			if(System.currentTimeMillis() > startTime+timeOutMillis) {
				System.out.println("planar layout Timeout after: "+timeOutMillis+" milliseconds");
				return false;
			}
		}


		return true;
	}
	
	
	public static boolean isPlanar(DualGraph dg) {
		Graph<String, DefaultEdge> jGraph = Simplify.buildJGraphT(dg);
		boolean ret = isPlanar(jGraph);
		return ret;
	}

	
	private static boolean isPlanar(Graph<String, DefaultEdge> jGraph) {
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
		boolean ret = planarityInspector.isPlanar();
		return ret;
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

