package euler.inductive;

import javax.imageio.*;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.*;
import pjr.graph.*;
import euler.*;
import euler.drawers.*;
import euler.utilities.*;
import euler.views.*;


/** Graph layout window using GraphPanel */
public class EulerGraphWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 750;
	public static int HEIGHT = 750;

	protected DiagramPanel dp = null;
	protected DualGraph dualGraph = null;
	protected EulerGraphWindow dgw = null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = WIDTH;
	protected int height = HEIGHT;

	public GeneralXML generalXML;
	
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {
		HybridGraph hg;
		DualGraph eg;
		ArrayList<Edge> edgePath;
		ArrayList<String> splitZones;
		ArrayList<String> containedZones;

		hg = new HybridGraph(new DualGraph());
		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		splitZones.add("O");
		edgePath = hg.findSimplePath(splitZones, containedZones, 0, false);
		eg = hg.eulerGraphWithEdgePath("a", edgePath);
//System.out.println(edgePath);
		hg = new HybridGraph(eg);
//		new InductiveWindow("Hybrid Graph a",hg.clone());
		
		
		splitZones = new ArrayList<String>();
		containedZones = new ArrayList<String>();
		splitZones.add("a");
		splitZones.add("O");
		edgePath = hg.findSimplePath(splitZones, containedZones, 0, false);
		eg = hg.eulerGraphWithEdgePath("b", edgePath);
		new EulerGraphWindow("Euler Graph 0 a b ab",eg);
	}
	
	
	public EulerGraphWindow(String title, DualGraph dg) {
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
		
		dp.setParallelEdgeOffset(0);
		
		getContentPane().add(dp);
		
		init();

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

	private void init() {
		
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

		
		dp.addDiagramUtility(new DiagramUtilityGenerateHybridGraph(KeyEvent.VK_F, "Find HybridGraph",KeyEvent.VK_F,this));
		dp.addDiagramDrawer(new DiagramDrawerEulerGraphForce(KeyEvent.VK_S, "Spring Embedder", KeyEvent.VK_S));

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



