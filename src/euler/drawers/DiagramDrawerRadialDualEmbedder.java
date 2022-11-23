package euler.drawers;

import java.awt.event.*;
import euler.*;

/**
 * Draw a Euler diagram dual in a radial manner.
 * 
 * This assumes the graph is an AbstractGraph
 *
 * @author Peter Rodgers
 */
public class DiagramDrawerRadialDualEmbedder extends DiagramDrawer {
	
	AbstractDiagram ad = null;


/** Trivial constructor. */
	public DiagramDrawerRadialDualEmbedder(AbstractDiagram ad) {
		super(KeyEvent.VK_R,"Euler Dual Radializer");
		this.ad = ad;
	}

/** Trivial constructor. */
	public DiagramDrawerRadialDualEmbedder(AbstractDiagram ad, int key, String s) {
		super(key,s);
		this.ad = ad;
	}


	public void layout() {
		
		
	}



}
