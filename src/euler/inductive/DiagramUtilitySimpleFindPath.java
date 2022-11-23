package euler.inductive;

import java.util.*;

import javax.swing.JOptionPane;


import euler.*;
import euler.utilities.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilitySimpleFindPath extends DiagramUtility {
	
	InductiveWindow iw;
	
/** Trivial constructor. */
	public DiagramUtilitySimpleFindPath(int inAccelerator, String inMenuText, int inMnemonic, InductiveWindow iw) {
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
		
		ArrayList<String> zones = HybridGraph.findZoneList(hg.getLargeGraph());

		SimpleFindPathDialog sfpd = new SimpleFindPathDialog(hg, Character.toString(c), iw, zones, null);

		if(sfpd.getSuccess()) {
			JOptionPane.showMessageDialog(iw, "Time take to find path: "+sfpd.getPathTime()/1000.0,"Success",JOptionPane.PLAIN_MESSAGE);
			iw.dispose();
		}
		
	}

	
}
