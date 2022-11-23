package euler;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import euler.comparators.*;

import pjr.graph.*;

/** The edge of a triangulation. Three of these per TriangulationFace */
public class TriangulationEdge {

	protected Node from;
	protected Node to;
	/** The matching edge in the graph, or null if there is no such edge */
	protected Edge edge;
	protected boolean visited;
	/**
	 * The faces which the triangulation edge is in.
	 * There will be one or two, depending on if it is a new
	 * triangulation edge, or one paralleling an existing edge
	 */
	protected ArrayList<Face> faceList = new ArrayList<Face>();
	/**
	 * The triangulation faces which the triangulation edge is in.
	 * There will be two for edges on inner faces, one for edges
	 * on the outer face. These are added when the Triangulation Face
	 * is created.
	 */
	protected ArrayList<TriangulationFace> triangulationFaceList = new ArrayList<TriangulationFace>();;
	/**
	 * The coordinates of the contours through the edge. null if not assigned.
	 * The list is maintained as sorted during adding coordinates, with the
	 * ccs closest to the from node at the begining
	 */
	protected ArrayList<CutPoint> cutPoints = null; 
	protected String label = "";

	protected Color unassignedLineColor = Color.red;
	protected Color assignedLineColor = Color.blue;
    protected BasicStroke stroke = new BasicStroke(1.0f);
	protected Color unassignedTextColor = Color.red;
	protected Color assignedTextColor = Color.blue;

	
	public TriangulationEdge(Node inFrom, Node inTo, Edge e, Face f) {
		from = inFrom;
		to = inTo;
		edge = e;
		label = DualGraph.findLabelDifferences(inFrom.getLabel(), inTo.getLabel());

		faceList = new ArrayList<Face>();
		addFace(f);
		triangulationFaceList = new ArrayList<TriangulationFace>();
	}

	public Node getFrom() {return from;}
	public Node getTo() {return to;}
	public Edge getEdge() {return edge;}
	public boolean getVisited() {return visited;}
	public ArrayList<Face> getFaceList() {return faceList;}
	public ArrayList<TriangulationFace> getTriangulationFaceList() {return triangulationFaceList;}
	public String getLabel() {return label;}
	public Color getUnassignedLineColor() {return unassignedLineColor;}
	public Color getAssignedLineColor() {return assignedLineColor;}
	public BasicStroke getStroke() {return stroke;}
	public Color getUnassignedTextColor() {return unassignedTextColor;}
	public Color getAssignedTextColor() {return assignedTextColor;}
	public ArrayList<CutPoint> getCutPoints() {return cutPoints;}

	public void setFrom(Node from) {this.from = from;}
	public void setTo(Node to) {this.to = to;}
	public void setEdge(Edge edge) {this.edge = edge;}
	public void setVisited(Boolean flag) {this.visited = flag;}
	public void setLabel(String label) {this.label = label;}
	public void setUnassignedLineColor(Color unassignedLineColor) {this.unassignedLineColor = unassignedLineColor;}
	public void setStroke(BasicStroke stroke) {this.stroke = stroke;}
	public void setUnassignedTextColor(Color unassignedTextColor) {this.unassignedTextColor = unassignedTextColor;}
	public void setAssignedTextColor(Color assignedTextColor) {this.assignedTextColor = assignedTextColor;}
	public void setCutPoints(ArrayList<CutPoint> cps) {this.cutPoints = cps;}

	/**
	 * Add a face to the faceList as long as it is not already present. Returns true
	 * if it was added, false if not.
	 */
	public boolean addFace(Face face) {
		if(faceList.contains(face)) {
			return false;
		}
		faceList.add(face);
		return true;
		}
	
	/**
	 * Add a TF to the triangulationFaceList as long as it is not already present.
	 * Returns true if it was added, false if not.
	 */
	public boolean addTriangulationFace(TriangulationFace tf) {
		if(triangulationFaceList.contains(tf)) {
			return false;
		}
		triangulationFaceList.add(tf);
		return true;
		}
	
	/**
	 * Assign CutPoints to contours evenly between the nodes. The
	 * first element in contourOrder is closest to the from node.
	 */
	public void assignCutPointsBetweenNodes(ArrayList<String> contourOrder) {
		assignCutPointsBetweenPoints(contourOrder,getFrom().getCentre(),getTo().getCentre());
		sortCutPoints();
	}
	
	
	/**
	 * Assign coordinates to contours evenly between the points.
	 */
	public void assignCutPointsBetweenPoints(ArrayList<String> contourOrder, Point p1, Point p2) {
		int xDiff = p2.x-p1.x;
		int yDiff = p2.y-p1.y;
		
		int xStep = xDiff/(contourOrder.size()+1);
		int yStep = yDiff/(contourOrder.size()+1);
		
		cutPoints = new ArrayList<CutPoint>();

		int xPos = p1.x+xStep;
		int yPos = p1.y+yStep;
		for(String contours : contourOrder) {
			Point p = new Point(xPos,yPos);
			CutPoint cp = new CutPoint(this,new ArrayList<ContourLink>(),p);
			
			// create a contour link for each contour
			ArrayList<String> contourList = AbstractDiagram.findContourList(contours);
			for(String contour : contourList) {
				new ContourLink(contour,cp,null,null);  // this adds the cl to the cp
			}
			
			cutPoints.add(cp);
			xPos += xStep;
			yPos += yStep;
		}

	}
	
	
	/**
	 * Set the min and max limit for the contour routing through the TEs.
	 * @param gapFraction gives the total fraction of space given over
	 * to padding.
	 */
	public void assignCPRange(CutPoint cp, double gapFraction) {
		if(cp.getTriangulationEdge() == null) {
			// case of point in middle of TF
			return;
		}
		
		TriangulationEdge te = cp.getTriangulationEdge();
		ArrayList<CutPoint> cpList = te.getCutPoints();
		Node from = te.getFrom();
		Node to = te.getTo();
		int fromX = from.getX();
		int fromY = from.getY();
		int toX = to.getX();
		int toY = to.getY();
		double lengthX = Math.abs(fromX-toX);
		double lengthY = Math.abs(fromY-toY);
		// make sure min is nearest the node with the label
		Node labelNode = from;
		// assumption of single element contour link here
		if(to.getLabel().contains(cp.getContourLinks().get(0).getContour())) {
			labelNode = to;
		}
		double paddingX = gapFraction*(lengthX/(cpList.size()+1));
		double paddingY = gapFraction*(lengthY/(cpList.size()+1));
		double paddingRequirementX = paddingX*(cpList.size()+1);
		double paddingRequirementY = paddingY*(cpList.size()+1);
		double standardRangeX = (lengthX-paddingRequirementX)/cpList.size();
		double standardRangeY = (lengthY-paddingRequirementY)/cpList.size();
		
		int cpIndex = cpList.indexOf(cp);
		double minDistanceX = paddingX;
		double minDistanceY = paddingY;
		if(cpIndex != 0) { // if the cp is the first in the list, then the limit is just a gap to the from node
			// if the cp closer to the from node has a limit
			// assigned, then this cp limit is next to the limit furthest from the from node
			CutPoint lowerCP = cpList.get(cpIndex-1);
			
			if(lowerCP.getMaxLimit() != null || lowerCP.getMinLimit() != null ) {
				
				Point otherLimit = lowerCP.getMaxLimit();
				
				double minFromDistance = pjr.graph.Util.distance(from.getCentre(), lowerCP.getMinLimit());
				double maxFromDistance = pjr.graph.Util.distance(from.getCentre(), lowerCP.getMaxLimit());
				if(minFromDistance > maxFromDistance) {
					otherLimit = lowerCP.getMinLimit();
				}

				double lowerCPMaxX = Math.abs(fromX - otherLimit.x);
				double lowerCPMaxY = Math.abs(fromY - otherLimit.y);
				minDistanceX = lowerCPMaxX+paddingX;
				minDistanceY = lowerCPMaxY+paddingY;
				
				
			} else {
				minDistanceX = paddingX+(standardRangeX+paddingX)*cpIndex;
				minDistanceY = paddingY+(standardRangeY+paddingY)*cpIndex;
			}
		}
		
		double maxDistanceX = lengthX-paddingX;
		double maxDistanceY = lengthY-paddingY;
		if(cpIndex != cpList.size()-1) { // if the cp is the last in the list, then the limit is just a gap to the to node
			// if the cp closer to the from node has a limit
			// assigned, then this cp limit is next to the limit closest to the from node
			CutPoint upperCP = cpList.get(cpIndex+1);
			
			if(upperCP.getMaxLimit() != null || upperCP.getMinLimit() != null ) {
				
				Point otherLimit = upperCP.getMaxLimit();
				
				double minFromDistance = pjr.graph.Util.distance(from.getCentre(), upperCP.getMinLimit());
				double maxFromDistance = pjr.graph.Util.distance(from.getCentre(), upperCP.getMaxLimit());
				if(minFromDistance < maxFromDistance) {
					otherLimit = upperCP.getMinLimit();
				}
				
				double upperCPMaxX = Math.abs(fromX - otherLimit.x);
				double upperCPMaxY = Math.abs(fromY - otherLimit.y);
				maxDistanceX = upperCPMaxX-paddingX;
				maxDistanceY = upperCPMaxY-paddingY;
			} else {
				maxDistanceX = (standardRangeX+paddingX)*(cpIndex+1);
				maxDistanceY = (standardRangeY+paddingY)*(cpIndex+1);
			}
		}

		int xMinCoord = pjr.graph.Util.convertToInteger(from.getX()+minDistanceX); 
		int xMaxCoord = pjr.graph.Util.convertToInteger(from.getX()+maxDistanceX); 
		if(to.getX() < from.getX()) {
			xMinCoord = pjr.graph.Util.convertToInteger(from.getX()-minDistanceX); 
			xMaxCoord = pjr.graph.Util.convertToInteger(from.getX()-maxDistanceX); 
		}
		int yMinCoord = pjr.graph.Util.convertToInteger(from.getY()+minDistanceY); 
		int yMaxCoord = pjr.graph.Util.convertToInteger(from.getY()+maxDistanceY); 
		if(to.getY() < from.getY()) {
			yMinCoord = pjr.graph.Util.convertToInteger(from.getY()-minDistanceY); 
			yMaxCoord = pjr.graph.Util.convertToInteger(from.getY()-maxDistanceY); 
		}
		
		Point minPoint = new Point(xMinCoord,yMinCoord);
		Point maxPoint = new Point(xMaxCoord,yMaxCoord);

		// make sure min is nearest the node with the label
		double minLabelDistance = pjr.graph.Util.distance(labelNode.getCentre(), minPoint);
		double maxLabelDistance = pjr.graph.Util.distance(labelNode.getCentre(), maxPoint);
		if(minLabelDistance > maxLabelDistance) {
			// swap the min and max points
			Point tempPoint = minPoint;
			minPoint = maxPoint;
			maxPoint = tempPoint;
		}
		
		cp.setMinLimit(minPoint);
		cp.setMaxLimit(maxPoint);
//Rectangle r1 = new Rectangle(minPoint.x-3,minPoint.y-3,6,6);
//Rectangle r2 = new Rectangle(maxPoint.x-3,maxPoint.y-3,6,6);
//DiagramPanel.areas.add(new Area(r1));
//DiagramPanel.areas.add(new Area(r2));
//System.out.println("contour "+cp.getContourLinks()+" min "+cp.getMinLimit()+ " max "+cp.getMaxLimit());
	}


	
	/**
	 * Assign coordinates to contours evenly between the points. The
	 * first element in contourOrder is closest to p1.
	 */
	public void addContourPoint(String contour, Point point) {

		CutPoint cp = new CutPoint(this,new ArrayList<ContourLink>(),point);
		new ContourLink(contour,cp,null,null); // this adds the cl to the cp
		
		if(cutPoints == null) {
			cutPoints = new ArrayList<CutPoint>();
		}
		cutPoints.add(cp);
		
		sortCutPoints();
	}

	public ArrayList<String> findContourList() {
		ArrayList<String> contours = AbstractDiagram.findContourList(getLabel());
		return contours;
	}

	
	public void sortCutPoints() {
		if(cutPoints == null) {
			return;
		}
		CutPointComparator cpc = new CutPointComparator(getFrom().getCentre());
		Collections.sort(cutPoints, cpc);
	}

	
	/**
	 * Return the ContourPoints with the contour, or empty list if there
	 * is no such ContourPoint
	 */
	public ArrayList<ContourLink> contourLinksWithContour(String contour) {
		ArrayList<ContourLink> ret = new ArrayList<ContourLink>();
		if(getCutPoints() == null) {
			return ret;
		}
		for(CutPoint cp : getCutPoints()) {
			for(ContourLink cl : cp.getContourLinks()) {
				if(cl.getContour().equals(contour)) {
					ret.add(cl);
				}
			}
		}
		return ret;
	}
	
	
	public String toString() {
		String ret = getLabel()+":"+getFrom().getLabel()+","+getTo().getLabel();
		if(cutPoints != null) {
			ret += "|"+cutPoints;
		}
		return ret;
	}

}
