package euler.comparators;

import java.util.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public class ZoneStringComparator implements Comparator<String> {

	public int compare(String s1,String s2) {
		if(s1.length() > s2.length()) {
			return 1;
		}
		if(s1.length() < s2.length()) {
			return -1;
		}
		return(s1.compareTo(s2));
	}
}
