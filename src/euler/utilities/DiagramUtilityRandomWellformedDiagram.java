package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.drawers.*;
import pjr.graph.*;


/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityRandomWellformedDiagram extends DiagramUtility {


	protected int numberOfSets = 4;
	protected boolean layoutFlag = false;

	

/** Trivial constructor. */
	public DiagramUtilityRandomWellformedDiagram(int numberOfSets) {
		super(KeyEvent.VK_R,"Create Random Graph",KeyEvent.VK_R);
		this.numberOfSets = numberOfSets;
	}

/** Trivial constructor. */
	public DiagramUtilityRandomWellformedDiagram(int inAccelerator, String inMenuText, int inMnemonic, int numberOfSets, boolean layout) {
		
		super(inAccelerator, inMenuText, inMnemonic);
		this.numberOfSets = numberOfSets;
		this.layoutFlag = layout;
	}
	
	/** Trival accessor. */
	public int getNumberOfSets() {return numberOfSets;}


	public void apply() {
		DualGraph dg = null;
		
		DiagramPanel dp = getDiagramPanel();	
		

		dp.setForceNoRedraw(true); // turn off the panel redraw		
		dg = generateRandomWFDiagram(numberOfSets, false);	
		
		if(dg == null){
			System.out.println("ERROR generateRandomWFDiagram("+numberOfSets+", false) failed to return a diagram");
			dg = DualGraph.findNonWellformedPlanarGraph(dg);
		}
		
		dp.setDualGraph(dg);
	/*	DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_9, "Planar Layout Algorithm", KeyEvent.VK_9);
		ddp.setDiagramPanel(dp);
		ddp.layout();
		if(this.getDiagramPanel() == null){
			System.out.println("DiagramUtilityRandomWellformedDiagram dg = null");
		}*/
		
		//Node emptyNode = dg.firstNodeWithLabel("");
		
		// TODO This is dirty fix for avoiding lines of nodes
		// which is what can happen when the empty node is removed
		// this does not occur when the empty node is present
		// Needs fixing by treating faces with lines in properly
		// NOTE STILL FAILS FOR ARTICULATION POINTS
		/*
		Node lastConnectionRemoved = null;
		for(Edge e : emptyNode.connectingEdges()) {
			Node n = e.getOppositeEnd(emptyNode);
			if(n.degree() != 2) {
				// only remove the edge if the edge removal does not create a node of degree 1
				dg.removeEdge(e);
				lastConnectionRemoved = n;
			}
		}
		if(emptyNode.degree() == 0) {
			// if we have disconnected the empty node, remove it
			dg.removeNode(emptyNode);
		} else if(emptyNode.degree() == 1 && lastConnectionRemoved != null) {
			// dont want 1 degree empty nodes, as this is still dangling
			dg.addEdge(new Edge(emptyNode,lastConnectionRemoved));
		}
		*/
		// dirty fix ends here, uncomment the next line

		//getDualGraph().removeNode(emptyNode);
		
		
		dp.resetDiagram();
		if(layoutFlag) {
			WellFormedConcreteDiagram cd = new WellFormedConcreteDiagram(getDualGraph());
			dp.setConcreteDiagram(cd);
			cd.setOptimizeMeetingPoints(true);
			cd.setOptimizeContourAngles(true);
			cd.setFitCircles(true);
			cd.generateContours();
			PlanarForceTriangulationLayout pftl = new PlanarForceTriangulationLayout(cd);
			pftl.setDiagramPanel(getDiagramPanel());
			pftl.setAnimateFlag(false);
			pftl.drawGraph();
			if(cd.getCloneGraph().findTriangulationEdgeCrossings().size() > 0) {
				// here the nice layout algorithm fails, so restore planar embedding
				DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_9, "Planar Layout Algorithm", KeyEvent.VK_9,this.getDiagramPanel());
				ddp.setDualGraph(dg);
				ddp.setDiagramPanel(getDiagramPanel());
				ddp.layout();
				cd = new WellFormedConcreteDiagram(getDualGraph());
				dp.setConcreteDiagram(cd);
				cd.setOptimizeMeetingPoints(true);
				cd.setOptimizeContourAngles(true);
				cd.setFitCircles(true);
				cd.generateContours();
			}
		}

		boolean correct = false;
		ArrayList<String> duplicateZones = null;
		ArrayList<TriangulationFace> wrongCrossings = null;
		AbstractDiagram polygonDiagram = null;
		try {
			getDualGraph();
			ConcreteDiagram cd = dp.getConcreteDiagram();
			if(cd == null) {
				cd = new WellFormedConcreteDiagram(getDualGraph());
				dp.setConcreteDiagram(cd);
				if(layoutFlag) {
					cd.setOptimizeMeetingPoints(true);
					cd.setOptimizeContourAngles(true);
					cd.setFitCircles(true);
				} else {
					cd.setOptimizeMeetingPoints(false);
					cd.setOptimizeContourAngles(false);
					cd.setFitCircles(false);
				}
				cd.generateContours();
			}
			correct = cd.correctConcreteDiagram();
			duplicateZones = cd.findDuplicateZones();
			wrongCrossings = cd.findIncorrectTriangulationCrossings();
			polygonDiagram = cd.generateAbstractDiagramFromPolygons();
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("Generated a well formed\none well fromed diagram with "+numberOfSets+" sets");
		System.out.println("from abstract diagram: " +dg.findAbstractDiagram());
		if(wrongCrossings.size() != 0) {
			System.out.println("wrong contour crossing in TFs: "+wrongCrossings);
		}
		if(wrongCrossings.size() != 0) {
			System.out.println("duplicate zones: "+duplicateZones+", count "+duplicateZones.size());
		}
		if(!correct) {
			System.out.println("INCORRECT DIAGRAM");
			System.out.println("abstract diagram by polygons: "+polygonDiagram);
		}
		if( dp == null){
			System.out.println("dp == null in wellformed diagram ");
		}
		
		dp.setForceNoRedraw(false); // turn on the panel redraw
		dp.fitGraphInPanel();		
		dp.update(dp.getGraphics());
		
	}
	
	
	/**
	 * Generate a wellformed diagram of the given size.
	 */
	public DualGraph generateRandomWFDiagram(int size, boolean subdiagramsAllowed) {
		DualGraph dg = null;
		boolean loop = true;
		while(loop) {
			
			loop = false;
			dg = DualGraph.randomWellformedDualGraphFactoryByRectangles(size,50,600,true);
			boolean connected = false;
			
			if( dg!= null){
				connected = dg.connected();
			}
			if(!connected) {
				loop = true;
				continue;
			}
			boolean passConnectivityConditions = dg.checkConnectivity();
			if(!passConnectivityConditions) {
				loop = true;
				continue;
			}
			int nestedDiagramsCount = dg.findNestedSubdiagrams(false).size();
			if(!subdiagramsAllowed && nestedDiagramsCount != 0 ) {
				loop = true;
				continue;
			}
			
			dg.randomizeNodePoints(new Point(50,50),400,400);

			try {
				DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_9, "Planar Layout Algorithm", KeyEvent.VK_9, this.getDiagramPanel());
				ddp.setDualGraph(dg);
				ddp.setDiagramPanel(getDiagramPanel());
				ddp.layout();

			} catch(Exception e) {
				System.out.println("fail planar layout");
				e.printStackTrace();			
			}
			
			/*		if(dg.findEdgeCrossings().size() != 0) {
				System.out.println("BUG - Passed planar test, but is not plane layout, diagram: "+dg.findAbstractDiagram());
				loop = true;
				continue;
			}
			*/
/*			if(nestedDiagramsCount == 0) {
				dg.formFaces();			
			}
*/				
			boolean passFaceCondition = dg.passFaceConditions();
			if(!passFaceCondition) {
				loop = true;
				continue;
			}
		}
		
		return dg;
	}


	public static boolean isWellformed(DualGraph dg){
		
		if(!dg.connected()) {
			return false;
		}
		if(!dg.checkConnectivity()) {
			return false;
		}
		if(dg.findNestedSubdiagrams(false).size()!= 0 ) {
			return false;
		}
		if(dg.findEdgeCrossings().size() != 0) {
			return false;
		}		
		dg.formFaces();	
		if(!dg.passFaceConditions()) {
			return false;
		}
		return true;
	}
	
	
}
