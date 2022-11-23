package euler.library;

import java.util.ArrayList;

import euler.inductive.AddPathDialog;
import euler.inductive.HybridGraph;
import euler.inductive.InductiveWindow;
import euler.utilities.DiagramUtility;

public class DiagramUtilitySetAbstractDescription extends DiagramUtility {
	
	EulerDiagramWindow ew;
	
/** Trivial constructor. */
	public DiagramUtilitySetAbstractDescription(int inAccelerator, String inMenuText, int inMnemonic, EulerDiagramWindow ew) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.ew = ew;
	}
	

	public void apply() {
		

		
		// find the next unused contour label
/*		char c = 'a';
		ArrayList<String> contourList = hg.findContourList();
		while(contourList.contains(Character.toString(c))) {
			int i = (int)c;
			i++;
			c = (char)i;
		}

		new SetAbstractDescriptionDialog(HybridGraph.EMPTY_ZONE_LABEL+"1"+" "+HybridGraph.EMPTY_ZONE_LABEL+"1", Character.toString(c), ew, null);
		*/
	}

	
}