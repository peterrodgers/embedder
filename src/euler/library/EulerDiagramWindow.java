package euler.library;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import euler.ConcreteContour;
import euler.DiagramPanel;
import euler.construction.ConstructedConcreteDiagram;
import euler.drawers.DiagramDrawer;
import euler.utilities.DiagramUtility;
import euler.views.DiagramView;


public class EulerDiagramWindow extends JFrame implements ActionListener {
	
	public static int WIDTH = 850;
	public static int HEIGHT = 850;
	protected EulerDiagramWindow ew = null;
	protected ArrayList<ConcreteContour> concreteContours	= null;
	protected File startDirectory;
	protected int width = WIDTH;
	protected int height = HEIGHT;
	protected File currentFile = null;
	protected EulerDiagramPanel cdp = null;
	protected Library library = new Library();
	String selectedDiagram = null;
	private static final long serialVersionUID = 1L;
	protected ConstructedConcreteDiagram ccd = null;
	
	public EulerDiagramWindow(String abstractDescription, ArrayList<ConcreteContour> concreteContours) {
		super("Euler Diagram Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		String startDirectoryName = System.getProperty("user.dir");
		startDirectory = new File(startDirectoryName);		
		ccd = new ConstructedConcreteDiagram(abstractDescription, concreteContours);
//		cdp = new EulerDiagramPanel(ccd);		
		library.loadDefaultThreeSetDiagrams();
		getContentPane().add(cdp);
		ew = this;
		initMenu();
	 	initUtility();		
	 	setSize(width,height);
		Dimension frameDim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (frameDim.width - getSize().width)/2;
		int posY = (frameDim.height - getSize().height)/2;
		setLocation(posX, posY);
		setLocation(0,0);
		setVisible(true);
		cdp.requestFocus();
	}

	public Library getLibrary(){return library;}
	public EulerDiagramPanel getEulerDiagramPanel() {return cdp;}
	public void setSelectedDiagram(String s){selectedDiagram = s;}
	public void actionPerformed(ActionEvent event) {
	
		JMenuItem source = (JMenuItem)(event.getSource());
		for(DiagramView v : cdp.getDiagramViewList()) {
			if (v.getMenuText().equals(source.getText())) {
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
				repaint();
				return;
			}
		}
	}
	private void initUtility() {	
		cdp.addDiagramUtility(new DiagramUtilitySearchDiagramFromLibrary(KeyEvent.VK_F, "Find Diagram",KeyEvent.VK_F,this));
	}
	private void initMenu() {
		
		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

// File Menu
		JMenu fileMenu = new JMenu("File");

		fileMenu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(fileMenu);	
	
		JMenuItem fileOpenItem = new JMenuItem("Open...",KeyEvent.VK_O);
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(fileOpenItem);

	

		JMenuItem fileSaveAsItem = new JMenuItem("Save As...");
		fileMenu.add(fileSaveAsItem);
		
		JMenuItem fileSaveAsImageItem = new JMenuItem("Save as image",KeyEvent.VK_S);
		fileSaveAsImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(fileSaveAsImageItem);
		
		JMenuItem fileSaveLibraryItem = new JMenuItem("Save Library...");
		fileMenu.add(fileSaveLibraryItem);
		fileSaveLibraryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveLibrary();
			}
		});	
		JMenuItem fileAppendLibraryItem = new JMenuItem("Append to Library...");
		fileMenu.add(fileAppendLibraryItem );
		fileAppendLibraryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				appendToLibrary();
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
	
		fileOpenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileOpen();
			}
		});

		fileSaveAsImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveASImage();
			}
		});

		fileSaveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				fileSaveAs();
			}
		});		


// Edit Menu
		JMenu editMenu = new JMenu("Edit");

		editMenu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(editMenu);	
	

		JMenuItem editMoveGraphItem = new JMenuItem("Move Graph...");
		editMenu.add(editMoveGraphItem);


		editMoveGraphItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				moveGraph();
			}
		});


		
//Library Menu
		JMenu libraryMenu = new JMenu("Library");
		libraryMenu.setMnemonic(KeyEvent.VK_L);
		menuBar.add(libraryMenu);	
		
		JMenuItem loadLibraryItem = new JMenuItem("Load Library");
		loadLibraryItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				loadLibrary();
			}
		});
		libraryMenu.add(loadLibraryItem);	
		libraryMenu.setMnemonic(KeyEvent.VK_S);
		menuBar.add(libraryMenu);	
		
		JMenuItem searchDiagramItem = new JMenuItem("Search Diagram");
		
		searchDiagramItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				searchDiagram();			 
			}
		});
		libraryMenu.add(searchDiagramItem);	
		
		;
		
		JMenuItem thumnailViewItem = new JMenuItem("Library ThumNail View");
		thumnailViewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				thumnailView();
			}
		});
		libraryMenu.add(thumnailViewItem);
		
	}	
	
	protected void loadLibrary(){
		library = new Library();
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}			
		int returnVal = chooser.showOpenDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {			
			currentFile = chooser.getSelectedFile();
			library.loadLibrary(currentFile);	
		}		
	}
	protected void saveLibrary(){
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			
			}
		}
		int returnVal = chooser.showSaveDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			try{
				library.saveAll(currentFile);			
			}catch (Exception e){System.out.println(e.getMessage());}		
		}
	}
	protected void appendToLibrary(){
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
				System.out.println(ccd.getAbstractDescription());
				if(ccd!=null){
					
					ccd.appendToFile(currentFile);
				}		
			}
		}
		int returnVal = chooser.showSaveDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			try{
							
			}catch (Exception e){System.out.println(e.getMessage());}		
		}
	}
	
	protected void displayLibrary(){
           	
		SelectDiagramFrame app = new SelectDiagramFrame(this);
	    app.setVisible(true);  
	}
	public void searchDiagram(){
		SearchDiagramDialog sd= new SearchDiagramDialog(this,null);
		sd.setVisible(true);
	}
	
	protected void fileOpen() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
		}
			
		int returnVal = chooser.showOpenDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {			
			currentFile = chooser.getSelectedFile();
			ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(currentFile);
			cdp.setConstructedConcreteDiagram(ccd);
			cdp.resetDiagram(ccd);
			cdp.update(cdp.getGraphics());
		}
	}
	protected void fileSaveASImage() {
		JFileChooser chooser = null;
		if (currentFile == null) {
			chooser = new JFileChooser(startDirectory);
		} else {
			chooser = new JFileChooser(currentFile);
			if (!currentFile.isDirectory()) {
				chooser.setSelectedFile(currentFile);
			
			}
		}
		int returnVal = chooser.showSaveDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			try{
				saveImageJPG(currentFile);
			}catch (Exception e){System.out.println(e.getMessage());}		
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
		int returnVal = chooser.showSaveDialog(ew);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = chooser.getSelectedFile();
			cdp.getConstructedConcreteDiagram().saveToFile(currentFile);
		}
	}	
	protected void fileExit() {
		System.exit(0);
	}
	public void moveGraph() {
		new MoveEulerDiagramFrame(cdp);
	}
	public  void saveImageJPG(File file) throws AWTException, IOException {
		// capture the whole screen
		BufferedImage screencapture = new Robot().createScreenCapture(
				  new Rectangle(10,80,680,680));
		  //  new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) );
		
		// Save as JPEG		 
		ImageIO.write(screencapture, "jpg", file);		
	}
	public void paintComponent(ArrayList<ConcreteContour> ccs, Graphics g, int startX, int startY) {
		Graphics2D g2 = (Graphics2D) g;		
		paintContours(ccs,g2, startX, startY);	
	}
	
	protected void paintContours(ArrayList<ConcreteContour> ccs, Graphics2D g2, int startX, int startY) {
		
		ArrayList<ConcreteContour> ccs0 = new ArrayList<ConcreteContour>();
		g2.setBackground(Color.white);
		for(ConcreteContour cc : ccs) {
			Polygon pol = new Polygon();
			for(int i = 0 ; i < cc.getPolygon().npoints; i++){
				pol.addPoint(cc.getPolygon().xpoints[i]/2+startX, cc.getPolygon().ypoints[i]/2+startY);
			}
			String abs = cc.getAbstractContour();
			ConcreteContour cc0 = new ConcreteContour(abs,pol);	
			ccs0.add(cc0);
		}
		if(ccs0 != null) {
			ArrayList<Rectangle2D> avoidRectangles = new ArrayList<Rectangle2D>();
			for(ConcreteContour cc0 : ccs0) {
				Rectangle2D r = paintContour(g2,cc0,true,true,ccs0);
				avoidRectangles.add(r);
			}					
		}
		for(ConcreteContour cc0 : ccs0) {
			String c = cc0.getAbstractContour();
			TextLayout labelLayout =  DiagramPanel.contourLabelLayoutMap.get(c);
			
			if(labelLayout != null) {
				Color solidColor =  DiagramPanel.contourSolidColourMap.get(c);
				Point2D.Float labelPoint =  DiagramPanel.contourLabelPointMap.get(c);
				float labelX = (float)labelPoint.getX();
				float labelY = (float)labelPoint.getY();
				Rectangle2D bounds =  DiagramPanel.contourBoundsMap.get(c);
				g2.setColor( DiagramPanel.PANELBACKGROUNDCOLOR);
				g2.fill(bounds);
				g2.setColor(solidColor);
				labelLayout.draw(g2,labelX,labelY);
			}
		}
		g2.setColor(Color.GRAY);
		g2.drawRect(startX,startY, 400,400);	
	}
	public static Rectangle2D paintContour(Graphics2D g2, ConcreteContour cc, boolean showContourLabel, boolean showContourAreas, ArrayList<ConcreteContour> ccs) {

		String label = cc.getAbstractContour();
		
		char contourChar = label.charAt(0);
		Color solidColor = DiagramPanel.getColorFromChar(contourChar);
		if(showContourAreas) {
			Area area = new Area(cc.getPolygon());
			if(area != null) {
				Color transparentColor = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.2f);
				g2.setColor(transparentColor);
				g2.fill(area);
			}
		}

		g2.setColor(solidColor);
		BasicStroke basicStroke =  DiagramPanel.getStrokeFromChar(contourChar);
		g2.setStroke(basicStroke);
		
		g2.drawPolygon(cc.getPolygon());
		
		if(showContourLabel) {
			Font font = new Font( DiagramPanel.LABELFONTNAME, DiagramPanel.LABELFONTSTYLE, DiagramPanel.CONTOURLABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(label, font, frc);	
			Point2D.Double labelPoint = pjr.graph.Util.getTopLeftMostPolygonPoint(cc.getPolygon());
			
			int labelX = pjr.graph.Util.convertToInteger(labelPoint.x);
			int labelY = pjr.graph.Util.convertToInteger(labelPoint.y);
			
			
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+labelX-2, bounds.getY()+labelY-2, bounds.getWidth()+4,bounds.getHeight()+4);
			
			g2.setColor( DiagramPanel.PANELBACKGROUNDCOLOR);
			g2.fill(bounds);
			g2.setColor(solidColor);
			labelLayout.draw(g2,labelX,labelY);
			
			return bounds;
		}
		return null;
	}
	public void thumnailView(){
		//this.removeAll();
		if(library != null){
//			LibraryThumnailView lv = new LibraryThumnailView(library,this);
		}		   
		else{
			 System.out.println("empty library");
		 }
		
		
		
	
		
	}

	
}



