package euler.simplify;

import java.awt.event.KeyEvent;

import euler.AbstractDiagram;
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

		String ret = "{\n";
		ret += "\"input\": {";
		for(String z : simplify.getAbstractDiagram().getZoneList()) {
			String outZ = z;
			if(z.equals("")) {
				outZ = "0";
			}
			ret += addInteger(outZ,simplify.getZoneWeights().get(z));
			ret += ",";
		}
		ret = ret.substring(0,ret.length()-1);// remove last comma
		ret += "}";
		ret += "\n}";
			
		
		return ret;
	}


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



	
	
}
