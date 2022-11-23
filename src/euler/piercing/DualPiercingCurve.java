package euler.piercing;

public class DualPiercingCurve extends PiercingCurve{


	protected String dual1;
	protected String dual2;
	
	public DualPiercingCurve(String pc1, String pc2, String pc3, String outer){
		label = pc1;
		dual1 = pc2;
		dual2 = pc3;
		outerCurves = outer;
	}	

	public String getDual1(){return dual1;}
	public String getDual2(){return dual2;}
	
	public void print(){
		System.out.println("Double piercing " + label + " 1." + dual1 
				+ " 2. " +dual2  + " outer " + outerCurves +" inside curves " 
				+ insideCurves  + " radius " + radius );
	}
	
}
