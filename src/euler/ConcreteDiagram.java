package euler;

import java.awt.*;
import java.awt.geom.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import pjr.graph.*;

public abstract class ConcreteDiagram {
	
	public static NodeType outerNodeType = new NodeType("outer");
	public static EdgeType outerEdgeType = new EdgeType("outer");	
	protected boolean optimizeContourAngles;
	protected boolean optimizeMeetingPoints = false;
	protected boolean fitCircles;
	public static final int PARALLEL_FUDGE_FACTOR = 4; // how close in degrees lines have to be to be parallel
	public static final int OUTER_FACE_TRIANGULATION_BOUNDARY = 100;
	public static final int CP_OPTIMIZATION_ITERATIONS = 50;
	/** minimum gap between separate contours through TEs, as a percentage of edge length. */
	public static final double CONTOUR_GAP_PERCENT = 0.3;
	public static final int CONCURRENT_OFFSET = 2;
	/** how much gap between concurrent contours*/
	protected int concurrentOffset = CONCURRENT_OFFSET;
	protected DualGraph dualGraph;
	protected ArrayList<ConcreteContour> concreteContours;
	/** This is to allow access to the full triangulation for visualization */
	protected DualGraph cloneGraph;
	
	
	public ConcreteDiagram(DualGraph dualGraph) {
		this.dualGraph = dualGraph;			
	}	
	
	public ConcreteDiagram(ArrayList<ConcreteContour> ccs){
		concreteContours = ccs;		
	}
	
	public DualGraph getDualGraph() {return dualGraph;}
	public DualGraph getCloneGraph() {return cloneGraph;}
	public ArrayList<ConcreteContour> getConcreteContours() {return concreteContours;}
	public int getConcurrentOffset() {return concurrentOffset;}
	public boolean getOptimizeContourAngles() {return optimizeContourAngles;}
	public boolean getFitCircles() {return fitCircles;}
	public boolean getOptimizeMeetingPoints() {return optimizeMeetingPoints;}
	public void setConcurrentOffset(int offset) {this.concurrentOffset = offset;}
	public void setOptimizeContourAngles(boolean flag) {optimizeContourAngles = flag;}
	public void setFitCircles(boolean flag) {fitCircles = flag;}
	public void setOptimizeMeetingPoints(boolean flag) {optimizeMeetingPoints = flag;}


	/**
	 * This must be called before accessing concrete contours.
	 */
	public abstract void generateContours();
	/**
	 * Assumes a triangulation, assigns order to the TEs and builds contours
	 */
	public abstract void routeContours();

	
	/**
	 * For each contour in the diagram, build the concrete contour.
	 * The ContourPoint connectivity needs to be assigned before
	 * calling this.
	 */
	public void buildContours() {
//DiagramPanel.lineList = new ArrayList<Point>();
		concreteContours = new ArrayList<ConcreteContour>();

		for(String contour : cloneGraph.findAbstractDiagram().getContours()) {
			
			int i = (int)contour.charAt(0) - (int)'a';
			if(i < 0) {i = 0-i;} // capitals give a negative i
			
			Polygon polygon = buildPolygon(contour);

			ConcreteContour cc = new ConcreteContour(contour,polygon);
			
			concreteContours.add(cc);
			
		}
	}
	
	public void setConcreteContours(ArrayList<ConcreteContour> contours){
		concreteContours = contours;
	}
	
	/**
	 * Builds a polygon from the contour links for the contour. The
	 * ContourLink connectivity needs to be assigned before calling this.
	 */
	public Polygon buildPolygon(String contour) {
		TriangulationEdge te = cloneGraph.firstTriangulationEdgeWithContour(contour);

		// we don't add the first CP to the polygon
		ContourLink startCL = te.contourLinksWithContour(contour).get(0);
		ContourLink currentCL = null;
		Polygon polygon = new Polygon();
		
		while(startCL != currentCL) {
			if(currentCL == null) {
				currentCL = startCL;
			}
			
			currentCL = currentCL.getNext();
			
			if(currentCL == null) {
				// should never get here
				System.out.println("currentCL is null in buildPolygon(\""+contour+"\")");
				return null;
			}
			if(currentCL.getNext() == null) {
				// should never get here
				System.out.println("currentCL.getNext() is null in buildPolygon("+contour+")");
				return null;
			}
			if(currentCL.getPrev() == null) {
				// should never get here
				System.out.println("currentCL.getPrev() is null in buildPolygon("+contour+")");
				return null;
			}
			
			CutPoint currentCP = currentCL.getCutPoint();
						
			TriangulationEdge currentTE = currentCP.getTriangulationEdge();
			
			int newX = currentCP.getCoordinate().x;
			int newY = currentCP.getCoordinate().y;
			
			Point2D.Double cpCoord = new Point2D.Double(newX,newY);
			
// case where the cp is a middle point, in which case
// its the last awayFromNode, from the prev (or could be next)
			Point2D.Double[] prevLine = null;
			Point2D.Double[] nextLine = null;
			if(currentTE != null) {
				// can use the current CL for the first point as it has a TE
				prevLine = findOffsetLine(currentCL,currentCL.getPrev());
				nextLine = findOffsetLine(currentCL,currentCL.getNext());
			} else {
				// if the current CL has no TE, the prev and next must have
				// because the CL is on in the middle of a face
				prevLine = findOffsetLine(currentCL.getPrev(),currentCL);
				nextLine = findOffsetLine(currentCL.getNext(),currentCL);
			}
			
			Point2D.Double prevLine1 = prevLine[0];
			Point2D.Double prevLine2 = prevLine[1];
			Point2D.Double nextLine1 = nextLine[0];
			Point2D.Double nextLine2 = nextLine[1];
			
			Point prev1I = new Point(pjr.graph.Util.convertToInteger(prevLine1.x),pjr.graph.Util.convertToInteger(prevLine1.y));
			Point prev2I = new Point(pjr.graph.Util.convertToInteger(prevLine2.x),pjr.graph.Util.convertToInteger(prevLine2.y));
			Point next1I = new Point(pjr.graph.Util.convertToInteger(nextLine1.x),pjr.graph.Util.convertToInteger(nextLine1.y));
			Point next2I = new Point(pjr.graph.Util.convertToInteger(nextLine2.x),pjr.graph.Util.convertToInteger(nextLine2.y));

			Point2D lineIntersection = null;
			//if(pjr.graph.Util.linesNearlyParallel(prev1I,prev2I,next1I,next2I,PARALLEL_FUDGE_FACTOR)) {
			if(pjr.graph.Util.linesNearlyParallel(prevLine1,prevLine2,nextLine1,nextLine2,PARALLEL_FUDGE_FACTOR)) {
				// parallel lines, these should merge into each other, so
				// intersection point just has to be on one line, but
				// for neatness put it at the end of the prev line that is closest to
				// the cut point
				if(pjr.graph.Util.distance(prevLine1, cpCoord) < pjr.graph.Util.distance(prevLine2, cpCoord)) {
					lineIntersection = prev1I;
				} else {
					lineIntersection = prev2I;
				}
			} else {

				lineIntersection = pjr.graph.Util.intersectionPointOfTwoLines(prev1I, prev2I, next1I, next2I);
			}

			newX = pjr.graph.Util.convertToInteger(lineIntersection.getX());
			newY = pjr.graph.Util.convertToInteger(lineIntersection.getY());
			
		
			polygon.addPoint(newX,newY);
		}
		
		return polygon;
	}



	/** Find a line between the two cls, with an offset depending on
	 * the number of other contours of lower alphabet labels that
	 * are between this contour and the node the contour is wrapping.
	 * The currentCL must be a contourlink of a CP on a TE
	 */
	private Point2D.Double[] findOffsetLine(ContourLink cl1, ContourLink connectingCL) {
		
		CutPoint cp1 = cl1.getCutPoint();
		TriangulationEdge te1 = cp1.getTriangulationEdge();
		String contour = cl1.getContour();
		// offset away from the node with the contour label in
		// offset away from the node with the contour label in
		Node awayFromNode = te1.getFrom();
		Node towardsNode = te1.getTo();
		if(te1.getTo().getLabel().contains(contour)) {
			awayFromNode = te1.getTo();
			towardsNode = te1.getFrom();
		}
		
		CutPoint cp2 = connectingCL.getCutPoint();
		
		Point line1 = cp2.getCoordinate();
		Point line2 = cp1.getCoordinate();

		// offset proportional to the number of contours that need to be closer to the node
		String awayFromShared = cp1.findCommonContourLabels(awayFromNode.getLabel());
		String towardsShared = cp1.findCommonContourLabels(towardsNode.getLabel());
		int offsetMultiplier = awayFromShared.indexOf(contour);
		if(towardsShared.length() > 0 && awayFromShared.length() > 0) {
			// might need to add one to the diff to prevent the first
			// two contours in awayfrom and towards overlapping
			if(awayFromShared.charAt(0) > towardsShared.charAt(0)) {
				// here all contours in node "bef" have one added to them
				// if the other node is "acd"
				offsetMultiplier++;
			}

		}
		// overlap of first 
		//int offsetMultiplier = currentCP.getContourLinks().indexOf(currentCL);
		int scaledOffset = concurrentOffset*offsetMultiplier;
		
		Point awayFromPoint = awayFromNode.getCentre();
		Point2D.Double awayFromPointD = new Point2D.Double(awayFromPoint.x,awayFromPoint.y);

		double lineAngle = pjr.graph.Util.calculateAngle(line1.x, line1.y, line2.x, line2.y);
		double linePerpAngle = lineAngle+90;
		Point2D.Double correctedLine1 = pjr.graph.Util.movePoint(line1,scaledOffset,linePerpAngle);
		Point2D.Double correctedLine2 = pjr.graph.Util.movePoint(line2,scaledOffset,linePerpAngle);
		if(pjr.graph.Util.distance(correctedLine1,awayFromPointD) < pjr.graph.Util.distance(line1,awayFromPoint)) {
			// make sure the new line is away from the node
			correctedLine1 = pjr.graph.Util.movePoint(line1,-scaledOffset,linePerpAngle);
			correctedLine2 = pjr.graph.Util.movePoint(line2,-scaledOffset,linePerpAngle);
		}

		Point2D.Double[] ret = new Point2D.Double[2];
		ret[0] = correctedLine1;
		ret[1] = correctedLine2;
		
		return ret;

	}

	/**
	 * Deletes the CPs from the TEs so that the TE order can be reassigned
	 */
	public void resetTriangulationEdges() {
		ArrayList<TriangulationEdge> teList = cloneGraph.findTriangulationEdges();
		ArrayList<TriangulationFace> tfList = cloneGraph.getTriangulationFaces();

		for (TriangulationEdge te : teList) {
			te.setCutPoints(null);
		}
		for (TriangulationFace tf : tfList) {
			tf.setMeetingPoint(null);
		}
	}

		
	/**
	 * Add extra nodes and edges for outer edge triangulation. Call @link removeBoundingNodes
	 * to restore the graph after triangulation.
	 * 
	 * NOTE that to get the correct crossing order for the newly created
	 * outer faces @link repairOuterFaces needs to be called on this graph
	 * after @link Graph.formFaces is called.
	 * 
	 * @return the bounding nodes
	 */
	public static ArrayList<Node> addBoundingNodes(DualGraph dg, int padding, ArrayList<Node> outerFaceNodes) {

		ArrayList<Node> ret = new ArrayList<Node>();
		
		int x1 = dg.findMinimumX()-padding;
		int y1 = dg.findMinimumY()-padding;
		int x2 = dg.findMinimumX()+dg.findWidth()+padding;
		int y2 = dg.findMinimumY()+dg.findHeight()+padding;

		Node topLeftNode = new Node(new Point(x1,y1));
		topLeftNode.setType(outerNodeType);
		dg.addNode(topLeftNode);
		ret.add(topLeftNode);
		
		Node topNode1 = new Node(new Point(x1+(x2-x1)/4,y1));
		topNode1.setType(outerNodeType);
		dg.addNode(topNode1);
		ret.add(topNode1);
		
		Node topNode2 = new Node(new Point(x1+2*(x2-x1)/4,y1));
		topNode2.setType(outerNodeType);
		dg.addNode(topNode2);
		ret.add(topNode2);
		
		Node topNode3 = new Node(new Point(x1+3*(x2-x1)/4,y1));
		topNode3.setType(outerNodeType);
		dg.addNode(topNode3);
		ret.add(topNode3);
		
		Node topRightNode = new Node(new Point(x2,y1));
		topRightNode.setType(outerNodeType);
		dg.addNode(topRightNode);
		ret.add(topRightNode);
		
		Node rightNode1 = new Node(new Point(x2,y1+(y2-y1)/4));
		rightNode1.setType(outerNodeType);
		dg.addNode(rightNode1);
		ret.add(rightNode1);
		
		Node rightNode2 = new Node(new Point(x2,y1+2*(y2-y1)/4));
		rightNode2.setType(outerNodeType);
		dg.addNode(rightNode2);
		ret.add(rightNode2);
		
		Node rightNode3 = new Node(new Point(x2,y1+3*(y2-y1)/4));
		rightNode3.setType(outerNodeType);
		dg.addNode(rightNode3);
		ret.add(rightNode3);
		
		Node bottomRightNode = new Node(new Point(x2,y2));
		bottomRightNode.setType(outerNodeType);
		dg.addNode(bottomRightNode);
		ret.add(bottomRightNode);
		
		Node bottomNode1 = new Node(new Point(x1+3*(x2-x1)/4,y2));
		bottomNode1.setType(outerNodeType);
		dg.addNode(bottomNode1);
		ret.add(bottomNode1);
		
		Node bottomNode2 = new Node(new Point(x1+2*(x2-x1)/4,y2));
		bottomNode2.setType(outerNodeType);
		dg.addNode(bottomNode2);
		ret.add(bottomNode2);
		
		Node bottomNode3 = new Node(new Point(x1+(x2-x1)/4,y2));
		bottomNode3.setType(outerNodeType);
		dg.addNode(bottomNode3);
		ret.add(bottomNode3);
		
		Node bottomLeftNode = new Node(new Point(x1,y2));
		bottomLeftNode.setType(outerNodeType);
		dg.addNode(bottomLeftNode);
		ret.add(bottomLeftNode);
		
		Node leftNode1 = new Node(new Point(x1,y1+3*(y2-y1)/4));
		leftNode1.setType(outerNodeType);
		dg.addNode(leftNode1);
		ret.add(leftNode1);
		
		Node leftNode2 = new Node(new Point(x1,y1+2*(y2-y1)/4));
		leftNode2.setType(outerNodeType);
		dg.addNode(leftNode2);
		ret.add(leftNode2);
		
		Node leftNode3 = new Node(new Point(x1,y1+(y2-y1)/4));
		leftNode3.setType(outerNodeType);
		dg.addNode(leftNode3);
		ret.add(leftNode3);

		
		Edge e;
		
		e = new Edge(topLeftNode,topNode1,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(topNode1,topNode2,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(topNode2,topNode3,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(topNode3,topRightNode,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(topRightNode,rightNode1,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(rightNode1,rightNode2,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(rightNode2,rightNode3,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(rightNode3,bottomRightNode,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(bottomRightNode,bottomNode1,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(bottomNode1,bottomNode2,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(bottomNode2,bottomNode3,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(bottomNode3,bottomLeftNode,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(bottomLeftNode,leftNode1,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(leftNode1,leftNode2,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);

		e = new Edge(leftNode2,leftNode3,"");
		e.setType(outerEdgeType);
		dg.addEdge(e);
		
		e = new Edge(leftNode3,topLeftNode,"");		
		e.setType(outerEdgeType);
		dg.addEdge(e);


		if(outerFaceNodes == null || outerFaceNodes.size() == 0) {
			// if there is no nearest node possible, leave here
			// this should be due to an empty dg before the
			// addition of the new nodes.
			return ret;
		}
		
		return ret;

	}
	


	/**
	 * Remove nodes and edges created for outer edge triangulation.
	 */
	public static ArrayList<Node> removeBoundingNodes(DualGraph dg) {
		
		ArrayList<Edge> removeEdges = new ArrayList<Edge>();
		for(Edge e : dg.getEdges()) {
			if(e.getType() == outerEdgeType) {
				removeEdges.add(e);
			}
		}
		dg.getEdges().removeAll(removeEdges);
		
		ArrayList<Node> removeNodes = new ArrayList<Node>();
		for(Node n : dg.getNodes()) {
			if(n.getType() == outerNodeType) {
				removeNodes.add(n);
			}
		}
		dg.getNodes().removeAll(removeNodes);
		
		return removeNodes;
		
	}

	/**
	 * Return the edges in the bounding face.
	 */
	public static ArrayList<Edge> getBoundingEdges(DualGraph dg) {
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		// reset the TEs of edges we are going to delete
		for(Edge e : dg.getEdges()) {
			if(e.getType() == outerEdgeType) {
				edges.add(e);

			}
		}

		return edges;
	}

	/**
	 * Return the nodes in the bounding face.
	 */
	public static ArrayList<Node> getBoundingNodes(DualGraph dg) {
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		for(Node n : dg.getNodes()) {
			if(n.getType() == outerNodeType) {
				nodes.add(n);
			}
		}
		
		return nodes;
		
	}

	
	
	/**
	 * Assuming we have a connected graph that has been triangulated
	 * and then have added a connected bounding subgraph surrounding it.
	 * This triangualates the area between the bounding nodes and the
	 * original outer face nodes.
	 * 
	 * @param dg needs to have the boundary nodes and edges added.
	 * 
	 * @param outerFace is the nodes in the outer face of the original graph
	 * before the boundary nodes and edges were added.
	 */

	public static void triangulateBoundingFace(DualGraph dg, Face outerFace) {
		
		DualGraph copyGraph = dg.clone();

		ArrayList<Node> boundingNodes = new ArrayList<Node>();
		ArrayList<Node> nonBoundingNodes = new ArrayList<Node>();
		ArrayList<Node> copyMapToOuterFace = new ArrayList<Node>();
		for(Node n : copyGraph.getNodes()) {
			if(n.getType() == outerNodeType) {
				boundingNodes.add(n);
			} else {
				nonBoundingNodes.add(n);
			}
			Node originalMap = dg.firstNodeAtPoint(n.getCentre());
			if(outerFace != null && outerFace.getNodeList().contains(originalMap)) {
				copyMapToOuterFace.add(n);
			}
		}
		if(outerFace == null) {
			// all nodes should be considered to be in the outer face when
			// the outerface is null as this could be due
			// to the dual being a graph with no cycles (2venn for example)
			copyMapToOuterFace.addAll(nonBoundingNodes);
		}

		// connect the graph
		for(Node n : boundingNodes) {
			Node nearestNode = findNearestNodeWithoutCrossing(copyGraph,n.getCentre(),copyMapToOuterFace);
			Edge e = new Edge(n,nearestNode);
			e.setType(outerEdgeType);
			copyGraph.addEdge(e);
		}
		
		copyGraph.formFaces();
		//get a triangulation for the outer faces
		Face cloneOuterFace = copyGraph.getOuterFace();
		for(Face f : copyGraph.getFaces()) {
			if(f == cloneOuterFace) {
				// dont want the outside face triangulated
				continue;
			}
			// only want the faces in the boundary of the clone graph
			boolean isBoundaryFace = false;
			for(Node n : f.getNodeList()) {
				if(n.getType() == outerNodeType) {
					isBoundaryFace = true;
					break;
				}
			}
			if(!isBoundaryFace) {
				continue;
			}
			copyGraph.triangulateFace(f);	
		}
		
		// convert the triangulation back to the original graph
		for(TriangulationFace cloneTF : copyGraph.getTriangulationFaces()) {
			
			TriangulationEdge cloneTE1 = cloneTF.getTE1();
			TriangulationEdge cloneTE2 = cloneTF.getTE2();
			TriangulationEdge cloneTE3 = cloneTF.getTE3();

			Node from1 = dg.firstNodeAtPoint(cloneTE1.getFrom().getCentre());
			Node to1 = dg.firstNodeAtPoint(cloneTE1.getTo().getCentre());
			Edge e1 = dg.getEdge(from1,to1);
			TriangulationEdge te1 = dg.findTriangulationEdge(from1,to1);
			if(te1 == null) {
				te1 = new TriangulationEdge(from1,to1,e1,outerFace);
			} else {
				te1.addFace(outerFace);
			}
			
			Node from2 = dg.firstNodeAtPoint(cloneTE2.getFrom().getCentre());
			Node to2 = dg.firstNodeAtPoint(cloneTE2.getTo().getCentre());
			Edge e2 = dg.getEdge(from2,to2);
			TriangulationEdge te2 = dg.findTriangulationEdge(from2,to2);
			if(te2 == null) {
				te2 = new TriangulationEdge(from2,to2,e2,outerFace);
			} else {
				te2.addFace(outerFace);
			}

			Node from3 = dg.firstNodeAtPoint(cloneTE3.getFrom().getCentre());
			Node to3 = dg.firstNodeAtPoint(cloneTE3.getTo().getCentre());
			Edge e3 = dg.getEdge(from3,to3);
			TriangulationEdge te3 = dg.findTriangulationEdge(from3,to3);
			if(te3 == null) {
				te3 = new TriangulationEdge(from3,to3,e3,outerFace);
			} else {
				te3.addFace(outerFace);
			}
			
			dg.addTriangulationFace(outerFace,te1,te2,te3);
			
		}
	}
	
	
	/**
	 * gets the nearest node in the list that can be connected without crossing
	 * an edge in the graph.
	 * @return the closest node that can be connected without crossing an edge, or null
	 * if there is no such node.
	 */
	public static Node findNearestNodeWithoutCrossing(DualGraph dg, Point p, ArrayList<Node> nodeList) {
		if(nodeList == null) {
			return null;
		}
		ArrayList<Node> currentList = new ArrayList<Node>(nodeList);
		while(currentList.size() != 0) {
			Node n = dg.closestNode(p,currentList);
			Point nPoint = n.getCentre();
			currentList.remove(n);
			
			boolean crossing = false;
			for(Edge e : dg.getEdges()) {
				Point from = e.getFrom().getCentre();
				Point to = e.getTo().getCentre();
				if(pjr.graph.Util.linesCross(nPoint,p,from,to)) {
					// check to see if they are just touching
					if(from.equals(nPoint)) {continue;}
					if(to.equals(nPoint)) {continue;}
					if(from.equals(p)) {continue;}
					if(to.equals(p)) {continue;}
					
					crossing = true;
					break;
				}
			}
			if(!crossing) {
				return n;
			}
		}
	
		return null;
	}
	public ArrayList<Polygon> replacePolygon(){
		ArrayList<Polygon> ret = new ArrayList<Polygon>();
		
		
		
		return ret;
	}
	
	public Polygon replaceWithCircle(Polygon pol){
		Polygon ret = new Polygon();
		
		
		
		
		
		return ret;
	}



	
	
	/**
	 * Test the polygons against the concrete diagram
	 */
	public boolean correctConcreteDiagram() {
		
		String concreteZones = ConcreteContour.generateAbstractDiagramFromList(getConcreteContours());
		String abstractZones = dualGraph.findAbstractDiagram().toString();
		if(abstractZones.indexOf("0") == -1) {
			abstractZones = "0 "+abstractZones;
		}
		
		boolean ret = concreteZones.equals(abstractZones);
		if(!ret) {
			System.out.println("FAIL returned concrete: -"+concreteZones+"- different to abstract: -"+abstractZones+"-");
		}
		return ret;
	
	}
	
	


	/**
	 * Find the duplicate zones in the concrete contours list
	 */
	public ArrayList<String> findDuplicateZones() {
		ArrayList<String> ret = ConcreteContour.findDuplicateZones(getConcreteContours());
		return ret;
	}
	
	
	/**
	 * Find the TFs where not every crossing involves one contour.
	 * So crossings like [ab, ac, ad] are allowed, [ab, cd] are not
	 */
	public ArrayList<TriangulationFace> findIncorrectTriangulationCrossings() {
		ArrayList<TriangulationFace> ret = new ArrayList<TriangulationFace>();
		for(TriangulationFace tf : cloneGraph.getTriangulationFaces()) {
			if(!tf.correctContourCrossings()) {
				ret.add(tf);
			}
		}
		return ret;
	}
	
	
	public AbstractDiagram generateAbstractDiagramFromPolygons() {
		String zonesString = ConcreteContour.generateAbstractDiagramFromList(concreteContours);
		if(AbstractDiagram.hasDuplicatesInSortedList(AbstractDiagram.findZoneList(zonesString))) {
			System.out.println("Duplicate zones in ConcreteDiagram.generateAbstractDiagramFromPolygons: "+zonesString);
			return null;
		}

		AbstractDiagram ad = new AbstractDiagram(zonesString);
		return ad;

	}


	
	/**
	 * Scales and centers the contours so that they are maximized
	 * in the given rectangle. This does not change the
	 * aspect ratio of the graph, it is either maximized
	 * in x or y directions.
	 */
	public static void fitContoursInRectangle(ArrayList<ConcreteContour> ccs, int x1, int y1, int x2, int y2) {
		int width = findContoursWidth(ccs);
		int height = findContoursHeight(ccs);
		int rectX1 = x1;
		int rectX2 = x2;
		if(rectX1 > rectX2) {
			rectX1 = x2;
			rectX2 = x1;
		}
		int rectY1 = y1;
		int rectY2 = y2;
		if(rectY1 > rectY2) {
			rectY1 = y2;
			rectY2 = y1;
		}
		int requiredX = rectX2-rectX1;
		int requiredY = rectY2-rectY1;
		
		int centreX = rectX1 + requiredX/2;
		int centreY = rectY1 + requiredY/2;

		double xRatio = requiredX/(double)width;
		double yRatio = requiredY/(double)height;
		// scale to the ratio that will not take the graph over the limits
		double scaleFactor = xRatio;
		if(yRatio < xRatio) {
			scaleFactor = yRatio;
		}
		
		scaleContours(ccs, scaleFactor);
		centreContoursOnPoint(ccs, centreX, centreY);
		
	}
	
	
	/** The width of the contours */
	public static int findContoursWidth(ArrayList<ConcreteContour> ccs) {
		
		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		
		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				int x = p.xpoints[i];
				if(x < minX) {
					minX = x;
				}
				if(x > maxX) {
					maxX = x;
				}
			}
		}

		return maxX-minX;
	}
	
	/** The height of the contours */
	public static int findContoursHeight(ArrayList<ConcreteContour> ccs) {

		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		
		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				int y = p.ypoints[i];
				if(y < minY) {
					minY = y;
				}
				if(y > maxY) {
					maxY = y;
				}
			}
		}
		
		return maxY-minY;
	}
	
	

	/**
	* Finds the centre of the contours, based on forming a rectangle
	* around the limiting nodes and edge bends in the graph.
	*/
	public static Point findContoursCentre(ArrayList<ConcreteContour> ccs) {

		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;

		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				int x = p.xpoints[i];
				int y = p.ypoints[i];
				if(x > maxX) {
					maxX = x;
				}
				if(x < minX) {
					minX = x;
				}
				if(y > maxY) {
					maxY = y;
				}
				if(y < minY) {
					minY = y;
				}
			}
		}

		int retX = minX + (maxX - minX)/2;
		int retY = minY + (maxY - minY)/2;

		Point ret = new Point(retX,retY);
		return ret;
	}
	/**
	* Finds the centre of the contours, based on forming a rectangle
	* around the limiting nodes and edge bends in the graph.
	*/
	public static Rectangle findContoursBounds(ArrayList<ConcreteContour> ccs) {

		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;

		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				int x = p.xpoints[i];
				int y = p.ypoints[i];
				if(x > maxX) {
					maxX = x;
				}
				if(x < minX) {
					minX = x;
				}
				if(y > maxY) {
					maxY = y;
				}
				if(y < minY) {
					minY = y;
				}
			}
		}

		Rectangle ret = new Rectangle(minX,minY,maxX-minX,maxY-minY);
		return ret;
	}
	/** Centre the contours on the given point */
	public static void centreContoursOnPoint(ArrayList<ConcreteContour> ccs, int centreX, int centreY) {
		Point centre = findContoursCentre(ccs);
	
		int moveX = centreX - centre.x;
		int moveY = centreY - centre.y;
		
		moveContours(ccs,moveX,moveY);
	}
	/** Scales the contours on the contours centre. */
	public static void scaleContours(ArrayList<ConcreteContour> ccs, double multiplier) {

		if(multiplier == 0.0) {
			return;
		}

		Point centre = findContoursCentre(ccs);

		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				p.xpoints[i] = pjr.graph.Util.scaleCoordinate(p.xpoints[i],centre.x,multiplier);
				p.ypoints[i] = pjr.graph.Util.scaleCoordinate(p.ypoints[i],centre.y,multiplier);
			}
			cc.resetArea();
		}
	}	
	/** Move all the points by the values given */
	public static void moveContours(ArrayList<ConcreteContour> ccs, int moveX, int moveY) {
	
		for(ConcreteContour cc : ccs) {
			Polygon p = cc.getPolygon();
			for(int i = 0; i < p.npoints; i++) {
				p.xpoints[i] = p.xpoints[i]+moveX;
				p.ypoints[i] = p.ypoints[i]+moveY;
			}
			cc.resetArea();
		}	
	}

}
