package euler.inductive;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import euler.*;
import euler.display.*;
import euler.drawers.*;
import euler.inductive.comparators.*;
import euler.polygon.*;

import pjr.graph.*;
import pjr.graph.display.*;
import pjr.graph.triangulator.*;


public class HybridGraph extends DualGraph {

	public static final NodeType EULER_NODE = new NodeType("EulerNode",30,30,"Ellipse",false,Color.BLACK,Color.BLACK);
	public static NodeType DUAL_NODE = new NodeType("DualNode",30,30,"Ellipse",false,Color.RED,Color.RED);
	public static NodeType NEW_NODE = new NodeType("NewNode",30,30,"Ellipse",false,Color.GREEN,Color.GREEN);
	
	/** For routing Euler graph edges */
	public static final NodeType POLY_EULER_NODE = new NodeType("polyEulerNode",1,1,"Rectangle",true);
	/** For routing red edges */
	public static final NodeType POLY_DUAL_NODE = new NodeType("polyDualNode",1,1,"Rectangle",true);
	/** For routing green edges */
	public static final NodeType POLY_NEW_NODE = new NodeType("polyNewNode",1,1,"Rectangle",true);


	public static final EdgeType EULER_EDGE = new EdgeType("EulerEdge",false,Color.BLACK);
	public static final EdgeType DUAL_EDGE = new EdgeType("DualEdge",false,Color.RED);
	public static final EdgeType NEW_EDGE = new EdgeType("NewEdge",false,Color.GREEN);
	
	public static final EdgeType SPLIT_EULER_EDGE = new EdgeType("SplitEulerEdge",false,Color.BLACK);
	
	public static final String EMPTY_ZONE_LABEL = "O";

	public static final int EMPTY_DIAGRAM_POLY_POINTS = 20;
	public static final int SCALE = 1;
	public static final int EMPTY_DIAGRAM_DIAMETER = 120*SCALE;
	public static final int SURROUNDING_POLY_POINTS = 40;
	public static final int SURROUNDING_POLY_BORDER = 10*SCALE;
	public static final int CENTREX = 350;
	public static final int CENTREY = 350;
	public static String COMPSTRINGCTL = "Concurrency TriplePoints PathLength";
	public static String COMPSTRINGTCL = "TriplePoints Concurrency PathLength";
	public static String COMPSTRINGLTC = "PathLength TriplePoints Concurrency";
	
	
	public static boolean errorOutput = false;

	/** The version of the graph at large scale */
	protected DualGraph largeGraph = new DualGraph();
	/** The version of the graph with bend points replaced by routing edges to allow face finding and triangulation */
	protected DualGraph multiGraph;
	/** To match the multiGraph to this graph */
	protected HashMap<Node,Node> multiNodeMap;
	/** To match the multiGraph to this graph */
	protected HashMap<Edge,Edge> multiEdgeMap;
	/** Indicates successful creation or not */
	protected boolean objectCreatedSuccessfully = true;
	/** Changes the priority order of the comparators */
	protected String compString = HybridGraph.COMPSTRINGTCL;
	/** For placing a progress message on the display */
	protected DiagramPanel diagramPanel = null;
	
	public static void main(String[] args) {
	
		HybridGraph hg;
		DualGraph eg;
		ArrayList<Edge> edgePath;
		ArrayList<String> splitZones;
		ArrayList<String> containedZones;

		hg = new HybridGraph(new DualGraph());
		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		splitZones.add("O");
		edgePath = hg.findSimplePath(splitZones, containedZones, 0, false);
		eg = hg.eulerGraphWithEdgePath("a", edgePath);
//System.out.println(edgePath);
		hg = new HybridGraph(eg);
//		new InductiveWindow("Hybrid Graph a",hg.clone());
		
		
		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		splitZones.add("a");
		splitZones.add("O");
		edgePath = hg.findSimplePath(splitZones, containedZones, 0, false);
		eg = hg.eulerGraphWithEdgePath("b", edgePath);
		hg = new HybridGraph(eg);
		new InductiveWindow("Hybrid Graph ab",hg.clone());
//hg.drawWithSpringEmbedder();
//new InductiveWindow("Spring Embedded",hg);
//new InductiveWindow("Hybrid Graph ab",hg);
//hg.fitInRectangle(0, 0, 500, 500);
/*		

		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		containedZones.add("b");
		splitZones.add("a");
		splitZones.add("ab");
		edgePath = hg.findSimplePath(splitZones, containedZones,0);
		eg = hg.eulerGraphWithEdgePath("c", edgePath);
		hg = new HybridGraph(eg);
		new InductiveWindow("Hybrid Graph abc",hg.clone());
*/
	}	
	
	/** Creates an empty graph */
	public HybridGraph() {
		super();
		errorOutput = false;
		buildFromEulerGraph(new DualGraph());
	}
	

	/** Build the hybrid graph from the dual graph */
	public HybridGraph(DualGraph eulerGraph) {
		super();
		errorOutput = false;
		try {
			buildFromEulerGraph(eulerGraph);
		} catch (Exception e) {
			e.printStackTrace();
			objectCreatedSuccessfully = false;
			return;
		}
		objectCreatedSuccessfully = true;
		
	}

	/** Build the hybrid graph from the dual graph */
	public HybridGraph(DualGraph eulerGraph, DiagramPanel diagramPanel) {
		super();
		this.diagramPanel = diagramPanel;
		errorOutput = false;
		try {
			buildFromEulerGraph(eulerGraph);
		} catch (Exception e) {
			e.printStackTrace();
			objectCreatedSuccessfully = false;
			return;
		}
		objectCreatedSuccessfully = true;
		
	}


	public boolean getObjectCreatedSuccessfully() {return objectCreatedSuccessfully;}
	public String getCompString() {return compString;}
	public DualGraph getLargeGraph() {return largeGraph;}

	public void setCompString(String compString) {this.compString = compString;}
	public void setLargeGraph(DualGraph largeGraph) {this.largeGraph = largeGraph;}

	public static ArrayList<String> compStrings() {
		ArrayList<String> compList = new ArrayList<String>();
		compList.add(COMPSTRINGCTL);
		compList.add(COMPSTRINGTCL);
		compList.add(COMPSTRINGLTC);
		return compList;
	}

	
	/**
	 * Should be given a valid atomic eulerGraph.
	 */
	protected void buildFromEulerGraph(DualGraph eg) {
		
		largeGraph.clear();
		
		// graph contains all the items in the euler graph, so copy them
		HashMap<Node,Node> nodeMap = new HashMap<Node,Node>();

		for(Node n : eg.getNodes()) {
			Node newN = new Node(n.getLabel(),new Point(n.getCentre()));
			newN.setType(EULER_NODE);
			largeGraph.addNode(newN);
			nodeMap.put(n,newN);
		}
		for(Edge e : eg.getEdges()) {
			Node nFrom = nodeMap.get(e.getFrom());
			Node nTo = nodeMap.get(e.getTo());
			Edge newE = new Edge(nFrom,nTo,e.getLabel());
			newE.setType(EULER_EDGE);
			newE.setBends(new ArrayList<Point>(e.getBends()));
			largeGraph.addEdge(newE);
		}
		
long time2 = System.currentTimeMillis();			
		
		
		if(eg.getNodes().size() == 0) {
			// case of an empty diagram
			addEmptyEulerGraphItems(largeGraph);
			findGraphFromLargeGraph();
			return;
		}
		addDualAndNewItems();
long time3 = System.currentTimeMillis();			
System.out.println("buildFromEulerGraph addDualAndNewItems "+(time3-time2)/1000.0);

		findGraphFromLargeGraph();

	}
	

	/**
	 * If the Euler graph is not empty, add correct green and red components
	 */
	protected boolean addDualAndNewItems() {
		
		if(diagramPanel != null) {
			panelMessage(diagramPanel,"Starting adding new nodes on Euler graph edges");
		}
		
		// add the new nodes on the euler edges
		ArrayList<Edge> edges = new ArrayList<Edge>(largeGraph.getEdges());
		for(Edge e : edges) {
			if(e.getType() == EULER_EDGE) {
				String label = e.getLabel();
				label = addUniqueIndex(label,largeGraph.getNodes());
				splitEdgeWithNewNode(largeGraph,e,label,NEW_NODE);
			}
		}


		ArrayList<ConcreteContour> ccs = findConcreteContours();
		HashMap<String,Area> zoneAreas = ConcreteContour.generateZoneAreas(ccs);
		//Rectangle fullR = null;
long time2 = System.currentTimeMillis();

		for(String zone : zoneAreas.keySet()) {
			if(diagramPanel != null) {
				panelMessage(diagramPanel,"Adding nodes and edges to zone "+zone);
			}
			

			if(!zone.equals("")) {

				Area a = zoneAreas.get(zone);
				ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
				Polygon p = ps.get(0);
				
				/* This is replaced by the below
				euler.maxrectangle.MaxRectangleFinder maxRectangleFinder = new euler.maxrectangle.MaxRectangleFinder(p);
				fullR = maxRectangleFinder.getMaxSquare();
				if(fullR == null) {
					// nasty fix to prevent a crash
					fullR = p.getBounds();
				}
				
				int x = (int)(fullR.getX()+fullR.getWidth()/2);
				int y = (int)(fullR.getY()+fullR.getHeight()/2);
				Point middleP = new Point(x,y);
				To here */

				Point2D middlePD = findMiddlePointInsidePolygon(p);
				Point middleP = new Point((int)middlePD.getX(),(int)middlePD.getY());
				
				
				// add in the edges
				createMultiGraph();
				multiGraph.formFaces();

				Node dualNode = new Node(zone,DUAL_NODE,middleP);
				largeGraph.addNode(dualNode);

				// find the nodes which edges need to be created to link to the new middle node
				Face f = findContainingFace(multiGraph,dualNode.getCentre());
				ArrayList<Node> joinMultiNodes = new ArrayList<Node>();
				for(Node multiNode : f.getNodeList()) {
					if(multiNode.getType() == EULER_NODE || multiNode.getType() == NEW_NODE) {
						joinMultiNodes.add(multiNode);
					}
				}
				
				// create and route the new edges
				
				// test for edges that can be created without a polyline
 				ArrayList<Node> nodeList = new ArrayList<Node>(joinMultiNodes);
				for(Node multiN : nodeList) {
					Polygon poly = f.getPolygon();

					if(euler.Util.lineInPolygon(poly, multiN.getCentre(), dualNode.getCentre())) {
						Edge e = new Edge(dualNode,multiNodeMap.get(multiN),"");
						if(multiN.getType() == EULER_NODE) {
							e.setType(NEW_EDGE);
						} else {
							e.setType(DUAL_EDGE);
						}
						largeGraph.addEdge(e);
						joinMultiNodes.remove(multiN);
					}

				}
				
				// we are going to destroy the multigraph, so
				// find the nodes that need multi edges
				ArrayList<Node> joinNodes = new ArrayList<Node>();
				for(Node multiN : joinMultiNodes) {
					joinNodes.add(multiNodeMap.get(multiN));
				}
				
				// find polylines for the joinNodes
				for(Node n : joinNodes) {

//System.out.println("multi edge for "+dualNode+" "+n);
					// diagram has changed, so reform the multigraph

					createMultiGraph();
					multiGraph.formFaces();
					multiGraph.triangulate();
					
					Node multiN = multiGraph.firstNodeAtPoint(n.getCentre());
					Node multiDualNode = multiGraph.firstNodeAtPoint(dualNode.getCentre());
					Face containingFace = null;
					// find the face with both nodes in
					for(Face newFace : multiGraph.getFaces()) {
						ArrayList<Node> faceNodes = newFace.getNodeList(); 
						if(faceNodes.contains(multiN) && faceNodes.contains(multiDualNode)) {
							containingFace = newFace;
						}
					}

					ArrayList<Point> path = multiGraph.findPathThroughFace(containingFace,multiDualNode,multiN);


					Edge e = new Edge(dualNode,n,"");
					if(n.getType() == EULER_NODE) {
						e.setType(NEW_EDGE);
					} else {
						e.setType(DUAL_EDGE);
					}
					e.setBends(path);
					largeGraph.addEdge(e);

				}

				
				// failed replacement for the above
/*
				// create the edges
				// test for edges that can be created without a polyline
//				Node firstMultiNode = null;
//				Edge firstMultiEdge = null;
				ArrayList<Edge> joinMultiEdges = new ArrayList<Edge>();
				for(Node multiN : joinMultiNodes) {

					Edge e = new Edge(dualNode,multiNodeMap.get(multiN),"");
					if(multiN.getType() == EULER_NODE) {
						e.setType(NEW_EDGE);
					} else {
						e.setType(DUAL_EDGE);
					}
					largeGraph.addEdge(e);
					joinMultiEdges.add(e);


				}
				
				//route the edges
				
				// first find the nodes in the face and form a new graph
				DualGraph faceGraph = findFaceGraph(f);

				faceGraph.formFaces();
				Face firstInnerFace = null;
				for(Face testFace : faceGraph.getFaces()) {
					if(faceGraph.getOuterFace() != testFace) {
						firstInnerFace = testFace;
					}
				}
				Polygon firstInnerPoly = firstInnerFace.getPolygon();

				Node faceDualNode = new Node(dualNode.getLabel(),dualNode.getCentre());
				faceGraph.addNode(faceDualNode);

				// find two dummy edges to ensure that faces can be found
				Edge dummyFaceEdge1 = null;
				Edge dummyFaceEdge2 = null;
				int count = 0; // this to attempt to separate the dummy edges
				int numberOfNodes = faceGraph.getNodes().size()-1;
				for(Node testNode : faceGraph.getNodes()) {
					if(testNode == faceDualNode) {
						continue;
					}
					// must be a poly node, otherwise it might parallel a desired edge
					if(testNode.getType() != POLY_DUAL_NODE && testNode.getType() != POLY_EULER_NODE && testNode.getType() != POLY_NEW_NODE && testNode.getType() != POLY_NODE_TYPE) {
						continue;
					}
					if(dummyFaceEdge1 == null) {
						if(isGoodLine(firstInnerPoly, testNode.getCentre(), faceDualNode.getCentre())) {
							dummyFaceEdge1 = new Edge(faceDualNode,testNode);
							faceGraph.addEdge(dummyFaceEdge1);
						}

					} else {
						count++;

//						if(count > numberOfNodes/2) {
							if(isGoodLine(firstInnerPoly, testNode.getCentre(), faceDualNode.getCentre())) {
								dummyFaceEdge2 = new Edge(faceDualNode,testNode);
								faceGraph.addEdge(dummyFaceEdge2);
								break;
							}
//						}
					}
				}
				if(dummyFaceEdge1 == null || dummyFaceEdge2 == null) {
					System.out.println("FAILED to find two dummy face edges values: "+dummyFaceEdge1+" "+dummyFaceEdge2);
				}
				
				// route the rest of the edges
				for(Edge multiE : joinMultiEdges) {
					
					Node multiN = multiE.getOppositeEnd(dualNode);
					
					faceGraph.formFaces();
					faceGraph.triangulate();
					
					Node faceN = faceGraph.firstNodeAtPoint(multiN.getCentre());
					for(Face face : faceGraph.getFaces()) {
						if(faceGraph.getOuterFace() == face) {
							continue;
						}
						if(face.getNodeList().contains(faceDualNode) && face.getNodeList().contains(faceN)) {


							ArrayList<Point> bends = faceGraph.findPathThroughFace(face, faceDualNode, faceN);

							multiE.setBends(bends);
							addBendsAsPolyEdge(faceGraph,bends,faceDualNode,faceN);

							break;
						}
					}
				}
				
//DualGraphWindow dgw = new DualGraphWindow(faceGraph);				
long timex2 = System.currentTimeMillis();
System.out.println("x addDualAndNewItems finding polylines for new edges "+(timex2-timex1)/1000.0);
*/


			}
			
		}
long time3 = System.currentTimeMillis();			
System.out.println("2 addDualAndNewItems add the red nodes plus edges "+(time3-time2)/1000.0);


		
		if(diagramPanel != null) {
			panelMessage(diagramPanel,"Adding outside zone nodes and edges");
		}
		
		// add the outside nodes and connect them
		// this method relies on their always being a straight line from the node or
		// edge and the new node
		
		createMultiGraph();
		multiGraph.formFaces();
		
		Point multiCentre = multiGraph.findCentre();
		int multiWidth = multiGraph.findWidth();
		int multiHeight = multiGraph.findHeight();
		double multiDiagonal = Math.sqrt(multiHeight*multiHeight+multiWidth*multiWidth);
		int radius = (int)multiDiagonal/2+SURROUNDING_POLY_BORDER;
		Polygon surroundingPolygon = RegularPolygon.generateRegularPolygon(multiCentre.x, multiCentre.y, radius, SURROUNDING_POLY_POINTS);

		Face outerMultiFace = multiGraph.getOuterFace();
		
		ArrayList<Node> outerNodeList = outerMultiFace.getNodeList();

		for(Node multiNode : outerNodeList) {
			createMultiGraph();
			multiGraph.formFaces();
			
			ArrayList<Node> createdNodes = new ArrayList<Node>();
			
			if(multiNode.getType() == EULER_NODE || multiNode.getType() == NEW_NODE) {
				Point newNodePoint = findBestPointInPolygon(multiGraph,multiNode,surroundingPolygon);
				if(newNodePoint == null) {
					//TODO - find polyline routings in case of failed straight line
					System.out.println("CANT find straight line from node "+multiNode+" to surrounding polygon");
					return false;
				} else {
					if(multiNode.getType() == EULER_NODE) {
						String label = addUniqueIndex(EMPTY_ZONE_LABEL,largeGraph.getNodes());
						Node n = new Node(label,NEW_NODE,newNodePoint);
						largeGraph.addNode(n);
						createdNodes.add(n);
						Node mappedNode = largeGraph.firstNodeWithLabel(multiNode.getLabel());

						Edge e = new Edge(n,mappedNode,"",NEW_EDGE);
						largeGraph.addEdge(e);
					} else if(multiNode.getType() == NEW_NODE) {
						String label = addUniqueIndex(EMPTY_ZONE_LABEL,largeGraph.getNodes());
						Node n = new Node(label,DUAL_NODE,newNodePoint);
						largeGraph.addNode(n);
						createdNodes.add(n);
						Node mappedNode = largeGraph.firstNodeWithLabel(multiNode.getLabel());

						Edge e = new Edge(n,mappedNode,"",DUAL_EDGE);
						largeGraph.addEdge(e);
					}
				}
			}
		}
long time4 = System.currentTimeMillis();			
System.out.println("3 addDualAndNewItems adding outer nodes and some edges "+(time4-time3)/1000.0);


		Node firstNode = null;
		Node prevNode = null;
		Node nextNode = null;
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Point> partialPath = new ArrayList<Point>();
		double[] coords = new double[6];
		PathIterator pi = surroundingPolygon.getPathIterator(null);
		while (!pi.isDone()) {
			int coordType = pi.currentSegment(coords);
			if (coordType == PathIterator.SEG_CLOSE) {
				pi.next();
				continue;
			}
			
			int x = pjr.graph.Util.convertToInteger(coords[0]);
			int y = pjr.graph.Util.convertToInteger(coords[1]);
			Point point = new Point(x,y);

			Node n = largeGraph.firstNodeAtPoint(point);

			if(n != null) {
				if(firstNode == null) {
					firstNode = n;
					nextNode = n;
					partialPath.addAll(path);
					path = new ArrayList<Point>();
				} else {
					prevNode = nextNode;
					nextNode = n;
					Edge e = new Edge(prevNode,nextNode);
					e.setType(DUAL_EDGE);
					e.setBends(path);
					largeGraph.addEdge(e);
					path = new ArrayList<Point>();
				}
			} else {
				path.add(point);
			}
			pi.next();
		}
		
		if(firstNode != null) {
			Edge e = new Edge(nextNode,firstNode);
			e.setType(DUAL_EDGE);
			path.addAll(partialPath);
			e.setBends(path);
			largeGraph.addEdge(e);
			
		}

		
		return true;
		
	}



	public static void addBendsAsPolyEdge(DualGraph dg, ArrayList<Point> bends, Node startNode, Node endNode) {
		Node prev = startNode;
		for(Point point : bends) {
			Node newNode = new Node("",POLY_NODE_TYPE,point);
			dg.addNode(newNode);
			Edge newEdge = new Edge(prev,newNode,"",POLY_EDGE_TYPE);
			dg.addEdge(newEdge);
			prev = newNode;
		}
		Edge newEdge = new Edge(prev,endNode,"",POLY_EDGE_TYPE);
		dg.addEdge(newEdge);
		
	}

	/**
	 * Find a new graph formed from the face.
	 */
	public static DualGraph findFaceGraph(Face f) {
		DualGraph faceGraph = new DualGraph();
		HashMap<Node,Node> createdNodeMap = new HashMap<Node,Node>();
		for(Node n : f.getNodeList()) {
			Node newN = new Node(n.getLabel(),new Point(n.getCentre()));
			newN.setType(n.getType());
			faceGraph.addNode(newN);
			
			// create any connecting edges, note edges must be straight line
			for(Edge e : n.connectingEdges()) {
				for(Node oldN : createdNodeMap.keySet()) {
					if(oldN == e.getOppositeEnd(n)) {
						Edge newE = new Edge(newN,createdNodeMap.get(oldN),e.getLabel());
						newE.setType(e.getType());
						faceGraph.addEdge(newE);
					}
				}
			}
			createdNodeMap.put(n,newN);
		}
		
		return faceGraph;
	}


	/**
	 * Find the best (middle point in the polygon that connects to the node to
	 * the polygon with a straight line without crossing any edges in the graph.
	 * @return Point or null if there is no line possible
	 */
	public static Point findBestPointInPolygon(Graph g, Node n, Polygon p) {
		
		ArrayList<Point> pointList = new ArrayList<Point>();
		ArrayList<Point> pointListAfterBreak = new ArrayList<Point>();
		
		boolean breakInList = false;
		
		double[] coords = new double[6];
		PathIterator pi = p.getPathIterator(null);
		while (!pi.isDone()) {
			int coordType = pi.currentSegment(coords);
			if (coordType == PathIterator.SEG_CLOSE) {
				pi.next();
				continue;
			}
			int x = pjr.graph.Util.convertToInteger(coords[0]);
			int y = pjr.graph.Util.convertToInteger(coords[1]);
			
			Point polyPoint = new Point(x,y);
			
			if(!crossesEdge(g,polyPoint,n.getCentre())) {
				Node closestNode = g.firstNodeAtPoint(polyPoint);
				if(closestNode == null) { // check to see if there is already a node at the point
					if(!breakInList) {
						pointList.add(polyPoint);
					} else {
						pointListAfterBreak.add(polyPoint);
					}
				}
			} else {
				if(pointList.size() != 0) {
					breakInList = true;
				}
			}
			pi.next();
		}

		if(pointList.size() == 0) {
			return null;
		}
		
		if(pointListAfterBreak.size() != 0) {
			// the later points should be just before the end of the list
			pointList.addAll(0, pointListAfterBreak);
		}
		
		Point graphCentre = g.findCentre();
		
		double minAngle = Double.MAX_VALUE;
		Point ret = null;
		for(Point pTest : pointList) {
			double centreAngle = pjr.graph.Util.lineAngle(graphCentre,n.getCentre());
			double pointAngle = pjr.graph.Util.lineAngle(n.getCentre(),pTest);
			double angle = centreAngle-pointAngle;
			if(angle < 0) {
				angle = -angle;
			}
			if(angle < minAngle) {
				minAngle = angle;
				ret = pTest;
			}
		}
		
				
		return ret;
	}



	/**
	 * Find out if the line between the two points crosses a current edge
	 * in the graph
	 */
	public static boolean crossesEdge(Graph g, Point p1, Point p2) {
		
		for(Edge e : g.getEdges()) {
			if(e.getFrom().getCentre().equals(p1)) {continue;}
			if(e.getTo().getCentre().equals(p1)) {continue;}
			if(e.getFrom().getCentre().equals(p2)) {continue;}
			if(e.getTo().getCentre().equals(p2)) {continue;}
			if(pjr.graph.Util.linesCross(e.getFrom().getCentre(),e.getTo().getCentre(),p1,p2)) {
				return true;
			}
		}
		
		return false;
	}


	/**
	 * See if the two points have a link in the graph.
	 * Relies on straight edges.
	 */
/*	public static boolean haveStraightLineLink(DualGraph dg, Point p1, Point p2) {
		
		for(Edge e : dg.getEdges()) {
			if(e.getFrom().getCentre() == p1) {continue;}
			if(e.getTo().getCentre() == p1) {continue;}
			if(e.getFrom().getCentre() == p2) {continue;}
			if(e.getTo().getCentre() == p2) {continue;}
			if(pjr.graph.Util.linesCross(e.getFrom().getCentre(), e.getTo().getCentre(), p1, p2)) {
				return false;
			}
		}
		
		return true;
	}
*/



	/**
	 * Finds the face in the graph containing the point
	 * @return the containing face, or null if no such face.
	 */
	public static Face findContainingFace(DualGraph dg, Point centre) {
		for(Face face : dg.getFaces()) {
			if(face == dg.getOuterFace()) {
				continue;
			}
			if(face.getPolygon().contains(centre)) {
				return face;
			}
		}
		return null;
	}



	/**
	 * Deletes the edge and adds a new node and two new edges that split the old edge.
	 */
	private static void splitEdgeWithNewNode(DualGraph dg, Edge e, String label, NodeType type) {

		double edgeLength = e.findLength();
		double nodeDistance = edgeLength/2;
		
		ArrayList<Point> bends = new ArrayList<Point>(e.getBends());
		bends.add(e.getTo().getCentre());

		int length = 0;
		ArrayList<Point> bends1 = new ArrayList<Point>();
		ArrayList<Point> bends2 = new ArrayList<Point>();
		Node newNode = null;
		Point last = e.getFrom().getCentre();
		for(Point p : bends) {
			double segmentLength = pjr.graph.Util.distance(last,p);
			length += segmentLength;

			if(pjr.graph.Util.convertToInteger(length) == pjr.graph.Util.convertToInteger(nodeDistance)) {
				if(newNode == null) {
					newNode = new Node(label,type,p);
					dg.addNode(newNode);
				}
			} else if(length < nodeDistance) {
				bends1.add(p);
			} else {
				bends2.add(p);
				if(newNode == null) {
					double remainingDistance = segmentLength-(length-nodeDistance);
					double fraction = remainingDistance/segmentLength;
					Point newNodePoint = pjr.graph.Util.betweenPoints(last,p,fraction);
					newNode = new Node(label,type,newNodePoint);
					dg.addNode(newNode);
				}
			}
			last = p;
		}

		bends2.remove(bends2.size()-1); // remove the last element, its the to node centre
		Edge e1 = new Edge(e.getFrom(),newNode,e.getLabel(),SPLIT_EULER_EDGE);
		Edge e2 = new Edge(newNode,e.getTo(),e.getLabel(),SPLIT_EULER_EDGE);
		e1.setBends(bends1);
		e2.setBends(bends2);
		
/*System.out.println("AAAA");
for(Point p : bends) {System.out.print(p.x+","+p.y+" ");}
System.out.println();
for(Point p : bends1) {System.out.print(p.x+","+p.y+" ");}
System.out.println();
for(Point p : bends2) {System.out.print(p.x+","+p.y+" ");}
System.out.println();
System.out.println(e.getFrom().getCentre()+" "+newNode.getCentre()+" "+e.getTo().getCentre());
*/
		dg.addEdge(e1);
		dg.addEdge(e2);
		dg.removeEdge(e);
	}

/*	private static void splitEdgeWithNewNode(DualGraph dg, Edge e, String label, NodeType type) {
		ArrayList<Point> bends = e.getBends();
		boolean noBends = false;
		if(bends.size() == 0) {
			noBends = true;
		} else if(bends.size() == 1) {
			// special case for single bend in a straight edge
			// discard the bend
			Point p1 = e.getFrom().getCentre();
			Point p2 = bends.get(0);
			Point p3 = e.getTo().getCentre();
			if(pjr.graph.Util.linesNearlyParallel(p1, p2, p2, p3,5)) {
				noBends =  true;
			}
		}

		if(noBends) {
			Point newNodePoint = pjr.graph.Util.midPoint(e.getFrom().getCentre(), e.getTo().getCentre());
			Node newNode = new Node(label,type,newNodePoint);
			dg.addNode(newNode);
			Edge e1 = new Edge(e.getFrom(),newNode,e.getLabel(),SPLIT_EULER_EDGE);
			Edge e2 = new Edge(newNode,e.getTo(),e.getLabel(),SPLIT_EULER_EDGE);
			dg.addEdge(e1);
			dg.addEdge(e2);
			dg.removeEdge(e);
		}  else {
			
			int bendsCount = 0;
			ArrayList<Point> bends1 = new ArrayList<Point>();
			ArrayList<Point> bends2 = new ArrayList<Point>();
			Node newNode = null;
			Point last = e.getFrom().getCentre();
			for(Point p : bends) {
				bendsCount++;
				if(bendsCount <= bends.size()/2) {
					bends1.add(p);
				} else {
					if(newNode == null) {
						Point newNodePoint = pjr.graph.Util.midPoint(last, p);
						newNode = new Node(label,type,newNodePoint);
						dg.addNode(newNode);
					}
					bends2.add(p);
				}
				last = p;
			}
			Edge e1 = new Edge(e.getFrom(),newNode,e.getLabel(),SPLIT_EULER_EDGE);
			Edge e2 = new Edge(newNode,e.getTo(),e.getLabel(),SPLIT_EULER_EDGE);
			e1.setBends(bends1);
			e2.setBends(bends2);
			dg.addEdge(e1);
			dg.addEdge(e2);
			dg.removeEdge(e);
		}

		
	}
*/


	/**
	 * Turn the Euler graph into a list of concrete contours
	 */
	public ArrayList<ConcreteContour> findConcreteContours() {
		// build the concrete contours
		ArrayList<ConcreteContour> ccs = new ArrayList<ConcreteContour>();
		DualGraph eulerGraph = findEulerGraph(largeGraph);
		for(String c : findContourList()) {
			Polygon p = generateContourPolygon(eulerGraph,c);
			ConcreteContour cc = new ConcreteContour(c,p);
			ccs.add(cc);
		}
		return ccs;
	}
	
	/**
	 * Take a sequence of edges forming a closed path and create a polygon
	 * from them.
	 */
	protected static Polygon createPolygonFromPath(ArrayList<Edge> path) {
		
		Edge startEdge = path.get(0);
		Node startNode = startEdge.getFrom();
		if(path.size() > 1) {
			Edge e1 = path.get(1);
			if(e1.getFrom() != startNode && e1.getTo() != startNode) {
				startNode = startEdge.getTo();
			}
		}
			
		Polygon ret = new Polygon();
		
		Node nextNode = startEdge.getOppositeEnd(startNode);
		
		for(Edge e : path) {
			
			// bends might be the wrong way round
			ArrayList<Point> bends = new ArrayList<Point>(e.getBends());
			if(nextNode == e.getTo()) {
				Collections.reverse(bends);
			}
			for(Point p : bends) {
				ret.addPoint(p.x, p.y);
			}
			nextNode = e.getOppositeEnd(nextNode);
			Point centre = nextNode.getCentre();
			ret.addPoint(centre.x, centre.y);
		}
		
		return ret;

	}
	

	

	/**
	 * Find a polygon by following a path made from the nodes and
	 * edge bend points in the Euler graph that have the contour label.
	 * @return the polygon or null if the contour is not contained
	 * in the diagram. The graph passed must be an Euler graph.
	 */
	protected static Polygon generateContourPolygon(DualGraph eulerGraph, String c) {

		eulerGraph.setEdgesVisited(false);
		
		// Find any node with the contour in the label
		Node startNode = null;
		for(Node n : eulerGraph.getNodes()) {
			if(n.getLabel().contains(c)) {
				startNode = n;
				break;
			}
		}
		if(startNode == null) {
			// cant find a node with the contour label
			return null;
		}
		
		Polygon ret = new Polygon();
		ret.addPoint(startNode.getX(), startNode.getY());
		
		Node next = null;
		Node prev = null;
		
		while(next == null || next != startNode) {
			if(next == null) {
				next = startNode;
			}
			
			Edge link = null;
			ArrayList<Edge> connectingEdges = next.unvisitedConnectingEdges();

			for(Edge e : connectingEdges) {
				if(e.getLabel().contains(c)) {
					Node n = e.getOppositeEnd(next);
					link = e;
					prev = next;
					next = n;
					e.setVisited(true);
					break;
				}
			}
			// bends might be the wrong way round
			ArrayList<Point> bends = new ArrayList<Point>(link.getBends());
			if(prev == link.getTo()) {
				Collections.reverse(bends);
			}
			for(Point p : bends) {
				ret.addPoint(p.x, p.y);
			}
			ret.addPoint(next.getX(), next.getY());
			
		}
		
		return ret;
		
	}



	/** Find the contours in the diagram */ 
	public ArrayList<String> findContourList() {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(Node n : largeGraph.getNodes()) {
			if(n.getType() == EULER_NODE) {
				String label = stripDecimals(n.getLabel());
				ArrayList<String> cs = AbstractDiagram.findContourList(label);
				for(String c : cs) {
					if(!ret.contains(c)) {
						ret.add(c);
					}
				}
			}
		}
		
		// zones always contain empty zone
		if(!ret.contains(EMPTY_ZONE_LABEL)) {
			ret.add(EMPTY_ZONE_LABEL);
		}
		
		return ret;
	}

	
	/** Find the zones in the diagram */ 
	public static ArrayList<String> findZoneList(DualGraph dg) {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(Node n : dg.getNodes()) {
			if(n.getType() == DUAL_NODE) {
				String label = stripDecimals(n.getLabel());
				if(!ret.contains(label)) {
					ret.add(label);
				}
			}
		}
		
		// zones always contain empty zone
		if(!ret.contains(EMPTY_ZONE_LABEL)) {
			ret.add(EMPTY_ZONE_LABEL);
		}
		
		return ret;
	}

	

	/** Add the red and green items when the Euler Graph is empty */
	protected static void addEmptyEulerGraphItems(DualGraph dg) {
		Polygon p = RegularPolygon.generateRegularPolygon(CENTREX, CENTREY, EMPTY_DIAGRAM_DIAMETER, EMPTY_DIAGRAM_POLY_POINTS);

		double[] coords = new double[6];
		PathIterator pi = p.getPathIterator(null);
		Node theNode = null;
		Edge theEdge = null;
		while (!pi.isDone()) {
			int coordType = pi.currentSegment(coords);
			if (coordType == PathIterator.SEG_CLOSE) {
				pi.next();
				continue;
			}
			int x = pjr.graph.Util.convertToInteger(coords[0]);
			int y = pjr.graph.Util.convertToInteger(coords[1]);
			if(theNode == null) {
				theNode = new Node(EMPTY_ZONE_LABEL+"1",DUAL_NODE,new Point(x,y));
				dg.addNode(theNode);
				theEdge = new Edge(theNode,theNode,DUAL_EDGE);
				dg.addEdge(theEdge);
			} else {
				theEdge.addBend(new Point(x,y));
			}
			pi.next();
		}
		
	}
	
	
	
	/**
	 * A deep copy of the Graph, attempting to duplicate nodes and edges,
	 * but does not copy derived information like adjList and faceNodes.
	 * Creates the Node and Edge mappings.
	 */
		public DualGraph cloneAndMap() {
			
			DualGraph ret = new DualGraph();
			ret.setLabel(largeGraph.getLabel());
			
			HashMap<Node,Node> nodeMap = new HashMap<Node,Node>(largeGraph.getNodes().size());
			multiNodeMap = new HashMap<Node,Node>(largeGraph.getNodes().size());
			multiEdgeMap = new HashMap<Edge,Edge>(largeGraph.getEdges().size());
			
			for(Node thisNode : largeGraph.getNodes()) {
				Node newNode = new Node(thisNode.getLabel(),thisNode.getType(),thisNode.getCentre());
				newNode.setVisited(thisNode.getVisited());
				newNode.setScore(thisNode.getScore());
				newNode.setMatch(thisNode.getMatch());
				ret.addNode(newNode);
				nodeMap.put(thisNode,newNode);
				multiNodeMap.put(newNode,thisNode);
			}
			
			for(Edge thisEdge : largeGraph.getEdges()) {
				Node thisFrom = thisEdge.getFrom();
				Node thisTo = thisEdge.getTo();
				Node newFrom = nodeMap.get(thisFrom);
				Node newTo = nodeMap.get(thisTo);
				
				Edge newEdge = new Edge(newFrom, newTo, thisEdge.getLabel(), thisEdge.getWeight(), thisEdge.getType());
				newEdge.setVisited(thisEdge.getVisited());
				newEdge.setScore(thisEdge.getScore());
				newEdge.setMatch(thisEdge.getMatch());
				newEdge.setBends(new ArrayList<Point>(thisEdge.getBends()));
				ret.addEdge(newEdge);
				
				multiEdgeMap.put(newEdge,thisEdge);
			}
			return ret;
		}

	

	/**
	 * Creates the matching multi graph, replacing bend points
	 * with nodes. Also creates the maps from the multi graph to
	 * this graph
	 */
	protected void createMultiGraph() {
		
		multiGraph = cloneAndMap();
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>(multiGraph.getEdges());
		ArrayList<Edge> removeList = new ArrayList<Edge>();
		
		for(Edge e : edgeList) {
			if(e.getBends().size() != 0) {
				NodeType nodeType = null;
				EdgeType edgeType = e.getType();
				String edgeLabel = e.getLabel();
				if(e.getType() == EULER_EDGE) {
					nodeType = POLY_EULER_NODE;
				} else if(e.getType() == SPLIT_EULER_EDGE) {
					nodeType = POLY_EULER_NODE;
				} else if(e.getType() == DUAL_EDGE) {
					nodeType = POLY_DUAL_NODE;
				} else if(e.getType() == NEW_EDGE) {
					nodeType = POLY_NEW_NODE;
				}
				
				Edge hybridEdge = multiEdgeMap.get(e);
				
				Node prev = e.getFrom();
				for(Point p : e.getBends()) {
					Node newNode = new Node("",nodeType,p);
					multiGraph.addNode(newNode);
					Edge newEdge = new Edge(prev,newNode,edgeLabel,edgeType);
					multiGraph.addEdge(newEdge);
					prev = newNode;
					multiEdgeMap.put(newEdge, hybridEdge); // new edge maps back to the corresponding hybrid edge
				}
				Edge newEdge = new Edge(prev,e.getTo(),edgeLabel,edgeType);
				multiGraph.addEdge(newEdge);
				
				removeList.add(e);
				
			}
		}
		
		for(Edge e : removeList) {
			multiGraph.removeEdge(e);
			multiEdgeMap.remove(e);
		}
		
	}
	
	
	/**
	 * A deep copy of the abstract graph, attempting to duplicate nodes and edges
	 **/
	public HybridGraph clone() {
			
		HybridGraph ret = new HybridGraph();
		Graph clonedGraph = super.clone();			
		
		ret.label = clonedGraph.getLabel();
		ret.nodes = clonedGraph.getNodes();
		ret.edges = clonedGraph.getEdges();
		
		ret.setLargeGraph(getLargeGraph().clone());
		
		return ret;
	}



	

	/**
	 * Returns the new Euler Graph created from following the path.
	 * The path argument should include the start node at the start and
	 * end of the list.
	 * TODO Deal with dangling components.
	 */
	public static DualGraph eulerGraphWithNodePath(String contourLabel, ArrayList<Node> path, DualGraph dg) {
		euler.Util.log("eulerGraphWithNodePath("+contourLabel+", "+path+")");

		if(path.get(0) != path.get(path.size()-1)) {
			System.out.println("ERROR in eulerGraphWithNodePath. The first and last nodes of the path are not the same");
			return null;
		}
		
		DualGraph cloneGraph = dg.clone();
		
		HashMap<Node,String> labelMap = new HashMap<Node,String>();
		
		cloneGraph.setEdgesVisited(false);

		Node prevNode = null;

		for(Node pathNode : path) {
			
			if(pathNode == null) {
				System.out.println("ERROR in eulerGraphWithNodePath. null Node in path");
				return null;
			}
				
			
			Node n = cloneGraph.firstNodeWithLabel(pathNode.getLabel());
			
			if(n == null) {
				System.out.println("ERROR in eulerGraphWithNodePath. There is no node with label "+pathNode.getLabel());
			}

			// if the node is not a current Euler graph node, its just the contour
			// if it is a Euler graph node is the current contours, plus the new one
			String label = contourLabel;
			if(n.getType() == EULER_NODE || n.getType() == NEW_NODE) {
				// strip numbers off
				ArrayList<String> cList = new ArrayList<String>();
				ArrayList<String> sList = AbstractDiagram.findContourList(n.getLabel());
				for(String s : sList) {
					char c = s.charAt(0);
					if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER) {
						cList.add(s);
					}
				}
				// add contour
				cList.add(contourLabel);
				// sort
				AbstractDiagram.sortZoneList(cList);
				
				StringBuffer labelBuff = new StringBuffer();
				for(String c : cList) {
					if(!c.equals(EMPTY_ZONE_LABEL)) { // remove any outer zone occurence
						labelBuff.append(c);
					}
				}
				label = labelBuff.toString();
			}
			
			labelMap.put(n,label);
			
			if(prevNode == null) {
				prevNode = n;
				continue;
			}
			
			// try and find the route from prev to n
			ArrayList<Edge> connectingEdges = n.unvisitedConnectingEdges();
			Edge connectingEdge = null;
			for(Edge e : connectingEdges) {
				if(e.getFrom() == prevNode || e.getTo() == prevNode) {
					connectingEdge = e;
					break;
				}
			}
			if(connectingEdge == null) {
				System.out.println("ERROR in eulerGraphWithNodePath. There is no edge between nodes "+prevNode+" "+n);
				return null;
			}

			// if the edge followed is an Euler edge, add the contour label
			// to it, rather than create a parallel edge
			// if the edge folloed is not, create a new Edge.
			connectingEdge.setVisited(true);
			Edge e = null;
			if(connectingEdge.getType() == EULER_EDGE || connectingEdge.getType() == SPLIT_EULER_EDGE) {
				e = connectingEdge;
				// dont want findEulerGraph to mess about with split edges
				if(e.getType() == SPLIT_EULER_EDGE) {
					e.setType(EULER_EDGE);
				}
				String edgeLabel = e.getLabel()+contourLabel;
				edgeLabel = AbstractDiagram.orderZone(edgeLabel);
				e.setLabel(edgeLabel);
			} else {
				e = new Edge(n,prevNode,contourLabel,EULER_EDGE);
				ArrayList<Point> bends = new ArrayList<Point>(connectingEdge.getBends());
				if(connectingEdge.getTo() == n) {
					Collections.reverse(bends);
				}
				e.setBends(bends);
				cloneGraph.addEdge(e);
			}
			
			prevNode.setType(EULER_NODE);
			n.setType(EULER_NODE);
			
			prevNode = n;
			
		}
		
		
		for(Node n : labelMap.keySet()) {
			// add unique identifier
			String label = labelMap.get(n);
			label = addUniqueIndex(label,cloneGraph.getNodes());
			n.setLabel(label);
		}
		
		DualGraph eulerGraph = findEulerGraph(cloneGraph);
		
		eulerGraph.centreOnPoint(CENTREX, CENTREY);

		return eulerGraph;
		
	}
	


	/**
	 * Returns the new Euler Graph created from following the edges in the path.
	 * The path argument should include no duplicate edges or 
	 * twice visited nodes.
	 * TODO Deal with dangling components.
	 */
	public DualGraph eulerGraphWithEdgePath(String contourLabel, ArrayList<Edge> path) {
		euler.Util.log("eulerGraphWithEdgePath("+contourLabel+", "+path+")");

		DualGraph cloneGraph = largeGraph.clone();
		cloneGraph.setEdgesVisited(false);

		HashMap<Node,String> labelMap = new HashMap<Node,String>();
		
		Edge startEdge = path.get(0);
		Node startNode = startEdge.getFrom();
		if(path.size() > 1) {
			Edge e1 = path.get(1);
			if(e1.getFrom() != startNode && e1.getTo() != startNode) {
				startNode = startEdge.getTo();
			}
		}
			
		for(Edge pathEdge : path) {
			
			if(pathEdge == null) {
				System.out.println("ERROR in eulerGraphWithEdgePath. null Node in path");
				return null;
			}
			
			Node n1 = cloneGraph.firstNodeWithLabel(pathEdge.getFrom().getLabel());
			Node n2 = cloneGraph.firstNodeWithLabel(pathEdge.getTo().getLabel());
			Edge e = cloneGraph.getEdge(n1,n2);
			
			if(e == null) {
				System.out.println("ERROR in eulerGraphWithEdgePath. There is no edge "+pathEdge);
			} else {
				if(e.getType() == EULER_EDGE || e.getType() == SPLIT_EULER_EDGE) {
					e.setLabel(e.getLabel()+contourLabel);
				} else {
					e.setType(EULER_EDGE);
					e.setLabel(contourLabel);
				}
			}
			
			String label = contourLabel;
			if(n1.getType() == EULER_NODE || n1.getType() == NEW_NODE) {
				label = stripDecimals(n1.getLabel());
				if(label.equals(EMPTY_ZONE_LABEL)) { // remove any outer zone occurence
					label = "";
				}
				label = label+contourLabel;
			}
			labelMap.put(n1,label);
		
			label = contourLabel;
			if(n2.getType() == EULER_NODE || n2.getType() == NEW_NODE) {
				label = stripDecimals(n2.getLabel());
				if(label.equals(EMPTY_ZONE_LABEL)) { // remove any outer zone occurence
					label = "";
				}
				label = label+contourLabel;
			}
			labelMap.put(n2,label);
		}
		
		
		for(Node n : labelMap.keySet()) {
			// add unique identifier
			String l = labelMap.get(n);
			l = addUniqueIndex(l,cloneGraph.getNodes());
			n.setLabel(l);
			n.setType(EULER_NODE);
		}
		
		DualGraph eulerGraph = findEulerGraph(cloneGraph);

		eulerGraph.centreOnPoint(CENTREX, CENTREY);

		return eulerGraph;
		
	}
	


	
	/** Return a new String with any decimal numbers stripped out. */
	public static String stripDecimals(String label) {
		ArrayList<String> cList = new ArrayList<String>();
		ArrayList<String> sList = AbstractDiagram.findContourList(label);
		for(String s : sList) {
			char c = s.charAt(0);
			if(Character.getType(c) != Character.DECIMAL_DIGIT_NUMBER) {
				cList.add(s);
			}
		}
		AbstractDiagram.sortZoneList(cList);
		
		StringBuffer labelBuff = new StringBuffer();
		for(String c : cList) {
			labelBuff.append(c);
		}
		String ret = labelBuff.toString();
		return ret;
	}
	
	
	/**
	 * Add an index from 1 to make the label unique compared to the node labels in the node list.
	 */
	public static String addUniqueIndex(String label, ArrayList<Node> nodes) {

		String ret = null;
		
		int index = 0;
		boolean stop = false;
		while(!stop) {
			index ++;
			ret = label+Integer.toString(index);
			boolean found = false;
			for(Node n : nodes) {
				if(n.getLabel().equals(ret)) {
					found = true;
				}
			}
			if(!found) {
				stop = true;
			}
		}

		return ret;
	}


	/**
	 * Returns the Euler graph in this hybrid graph, repairing any split euler edges.
	 */
	public static DualGraph findEulerGraph(DualGraph dg) {

		DualGraph eg = dg.clone();
		
		// deal with the split Euler edges
		ArrayList<Node> removeNodes = new ArrayList<Node>();
		for(Node n : eg.getNodes()) {
			if(n.getType() == NEW_NODE) {
				ArrayList<Edge> splitEdges = new ArrayList<Edge>();
				for(Edge e : n.connectingEdges()) {
					if(e.getType() == SPLIT_EULER_EDGE) {
						splitEdges.add(e);
					}
				}
				if(splitEdges.size() == 2) {
					// a genuine split, so join them up
					
					Edge e1 = splitEdges.get(0);
					Edge e2 = splitEdges.get(1);
					ArrayList<Point> bends = new ArrayList<Point>(e1.getBends());
					bends.addAll(new ArrayList<Point>(e2.getBends()));

					if(e1.getFrom() == n) {
						// other way round
						bends = new ArrayList<Point>(e1.getBends());
						Collections.reverse(bends);
						ArrayList<Point> bends2 = new ArrayList<Point>(e2.getBends());
						Collections.reverse(bends2);
						bends.addAll(bends2);
					}
						
					Node end1 = e1.getOppositeEnd(n);
					Node end2 = e2.getOppositeEnd(n);
					Edge eulerEdge = new Edge(end1,end2,e1.getLabel(),EULER_EDGE);
					eg.addEdge(eulerEdge);
					eulerEdge.setBends(bends);
					removeNodes.add(n); // this removes the split edges as well
				}
			}
		}
		
		for(Node n : removeNodes) {
			eg.removeNode(n);
		}
		
		// change any remaining split edges into Euler edges
		for(Edge e : eg.getEdges()) {
			if(e.getType() == SPLIT_EULER_EDGE) {
				e.setType(EULER_EDGE);
			}
		}
		
		// should be able to remove all non euler graph nodes
		// and so many non euler graph edges also are removed
		ArrayList<Node> removeNodeList = new ArrayList<Node>();
		for(Node n : eg.getNodes()) {
			if(n.getType() != EULER_NODE && n.getType() != POLY_EULER_NODE) {
				removeNodeList.add(n);
			}
		}
		
		for(Node n : removeNodeList) {
			eg.removeNode(n);
		}
		
		// remove the remaining non Euler graph edges, these connect two euler graph nodes
		ArrayList<Edge> removeEdgeList = new ArrayList<Edge>();
		for(Edge e : eg.getEdges()) {
			if(e.getType() != EULER_EDGE) {
				removeEdgeList.add(e);
			}
		}
		
		for(Edge e : removeEdgeList) {
			eg.removeEdge(e);
		}
		
		
		// euler graph nearly complete, now remove 2 degree nodes except self connecting nodes
		boolean loop = true;
		while(loop) {
			Node n = null;
			Edge e1 = null;
			Edge e2 = null;
			loop = false;
			for(Node tryN : eg.getNodes()) {
				
				ArrayList<Edge> connectingEdges = tryN.connectingEdges();

				if(connectingEdges.size() == 2) {
					e1 = connectingEdges.get(0);
					e2 = connectingEdges.get(1);
					
					if(e1 == e2) {
						continue;
					}
					
					n = tryN;
					loop = true;
					break;
				}
			}
			if(n != null) {
				Edge e = replaceEdges(eg,e1,e2,n);
				e.setLabel(e1.getLabel());
				e.setType(EULER_EDGE);
			}
			
		}

		
		return eg;
	}

	/**
	 * n must be connected to e1 and e2. n and all connecting edges
	 * are removed, and another edge, replacing e1 and e2 is added.
	 * Bend points are created, at least one - at n's centre.
	 */
	public static Edge replaceEdges(Graph g, Edge e1, Edge e2, Node n) {
		Node otherEnd1 = e1.getOppositeEnd(n);
		Node otherEnd2 = e2.getOppositeEnd(n);
		ArrayList<Point> bends = e1.getBends();
		if(e1.getFrom() == n) {
			Collections.reverse(bends);
		}
		ArrayList<Point> bends2 = e2.getBends();
		if(e2.getTo() == n) {
			Collections.reverse(bends2);
		}
		Edge e = new Edge(otherEnd1,otherEnd2);
		bends.add(new Point(n.getCentre()));
		bends.addAll(bends2);
		e.setBends(bends);

		g.addEdge(e);
		
		g.removeNode(n); // removes the two edges as well
		
		return e;
	}
	
	
	
	
	public boolean isEmptyZone(String z) {
		if(z.equals("")) {return true;}
		if(z.equals("0")) {return true;}
		if(z.equals("O")) {return true;}
		return false;
	}
	

	/**
	 * Find a path through the hybrid graph that matches the specification
	 * for the new contour. Attempts to find a simple path implying that
	 * it only adds simple curves.
	 */
	public ArrayList<Edge> findSimplePath(ArrayList<String> splitZones, ArrayList<String> containedZones, int listElement, boolean returnEarliestPath) {
		euler.Util.log("findPath(splitZones "+splitZones+", containedZones "+containedZones+") comparator "+compString);

		ArrayList<String> zones = findZoneList(largeGraph);
		
		DualGraph cloneGraph = cloneAndMap();
		
		if(containedZones.size() == 0 && splitZones.size() == 0) {
			errorMessage("Must contain or split at least one zone");
			return null;
		}
		
		// sanity check
		for(String z : containedZones) {
			if(isEmptyZone(z)) {
				errorMessage("Cannot contain the infinitely bounded outer zone "+z);
				return null;
			}
			if(!zones.contains(z)) {
				errorMessage("Zone "+z+" in the contained zones but not found in diagam");
				return null;
			}
		}
		
		// empty zone needs to be denoted by "O"
		if(splitZones.remove("")) {
			splitZones.add(EMPTY_ZONE_LABEL);
		}
		// sanity check
		for(String z : splitZones) {
			if(!zones.contains(z)) {
				errorMessage("Zone "+z+" in the split zones but not found in diagam");
				return null;
			}
		}

		
		// derive the zones that should not be contained
		ArrayList<String> uncontainedZones = new ArrayList<String>();
		for(String z : zones) {
			if(!splitZones.contains(z) && !containedZones.contains(z)) {
				uncontainedZones.add(z);
			}
		}
		
		
		// find a suitable start node for the search
		Node startNode = null;
		// try finding a NEW_NODE that is between 
		// a contained and uncontained DUAL_NODE
		for(Node n : cloneGraph.getNodes()) {
			if(n.getType() == NEW_NODE) {
				// find the two connecting DUAL_NODES
				Node n1 = null;
				Node n2 = null;
				for(Node nTest : n.connectingNodes()) {
					if(nTest.getType() == DUAL_NODE) {
						if(n1 == null) {
							n1 = nTest;
						} else {
							n2 = nTest;
							break;
						}
					}
				}
				if(n2 == null) {
					// some dual edges are self sourcing
					continue;
				}
				// do the test
				String n1Label = stripDecimals(n1.getLabel());
				String n2Label = stripDecimals(n2.getLabel());
				if(containedZones.contains(n1Label) && uncontainedZones.contains(n2Label)) {
					startNode = n;
					break;
				}
				if(uncontainedZones.contains(n1Label) && containedZones.contains(n2Label)) {
					startNode = n;
					break;
				}
			}
		}
		
		if(startNode == null) {

			// otherwise there must be a split zone, then use a split zone
			// DUAL_NODE as the starting node, preferably not an empty zone dual node
			String zone = splitZones.get(0);
			for(Node n : cloneGraph.getNodes()) {
				String label = stripDecimals(n.getLabel());
				if(n.getType() == DUAL_NODE && label.equals(zone)) {
					startNode = n;
					if(!zone.equals(EMPTY_ZONE_LABEL)) {
						// if the node is in the empty zone, try for another
						break;
					}
				}
			}
		}

		// optimization - remove the contained euler nodes, and keep a record of their points
		ArrayList<Node> removeNodes = new ArrayList<Node>();
		ArrayList<Point> containedPoints = new ArrayList<Point>();
		for(Node n : cloneGraph.getNodes()) {
			String label = stripDecimals(n.getLabel());
			
			if(n.getType() == DUAL_NODE && containedZones.contains(label)) {
				removeNodes.add(n);
				containedPoints.add(n.getCentre());
			}
		}
		
		// optimization - remove the uncontained euler nodes
		ArrayList<Point> uncontainedPoints = new ArrayList<Point>();
		for(Node n : cloneGraph.getNodes()) {
			String label = stripDecimals(n.getLabel());
			
			if(uncontainedZones.contains(label)) {
				if(n.getType() == DUAL_NODE) {
					removeNodes.add(n);
					uncontainedPoints.add(n.getCentre());
				} else if(n.getType() == NEW_NODE && label.equals(EMPTY_ZONE_LABEL)) {
				// special case for empty zone
					removeNodes.add(n);
					uncontainedPoints.add(n.getCentre());
				}
			}
		}
		

		// optimization - remove the NEW_NODES that connect to
		// two contained or two uncontained DUAL_NODES
		for(Node n : cloneGraph.getNodes()) {
			if(n.getType() == NEW_NODE) {
				// find the two connecting DUAL_NODES
				Node n1 = null;
				Node n2 = null;
				for(Node nTest : n.connectingNodes()) {
					if(nTest.getType() == DUAL_NODE) {
						if(n1 == null) {
							n1 = nTest;
						} else {
							n2 = nTest;
							break;
						}
					}
				}
				if(n2 == null) {
					// some new nodes connect to the same dual node
					continue;
				}
				// do the test
				String n1Label = stripDecimals(n1.getLabel());
				String n2Label = stripDecimals(n2.getLabel());
				if(containedZones.contains(n1Label) && containedZones.contains(n2Label)) {
					removeNodes.add(n);
				}
				if(uncontainedZones.contains(n1Label) && uncontainedZones.contains(n2Label)) {
					removeNodes.add(n);
				}
			}
		}

		
		for(Node n : removeNodes) {
			cloneGraph.removeNode(n);
		}

		// backtracking search from here
		HashMap<Node,Integer> currentIndex = new HashMap<Node,Integer>();
		ArrayList<Edge> edgePath = new ArrayList<Edge>();
		ArrayList<Node> nodePath = new ArrayList<Node>();
		
		ArrayList<ArrayList<Edge>> storedEdgePaths = new ArrayList<ArrayList<Edge>>();
		ArrayList<ArrayList<Node>> storedNodePaths = new ArrayList<ArrayList<Node>>();

		Node currentNode = startNode;
		currentIndex.put(currentNode,0);
		nodePath.add(currentNode);
		while(true) {
			if(currentIndex.get(currentNode) >= currentNode.connectingEdges().size()) {
				// backtracking
				nodePath.remove(nodePath.size()-1);
				if(nodePath.size() == 0) {
					// first node, so fail
					break;
				}
				edgePath.remove(edgePath.size()-1);
				currentNode = nodePath.get(nodePath.size()-1);
				int index = currentIndex.get(currentNode);
				index++;
				currentIndex.put(currentNode, index);
			} else {
				// try and keep going
				int index = currentIndex.get(currentNode);
				Edge e = currentNode.connectingEdges().get(index);

				// get the next node
				Node oppositeEnd = e.getOppositeEnd(currentNode);
				if(edgePath.contains(e)) {
					// edge is no good because its already in the edgePath
					// try the next edge or backtrack next time
					index++;
					currentIndex.put(currentNode,index);
				} else if(oppositeEnd == startNode) {
					// if we have returned to the start, we have a closed path, so test it
					nodePath.add(oppositeEnd);
					edgePath.add(e);
					if(pathFound(cloneGraph,splitZones,containedPoints,uncontainedPoints,edgePath,nodePath)) {
						if(returnEarliestPath) {
							if(storedEdgePaths.size() == listElement) {
System.out.println("returning path number "+index+" found. Path "+edgePath);
								return(new ArrayList<Edge>(edgePath));
							}
						}
System.out.println("Found new path "+edgePath);
						storedEdgePaths.add(new ArrayList<Edge>(edgePath));
						storedNodePaths.add(new ArrayList<Node>(nodePath));
//System.out.println("good node path"+nodePath);
					}
					// try the next edge or backtrack next time
					nodePath.remove(nodePath.size()-1);
					edgePath.remove(e);
					index++;
					currentIndex.put(currentNode,index);
				} else if(nodePath.contains(oppositeEnd)) {
					// opposite end is no good because its already in the nodePath
					// try the next edge or backtrack next time
					index++;
					currentIndex.put(currentNode,index);
				} else {
					// opposite end is a candiate
					currentNode = oppositeEnd;
					nodePath.add(currentNode);
					edgePath.add(e);
					currentIndex.put(currentNode, 0);
				}
			}
		}
		if(storedEdgePaths.size() == 0) {
			System.out.println("Could not find contour splitting "+splitZones+" and containing "+containedZones+" in findSimplePath");
			return null;
		}

		// find the best path and return it
		ArrayList<Edge> ret = findBestPath(storedEdgePaths, listElement, compString, largeGraph);
		
		return ret;
	}
	
	

	/**
	 * Find out if the node and edge list forms a suitable path
	 * around the graph
	 */
	protected static boolean pathFound(DualGraph g, ArrayList<String> splitZones, ArrayList<Point> containedPoints, ArrayList<Point> uncontainedPoints, ArrayList<Edge> edgePath, ArrayList<Node> nodePath) {
		
		// must be a non empty closed path
		if(edgePath.size() == 0 || nodePath.get(0) != nodePath.get(nodePath.size()-1)) {
			return false;
		}

		// all split zones must have a dual node present
		ArrayList<String> remainingSplitZones = new ArrayList<String>(splitZones);
		for(Node n : nodePath) {
			if(n.getType() == DUAL_NODE) {
				String label = stripDecimals(n.getLabel());
				remainingSplitZones.remove(label);
			}
		}
		if(remainingSplitZones.size() != 0) {
			return false;
		}
		
		Polygon poly = createPolygonFromPath(edgePath);
		
		// all contained points must be contained in the polygon formed from
		// the path
		for(Point p : containedPoints) {
			if(!poly.contains(p)) {
				return false;
			}
		}
		
		// all uncontained points must not be contained in the polygon formed
		// from the path
		for(Point p : uncontainedPoints) {
			if(poly.contains(p)) {
				return false;
			}
		}
		
		return true;
	}
	
	

	/** ListElement gives the nth best path that should be returned */
	protected static ArrayList<Edge> findBestPath(ArrayList<ArrayList<Edge>> storedEdgePaths, int listElement, String compString, DualGraph dg) {
		
		if(listElement >= storedEdgePaths.size()) {
			return null;
		}
		
		PathComparator comp = null;
		if(compString.equals(HybridGraph.COMPSTRINGCTL)) {
			comp = new ConcurrencyTPLengthComparator(dg);
		} else if(compString.equals(HybridGraph.COMPSTRINGTCL)) {
			comp = new TPConcurrencyLengthComparator(dg);
		} else if(compString.equals(HybridGraph.COMPSTRINGLTC)) {
			comp = new LengthTPConcurrencyComparator(dg);
		}
		
		if(comp != null) {
			Collections.sort(storedEdgePaths,comp);
		}
		ArrayList<Edge> ret = storedEdgePaths.get(listElement);
		return ret;
		
/*
		int minListSize = Integer.MAX_VALUE;
		ArrayList<Edge> minList = new ArrayList<Edge>();
		for(ArrayList<Edge> list : storedEdgePaths) {
			if(list.size() < minListSize) {

				minListSize = list.size();
				minList = list;
			}
		}
		return minList;
*/		
	}



	public static int compareLength(ArrayList<Edge> path1, ArrayList<Edge> path2) {
		if(path1.size() > path2.size()) {
			return 1;
		}
		if(path1.size() < path2.size()) {
			return -1;
		}
		return 0;
	}



	public static int compareTriplePoints(ArrayList<Edge> path1, ArrayList<Edge> path2, DualGraph dg) {
		
		int tp1 = countTriplePoints(path1,dg);
		int tp2 = countTriplePoints(path2,dg);
		
		if(tp1 > tp2) {
			return 1;
		}
		if(tp1 < tp2) {
			return -1;
		}
		return 0;
	}




	public static int compareEulerIntersections(ArrayList<Edge> path1, ArrayList<Edge> path2, DualGraph dg) {
		
		int tp1 = countEulerIntersections(path1,dg);
		int tp2 = countEulerIntersections(path2,dg);
		
		if(tp1 > tp2) {
			return 1;
		}
		if(tp1 < tp2) {
			return -1;
		}
		return 0;
	}



	public static int compareConcurrency(ArrayList<Edge> path1, ArrayList<Edge> path2) {
		
		int c1 = countConcurrentEdges(path1);
		int c2 = countConcurrentEdges(path2);
		
		if(c1 > c2) {
			return 1;
		}
		if(c1 < c2) {
			return -1;
		}
		return 0;
	}



	/**
	 * Counts triple points created or increased by adding this path to
	 * the diagram. Relies on l being a simple path, no repeated
	 * edges or nodes
	 */
	public static int countTriplePoints(ArrayList<Edge> path,DualGraph dg) {
		ArrayList<String> nodeLabels = new ArrayList<String>();
		for(Edge e : path) {
			if(!nodeLabels.contains(e.getTo().getLabel())) {
				nodeLabels.add(e.getTo().getLabel());
			}
			if(!nodeLabels.contains(e.getFrom().getLabel())) {
				nodeLabels.add(e.getFrom().getLabel());
			}
		}
		int count = 0;
		for(String s : nodeLabels) {
			Node n = dg.firstNodeWithLabel(s);
			
			if(n.getType() == EULER_NODE) {
				int numberOfEulerEdges = 0;

				for(Edge e : n.connectingEdges()) {
					if(e.getType() == EULER_EDGE || e.getType() == SPLIT_EULER_EDGE) {
						 // triple points can occur when an edge crosses a concurrent edge
						numberOfEulerEdges += e.getLabel().length();
					}
				}

				if(numberOfEulerEdges > 2) {
					count++;
				}
			}
		}
		return count;
	}



	/**
	 * Counts additional concurrecy created by adding this path to
	 * the diagram. Relies on l being a simple path, no repeated
	 * edges or nodes
	 */
	public static int countConcurrentEdges(ArrayList<Edge> path) {

		int count = 0;
		for(Edge e : path) {
			if(e.getType() == EULER_EDGE || e.getType() == SPLIT_EULER_EDGE) {
				count++;
			}
		}
		return count;
	}


	/**
	 * Counts the number of times this path touches or crosses a Euler graph
	 * node or edge.
	 */
	public static int countEulerIntersections(ArrayList<Edge> path,DualGraph dg) {

		ArrayList<String> nodeLabels = new ArrayList<String>();
		for(Edge e : path) {
			if(!nodeLabels.contains(e.getTo().getLabel())) {
				nodeLabels.add(e.getTo().getLabel());
			}
			if(!nodeLabels.contains(e.getFrom().getLabel())) {
				nodeLabels.add(e.getFrom().getLabel());
			}
		}
		int count = 0;
		for(String s : nodeLabels) {
			Node n = dg.firstNodeWithLabel(s);
			
			if(n.getType() == EULER_NODE) {
				count++;
			} else if(n.getType() == NEW_NODE) {
				// test to see if the new node is on an edge of the Euler graph
				for(Edge e : n.connectingEdges()) {
					if(e.getType() == EULER_EDGE || e.getType() == SPLIT_EULER_EDGE) {
						count++;
						break;
					}
				}
			}
		}
		return count;
	}



	public void outputCountData(ArrayList<Edge> path) {
		System.out.println(path);
		System.out.println("    concurrency count: "+countConcurrentEdges(path));
		System.out.println("             tp count: "+countTriplePoints(path,largeGraph));
		System.out.println("         length count: "+path.size());
		System.out.println("Euler intersect count: "+countEulerIntersections(path,largeGraph));
		
	}

	public void findGraphFromLargeGraph() {

		clear();
		Graph clonedGraph = largeGraph.clone();			
		
		label = clonedGraph.getLabel();
		nodes = clonedGraph.getNodes();
		edges = clonedGraph.getEdges();
		
		scale(1.0/SCALE);

	}

	
	/**
	 * Find middle triangle, then find biggest containing rectangle.
	 */
	public static Point findMiddlePointInsidePolygon(Polygon polygon) {
		
//		Rectangle bounds = polygon.getBounds();
//		int targetCentreX = bounds.x + bounds.width/2;
//		int targetCentreY = bounds.y + bounds.height/2;
		Point2D.Double pCentroid = euler.Util.computePolygonCentroid(polygon);
		int targetCentreX = pjr.graph.Util.convertToInteger(pCentroid.getX());
		int targetCentreY = pjr.graph.Util.convertToInteger(pCentroid.getY());

		double lastX = Double.MAX_VALUE;
		double lastY = Double.MAX_VALUE;
		PolygonTriangulator bt = new PolygonTriangulator();
		for(int i = 0 ; i < polygon.npoints; i++) {
			double x = polygon.xpoints[i];
			double y = polygon.ypoints[i];
			if(x == lastX && y == lastY) {
				// sometimes points are duplicated
				continue;
			}
			bt.addPolyPoint(x, y);		
			lastX = x;
			lastY = y;
		}
		ArrayList<Point2D> tris = bt.getTris();
//System.out.println(tris+" "+toString());
		double minDistance = Double.MAX_VALUE;
		Point triangleMiddle = new Point(0,0);

		if(tris!=null){
			for(int j = 0 ; j < tris.size(); j+=3){
				Point2D t1 = tris.get(j);
				Point2D t2 = tris.get(j+1);
				Point2D t3 = tris.get(j+2);
				
				Point2D.Double centroid = new Point2D.Double((t1.getX()+t2.getX()+t3.getX())/3,(t1.getY()+t2.getY()+t3.getY())/3);

				double distance = pjr.graph.Util.distance(targetCentreX, targetCentreY, centroid.getX(), centroid.getY());
				if(minDistance > distance) {
					triangleMiddle = new Point(pjr.graph.Util.convertToInteger(centroid.getX()),pjr.graph.Util.convertToInteger(centroid.getY()));
					minDistance = distance;
				}

			}
		}
		

		Rectangle r = findLargestRectangleInPolygon(polygon, triangleMiddle);
		double retXD = r.getCenterX();
		double retYD = r.getCenterY();
		Point ret = new Point(pjr.graph.Util.convertToInteger(retXD),pjr.graph.Util.convertToInteger(retYD));
		return ret;
		
	}

	/**
	 * Find a rectangle inside the polygon starting at the given point,
	 * the point must be inside the polygon.
	 */
	public static Rectangle findLargestRectangleInPolygon(Polygon polygon, Point point) {

		Area area = new Area(polygon);
		final int startGrid = 1*SCALE;
		int topX = point.x;
		int topY = point.y;
		double width = 0.01; // teeny rectangle to start with
		double height = 0.01;

		int grid = startGrid;
		boolean finish = false;
		while(!finish) {
			boolean noMove = true;
			Rectangle2D.Double r;
			
			int tryTopX = topX-grid;
			r = new Rectangle2D.Double(tryTopX,topY,width,height);

			if(area.contains(r)) {
				topX = tryTopX;
				width+=grid;
				noMove = false;
			}
			
			int tryTopY = topY-grid;
			r = new Rectangle2D.Double(topX,tryTopY,width,height);
			if(area.contains(r)) {
				topY = tryTopY;
				height+=grid;
				noMove = false;
			}
			
			double tryWidth = width+grid;
			r = new Rectangle2D.Double(topX,topY,tryWidth,height);
			if(area.contains(r)) {
				width = tryWidth;
				noMove = false;
			}
			
			double tryHeight = height+grid;
			r = new Rectangle2D.Double(topX,topY,width,tryHeight);
			if(area.contains(r)) {
				height = tryHeight;
				noMove = false;
			}
			
			if(grid == 1 && noMove) {
				finish = true;
			} else if(noMove) {
				grid = grid/2;
			}

		}
		
		Rectangle ret = new Rectangle(topX,topY,pjr.graph.Util.convertToInteger(width),pjr.graph.Util.convertToInteger(height));
		return ret;
		
	}

	/**
	 * Route between the point and node. Graph must be a face graph, just a border.
	 * The point must be in the middle of the face graph, the node must be on
	 * the border of the polygon.
	 */
	public static ArrayList<Point> routeMiddleToOutside(DualGraph faceGraph, Point startPoint, Node targetNode) {
		
		faceGraph.formFaces();
		faceGraph.triangulate();
		
		
		Face f = null;
		for(Face testFace : faceGraph.getFaces()) {
			if(testFace != faceGraph.getOuterFace()) {
				f = testFace;
				break;
			}
		}
		
		for(TriangulationEdge te : faceGraph.findTriangulationEdges()) {
			if(te.getEdge() == null) {
				te.setVisited(false);
			} else {
				te.setVisited(true);
			}
		}
		
		// find faces containing the source and target TFs
		TriangulationFace sourceTF = null;
		TriangulationFace targetTF = null;
		for(TriangulationFace tf : faceGraph.getTriangulationFaces()) {
			
			// check if the TF is inside the required face
			Point tfCentroid = tf.centroid();
			if(!f.getPolygon().contains(tfCentroid)) {
				continue;
			}
			
			if(tf.contains(startPoint)) {
				sourceTF = tf;
			}
			
			if(tf.getNode1() == targetNode) {targetTF = tf;}
			if(tf.getNode2() == targetNode) {targetTF = tf;}
			if(tf.getNode3() == targetNode) {targetTF = tf;}
		}
		
		// no need to do any routing if the target node is on the tf of the start point
		if(sourceTF == targetTF) {
			return new ArrayList<Point>();
		}
		
		Stack<TriangulationFace> stack = new Stack<TriangulationFace>();

		stack.push(sourceTF);
		
		boolean routeFound = false;
		while(!routeFound) {
			TriangulationFace currentTF = stack.peek();

			ArrayList<TriangulationEdge> unvisitedEdges = currentTF.getUnvisitedTEs();
			if(unvisitedEdges.size() == 0) {
				stack.pop();
			} else {
				TriangulationEdge nextTE = unvisitedEdges.get(0);
				nextTE.setVisited(true);
				TriangulationFace nextTF = nextTE.getTriangulationFaceList().get(0);
				if(nextTF == currentTF) {
					nextTF = nextTE.getTriangulationFaceList().get(1);
				}
				stack.push(nextTF);
			}

			if(stack.peek() == targetTF) {
				routeFound = true;
			}
		}
//System.out.println("stack"+stack);
		
		ArrayList<Point> ret = new ArrayList<Point>();

		// route the points through the TEs
		TriangulationFace lastTF = null;
		for(TriangulationFace currentTF : stack) {
			if(lastTF != null) {
				TriangulationEdge te = currentTF.findJoiningTE(lastTF);
				Point middleOfTE = pjr.graph.Util.midPoint(te.getFrom().getCentre(), te.getTo().getCentre());
				ret.add(middleOfTE);
			}
			lastTF = currentTF;
			
		}

//DualGraphWindow dgw = new DualGraphWindow(searchGraph);
//dgw.getDiagramPanel().setShowTriangulation(true);
		
		return ret;
	}


	
	public void drawWithSpringEmbedder() {
		

		createMultiGraph();
		
//		ptfl.setAnimateFlag(false);

		multiGraph.triangulate();
		
		// instantiate the triangulated edges to real edges
		for(TriangulationEdge te : multiGraph.findTriangulationEdges()) {
			if(te.getEdge() == null) {
				Edge multiEdge = new Edge(te.getFrom(),te.getTo(),te.getLabel());
				multiGraph.addEdge(multiEdge);
			}
		}

		// find the clone to dual node mappings to ensure that the
		// clone can be set to dual after drawing
		HashMap<Node,Node> cloneToLargeMap = new HashMap<Node,Node>();
		for(Node largeNode : largeGraph.getNodes()) {
			Node cloneNode = multiGraph.closestNode(largeNode.getCentre());
			cloneToLargeMap.put(cloneNode,largeNode);
		}
		
		PlanarForceLayout pfl = new PlanarForceLayout();
		
//uncomment this for animation	
/*DiagramPanel dp = new DiagramPanel(multiGraph);
new DualGraphWindow(dp);
pfl.setDiagramPanel(dp);
*/		
		pfl.setDualGraph(multiGraph);
		pfl.setTimeLimit(2000);
//		pfl.setTimeLimit(10000);
		
		// TODO scale the constants to allow for largeGraph
		
		pfl.drawGraph();
		

		for(Node multiN : multiNodeMap.keySet()) {
			Node largeN = multiNodeMap.get(multiN);
			largeN.setCentre(multiN.getCentre());

// map the bends from the multi edges
			ArrayList<Edge> startMultiEdges = multiN.getEdgesFrom();
			for(Edge largeE : largeN.getEdgesFrom()) {
				Edge currentMultiEdge = null;
				for(Edge testMultiEdge : startMultiEdges) {
					if(multiEdgeMap.get(testMultiEdge) == largeE) {
						currentMultiEdge = testMultiEdge;
						break;
					}
				}
				Node currentMultiNode = currentMultiEdge.getOppositeEnd(multiN);
				ArrayList<Point> bends = new ArrayList<Point>();
				while(currentMultiNode.getType() == POLY_EULER_NODE || currentMultiNode.getType() == POLY_DUAL_NODE || currentMultiNode.getType() == POLY_NEW_NODE || currentMultiNode.getType() == POLY_NODE_TYPE) {
					bends.add(currentMultiNode.getCentre());
					for(Edge e :currentMultiNode.connectingEdges()) {
						if(e != currentMultiEdge) { // should only ever be two edges connecting to a poly node
							currentMultiEdge = e;
							break;
						}
					}
					currentMultiNode = currentMultiEdge.getOppositeEnd(currentMultiNode);
					
				}
				largeE.setBends(bends);
			}
		}


		findGraphFromLargeGraph();

	}
	
	
	protected static void errorMessage(String message) {
		errorOutput = true;
		System.out.println(message);
		JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.PLAIN_MESSAGE);
	}

	
	protected static void panelMessage(DiagramPanel panel, String message) {
		Graphics2D g2 = (Graphics2D)panel.getGraphics();
		panel.update(g2);
/*		
Rectangle2D.Double bounds = new Rectangle2D.Double(20,20,20,20);
g2.setColor(Color.RED);
g2.fill(bounds);
*/

		Font font = new Font("Arial",Font.BOLD,30);
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout labelLayout = new TextLayout(message, font, frc);
		
		Rectangle2D bounds = labelLayout.getBounds();

		int x = panel.getWidth()/2-(int)bounds.getWidth()/2;
		int y = panel.getHeight()/2-(int)bounds.getHeight()/2;

		g2.setColor(Color.YELLOW);
		bounds.setRect(bounds.getX()+x-10, bounds.getY()+y-10, bounds.getWidth()+20,bounds.getHeight()+20);
		g2.fill(bounds);

		g2.setColor(Color.BLACK);
		labelLayout.draw(g2,x,y);

	}
	
	
}

