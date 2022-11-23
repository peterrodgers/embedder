package euler;

import java.util.*;

public class ContourZoneOccurrence implements Comparable<ContourZoneOccurrence> {
	
	protected String contour = "";
	protected ArrayList<Integer> occurrences = new ArrayList<Integer>();


	public ContourZoneOccurrence(String contour, ArrayList<Integer> occurences) {
		super();
		this.contour = contour;
		this.occurrences = occurences;
	}
	

	public String getContour() {return contour;}
	public ArrayList<Integer> getOccurrences() {return occurrences;}



	public int compareTo(ContourZoneOccurrence czo) {
		if(getContour().compareTo(czo.getContour()) != 0) {
			return getContour().compareTo(czo.getContour());
		}
		return compareLists(czo);
	}
	
	
	public boolean equals(ContourZoneOccurrence czo) {
		if(compareTo(czo) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean equals(Object o) {
		ContourZoneOccurrence czo = (ContourZoneOccurrence)o;
		if(compareTo(czo) == 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * @return 0 if the lists are the same, -1 or 1 otherwise.
	 */
	public int compareLists(ContourZoneOccurrence czo) {
		
		ArrayList<Integer> list1 = getOccurrences();
		ArrayList<Integer> list2 = czo.getOccurrences();
		
		if(list1 == null && list2 == null) {return 0;}
		if(list1 == null) {return -1;}
		if(list2 == null) {return 1;}
		
		if(list1.size() > list2.size()) {return 1;}
		if(list1.size() < list2.size()) {return -1;}
		
		Iterator<Integer> it1 = list1.iterator();
		Iterator<Integer> it2 = list2.iterator();
		while(it1.hasNext()) {
			Integer i1 = it1.next();
			Integer i2 = it2.next();
			if(i1.compareTo(i2) != 0) {
				return i1.compareTo(i2);
			}
		}
		
		return 0;
	}
	
	
	public String toString() {
		return "("+contour+":"+occurrences+")";
	}

}
