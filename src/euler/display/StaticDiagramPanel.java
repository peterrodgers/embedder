package euler.display;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;
import euler.*;

public class StaticDiagramPanel extends JPanel {
	
	protected static final Color PANELBACKGROUNDCOLOR = Color.WHITE;
	
	ArrayList<WellFormedConcreteDiagram> concreteDiagrams = null;
	
	public StaticDiagramPanel(ArrayList<WellFormedConcreteDiagram> concreteDiagrams, Frame inContainerFrame) {
		super();
		this.concreteDiagrams = concreteDiagrams;
		setBackground(PANELBACKGROUNDCOLOR);
	}
	

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//paint background
		super.paintComponent(g2);
		
		for(WellFormedConcreteDiagram cd : concreteDiagrams) {
			int tX = 0;
			int tY = 0;
			ArrayList<Rectangle2D> avoidRectangles = new ArrayList<Rectangle2D>();
			ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
			for(ConcreteContour cc : ccs) {
				Rectangle2D r = DiagramPanel.paintContour(g2, cd.getDualGraph(), cc, true, true, false, 0, ccs, avoidRectangles);
				avoidRectangles.add(r);

				tX += 2;
				tY += 2;
			}
// This bit shows the rectangle inside the chosen zone
/*
HashMap<String,Area> zoneAreas = ConcreteContour.generateZoneAreas(cd.getConcreteContours());
for(String zone : zoneAreas.keySet()) {
	if(!zone.equals("a")) {
		continue;
	}
	Area a = zoneAreas.get(zone);
	ArrayList<Polygon>  ps = ConcreteContour.polygonsFromArea(a);
	Polygon p = ps.get(0);
	
	
	euler.rectangle.MaxRectangleFinder maxRectangleFinder = new euler.rectangle.MaxRectangleFinder(p);
	Rectangle r = maxRectangleFinder.getMaxSquare();
	if(r == null) {
		// nasty fix to prevent a crash
		r = p.getBounds();
	}
	BasicStroke basicStroke = new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,0,new float[] {9},0);
	g2.setStroke(basicStroke);
	g2.setColor(Color.black);
	g2.draw(r);
}
*/
		}
		
		
	}


}
