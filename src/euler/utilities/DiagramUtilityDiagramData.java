package euler.utilities;


import java.awt.event.*;
import java.util.*;
import euler.*;

/**
 * Output useful diagram information.
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityDiagramData extends DiagramUtility {

/** Trivial constructor. */
	public DiagramUtilityDiagramData() {
		super(KeyEvent.VK_D,"Diagram Information",KeyEvent.VK_D);
	}

/** Trivial constructor. */
	public DiagramUtilityDiagramData(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}


	public void apply() {

		DualGraph dg = getDualGraph();
		
		boolean passFaceCondition;
		boolean passConnectivityConditions;
		boolean connected;
		boolean correct;
		int numberOfSubdiagrams;
		int crossings;
		ArrayList<TriangulationFace> wrongCrossings = null;
		ArrayList<String> duplicateZones = null;
		
		connected = dg.connected();
		passConnectivityConditions = dg.checkConnectivity();
		numberOfSubdiagrams = dg.findNestedSubdiagrams(false).size();
			
		dg.formFaces();
		passFaceCondition = dg.passFaceConditions();

		crossings = dg.findEdgeCrossings().size();

		correct = false;
		try {
			GeneralConcreteDiagram cd = new GeneralConcreteDiagram(dg);
			cd.setConcurrentOffset(0);
			cd.setOptimizeContourAngles(true);
			cd.setFitCircles(true);
			cd.generateContours();
			correct = cd.correctConcreteDiagram();
			duplicateZones = cd.findDuplicateZones();
			wrongCrossings = cd.findIncorrectTriangulationCrossings();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		boolean wellformed = passFaceCondition && passConnectivityConditions && connected;
		
		System.out.println("DIAGRAM DATA");
		System.out.println("wellformed : "+wellformed);
// TODO FIX THE BUG WITH FACE CONDITIONS
		if(dg.firstNodeWithLabel("") != null) {
			System.out.println("  pass face conditions: "+passFaceCondition);
		} else {
			System.out.println("  face conditions not tested - no outside zone");
		}
		System.out.println("  pass connectivity: "+passConnectivityConditions);
		System.out.println("  connected: "+connected);
		System.out.println("dual edge crossings: "+crossings);
		System.out.println("wrong contour crossing in TFs: "+wrongCrossings+", count "+wrongCrossings.size());
		System.out.println("subdiagrams: "+numberOfSubdiagrams);
		System.out.println("duplicate zones: "+duplicateZones+", count "+duplicateZones.size());
		System.out.println("correct diagram: "+correct);
		System.out.println("abstract diagram: "+dg.findAbstractDiagram());
		
	}

	
}
