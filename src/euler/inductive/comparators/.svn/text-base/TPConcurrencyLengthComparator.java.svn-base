package euler.inductive.comparators;

import java.util.*;

import euler.DualGraph;
import euler.inductive.*;

import pjr.graph.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public class TPConcurrencyLengthComparator extends PathComparator {
	
	public TPConcurrencyLengthComparator(DualGraph dg) {
		super(dg);
	}

	public int compare(ArrayList<Edge> path1,ArrayList<Edge> path2) {
		
		
		int triplePoints = HybridGraph.compareTriplePoints(path1,path2,dg);
		if(triplePoints != 0) {
			return triplePoints;
		}
		int concurrency = HybridGraph.compareConcurrency(path1,path2);
		if(concurrency != 0) {
			return concurrency;
		}
		int length = HybridGraph.compareLength(path1,path2);
		if(length != 0) {
			return length;
		}
		
		return 0;
		
	}
}
