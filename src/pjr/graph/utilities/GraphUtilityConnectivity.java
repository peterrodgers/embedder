package pjr.graph.utilities;

import pjr.graph.*;
import java.io.*;
import java.awt.event.*;


public class GraphUtilityConnectivity extends GraphUtility implements Serializable{
	
	
	
/** Trivial constructor. */
	public GraphUtilityConnectivity() {
		super(KeyEvent.VK_S,"Check connectivity");
	}

/** Trivial constructor. */
	public GraphUtilityConnectivity(int key, String s) {
		super(key,s);
	}

	public GraphUtilityConnectivity(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}

	public void apply() {
		Graph g = getGraph();
		checkGraphConnectivity(g);		
	}
	
	public void checkGraphConnectivity(Graph g){
		
		if(g.connected()==true)
			System.out.println("The graph is connected.");
		else
			System.out.println("The graph is not connect.");
		
		
	}
	

	
}