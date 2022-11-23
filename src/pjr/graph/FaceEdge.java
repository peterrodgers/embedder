package pjr.graph;

import java.awt.Point;
import java.util.ArrayList;


/**
 * This is a simple face edge, connecting two nodes together in a graph.
 *  <p>
 *	A face edge is directed, it has a start node and a end node. 
 * @see Face
 * @author Leishi Zhang
 */

public class FaceEdge{
	
	Node from;
	Node to;
	double angle;
	int index;
	boolean isVisited;
	boolean reverse;
	String label;
	Edge edge;
	protected ArrayList<Point> bends = new ArrayList<Point>();
	
	/** Trival accessor. */
	public FaceEdge(Node inFrom, Node inTo, Edge e){
		from = inFrom;
		to = inTo;
		edge = e;
		isVisited = false;
		reverse = false;
		setAngle();
	}
	public void setReverse(boolean rev){reverse = rev;}
	public boolean getReverse(){return reverse;}
	public void setLabel(String aLabel){label = aLabel;}	
	public String getLabel(){return label;}
	/** Trival accessor. */
	public FaceEdge(Node inFrom, Node inTo){
		from = inFrom;
		to = inTo;
		edge = null;
		isVisited = false;
		//setAngle();
		
	}

	/** Trival accessor. */
	public void setVisited(boolean inVisited){isVisited = inVisited;}
  	/** 
  	 * method to set the angle of the face edge
  	 * */
  	public void setAngle(){
  		angle = Util.lineAngle(to.getCentre(),from.getCentre());
  }	
  	
  	/** Trival accessor. */
	public void setIndex(int i){ index =i;}
	
	/** Trival accessor. */
	public double getAngle(){ return angle;}
	/**Trival accessor. */
	public int getIndex(){return index;}

	
	/** Trival accessor. */
	public Node getFrom() {return from;}
	/** Trival accessor. */
	public Node getTo() {return to;}
	/** Trival accessor. */	
	public Edge getEdge(){return edge;}
	/** Trival accessor. */
	public boolean getVisited(){ return isVisited;}
	public ArrayList<Point> getBends() {return bends;}
	public void setBends(ArrayList<Point> inEdgeBends) {bends = inEdgeBends;}
	
	/**
	 * Gives the other end of the edge to the argument node
	 * @return the node at the other end of the edge, or null if the passed node is not connected to the edge
	 */
	public Node oppositeEnd(Node n) {
		Node ret = null;
		if (getFrom() == n) {
			ret = getTo();
		}
		if (getTo() == n) {
			ret = getFrom();
		}
		return(ret);
	}
	
	/**
	 * Gives the node that connects the two face edges.
	 * @return the connecting node, or null if the two edges are not connected
	 */
	public Node connectingNode(FaceEdge fe) {
		if (getFrom() == fe.getFrom()) {
			return getFrom();
		}
		if (getFrom() == fe.getTo()) {
			return getFrom();
		}
		if (getTo() == fe.getFrom()) {
			return getTo();
		}
		if (getTo() == fe.getTo()) {
			return getTo();
		}
		return null;
	}
	/** Adds an edge bend to the end of the edge bend list. */
	public void addBend(Point p) {
		getBends().add(p);
	}
/** Removes all edge bends by setting the bend list to a new, empty list. */
	public void removeAllBends() {
		bends = new ArrayList<Point>();
	}
	/**
	 * method to check if the face edge has a node
	 * */
	public boolean hasNode(Node n){
		
		if(from.equals(n)||to.equals(n))
			return true;
		else
			return false;		
	}
	
	/**
	 * method to print out the face edges, angle and index of a face edge
	 * */
	public void print(){
		
		System.out.println("face edge (" + from.getLabel() + " , " + to.getLabel() + ")" 
					+ " angle = " + angle + " index = " + index); 		
	}
	/** 
	 * Gives the start and end node of the face edge, plus the angle and index. 
	 * */
	public String toString(){		
		return from.getLabel()+","+to.getLabel()+";"+ angle+";"+ index;
		
	}
	
}