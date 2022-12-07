package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.drawers.*;
import euler.simplify.Simplify;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityMergeSetsForConcurrency extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityMergeSetsForConcurrency() {
		super(KeyEvent.VK_M,"Merge Sets for Concurrency",KeyEvent.VK_M);
	}

/** Trivial constructor. */
	public DiagramUtilityMergeSetsForConcurrency(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		DualGraph dg = getDualGraph();

		Simplify simplify = new Simplify(dg);
		
		boolean concurrency = simplify.getDualGraph().hasConcurrentEdges();

		if(concurrency) {
			simplify.reduceConcurrencyInDualGraph();
		}
		
		diagramPanel.resetDiagram();
		diagramPanel.update(diagramPanel.getGraphics());

		DualGraph dgNew = simplify.getDualGraph();
//		DualGraph dgNew = new DualGraph();
		
		DualGraphWindow dw2 = new DualGraphWindow(dgNew);
		dw2.getDiagramPanel().setShowGraph(true);
		dw2.getDiagramPanel().setShowEdgeDirection(false);
		dw2.getDiagramPanel().setShowEdgeLabel(true);
		dw2.getDiagramPanel().setShowContour(false);
		dw2.getDiagramPanel().setShowContourLabel(true);
		dw2.getDiagramPanel().setShowTriangulation(false);
		dw2.getDiagramPanel().setJiggleLabels(false);
		dw2.getDiagramPanel().setForceNoRedraw(false);
		dw2.getDiagramPanel().update(dw2.getDiagramPanel().getGraphics());

		
for(String type : simplify.getTypeMergeHistory()) {
	System.out.println("type merge history: "+type);		
}
for(String[] h : simplify.getSetMergeHistory()) {
	System.out.println("set merge history: "+h[0]+" "+h[1]);		
}
for(AbstractDiagram h : simplify.getAbstractDiagramMergeHistory()) {
	System.out.println("abstract diagram history: "+h);		
}
System.out.println("current abstract diagram: "+simplify.getAbstractDiagram());

	}
	
	
	
}
