package pjr.graph.drawers;

import java.util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.event.*;

import pjr.graph.*;

/**
 * Iterative barycenter drawing method.
 * Selected nodes are not moved.
 *
 * @author Peter Rodgers
 */

public class GraphDrawerBarycenter extends GraphDrawer implements Serializable {

/** The number of iterations */
	protected int iterations = 20;
/** The node buffer. This holds copies of node locations */
	protected DrawCoordCollection nodeBuffer = new DrawCoordCollection();
/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
/** The nodes selected at the start of the algorithm */
	protected ArrayList selectedNodes = null;

/** Trivial constructor. */
	public GraphDrawerBarycenter() {
		super(KeyEvent.VK_B,"Barycenter");
	}

/** Trivial constructor. */
	public GraphDrawerBarycenter(int key, String s) {
		super(key,s);
	}


/** Trival accessor. */
	public long getTime() {return time;}
/** Trival accessor. */
	public int getIterations() {return iterations;}
/** Trival accessor. */
	public boolean getAnimateFlag() {return animateFlag;}

/** Trivial modifier. */
	public void setIterations(int inIterations) {iterations = inIterations;}
/** Trivial modifier. */
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}


/** Draws the graph. */
	public void layout() {

		selectedNodes = getGraphPanel().getSelection().getNodes();

		Date oldTimer = new Date();

		nodeBuffer.setUpNodes(getGraph().getNodes());
		for(int i = 1; i <= iterations; i++) {
			embed();
			if(animateFlag && getGraphPanel() != null) {
				nodeBuffer.switchOldCentresToNode();
				getGraphPanel().update(getGraphPanel().getGraphics());
			}
		}

		nodeBuffer.switchOldCentresToNode();

		Date newTimer = new Date();
		time = newTimer.getTime() - oldTimer.getTime();
//System.out.println("time: "+time);
	}


/**
 * Move all the nodes in the graph.
 */
	public void embed() {
		for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
			if (!selectedNodes.contains(nb.getNode())) {
				nb.setNewCentre(force(nb));
			}
		}
		nodeBuffer.switchNewCentresToOld();
	}




/**
 * Finds the new location of a node.
 */
	public Point2D.Double force(DrawCoord nb) {

		Node n = nb.getNode();
		Point2D.Double p = new Point2D.Double(0,0);
		Collection<Node> neighbours = n.connectingNodes();

		if(neighbours.size() == 0) {
			return (new Point2D.Double(n.getX(),n.getY()));
		}

		for(Node nextN : neighbours) {
			Point2D.Double nextP = new Point2D.Double(nextN.getX(),nextN.getY());
			p.x += nextP.x;
			p.y += nextP.y;
		}

		p.x = p.x / neighbours.size();
		p.y = p.y / neighbours.size();

		return(p);
	}

}

 
