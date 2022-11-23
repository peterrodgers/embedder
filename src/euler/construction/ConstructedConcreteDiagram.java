package euler.construction;

import java.awt.*;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import pjr.graph.*;
import pjr.graph.triangulator.*;
import euler.*;
import euler.maxrectangle.*;

/**
 * Differs from other ConcreteDiagrams by being created with concrete
 * contours, from which the dual is generated.
 */
public class ConstructedConcreteDiagram {

	/* maps from the Euler graph to the dual of the euler graph. Set when the dual is found */
	HashMap<Edge,Edge> dualEdgeMap = new HashMap<Edge,Edge>();
	HashMap<Edge,Point2D> edgeBendPoint = new HashMap<Edge,Point2D>();
	ArrayList<Node> greenNode = new ArrayList<Node>();
	public static final int EDGEPADDING = 2;
	public static final int PADDINGMULTIPIER = 2;
	protected DualGraph dg;
	protected ArrayList<ConcreteContour> concreteContours;	
	protected String abstractDescription = null;
 	public final String FILESTARTABSTRACTDESCRIPTION  = "ABSTRACTDESCRIPTION";
 	public final String FILESTARTCONTOURS = "CONTOURS";
 	public final String FILESTARTDIAGRAM = "DIAGRAM";
 	public final char FILESEPARATOR = '|';
 	public ArrayList<String> zones = null;

		
	/** Creates a diagram with no contours */
	public ConstructedConcreteDiagram( String abstractDescription, ArrayList<ConcreteContour> concreteContours) 
	{
		this.concreteContours = concreteContours;
		this.abstractDescription = abstractDescription;
		generateDualGraph();
	}
	public ConstructedConcreteDiagram (File file){
		this.loadDiagram(file);
		generateDualGraph();
	}
	
	public String getAbstractDescription(){
		return abstractDescription;
	}
	public ArrayList<ConcreteContour> getConcreteContours() {return concreteContours;}
	public DualGraph getDualGraph() {return dg;}

	public HashMap<Edge,Point2D> getEdgeBendPoint(){
		return edgeBendPoint;
	}
	
	public void clear() {
		concreteContours = new ArrayList<ConcreteContour>();
		dg = new DualGraph();
	}

	public ConstructedConcreteDiagram clone() {

		ArrayList<ConcreteContour> cloneCCs = new ArrayList<ConcreteContour>();
		for(ConcreteContour cc : getConcreteContours()) {
			cloneCCs.add(cc.clone());
		}		
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(abstractDescription, cloneCCs);
		
		return ccd;

	}
	public void generateDualGraph(){
		String s = "0"+abstractDescription;		
		dg = new DualGraph(new AbstractDiagram(s));
		//dg.printAll();	
	 	HashMap<String, Polygon> zonePolygonList = getZonesByPolygon(concreteContours);
		for(Node n:dg.getNodes()){
			if(n.getLabel().compareTo("")!=0){
				Polygon pol = zonePolygonList.get(n.getLabel());
				if(pol!=null){
					Point p = getCentrePoint(pol);
					n.setCentre(p);
				}
			}	
			else{
				n.setLabel("");
				n.setCentre(new Point(250,400));		
			}
		} 		
	}
	
	public String getNextUnusedLabel() {
		char ret = 'a';		
		ArrayList<String> labelList = new ArrayList<String>(); 
		for(ConcreteContour cc : concreteContours) {
			labelList.add(cc.getAbstractContour());
		}		
		Collections.sort(labelList);		
		for(String label : labelList) {
			if(label.equals(Character.toString(ret)));
			ret++;
		}		
		return Character.toString(ret);
	}	
	
	public void addConcreteContour(ConcreteContour contour) {
		concreteContours.add(contour); 	
		getStraightLineDualOfGraph(concreteContours);
	}
	
	public static Rectangle generateMaxRectangle(Polygon pol) {
		ConvexHull hull1 = new ConvexHull(pol);
		return hull1.getMaxRectangle();
	}	
	public static HashMap<String, Polygon> getZonesByPolygon(ArrayList<ConcreteContour> concreteContours){
		
		HashMap<String, Polygon> ret = new HashMap<String, Polygon>();
		HashMap<String, Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		
		for (String zone : zoneAreaMap.keySet()) {		
			Area area = zoneAreaMap.get(zone);			
			ArrayList<Polygon> polygons = ConcreteContour.polygonsFromArea(area);
			if(polygons.size()!=0){
				for(Polygon pol: polygons){
					Polygon temp = removeDuplicatedPoints(pol);
					ret.put(zone, temp);
				}
			}
		}
		return ret;
	}
	public static Polygon removeDuplicatedPoints(Polygon pol){
		Polygon ret = new Polygon();
		ArrayList<Point> points = new ArrayList<Point>();
		for(int i = 0 ; i < pol.npoints; i++){
			Point p = new Point(pol.xpoints[i],pol.ypoints[i]);
			if(!points.contains(p))
				points.add(p);	
		}
		for(Point p1: points){
			ret.addPoint((int)p1.getX(),(int)p1.getY());
		}
		return ret;
	}

	public static Point getCentrePoint(final Polygon pol) {
		
		Rectangle polyBounds = pol.getBounds();
		Point polyCentre = new Point(polyBounds.x + polyBounds.width/2,polyBounds.y + polyBounds.height/2);
	
		PolygonTriangulator pt = new PolygonTriangulator();
		for(int i = 0 ; i < pol.npoints; i++){
			pt.addPolyPoint(pol.xpoints[i], pol.ypoints[i]);
		}
		ArrayList<Point2D> tris = pt.getTris();
		if(tris==null) {
			return new Point(0,0);
		}

		double closestDistance = Double.MAX_VALUE;
		Point closestCentre = null;
		for(int j = 0 ; j < tris.size(); j+=3){
			Point2D p1 = tris.get(j);
			Point2D p2 = tris.get(j+1);
			Point2D p3 = tris.get(j+2);
			int cx = (int)((p1.getX()+p2.getX()+p3.getX())/3);
			int cy = (int)((p1.getY()+p2.getY()+p3.getY())/3);
			Point triCentre = new Point(cx,cy);
			double distance = pjr.graph.Util.distance(triCentre, polyCentre);
			if(distance < closestDistance) {
				closestDistance = distance;
				closestCentre = triCentre;
			}
		}
	   return closestCentre;
	}	

	public static HashMap<String, Polygon> generatePolygonAreas(ArrayList<ConcreteContour> concreteContours){
		
		HashMap<String, Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		HashMap<String, Polygon> zonePolygonMap = new HashMap<String, Polygon>();
		for (String zone : zoneAreaMap.keySet()) {	
			Area area = zoneAreaMap.get(zone);			
				ArrayList<Polygon> polygons = ConcreteContour.polygonsFromArea(area);
				if(polygons.size()!=0){
					for(Polygon pol: polygons){
						Polygon temp = removeDuplicatedPoints(pol);
						zonePolygonMap.put(zone, temp);
					}
				}
		}			
		return zonePolygonMap;
	}
	public static Area getOutsideArea(ArrayList<ConcreteContour> concreteContours){
		Area ret = new Area();
		HashMap<String, Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		for (String zone : zoneAreaMap.keySet()) {	
			Area area = zoneAreaMap.get(zone);
			ret.add(area);
		}
		return ret;	
	}
	
	public DualGraph getStraightLineDualOfGraph(ArrayList<ConcreteContour> concreteContours){
		
		DualGraph ret = new DualGraph();
		dualEdgeMap = new HashMap<Edge,Edge>();
		DualGraph eulerGraph = generateEulerGraph(concreteContours);

		ArrayList<Node> emptyNodes = new ArrayList<Node>();
		ArrayList<Node> greenNodes = new ArrayList<Node>();
		ArrayList<Edge> greenEdges = new ArrayList<Edge>();
		Area contour  = getOutsideArea(concreteContours);	
		
		ArrayList<Polygon> polygons = ConcreteContour.polygonsFromArea(contour);
		Rectangle rect = contour .getBounds();
		Point centre = new Point((int)rect.getCenterX(), (int)rect.getCenterY());
		Point tl = new Point((int)rect.getX(),(int)rect.getY());
		Point tr = new Point((int)rect.getMaxX(),(int)rect.getY());
		Point bl = new Point((int)rect.getX(),(int)rect.getMaxY());
		Point br = new Point((int)rect.getMaxX(),(int)rect.getMaxY());
		
		if(concreteContours.size() == 1){
			ConcreteContour cc = concreteContours.get(0);
			Polygon pol = cc.getPolygon();
			String label = cc.getAbstractContour();
			Node n = new Node(label);
			Point p = getCentrePoint(pol);
			n.setCentre(p);			
			Node empty = new Node("");
			empty.setCentre(tl);
			emptyNodes.add(empty);
			Edge e = new Edge (n, empty);
			ret.addNode(n);
			ret.addNode(empty);
			ret.addEdge(e);			
			
			Edge newEdge = new Edge(empty,empty);
			if(topleft(centre, empty.getCentre())||topright(centre, empty.getCentre())){
				newEdge.addBend(tr);
				newEdge.addBend(br);
				newEdge.addBend(bl);
				newEdge.addBend(tl);
			}
			if(bottomleft(centre, empty.getCentre())||bottomright(centre, empty.getCentre())){
				newEdge.addBend(bl);
				newEdge.addBend(tl);
				newEdge.addBend(tr);
				newEdge.addBend(br);
			}
			dualEdgeMap.put(e,newEdge);
		//	edgeBendPoint.put(newEdge, p2);
			ret.addEdge(newEdge);			
			return ret;			
		}
		
		HashMap<String, Polygon> zonePolygonMap = generatePolygonAreas(concreteContours);
		for (String zone : zonePolygonMap.keySet()) {
			Polygon pol = zonePolygonMap.get(zone);	
			if(!zone.equals("")){
				Point p = getCentrePoint(pol);
				Node n = new Node();
				n.setCentre(p);
				n.setLabel(zone);
			 	ret.addNode(n);
			}
		}	
		
		ArrayList<Node> nodes = ret.getNodes();		
	
		for(int n1Index = 0; n1Index < nodes.size(); n1Index++) {
			Node n1 = nodes.get(n1Index);
			
			for(int n2Index = n1Index+1; n2Index < nodes.size(); n2Index++) {
								
				Node n2 = nodes.get(n2Index);					
				int labelDifferences = DualGraph.countLabelDifferences(n1.getLabel(),n2.getLabel());
				if(labelDifferences == 1) {
					for(Edge e: eulerGraph.getEdges()){
						String s1 = ","+n1.getLabel() + ","+n2.getLabel();
						String s2 = ","+n2.getLabel() + ","+n1.getLabel();
						if(e.getLabel().compareTo(s1)==0||e.getLabel().compareTo(s2)==0){							
							Point c;
							if(e.getBends().size()!=0){
								c = e.getBends().get(0);							
							}
							else{							
							Point p2 = pjr.graph.Util.getLineLineIntersection(e.getFrom().getCentre(), e.getTo().getCentre(),
									n1.getCentre(), n2.getCentre());
								c = p2;
							}
							Node n;
							if(ret.firstNodeAtPoint(c) == null){
								n = new Node("green");
								n.setCentre(c);
								greenNode.add(n);
								ret.addNode(n);
							}
							else{
								n = ret.firstNodeAtPoint(c);
							}
							if(ret.getEdge(n1, n) == null){
								Edge newEdge1 = new Edge(n1,n);
								ret.addEdge(newEdge1);
								dualEdgeMap.put(e,newEdge1);
							}
							if(ret.getEdge(n, n2) == null){
								Edge newEdge2 = new Edge(n,n2);
								ret.addEdge(newEdge2);
								dualEdgeMap.put(e,newEdge2);
							}
		
							Node from  = e.getFrom();
							Node to  = e.getTo();
							Node n3, n4;
							if(ret.firstNodeAtPoint(from.getCentre())==null){
								n3 = new Node(from.getLabel());
								n3.setCentre(from.getCentre());								
								ret.addNode(n3);
							//	System.out.println("node added " +  n3.toString());
							}
							else{
								n3 = ret.firstNodeAtPoint(from.getCentre());
							}
							if(ret.firstNodeAtPoint(to.getCentre()) ==null){								
								n4 = new Node(to.getLabel());
								n4.setCentre(to.getCentre());
								ret.addNode(n4);	
							//	System.out.println("node added " +  n4.toString());
							}
							else{
								n4 = ret.firstNodeAtPoint(from.getCentre());
							}
							if(ret.getEdge(n3, n) == null){
								Edge newEdge3 = new Edge(n3,n);
								ret.addEdge(newEdge3);
							//	System.out.println("--------" + n3.getLabel());
							}
							 if(ret.getEdge(n, n4)==null){
								Edge newEdge4 = new Edge(n,n4);
								if(e.getBends().size()>1){
									for(int i = 1;i<e.getBends().size(); i++){
										newEdge4.addBend(e.getBends().get(i));
									}
								}								
								ret.addEdge(newEdge4);			
							//	System.out.println("*********" + n4.getLabel());
							}
									
						}					
					}
				}
			}
		}
			
		Grid grid = new Grid(rect,5);
		for(Edge e: eulerGraph.getEdges()){
			if(e.getLabel().length()==2){
				char c = e.getLabel().charAt(1);
				String c1 = Character.toString(c);
				Node empty = new Node("empty");
				ret.addNode(empty);
				emptyNodes.add(empty);
				Node start = ret.firstNodeWithLabel(c1);
				Point bendPoint;
				
				if(e.getBends().size()!=0){
					bendPoint = e.getBends().get(0);
				}
				else{
					bendPoint = e.getMidPoint();
				}
				Point p = grid.getClosestGridUnitCentre(bendPoint, polygons);
				
				if(p!=null){
				empty.setCentre(p);
				}
				empty.setVisited(false);
				Node n; 
				if(ret.firstNodeAtPoint(bendPoint) == null){
					n = new Node("green");
					n.setCentre(bendPoint);
					ret.addNode(n);			
					greenNode.add(n);
				}
				else{
					n = ret.firstNodeAtPoint(bendPoint);
				}
				
				Edge newEdge1 = new Edge(start, n);
				Edge newEdge2 = new Edge(n, empty);			
				dualEdgeMap.put(e,newEdge1);
				dualEdgeMap.put(e,newEdge2);
				ret.addEdge(newEdge1);
				ret.addEdge(newEdge2);
					
				Node from  = e.getFrom();
				Node to  = e.getTo();
				String s1 = from.getLabel();
				String s2 = to.getLabel();
				Node n3, n4;
				if(ret.firstNodeAtPoint(from.getCentre())==null){
					n3 = new Node(s1);
					n3.setCentre(from.getCentre());
					ret.addNode(n3);
				}
				else{
					n3 = ret.firstNodeAtPoint(from.getCentre());
				}
				if(ret.firstNodeAtPoint(to.getCentre())==null){
					n4 = new Node(s2);
					n4.setCentre(to.getCentre());					
					ret.addNode(n4);
				}
				else{
					n4 = ret.firstNodeAtPoint(to.getCentre());
				}
				if(ret.getEdge(n3, n)==null){
					Edge newEdge3 = new Edge(n3,n);
					ret.addEdge(newEdge3);
				}
				if(ret.getEdge(n, n4)==null){
					Edge newEdge4 = new Edge(n,n4);
						if(e.getBends().size()>1){
							for(int i = 1;i<e.getBends().size(); i++){
								newEdge4.addBend(e.getBends().get(i));
							}
						}								
					ret.addEdge(newEdge4);
				}
			}
		}		
		
		if(emptyNodes.size()==1){
			Node n = emptyNodes.get(0);
			Edge newEdge = new Edge(n,n);
			double x = n.getX();
			double y = n.getY();
			if(topleft(centre, n.getCentre())){
				newEdge.addBend(new Point((int)x,(int)rect.getY()));
				newEdge.addBend(tr);
				newEdge.addBend(br);
				newEdge.addBend(bl);
				newEdge.addBend(tl);
			}
			if(topright(centre,n.getCentre())){
				newEdge.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)y));
				newEdge.addBend(br);
				newEdge.addBend(bl);
				newEdge.addBend(tl);
				newEdge.addBend(tr);
			}
			if(bottomright(centre,n.getCentre())){
				newEdge.addBend(new Point((int)x,(int)(y+rect.getY()+rect.getHeight())));
				newEdge.addBend(bl);
				newEdge.addBend(tl);
				newEdge.addBend(tr);
				newEdge.addBend(br);
			}
			if(bottomleft(centre,n.getCentre())){
				newEdge.addBend(new Point((int)(rect.getX()),(int)y));
				newEdge.addBend(tl);
				newEdge.addBend(tr);
				newEdge.addBend(br);
				newEdge.addBend(bl);
			}
	
			ret.addEdge(newEdge);
		}
		else if(emptyNodes.size()==2){
			Node n1 = emptyNodes.get(0);
			Node n2 = emptyNodes.get(1);
			Edge newEdge1 = null;
			Edge newEdge2 = null;
			Point c1 = n1.getCentre();
			Point c2 = n2.getCentre();
			
			if(topleft(centre,c1)||topright(centre,c1)){
				if(topleft(centre, c2)||topright(centre,c2)){
					Node left, right;
					if(n1.getX()<n2.getX()){
						left = n1;
						right = n2;
					}
					else{
						left = n2;
						right = n1;
					}
					newEdge1 = new Edge(left, right);
					newEdge2 = new Edge(right, left);
					newEdge1.addBend(new Point((int)left.getX(),(int)rect.getY()));
					newEdge1.addBend(new Point((int)right.getX(),(int)rect.getY()));					
					newEdge2.addBend(new Point((int)right.getX(),(int)rect.getY()));
					newEdge2.addBend(tr);
					newEdge2.addBend(br);
					newEdge2.addBend(bl);	
					newEdge2.addBend(tl);
					newEdge2.addBend(new Point((int)left.getX(),(int)rect.getY()));
				
				}
				if(bottomleft(centre,c2)){
					newEdge1 = new Edge(n1, n2);
					newEdge2 = new Edge(n2, n1);				
					newEdge1.addBend(new Point((int)rect.getX(),(int)n1.getY()));
					newEdge1.addBend(new Point((int)rect.getX(),(int)n2.getY()));
					newEdge2.addBend(new Point((int)rect.getX(),(int)n2.getY()));
					newEdge2.addBend(bl);
					newEdge2.addBend(br);
					newEdge2.addBend(tr);
					newEdge2.addBend(tl);
					newEdge2.addBend(new Point((int)rect.getX(),(int)n1.getY()));
				}
				if(bottomright(centre,c2)){
					newEdge1 = new Edge(n1, n2);
					newEdge2 = new Edge(n2, n1);
					if(topright(centre,c1)){
						newEdge1.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n1.getY()));
						newEdge1.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(br);
						newEdge2.addBend(bl);
						newEdge2.addBend(tl);
						newEdge2.addBend(tr);
						newEdge2.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n1.getY()));						
					}
					if(topleft(centre,c1)){
						newEdge1 = new Edge(n1, n2);
						newEdge2 = new Edge(n2, n1);
						newEdge1.addBend(new Point((int)n1.getX(),(int)rect.getY()));
						newEdge1.addBend(tr);
						newEdge1.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(br);
						newEdge2.addBend(bl);
						newEdge2.addBend(tl);
						newEdge2.addBend(new Point((int)n1.getX(),(int)rect.getY()));					
					}
				}
			}			
			if(bottomleft (centre,c1)|| bottomright(centre, c1)){
				if(bottomleft(centre, c2)||bottomright(centre,c2)){
					Node left, right;
					if(n1.getX()<n2.getX()){
						left = n1;
						right = n2;
					}
					else{
						left = n2;
						right = n1;
					}
					newEdge1 = new Edge(left, right);
					newEdge2 = new Edge(right, left);
					newEdge1.addBend(new Point((int)left.getX(),(int)rect.getMaxY()));
					newEdge1.addBend(new Point((int)right.getX(),(int)rect.getMaxY()));
					newEdge2.addBend(new Point((int)right.getX(),(int)rect.getMaxY()));
					newEdge2.addBend(br);
					newEdge2.addBend(tr);
					newEdge2.addBend(tl);
					newEdge2.addBend(bl);
					newEdge2.addBend(new Point((int)left.getX(),(int)rect.getMaxY()));
				}
				if(topleft(centre,c2)){
					newEdge1 = new Edge(n1,n2);
					newEdge2 = new Edge(n2,n1);
					newEdge1.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
					newEdge1.addBend(bl);
					newEdge1.addBend(new Point((int)rect.getX(),(int)n2.getY()));
					newEdge2.addBend(new Point((int)rect.getX(),(int)n2.getY()));
					newEdge2.addBend(tl);
					newEdge2.addBend(tr);
					newEdge2.addBend(br);
					newEdge2.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
				}
				if(topright(centre,c2)){
					if(bottomright(centre,c1)){
						newEdge1 = new Edge(n1, n2);
						newEdge2 = new Edge(n2, n1);
						newEdge1.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n1.getY()));
						newEdge1.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n2.getY()));
						newEdge2.addBend(br);
						newEdge2.addBend(bl);
						newEdge2.addBend(tl);
						newEdge2.addBend(tr);
						newEdge2.addBend(new Point((int)(rect.getX()+rect.getWidth()),(int)n1.getY()));
					}
					if(bottomleft(centre,c1)){	
						newEdge1 = new Edge(n1, n2);
						newEdge2 = new Edge(n2, n1);
						newEdge1.addBend(new Point((int)n1.getX(),(int)(rect.getY()+rect.getHeight())));
						newEdge1.addBend(bl);
						newEdge1.addBend(tl);
						newEdge1.addBend(tr);
						newEdge1.addBend(new Point((int)rect.getMaxX(),n2.getY()));
						newEdge2.addBend(new Point((int)rect.getMaxX(),n2.getY()));
						newEdge2.addBend(br);
						newEdge2.addBend(new Point((int)n1.getX(),(int)(rect.getY()+rect.getHeight())));
					}					
				}
			}		
			if(newEdge1!=null)
			ret.addEdge(newEdge1);
			if(newEdge2!=null)
			ret.addEdge(newEdge2);
		}
		else if(emptyNodes.size()>2){
		
			double[] angle = new double[emptyNodes.size()];	
			for(int i = 0 ; i < emptyNodes.size(); i++){
				Node n1 = emptyNodes.get(i);
				Point p = n1.getCentre();
				if(p ==null){
					System.out.println("p = null");
				}
				angle[i] = pjr.graph.Util.lineAngle(p,centre);
					
				for(int k = 0 ; k < angle.length; k++){
				 	if(k != i && angle[k] == angle[i]){
				 		Node n2 = emptyNodes.get(k);
				 		Node g1 = greenNodes.get(i);
				 		Node g2 = greenNodes.get(k);
				 		double a1 = pjr.graph.Util.lineAbsoluteAngle(g1.getCentre(), centre);
				 		double a2 = pjr.graph.Util.lineAbsoluteAngle(g2.getCentre(), centre);
						Point p1 = nextPointInRectangle(rect, p);						
						if(a1<=a2){
							n1.setCentre(p1);
							angle[i] = pjr.graph.Util.lineAbsoluteAngle(p, centre);
						}
						else{
							n2.setCentre(p1);
							angle[k] = pjr.graph.Util.lineAbsoluteAngle(p, centre);
						}
					}
				}
			}
			double[] temp = angle.clone();
			Arrays.sort(temp);
			ArrayList<Node> sortedEmptyNodes = new ArrayList<Node>();
			for(int j = 0 ; j < temp.length; j++){
				System.out.println(temp[j]);
				for(int l = 0 ; l < angle.length; l++){
					if(angle[l]== temp[j]&& emptyNodes.get(l).getVisited()==false){
						//emptyNodes.get(l).setLabel(Integer.toString(j));
						emptyNodes.get(l).setVisited(true);
						sortedEmptyNodes.add(emptyNodes.get(l));						
					}
				}
			}
		
			Node n2 = sortedEmptyNodes.get(sortedEmptyNodes.size()-1);
			Node n1 = sortedEmptyNodes.get(0);
			Edge newEdge = new Edge(n1,n2);
			Point c1 = n1.getCentre();
			Point c2 = n2.getCentre();
			
			if(topleft(centre,c1)){					
				if(topleft(centre,c2)||topright(centre,c2)){	
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
					newEdge.addBend(new Point((int)n2.getX(),(int)rect.getY()));
				}	
				if(bottomleft(centre, c2)){
					newEdge.addBend(new Point((int)rect.getX(),(int)n1.getY()));
					newEdge.addBend(new Point((int)rect.getX(),(int)n2.getY()));					
				}
				if(bottomright(centre,c2)){
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
					newEdge.addBend(tr);
					newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));			
				}
			}
			if(topright(centre,c1)){
				if(topleft(centre,c2)||topright(centre,c2)){	
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
					newEdge.addBend(new Point((int)n2.getX(),(int)rect.getY()));
				}	
				if(bottomright(centre,c2)){
					newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
					newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));
				}
				if(bottomleft(centre,c2)){
					newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
					newEdge.addBend(br);
					newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
				}
			}
			
			if(bottomright(centre, c1)){
				if(bottomleft(centre, c2)||bottomright(centre,c2)){
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
					newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
				}
				if(topleft(centre,c2)){
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
					newEdge.addBend(bl);
					newEdge.addBend(new Point((int)rect.getX(),n2.getY()));
				}
				if(topright(centre,c2)){
					newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
					newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));
				}				
			}
			if(bottomleft(centre,c1)){
				if(bottomleft(centre, c2)||bottomright(centre,c2)){
					newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
					newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
				}
				if(topleft(centre,c2)){
					newEdge.addBend(new Point((int)rect.getX(),n1.getY()));
					newEdge.addBend(new Point((int)rect.getX(),n2.getY()));
				}
				if(topright(centre,c2)){
					newEdge.addBend(new Point((int)rect.getX(),n1.getY()));
					newEdge.addBend(tl);
					newEdge.addBend(new Point(n2.getX(),(int)rect.getY()));
				}				
			}
			ret.addEdge(newEdge);
			
			for(int m = 0; m < sortedEmptyNodes.size() -1 ; m++){
				n1 = sortedEmptyNodes.get(m);
				n2 = sortedEmptyNodes.get(m+1);

				c1 = n1.getCentre();
				c2 = n2.getCentre();
				newEdge = new Edge(n1,n2);
				
				if(topleft(centre,c1)){					
					if(topleft(centre,c2)||topright(centre,c2)){	
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
						newEdge.addBend(new Point((int)n2.getX(),(int)rect.getY()));
					}	
					if(bottomleft(centre, c2)){
						newEdge.addBend(new Point((int)rect.getX(),(int)n1.getY()));
						newEdge.addBend(new Point((int)rect.getX(),(int)n2.getY()));					
					}
					if(bottomright(centre,c2)){
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
						newEdge.addBend(tr);
						newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));			
					}
				}
				if(topright(centre,c1)){
					if(topleft(centre,c2)||topright(centre,c2)){	
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getY()));
						newEdge.addBend(new Point((int)n2.getX(),(int)rect.getY()));
					}	
					if(bottomright(centre,c2)){
						newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
						newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));
					}
					if(bottomleft(centre,c2)){
						newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
						newEdge.addBend(br);
						newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
					}
				}
				
				if(bottomright(centre, c1)){
					if(bottomleft(centre, c2)||bottomright(centre,c2)){
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
						newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
					}
					if(topleft(centre,c2)){
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
						newEdge.addBend(bl);
						newEdge.addBend(new Point((int)rect.getX(),n2.getY()));
					}
					if(topright(centre,c2)){
						newEdge.addBend(new Point((int)rect.getMaxX(),n1.getY()));
						newEdge.addBend(new Point((int)rect.getMaxX(),n2.getY()));
					}				
				}
				if(bottomleft(centre,c1)){
					if(bottomleft(centre, c2)||bottomright(centre,c2)){
						newEdge.addBend(new Point((int)n1.getX(),(int)rect.getMaxY()));
						newEdge.addBend(new Point((int)n2.getX(),(int)rect.getMaxY()));
					}
					if(topleft(centre,c2)){
						newEdge.addBend(new Point((int)rect.getX(),n1.getY()));
						newEdge.addBend(new Point((int)rect.getX(),n2.getY()));
					}
					if(topright(centre,c2)){
						newEdge.addBend(new Point((int)rect.getX(),n1.getY()));
						newEdge.addBend(tl);
						newEdge.addBend(new Point(n2.getX(),(int)rect.getY()));
					}					
				}				
			ret.addEdge(newEdge);
			}
		}
		ArrayList<Face> faces = ret.formUnitFacesWithEdgeBends();
		for(Face f0: faces){
			//f0.print();
			ArrayList<Node> nonGreenNodes = new ArrayList<Node>();
			for(Node n : f0.getNodeList()){
				if(n.getLabel().compareTo("green")!=0)
					nonGreenNodes.add(n);
			}			
			if(nonGreenNodes.size()==2){
				Node n1 = nonGreenNodes.get(0);
				Node n2 = nonGreenNodes.get(1);
				if(ret.getEdge(n1, n2)==null){
					Edge e = new Edge(n1,n2);
					e.setLabel("green");
					greenEdges.add(e);
					ret.addEdge(e);
				}
			}
			if(nonGreenNodes.size()>2){
				Node n1 = nonGreenNodes.get(nonGreenNodes.size()-1);
				Node n2 = nonGreenNodes.get(0);
				if(ret.getEdge(n1, n2)==null){
					Edge e = new Edge(n1,n2);
					e.setLabel("green");
					greenEdges.add(e);
					ret.addEdge(e);
				}				
				for(int j = 0 ; j < nonGreenNodes.size()-1; j++){
					n1 = nonGreenNodes.get(j);
					n2 = nonGreenNodes.get(j+1);
					if(ret.getEdge(n1, n2)==null){
						Edge e = new Edge(n1,n2);
						e.setLabel("green");
						greenEdges.add(e);
						ret.addEdge(e);
					}
				}				
			}			
		}		
		for(int l = 0 ; l < ret.getNodes().size(); l++){
			ret.getNodes().get(l).setLabel(Integer.toString(l));
		}		
		return ret;
	}
	public Point nextPointInRectangle(Rectangle2D rect, Point p){
		Point ret = null;

		double y0 = rect.getCenterY();
		double x = p.getX();
		double y = p.getY();
		double delta = 5;
		if( y<=y0){
			return new Point((int)(x+delta),(int)(y-delta));	
		}	
		if(y>=y0){
			return new Point((int)(x-delta),(int)(y+delta));
			
		}		
		return ret;
	}
	
	
	public boolean topleft(Point centre, Point p){
		if(p.getX()<=centre.getX()&&p.getY()<=centre.getY())
			return true;
		return false;
	}
	public boolean topright(Point centre, Point p){
		if(p.getX()>=centre.getX()&&p.getY()<=centre.getY())
			return true;
		return false;
	}
	public boolean bottomleft(Point centre, Point p){
		if(p.getX()<=centre.getX()&&p.getY()>=centre.getY())
			return true;
		return false;
	}
	public boolean bottomright(Point centre, Point p){
		if(p.getX()>=centre.getX()&&p.getY()>=centre.getY())
			return true;
		return false;
	}
	public static DualGraph generateEulerGraph(ArrayList<ConcreteContour> concreteContours){
		
		DualGraph eg = new DualGraph();
		if(concreteContours.size() == 1){
			ConcreteContour cc = concreteContours.get(0);
			Polygon pol = cc.getPolygon();
		//	eg = ConstructedConcreteDiagram.buildGraphFromPolygon(pol);
			Node n1 = new Node("0");
			Point p1 = new Point(pol.xpoints[0], pol.ypoints[0]);
			n1.setCentre(p1);
			eg.addNode(n1);
			Edge e = new Edge(n1,n1);
			for(int i = 1; i< pol.npoints; i++){
				Point p = new Point(pol.xpoints[i], pol.ypoints[i]);
				e.addBend(p);
			}
			eg.addEdge(e);	
			return eg;
		}
	
		ArrayList<Point2D.Double> intersectionPoints = new ArrayList<Point2D.Double>();
		ArrayList<ContourLine> cl = new ArrayList<ContourLine>();
		
		//form contour line list 
		for(ConcreteContour cc: concreteContours){
			cc.setContourLines();
			for(ContourLine l : cc.getContourLines()){
				if(!cl.contains(l)){
					cl.add(l);	
					l.setLabel(cc.getAbstractContour());
				}
			}	
		}
		//add cross points to each contour and put a node on each cross point
		for(ConcreteContour cc1 : concreteContours){	
			ArrayList<ContourLine> cl1 = cc1.getContourLines();
			for(ContourLine l1 : cl1){
				for(ContourLine l2 : cl){
					if( l1.getLabel().compareTo(l2.getLabel())!=0 &&l1.getLine().intersectsLine(l2.getLine())){
						double x1,x2,x3,x4,y1,y2,y3,y4;
						x1 = l1.getLine().getX1();	x2 = l1.getLine().getX2();
						x3 = l2.getLine().getX1();	x4 = l2.getLine().getX2();
						y1 = l1.getLine().getY1();	y2 = l1.getLine().getY2();
						y3 = l2.getLine().getY1();	y4 = l2.getLine().getY2();						
						Point2D.Double p1 = new Point2D.Double((int)x1,(int)y1);
						Point2D.Double p2 = new Point2D.Double((int)x2,(int)y2);
						Point2D.Double p3 = new Point2D.Double((int)x3,(int)y3);
						Point2D.Double p4 = new Point2D.Double((int)x4,(int)y4);
						
						Point2D.Double p = pjr.graph.Util.intersectionPointOfTwoLines(p1, p2, p3, p4);
						if( p!= null && !containsPoint(intersectionPoints,p)){							
							intersectionPoints.add(new Point2D.Double((int)p.getX(),(int)p.getY()));					
						 	l1.addCrossPoint(p);
						 	l2.addCrossPoint(p);
						 	addPoint(concreteContours,l1.getLabel(),l1.getLine(),p);
						 	addPoint(concreteContours,l2.getLabel(),l1.getLine(),p);
							Node n = new Node();
							n.setX((int)p.getX());
							n.setY((int)p.getY());
							eg.addNode(n);	
							n.setLabel(l1.getLabel()+l2.getLabel());
						}					
					}
				}			
			}			
		}
		
		//add edges and edge bends
		for(ConcreteContour cc2 : concreteContours){			
						
			ArrayList<Point2D> points = new ArrayList<Point2D>();			
			int numberOfCrossing = 0;		
			for(ContourLine l4 : cc2.getContourLines())
			{
				l4.setLabel(cc2.getAbstractContour());
				l4.sortCorssPoints();
				numberOfCrossing += l4.getCrossPoints().size();
			}
	
			int index [] = new int[50];
			int idx = 0;
			for(ContourLine l4 : cc2.getContourLines())
			{
				if(l4.getCrossPoints().size()==0){
					points.add(l4.getLine().getP1());
					index[idx] = 0;
					idx++;
				}
				else{
					points.add(l4.getLine().getP1());
					index[idx] = 0;
					idx++;
					for(Point2D p : l4.getCrossPoints()){
						points.add(p);
						index[idx] = 1;
						idx++;
					}				
				}
			}
			if(numberOfCrossing!=0){
				int crossIndex[] = new int[numberOfCrossing]; 
				int idx1 = 0;
				
				for(int j = 0 ; j < points.size(); j++){
					if(index[j]==1){
						crossIndex[idx1] = j;
						idx1++;
					}
				}
				int start =  crossIndex[0];
				int end = crossIndex[numberOfCrossing-1];
				Point2D startP = points.get(start);
				Point2D endP = points.get(end);
				Point startPInt = new Point((int)(startP.getX()),(int)(startP.getY()));
				Point endPInt = new Point((int)(endP.getX()),(int)(endP.getY()));
				Node startNode = eg.firstNodeAtPoint(startPInt);
				Node endNode = eg.firstNodeAtPoint(endPInt);
				Edge e = new Edge(endNode,startNode);
				if(!eg.addEdge(e)){			
					System.out.println("failed to add edge");				
				}	
				if(end != points.size()-1){
					for(int m = end+1; m < points.size(); m ++){
						Point2D pp1 = points.get(m);
						e.addBend(new Point((int)pp1.getX(),(int)pp1.getY()));	
					}
				}
				for(int l = 0; l < start; l++){
					Point2D pp = points.get(l);
					e.addBend(new Point((int)pp.getX(),(int)pp.getY()));		
				}
			
				for(int k = 0 ; k < numberOfCrossing -1; k++){
					start = crossIndex[k];
					end = crossIndex[k+1];
					startP = points.get(start);
					endP = points.get(end);
					startPInt = new Point((int)(startP.getX()),(int)(startP.getY()));
					endPInt = new Point((int)(endP.getX()),(int)(endP.getY()));
					startNode = eg.firstNodeAtPoint(startPInt);
					endNode = eg.firstNodeAtPoint(endPInt);
					Edge e1 = new Edge(startNode,endNode);
					if(!eg.addEdge(e1)){
						System.out.println("failed to add edge");				
					}
					for(int n = start+1; n< end; n++){
						Point2D pp1 = points.get(n);
						e1.addBend(new Point((int)pp1.getX(),(int)pp1.getY()));	
					}		
				}
			}
		}
		//add edge labels
		HashMap<String, Polygon> zonePolygonMap = generatePolygonAreas(concreteContours);
		for(Edge e : eg.getEdges()){
			for (String zone : zonePolygonMap.keySet()) {
				if(zone!=""){
					Polygon pol = zonePolygonMap.get(zone);				
					if(edgeInPolygon(e,pol)){
							e.setLabel(e.getLabel()+","+zone);							
					}
				}
			
			}
		}
		return eg;
	}
	
	public static ConcreteContour addPoint(ArrayList<ConcreteContour> ccs, String zone, Line2D line, Point2D pointToAdd){
	
		for(ConcreteContour cc: ccs)
		{
			if(cc.getAbstractContour().compareTo(zone) == 0){
				Polygon pol = cc.getPolygon();
				Polygon temp = new Polygon();
				int idx = 0;
				for(int i = 0; i< pol.npoints; i++){
					if((pol.xpoints[i]==line.getX1()&&pol.ypoints[i]==line.getY1())
							||(pol.xpoints[i] == line.getX2()&&pol.ypoints[i]==line.getY2())){
						idx = i;
					}
				}
				for(int j = 0 ; j <= idx; j++){
					temp.addPoint(pol.xpoints[j], pol.ypoints[j]);		
				}
				temp.addPoint((int)pointToAdd.getX(), (int)pointToAdd.getY());
				for(int k = idx+2; k < pol.npoints+1; k++){
					temp.addPoint(pol.xpoints[k-1], pol.ypoints[k-1]);		
				}
				return (new ConcreteContour(cc.getAbstractContour(),temp));
			}
		}
		System.out.println("failed to add point to polygon");
		return null;
		
	}
	public static boolean edgeInPolygon(Edge e, Polygon pol){
		
		Point p1 = new Point((int)e.getFrom().getX(),(int)e.getFrom().getY());
		Point p2 = new Point((int)e.getTo().getX(),(int)e.getTo().getY());
		
		ArrayList<Point> polygonPoints = new ArrayList<Point>();
		for(int i = 0 ; i < pol.npoints ; i++){
			Point p = new Point((int)pol.xpoints[i],(int)pol.ypoints[i]);
			polygonPoints.add(p);			
		}	
		if(e.getBends().size()==0){
			int idx1 = polygonContainsPointAppro(pol,p1);
			int idx2 = polygonContainsPointAppro(pol,p2);
			if( idx1!=-1 && idx2!=-1 
					&& (Math.abs(idx1 - idx2)==1
							|| Math.abs(idx1 - idx2) == pol.npoints -1)){
				return true;			
			}
		}
		else 
			if(polygonPoints.containsAll(e.getBends())){
			return true;
		}	
		return false;
	}

	public static int polygonContainsPointAppro(Polygon pol, Point p){
		
		int idx = -1;
		for(int i= 0 ; i < pol.npoints; i++){
			int x1 = pol.xpoints[i];
			int y1 = pol.ypoints[i];
			double x2 = p.x;
			double y2 = p.y;
			if(pjr.graph.Util.distance(x1,y1,x2,y2)<=2)
				idx = i;
		
		}
		
		return idx;
	}	
	
	
	public Node findNode(Point2D p, ArrayList<Node> nodeList){
		
		int x = (int)p.getX();
		int y = (int)p.getY();
		
		for(Node n: nodeList){
			if(x==(int)n.getCentre().getX()&& y ==(int)n.getCentre().getY()){
			return n;
			}			
		}	
		return null;
	}
	
	
	public static boolean containsPoint(ArrayList<Point2D.Double> list, Point2D.Double p){
		
		if(list.size()!=0){
			for(Point2D.Double p1 : list){
				if( (int)p1.getX() == (int)p.getX() && (int)p1.getY() == (int)p.getY())
					return true;
			}
		}
		return false;
	}

	/**
	 * Takes the current concreteContours and attempts to generate the dualGraph
	 * from them, adding poly lines to edges where necessary. The graph has duplicated
	 * edges to the outside zone.
	 */
	public void generateDualGraphWithDoubledOutsideEdges() {
		dg = getStraightLineDualOfGraph(concreteContours);
		
		HashMap<String,Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		
		// first double up all edges to the outside zone
		ArrayList<Edge> clockwiseEdges = new ArrayList<Edge>();
		ArrayList<Edge> antiClockwiseEdges = new ArrayList<Edge>();
		for(Edge e : dg.getEdges()) {
			if(e.getFrom().getLabel().equals("") || e.getTo().getLabel().equals("")) {
				clockwiseEdges.add(e);
			}
		}
		
		for(Edge eC : clockwiseEdges) {
			Edge eA = new Edge(eC.getFrom(),eC.getTo(),eC.getLabel());
			dg.addEdge(eA);
			antiClockwiseEdges.add(eA);
		}
		
		ArrayList<Edge> routedInsideEdges = new ArrayList<Edge>();
		
		// route inside zone edges first
		
		for(Edge e : dg.getEdges()) {
			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			if(!z1.equals("") && !z2.equals("")) {
				// two inside zones
			
				Area a1 = zoneAreaMap.get(z1);
				Area a2 = zoneAreaMap.get(z2);
				
				Area a = new Area(a1);
				a.add(a2);
				
				// stop any edge crossings, by removing edges already routed from the area
/*				for(Edge otherE : routedInsideEdges) {
					a.subtract(findEdgeArea(otherE,EDGEPADDING));
				}
*/			
				// force edges with parallels to go on the correct route
				ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
				dualEdgeMap = new HashMap<Edge,Edge>();
				for(Edge eEulerGraph : dualEdgeMap.keySet()) {
					Edge eDual = dualEdgeMap.get(eEulerGraph);
					if(eDual == e) {
						// dont need to consider this edge
						continue;
					}
					if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
						parallelEdges.add(eEulerGraph);
					}
				}
				for(Edge parallelEdge : parallelEdges) {
					a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
				}
				
				
				ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
				Polygon p = null;
				for(Polygon pTry : ps) {
					if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
						p = pTry;
					}
				}
				if(p == null) {
					System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
				} else {
				
					ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedInsideEdges,new ArrayList<Edge>());
					if(edgeBends == null) {
							System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
					} else {
						e.setBends(edgeBends);
						routedInsideEdges.add(e);
					}
				}

			}
			
		}
		
		
		// clockwise outside zone edges second
		ArrayList<Edge> routedClockwiseEdges = new ArrayList<Edge>();
		
		for(Edge e : clockwiseEdges) {
			
			if(e.getFrom().getLabel().equals("")) {
				// always start at the inner node
				e.reverse();
			}

			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			Area a1 = zoneAreaMap.get(z1);
			Area a2 = zoneAreaMap.get(z2);

			
			Area a = new Area(a1);
			a.add(a2);
			
			// force edges with parallels to go on the correct route
			ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
			for(Edge eEulerGraph : dualEdgeMap.keySet()) {
				Edge eDual = dualEdgeMap.get(eEulerGraph);
				if(eDual == e) {
					// dont need to consider this edge
					continue;
				}
				if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
					parallelEdges.add(eEulerGraph);
				}
			}
			for(Edge parallelEdge : parallelEdges) {
				a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
			}
			
			// stop any edge crossings, by removing edges already routed from the area
			ArrayList<Edge> routedEdges = new ArrayList<Edge>();
			routedEdges.addAll(routedInsideEdges);
			routedEdges.addAll(routedClockwiseEdges);

			// stop anticlockwise routing
			// assumes outside node is placed directly below contours
			Node outsideNode = dg.firstNodeWithLabel("");
			Point lowestPoint = new Point(outsideNode.getX(),outsideNode.getY()+1000);
			Point midPoint = new Point(outsideNode.getX()+5,outsideNode.getY());
			Rectangle bounds = GeneralConcreteDiagram.findContoursBounds(concreteContours); 
			Point topPoint = new Point(midPoint.x, bounds.y-2);
			
			GeneralPath path = new GeneralPath();
			path.moveTo(lowestPoint.x, lowestPoint.y);
			path.lineTo(midPoint.x, midPoint.y);
			path.lineTo(topPoint.x, topPoint.y);
			path.lineTo(midPoint.x+3, midPoint.y);

			
			Area aPath = new Area(path);
			
			a.subtract(aPath);
				
			ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
			Polygon p = null;
			for(Polygon pTry : ps) {
				if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
					p = pTry;
				}
			}

			if(p == null) {
				System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing clockwise, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
			} else {

				ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedEdges, new ArrayList<Edge>());
				if(edgeBends == null) {
						System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing clockwise, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
				} else {
					e.setBends(edgeBends);
					routedClockwiseEdges.add(e);
				}
			}

		}
			
		// anticlockwise outside zone edges third
		ArrayList<Edge> routedAntiClockwiseEdges = new ArrayList<Edge>();
		
		for(Edge e : antiClockwiseEdges) {
			
			if(e.getFrom().getLabel().equals("")) {
				// always start at the inner node
				e.reverse();
			}

			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			Area a1 = zoneAreaMap.get(z1);
			Area a2 = zoneAreaMap.get(z2);

			
			Area a = new Area(a1);
			a.add(a2);
			
			// force edges with parallels to go on the correct route
			// TODO commmented out the below becuase it does not work properly
/*			ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
			for(Edge eEulerGraph : dualEdgeMap.keySet()) {
				Edge eDual = dualEdgeMap.get(eEulerGraph);
				if(eDual == e) {
					// dont need to consider this edge
					continue;
				}
				if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
					parallelEdges.add(eEulerGraph);
				}
			}
			for(Edge parallelEdge : parallelEdges) {
				a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
			}
*/			
			// stop any edge crossings, by removing edges already routed from the area
			ArrayList<Edge> routedEdges = new ArrayList<Edge>();
			routedEdges.addAll(routedInsideEdges);
			routedEdges.addAll(routedAntiClockwiseEdges);

			// stop clockwise routing
			// assumes outside node is placed directly below contours
			Node outsideNode = dg.firstNodeWithLabel("");
			Point lowestPoint = new Point(outsideNode.getX(),outsideNode.getY()+1000);
			Point midPoint = new Point(outsideNode.getX()-5,outsideNode.getY());
			Rectangle bounds = GeneralConcreteDiagram.findContoursBounds(concreteContours); 
			Point topPoint = new Point(midPoint.x, bounds.y-2);
			
			GeneralPath path = new GeneralPath();
			path.moveTo(lowestPoint.x, lowestPoint.y);
			path.lineTo(midPoint.x, midPoint.y);
			path.lineTo(topPoint.x, topPoint.y);
			path.lineTo(midPoint.x+3, midPoint.y);

			
			Area aPath = new Area(path);
			
			a.subtract(aPath);
				
			ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
			Polygon p = null;
			for(Polygon pTry : ps) {
				if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
					p = pTry;
				}
			}
/*			
if(z1.equals("b") || z2.equals("b")) {
ConstructedDiagramPanel.areas = new ArrayList<Area>();
ConstructedDiagramPanel.areas.add(a);
test = true;
} else {
test = false;
}
*/


			if(p == null) {
				System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing anticlockwise, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
			} else {

				ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedEdges, clockwiseEdges);
				if(edgeBends == null) {
						System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing anticlockwise, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
				} else {
					e.setBends(edgeBends);
					routedAntiClockwiseEdges.add(e);
				}
			}

		}

	}
	
	
	
	
public boolean test = false;
int count = 0;
	
	/**
	 * Returns an area that covers the edge
	 */
	public Area findEdgeArea(Edge e, int padding) {
/*		
		final double SCALE_FACTOR = 1.9;
		
		Point from = e.getFrom().getCentre();
		Point to = e.getFrom().getCentre();
		
		ArrayList<Point> bends = e.getBends();
		
		int xTotal = from.x+to.x;
		int yTotal = from.y+to.y;
		int xCount = 2;
		int yCount = 2;
		for(Point point : bends) {
			xTotal+= point.x;
			yTotal+= point.y;
			xCount++;
			yCount++;
		}
		
		Point lineCentre = new Point(xTotal/xCount,yTotal/yCount);
		
		ArrayList<Point> scaledBends = new ArrayList<Point>(e.getBends());
		
		int xPadding = 0;
		int yPadding = padding;
		if(isUprightLine(from,to)) {
			xPadding = padding;
			yPadding = 0;
		}


		// scale and shift
		for(Point point : scaledBends) {
			point.x = pjr.graph.Util.scaleCoordinate(point.x, lineCentre.x, SCALE_FACTOR);
			point.y = pjr.graph.Util.scaleCoordinate(point.y, lineCentre.y, SCALE_FACTOR);
//			point.x += xPadding;
//			point.y += yPadding;
		}
		
		
		Polygon p = new Polygon();

		p.addPoint(from.x,from.y);
		for(Point point : bends) {
			p.addPoint(point.x, point.y);
		}
		p.addPoint(to.x,to.y);
		Collections.reverse(scaledBends);
		for(Point point : scaledBends) {
			p.addPoint(point.x, point.y);
		}
if(test) {
ConstructedDiagramPanel.areas = new ArrayList<Area>();
ConstructedDiagramPanel.areas.add(new Area(p));
}
		
		return new Area(p);
*/		
	
		final int FUDGE = 0;
		
		Area a = new Area();
		
		ArrayList<Point> bends = e.getBends();
		Point from = e.getFrom().getCentre();
		Point to = e.getFrom().getCentre();
		
		if(bends.size() == 0) {
			Polygon p = new Polygon();
			Point midPoint = e.getMidPoint();
			p.addPoint(from.x,from.y);
			int xPadding = 0;
			int yPadding = padding;
			if(isUprightLine(from,to)) {
				xPadding = padding;
				yPadding = 0;
			}
			
			p.addPoint(midPoint.x-xPadding,midPoint.y-yPadding);
			p.addPoint(to.x,to.y);
			p.addPoint(midPoint.x+xPadding,midPoint.y+yPadding);
			a.add(new Area(p));
			return a;
		}
		
		
		Point lastPoint = null;
		for(Point point : bends) {
			if(lastPoint == null) {
				// put in a triangle for the first point
				int xPadding = 0;
				int yPadding = padding;
				if(isUprightLine(from,point)) {
					xPadding = padding;
					yPadding = 0;
				}

				// hack to make sure the triangle meets with rectangles
				if(from.x != point.x) {
					xPadding -= FUDGE;
					if(from.x > point.x) {
						xPadding += FUDGE;
					}
				}
				if(from.y != point.y) {
					yPadding -= FUDGE;
					if(from.y > point.y) {
						xPadding += FUDGE;
					}
				}
				
				Polygon p = new Polygon();
				p.addPoint(from.x-xPadding,from.y-yPadding);
				p.addPoint(point.x-xPadding,point.y-yPadding);
				p.addPoint(point.x+xPadding,point.y+yPadding);
				p.addPoint(from.x+xPadding,from.y+yPadding);
				a.add(new Area(p));
			} else {
				int xPadding = 0;
				int yPadding = padding;
				if(isUprightLine(lastPoint,point)) {
					xPadding = padding;
					yPadding = 0;
				}
				Polygon p = new Polygon();
				p.addPoint(lastPoint.x-xPadding,lastPoint.y-yPadding);
				p.addPoint(lastPoint.x+xPadding,lastPoint.y+yPadding);
				p.addPoint(point.x+xPadding,point.y+yPadding);
				p.addPoint(point.x-xPadding,point.y-yPadding);

				a.add(new Area(p));

			}
			lastPoint = point;
			
		}
		
		int xPadding = 0;
		int yPadding = padding;
		if(isUprightLine(lastPoint,to)) {
			xPadding = padding;
			yPadding = 0;
		}
		// hack to make sure the triangle meets with rectangles
		if(from.x != lastPoint.x) {
			xPadding -= FUDGE;
			if(from.x > lastPoint.x) {
				xPadding += FUDGE;
			}
		}
		if(from.y != lastPoint.y) {
			yPadding -= FUDGE;
			if(from.y > lastPoint.y) {
				xPadding += FUDGE;
			}
		}
		Polygon p = new Polygon();
		p.addPoint(to.x-xPadding,to.y-yPadding);
		p.addPoint(lastPoint.x-xPadding,lastPoint.y-yPadding);
		p.addPoint(lastPoint.x+xPadding,lastPoint.y+yPadding);
		p.addPoint(to.x+xPadding,to.y+yPadding);
		a.add(new Area(p));
		
		return a;

		
	}

	
	/** Tests to see if the line between the points is more vertical than horizontal */
	private static boolean isUprightLine(Point from, Point to) {
		int x = from.x-to.x;
		int y = from.y-to.y;
		if(x < 0) {
			x = -x;
		}
		if(y < 0) {
			y = -y;
		}
		
		if(x > y) {
			return false;
		}
		
		return true;

	}

	/**
	 * Takes a polygon, an end point and a start point and
	 * routes a connection between points. List may be empty
	 * if a straight line suffices.
	 * @return a list of points that route through the polygon
	 * or null if there is no possible route
	 * 
	 */	
	public ArrayList<Point> routeThroughPolygon(Polygon p, Point start, Point end, ArrayList<Edge> edges, ArrayList<Edge> allowedCrosses) {

		ArrayList<Point> ret = new ArrayList<Point>();
		
		if(!p.contains(start) || !p.contains(end)) {
			return null;
		}
		
		if(euler.Util.lineInPolygon(p, start, end)) { // straight line fits, so no points required
			return ret;
		}
		
		// edge needs a polyline from here
		
		DualGraph dg = buildGraphFromPolygon(p);

		dg.formFaces();
		dg.triangulate();
		
		// there is only one inner face, and this is the one we need
		Face innerFace = null;
		for(Face f : dg.getFaces()) {
			if(f != dg.getOuterFace()) {
				innerFace = f;
			}
		}
//if(test) {
//euler.DiagramPanel.areas = new ArrayList<Area>();		
//euler.DiagramPanel.areas.add(new Area(innerFace.getPolygon()));		
//}
		
		TriangulationFace startTriangle = null;
		TriangulationFace endTriangle = null;
		for(TriangulationFace tf : dg.getTriangulationFaces()) {
			if(tf.contains(start)) {
				startTriangle = tf;
			}
			if(tf.contains(end)) {
				endTriangle = tf;
			}
		}
		
		
		ArrayList<TriangulationEdge> route = routeThroughFace(innerFace,startTriangle,endTriangle);

		Point lastPoint = null;
		for(TriangulationEdge te : route) {

			Point point = pjr.graph.Util.midPoint(te.getFrom().getCentre(), te.getTo().getCentre());
			if(lastPoint != null) {
				point = findCrossingAcceptingPoint(te, lastPoint, edges, allowedCrosses);
			}

			ret.add(point);
			lastPoint = point;
		}


		return ret;
		
	}
	

	/**
	 * Find a point between end1 and end2 that crosses only
	 * the edges in allowedCrosses, and no other edges in edges.
	 * Removes any crosses from allowedCrosses.
	 */
	public Point findCrossingAcceptingPoint(TriangulationEdge te, Point lastPoint, ArrayList<Edge> edges, ArrayList<Edge> allowedCrosses) {
		
		Point p1 = te.getFrom().getCentre();
		Point p2 = te.getTo().getCentre();
		
		Point ret = null;
		
		final int TESTPOINTS = 20;
		
		ArrayList<Point> pointList = new ArrayList<Point>();
		
		for(int i = 1; i <=TESTPOINTS-1; i++) {
			Point point = pjr.graph.Util.betweenPoints(p1, p2, i/(double)TESTPOINTS);
			pointList.add(point);
		}

		ArrayList<Point> acceptableList = new ArrayList<Point>();
		
		boolean acceptable = false;
		
		for(Point p : pointList) {
			boolean pointAcceptable = isAcceptableLineSegment(p,lastPoint,edges,allowedCrosses);
			if(acceptable && pointAcceptable) {
				acceptableList.add(p);
			} else if(!acceptable && pointAcceptable) {
				acceptableList.add(p);
				acceptable = true;
			} else if(acceptable && !pointAcceptable) {
				break;
			}
		}
		
		if(acceptableList.size() == 0) {
			System.out.println("No crossing acceptable point in ConstructedConcreteDiagram.findCrossingAcceptingPoint returning midpoint");
			return pjr.graph.Util.midPoint(p1, p2); // default in case of no acceptable points
		}
		
		int count = 0;
		int xTotal = 0;
		int yTotal = 0;
		for(Point p : acceptableList) {
			count++;
			xTotal += p.x;
			yTotal += p.y;
		}
		
		ret = new Point(xTotal/count,yTotal/count);

		ArrayList<Edge> removeList = new ArrayList<Edge>();
		for(Edge e : allowedCrosses) {
			if(edgeCrossLineSegment(ret,lastPoint,e)) {
				removeList.add(e);
			}
		}
		for(Edge e : removeList) {
			allowedCrosses.remove(e);
		}

		return ret;
	}

	
	public boolean isAcceptableLineSegment(Point p1, Point p2, ArrayList<Edge> edges, ArrayList<Edge> allowedCrosses) {

		for(Edge e : edges) {
			if(edgeCrossLineSegment(p1, p2, e)) {
				if(!allowedCrosses.contains(e)) {
					return false;
				}
			}
		}

		return true;
	}

	/** Test to see if the edge crosses the line between p1 and p2 */
	public static boolean edgeCrossLineSegment(Point p1, Point p2, Edge e) {
		
		Point lastPoint = e.getFrom().getCentre();
		for(Point p : e.getBends()) {
			if(pjr.graph.Util.linesCross(p1, p2, p, lastPoint)) {
				return true;
			}
			lastPoint = p;
		}
		if(pjr.graph.Util.linesCross(p1, p2, e.getTo().getCentre(), lastPoint)) {
			return true;
		}

		return false;
	}

	/**
	 * Find a route using TEs between the two TFs. The
	 * two TFs must be in the same face.
	 */
	public ArrayList<TriangulationEdge> routeThroughFace(Face f, TriangulationFace startTriangle, TriangulationFace endTriangle) {

		HashSet<TriangulationFace> visited = new HashSet<TriangulationFace>();
		ArrayList<TriangulationEdge> queue = new ArrayList<TriangulationEdge>();
		HashMap<TriangulationEdge,ArrayList<TriangulationEdge>> paths = new HashMap<TriangulationEdge,ArrayList<TriangulationEdge>>();

		TriangulationEdge te;
		
		visited.add(startTriangle);
		te = startTriangle.getTE1();
		if(te.getEdge() == null) {
			queue.add(te);
			ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>();
			path.add(te);
			paths.put(te,path);
		}
		te = startTriangle.getTE2();
		if(te.getEdge() == null) {
			queue.add(te);
			ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>();
			path.add(te);
			paths.put(te,path);
		}
		te = startTriangle.getTE3();
		if(te.getEdge() == null) {
			queue.add(te);
			ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>();
			path.add(te);
			paths.put(te,path);
		}

		TriangulationEdge current = null;
		while(queue.size() != 0) {
			current = queue.get(0);
			queue.remove(0);
			
			for(TriangulationFace tf: current.getTriangulationFaceList()) {
				
				if(tf == endTriangle) {
					// end found so return path
					return paths.get(current);
				}
				
				if(!visited.contains(tf)) {
					visited.add(tf);

					te = tf.getTE1();
					if(te.getEdge() == null) {
						queue.add(te);
						ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>(paths.get(current));
						path.add(te);
						paths.put(te,path);
					}
					te = tf.getTE2();
					if(te.getEdge() == null) {
						queue.add(te);
						ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>(paths.get(current));
						path.add(te);
						paths.put(te,path);
					}
					te = tf.getTE3();
					if(te.getEdge() == null) {
						queue.add(te);
						ArrayList<TriangulationEdge> path = new ArrayList<TriangulationEdge>(paths.get(current));
						path.add(te);
						paths.put(te,path);
					}
				}
			}

		}

		// no path found
		return null;
	}

	public static DualGraph buildGraphFromPolygon(Polygon p) {
		
		DualGraph dg = new DualGraph();
		
		Node startNode = null;
		Node prevNode = null;
		ArrayList<Point> points = new ArrayList<Point>();
		
		for(int i = 0 ; i < p.npoints; i++){
			Point centre = new Point(p.xpoints[i],p.ypoints[i]);
			if(!points.contains(centre)) {
				points.add(centre);
			
				Node n = new Node(centre);
				dg.addNode(n);
				if(startNode == null) {
					startNode = n;
				}
				if(prevNode != null) {
					Edge e = new Edge(prevNode,n);
					dg.addEdge(e);
				}
				prevNode = n;
			}
		}
		Edge e = new Edge(prevNode,startNode);
		dg.addEdge(e);

		return dg;
	}

	public DualGraph generateDualGraphWithDoubledOutsideEdges(ArrayList<ConcreteContour> concreteContours){
		
		DualGraph dg = getStraightLineDualOfGraph(concreteContours);		
		HashMap<String,Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		ArrayList<Edge> routedInsideEdges = new ArrayList<Edge>();
		
		// route inside zone edges first
		
		for(Edge e : dg.getEdges()) {
			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			if(!z1.equals("") && !z2.equals("")) {
				// two inside zones
				Area a1 = zoneAreaMap.get(z1);
				Area a2 = zoneAreaMap.get(z2);
				
				Area a = new Area(a1);
				a.add(a2);
				
				// force edges with parallels to go on the correct route
				ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
	
				for(Edge eEulerGraph : dualEdgeMap.keySet()) {
					Edge eDual = dualEdgeMap.get(eEulerGraph);
					if(eDual == e) {
						// dont need to consider this edge
						continue;
					}
					if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
						parallelEdges.add(eEulerGraph);
					}
				}
				for(Edge parallelEdge : parallelEdges) {
					a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
				}
				
				ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
				Polygon p = null;
				for(Polygon pTry : ps) {
					if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
						p = pTry;
					}
				}
				if(p == null) {
					System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
				} else {
				
					ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedInsideEdges,new ArrayList<Edge>());
					if(edgeBends == null) {
							System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
					} else {
						e.setBends(edgeBends);
						routedInsideEdges.add(e);
					}
				}
			}
		}
		
			
		// first double up all edges to the outside zone
		ArrayList<Edge> clockwiseEdges = new ArrayList<Edge>();
		ArrayList<Edge> antiClockwiseEdges = new ArrayList<Edge>();
		for(Edge e : dg.getEdges()) {
			if(e.getFrom().getLabel().equals("") || e.getTo().getLabel().equals("")) {
				clockwiseEdges.add(e);
			}
		}		
		for(Edge eC : clockwiseEdges) {
			Edge eA = new Edge(eC.getFrom(),eC.getTo(),eC.getLabel());
		//	dg.addEdge(eA);
			antiClockwiseEdges.add(eA);
		}
		
		// clockwise outside zone edges second
		ArrayList<Edge> routedClockwiseEdges = new ArrayList<Edge>();
	
		for(Edge e : clockwiseEdges) {
			
			if(e.getFrom().getLabel().equals("")) {
				// always start at the inner node
				e.reverse();
			}

			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			Area a1 = zoneAreaMap.get(z1);
			Area a2 = zoneAreaMap.get(z2);

			
			Area a = new Area(a1);
			a.add(a2);
			
			// force edges with parallels to go on the correct route
			ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
			for(Edge eEulerGraph : dualEdgeMap.keySet()) {
				Edge eDual = dualEdgeMap.get(eEulerGraph);
				if(eDual == e) {
					// dont need to consider this edge
					continue;
				}
				if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
					parallelEdges.add(eEulerGraph);
				}
			}
			for(Edge parallelEdge : parallelEdges) {
				a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
			}
			
			// stop any edge crossings, by removing edges already routed from the area
			ArrayList<Edge> routedEdges = new ArrayList<Edge>();
			routedEdges.addAll(routedInsideEdges);
			routedEdges.addAll(routedClockwiseEdges);

			// stop anticlockwise routing
			// assumes outside node is placed directly below contours
			Node outsideNode = dg.firstNodeWithLabel("");
			Point lowestPoint = new Point(outsideNode.getX(),outsideNode.getY()+1000);
			Point midPoint = new Point(outsideNode.getX()+5,outsideNode.getY());
			Rectangle bounds = GeneralConcreteDiagram.findContoursBounds(concreteContours); 
			Point topPoint = new Point(midPoint.x, bounds.y-2);
			
			GeneralPath path = new GeneralPath();
			path.moveTo(lowestPoint.x, lowestPoint.y);
			path.lineTo(midPoint.x, midPoint.y);
			path.lineTo(topPoint.x, topPoint.y);
			path.lineTo(midPoint.x+3, midPoint.y);

			
			Area aPath = new Area(path);
			
			a.subtract(aPath);
				
			ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
			Polygon p = null;
			for(Polygon pTry : ps) {
				if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
					p = pTry;
				}
			}

			if(p == null) {
				System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing clockwise, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
			} else {

				ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedEdges, new ArrayList<Edge>());
				if(edgeBends == null) {
						System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing clockwise, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
				} else {
					e.setBends(edgeBends);
					routedClockwiseEdges.add(e);
				}
			}

		}
			
		// anticlockwise outside zone edges third
		ArrayList<Edge> routedAntiClockwiseEdges = new ArrayList<Edge>();
		
		for(Edge e : antiClockwiseEdges) {
			
			if(e.getFrom().getLabel().equals("")) {
				// always start at the inner node
				e.reverse();
			}

			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();
			
			Area a1 = zoneAreaMap.get(z1);
			Area a2 = zoneAreaMap.get(z2);

			
			Area a = new Area(a1);
			a.add(a2);
	
			// stop any edge crossings, by removing edges already routed from the area
			ArrayList<Edge> routedEdges = new ArrayList<Edge>();
			routedEdges.addAll(routedInsideEdges);
			routedEdges.addAll(routedAntiClockwiseEdges);

			// stop clockwise routing
			// assumes outside node is placed directly below contours
			Node outsideNode = dg.firstNodeWithLabel("");
			Point lowestPoint = new Point(outsideNode.getX(),outsideNode.getY()+1000);
			Point midPoint = new Point(outsideNode.getX()-5,outsideNode.getY());
			Rectangle bounds = GeneralConcreteDiagram.findContoursBounds(concreteContours); 
			Point topPoint = new Point(midPoint.x, bounds.y-2);
			
			GeneralPath path = new GeneralPath();
			path.moveTo(lowestPoint.x, lowestPoint.y);
			path.lineTo(midPoint.x, midPoint.y);
			path.lineTo(topPoint.x, topPoint.y);
			path.lineTo(midPoint.x+3, midPoint.y);

			
			Area aPath = new Area(path);
			
			a.subtract(aPath);
				
			ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
			Polygon p = null;
			for(Polygon pTry : ps) {
				if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
					p = pTry;
				}
			}

			if(p == null) {
				System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing anticlockwise, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
			} else {

				ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedEdges, clockwiseEdges);
				if(edgeBends == null) {
						System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing anticlockwise, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
				} else {
					e.setBends(edgeBends);
					routedAntiClockwiseEdges.add(e);
				}
			}

		}
		
		
		
	
		return dg;
	}
	
	public DualGraph generateDualGraphWithOutsideEdges(ArrayList<ConcreteContour> concreteContours){
		
		DualGraph dg = getStraightLineDualOfGraph(concreteContours);		
		HashMap<String,Area> zoneAreaMap = ConcreteContour.generateZoneAreas(concreteContours);
		ArrayList<Edge> routedInsideEdges = new ArrayList<Edge>();
		
	
		for(Edge e : dg.getEdges()) {
			Node n1 = e.getFrom();
			Node n2 = e.getTo();
			
			String z1 = n1.getLabel();
			String z2 = n2.getLabel();

			Area a1 = zoneAreaMap.get(z1);
			Area a2 = zoneAreaMap.get(z2);
			Area a;
			if(a1!=null){	
				a = new Area(a1);
				if(a2!=null)
				a.add(a2);
			
				
			// force edges with parallels to go on the correct route
			ArrayList<Edge> parallelEdges = new ArrayList<Edge>();
	
			for(Edge eEulerGraph : dualEdgeMap.keySet()) {
				Edge eDual = dualEdgeMap.get(eEulerGraph);
					if(eDual == e) {
						continue;
					}
					if((eDual.getFrom() == e.getFrom() && eDual.getTo() == e.getTo()) || (eDual.getFrom() == e.getTo() && eDual.getTo() == e.getFrom())) {
						parallelEdges.add(eEulerGraph);
					}
				}
				for(Edge parallelEdge : parallelEdges) {
					a.subtract(findEdgeArea(parallelEdge,EDGEPADDING));
				}
				
				ArrayList<Polygon> ps = ConcreteContour.polygonsFromArea(a);
				Polygon p = null;
				for(Polygon pTry : ps) {
					if(pTry.contains(n1.getCentre()) && pTry.contains(n2.getCentre())) {
						p = pTry;
					}
				}
				if(p == null) {
					System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' no containing polygon found");
				} 
				else {
					ArrayList<Point> edgeBends = routeThroughPolygon(p,n1.getCentre(),n2.getCentre(),routedInsideEdges,new ArrayList<Edge>());
					if(edgeBends == null) {
							System.out.println("PROBLEM IN ConstructedConcreteDiagram.generateDualGraph() when routing inside edge, joining adjacent zones '"+z1+"' and '"+z2+"' finding edge bends results in null list.");
					} else {
						e.setBends(edgeBends);
						routedInsideEdges.add(e);
					}
				}
			}			
		}
		return dg;
	}
	public boolean loadDiagram(File fileName){		
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));			
			Character c = new Character(FILESEPARATOR);
			String separatorString = new String(c.toString());
			concreteContours = new ArrayList<ConcreteContour>();
			abstractDescription = "";
			boolean readingAbstractDescription = false;
			boolean readingContours = false;			
			String line = b.readLine();			
			while(line != null) { 				
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}
				if(readingAbstractDescription && line.compareTo(FILESTARTABSTRACTDESCRIPTION)!=0 && line.compareTo(FILESTARTCONTOURS)!=0) {
					abstractDescription = line;
				//	System.out.println(abstractDescription);
				}
				if(readingContours && line.compareTo(FILESTARTCONTOURS)!=0){ 
					StringBuffer parseLine = new StringBuffer(line);
					int separatorInd = 0;
					
				// get contour label
					separatorInd = parseLine.indexOf(separatorString);
					String label = parseLine.substring(0,separatorInd);
					parseLine.delete(0,separatorInd+1);
				//	System.out.println(label);
					//get coordinates					
					ArrayList<String> XYCor = new ArrayList<String>();
					
					while(parseLine.length()!=0){
						separatorInd = parseLine.indexOf(separatorString);
						String cor = parseLine.substring(0,separatorInd);
					//	System.out.println(cor);
						XYCor.add(cor);
						parseLine.delete(0,separatorInd+1);
					}
					int xy[] = new int[XYCor.size()];					
					for(int i = 0 ; i < XYCor.size(); i++){
						xy[i] = new Integer(Integer.parseInt(XYCor.get(i)));					
					}					
					Polygon pol = new Polygon();
					for(int i = 0 ; i < xy.length; i+=2){
						pol.addPoint(xy[i],xy[i+1]);						
					}					
					ConcreteContour cc = new ConcreteContour(label, pol);					
					concreteContours.add(cc);
				}				
				if(line.compareTo(FILESTARTABSTRACTDESCRIPTION)==0) {
					readingAbstractDescription = true;
					readingContours = false;				
				}
				if(line.compareTo(FILESTARTCONTOURS)==0) {
					readingAbstractDescription = false;
					readingContours = true;
				}		
				line = b.readLine();				
			}			
			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAdjacencyFile("+fileName+") in ThreeSetDiagramLibrary.java: "+e+"\n");
			System.exit(1);
		}
		return(true);		
	}
	public boolean saveToFile(File file) {	
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));
			b.append(FILESTARTDIAGRAM);
			b.newLine();
				b.append(FILESTARTABSTRACTDESCRIPTION);
			b.newLine();
			b.append(abstractDescription);
			b.newLine();
				b.append(FILESTARTCONTOURS);
			b.newLine();
				
			for(ConcreteContour cc : concreteContours){
				//System.out.println(cc.getAbstractContour());
				StringBuffer outputContour = new StringBuffer("");
				outputContour.append(cc.getAbstractContour());
				outputContour.append(FILESEPARATOR);
				Polygon pol = cc.getPolygon();
				for(int i = 0 ; i < pol.npoints; i++){
				//		System.out.println(pol.xpoints[i]+" " +pol.ypoints[i]);
						outputContour.append(pol.xpoints[i]);
						outputContour.append(FILESEPARATOR);
						outputContour.append(pol.ypoints[i]);
						outputContour.append(FILESEPARATOR);
					}
					b.append(outputContour);
					b.newLine();
				}		
				b.close();
			}
			catch(IOException e) {
				System.out.println("An IO exception occured when executing saveAll("+file.getName()+") in ThreeSetDiagramLibrary.java "+e+"\n");
				return false;
			}
			return true;
		}
		public boolean appendToFile(File file) {		 
	
			try {
				BufferedWriter b = new BufferedWriter(new FileWriter(file,true));
				b.append(FILESTARTDIAGRAM);
				b.newLine();
				b.append(FILESTARTABSTRACTDESCRIPTION);
				b.newLine();
				b.append(abstractDescription);
				b.newLine();
				b.append(FILESTARTCONTOURS);
				b.newLine();				
				for(ConcreteContour cc : concreteContours){
					//System.out.println(cc.getAbstractContour());
					StringBuffer outputContour = new StringBuffer("");
					outputContour.append(cc.getAbstractContour());
					outputContour.append(FILESEPARATOR);
					Polygon pol = cc.getPolygon();
					for(int i = 0 ; i < pol.npoints; i++){
				//		System.out.println(pol.xpoints[i]+" " +pol.ypoints[i]);
						outputContour.append(pol.xpoints[i]);
						outputContour.append(FILESEPARATOR);
						outputContour.append(pol.ypoints[i]);
						outputContour.append(FILESEPARATOR);
					}
					b.append(outputContour);
					b.newLine();
				}		
				b.close();
			}
			catch(IOException e) {
				System.out.println("An IO exception occured when executing saveAll("+file.getName()+") in ThreeSetDiagramLibrary.java "+e+"\n");
				return false;
			}
			return true;
	}
		public void scale(double multiplier){
			ConcreteDiagram.scaleContours(concreteContours, multiplier);
		}
		public Point findCentre() {

			int maxX = Integer.MIN_VALUE;
			int minX = Integer.MAX_VALUE;
			int maxY = Integer.MIN_VALUE;
			int minY = Integer.MAX_VALUE;
			for(ConcreteContour cc : concreteContours){
				Polygon pol = cc.getPolygon();
				for(int i = 0 ; i < pol.npoints; i++){
					if(pol.xpoints[i] > maxX){
						maxX = pol.xpoints[i];					
					}
					if(pol.xpoints[i] < minX){
						minX = pol.xpoints[i];
					}
					if(pol.ypoints[i] >maxY){
						maxY = pol.ypoints[i];
					}
					if(pol.ypoints[i] < minY){
						minY = pol.ypoints[i];
					}					
				}
			}			
			int x = minX + (maxX - minX)/2;
			int y = minY + (maxY - minY)/2;

			Point ret = new Point(x,y);
			return ret;
		}
		public ConcreteContour getContourNearPoint(Point p){			
			for(ConcreteContour cc : concreteContours){
				Polygon pol = cc.getPolygon();
				if(pol.contains(p))
					return cc;			
			} 
			return null;
		}		
	
}


	