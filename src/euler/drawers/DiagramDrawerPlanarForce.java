package euler.drawers;


import java.awt.event.*;
import euler.*;

/**
 * A version of Eades spring embedder for laying out planar graphs
 * without changing the planarity and faces
 *
 * @author Peter Rodgers
 * @author Leishi Zhang
 */
public class DiagramDrawerPlanarForce extends DiagramDrawer {

	PlanarForceLayout pfl = new PlanarForceLayout();
	
	/** Trivial constructor. */
	public DiagramDrawerPlanarForce() {
		super(KeyEvent.VK_E,"Spring Embedder with Edge Force");
	}
	
	/** Trivial constructor. */
	public DiagramDrawerPlanarForce(int key, String s) {
		super(key,s);
	}
	
	/** Trivial constructor. */
	public DiagramDrawerPlanarForce(int key, String s, int mnemomic) {
		super(key,s,mnemomic);
	}

	public long getTimeLimit() {return pfl.getTimeLimit();}
	public long getTime() {return pfl.getTime();}
	public double getQ() {return pfl.getQ();}
	public double getK() {return pfl.getK();}
	public double getR() {return pfl.getR();}
	public double getF() {return pfl.getF();}
	public boolean getAnimateFlag() {return pfl.getAnimateFlag();}
	public int getBorderLimit() {return pfl.getBorderLimit();}
	public boolean getUseBorderLimit() {return pfl.getUseBorderLimit();}
	public int getIterations() {return pfl.getIterations();}
	
	public void setTimeLimit(int inT) {pfl.setTimeLimit(inT);}
	public void setQ(double inQ) {pfl.setQ(inQ);}
	public void setK(double inK) {pfl.setK(inK);}
	public void setR(double inR) {pfl.setR(inR);}
	public void setF(double inF) {pfl.setF(inF);}
	public void setAnimateFlag(boolean inAnimateFlag) {pfl.setAnimateFlag(inAnimateFlag);}
	public void setBorderLimit(int limit) {pfl.setBorderLimit(limit);}
	public void setUseBorderLimit(boolean flag) {pfl.setUseBorderLimit(flag);}
	public void setIterations(int iterations) {pfl.setIterations(iterations);}
	
	/** overloaded method */
	public void setDiagramPanel(DiagramPanel inDiagramPanel) {
		super.setDiagramPanel(inDiagramPanel);
		pfl.setDiagramPanel(inDiagramPanel);
	}

	/** Draws the graph. */
	public void layout() {
		
		pfl.setDiagramPanel(getDiagramPanel());
		
		pfl.drawGraph();
		
        //getDiagramPanel().paintContours((Graphics2D)getDiagramPanel().getGraphics(),getDiagramPanel().getDualGraph());
	    //getDiagramPanel().update(getDiagramPanel().getGraphics());
		if(!getAnimateFlag() && getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}
	

}
