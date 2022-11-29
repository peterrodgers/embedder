package euler.simplify;

import java.awt.event.KeyEvent;
import java.util.*;

import euler.AbstractDiagram;
import euler.DualGraph;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;


/**
 * 
 * Code based on the simplification ideas from Dagstuhl Seminar 22462.
 * 
 * @author Peter Rodgers
 */

public class Simplify {
	


	AbstractDiagram abstractDiagram = null;
	DualGraph dualGraph = null;

	/** Earliest first. Merged pairs, first of the pair is the merged label */
	ArrayList<String[]> setMergeHistory = new ArrayList<>();
	/** Earliest first */
	ArrayList<AbstractDiagram> abstractDiagramMergeHistory = new ArrayList<>();
	
	public static void main(String[] args) {
		
		AbstractDiagram ad = new AbstractDiagram("0 a b abe cde ae be ce adbe ade e abc abce ab");
		
		Simplify simplify = new Simplify(ad);
		
		simplify.simplifyUntilPlanarGreedy();

		
		//AbstractDiagram ad = new AbstractDiagram("0 a b abq cdq aq");
		//AbstractDiagram ad = new AbstractDiagram("0 a b bc ac ad bd");
		//AbstractDiagram ad = AbstractDiagram.randomDiagramFactory(4);
//System.out.println(ad);
		
//		AbstractDiagram adSimplified = mergeSets(ad,"a","b");

		


		DualGraphWindow dw = new DualGraphWindow(simplify.getDualGraph());
		dw.getDiagramPanel().setShowGraph(true);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(false);
		dw.getDiagramPanel().setShowContourLabel(true);
		dw.getDiagramPanel().setShowTriangulation(false);
		dw.getDiagramPanel().setJiggleLabels(false);
		dw.getDiagramPanel().setForceNoRedraw(true);
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
	 	ddp.layout();
		dw.getDiagramPanel().fitGraphInPanel();
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());

	}

	
	public AbstractDiagram getAbstractDiagram() {return abstractDiagram;}
	public DualGraph getDualGraph() {return dualGraph;}
	public ArrayList<String[]> getSetMergeHistory() {return setMergeHistory;}
	public ArrayList<AbstractDiagram> getAbstractDiagramMergeHistory() {return abstractDiagramMergeHistory;}


	public Simplify(AbstractDiagram ad) {
		super();
		this.abstractDiagram = ad;
		formDualGraph();
	}

	
	/**
	 * Assumes the abstractDiagram exists, then form the dualGrpah with connected contours.
	 */
	private void formDualGraph() {
		dualGraph = new DualGraph(abstractDiagram);
		dualGraph.connectDiscconnectedContours();
	
	}
	
	
	/**
	 * merge sets until the dual Graph is planar.
	 */
	private void simplifyUntilPlanarGreedy() {
		// TODO Auto-generated method stub
		abstractDiagram = mergeSets("a","b");
		formDualGraph();
System.out.println(setMergeHistory.get(0)[0]+" "+setMergeHistory.get(0)[1]);		
System.out.println(setMergeHistory.get(0)[0]);		
System.out.println(abstractDiagramMergeHistory);
System.out.println(abstractDiagram);
/*		abstractDiagram = mergeSets("a","c");
		formDualGraph();
System.out.println(setMergeHistory.get(0)[0]+" "+setMergeHistory.get(0)[1]);		
System.out.println(setMergeHistory.get(0)[0]);		
System.out.println(abstractDiagramMergeHistory);
System.out.println(abstractDiagram);
*/	}



	/**
	 * 
	 * Merge two sets, with the merged set taking the label of the first set.
	 * 
	 * Merge sets, 
	 * @param ad the abstract diagram to change
	 * @param set1 the first set to merge
	 * @param set2 the second set to merge
	 * @return an abstract diagram with set1 and set2 merged, with the merged set taking set1 label.
	 */
	public AbstractDiagram mergeSets(String set1, String set2) {
		
		ArrayList<String> retZones = new ArrayList<>();
		
		for(String z : abstractDiagram.getZoneList()) {
			String zNew = z;
			if(z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,"");
			}
			if(!z.contains(set1) && z.contains(set2)) {
				zNew = z.replace(set2,set1);
				zNew = AbstractDiagram.orderZone(zNew); 
			}
			if(!retZones.contains(zNew)) {
				retZones.add(zNew);
			}
		}

		String [] setPair = new String[2];
		setPair[0] = set1;
		setPair[1] = set2;
		setMergeHistory.add(setPair);
		abstractDiagramMergeHistory.add(abstractDiagram);
		
		AbstractDiagram ret = new AbstractDiagram(retZones);
		return ret;
	
	}

}
