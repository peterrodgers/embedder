package euler.comparators;

import java.util.*;

/**
 * Orders zone strings by their length (longer is greater) then lexographically.
 */
public class ContourValueMapComparator implements Comparator<String> {
	
	HashMap<String,Integer> comparatorValueMap;

	
	public ContourValueMapComparator(HashMap<String,Integer> comparatorValueMap) {
		super();
		this.comparatorValueMap = comparatorValueMap;
	}


	public int compare(String contour1, String contour2) {
		Integer value1 = comparatorValueMap.get(contour1);
		Integer value2 = comparatorValueMap.get(contour2);
		
		return(value1.compareTo(value2));
	}
}
