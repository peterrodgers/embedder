package pjr.graph.comparators;

import java.util.*;

import pjr.graph.*;

/**
 * Orders edge types by their priority.
 */
public class EdgeTypePriorityComparator implements Comparator<EdgeType> {

	public int compare(EdgeType et1, EdgeType et2) {
		int ret = et1.getPriority() - et2.getPriority();
		if(ret == 0) {
			ret = et1.getLabel().compareTo(et2.getLabel());
		}
		return(ret);
	}
}


