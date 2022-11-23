package euler.experiments;

import euler.*;

/**
 * As a GraphView, but for DiagramPanel
 *
 * @author Peter Rodgers
 */


public abstract class DiagramExperiment {

	/** the associated DiagramPanel */
	protected DiagramPanel diagramPanel = null;
/** the key that should be pressed to start the graph drawing method. Codes can be found in the KeyEvent class. */
	protected int acceleratorKey = 0;
/** the message that should appear in menu options to start the graph drawing method */
	protected String menuText;
/** the menu mnemonic for initiating the layout. Codes can be found in the KeyEvent class. */
	protected int mnemonicKey = 0;

/** Minimal constructor. */
	public DiagramExperiment(String inMenuText) {
		menuText = inMenuText;
	}

/** Trivial constructor. */
	public DiagramExperiment(int inAccelerator, String inMenuText) {
		acceleratorKey = inAccelerator;
		menuText = inMenuText;
	}

/** Trivial constructor. */
	public DiagramExperiment(int inAccelerator, String inMenuText, int inMnemonic) {
		acceleratorKey = inAccelerator;
		menuText = inMenuText;
		mnemonicKey = inMnemonic;
	}

/** Trival accessor. */
	public DiagramPanel getDiagramPanel() {return diagramPanel;}
	/** Returns the dual graph of diagramPanel, as this is the one that is drawn. */
	public DualGraph getDualGraph() {return diagramPanel.getDualGraph();}
/** Trival accessor. */
	public int getAcceleratorKey() {return acceleratorKey;}
/** Trival accessor. */
	public String getMenuText() {return menuText;}
/** Trival accessor. */
	public int getMnemonicKey() {return mnemonicKey;}
/** This modifier should only be used by DiagramPanel. */
	public void setDiagramPanel(DiagramPanel inDiagramPanel) {diagramPanel=inDiagramPanel;}
/** Trivial modifier. */
	public void setAcceleratorKey(int inKey) {acceleratorKey=inKey;}
/** Trivial modifier. */
	public void setMenuText(String inMenuText) {menuText=inMenuText;}
/** Trivial modifier. */
	public void setMnemonicKey(int inKey) {mnemonicKey=inKey;}
	
	/** Overwrite this with the code to run the experiment. */
	public abstract void experiment();
}



