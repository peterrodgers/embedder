package euler.inductive;

import java.util.*;


import euler.DualGraph;
import euler.utilities.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityFindPath extends DiagramUtility {
	
	InductiveWindow iw;
	
/** Trivial constructor. */
	public DiagramUtilityFindPath(int inAccelerator, String inMenuText, int inMnemonic, InductiveWindow iw) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.iw = iw;
	}
	

	public void apply() {
		
		HybridGraph hg = iw.getHybridGraph();
		
		// find the next unused contour label
		char c = 'a';
		ArrayList<String> contourList = hg.findContourList();
		while(contourList.contains(Character.toString(c))) {
			int i = (int)c;
			i++;
			c = (char)i;
		}

		new FindPathDialog(hg, Character.toString(c), iw, null);
		
		
	}

	
}
