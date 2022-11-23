package euler.construction;

import javax.swing.*;

import pjr.graph.*;
import euler.*;
import euler.drawers.*;
import euler.experiments.*;
import euler.polygon.*;
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
public class ConstructedDiagramPanel extends DiagramPanel implements MouseListener, MouseMotionListener, KeyListener {

	public static final int LEFT_CIRCLE_RADIUS = 150;
	public static final int RIGHT_CIRCLE_RADIUS = 100;
	public static final int REGULARPOLYGONPOINTS = 10;

	protected ConstructedConcreteDiagram constructedConcreteDiagram = null;
	
	public DualGraph getDualGraph() {return constructedConcreteDiagram.getDualGraph();}

	public ConstructedDiagramPanel(ConstructedConcreteDiagram ccd, Applet inApplet) {
		super();
		constructedConcreteDiagram = ccd;
		containerFrame = null;
		containerApplet = inApplet;
		setup();
	}

	public ConstructedDiagramPanel(ConstructedConcreteDiagram ccd, Frame inContainerFrame) {
		super();
		constructedConcreteDiagram = ccd;
		containerFrame = inContainerFrame;
		containerApplet = null;
		setup();
	}
	
	protected void setup() {
		
		selection = new GraphSelection(getDualGraph());
		setBackground(panelBackgroundColor);
		addMouseListener(this);
		addKeyListener(this);
	}

	
	public ConstructedConcreteDiagram getConstructedConcreteDiagram() {return constructedConcreteDiagram;}


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


	public void paintComponent(Graphics g) {
		if(forceNoRedraw) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g;
	
		//paint background
		super.superPaintComponent(g2);
		
int i;
i = 0;
for(Area a:areas) {
	if(i >= contourColors.length) {
		i = 0;
	}
	Color solidColor;
	solidColor = contourColors[i];
//	solidColor = Color.red;
	Color c = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.3f);
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

		if(showGraph && getDualGraph() != null) {
			//draw the edges
			if(!separateParallel) {
				paintOverlaidEdges(g2,getDualGraph());
			} else {
				paintSeparateEdges(g2,getDualGraph());
			}
	
			//draw the new edge drag
			if (newEdgePoint != null) {
				g2.setColor(selectedPanelAreaColor);
				Point centre = newEdgeNode.getCentre();
				g2.drawLine(centre.x,centre.y,newEdgePoint.x,newEdgePoint.y);
			}
	
			//draw the nodes
			for(Node n : getDualGraph().getNodes()) {
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

	}
	
	/**
	 * This generates a triangulation of a graph that includes nodes
	 * and edges bordering the original graph that are not in the original
	 * graph
	 */
	protected void paintContours(Graphics2D g2) {
		
		if(showContour) {
			ArrayList<ConcreteContour> ccs = constructedConcreteDiagram.getConcreteContours();
			if(ccs != null) {
				ArrayList<Rectangle2D> avoidRectangles = new ArrayList<Rectangle2D>();
				for(ConcreteContour cc : ccs) {
					Rectangle2D r = paintContour(g2,cc,showContourLabel,showContourAreas,ccs);
					avoidRectangles.add(r);
				}
					
			}
		}
		
		if(showTriangulation) {
			// TODO

		}
		if(showContourLabel) {
			for(ConcreteContour cc : constructedConcreteDiagram.getConcreteContours()) {
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
		
	}

	public static Rectangle2D paintContour(Graphics2D g2, ConcreteContour cc, boolean showContourLabel, boolean showContourAreas, ArrayList<ConcreteContour> ccs) {

		String label = cc.getAbstractContour();
		
		char contourChar = label.charAt(0);
		Color solidColor = getColorFromChar(contourChar);
		if(showContourAreas) {
			Area area = new Area(cc.getPolygon());
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
		
		if(showContourLabel) {
			Font font = new Font(LABELFONTNAME,LABELFONTSTYLE,CONTOURLABELFONTSIZE);
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(label, font, frc);	
			Point2D.Double labelPoint = pjr.graph.Util.getTopLeftMostPolygonPoint(cc.getPolygon());
			
			int labelX = pjr.graph.Util.convertToInteger(labelPoint.x);
			int labelY = pjr.graph.Util.convertToInteger(labelPoint.y);
			
			
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+labelX-2, bounds.getY()+labelY-2, bounds.getWidth()+4,bounds.getHeight()+4);
			
			g2.setColor(PANELBACKGROUNDCOLOR);
			g2.fill(bounds);
			g2.setColor(solidColor);
			labelLayout.draw(g2,labelX,labelY);
			
			return bounds;
		}
		return null;
	}

	

	public void resetDiagram() {		
		constructedConcreteDiagram.generateDualGraphWithDoubledOutsideEdges();		
	}
	
	
	public void addRegularPolygon(Point point, int radius) {
		Polygon rp = RegularPolygon.generateRegularPolygon(point.x, point.y, radius, REGULARPOLYGONPOINTS);
		String label = constructedConcreteDiagram.getNextUnusedLabel();
		constructedConcreteDiagram.addConcreteContour(new ConcreteContour(label,rp));
System.out.println("added regular polygon with label '"+label+"' at "+point+" radius "+radius+" number of points "+REGULARPOLYGONPOINTS);
	}
	
	public void mouseClicked(MouseEvent event) {
		event.consume();
	}
	
	public void mousePressed(MouseEvent event) {
		event.consume();
	}

	public void mouseReleased(MouseEvent event) {
		event.consume();
	}

}



