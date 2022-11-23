package forcedirected.Trash;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import pjr.graph.GeneralXML;

import euler.AbstractDiagram;
import euler.ConcreteContour;
import euler.ConcreteDiagram;
import euler.DiagramPanel;
import euler.DualGraph;
import euler.construction.ConstructedConcreteDiagram;
import euler.construction.ConstructedDiagramPanel;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawer;
import euler.drawers.DiagramDrawerPlanar;
import euler.drawers.DiagramDrawerPlanarForceTriangulation;
import euler.drawers.DiagramDrawerPlanarForceWithDialog;
import euler.drawers.DiagramDrawerTriangulationForceWithoutEmpty;
import euler.utilities.DiagramUtility;
import euler.utilities.DiagramUtilityAttemptToConnect;
import euler.utilities.DiagramUtilityCheckHamiltonCycle;
import euler.utilities.DiagramUtilityDiagramData;
import euler.utilities.DiagramUtilityFaceEdgeAddition;
import euler.utilities.DiagramUtilityFaceEdgeRemoval;
import euler.utilities.DiagramUtilityPlanarFromNonPlanar;
import euler.utilities.DiagramUtilityRandomAtomicDiagram;
import euler.utilities.DiagramUtilityRandomEulerDiagram;
import euler.utilities.DiagramUtilityRandomWellformedDiagram;
import euler.utilities.DiagramUtilityRenameDisconnectedContours;
import euler.utilities.DiagramUtilityResetConcreteDiagram;
import euler.views.DiagramView;
import euler.views.DiagramViewCycleItemsDisplayed;
import euler.views.DiagramViewFitDiagramInWindow;

public class ForceDirectedWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 600;
	public static int HEIGHT = 600;
	public static String DEFAULT_WIN_TITLE = "Force Directed Editor ForceDirectedWindow.java"; 

	protected ConstructedDiagramPanel constructedDiagramPanel;
	protected ConcreteDiagram concreteDiagram;
	protected File currentFile = null;
	protected File startDirectory;
	
	protected int width = WIDTH;
	protected int height = HEIGHT;
	protected String winTitle = DEFAULT_WIN_TITLE;
	
	protected DiagramPanel diagramPanel = null; //

	
	
	public static void main(String[] args) {
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
	    DiagramPanel dp = new DiagramPanel(); //dg);
		
		DiagramUtilityRandomWellformedDiagram wfd = new DiagramUtilityRandomWellformedDiagram(3);
		wfd.setDiagramPanel(dp);
		wfd.apply();
		
		
		ConcreteDiagram cd = dp.getConcreteDiagram();
		//ArrayList<ConcreteContour> ccs = cd.getConcreteContours();

		
		//DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
		//
		//dg.randomizeNodePoints(new Point(50,50),400,400);
		
		//dg.setConnectivityRemovableUnRemovableEdges();
		
		ForceDirectedWindow dw = new ForceDirectedWindow(cd);
		ConstructedDiagramPanel cdp = dw.getConstructedDiagramPanel();
		cdp.setShowEdgeDirection(false);
		cdp.setShowEdgeLabel(false);
		cdp.setShowGraph(false);
		cdp.setShowContour(true);
		cdp.setShowContourLabel(true);
		cdp.setShowTriangulation(false);
		cdp.setJiggleLabels(false);
		
		cdp.setShowEdgeLabel(true);
		cdp.setJiggleLabels(false);
		//dp.setShowGraph(false); 
		cdp.setShowRegion(false);
		cdp.setShowContour(true);
		cdp.setShowTriangulation(false);
		cdp.setShowEdgeLabel(false);
		cdp.setShowContourLabel(true);
		cdp.setShowContourAreas(false);
		cdp.setOptimizeContourAngles(false);
		cdp.setOptimizeMeetingPoints(false);
		cdp.setFitCircles(false);
		
		//dw.getDiagramPanel().setForceNoRedraw(true);

		
		//diagramDrawerForceDirected.setDualGraphWindowgramWindow(dw);
		//diagramDrawerForceDirected.setDiagramPanel(dp);
		
		//ESE_rep_ext2 diagramDrawerForceDirected = (ESE_rep_ext2) dw.getDiagramPanel().getDiagramDrawerList().get(1); //= new ESE_rep_ext2();
		//diagramDrawerForceDirected.setConcreteContours(ccs);
		//diagramDrawerForceDirected.setDiagramPanel(dp);
		
		//dp.addDiagramDrawer(diagramDrawerForceDirected);
		//dw.initMenu();   //can do it with cdw.actionPerformed(event); instead and => can reset initMenu to private? 
		cdp.requestFocus();
		
		cdp.update(cdp.getGraphics());
		
	}


	/// Constructors 
	
	public ForceDirectedWindow(ConstructedDiagramPanel inConstructedDiagramPanel) {
		super(DEFAULT_WIN_TITLE);
		setConstructedDiagramPanel(inConstructedDiagramPanel);
		setupWindow();
	}
	
	public ForceDirectedWindow(ConstructedConcreteDiagram inConstructedConcreteDiagram) {
		super(DEFAULT_WIN_TITLE);
		//concreteDiagram = new ConcreteDiagram(inConstructedConcreteDiagram.getConcreteContours());
		setConstructedDiagramPanel(new ConstructedDiagramPanel(inConstructedConcreteDiagram, this));
		setupWindow();
	}
	
	public ForceDirectedWindow(ConcreteDiagram inConcreteDiagram) {
		super(DEFAULT_WIN_TITLE);
		concreteDiagram = inConcreteDiagram;
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram("", concreteDiagram.getConcreteContours());
		setConstructedDiagramPanel(new ConstructedDiagramPanel(ccd, this));
	}
	/*
	public ForceDirectedWindow(AbstractDiagram inAbstractDiagram) {
		super(DEFAULT_WIN_TITLE);
		setConstructedDiagramPanel(new ConcreteDiagram(new DualGraph(inAbstractDiagram)));
		setupWindow();
	}	
	*/
	
	public void setupWindow() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);

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

		constructedDiagramPanel.requestFocus();
	}
	
	
	/// Properties 
	
	public void setConstructedDiagramPanel(ConstructedDiagramPanel inConstructedDiagramPanel){
		constructedDiagramPanel = inConstructedDiagramPanel;
		getContentPane().add(constructedDiagramPanel);
	}/*
	public void setConstructedDiagramPanel(ConstructedConcreteDiagram inConstructedConcreteDiagram){
		constructedDiagramPanel = inConstructedConcreteDiagram;
		getContentPane().add(constructedDiagramPanel."", inConstructedConcreteDiagram));
	}
	public void setConstructedDiagramPanel(ConstructedConcreteDiagram inConstructedConcreteDiagram){
		constructedDiagramPanel = inConstructedConcreteDiagram;
		getContentPane().add(constructedDiagramPanel);
	}*/
	public ConstructedDiagramPanel getConstructedDiagramPanel(){ 
		return constructedDiagramPanel;
	}
	
	public void setConcreteDiagram (ConcreteDiagram inConcreteDiagram){ 
		concreteDiagram = inConcreteDiagram;
	}
	public ConcreteDiagram getConcreteDiagram (){
		return concreteDiagram;
	}
	
	
	
	// Private Methods 

	private void initView() {
		diagramPanel.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_C, "Cycle Items Displayed",KeyEvent.VK_C));
		diagramPanel.addDiagramView(new DiagramViewFitDiagramInWindow(KeyEvent.VK_F, "Fit Diagram In Window",KeyEvent.VK_F));
	}

	
	private void initUtility() {
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_2, "Create Atomic 2 Set Euler Dual",KeyEvent.VK_2,2,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_3, "Create Atomic 3 Set Euler Dual",KeyEvent.VK_3,3,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_4, "Create Atomic 4 Set Euler Dual",KeyEvent.VK_4,4,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_5, "Create Atomic 5 Set Euler Dual",KeyEvent.VK_5,5,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_6, "Create Atomic 6 Set Euler Dual",KeyEvent.VK_6,6,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_7, "Create Atomic 7 Set Euler Dual",KeyEvent.VK_7,7,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_8, "Create Atomic 8 Set Euler Dual",KeyEvent.VK_8,8,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_9, "Create Atomic 9 Set Euler Dual",KeyEvent.VK_9,9,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityDiagramData(KeyEvent.VK_D, "Diagram Data",KeyEvent.VK_D));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F6, "Create Wellformed 2 Set Euler Dual",KeyEvent.VK_F6,2,false));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F7, "Create Wellformed 3 Set Euler Dual",KeyEvent.VK_F7,3, false));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F8, "Create Wellformed 4 Set Euler Dual",KeyEvent.VK_F8,4,false));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F9, "Create Wellformed 5 Set Euler Dual",KeyEvent.VK_F9,5,true));
		diagramPanel.addDiagramUtility(new DiagramUtilityRandomEulerDiagram(KeyEvent.VK_C, "Create Random 5 Set Euler Dual",KeyEvent.VK_C,5));
		diagramPanel.addDiagramUtility(new DiagramUtilityResetConcreteDiagram(KeyEvent.VK_R, "Reset Concrete Diagram",KeyEvent.VK_R));
		diagramPanel.addDiagramUtility(new DiagramUtilityFaceEdgeRemoval(KeyEvent.VK_U,"Remove All Poly Edges",KeyEvent.VK_U));

		diagramPanel.addDiagramUtility(new DiagramUtilityPlanarFromNonPlanar(KeyEvent.VK_F1, "Find Best wf Planar Dual",KeyEvent.VK_F1));
		diagramPanel.addDiagramUtility(new DiagramUtilityAttemptToConnect(KeyEvent.VK_F2, "Attempt To Connect Disconnected Components",KeyEvent.VK_F2));
		diagramPanel.addDiagramUtility(new DiagramUtilityFaceEdgeAddition(KeyEvent.VK_F3, "Add Edges to Faces",KeyEvent.VK_F3));
		diagramPanel.addDiagramUtility(new DiagramUtilityRenameDisconnectedContours(KeyEvent.VK_F4, "Rename Disconnected Contours",KeyEvent.VK_F4));
		diagramPanel.addDiagramUtility(new DiagramUtilityCheckHamiltonCycle(KeyEvent.VK_F5, "Check Hamilton Cycle",KeyEvent.VK_F5));
	}


	private void initLayout() {
		//diagramPanel.addDiagramDrawer(new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout", KeyEvent.VK_P,this.getDiagramPanel()));
		//diagramPanel.addDiagramDrawer(new ESE_rep_ext2(this.getDiagramPanel()));
		diagramPanel.addDiagramDrawer(new DiagramDrawerPlanarForceWithDialog(KeyEvent.VK_E, "Spring Embedder with Edge Force",KeyEvent.VK_E));
		diagramPanel.addDiagramDrawer(new DiagramDrawerPlanarForceTriangulation(KeyEvent.VK_T, "Spring Embedder on Triangulated Graph",KeyEvent.VK_T));
		diagramPanel.addDiagramDrawer(new DiagramDrawerTriangulationForceWithoutEmpty(KeyEvent.VK_0, "No Empty set, Spring Embedder on Triangulated Graph",KeyEvent.VK_0));
		
	//	gp.addGraphDrawer(new GraphDrawerPlanarForce());
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

		JMenuItem fileOpenItem = new JMenuItem("Open...",KeyEvent.VK_O);
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(fileOpenItem);

		//JMenuItem fileOpenAdjacencyItem = new JMenuItem("Open Adjacency File...");
		//fileMenu.add(fileOpenAdjacencyItem);

		//JMenuItem fileOpenWeightedAdjacencyItem = new JMenuItem("Open Weighted Adjacency File...");
		//fileMenu.add(fileOpenWeightedAdjacencyItem);

		//JMenuItem fileOpenXMLItem = new JMenuItem("Open XML File...");
		//fileMenu.add(fileOpenXMLItem);

		JMenuItem fileSaveItem = new JMenuItem("Save",KeyEvent.VK_S);
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveItem);

		JMenuItem fileSaveAsItem = new JMenuItem("Save As...");
		fileMenu.add(fileSaveAsItem);

		//JMenuItem fileSaveSimpleItem = new JMenuItem("Save Simple Graph...");
		//fileMenu.add(fileSaveSimpleItem);

		//JMenuItem fileSaveXMLItem = new JMenuItem("Save XML File...");
		//fileMenu.add(fileSaveXMLItem);

		JMenuItem filePNGItem = new JMenuItem("Export to png");
		fileMenu.add(filePNGItem);

		JMenuItem fileExitItem = new JMenuItem("Exit",KeyEvent.VK_X);
		fileExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		fileMenu.add(fileExitItem);

		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileExit();
			}
		});
		
		fileNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileNew();
			}
		});

		fileOpenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpen();
			}
		});

		/*
		fileOpenAdjacencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenAdjacency();
			}
		});
		

		fileOpenWeightedAdjacencyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenWeightedAdjacency();
			}
		});

		fileOpenXMLItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpenXML();
			}
		});
		*/
		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSave();
			}
		});

		fileSaveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});
		
		/*
		fileSaveSimpleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveSimple();
			}
		});

		fileSaveXMLItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveXML();
			}
		});
		 */
		filePNGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filePNG();
			}
		});
		

// Edit Menu
		JMenu editMenu = new JMenu("Edit");

		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);
	
		JMenuItem editNodesItem = new JMenuItem("Edit Selected Nodes...",KeyEvent.VK_N);
		editNodesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.SHIFT_MASK));
		editMenu.add(editNodesItem);

		JMenuItem editEdgesItem = new JMenuItem("Edit Selected Edges...",KeyEvent.VK_E);
		editEdgesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.SHIFT_MASK));
		editMenu.add(editEdgesItem);

		JMenuItem editEdgeTypesItem = new JMenuItem("Edit Edge Types...");
		editMenu.add(editEdgeTypesItem);

		JMenuItem editNodeTypesItem = new JMenuItem("Edit Node Types...");
		editMenu.add(editNodeTypesItem);

//		JMenuItem editMoveGraphItem = new JMenuItem("Move Graph...");
//		editMenu.add(editMoveGraphItem);

		JMenuItem editAddEdgeBendItem = new JMenuItem("Add Edge Bend");
		editMenu.add(editAddEdgeBendItem);

		JMenuItem editRemoveEdgeBendsItem = new JMenuItem("Remove Edge Bends");
		editMenu.add(editRemoveEdgeBendsItem);

		JMenuItem editSelectAllItem = new JMenuItem("Select All",KeyEvent.VK_A);
		editSelectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		editMenu.add(editSelectAllItem);

		editNodesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.editNodes(diagramPanel.getSelection().getNodes());
			}
		});
		
		editEdgesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.editEdges(diagramPanel.getSelection().getEdges());
			}
		});

		editEdgeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.editEdgeTypes();
			}
		});

		editNodeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.editNodeTypes();
			}
		});

/*		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.moveGraph();
			}
		});
*/
		editAddEdgeBendItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.addEdgeBend();
			}
		});

		editRemoveEdgeBendsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				diagramPanel.removeEdgeBends();
			}
		});

		editSelectAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editSelectAll();
			}
		});


//		 View Menu
		JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(viewMenu);

		for(DiagramView v : diagramPanel.getDiagramViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			viewMenu.add(menuItem);
		}


// Utilities Menu
		JMenu utilitiesMenu = new JMenu("Utilities");
        utilitiesMenu.setMnemonic(KeyEvent.VK_U);
		menuBar.add(utilitiesMenu);

		for(DiagramUtility u : diagramPanel.getDiagramUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			utilitiesMenu.add(menuItem);
		}


		JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(layoutMenu);

		for(DiagramDrawer d : diagramPanel.getDiagramDrawerList()) {
	        JMenuItem menuItem = new JMenuItem(d.getMenuText(), d.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(d.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			layoutMenu.add(menuItem);
		}
		
	}

	protected void fileNew() {
		if (currentFile != null) {
			if (!currentFile.isDirectory()) {
				currentFile = currentFile.getParentFile();
			}
		}
		diagramPanel.getDualGraph().clear();
		diagramPanel.resetDiagram();
		diagramPanel.update(diagramPanel.getGraphics());
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
			diagramPanel.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			DualGraph dg = new DualGraph();
			dg.loadAll(currentFile);
			diagramPanel.setDualGraph(dg);
			diagramPanel.resetDiagram();
			diagramPanel.update(diagramPanel.getGraphics());
		}
	}
	
	protected void fileOpenAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			diagramPanel.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			diagramPanel.getDualGraph().loadAdjacencyFile(currentFile.getAbsolutePath());
			diagramPanel.update(diagramPanel.getGraphics());
		}
	}


	protected void fileOpenWeightedAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			diagramPanel.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			diagramPanel.getDualGraph().loadWeightedAdjacencyFile(currentFile.getAbsolutePath());
			//diagramPanel.getDualGraph().randomizeNodePoints(new Point(50,50),400,400);
			diagramPanel.update(diagramPanel.getGraphics());
		}
	}



	protected void fileSave() {
		if (currentFile == null) {
			fileSaveAs();
		} else {
			if (currentFile.isDirectory()) {
				fileSaveAs();
			} else {
				diagramPanel.resetDiagram();
				diagramPanel.getDualGraph().saveAll(currentFile);
				diagramPanel.update(diagramPanel.getGraphics());
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
			diagramPanel.resetDiagram();
			diagramPanel.getDualGraph().saveAll(currentFile);
		}
	}


	protected void fileSaveSimple() {
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
			diagramPanel.resetDiagram();
			diagramPanel.getDualGraph().saveSimple(currentFile);
		}
	}




	protected void filePNG() {

		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			pngFile = new File(currentFile.getName()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}

		if (pngFile == null){
			//return;
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentFile = chooser.getSelectedFile();
				pngFile = new File(currentFile.getName()+".png");
				chooser = new JFileChooser(pngFile);
				if (!currentFile.isDirectory()) {
					chooser.setSelectedFile(currentFile);
				}
			}
		}
		try {
			BufferedImage image = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
			paint(image.getGraphics());
			ImageIO.write(image,"png",pngFile);
		} catch (Exception e) {}
		return;
/*		JFileChooser chooser = null;
		File pngFile = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			pngFile = new File(currentFile.getName()+".png");
			chooser = new JFileChooser(pngFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			}
		}
		int returnVal = chooser.showSaveDialog(gw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File pngFile = chooser.getSelectedFile();

			int maxX = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;

			while(Node node : getNodes()) {
				if(node.getX() > maxX) {
					maxX = node.getX();
				}
				if(node.getX() < minX) {
					minX = node.getX();
				}
				if(node.getY() > maxY) {
					maxY = node.getY();
				}
				if(node.getY() < minY) {
					minY = node.getY();
				}
			}

			BufferedImage image = new BufferedImage(maxX-minX+50,maxY-minY+50,BufferedImage.TYPE_INT_RGB);



			ImageIO.write(image,"png",pngFile);
		}
*/	}


	protected void fileExit() {
		System.exit(0);
	}


	protected void editSelectAll() {
		diagramPanel.getSelection().addNodes(diagramPanel.getDualGraph().getNodes());
		diagramPanel.getSelection().addEdges(diagramPanel.getDualGraph().getEdges());
		diagramPanel.repaint();
	}
	
	
	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(DiagramView v : diagramPanel.getDiagramViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}
		for(DiagramDrawer d : diagramPanel.getDiagramDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(DiagramUtility u : diagramPanel.getDiagramUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.apply();
				return;
			}
		}

	}
	
}
