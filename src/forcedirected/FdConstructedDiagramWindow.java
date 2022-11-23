package forcedirected;

import javax.imageio.*;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;

import euler.*;
import euler.drawers.*;
import euler.construction.*;
import euler.utilities.*;
import euler.views.*;
import forcedirected.Trash.ConstructedDiagramWindow;
import forcedirected.Trash.ESE_rep_ext2;


/** Graph layout window using ConstructedDiagramPanel */
public class FdConstructedDiagramWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 600;
	public static int HEIGHT = 600;
	public static String DEFAULT_WIN_TITLE = "Force Directed Constructed Diagram Editor ConstructedDiagramWindow.java"; 

	ArrayList<ConcreteContour> concreteContours	= null;
	protected File currentFile = new File("C:\\Users\\luana\\Desktop\\Samples\\");
	protected File startDirectory = null;
	protected int width = WIDTH;
	protected int height = HEIGHT;
	protected String winTitle = DEFAULT_WIN_TITLE;
	
	protected ConstructedDiagramPanel cdp = null;
	protected DiagramPanel dp = null;
	protected ConstructedConcreteDiagram initCCD = null;
	
	protected DiagramDrawerConstructedDiagramForceModel diagramDrawerForceDirected = new DiagramDrawerConstructedDiagramForceModel();

	private static final long serialVersionUID = 1L;

	
	
	public static void main(String[] args) {
		
		DiagramPanel dp = new DiagramPanel();
		DiagramUtilityRandomWellformedDiagram wfd = new DiagramUtilityRandomWellformedDiagram(3);
		wfd.setDiagramPanel(dp);
		wfd.apply();
		
		ConcreteDiagram cd = dp.getConcreteDiagram();
		ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
		DiagramDrawerConstructedDiagramForceModel diagramDrawerForceDirected = new DiagramDrawerConstructedDiagramForceModel();
		diagramDrawerForceDirected.setConcreteContours(ccs);
		diagramDrawerForceDirected.setDiagramPanel(dp);
		
		FdConstructedDiagramWindow cdw = new FdConstructedDiagramWindow ("",ccs, dp, diagramDrawerForceDirected); 
		ConstructedDiagramPanel	cdp = cdw.getConstructedDiagramPanel();	
		
		cdp.setShowEdgeDirection(false);
		cdp.setShowEdgeLabel(true);
		cdp.setShowGraph(false);
		cdp.setShowContour(true);
		cdp.setShowContourLabel(true);
		cdp.setShowTriangulation(false);
		
		cdp.setShowEdgeLabel(true);
		cdp.setJiggleLabels(false);
		cdp.setShowRegion(false);
		cdp.setShowContourAreas(false);
		cdp.setOptimizeContourAngles(false);
		cdp.setOptimizeMeetingPoints(false);
		cdp.setFitCircles(false);
		
		diagramDrawerForceDirected.setConstructedDiagramPanel(cdp);
		cdp.addDiagramDrawer(diagramDrawerForceDirected);
		cdw.initMenu();  
		cdp.requestFocus();
	}
	
	
	public FdConstructedDiagramWindow(String abstractDescription, ArrayList<ConcreteContour> inConcreteContours, DiagramPanel inDp, DiagramDrawerConstructedDiagramForceModel inDiagramDrawerForceDirected) {
		super(DEFAULT_WIN_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		concreteContours = inConcreteContours;
		dp = inDp;
		diagramDrawerForceDirected = inDiagramDrawerForceDirected;
		
		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);
		
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(abstractDescription, concreteContours);
		initCCD = ccd.clone();
		
		cdp = new ConstructedDiagramPanel(ccd,this);
		
		getContentPane().add(cdp);
			
		initView();
		initUtility();
		initLayout();
		initMenu();
		
		setSize(width,height);

		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);

		setVisible(true);

		cdp.requestFocus();
	}


	public ConstructedDiagramPanel getConstructedDiagramPanel() {return cdp;}

	
	
	private void initView() {
		cdp.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_C, "Cycle Items Displayed",KeyEvent.VK_C));
		cdp.addDiagramView(new DiagramViewFitDiagramInWindow(KeyEvent.VK_F, "Fit Diagram In Window",KeyEvent.VK_F));
	}

	
	private void initUtility() {
		/*
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_2, "Create Atomic 2 Set Euler Dual",KeyEvent.VK_2,2,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_3, "Create Atomic 3 Set Euler Dual",KeyEvent.VK_3,3,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_4, "Create Atomic 4 Set Euler Dual",KeyEvent.VK_4,4,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_5, "Create Atomic 5 Set Euler Dual",KeyEvent.VK_5,5,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_6, "Create Atomic 6 Set Euler Dual",KeyEvent.VK_6,6,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_7, "Create Atomic 7 Set Euler Dual",KeyEvent.VK_7,7,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_8, "Create Atomic 8 Set Euler Dual",KeyEvent.VK_8,8,true));
		cdp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_9, "Create Atomic 9 Set Euler Dual",KeyEvent.VK_9,9,true));
		cdp.addDiagramUtility(new DiagramUtilityDiagramData(KeyEvent.VK_D, "Diagram Data",KeyEvent.VK_D));
		*/
		cdp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F6, "Create Wellformed 2 Set Euler Dual",KeyEvent.VK_F6,2,false));
		cdp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F7, "Create Wellformed 3 Set Euler Dual",KeyEvent.VK_F7,3, false));
		cdp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F8, "Create Wellformed 4 Set Euler Dual",KeyEvent.VK_F8,4,false));
		cdp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F9, "Create Wellformed 5 Set Euler Dual",KeyEvent.VK_F9,5,true));
		/*
		cdp.addDiagramUtility(new DiagramUtilityRandomEulerDiagram(KeyEvent.VK_C, "Create Random 5 Set Euler Dual",KeyEvent.VK_C,5));
		cdp.addDiagramUtility(new DiagramUtilityResetConcreteDiagram(KeyEvent.VK_R, "Reset Concrete Diagram",KeyEvent.VK_R));
		cdp.addDiagramUtility(new DiagramUtilityFaceEdgeRemoval(KeyEvent.VK_U,"Remove All Poly Edges",KeyEvent.VK_U));

		cdp.addDiagramUtility(new DiagramUtilityPlanarFromNonPlanar(KeyEvent.VK_F1, "Find Best wf Planar Dual",KeyEvent.VK_F1));
		cdp.addDiagramUtility(new DiagramUtilityAttemptToConnect(KeyEvent.VK_F2, "Attempt To Connect Disconnected Components",KeyEvent.VK_F2));
		cdp.addDiagramUtility(new DiagramUtilityFaceEdgeAddition(KeyEvent.VK_F3, "Add Edges to Faces",KeyEvent.VK_F3));
		cdp.addDiagramUtility(new DiagramUtilityRenameDisconnectedContours(KeyEvent.VK_F4, "Rename Disconnected Contours",KeyEvent.VK_F4));
		cdp.addDiagramUtility(new DiagramUtilityCheckHamiltonCycle(KeyEvent.VK_F5, "Check Hamilton Cycle",KeyEvent.VK_F5));
		*/
	}


	private void initLayout() {
		cdp.addDiagramDrawer(new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout", KeyEvent.VK_P,cdp));
		//cdp.addDiagramDrawer(new ESE_rep_ext2());
		cdp.addDiagramDrawer(new DiagramDrawerPlanarForceWithDialog(KeyEvent.VK_E, "Spring Embedder with Edge Force",KeyEvent.VK_E));
		cdp.addDiagramDrawer(new DiagramDrawerPlanarForceTriangulation(KeyEvent.VK_T, "Spring Embedder on Triangulated Graph",KeyEvent.VK_T));
		cdp.addDiagramDrawer(new DiagramDrawerTriangulationForceWithoutEmpty(KeyEvent.VK_0, "No Empty set, Spring Embedder on Triangulated Graph",KeyEvent.VK_0));
	}

	public void initMenu() {
	
		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

		// File Menu
		JMenu fileMenu = new JMenu("File");

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);
	
		JMenuItem fileNewItem = new JMenuItem("New",KeyEvent.VK_N);
		fileNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(fileNewItem);
		fileNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileNew();
			}
		});
		
		JMenuItem fileOpenItem = new JMenuItem("Open...",KeyEvent.VK_O);
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(fileOpenItem);
		fileOpenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpen();
			}
		});


		//JMenuItem fileOpenAdjacencyItem = new JMenuItem("Open Adjacency File...");
		//fileMenu.add(fileOpenAdjacencyItem);

		//JMenuItem fileOpenWeightedAdjacencyItem = new JMenuItem("Open Weighted Adjacency File...");
		//fileMenu.add(fileOpenWeightedAdjacencyItem);

		//JMenuItem fileOpenXMLItem = new JMenuItem("Open XML File...");
		//fileMenu.add(fileOpenXMLItem);

		JMenuItem fileSaveItem = new JMenuItem("Save",KeyEvent.VK_S);
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveItem);
		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSave();
			}
		});
		
		JMenuItem fileSaveAsItem = new JMenuItem("Save As...",KeyEvent.VK_A);
		fileSaveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveAsItem);
		fileSaveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});
		
		JMenuItem fileSaveDualGraphItem = new JMenuItem("Save Dual Graph...",KeyEvent.VK_D);
		fileSaveDualGraphItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveDualGraphItem);
		fileSaveDualGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveDualGraph();
			}
		});
		
		
		JMenuItem fileSaveInitCDItem = new JMenuItem("Save Initial Concrete Diagram...",KeyEvent.VK_I);
		fileSaveInitCDItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveInitCDItem);
		fileSaveInitCDItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveInitCD();
			}
		});
		
		//JMenuItem fileSaveSimpleItem = new JMenuItem("Save Simple Graph...");
		//fileMenu.add(fileSaveSimpleItem);

		//JMenuItem fileSaveXMLItem = new JMenuItem("Save XML File...");
		//fileMenu.add(fileSaveXMLItem);

		JMenuItem filePNGItem = new JMenuItem("Export to png",KeyEvent.VK_E);
		filePNGItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		fileMenu.add(filePNGItem);
		filePNGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filePNG();
			}
		});
		
		JMenuItem fileSaveAllItem = new JMenuItem("Save All...",KeyEvent.VK_L);
		fileSaveAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveAllItem);
		fileSaveAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAll();
			}
		});
		
		JMenuItem fileExitItem = new JMenuItem("Exit",KeyEvent.VK_X);
		fileExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		fileMenu.add(fileExitItem);

		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileExit();
			}
		});
		
		// Edit Menu
		JMenu editMenu = new JMenu("Edit");

		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
	
		JMenuItem editClearAllItem = new JMenuItem("Clear Diagram",KeyEvent.VK_X);
		editClearAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		editMenu.add(editClearAllItem);


		editClearAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editClearAll();
			}

		});


		//View Menu
		JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(viewMenu);

		for(DiagramView v : cdp.getDiagramViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			viewMenu.add(menuItem);
		}


		// Utilities Menu
		JMenu utilitiesMenu = new JMenu("Utilities");
        utilitiesMenu.setMnemonic(KeyEvent.VK_U);
		menuBar.add(utilitiesMenu);

		for(DiagramUtility u : cdp.getDiagramUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			utilitiesMenu.add(menuItem);
		}


		JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(layoutMenu);

		for(DiagramDrawer d : cdp.getDiagramDrawerList()) {
	        JMenuItem menuItem = new JMenuItem(d.getMenuText(), d.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(d.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			layoutMenu.add(menuItem);
		}
		
	}

	
	
	protected void fileExit() {
		System.exit(0);
	}
	
	protected void fileNew() {
		if (currentFile != null) {
			if (!currentFile.isDirectory()) {
				currentFile = currentFile.getParentFile();
			}
		}
		cdp.getConstructedConcreteDiagram().clear();
		//cdp.resetDiagram();
		cdp.update(cdp.getGraphics());
	}

	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
					
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			cdp.getConstructedConcreteDiagram().clear();
			currentFile = chooser.getSelectedFile();
		    cdp.getConstructedConcreteDiagram().clear();
		    cdp.getConstructedConcreteDiagram().loadDiagram(currentFile);
for(ConcreteContour cc : cdp.getConstructedConcreteDiagram().getConcreteContours()) {
	System.out.println(cc.getAbstractContour());
}
cdp.resetDiagram();
			cdp.update(cdp.getGraphics());

		}
	}
	
	protected void fileSave() {
		if (currentFile == null) {
			fileSaveAs();
		} else {
			if (currentFile.isDirectory()) {
				fileSaveAs();
			} else {
				//cdp.resetDiagram();
				cdp.getConstructedConcreteDiagram().saveToFile(new File(currentFile.getAbsolutePath()+".ccd"));
				//cdp.update(cdp.getGraphics());
			}
		}
	}


	protected void fileSaveAs() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			File cdFile = new File(currentFile.getAbsolutePath()+".ccd");
			chooser = new JFileChooser(cdFile);
			//cdp.resetDiagram();
			cdp.getConstructedConcreteDiagram().saveToFile(cdFile);
		}
	}
	
	protected void fileSaveDualGraph() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		if (currentFile == null) {
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				File dgFile = new File(currentFile.getAbsolutePath()+".dg");
				chooser = new JFileChooser(dgFile);
				dp.getDualGraph().saveAll(dgFile);
				//cdp.getConstructedConcreteDiagram().generateDualGraph();
				//cdp.getConstructedConcreteDiagram().getDualGraph().saveAll(currentFile);
			}
		} else{
			File dgFile = new File(currentFile.getAbsolutePath()+".dg");
			chooser = new JFileChooser(dgFile);
			dp.getDualGraph().saveAll(dgFile);
		}
	}
	
	protected void fileSaveInitCD() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			File cdFile = new File(currentFile.getAbsolutePath()+".ccd");
			chooser = new JFileChooser(cdFile);
			//cdp.resetDiagram();
			initCCD.saveToFile(cdFile);
		}
	}
	
	
	protected void filePNG() {

		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				pngFile = new File(currentFile.getAbsolutePath()+".png");
				chooser = new JFileChooser(pngFile);
				if (!currentFile.isDirectory()) {
					chooser.setSelectedFile(currentFile);
				}
			}
		} else {
			pngFile = new File(currentFile.getAbsolutePath()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}

		/*
		if (pngFile == null){
			return;
		}
		*/
		try {
			BufferedImage image = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
			paint(image.getGraphics());
			ImageIO.write(image,"png",pngFile);
		} catch (Exception e) {}
		return;
	}
	
	protected void fileSaveAll() {
		fileSaveAs();
		fileSaveDualGraph();
		filePNG();
	}

	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(DiagramView v : cdp.getDiagramViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}
		for(DiagramDrawer d : cdp.getDiagramDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(DiagramUtility u : cdp.getDiagramUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.setDiagramPanel(dp);
				u.apply();
				
				ConcreteDiagram cd = dp.getConcreteDiagram();
				ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
				diagramDrawerForceDirected.setConcreteContours(ccs);
				diagramDrawerForceDirected.setDiagramPanel(dp);
				cdp.getConstructedConcreteDiagram().clear();
				/*
				for (ConcreteContour c : ccs){
					cdp.getConstructedConcreteDiagram().addConcreteContour(c);
				}
				*/
				/*
				getContentPane().remove(cdp);
				
				cdp = new ConstructedDiagramPanel(new ConstructedConcreteDiagram("",ccs),this);
				getContentPane().add(cdp);
				cdp.update(cdp.getGraphics());
				*/
				cdp.update(cdp.getGraphics());
				
				//repaint();
				return;
			}
		}
	}
	
	private void editClearAll() {
		getConstructedDiagramPanel().getConstructedConcreteDiagram().clear();	
		repaint();
		
	}



}
