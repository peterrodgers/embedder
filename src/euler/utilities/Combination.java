package euler.utilities;

public class Combination {
	  int n, m;

	  int[] pre;//previous combination.

	  public Combination(int n, int m) {
	   this.n = n;
	   this.m = m;
	  }
	  public int[] next() {
		   if (pre == null) {
		    pre = new int[n];
		    for (int i = 0; i < pre.length; i++) {
		     pre[i] = i;
		    }
	
		    int[] ret = new int[n];
		    System.arraycopy(pre, 0, ret, 0, n);
		    return ret;
		   }
		   int ni = n - 1, maxNi = m - 1;
		   while (pre[ni] + 1 > maxNi) {
		    ni--;
		    maxNi--;
		    if (ni < 0)
		     return null;
		   }
		   pre[ni]++;
		   while (++ni < n) {
		    pre[ni] = pre[ni - 1] + 1;
		   }
		   int[] ret = new int[n];
		   System.arraycopy(pre, 0, ret, 0, n);
		   return ret;
	  }
	  public static void main(String[] args) {
		  Combination com = new Combination(3,10);
		  int[] next = com.next();
		  while(next!=null){
			  for(int i = 0 ; i < next.length; i++){
			  System.out.print(next[i] + " ");
			  }
			  System.out.println();
			  next = com.next();
		  }
		  
	  }
	  
	 }


