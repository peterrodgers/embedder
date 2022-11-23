package euler;


/** A contour with links backwards and forwards indicating curve routing. */
public class ContourLink {

	/** The contour */
	protected String contour;
	/** The containing {@link CutPoint} */
	protected CutPoint cutPoint;
	/** The linking cut point in one of the TriangulationEdge faces */
	protected ContourLink prev;
	/** The linking cut point in the other TriangulationEdge face */
	protected ContourLink next;

	public ContourLink(String contour, CutPoint cutPoint, ContourLink prev, ContourLink next) {
		super();
		this.contour = contour;
		this.cutPoint = cutPoint;
		this.prev = prev;
		this.next = next;
		cutPoint.addContourLink(this);
	}
	
	public String getContour() {return contour;}
	public CutPoint getCutPoint() {return cutPoint;}
	public ContourLink getNext() {return next;}
	public ContourLink getPrev() {return prev;}

	public void setContour(String contour) {this.contour = contour;}
	public void setCutPoint(CutPoint cutPoint) {this.cutPoint = cutPoint;}
	public void setNext(ContourLink next) {this.next = next;}
	public void setPrev(ContourLink prev) {this.prev = prev;}

	
	public String toString() {
		return contour;
	}



	
}
