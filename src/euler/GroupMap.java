package euler;

import java.util.*;


public class GroupMap {
	
	int groupCount;
	int mapping[];
	
	public int getGroupCount() {return groupCount;}
	public int[] getMapping() {return mapping;}
	
	public static void main(String args[]) {
		 
		
		GroupMap gm = new GroupMap(4);

		boolean loop = true;
		while(loop) {
			System.out.println(gm);
			loop = gm.nextMapping();
		}
		System.out.println(gm);
	}

	public GroupMap(int groupCount) {
		super();
		this.groupCount = groupCount;
		firstMapping();
	}
	
	
	public void firstMapping() {
		this.mapping = new int[groupCount];
		for(int i = 0; i < groupCount; i++) {
			mapping[i] = i;
		}
	}

	
	/**
	 * Find the next permutation.
	 * @return if the mapping is at the last permutation, if so set to the first mapping.
	 */
	public boolean nextMapping() {

		boolean notAtEnd = nextPerm(mapping);
		if(!notAtEnd) {
			firstMapping();
		}
		return !notAtEnd;

	}

	
	/**
	 * nextPerm code from
	 * http://forum.java.sun.com/thread.jspa?threadID=389012&messageID=1677793
	 */
	public static boolean nextPerm(int[] p) {
		 
		int i;
		for (i= p.length-1; i-- > 0 && p[i] > p[i+1];)
		;
 
		if (i < 0) {
			return false;
		}
 
		int j;
 
		for (j= p.length; --j > i && p[j] < p[i];)
			;
 
		swap(p, i, j);
 
		for (j= p.length; --j > ++i; swap(p, i, j))
			;
 
		return true;
	}
	
	private static void swap(int[] p, int i, int j) {
		 
		int t= p[i];
		p[i]= p[j];
		p[j]= t;
	}
 
	public String toString () {
		StringBuffer ret = new StringBuffer(mapping.length*2+1);
		for (int i= 0; i < mapping.length; ++i) {
			ret.append(mapping[i]+" ");
		}
		return ret.toString().trim();
	}
 
}

