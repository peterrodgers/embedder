package euler.comparators;

import java.util.*;

import pjr.graph.*;

/**
 * Orders nodes that are labelled with zones by their label length (longer is greater) then lexographically.
 */
public class ZoneNodeComparator implements Comparator<Node> {

	public int compare(Node n1,Node n2) {
		String s1 = n1.getLabel();
		String s2 = n2.getLabel();
		if(s1.length() > s2.length()) {
			return 1;
		}
		if(s1.length() < s2.length()) {
			return -1;
		}
		return(s1.compareTo(s2));
	}
}
