package euler.inductive;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import pjr.graph.*;
import euler.*;
import euler.drawers.*;
import euler.utilities.*;
import euler.views.*;


/** Graph layout window using GraphPanel */
public class InductiveWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 750;
	public static int HEIGHT = 750;
	protected DiagramPanel dp = null;
	protected HybridGraph hybridGraph = null;
	protected InductiveWindow iw = null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = WIDTH;
	protected int height = HEIGHT;
	protected boolean simpleVersion = false;

	public GeneralXML generalXML;
	
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {

DualGraph dg = new DualGraph();
HybridGraph hg = new HybridGraph(dg);
new InductiveWindow("Hybrid View",hg.clone(),true);
		
/*		
		DualGraph dg0 = new DualGraph();
		HybridGraph hg0 = new HybridGraph(dg0);
		ArrayList<String> splitZones = new ArrayList<String>();
		ArrayList<String> containedZones = new ArrayList<String>();
		splitZones.add("O");
		ArrayList<Edge> edgePath = hg0.findSimplePath(splitZones, containedZones,0);
		DualGraph eg1 = hg0.eulerGraphWithEdgePath("a", edgePath);
		
		HybridGraph hg1 = new HybridGraph(eg1);
//		new InductiveWindow(hg1.clone());
		
		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		splitZones.add("O");
		splitZones.add("a");
		edgePath = hg1.findSimplePath(splitZones, containedZones,0);
		DualGraph eg2 = hg1.eulerGraphWithEdgePath("b", edgePath);
		
		HybridGraph hg2 = new HybridGraph(eg2);
		new InductiveWindow(hg2.clone());
*/
	}
	
	
	public void setDualGraph(HybridGraph hg){ iw = new InductiveWindow(hg);}
	
	public InductiveWindow(HybridGraph hg) {
		super("Inductive Euler Diagram Generation InductiveWindow.java");
		this.simpleVersion = false;
		setup(hg);
	}
	
	public InductiveWindow(String title, HybridGraph hg) {
		super(title);
		this.simpleVersion = false;
		setup(hg);
	}
	
	public InductiveWindow(String title, HybridGraph hg, boolean simpleVersion) {
		super(title);
		this.simpleVersion = simpleVersion;
		setup(hg);
	}
	
	protected void setup(HybridGraph hg) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);

		iw = this;
		
		generalXML = new GeneralXML(hg);
		hybridGraph = hg;
		dp = new DiagramPanel(hybridGraph,this);
		
		getContentPane().add(dp);

		if(simpleVersion) {
			initSimpleVersion();
		} else {
			initView();
			initUtility();
			initLayout();
			initMenu();
		}

		setSize(width,height);

		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);

		setVisible(true);
		dp.requestFocus();
	}


	public HybridGraph getHybridGraph() {return hybridGraph;}
	public DiagramPanel getDiagramPanel() {return dp;}

	public void setDiagramPanel(DiagramPanel inDP) {dp = inDP;}
	
	public void setHybridGraph(HybridGraph hg) {
		hybridGraph = hg;
		dp.setDualGraph(hybridGraph);
		dp.resetDiagram();
		repaint();
	}


	private void initView() {
		dp.addDiagramView(new DiagramViewShowEulerGraph(KeyEvent.VK_E, "Show Euler Graph",KeyEvent.VK_E,this));
	}

	
	private void initUtility() {
		dp.addDiagramUtility(new DiagramUtilityAddPath(KeyEvent.VK_A, "Add Path",KeyEvent.VK_A,this));
		dp.addDiagramUtility(new DiagramUtilityFindPath(KeyEvent.VK_F, "Find Path",KeyEvent.VK_F,this));
	}


	private void initLayout() {
		dp.addDiagramDrawer(new DiagramDrawerPlanarForce(KeyEvent.VK_P, "Spring Embedder with Edge Force", KeyEvent.VK_P));
		dp.addDiagramDrawer(new DiagramDrawerPlanarForceWithDialog(KeyEvent.VK_Q, "Dialog Spring Embedder with Edge Force",KeyEvent.VK_E));
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

		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph();
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
	
	private void initSimpleVersion() {
		
		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

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

		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph();
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

		dp.addDiagramUtility(new DiagramUtilitySimpleFindPath(KeyEvent.VK_F, "Find Path",KeyEvent.VK_F,this));
		dp.addDiagramDrawer(new DiagramDrawerHybridForce(KeyEvent.VK_S, "Spring Embedder", KeyEvent.VK_S, hybridGraph));

// Actions Menu
		JMenu actionMenu = new JMenu("Actions");
        actionMenu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(actionMenu);
		
		for(DiagramUtility u : dp.getDiagramUtilityList()) {
	        JMenuItem menuItem = new JMenuItem(u.getMenuText(),u.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(u.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			actionMenu.add(menuItem);
		}
		
		for(DiagramView v : dp.getDiagramViewList()) {
	        JMenuItem menuItem = new JMenuItem(v.getMenuText(),v.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(v.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			actionMenu.add(menuItem);
		}

		for(DiagramDrawer d : dp.getDiagramDrawerList()) {
	        JMenuItem menuItem = new JMenuItem(d.getMenuText(),d.getMnemonicKey());
			menuItem.setAccelerator(KeyStroke.getKeyStroke(d.getAcceleratorKey(),0));
			menuItem.addActionListener(this);
			actionMenu.add(menuItem);
		}


	}
	
	public void moveGraph() {
		new MoveHybridGraphFrame(dp,hybridGraph);
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
			
		int returnVal = chooser.showOpenDialog(iw);
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
			
		int returnVal = chooser.showOpenDialog(iw);
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
			
		int returnVal = chooser.showOpenDialog(iw);
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
			
		int returnVal = chooser.showOpenDialog(iw);
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
		int returnVal = chooser.showSaveDialog(iw);
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
		int returnVal = chooser.showSaveDialog(iw);
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
		int returnVal = chooser.showSaveDialog(iw);
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



