package pjr.graph.drawers;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import pjr.graph.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle,
 * then apply the spring embedder.
 *
 * @author Peter Rodgers
 */

public class BasicSpringEmbedder extends GraphDrawer {

	public static void main(String[] args) {
		String inFileName = "";
		String outFileName = "";

		if (args.length == 2) {
			inFileName = args[0];
			outFileName = args[1];
		} else {
			System.out.println("Must have 2 parameters: inFileName outFileName");
			System.exit(0);
		}
		
		File inFile = new File(inFileName);
		File outFile = new File(outFileName);
		
		Graph graph = new Graph();
		GeneralXML xml = new GeneralXML(graph);
		if(!xml.loadGraph(inFile)) {
			System.exit(0);
		}
		
		BasicSpringEmbedder se = new BasicSpringEmbedder();
		se.setGraphPanel(new GraphPanel(graph,new Frame()));
		se.layout();
		
		xml.saveGraph(outFile);
		System.out.println("Spring Embedder applied. Saving "+outFile);
	}


/** Trivial constructor. */
	public BasicSpringEmbedder() {
		super(KeyEvent.VK_S,"Spring Embedder");
	}

/** Trivial constructor. */
	public BasicSpringEmbedder(int key, String s) {
		super(key,s);
	}

	public BasicSpringEmbedder(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

	public void layout() {

//		 standard spring embedder with randomization and no animation
		GraphDrawerSpringEmbedder se = new GraphDrawerSpringEmbedder(KeyEvent.VK_Q,"Spring Embedder - randomize, no animation",true);
//		se.setAnimateFlag(true);
		se.setAnimateFlag(false);
		se.setIterations(1000);
		se.setTimeLimit(2000);
		if(getGraphPanel() != null) {
			se.setGraphPanel(getGraphPanel());
		}
		se.layout();
	}


	
	
	
}
