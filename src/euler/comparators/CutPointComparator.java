package euler.comparators;

import java.awt.*;
import java.util.*;

import euler.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public class CutPointComparator implements Comparator<CutPoint> {

	Point edgeStart = null;
	
	
	public CutPointComparator(Point edgeStart) {
		super();
		this.edgeStart = edgeStart;
	}


	public int compare(CutPoint cp1,CutPoint cp2) {
		Point p1 = cp1.getCoordinate();
		Point p2 = cp2.getCoordinate();
		if(pjr.graph.Util.distance(p1, edgeStart) > pjr.graph.Util.distance(p2, edgeStart)) {
			return 1;
		}
		if(pjr.graph.Util.distance(p1, edgeStart) < pjr.graph.Util.distance(p2, edgeStart)) {
			return -1;
		}
		return(0);
	}
}
