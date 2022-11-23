package pjr.graph;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import euler.*;
import pjr.graph.triangulator.*;

/**
 * This is a face which edges forms a circle in the graph 
 * <p>
 * When a graph is drawn without any crossing, any cycle that surrounds a region
 * without any edge reaching from the cycle inside to such region forms a face. 
 * @see FaceEdge
 * @author Leishi Zhang
 */
public class Face implements Serializable {
	
	protected ArrayList<Node> nodeList = null;
	protected ArrayList<FaceEdge> faceEdgeList = null;
	protected ArrayList<Point2D> midPoints = null;
	/** contours that cross in the face. */
	protected ArrayList<String> crossingContours = new ArrayList<String>();
	/** Used to keep track of which contours have been crossed in the face */
	protected ArrayList<String> remainingCrossingContours = new ArrayList<String>();
	protected String faceSymbols = "";
	protected String symbolList = ""; 
	protected Polygon polygon;
	/** Used when there is a single meeting point for all contours in a face */
	protected TriangulationFace meetingTF = null;
	
	/** Trival constructor. */
	public Face(ArrayList<FaceEdge> edges){
		faceEdgeList = edges;
		formNodeList(faceEdgeList);
		formSymbolList();
	 	calculateCrossingContours();
	 	resetRemainingCrossingContours();
		generatePolygon();
	}
	public Face(ArrayList<FaceEdge> edges, boolean set){
			if(!set){
				 faceEdgeList = edges;
			 	 formNodeList(edges);
				}
			else{
				faceEdgeList = edges;
				formNodeList(faceEdgeList);
				formSymbolList();
			 	calculateCrossingContours();
			 	resetRemainingCrossingContours();
				generatePolygon();
			}
	}
	
	public TriangulationFace getMeetingTF() {return meetingTF;}
	public Polygon getPolygon() {return polygon;}
	public String getFaceSymbols() {return faceSymbols;}
	public ArrayList<String> getCrossingContours() {return crossingContours;}
	public ArrayList<String> getRemainingCrossingContours() {return remainingCrossingContours;}
	public int getCrossingIndex() {return crossingContours.size();}
	public ArrayList<FaceEdge> getFaceEdgeList() {return faceEdgeList;}
	public ArrayList<Node> getNodeList() {return nodeList;}

	public void setMeetingTF(TriangulationFace tf) {meetingTF = tf;}

	
	public ArrayList<Point2D> getMidPoints(String label){
		getInsideLines(label);
		return midPoints;
	}
	public ArrayList<Edge> findEdgeList() {
		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(FaceEdge fe : getFaceEdgeList()) {
			ret.add(fe.getEdge());
		}
		return ret;
	}

	/** Return a sorted unique list of contours passing through the face. */
	public ArrayList<String> findContours() {
		ArrayList<String> contours = new ArrayList<String>(); 
		
		for(FaceEdge fe : getFaceEdgeList()) {
			Edge e = fe.getEdge();
			ArrayList<String> edgeContours = AbstractDiagram.findContourList(e.getLabel());
			contours.addAll(edgeContours);
		}
		
		Collections.sort(contours);
		AbstractDiagram.removeDuplicatesFromSortedList(contours);
		
		return contours;

	}
	
	/**
	 * method to generate polygon around a face
	 * */
	public Polygon generatePolygon(){

		polygon = new Polygon();		
	//	System.out.println("number of face edges" + faceEdgeList.size());
		for(FaceEdge fe: faceEdgeList){
			Node n1 = fe.getFrom();			
			Point p = n1.getCentre();
			//System.out.println(n1.getLabel() + " " + p);
			polygon.addPoint((int)p.getX(), (int)p.getY());
			if(fe.getBends().size()!=0){
			//	System.out.println("bend point");
				if(fe.getReverse() == false){
					for(Point p0 : fe.getBends()){
						//System.out.println(p0);
						polygon.addPoint((int)p0.getX(),(int)p0.getY());
					}
				}
				else{
					for(int i = fe.getBends().size()-1 ; i >=0; i--){
						Point p0 = fe.getBends().get(i);
						polygon.addPoint((int)p0.getX(),(int)p0.getY());
					}
				}
			}		
		}
	/*	System.out.println("polygon");
		for(int i = 0 ; i < polygon.npoints; i++){
			System.out.println( polygon.xpoints[i] +" " +polygon.ypoints[i]);
		}*/
		
		return polygon;
	}
	

	/**
	 * method to form the node list from given face edge list
	 * */
	public void formNodeList(ArrayList<FaceEdge> edges){
		nodeList = new ArrayList<Node>();	
		if(edges.size()!=0){	
			for(FaceEdge edge : edges){
				if(edge!=null){
					Node from = edge.getFrom();
					Node to = edge.getTo();
					if(!existInNodeList(from))
						nodeList.add(from);
					if(!existInNodeList(to) )
						nodeList.add(to);
				}
			}	
		}		

	}
	public FaceEdge getFaceEdge(Node n1, Node n2){
		for(FaceEdge fe : faceEdgeList){
			if((fe.getFrom() == n1 && fe.getTo() ==n2)||(fe.getTo() == n1 && fe.getFrom() == n2)){
				return fe;
			}		
		}
		return null;
		
	}
	/**
	 * method to check if a node is in the face node list
	 * @return true if the node exists in the node list
	 * */
	public boolean existInNodeList(Node n){
		
		if(nodeList != null){					
			for(Node node: nodeList){	
				if(node.equals(n))
				return true;
			}
		}
			return false;
	}
	/**
	 * method to calculate the cross index of a face
	 * this method is used to test if a dual graph passes the face conditions.
	 * Modified to cope with parallel edges.
	 */
	public void calculateCrossingContours() {

		crossingContours = new ArrayList<String>();
		
		char first = ' ';
		char second = ' ';
		
		for(int i = 0; i < symbolList.length(); i++){
			first = symbolList.charAt(i);
		    if(i+1 < symbolList.length()){
				for(int j = i+1; j <symbolList.length(); j++)
				{
					second = symbolList.charAt(j);	
 					if(findIfContoursCross( first, second, faceSymbols)) {
						String crossing = Character.toString(first)+Character.toString(second);
 						if(first > second) {
 							crossing = Character.toString(second)+Character.toString(first);
 						}
 						crossingContours.add(crossing);
 					}
				}
			}
		}
		
		// Filter out crosses for edges that enter the face together
		ArrayList<String> removeCrosses = new ArrayList<String>();
		for(FaceEdge fe : getFaceEdgeList()) {
			String label = fe.getEdge().getLabel();
			if(label != null && label.length() > 1) {
				// parallel edges
				// see if any of the crossings are in the label
				for(String cross : crossingContours) {
					String c1 = cross.substring(0,1);
					String c2 = cross.substring(1,2);
					if(label.contains(c1) && label.contains(c2)) {
						removeCrosses.add(cross);
					}
				}
			}
		}
		
 		for(String cross : removeCrosses) {
 			crossingContours.remove(cross);
 		}
 		
 		Collections.sort(crossingContours);
 		
 		AbstractDiagram.removeDuplicatesFromSortedList(crossingContours);
 		
	}
	

	public static boolean findIfContoursCross(char c1, char c2, String s){
		
		StringBuffer filteredLabel = new StringBuffer("");		
		for(int i = 0; i < s.length(); i++){
							
			char c = s.charAt(i);				
			if(c == c1 || c == c2)				
			filteredLabel.append(c);	
		}
		if(filteredLabel.length()%2==1){
							
			if(filteredLabel.charAt(filteredLabel.length()-1) == c1)					
				filteredLabel.append(c2);				
			else					
				filteredLabel.append(c1);			
		}	
	  	return isCrossed(filteredLabel.toString());

	}
	
	/**
	 * given a symbol string, test if there is any cross
	 * by checking for a nested pair of contour labels.
	 */
	public static boolean isCrossed(String s){
		
		for(int i = 0 ; i < s.length() -1; i++)
		{			
			char c1 = s.charAt(i);
			char c2 =s.charAt(i+1);
			if( c1 == c2){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * given two nodes, get the difference between the two node labels
	 * */
	public String getSymbol(Node n1, Node n2){
		
		String s1 = n1.getLabel();
		String s2 = n2.getLabel();
		
		String s = DualGraph.findLabelDifferences(s1,s2);
		return s;
	}
	
	/**
	 * method to generate the symbol list of a face
	 * */
	public void formSymbolList(){
		
		for(int i = 0 ; i < nodeList.size()-1; i++){
			
			Node n1 = nodeList.get(i);
			Node n2 = nodeList.get(i+1);
			
			String s = getSymbol(n1, n2);			
			if(s != " "){
				faceSymbols += s;
				if(hasString(symbolList, s) == false)
		 		symbolList += s;
			}
			else{
				System.out.println("error creating symbol list");
			}
		}		
		faceSymbols += getSymbol(nodeList.get(0), nodeList.get(nodeList.size()-1));
	}
	
	

	/**
	 * Find the edge symbol list going around the face from node1 
	 * to node2. The full symbol list can be found by passing the
	 * same node as both arguments. @see formSymbolList
	 */
	public String findPartialWord(Node nodei, Node nodej) {
		
		StringBuffer ret = new StringBuffer("");
		
		int index = nodeList.indexOf(nodei);
		
		Node n1 = null;
		Node n2 = null;
		while(n2 != nodej) {
			
			n1 = nodeList.get(index);
			
			index++;
			if(index == nodeList.size()) {
				index = 0;
			}
			
			n2 = nodeList.get(index);
			
			String s = getSymbol(n1, n2);			
			ret.append(s);
		}
		
		return ret.toString();

	}

	
	
	/**
	 * method to check if the face contains a given edge
	 * */
	public boolean hasEdge(Edge e){
		for(FaceEdge fe: faceEdgeList){			
			if(fe.getEdge().equals(e))
					return true;	
		}
		return false;
	}
	
	
	
	/**
	 * method to check if the face contains an edge given the start and end node of the edge
	 */
	public boolean hasEdge(Node start, Node end){
		String s1 = start.getLabel();
		String s2 = end.getLabel();
		for(FaceEdge fe: faceEdgeList){
			String s3 = fe.getFrom().getLabel();
			String s4 = fe.getTo().getLabel();
			if((s1.compareTo(s3) == 0 && s2.compareTo(s4)==0)
				||(s2.compareTo(s3)==0&&s1.compareTo(s4)==0))
					return true;	
		}
		//System.out.println("no edge between" + s1 + "," + s2);
		return false;
	}
	
	/**
	 * method to check if a node is a face node
	 * */
	public boolean hasNode(Node node)
	{
		if(faceEdgeList.size()!=0){
			for(FaceEdge fe: faceEdgeList){
				if(fe.getFrom().equals(node)||fe.getTo().equals(node)){
				//	System.out.println("Face has node:" +node.getLabel());
					return true;
				}
			}		
		}
	
		return false;
	}
	
	/**
	 * method to check if a string contains a particular character
	 * */	
	public boolean hasString(String string1, String string2){
		
		if(string1.contains(string2))
			return true;
		return false;
	}
	
	
	/**
	 * @return passFaceConditions
	 * */	
	public boolean passFaceConditions(){
		if(crossingContours.size() == (faceEdgeList.size()/2)-1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * get the edge opposite a node in the face which is not connected to the node  
	 * */
	public FaceEdge getOppositeFaceEdge(Node n){
	
		for(FaceEdge fe : faceEdgeList){
			if(!fe.hasNode(n))
				return fe;				
		}		
		return null;
	}


	
	public ArrayList<Line2D> getInsideLines(String label){
		
		ArrayList<Line2D> insideLines = new ArrayList<Line2D>();
		midPoints = new ArrayList<Point2D>();
		if(this.getFaceSymbols().contains(label)){
			if(!pjr.graph.Util.isConcave(polygon)){
				for(FaceEdge fe: faceEdgeList){
					if(fe.getEdge().getLabel().compareTo(label)==0){
						midPoints.add(fe.getEdge().getMidPoint());
					}
				}
				for(int i = 0 ; i < midPoints.size()-1; i++){
					Line2D line = new Line2D.Double();
					line.setLine(midPoints.get(i),midPoints.get(i+1));
					insideLines.add(line);
				}
			}
			else if(pjr.graph.Util.isConcave(polygon) &&nodeList.size() == 4){
		
				Point p  = getCentre(label);
				for(FaceEdge fe: faceEdgeList){
					if((fe.getEdge().getLabel().compareTo(label) == 0)){
						midPoints.add(fe.getEdge().getMidPoint());
					}
				}
				
				insideLines.add(new Line2D.Double(midPoints.get(0),p));
				insideLines.add(new Line2D.Double(p,midPoints.get(1)));
			}
			else{			
				Node startNode = null;
				Node endNode = null;
				Point [] midP = new Point[2];
				int index = 0;
				int startIndex = 0;
				
				ArrayList<Point> tempP = new ArrayList<Point>();
				for(int x = 0; x <faceEdgeList.size(); x++){
					FaceEdge fe = faceEdgeList.get(x);
					Node n1 = fe.getFrom();
					Node n2 = fe.getTo();
					if(fe.getEdge().getLabel().compareTo(label) == 0){
						if(!tempP.contains(fe.getEdge().getMidPoint()))
							tempP.add(fe.getEdge().getMidPoint());
						
						Node temp;
						if(n1.getLabel().contains(label)){
							temp = n2;
						}
						else{
							temp = n1;
						}
						if(index == 0){
							startNode = temp;
							midP[0] = fe.getEdge().getMidPoint();
							startIndex = x;
							index =1;
						}
						if(index == 1 && !temp.equals(startNode)){
							endNode = temp;
							midP[1] = fe.getEdge().getMidPoint();
							index = 2;
						}
					}
				}	
	
				ArrayList<Node> tempList1 = new ArrayList<Node>();
				for(int j = startIndex+1; j< nodeList.size(); j++){
					if(!nodeList.get(j).equals(startNode)&& !nodeList.get(j).equals(endNode))
						tempList1.add(nodeList.get(j));				
				}
				for(int k = 0; k< startIndex; k++){
					if(!nodeList.get(k).equals(startNode)&& !nodeList.get(k).equals(endNode)){
						tempList1.add(nodeList.get(k));
					}				
				} 
				
				ArrayList<Node> reversedNodes = new ArrayList<Node>();
				for(int l =  nodeList.size() -1; l>=0; l--){
					reversedNodes.add( nodeList.get(l));
					if(nodeList.get(l).equals(startNode))
							startIndex = l;
					
				}
				ArrayList<Node> tempList2 = new ArrayList<Node>();
				for(int m = startIndex; m< reversedNodes.size(); m++){
					if(! reversedNodes.get(m).equals(startNode)&& ! reversedNodes.get(m).equals(endNode))
						tempList2.add( reversedNodes.get(m));				
				}
				for(int n = 0; n< startIndex; n++){
					if(! reversedNodes.get(n).equals(startNode)&& ! reversedNodes.get(n).equals(endNode)){
						tempList2.add( reversedNodes.get(n));
					}				
				} 
				ArrayList<Point> tempP1 = new ArrayList<Point>();
				for(int o = 0; o < (int)(tempList1.size()/2); o++){
					Node n1 = tempList1.get(o);
					Node n2 = tempList2.get(o);
					if(!hasEdge(n1,n2)){					
						Point p = new Point((int)((n1.getX()+n2.getX())/2),(int)((n1.getY()+n2.getY())/2) );
						if(polygon.contains(p) && !tempP1.contains(p))
						tempP1.add(p);
					}
				}
				
				midPoints.add(tempP.get(0));
				for(Point p : tempP1){
					midPoints.add(p);
				}
				for(int x = 1; x< tempP.size(); x++){
					midPoints.add(tempP.get(x));
				}
				
				
	
				if(midPoints.size()>2){
					for(int i = 0 ; i < midPoints.size()-1; i++){
						if(midPoints.get(i)!=null && midPoints.get(i+1)!=null){
						Line2D line = new Line2D.Double(midPoints.get(i),midPoints.get(i+1));
						insideLines.add(line);
						}
					}
				}
			}	
		}
		return insideLines;
	}
	/**
	 * method to check if a line intersect with a list of lines
	 * */
	public boolean intersect(Line2D l, ArrayList<Line2D> lines){
			for(Line2D l1 : lines){
			if (l.intersectsLine(l1))
			return true;
		}
		return false;		
		
	}
	/**
	 * middle point of two nodes
	 * */
	public Point midPoint(Node n1, Node n2){
		
		Point p1 = n1.getCentre();
		Point p2 = n2.getCentre();
		 return new Point((int)(p1.getX()+p2.getX())/2, (int)(p1.getY()+p2.getY())/2);
		
	}

	/**
	 * method to print the nodes and face edges of the face
	 * */
	public void print(){
		System.out.println("face");
		for(Node node : nodeList){
			System.out.println(node.getLabel());
		}		
		for(FaceEdge fe : faceEdgeList){
			fe.print();
		}
	}
	/**
	 * method to check if this face contains a face edge
	 * */
	protected boolean hasFaceEdge(FaceEdge faceEdge){
		if(this.faceEdgeList!= null){
			for(FaceEdge fe: faceEdgeList){
				if(fe.getFrom().equals(faceEdge.getFrom())&& fe.getTo().equals(faceEdge.getTo()))				
					return true;		
			}
		}
		return false;		
	}
	/**
	 * returns the nodes and face edges of the face
	 * */
	public String toString(){
		
		StringBuffer ret = new StringBuffer();
		
		for(Node node : nodeList){
			String label = node.getLabel();
			if(label.equals("")) {
				label = "0";
			}
			ret.append(label+" ");
		}
		ret.append("| ");
		for(FaceEdge fe : faceEdgeList){
			ret.append(fe.toString()+" ");
		}
		return ret.toString();
	}
	/**
	 * returns the centre of the face
	 * */
	public Point getCentre(String alabel){		
		HashSet<Point> midPoints = new HashSet<Point>();
		for(Node n1 : nodeList){
			for(Node n2 : nodeList){
				 if(!n1.equals(n2)&& !hasEdge(n1,n2)&&(n1.getLabel().contains(alabel)||n2.getLabel().contains(alabel))){
					 Point p = new Point((int)((n1.getX()+n2.getX())/2),(int)((n1.getY()+n2.getY())/2));
					 midPoints.add(p);		 
				 }			 
			 }		}
		for(Point mp : midPoints){
			if(generatePolygon().contains(mp.getX(), mp.getY()))
				 return mp;
		}
		return null;		
	}
	
	
	/**
	 * triangulate the face and returns a list of tri nodes
	 * */
	public ArrayList<Node[]> generateTrisNodeList(){
		//this.print();
		
		ArrayList<Node[]> ret = new ArrayList<Node[]>();
		ArrayList<Node> nodeTriples = new ArrayList<Node>();

		PolygonTriangulator bt = new PolygonTriangulator();
		for(int i = 0 ; i < polygon.npoints; i++){
			double x = polygon.xpoints[i];
			double y = polygon.ypoints[i];
			bt.addPolyPoint(x, y);		
		}
		ArrayList<Point2D> tris = bt.getTris();
//System.out.println(tris+" "+toString());
		if(tris!=null){
			for(Point2D p : tris){
				double x = p.getX();
				double y = p.getY();
				for(Node n: nodeList){					
					if((n.getCentre().getX() == x)&&( n.getCentre().getY() == y))
						nodeTriples.add(n);	
				//	System.out.println("node" + n.getLabel());
				}
			}
			
			for(int j = 0 ; j < nodeTriples.size(); j+=3){
				Node [] nList = new Node [3];
				nList[0] = nodeTriples.get(j);
				nList[1] = nodeTriples.get(j+1);
				nList[2] = nodeTriples.get(j+2);
				ret.add(nList);
			}
		}
	
		return ret;
	
	}


	/**
	 * Resets the remainingCrossingContours to be all the crossing
	 * contours in the face
	 */
	public void resetRemainingCrossingContours() {
		remainingCrossingContours = new ArrayList<String>(crossingContours);
	}

	
	/**
	 * Removes the String containing two ordered chars from the remaining list
	 */
	public boolean removeRemainingCrossingContour(String cross) {
		if(!remainingCrossingContours.contains(cross)) {
			return false;
		}
		remainingCrossingContours.remove(cross);
		return true;
	}
	/**
	 * method to check if a all nodes in a list is the face nodes of a particular face
	 * */
	public boolean allNodesInFaceNodeList( ArrayList<Node> nodeList){		
		for(Node n : nodeList){
			if(!hasNode(n))
				return false;		
		}
		return true;		
	}
	/**
	 * method to check if all nodes are in a face node list
	 * */
	public boolean allNodesInFace(ArrayList<Node> nodeList){
		
		for(Node n : nodeList){
			Point p = n.getCentre();
			if(!polygon.contains(p))
				return false;
		}
		return true;
	}	



}
