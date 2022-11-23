package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import pjr.graph.FaceEdge;
import pjr.graph.Node;
import euler.*;
import euler.drawers.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityRandomEulerDiagram extends DiagramUtility {


	protected int numberOfSets = 4;

/** Trivial constructor. */
	public DiagramUtilityRandomEulerDiagram() {
		super(KeyEvent.VK_R,"Create Random Graph",KeyEvent.VK_R);
	}

/** Trivial constructor. */
	public DiagramUtilityRandomEulerDiagram(int inAccelerator, String inMenuText, int inMnemonic, int numberOfSets) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.numberOfSets = numberOfSets;
	}

/** Trivial accessor. */
	public int getNumberOfSets() {return numberOfSets;}


	public void apply() {

		
		DualGraph dg = new DualGraph(AbstractDiagram.randomDiagramFactory(numberOfSets));
		while(!dg.connected()) {
			dg = new DualGraph(AbstractDiagram.randomDiagramFactory(numberOfSets));
		}

		DiagramPanel dp = getDiagramPanel();
		dp.setDualGraph(dg);
		dp.setShowEdgeDirection(false);
		dp.setShowEdgeLabel(true);
		//dg.randomizeNodePoints(new Point(50,50),400,400);

		DiagramDrawerPlanar.planarLayout(dg);

		boolean emptyInOuter = false;
		dg.formFaces();
		if(dg.getOuterFace()!=null){
			for(Node n : dg.getOuterFace().getNodeList()) {
				if(n.getLabel().equals("")) {
					emptyInOuter = true;					
				}
			}
		}
		if(!emptyInOuter && DiagramDrawerPlanar.isPlanar(dg)) {
			//System.out.println("BUG - outer face of plane layout does not include empty zone, diagram: "+dg.findAbstractDiagram());
		}
	/*	System.out.println("---------------------");
		ArrayList<ArrayList<FaceEdge>> circles = dg.allPossibleCycles();
		System.out.println("--------------------- " + circles.size());
		if(circles.size()!=0){
			for(ArrayList<FaceEdge> cycle: circles){
				System.out.println("cycle-----");
				for(FaceEdge fe: cycle){
					System.out.println(fe.toString());
				}
			}
		}&*/
		dp.fitGraphInPanel();
		
		dp.update(dp.getGraphics());
	}


	
}
