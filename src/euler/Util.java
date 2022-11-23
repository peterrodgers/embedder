package euler;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.util.*;

import pjr.graph.*;

public class Util {
	
	public static String logFile = "log.txt";

	public static boolean reportErrors = true;
	
	public static void outError(String err) {
		if(reportErrors) {
			System.out.println("ERROR: "+err);
		}
	}
	
	
	/**
	 * Returns a number of rectangles equal to or greater than
	 * the number given, these evenly divide up the given one.
	 * The sides are either even or one number apart. If not equal,
	 * the largest number is along the longest side. The returned
	 * rectangles read along then down.
	 */
	public static ArrayList<Rectangle> divideIntoRectangles(Rectangle r, int number) {
		ArrayList<Rectangle> ret = new ArrayList<Rectangle>();
		
		if(number <= 0) {
			return null;
		}
		
//		if(number == 1) {
//			ret.add(new Rectangle(r));
//			return ret;
//		}
		
		double sqrt = Math.sqrt(number);
		int sqrtInt = (int)(sqrt+0.5); // make sure the number rounds up
		while(sqrtInt*sqrtInt < number) {
			sqrtInt++;
		}
		int xRectangles = sqrtInt;
		int yRectangles = sqrtInt;
		if(sqrtInt*(sqrtInt-1)>= number) {
			// uneven numbers are closer
			if(r.width < r.height) {
				xRectangles = sqrtInt-1;
			} else {
				yRectangles = sqrtInt-1;
			}
		}
		
		int xGap = r.width/xRectangles;
		int yGap = r.height/yRectangles;
		
		for(int yCount = 0; yCount < yRectangles; yCount++) {
			int y = r.y + yCount*yGap;
			for(int xCount = 0 ; xCount < xRectangles; xCount++) {
				int x = r.x + xCount*xGap;
				Rectangle subR = new Rectangle(x,y,xGap,yGap);
//System.out.println("x "+x+" y "+y+" width "+xGap+" height "+yGap);
				ret.add(subR);
			}
		}
		
		return ret;
		
	}
	
	
	
	public static boolean lineInPolygon(Polygon p, Point p1, Point p2) {
		// test if a line would cross any polygon lines, and that
		// the line is inside the polygon
		boolean lineInPolygon = true;
		for(int i = 0; i < p.npoints; i++) {
			
			int xCurrent = p.xpoints[i];
			int yCurrent = p.ypoints[i];
			
			
			int xNext = p.xpoints[0];
			int yNext = p.ypoints[0];
			if(i+1 != p.npoints) {
				xNext = p.xpoints[i+1];
				yNext = p.ypoints[i+1];
			}
			// touching lines dont cross
			if(xCurrent == p1.getX() && yCurrent == p1.getY()) {continue;}
			if(xCurrent == p2.getX() && yCurrent == p2.getY()) {continue;}

			if(xNext == p1.getX() && yNext == p1.getY()) {continue;}
			if(xNext == p2.getX() && yNext == p2.getY()) {continue;}
			
			if(pjr.graph.Util.linesCross(p1, p2, new Point(xCurrent,yCurrent), new Point(xNext,yNext))) {
				return false;

			}
		}
		if(lineInPolygon) {
			if(p.contains(pjr.graph.Util.midPoint(p1,p2))) {
				return true;
			}
		}
		
		return false;

	}
	
	
	public static boolean log(String logMessage) {
		
		Date date = new Date();
   		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss"); 
   		String dateString = formatter.format(date);
		
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(logFile,true));
	
			// save the graph label
			b.write(dateString+" "+logMessage);
			b.newLine();
			b.close();
		} catch(IOException e) {
			System.out.println("An IO exception occured when executing Euler.Util.log("+logMessage+")\n");
			return false;
		}
		return true;
	}
	
	
	
	public static double computePolygonArea (Polygon p) {

		double area = 0.0;
		for (int i = 0; i < p.npoints - 1; i++) {
			area += (p.xpoints[i] * p.ypoints[i+1]) - (p.xpoints[i+1] * p.ypoints[i]);
		}
		area += (p.xpoints[p.npoints-1] * p.ypoints[0]) - (p.xpoints[0] * p.ypoints[p.npoints-1]);  

		area *= 0.5;

		return area;
	}


	
	
	public static Point2D.Double computePolygonCentroid(Polygon p) {
		double cx = 0.0;
		double cy = 0.0;

		for(int i = 0 ; i < p.npoints-1; i++) {
			double a = p.xpoints[i] * p.ypoints[i+1] - p.xpoints[i+1] * p.ypoints[i];
			cx += (p.xpoints[i] + p.xpoints[i+1]) * a;
			cy += (p.ypoints[i] + p.ypoints[i+1]) * a;
		}
		double a = p.xpoints[p.npoints-1] * p.ypoints[0] - p.xpoints[0] * p.ypoints[p.npoints-1];
		cx += (p.xpoints[p.npoints-1] + p.xpoints[0]) * a;
		cy += (p.ypoints[p.npoints-1] + p.ypoints[0]) * a;
		  
		double area = computePolygonArea(p);

		cx /= 6 * area;
		cy /= 6 * area;	
		
		return new Point2D.Double(cx, cy);
	}  

	

	
}
