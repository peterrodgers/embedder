package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.drawers.*;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityAttemptToConnect extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityAttemptToConnect() {
		super(KeyEvent.VK_A,"Add Edges to Faces",KeyEvent.VK_A);
	}

/** Trivial constructor. */
	public DiagramUtilityAttemptToConnect(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		DualGraph dg = getDualGraph();
		
		// first make sure there are no disconnected parts of the graph
		ArrayList<ArrayList<Node>> subgraphs = dg.findDisconnectedSubGraphs(null);
		ArrayList<Node> firstSubgraph = subgraphs.get(0);
		subgraphs.remove(0);
		for(ArrayList<Node> subgraph : subgraphs) {
			Edge e = new Edge(firstSubgraph.get(0),subgraph.get(0));
			dg.addEdge(e);
		}

		dg.connectDisconnectedComponents();
		
		diagramPanel.resetDiagram();
		diagramPanel.update(diagramPanel.getGraphics());
		
	}
	
	
	
}
