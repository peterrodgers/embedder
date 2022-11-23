package euler.comparators;

import java.awt.*;
import java.util.*;

import euler.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public class ContourLinkComparator implements Comparator<ContourLink> {

	Point edgeStart = null;
	
	
	public ContourLinkComparator() {
		super();
	}


	public int compare(ContourLink cl1, ContourLink cl2) {
		String s1 = cl1.getContour();
		String s2 = cl2.getContour();
		return s1.compareTo(s2);
	}
}
