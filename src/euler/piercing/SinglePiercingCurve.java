package euler.piercing;

public class SinglePiercingCurve extends PiercingCurve{	
	
	protected Circle circle = null;
	protected String piercedTo = "";
		
	protected int numCurvesAdded = 0;
	protected double unitDegree = 0;
	protected int numOfUnits = 0;
	
	public SinglePiercingCurve(String aLabel,String curvePiercedTo, String outerCurveList){
		label = aLabel;
		piercedTo = curvePiercedTo;
		outerCurves = outerCurveList;		
	}	

	public void setPiercedTo(String aPiercedTo){piercedTo = aPiercedTo;}
	public String getPiercedTo(){return piercedTo;}


	public Circle getCircle(){return circle;}	
	public void setCurvesAdded(int num){numCurvesAdded = num;}

	public int getNumOfCurvesAdded(){return numCurvesAdded;}
	

	public void incrementNumCurvesAdded(){numCurvesAdded++;}
		
	
	public void addPiercingCurve(String s){
		if(!piercingCurves.contains(s))
		piercingCurves+=s;
	}
	public void addOutsideCurve(String s){outerCurves+=s;}
	
	public void setUnitDegree(double degree){
		unitDegree = degree;
	}
	public double getUnitDegree(){return unitDegree;}
	public void setNumOfUnits(int num){numOfUnits = num;}
	public int getNumOfUnits(){return numOfUnits;}
	
	public void print(){		
	System.out.println("Single piercing "+ label + " pierced to " + piercedTo 
			+ "  piercing list " + piercingCurves
				+" outer curves " + outerCurves + " inside curves "
				+ insideCurves + " radius " + radius );
	}


}
