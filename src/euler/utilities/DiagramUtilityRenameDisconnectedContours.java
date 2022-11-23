package euler.utilities;

import java.awt.event.*;
import euler.*;

/**
 * Output useful diagram information.
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityRenameDisconnectedContours extends DiagramUtility {

/** Trivial constructor. */
	public DiagramUtilityRenameDisconnectedContours() {
		super(KeyEvent.VK_Q,"Rename Disconnected Contours",KeyEvent.VK_Q);
	}

/** Trivial constructor. */
	public DiagramUtilityRenameDisconnectedContours(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}


	public void apply() {

		DualGraph dg = getDualGraph();
		dg.renameDisconnectedContours();
/*
System.out.print("contourLabelMap: ");
for(String k : dg.getContourLabelMap().keySet()) {
	System.out.println(k+","+dg.getContourLabelMap().get(k)+" ");
}
System.out.println();
System.out.print("holeLabelMap: ");
for(String k : dg.getHoleLabelMap().keySet()) {
	System.out.print(k+","+dg.getHoleLabelMap().get(k)+" ");
}
System.out.println();
*/
		DiagramPanel dp = getDiagramPanel();
		dp.resetDiagram();
		dp.update(dp.getGraphics());
		
	}

	
}
