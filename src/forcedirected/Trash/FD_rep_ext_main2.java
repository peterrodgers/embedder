package forcedirected.Trash;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import euler.*;
import euler.construction.ConstructedDiagramPanel;
import euler.construction.ConstructedDiagramWindow;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawer;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.DiagramDrawerPlanarForce;
import euler.drawers.PlanarForceLayout;
import euler.inductive.HybridGraph;
import euler.polygon.RegularPolygon;
import euler.utilities.DiagramUtility;
import euler.utilities.DiagramUtilityRandomWellformedDiagram;
import euler.views.DiagramView;
import euler.views.DiagramViewCycleItemsDisplayed;
import pjr.graph.*;
import pjr.graph.display.*;
import pjr.graph.drawers.*;
import java.util.HashMap;

public class FD_rep_ext_main2 extends JFrame {

	public static void main(String[] args) {
		
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
		
		DiagramPanel dp = new DiagramPanel(); //dg);
		
		DiagramUtilityRandomWellformedDiagram wfd = new DiagramUtilityRandomWellformedDiagram(3);
		wfd.setDiagramPanel(dp);
		wfd.apply();
		
		ConcreteDiagram cd = dp.getConcreteDiagram();
		ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
		ESE_rep_ext2 diagramDrawerForceDirected = new ESE_rep_ext2();
		diagramDrawerForceDirected.setConcreteContours(ccs);
		diagramDrawerForceDirected.setDiagramPanel(dp);
		
		
		ConstructedDiagramWindow cdw = new ConstructedDiagramWindow (" a b c ab bc ac abc",ccs); //(" a b c ab ac bc abc", ccs);
		//cannot use AbstractDiagram.VennFactory(3).toString() because end up having "0 0 a b c ab ac bc abc" 
		//and need to have a space at the beginning of string
		//that is " a b c ab ac bc abc" not "a b c ab ac bc abc" or else would get node 'a' out of place
		
		ConstructedDiagramPanel	cdp = cdw.getConstructedDiagramPanel();	
		
		cdp.setShowEdgeDirection(false); //f
		cdp.setShowEdgeLabel(true);
		cdp.setShowGraph(false);//f
		cdp.setShowContour(true);
		cdp.setShowContourLabel(true);
		cdp.setShowTriangulation(false);//f
			
		
		//diagramDrawerForceDirected.setConstructedDiagramWindow(cdw);
		diagramDrawerForceDirected.setConstructedDiagramPanel(cdp);
		

		cdp.addDiagramDrawer(diagramDrawerForceDirected);
		cdw.initMenu();   //can do it with cdw.actionPerformed(event); instead and => can reset initMenu to private? 
		cdp.requestFocus();
	}

		/*
		 * 
		 * just to ensure that it is getting the right zones
		 
		// Get polygons of all zones of all contours
		// Get polygons of all zones of all contours
		HashMap<String, Area> zoneAreas = ConcreteContour.generateZoneAreas(ccs);
		Iterator itr = zoneAreas.entrySet().iterator();
		ArrayList<ArrayList<Polygon>> zspolys = new ArrayList<ArrayList<Polygon>>();
		ArrayList<Polygon> polys = new ArrayList<Polygon>();
		while (itr.hasNext()){
			Entry<String,Area> pairs = (Entry<String,Area>)itr.next();
			zspolys.add(ConcreteContour.polygonsFromArea(pairs.getValue()));
		}
		zspolys.remove(0);  //remove the empty set
		
		for (int pntI=0; pntI< p.npoints; pntI++){
			Point pnt = new Point (p.xpoints[pntI], p.ypoints[pntI]); 
			for (ArrayList<Polygon> zpolys : zspolys){
				for (Polygon zpoly : zpolys){
					
					if (zpoly.contains(pnt)){
						polys.add(zpoly);
					}
				}
			}
		}
		*/
		
	
		
		
		/*
			AbstractDiagram abstractDiagram = new AbstractDiagram ("0 a b c ac bc ab abc"); //AbstractDiagram.VennFactory(3);
			DualGraph dualGraph = new DualGraph(abstractDiagram);
			dualGraph.randomizeNodePoints(new Point(50,50),400,400);

			GeneralConcreteDiagram concreteDiagram = new GeneralConcreteDiagram(dualGraph);
		
			//concreteDiagram.generateContours();
			ArrayList<ConcreteContour> concreteContours = new ArrayList<ConcreteContour>();
			concreteContours = concreteDiagram.getConcreteContours();
		*/	
	//}
	
	
	public static ESE_rep_ext2 setupEmbedder (DualGraphWindow dw){
		
		DiagramPanel dp = dw.getDiagramPanel();
		
		ESE_rep_ext2 embedder = new ESE_rep_ext2();
		
		embedder.setDualGraphWindow(dw);
		embedder.setDiagramPanel(dp);
		
		dp.addDiagramDrawer(embedder);
		//dw.initMenu();  // initMenu was set to public; before it was private 
		
		return embedder;
	}
	
	

	public static void setupWindow(DualGraphWindow dw) {
		
		DiagramPanel dp = dw.getDiagramPanel();
		
		dp.setShowEdgeLabel(true);
		dp.setJiggleLabels(false);
		dp.setShowGraph(false); 
		dp.setShowRegion(false);
		dp.setShowContour(true);
		dp.setShowTriangulation(false);
		dp.setShowEdgeLabel(false);
		dp.setShowContourLabel(true);
		dp.setShowContourAreas(false);
		dp.setOptimizeContourAngles(false);
		dp.setOptimizeMeetingPoints(false);
		dp.setFitCircles(false);

		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(true);
		dw.getDiagramPanel().setShowContourLabel(true);
		dw.getDiagramPanel().setShowTriangulation(false);
		dw.getDiagramPanel().setJiggleLabels(false);
		
		dp.setForceNoRedraw(true);
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dp);
	 	ddp.layout();

		PlanarForceLayout pfl = new PlanarForceLayout(dw.getDiagramPanel());
		pfl.setAnimateFlag(false);
		pfl.setIterations(50);
		pfl.drawGraph();

		dp.fitGraphInPanel();
		dp.setForceNoRedraw(false);
		dp.update(dw.getDiagramPanel().getGraphics());
	}
		
}