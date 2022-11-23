package euler.inductive.comparators;

import java.util.*;

import euler.*;
import euler.inductive.*;

import pjr.graph.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public abstract class PathComparator implements Comparator<ArrayList<Edge>> {
	
	DualGraph dg;

	public PathComparator(DualGraph dg) {
		super();
		this.dg = dg;
	}
	
	public DualGraph getDualGraph() {return dg;}

	abstract public int compare(ArrayList<Edge> path1,ArrayList<Edge> path2);
		
}
