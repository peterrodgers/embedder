package euler;

import javax.swing.*;
import javax.swing.event.*;
import pjr.graph.*;
import pjr.graph.dialogs.*;
import euler.drawers.*;
import euler.experiments.*;
import euler.utilities.*;
import euler.views.*;
import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;

/**
  * A panel on which a dual graph and euler diagram are displayed.
 * The graph is editable, the euler diagram is not, it appears
 * based on the dual graph in the window. This code is based on
 * GraphPanel.java
 * 
 * <p>
 * Functions:
 * <br>
 * - Add a node with double button 1 click on the background.
 * <br>
 * - Add an edge with a button 3 drag (picks closest nodes to start
 *   and end of the drag, but does not add self sourcing nodes).
 * <br>
 * - Edit a node or edge with a double button 1 click on the item.
 * <br>
 * - Drag a node with a button 1 drag on a node.
 * <br>
 * - Select a node with a single button 1 click on a node or a button 1 drag on the background,
 *   add new nodes to the selection by pressing the control key whilst selecting.
 * <br>
 * - Delete the selection with Del or Backspace
 *
 * @author Peter Rodgers
 */
public class DiagramPanel extends JPanel implements MouseInputListener, KeyListener {

	private static final long serialVersionUID = 1L;

	public static final Color PANELBACKGROUNDCOLOR = Color.white;
	public static final Color SELECTEDPANELAREACOLOR = Color.gray;
	public static final Color ADDBENDCOLOR = Color.orange;
    public static final BasicStroke SELECTEDPANELAREASTROKE = new BasicStroke(1.0f);
	public static final String LABELFONTNAME = "Arial";
	public static final int LABELFONTSTYLE = Font.BOLD;
	public static final int LABELFONTSIZE = 12;
	public static final int CONTOURLABELFONTSIZE = 18;
	public static final Dimension BUTTONSIZE = new Dimension(78,32);
	public static final Dimension LARGEBUTTONSIZE = new Dimension(116,32);
	public static final Point ZEROOFFSET = new Point(0,0);
	public static final int PARALLELEDGEOFFSET = 4;
	public static final int CONTOURBORDERPADDING = 30;
	protected boolean showGraph = true;
	protected boolean showContour = false;
	protected boolean showRegion = false;
	/** Move the labels by a random small amount. Useful when label occlusion is present */
	protected boolean jiggleLabels = false;
	protected int jiggleMovement = 10;
	protected boolean showContourLabel = true; // only shown when a contour is shown
	protected boolean showContourAreas = true; // only shown when a contour is shown
	protected boolean optimizeContourAngles = true;
	protected boolean optimizeMeetingPoints = true;
	protected boolean fitCircles = false;
	protected boolean showTriangulation = false;
	protected int contourTranslation = 2;
	protected boolean showEdgeDirection = true;
	protected boolean showTriangulationEdgeLabel = true; // only shown when the triangulation is shown
	protected boolean showEdgeLabel = true; // only shown when the graph is shown
	protected boolean showNodeLabel = true; // only shown when the graph is shown
	/** Indicates if parallel edges should be separated when displayed. */
	protected boolean separateParallel = true;
	/** Stops the panel from being redrawn */
	protected boolean forceNoRedraw = false;
	protected DualGraph dualGraph;
	protected ConcreteDiagram concreteDiagram = null; // only formed when drawing contours
	protected ArrayList<DiagramDrawer> diagramDrawerList = new ArrayList<DiagramDrawer>();
	protected ArrayList<DiagramUtility> diagramUtilityList = new ArrayList<DiagramUtility>();
	protected ArrayList<DiagramView> diagramViewList = new ArrayList<DiagramView>();
	protected ArrayList<DiagramExperiment> diagramExperimentList = new ArrayList<DiagramExperiment>();
	protected GraphSelection selection;
	protected boolean dragSelectionFlag = false;
	protected Node dragNode = null;
	protected Node selectNode = null;
	protected Edge selectEdge = null;
	protected Node newEdgeNode = null;
	protected Point newEdgePoint = null;
	protected Point pressedPoint = null;
	protected Point lastPoint = null;
	protected Point dragSelectPoint = null;
	protected Edge addBendEdge = null;
	protected Point addBendPoint1 = null;
	protected Point addBendPoint2 = null;
	protected Point sharedBendPoint = null;
	protected Frame containerFrame = null;
	protected Applet containerApplet = null;
	protected Color panelBackgroundColor = PANELBACKGROUNDCOLOR;
	protected Color selectedPanelAreaColor = SELECTEDPANELAREACOLOR;
	protected BasicStroke selectedPanelAreaStroke = SELECTEDPANELAREASTROKE;
	protected int parallelEdgeOffset = PARALLELEDGEOFFSET;
	public static Color c1 = new Color(200,100,100); 
	public static Color c2 = new Color(0,200,100);
	public static Color c3 = new Color(100,0,200); 
	public static Color c4 = new Color(200,100,0); 

	public static Color contourColors[] = {Color.RED,Color.GREEN,Color.CYAN,Color.GRAY,Color.MAGENTA,Color.BLACK,
		Color.PINK,Color.ORANGE,Color.LIGHT_GRAY,Color.YELLOW,Color.DARK_GRAY,c1,c2,c3,c4};
	
	private Random random = new Random(System.currentTimeMillis());

	public Font [] fonts;


	public DiagramPanel() {
		super();
		dualGraph = null;
		containerFrame = null;
		containerApplet = null;
	}

	public DiagramPanel(DualGraph inDualGraph, Applet inApplet) {
		super();
		dualGraph = inDualGraph;
		containerFrame = null;
		containerApplet = inApplet;
		setup(dualGraph);
	}

	public DiagramPanel(DualGraph inDualGraph, Frame inContainerFrame) {
		super();
		dualGraph = inDualGraph;
		containerFrame = inContainerFrame;
		containerApplet = null;
		setup(dualGraph);
	}
	
	/** Just used for testing */
	public DiagramPanel(DualGraph inDualGraph) {
		super();
		dualGraph = inDualGraph;
		containerFrame = null;
		containerApplet = null;
		setup(dualGraph);
	}
	
	protected void setup(DualGraph dualGraph) {
		
		selection = new GraphSelection(dualGraph);
		setBackground(panelBackgroundColor);
		addMouseListener(this);
		addKeyListener(this);
	}

	
	public boolean getShowGraph() {return showGraph;}
	public boolean getShow() {return showRegion;}
	public boolean getShowContour() {return showContour;}
	public boolean getOptimizeContourAngles() {return optimizeContourAngles;}
	public boolean getOptimizeMeetingPoints() {return optimizeMeetingPoints;}
	public boolean getFitCircles() {return fitCircles;}
	public boolean getJiggleLabels() {return jiggleLabels;}
	public int getJiggleMovement() {return jiggleMovement;}
	public boolean getShowContourLabel() {return showContourLabel;}
	public boolean getShowContourAreas() {return showContourAreas;}
	public boolean getShowTriangulation() {return showTriangulation;}
	public int getContourTranslation() {return contourTranslation;}
	public boolean getShowEdgeDirection() {return showEdgeDirection;}
	public boolean getShowTriangulationEdgeLabel() {return showTriangulationEdgeLabel;}
	public boolean getShowEdgeLabel() {return showEdgeLabel;}
	public boolean getShowNodeLabel() {return showNodeLabel;}
	public boolean getSeparateParallel() {return separateParallel;}
	public boolean getForceNoRedraw() {return forceNoRedraw;}
	public ConcreteDiagram getConcreteDiagram() {return concreteDiagram;}
	public DualGraph getDualGraph() {return dualGraph;}
	public GraphSelection getSelection() {return selection;}
	public ArrayList<DiagramDrawer> getDiagramDrawerList() {return diagramDrawerList;}
	public ArrayList<DiagramUtility> getDiagramUtilityList() {return diagramUtilityList;}
	public ArrayList<DiagramView> getDiagramViewList() {return diagramViewList;}
	public ArrayList<DiagramExperiment> getDiagramExperimentList() {return diagramExperimentList;}
	public Frame getContainerFrame() {return containerFrame;}
	public int getParallelEdgeOffset() {return parallelEdgeOffset;}

	/**
	 * Clear the selection when showing or hiding the graph.
	 */
	public void setShowGraph(boolean flag) {
		// if it is going to change, clear the partial edits
		if(showGraph != flag) {
			showGraph = flag;
			selection.clear();
			
			// deal with drag nodes, creating edges and creating new
			newEdgeNode = null;
			dragNode = null;
			newEdgeNode = null;
			newEdgePoint = null;
			dragSelectPoint = null;
		}
	}
	public void setShowRegion(boolean flag) {showRegion = flag;}
	public void setShowContour(boolean flag) {showContour = flag;}
	public void setOptimizeContourAngles(boolean flag) {optimizeContourAngles = flag;}
	public void setOptimizeMeetingPoints(boolean flag) {optimizeMeetingPoints = flag;}
	public void setFitCircles(boolean flag) {fitCircles = flag;}
	public void setJiggleLabels(boolean flag) {jiggleLabels = flag;}
	public void setJiggleMovement(int move) {jiggleMovement = move;}
	public void setShowContourLabel(boolean flag) {showContourLabel = flag;}
	public void setShowContourAreas(boolean flag) {showContourAreas = flag;}
	public void setShowTriangulation(boolean flag) {showTriangulation = flag;}
	public void setContourTranslation(int i) {contourTranslation = i;}
	public void setShowEdgeDirection(boolean flag) {showEdgeDirection = flag;}
	public void setShowTriangulationEdgeLabel(boolean flag) {showTriangulationEdgeLabel = flag;}
	public void setShowEdgeLabel(boolean flag) {showEdgeLabel = flag;}
	public void setShowNodeLabel(boolean flag) {showNodeLabel = flag;}
	public void setSeparateParallel(boolean flag) {separateParallel = flag;}
	public void setForceNoRedraw(boolean flag) {forceNoRedraw = flag;}
	public void setParallelEdgeOffset(int o) {parallelEdgeOffset = o;}
	public void setConcreteDiagram(ConcreteDiagram cd) {concreteDiagram = cd;}
	public void setDualGraph(DualGraph dg) {
		dualGraph = dg;
		resetDiagram();
		repaint();
	}

	/** Add a drawing algorithm to the panel. */
	public void addDiagramDrawer(DiagramDrawer dd) {
		diagramDrawerList.add(dd);
		dd.setDiagramPanel(this);
	}

	/** Removes a drawing algorithm from the panel. */
	public void removeDiagramDrawer(DiagramDrawer dd) {
		diagramDrawerList.remove(dd);
		dd.setDiagramPanel(null);
	}

	/** Add a graph utility to the panel. */
	public void addDiagramUtility(DiagramUtility du) {
		diagramUtilityList.add(du);
		du.setDiagramPanel(this);
	}

	/** Removes a graph utility from the panel. */
	public void removeDiagramUtility(DiagramUtility du) {
		diagramUtilityList.remove(du);
		du.setDiagramPanel(null);
	}

	/** Add a graph view to the panel. */
	public void addDiagramView(DiagramView dv) {
		diagramViewList.add(dv);
		dv.setDiagramPanel(this);
	}

	/** Removes a graph view from the panel. */
	public void removeDiagramView(DiagramView dv) {
		diagramViewList.remove(dv);
		dv.setDiagramPanel(null);
	}

	/** Add a graph experiment to the panel. */
	public void addDiagramExperiment(DiagramExperiment de) {
		diagramExperimentList.add(de);
		de.setDiagramPanel(this);
	}

	/** Removes a graph experiment from the panel. */
	public void removeDiagramExperiment(DiagramExperiment de) {
		diagramExperimentList.remove(de);
		de.setDiagramPanel(null);
	}
	

// TODO Remove this test code at some point
public static ArrayList<Area> areas = new ArrayList<Area>();
public static ArrayList<Polygon> polygons = new ArrayList<Polygon>();
public static ArrayList<String> strings = new ArrayList<String>();
public static ArrayList<Point> stringPoints = new ArrayList<Point>();

	public void superPaintComponent(Graphics2D g2) {
		super.paintComponent(g2);
	}


	public void paintComponent(Graphics g) {
		if(forceNoRedraw) {
			return;
		}

// automatically generated graphs now have empty set removed so no need for this heavy fix
//		dualGraph.removeNode("");
		
		Graphics2D g2 = (Graphics2D) g;
	
		//paint background
		superPaintComponent(g2);
		
int i;
i = 0;
for(Area a:areas) {
	if(i >= contourColors.length) {
		i = 0;
	}
	Color solidColor;
	solidColor = contourColors[i];
//	solidColor = Color.red;
	Color c = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.2f);

//	c = Color.black;
	g2.setColor(c);

	if(!a.isEmpty()) {
		g2.fill(a);
	}
	i++;
}

i = 0;
for(Polygon p:polygons) {
	if(i > contourColors.length) {
		i = 0;
	}
//	g2.setColor(contourColors[i]);
	g2.setColor(Color.blue);
	g2.draw(p);
	i++;
}
for(int j = 0; j< strings.size(); j++) {
//	int k = j;
//	if(j > contourColors.length) {
//		k = 0;
//	}
//	g2.setColor(contourColors[k]);
	g2.setColor(Color.red);
	String s = strings.get(j);
	Point p = stringPoints.get(j);
	Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
	FontRenderContext frc = g2.getFontRenderContext();
	TextLayout labelLayout = new TextLayout(s, font, frc);	
	
	labelLayout.draw(g2,p.x,p.y);
	
	
	
}

		if(showGraph) {
			//draw the edges
			if(!separateParallel) {
				paintOverlaidEdges(g2,dualGraph);
			} else {
				paintSeparateEdges(g2,dualGraph);
			}
	
			//draw the new edge drag
			if (newEdgePoint != null) {
				g2.setColor(selectedPanelAreaColor);
				Point centre = newEdgeNode.getCentre();
				g2.drawLine(centre.x,centre.y,newEdgePoint.x,newEdgePoint.y);
			}
	
			//draw the nodes
			for(Node n : dualGraph.getNodes()) {
				paintNode(g2,n);
			}
		}

		if(showRegion) {
			paintContoursAndRegions(g2);
		} else {
			if(showContour || showTriangulation) {
				paintContours(g2);
			}
		}
		// draw the area selection
		if (dragSelectPoint != null) {
			g2.setColor(selectedPanelAreaColor);
	        g2.setStroke(selectedPanelAreaStroke);
			Shape r = convertPointsToRectangle(pressedPoint,dragSelectPoint);
			g2.draw(r);
		}
		
		// draw the new bend point lines
		if(sharedBendPoint != null) {
			g2.setColor(ADDBENDCOLOR);
			g2.drawLine(sharedBendPoint.x,sharedBendPoint.y,addBendPoint1.x,addBendPoint1.y);
			g2.drawLine(sharedBendPoint.x,sharedBendPoint.y,addBendPoint2.x,addBendPoint2.y);
		}


	}
	
	/**
	 * This generates a triangulation of a graph that includes nodes
	 * and edges bordering the original graph that are not in the original
	 * graph
	 */
	protected void paintContours(Graphics2D g2) {
		if(concreteDiagram == null) {
			concreteDiagram = new GeneralConcreteDiagram(dualGraph);
			concreteDiagram.generateContours();
			concreteDiagram.setConcurrentOffset(ConcreteDiagram.CONCURRENT_OFFSET);
			concreteDiagram.setOptimizeContourAngles(optimizeContourAngles);
			concreteDiagram.setOptimizeMeetingPoints(optimizeMeetingPoints);
			concreteDiagram.setFitCircles(fitCircles);
//			concreteDiagram.generateContours();
		} else {
			concreteDiagram.setOptimizeContourAngles(optimizeContourAngles);
			concreteDiagram.setOptimizeMeetingPoints(optimizeMeetingPoints);
			concreteDiagram.setFitCircles(fitCircles);
			concreteDiagram.routeContours();
		}
		
contourSolidColourMap = new HashMap<String,Color>();
contourPolygonMap = new HashMap<String,Polygon>();
contourBoundsMap = new HashMap<String,Rectangle2D>();
contourLabelLayoutMap = new HashMap<String,TextLayout>();
contourLabelPointMap = new HashMap<String,Point2D.Float>();
		if(showContour) {
			ArrayList<ConcreteContour> ccs = concreteDiagram.getConcreteContours();
//Collections.reverse(ccs);
			if(ccs != null) {
				ArrayList<Rectangle2D> avoidRectangles = new ArrayList<Rectangle2D>();
				for(ConcreteContour cc : ccs) {
					Rectangle2D r = paintContour(g2,dualGraph,cc,showContourLabel,showContourAreas,jiggleLabels,jiggleMovement,ccs,avoidRectangles);
					avoidRectangles.add(r);
				}
					
			}
		}
		
		if(showTriangulation) {
			DualGraph cloneGraph = concreteDiagram.getCloneGraph();

			paintTriangulationFaces(g2,cloneGraph);
		}
if(showContourLabel) {
	for(ConcreteContour cc : concreteDiagram.getConcreteContours()) {
		String c = cc.getAbstractContour();
		TextLayout labelLayout = contourLabelLayoutMap.get(c);
	
		if(labelLayout != null) {
			Color solidColor = contourSolidColourMap.get(c);
			Point2D.Float labelPoint = contourLabelPointMap.get(c);
			float labelX = (float)labelPoint.getX();
			float labelY = (float)labelPoint.getY();
			Rectangle2D bounds = contourBoundsMap.get(c);
			g2.setColor(PANELBACKGROUNDCOLOR);
			g2.fill(bounds);
			g2.setColor(solidColor);
			labelLayout.draw(g2,labelX,labelY);
		}
	}
}

/*
ArrayList<ConcreteContour> ccs = concreteDiagram.getConcreteContours();
HashMap<String,Area> zoneAreas = ConcreteContour.generateZoneAreas(ccs);
for(String ac : zoneAreas.keySet()) {
if(ac.equals("")) {
	continue;
}
Area a = zoneAreas.get(ac);
ArrayList<Polygon>  ps = ConcreteContour.polygonsFromArea(a);
Polygon p = ps.get(0);
g2.setColor(Color.gray);
euler.rectangle.MaxRectangleFinder maxRectangleFinder = new euler.rectangle.MaxRectangleFinder(p);
Rectangle r = maxRectangleFinder.getMaxSquare();
g2.draw(r);
}
*/
		
	}


	/**
	 * Returns the color for the contour label
	 */
	public static Color getColorFromChar(char c) {
		int i = (int)c - (int)'a';
		if(i < 0) {i = 0-i;} // capitals give a negative i
		
		int colorInt = i % contourColors.length;
	    Color color = contourColors[colorInt];
		while(i >= contourColors.length) {
			color.brighter();
			i = i - contourColors.length;
		}
		return color;
	}

	/**
	 * Returns the stroke for the contour label
	 */
	public static BasicStroke getStrokeFromChar(char c) {
	    BasicStroke basicStroke = new BasicStroke(2);
		int i = (int)c - (int)'a';
		if(i < 0) {i = 0-i;} // capitals give a negative i
		
		while(i >= contourColors.length) {
			basicStroke = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,0,new float[] {9},0);
			i = i - contourColors.length;
		}
		return basicStroke;
	}

	
	public static Rectangle2D paintContour(Graphics2D g2, DualGraph dg, ConcreteContour cc, boolean showContourLabel, boolean showContourAreas, boolean jiggleLabels, int jiggleMovement, ArrayList<ConcreteContour> ccs, ArrayList<Rectangle2D> avoidRectangles) {

		String oldLabel = cc.getAbstractContour();
/*
if(cc.getAbstractContour().equals("b")) {
System.out.println("In paintContour");
Area a = cc.getArea();
double[] coords = new double[6];
PathIterator pi = a.getPathIterator(null);
while (!pi.isDone()) {
int coordType = pi.currentSegment(coords);
if (coordType == PathIterator.SEG_CLOSE) {System.out.print("SEG_CLOSE ");}
if (coordType == PathIterator.SEG_MOVETO) {System.out.print("SEG_MOVETO ");}
if (coordType == PathIterator.SEG_LINETO) {System.out.print("SEG_LINETO ");}
if (coordType == PathIterator.SEG_CUBICTO) {System.out.print("SEG_CUBICTO ");}
if (coordType == PathIterator.SEG_QUADTO) {System.out.print("SEG_QUADTO ");}

int x = pjr.graph.Util.convertToInteger(coords[0]);
int y = pjr.graph.Util.convertToInteger(coords[1]);
System.out.println(x+" "+y);

pi.next();
}
}
*/
		
		// might need to relabel contours
		HashMap<String,String> duplicateMap = dg.getContourDuplicateLabelMap();
		
		String label = oldLabel;
		if(duplicateMap != null) {
			if(duplicateMap.get(oldLabel) != null) {
				label = duplicateMap.get(oldLabel);
			}
		}
		HashMap<String,String> holeMap = dg.getContourHoleLabelMap();
		if(holeMap != null) {
			if(holeMap.get(oldLabel) != null) {
				label = holeMap.get(oldLabel);
			}
		}
		
		char contourChar = label.charAt(0);
		Color solidColor = getColorFromChar(contourChar);
		if(showContourAreas) {
			Area area = findDuplicateAndHoleArea(dg,cc,ccs);
			if(area != null) {
				Color transparentColor = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.2f);
				g2.setColor(transparentColor);
				g2.fill(area);
			}
		}

		g2.setColor(solidColor);
		BasicStroke basicStroke = getStrokeFromChar(contourChar);
		g2.setStroke(basicStroke);
		
		g2.drawPolygon(cc.getPolygon());
contourPolygonMap.put(oldLabel,cc.getPolygon());
		
		if(showContourLabel) {
			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,CONTOURLABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(label, font, frc);
			Point2D.Double labelPoint = findContourLabelPosition(cc);
			
			int labelX = pjr.graph.Util.convertToInteger(labelPoint.x);
			int labelY = pjr.graph.Util.convertToInteger(labelPoint.y);
			
			if(jiggleLabels) {
				Random random = new Random(System.currentTimeMillis());
				int rX = random.nextInt(jiggleMovement+1);
				rX -= jiggleMovement/2;
				int rY = random.nextInt(jiggleMovement+1);
				rY -= jiggleMovement/2;
				
				labelX += rX;
				labelY += rY;
			}
			
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+labelX-2, bounds.getY()+labelY-2, bounds.getWidth()+4,bounds.getHeight()+4);
			
			if(avoidRectangles != null) {
				boolean overlaps = true;
				while(overlaps) {
					overlaps = false;
					for(Rectangle2D r : avoidRectangles) {
						if(r.intersects(bounds.getX(),bounds.getY(),bounds.getWidth(),bounds.getHeight())) {
							overlaps = true;
							break;
						}
					}
					if(overlaps) {
						labelX += 5;
						bounds = labelLayout.getBounds();
						bounds.setRect(bounds.getX()+labelX-2, bounds.getY()+labelY-2, bounds.getWidth()+4,bounds.getHeight()+4);
					}
				}
			}
			
			g2.setColor(PANELBACKGROUNDCOLOR);
			g2.fill(bounds);
			g2.setColor(solidColor);
			labelLayout.draw(g2,labelX,labelY);
contourSolidColourMap.put(oldLabel,solidColor);
contourBoundsMap.put(oldLabel,bounds);
contourLabelLayoutMap.put(oldLabel,labelLayout);
contourLabelPointMap.put(oldLabel,new Point2D.Float(labelX,labelY));
			
			return bounds;
		}
		return null;
	}

	
	/**
	 * Given a polygon, this method finds the point on the polygon closest
	 * to the origin and returns a point on the line connecting to that point.
	 */
	public static Point2D.Double findContourLabelPosition(ConcreteContour cc) {
		
		Polygon p = cc.getPolygon();
		
		if(p == null) {
			return null;
		}
		
		Point2D.Double origin = new Point2D.Double(0,0);
		double points[] = new double[2];
		
		Point2D.Double topLeft = null;
		Point2D.Double previousToTopLeft = null;
		Point2D.Double prev = null;
		double distance = Double.MAX_VALUE;
		
		PathIterator pit = p.getPathIterator(null);
		while(!pit.isDone()) {
			pit.currentSegment(points);
			Point2D.Double nextPoint = new Point2D.Double(points[0],points[1]);
			double nextDistance = pjr.graph.Util.distance(origin,nextPoint);

			if(nextDistance < distance) {
				topLeft = nextPoint;
				previousToTopLeft = prev;
				distance = nextDistance;
			}
			prev = nextPoint;
			pit.next();
			// in case the topLeft is the first point
			if(previousToTopLeft == null) {
				if(pit.isDone()) {
					PathIterator pit2 = p.getPathIterator(null);
					pit2.next(); // at least 2 points in a polygon, start point is the same as the end
					pit2.currentSegment(points);
					previousToTopLeft = new Point2D.Double(points[0],points[1]);
				}
			}

		}
		// split the two points
		Point2D.Double ret = pjr.graph.Util.midPoint(topLeft, previousToTopLeft);
		return ret;
	}


	
	public static HashMap<String,Color> contourSolidColourMap = new HashMap<String,Color>();
	public static HashMap<String,Polygon> contourPolygonMap = new HashMap<String,Polygon>();
	public static HashMap<String,Rectangle2D> contourBoundsMap = new HashMap<String,Rectangle2D>();
	public static HashMap<String,TextLayout> contourLabelLayoutMap = new HashMap<String,TextLayout>();
	public static HashMap<String,Point2D.Float> contourLabelPointMap = new HashMap<String,Point2D.Float>();

	
	private static Area findDuplicateAndHoleArea(DualGraph dg, ConcreteContour cc, ArrayList<ConcreteContour> ccs) {
		
		Area ret = new Area();
		
		HashMap<String,String> duplicateMap = dg.getContourDuplicateLabelMap();

		if(duplicateMap == null) {
			return null;
		}
		
		for(String label : duplicateMap.keySet()) {
			String contour = duplicateMap.get(label);
			if(contour.equals(cc.getAbstractContour())) {
				for(ConcreteContour mapCC : ccs) {
					if(label.equals(mapCC.getAbstractContour())) {
						ret.add(mapCC.getArea());
					}
				}
			}
		}

		HashMap<String,String> holeMap = dg.getContourHoleLabelMap();
		for(String label : holeMap.keySet()) {
			String contour = holeMap.get(label);
			if(contour.equals(cc.getAbstractContour())) {
				for(ConcreteContour mapCC : ccs) {
					if(label.equals(mapCC.getAbstractContour())) {
						ret.subtract(mapCC.getArea());
					}
				}
			}
		}


		return ret;
	}

	public void paintTriangulationFaces(Graphics2D g2,DualGraph dg) {
		
		for(TriangulationEdge te : dg.findTriangulationEdges()) {
			
//			if(te.getEdge() != null) { // dont draw the edges that parallel graph edges
//				continue;
//			}

			g2.setColor(te.getUnassignedLineColor());
			if(te.getCutPoints() != null) {
				g2.setColor(te.getAssignedLineColor());
			}
			
	        g2.setStroke(te.getStroke());

			Point fromPoint = new Point(te.getFrom().getCentre());
			Point toPoint = new Point(te.getTo().getCentre());
			
			g2.drawLine(fromPoint.x,fromPoint.y,toPoint.x,toPoint.y);
			
			if(!te.getLabel().equals("") && showTriangulationEdgeLabel) {

				if(te.getCutPoints() == null) {
					// if there are no cut point coordinates assigned yet
					int n1X = te.getFrom().getCentre().x;
					int n1Y = te.getFrom().getCentre().y;
					int n2X = te.getTo().getCentre().x;
					int n2Y = te.getTo().getCentre().y;
					int x = 0;
					int y = 0;
					if(n1X-n2X > 0) {
						x = n2X+(n1X-n2X)/2;
					} else {
						x = n1X+(n2X-n1X)/2;
					}
					if(n1Y-n2Y > 0) {
						y = n2Y+(n1Y-n2Y)/2;
					} else {
						y = n1Y+(n2Y-n1Y)/2;
					}
					
					drawContourLabel(g2,te.getLabel(),x,y,te.getUnassignedTextColor());
				} else {
					// if the cut point coordinates have been assigned
					for(CutPoint cp :te.getCutPoints()) {
						StringBuffer contoursBuf = new StringBuffer("");
						for(ContourLink cl : cp.getContourLinks()) {
							contoursBuf.append(cl.getContour());
						}
						String contours = contoursBuf.toString();
						drawContourLabel(g2,contours,cp.getCoordinate().x,cp.getCoordinate().y,te.getAssignedTextColor());
					}
				}
			}
/*			
			// indicate the TFs this TE belongs to, black for the first, blue for the second
			g2.setColor(Color.BLACK);
			for(TriangulationFace tf : te.getTriangulationFaceList()) {
				Point teMid = pjr.graph.Util.midPoint(te.getFrom().getCentre(), te.getTo().getCentre());
				Point inTFPoint = pjr.graph.Util.midPoint(teMid, tf.centroid());
				g2.drawLine(teMid.x,teMid.y,inTFPoint.x,inTFPoint.y);
				g2.setColor(Color.BLUE);
			}
*/
		}
		

		
// TEMP TO draw a square at the meeting point in a face
/*
for(Face f : dg.getFaces()) {
	if(f.getMeetingTF() != null) {
		if(f.getMeetingTF().getMeetingPoint() != null) {
			Point c = f.getMeetingTF().getMeetingPoint().getCoordinate();
			g2.setColor(Color.black);
			g2.drawRect(c.x-5,c.y-5,10,10);
		}
	}
}
*/

Iterator<Point> pi = lineList.iterator();
g2.setColor(Color.black);
while(pi.hasNext()) {
	Point p1 = pi.next();
	Point p2 = pi.next();
	g2.drawLine(p1.x, p1.y, p2.x, p2.y);
}
/*	
for(TriangulationEdge te : dg.findTriangulationEdges()) {
	for(CutPoint cp : te.getCutPoints()) {
		for(ContourLink cl : cp.getContourLinks()) {
			g2.setColor(Color.blue);
//			Point p1 = cl.getPrev().getCutPoint().getCoordinate();
			Point p2 = cl.getCutPoint().getCoordinate();
			Point p3 = cl.getNext().getCutPoint().getCoordinate();
//			g2.drawLine(p1.x, p1.y, p2.x, p2.y);
			g2.drawLine(p3.x, p3.y, p2.x, p2.y);
		}
	}
}
*/	

	}
	
//TODO remove this at some point
public static ArrayList<Point> lineList = new ArrayList<Point>();

	
	
	public void paintContoursAndRegions(Graphics2D g2) {

		if(concreteDiagram == null) {
			concreteDiagram = new GeneralConcreteDiagram(dualGraph);
			concreteDiagram.generateContours();
			concreteDiagram.setConcurrentOffset(ConcreteDiagram.CONCURRENT_OFFSET);
			concreteDiagram.setOptimizeContourAngles(optimizeContourAngles);
			concreteDiagram.setOptimizeMeetingPoints(optimizeMeetingPoints);
			concreteDiagram.setFitCircles(fitCircles);
			concreteDiagram.generateContours();
		} else {
			concreteDiagram.setOptimizeContourAngles(optimizeContourAngles);
			concreteDiagram.setOptimizeMeetingPoints(optimizeMeetingPoints);
			concreteDiagram.setFitCircles(fitCircles);
			concreteDiagram.routeContours();
		}
		
		ArrayList<ConcreteContour> ccs = concreteDiagram.getConcreteContours();
		if(ccs == null) {
			return;
		}
		
contourSolidColourMap = new HashMap<String,Color>();
contourPolygonMap = new HashMap<String,Polygon>();
contourBoundsMap = new HashMap<String,Rectangle2D>();
contourLabelLayoutMap = new HashMap<String,TextLayout>();
contourLabelPointMap = new HashMap<String,Point2D.Float>();
		if(showContour) {
			ArrayList<Rectangle2D> avoidRectangles = new ArrayList<Rectangle2D>();

			for(ConcreteContour cc : ccs) {	
				Rectangle2D r = paintContour(g2,dualGraph,cc,showContourLabel,showContourAreas,jiggleLabels,jiggleMovement,ccs,avoidRectangles);
				avoidRectangles.add(r);
			}
		}
		
		DualGraph cloneGraph = concreteDiagram.getCloneGraph();
		if(showTriangulation) {
			paintTriangulationFaces(g2,cloneGraph);
		}
		
		for(ConcreteContour cc : ccs) {
			String contour = cc.getAbstractContour();
			TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
			ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);
	
			// find the allowed range for the contour
			ContourLink cl = null;
	
			while(startCL != cl) {
				if(cl == null) {
					cl = startCL;
				}
				startTE.assignCPRange(cl.getCutPoint(),0.1);
				
				cl = cl.getNext();
			}
			Polygon minP = cc.findMinPolygon(cloneGraph);
			Polygon maxP = cc.findMaxPolygon(cloneGraph);

			Area a = new Area(maxP);
			a.subtract(new Area(minP));
/*		
			g2.setColor(Color.black);
			MaxRectangleFinder max1 = new MaxRectangleFinder(maxP);
	        Rectangle rect1 = max1.getMaxSquare();
	        Polygon pol1 = ConcreteContour.generateRegularPolygonInRectangle(10, rect1);
		    g2.draw(pol1);
*/
	        

			char contourChar = cc.getAbstractContour().charAt(0);
			Color solidColor = getColorFromChar(contourChar);
			Color transparentColor = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.2f);

			g2.setColor(transparentColor);
// restriction to show the region for only contour a
if(contour.equals("a")) {
			g2.fill(a);
}
		}
/*for(ConcreteContour cc : ccs) {
	Rectangle2D bounds = contourBoundsMap.get(cc);
	Color solidColor = contourSolidColourMap.get(cc);
	Point2D.Float labelPoint = contourLabelPointMap.get(cc);
	float labelX = (float)labelPoint.getX();
	float labelY = (float)labelPoint.getY();
	TextLayout labelLayout = contourLabelLayoutMap.get(cc);
	g2.setColor(PANELBACKGROUNDCOLOR);
	g2.fill(bounds);
	g2.setColor(solidColor);
	labelLayout.draw(g2,labelX,labelY);
}
*/

	}
	

	public void drawContourLabel(Graphics2D g2, String s, int x, int y, Color color) {

		Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout labelLayout = new TextLayout(s, font, frc);
		
		
/*
		g2.setColor(PANELBACKGROUNDCOLOR);
		Rectangle2D bounds = labelLayout.getBounds();
		bounds.setRect(bounds.getX()+x-2, bounds.getY()+y-2, bounds.getWidth()+4,bounds.getHeight()+4);
		g2.fill(bounds);

		g2.setColor(color);
*/		
		
		if(jiggleLabels) {
			int rX = random.nextInt(jiggleMovement+1);
			rX -= jiggleMovement/2;
			int rY = random.nextInt(jiggleMovement+1);
			rY -= jiggleMovement/2;
			
			x += rX;
			y += rY;
		}


		labelLayout.draw(g2,x,y);
	}
	
	
	/** This is used when parallel edges are overlaid */
	protected void paintOverlaidEdges(Graphics2D g2, Graph g) {

		for(Edge e : dualGraph.getEdges()) {
			paintEdge(g2,e,ZEROOFFSET);
		}
	}
	
	
	/** This is used when parallel edges are displayed separately */
	protected void paintSeparateEdges(Graphics2D g2, Graph g) {

		// set up the lists of parallel edges
		ParallelEdgeList parallelList = new ParallelEdgeList(g);

		// iterate through the lists displaying the edges
		// first consider any edge type order in the nodes neighbouring the parallel edges.
		// second order by edge type priority
		// third, choose randomly

		parallelList.setAllSorted(false);

		for(ParallelEdgeTuple tuple : parallelList.getParallelList()) {
			ParallelEdgeTuple sortedTuple = null;

			// check for current order from neighbouring nodes
			//sortedTuple = getSortedNeigbour(tuple);
			// TBD this will order the edges based on the neighbours ordering. Is this needed?

			// order by sorting
			if(sortedTuple == null) {
				tuple.sortList();
			}

			// set up the offset values

			Node n1 = tuple.getFromNode();
			Node n2 = tuple.getToNode();

			double xDiff = n1.getX()-n2.getX();
			double yDiff = n1.getY()-n2.getY();

			double incrementX = 0;
			double incrementY = 0;
			double divisor = Math.abs(xDiff)+Math.abs(yDiff);

			if (divisor != 0) {
				incrementX = yDiff/divisor;
				incrementY = -xDiff/divisor;
			}

			incrementX *= parallelEdgeOffset;
			incrementY *= parallelEdgeOffset;

			// find a sensible starting offset
			double numberOfEdges = tuple.getList().size();
			Point offset = new Point((int)(-((numberOfEdges-1)*incrementX)/2),(int)(-((numberOfEdges-1)*incrementY)/2));

			// display the edges given the order
			for(Edge e : tuple.getList()) {
				paintEdge(g2,e,offset);
				offset.x += (int)incrementX;
				offset.y += (int)incrementY;
			}
			tuple.setSorted(true);
		}
	}

	/** Draws an edge on the graphics */
	public void paintEdge(Graphics2D g2, Edge e, Point offset) {

		EdgeType et = e.getType();

		if(!selection.contains(e)) {
			g2.setColor(et.getLineColor());
		} else {
			g2.setColor(et.getSelectedLineColor());
		}
		if(!selection.contains(e)) {
	        g2.setStroke(et.getStroke());
		} else {
	        g2.setStroke(et.getSelectedStroke());
		}

		Shape edgeShape = e.generateShape(offset);
		g2.draw(edgeShape);

		// draw the arrow if required
		if(et.getDirected() && showEdgeDirection) {

			// TODO take account of edge bends, put the arrow on the the middle edge bend

			int n1X = e.getFrom().getCentre().x+offset.x;
			int n1Y = e.getFrom().getCentre().y+offset.y;
			int n2X = e.getTo().getCentre().x+offset.x;
			int n2Y = e.getTo().getCentre().y+offset.y;
			int tipX = 0;
			int tipY = 0;
				tipX = (int)(n2X+(n1X-n2X)/1.5); // 1.5 puts the arrow 1/3 down the edge, avoiding the label
				tipY = (int)(n2Y+(n1Y-n2Y)/1.5);
			
			Point2D.Double tipPoint = new Point2D.Double(tipX,tipY);

			double angle = pjr.graph.Util.calculateAngle(n1X, n1Y, n2X, n2Y);
			double reverseAngle = angle-180;
			double line1Angle = reverseAngle - et.getArrowAngle()/2;
			double line2Angle = reverseAngle + et.getArrowAngle()/2;
			
			Point2D.Double line1Point = pjr.graph.Util.movePoint(tipPoint,et.getArrowLength(),line1Angle);
			Point2D.Double line2Point = pjr.graph.Util.movePoint(tipPoint,et.getArrowLength(),line2Angle);
			
			Line2D.Double arrowLine1Shape = new Line2D.Double(tipPoint,line1Point);
			Line2D.Double arrowLine2Shape = new Line2D.Double(tipPoint,line2Point);
			g2.draw(arrowLine1Shape);
			g2.draw(arrowLine2Shape);
		}
		
		// draw the label if required
		if(!e.getLabel().equals("") && showEdgeLabel) {
			
			Point p1 = e.getFrom().getCentre();
			Point p2 = e.getTo().getCentre();
			ArrayList<Point> bends = e.getBends();
			if(bends.size() == 1) {
				p2 = bends.get(0);
			} else if(bends.size() > 1) {
				int index = bends.size()/2;
				p1 = bends.get(index-1);
				p2 = bends.get(index);
			}

			int x1 = p1.x+offset.x;
			int y1 = p1.y+offset.y;
			int x2 = p2.x+offset.x;
			int y2 = p2.y+offset.y;
			int x = 0;
			int y = 0;
			if(x1-x2 > 0) {
				x = x2+(x1-x2)/2;
			} else {
				x = x1+(x2-x1)/2;
			}
			if(y1-y2 > 0) {
				y = y2+(y1-y2)/2;
			} else {
				y = y1+(y2-y1)/2;
			}

			if(jiggleLabels) {
				int rX = random.nextInt(jiggleMovement+1);
				rX -= jiggleMovement/2;
				int rY = random.nextInt(jiggleMovement+1);
				rY -= jiggleMovement/2;
				
				x += rX;
				y += rY;
			}

			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(e.getLabel(), font, frc);

			g2.setColor(PANELBACKGROUNDCOLOR);
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+x-2, bounds.getY()+y-2, bounds.getWidth()+4,bounds.getHeight()+4);
			g2.fill(bounds);

			if(!selection.contains(e)) {
				g2.setColor(et.getTextColor());
			} else {
				g2.setColor(et.getSelectedTextColor());
			}
			labelLayout.draw(g2,x,y);
		}

	}
	
	
	public void fitGraphInPanel() {
		int padding = WellFormedConcreteDiagram.OUTER_FACE_TRIANGULATION_BOUNDARY+5; // this fits a triangulated graph in the panel to the triangulation values
		fitGraphInPanel(padding);
	}
	
	public void fitGraphInPanel(int padding) {
		int width = getWidth();
		if(width == 0) {
			width = 500;
		}
		int height = getHeight();
		if(height == 0) {
			height = 500;
		}
		getDualGraph().fitInRectangle(padding,padding,width-padding,height-padding);
	}
	
	
	public void fitContoursInWindow() {
		fitContoursInWindow(CONTOURBORDERPADDING);
	}
	
	
	/** Fit the contours in the window */
	public void fitContoursInWindow(int padding) {

		DualGraph dg = getDualGraph();

		ConcreteDiagram cd = getConcreteDiagram();
		if(cd == null) {
			fitGraphInPanel();
			return;
		}
		DualGraph cloneGraph = cd.getCloneGraph();
		if(cloneGraph == null) {
			fitGraphInPanel();
			return;
		}
		
		int x1 = padding;
		int y1 = padding;
		int x2 = getWidth()-padding;
		int y2 = getHeight()-padding;

		int width = ConcreteDiagram.findContoursWidth(cd.getConcreteContours());
		int height = ConcreteDiagram.findContoursHeight(cd.getConcreteContours());
		int requiredX = x2-x1;
		int requiredY = y2-y1;
		
		int centreX = x1 + requiredX/2;
		int centreY = y1 + requiredY/2;

		double xRatio = requiredX/(double)width;
		double yRatio = requiredY/(double)height;
		// scale to the ratio that will not take the graph over the limits
		double scaleFactor = xRatio;
		if(yRatio < xRatio) {
			scaleFactor = yRatio;
		}
		
		Point currentCentre = ConcreteDiagram.findContoursCentre(cd.getConcreteContours());
		int moveX = centreX - currentCentre.x;
		int moveY = centreY - currentCentre.y;
		
		cloneGraph.scale(scaleFactor);
		cloneGraph.moveGraph(moveX,moveY);
		dg.scale(scaleFactor);
		dg.moveGraph(moveX,moveY);
		

	}

	
	/** Draws a node on the graphics */
	public void paintNode(Graphics2D g2, Node n) {

		NodeType nt = n.getType();
		Point centre = n.getCentre();

		if(!selection.contains(n)) {
			g2.setColor(nt.getFillColor());
		} else {
			g2.setColor(nt.getSelectedFillColor());
		}
		if(!selection.contains(n)) {
			g2.setStroke(nt.getStroke());
		} else {
			g2.setStroke(nt.getSelectedStroke());
		}

		Shape nodeShape = n.generateShape();
		g2.fill(nodeShape);

		if(!selection.contains(n)) {
			g2.setColor(nt.getBorderColor());
		} else {
			g2.setColor(nt.getSelectedBorderColor());
		}

		g2.draw(nodeShape);

		if(!n.getLabel().equals("") && showNodeLabel && !nt.getHideLabels()) {
			if(!selection.contains(n)) {
				g2.setColor(nt.getTextColor());
			} else {
				g2.setColor(nt.getSelectedTextColor());
			}

			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,LABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(n.getLabel(), font, frc);

			Rectangle2D labelBounds = labelLayout.getBounds();
			int labelX = (int)Math.round(centre.x-(labelBounds.getWidth()/2));
			int labelY = (int)Math.round(centre.y+(labelBounds.getHeight()/2));

			labelLayout.draw(g2,labelX,labelY);
		}
	}


	/**
	 * This converts two points to a rectangle, with first two
	 * coordinates always the top left of the rectangle
	 */
	public Shape convertPointsToRectangle (Point p1, Point p2) {
		int x1,x2,y1,y2;

		if (p1.x < p2.x) {
			x1 = p1.x;
			x2 = p2.x;
		} else {
			x1 = p2.x;
			x2 = p1.x;
		}
		if (p1.y < p2.y) {
			y1 = p1.y;
			y2 = p2.y;
		} else {
			y1 = p2.y;
			y2 = p1.y;
		}
		return (new Rectangle2D.Double(x1,y1,x2-x1,y2-y1));

	}


	public void mouseClicked(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}

		// left button only
		if (!SwingUtilities.isLeftMouseButton(event)) {
			selection.clear();
			repaint();
			return;
		}
		selectNode = dualGraph.getNodeNearPoint(event.getPoint(),1);
		if (selectNode == null) {

			selectEdge = dualGraph.getEdgeNearPoint(event.getPoint(),3);
			if (selectEdge == null) {
				// no node or edge selected so add a node on double click
				if (event.getClickCount() > 1) {
					dualGraph.addNode(new Node("",new Point(event.getPoint())));
					resetDiagram();
					selection.clear();
				} else {
					// single click might have been a missed selection
					if (!event.isControlDown()) {
						selection.clear();
					}
				}
				repaint();
			} else {
				if (event.getClickCount() == 1) {
					// edge selected, so add it to the selection
					if (!event.isControlDown()) {
						selection.clear();
					}
					selection.addEdge(selectEdge);
					repaint();
				} else {
					// edit edge dialog on double click
					ArrayList<Edge> el = new ArrayList<Edge>();
					el.add(selectEdge);
					editEdges(el);
				}
			}
		} else {
			if (event.getClickCount() == 1) {
				// node selected
				if (!event.isControlDown()) {
					selection.clear();
				}
				selection.addNode(selectNode);
				repaint();
			} else {
				// edit node dialog on double click
				ArrayList<Node> nl = new ArrayList<Node>();
				nl.add(selectNode);
				editNodes(nl);
			}
			selectNode = null;
		}
		event.consume();
	}
	
	
	/** Call this to edit nodes in the graph panel */
	public void editNodes(ArrayList<Node> nodes) {
		if(nodes.size() == 0) {
			return;
		}
		new EditNodeDialog(nodes,this,containerFrame,selection);

		resetDiagram();
	}
	
	
	/** Call this to edit edges in the graph panel */
	public void editEdges(ArrayList<Edge> edges) {
		if(edges.size() == 0) {
			return;
		}
		new EditEdgeDialog(edges,this,containerFrame,selection);
		resetDiagram();
	}
	
	
	/** Call this to edit all edge types */
	public void editEdgeTypes() {
		new ManageEdgeTypesDialog(this,containerFrame);
		repaint();
	}
	

	/** Call this to edit all node types */
	public void editNodeTypes() {
		new ManageNodeTypesDialog(this,containerFrame);
		repaint();
	}
	
	/** Call this to allow the user to add an edge bend to the selected edge by clicking on the panel. */
	public void addEdgeBend() {

		if(selection.getEdges().size() != 1) {
			return;
		}
		addBendEdge = (Edge)selection.getEdges().get(0);
		addBendPoint1 = addBendEdge.getFrom().getCentre();
		ArrayList<Point> bends = addBendEdge.getBends();
		if(bends != null && bends.size() != 0) {
			addBendPoint1 = bends.get(bends.size()-1);
		}
		addBendPoint2 = addBendEdge.getTo().getCentre();
		
		addMouseMotionListener(this);
		
		repaint();
	}


	/** Call this to remove all edge bends from selected edges */
	public void removeEdgeBends() {
		for(Edge e : selection.getEdges()) {
			e.removeAllBends();
		}
		repaint();
	}


	public void mousePressed(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}
		requestFocus();
		pressedPoint = event.getPoint();
		lastPoint = event.getPoint();
		addMouseMotionListener(this);
		if (SwingUtilities.isLeftMouseButton(event)) {

			if (addBendEdge != null) {
				addBendEdge.addBend(event.getPoint());
				addBendEdge = null;
				addBendPoint1 = null;
				addBendPoint2 = null;
				sharedBendPoint = null;
				repaint();
				event.consume();
				return;
			}
			
			Node chosenNode = dualGraph.getNodeNearPoint(pressedPoint,1);
			if(chosenNode != null) {
				if(selection.contains(chosenNode)) {
					// if its a selected node then we are dragging a selection
					dragSelectionFlag = true;
				} else {
					// otherwise just drag the node
					dragNode = chosenNode;
				}
			} else {
				// no node chosen, so drag an area selection
				dragSelectPoint = new Point(event.getPoint());
			}
			dualGraph.moveNodeToEnd(dragNode);
			repaint();
		}
		if (SwingUtilities.isRightMouseButton(event)) {
			newEdgeNode = dualGraph.closestNode(pressedPoint);
			dualGraph.moveNodeToEnd(newEdgeNode);
			newEdgePoint = new Point(event.getPoint());
			repaint();
		}
		event.consume();
	}



	public void mouseReleased(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}

		removeMouseMotionListener(this);
		if(pressedPoint.distance(event.getPoint()) < 1) {
			// dont do anything if no drag occurred
			dragSelectionFlag = false;
			dragNode = null;
			dragSelectPoint = null;
			newEdgeNode = null;
			newEdgePoint = null;
			return;
		}

		addBendEdge = null;
		addBendPoint1 = null;
		addBendPoint2 = null;
		sharedBendPoint = null;

		// select all in the area
		if (dragSelectPoint != null) {

			// if no control key modifier, then replace current selection
			if (!event.isControlDown()) {
				selection.clear();
			}

			Shape r = convertPointsToRectangle(pressedPoint,event.getPoint());

			for(Node node : dualGraph.getNodes()) {
				Point centre = node.getCentre();

				if(r.contains(centre) && !selection.contains(node)) {
					selection.addNode(node);
				}
			}

			for(Edge edge : dualGraph.getEdges()) {
				Rectangle edgeBounds = edge.shape().getBounds();

				//rectangles with zero dimension dont get included, so quick hack
				edgeBounds.grow(1,1);

				if(r.contains(edgeBounds) && !selection.contains(edge)) {
					selection.addEdge(edge);
				}
			}

			dragSelectPoint = null;
			repaint();
		}

		// finish the selection drag
		if (dragSelectionFlag) {
			dragSelectionFlag = false;
			repaint();
		}

		// finish the node drag
		if (dragNode != null) {
			dragNode = null;
			repaint();
		}

		// finish adding an edge
		if (newEdgeNode != null) {
			Node toNode = dualGraph.closestNode(event.getPoint());
			// dont add a self sourcing edge
			if (newEdgeNode != toNode) {
				dualGraph.addEdge(new Edge(newEdgeNode,toNode));
			}
			newEdgeNode = null;
			newEdgePoint = null;
			dualGraph.moveNodeToEnd(toNode);
			repaint();
		}
		event.consume();
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseDragged(MouseEvent event) {


		if(!showGraph) {
//			event.consume();
			return;
		}
		
		if (dragSelectPoint != null) {
			dragSelectPoint = event.getPoint();
			repaint();
		}

		if (newEdgePoint != null) {
			newEdgePoint = event.getPoint();
			repaint();
		}

		if (dragSelectionFlag) {
			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			for(Node n : selection.getNodes()) {
				Point centre = n.getCentre();
				centre.setLocation(centre.x+deltaX,centre.y+deltaY);
			}
			lastPoint = event.getPoint();

			repaint();
		}

		if (dragNode != null) {

			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;

			Point centre = dragNode.getCentre();
			centre.setLocation(centre.x+deltaX,centre.y+deltaY);

			lastPoint = event.getPoint();

			repaint();
		}
//		event.consume();
	}


	public void mouseMoved(MouseEvent event) {
		if(addBendPoint1 != null && addBendPoint2 != null) {
			sharedBendPoint = event.getPoint();
			repaint();
		}
//		event.consume();
	}

	public void keyTyped(KeyEvent event) {
	}


	public void keyPressed(KeyEvent event) {
	}


	public void keyReleased(KeyEvent event) {

		// this stuff would be in keyTyped, but it doesnt register delete
		if((event.getKeyChar() == KeyEvent.VK_BACK_SPACE) || (event.getKeyChar() == KeyEvent.VK_DELETE)) {
			dualGraph.removeEdges(selection.getEdges());
			dualGraph.removeNodes(selection.getNodes());
			selection.clear();
			resetDiagram();
			repaint();
		}
	}

	public void resetDiagram() {
		if(dualGraph.getContourDuplicateLabelMap() != null) {
			dualGraph.restoreRenamedContours();
		}
		setConcreteDiagram(null);
		dualGraph.setContourDuplicateLabelMap(null);
		dualGraph.setContourHoleLabelMap(null);
		
	}

}



