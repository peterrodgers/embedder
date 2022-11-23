package euler.inductive;

import pjr.graph.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import euler.*;
import euler.drawers.*;

/**
 * A version of Eades spring embedder for laying out 
 * planar graphs without changing the planarity and faces
 * @author Peter Rodgers
 * @author Leishi Zhang
 */

public class DiagramDrawerEulerGraphForce extends DiagramDrawer {
	
	public static int DRAWNGRAPHPADDING = 100;

	/** Trivial constructor. */
	public DiagramDrawerEulerGraphForce() {
		super(KeyEvent.VK_S, "Spring Embedder on Dual Graph",KeyEvent.VK_S);
	}

	/** Trivial constructor. */
	public DiagramDrawerEulerGraphForce(int key, String s, int mnemomic) {
		super(key,s,mnemomic);
	}


	
	/** Draws the graph. */
	public void layout() {
		
		drawDualGraphWithTriangulationForce(getDualGraph(),getDiagramPanel(),false);
		
		if(getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}
	
	
	public static void drawDualGraphWithTriangulationForce(DualGraph dg, DiagramPanel diagramPanel, boolean animateFlag) {

		if(diagramPanel != null) {
			HybridGraph.panelMessage(diagramPanel,"Applying spring embedder");
		}
		
		PlanarForceLayout pfl = new PlanarForceLayout();
		
		DualGraph cloneGraph = dg.clone();
		
		// replace the bend points by nodes
		HashMap<Edge,ArrayList<Node>> dualEdgeToNodeListMap = new HashMap<Edge, ArrayList<Node>>(); // stores the node list that replaces the bend points of the edge
		for(Edge dualGraphEdge : dg.getEdges()) {
			Edge e = cloneGraph.getMatchingEdgeList(dualGraphEdge).get(0);
			ArrayList<Point> bends = e.getBends();
			if(bends.size() > 0) {
				ArrayList<Node> nodeList = new ArrayList<Node>();
				String edgeLabel = e.getLabel();
				Node prev = e.getFrom();
				for(Point p : bends) {
					Node newNode = new Node("",HybridGraph.POLY_EULER_NODE,p);
					cloneGraph.addNode(newNode);
					nodeList.add(newNode);
					Edge newEdge = new Edge(prev,newNode,edgeLabel,HybridGraph.POLY_EDGE_TYPE);
					cloneGraph.addEdge(newEdge);
					prev = newNode;
				}
				dualEdgeToNodeListMap.put(dualGraphEdge,nodeList);
				Edge newEdge = new Edge(prev,e.getTo(),edgeLabel,HybridGraph.POLY_EDGE_TYPE);
				cloneGraph.addEdge(newEdge);
			}
		}

		// find the edges that need removing
		ArrayList<Edge> removeEdges = new ArrayList<Edge>();
		for(Edge e : cloneGraph.getEdges()) {
			if(e.getBends().size() > 0) {
				removeEdges.add(e);
			}
		}
		for(Edge e : removeEdges) {
			cloneGraph.removeEdge(e);
		}
		
		cloneGraph.formFaces();
		Face outerFace = cloneGraph.getOuterFace();
		ArrayList<Node> outerNodes;
		if(outerFace == null) {
			// assume that there is no outer face because the dual forms a straight
			// line or is empty
			outerNodes = cloneGraph.getNodes();
		} else {
			outerNodes = outerFace.getNodeList();
		}
		ConcreteDiagram.addBoundingNodes(cloneGraph,ConcreteDiagram.OUTER_FACE_TRIANGULATION_BOUNDARY,outerNodes);

		// take out the bounding nodes and edges to allow triangulation
		ArrayList<Node> removedNodes = GeneralConcreteDiagram.getBoundingNodes(cloneGraph);
		ArrayList<Edge> removedEdges = GeneralConcreteDiagram.getBoundingEdges(cloneGraph);
		ConcreteDiagram.removeBoundingNodes(cloneGraph);
		
		cloneGraph.formFaces();
		Face cloneOuterFace = cloneGraph.getOuterFace();
		cloneGraph.triangulate();
		
		// add the bounding nodes and edges back in
		for(Node n : removedNodes) {
			cloneGraph.addNode(n);
		}
		for(Edge e : removedEdges) {
			cloneGraph.addEdge(e);
		}
		ConcreteDiagram.triangulateBoundingFace(cloneGraph,cloneOuterFace);

		// instantiate the triangulated edges to real edges
		for(TriangulationEdge te : cloneGraph.findTriangulationEdges()) {
			if(te.getEdge() == null) {
				Edge dualEdge = new Edge(te.getFrom(),te.getTo(),te.getLabel());
				cloneGraph.addEdge(dualEdge);
			}
		}
		

		
		// find the clone to dual node mappings to ensure that the
		// dual nodes can be set to clone nodes after drawing
		HashMap<Node,Node> cloneToDualNodeMap = new HashMap<Node,Node>();
		for(Node dualNode : dg.getNodes()) {
			Node cloneNode = cloneGraph.closestNode(dualNode.getCentre());
			cloneToDualNodeMap.put(cloneNode,dualNode);
		}
		
		pfl.setDualGraph(cloneGraph);
		pfl.setTimeLimit(3000);
		pfl.setAnimateFlag(animateFlag);
		
		
		pfl.drawGraph();
		
		
		// reset the centres of the dualNodes
		for(Node cloneNode : cloneToDualNodeMap.keySet()) {
			Node dualNode = cloneToDualNodeMap.get(cloneNode);
			dualNode.setCentre(cloneNode.getCentre());
		}
		
		// reset the dual bend points
		for(Edge dualEdge : dualEdgeToNodeListMap.keySet()) {
			ArrayList<Node> cloneNodeList = dualEdgeToNodeListMap.get(dualEdge);
			ArrayList<Point> bends = new ArrayList<Point>();
			for(Node cloneNode : cloneNodeList) {
				bends.add(cloneNode.getCentre());
			}
			dualEdge.setBends(bends);
		}
		
		if(diagramPanel != null) {
			// use the original diagram panel to set the size of the drawing
			diagramPanel.fitGraphInPanel(DRAWNGRAPHPADDING);
		}
		

		
	}

	

}
