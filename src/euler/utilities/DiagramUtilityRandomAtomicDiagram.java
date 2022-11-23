package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

import pjr.graph.*;
import pjr.graph.drawers.BasicSpringEmbedder;
import euler.*;
import euler.drawers.*;

/**
 * @author Peter Rodgers
 */

public class DiagramUtilityRandomAtomicDiagram extends DiagramUtility {


	protected int numberOfSets = 4;
	protected boolean layoutFlag = false;
	protected static Random r = new Random(System.currentTimeMillis());
	

/** Trivial constructor. */
	public DiagramUtilityRandomAtomicDiagram(int numberOfSets) {
		super(KeyEvent.VK_R,"Create Random Graph",KeyEvent.VK_R);
		this.numberOfSets = numberOfSets;
	}

/** Trivial constructor. */
	public DiagramUtilityRandomAtomicDiagram(int inAccelerator, String inMenuText, int inMnemonic, int numberOfSets, boolean layout) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.numberOfSets = numberOfSets;
		this.layoutFlag = layout;
	}
	
	/** Trival accessor. */
	public int getNumberOfSets() {return numberOfSets;}


	public void apply() {

		double zoneProbability = 0.5;
		if(numberOfSets == 2) {zoneProbability = 0.5;}
		if(numberOfSets == 3) {zoneProbability = 0.5;}
		if(numberOfSets == 4) {zoneProbability = 0.4;}
		if(numberOfSets == 5) {zoneProbability = 0.4;}
		if(numberOfSets == 6) {zoneProbability = 0.3;}
		if(numberOfSets == 7) {zoneProbability = 0.3;}
		if(numberOfSets == 8) {zoneProbability = 0.2;}
		if(numberOfSets == 9) {zoneProbability = 0.2;}

		DualGraph dg = null;
		while(dg == null) {
			AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(numberOfSets,true,0.5);
System.out.println("Testing "+numberOfSets+" set diagram "+ad+" for atomicness");
			dg = generalDualFromAtomicAbstract(ad);
		}
		
System.out.println("Attempting to draw "+numberOfSets+" set diagram "+dg.findAbstractDiagram());
		
		diagramPanel.setDualGraph(dg);
		diagramPanel.resetDiagram();
		diagramPanel.fitGraphInPanel(100);
		diagramPanel.update(diagramPanel.getGraphics());
System.out.println("Finished drawing diagram ");

	}
	
	/**
	 * Take the abstract diagram
	 * - create a superdual
	 * - connect up any disconnected dual graph components
	 * - remove edges to find a planar dual
	 * @return concrete dual or null if abstract diagram is not atomic. 
	 */
	public static DualGraph generalDualFromAtomicAbstract(AbstractDiagram ad) {
		
		int diagramCount = ad.generateAtomicDiagrams().countAtomicDiagrams();
		if(diagramCount != 1) {
			return null;
		}
		
		DualGraph dg = new DualGraph(ad);
		
		// connect up a disconnected dual graph
		ArrayList<ArrayList<Node>> subgraphs = dg.findDisconnectedSubGraphs(null);
		ArrayList<Node> firstSubgraph = subgraphs.get(0);
		subgraphs.remove(0);
		for(ArrayList<Node> subgraph : subgraphs) {
			Edge e = new Edge(firstSubgraph.get(0),subgraph.get(0));
			dg.addEdge(e);
		}
		
		// find a planar layout with minimal edges removed
		// aiming to maintain well-connectedness
		if(!dg.checkConnectivity()) {
			DualGraph newDual = dg.findWellformedPlanarGraph();
			boolean planar = DiagramDrawerPlanar.planarLayout(dg);
			if(planar) {
				newDual = dg;
			} else if(newDual == null) {
				//System.out.println("can not find a wellformed planar graph after edge removing");
				BasicSpringEmbedder se = new BasicSpringEmbedder();
				se.setGraphPanel(new GraphPanel(dg, new Frame()));
				se.layout();
				DualGraph temp = null;
				while(temp == null) {
					temp = DualGraph.findNonWellformedPlanarGraph(dg);
				}
				newDual = temp;
			}
				 
				// nasty fix in case of non-planar result
			planar = DiagramDrawerPlanar.planarLayout(dg);
			while(!planar) {
System.out.println("finding planar layout by random edge removal");
					
				dg.removeEdge(dg.getEdges().get(r.nextInt(dg.getEdges().size())));
				subgraphs = dg.findDisconnectedSubGraphs(null);
				firstSubgraph = subgraphs.get(0);
				subgraphs.remove(0);
				for(ArrayList<Node> subgraph : subgraphs) {
System.out.println("Adding Edge ");
					Edge e = new Edge(firstSubgraph.get(0),subgraph.get(0));
					dg.addEdge(e);
				}
				planar = DiagramDrawerPlanar.planarLayout(dg);

			}

			dg = newDual;
		}
		
		// if the planar graph is not well-connected
		// make it as well-connected as possible by
		// adding the smallest parallel edges
		dg.connectDisconnectedComponents();
		
		DiagramDrawerPlanar.planarLayout(dg);

		// draw the graph nicely before triangulating
		PlanarForceLayout pfl = new PlanarForceLayout(dg);
		pfl.setAnimateFlag(false);
		pfl.drawGraph();
		if(dg.findEdgeCrossings().size() > 0) {
System.out.println("STANDARD PLANAR FORCE LAYOUT FAILED to generate nice layout failed, restoring planar embedding");
			// here the nice layout algorithm fails, so restore planar embedding
			DiagramDrawerPlanar.planarLayout(dg);
		}

		
		// DRAWING OF HOLES AND DUPLICATE CONTOURS by may be done here by
		// renaming or done in the contour layout process, probably
		// not here

		// draw the graph

		// Lay the dual graph out nicely
		
		// split any faces that need splitting
		// dg.addAllFaceSplits();
		
		return dg;
		
	}
	
	
}
