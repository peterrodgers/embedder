package euler.library;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import euler.ConcreteContour;
import euler.DiagramPanel;
import euler.construction.ConstructedConcreteDiagram;

public class ThumnailEulerDiagramPanel extends JPanel implements MouseListener{

	protected ArrayList<ConstructedConcreteDiagram> constructedConcreteDiagrams = null;	
	public static ArrayList<Area> areas = new ArrayList<Area>();
	public static ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	public static ArrayList<String> strings = new ArrayList<String>();
	public static ArrayList<Point> stringPoints = new ArrayList<Point>();
	protected int numberOfRows = 0;	
	protected LibraryThumnailView parentFrame = null;
	
	
	public ThumnailEulerDiagramPanel(ArrayList<ConstructedConcreteDiagram> ccd, LibraryThumnailView lv) {
		super();
		constructedConcreteDiagrams = ccd;
		numberOfRows = ccd.size()/3+1;
		setPreferredSize(new Dimension(620, numberOfRows*200));
		addMouseListener(this);
		this.parentFrame = lv;
	}
	
	protected void setup() {	
		setBorder(new javax.swing.border.LineBorder(Color.BLACK,1)); 	
	}
	public void setConstructedConcreteDiagram(ArrayList<ConstructedConcreteDiagram> ccd){
		this.constructedConcreteDiagrams = ccd;
	}
	
	public ArrayList<ConstructedConcreteDiagram> getConstructedConcreteDiagram() {
		return constructedConcreteDiagrams;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;	
		int startX = 0;
		int startY = 0;
	 	for(ConstructedConcreteDiagram ccd : constructedConcreteDiagrams){
			ArrayList<ConcreteContour> ccs =  ccd.getConcreteContours();
			String s = ccd.getAbstractDescription();
			paintContours(g2, startX, startY,ccs,s);
			startX+=200;
			if(startX==600){
				startX=0;
				startY+=200;
			}
	 	}	
	}

	protected void paintContours(Graphics2D g2, int startX, int startY, 
		ArrayList<ConcreteContour> ccs, String aDescription) {
		
		ArrayList<ConcreteContour> ccs0 = new ArrayList<ConcreteContour>();
		g2.setBackground(Color.white);
		
		for(ConcreteContour cc : ccs) {
			Polygon pol = new Polygon();
			for(int i = 0 ; i < cc.getPolygon().npoints; i++){
				pol.addPoint(cc.getPolygon().xpoints[i]/4+startX, cc.getPolygon().ypoints[i]/4+startY);
			}
			String abs = cc.getAbstractContour();
			ConcreteContour cc0 = new ConcreteContour(abs,pol);	
			ccs0.add(cc0);
		}
		Font font = new Font(DiagramPanel.LABELFONTNAME,DiagramPanel.LABELFONTSTYLE,DiagramPanel.CONTOURLABELFONTSIZE);
		Color solidColor;
		
		for(ConcreteContour cc0 : ccs0) {
			String label = cc0.getAbstractContour();
			char contourChar = label.charAt(0);
			solidColor = DiagramPanel.getColorFromChar(contourChar);					
			Area area = new Area(cc0.getPolygon());
			if(area != null) {
				Color transparentColor = new Color(solidColor.getRed()/255,solidColor.getGreen()/255,solidColor.getBlue()/255,0.2f);
				g2.setColor(transparentColor);
				g2.fill(area);
			}			

			g2.setColor(solidColor);
			BasicStroke basicStroke = DiagramPanel.getStrokeFromChar(contourChar);
			g2.setStroke(basicStroke);					
			g2.drawPolygon(cc0.getPolygon());
			FontRenderContext frc = g2.getFontRenderContext();
			TextLayout labelLayout = new TextLayout(label, font, frc);	
			Point2D.Double labelPoint = pjr.graph.Util.getTopLeftMostPolygonPoint(cc0.getPolygon());
						
			int labelX = pjr.graph.Util.convertToInteger(labelPoint.x);
			int labelY = pjr.graph.Util.convertToInteger(labelPoint.y);
							
			Rectangle2D bounds = labelLayout.getBounds();
			bounds.setRect(bounds.getX()+labelX-2, bounds.getY()+labelY-2, bounds.getWidth()+4,bounds.getHeight()+4);
						
			g2.setColor(DiagramPanel.PANELBACKGROUNDCOLOR);
			g2.fill(bounds);
			g2.setColor(solidColor);
			labelLayout.draw(g2,labelX,labelY);
		}
		g2.setColor(Color.GRAY);
		g2.drawRect(startX,startY, 200,200);
		g2.setFont(font);
		g2.drawString(aDescription, startX+15, startY+15);
	}
	public Dimension getPreferedSize(){
		return new Dimension(820, numberOfRows*200);	
	}
	public void mouseReleased(MouseEvent event) {
		
	}
	public void mousePressed(MouseEvent event) {
	
	}	
	public void mouseExited(MouseEvent event) {
		
		
	} 
	 public void mouseClicked(MouseEvent event) {
		 Point p = event.getPoint();
		 int col = (int)(p.getX()/200);
		 int row = (int)(p.getY()/200);	
		 int idx = row*3+col;
		 //System.out.println(idx);
		 parentFrame.updatePareantFrame(idx);
		 highlightArea(row, col);
	 }
	 public void mouseEntered(MouseEvent event){
		
		
	 }
	 public void highlightArea(int row, int col){		
		 Graphics g = this.getGraphics();	
		 this.paintComponent(g);
		 Graphics2D g2 = (Graphics2D) g;	
		 g2.setColor(Color.YELLOW);
		 g2.setStroke(new BasicStroke(3));
		 g2.drawRect(col*200,row*200, 200, 200);	
		 revalidate();		 
	 }

}



