package euler.inductive;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


import euler.*;
import euler.display.*;
import euler.views.*;


/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class DiagramViewShowEulerGraph extends DiagramView {
	
	protected InductiveWindow iw;
	
	public static DualGraphWindow eulerGraphWindow = null;
	

/** Trivial constructor. */
	public DiagramViewShowEulerGraph(int acceleratorKey, String s, int mnemonicKey, InductiveWindow iw) {
		super(acceleratorKey,s,mnemonicKey);
		this.iw = iw;
	}


	public void view() {
		HybridGraph hg = iw.getHybridGraph();
		DualGraph largeGraph = hg.getLargeGraph();

		DualGraph eg = HybridGraph.findEulerGraph(largeGraph);
		eg.scale(1.0/HybridGraph.SCALE);
		
		if(eulerGraphWindow == null) {
			eulerGraphWindow = new DualGraphWindow(eg.clone());
			eulerGraphWindow.setLocation(eulerGraphWindow.getLocation().x+50, eulerGraphWindow.getLocation().y);
			eulerGraphWindow.getDiagramPanel().setParallelEdgeOffset(0);
			eulerGraphWindow.getDiagramPanel().requestFocus();
		} else {
			eulerGraphWindow.setDualGraph(eg.clone());
			eulerGraphWindow.getDiagramPanel().requestFocus();
		}
		
	}
	
	
}

