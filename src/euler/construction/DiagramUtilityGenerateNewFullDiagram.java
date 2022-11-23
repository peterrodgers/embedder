package euler.construction;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.utilities.*;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityGenerateNewFullDiagram extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityGenerateNewFullDiagram() {
		super(KeyEvent.VK_G,"Generate a new full diagram",KeyEvent.VK_G);
	}
/** Trivial constructor. */
	public DiagramUtilityGenerateNewFullDiagram(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	

	public void apply() {
		
		ConstructedDiagramPanel oldCDP = (ConstructedDiagramPanel)getDiagramPanel();
		ConstructedConcreteDiagram oldCCD = oldCDP.getConstructedConcreteDiagram();
		
		DualGraph oldDG = oldCCD.getDualGraph();
		ArrayList<Node> nodes = oldDG.getNodes();
		ArrayList<Edge> edges = oldDG.getEdges();
		
		int n = nodes.size();  
		int m = edges.size();
		int cycle[] = new int[n+1];
		int nodei[] = new int [m+1];
		int nodej[] = new int [m+1];
		for(int i = 0 ; i < n; i++){
			nodes.get(i).setIndex(i+1);
		}
		nodei[0] = 0;
		nodej[0] = 0;
		for(int j = 1 ; j < m+1 ; j++){
			Edge e = edges.get(j-1);
			nodei[j] = e.getFrom().getIndex();
			nodej[j] = e.getTo().getIndex();	

		}
		boolean directed = false;
		pjr.graph.Graph.HamiltonCycle(n,m,directed,nodei,nodej,cycle);

		Polygon p = new Polygon();
		Node lastNode = null;
		for(int k = 1; k < cycle.length - 1; k++)	{
			Node n1 = nodes.get(cycle[k]-1);
			Node n2 = nodes.get(cycle[k+1]-1);
			lastNode = n2;
			
			Edge e = oldDG.getEdge(n1, n2);
			
			ArrayList<Point> bends = new ArrayList<Point>(e.getBends());
			if(e.getTo() == n1) {
				Collections.reverse(bends);
			}
			
			p.addPoint(n1.getX(), n1.getY());

			for(Point point : bends) {
				p.addPoint(point.x,point.y);
			}
		}
		p.addPoint(lastNode.getX(), lastNode.getY());
		ConstructedConcreteDiagram ccd = oldCCD.clone();
		
		ConcreteContour newCC = new ConcreteContour(ccd.getNextUnusedLabel(),p);
		ccd.addConcreteContour(newCC);		
		ConstructedDiagramWindow cdw = new ConstructedDiagramWindow(ccd.getAbstractDescription(),ccd.getConcreteContours());
		ConstructedDiagramPanel cdp = cdw.getConstructedDiagramPanel();
		cdp.setShowContour(true);		
		
	}
	
	
	public static boolean anyCrossings(Edge e, ArrayList<Edge> es) {

		for(Edge next :es) {
			if(edgesCross(e,next)) {
				return false;
			}
		}
		
		return false;
	}
	
	/** Test to see if the edges cross, including bend points. */
	public static boolean edgesCross(Edge e1, Edge e2) {
		
		Point lastPoint = e1.getFrom().getCentre();
		for(Point p : e1.getBends()) {
			if(ConstructedConcreteDiagram.edgeCrossLineSegment(lastPoint, p, e2)) {
				return true;
			}
			lastPoint = p;
		}
		if(ConstructedConcreteDiagram.edgeCrossLineSegment(lastPoint, e1.getTo().getCentre(), e2)) {
			return true;
		}

		return false;
	}

	
}
