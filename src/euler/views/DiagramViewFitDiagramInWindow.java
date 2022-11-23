package euler.views;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


import euler.*;


/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class DiagramViewFitDiagramInWindow extends DiagramView {
	
	public static final int PADDING = 30;

/** Trivial constructor. */
	public DiagramViewFitDiagramInWindow() {
		super(KeyEvent.VK_F,"Fit Diagram In Window",KeyEvent.VK_F);
	}


/** Trivial constructor. */
	public DiagramViewFitDiagramInWindow(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {
		DiagramPanel dp = getDiagramPanel();
		dp.fitContoursInWindow();
	}
	
	
}

