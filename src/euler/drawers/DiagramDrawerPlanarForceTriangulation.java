package euler.drawers;


import java.awt.event.*;

/**
 * A version of Eades spring embedder for laying out 
 * planar graphs without changing the planarity and faces
 * @author Peter Rodgers
 * @author Leishi Zhang
 */

public class DiagramDrawerPlanarForceTriangulation extends DiagramDrawer {

	PlanarForceTriangulationLayout pftl = new PlanarForceTriangulationLayout();
	
	/** Trivial constructor. */
	public DiagramDrawerPlanarForceTriangulation() {
		super(KeyEvent.VK_T, "Spring Embedder on Triangulated Graph",KeyEvent.VK_T);
	}

	/** Trivial constructor. */
	public DiagramDrawerPlanarForceTriangulation(int key, String s, int mnemomic) {
		super(key,s,mnemomic);
	}

	public long getTimeLimit() {return pftl.getTimeLimit();}
	public long getTime() {return pftl.getTime();}
	public double getQ() {return pftl.getQ();}
	public double getK() {return pftl.getK();}
	public double getR() {return pftl.getR();}
	public double getF() {return pftl.getF();}
	public boolean getAnimateFlag() {return pftl.getAnimateFlag();}
	public int getBorderLimit() {return pftl.getBorderLimit();}
	public int getIterations() {return pftl.getIterations();}
	
	public void setTimeLimit(int inT) {pftl.setTimeLimit(inT);}
	public void setQ(double inQ) {pftl.setQ(inQ);}
	public void setK(double inK) {pftl.setK(inK);}
	public void setR(double inR) {pftl.setR(inR);}
	public void setF(double inF) {pftl.setF(inF);}
	public void setAnimateFlag(boolean inAnimateFlag) {pftl.setAnimateFlag(inAnimateFlag);}
	public void setBorderLimit(int limit) {pftl.setBorderLimit(limit);}
	public void setIterations(int iterations) {pftl.setIterations(iterations);}
	
	/** Draws the graph. */
	public void layout() {
		
		setAnimateFlag(false);
		
		pftl.setDiagramPanel(getDiagramPanel());
		
		pftl.drawGraph();
		
		if(!getAnimateFlag() && getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}
	

}
