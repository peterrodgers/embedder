package euler.display;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import pjr.graph.*;
import euler.*;
import euler.drawers.*;
import euler.utilities.*;

public class WellformedDiagramWindow extends JFrame {

	public final static int numberOfSets = 6;
	public final static int WINDOWWIDTH = 700;
	public final static int WINDOWHEIGHT = 700;
	
	public static ArrayList<WellFormedConcreteDiagram> cds = new ArrayList<WellFormedConcreteDiagram>();
	public static Rectangle windowRectangle = new Rectangle(50,50,WINDOWWIDTH-100,WINDOWHEIGHT-100);
	
	public static void main(String[] args) {
		
		
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a ab ac"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d e f g h i"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab ac ad acd ae"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab ac acd acde acdf acdef"));
		
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c d e ae bd cd"));
		
		// Avi paper diagrams
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab ac ad acd"));
//		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b c ab ac bc abc ad ae af aef bcg bch bcgh bcghi"));
		DiagramUtilityRandomWellformedDiagram dur = new DiagramUtilityRandomWellformedDiagram(numberOfSets);
		DualGraph dg = dur.generateRandomWFDiagram(numberOfSets, true);

System.out.println("Diagram to draw    "+ dg.findAbstractDiagram());
//		ArrayList<DualGraph> subDiagrams = dg.findNestedSubdiagrams(true);
//		ConcreteDiagram cd = nicelyDrawWFUnitaryDiagram(dg);
//		cds.add(cd);

		ArrayList<ArrayList<DualGraph>> subDiagrams = new ArrayList<ArrayList<DualGraph>>();
		ArrayList<DualGraph> first = new ArrayList<DualGraph>();
		first.add(dg);
		subDiagrams.add(first);
		
		subDiagramDrawRec(null,subDiagrams);
		
		new WellformedDiagramWindow("Test",cds);

	}

	public WellformedDiagramWindow(String title, ArrayList<WellFormedConcreteDiagram> cds) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		StaticDiagramPanel panel = new StaticDiagramPanel(cds,this);
		getContentPane().add(panel);
		setSize(WINDOWWIDTH,WINDOWHEIGHT);
		
		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);

		setVisible(true);

		panel.requestFocus();
		
		panel.update(panel.getGraphics());

	}
	
	public static void subDiagramDrawRec(WellFormedConcreteDiagram containingDiagram, ArrayList<ArrayList<DualGraph>> subDiagrams) {
		
		for(ArrayList<DualGraph> dgs : subDiagrams) {

			DualGraph firstDG = dgs.get(0);
			
			String containingZone = firstDG.getLabel();

			ArrayList<ConcreteContour> containingContours = null;
			if(containingDiagram != null) {
				containingContours = containingDiagram.getConcreteContours();
			}
			
			HashMap<String,Area> zoneAreas = new HashMap<String, Area>();
			if(containingContours == null || containingContours.size() == 0) {
				// outside zone or parent is outside zone
				zoneAreas.put("",new Area(windowRectangle));
			} else {
				zoneAreas = ConcreteContour.generateZoneAreas(containingContours);
			}

			Rectangle fullR = null;
			for(String ac : zoneAreas.keySet()) {
				if(ac.equals(containingZone)) {
					Area a = zoneAreas.get(ac);
					ArrayList<Polygon>  ps = ConcreteContour.polygonsFromArea(a);
					Polygon p = ps.get(0);
					
					
					euler.maxrectangle.MaxRectangleFinder maxRectangleFinder = new euler.maxrectangle.MaxRectangleFinder(p);
					fullR = maxRectangleFinder.getMaxSquare();
					if(fullR == null) {
						// nasty fix to prevent a crash
						fullR = p.getBounds();
					}
				}
			}
			ArrayList<Rectangle> rectangles = euler.Util.divideIntoRectangles(fullR, dgs.size());

			int count = 0;
			for(DualGraph dg : dgs) {
			
				char[] containingChars = containingZone.toCharArray();
			
				// now remove the containing node from every node in the sub diagram
				for(Node n : dg.getNodes()) {
					String newLabel = new String(n.getLabel());
					for(int i = 0; i < containingChars.length; i++) {
						String contour = Character.toString(containingChars[i]);
						if(n.getLabel().contains(contour)) {
							newLabel = newLabel.replaceFirst(contour, "");
						}
						n.setLabel(newLabel);
					}
				}
				// needs to be removed from the graph label as well
				String newLabel = new String(dg.getLabel());
				for(int i = 0; i < containingChars.length; i++) {
					String contour = Character.toString(containingChars[i]);
					if(dg.getLabel().contains(contour)) {
						newLabel = newLabel.replaceFirst(contour, "");
					}
					dg.setLabel(newLabel);
				}
//Node emptyNode = dg.firstNodeWithLabel("");
//dg.removeNode(emptyNode);
				
				ArrayList<DualGraph> nestedDiagrams = dg.findNestedSubdiagrams(true);
				ArrayList<ArrayList<DualGraph>> groupedSubDiagrams = groupByGraphLabel(nestedDiagrams);
				WellFormedConcreteDiagram cd = nicelyDrawWFAtomicDiagram(dg);
				cds.add(cd);
	
	
				ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
				Rectangle r = rectangles.get(count);
				final double PADDING = 0.05;
				int x1 = r.x + pjr.graph.Util.convertToInteger(r.width*PADDING);
				int y1 = r.y + pjr.graph.Util.convertToInteger(r.height*PADDING);
				int x2 = r.x + r.width - pjr.graph.Util.convertToInteger(r.height*PADDING);
				int y2 = r.y + r.height - pjr.graph.Util.convertToInteger(r.height*PADDING);
				ConcreteDiagram.fitContoursInRectangle(ccs, x1, y1, x2, y2);
				cds.add(cd);

				// recursive call
				subDiagramDrawRec(cd, groupedSubDiagrams);
				
				count++;
			}
		}
		
	}
	

	/**
	 * Take the subdiagrams and group them by the zone they are contained in
	 */
	public static ArrayList<ArrayList<DualGraph>> groupByGraphLabel(ArrayList<DualGraph> nestedDiagrams) {
		HashMap<String,ArrayList<DualGraph>> map = new HashMap<String, ArrayList<DualGraph>>();
		for(DualGraph dg : nestedDiagrams) {
			String label = dg.getLabel();
			ArrayList<DualGraph> list = map.get(label);
			if(list == null) {
				list = new ArrayList<DualGraph>();
				list.add(dg);
				map.put(label, list);
			} else {
				list.add(dg);
			}
		}
		
		ArrayList<ArrayList<DualGraph>> ret = new ArrayList<ArrayList<DualGraph>>();
		for(String label : map.keySet()) {
			ArrayList<DualGraph> list = map.get(label);
			ret.add(list);
		}
		return ret;
	}

	/** Takes a unitary diagram and draws it in best possible way. */
	public static WellFormedConcreteDiagram nicelyDrawWFAtomicDiagram(DualGraph dg) {
System.out.println("Drawing subdiagram "+dg.findAbstractDiagram());

//DualGraphWindow dgw = new DualGraphWindow(dg);
//DiagramPanel dp = dgw.getDiagramPanel();
		DiagramPanel dp = new DiagramPanel(dg);
		dp.setForceNoRedraw(true);

		//dg.randomizeNodePoints(new Point(50,50),400,400);
		
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(dp);
		ddp.setDiagramPanel(dp);
		ddp.layout();

		WellFormedConcreteDiagram cd = new WellFormedConcreteDiagram(dg);
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.generateContours();
		dp.setConcreteDiagram(cd);
		
		if(dg.findAbstractDiagram().isomorphicTo(new AbstractDiagram("0"))) {
			//no need to do futher layout if the the diagram is empty
			return cd;
		}

		PlanarForceTriangulationLayout pftl = new PlanarForceTriangulationLayout(cd);
		pftl.setDiagramPanel(dp);
		pftl.setAnimateFlag(false);
		pftl.setTimeLimit(5000);
		pftl.setIterations(5000);
		pftl.drawGraph();
		
		cd.setOptimizeContourAngles(true);
		cd.setFitCircles(true);
		cd.routeContours();
		
		dp.setShowEdgeDirection(false);
		dp.setShowEdgeLabel(true);
		dp.setShowContour(true);
		dp.setShowContourLabel(true);
		dp.setShowTriangulation(true);
		
		dp.setForceNoRedraw(false);
		
		dp.update(dp.getGraphics());
		
		return cd;
	}
	

}
