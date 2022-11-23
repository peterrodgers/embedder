package euler;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;


public class ContourLine {
	Line2D.Double contourLine;
	String contourLabel;
	ArrayList<Point2D.Double> crossPoints;
	
	public ContourLine(String label, Line2D.Double line){
		contourLine = line;
		contourLabel = label;
		crossPoints = new ArrayList<Point2D.Double>();
	}
	public String getLabel(){
		return contourLabel;
	}
	public Line2D.Double getLine(){
		return contourLine;
	}
	public ArrayList<Point2D.Double> getCrossPoints(){
		return crossPoints;
	}
	public void setLabel(String label){
		contourLabel = label;
	}
	public void appendLabel(String s){
		if(!contourLabel.contains(s)){
			contourLabel+=s;
		}
	}

	public void addCrossPoint(Point2D.Double p){
		if(!crossPoints.contains(p))
		crossPoints.add(p);
	}
	public boolean equal(ContourLine l){
		if(l.getLabel().compareTo(this.getLabel())==0 && l.getLine().equals(this.contourLine)){
			return true;
		}
		return false;
	}
	public void sortCorssPoints(){
		
		//System.out.println("cross points number = " + crossPoints.size());
		if(crossPoints.size()>1){
			ArrayList<Point2D.Double> sortedPoints = new ArrayList<Point2D.Double>();
			Point2D startP = contourLine.getP1();
			double dis [] = new double[crossPoints.size()];
			for(int i = 0 ; i<crossPoints.size(); i++){
				Point2D p = crossPoints.get(i);
				dis[i] = pjr.graph.Util.distance(startP.getX(), startP.getY(),
						p.getX(), p.getY());
				//System.out.println(p.toString());
			}		
			 Arrays.sort(dis);
			 for(int j = 0 ; j< dis.length; j++){
				 for(Point2D.Double currentP: crossPoints){
					 double d = pjr.graph.Util.distance(startP.getX(), startP.getY(),currentP.getX(),currentP.getY());
					 if(d == dis[j]){						 
						 if(!sortedPoints.contains(currentP)){
							 sortedPoints.add(currentP);
							// System.out.println(dis[j] + " "+ currentP.toString());
						 }
					 }
				 }
			 }
			 
			 
		
			 crossPoints =  sortedPoints;
			 for(Point2D.Double p0: crossPoints){
				//System.out.println(p0.toString()); 
			 }
		}
		
	}
	
	

}
