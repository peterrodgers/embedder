package euler.drawers;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

import pjr.graph.*;

import euler.*;
import euler.display.*;

/**
 *
 * @author Peter Rodgers
 */
public class DiagramDrawerTriangulationForceWithoutEmpty extends DiagramDrawer {

	PlanarForceTriangulationLayout pftl = new PlanarForceTriangulationLayout();
	
	/** Trivial constructor. */
	public DiagramDrawerTriangulationForceWithoutEmpty() {
		super(KeyEvent.VK_0, "No Empty set, Spring Embedder on Triangulated Graph",KeyEvent.VK_0);
	}

	/** Trivial constructor. */
	public DiagramDrawerTriangulationForceWithoutEmpty(int key, String s, int mnemomic) {
		super(key,s,mnemomic);
	}

	public long getTimeLimit() {return pftl.getTimeLimit();}
	public long getTime() {return pftl.getTime();}
	public double getQ() {return pftl.getQ();}
	public double getK() {return pftl.getK();}
	public double getR() {return pftl.getR();}
	public double getF() {return pftl.getF();}
	public boolean getAnimateFlag() {return pftl.getAnimateFlag();}
	public int getBorderLimit() {return pftl.getBorderLimit();}
	public int getIterations() {return pftl.getIterations();}
	
	public void setTimeLimit(int inT) {pftl.setTimeLimit(inT);}
	public void setQ(double inQ) {pftl.setQ(inQ);}
	public void setK(double inK) {pftl.setK(inK);}
	public void setR(double inR) {pftl.setR(inR);}
	public void setF(double inF) {pftl.setF(inF);}
	public void setAnimateFlag(boolean inAnimateFlag) {pftl.setAnimateFlag(inAnimateFlag);}
	public void setBorderLimit(int limit) {pftl.setBorderLimit(limit);}
	public void setIterations(int iterations) {pftl.setIterations(iterations);}
	
	/** Draws the graph. */
	public void layout() {
		
		setAnimateFlag(false);
		
		DiagramPanel dp = getDiagramPanel();
		
		dp.resetDiagram();
		
		DualGraph dg = getDualGraph();
		Node emptyNode0 = dg.firstNodeWithLabel("");
		ArrayList<Node> emptyConnection = null;
		if(emptyNode0 != null) {
			emptyConnection = new ArrayList<Node>(emptyNode0.connectingNodes());
			dg.removeNode(emptyNode0);
System.out.println(dg);
		}
		
		DualGraph cloneGraph = dg.generateTriangulatedClone();
		
		pftl.setDiagramPanel(dp);
		
//		pftl.drawGraphBasedOnClone(dg,cloneGraph);
		
		if(emptyNode0 != null) {
			// reinsert the empty node and its connections to the empty node
			Node newEmptyNode = new Node("");
			int x = dg.findMinimumX()-30;
			int y = dg.findMinimumY()-30;
			newEmptyNode.setCentre(new Point(x,y));
			// find any Node that can be attached to the new one
			Node nearestNode = ConcreteDiagram.findNearestNodeWithoutCrossing(dg,newEmptyNode.getCentre(),dg.getNodes());
			dg.addNode(newEmptyNode);
			Edge tempEdge = new Edge(newEmptyNode,nearestNode);
			dg.addEdge(tempEdge);
			
			if(emptyConnection.contains(nearestNode)) { // if we happen to have made a good connection
				tempEdge = null;
				emptyConnection.remove(nearestNode);
			}
			while(emptyConnection.size() != 0) {
				Node n = findFurthestNode(newEmptyNode.getCentre(),emptyConnection);
				emptyConnection.remove(n);
				Face addFace = null;
				dg.formFaces();
				for(Face f : dg.getFaces()) {
					ArrayList<Node> faceNodes = f.getNodeList();
					if(faceNodes.contains(newEmptyNode) && faceNodes.contains(n)) {
						addFace = f;
						continue;
					}
				}
				dg.addEdgeInFace(addFace,newEmptyNode,n);
				if(tempEdge != null) {
					dg.removeEdge(tempEdge);
					tempEdge = null;
				}
			}
		}
		
		if(!getAnimateFlag() && getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}

	/**
	 * Find the furthest node in the list from the Point.
	 */
	public static Node findFurthestNode(Point p, ArrayList<Node> nodeList) {
		double maxDistance = -1;
		Node retNode = null;
		for(Node n: nodeList) {
			double distance = pjr.graph.Util.distance(n.getCentre(), p);
			if(distance > maxDistance) {
				maxDistance = distance;
				retNode = n;
			}
		}
		
		return retNode;

	}
	

}
