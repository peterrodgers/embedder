package euler.simplify;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import euler.*;
import euler.GeneralConcreteDiagram;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawerPlanar;




public class GenerateJson {

	
	private Simplify simplify;
	
	
	public static void main(String[] args) {
		
		AbstractDiagram ad = new AbstractDiagram("0 a b c abc abd af");
		
		Simplify simplify = new Simplify(ad);
		simplify.randomizeWeights(1,10);
		
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
			ret += "\n\t{\"zone\": \""+outZ+"\", \"weight\": "+simplify.getZoneWeights().get(z)+"},";
		}
		ret = ret.substring(0,ret.length()-1);// remove last comma
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
			ret = ret.substring(0,ret.length()-1);// remove last comma
			ret += "\n\t\t]}},";
		}
		ret = ret.substring(0,ret.length()-1);// remove last comma
		ret += "\n\t]";

		
		// triangulation
		
		ret += "\n}";
		
		return ret;
	}

/*
	private String addString(String key, String value) {

		String ret = "";
		ret += "\""+key+"\":";
		ret += "\""+value+"\"";
		
		return ret;
	}

	private String addInteger(String key, Integer value) {

		String ret = "";
		ret += "\""+key+"\":";
		ret += ""+value.toString()+"";
		
		return ret;
	}

*/

	
	
}
