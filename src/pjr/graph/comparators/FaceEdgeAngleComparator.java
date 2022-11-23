package pjr.graph.comparators;

import java.util.*;

import pjr.graph.FaceEdge;


/**
 * Orders edges by their slope value
 */
public class FaceEdgeAngleComparator implements Comparator<FaceEdge> {

	public int compare(FaceEdge e1, FaceEdge e2) {

		Double s1 = new Double(e1.getAngle());

		int ret = s1.compareTo(new Double(e2.getAngle()));

		return(ret);
	}
	


	
}


