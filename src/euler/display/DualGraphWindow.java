package euler.display;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.image.*;
import pjr.graph.*;
import euler.*;
import euler.drawers.*;
import euler.utilities.*;
import euler.views.*;


/** Graph layout window using GraphPanel */
public class DualGraphWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 750;
	public static int HEIGHT = 600;

	protected DiagramPanel dp = null;
	protected DualGraph dualGraph = null;
	protected DualGraphWindow dgw = null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = WIDTH;
	protected int height = HEIGHT;

	public GeneralXML generalXML;
	
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {
		DualGraph dg = new DualGraph(AbstractDiagram.VennFactory(3));
		
		dg.randomizeNodePoints(new Point(50,50),400,400);
		
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
		dw.getDiagramPanel().fitGraphInPanel();
		dw.getDiagramPanel().setForceNoRedraw(false);
		dw.getDiagramPanel().update(dw.getDiagramPanel().getGraphics());
	}
	
	public DualGraphWindow(DiagramPanel dp) {
		super("Diagram Editor DualGraphWindow.java v 0.3");
		this.dp = dp;
		dualGraph = dp.getDualGraph();
		setup(dp.getDualGraph());
	}
	
	public DualGraphWindow(DualGraph dg) {
		super("Diagram Editor DualGraphWindow.java v 0.3");
		setup(dg);
	}
	
	public DualGraphWindow(String title, DualGraph dg) {
		super(title);
		setup(dg);
	}
	
	public void setDualGraph(DualGraph dg) {
		dualGraph = dg;
		dp.setDualGraph(dg);
	}
	
	
	public void setup(DualGraph dg) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);

		dgw = this;
		
		generalXML = new GeneralXML(dg);
		dualGraph = dg;
		if(dp == null) {
			dp = new DiagramPanel(dualGraph,this);
		}
		
		getContentPane().add(dp);

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

		dp.requestFocus();
	}


	public DualGraph getDualGraph() {return dp.getDualGraph();}
	public DiagramPanel getDiagramPanel() {return dp;}

	public void setDiagramPanel(DiagramPanel inDP) {dp = inDP;}

	private void initView() {
		dp.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_C, "Cycle Items Displayed",KeyEvent.VK_C));
		dp.addDiagramView(new DiagramViewFitDiagramInWindow(KeyEvent.VK_F, "Fit Diagram In Window",KeyEvent.VK_F));
	}

	
	private void initUtility() {
		dp.addDiagramUtility(new DiagramUtilityMergeSetsForConcurrency(KeyEvent.VK_M, "Merge Sets For Concurrency",KeyEvent.VK_M));
		dp.addDiagramUtility(new DiagramUtilityJsonOutput(KeyEvent.VK_J, "Output Json",KeyEvent.VK_J));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_2, "Create Atomic 2 Set Euler Dual",KeyEvent.VK_2,2,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_3, "Create Atomic 3 Set Euler Dual",KeyEvent.VK_3,3,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_4, "Create Atomic 4 Set Euler Dual",KeyEvent.VK_4,4,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_5, "Create Atomic 5 Set Euler Dual",KeyEvent.VK_5,5,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_6, "Create Atomic 6 Set Euler Dual",KeyEvent.VK_6,6,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_7, "Create Atomic 7 Set Euler Dual",KeyEvent.VK_7,7,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_8, "Create Atomic 8 Set Euler Dual",KeyEvent.VK_8,8,true));
		dp.addDiagramUtility(new DiagramUtilityRandomAtomicDiagram(KeyEvent.VK_9, "Create Atomic 9 Set Euler Dual",KeyEvent.VK_9,9,true));
		dp.addDiagramUtility(new DiagramUtilityDiagramData(KeyEvent.VK_D, "Diagram Data",KeyEvent.VK_D));
		dp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F6, "Create Wellformed 2 Set Euler Dual",KeyEvent.VK_F6,2,true));
		dp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F7, "Create Wellformed 3 Set Euler Dual",KeyEvent.VK_F7,3,true));
		dp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F8, "Create Wellformed 4 Set Euler Dual",KeyEvent.VK_F8,4,true));
		dp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_F9, "Create Wellformed 5 Set Euler Dual",KeyEvent.VK_F9,5,true));
		dp.addDiagramUtility(new DiagramUtilityRandomEulerDiagram(KeyEvent.VK_C, "Create Random 5 Set Euler Dual",KeyEvent.VK_C,5));
		dp.addDiagramUtility(new DiagramUtilityResetConcreteDiagram(KeyEvent.VK_R, "Reset Concrete Diagram",KeyEvent.VK_R));
		dp.addDiagramUtility(new DiagramUtilityFaceEdgeRemoval(KeyEvent.VK_U,"Remove All Poly Edges",KeyEvent.VK_U));

		dp.addDiagramUtility(new DiagramUtilityPlanarFromNonPlanar(KeyEvent.VK_F1, "Find Best wf Planar Dual",KeyEvent.VK_F1));
		dp.addDiagramUtility(new DiagramUtilityAttemptToConnect(KeyEvent.VK_F2, "Attempt To Connect Disconnected Components",KeyEvent.VK_F2));
		dp.addDiagramUtility(new DiagramUtilityFaceEdgeAddition(KeyEvent.VK_F3, "Add Edges to Faces",KeyEvent.VK_F3));
		dp.addDiagramUtility(new DiagramUtilityRenameDisconnectedContours(KeyEvent.VK_F4, "Rename Disconnected Contours",KeyEvent.VK_F4));
		dp.addDiagramUtility(new DiagramUtilityCheckHamiltonCycle(KeyEvent.VK_F5, "Check Hamilton Cycle",KeyEvent.VK_F5));
		
		
	}


	private void initLayout() {
		dp.addDiagramDrawer(new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout", KeyEvent.VK_P,this.getDiagramPanel()));
		//dp.addDiagramDrawer(new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout", KeyEvent.VK_P, this.getDiagramPanel()));
		dp.addDiagramDrawer(new DiagramDrawerPlanarForceWithDialog(KeyEvent.VK_E, "Spring Embedder with Edge Force",KeyEvent.VK_E));
		dp.addDiagramDrawer(new DiagramDrawerPlanarForceTriangulation(KeyEvent.VK_T, "Spring Embedder on Triangulated Graph",KeyEvent.VK_T));
		dp.addDiagramDrawer(new DiagramDrawerTriangulationForceWithoutEmpty(KeyEvent.VK_0, "No Empty set, Spring Embedder on Triangulated Graph",KeyEvent.VK_0));
		dp.addDiagramDrawer(new DiagramDrawerSpringEmbedder(KeyEvent.VK_B, "Basic Spring Embedder, select nodes to fix",KeyEvent.VK_B,false));
		
	//	gp.addGraphDrawer(new GraphDrawerPlanarForce());
	}

	private void initMenu() {
	
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

		//JMenuItem filePNGItem = new JMenuItem("Export to png");
		//fileMenu.add(filePNGItem);

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

		filePNGItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				filePNG();
			}
		});
		*/

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
				dp.editNodes(dp.getSelection().getNodes());
			}
		});
		
		editEdgesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.editEdges(dp.getSelection().getEdges());
			}
		});

		editEdgeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.editEdgeTypes();
			}
		});

		editNodeTypesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.editNodeTypes();
			}
		});

/*		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.moveGraph();
			}
		});
*/
		editAddEdgeBendItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.addEdgeBend();
			}
		});

		editRemoveEdgeBendsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dp.removeEdgeBends();
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

		for(DiagramView v : dp.getDiagramViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			viewMenu.add(menuItem);
		}


// Utilities Menu
		JMenu utilitiesMenu = new JMenu("Utilities");
        utilitiesMenu.setMnemonic(KeyEvent.VK_U);
		menuBar.add(utilitiesMenu);

		for(DiagramUtility u : dp.getDiagramUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			utilitiesMenu.add(menuItem);
		}


		JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(layoutMenu);

		for(DiagramDrawer d : dp.getDiagramDrawerList()) {
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
		dp.getDualGraph().clear();
		dp.resetDiagram();
		dp.update(dp.getGraphics());
	}

	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dp.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			DualGraph dg = new DualGraph();
			dg.loadAll(currentFile);
			dp.setDualGraph(dg);
			dp.resetDiagram();
			dp.update(dp.getGraphics());
		}
	}
	
	protected void fileOpenAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dp.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			dp.getDualGraph().loadAdjacencyFile(currentFile.getAbsolutePath());
			dp.update(dp.getGraphics());
		}
	}


	protected void fileOpenWeightedAdjacency() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dp.getDualGraph().clear();
			currentFile = chooser.getSelectedFile();
			dp.getDualGraph().loadWeightedAdjacencyFile(currentFile.getAbsolutePath());
			//dp.getDualGraph().randomizeNodePoints(new Point(50,50),400,400);
			dp.update(dp.getGraphics());
		}
	}

	protected void fileOpenXML() {
		generalXML = new GeneralXML(dp.getDualGraph());

		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			generalXML.loadGraph(currentFile);
		}
		dp.update(dp.getGraphics());

	}


	protected void fileSave() {
		if (currentFile == null) {
			fileSaveAs();
		} else {
			if (currentFile.isDirectory()) {
				fileSaveAs();
			} else {
				dp.resetDiagram();
				dp.getDualGraph().saveAll(currentFile);
				dp.update(dp.getGraphics());
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
		int returnVal = chooser.showSaveDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			dp.resetDiagram();
			dp.getDualGraph().saveAll(currentFile);
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
		int returnVal = chooser.showSaveDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			dp.resetDiagram();
			dp.getDualGraph().saveSimple(currentFile);
		}
	}

	protected void fileSaveXML() {
		generalXML = new GeneralXML(dp.getDualGraph());
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
		int returnVal = chooser.showSaveDialog(dgw);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			generalXML.saveGraph(currentFile);
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

		if (pngFile == null)
			return;
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
		dp.getSelection().addNodes(dp.getDualGraph().getNodes());
		dp.getSelection().addEdges(dp.getDualGraph().getEdges());
		dp.repaint();
	}


	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(DiagramView v : dp.getDiagramViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}
		for(DiagramDrawer d : dp.getDiagramDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(DiagramUtility u : dp.getDiagramUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.apply();
//				repaint();
				return;
			}
		}

	}


}



