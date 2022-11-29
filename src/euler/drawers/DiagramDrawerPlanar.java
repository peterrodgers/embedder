package euler.drawers;

import euler.*;
import euler.display.DualGraphWindow;

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

/*		DualGraph dg = new DualGraph(new AbstractDiagram("0 ab ac"));
		Node empty = dg.firstNodeWithLabel("");
		Node ab = dg.firstNodeWithLabel("ab");
		Node ac = dg.firstNodeWithLabel("ac");
		dg.addEdge(new Edge(empty,ab));
		dg.addEdge(new Edge(empty,ac));
		dg.addEdge(new Edge(ab,ac));
*/	 
		
		// This has three faces, large outer face splits along  between 0 and a 
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ab ac"));
/*
		// This has two faces, both split along ab between cd and abcd 
		DualGraph dg = new DualGraph(new AbstractDiagram("0 cd cdef abcdef abcd abd a"));
		dg.removeEdges(new ArrayList<Edge>(dg.getEdges()));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("cd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("cd"),dg.firstNodeWithLabel("cdef")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("cdef"),dg.firstNodeWithLabel("abcdef")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abcdef"),dg.firstNodeWithLabel("abcd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abcd"),dg.firstNodeWithLabel("abd")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abd"),dg.firstNodeWithLabel("a")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("")));
*/
		// This has two faces, both split along def between ae and adf and split along d between a and ad
/*
		DualGraph dg = new DualGraph(new AbstractDiagram("0 a ae abce abcdf adf ad d"));
		dg.removeEdges(new ArrayList<Edge>(dg.getEdges()));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("a")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ae")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ae"),dg.firstNodeWithLabel("abce")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abce"),dg.firstNodeWithLabel("abcdf")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("abcdf"),dg.firstNodeWithLabel("adf")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("adf"),dg.firstNodeWithLabel("ad")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ad"),dg.firstNodeWithLabel("d")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("d"),dg.firstNodeWithLabel("")));
*/

		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ac bc"));
		//dg.addEdge(new Edge(dg.firstNodeWithLabel("ac"),dg.firstNodeWithLabel("bc")));
/*		
		// The minimal concurrency problem
		DualGraph dg = new DualGraph(new AbstractDiagram("0 a ab bc c"));
		dg.removeEdges(new ArrayList<Edge>(dg.getEdges()));
		dg.addEdge(new Edge(dg.firstNodeWithLabel(""),dg.firstNodeWithLabel("a")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("a"),dg.firstNodeWithLabel("ab")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("ab"),dg.firstNodeWithLabel("bc")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("bc"),dg.firstNodeWithLabel("c")));
		dg.addEdge(new Edge(dg.firstNodeWithLabel("c"),dg.firstNodeWithLabel("")));
*/	
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ab ac bc"));

		
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a ab abc abcd abcde bcde bcd bc b"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a ab abc ac ad"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b d ab bd cd bcd"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ab ac bc"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b d f ab af cd de df cde cdf def cdef"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab ac abc"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab ac bc"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d e ab ac ae bd be ce abe ace"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 b c d e f ab ad bc bf cd de df abc abf acd adf bcd bdf abcd abcf abdf acdf abcdf"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d e f g ae af bd bf cf cg df ef fg aef bdf cfg"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 b c d e f af bc bf cd ce cf abf acf bcf abcf"));
		
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 abc d e b c a ace bce ac ad	abce ce	bc ae be"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 d bd bcd abcd abd ab acd ac"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 c ab ac bc abc"));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(1));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(2));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(4));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(5));
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(6));
		//DualGraph dg = new DualGraph(AbstractDiagram.randomDiagramFactory(4));
		//DualGraph dg = new DualGraph(AbstractDiagram.randomDiagramFactory(5));
		//DualGraph dg = new DualGraph(AbstractDiagram.randomDiagramFactory(6));
		
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d e f ab ae af bf ce cf de df ef abf aef cdf cef def cdef"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 d abd e b c a bde de ac ad ab ce bd"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 d abd e b c a bde de ac ad ab ce bd"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 c ab ad bc bd cd abc bcd abcd"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 abc def ghi adg beh cfi"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ac bc"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab bc abc bcd abcd bcde bce bf f"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d f ab ac ad af"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a ab abc bc bcd abcd acd ad d"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ac ab ad bc abc abd abcd"));
		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ac ab bc abc"));
		//DualGraph dg = new DualGraph(new AbstractDiagram("0 abcd defg aehi bfhk cgik")); //K5,5 example needing non-simple contours
		
		//dg.randomizeNodePoints(new Point(50,50),400,400);
		
		//System.out.println("empty node index " + emptyNodeIndex);
		// uncomment this to load a graph from a file
		// dg.loadAll(new File("C:\\code\\embedder\\trunk\\data\\buggyDiagrams\\holeInHole.txt"));
		//dg.loadAll(new File("c:\\code\\embedder\\trunk\\default.txt"));
		//dg.loadAll(new File("c:\\code\\embedder\\trunk\\contourRoutingBug2.txt"));
		//dg.loadAll(new File("c:\\code\\embedder\\trunk\\default.txt"));

		DualGraphWindow dw = new DualGraphWindow(dg);
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
//		PlanarForceLayout pfl = new PlanarForceLayout(dw.getDiagramPanel());
//		pfl.setAnimateFlag(false);
//		pfl.setIterations(50);//
//		pfl.drawGraph();
		dw.getDiagramPanel().fitGraphInPanel();
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());
		//dg.checkInductivePiercingDiagram();
		
				

	}
	

	

	/** Draws the graph.
	 * 1. check the planarity of the graph
	 * 2. if the graph is planar, find the planar embedding of the graph
	 *	else print out error message
	 */
	public void layout() {
		
		DiagramPanel dp = getDiagramPanel();
			
		DualGraph dg = dp.getDualGraph();
		if(dg!=null) {
			boolean planar = isPlanar(dg);
			if(!planar) {
				System.out.println("not planar");
			} else {
				boolean drawn = planarLayout(dg);
				getDiagramPanel().setDualGraph(dg);
				if(dp != null && drawn) {
		//			dp.fitGraphInPanel();
					dp.update(dp.getGraphics());				
				} else {
					if(!drawn) {
						System.out.println("no layout");
					} else {
						System.out.println("dp == null");
					}
				}
			}
		}
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
		
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dg);
		
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
		Graph<String, DefaultEdge> jGraph = buildJGraphT(dg);
		boolean ret = isPlanar(jGraph);
		return ret;
	}

	
	private static boolean isPlanar(Graph<String, DefaultEdge> jGraph) {
		BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(jGraph);
		boolean ret = planarityInspector.isPlanar();
		return ret;
	}

	private static HashMap<Node,String> nodeVertexMap;
	private static HashMap<String,Node> vertexNodeMap;
	
	private static Graph<String, DefaultEdge> buildJGraphT(DualGraph dg) {
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

