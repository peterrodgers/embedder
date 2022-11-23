package euler;

import java.awt.*;
import java.util.*;

import euler.comparators.*;

/** A coordinate assignment for a group of abstract contours. */
public class CutPoint {
	
	protected TriangulationEdge te; // this is set for a CP on a te
	protected TriangulationFace tf; // this is set for a CP in the middle of a tf
	protected ArrayList<ContourLink> contourLinks;
	protected Point coordinate;
	/**
	 * The closest to the from node of the TE that the coordinate can be.
	 * Set this to the coordinate when the coordinate is fixed.
	 */
	protected Point minLimit = null;
	/**
	 * The furthest from the from node of the TE that the coordinate can be.
	 * Set this to the coordinate when the coordinate is fixed.
	 */
	protected Point maxLimit = null;
	
	private ContourLinkComparator clc = new ContourLinkComparator();

	
	public CutPoint(TriangulationEdge te, ArrayList<ContourLink> contourLinks, Point coordinate) {
		this.contourLinks = contourLinks;
		this.coordinate = coordinate;
		this.te = te;
		this.tf = null;
		
		Collections.sort(contourLinks,clc);
	}

	public CutPoint(TriangulationFace tf, ArrayList<ContourLink> contourLinks, Point coordinate) {
		this.contourLinks = contourLinks;
		this.coordinate = coordinate;
		this.te = null;
		this.tf = tf;
		
		Collections.sort(contourLinks,clc);
	}


	public TriangulationEdge getTriangulationEdge() {return te;}
	public TriangulationFace getTriangulationFace() {return tf;}
	public ArrayList<ContourLink> getContourLinks() {return contourLinks;}
	public Point getCoordinate() {return coordinate;}
	public Point getMaxLimit() {return maxLimit;}
	public Point getMinLimit() {return minLimit;}

	public void setCoordinate(Point coordinate) {this.coordinate = coordinate;}
	public void setMaxLimit(Point maxLimit) {this.maxLimit = maxLimit;}
	public void setMinLimit(Point minLimit) {this.minLimit = minLimit;}

	/** Only used in ContourLink constructor to maintain dual linkage between ContourLink and CutPoint */
	protected void addContourLink(ContourLink cl) {
		contourLinks.add(cl);
		Collections.sort(contourLinks,clc);
	}
	
	/**
	 * Find the contours in the argument that are also in this CP.
	 */
	public String findCommonContourLabels(String contours) {
		
		StringBuffer ret = new StringBuffer("");
		
		for(ContourLink cl : getContourLinks()) {
			if(contours.contains(cl.getContour())) {
				ret.append(cl.getContour());
			}
		}
		
		return ret.toString();
	}
		
	/**
	 * Find the ContourLink for the given contour, or return null.
	 */
	public ContourLink findCLWithContour(String contour) {
		
		for(ContourLink cl : getContourLinks()) {
			if(cl.getContour().equals(contour)) {
				return cl;
			}
		}
		return null;
	}
		

	
	public String toString() {
		String ret = "";
		for(ContourLink cl : contourLinks) {
			ret += cl.getContour()+";";
		}
		ret += coordinate.x+","+coordinate.y;

		return ret;
	}


	
}
