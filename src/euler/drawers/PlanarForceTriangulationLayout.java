package euler.drawers;

import java.util.*;
import euler.*;
import pjr.graph.*;
/**
 * Lay out a graph by triangulating it first.
 *
 * @author Peter Rodgers
 */
public class PlanarForceTriangulationLayout {

	PlanarForceLayout pfl = new PlanarForceLayout();
	DiagramPanel diagramPanel = null;
	ConcreteDiagram concreteDiagram = null;
	
	/** Trivial constructor. */
	public PlanarForceTriangulationLayout() {
	}
	
	/** Trivial constructor. */
	public PlanarForceTriangulationLayout(ConcreteDiagram cd) {
		concreteDiagram = cd;
	}
	
	/** Trivial constructor. */
	public PlanarForceTriangulationLayout(DiagramPanel dp) {
		diagramPanel = dp;
		concreteDiagram = dp.getConcreteDiagram();
	}
	

	public long getTimeLimit() {return pfl.getTimeLimit();}
	public long getTime() {return pfl.getTime();}
	public double getQ() {return pfl.getQ();}
	public double getK() {return pfl.getK();}
	public double getR() {return pfl.getR();}
	public double getF() {return pfl.getF();}
	public boolean getAnimateFlag() {return pfl.getAnimateFlag();}
	public int getBorderLimit() {return pfl.getBorderLimit();}
	public boolean getUseBorderLimit() {return pfl.getUseBorderLimit();}
	public int getIterations() {return pfl.getIterations();}
	public ConcreteDiagram getConcreteDiagram() {return concreteDiagram;}
	public DiagramPanel getDiagramPanel() {return diagramPanel;}
	
	public void setTimeLimit(int inT) {pfl.setTimeLimit(inT);}
	public void setQ(double inQ) {pfl.setQ(inQ);}
	public void setK(double inK) {pfl.setK(inK);}
	public void setR(double inR) {pfl.setR(inR);}
	public void setF(double inF) {pfl.setF(inF);}
	public void setAnimateFlag(boolean inAnimateFlag) {pfl.setAnimateFlag(inAnimateFlag);}
	public void setBorderLimit(int limit) {pfl.setBorderLimit(limit);}
	public void setUseBorderLimit(boolean flag) {pfl.setUseBorderLimit(flag);}
	public void setIterations(int iterations) {pfl.setIterations(iterations);}
	public void setConcreteDiagram(WellFormedConcreteDiagram cd) {concreteDiagram = cd;}
	
	public void setDiagramPanel(DiagramPanel dp) {
		diagramPanel = dp;
		if(dp != null) {
			concreteDiagram = dp.getConcreteDiagram();
		}
	}

	/**
	 * Draws the graph. This needs a diagram panel or dual graph to be
	 * set before drawing.
	 */
	public void drawGraph() {
		if(concreteDiagram == null) {return;}
		DualGraph cloneGraph = concreteDiagram.getCloneGraph();
		DualGraph dualGraph = concreteDiagram.getDualGraph();
		if(cloneGraph == null) {return;}
		drawGraphBasedOnClone(dualGraph,cloneGraph);
	}

	
	public void drawGraphBasedOnClone(DualGraph dualGraph,DualGraph cloneGraph) {
		
		// add the border nodes
		ArrayList<Node> addedNodes = new ArrayList<Node>();
		for(Node cloneNode : cloneGraph.getNodes()) {
			if(cloneNode.getType() == WellFormedConcreteDiagram.outerNodeType) {
				Node dualNode = new Node(cloneNode.getCentre());
				dualNode.setType(WellFormedConcreteDiagram.outerNodeType);
				dualGraph.addNode(dualNode);
				addedNodes.add(dualNode);
			}
		}
		
		ArrayList<Edge> addedEdges = new ArrayList<Edge>();
		// add the border edges
		for(Edge cloneEdge : cloneGraph.getEdges()) {
			if(cloneEdge.getType() == WellFormedConcreteDiagram.outerEdgeType) {
				Node dualFromNode = dualGraph.closestNode(cloneEdge.getFrom().getCentre());
				Node dualToNode = dualGraph.closestNode(cloneEdge.getTo().getCentre());
				Edge dualEdge = new Edge(dualFromNode,dualToNode);
				dualEdge.setType(WellFormedConcreteDiagram.outerEdgeType);
				dualGraph.addEdge(dualEdge);
				addedEdges.add(dualEdge);
			}
		}
		
		// instantiate the triangulated edges to real edges
		for(TriangulationEdge te : cloneGraph.findTriangulationEdges()) {
			if(te.getEdge() == null) {
				Node dualFromNode = dualGraph.closestNode(te.getFrom().getCentre());
				Node dualToNode = dualGraph.closestNode(te.getTo().getCentre());
				Edge dualEdge = new Edge(dualFromNode,dualToNode,te.getLabel());
				dualGraph.addEdge(dualEdge);
				addedEdges.add(dualEdge);
			}
		}

		
		// find the clone to dual node mappings to ensure that the
		// clone can be set to dual after drawing
		HashMap<Node,Node> cloneToDualMap = new HashMap<Node,Node>();
		for(Node dualNode : dualGraph.getNodes()) {
			Node cloneNode = cloneGraph.closestNode(dualNode.getCentre());
			cloneToDualMap.put(cloneNode,dualNode);
		}
		
		pfl.setDiagramPanel(getDiagramPanel());
		pfl.setDualGraph(dualGraph);
		setAnimateFlag(false);
		
		pfl.drawGraph();
		
		if(getDiagramPanel() != null) {
			// use the original diagram panel to set the size of the drawing
			getDiagramPanel().fitGraphInPanel(0);
		}
		
		// reset the centres of the cloneNodes
		for(Node cloneNode : cloneToDualMap.keySet()) {
			Node dualNode = cloneToDualMap.get(cloneNode);
			cloneNode.setCentre(dualNode.getCentre());
		}
		
		// remove the added edges and nodes
		dualGraph.removeEdges(addedEdges);
		dualGraph.removeNodes(addedNodes);
		
		if(!getAnimateFlag() && getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}
	

}
