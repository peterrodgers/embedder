package euler.simplify;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import euler.*;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;
import pjr.graph.Node;




public class GenerateJson {

	
	private Simplify simplify;
	
	
	public static void main(String[] args) {
		
		AbstractDiagram ad = new AbstractDiagram("0 a b c abc abd af");
		
		Simplify simplify = new Simplify(ad);
		simplify.randomizeWeights(1,10);
		
		DualGraphWindow dw = new DualGraphWindow(simplify.getDualGraph());
		DiagramPanel panel = dw.getDiagramPanel();
		
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
	 	ddp.layout();
		panel.fitGraphInPanel();
		panel.setForceNoRedraw(false);
		panel.update(dw.getDiagramPanel().getGraphics());
		
		panel.setShowGraph(true);
		panel.setShowRegion(false);
		panel.setShowContour(true);
		panel.setShowTriangulation(true);
		panel.setShowEdgeDirection(false);
		
		panel.setShowEdgeLabel(true);
		panel.setShowContourAreas(true);
		panel.setOptimizeContourAngles(true);
		panel.setOptimizeMeetingPoints(false);
		panel.setFitCircles(false);
		
		
	}
	

	public GenerateJson(Simplify s) {
		super();
		this.simplify = s;
	}
		
	

	public String jsonOutput() {

		AbstractDiagram ad = simplify.getAbstractDiagram();
		DualGraph dg = simplify.getDualGraph();
		
		String ret = "{\n";
		ret += "\"inputZones\": \""+ad.toString()+"\",\n\t\"abstractDiagram\": [";
		for(String z : simplify.getAbstractDiagram().getZoneList()) {
			String outZ = z;
			if(z.equals("")) {
				outZ = "0";
			}
			Node n = dg.firstNodeWithLabel(z);
			int x = n.getX();
			int y = n.getY();
			ret += "\n\t{\"zone\": \""+outZ+"\", \"weight\": "+simplify.getZoneWeights().get(z)+", \"coordinate\": {\"x\": "+x+", \"y\": "+y+"}},";
		}
		
		if(simplify.getAbstractDiagram().getZoneList().size() != 0) {
			ret = ret.substring(0,ret.length()-1);// remove last comma
		}
		ret += "\t\n],";
		
		// curves
		
		ConcreteDiagram concreteDiagram = new GeneralConcreteDiagram(dg);
		concreteDiagram.generateContours();
		concreteDiagram.setConcurrentOffset(ConcreteDiagram.CONCURRENT_OFFSET);
		ret += "\n\t\"curves\" :[";
		for(ConcreteContour cc : concreteDiagram.getConcreteContours()) {
			ret += "\n\t{\"curve\": {\"name\":\""+cc.getAbstractContour()+"\",\n\t\t\"coordinates\": [";
			
			Polygon polygon = cc.getPolygon();
			for(int i = 0 ; i < polygon.npoints; i++){
				int x = polygon.xpoints[i];
				int y = polygon.ypoints[i];
				ret += "\n\t\t\t{\"x\": "+x+", \"y\": "+y+"},";
			}
			if(polygon.npoints != 0) {
				ret = ret.substring(0,ret.length()-1);// remove last comma
			}
			ret += "\n\t\t]}},";
		}
		if(concreteDiagram.getConcreteContours().size() != 0) {
			ret = ret.substring(0,ret.length()-1);// remove last comma
		}
		ret += "\n\t],";

		DualGraph cloneGraph = concreteDiagram.getCloneGraph();
		
		ret += "\n\t\"triangulationEdges\" :[";
		for(TriangulationEdge te : cloneGraph.findTriangulationEdges()) {

			ret += "\n\t\t{\"edge\": {\"startX\": "+te.getFrom().getX()+", \"startY\": "+te.getFrom().getY()+", \"endX\": "+te.getTo().getX()+", \"endY\": "+te.getTo().getY()+", \"cut points\": [";
			
			for(CutPoint cp : te.getCutPoints()) {
				
				String contourString = "";
				for(ContourLink cl : cp.getContourLinks()) {
					contourString += cl.getContour();
				}
				int x = (int)(cp.getCoordinate().getX());
				int y = (int)(cp.getCoordinate().getY());

				ret += "\n\t\t\t{\"cutPoint\": {\"sets\": \""+contourString+"\", \"x\": "+x+", \"y\": "+y+"}},";
				
			}
			if(te.getCutPoints().size() != 0) {
				ret = ret.substring(0,ret.length()-1);// remove last comma
			}
			ret += "\n\t\t]}},";
			
		}
		if(cloneGraph.findTriangulationEdges().size() != 0) {
			ret = ret.substring(0,ret.length()-1);// remove last comma
		}
		ret += "\n\t]";

		ret += "\n}";
		
		return ret;
	}

	
	
}
