package euler.piercing;

import java.awt.Polygon;
import euler.polygon.RegularPolygon;

public class Circle {
	Polygon circle = null;
	int centreX = 0;
	int centreY = 0;
	int radius = 0;
	
	public Circle(int aRadius, int cX, int cY){
		circle = RegularPolygon.generateRegularPolygon(cX, cY, aRadius, 20);
		centreX = cX;
		centreY = cY;
		radius = aRadius;	
	}
	public Polygon getCircle(){return circle;}
	public int getCentreX(){return centreX;}
	public int getCentreY(){return centreY;}
	public int getRadius(){return radius;}

}
