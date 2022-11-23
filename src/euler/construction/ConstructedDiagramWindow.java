package euler.construction;

import javax.imageio.*;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import pjr.graph.*;
import euler.*;
import euler.display.*;
import euler.drawers.*;
import euler.construction.*;
import euler.polygon.*;
import euler.utilities.*;
import euler.views.*;


/** Graph layout window using ConstructedDiagramPanel */
public class ConstructedDiagramWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 600;
	public static int HEIGHT = 600;

	ArrayList<ConcreteContour> concreteContours	= null;
	protected File currentFile = null;
	protected File startDirectory;
	protected int width = WIDTH;
	protected int height = HEIGHT;
	
	protected ConstructedDiagramPanel cdp = null;

	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) {

		ArrayList<Polygon> ps = new ArrayList<Polygon>();
		ArrayList<String> ss = new ArrayList<String>();
		Polygon p;
/*		
		p = new Polygon();
		p.addPoint(200, 100);
		p.addPoint(200, 300);
		p.addPoint(450, 300);
		ps.add(p);
		ss.add("a");
		
		p = new Polygon();
		p.addPoint(400, 100);
		p.addPoint(250, 200);
		p.addPoint(400, 200);
		ps.add(p);
		ss.add("b");
		
		p = new Polygon();
		p.addPoint(500, 400);
		p.addPoint(300, 250);
		p.addPoint(500, 250);
		ps.add(p);
		ss.add("c");
		
		p = new Polygon();
		p.addPoint(150, 100);
		p.addPoint(150, 200);
		p.addPoint(230, 150);
		ps.add(p);
		ss.add("d");
		
		p = new Polygon();
		p.addPoint(150, 220);
		p.addPoint(260, 250);
		p.addPoint(190, 400);
		ps.add(p);
		ss.add("e");	
		
		p = new Polygon();
		p.addPoint(470, 280);
		p.addPoint(580, 280);
		p.addPoint(560, 360);
		ps.add(p);
		ss.add("f");	
		
		p = new Polygon();
		p.addPoint(270, 220);
		p.addPoint(330, 220);
		p.addPoint(330, 280);
		p.addPoint(270, 280);
		ps.add(p);
		ss.add("g");
*/
		final int POINTS = 10;
		
		p = new Polygon();
		p = RegularPolygon.generateRegularPolygon(200, 200, 100, POINTS);
		ps.add(p);
		ss.add("a");

		p = new Polygon();
		p = RegularPolygon.generateRegularPolygon(300, 200, 100, POINTS);
		ps.add(p);
		ss.add("b");
		
		p = new Polygon();
		p = RegularPolygon.generateRegularPolygon(250, 270, 100, POINTS);
		ps.add(p);
		ss.add("c");
		
		ArrayList<ConcreteContour> ccs = new ArrayList<ConcreteContour>();
		Iterator<String> sI = ss.iterator();
		for(Polygon polygon : ps) {
			String s = sI.next();
			ccs.add(new ConcreteContour(s,polygon));
		}
		
		//dg.randomizeNodePoints(new Point(50,50),400,400);		
		//dg.setConnectivityRemovableUnRemovableEdges();
		
		ConstructedDiagramWindow cdw = new ConstructedDiagramWindow(" a b c ab bc ac abc", ccs);
		cdw.getConstructedDiagramPanel().setShowEdgeDirection(false);
		cdw.getConstructedDiagramPanel().setShowEdgeLabel(true);
		cdw.getConstructedDiagramPanel().setShowGraph(true);
		cdw.getConstructedDiagramPanel().setShowContour(true);
		cdw.getConstructedDiagramPanel().setShowContourLabel(true);
		cdw.getConstructedDiagramPanel().setShowTriangulation(false);
		
		
 
	}
	
	public ConstructedDiagramWindow(String abstractDescription, ArrayList<ConcreteContour> concreteContours) {
		super("Constructed Diagram Editor ConstructedDiagramWindow.java");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);
		
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(abstractDescription, concreteContours);

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

	public void initView() {
		cdp.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_C, "Cycle Items Displayed",KeyEvent.VK_C));
		cdp.addDiagramView(new DiagramViewFitDiagramInWindow(KeyEvent.VK_F, "Fit Diagram In Window",KeyEvent.VK_F));
	}

	
	public void initUtility() {
		cdp.addDiagramUtility(new DiagramUtilityGenerateNewFullDiagram(KeyEvent.VK_G, "New Full Diagram",KeyEvent.VK_G));
	}


	public void initLayout() {
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


//		 View Menu
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
				u.apply();
				return;
			}
		}

	}
	
	private void editClearAll() {
		getConstructedDiagramPanel().getConstructedConcreteDiagram().clear();	
		repaint();
		
	}



}



