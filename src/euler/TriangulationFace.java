package euler;

import java.awt.*;
import java.util.*;

import pjr.graph.*;

/** Three edges, three nodes. A triangulation of a face of a planar graph. */
public class TriangulationFace {
	
	/** The face in the dual graph which this triangle is part of */ 
	protected Face face = null;
	protected TriangulationEdge te1 = null;
	protected TriangulationEdge te2 = null;
	protected TriangulationEdge te3 = null;
	// Nodes are the corners of the face
	protected Node node1 = null;
	protected Node node2 = null;
	protected Node node3 = null;
	
	/** This is only set if this TF is the TF in the Face with the meeting point in it */
	protected CutPoint meetingPoint = null;
	

	TriangulationFace(Face face, TriangulationEdge te1, TriangulationEdge te2, TriangulationEdge te3) {
		this.face = face;
		this.te1 = te1;
		this.te2 = te2;
		this.te3 = te3;
		
		node1 = te1.getFrom();
		node2 = te1.getTo();
		node3 = te2.getFrom();
		if(te2.getFrom() == node1 || te2.getFrom() == node2) {
			node3 = te2.getTo();
		}
		
		te1.addTriangulationFace(this);
		te2.addTriangulationFace(this);
		te3.addTriangulationFace(this);
	}
	
	public Face getFace() {return face;}
	public TriangulationEdge getTE1() {return te1;}
	public TriangulationEdge getTE2() {return te2;}
	public TriangulationEdge getTE3() {return te3;}
	public Node getNode1() {return node1;}
	public Node getNode2() {return node2;}
	public Node getNode3() {return node3;}
	public CutPoint getMeetingPoint() {return meetingPoint;}

	public void setMeetingPoint(CutPoint cp) {meetingPoint = cp;}

	
	public ArrayList<String> findContourList() {
		ArrayList<String> contours = new ArrayList<String>(); 

		ArrayList<String> teContours;
		
		teContours = te1.findContourList();
		contours.addAll(teContours);

		teContours = te2.findContourList();
		contours.addAll(teContours);

		teContours = te3.findContourList();
		contours.addAll(teContours);

		Collections.sort(contours);
		AbstractDiagram.removeDuplicatesFromSortedList(contours);
		return contours;
		
	}
	

	/**
	 * Finds the contour list in order, starting at the
	 * from node of TE1.
	 * 
	 */
	public ArrayList<String> findCycleContours() {
		ArrayList<String> ret = new ArrayList<String>();
		for(ContourLink cl : findCycleContourLinks()) {
			ret.add(cl.getContour());
		}
		return ret;
	}
	
	
	/**
	 * Get all the ContourLinks in the TF starting at the
	 * from node of TE1.
	 */
	public ArrayList<ContourLink> findCycleContourLinks() {
		ArrayList<ContourLink> ret = new ArrayList<ContourLink>();
		for(CutPoint cp : findCycleCutPoints()) {
			ret.addAll(cp.getContourLinks());
		}
		return ret;
	}
	
	/**
	 * Get all the CutPoints in the TF starting at the
	 * from node of TE1.
	 */
	public ArrayList<CutPoint> findCycleCutPoints() {
		ArrayList<CutPoint> cpList = new ArrayList<CutPoint>(); 

		if(te1.getCutPoints() == null) {
			return null;
		}
		cpList.addAll(te1.getCutPoints());

		TriangulationEdge teSecond = findOtherConnectingTE(te1.getTo(), te1);
		if(teSecond.getCutPoints() == null) {
			return null;
		}
		ArrayList<CutPoint> secondCPList = new ArrayList<CutPoint>();
		secondCPList.addAll(teSecond.getCutPoints());
		// cut points always ordered from the from node
		if(te1.getTo() == teSecond.getTo()) {
			Collections.reverse(secondCPList);
		}
		cpList.addAll(secondCPList);

		TriangulationEdge teThird = findOtherConnectingTE(te1.getFrom(), te1);
		if(teThird.getCutPoints() == null) {
			return null;
		}
		ArrayList<CutPoint> thirdCPList = new ArrayList<CutPoint>();
		thirdCPList.addAll(teThird.getCutPoints());
		// cut points always ordered from the from node
		if(te1.getFrom() == teThird.getFrom()) {
			Collections.reverse(thirdCPList);
		}
		cpList.addAll(thirdCPList);
		
		return cpList;
	}

	
	
	/**
	 * Return the other Edge in the triangle connecting to the node, or
	 * null if the node is not in the triangle.
	 */
	public TriangulationEdge findOtherConnectingTE(Node n, TriangulationEdge te) {
		if(te.getFrom() != n && te.getTo() != n) {
			// TE not connected to n
			return null;
		}
		if(te1.getTo() == n && te1 != te) {
			return te1;
		}
		if(te2.getTo() == n && te2 != te) {
			return te2;
		}
		if(te3.getTo() == n && te3 != te) {
			return te3;
		}
		if(te1.getFrom() == n && te1 != te) {
			return te1;
		}
		if(te2.getFrom() == n && te2 != te) {
			return te2;
		}
		if(te3.getFrom() == n && te3 != te) {
			return te3;
		}
		
		return null;
	}
		
		
	/**
	 * Find the Tes with the contour going through.
	 */
	public ArrayList<TriangulationEdge> findTEsWithContour(String contour) {
		ArrayList<TriangulationEdge> ret = new ArrayList<TriangulationEdge>();
		if(te1.getCutPoints() == null || te2.getCutPoints() == null || te3.getCutPoints() == null) {
			return ret;
		}
		
		for(CutPoint cp : te1.getCutPoints()) {
			for(ContourLink cl : cp.getContourLinks()) {
				if(cl.getContour().equals(contour)) {
					ret.add(te1);
				}
			}
		}
		for(CutPoint cp : te2.getCutPoints()) {
			for(ContourLink cl : cp.getContourLinks()) {
				if(cl.getContour().equals(contour)) {
					ret.add(te2);
				}
			}
		}
		for(CutPoint cp : te3.getCutPoints()) {
			for(ContourLink cl : cp.getContourLinks()) {
				if(cl.getContour().equals(contour)) {
					ret.add(te3);
				}
			}
		}


		return ret;
	}
	
	public String toString() {
		String ret = node1.getLabel()+","+node2.getLabel()+","+node3.getLabel();
		return ret;
	}

	
	
	/**
	 * Check that each crossing in the TF uses a common
	 * contour. So crossings like [ab, ac, ad] returns true,
	 * [ab, cd] returns false.
	 * Assumes the TE cut points have been assigned an order.
	 * Returns false if the cut points have not been assigned.
	 */
	public boolean correctContourCrossings() {
		
		ArrayList<String> crosses = findCrossingContours();
		if(crosses == null) {
			return false;
		}
		if(crosses.size() <= 1) {
			return true;
		}
		ArrayList<String> crossingContours = new ArrayList<String>(crosses);
		String cross1 = crossingContours.get(0);
		crossingContours.remove(0);
		char contour11 = cross1.charAt(0);
		char contour12 = cross1.charAt(1);
		
		String cross2 = crossingContours.get(0);
		crossingContours.remove(0);
		char contour21 = cross2.charAt(0);
		char contour22 = cross2.charAt(1);
		
		char crossContour = ' ';
		if(contour11 == contour21 || contour11 == contour22) {
			crossContour = contour11;
		}
		if(contour12 == contour21 || contour12 == contour22) {
			crossContour = contour12;
		}
		if(crossContour == ' ') {
			return false;
		}
		for(String crossi : crossingContours) {
			char contouri1 = crossi.charAt(0);
			char contouri2 = crossi.charAt(1);
			if(crossContour != contouri1 && crossContour != contouri2) {
				return false;
			}
		}
		
		return true;
	}

	
	
	/** 
	 * Assuming the TE cut points have an assigned order,
	 * this returns the contours that cross in the TF. Returns
	 * null if the cut points have not been assigned.
	 */
	public ArrayList<String> findCrossingContours() {
		ArrayList<String> crossingContours = new ArrayList<String>();
		
		ArrayList<String> symbolList = findCycleContours();
		if(symbolList == null) {
			return null;
		}
		StringBuffer symbolSB = new StringBuffer();
		for(String s : symbolList) {
			symbolSB.append(s);
		}
		String faceSymbols = symbolSB.toString();
		
		char first = ' ';
		char second = ' ';
		
		ArrayList<String> sortedList = new ArrayList<String>(symbolList);
		Collections.sort(sortedList);
		AbstractDiagram.removeDuplicatesFromSortedList(sortedList);
		
		for(int i = 0; i < sortedList.size(); i++) {
			first = sortedList.get(i).charAt(0);
		    if(i+1 < sortedList.size()) {
				for(int j = i+1; j < sortedList.size(); j++) {
					second = sortedList.get(j).charAt(0);	
 					if(Face.findIfContoursCross(first, second, faceSymbols)) {
						String crossing = Character.toString(first)+Character.toString(second);
 						if(first > second) {
 							crossing = Character.toString(second)+Character.toString(first);
 						}
 						crossingContours.add(crossing);
 					}
				}
			}
		}
		return crossingContours;
	}
	

	/** Find the centriod */
	public Point centroid() {
		int x1 = getNode1().getX();
		int y1 = getNode1().getY();
		int x2 = getNode2().getX();
		int y2 = getNode2().getY();
		int x3 = getNode3().getX();
		int y3 = getNode3().getY();
		return new Point((x1+x2+x3)/3,(y1+y2+y3)/3);
	}

	/** Test to see if this TF is the meeting TF in its face */
	public boolean isMeetingTF() {
		Face face = getFace();
		if(face == null) {
			return false;
		}
		TriangulationFace meetingTF = face.getMeetingTF();
		if(meetingTF == null) {
			return false;
		}
		if(this != meetingTF) {
			return false;
		}
		return true;
	}

	
	public ArrayList<TriangulationEdge> getUnvisitedTEs() {
		ArrayList<TriangulationEdge> ret = new ArrayList<TriangulationEdge>();
		if(!te1.getVisited()) {
			ret.add(te1);
		}
		if(!te2.getVisited()) {
			ret.add(te2);
		}
		if(!te3.getVisited()) {
			ret.add(te3);
		}
		return ret;
	}

	
	/**
	 * Returns the shared TE, or null if there is not one.
	 */
	public TriangulationEdge findJoiningTE(TriangulationFace tf) {
		if(te1 == tf.getTE1()) {return te1;}
		if(te1 == tf.getTE2()) {return te1;}
		if(te1 == tf.getTE3()) {return te1;}

		if(te2 == tf.getTE1()) {return te2;}
		if(te2 == tf.getTE2()) {return te2;}
		if(te2 == tf.getTE3()) {return te2;}

		if(te3 == tf.getTE1()) {return te3;}
		if(te3 == tf.getTE2()) {return te3;}
		if(te3 == tf.getTE3()) {return te3;}

		return null;
	}

	public Polygon generateTriangle() {
		Polygon p = new Polygon();
		p.addPoint(node1.getX(), node1.getY());
		p.addPoint(node2.getX(), node2.getY());
		p.addPoint(node3.getX(), node3.getY());
		
		return p;

	}

	/**
	 * See if the triangle contains the point
	 */
	public boolean contains(Point point) {
		
		Polygon polygon = generateTriangle();
		if(polygon.contains(point)) {
			return true;
		}
		return false;
	}
	
}
