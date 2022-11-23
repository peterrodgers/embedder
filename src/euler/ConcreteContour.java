package euler;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import pjr.graph.Edge;
import pjr.graph.Node;
import pjr.graph.triangulator.PolygonTriangulator;
//import sun.security.action.GetLongAction;
import euler.maxrectangle.ConvexHull;

public class ConcreteContour {

	protected String abstractContour;
	protected Polygon polygon;
	protected Area area;
	protected Polygon maxArea;
	protected Polygon minArea;
	protected ArrayList<ContourLine> contourLines = null;
	
	public ConcreteContour() {
	
	}

	public ConcreteContour(String abstractContour, Polygon polygon) {
		this.abstractContour = abstractContour;
		this.polygon = polygon;
		resetArea();
	}
	public void setLabel(String aLabel){abstractContour=aLabel;}

	public String getAbstractContour() {
		return abstractContour;
	}
	public Polygon getPolygon() {
		return polygon;
	}

	public Area getArea() {
		return area;
	}

	public void resetArea() {
		if (polygon == null) {
			area = new Area();
		} else {
			area = new Area(polygon);
		}
		//setContourLines();
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;		
		resetArea();
	}
	
	public void setContourLines(){
		contourLines = new ArrayList<ContourLine>();
		if(polygon!=null){
		int nPoints = polygon.npoints;
			if(nPoints>2){
				for(int i = 0 ; i < nPoints -1; i++){						
					double x1 = polygon.xpoints[i];
					double y1 = polygon.ypoints[i];
					double x2 = polygon.xpoints[i+1];
					double y2 = polygon.ypoints[i+1];
					Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);
					contourLines.add(new ContourLine(abstractContour, line));			
				}		
				double xs = polygon.xpoints[0];
				double ys = polygon.ypoints[0];
				double xe = polygon.xpoints[nPoints -1];
				double ye = polygon.ypoints[nPoints -1];
				Line2D.Double line = new Line2D.Double(xe,ye,xs,ys);
				contourLines.add(new ContourLine(abstractContour, line));
			}
		}
	}
	public ArrayList<ContourLine> getContourLines(){
		return contourLines;
	}
	
	public ConcreteContour clone() {
		Polygon clonePolygon = new Polygon();
		
		Polygon polygon = getPolygon();
		
		for(int i = 0 ; i < polygon.npoints; i++){
			Point p = new Point(polygon.xpoints[i],polygon.ypoints[i]);
			
			clonePolygon.addPoint(p.x, p.y);
		}
		
		ConcreteContour cc = new ConcreteContour(getAbstractContour(),clonePolygon);
		
		return cc;
	}

	/**
	 * Generate the areas for each zone
	 */
	public static HashMap<String, Area> generateZoneAreas(
			ArrayList<ConcreteContour> concreteContours) {
		// We could try each possible intersection
		// but that is a guarenteed 2 power n algorithm
		// Here we take each intersecting pair and
		// test if any contours can be added to it.
		// The intersecting contours are then built up.
		// Then any intersections that are wholly contained
		// in the remaining contour set are removed.

		// the zones that  still may have further intersections
		ArrayList<String> activeZones = new ArrayList<String>();
		// all the zones tried for intersection
		ArrayList<String> triedZones = new ArrayList<String>();
		// the correct zones and areas
		HashMap<String, Area> currentZoneMap = new HashMap<String, Area>();
		// all tried maps
		HashMap<String, Area> zoneAreaMap = new HashMap<String, Area>(); 

		// create all existing intersections
		// then filter for those that dont exist except in other
		// zones - eg. the diagram "0 abc", first we create a b c ab ac abc
		// then remove all but abc by testing against contours not
		// in the intersection

		// start with the outside zone
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		for (ConcreteContour concreteContour : concreteContours) {
			// String abstractContour = concreteContour.getAbstractContour();

			Area contourArea = new Area(concreteContour.getArea());
			// currentZoneMap.put(abstractContour,contourArea);
			// activeZones.add(abstractContour);
			// zoneAreaMap.put(abstractContour, new Area(contourArea));

			Rectangle bounds = contourArea.getBounds();
			if (bounds.getX() < minX) {
				minX = bounds.getX();
			}
			if (bounds.getX() + bounds.getWidth() > maxX) {
				maxX = bounds.getX() + bounds.getWidth();
			}
			if (bounds.getY() < minY) {
				minY = bounds.getY();
			}
			if (bounds.getY() + bounds.getHeight() > maxY) {
				maxY = bounds.getY() + bounds.getHeight();
			}
		}
		if (minX > maxX) {
			minX = 0.0;
			maxX = 0.0;
			minY = 0.0;
			maxY = 0.0;
		}

		// use a rectangle that bounds all contours, to represent the outer zone
		int outerX1 = pjr.graph.Util.convertToInteger(minX) - 100;
		int outerX2 = pjr.graph.Util.convertToInteger(maxX) + 100;
		int outerY1 = pjr.graph.Util.convertToInteger(minY) - 100;
		int outerY2 = pjr.graph.Util.convertToInteger(maxY) + 100;
		Polygon outerPolygon = new Polygon();
		outerPolygon.addPoint(outerX1, outerY1);
		outerPolygon.addPoint(outerX2, outerY1);
		outerPolygon.addPoint(outerX2, outerY2);
		outerPolygon.addPoint(outerX1, outerY2);
		Area outerArea = new Area(outerPolygon);

		currentZoneMap.put("", outerArea);
		activeZones.add("");
		zoneAreaMap.put("", new Area(outerArea));

		// add the existing intersections
		while (activeZones.size() > 0) {
			while (activeZones.size() != 0) {

				String activeZone = activeZones.get(0);
				activeZones.remove(0);

				// test every contour intersection with a zone
				for (ConcreteContour concreteContour : concreteContours) {

					String abstractContour = concreteContour.getAbstractContour();
					if (activeZone.indexOf(abstractContour) != -1) {
						// dont need to consider a zone that already contains the contour
						continue;
					}
					String testZone = activeZone + abstractContour;
					testZone = AbstractDiagram.orderZone(testZone);

					if (triedZones.contains(testZone)) {
						// don't need to consider zones that have already been attempted
						continue;
					}
					triedZones.add(testZone);
					Area intersectArea = new Area(concreteContour.getArea());
					Area zoneArea = zoneAreaMap.get(activeZone);
					intersectArea.intersect(zoneArea);

					if (!intersectArea.isEmpty()) {
						currentZoneMap.put(testZone, intersectArea);
						activeZones.add(testZone);
						zoneAreaMap.put(testZone, intersectArea);
					}
				}
			}
		}

		// filter out the intersections that are completely contained
		// in the other contours
		HashMap<String, Area> retZoneMap = new HashMap<String, Area>();
		for (String z : currentZoneMap.keySet()) {

			Area zoneArea = new Area(zoneAreaMap.get(z));
			for (ConcreteContour concreteContour : concreteContours) {
				String abstractContour = concreteContour.getAbstractContour();
				if (z.indexOf(abstractContour) == -1) {
					Area otherContoursArea = concreteContour.getArea();
					zoneArea.subtract(otherContoursArea);
				}
			}

			if (!zoneArea.isEmpty()) {
				retZoneMap.put(z, zoneArea);
			}
		}

		return retZoneMap;
	}

	/**
	 * Deals with areas that have a nearly zero size section. This returns the
	 * area without that section. This repairs problems caused by
	 * Area.intersect.
	 */
	public static ArrayList<Polygon> polygonsFromArea(Area a) {
		if (!a.isPolygonal()) {
			// cant do anything if its not a polygon
			return null;
		}

		// create polygons, add them to the returned list if their area is large
		// enough
		ArrayList<Polygon> ret = new ArrayList<Polygon>();
		Polygon p = new Polygon();
		double[] coords = new double[6];
		PathIterator pi = a.getPathIterator(null);
		while (!pi.isDone()) {
			int coordType = pi.currentSegment(coords);
			if (coordType == PathIterator.SEG_CLOSE
					|| coordType == PathIterator.SEG_MOVETO) {
				if (coordType == PathIterator.SEG_CLOSE) {
					int x = pjr.graph.Util.convertToInteger(coords[0]);
					int y = pjr.graph.Util.convertToInteger(coords[1]);
					p.addPoint(x, y);
				}
				if (p.npoints > 2) { // no need to deal with empty polygons
					Rectangle2D boundingRectangle = p.getBounds2D();
					double boundingArea = boundingRectangle.getWidth()
							* boundingRectangle.getHeight();
					if (boundingArea >= 1.0) { // only add polygons of decent
												// size to returned area
						ret.add(p);
					}
				}
				p = new Polygon(); // start with the next polygon
				if (coordType == PathIterator.SEG_MOVETO) {
					int x = pjr.graph.Util.convertToInteger(coords[0]);
					int y = pjr.graph.Util.convertToInteger(coords[1]);
					p.addPoint(x, y);
				}
			}
			if (coordType == PathIterator.SEG_LINETO) {
				int x = pjr.graph.Util.convertToInteger(coords[0]);
				int y = pjr.graph.Util.convertToInteger(coords[1]);
				p.addPoint(x, y);
			}
			;
			if (coordType == PathIterator.SEG_CUBICTO) {
				System.out.println("Found a PathIterator.SEG_CUBICTO");
			}
			if (coordType == PathIterator.SEG_QUADTO) {
				System.out.println("Found a PathIterator.SEG_QUADTO");
			}

			pi.next();
		}
		return ret;
	}

	/**
	 * Find the duplicate zones by testing for connectivity of polygons that
	 * make up the zones, then checking for holes. This assumes no duplicate
	 * contours.
	 */
	public static ArrayList<String> findDuplicateZones(
			ArrayList<ConcreteContour> concreteContours) {
		ArrayList<String> retZones = new ArrayList<String>();

		if (concreteContours == null) {
			return retZones;
		}

		HashMap<String, Area> zoneAreaMap = generateZoneAreas(concreteContours);

		// find zones where multiple polygons that make up the zone
		for (String zone : zoneAreaMap.keySet()) {
			Area area = zoneAreaMap.get(zone);

			ArrayList<Polygon> polygons = polygonsFromArea(area);
			Polygon outerPolygon = null;
			if (zone.equals("")) {
				// outer zone is a special case where 3 nested polygons
				// can appear - the hole, the bounds outside hole and
				// the border, so remove the border for now.
				outerPolygon = findOuterPolygon(polygons);
				polygons.remove(outerPolygon);
			}

			// remove polygons that surround holes in the zone
			// we only want polygons where the fill is the zone
			// eg. diagram "0 a b ab" where a and b go through each other
			// has two polys filled with the zone for both a and b
			// the diagram "0 a b" drawn normally has three
			// polys for 0 (including border), only one of which
			// is filled with 0.
			//
			// What about holes in holes? Does this happen with
			// simple polygons? I don't think so.
			ArrayList<Polygon> polysCopy = new ArrayList<Polygon>(polygons);
			for (Polygon polygon : polysCopy) {
				Point2D insidePoint = findPointInsidePolygon(polygon);
				if (insidePoint != null && !area.contains(insidePoint)) {
					polygons.remove(polygon);
				}
			}

			if (zone.equals("")) {
				polygons.add(outerPolygon);
			}

			if (polygons.size() > 1) {
				retZones.add(zone);
			}
		}

		AbstractDiagram.sortZoneList(retZones);

		return retZones;
	}
	
	
	
	

	/**
	 * Find any point inside the polygon. For a simple polygon there must be a
	 * triple of consecutive points that have their triangle centre in the
	 * polygon, so return that centre. Return null if no point can be found (in
	 * case of empty polygon or some non-simple polygons).
	 */
	public static Point2D findPointInsidePolygon(Polygon p) {

		double delta = 0.1;
		// keep reducing delta further until an internal point is found
		for (int reduceLoop = 1; reduceLoop < 200; reduceLoop++) { 
			for (int i1 = 0; i1 <= p.npoints - 3; i1++) {
				double x1 = p.xpoints[i1];
				double y1 = p.ypoints[i1];

				int i2 = i1 + 1;
				double x2 = p.xpoints[i2];
				double y2 = p.ypoints[i2];
				while (x2 == x1 && y2 == y1) {
					// duplicate points sometimes seen in polygons, so skip them
					i2++;
					if (i2 >= p.npoints) {
						break;
					}
					x2 = p.xpoints[i2];
					y2 = p.ypoints[i2];
				}
				if (i2 >= p.npoints) {
					break;
				}

				int i3 = i2 + 1;
				double x3 = p.xpoints[i3];
				double y3 = p.ypoints[i3];
				while (x3 == x2 && y3 == y2) {
					// duplicate points sometimes seen in polygons, so skip them
					i3++;
					if (i3 >= p.npoints) {
						break;
					}
					x3 = p.xpoints[i3];
					y3 = p.ypoints[i3];
				}
				if (i3 >= p.npoints) {
					break;
				}

				double xMiddle12 = x1 + (x2 - x1) / 2;
				double xMiddle13 = x1 + (x3 - x1) / 2;
				double xMiddle23 = x2 + (x3 - x2) / 2;

				double yMiddle12 = y1 + (y2 - y1) / 2;
				double yMiddle13 = y1 + (y3 - y1) / 2;
				double yMiddle23 = y2 + (y3 - y2) / 2;

				double xTriangleMiddle = (xMiddle12 + xMiddle13 + xMiddle23) / 3;
				double yTriangleMiddle = (yMiddle12 + yMiddle13 + yMiddle23) / 3;
				if (p.contains(xTriangleMiddle, yTriangleMiddle)) {
					// dont pick a point too close to the edge of the poly, as
					// this may not
					// be in the area, due to rounding
					if (!p.contains(xTriangleMiddle, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle, yTriangleMiddle - delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle + delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle + delta, yTriangleMiddle - delta)) {
						continue;
					}
					if (!p.contains(xTriangleMiddle - delta, yTriangleMiddle - delta)) {
						continue;
					}

					return new Point2D.Double(xTriangleMiddle, yTriangleMiddle);
				}
			}
			delta = delta / 2;
		}

		System.out.println("Can't find internal point for polygon");

		return null; // should never get here for simple polygons
	}

	/**
	 * Find out if p2 is wholly inside p1. Equality implies containment (ie
	 * called with the same polygon in both arguments, this will return true).
	 * Returns true for empty p2.
	 */
	public static boolean polygonContainment(Polygon p1, Polygon p2) {

		Area a1 = new Area(p1);
		Area a2 = new Area(p2);
		a2.subtract(a1);
		if (a2.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * If one polygon surrounds all the rest, this returns it, otherwise null.
	 * If two polygons on top of each other are outside, null is returned.
	 * Touching does not count as containment.
	 */
	public static Polygon findOuterPolygon(ArrayList<Polygon> polygons) {

		if (polygons.size() == 1) {
			return polygons.get(0);
		}

		for (Polygon p1 : polygons) {
			boolean contains = true;
			for (Polygon p2 : polygons) {
				if (p1 == p2) {
					continue;
				}
				if (polygonContainment(p1, p2) && polygonContainment(p2, p1)) {
					contains = false;
					break;
				}
				if (!polygonContainment(p1, p2)) {
					contains = false;
					break;
				}
			}
			if (contains) {
				return p1;
			}
		}

		return null;
	}

	/**
	 * Generate an sorted list of zones from the interlinking polygons.
	 * Duplicate zones are not returned.
	 */
	public static String generateAbstractDiagramFromList(
			ArrayList<ConcreteContour> concreteContours) {

		if (concreteContours == null) {
			return "0";
		}

		HashMap<String, Area> zoneMap = generateZoneAreas(concreteContours);

		ArrayList<String> zones = new ArrayList<String>(zoneMap.keySet());

		AbstractDiagram.sortZoneList(zones);

		StringBuffer zoneSB = new StringBuffer();
		Iterator<String> it = zones.iterator();
		while (it.hasNext()) {
			String z = it.next();
			if (z.equals("")) {
				z = "0";
			}
			zoneSB.append(z);
			if (it.hasNext()) {
				zoneSB.append(" ");
			}
		}

		return zoneSB.toString();
	}

	/**
	 * Find the minimum bounding range for the contour. Assumes the TEs in the
	 * dualgraph have had their range assigned.
	 */
	public Polygon findMinPolygon(DualGraph dg) {
		TriangulationEdge startTE = dg
				.firstTriangulationEdgeWithContour(getAbstractContour());
		ContourLink startCL = startTE.contourLinksWithContour(
				getAbstractContour()).get(0);

		Polygon minP = new Polygon();

		ContourLink currentCL = null;
		while (startCL != currentCL) {
			if (currentCL == null) {
				currentCL = startCL;
			}
			currentCL = currentCL.getNext();

			// some ranges are not set, so default to the point
			int minLimitX = currentCL.getCutPoint().getCoordinate().x;
			int minLimitY = currentCL.getCutPoint().getCoordinate().y;
			if(currentCL.getCutPoint().getMinLimit() != null) {
				minLimitX = currentCL.getCutPoint().getMinLimit().x;
				minLimitY = currentCL.getCutPoint().getMinLimit().y;
			}

			minP.addPoint(minLimitX, minLimitY);
		}
		return minP;

	}

	/**
	 * Find the maximum bounding range for the contour. Assumes the TEs in the
	 * dualgraph have had their range assigned.
	 */
	public Polygon findMaxPolygon(DualGraph dg) {
		TriangulationEdge startTE = dg
				.firstTriangulationEdgeWithContour(getAbstractContour());
		ContourLink startCL = startTE.contourLinksWithContour(
				getAbstractContour()).get(0);

		// find the allowed range for the contour

		Polygon maxP = new Polygon();

		ContourLink currentCL = null;
		while (startCL != currentCL) {
			if (currentCL == null) {
				currentCL = startCL;
			}
			currentCL = currentCL.getNext();

			// some ranges are not set, so default to the point
			int maxLimitX = currentCL.getCutPoint().getCoordinate().x;
			int maxLimitY = currentCL.getCutPoint().getCoordinate().y;
			if(currentCL.getCutPoint().getMaxLimit() != null) {
				maxLimitX = currentCL.getCutPoint().getMaxLimit().x;
				maxLimitY = currentCL.getCutPoint().getMaxLimit().y;
			}

			maxP.addPoint(maxLimitX, maxLimitY);
		}
		return maxP;
	}
	public void scale(double xScale, double yScale){
		for(int i = 0 ; i < polygon.npoints; i++){
			polygon.xpoints[i] *= xScale;
			polygon.ypoints[i] *= yScale;
		}
	}
	

}
