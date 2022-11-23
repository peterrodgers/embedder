package euler.inductive;

import java.util.*;

import javax.swing.JOptionPane;


import euler.*;
import euler.utilities.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityGenerateHybridGraph extends DiagramUtility {
	
	EulerGraphWindow egw;
	
/** Trivial constructor. */
	public DiagramUtilityGenerateHybridGraph(int inAccelerator, String inMenuText, int inMnemonic, EulerGraphWindow egw) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.egw = egw;
	}
	

	public void apply() {
		
		DualGraph eg = egw.getDualGraph();
	
		HybridGraph hg = new HybridGraph(eg,egw.getDiagramPanel());
		ArrayList<String> zoneList = HybridGraph.findZoneList(hg.getLargeGraph());
		AbstractDiagram.sortZoneList(zoneList);
		String zones = "";
		for(String z : zoneList) {
			zones += z+" ";
		}
		zones.trim();
		
		if(!hg.getObjectCreatedSuccessfully()) {
			if(!HybridGraph.errorOutput) {
				JOptionPane.showMessageDialog(egw,"Failed to create hybrid graph. Try manually smoothing the diagram or laying the graph out with the spring embedder - key 's'.","Error",JOptionPane.PLAIN_MESSAGE);
			}
		} else {
			new InductiveWindow("Hybrid Graph for: "+zones,hg,true);
			egw.dispose();
		}
		
	}

	
}
