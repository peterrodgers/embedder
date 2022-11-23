package euler.inductive;


import java.awt.event.*;

import euler.drawers.*;

/**
 * A version of Eades spring embedder for laying out 
 * planar graphs without changing the planarity and faces
 * @author Peter Rodgers
 * @author Leishi Zhang
 */

public class DiagramDrawerHybridForce extends DiagramDrawer {
	
	HybridGraph hg;

	/** Trivial constructor. */
	public DiagramDrawerHybridForce(int key, String s, int mnemomic, HybridGraph hg) {
		super(key,s,mnemomic);
		this.hg = hg;
	}

	
	/** Draws the graph. */
	public void layout() {
		
		if(diagramPanel != null) {
			HybridGraph.panelMessage(diagramPanel,"Applying spring embedder");
		}
		
		hg.drawWithSpringEmbedder();

		if(diagramPanel != null) {
			diagramPanel.fitGraphInPanel(DiagramDrawerEulerGraphForce.DRAWNGRAPHPADDING);
		}
		
	}
	

}
