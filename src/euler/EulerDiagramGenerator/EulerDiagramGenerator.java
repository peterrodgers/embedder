package euler.EulerDiagramGenerator;

import java.util.ArrayList;

import euler.AbstractDiagram;
import euler.piercing.DualPiercingCurve;
import euler.piercing.PiercingCurve;
import euler.piercing.PiercingDiagram;
import euler.piercing.SinglePiercingCurve;

public class EulerDiagramGenerator {
	
	protected String abstractDescription;
	protected boolean isPiercing;
	protected PiercingDiagram piercingDiagram;
	protected ArrayList<PiercingCurve> piercingCurves;
	protected String abstractDescriptionWithoutPiercing;
	
	public static void main(String[] args) {
	
	  	String abs ="a b c ab ac bc abc d ad s";
	  	EulerDiagramGenerator eg = new EulerDiagramGenerator (abs);
	  	eg.checkIsPiercing();
	}	
	
	public EulerDiagramGenerator(String abstractDescription){
		this.abstractDescription = abstractDescription;
	}
	
	public void checkIsPiercing(){
		PiercingDiagram pd = new PiercingDiagram(abstractDescription);
		isPiercing = pd.IsPiercingDiagram();
		piercingDiagram = pd;
		piercingCurves = pd.getPiercingCurves();
		if(piercingCurves.size()!=0){
			for(PiercingCurve pc: piercingCurves){
				if(pc.isDual()){
					DualPiercingCurve c = (DualPiercingCurve)pc;
					c.print();
				}
				else{
					SinglePiercingCurve c = (SinglePiercingCurve)pc;
					c.print();
				}
			}
		}
		if(!isPiercing){
			AbstractDiagram ad = new AbstractDiagram(abstractDescription);
			for(PiercingCurve pc : piercingCurves){
				ad = PiercingDiagram.removeCurve(pc.getlabel(),ad);
			}
			System.out.println(ad.getAbstractDescription());
		}
			
	}
	
 

	
	
	
	
	
	
	

}
