package euler.piercing;

import java.util.ArrayList;

public class PiercingCurve {	
	
	protected String label = "";
	protected double radius = 0;
	protected int centreX = 0;
	protected int centreY = 0;
	protected boolean isDualPiercing = false;
	protected String outerCurves = "";
	protected String insideCurves = "";		
	protected double startDegree = 0;
	protected int noOfUnits = 0;
	protected double unitDegree = 0;	
	protected ArrayList<Pie> avaliableSections = new ArrayList<Pie>();
	protected String piercingCurves = "";
	public void setLabel(String aLabel){label = aLabel;}
	public String getlabel(){return label;}	
	public void setCentre(int cX, int cY){
		centreX = cX;
		centreY = cY;	
	}	
	public String getPiercingCurves(){return piercingCurves;}
	public void addPiercingCurve(String s){piercingCurves+=s;}
	public void setNoOfUnits(int noUnits){noOfUnits = noUnits;}
	public int getNumberOfUnits(){return noOfUnits;}
	public int getCentreX(){return centreX;}
	public int getCentreY(){return centreY;}
	public double getStartDegree(){return startDegree; }
	public void setRadius(double aRadius){
		radius = aRadius;
	}
	public void setStartDegree(double aDegree){
		startDegree = aDegree%(Math.PI*2);
	}
	public void setUnitDegree(double degree){unitDegree = degree;}
	public double getUnitDegree(){return unitDegree;}
	public double getRadius(){return radius;}
	public void setIsDualPiercing(boolean dual){isDualPiercing = dual;}
	public void addOuterCurve(String s){outerCurves+=s;}
	public void setOuterCurves(String outerCurveList){outerCurves = outerCurveList;}
	public boolean isDual(){return isDualPiercing;}
	public String getOuterCurves(){return outerCurves;}
	public String getInsideCurves(){return insideCurves;}
	
	public void print(){		
	System.out.println("piercing "+ label +  
			" centre (" + centreX +","+ centreY+ ") "+" radius " + radius );
	}	
	
	public void addInsideCurve(String s){
		if(!insideCurves.contains(s))
		insideCurves+=s;
	}	
	
 	public static double angle(double x1, double y1, double x2, double y2){
  		double rise = y1 - y2;
  		double run = x1 - x2;
  		double angle = Math.atan2(rise, run);
  		if(angle <0){
  			angle = (Math.PI*2) + angle;
  		}
  		return angle; 
    } 	
 	public static double[][] intersect( PiercingCurve c1,PiercingCurve c2 ) {

	    double ret [][] = new double[2][2];
		double dx = c1.getCentreX() - c2.getCentreX();
		double dy = c1.getCentreY() - c2.getCentreY();
		double d2 = dx*dx + dy*dy;
		double d = Math.sqrt( d2 );

		if ( d>c1.getRadius()+c2.getRadius() || d< Math.abs(c1.getRadius()-c2.getRadius()) ) 
			return null; // no solution
		

		double a = (c1.getRadius()*c1.getRadius() - c2.getRadius()*c2.getRadius() + d2) / (2*d);
		double h = Math.sqrt( c1.getRadius()*c1.getRadius() - a*a );
		double x2 = c1.getCentreX() + a*(c2.getCentreX() - c1.getCentreX())/d;
		double y2 = c1.getCentreY() + a*(c2.getCentreY() - c1.getCentreY())/d;

	
		double paX = x2 + h*(c2.getCentreY() - c1.getCentreY())/d;
		double paY = y2 - h*(c2.getCentreX() - c1.getCentreX())/d;
		double pbX = x2 - h*(c2.getCentreY() - c1.getCentreY())/d;
		double pbY = y2 + h*(c2.getCentreX() - c1.getCentreX())/d;
		
		ret[0][0] = paX;
		ret[0][1] = paY;
		ret[1][0] = pbX;
		ret[1][1] = pbY;
		
		return ret;
	}

}
