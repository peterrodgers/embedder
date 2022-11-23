package euler.display;
/**
 * Dual Graph  Applet 
 * @author Leishi Zhang
 */
 
import java.awt.*;
import java.applet.*;
import java.awt.event.KeyEvent;
import euler.AbstractDiagram;
import euler.DualGraph;
import euler.drawers.*;

public class DualGraphApplet extends Applet {
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
	new DualGraphApplet();
		
	}
	
	public void init() {

		DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
		
		//dg.randomizeNodePoints(new Point(50,50),400,400);
		
		//dg.setConnectivityRemovableUnRemovableEdges();
		
		DualGraphWindow dw = new DualGraphWindow(dg);
		dw.getDiagramPanel().setShowEdgeDirection(false);
		dw.getDiagramPanel().setShowEdgeLabel(true);
		dw.getDiagramPanel().setShowContour(false);
		dw.getDiagramPanel().setShowContourLabel(true);
		dw.getDiagramPanel().setShowTriangulation(false);
		
		dw.getDiagramPanel().setForceNoRedraw(true);
		DiagramDrawerPlanar ddp = new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout Algorithm", KeyEvent.VK_P, dw.getDiagramPanel());
		ddp.setDiagramPanel(dw.getDiagramPanel());
		ddp.layout();
//		PlanarForceTriangulationLayout pftl = new PlanarForceTriangulationLayout(dw.getDualGraph());
//		pftl.setAnimateFlag(false);
//		pftl.setIterations(50);
//		pftl.drawGraph();
		dw.getDiagramPanel().fitGraphInPanel();
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());

		
	}

	public void paint(Graphics g) {
		
	}
}

