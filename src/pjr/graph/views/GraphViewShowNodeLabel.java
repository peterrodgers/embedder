package pjr.graph.views;

import java.io.*;
import java.awt.event.*;

import pjr.graph.GraphPanel;

/**
 * Toggle the display of node labels.
 *
 * @author Peter Rodgers
 */

public class GraphViewShowNodeLabel extends GraphView implements Serializable {

/** Trivial constructor. */
	public GraphViewShowNodeLabel() {
		super(KeyEvent.VK_K,"Toggle Node Label Display",KeyEvent.VK_K);
	}


/** Trivial constructor. */
	public GraphViewShowNodeLabel(int key, String s) {
		super(key,s,key);
	}

/** Trivial constructor. */
	public GraphViewShowNodeLabel(int acceleratorKey, String s, int mnemonicKey) {
		super(acceleratorKey,s,mnemonicKey);
	}


	public void view() {

		GraphPanel panel = getGraphPanel();

		if(panel.getShowNodeLabel()) {
			panel.setShowNodeLabel(false);
		} else {
			panel.setShowNodeLabel(true);
		}

	}
}

