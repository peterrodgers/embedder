package euler.views;


import java.awt.event.*;
import euler.*;


/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class DiagramViewCycleItemsDisplayed extends DiagramView {

	int cycle  = 1;
	
/** Trivial constructor. */
	public DiagramViewCycleItemsDisplayed() {
		super(KeyEvent.VK_K,"Toggle Label And Direction Display",KeyEvent.VK_L);
	}


/** Trivial constructor. */
	public DiagramViewCycleItemsDisplayed(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public DiagramViewCycleItemsDisplayed(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {
				
		DiagramPanel panel = getDiagramPanel();
		while(true) {
			if(cycle == 0) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(false);
				panel.setShowTriangulation(false);
				
				panel.setShowEdgeLabel(true);
				panel.setShowContourAreas(true);
				panel.setOptimizeContourAngles(true);
				panel.setOptimizeMeetingPoints(false);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 1) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(false);
				panel.setShowTriangulation(true);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(false);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(false);
				panel.setOptimizeMeetingPoints(false);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 2) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(true);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(false);
				panel.setOptimizeMeetingPoints(false);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 3) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(true);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(true);
				panel.setOptimizeMeetingPoints(true);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 4) {
				panel.setShowGraph(false);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(false);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(false);
				panel.setOptimizeMeetingPoints(false);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 5) {
				panel.setShowGraph(false);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(false);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(true);
				panel.setOptimizeContourAngles(true);
				panel.setOptimizeMeetingPoints(true);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
			if(cycle == 6) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(false);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(false);
				panel.setOptimizeMeetingPoints(false);
				panel.setFitCircles(false);
				
				cycle++;
				return;
			}
/*			if(cycle == 7) {
				panel.setShowGraph(true);
				panel.setShowRegion(false);
				panel.setShowContour(true);
				panel.setShowTriangulation(false);
				
				panel.setShowEdgeLabel(false);
				panel.setShowContourLabel(true);
				panel.setShowContourAreas(false);
				panel.setOptimizeContourAngles(true);
				panel.setOptimizeMeetingPoints(true);
				panel.setFitCircles(true);
				
				cycle++;
				return;
			}
*/			
			cycle = 0;
		}
	}
}

