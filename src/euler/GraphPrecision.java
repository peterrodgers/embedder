package euler;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import pjr.graph.*;

/**
 * A way of providing double precision for {@link}pjr.graph.Node centres
 * and {@link}pjr.graph.Edge bend points
 */ 
public class GraphPrecision {
	
//	public static final int CAPACITY = 1000;
//	public static final float LOADFACTOR = 0.75f;
	
public static final int CAPACITY = 10;
public static final float LOADFACTOR = 0.75f;
	
	protected Graph graph;
	protected HashMap<Node,Point2D.Double> nodeCentreMap = null;
	protected HashMap<Edge,ArrayList<Point2D.Double>> edgeBendsMap = null;
	


	/**
	 * Always needs a graph to start with, can be empty.
	 */
	public GraphPrecision(Graph graph) {
		super();
		this.graph = graph;
		setupMaps();
	}
	
	
	public HashMap<Node,Point2D.Double> getNodeCentreMap() {return nodeCentreMap;}
	public HashMap<Edge,ArrayList<Point2D.Double>> getEdgeBendsMap() {return edgeBendsMap;}


	/*
	 * Return the double precision centre of the node
	 */
	public Point2D.Double findNodeCentre(Node n) {
		Point2D.Double ret = nodeCentreMap.get(n);
		return ret;
	}

	/*
	 * Return the double precision bends of the edge
	 */
	public ArrayList<Point2D.Double> findEdgeBends(Edge e) {
		ArrayList<Point2D.Double> ret = edgeBendsMap.get(e);
		return ret;
	}

	/**
	 * Resets the node and edge maps with the current graph.
	 */
	protected void setupMaps() {
		
		nodeCentreMap = new HashMap<Node,Point2D.Double>(CAPACITY,LOADFACTOR);
		edgeBendsMap = new HashMap<Edge,ArrayList<Point2D.Double>>(CAPACITY,LOADFACTOR);
		
		for(Node n : graph.getNodes()) {
			Point2D.Double p = new Point2D.Double(n.getCentre().x,n.getCentre().y);
			nodeCentreMap.put(n,p);
		}
		
		for(Edge e : graph.getEdges()) {
			ArrayList<Point2D.Double> bends = new ArrayList<Point2D.Double>();
			
			for(Point b : e.getBends()) {
				Point2D.Double p = new Point2D.Double(b.x,b.y);
				bends.add(p);
			}
			edgeBendsMap.put(e,bends);
		}
	}

	
	/**
	 * Adds the node to the graph and to the precision list.
	 */
	public boolean addNode(Node n, Point2D.Double centre) {
		if(centre == null) {
			System.out.println("ERROR - null edge bends in GraphPrecision.addNode("+n+","+centre+"), node not added");
			return false;
		}
		int intX = pjr.graph.Util.convertToInteger(centre.x);
		int intY = pjr.graph.Util.convertToInteger(centre.y);
		n.setCentre(new Point(intX,intY));
		if(!graph.addNode(n)) {
			return false;
		}
		nodeCentreMap.put(n, centre);
		return true;
	}
	
	/**
	 * Adds the edge to the graph and to the precision list.
	 */
	public boolean addEdge(Edge e, ArrayList<Point2D.Double> bends) {
		if(bends == null) {
			System.out.println("ERROR - null edge bends in GraphPrecision.addEdge("+e+","+bends+")");
			return false;
		}
		ArrayList<Point> intBends = new ArrayList<Point>(bends.size());
		for(Point2D.Double b : bends) {
			int intX = pjr.graph.Util.convertToInteger(b.x);
			int intY = pjr.graph.Util.convertToInteger(b.y);
			Point intB = new Point(intX,intY);
			intBends.add(intB);
		}
		e.setBends(intBends);
		if(!graph.addEdge(e)) {
			return false;
		}
		edgeBendsMap.put(e,bends);
		return true;
	}
	
	public boolean addEdgeBend(Edge e, Point2D.Double pd) {
		
		ArrayList<Point> bendsI = e.getBends();
		if(bendsI == null) {
			return false;
		}
		int xI = pjr.graph.Util.convertToInteger(pd.x);
		int yI = pjr.graph.Util.convertToInteger(pd.y);
		bendsI.add(new Point(xI,yI));
		
		ArrayList<Point2D.Double> bendsD = edgeBendsMap.get(e);
		if(bendsD == null) {
			return false;
		}
		bendsD.add(pd);
		edgeBendsMap.put(e,bendsD);
		
		return true;
	}
	
	
	/**
	 * Removes the node from the graph and the precision list.
	 */
	public boolean removeNode(Node n) {
		
		ArrayList<Edge> edgeCollection = n.connectingEdges();
		for(Edge e : edgeCollection) {
			removeEdge(e);
		}
		
		if(!graph.removeNode(n)) {
			return false;
		}
		nodeCentreMap.remove(n);
		return true;
	}
	
	
	/**
	 * Removes the edge from the graph and the precision list.
	 */
	public boolean removeEdge(Edge e) {
		if(!graph.removeEdge(e)) {
			return false;
		}
		edgeBendsMap.remove(e);
		return true;
	}
	
	
	
	/*
	 * Tests to see if the graph is consistent with the graphPrecision,
	 * that is, contains the same nodes and edges, and that the values
	 * are the same.
	 */
	public boolean consistent() {
		
		if(!graph.consistent()) {
			if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Graph.consisent() is false");}
			return false;
		}

		ArrayList<Node> mapNodeList = new ArrayList<Node>(nodeCentreMap.keySet());
		ArrayList<Edge> mapEdgeList = new ArrayList<Edge>(edgeBendsMap.keySet());
		
		if(mapNodeList.size() != graph.getNodes().size()) {
			if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Node lists are of different sizes");}
			return false;
		}
		if(mapEdgeList.size() != graph.getEdges().size()) {
			if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Edge lists are of different sizes");}
			return false;
		}
		
		for(Node n : graph.getNodes()) {
			if(!mapNodeList.contains(n)) {
				if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Node "+n+" is in graph but not in precision map");}
				return false;
			}
			Point2D pi = n.getCentre();
			Point2D.Double pd = nodeCentreMap.get(n);
			if(pi.getX() != pjr.graph.Util.convertToInteger(pd.getX())) {
				if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Point for node "+n+" is "+pi+" x coordinate differs with precision map centre "+pd);}
				return false;
			}
			if(pi.getY() != pjr.graph.Util.convertToInteger(pd.getY())) {
				if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Point for node "+n+" is "+pi+" y coordinate differs with precision map centre "+pd);}
				return false;
			}
		}

		for(Edge e : graph.getEdges()) {
			if(!mapEdgeList.contains(e)) {
				if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Edge "+e+" is in graph but not in precision map");}
				return false;
			}
			
			ArrayList<Point2D.Double> bendsD = edgeBendsMap.get(e);
			if(e.getBends().size() != bendsD.size()) {
				if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Edge bend lists for edge "+e+ " are of different sizes");}
				return false;
			}
			
			for(int i = 0; i < bendsD.size(); i++) {
				Point2D pi = e.getBends().get(i);
				Point2D.Double pd = bendsD.get(i);
				if(pi.getX() != pjr.graph.Util.convertToInteger(pd.getX())) {
					if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Point in edge bend list for edge "+e+" is "+pi+" x coordinate differs with precision map centre "+pd);}
					return false;
				}
				if(pi.getY() != pjr.graph.Util.convertToInteger(pd.getY())) {
					if(euler.Util.reportErrors) {System.out.println("GraphPrecision.consistent() is false. Point in edge bend list for edge "+e+" is "+pi+" y coordinate differs with precision map centre "+pd);}
					return false;
				}
			}
		}

		return true;
	}	


	
	public String toString() {
		StringBuffer retBuff = new StringBuffer("NODES\n");
		for(Node n : nodeCentreMap.keySet()) {
			Point2D.Double p = nodeCentreMap.get(n);
			retBuff.append(n.getLabel()+";"+ n.getType().toString()+": "+"("+p.x+","+p.y+")\n");
		}
		retBuff.append("EDGES\n");
		for(Edge e : edgeBendsMap.keySet()) {
			retBuff.append(e.getFrom()+"-"+e.getTo()+";"+ e.getType().toString()+": ");
			for(Point2D.Double p : edgeBendsMap.get(e)) {
				retBuff.append("("+p.x+","+p.y+") ");
			}
			retBuff.append("\n");
		}
		return retBuff.toString();
	}


	

}
