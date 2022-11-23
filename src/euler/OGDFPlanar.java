package euler;

public class OGDFPlanar {
	
	public static void main(String[] args){

		double nodes[] = new double[8];
		nodes[0]=20;
		nodes[1]=40;
		nodes[2]=10;
		nodes[3]=0;
		nodes[4]=50;
		nodes[5]=30;
		nodes[6]=100;
		nodes[7]=80;
		//{20,40,10,0,50,30,100,80};
		int edges[] = new int[12];
		edges[0]=0;
		edges[1]=1;
		edges[2]=0;
		edges[3]=2;
		edges[4]=0;
		edges[5]=3;
		edges[6]=1;
		edges[7]=2;
		edges[8]=1;
		edges[9]=3;
		edges[10]=2;
		edges[11]=3;
		double [] newCoor = planarEmbedding(nodes,edges,4,6,0);
		for(int i = 0 ; i < 8; i++){
			System.out.println(newCoor[i]);
		}
	}
	 native static double[] planarEmbedding(double[] nodes, int[] edges, int numberOfNodes, int numberOfEdges, int emptyNodeIndex);
	 native  static int get();
	 static {
		 System.loadLibrary("euler_OGDFPlanar");
	 }



}
