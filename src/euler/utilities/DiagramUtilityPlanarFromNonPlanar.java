package euler.utilities;

import java.awt.Frame;
import java.awt.event.*;
import java.util.*;

import pjr.graph.*;
import pjr.graph.drawers.BasicSpringEmbedder;
import euler.*;
import euler.drawers.*;

/**
 * Output useful diagram information.
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityPlanarFromNonPlanar extends DiagramUtility {

/** Trivial constructor. */
	public DiagramUtilityPlanarFromNonPlanar() {
		super(KeyEvent.VK_N,"Attempt to find the most wf planar dual",KeyEvent.VK_N);
	}

/** Trivial constructor. */
	public DiagramUtilityPlanarFromNonPlanar(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}


	public void apply() {

		DiagramPanel dp = getDiagramPanel();
		DualGraph dg = getDualGraph();

		//DualGraph newDual= null;
		DualGraph newDual = dg.findWellformedPlanarGraph();
		if(newDual == null) {
			//System.out.println("can not find a wellformed planar graph after edge removing");
			BasicSpringEmbedder se = new BasicSpringEmbedder();
			se.setGraphPanel(new GraphPanel(dg, new Frame()));
			se.layout();
			newDual = DualGraph.findNonWellformedPlanarGraph(dg);
		}
		
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(dp);
		ddp.planarLayout(newDual);

		
		dp.setDualGraph(newDual);
		
		dp.fitGraphInPanel();
		
		dp.resetDiagram();
		dp.update(dp.getGraphics());
		
	}

	
}
