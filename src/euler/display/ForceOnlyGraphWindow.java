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


/**
 * Restricted Diagram layout window using DiagramPanel.
 * Only intended for node-edge force testing.
 */
public class ForceOnlyGraphWindow extends JFrame implements ActionListener {

	protected DiagramPanel dp = null;
	protected ForceOnlyGraphWindow dgw = null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = 600;
	protected int height = 600;

	public GeneralXML generalXML;
	
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {
		DualGraph dg = new DualGraph();
		ForceOnlyGraphWindow dgw = new ForceOnlyGraphWindow(dg);
		DiagramPanel gp = dgw.getDiagramPanel();
		dgw.dp = dgw.getDiagramPanel();
		dgw.dp.setShowEdgeDirection(false);
		dgw.dp.setShowEdgeLabel(true);
		dgw.dp.setShowContour(false);
		dgw.dp.setShowGraph(true);
		
	}
		

	public ForceOnlyGraphWindow(DualGraph dg) {
		super("Graph Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);

		dgw = this;
		
		generalXML = new GeneralXML(dg);

		dp = new DiagramPanel(dg,this);
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

		dp.requestFocus();		
		setVisible(true);

	}


	public DiagramPanel getDiagramPanel() {return dp;}


	private void initView() {
		dp.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_C, "Cycle Items Displayed",KeyEvent.VK_C));
	}

	
	private void initUtility() {
		dp.addDiagramUtility(new DiagramUtilityRandomEulerDiagram(KeyEvent.VK_R, "Create Random 4 Set Euler Dual",KeyEvent.VK_R,4));
		dp.addDiagramUtility(new DiagramUtilityRandomWellformedDiagram(KeyEvent.VK_W, "Create Wellformed 4 Set Euler Dual",KeyEvent.VK_W,4,false));
		dp.addDiagramUtility(new DiagramUtilityCheckHamiltonCycle(KeyEvent.VK_F5, "Check Hamilton Cycle",KeyEvent.VK_F5));
		dp.addDiagramUtility(new DiagramUtilityCheckAllClosedPath(KeyEvent.VK_B, "Check All Closed Path",KeyEvent.VK_B));
		
	}


	private void initLayout() {

		//dp.addDiagramDrawer(new DiagramDrawerPlanar(KeyEvent.VK_P, "Planar Layout", KeyEvent.VK_P,this.getDiagramPanel()));
		dp.addDiagramDrawer(new DiagramDrawerPlanarForceWithDialog(KeyEvent.VK_E, "Spring Embedder with Edge Force",KeyEvent.VK_E));
		
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

		JMenuItem editMoveGraphItem = new JMenuItem("Move Graph...");
		editMenu.add(editMoveGraphItem);

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
			dp.getDualGraph().loadAll(currentFile);
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
				repaint();
				return;
			}
		}

	}

}



