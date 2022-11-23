package euler.inductive;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import euler.*;
import euler.display.*;
import euler.utilities.*;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityAddPath extends DiagramUtility {
	
	InductiveWindow iw;
	
/** Trivial constructor. */
	public DiagramUtilityAddPath(int inAccelerator, String inMenuText, int inMnemonic, InductiveWindow iw) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.iw = iw;
	}
	

	public void apply() {
		
		HybridGraph hg= iw.getHybridGraph();
		
		// find the next unused contour label
		char c = 'a';
		ArrayList<String> contourList = hg.findContourList();
		while(contourList.contains(Character.toString(c))) {
			int i = (int)c;
			i++;
			c = (char)i;
		}

		new AddPathDialog(hg, HybridGraph.EMPTY_ZONE_LABEL+"1"+" "+HybridGraph.EMPTY_ZONE_LABEL+"1", Character.toString(c), iw, null);

		
	}

	
}
