package euler.library;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import pjr.graph.Node;
import euler.ConcreteContour;
import euler.DiagramPanel;

import euler.construction.ConstructedConcreteDiagram;


public class EulerDiagramPanel extends DiagramPanel implements MouseListener, MouseMotionListener, KeyListener {


	protected ConstructedConcreteDiagram constructedConcreteDiagram = null;	
	protected ConcreteContour selectedContour = null;
	public static ArrayList<Area> areas = new ArrayList<Area>();
	public static ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	public static ArrayList<String> strings = new ArrayList<String>();
	public static ArrayList<Point> stringPoints = new ArrayList<Point>();
	
	public EulerDiagramPanel(ConstructedConcreteDiagram ccd, Applet inApplet) {
		super();
		constructedConcreteDiagram = ccd;
		containerFrame = null;
		containerApplet = inApplet;
		setup();
	}

	public EulerDiagramPanel(ConstructedConcreteDiagram ccd) {
		super();
		constructedConcreteDiagram = ccd;
		containerApplet = null;
		setup();
	}
	
	protected void setup() {		
		setBackground(panelBackgroundColor);
		addMouseListener(this);
		addKeyListener(this);
	}
	public void setConstructedConcreteDiagram(ConstructedConcreteDiagram ccd){
		this.constructedConcreteDiagram = ccd;
	}
	
	public ConstructedConcreteDiagram getConstructedConcreteDiagram() {
		return constructedConcreteDiagram;
	}

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
			Color c = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.3f);
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
			g2.setColor(Color.blue);
			g2.draw(p);
			i++;
		}
		for(int j = 0; j< strings.size(); j++) {
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
	public void resetDiagram(ConstructedConcreteDiagram ccd) {
		constructedConcreteDiagram = ccd;
	}
	public void mouseDragged(MouseEvent event) {

		if(!showGraph) {
			event.consume();
			return;
		}		
		if (SwingUtilities.isLeftMouseButton(event)) {
			int deltaX = event.getX()-lastPoint.x;
			int deltaY = event.getY()-lastPoint.y;
			lastPoint = event.getPoint();
			if (selectedContour!=null) {			
				selectedContour.getPolygon().translate(deltaX, deltaY);			
				selectedContour.getPolygon().translate(deltaX, deltaY);	
				lastPoint = event.getPoint();	
				repaint();		
			}		
		}
		if (SwingUtilities.isRightMouseButton(event)){
			lastPoint = event.getPoint();
			double minDis = Double.MAX_VALUE;
			//System.out.println(minDis);
			ConcreteContour cToMove = null;
			int pIdx = 0;
			for(ConcreteContour cc :constructedConcreteDiagram.getConcreteContours()){
				Polygon pol = cc.getPolygon();
				for(int i = 0 ; i < pol.npoints;i++){
					double dis = pjr.graph.Util.distance(lastPoint.getX(), lastPoint.getY(), pol.xpoints[i], pol.ypoints[i]);
					if(dis<minDis){
						minDis = dis;
						cToMove = cc;
						pIdx = i;
					}
				}
			}
			
			if(cToMove!=null){
				//System.out.println(cToMove.getAbstractContour());
				cToMove.getPolygon().xpoints[pIdx] = (int)lastPoint.getX();
				cToMove.getPolygon().ypoints[pIdx] = (int)lastPoint.getY();
				repaint();				
			}
			
			repaint();	
		 
		}

		event.consume();
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
		ConcreteContour cc = constructedConcreteDiagram.getContourNearPoint(pressedPoint);	
		if(cc != null) {
			selectedContour = cc;				
		}
		repaint();		
		event.consume();
	} 
	 public void mouseClicked(MouseEvent event){
			
		 if(!showGraph) {
				event.consume();
				return;
			}
			requestFocus();
			pressedPoint = event.getPoint();
			lastPoint = event.getPoint();
			addMouseMotionListener(this);
			ConcreteContour cc = constructedConcreteDiagram.getContourNearPoint(pressedPoint);	
			if(cc != null) {
				selectedContour = cc;				
			}
			new EditContourDialog(this,null);
			repaint();		
			event.consume();
	 }
	 
	 
	public void mouseReleased(MouseEvent event) {		
		event.consume();
	}

	
	


}



