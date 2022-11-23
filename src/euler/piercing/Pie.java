package euler.piercing;

public class Pie {	
	protected double startDegree;
	protected double endDegree;
	
	public Pie(double start, double end){
	
		startDegree = start %(Math.PI*2);
		endDegree = end %(Math.PI*2);
		if( endDegree == 0){
			endDegree = Math.PI*2;
		}
		if(startDegree == Math.PI*2){
			startDegree = 0;
		}
	}
	public void setStartDegree(double degree){
		startDegree = degree;
	}
	public void setEndDegree(double degree){
		endDegree = degree;
	}	
	public double getStartDegree(){
		return startDegree;
	}
	public double getEndDegree(){
		return endDegree;
	}
	public double getSize(){
		if(startDegree<endDegree){
			return (endDegree-startDegree);
		}
		else{
			return (endDegree + Math.PI*2-startDegree);
		}
	}
	public boolean contains(double degree){
		if(endDegree > startDegree){
			if(degree<endDegree && degree>startDegree)
				return true;
		}
		if(startDegree > endDegree){
			if(degree>startDegree || degree <endDegree)
				return true;
		}
		
		return false;
	}
	public double getMiddle(){
		if(startDegree<endDegree){
			return (endDegree-startDegree)/2;
		}
		else{
			return(endDegree+Math.PI*2 - startDegree);
		}
	}	
	
}
