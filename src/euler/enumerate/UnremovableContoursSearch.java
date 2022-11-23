package euler.enumerate;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.utilities.*;

import pjr.graph.*;


public class UnremovableContoursSearch {

	
	public static void main(String[] args) {

		final int numberOfContours = 6;

		int count = 1;
		
		DualGraph dg = findWFDiagram(numberOfContours);
		
		while(!noRemovableContoursTest(dg)) {
System.out.println(numberOfContours+ " contours, FAILED on WF diagram "+count+" "+dg+"\n");
			dg = findWFDiagram(numberOfContours);
			count++;
		}
		
		System.out.println(dg);
		
		DualGraphWindow dgw = new DualGraphWindow(dg);
		DiagramPanel.areas = DualGraph.factoryCreatedAreas;

		
	}
	
	

	/**
	 * Test to see if every contour in the diagram cannot
	 * be removed without creating a duplicate zone.
	 */
	public static boolean noRemovableContoursTest(DualGraph dg) {
/*
//This just spots a single unremovable contour
ArrayList<String> contours = dg.findAbstractDiagram().getContours();
for(String contour : contours) {
	if(unremovableContourTest(dg, contour)) {
System.out.println("unremovable contour "+contour);
		return true;
	}
}
return false;
*/

		ArrayList<String> contours = dg.findAbstractDiagram().getContours();
		if(contours.size() == 0) {
			return false;
		}
		
		ArrayList<String> unremovableContours = new ArrayList<String>();
		
		boolean ret = true;
		
		for(String contour : contours) {
			if(!unremovableContourTest(dg, contour)) {
				// return false;
				ret = false;
			} else {
				unremovableContours.add(contour);
			}
		}
if(unremovableContours.size() != 0) {
	System.out.println("unremovable contours "+unremovableContours);
}

		// return true;
		return ret;

	}
	
	
	/*
	 * If there are two node labels with the contour
	 * label as their difference and the nodes are not adjacent
	 * then the contour is not removable without creating a duplicate zone.
	 */
	public static boolean unremovableContourTest(DualGraph dg, String contour) {
		
		ArrayList<Node> nodes = dg.getNodes();
		
		ArrayList<Node> contourNodes = new ArrayList<Node>();
		for(Node n : nodes) {
			if(n.getLabel().contains(contour)) {
				contourNodes.add(n);
			}
		}
		
		for(Node n: contourNodes) {
			ArrayList<String> labelList = AbstractDiagram.findContourList(n.getLabel());
			labelList.remove(contour);
			
			StringBuffer diffBuffer = new StringBuffer();
			for(String s: labelList) {
				diffBuffer.append(s);
			}
			String diffLabel = diffBuffer.toString();
			
			Node diffNode = dg.firstNodeWithLabel(diffLabel);
			
			if(diffNode == null) {
				continue;
			}
//if(n.getLabel().contains("c")) {System.out.println("c: "+n+" "+diffNode);}
			
			if(!n.connectingNodes().contains(diffNode)) {
				return true;
			}
		}

		return false;
	}



public static int totalDiagramsCount = 0;

	public static DualGraph findWFDiagram(int numberOfContours) {
		DualGraph dg = null;
		while(dg == null || !DiagramUtilityRandomWellformedDiagram.isWellformed(dg)) {
			dg = DualGraph.randomDualGraphFactoryByTriangles(numberOfContours, 50,600, true);
//			dg = DualGraph.randomDualGraphFactoryByRectangles(numberOfContours, 50,600, true);
totalDiagramsCount++;
if(totalDiagramsCount%1000 == 0){
	System.out.println(totalDiagramsCount+" total diagrams tested");
}

		}
		
		return dg;
	}

}
