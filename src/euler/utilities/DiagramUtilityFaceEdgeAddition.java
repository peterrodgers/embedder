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

public class DiagramUtilityFaceEdgeAddition extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityFaceEdgeAddition() {
		super(KeyEvent.VK_F,"Add Edges to Faces",KeyEvent.VK_F);
	}

/** Trivial constructor. */
	public DiagramUtilityFaceEdgeAddition(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		getDualGraph().addAllFaceSplits();
		
		diagramPanel.resetDiagram();
		diagramPanel.update(diagramPanel.getGraphics());
		
	}
	
	
	
}
