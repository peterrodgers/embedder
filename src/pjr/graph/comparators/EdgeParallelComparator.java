package pjr.graph.comparators;

import java.util.*;


import pjr.graph.Edge;
import pjr.graph.EdgeType;

/**
 * Orders edges by their types, then by their labels.
 */
public class EdgeParallelComparator implements Comparator<Edge> {

	public int compare(Edge e1, Edge e2) {

		EdgeType et1 = e1.getType();
		EdgeType et2 = e2.getType();
// first compare the type priorities
		int ret = et1.getPriority() - et2.getPriority();
// then compare the type labels
		if(ret == 0) {
			ret = et1.getLabel().compareTo(et2.getLabel());
		}
// then compare the edge labels
		if(ret == 0) {
			ret = e1.getLabel().compareTo(e2.getLabel());
		}

		return(ret);
	}
}


