package pjr.graph;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;

import euler.*;
import euler.utilities.*;

import pjr.graph.comparators.*;

/**
 * This is a graph containing nodes and edges.
 * <p>
 * The graph can be used for both directed and undirected graphs.
 * The current interpretation of undirected edges in the
 * algorithms is being able to traverse a single edge in either
 * direction. This is reflected in the graph, node and edge access functions,
 * which provide for this form of undirected access.
 * <p>
 * The consistency of the graph takes several forms:
 * all nodes and edges attach to items in the graph;
 * unique nodes and edges in the collections (no duplicate objects);
 * edges having a connection at each end; and
 * edge to and from corresponding to node to and from.
 * <p>
 * Consistency is maintained through the operations in graph, so
 * that its not possible to add an edge without the nodes being
 * in the graph.
 * Also, the node delete removes any connecting edges from the graph
 * <p>
 * Note that it is still possible to create an inconsistent graph by
 * accessing fields directly, or via poor use of Node or Edge methods.
 *
 * @see Node
 * @see Edge
 * @author Peter Rodgers
 * @author Leishi Zhang
 */
public class Graph implements Cloneable {
	

	/** The default edge type. */
	public static final EdgeType DEFAULT_EDGE_TYPE = new EdgeType("defaultEdgeType",1000);
	/** The default node type. */
	public static final NodeType DEFAULT_NODE_TYPE = new NodeType("defaultNodeType");
	/** Collection of nodes. */
	protected ArrayList<Node> nodes = new ArrayList<Node>();
	/** Collection of edges. */
	protected ArrayList<Edge> edges = new ArrayList<Edge>();
	/** Graph label, can be empty. */
	protected String label = "";
	/** Graph adjacency list - for planar graph drawing*/
	protected Vector<ArrayList<Node>> adjList = new Vector<ArrayList<Node>>();
	/** Face edges of the graph. */		
	protected ArrayList<FaceEdge> doubledUpFaceEdges = new ArrayList<FaceEdge>();	
	/** Faces of the graph. */
	protected ArrayList<Face> faces = new ArrayList<Face>();
	/** boolean value to indicate if the graph passes the face conditions*/	
	public final char ADJACENCYSEPARATOR = ':';
	public final char WEIGHTEDSEPARATOR = ' ';
	public final char SIMPLEFILESEPARATOR = ' ';
	public final char FILESEPARATOR = '|';
	public final char XYSEPARATOR = ',';
	public final char COORDINATESEPARATOR = ';';
	public final String FILESTARTNODES = "NODES";
	public final String FILESTARTEDGES = "EDGES";
	public final String FILESTARTNODETYPES = "NODETYPES";
	public final String FILESTARTEDGETYPES = "EDGETYPES";
	public final String FILESTARTLABEL = "LABEL";

	/** Minimal constructor. It creates an empty unlabelled graph*/
	public Graph() {}
	/** Trivial constructor. It creates an empty labelled graph */
	public Graph(String inLabel) {setLabel(inLabel);}
	/** Trivial constructor. It is a converter for class PGraph */
/*	public Graph(PGraph gp){
		
	     Vector  pNodes = gp.getNodes();
	     Vector pEdges = gp.getEdges();	 
		 for(int i = 0 ; i < pNodes.size(); i ++){
		 	Node node = new Node((PNode)pNodes.elementAt(i));
		 	nodes.add(node);
		 }
		 for(int j = 0 ; j < pEdges.size(); j++){		 	
		 	Edge edge = new Edge ((PEdge)pEdges.elementAt(j));
		 	edges.add(edge);  
		 }
	}*/

	/** Trival accessor. */
	public String getLabel() {return label;}
	/** Trival accessor. */
	public ArrayList<Face> getFaces() {return faces;}
	/** Trivial modifier. */
	public void setLabel(String inLabel) {label=inLabel;}
	
	/**
	 * Adds a node to the graph.
	 * @return true if the node is added, false if it fails because it already there.
	 */
	public boolean addNode(Node n)  {
		if (containsNode(n)) {
			return(false);
		}
		return(nodes.add(n));
	}
	
	/**
	 * Adds an edge to the graph.
	 * @return true if the node is added, false if it fails because the edge is
	 * already there, or either node is not in the graph.
	 */
	public boolean addEdge(Edge e) {

		if(!containsNode(e.getTo())) {
			return(false);
		}
		if(!containsNode(e.getFrom())) {
			return(false);
		}
		if(containsEdge(e)) {
			return(false);
		}
		return(edges.add(e));
	}
		
	public ArrayList<Node> getNodes() {return nodes;}
	public ArrayList<Edge> getEdges() {return edges;}
	
	/** Return the Edge connecting the two nodes. */
	public Edge getEdge(Node n1, Node n2){
		for(Edge e: edges) {
			if(e.getTo() == n1 && e.getFrom() == n2) {
				return e;
			}
			if(e.getTo() == n2 && e.getFrom()== n1) {
				return e;		
			}
		}
		return null;
	}
	
	/** Return all the Edges connecting the two nodes. */
	public ArrayList<Edge> edgesBetween(Node n1, Node n2){
		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(Edge e: edges) {
			if(e.getTo() == n1 && e.getFrom() == n2) {
				ret.add(e);
			} else if(e.getTo() == n2 && e.getFrom()== n1) {
				ret.add(e);
			}
		}
		return ret;
	}
	
	
	/**
	 * Set the visited fields of all the nodes in the graph to false.
	 * this, or the one argument alternative should be called at the
	 * start of an algorithm which uses the visited flags of nodes.
	 */
	public void setNodesVisited() {
		setNodesVisited(getNodes(),false);		
	}
	
	/**
	 * Set the visited fields of all the nodes in the graph with the argument.
	 */
	public void setNodesVisited(boolean inVisited) {
		setNodesVisited(getNodes(),inVisited);
	}
		
	/**
	 * Set the visited fields of the given nodes with the argument.
	 */
	public void setNodesVisited(Collection<Node> inNodes, boolean inVisited) {
		for(Node n : inNodes) {
			n.setVisited(inVisited);
		}
	}
		
	/**
	 * Set the score fields of all the nodes in the graph with the argument.
	 */
	public void setNodesScores(double inScore) {
		setNodesScores(nodes, inScore);
	}
		
	/**
	 * Set the score fields of the given nodes with the argument.
	 */
	public void setNodesScores(Collection<Node> inNodes, double inScore) {
		for(Node n : inNodes) {
			n.setScore(inScore);
		}
	}
		
	/**
	 * Set the match fields of all the nodes in the graph to null.
	 */
	public void setNodesMatches() {
		setNodesMatches(nodes,null);
	}
		
	/**
	 * Set the match fields of all the nodes in the graph to the given value.
	 */
	public void setNodesMatches(Object inMatch) {
		setNodesMatches(nodes,inMatch);
	}
		
	/**
	 * Set the match fields of the given node collection to the given value.
	 */
	public void setNodesMatches(Collection<Node> inNodes, Object inMatch) {
		for(Node n : inNodes) {
			n.setMatch(inMatch);
		}
	}
		
	/**
	 * Set the visited values of the edges to false,
	 * this should be called before using the
	 * visited flags of nodes.
	 */
	public void setEdgesVisited() {
		setEdgesVisited(getEdges(),false);
	}
		
	/**
	 * Set the visited values of all the edges in the graph to the argument.
	 */
	public void setEdgesVisited(boolean inVisited) {
		setEdgesVisited(getEdges(),inVisited);
	}
		
	/**
	 * Set the visited values of the given edges to the argument.
	 */
	public void setEdgesVisited(Collection<Edge> inEdges, boolean inVisited) {
		for(Edge e : inEdges) {
			e.setVisited(inVisited);
		}
	}
		
	/**
	 * Set the score fields of all the edges in the graph with the argument.
	 */
	public void setEdgesScores(double inScore) {
		setEdgesScores(getEdges(),inScore);
	}
		
	/**
	 * Set the score fields of the given edges with the argument.
	 */
	public void setEdgesScores(Collection<Edge> inEdges, double inScore) {
		for(Edge e : inEdges) {
			e.setScore(inScore);
		}
	}
		
	/**
	 * Set the weight fields of all the edges in the graph with the argument.
	 */
	public void setEdgesWeights(double inWeight) {
		setEdgesWeights(getEdges(),inWeight);
	}
		
	/**
	 * Set the weight fields of the given edges with the argument.
	 */
	public void setEdgesWeights(Collection<Edge> inEdges, double inWeight) {
		for(Edge e : inEdges) {
			e.setWeight(inWeight);
		}
	}
		
	/**
	 * Set the match fields of all the edges in the graph to null.
	 */
	public void setEdgesMatches() {
		setEdgesMatches(edges,null);
	}
		
	/**
	 * Set the match fields of all the edges in the graph to the given value.
	 */
	public void setEdgesMatches(Object inMatch) {
		setEdgesMatches(edges,inMatch);
	}
		
	/**
	 * Set the match fields of the given edge collection to the given value.
	 */
	public void setEdgesMatches(Collection<Edge> inEdges, Object inMatch) {
		for(Edge e : inEdges) {
			e.setMatch(inMatch);
		}
	}
		
	/** Gives the nodes that are not marked as visited. */
	public ArrayList<Node> unvisitedNodes() {
		ArrayList<Node> ret = new ArrayList<Node>();

		for(Node n : getNodes()) {
			if(n.getVisited() == false) {
				ret.add(n);
			}
		}

		return(ret);
	}
		
	/** Gives the nodes that are marked as visited. */
	public ArrayList<Node> visitedNodes() {
		ArrayList<Node> ret = new ArrayList<Node>();

		for(Node n : getNodes()) {
			if(n.getVisited() != false) {
				ret.add(n);
			}
		}

		return(ret);
	}
		
	/** Gives the nodes that don't have matches. */
	public ArrayList<Node> unmatchedNodes() {
		ArrayList<Node> ret = new ArrayList<Node>();

		for(Node n : getNodes()) {
			if(n.getMatch() == null) {
				ret.add(n);
			}
		}

		return(ret);
	}
		
	/** Gives the nodes that have matches. */
	public ArrayList<Node> matchedNodes() {
		ArrayList<Node> ret = new ArrayList<Node>();

		for(Node n : getNodes()) {
			if(n.getMatch() != null) {
				ret.add(n);
			}
		}

		return(ret);
	}
		
	/** Gives the edges that are not marked as visited. */
	public ArrayList<Edge> unvisitedEdges() {
		ArrayList<Edge> ret = new ArrayList<Edge>();

		for(Edge e : getEdges()) {
			if(e.getVisited() == false) {
				ret.add(e);
			}
		}

		return(ret);
	}
		
	/** Gives the edges that are marked as visited. */
	public ArrayList<Edge> visitedEdges() {
		ArrayList<Edge> ret = new ArrayList<Edge>();

		for(Edge e : getEdges()) {
			if(e.getVisited() != false) {
				ret.add(e);
			}
		}
		return(ret);
	}
		
	/** Gives the edges that dont have matches. */
	public ArrayList<Edge> unmatchedEdges() {
		ArrayList<Edge> ret = new ArrayList<Edge>();

		for(Edge e : getEdges()) {
			if(e.getMatch() == null) {
				ret.add(e);
			}
		}
		return(ret);
	}
		
	/** Gives the edges that have matches. */
	public ArrayList<Edge> matchedEdges() {
		ArrayList<Edge> ret = new ArrayList<Edge>();

		for(Edge e : getEdges()) {
			if(e.getMatch() != null) {
				ret.add(e);
			}
		}
		return(ret);
	}
	
	/**
	 * Sum all the edge weights in the graph.
	 */
	public double sumEdgeWeights() {
		return(sumEdgeWeights(getEdges()));
	}
	
	/**
	 * Finds if the node is already present in the graph.
	 */
	public boolean containsNode(Node n) {
		for(Node next : getNodes()) {
			if(n == next) {
				return(true);
			}
		}
		return(false);
	}
	
	/**
	 * Finds if the edge is already present in the graph.
	 */
	public boolean containsEdge(Edge e) {
		for(Edge next : getEdges()) {
			if(e == next) {
				return(true);
			}
		}
		return(false);
	}
	
	/**
	 * Sum the edge weights of edges in the given collection.
	 */
	public double sumEdgeWeights(Collection<Edge> edgeCollection) {

		double sum = 0.0;
		for(Edge e : edgeCollection) {
			sum += e.getWeight();
		}
		return(sum);
	}
	
	
	/**
	 * Returns the edge crossings in the graph.
	 * It gives a list of 2 element arrays, each
	 * element in the list is a pair of crossing edges.
	 */
	public ArrayList<Edge[]> findEdgeCrossings() {

		ArrayList<Edge[]> ret = new ArrayList<Edge[]>();
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>(getEdges());
		
		for(int i1 = 0; i1< edgeList.size(); i1++) {
			for(int i2 = i1+1; i2< edgeList.size(); i2++) {
				Edge e1 = edgeList.get(i1);
				Edge e2 = edgeList.get(i2);
				
				// edges sharing a node cant cross
				if(e1.getFrom() == e2.getFrom() || e1.getFrom() == e2.getTo()) {
					continue;
				}
				if(e1.getTo() == e2.getFrom() || e1.getTo() == e2.getTo()) {
					continue;
				}
				
				if(pjr.graph.Util.linesCross(e1.getFrom().getCentre(),e1.getTo().getCentre(),e2.getFrom().getCentre(),e2.getTo().getCentre())) {
					Edge[] pair = {e1,e2};
					ret.add(pair);
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * Undirected graph connectedness test by breadth first search. A Breadth First Search
	 * is made through the graph starting at any node. After the BFS if there
	 * are any nodes that have not been visited then the graph is not connected.
	 * This is detected by counting the number of nodes that have appeared
	 * in the queue, if is the same as the number in the graph the graph is
	 * connected. Here we implement a queue using an ArrayList.
	 * @return true if the graph is connected, false if it is unconnected.
	 */
	public boolean connected() {

		setNodesVisited();
		int totalNodeNumber = getNodes().size();

		Iterator ni = getNodes().iterator();
		if(!ni.hasNext()) {
			return(true);
		}
		Node first = (Node)ni.next();
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(first);
		first.setVisited(true);
		int numberTested = 0;
		while(!queue.isEmpty()) {

			Node head = (Node)queue.get(0);
			queue.remove(0);

			numberTested++;

			ArrayList<Node> neighbours = head.unvisitedConnectingNodes();
			setNodesVisited(neighbours,true);
			queue.addAll(neighbours);
		}

		setNodesVisited();

		if (numberTested == totalNodeNumber) {
			return(true);
		}

		return(false);
	}
	
	/**
	 * Test to see if two nodes are connected by breadth first search.
	 * @return true if the nodes are connected, false if they are unconnected.
	 */
	public boolean nodesConnected(Node source, Node target) {

		setNodesVisited();
		
		if(!getNodes().contains(source)) {
			return false;
		}

		if(!getNodes().contains(target)) {
			return false;
		}

		if(source == target) {
			return true;
		}
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(source);
		source.setVisited(true);
		Node current = null;
		while(!queue.isEmpty() && current != target) {

			current = (Node)queue.get(0);
			queue.remove(0);

			ArrayList<Node> neighbours = current.unvisitedConnectingNodes();
			setNodesVisited(neighbours,true);
			queue.addAll(neighbours);
		}

		setNodesVisited();

		if (current == target) {
			return true;
		}

		return false;
	}


	/**
	 * Find the unvisited nodes connected to the argument node and mark them visited.
	 */
	public ArrayList<Node> connectedUnvisitedNodes(Node n) {
		
		ArrayList<Node> ret = new ArrayList<Node>();
		
		if(n.getVisited()) {
			return ret;
		}
		n.setVisited(true);

		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(n);

		while(queue.size() > 0) {
			Node head = queue.get(0);
			queue.remove(0);
			ret.add(head);
			ArrayList<Node> addList = head.unvisitedConnectingNodes();
			setNodesVisited(addList,true);
			queue.addAll(addList);
		}
		return ret;
	}


	
	/**
	 * Removes the node from the graph and deletes any connecting edges. Accounts
	 * for redundant data connecting nodes.
	 * @return the success of the operation, failure is due to node not being in the graph.
	 */
	public boolean removeNode(Node removeNode) {

		if(!containsNode(removeNode)) {
			return(false);
		}

		ArrayList<Edge> edgeCollection = removeNode.connectingEdges();
		for(Edge e : edgeCollection) {
			removeEdge(e);
		}
		nodes.remove(removeNode);
		return(true);
	}

	/**
	 * Removes the nodes with the given label from the graph and removes
	 * any connecting edges. Accounts for redundant data connecting nodes.
	 * @return the success of the operation, false if there is no node
	 * with the label in the graph.
	 */
	public boolean removeNode(String removeString) {

		boolean ret = false;

		boolean delete = true;
		while(delete) {
			delete = false;
			Iterator ni = nodes.iterator();
			while (ni.hasNext() && !delete) {
				Node n = (Node)ni.next();
				if (removeString.compareTo(n.getLabel())==0) {
					removeNode(n);
					delete = true;
					ret = true;
				}
			}
		}

		return(ret);
	}
	
	/**
	 * Removes the given node collection. Does nothing if the elements
	 * in the collection are not nodes in the graph.
	 */
	public void removeNodes(Collection<Node> c) {
		for(Node n : c) {
			removeNode(n);
		}
	}
	
	/**
	 * Removes the given edge collection. Does nothing if the elements
	 * in the collection are not edges in the graph.
	 */
	public void removeEdges(Collection<Edge> c) {
		for(Edge e : c) {
			removeEdge(e);
		}
	}
		
	/**
	 * Tests the number of edges connecting to all nodes.
	 * @return true if all nodes have an even number of connecting edges,
	 * false if any are odd.
	 */
	public boolean nodesHaveEvenDegree() {

		for(Node n : getNodes()) {
			if(n.degree()%2 != 0) {
				return(false);
			}
		}
		return(true);
	}
	
	/**
	 * Removes the edge from the graph. Accounts
	 * for redundant data connecting nodes.
	 * @return the success of the operation, failure is due to edge not being in the graph.
	 */
	public  boolean removeEdge(Edge removeEdge) {

		if(!containsEdge(removeEdge)) {
			return(false);
		}

		// this is the best I can do - set the source and target of the
		// connecting edge to null, if there are any external references
		// to the edge, then their attempt to access the nodes at the ends
		// may crash.

		removeEdge.setFromTo(null,null);
		edges.remove(removeEdge);

		return(true);
	}

	/**
	 * Moves the given node to the end of the list.
	 * @return true if the node is moved, false if it is not present.
	 */
	public boolean moveNodeToEnd(Node n) {
		if (!containsNode(n)) {
			return(false);
		}
		// need to remove the node directly from the list
		// to avoid connecting edge removal
		nodes.remove(n);
		nodes.add(n);
		return(true);
	}
	
	/**
	 * Moves the given edge to the end of the list.
	 * @return true if the edge is moved, false if it is not present.
	 */
	public boolean moveEdgeToEnd(Edge e) {
		if (!containsEdge(e)) {
			return(false);
		}
		edges.remove(e);
		edges.add(e);
		return(true);
	}

	/**
	 * Removes the nodes and edges from the graph, leaving an empty graph.
	 * This is a destructive clear as nodes are removed from the graph, so
	 * loosing their edge connections.
	 */
	public void clear() {
		setLabel("");
		Iterator ni = nodes.iterator();
		while(ni.hasNext()) {
			Node n = (Node)ni.next();
			removeNode(n);
			ni = nodes.iterator();
		}
	}
	
	/**
	 * Saves most of the graph node, edge and associated type information.
	 * All edge data saved, but does not save deep node information: node
	 * match and node path attributes.
	 */
	public boolean saveAll(File file) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

			// save the graph label
			b.write(FILESTARTLABEL);
			b.newLine();
			b.write(getLabel());
			b.newLine();
			
			// save the node types
			b.write(FILESTARTNODETYPES);
			b.newLine();
			
			for(NodeType nt : NodeType.getExistingTypes()) {

				StringBuffer outNodeType = new StringBuffer("");
				outNodeType.append(nt.getLabel());
				outNodeType.append(FILESEPARATOR);
				if(nt.getParent() != null) {
					outNodeType.append(nt.getParent().getLabel());
				}
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getWidth());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getHeight());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getShapeString());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getFillColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getBorderColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getTextColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getStroke().getLineWidth());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedFillColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedBorderColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedTextColor().getRGB());
				outNodeType.append(FILESEPARATOR);
				outNodeType.append(nt.getSelectedStroke().getLineWidth());

				b.write(outNodeType.toString());
				b.newLine();
			}


			// save the edge types
			b.write(FILESTARTEDGETYPES);
			b.newLine();

			for(EdgeType et : EdgeType.getExistingTypes()) {

				StringBuffer outEdgeType = new StringBuffer("");
				outEdgeType.append(et.getLabel());
				outEdgeType.append(FILESEPARATOR);
				if(et.getParent() != null) {
					outEdgeType.append(et.getParent().getLabel());
				}
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getDirected());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getLineColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getStroke().getLineWidth());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedLineColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedStroke().getLineWidth());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getTextColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getSelectedTextColor().getRGB());
				outEdgeType.append(FILESEPARATOR);
				outEdgeType.append(et.getPriority());

				b.write(outEdgeType.toString());
				b.newLine();
			}


			// save the nodes with generated ids
			b.write(FILESTARTNODES);
			b.newLine();
			// use match to save the index, as match cannot be saved
			ArrayList<Node> nodeList  = new ArrayList<Node>(getNodes());
			int index = 0; 
			while (index < nodeList.size()){
				Node n = (Node)nodeList.get(index);
				n.setMatch(new Integer(index));

				StringBuffer outNode = new StringBuffer("");
				outNode.append(index);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getLabel());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getType().getLabel());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getCentre().x);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getCentre().y);
				outNode.append(FILESEPARATOR);
				outNode.append(n.getVisited());
				outNode.append(FILESEPARATOR);
				outNode.append(n.getScore());

				b.write(outNode.toString());
				b.newLine();
				index++;
			}
			// save the edges
			b.write(FILESTARTEDGES);
			b.newLine();
			for(Edge e : getEdges()) {
				StringBuffer outEdge = new StringBuffer("");
				outEdge.append(e.getFrom().getMatch().toString());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getTo().getMatch().toString());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getLabel());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getWeight());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getType().getLabel());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getVisited());
				outEdge.append(FILESEPARATOR);
				outEdge.append(e.getScore());

				outEdge.append(FILESEPARATOR);
				Iterator bi = e.getBends().iterator();
				while(bi.hasNext()) {
					Point point = (Point)bi.next();
					outEdge.append(point.x);
					outEdge.append(XYSEPARATOR);
					outEdge.append(point.y);
					if(bi.hasNext()) {
						outEdge.append(COORDINATESEPARATOR);
					}
				}

				b.write(outEdge.toString());
				b.newLine();
			}
			b.close();
		}
		catch(IOException e) {
			System.out.println("An IO exception occured when executing saveAll("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;
	}

	/**
	 * Saves only the node locations and label and the edge labels, the edges
	 * linked by node lables, rather than ids, hence the node labels must be unique
	 */
	public boolean saveSimple(File file) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

			// save the nodes
			b.write(FILESTARTNODES);
			b.newLine();
			for(Node n : getNodes()) {
				StringBuffer outNode = new StringBuffer("");
				outNode.append(n.getLabel());
				outNode.append(SIMPLEFILESEPARATOR);
				outNode.append(n.getCentre().x);
				outNode.append(SIMPLEFILESEPARATOR);
				outNode.append(n.getCentre().y);
				b.write(outNode.toString());
				b.newLine();
			}


			// save the edges
			b.write(FILESTARTEDGES);
			b.newLine();
			for(Edge e : getEdges()) {
				StringBuffer outEdge = new StringBuffer("");
				outEdge.append(e.getFrom().getLabel());
				outEdge.append(SIMPLEFILESEPARATOR);
				outEdge.append(e.getTo().getLabel());
				outEdge.append(SIMPLEFILESEPARATOR);
				outEdge.append(e.getLabel());
				b.write(outEdge.toString());
				b.newLine();
			}
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveSimple("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;
	}
	
	
	/** loads all the graph information and associated types */
	public boolean loadAll(File file) {

		clear();

		try {
			Character c = new Character(FILESEPARATOR);
			String separatorString = new String(c.toString());

			c = new Character(COORDINATESEPARATOR);
			String coordinateString = new String(c.toString());

			c = new Character(XYSEPARATOR);
			String xyString = new String(c.toString());

			boolean readingNodeTypes = false;
			boolean readingEdgeTypes = false;
			boolean readingEdges = false;
			boolean readingNodes = false;
			boolean readingLabel = false;

			ArrayList<Node> nodeList = new ArrayList<Node>();

			BufferedReader b = new BufferedReader(new FileReader(file));
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}

				if(readingLabel && !line.equals(FILESTARTNODETYPES)) {
					label += line;
				}

				if(readingNodeTypes && !line.equals(FILESTARTEDGETYPES)) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;
					// get label
					separatorInd = parseLine.indexOf(separatorString);
					String nodeLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
					// check for current node type
					NodeType nt = NodeType.withLabel(nodeLabel);
					if (nt == null) {
						// if not already in graph- node type label can only be set by constructor
						nt = new NodeType(nodeLabel);
					}

					// get parent
					separatorInd = parseLine.indexOf(separatorString);
					String parentLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
					// check if parent exists
					if(!parentLabel.equals("")) {
						NodeType pt = NodeType.withLabel(parentLabel);
						if (pt == null) {
							// if parent is not already in graph- create a new node type, if the type is loaded
							// later then the default settings should be overridden
							pt = new NodeType(parentLabel);
						}
						nt.setParent(pt);
					}

					Integer rgb = null;
					Integer size = null;
					Float width = null;

					// get node type width
					separatorInd = parseLine.indexOf(separatorString);
					size = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setWidth(size.intValue());
					parseLine.delete(0,separatorInd+1);

					// get node type height
					separatorInd = parseLine.indexOf(separatorString);
					size = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setHeight(size.intValue());
					parseLine.delete(0,separatorInd+1);

					// get node type shape
					separatorInd = parseLine.indexOf(separatorString);
					String shapeString = parseLine.substring(0,separatorInd);
					nt.setShapeString(shapeString);
					parseLine.delete(0,separatorInd+1);

					// get node type fill color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setFillColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type border color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setBorderColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					nt.setStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type selected fill color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedFillColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type selected border color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedBorderColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type selected text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					nt.setSelectedTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get node type selected stroke, the last attribute
					width = new Float(Float.parseFloat(parseLine.toString()));
					nt.setSelectedStroke(new BasicStroke(width.floatValue()));
				}

				if(readingEdgeTypes && !line.equals(FILESTARTNODES)) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

					// get edge type label
					separatorInd = parseLine.indexOf(separatorString);
					String edgeLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
					// check for current edge type
					EdgeType et = EdgeType.withLabel(edgeLabel);
					if (et == null) {
						// if not already in graph- edge type label can only be set by constructor
						et = new EdgeType(edgeLabel);
					}

					// get edge type parent
					separatorInd = parseLine.indexOf(separatorString);
					String parentLabel = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
					// check if parent exists
					if(!parentLabel.equals("")) {
						EdgeType pt = EdgeType.withLabel(parentLabel);
						if (pt == null) {
							// if parent is not already in graph- create a new edge type, if the type is loaded
							// later then the default settings should be overridden
							pt = new EdgeType(parentLabel);
						}
						et.setParent(pt);
					}

					Integer rgb = null;
					Float width = null;


					// get edge type directed value
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						et.setDirected(true);
					} else {
						et.setDirected(false);
					}
					parseLine.delete(0,separatorInd+1);

					// get edge type line color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setLineColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get edge type stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					et.setStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

					// get edge type selected line color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setSelectedLineColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// get edge type selected stroke
					separatorInd = parseLine.indexOf(separatorString);
					width = new Float(Float.parseFloat(parseLine.substring(0,separatorInd)));
					et.setSelectedStroke(new BasicStroke(width.floatValue()));
					parseLine.delete(0,separatorInd+1);

					// edge type text color
					separatorInd = parseLine.indexOf(separatorString);
					rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					et.setTextColor(new Color(rgb.intValue()));
					parseLine.delete(0,separatorInd+1);

					// edge type selected text color, might be last attribute in old files
					separatorInd = parseLine.indexOf(separatorString);
					if(separatorInd == -1) {
						rgb = new Integer(Integer.parseInt(parseLine.toString()));
						et.setSelectedTextColor(new Color(rgb.intValue()));
					} else {
						rgb = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
						et.setSelectedTextColor(new Color(rgb.intValue()));
						parseLine.delete(0,separatorInd+1);

						Integer priority = new Integer(Integer.parseInt(parseLine.toString()));
						if(priority.intValue() != -1) {
							et.setPriority(priority.intValue());
						}
					}
				}

				if(readingNodes && !line.equals(FILESTARTEDGES)) {
					Node n = new Node();
					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

					// get node id used for edges later
					separatorInd = parseLine.indexOf(separatorString);
					Integer index = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setMatch(index);
					parseLine.delete(0,separatorInd+1);

					// get node label
					separatorInd = parseLine.indexOf(separatorString);
					String nodeLabel = parseLine.substring(0,separatorInd);
					n.setLabel(nodeLabel);
					parseLine.delete(0,separatorInd+1);

					// get node type and find it in the node type list
					separatorInd = parseLine.indexOf(separatorString);
					String typeLabel = parseLine.substring(0,separatorInd);
					NodeType nt = NodeType.withLabel(typeLabel);
					if(nt != null) {
						n.setType(nt);
					}
					parseLine.delete(0,separatorInd+1);

					// get node x coord
					separatorInd = parseLine.indexOf(separatorString);
					Integer x = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setX(x.intValue());
					parseLine.delete(0,separatorInd+1);

					// get node y coord
					separatorInd = parseLine.indexOf(separatorString);
					Integer y = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					n.setY(y.intValue());
					parseLine.delete(0,separatorInd+1);

					// get node visited flag
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						n.setVisited(true);
					} else {
						n.setVisited(false);
					}
					parseLine.delete(0,separatorInd+1);

					// get node score, the last attribute
					Double s = new Double(Double.parseDouble(parseLine.toString()));
					n.setScore(s.doubleValue());

					addNode(n);

					int indexInt = index.intValue();

					while(nodeList.size() <= indexInt) {
						nodeList.add(null);
					}

					nodeList.set(indexInt,n);
				}

				if(readingEdges) {

					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;

					// get id of first node
					separatorInd = parseLine.indexOf(separatorString);
					Integer i1 = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					parseLine.delete(0,separatorInd+1);

					// get id of second node
					separatorInd = parseLine.indexOf(separatorString);
					Integer i2 = new Integer(Integer.parseInt(parseLine.substring(0,separatorInd)));
					parseLine.delete(0,separatorInd+1);

					Edge e = new Edge((Node)nodeList.get(i1.intValue()),(Node)nodeList.get(i2.intValue()));

					// get edge label
					separatorInd = parseLine.indexOf(separatorString);
					String edgeLabel = parseLine.substring(0,separatorInd);
					e.setLabel(edgeLabel);
					parseLine.delete(0,separatorInd+1);

					// get edge weight
					separatorInd = parseLine.indexOf(separatorString);
					Double w = new Double(Double.parseDouble(parseLine.substring(0,separatorInd)));
					e.setWeight(w.doubleValue());
					parseLine.delete(0,separatorInd+1);

					// get edge type and find it in the edge type list
					separatorInd = parseLine.indexOf(separatorString);
					String typeLabel = parseLine.substring(0,separatorInd);
					EdgeType et = EdgeType.withLabel(typeLabel);
					if(et != null) {
						e.setType(et);
					}
					parseLine.delete(0,separatorInd+1);

					// get edge visited flag
					separatorInd = parseLine.indexOf(separatorString);
					if(parseLine.substring(0,separatorInd).equals("true")) {
						e.setVisited(true);
					} else {
						e.setVisited(false);
					}
					parseLine.delete(0,separatorInd+1);

					// get edge score, may be the last attribute
					separatorInd = parseLine.indexOf(separatorString);
					if(separatorInd == -1) {
						String scoreString = parseLine.toString();
						Double s = new Double(Double.parseDouble(scoreString));
						e.setScore(s.doubleValue());
					} else {
						String scoreString = parseLine.substring(0,separatorInd);
						Double s = new Double(Double.parseDouble(scoreString));
						e.setScore(s.doubleValue());
						parseLine.delete(0,separatorInd+1);

						// if edge score is not the last attribute, then the edge bends are all that are left in parseLine
						while(parseLine.length() != 0) {
							separatorInd = parseLine.indexOf(coordinateString);
							String pairString = null;
							if(separatorInd == -1) {
								separatorInd = parseLine.length();
							}
							pairString = parseLine.substring(0,separatorInd);
							parseLine.delete(0,separatorInd+1);
							int xySeparatorInd = pairString.indexOf(xyString);
							String xString = pairString.substring(0,xySeparatorInd);
							String yString = pairString.substring(xySeparatorInd+1);

							Integer ix = new Integer(Integer.parseInt(xString));
							Integer iy = new Integer(Integer.parseInt(yString));
							Point bend = new Point(ix.intValue(),iy.intValue());
							e.addBend(bend);
						}
					}


					addEdge(e);

				}

				if(line.equals(FILESTARTLABEL)) {
					readingNodeTypes = false;
					readingEdgeTypes = false;
					readingEdges = false;
					readingNodes = false;
					readingLabel = true;
				}
				if(line.equals(FILESTARTNODETYPES)) {
					readingNodeTypes = true;
					readingEdgeTypes = false;
					readingEdges = false;
					readingNodes = false;
					readingLabel = false;
				}
				if(line.equals(FILESTARTEDGETYPES)) {
					readingNodeTypes = false;
					readingEdgeTypes = true;
					readingEdges = false;
					readingNodes = false;
					readingLabel = false;
				}
				if(line.equals(FILESTARTEDGES)) {
					readingNodeTypes = false;
					readingEdgeTypes = false;
					readingEdges = true;
					readingNodes = false;
					readingLabel = false;
				}
				if(line.equals(FILESTARTNODES)) {
					readingNodeTypes = false;
					readingEdgeTypes = false;
					readingEdges = false;
					readingNodes = true;
					readingLabel = false;
				}
				line = b.readLine();

			}
			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAll("+file+") in Graph.java: "+e+"\n");
			return false;
		}

		return true;
	}
		
	/**
	 * This saves an adjacency list graph file. The file is in the form of a
	 * simple adjacency list, each line
	 * of the file is an edge, with nodes separated by the {@link #ADJACENCYSEPARATOR}
	 * character. Any empty line
	 * or line with more than one colon is ignored.
	 * Nodes are assumed to have unique labels for the purpose of this file writer
	 * if there are duplicates, they will be merged to the same node.
	 */
	public void saveAdjacencyFile(String fileName) {
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(fileName));
			ArrayList<Node> remainingNodes = new ArrayList<Node>(getNodes());
			StringBuffer s = new StringBuffer();
			for(Edge e : getEdges()){
				s.setLength(0);
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				s.append(n1.getLabel()).append(ADJACENCYSEPARATOR).append(n2.getLabel());
				b.write(s.toString());
				b.newLine();

				remainingNodes.remove(n1);
				remainingNodes.remove(n2);
			}
			//deal with singleton nodes
			for(Node n : remainingNodes) {
				b.write(n.toString());
				b.newLine();
			}

			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveAdjacencyFile("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}
	}

	/**
	 * This loads the given adjacency list graph file into the graph, deleting any
	 * current nodes and edges. The file is in the form of a simple adjacency list, each line
	 * of the file is an edge, with nodes separated by the {@link #ADJACENCYSEPARATOR}
	 * character. Any empty line
	 * or line with more than one colon is ignored.
	 * Nodes are assumed to have unique labels for the purpose of this loader.
	 * may appear in each line. Any non empty line without a colon is
	 * considered to be a node with no connecting edges.
	 * @return true if successful, false if there was a formatting problem
	 */
	public boolean loadAdjacencyFile(String fileName) {

		clear();
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line = b.readLine();
			while(line != null) {

				int separatorInd = line.indexOf(ADJACENCYSEPARATOR);

				// ignore any empty lines and lines with more than 1 separator
				if(line.length() > 0 && separatorInd == line.lastIndexOf(ADJACENCYSEPARATOR)) {
					if(separatorInd >= 0) {
						String n1 = line.substring(0,separatorInd);
						String n2 = line.substring(separatorInd+1);
						addAdjacencyEdge(n1,n2);
					} else {
						// no separator, so just add a singleton node
						addAdjacencyNode(line);
					}
				}
				line = b.readLine();
			}

			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAdjacencyFile("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}

		return(true);
	}

	/**
	 * This loads the given adjacency list graph file into the graph, deleting any
	 * current nodes and edges. The file is in the form of a simple adjacency list, each line
	 * of the file is an edge, two nodes and a weight, separated by the {@link #ADJACENCYSEPARATOR}
	 * character. Any empty line is ignored.
	 * Any non empty line without a colon is
	 * considered to be a node with no connecting edges.
	 * Any line with only one colon is considered to represent an edge with 0 weight
	 * Nodes are assumed to have unique labels for the purpose of this loader.
	 * @return the first Node found or null if empty.
	 */
	public Node loadWeightedAdjacencyFile(String fileName) {

		Node firstNode = null;
		clear();
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line = b.readLine();
			while(line != null) {
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}

				int separatorInd = line.indexOf(WEIGHTEDSEPARATOR);

				if(separatorInd >= 0) {
					String n1 = line.substring(0,separatorInd);
					String remainder = line.substring(separatorInd+1);
					separatorInd = remainder.indexOf(WEIGHTEDSEPARATOR);

					String n2 = remainder;
					double weight = 0.0;
					String weightString = "";
					if(separatorInd != -1) {
						n2 = remainder.substring(0,separatorInd);
						weightString = remainder.substring(separatorInd+1);
						try {
							weight = Double.parseDouble(weightString);
						} catch(NumberFormatException e) {
							weight = 0;
						}
					}

					Edge e = addAdjacencyEdge(n1,n2,weightString, weight);
					if(firstNode == null) {
						firstNode = e.getFrom();
					}
				} else {
					// no separator, so just add a singleton node
					Node n = addAdjacencyNode(line);
					if(firstNode == null) {
						firstNode = n;
					}
				}
				line = b.readLine();
			}

			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadWeightedAdjacencyFile("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}

		return(firstNode);
	}

	/**
	 * Creates an edge between the nodes with the given labels. This is the unweighted
	 * unlabelled edge version of the method.
	 * @return the created edge, or null if was not created.
	 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel) {

		return(addAdjacencyEdge(fromLabel, toLabel, "", 0.0));
	}

	/**
	 * Creates an edge between the nodes with the given labels. This is the
	 * unlabeled edge version of the method.
	 * @return the created edge, or null if was not created.
	 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel, double edgeWeight) {

		return(addAdjacencyEdge(fromLabel, toLabel, "", edgeWeight));
	}

	/**
	 * Creates an edge between the nodes with the given labels. This is the unweighted version
	 * of the method.
	 * @return the created edge, or null if was not created.
	 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel, String edgeLabel) {

		return addAdjacencyEdge(fromLabel, toLabel, edgeLabel, 0.0);
	}

	/**
	 * Creates an edge between the nodes with the given labels, creating the nodes if needed.
	 * If a node label is empty, the node is not created. If one or both the labels are empty, the
	 * edge is not created. Self sourcing and parallel edges are allowed. Third argument is the
	 * edge label, fourth is the edge weight.
	 * @return the created edge, or null if was not created.
	 */
	public Edge addAdjacencyEdge(String fromLabel, String toLabel, String edgeLabel, double edgeWeight) {

		Node fromNode = null;
		Node toNode = null;

		if(!fromLabel.equals("")) {
			fromNode = addAdjacencyNode(fromLabel);
		}

		if(!toLabel.equals("")) {
			toNode = addAdjacencyNode(toLabel);
		}

		if(fromNode != null && toNode != null) {

			Edge e = new Edge(fromNode, toNode, edgeLabel, edgeWeight);
			addEdge(e);
			return(e);
		}

		return(null);
	}

	/**
	 * Either returns an existing node with the label, or if there is none
	 * creates a node with the label and adds it to the graph.
	 * @return the found or created node, or null if there is more than one node with the label.
	 */
	public Node addAdjacencyNode(String nodeLabel) {

		Node returnNode = null;
		for(Node n : getNodes()) {
			if(nodeLabel.equals(n.getLabel())) {
				if(returnNode != null) {
					return(null);
				}
				returnNode = n;
			}
		}
		if (returnNode != null) {
			return(returnNode);
		}

		Node newNode = new Node(nodeLabel);
		addNode(newNode);

		return(newNode);
	}

	/**
	 * Finds the list of Nodes with the given label.
	 */
	public ArrayList<Node> findNodesWithLabel(String nodeLabel) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : getNodes()) {
			if(nodeLabel.equals(n.getLabel())) {
				ret.add(n);
			}
		}
		return(ret);
	}

	/**
	 * Finds the list of Nodes with the given type.
	 */
	public ArrayList<Node> findNodesWithType(NodeType nodeType) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : getNodes()) {
			if(nodeType == n.getType()) {
				ret.add(n);
			}
		}
		return(ret);
	}


	/**
	 * Finds the list of Nodes with the given label.
	 */
	public ArrayList<Edge> findEdgesWithLabel(String edgeLabel) {

		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(Edge e : getEdges()) {
			if(edgeLabel.equals(e.getLabel())) {
				ret.add(e);
			}
		}
		return(ret);
	}

	/**
	 * Finds the list of Edges with the given type.
	 */
	public ArrayList<Edge> findEdgesWithType(EdgeType edgeType) {

		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(Edge e : getEdges()) {
			if(edgeType == e.getType()) {
				ret.add(e);
			}
		}
		return(ret);
	}

	/**
	 * Finds the list of Nodes with the given label.
	 */
	public ArrayList<Node> findNodesWithScore(double score) {

		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node n : getNodes()) {
			if(score == n.getScore()) {
				ret.add(n);
			}
		}
		return(ret);
	}
	/**
	 * Generates a complete graph of the size of the number of nodes passed.
	 * The old graph gets deleted. The nodes are labeled 1 to the size of the
	 * graph, the edges are not labeled.
	 */
	public void generateCompleteGraph(int nodeNumber) {

		clear();
		if(nodeNumber==1) {
			addAdjacencyNode("1");
		}
		for(int i = 1; i <= nodeNumber; i++) {
			for(int j = 1; j <= nodeNumber; j++) {
				if(i<j) {
					Integer nodei = new Integer(i);
					Integer nodej = new Integer(j);
					addAdjacencyEdge(nodei.toString(),nodej.toString());
				}
			}
		}
	}

	/**
	 * Check to see if two nodes with the given labels are already connected in
	 * the graph.
	 */
	boolean nodeLabelsAlreadyConnected(String label1, String label2) {
		Node n1 = null;
		for(Node n : getNodes()) {
			if(label1.equals(n.getLabel())) {
				n1 = n;
				break;
			}
		}

		if(n1 != null) {
			for(Node connectedN : n1.connectingNodes()) {
				if(label2.equals(connectedN.getLabel())) {
					return true;

				}
			}
		}
		return false;
	}
	
	/**
	 * Generates a random graph of approximately the size of nodes and edges
	 * passed. It does this by generating edges between random nodes, which
	 * are labelled with the relevant numbers. Only these attached nodes get
	 * created, so that the number of nodes may be less than that passed.
	 * The old graph gets deleted.
	 */
	public void generateRandomGraph(int nodeNumber, int edgeNumber, boolean selfSourcing, boolean parallel) {

		clear();
		Random r = new Random();
		for(int i = 0; i < edgeNumber; i++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!parallel) {
				if(nodeLabelsAlreadyConnected(node1.toString(),node2.toString())) {
					continue;
				}
			}
			if(!node1.equals(node2) || selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			}
		}
	}

	public void generateRandomGraph(int nodeNumber, int edgeNumber) {
		generateRandomGraph(nodeNumber,edgeNumber,false,true);
	}
	
	/**
	 * Generates a random graph of exactly the size of nodes and edges
	 * passed, by creating singleton nodes.
	 */
	public void generateRandomGraphExact(int nodeNumber, int edgeNumber, boolean selfSourcing) {

		clear();
		for(int i = 0; i < nodeNumber; i++) {
			Integer node = new Integer(i);
			addNode(new Node(node.toString()));
		}
		Random r = new Random();
		for(int i = 0; i < edgeNumber; i++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!node1.equals(node2) || selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			}
		}
	}

	/**
	 * Randomizes the graph node locations to be within the rectangle defined
	 * by the parameters.
	 */
	public void randomizeNodePoints(Point topleft, int width, int height) {
		Random r = new Random(System.currentTimeMillis());
		for(Node n : getNodes()) {
			int x = r.nextInt(width);
			int y = r.nextInt(height);
			n.setCentre(new Point(topleft.x+x,topleft.y+y));
		}
		return;
	}
	
	/** Compares the graph based on node labels and the node labels that edges
	 * connect to, it works as a way of comparing graphs when nodes have
	 * unique labels and no edge labels. An isomorphism test is required where
	 * duplicate labels are present or only the topology is of interest.
	 * @return true if the graphs are equal by node label and edge connections,
	 * false otherwise.
	 */
	public boolean equalsByNodeLabel(Graph g) {
	
		String n1[] = new String[getNodes().size()];

		// first test that the two graphs have the same number of node labels
		int i1 = 0;
		for(Node n : this.getNodes()) {
			n1[i1] = n.getLabel();
			i1++;
		}

		String n2[] = new String[g.getNodes().size()];

		int i2 = 0;
		for(Node n : g.getNodes()) {
			n2[i2] = n.getLabel();
			i2++;
		}

		Arrays.sort(n1);
		Arrays.sort(n2);

		if (!Arrays.equals(n1,n2)) {
			return(false);
		}

		// next test that there are the same number of edges going to and from nodes
		// with the same labels
		String e1[] = new String[this.getEdges().size()];

		int j1 = 0;
		for(Edge e : this.getEdges()) {
			e1[j1] = new String(e.getFrom().getLabel()+":"+e.getTo().getLabel());
			j1++;
		}

		String e2[] = new String[g.getEdges().size()];

		int j2 = 0;
		for(Edge e : g.getEdges()) {
			e2[j2] = new String(e.getFrom().getLabel()+":"+e.getTo().getLabel());
			j2++;
		}

		Arrays.sort(e1);
		Arrays.sort(e2);

		if (!Arrays.equals(e1,e2)) {
			return(false);
		}

		return(true);
	}

	/**
	 * Finds the first node in the graph with the given label.
	 * @return null if there is no such node.
	 */
	public Node firstNodeWithLabel(String nodeLabel) {

		Iterator ni = getNodes().iterator();
		while(ni.hasNext()) {
			Node n = (Node)ni.next();
			if(nodeLabel.equals(n.getLabel())) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Finds the first node in the graph at the given point.
	 * @return null if there is no such node.
	 */
	public Node firstNodeAtPoint(Point point) {

		Iterator ni = getNodes().iterator();
		while(ni.hasNext()) {
			Node n = (Node)ni.next();
			if(n.getX() == point.x && n.getY() == point.y) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Find the shortest path between two nodes. Treats the graph as undirected and
	 * unweighted.
	 * <p>
	 * This is a breadth first search through the graph. A queue is used for this,
	 * FIFO - First In First Out. Add the start node to the queue, then keep taking
	 * the head of the queue and adding the heads neighbours until the end node
	 * has been found. In this case we also have to keep track of the shortest paths to
	 * the nodes we have detected using the path facility provided in the node class.
	 * <p>
	 * There is no actual Queue class or interface in Java, but anything which
	 * implements 'List' will do, as its possible to add to the end of the list, and
	 * treat the head of the list as index 0, and use the remove methods.
	 * Here we are using a ArrayList, which implements 'List'.
	 *
	 * @return null if there is no path between the nodes, if there is a path it returns
	 * a list of nodes representing the path, including both end nodes.
	 */
	public ArrayList<Node> unweightedShortest(Node startNode, Node goal) {

		Node head = null;
		ArrayList<Node> path = new ArrayList<Node>();
		ArrayList<Node> queue = new ArrayList<Node>();
		HashMap<Node,ArrayList<Node>> paths = new HashMap<Node,ArrayList<Node>>();
		for(Node n : getNodes()) {
			paths.put(n,null);
		}
			
		queue.add(startNode);
		paths.put(startNode,path);

		while(!queue.isEmpty() && head != goal) {
			head = queue.remove(0);

			ArrayList<Node> headPath = paths.get(head);
			path = new ArrayList<Node>(headPath);
			path.add(head);

			ArrayList<Node> neighbours = head.connectingNodes();
			for(Node n : neighbours) {
				if(paths.get(n) == null) {
					paths.put(n,path);
					queue.add(n);
				}
			}
		}

		if (head == goal) {
			ArrayList<Node> result = new ArrayList<Node>(paths.get(goal));
			result.add(goal);
			return result;
		}
		return null;
	}

	/**
	 * Find a Euler circuit in the graph, by repeatedly tracing closed trails in the graph
	 * using {@link #trace}
	 * @return a list of nodes which indicates a circuit, or null if no trail is present.
	 */
	public ArrayList<Node> euler() {

		if (!eulerian()) {
			return(null);
		}

		setEdgesVisited(false);

		// get any node
		Node n;
		Iterator ni = getNodes().iterator();
		if(ni.hasNext()) {
			n = (Node)ni.next();
			if(!ni.hasNext() && getEdges().size() == 0) {
				// one node and no edges so return the empty list
				return(new ArrayList<Node>());
			}
		} else {
			// no nodes so return the empty list
			return(new ArrayList<Node>());
		}

		ArrayList<Node> K = trace(n);

		int currentIndex = 0;
		while (currentIndex < K.size()) {

			Node currentNode = (Node)K.get(currentIndex);
			if(currentNode.unvisitedConnectingEdges().size() > 0) {
				ArrayList<Node> C = trace(currentNode);
				K.remove(currentIndex);
				K.addAll(currentIndex,C);
			}
			currentIndex++;
		}

		return(K);
	}	

	/**
	 * Returns a list of nodes that form a circuit. In this case it is
	 * a loop that does not use the same edge twice and where the edge has
	 * not already been visited. It finds a circuit that includes all the
	 * unvisited edges from the argument node by only finishing when the
	 * argument node has no more unconnected edges. It maintains the
	 * visited edges, but not the nodes  as it goes. It does not reset
	 * the visited edges at the start of the method because it is expected
	 * to be called several times to build up the tour as designed for use
	 * by a Euler circuit method. The graph must be eulerian.
	 * @return a list of nodes which indicates a circuit.
	 */
	public ArrayList<Node> trace(Node startNode) {

		ArrayList<Node> C = new ArrayList<Node>();
		Node currentNode = startNode;
		C.add(currentNode);
		boolean moreEdges = true;
		while(moreEdges) {
			Iterator ei = currentNode.unvisitedConnectingEdges().iterator();
			if(!ei.hasNext()) {
				moreEdges = false;
			} else {

				Edge e = (Edge)ei.next();
				e.setVisited(true);
				currentNode = e.getOppositeEnd(currentNode);
				C.add(currentNode);
			}
		}

		return(C);
	}

	/**
	 * Test for the graph having a euler tour. Graph must be connected, and all
	 * nodes have even degree.
	 */
	public boolean eulerian() {

		if(!nodesHaveEvenDegree()) {
			return(false);
		}
		if(!connected()) {
			return(false);
		}
		return(true);
	}

	/**
	 * Loads in a tour in list string format and returns the equivalent ArrayList
	 * If the file contains the string "Fail" then the tour is set to null.
	 */
	public ArrayList<Node> loadTour(String fileName) {

		String tourString = "";
		ArrayList<Node> tour = new ArrayList<Node>();

		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line = b.readLine();
			while(line != null) {

				if(line !=null && !line.equals("")) {
					tourString = line;
				}

				line = b.readLine();
			}

			if(line !=null && !line.equals("")) {
				tourString = line;
			}
			b.close();
			StringTokenizer st = new StringTokenizer(tourString,"	 ,[]");

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if(token.compareToIgnoreCase("FAIL") == 0) {
					tour = null;
					break;
				} else {
					// this bit finds a node with the appropriate label
					boolean fail = true;
					for(Node n : getNodes()) {
						if(token.equals(n.getLabel())) {
							tour.add(n);
							fail = false;
							break;
						}
					}
					if (fail) {
						System.out.println("ERROR WHEN LOADING TOUR, node "+token+" not found in graph");
						tour = null;
					}
				}
			}

		} catch(IOException e){
			System.out.println("An IO exception occured when executing EulerTest.loadTour("+fileName+") in EulerTest.java: "+e+"\n");
			System.exit(1);
		}

		return tour ;
	}

	/**
	 * Saves the given tour in the List string format.
	 */
	public void saveTour(String fileName, AbstractList tour) {
		saveTour(fileName, tour, true);
	}

	/**
	 * Saves the given tour in the List string format, if the boolean is false
	 * only the string "Fail" is written.
	 */
	public void saveTour(String fileName, AbstractList tour, boolean succeed) {

		try {
			String outString;
			if (succeed == false) {
				outString = "Fail";
			} else {
				outString = tour.toString();
			}
			BufferedWriter b = new BufferedWriter(new FileWriter(fileName));
			b.write(outString);
			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing saveTour("+fileName+") in Graph.java: "+e+"\n");
			System.exit(1);
		}
	}

	/**
	 * Checks for existence of tour, array list must just be null if there
	 * is no tour.
	 *
	 * NOTE BUG - [A] can be given as a correct tour for the graph with
	 * just the node A and no edges. The correct result is only [].
	 *
	 * @return true if the tour if it is in the graph, false if it is not.
	 */
	public boolean eulerTourInGraph(ArrayList tour) {

		if (!eulerian()) {
			// not a eulerian graph
			if (tour == null) {
				return(true);
			} else {
				return(false);
			}
		}

		if(tour == null) {
			// no tour given, but graph is eulerian
			return(false);
		}

		setEdgesVisited();

		Node first = null;
		Node prev = null;
		Node next = null;

		ArrayList<Edge> edgeSet;
		Iterator ti = tour.iterator();
		if(!ti.hasNext() && (getNodes().size() > 1)) {
			// Tour is empty, but the graph has a node
			return(false);
		}

		while(ti.hasNext()) {
			prev = next;
			next = (Node)ti.next();
			if(!containsNode(next)) {
				// Tour node not found in the graph
				return(false);
			}

			if(prev == null) {
				first = next;
			} else {
				edgeSet = prev.unvisitedConnectingEdges();
				Edge connectingEdge = null;
				for(Edge e : edgeSet) {
					if(e.getOppositeEnd(prev) == next) {

						e.setVisited(true);
						connectingEdge = e;
						break;
					}
				}
				if(connectingEdge == null) {
					// No more unvisited edges connecting the two nodes
					return(false);
				}
			}
		}

		for(Edge edge : getEdges()) {
			if(edge.getVisited() == false) {
				// not all edges have been visited
				return(false);
			}
		}

		if (first != next) {
			// start node not last node
			return(false);
		}

 		return(true);
	}

	/**
	 * Generate Eulerian graph by randomly generating a graph of the size given,
	 * then adding and removing edges till its eularian. The delete and add is random,
	 * but attempts to be a bit clever, tending towards nodes of even degree. Clearly
	 * too small number of edges might cause problems, but it will
	 * get there in the end. The selfSourcing parameter indicates if edges sourced and
	 * targetted at the same node are allowed. The parallel parameter indicates if
	 * parallel edges are required. However, it must be noted that to make the graph
	 * Eulerian, some parallel edges may be added.
	 */
	public void generateRandomEulerGraph(int nodeNumber, int edgeNumber, boolean selfSourcing, boolean parallel) {

		clear();

		Node newNode = null;
		StringBuffer newString = new StringBuffer();
		for(int i = 0; i < nodeNumber; i++) {
			newString.setLength(0);
			newString.append(i);
			newNode = new Node(newString.toString());
			addNode(newNode);
		}

		Random r = new Random();
		for(int j = 0; j < edgeNumber; j++) {
			Integer node1 = new Integer(r.nextInt(nodeNumber));
			Integer node2 = new Integer(r.nextInt(nodeNumber));
			if(!parallel) {
				if(nodeLabelsAlreadyConnected(node1.toString(),node2.toString())) {
					continue;
				}
			}
			if(selfSourcing) {
				addAdjacencyEdge(node1.toString(),node2.toString());
			} else {
				if(!node1.equals(node2)) {
					addAdjacencyEdge(node1.toString(),node2.toString());
				}
			}
		}

		// tidy up the graph to ensure the graph is connected and that the
		// nodes have even degree

		// make connected
		setNodesVisited();

		boolean connected = false;
		ArrayList<Node> queue = new ArrayList<Node>();
		queue.add(newNode);
		Node head = null;

		while(!connected) {
			while(!queue.isEmpty()) {
				head = (Node)queue.remove(0);
				head.setVisited(true);
				ArrayList<Node> neighbours = head.unvisitedConnectingNodes();
				queue.addAll(neighbours);
			}

			ArrayList unvisited = unvisitedNodes();
			if (unvisited.size() == 0) {
				connected = true;
			} else {
				Iterator ni = unvisited.iterator();
				Node n = (Node)ni.next();
				addEdge(new Edge(head,n));
				queue.add(n);
			}
		}

		// add edges to nodes with odd degree
		Node source = null;
		for(Node n : getNodes()) {
			int degree = n.degree();

			if(degree%2 != 0) {
				if(source == null) {
					source = n;
				} else {
					addEdge(new Edge(source,n));
					source = null;
				}
			}
		}
	}
	
	public void generateRandomEulerGraph(int nodeNumber, int edgeNumber) {
		generateRandomEulerGraph(nodeNumber, edgeNumber, false, true);
	}
	
	/**
	 * Adds random weights to the edges of a graph, value is between
	 * (inclusive) the arguments.
	 */
	public void randomlyWeightGraph(int from, int to) {
		Random r = new Random();
		for(Edge e : getEdges()) {
			int value = from+r.nextInt(to-from);
			e.setWeight(value);
		}
	}


	/**
	 * Equality of graphs. Returns a mapping if this graph is equal
	 * to the given graph.
	 *
	 * @return true if there is an equality with the
	 * given graph, null if is not.
	 */
	public boolean isomorphic(Graph g) {

		// ensure that the same graph returns true
		if(g == this) {
			return(true);
		}

		ArrayList<Node> nodes1 = new ArrayList<Node>(getNodes());
		ArrayList<Node> nodes2 = new ArrayList<Node>(g.getNodes());
		ArrayList<Edge> edges1 = new ArrayList<Edge>(getEdges());
		ArrayList<Edge> edges2 = new ArrayList<Edge>(g.getEdges());

		int node1Size = nodes1.size();
		int node2Size = nodes2.size();
		int edge1Size = edges1.size();
		int edge2Size = edges2.size();


		if(node1Size != node2Size) {
System.out.println("Not isomorphic: node collections are different sizes");
			return(false);
		}
		if(edge1Size != edge2Size) {
System.out.println("Not isomorphic: edge collections are different sizes");
			return(false);
		}
		if(node1Size == 0 && node2Size == 0) {
System.out.println("Isomorphic: empty graphs");
			return(true);
		}

		int maxDegree2 = 0;
		ArrayList<ArrayList<Node>> degrees2 = new ArrayList<ArrayList<Node>>();

		for(Node n: nodes2) {
			int degree = n.degree();
			// if the list is not as big as the degree, there cant
			// already be a degree of that size
			if(degrees2.size() <= degree) {
				while(degrees2.size() <= degree) {
					degrees2.add(null);
				}
				ArrayList<Node> newL = new ArrayList<Node>();
				newL.add(n);
				degrees2.ensureCapacity(degree+1);
				degrees2.set(degree, newL);
			} else if(degrees2.get(degree) == null) {
				ArrayList<Node> newL = new ArrayList<Node>();
				newL.add(n);
				degrees2.set(degree, newL);
			} else {
				ArrayList<Node> oldL = degrees2.get(degree);
				oldL.add(n);
			}
			if(degree > maxDegree2) {
				maxDegree2 = degree;
			}
		}

		int[] degreeCheck = new int[maxDegree2+1];
		Arrays.fill(degreeCheck,0);

		for(Node n : nodes1) {
			int degree = n.degree();
			if(degree > maxDegree2) {
System.out.println("Not isomorphic: bigger degree in second graph");
				return(false);
			}
			degreeCheck[degree]++;
		}

		//check degrees
		for (int i = 0; i < degreeCheck.length; i++) {

			if (degrees2.size() <= i) {
				if (degreeCheck[i] == 0) {
					continue;
				} else {
System.out.println("Not isomorphic: graphs have different number of nodes with degree " + i);
					return(false);
				}
			}

			ArrayList list2 = (ArrayList)degrees2.get(i);
			if(list2 == null) {
				if(degreeCheck[i] != 0) {
System.out.println("Not isomorphic: graphs have different number of nodes with degree " + i);
					return(false);
				}
			} else {
				if(degreeCheck[i] != list2.size()) {
System.out.println("Not isomorphic: graphs have different number of nodes with degree " + i);
					return(false);
				}
			}
		}

		// do the backtracking search
		
		setNodesMatches(nodes1,null);
		setNodesMatches(nodes2,null);

		// get the possible matches for each node
		// each array member is a list of possible matchs in graph2
		// for the corresponding node in graph1

		ArrayList<BacktrackIso> state = new ArrayList<BacktrackIso>(node1Size);

		for(Node n1 : nodes1) {
			ArrayList<Node> equalDegrees = new ArrayList<Node>();

			for(Node n2 : nodes2) {
				if(n1.connectingNodes().size() == n2.connectingNodes().size()) {
					equalDegrees.add(n2);
				}
			}
			state.add(new BacktrackIso(equalDegrees,0));
		}

		int currentProgress = 0;

		while((currentProgress < node1Size) && (currentProgress != -1)) {
			Node currentNode= (Node)nodes1.get(currentProgress);
			BacktrackIso currentState = (BacktrackIso)state.get(currentProgress);

			ArrayList allPossibles = currentState.possibles;
			java.util.List currentPossibles = allPossibles.subList(currentState.pos,allPossibles.size());

			boolean foundMatch = false;
			Iterator matchi = currentPossibles.iterator();
			while(!foundMatch && matchi.hasNext()) {

				Node tryNode = (Node)matchi.next();
				currentState.pos++;

//A				currentNode.setMatch(tryNode);
//A				tryNode.setMatch(currentNode);

				if (isAnUndirectedMatch(currentNode,tryNode)) {
					// set up matched attributes and return true
					foundMatch = true;
					currentProgress++;
//***A replaced this
					currentNode.setMatch(tryNode);
					tryNode.setMatch(currentNode);
				}
//*** to here
//A				} else {
//A					currentNode.setMatch(null);
//A					tryNode.setMatch(null);
//A				}
// problem seems to be in matching, cant check self sourcing properly due
// to not being flagged as matched, so not checked. not clear what
// the answer is - check self sourcing sepearately in isAnUndirectedMatch?
			}

			// backtracking here
			if (!foundMatch) {
				currentProgress--;
				if(currentProgress != -1) {
					Node unMatchedNode = (Node)nodes1.get(currentProgress);
					Node oppositeNode = (Node)unMatchedNode.getMatch();
					unMatchedNode.setMatch(null);
					oppositeNode.setMatch(null);
					currentState.pos = 0;
				}

			}

		}

System.out.println("This match " +nodeMatchesToString());
System.out.println("Argument g "+g.nodeMatchesToString());

		if (currentProgress != node1Size) {
System.out.println("Not isomorphic: backtracking search failed ");
			return(false);
		}
System.out.println("Isomorphic");
		return(true);
	}

	/** used to store the state of backtracking in the isomorphism algorithm */
	protected class BacktrackIso {
		public ArrayList<Node> possibles;
		public int pos = 0;
		public BacktrackIso() {}
		public BacktrackIso(ArrayList<Node> list, int inPos) {
			possibles = list;
			pos = inPos;
		}
	}
 
	/**
	 * If the passed node2 is a match for node1, taking into account
	 * the current matched state of neighbouring nodes.
	 * then this method returns true, false if it is not a possible match.
	 * Accounts for current connections to matched nodes, both must have
	 * the same number of self sourcing edges. This method takes
	 * no account of node or edge lables or edge direction.
	 */
	protected boolean isAnUndirectedMatch(Node node1, Node node2) {

		if (node1.getMatch() != null) {
			return(false);
		}
		if (node2.getMatch() != null) {
			return(false);
		}

		// this should not be needed given a reasonably efficient
		// filtering before the backtracking
		if(node1.connectingNodes().size() != node2.connectingNodes().size()) {
			return(false);
		}

		ArrayList<Node> matched1 = setNodesNeighboursScoresForIsomorphism(node1);
		ArrayList<Node> matched2 = setNodesNeighboursScoresForIsomorphism(node2);

		// check current matches are valid, i.e. same number and connect to non
		// conflicting nodes compare the scores of the two sets
		// this deals with self sourcing edges as well as
		// normal matches

		boolean ret = true;
		for(Node matchedNode : matched1) {
			Node oppositeNode = (Node)matchedNode.getMatch(); 

			if (matchedNode.getScore() != oppositeNode.getScore()) {
				ret = false;
			}

		}

		setNodesScores(matched1, 0.0);
		setNodesScores(matched2, 0.0);

		return(ret);
	}

	/**
	 * This method uses the score attribute to find the number of connecting
	 * edges to each matched node. It returns a set of the nodes which have
	 * had scores set (the matched nodes). This collects a repeated bit of
	 * code used within the isomorphism algorithm.
	 */
	protected ArrayList<Node> setNodesNeighboursScoresForIsomorphism(Node node) {

		ArrayList<Node> neighbours = node.connectingNodes();
		ArrayList<Edge> edgeCollection = node.connectingEdges();

		// cope with self sourcing edges
		neighbours.add(node);

		setNodesScores(neighbours,0.0);

		ArrayList<Node> matched = new ArrayList<Node>();
		for(Edge theEdge : edgeCollection) {
			Node theNode = theEdge.getOppositeEnd(node);

			if(theNode.getMatch() != null) {
				// set up matched attributes and return true
				double score = theNode.getScore();
				theNode.setScore(score+1);
				// add the node to the collection of matched nodes
				matched.add(theNode);
			}
		}

		return(matched);
	}

	/**
	 * Prints the match pairs for nodes
	 */
	public String nodeMatchesToString() {

		String ret = new String("[");

		Iterator ni = nodes.iterator();
		while(ni.hasNext()) {
			Node n = (Node)ni.next();
			ret = ret+"<"+n.toString()+","+n.getMatch()+">";
			if(ni.hasNext()) {
				ret = ret+";";
			}
		}
		ret = ret+"]";

		return ret;
	}

	/**
	 * Adds unique identifiers to the edges of a graph, integers from 1.
	 * Can be used before calling TSP in order to cope with parallel edges.
	 */
	public void identifyEdgeLabels() {
		int count = 1;
		for(Edge e : getEdges()) {
			Integer i = new Integer(count);
			e.setLabel(i.toString());
			count++;
		}
	}

	/** Find a Minimum Spanning Tree using Kruskals algorithm. Outline:
	 * sort the edges by weight, and add them to the MST if a loop is not formed.
	 * uses {@link UnionFind} as the loop detector.
	 * Deals with parallel and self sourcing edges.
	 * @return The edges in the MST, or null if there is no MST.
	 */
	public ArrayList<Edge> kruskal() {

		EdgeWeightComparator eComp = new EdgeWeightComparator();

		ArrayList<Node> ns = new ArrayList<Node>(getNodes());
		ArrayList<Edge> es = new ArrayList<Edge>(getEdges());
		Collections.sort(es,eComp);
		ArrayList<Edge> mst = new ArrayList<Edge>();

		UnionFind uf = new UnionFind(ns.size());

		Iterator ei = es.iterator();
		while((mst.size() < ns.size()-1) && ei.hasNext()) {
			Edge e = (Edge)ei.next();
			int nFrom = ns.indexOf(e.getFrom());
			int nTo = ns.indexOf(e.getTo());
			if(!uf.find(nFrom,nTo)) {
				mst.add(e);
				uf.union(nFrom,nTo);
			}
		}

		if(mst.size() < ns.size()-1) {
			return(null);
		}

		return(mst);
	}

	/** Find a Minimum Spanning Tree using Prims algorithm. Outline:
	 * pick the nearest node in the PQ to the current nodes in the
	 * mst, add unseen neighbouring nodes (those with a score of -1.0)
	 * to the PQ, change the priority of seen neighbouring nodes in the
	 * pq. The nodes in the PQ are those with the visited flag set to true.
	 * Uses the visited field of nodes to indicate whether a node is in
	 * mst - if it is false and the score is not -1.0 then it is in the mst.
	 * Deals with parallel and self sourcing edges.
	 * Current pq implementation is a ArrayList to easily cope with parallel edges.
	 * This is a bit slower than optimal- a sorted collection might be better.
	 * @return the edges in the MST, or null if there is no MST
	 */
	public ArrayList<Edge> prim() {

		if(getNodes().size() == 0) {
			return(new ArrayList<Edge>());
		}

		NodeScoreComparator nComp = new NodeScoreComparator();

		int mstMaxSize = getNodes().size()-1;

		setNodesScores(-1.0);
		setNodesVisited(false);

		// get any node to start from
		Node startNode = null;
		Iterator niStart = getNodes().iterator();
		if(niStart.hasNext()) {
			startNode = (Node)niStart.next();
		}
		startNode.setScore(0.0);

		ArrayList<Edge> mst = new ArrayList<Edge>();
		ArrayList<Node> pq = new ArrayList<Node>();

		for(Edge e : startNode.connectingEdges()) {
			Node n = e.getOppositeEnd(startNode);
			if(n != startNode) {
				if(n.getScore() == -1) {
					n.setScore(e.getWeight());
				} else {
					// in case of parallel edges, get the cheapest
					if(n.getScore() > e.getWeight()) {
						n.setScore(e.getWeight());
					}
				}
				n.setVisited(true);
				pq.add(n);
			}
		}

		while(pq.size() > 0) {

			// pq head removal here. This is a bit inefficient, the pq is iterated
			// through in linear manner twice. Could be improved with a sorted collection
			Node currentNode = Collections.min(pq,nComp);
			pq.remove(currentNode);
			currentNode.setVisited(false);

			boolean addedToMST = false;
			for(Edge e : currentNode.connectingEdges()) {
				// this bit finds the minimum edge and adds it to the pq
				// also check that this edge is not self sourcing and connects to the MST
				if((!addedToMST) && e.getWeight() == currentNode.getScore()) {
					Node otherEnd = e.getOppositeEnd(currentNode);
					if(otherEnd != currentNode && !otherEnd.getVisited()) {
						mst.add(e);
						addedToMST = true;
					}
				}

				Node n = e.getOppositeEnd(currentNode);
				if(n != currentNode) {
					if(n.getScore()==-1.0) {
						n.setScore(e.getWeight());
						n.setVisited(true);
						pq.add(n);
					} else {

						if(n.getVisited() && n.getScore() > e.getWeight()) {
							n.setScore(e.getWeight());
							// if changing to a sorted PQ, change the order of n here
						}
					}
				}
			}
		}
		// case where the pq was empty, but the mst was not complete - must be
		// due to a unconnected graph
		if(mst.size() < mstMaxSize) {
			mst = null;
		}

		return(mst);
	}
	
/** Used by tsp. */
private double bestCount = 0.0;
/** Used by tsp. */
private ArrayList<Edge> bestPath = null;
/** Used by tsp. */
private Node start = null;

	/**
	 * The Travelling Salesman Problem optimal solution buy brute force. This is a
	 * liberal interpretation of the tsp, where edges may be traversed more than once.
	 * Works with parallel edges only if the edges have unique labels.
	 * @return a list containing the edges in the tsp, or null if the graph has no
	 * tsp solution.
	 */
	public ArrayList<Edge> tsp() {

		if(!connected()) {
			return(null);
		}
		bestCount = sumEdgeWeights()*2;
		bestPath = new ArrayList<Edge>();

		Iterator ni = nodes.iterator();
		if(ni.hasNext()) {
			start = (Node)ni.next();
		} else {
			return(bestPath);
		}

		tspRec(new ArrayList<Edge>(),0.0,start);
		return(bestPath);
	}

	/** Recursive tsp finder. */
	private void tspRec(ArrayList<Edge> edgePath, double count, Node node) {

		for(Edge e : node.connectingEdges()) {
			double nextCount = count + e.getWeight();

			// only go down the edge there is a chance of improvement
			if((nextCount <= bestCount) && !e.selfSourcing()) {

				Node nextNode = e.getOppositeEnd(node);

				ArrayList<Edge> nextEdgePath = new ArrayList<Edge>(edgePath);
				nextEdgePath.add(e);

				// if this is a full tour, record it as the best, else go on to the next iteration
				if(nextNode == start && containsAllNodes(nextEdgePath,getNodes())) {
					bestCount = nextCount;
					bestPath = nextEdgePath;
				} else {

					//edge may have already been traversed once this tour
					if(!containsConnections(edgePath,e,2)) {
						tspRec(nextEdgePath,nextCount,nextNode);
					}	
				}
			}
		}
	}

	/**
	 * Used in tsp. Checks that all the nodes are in the edge list (i.e. if the
	 * edge collection is a tour, check if it is a tour of all the nodes).
	 */
	protected boolean containsAllNodes(Collection<Edge> edgeCollection, Collection<Node> nodeCollection) {
		ArrayList<Node> nodeList = new ArrayList<Node>(nodeCollection);
		for(Edge e : edgeCollection) {
			nodeList.remove(e.getFrom());
			nodeList.remove(e.getTo());
		}		
		if(nodeList.size() != 0) {
			return(false);
		}
		return(true);
	}

	/**
	 * Used in tsp. Finds an edge, based on equality of
	 * connections (directed) and label.
	 */
	protected boolean containsConnections(ArrayList<Edge> track, Edge e, int number) {

		int count = 0;
		for(Edge trackE : track) {
			if((trackE.getFrom()==e.getFrom() && trackE.getTo()==e.getTo()) || (trackE.getFrom()==e.getTo() && trackE.getTo()==e.getFrom())) {
				if(trackE.getLabel() == e.getLabel()) {
					count++;
				}
			}
		}
		if(count < number) {
			return(false);
		}
		return(true);
	}

	/**
	 * Finds a node within the passed point, or returns null
	 * if the argument point is not over a node. The padding
	 * refers to the distance the point can be from the 
	 * node, and must be greater than 0. If there is more
	 * than one node, it finds the
	 * last one in the collection, which hopefully should
	 * be the one on top of the display.
	 */
	public Node getNodeNearPoint(Point p, int padding) {

		Node returnNode = null;

		for(Node n : getNodes()) {
			Shape nodeShape = n.shape();
			Rectangle r = new Rectangle(p.x-padding,p.y-padding,padding*2,padding*2);
			if(nodeShape.intersects(r)) {
				returnNode = n;
			}
		}
		return(returnNode);
	}

	/**
	 * Finds an edge close to the passed point, or returns null
	 * if the argument point is not over an edge. The padding
	 * refers to the distance the point can be from the 
	 * edge, and must be greater than 0. If there is more
	 * than one edge, it finds the
	 * last one in the collection, which hopefully should
	 * be the one on top of the display.
	 */
	public Edge getEdgeNearPoint(Point p, int padding) {
		/*
		// replaced with code to take into account edge bends
		Edge returnEdge = null;

		for(Edge e : getEdges()) {
			Shape edgeShape = e.shape();
			Rectangle r = new Rectangle(p.x-padding,p.y-padding,padding*2,padding*2);
			if(edgeShape.intersects(r)) {
				returnEdge = e;
			}
		}
		return(returnEdge);
		*/

		for(Edge e : getEdges()) {
			ArrayList<Point> bends = e.getBends();
			Point nextPoint = e.getFrom().getCentre();
			if(bends != null && bends.size() !=0) {
				for(Point bend : bends) {
					double distance = pjr.graph.Util.pointLineDistance(p, nextPoint, bend);
					if(distance <= padding) {
						return e;
					}
					nextPoint = bend;
				}
			}
			double distance = pjr.graph.Util.pointLineDistance(p, nextPoint, e.getTo().getCentre());
			if(distance <= padding) {
				return e;
			}
		}
		
		return null;
	}

	/**
	* Finds the closest node to the point, or returns null
	* if there are no nodes in the graph.
	*/
	public Node closestNode(Point p) {
		return closestNode(p,getNodes());
	}

	/**
	* Finds the closest node in the collection to the point, or returns null
	* if there are no nodes in the collection.
	*/
	public Node closestNode(Point p, Collection<Node> nodeCollection) {

		Node returnNode = null;

		double closestDistance = Double.MAX_VALUE;

		for(Node n : nodeCollection) {
			Point centre = n.getCentre();
			double distance = centre.distance(p);

			if(distance < closestDistance) {
				returnNode = n;
				closestDistance = distance;
			}
		}
		return(returnNode);
	}

	
	/**
	* Finds the centre of the graph, based on forming a rectangle
	* around the limiting nodes and edge bends in the graph.
	*/
	public Point findCentre() {

		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;

		for(Node node : getNodes()) {
			if(node.getX() > maxX) {
				maxX = node.getX();
			}
			if(node.getX() < minX) {
				minX = node.getX();
			}
			if(node.getY() > maxY) {
				maxY = node.getY();
			}
			if(node.getY() < minY) {
				minY = node.getY();
			}
		}

		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				if(point.x > maxX) {
					maxX = point.x;
				}
				if(point.x < minX) {
					minX = point.x;
				}
				if(point.y > maxY) {
					maxY = point.y;
				}
				if(point.y < minY) {
					minY = point.y;
				}
			}
		}

		
		int x = minX + (maxX - minX)/2;
		int y = minY + (maxY - minY)/2;

		Point ret = new Point(x,y);
		return ret;
	}

	/**
	 * Snaps the nodes and edge bends to the given grid. Does not deal with
	 * overlapping items.
	 */
	public void snapToGrid(int gridX,int gridY) {

		for(Node node : getNodes()) {

			node.setX(getNearestGridPoint(node.getX(),gridX));
			node.setY(getNearestGridPoint(node.getY(),gridY));
		}

		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = getNearestGridPoint(point.x,gridX);
				point.y = getNearestGridPoint(point.y,gridY);
			}
		}
	}

	static public int getNearestGridPoint(int coordinate,int grid) {

		int remainder = coordinate%grid;
		int ret = coordinate - remainder;
		if(remainder > grid/2) {
			ret = ret+ grid;
		}
		return ret;
	}

	/** returns the list of nodes with degree equal to or less than the given parameter */
	public ArrayList<Node> getNodesWithFewEdges(int edgeNumber) {

		ArrayList<Node> ret = new ArrayList<Node>();
		
		for(Node n : getNodes()) {
			if(n.connectingEdges().size() <= edgeNumber) {
				ret.add(n);
			}
		}
		return ret;
	}
		
	/** returns the list of nodes with degree equal to or less than the given parameter */
	public int edgeIntersections() {
		int count = 0;
		for(Edge e1 : getEdges()) {
			for(Edge e2 : getEdges()) {
				if(e1 == e2) {
					continue;
				}
				if(e1.straightLineIntersects(e2)) {
					count++;
				}
			}
		}
		return count/2;
	}
	
	
	/** Centre the graph on the given point */
	public void centreOnPoint(int centreX, int centreY) {
		Point graphCentre = findCentre();
	
		int moveX = centreX - graphCentre.x;
		int moveY = centreY - graphCentre.y;
		
		moveGraph(moveX,moveY);
	}
	
	

	/** Scales the graph on the graph centre. */
	public void scale(double multiplier) {

		if(multiplier == 0.0) {
			return;
		}

		Point graphCentre = findCentre();

		for(Node node : getNodes()) {
			node.setX(pjr.graph.Util.scaleCoordinate(node.getX(),graphCentre.x,multiplier));
			node.setY(pjr.graph.Util.scaleCoordinate(node.getY(),graphCentre.y,multiplier));
		}

		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = pjr.graph.Util.scaleCoordinate(point.x,graphCentre.x,multiplier);
				point.y = pjr.graph.Util.scaleCoordinate(point.y,graphCentre.y,multiplier);
			}
		}
	}
	
	
	/** Move all the nodes and edge bends by the values given */
	public void moveGraph(int moveX, int moveY) {
	
		for(Node node : getNodes()) {
			node.setX(node.getX()+moveX);
			node.setY(node.getY()+moveY);
		}
	
		for(Edge edge : getEdges()) {
			for(Point point : edge.getBends()) {
				point.x = point.x+moveX;
				point.y = point.y+moveY;
			}
		}
	}

	
	/**
	 * Scales and centers the graph so that it is maximized
	 * in the given rectangle. This does not change the
	 * aspect ratio of the graph, it is either maximized
	 * in x or y directions.
	 */
	public void fitInRectangle(int x1, int y1, int x2, int y2) {
		int width = findWidth();
		int height = findHeight();
		int rectX1 = x1;
		int rectX2 = x2;
		if(rectX1 > rectX2) {
			rectX1 = x2;
			rectX2 = x1;
		}
		int rectY1 = y1;
		int rectY2 = y2;
		if(rectY1 > rectY2) {
			rectY1 = y2;
			rectY2 = y1;
		}
		int requiredX = rectX2-rectX1;
		int requiredY = rectY2-rectY1;
		
		int centreX = rectX1 + requiredX/2;
		int centreY = rectY1 + requiredY/2;

		double xRatio = requiredX/(double)width;
		double yRatio = requiredY/(double)height;
		// scale to the ratio that will not take the graph over the limits
		double scaleFactor = xRatio;
		if(yRatio < xRatio) {
			scaleFactor = yRatio;
		}
		
		scale(scaleFactor);
		centreOnPoint(centreX, centreY);
		
	}
	
	
	/** The width of the graph */
	public int findWidth() {
		if(getNodes().size() == 0) {
			return 0;
		}
		
		int maxX = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		
		for(Node n : getNodes()) {
			if(n.getX() < minX) {
				minX = n.getX();
			}
			if(n.getX() > maxX) {
				maxX = n.getX();
			}
		}
		
		for(Edge e : getEdges()) {
			for(Point p : e.getBends()) {
				if(p.x < minX) {
					minX = p.x;
				}
				if(p.x > maxX) {
					maxX = p.x;
				}
			}
		}
		
		return maxX-minX;
	}
	
	/** The height of the graph */
	public int findHeight() {
		if(getNodes().size() == 0) {
			return 0;
		}
		
		int maxY = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		
		for(Node n : getNodes()) {
			if(n.getY() < minY) {
				minY = n.getY();
			}
			if(n.getY() > maxY) {
				maxY = n.getY();
			}
		}
		
		for(Edge e : getEdges()) {
			for(Point p : e.getBends()) {
				if(p.y < minY) {
					minY = p.y;
				}
				if(p.y > maxY) {
					maxY = p.y;
				}
			}
		}
		
		return maxY-minY;
	}
	
	
	/** The minimum x location of the graph */
	public int findMinimumX() {
		if(getNodes().size() == 0) {
			return 0;
		}
		
		int minX = Integer.MAX_VALUE;
		
		for(Node n : getNodes()) {
			if(n.getX() < minX) {
				minX = n.getX();
			}
		}
		
		for(Edge e : getEdges()) {
			for(Point p : e.getBends()) {
				if(p.x < minX) {
					minX = p.x;
				}
			}
		}
		
		return minX;
	}
	
	/** The minimum Y of the graph */
	public int findMinimumY() {
		if(getNodes().size() == 0) {
			return 0;
		}
		
		int minY = Integer.MAX_VALUE;
		
		for(Node n : getNodes()) {
			if(n.getY() < minY) {
				minY = n.getY();
			}
		}
		
		for(Edge e : getEdges()) {
			for(Point p : e.getBends()) {
				if(p.y < minY) {
					minY = p.y;
				}
			}
		}
		
		return minY;
	}
	
	/**
	 * Check graph consistency by ensuring internal data structures are correct.
	 * That is: all redundant connection data is correct and check all connecting
	 * nodes and edges are in the graph.
	 * This method is a useful test to ensure that internal methods that
	 * change the graph leave a valid graph.
	 */
	public boolean consistent() {

		// go though nodes checking parent and all to and from vectors
		// correspond to connecting edges
		for(Node n : getNodes()) {
			if(!n.consistent(this)) {return false;}
		}

		// go through edges checking parent and all connecting ids are
		// in to and from vectors
		for(Edge e : getEdges()) {
			if(!e.consistent(this)) {return false;}
		}

		return(true);
	}	
	
	/**
	 * Outputs the node and edge lists in a string.
	 */
	public String toString() {
		return(getLabel()+"\nNodes:"+getNodes().toString()+"\nEdges:"+getEdges().toString());
	}
	
	/**
	 * Form an adjacency list of a graph
	 * */
	public void formAdjList()
	{
		for(Node n : nodes){
			ArrayList <Node> adj = new ArrayList<Node>();
			adj.add(n);
			for( Edge e :edges){				
				if(e.getFrom()==n){
					adj.add(e.to);
				}
				if(e.getTo() ==n){
					adj.add(e.getFrom());
				}
			}
			adjList.add(adj);
		}
	}
	public Vector <ArrayList<Node>> getAdjList (){return adjList;}
	
	/**
	 * Returns the adjacency list of the graph node
	 **/


	public ArrayList<Node> getNodeAdjList(Node n){
		for(ArrayList<Node> adj: adjList){
			if(adj.get(0)==n)
			return adj;

		}		
		return null;		
	}

	/**
	 * Forms face edge list.
	 * - for a  face node in the graph, form a edge list of this nodes 
	 * which contains all the edges that are connected to the node, and sort 
	 * the edges by angle
	 **/
	public ArrayList<FaceEdge> formFaceEdgeList(ArrayList<Node> adj){
		
		for(Edge e: edges){
			e.setLabel(DualGraph.findLabelDifferences(e.getFrom().getLabel(), e.getTo().getLabel()));
		}
		ArrayList<FaceEdge> faceEdgeList = new ArrayList<FaceEdge> ();
		Node start = adj.get(0);
		for(int i = 1; i< adj.size(); i++){
			Node end = adj.get(i);
				Edge e = getEdge(start,end);
				FaceEdge fEdge = new FaceEdge(start, end, e);
				faceEdgeList.add(fEdge);
		}	
		FaceEdgeAngleComparator eComp = new FaceEdgeAngleComparator();
		Collections.sort(faceEdgeList,eComp);			
		
		for(int j = 0; j < faceEdgeList.size(); j++){
			FaceEdge e = faceEdgeList.get(j);
			e.setIndex(j);
		}		
		return faceEdgeList;	
	} 
	

	public ArrayList<FaceEdge> formFaceEdges(){

		doubledUpFaceEdges = new ArrayList<FaceEdge>();
		for(Edge e : edges){
			FaceEdge e1 = new FaceEdge(e.getFrom(), e.getTo(), e);
			FaceEdge e2 = new FaceEdge(e.getTo(), e.getFrom(), e);
			doubledUpFaceEdges.add(e1);
			doubledUpFaceEdges.add(e2);
		}
		return doubledUpFaceEdges;
	}
	
	
		
	/**
	 * Finds all the faces of the graph.
 	 */	
	public ArrayList<Face> formFaces() {
		formAdjList();
	    doubledUpFaceEdges = formFaceEdges();
		faces = new ArrayList<Face>();

		while(allFaceEdgeVisited() == false){
			FaceEdge startEdge = getNextUnvisitedEdge(doubledUpFaceEdges);
			Face face = formFace(startEdge);
			if(face != null)
				faces.add(face);
		}
		return faces;			
	}
	
	
	public void printFaces(){
		for(int i = 0 ; i < faces.size(); i++){
			Face f = faces.get(i);
			System.out.println("----------Face"+ i +"--------------");
			f.print();		
		}	
	}
		
	/**
	 * Starts from an edge, try to find a face it belongs to
	 **/
	public Face formFace(FaceEdge startEdge){		
		boolean isCircle = false;
		Face face = null;
		ArrayList<FaceEdge> edgeList = new ArrayList<FaceEdge>();
		FaceEdge currentEdge = startEdge;
		edgeList.add(startEdge);
		startEdge.setVisited(true);
		while(! isCircle  && currentEdge != null ) {
			currentEdge = getNextEdge(currentEdge.getTo(), currentEdge);
			edgeList.add(currentEdge);

			FaceEdge doubledFE = getDoubledFE(doubledUpFaceEdges,currentEdge);
			doubledFE.setVisited(true);
			
			if(currentEdge.getTo()==startEdge.getFrom()) {
				FaceEdge testFE = getNextEdge(currentEdge.getTo(), currentEdge);
				FaceEdge doubledTestFE = getDoubledFE(doubledUpFaceEdges, testFE);
				if(doubledTestFE.getVisited()) { // this bit added for detecting faces that start on dangling edges
					isCircle = true;
				}
			}
		}
		face = new Face(edgeList);
				
		return face;
	}
	
	public FaceEdge getDoubledFE(ArrayList<FaceEdge> doubledUpFaceEdges2, FaceEdge currentFE) {
		
		for(FaceEdge fe : doubledUpFaceEdges) {
			if(fe.getFrom()==currentFE.getFrom() && fe.getTo()==currentFE.getTo()) {
				return fe;
			}
		}
		return null;

	}
	
	
	/**
	 * Finds the next edge around a face for a node. Its the node with smallest
	 * angle clockwise from faceEdge.
	 */
	public FaceEdge getNextEdge(Node n, FaceEdge faceEdge) {
		
		int edgeEndIndex = -1;
		int nextIndex = -1;
		
		ArrayList<FaceEdge> faceEdgeList = new ArrayList<FaceEdge>();
		
		for(ArrayList<Node> adj : adjList) {
			   if(adj.get(0) == n) {
					faceEdgeList = formFaceEdgeList(adj);
				}
			}

		
		for(FaceEdge fe: faceEdgeList){
			if( fe.getVisited()== false && (fe.getFrom()==faceEdge.getTo()&& fe.getTo()==faceEdge.getFrom())) {
				edgeEndIndex = fe.getIndex();
				if(edgeEndIndex == 0){
					nextIndex = getMaxIndex(fe, doubledUpFaceEdges);
				}
				else {
					nextIndex = edgeEndIndex -1;
				}				
				for(FaceEdge temp: faceEdgeList){		
					if(temp.getFrom()==faceEdge.getTo() && temp.getIndex() == nextIndex)
						return temp;						
				}
			}
		}
		return null;
	}

	/**
	 * Get the number of face edges that are connected with an face node 
	 **/
	public int getMaxIndex(FaceEdge faceEdge, ArrayList<FaceEdge> faceEdgeList){
			
		int index = -1;		
		for(FaceEdge fe: faceEdgeList){
			if(fe.getFrom()==faceEdge.getFrom())
			index++;
		}		
		return index;
			
	}
	
	/**
	 * Returns next unvisited edge in the face edge list.
	 **/
	public FaceEdge getNextUnvisitedEdge(ArrayList<FaceEdge> faceEdgeList){
		
		for(FaceEdge fe: faceEdgeList){				
			if(fe.getVisited() == false)
			return fe;
		}
		return null;		
	}	
	
	/**
	 * method to check if all the face edges are visited
	 **/
	public boolean allFaceEdgeVisited(){
		for(FaceEdge fe : doubledUpFaceEdges){			
			if(fe.getVisited() == false)
				return false;
		}
		return true;
	}
	
	/**
	 * Sets the face condition of a graph NOTE formFaces() must have been called
	 * before this method is called.
	 * The face condition of a graph is determined all the faces in the graph, 
	 * the graph passes face condition only when all the faces in the graph 
	 * pass face condition.
	 **/
	public boolean passFaceConditions(){
		ArrayList<Face> faces = getFaces();
		for(Face f :faces){
			if(!f.passFaceConditions()) {
				return false;
			}
		}
		return true;
	}
	
	
		
	/**
	 * Checks if the passed list forms a cycle of nodes, with
	 * the last node connecting to the first, edges are assumed
	 * to be undirected.
	 */
	public boolean isNodeCycle(ArrayList<Node> nodes) {
		setEdgesVisited();
		
		if(nodes.size() <= 1) {
			return true;
		}
		
		Node firstNode = null;
		Node thisNode = null;
		Node prevNode = null;
		for(Node n: nodes) {
			if(thisNode == null) {
				thisNode = n;
				firstNode = n;
				continue;
			}
			prevNode = thisNode;
			thisNode = n;
			boolean foundEdge = false;
			for(Edge e: thisNode.unvisitedConnectingEdges()) {
				if(e.getOppositeEnd(thisNode) == prevNode) {
					e.setVisited(true);
					foundEdge = true;
					break;
				}
			}
			if(foundEdge == false) {
				return false;
			}
		}
		
		// test the first and last nodes
		boolean foundEdge = false;
		for(Edge e: thisNode.unvisitedConnectingEdges()) {
			if(e.getOppositeEnd(thisNode) == firstNode) {
				e.setVisited(true);
				foundEdge = true;
				break;
			}
		}
		if(foundEdge == false) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the passed list forms a cycle of equivalent edges
	 * in the graph, edges are assumed to be undirected. If the
	 * use edgesOnce flag is set, then each edge in the graph
	 * can only be used once, otherwise it can be used multiple times
	 */
	public boolean isFaceEdgeCycle(ArrayList<FaceEdge> faceEdges, boolean useEdgesOnce) {
			setEdgesVisited();

		// 0 fe case
		if(faceEdges.size() == 0) {
			return true;
		}
		// 1 fe case
		if(faceEdges.size() == 1) {
			FaceEdge fe = faceEdges.get(0);
			Node from = fe.getFrom();
			Node to = fe.getTo();
			boolean cycle = false;
			if(from == to) {
				// only case where one edge forms a cycle is when it points to the same node
				cycle = true;
			}
			if(!cycle) {
				return false;
			}
			
			// check a corresponding edge for the FE exists in the graph
			boolean found = false;
			for(Edge e: from.unvisitedConnectingEdges()) {
				if(e.getOppositeEnd(from) == to) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
			return true;
		}
		
		// 2 fe case
		if(faceEdges.size() == 2) {
			FaceEdge fe1 = faceEdges.get(0);
			FaceEdge fe2 = faceEdges.get(1);
			
			Node from1 = fe1.getFrom();
			Node to1 = fe1.getTo();
			Node from2 = fe2.getFrom();
			Node to2 = fe2.getTo();
			// only case where two edges form a cycle is when they have the same node connections
			boolean cycle = false;
			if(from1 == from2 && to1 == to2) {
				cycle = true;
			}
			if(from1 == to2 && to1 == from2) {
				cycle = true;
			}
			if(!cycle) {
				return false;
			}
			
			// check a corresponding edge for both FEs exists in the graph
			boolean found1 = false;
			for(Edge e: from1.unvisitedConnectingEdges()) {
				if(e.getOppositeEnd(from1) == to1) {
					if(useEdgesOnce) {
						e.setVisited(true);
					}
					found1 = true;
					break;
				}
			}
			if(!found1) {
				return false;
			}

			// check a corresponding edge for both FEs exists in the graph
			boolean found2 = false;
			for(Edge e: from2.unvisitedConnectingEdges()) {
				if(e.getOppositeEnd(from2) == to2) {
					if(useEdgesOnce) {
						e.setVisited(true);
					}
					found2 = true;
					break;
				}
			}
			if(!found2) {
				return false;
			}
			return true;

		}

		// 3 or greater case
		Node firstNode = null;
		Node thisNode = null;
		FaceEdge firstFE = null;
		for(FaceEdge thisFE : faceEdges) {
			Node from = thisFE.getFrom();
			Node to = thisFE.getTo();
			// check that there is an unvisited edge connecting the two nodes
			// in the graph and mark the edge visited
			boolean foundEdge = false;
			Collection<Edge> allFromEdges = from.unvisitedConnectingEdges();
			for(Edge e : allFromEdges) {
				Node connectingNode = e.getOppositeEnd(from);
				if(connectingNode == to) {
					foundEdge = true;
					if(useEdgesOnce) {
						e.setVisited(true);
					}
					break;
				}
			}
			if(!foundEdge) {
				return false;
			}
			
			// first iteration
			if(firstFE == null) {
				firstFE = thisFE;
				continue;
			}
			
			// second iteration
			if(firstNode == null) {
				Node linkNode = firstFE.connectingNode(thisFE);
				if(linkNode == null) {
					return false;
				}
				firstNode = firstFE.oppositeEnd(linkNode);
				thisNode = thisFE.oppositeEnd(linkNode);
				continue;
			}
			
			// standard iteration
			thisNode = thisFE.oppositeEnd(thisNode);
			if(thisNode == null) {
				return false;
			}
		}
		
		if(thisNode != firstNode) {
			return false;
		}
		return true;
	}
		
	/**
	 * method to check if a graph node is a inner node 
	 **/
	public boolean isInnerNode(Node n, ArrayList<Face> faceList){
		
		boolean isInner = false;
		
		if(!this.containsNode(n)|| faceList.size() == 0){
			return false;
		}
		else{
			Iterator itr = faceList.iterator();
			while(itr.hasNext()){
				Face f = (Face) itr.next();
				if(isNodeInFace(n,f))
					isInner = true;
			}
			return isInner;	
		}
	}
	
	/**
	 * method to check if a given node is in the face node list
	 */
	public boolean isNodeInFace(Node n, Face f) {
		
		if(f.getNodeList().size()!=0){
			Iterator itr = f.getNodeList().iterator();
			while(itr.hasNext()){
				Node n0 = (Node) itr.next();
				Point p = n.getCentre();
				Polygon pol = f.generatePolygon();
				if(n==n0) 
					return false;
				else 
					if(!pol.contains(p)) 
						return false;
			}
		}
		return true;
	}

	/**
	 * returns the outer face of a graph NOTE formFaces() must have been called
	 * before this method is called.
	 */
	public Face getOuterFace()
	{				
		ArrayList<Node> innerNodes = new ArrayList<Node>();
		ArrayList<Node> exterNodes = new ArrayList<Node>();
		for(Node n: nodes){
			if(isInnerNode(n,faces))
			{
				innerNodes.add(n);
			//	System.out.println(n.getLabel() + " is inner node");
			}
		}
		
		for(Node n1: nodes){
			if(!isInNodeList(n1,innerNodes)){
				exterNodes.add(n1);
			//	System.out.println(n1.getLabel() + " is exter node");
			}
		}
	
		for(Face f: faces){
			if(allNodesInFaceNodeList(f, exterNodes)&& allNodesInFace(f, innerNodes))
			{
				//System.out.println("outer face: ");
				//f.print();
				return f;
			}
		}	
		
		return null;
	}
	
	
	/**
	 * returns the outer face of the graph
	 */
	public Face getOuterFace(ArrayList<Face> faceList)
	{
	
		ArrayList<Node> innerNodes = new ArrayList<Node>();
		ArrayList<Node> exterNodes = new ArrayList<Node>();
		for(Node n: nodes){
			if(isInnerNode(n,faceList))
			{
				innerNodes.add(n);
			//	System.out.println(n.getLabel() + " is inner node");
			}
		}
		
		for(Node n1: nodes){
			if(!isInNodeList(n1,innerNodes)){
				exterNodes.add(n1);
			//	System.out.println(n1.getLabel() + " is exter node");
			}
		}
	
		for(Face f: faceList){
			if(allNodesInFaceNodeList(f, exterNodes)&& allNodesInFace(f, innerNodes))
			{
				//System.out.println("outer face: ");
				//f.print();
				return f;
			}
		}	
		
		return null;
	}
	
	
	
	/**
	 * method to check if a node is in a node list
	 * */	
	public boolean isInNodeList(Node node, ArrayList<Node> nodeList){
		
		for(Node n : nodeList){
			if(node.equals(n))
				return true;		
		}
		return false;
	}	
	
	/**
	 * method to check if a all nodes in a list is the face nodes of a particular face
	 * */	
	public boolean allNodesInFaceNodeList(Face face, ArrayList<Node> nodeList){		
		for(Node n : nodeList){
			if(!face.hasNode(n))
				return false;		
		}
		return true;		
	}
	/**
	 * method to check if all nodea are in a face node list
	 * */
	public boolean allNodesInFace(Face face, ArrayList<Node> nodeList){
		
		for(Node n : nodeList){
			if(!isNodeInFace(n, face)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Groups the faces into connected sublists
	 * NOTE formFaces() must have been called
	 * before this method is called.
	 */
	public ArrayList<ArrayList<Face>> findConnectedFaces() {
		ArrayList<ArrayList<Face>> ret = new ArrayList<ArrayList<Face>>();
		for(Face newFace : faces) {
			Node newNode = newFace.getNodeList().get(0);
			ArrayList<Face> connectedFaceList = null;
			for(ArrayList<Face> oldFaceList : ret) {
				Face oldFace = oldFaceList.get(0);
				Node oldNode = oldFace.getNodeList().get(0);
				if(nodesConnected(newNode,oldNode)) {
					connectedFaceList = oldFaceList;
					break;
				}
			}
			if(connectedFaceList != null) {
				// face is connected to an existing face in ret
				connectedFaceList.add(newFace);
			} else {
				// face is not connected to an existing face in ret
				ArrayList<Face> newFaceList = new ArrayList<Face>();
				newFaceList.add(newFace);
				ret.add(newFaceList);
			}
		}

		return ret;
	}
	
/**
 * Prints the node list and edge list of the graph 
 **/	
	public void printAll(){
		
		System.out.println("nodes:");
		for(Node n : nodes){
			System.out.println(n.toString());
		}
		System.out.println("edges:");
		for(Edge e : edges){
			System.out.println(e.getFrom().getLabel() + ", " + e.getTo().getLabel());
		}
	}
	
/**
 * A deep copy of the Graph, attempting to duplicate nodes and edges,
 * but does not copy derived information like adjList and faceNodes.
 */
	public Graph clone() {
		Graph ret = new Graph(getLabel());
		HashMap<Node,Node> nodeMap = new HashMap<Node,Node>(getNodes().size());
		
		for(Node thisNode : getNodes()) {
			Point newCentre = new Point(thisNode.getCentre().x,thisNode.getCentre().y);
			Node newNode = new Node(thisNode.getLabel(),thisNode.getType(),newCentre);
			newNode.setVisited(thisNode.getVisited());
			newNode.setScore(thisNode.getScore());
			newNode.setMatch(thisNode.getMatch());
			ret.addNode(newNode);
			nodeMap.put(thisNode,newNode);
		}
		
		for(Edge thisEdge : getEdges()) {
			Node thisFrom = thisEdge.getFrom();
			Node thisTo = thisEdge.getTo();
			Node newFrom = nodeMap.get(thisFrom);
			Node newTo = nodeMap.get(thisTo);
			
			Edge newEdge = new Edge(newFrom, newTo, thisEdge.getLabel(), thisEdge.getWeight(), thisEdge.getType());
			newEdge.setVisited(thisEdge.getVisited());
			newEdge.setScore(thisEdge.getScore());
			newEdge.setMatch(thisEdge.getMatch());
			
			ArrayList<Point> bends = new ArrayList<Point>(thisEdge.getBends().size());
			for(Point oldP : thisEdge.getBends()) {
				Point newP = new Point(oldP.x,oldP.y);
				bends.add(newP);
			}
			newEdge.setBends(bends);
			
			ret.addEdge(newEdge);
		}
		return ret;
	}
	
	
	public void CheckHamiltonCycle(){
		
		int n = nodes.size();  
		int m = edges.size();
		int cycle[] = new int[n+1];
		int nodei[] = new int [m+1];
		int nodej[] = new int [m+1];
		for(int i = 0 ; i < n; i++){
			nodes.get(i).setIndex(i+1);
		}
		nodei[0] = 0;
		nodej[0] = 0;
		for(int j = 1 ; j < m+1 ; j++){
			Edge e = edges.get(j-1);
			nodei[j] = e.getFrom().getIndex();
			nodej[j] = e.getTo().getIndex();	

		}
		boolean directed = false;
		HamiltonCycle(n,m,directed,nodei,nodej,cycle);
		if (cycle[0] != 0)
		System.out.println("No Hamilton cycle is found.");
		else {
		 for(int k = 1; k < cycle.length - 1; k++)	{
			 Node n1 = nodes.get(cycle[k]-1);
			 Node n2 = nodes.get(cycle[k+1]-1);
			 Edge e = getEdge(n1, n2);
			 EdgeType et = new EdgeType("ham");
			 et.setLineColor(Color.red);
			 e.setType(et);
			 
			 n1 = nodes.get(cycle[cycle.length - 1]-1);
			 n2 = nodes.get(cycle[1]-1); 
			 e = getEdge(n1, n2);
			 e.setType(et);
		 }	
			
		}
		
		
	}
	public static void HamiltonCycle(int n, int m, boolean directed,
			int nodei[], int nodej[], int cycleNodes[]){
		
		int i,j,k,stacklen,lensol,stackindex,len,len1,len2,low,up;
		int firstedges[] = new int[n+2];
		int endnode[] = new int[m+m+1];
		int stack[] = new int[m+m+1];
		boolean connect[] = new boolean[n+1];
		boolean join,skip;
		// set up the forward star representation of the graph
		k = 0;
		
		for (i=1; i<=n; i++) {
			
			firstedges[i] = k + 1;
			for (j=1; j<=m; j++) {
				if (nodei[j] == i) {
					k++;
					endnode[k] = nodej[j];
				}
				if (!directed)
					if (nodej[j] == i) {
						k++;
						endnode[k] = nodei[j];
					}
				}
			}
			
		firstedges[n+1] = k + 1;
			
		// initialize
		lensol = 1;
		stacklen = 0;
		// find the next node
		while (true) {
			if (lensol == 1) {
				stack[1] = 1;
				stack[2] = 1;
				stacklen = 2;
			}
			else {
				len1 = lensol - 1;
				len2 = cycleNodes[len1];
				for (i=1; i<=n; i++) {
					connect[i] = false;
					low = firstedges[len2];
					up = firstedges[len2 + 1];
					if (up > low) {
						up--;
						for (k=low; k<=up; k++)
							if (endnode[k] == i) {
								connect[i] = true;
								break;
							}
					}
			}
			for (i=1; i<=len1; i++) {
				len = cycleNodes[i];
				connect[len] = false;
			}
			len = stacklen;
			skip = false;
			if (lensol != n) {
				for (i=1; i<=n; i++)
					if (connect[i]) {
						len++;
						stack[len] = i;
					}
					stack[len + 1] = len - stacklen;
					stacklen = len + 1;
			}
			else {
			for (i=1; i<=n; i++)
				if (connect[i]) {
					if (!directed) {
						if (i > cycleNodes[2]) {
							stack[len + 1] = len - stacklen;
							stacklen = len + 1;
							skip = true;
							break;
						}
			
					}
			join = false;
			low = firstedges[i];
			up = firstedges[i + 1];
		
			if (up > low) {
				up--;
				for (k=low; k<=up; k++)
					if (endnode[k] == 1) {
						join = true;
						break;
					}
			}
			if (join) {
				stacklen += 2;
				stack[stacklen - 1] = i;
				stack[stacklen] = 1;
			}
			else {
				stack[len + 1] = len - stacklen;
				stacklen = len + 1;
			}
			skip = true;
			break;
				}
			if (!skip) {
				stack[len + 1] = len - stacklen;
				stacklen = len + 1;
			}
			}
		}
			// search further
			while (true) {
			stackindex = stack[stacklen];
			stacklen--;
			if (stackindex == 0) {
			lensol--;
			if (lensol == 0) {
			cycleNodes[0] = 1;
			return;
			}
			continue;
			}
			else {
				cycleNodes[lensol] = stack[stacklen];
				stack[stacklen] = stackindex - 1;
				if (lensol == n) {
					cycleNodes[0] = 0;
					return;
				}
				lensol++;break;
				}
			}
		}			
	}
	
	public ArrayList<Face> allPossibleCycles(){
		
//		System.out.println("all possible circles");
		ArrayList<Face> faces = formUnitFacesWithEdgeBends();
		ArrayList<Face> circles = new ArrayList<Face>();
		circles.addAll(faces);
		ArrayList<Face> facesInCurrentRound = new ArrayList<Face>();
		ArrayList<Face> facesInPreviousRound =  new ArrayList<Face>();
		for(int i = 0 ; i < faces.size()-1;i++){
			facesInPreviousRound.add(faces.get(i));
		}
		int round = 1;
		while(round < faces.size()- 2){
			round++;	
			facesInCurrentRound = new ArrayList<Face>();
			for(Face f: facesInPreviousRound){
				for(int i = 0 ; i < faces.size()-1;i++){
					if( f!=faces.get(i)){
						Face f0 = mergeFace(faces.get(i),f);
						if(f0 != null && !containsFace(circles, f0)&& !containsFace(facesInCurrentRound,f0)){
							facesInCurrentRound.add(f0);
							circles.add(f0);
							//System.out.println("added");
						}
					}
				}
			}			
			facesInPreviousRound = facesInCurrentRound;					
		}
	/*	System.out.println("all possible circles---------------" + circles.size());
		for(Face f : circles){
			System.out.println("-----");
			f.print();			
		}
		System.out.println("all possible circles---------------" + circles.size());*/
		return circles;
	
	}
	public boolean containsFace(ArrayList<Face> faceList, Face f){
		 ArrayList<String> nodeList = new ArrayList<String>();		 
		 for(Node n: f.getNodeList()){
			 nodeList.add(n.getLabel());
		 }
		 ArrayList<String> nodeList1;
		 for(Face f1: faceList){
			nodeList1 = new ArrayList<String>();
			for(Node n1:f1.getNodeList()){
				nodeList1.add(n1.getLabel());
			}
			if(nodeList.containsAll(nodeList1)&&nodeList1.containsAll(nodeList))
			 return true;
		 }
		return false;
	}
	
	public boolean containsFace(Face f,ArrayList<String> nodeList){
	
		if(f.nodeList.size()!=nodeList.size())
			return false;
		ArrayList<String> nodeList1 = new ArrayList<String>();
		for(Node n1:f.getNodeList()){
			nodeList1.add(n1.getLabel());
		}
		for(String s: nodeList){
			if(!nodeList1.contains(s))
				return false;
		}
		return true;
	}
	
	
	
	public Face mergeFace(Face f1, Face f2){
		if(shareEdge(f1,f2)){
			ArrayList<FaceEdge> edgeList = new ArrayList<FaceEdge>();
			 for(FaceEdge fe1: f1.getFaceEdgeList()){
				if(!containsEdge(f2.getFaceEdgeList(),fe1))
				edgeList.add(fe1);
			 }
			 for(FaceEdge fe2: f2.getFaceEdgeList()){
				 if(!containsEdge(f1.getFaceEdgeList(),fe2))
					edgeList.add(fe2);			
			 }	
	/*		 System.out.println("face 1");
			 f1.print();
			 System.out.println("face 2");
			 f2.print();*/
			 return (new Face(edgeList,true));
		}
		return null;
	}
	 public boolean shareEdge(Face f1, Face f2){
		
		 for(FaceEdge fe1: f1.getFaceEdgeList()){
			for(FaceEdge fe2: f2.getFaceEdgeList()){
				if((fe1.getFrom().getLabel().compareTo(fe2.getFrom().getLabel())==0 
					&& fe1.getTo().getLabel().compareTo(fe2.getTo().getLabel())==0)
					||(fe1.getTo().getLabel().compareTo(fe2.getFrom().getLabel())==0
					&&fe1.getFrom().getLabel().compareTo(fe2.getTo().getLabel())==0)){
				//	System.out.println("share edge " + fe1.toString() + " " +fe2.toString());
					return true;
				}
			}
		}
		
		 return false;		 
	 }
	public boolean containsFace(Face f1, Face f2){
		 if(f1.getNodeList().size()<f2.getNodeList().size()){
			 return false;
		 }
		 ArrayList<String> nodeList = new ArrayList<String>();
		 for(Node n: f1.getNodeList()){
			 nodeList.add(n.getLabel());
		 }
		 for(Node n: f2.getNodeList()){
			 if(!nodeList.contains(n.getLabel())){
				 return false;
			 }				 
		 }	 
		 return true;
	 }
	
	public boolean containsEdge(ArrayList<FaceEdge> edgeList, FaceEdge e){
		for(FaceEdge fe: edgeList){
			String start1 = fe.getFrom().getLabel();
			String end1 = fe.getTo().getLabel();
			String start2 = e.getFrom().getLabel();
			String end2 = e.getTo().getLabel();			
			if((start1.compareTo(start2)==0&& end1.compareTo(end2)==0)
					||(start1.compareTo(end2)==0 && end1.compareTo(start2)==0))		
			return true;
		}
		return false;
	}
	
	public ArrayList<Face> formUnitFacesWithEdgeBends(){
		
		ArrayList<String> nodeLabels = new ArrayList<String>();
		
		for(int i = 0 ; i < nodes.size(); i++){
			Node n = nodes.get(i);
			nodeLabels.add(n.getLabel());
			n.setLabel(Integer.toString(i));
			n.setIndex(i);
		}
				
		ArrayList<Face> ret = new ArrayList<Face>();
		Combination com = new Combination(4,nodes.size());
		int[] next = com.next();
		while(next!=null){
			ArrayList<Node> fNodes = new ArrayList<Node>();
			for(int i = 0 ; i < next.length; i++){
				fNodes.add(nodes.get(next[i]));			
			}			
			Face f = getUnitFace(fNodes, nodeLabels);
				if(f!= null){
					ret.add(f);
					//f.print();
					for(int i = 0 ; i < next.length; i++){
						//System.out.print(nodes.get(next[i]).getLabel() + "\t" );
						System.out.print(next[i] + "\t" );
					}
					System.out.println();
				}
				next = com.next();
			}		
		
		for(int j = 0 ; j < nodes.size(); j++){
			nodes.get(j).setLabel(nodeLabels.get(j));			
		}
	
		System.out.println("number of faces "+ ret.size());
		return ret;
	}
	
	
	public Face getUnitFace(ArrayList<Node> nList, ArrayList<String> nodeLabels){		
		ArrayList<FaceEdge> fes = new ArrayList<FaceEdge>();
		for(Edge e: edges){
			if(nList.contains(e.getFrom())&& nList.contains(e.getTo())){
				FaceEdge fe1 = new FaceEdge(e.getFrom(), e.getTo());
				FaceEdge fe2 = new FaceEdge(e.getTo(),e.getFrom());
				if(e.getBends().size()!=0){
					fe1.setBends(e.getBends());
					fe2.setBends(e.getBends());
					fe2.setReverse(true);
				}
				fes.add(fe1);	
				fes.add(fe2);
			}
		}		
		if(fes.size()!=nList.size()*2)
			return null;
		int count [] = new int[ nList.size()];
		
		for(int i = 0 ; i < nList.size() ; i++){
			for(FaceEdge fe : fes){
				if(fe.getFrom()==nList.get(i)||fe.getTo()==nList.get(i))
					count[i]++;	
					}
			if(count[i]!=4)
				return null;			
		}
		
		ArrayList<FaceEdge> temp = new ArrayList<FaceEdge>();
		FaceEdge currentEdge = fes.get(0);
		temp.add(currentEdge);
		for(int i = 1 ; i <nList.size() ; i ++){
			FaceEdge fe = this.getNextFaceEdge(fes, currentEdge);
			if(fe!=null){
				temp.add(fe);
				currentEdge = fe;
			}
		}
		
		
		Face face  = new Face(temp, false);
		Polygon pol = face.generatePolygon();
		for(Node n : nodes){
			if(!nList.contains(n)&&(pol.contains(n.getCentre()))){
			//	System.out.println("------------------------------polygon contains other node " + n.getLabel());
				return null;
			}
		
				
			
		}
		//System.out.println("------------------------------polygon does not contain other node");
		return face;

	}
	public FaceEdge getNextFaceEdge(ArrayList<FaceEdge> eList, FaceEdge fe){
		String s1 = fe.getFrom().getLabel();
		String s2 = fe.getTo().getLabel();
		for(FaceEdge fe1: eList){
			if(fe1.getFrom().getLabel().compareTo(s2)== 0
					&& fe1.getTo().getLabel().compareTo(s1)!=0)
				return fe1;
		}		
		return null;
	}
	
	
	/**
	 * Return the list of edges that have the same label,
	 * end nodes in the same position and with the same labels
	 * and the same number of bend points.
	 */
	public ArrayList<Edge> getMatchingEdgeList(Edge e) {
		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(Edge thisE :getEdges()) {
			if(thisE.isMatchingEdge(e)) {
				ret.add(thisE);
			}
		}
		return ret;
	}
	


}



