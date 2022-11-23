package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import euler.*;
import euler.display.*;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityFaceEdgeRemoval extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityFaceEdgeRemoval() {
		super(KeyEvent.VK_U,"Remove All Poly Edges",KeyEvent.VK_U);
	}

/** Trivial constructor. */
	public DiagramUtilityFaceEdgeRemoval(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		getDualGraph().removeAllPolyEdges();
		
		diagramPanel.resetDiagram();
		diagramPanel.update(diagramPanel.getGraphics());
		
	}
	
	
	
}
