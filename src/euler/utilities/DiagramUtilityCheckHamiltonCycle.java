package euler.utilities;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import pjr.graph.Edge;
import pjr.graph.Node;
import euler.DualGraph;



/**
 *
 * @author Leishi Zhang
 */

public class DiagramUtilityCheckHamiltonCycle extends DiagramUtility{
	/** Trivial constructor. */
	public DiagramUtilityCheckHamiltonCycle() {
		super(KeyEvent.VK_A," Check Hamilton Cycle",KeyEvent.VK_A);
	}

/** Trivial constructor. */
	public DiagramUtilityCheckHamiltonCycle(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		DualGraph dg = getDualGraph();
		dg.CheckHamiltonCycle();
		//dg.PossibleCycles();
		
	}
	
}
