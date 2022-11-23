package pjr.graph.utilities;

public class Hamilton {
	
	/**
	 * 
	 * @param n - number of nodes of the graph
	 * @param m - number of edges of the graph
	 * @param directed - directed = true if the graph is directed, false otherwise 
	 * @param nodei 
	 * @param nodej - int[m+1]
	 * 				 if directed = false, then the graph is undirected, nodei[p] 
	 * 					nodej[p] are the end nodes of the p-th edge in the graph
	 * 				   if directed = true, then the graph is directed, the p-th 
	 * 					edge is directed from nodei[p] to nodej[p], for p = 1,2,...m
	 * @param cycle - int [n+1]
	 * 				cycle[0] = 1 if graph is non - Hamiltonian
	 * 				cycle[0] = 0 if the graph is Hamiltonian, and the node numbers 
	 * 				of the Hamilton cycle are given by cycle[1], cycle[2],..., cycle[n] 
	 */
	
	public static void HamiltonCycle(int n, int m, boolean directed,
			int nodei[], int nodej[], int cycle[]){
		
		int i,j,k,stacklen,lensol,stackindex,len,len1,len2,low,up;
		int firstedges[] = new int[n+2];
		int endnode[] = new int[m+m+1];
		int stack[] = new int[m+m+1];
		boolean connect[] = new boolean[n+1];
		boolean join,skip;
		// set up the forward star representation of the graph
		k = 0;
		
		for (i=1; i<=n; i++) {
			
			firstedges[i] = k + 1;
			for (j=1; j<=m; j++) {
				if (nodei[j] == i) {
					k++;
					endnode[k] = nodej[j];
				}
				if (!directed)
					if (nodej[j] == i) {
						k++;
						endnode[k] = nodei[j];
					}
				}
			}
			
		firstedges[n+1] = k + 1;
			
		// initialize
		lensol = 1;
		stacklen = 0;
		// find the next node
		while (true) {
			if (lensol == 1) {
				stack[1] = 1;
				stack[2] = 1;
				stacklen = 2;
			}
			else {
				len1 = lensol - 1;
				len2 = cycle[len1];
				for (i=1; i<=n; i++) {
					connect[i] = false;
					low = firstedges[len2];
					up = firstedges[len2 + 1];
					if (up > low) {
						up--;
						for (k=low; k<=up; k++)
							if (endnode[k] == i) {
								connect[i] = true;
								break;
							}
					}
			}
			for (i=1; i<=len1; i++) {
				len = cycle[i];
				connect[len] = false;
			}
			len = stacklen;
			skip = false;
			if (lensol != n) {
				for (i=1; i<=n; i++)
					if (connect[i]) {
						len++;
						stack[len] = i;
					}
					stack[len + 1] = len - stacklen;
					stacklen = len + 1;
			}
			else {
			for (i=1; i<=n; i++)
				if (connect[i]) {
					if (!directed) {
						if (i > cycle[2]) {
							stack[len + 1] = len - stacklen;
							stacklen = len + 1;
							skip = true;
							break;
						}
			
					}
			join = false;
			low = firstedges[i];
			up = firstedges[i + 1];
		
			if (up > low) {
				up--;
				for (k=low; k<=up; k++)
					if (endnode[k] == 1) {
						join = true;
						break;
					}
			}
			if (join) {
				stacklen += 2;
				stack[stacklen - 1] = i;
				stack[stacklen] = 1;
			}
			else {
				stack[len + 1] = len - stacklen;
				stacklen = len + 1;
			}
			skip = true;
			break;
				}
			if (!skip) {
				stack[len + 1] = len - stacklen;
				stacklen = len + 1;
			}
			}
		}
			// search further
			while (true) {
			stackindex = stack[stacklen];
			stacklen--;
			if (stackindex == 0) {
			lensol--;
			if (lensol == 0) {
			cycle[0] = 1;
			return;
			}
			continue;
			}
			else {
				cycle[lensol] = stack[stacklen];
				stack[stacklen] = stackindex - 1;
				if (lensol == n) {
					cycle[0] = 0;
					return;
				}
				lensol++;break;
				}
			}
		}			
	}
	
	public static void main(String args[]) {
		int n=20, m=30;
		int cycle[] = new int[n+1];
		int nodei[] = {0,5,2,4,1,3,2,4,1,3, 5,11, 9, 7,10,13,12,
		10,7,15,14,19,17,13,16,20,17,18,15,18,16};
		int nodej[] = {0,1,3,5,2,4,7,9,6,8,10, 6,14,12,15, 8, 6,
		11,13, 9, 8,14,12,18,11,19,16,19,20,17,20};
		boolean directed = false;
		HamiltonCycle(n,m,directed,nodei,nodej,cycle);
		if (cycle[0] != 0)
		System.out.println("No Hamilton cycle is found.");
		else {
		System.out.println("A Hamilton cycle is found:");
		for (int i=1; i<=n; i++)
		System.out.print(" " + cycle[i]);
		System.out.println();
		}
		}

}
