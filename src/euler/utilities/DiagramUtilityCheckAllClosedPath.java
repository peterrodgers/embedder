package euler.utilities;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import pjr.graph.FaceEdge;
import euler.DualGraph;

/**
 *
 * @author Leishi Zhang
 */	
	
public class DiagramUtilityCheckAllClosedPath extends DiagramUtility{
		/** Trivial constructor. */
		public  DiagramUtilityCheckAllClosedPath() {
			super(KeyEvent.VK_B," Check All Closed Path",KeyEvent.VK_A);
		}

	/** Trivial constructor. */
		public  DiagramUtilityCheckAllClosedPath(int inAccelerator, String inMenuText, int inMnemonic) {
			super(inAccelerator, inMenuText, inMnemonic);
		}

		public void apply() {
			
			DualGraph dg = getDualGraph();
	/*		System.out.println("---------------------");
			ArrayList<ArrayList<FaceEdge>> circles = dg.allPossibleCycles();
			System.out.println("---------------------"+ circles.size());
			if(circles.size()!=0){
				for(ArrayList<FaceEdge> cycle: circles){
					System.out.println("cycle-----");
					for(FaceEdge fe: cycle){
						System.out.println(fe.toString());
					}
				}
			}*/
		}
		
	}
