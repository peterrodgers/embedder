package euler.utilities;

import java.awt.event.*;
import euler.*;

/**
 * Output useful diagram information.
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityResetConcreteDiagram extends DiagramUtility {

/** Trivial constructor. */
	public DiagramUtilityResetConcreteDiagram() {
		super(KeyEvent.VK_R,"Reset Concrete Diagram",KeyEvent.VK_R);
	}

/** Trivial constructor. */
	public DiagramUtilityResetConcreteDiagram(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}


	public void apply() {

		DiagramPanel dp = getDiagramPanel();
		
		dp.resetDiagram();

		dp.update(dp.getGraphics());
		
		
	}

	
}
