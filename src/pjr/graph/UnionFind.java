package pjr.graph;

import java.util.*;
import java.io.*;

/**
 * Implementation of union-find with path compression.
 */

public class UnionFind implements Serializable {

	protected int[] parent;
	public final int ROOT = -1;

	public UnionFind(int nodeNumber) {
		parent = new int[nodeNumber+1];
		Arrays.fill(parent,ROOT);
	}


/** Trivial accessor */
	public int[] getParent() {return parent;}


/** Join up the two nodes */
	public void union(int n1,int n2) {

		int root1 = root(n1);
		int root2 = root(n2);

		if(n1 != n2 && root1 != root2) {
			if(parent[n1] ==ROOT) {
				parent[n1] = root2;
			} else {
 				parent[root2] = root1;
			}
		}
	}


/** Returns true if the two nodes are connected, false otherwise */ 
	public boolean find(int n1,int n2) {

		if(n1==n2) {
			return(true);
		}

		if (parent[n1] == ROOT && parent[n2] == ROOT) {
			return(false);
		}
		if(root(n1) != root(n2)) {
			return(false);
		}
		return(true);
	}



/**	Returns the root of the node in the parent array. Implements a sort
 * of path compression. */
	public int root(int n) {
		int current = n;

		if(current == ROOT) {
			return(current);
		}

		while(parent[current] != ROOT) {
			int next = parent[current];
// path compression
			if(parent[next] != ROOT) {
				parent[current] = parent[next];
			}
			current = next;
		}


		return(current);
	}

/** Outputs the parent array as a list of integers */
	public String toString() {
		StringBuffer out = new StringBuffer("[");
		for (int i = 0; i < parent.length; i++) {
			out.append(parent[i]);
			if(i+1 < parent.length) {
				out.append(",");
			}
		}
		out.append("]");
		return(out.toString());
	}

}


