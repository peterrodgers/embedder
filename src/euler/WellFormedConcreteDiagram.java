package euler;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

import euler.polygon.*;

import pjr.graph.*;


/** Draw wellformed dual graphs */
public class WellFormedConcreteDiagram extends ConcreteDiagram {
	

	/**
	 * 
	 */
	public WellFormedConcreteDiagram(DualGraph dualGraph) {
		super(dualGraph);
	}

	/**
	 * This must be called before accessing concrete contours.
	 */
	public void generateContours() {
		
		dualGraph.formFaces();

		cloneGraph = dualGraph.clone();
		cloneGraph.formFaces();
		Face outerFace = cloneGraph.getOuterFace();
		ArrayList<Node> outerNodes;
		if(outerFace == null) {
			// assume that there is no outer face because the dual forms a straight
			// line or is empty
			outerNodes = cloneGraph.getNodes();
		} else {
			outerNodes = outerFace.getNodeList();
		}
		ConcreteDiagram.addBoundingNodes(cloneGraph,OUTER_FACE_TRIANGULATION_BOUNDARY,outerNodes);

		// take out the bounding nodes and edges to allow triangulation
		ArrayList<Node> removedNodes = WellFormedConcreteDiagram.getBoundingNodes(cloneGraph);
		ArrayList<Edge> removedEdges = WellFormedConcreteDiagram.getBoundingEdges(cloneGraph);
		ConcreteDiagram.removeBoundingNodes(cloneGraph);
		
		cloneGraph.formFaces();
		Face cloneOuterFace = cloneGraph.getOuterFace();
		cloneGraph.triangulate();
		
		// add the bounding nodes and edges back in
		for(Node n : removedNodes) {
			cloneGraph.addNode(n);
		}
		for(Edge e : removedEdges) {
			cloneGraph.addEdge(e);
		}

		ConcreteDiagram.triangulateBoundingFace(cloneGraph,cloneOuterFace);

		routeContours();
	}
	
	
	/**
	 * Assumes a triangulation, assigns order to the TEs and builds contours
	 */
	public void routeContours() {
		
		// clear the cps for all TEs
		resetTriangulationEdges();
		
		// first order the contours on every triangulation edge
		assignTriangulationEdgeOrder();

		// then connect up the contour cycles in the TEs
		assignTriangulationEdgeConnectivity(); 

		if(optimizeContourAngles) {
		// remove the kinks in the graph
			optimiseContourLines();
		}
		
		//build polygons based on TE CP loops
		buildContours();
		
		if(fitCircles) {
		// try and place regular polygons in allowed regions
			fitRegularPolygons();
		}
	}
	
	
	
	/**
	 * Finds regular polygons that can be found for any of the contours.
	 */
	public void fitRegularPolygons() {
		for(ConcreteContour cc : getConcreteContours()) {
			Polygon p = findRegularPolygonInAllowedRegion(cc);
			if(p != null) {
				cc.setPolygon(p);
			}
		}
	}
	
	

	/**
	 * Find a regular polygon between the min and max of the contour cps.
	 * @return a regular polygon that fits, or null if one cannot be found.
	 */
	public Polygon findRegularPolygonInAllowedRegion(ConcreteContour cc) {
		
		String contour = cc.getAbstractContour();
		TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
		ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);

		// find the allowed range for the contour
		ContourLink cl = null;

		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}
			CutPoint cp = cl.getCutPoint();
			startTE.assignCPRange(cp,0.1);
			
			cl = cl.getNext();
		}
		
//if(cc.getAbstractContour().equals("a")) {
	
		Polygon minP = cc.findMinPolygon(cloneGraph);
		Polygon maxP = cc.findMaxPolygon(cloneGraph);
		
/*		
System.out.println("Contour "+cc.getAbstractContour());
System.out.println("Inner ");
for(int i = 0; i< minP.npoints; i++) {
	System.out.print("("+minP.xpoints[i]+","+minP.ypoints[i]+") ");
}
System.out.println();
System.out.println("Outer ");
for(int i = 0; i< maxP.npoints; i++) {
	System.out.print("("+maxP.xpoints[i]+","+maxP.ypoints[i]+") ");
}
System.out.println();
*/
//Polygon ret = null;

		Polygon ret = RegularPolygon.insideCircle(maxP,minP);
		
		return ret;
	}

	
	
	/**
	 * Orders the contours in each triangulation edge. We need to meet the
	 * following criteria:
	 * <p>
	 * 1. Exactly the number of crossings given by the crossing index can occur
	 * in each face
	 * <p>
	 * 2. All the crossing in a triangle must include a specified contour. This
	 * contour can cross several other contours in the triangle.
	 * <p>
	 * 
	 * We currently take a greedy method.
	 * 
	 * First we assign the single contour TEs, these are ordered trivially Then
	 * we take TEs with 2 assigned edges and attempt to assign the third edge.
	 * This edge order is assigned by taking the contour that has most
	 * potential crosses in the triangle and performing those crosses.
	 * 
	 */
	public void assignTriangulationEdgeOrder() {

		ArrayList<TriangulationEdge> teList = cloneGraph.findTriangulationEdges();
		ArrayList<TriangulationFace> tfList = cloneGraph.getTriangulationFaces();

		// find the 0 contour edges and assign no coordinates (these are in
		// the triangulation of the outer faces)
		for (TriangulationEdge te : teList) {
			if (te.getCutPoints() != null) {
				// don't want to deal with any edges already assigned
				// coordinates
				continue;
			}
			if (te.getLabel().length() == 0) {
				ArrayList<String> contours = new ArrayList<String>();
				te.assignCutPointsBetweenNodes(contours);
			}
		}

		// find the 1 contour edges and assign their coordinates first
		// because this is trivial
		for (TriangulationEdge te : teList) {
			if (te.getCutPoints() != null) {
				// dont want to deal with any edges already assigned
				// coordinates
				continue;
			}
			if (te.getLabel().length() == 1) {
				ArrayList<String> contours = new ArrayList<String>();
				contours.add(te.getLabel());
				te.assignCutPointsBetweenNodes(contours);
			}
		}

		
		boolean unassignedTEs = true;
		while (unassignedTEs) { // keep going till all TEs are ordered. That is,
								// we have no TEs with 1 unassigned face
			unassignedTEs = false;
			for (TriangulationFace tf : tfList) {

				TriangulationEdge te1 = tf.getTE1();
				TriangulationEdge te2 = tf.getTE2();
				TriangulationEdge te3 = tf.getTE3();
				ArrayList<CutPoint> cc1 = te1.getCutPoints();
				ArrayList<CutPoint> cc2 = te2.getCutPoints();
				ArrayList<CutPoint> cc3 = te3.getCutPoints();
				if (cc1 == null && cc2 != null && cc3 != null) {
					assignTFOrder(tf, te1, te2, te3);
					unassignedTEs = true;
				}
				if (cc1 != null && cc2 == null && cc3 != null) {
					assignTFOrder(tf, te2, te1, te3);
					unassignedTEs = true;
				}
				if (cc1 != null && cc2 != null && cc3 == null) {
					assignTFOrder(tf, te3, te1, te2);
					unassignedTEs = true;
				}

			}
		}
		
	}
	
	
	/**
	 * Takes a face with two assigned TEs and assigns cut points to the
	 * unassigned TE by looking through the contours finding the one with
	 * greatest crossing in the TF and ensuring only those crossings occur
	 */
	public void assignTFOrder(TriangulationFace tf,
			TriangulationEdge unassignedTE, TriangulationEdge assignedTE1,
			TriangulationEdge assignedTE2) {

		Face face = tf.getFace();
		ArrayList<String> unassignedContours = unassignedTE.findContourList();

		// find the currently unassigned contour with the most number of
		// unassigned crossings
		String crossingContour = "";
		ArrayList<String> maxCrossingContours = new ArrayList<String>();
		for (String contour1 : unassignedContours) {
			ArrayList<String> crossingContours = new ArrayList<String>();
			for (String contour2 : unassignedTE.findContourList()) {
				if (contour1.equals(contour2)) {
					continue;
				}
				String cross = contour1 + contour2;
				if (contour1.charAt(0) > contour2.charAt(0)) {
					cross = contour2 + contour1;
				}
				if (face != null && face.getRemainingCrossingContours().contains(cross)) {
					// this can happen when there is no outer face in the dual graph
					// 2Venn for example
					crossingContours.add(contour2);
				}
			}
			if (crossingContours.size() > maxCrossingContours.size()) {
				crossingContour = contour1;
				maxCrossingContours = crossingContours;
			}
		}

		Node from = unassignedTE.getFrom();
		Node to = unassignedTE.getTo();
		TriangulationEdge te1 = tf.findOtherConnectingTE(to, unassignedTE);
		TriangulationEdge te2 = tf.findOtherConnectingTE(from, unassignedTE);
		
		ArrayList<CutPoint> ccList1 = new ArrayList<CutPoint>(te1.getCutPoints());
		ArrayList<CutPoint> ccList2 = new ArrayList<CutPoint>(te2.getCutPoints());
		
		// make sure the lists are pointing the right way,
		// they should be assigned the reverse of the list
		// derived from starting at the unassigned
		// from node ccList then followed by the next ccList.
		if (to == te1.getTo()) {
			Collections.reverse(ccList1);
		}
		if (from == te2.getFrom()) {
			Collections.reverse(ccList2);
		}

		ArrayList<CutPoint> contourCycle = new ArrayList<CutPoint>(ccList1);
		contourCycle.addAll(ccList2);
		Collections.reverse(contourCycle);
		
		if (crossingContour == "") {

			// case where there are no crosses in the face
			// go round the face and pick the contours in order
			// use that order for the unassigned face
			ArrayList<String> contourOrder = new ArrayList<String>();

			for (CutPoint cp : contourCycle) {
				if(cp.getContourLinks().size() == 1) {
					String contour = cp.getContourLinks().get(0).getContour();
					if (unassignedContours.contains(contour)
							&& !contourOrder.contains(contour)) {
						contourOrder.add(contour);
					}
				} else {
					System.out.println("In WellFormedConcreteContour - found a concurrent CutPoint: "+cp);
				}
			}
			unassignedTE.assignCutPointsBetweenNodes(contourOrder);
			
		} else {
			
			// case where a contour crosses several others
			// first go round the face and pick the non crossing
			// contours in order use that order for the unassigned face
			// code very similar to the last case
			//
			// Then test the contour in each position in between the others,
			// take the one that involves the maximum correct edge crossing

			// this will hold the order of the non crossing contours
			ArrayList<String> nonCrossOrder = new ArrayList<String>();

			// Test both lists to build the non crossing contours list
			for (CutPoint cp : contourCycle) {
				if(cp.getContourLinks().size() == 1) {
					String contour = cp.getContourLinks().get(0).getContour();
					if (contour.equals(crossingContour)) {
						continue;
					}
					if (unassignedContours.contains(contour)
							&& !nonCrossOrder.contains(contour)) {
						nonCrossOrder.add(contour);
					}
				} else {
					System.out.println("In WellFormedConcreteContour - found a concurrent CutPoint: "+cp);
				}
			}

			int maxCrosses = -1;
			ArrayList<String> finalContoursCrossed = new ArrayList<String>();
			ArrayList<String> finalOrder = new ArrayList<String>(nonCrossOrder);
			finalOrder.add(crossingContour); // assign something to this in
												// case of zero result
			for (int i = 0; i <= nonCrossOrder.size(); i++) {
				ArrayList<String> fullContourOrder = new ArrayList<String>(nonCrossOrder);
				fullContourOrder.add(i, crossingContour);
				ArrayList<String> crossingContours = findCrossingForContour(crossingContour, fullContourOrder, ccList1, ccList2);
				// check that crosses with non crossing contours does not occur
				boolean undesiredCross = false;
				for (String c : crossingContours) {
					String currentCross = crossingContour + c;
					if (crossingContour.charAt(0) > c.charAt(0)) {
						currentCross = c + crossingContour;
					}
					if (!face.getRemainingCrossingContours().contains(currentCross)) {
						//System.out.println("Triangle "+tf+" undesired cross attempted between "+crossingContour+c+" trying another assignment");
						undesiredCross = true;
					}
				}
				if (undesiredCross) {
					continue;
				}
				if (crossingContours.size() > maxCrosses) {
					maxCrosses = crossingContours.size();
					finalOrder = fullContourOrder;
					finalContoursCrossed = crossingContours;
				}

			}
			if (maxCrosses == -1) {
				System.out.println("Triangle " + tf
						+ " no valid crossing assignment found for "
						+ crossingContour + " using default assignment");
			}

			unassignedTE.assignCutPointsBetweenNodes(finalOrder);

			// remove the crossing so that it wont be attempted again
			for (String c : finalContoursCrossed) {
				// for safety, try it both ways
				String cross1 = c + crossingContour;
				String cross2 = crossingContour + c;
				face.removeRemainingCrossingContour(cross1);
				face.removeRemainingCrossingContour(cross2);
			}

		}

	}

	
	
	
	/**
	 * This converts the ContourCoordinate to lists of contours and checks the
	 * crossing conditions to see which contours cross the given contour.
	 */
	private ArrayList<String> findCrossingForContour(String contour,
			ArrayList<String> unassignedContourOrder,
			ArrayList<CutPoint> ccList1,
			ArrayList<CutPoint> ccList2) {
		
		ArrayList<String> ret = new ArrayList<String>();

		ArrayList<String> contourList = new ArrayList<String>(
				unassignedContourOrder);
		for (CutPoint cc : ccList1) {
			// single element assumed in the list
			contourList.add(cc.getContourLinks().get(0).getContour());
		}
		for (CutPoint cc : ccList2) {
			// single element assumed in the list
			contourList.add(cc.getContourLinks().get(0).getContour());
		}

		String contourString = new String();
		for (String c : contourList) {
			contourString += c;
		}

		ArrayList<String> contoursInFace = new ArrayList<String>(contourList);
		Collections.sort(contoursInFace);
		AbstractDiagram.removeDuplicatesFromSortedList(contoursInFace);

		for (String c1 : contoursInFace) {
			for (String c2 : contoursInFace) {
				if (c1.equals(c2)) {
					continue;
				}
				// can do this here because the pair will be compared both ways
				if (!c1.equals(contour)) {
					continue;
				}
				if (Face.findIfContoursCross(c1.charAt(0), c2.charAt(0),
						contourString)) {
					ret.add(c2);
				}
			}
		}

		return ret;

	}
	
	
	/**
	 * Takes the TEs after they have been assigned cut point order
	 * and finds ContourLink connectivity
	 */
	public void assignTriangulationEdgeConnectivity() {
		for(String contour : cloneGraph.findAbstractDiagram().getContours()) {
			TriangulationEdge currentTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
			if(currentTE == null) { // empty TE graph
				return;
			}
			ContourLink startCL = currentTE.contourLinksWithContour(contour).get(0);
			ContourLink currentCL = startCL;
			TriangulationFace currentTF = null;
			TriangulationFace nextTF = null;
			TriangulationEdge nextTE = null;
			ContourLink nextCL = null;
			while(nextCL != startCL) {
				if(currentTE.getTriangulationFaceList().size() < 2) {
					// connects to a outer face node so we have a problem
					// this may happen when bounding nodes are not added to
					// the graph
					break;
				}
				nextTF = currentTE.getTriangulationFaceList().get(0);
				if(nextTF == currentTF) {
					nextTF = currentTE.getTriangulationFaceList().get(1);
				}
				nextTE = nextTF.findOtherConnectingTE(currentTE.getFrom(),currentTE);
				ArrayList<ContourLink> nextCLList = nextTE.contourLinksWithContour(contour);
				if(nextCLList.size() == 0) {
					nextTE = nextTF.findOtherConnectingTE(currentTE.getTo(),currentTE);
					nextCL = nextTE.contourLinksWithContour(contour).get(0);
				} else {
					nextCL = nextCLList.get(0);
				}
				currentCL.setNext(nextCL);
				nextCL.setPrev(currentCL);
				currentTE = nextTE;
				currentTF = nextTF;
				currentCL = nextCL;
			}
		}
	}
	

	/**
	 * Find the ideal angle for each cut point.
	 * Triangulation Edges should already have been assigned.
	 */
	public void optimiseContourLines() {

		for(int tries = 0;tries <= 4; tries++) {
			// do this several times so that earlier assigned contours can try freed up space from later contour assignments
			for(String contour : getCloneGraph().findAbstractDiagram().getContours()) {
				optimizeContour(contour);
			}
		}

	}

	
	public void optimizeContour(String contour) {
		
		TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
		ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);
		
		// find the allowed range for the contour
		ContourLink cl;
		int clTotal = 0;
		cl = null;
		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}
			startTE.assignCPRange(cl.getCutPoint(),CONTOUR_GAP_PERCENT);
			
			cl = cl.getNext();
			clTotal++;
		}
		// now attempt to even out the angles of the contour
		// several iterations to smooth things out
		double averageAngle = Math.PI*(clTotal-2)/(double)clTotal;
		
		for(int i =0; i < CP_OPTIMIZATION_ITERATIONS; i++) {
		double moveDistance = CP_OPTIMIZATION_ITERATIONS-i;
//double moveDistance = 1.0;
			cl = null;
			while(startCL != cl) { // run one way round
				if(cl == null) {
					cl = startCL;
				}
				ContourLink prevCL = cl.getPrev();
				ContourLink nextCL = cl.getNext();
				assignCLAngle(prevCL, cl, nextCL, averageAngle, moveDistance);
				cl = cl.getNext();
			}
			
			// run the other way round
			cl = null;
			while(startCL != cl) {
				if(cl == null) {
					cl = startCL;
				}

				ContourLink prevCL = cl.getPrev();
				ContourLink nextCL = cl.getNext();
				assignCLAngle(nextCL, cl, prevCL, averageAngle, moveDistance);
				cl = cl.getPrev();
			}
		}

		// set the cut point min and max to the assigned angle so leaving more
		// space for subseqent contours
		cl = null;
		while(startCL != cl) {
			if(cl== null) {
				cl = startCL;
			}
			CutPoint cp = cl.getCutPoint();
			Point coordinate = cp.getCoordinate();
			cp.setMinLimit(coordinate);
			cp.setMaxLimit(coordinate);
			
			cl = cl.getNext();
		}
	}
	
	/**
	 * Assign the coordinate of the cp for the cl based on the angles of the cl and connecting cl.
	 */
	public void assignCLAngle(ContourLink cl1, ContourLink cl, ContourLink cl2, double averageAngle, double moveDistance) {
		TriangulationEdge te = cl.getCutPoint().getTriangulationEdge();
		Node from = te.getFrom();
		Node to = te.getTo();

		double distanceX = Math.abs(to.getX()-from.getX());
		double distanceY = Math.abs(to.getY()-from.getY());

		double shareX = distanceX/(distanceX+distanceY);
		double shareY = distanceY/(distanceX+distanceY);
		
		double moveX = moveDistance*shareX;
		if(to.getX() < from.getX()) {
			moveX = 0-moveX;
		}

		double moveY = moveDistance*shareY;
		if(to.getY() < from.getY()) {
			moveY = 0-moveY;
		}
		
		Point currentP = cl.getCutPoint().getCoordinate();
		double currentQuality = clQuality(cl,averageAngle);
		
		// two new points to try
		Point2D.Double pd1 = new Point2D.Double(currentP.x+moveX,currentP.y+moveY);
		Point2D.Double pd2 = new Point2D.Double(currentP.x-moveX,currentP.y-moveY);
		int p1x = pjr.graph.Util.convertToInteger(pd1.x);
		int p1y = pjr.graph.Util.convertToInteger(pd1.y);
		int p2x = pjr.graph.Util.convertToInteger(pd2.x);
		int p2y = pjr.graph.Util.convertToInteger(pd2.y);
		Point p1 = new Point(p1x,p1y);
		Point p2 = new Point(p2x,p2y);
		// deal with rounding errors and make sure the points are on the TE
		p1 = pjr.graph.Util.perpendicularPoint(p1,from.getCentre(),to.getCentre());
		p2 = pjr.graph.Util.perpendicularPoint(p2,from.getCentre(),to.getCentre());
						
		if(pjr.graph.Util.pointIsWithinBounds(p1, cl.getCutPoint().getMinLimit(), cl.getCutPoint().getMaxLimit())) {
			cl.getCutPoint().setCoordinate(p1);
			double newQuality = clQuality(cl,averageAngle);
			if(newQuality >= currentQuality) { // reset coordinate if the new quality is worse
				cl.getCutPoint().setCoordinate(currentP);
			} else {
				// if we use the new point, dont bother testing the second point
				return;
			}
		}
		if(pjr.graph.Util.pointIsWithinBounds(p2, cl.getCutPoint().getMinLimit(), cl.getCutPoint().getMaxLimit())) {
			cl.getCutPoint().setCoordinate(p2);
			double newQuality = clQuality(cl, averageAngle);
			if(newQuality >= currentQuality) { // reset coordinate if the new quality is worse
				cl.getCutPoint().setCoordinate(currentP);
			}
			return;
		}

	}
	
	
	
		
	/**
	 * Quality of the given point for the CL, plus the one before and one after.
	 */
	public double clQuality(ContourLink cl, double averageAngle) {
		ContourLink cl1 = cl.getPrev();
		ContourLink cl3 = cl.getNext();
		
		double quality1 = singlePointQuality(cl1,averageAngle);
		double quality2 = singlePointQuality(cl,averageAngle);
		double quality3 = singlePointQuality(cl3,averageAngle);

		double quality = quality1*quality1+quality2*quality2+quality3*quality3;

		return quality;
	}

	/**
	 * Quality is the square of the difference from required
	 * angle, with an extra penalty for convex points. Returns
	 * low numbers for good quality, high numbers for poor quality.
	 */
	public double singlePointQuality(ContourLink cl, double averageAngle) {
		double angle = cpAngle(cl);
		double diff = angle-averageAngle;
		double pointQuality = diff*diff;
		// penalize concave points more heavily
		if(angle > Math.PI) {
			pointQuality = pointQuality*10;
		}
		pointQuality = pointQuality*pointQuality;
		return pointQuality;
	}
	
	
	/**
	 * The angle made by a cut point.
	 */
	public double cpAngle(ContourLink cl) {
		return cpAngle(cl,cl.getCutPoint().getCoordinate());
	}
	
	
	/**
	 * The angle made by a contour link if it were given the argument coordinate
	 */
	public double cpAngle(ContourLink cl, Point coordinate) {
		ContourLink cl1 = cl.getPrev();
		ContourLink cl3 = cl.getNext();
		Point p1 = cl1.getCutPoint().getCoordinate();
		Point p3 = cl3.getCutPoint().getCoordinate();
		
		TriangulationEdge te = cl.getCutPoint().getTriangulationEdge();
		Node from = te.getFrom();
		Node to = te.getTo();
		// we know the node with the contour label is inside the contour
		Node insideNode = from;
		if(to.getLabel().contains(cl.getContour())) {
			insideNode = to;
		}
		
		double angle = pjr.graph.Util.getRelativeAngle(p1, coordinate, p3, insideNode.getCentre());
		return angle;
	}
	
	
	/**
	 * Find the angle based quality of a contour from its CPs
	 */
	public double contourQuality(String contour) {
		double quality = 0.0;
		
		TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
		ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);
		
		// count the cut points
		ContourLink cl;
		int clTotal = 0;
		cl = null;
		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}

			cl = cl.getNext();
			clTotal++;
		}

		double averageAngle = Math.PI*(clTotal-2)/(double)clTotal;

		cl = null;
		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}
			
			double pointQuality = singlePointQuality(cl,averageAngle);
			quality += pointQuality;

			cl = cl.getNext();
		}
		
		return quality;
	}
	


	

	/**
	 * Finds the length of the contour.
	 * Cut Points must be assigned before this is called.
	 */
	public double findContourPerimeterLengthFromCP(String contour) {
		TriangulationEdge te = cloneGraph.firstTriangulationEdgeWithContour(contour);

		double length = 0.0;
		
		ContourLink startCL = te.contourLinksWithContour(contour).get(0);
		ContourLink cl = null;
		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}
			ContourLink nextCL = cl.getNext();
			Point nextP = nextCL.getCutPoint().getCoordinate();
			
			Point coordinate = cl.getCutPoint().getCoordinate();
			double distance = pjr.graph.Util.distance(nextP,coordinate);
			length+=distance;
			
			cl = cl.getNext();
		}
		return length;
	}
	
	

		
}
