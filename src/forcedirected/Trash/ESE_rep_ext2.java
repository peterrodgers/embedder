package forcedirected.Trash;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import euler.*;
import euler.construction.ConstructedDiagramPanel;
import euler.construction.ConstructedDiagramWindow;
import euler.display.DualGraphWindow;
import euler.drawers.DiagramDrawer;
import euler.inductive.*;

/**

 * @author Luana Micallef
 */


public class ESE_rep_ext2 extends DiagramDrawer{
	

	///Class Default Values 
	private static final int DEFAULT_ACCELERATORKEY = KeyEvent.VK_S; 
	
	protected static final int DEFAULT_I = 50;  //300; //75; 					//500; //500; //500 //120//500;
	
	protected static final double DEFAULT_A = 0.15; //0.15;                     //0.28//0.04; //0.2 //0.01
	protected static final double DEFAULT_CM_OC_A = 0;//0.001;//0.008;//0.01; //.01; //0.02; //0.001; 		//0.2;
	protected static final double DEFAULT_CM_NOC_A = 0;	 //useless	 	//0.2;
	protected static final double DEFAULT_ZM_IZ_A = .018; //0.015//0.01; //0.02;		//0.04; .01; //0.2;
	protected static final double DEFAULT_ZM_NIZ_A = .02;//0.025; 0.02; //0.15;		//0.04; .01; //0.2;
	
	protected static final double DEFAULT_R = 800; //800; 			  //10000; //90000 //1000
	protected static final double DEFAULT_CM_OC_R = 0;//25; //20;//50;//50;	//useless		//10000; //100000;
	protected static final double DEFAULT_CM_NOC_R = 0; //25;			//1000;//100;//10000; //100000;
	protected static final double DEFAULT_ZM_IZ_R = 30; //60;//100;		//10; //000; //50; //100000;	
	protected static final double DEFAULT_ZM_NIZ_R = 20;//30;//100;		//4000;//10; //000; //50; //100000;

	protected static final double DEFAULT_M = 1; 				//1.5; //1

	protected static final double DEFAULT_AT = 100; //100;
	protected static final double DEFAULT_RT = 20;//20;
	protected static final boolean at_on = false; //true; //set at to 100 -> for 4 sets; //false;
	protected static final boolean rt_on = true; //set rt to 20 - for 4 sets
	
	
	///Object Data Fields/Variables

	///  No. of Iterations
	protected int i;
	
	///  Forces' Constants
	///    Attraction 
	protected double a;
	///    Attraction from Contour Internal Middle (for points on contour)
	protected double cm_oc_a;
	///    Attraction from Contour Internal Middle (for points not on contour)
	protected double cm_noc_a;
	///    Attraction from Zone Internal Middle (for points in zone)
	protected double zm_iz_a;
	///    Attraction from Zone Internal Middle (for points not in zone)
	protected double zm_niz_a;
	
	///    Repulsion 
	protected double r;
	///    Repulsion from Contour Internal Middle (for points on contour)
	protected double cm_oc_r;
	///    Repulsion from Contour Internal Middle (for points not on contour)
	protected double cm_noc_r;
	///    Repulsion from Zone Internal Middle (for points in zone)
	protected double zm_iz_r;
	///    Repulsion from Zone Internal Middle (for points not in zone)
	protected double zm_niz_r;
	
	///  Node Movement Constant
	protected double m;
	
	protected double at = DEFAULT_AT;
	protected double rt = DEFAULT_RT;
	

	
	private ArrayList<ConcreteContour> concreteContours;
	private ConstructedDiagramPanel constructedDiagramPanel;
	private ConstructedDiagramWindow constructedDiagramWindow;
	private DiagramPanel diagramPanel;
	private DualGraphWindow dualGraphWindow;
	
	private ArrayList<ArrayList<Polygon>> ozspolys;
	
	
	
	///Constructors

	public ESE_rep_ext2(){ //(DiagramPanel inDiagramPanel) {
		super(DEFAULT_ACCELERATORKEY, "Force Directed", DEFAULT_ACCELERATORKEY);
		//setDiagramPanel(inDiagramPanel);
		setAll();
	}
	
	public ESE_rep_ext2(int inAcceleratorKey){ //, DiagramPanel inDiagramPanel) {
		super(inAcceleratorKey, "Force Directed", inAcceleratorKey);
		//setDiagramPanel(inDiagramPanel);
		setAll();
	}

	public ESE_rep_ext2 (int inAcceleratorKey, int inI, double inA, double inCM_OC_A, double inCM_NOC_A, double inZM_IZ_A, double inZM_NIZ_A, double inR, double inCM_OC_R, double inCM_NOC_R, double inZM_IZ_R, double inZM_NIZ_R, double inM){ //, DiagramPanel inDiagramPanel){
		super (inAcceleratorKey, "Force Directed", inAcceleratorKey);
		//setDiagramPanel(inDiagramPanel);
		setAll (inI, inA, inCM_OC_A, inCM_NOC_A, inZM_IZ_A, inZM_NIZ_A, inR, inCM_OC_R, inCM_NOC_R, inZM_IZ_R, inZM_NIZ_R, inM);
	}
	
	
	
	///Properties (accessor & mutator methods)

	public int getI(){
		return i;
	}
	
	public void setI(int inI){
		i = inI;
	}
	
	
	public double getA(){
		return a;
	}
	
	public void setA(double inA){
		a = inA;
	}

	public double getCM_OC_A(){
		return cm_oc_a;
	}
	
	public void setCM_OC_A(double inCM_OC_A){
		cm_oc_a = inCM_OC_A;
	}

	public double getCM_NOC_A(){
		return cm_noc_a;
	}
	
	public void setCM_NOC_A(double inCM_NOC_A){
		cm_noc_a = inCM_NOC_A;
	}
	
	
	public double getZM_IZ_A(){
		return zm_iz_a;
	}
	
	public void setZM_IZ_A(double inZM_IZ_A){
		zm_iz_a = inZM_IZ_A;
	}	
	
	public double getZM_NIZ_A(){
		return zm_niz_a;
	}
	
	public void setZM_NIZ_A(double inZM_NIZ_A){
		zm_niz_a = inZM_NIZ_A;
	}
	
	
	public double getR(){
		return r;
	}
	
	public void setR(double inR){
		r = inR;
	}
	
	public double getCM_OC_R(){
		return cm_oc_r;
	}
	
	public void setCM_OC_R(double inCM_OC_R){
		cm_oc_r = inCM_OC_R;
	}

	public double getCM_NOC_R(){
		return cm_noc_r;
	}
	
	public void setCM_NOC_R(double inCM_NOC_R){
		cm_noc_r = inCM_NOC_R;
	}
	
	
	public double getZM_IZ_R(){
		return zm_iz_r;
	}
	
	public void setZM_IZ_R(double inZM_IZ_R){
		zm_iz_r = inZM_IZ_R;
	}	
	
	public double getZM_NIZ_R(){
		return zm_niz_r;
	}
	
	public void setZM_NIZ_R(double inZM_NIZ_R){
		zm_niz_r = inZM_NIZ_R;
	}
	
	
	public double getM(){
		return m;
	}
	
	public void setM(double inM){
		m = inM;
	}
	
	
	public void setAll (){
		setI(DEFAULT_I);
		setA(DEFAULT_A);
		setCM_OC_A(DEFAULT_CM_OC_A);
		setCM_NOC_A(DEFAULT_CM_NOC_A);
		setZM_IZ_A(DEFAULT_ZM_IZ_A);
		setZM_NIZ_A(DEFAULT_ZM_NIZ_A);
		setR(DEFAULT_R);
		setCM_OC_R(DEFAULT_CM_OC_R);
		setCM_NOC_R(DEFAULT_CM_NOC_R);		
		setZM_IZ_R(DEFAULT_ZM_IZ_R);
		setZM_NIZ_R(DEFAULT_ZM_NIZ_R);
		setM(DEFAULT_M);
	}
	
	public void setAll (int inI, double inA, double inCM_OC_A, double inCM_NOC_A, double inZM_IZ_A, double inZM_NIZ_A, double inR, double inCM_OC_R, double inCM_NOC_R, double inZM_IZ_R, double inZM_NIZ_R, double inM){
		setI(inI);
		setA(inA);
		setCM_OC_A(inCM_OC_A);
		setCM_NOC_A(inCM_NOC_A);
		setZM_IZ_A(inZM_IZ_A);
		setZM_NIZ_A(inZM_NIZ_A);
		setR(inR);
		setCM_OC_R(inCM_OC_R);
		setCM_NOC_R(inCM_NOC_R);
		setZM_IZ_R(inZM_IZ_R);
		setZM_NIZ_R(inZM_NIZ_R);
		setM(inM);
	}
	
	

	public ArrayList<ConcreteContour> getConcreteContours(){
		return concreteContours;
	}
	
	public void setConcreteContours (ArrayList<ConcreteContour> inConcreteContours){
		concreteContours = inConcreteContours;
		for (ConcreteContour concreteContour : concreteContours){
			concreteContour.setContourLines();
		}
	}	
	
	
	public ConstructedDiagramPanel getConstructedDiagramPanel(){
		return constructedDiagramPanel;
	}
	
	public void setConstructedDiagramPanel (ConstructedDiagramPanel inConstructedDiagramPanel){
		constructedDiagramPanel = inConstructedDiagramPanel;
		//constructedDiagramPanel.requestFocus();
	}	
	
	
	public ConstructedDiagramWindow getConstructedDiagramWindow(){
		return constructedDiagramWindow;
	}
	
	public void setConstructedDiagramWindow (ConstructedDiagramWindow inConstructedDiagramWindow){
		constructedDiagramWindow = inConstructedDiagramWindow;
		//constructedDiagramPanel.requestFocus();
	}	
	


	public DiagramPanel getDiagramPanel(){
		return diagramPanel;
	}
	
	public void  setDiagramPanel (DiagramPanel inDiagramPanel){
		diagramPanel = inDiagramPanel;
		//constructedDiagramPanel.requestFocus();
	}	
	
	
	public DualGraphWindow getDualGraphWindow(){
		return dualGraphWindow;
	}
	
	public void setDualGraphWindow (DualGraphWindow inDualGraphWindow){
		dualGraphWindow = inDualGraphWindow;
		/*
		constructedDiagramWindow.getConstructedDiagramPanel().setShowEdgeDirection(false);
		constructedDiagramWindow.getConstructedDiagramPanel().setShowEdgeLabel(true);
		constructedDiagramWindow.getConstructedDiagramPanel().setShowGraph(false);
		constructedDiagramWindow.getConstructedDiagramPanel().setShowContour(true);
		constructedDiagramWindow.getConstructedDiagramPanel().setShowContourLabel(true);
		constructedDiagramWindow.getConstructedDiagramPanel().setShowTriangulation(false);
		
		ConstructedDiagramPanel cdp = constructedDiagramWindow.getConstructedDiagramPanel();
		cdp.addDiagramView(new DiagramViewCycleItemsDisplayed(KeyEvent.VK_D, "Force Directed", KeyEvent.VK_D));
		constructedDiagramWindow.initMenu();   //can do it with constructedDiagramWindow.actionPerformed(event); instead and => can reset initMenu to private? 
		//cdp.requestFocus();
		
		setConstructedDiagramPanel(constructedDiagramPanel);
		*/
	}	
	
	
	///Force functions
	
	protected double totalAttraction (double distance){
		double att = (a * distance);
		//if (distance < at){
		if ((at_on)&&(distance > at)){
			return 0;
		} else {
			return att;
		}
	}
	
	protected double totalContourMiddleAttraction_PointOnContour (double distance){
		double att = (cm_oc_a * distance);
		if ((at_on)&&(distance > at)){
			return 0;
		} else {
			return att;
		}
	}	
	
	protected double totalContourMiddleAttraction_PointNotOnContour (double distance){
		double att = (cm_noc_a * distance);
		if ((at_on)&&(distance > at)){
			return 0;
		} else {
			return att;
		}
	}	
	
	protected double totalZoneMiddleAttraction_PointInZone  (double distance){
		double att = (zm_iz_a * distance);
		if ((at_on)&&(distance > at)){
			return 0;
		} else {
			return att;
		}
	}
	
	protected double totalZoneMiddleAttraction_PointNotInZone  (double distance){
		double att = (zm_niz_a * distance);
		if ((at_on)&&(distance > at)){
			return 0;
		} else {
			return att;
		}
	}	
	
	protected double totalRepulsion (double distance){
		double rep =  r / ((double)(Math.pow(distance,2)));
		if ((rt_on)&&(distance < rt)){
			return 0;
		} else {
			return rep;
		}
	}
	
	protected double totalContourMiddleRepulsion_PointOnContour (double distance){
		double rep = (cm_oc_r / ((double)(Math.pow(distance,2))));
		if ((rt_on)&&(distance < rt)){
			return 0;
		} else{
			return rep;
		}
	}
	
	protected double totalContourMiddleRepulsion_PointNotOnContour (double distance){
		double rep = (cm_noc_r / ((double)(Math.pow(distance,2))));
		if ((rt_on)&&(distance < rt)){
			return 0;
		} else{
			return rep;
		}
	}
	
	protected double totalZoneMiddleRepulsion_PointInZone (double distance){
		double rep = (zm_iz_r / ((double)(Math.pow(distance,2))));
		if (distance < rt){
			return 0;
		} else{
			return rep;
		}
	}	
	
	protected double totalZoneMiddleRepulsion_PointNotInZone (double distance){
		double rep = (zm_niz_r / ((double)(Math.pow(distance,2))));
		if (distance < rt){
			return 0;
		} else{
			return rep;
		}
	}	
	
	///Methods 

	

	
	/**
	 * This needs to be implemented for all GraphDrawers.
	 * Its executed when the accelerator key is pressed.
	 */

	//	overrides parent GraphDrawer and is overridden by its inheriting children 
	
	public void layout() {
		
		//this.setConcreteContours((this.getDiagramPanel().getConcreteDiagram()).getConcreteContours());
		/*
		ConcreteDiagram cd = getDiagramPanel().getConcreteDiagram();
		setConcreteContours(cd.getConcreteContours());
		*/	
		
		//DiagramPanel panel = getDiagramPanel();
		
		//Graph g = gp.getGraph();
		//ArrayList<Node> nodes = g.getNodes();
		
		// no need to draw an empty graph
		//if(nodes.size() == 0) {
		//	return;
		//}
		
		/*
		// Get polygons of all zones of all contours
		HashMap<String, Area> zoneAreas = ConcreteContour.generateZoneAreas(concreteContours);
		Iterator itr = zoneAreas.entrySet().iterator();
		ArrayList<ArrayList<Polygon>> zspolys = new ArrayList<ArrayList<Polygon>>();
		while (itr.hasNext()){
			Entry<String,Area> pairs = (Entry<String,Area>)itr.next();
			zspolys.add(ConcreteContour.polygonsFromArea(pairs.getValue()));
		}
		zspolys.remove(0);  //remove the empty set
		*/
		
		
		// Equalise the number of points each contour has
		
		//  Find contour with max number of points 
		int maxPntsOnContour = 0;
		for (ConcreteContour cc : concreteContours){
			Polygon cpoly = cc.getPolygon();
			int cpolyPntsCount = cpoly.npoints;
			if (cpolyPntsCount > maxPntsOnContour)
				maxPntsOnContour = cpolyPntsCount;
		}
		
		//  Add required points to contours
		//      by just splitting up the current contour lines
		for (ConcreteContour cc : concreteContours){
			Polygon cpoly = cc.getPolygon();
			
			int currentPntCount = cpoly.npoints;
			int pntsToAddCount = maxPntsOnContour - currentPntCount;
			if (pntsToAddCount == 0)
				continue;
			int finalPntCount = currentPntCount + pntsToAddCount;
			
			ArrayList<ContourLine> contourlines = cc.getContourLines();
			int pntsAddedCount = 0;
			int cl_i = 0;
			Polygon npoly = new Polygon();
			do{
				if (cl_i == contourlines.size()){
					cc.setPolygon(npoly);
					cc.setContourLines();
					cl_i = 0;
					npoly = new Polygon();
				}
				
				contourlines = cc.getContourLines();
				ContourLine cl = contourlines.get(cl_i);
				Line2D.Double cl_line = cl.getLine(); 

				Point2D.Double newPnt = getMidPnt(new Point2D.Double (cl_line.x1, cl_line.y1), new Point2D.Double (cl_line.x2, cl_line.y2));
				npoly.addPoint((int)Math.round(cl_line.x1), (int)Math.round(cl_line.y1));
				npoly.addPoint((int)Math.round(newPnt.x), (int)Math.round(newPnt.y)); //need to reset the polygon of contour or since this is just pointed it, the actual one would be changed at this point
				//npoly.addPoint((int)Math.round(cl_line.x2), (int)Math.round(cl_line.y2));
				//cc.setPolygon(cpoly); //this or replacePolygon???
				//just add a point and setContourLines would automatically deduce the new contour lines
								
				//contourlines.add(new ContourLine(cl.getLabel(), new Line2D.Double (cl_line.x1, newPnt.x,  cl_line.y1, newPnt.y)));
				//contourlines.add(new ContourLine(cl.getLabel(), new Line2D.Double (newPnt.x,  cl_line.x2, newPnt.y, cl_line.y2)));
				//contourlines.remove(cl_i);
 
				pntsAddedCount++;
				cl_i++;
				
			} while (pntsAddedCount < pntsToAddCount);
			
			while (npoly.npoints < finalPntCount){
				ContourLine cl = contourlines.get(cl_i);
				Line2D.Double cl_line = cl.getLine(); 

				npoly.addPoint((int)Math.round(cl_line.x1), (int)Math.round(cl_line.y1));
				//npoly.addPoint((int)Math.round(cl_line.x2), (int)Math.round(cl_line.y2));
				
				cl_i++;
			}
			cc.setPolygon(npoly);
			cc.setContourLines();
		}
		
		
		
		/*
		for (ConcreteContour ccc : concreteContours){
			ccc.setContourLines();
		}
		*/
		
		
		// Get polygons of all zones of all contours
		HashMap<String, Area> ozoneAreas = ConcreteContour.generateZoneAreas(concreteContours);
		Iterator oitr = ozoneAreas.entrySet().iterator();
		//ArrayList<ArrayList<Polygon>> 
		ozspolys = new ArrayList<ArrayList<Polygon>>();
		
		HashMap<String, ArrayList<String>> origZonesForPnts = new HashMap<String, ArrayList<String>>();
				
		while (oitr.hasNext()){
			Entry<String,Area> opairs = (Entry<String,Area>)oitr.next();
			ArrayList<Polygon> ozpolys = (ConcreteContour.polygonsFromArea(opairs.getValue()));
			for (Polygon ozpoly : ozpolys){
				int ci =0;
				for (ConcreteContour cc : concreteContours){
					Polygon cpoly = cc.getPolygon();
					for (int p=0; p<cpoly.npoints; p++){
						Point2D.Double pnt = new Point2D.Double(cpoly.xpoints[p], cpoly.ypoints[p]);
						String pntID = Integer.toString(ci) + Integer.toString(p);
						if (ozpoly.contains(pnt)){
							if (origZonesForPnts.containsKey(pntID)){
								(origZonesForPnts.get(pntID)).add(opairs.getKey());
							}else{
								ArrayList<String> arrStr = new ArrayList<String>();
								arrStr.add(opairs.getKey());
								origZonesForPnts.put(pntID, arrStr);
							}
						}
					}
					ci++;
				}
				
			}
		}
		//ozspolys.remove(0);  //remove the empty set
		
		
		
		// Iterate & move pnts i times
		for (int c = 0; c < i; c++){
			
			ArrayList<ArrayList<Point2D.Double>> xyForceOnPntsOnContours = new ArrayList<ArrayList<Point2D.Double>>();
			
			// Get polygons of all zones of all contours
			ArrayList <String> zonesNames = new ArrayList <String>();
			
			HashMap<String, Area> zoneAreas = ConcreteContour.generateZoneAreas(concreteContours);
			Iterator itr = zoneAreas.entrySet().iterator();
			ArrayList<ArrayList<Polygon>> zspolys = new ArrayList<ArrayList<Polygon>>();
			while (itr.hasNext()){
				Entry<String,Area> pairs = (Entry<String,Area>)itr.next();
				zspolys.add(ConcreteContour.polygonsFromArea(pairs.getValue()));
				zonesNames.add(pairs.getKey());
			}
			zonesNames.remove(0);
			zspolys.remove(0);  //remove the empty set
		
			
			int cci =0;
			for (ConcreteContour cc : concreteContours){
				
				ArrayList<Point2D.Double> xyForceOnPntsOnContour = new ArrayList<Point2D.Double>();
		
				Polygon cpoly = cc.getPolygon();
				//cc.setContourLines();
				
				ArrayList<ContourLine> contourLines = cc.getContourLines();
				Point contourInternalMiddlePnt = HybridGraph.findMiddlePointInsidePolygon(cpoly);
				
				
				
				
				//Calculate all the forces on each point of this contour
				for (int p=0; p < cpoly.npoints; p++)
				{
					Point2D.Double pnt = new Point2D.Double (cpoly.xpoints[p], cpoly.ypoints[p]);
					Point2D.Double pnt2;
					
					Point2D.Double xyForceOnPnt = new Point2D.Double(0,0);
					
					/*
					//calculate repulsive force
					//    in relation with every other point on this and all the other contours
					for (ConcreteContour cc2 : concreteContours){	
 						Polygon cpoly2 = cc2.getPolygon();
						
						for (int p2=0; p2 < cpoly2.npoints; p2++)
						{
							//if refers to same node then skip
							if ((cc2 == cc) && (p2 == p)){
								continue;}
							
							pnt2 = new Point2D.Double(cpoly2.xpoints[p2], cpoly2.ypoints[p2]);
							xyForceOnPnt = addxyRepulsion(xyForceOnPnt, pnt, pnt2);
						}
					}
					*/
				 	
					
					// ** only one of the repulsive forces should be active
					
					//calculate repulsive force
					//    in relation with every other point ON THIS contour
					for (int p2=0; p2 < cpoly.npoints; p2++)
					{
						//if refers to same node then skip
						if (p2 == p){
							continue;}
						
						pnt2 = new Point2D.Double (cpoly.xpoints[p2], cpoly.ypoints[p2]);
						xyForceOnPnt = addxyRepulsion(xyForceOnPnt, pnt, pnt2);
					}
					
					
				
					//calculate attractive force 
					//    for every spring (i.e. edge) attached to the node
				
					for (ContourLine cl : contourLines){
						Line2D.Double clPnts = cl.getLine();
						if ((clPnts.x1 == pnt.x) && (clPnts.y1 == pnt.y)){
							pnt2 = new Point2D.Double (clPnts.x2, clPnts.y2);
							xyForceOnPnt = addxyAttraction(xyForceOnPnt, pnt, pnt2);
						} else if ((clPnts.x2 == pnt.x) && (clPnts.y2 == pnt.y)){
							pnt2 = new Point2D.Double (clPnts.x1, clPnts.y1);
							xyForceOnPnt = addxyAttraction(xyForceOnPnt, pnt, pnt2);
						}
					}
					
				

					//calculate attractive and repulsive force from internal middle of contour
					xyForceOnPnt = addxyContourMiddleAttraction_PointOnContour(xyForceOnPnt, pnt, new Point2D.Double(contourInternalMiddlePnt.x, contourInternalMiddlePnt.y));
					xyForceOnPnt = addxyContourMiddleRepulsion_PointOnContour(xyForceOnPnt, pnt, new Point2D.Double(contourInternalMiddlePnt.x, contourInternalMiddlePnt.y)); //useless
					/*
					for (ConcreteContour c2: concreteContours){
						Polygon c2poly = c2.getPolygon();
						Point contour2InternalMiddlePnt = HybridGraph.findMiddlePointInsidePolygon(c2poly);
						if (c2poly.contains(pnt)){
							xyForceOnPnt = addxyContourMiddleAttraction_PointOnContour(xyForceOnPnt, pnt, new Point2D.Double(contour2InternalMiddlePnt.x, contour2InternalMiddlePnt.y));
						} else {
							//useless //xyForceOnPnt = addxyContourMiddleAttraction_PointNotOnContour(xyForceOnPnt, pnt, new Point2D.Doublecontour2InternalMiddlePnt.y));
							xyForceOnPnt = addxyContourMiddleRepulsion_PointNotOnContour(xyForceOnPnt, pnt, new Point2D.Double(contour2InternalMiddlePnt.x, contour2InternalMiddlePnt.y));
						}
					}
					*/
					
					
					/*
					///calculate attractive and repulsive force from internal middle of zones
					for (ArrayList<Polygon> zpolys : zspolys){
						for (Polygon zpoly : zpolys){
							if (zpoly.contains(pnt)){
								Point zoneInternalMiddlePnt = HybridGraph.findMiddlePointInsidePolygon(zpoly);
								xyForceOnPnt = addxyZoneMiddleAttraction(xyForceOnPnt, pnt, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
								xyForceOnPnt = addxyZoneMiddleRepulsion(xyForceOnPnt, pnt, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
							}
						}
					}
					*/
				    
					
					///calculate attractive and repulsive force from internal middle of zones
					int z = 0;
					for (ArrayList<Polygon> zpolys : zspolys){
						String zoneName = zonesNames.get(z);
						String pntID = Integer.toString(cci) + Integer.toString(p);
						
						boolean pntOrigInZone = ((ArrayList<String>)origZonesForPnts.get(pntID)).contains(zoneName);
						
						for (Polygon zpoly : zpolys){
							
							boolean pntInZone = zpoly.contains(pnt);
					
							Point zoneInternalMiddlePnt = HybridGraph.findMiddlePointInsidePolygon(zpoly);
							
							Point2D.Double newPntPost = getNewPntPos(xyForceOnPnt, pnt);  //work out zm force that will be required on the new moved pnt not previous because after some changes due to forces in this iteration, a pnt might have ended up in a wrong zone
							
							
							if (pntInZone && pntOrigInZone){
								xyForceOnPnt = addxyZoneMiddleAttraction_PointInZone(xyForceOnPnt, newPntPost, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
							} else if (pntInZone && !pntOrigInZone){
								xyForceOnPnt = addxyZoneMiddleRepulsion_PointInZone(xyForceOnPnt, newPntPost, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
							} else if (!pntInZone && pntOrigInZone){
								xyForceOnPnt = addxyZoneMiddleAttraction_PointNotInZone(xyForceOnPnt, newPntPost, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
							} else if (!pntInZone && !pntOrigInZone){
								xyForceOnPnt = addxyZoneMiddleRepulsion_PointNotInZone(xyForceOnPnt, newPntPost, new Point2D.Double(zoneInternalMiddlePnt.x, zoneInternalMiddlePnt.y));
							}
							
						}
						z++;
					}
					
					xyForceOnPntsOnContour.add(xyForceOnPnt);
					
				}
				cci++;

				xyForceOnPntsOnContours.add(xyForceOnPntsOnContour);							
			}

			//Move the point
			for (int ci = 0; ci < xyForceOnPntsOnContours.size(); ci++){
				ConcreteContour cc = concreteContours.get(ci);
		
				Polygon cpoly = cc.getPolygon();
				ArrayList<Point2D.Double> xyForceOnPntsOnContour = xyForceOnPntsOnContours.get(ci);
				
				for (int p =0; p < xyForceOnPntsOnContour.size(); p++){
					Point2D.Double pnt = new Point2D.Double(cpoly.xpoints[p], cpoly.ypoints[p]);
					Point2D.Double newPntPost = getNewPntPos(xyForceOnPntsOnContour.get(p),pnt);
					
					cpoly.xpoints[p] = (int) (Math.round (newPntPost.x));
					cpoly.ypoints[p] = (int) (Math.round (newPntPost.y));	
				}
				cc.setPolygon(cpoly);
				cc.setContourLines();
			}

			constructedDiagramPanel.update(constructedDiagramPanel.getGraphics());
		}
		
	}

	
	
	
	// Protected Methods
	
	private Point2D.Double getMidPnt(Point2D.Double pnt1, Point2D.Double pnt2){
		return new Point2D.Double(((pnt1.x + pnt2.x)/2), ((pnt1.y + pnt2.y)/2));
	}
	
	//  Force Type
	protected enum ForceType {a, cm_oc_a, cm_noc_a, zm_iz_a, zm_niz_a, r, cm_oc_r, cm_noc_r, zm_iz_r, zm_niz_r};
	
	/// Add node's Attraction force to the current node's total force
	protected Point2D.Double addxyAttraction (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.a, xyForce, pnt1, pnt2));
	}

	/// Add node's Attraction force from internal middle of contour
	protected Point2D.Double addxyContourMiddleAttraction_PointOnContour (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.cm_oc_a, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Attraction force from internal middle of contour
	protected Point2D.Double addxyContourMiddleAttraction_PointNotOnContour (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.cm_noc_a, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Attraction force from internal middle of contour
	protected Point2D.Double addxyZoneMiddleAttraction_PointInZone (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.zm_iz_a, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Attraction force from internal middle of contour
	protected Point2D.Double addxyZoneMiddleAttraction_PointNotInZone (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.zm_niz_a, xyForce, pnt1, pnt2));
	}
	
	
	/// Add node's Repulsion force to the current node's total force
	protected Point2D.Double addxyRepulsion (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.r, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Repulsion force to the current node's total force
	protected Point2D.Double addxyContourMiddleRepulsion_PointOnContour (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.cm_oc_r, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Repulsion force to the current node's total force
	protected Point2D.Double addxyContourMiddleRepulsion_PointNotOnContour (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.cm_noc_r, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Repulsion force to the current node's total force
	protected Point2D.Double addxyZoneMiddleRepulsion_PointInZone  (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.zm_iz_r, xyForce, pnt1, pnt2));
	}
	
	/// Add node's Repulsion force to the current node's total force
	protected Point2D.Double addxyZoneMiddleRepulsion_PointNotInZone  (Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
		return (addxyForce(ForceType.zm_niz_r, xyForce, pnt1, pnt2));
	}	
	
	
	/// Add node's Attraction (opt ='a') / Repulsion (opt ='r') force to the current node's total force
	protected Point2D.Double addxyForce(ForceType ft, Point2D.Double xyForce, Point2D.Double pnt1, Point2D.Double pnt2){
	
		Point2D.Double xyDistance = getxyAbsDistances (pnt1, pnt2);
		double distance = getAbsHypotenuse(xyDistance);

		double totalForce = 0;
		Point2D.Double xyForceDirection = new Point2D.Double (1,1);
		switch (ft){
			case a : totalForce = totalAttraction(distance); 
					 xyForceDirection = getForceDirection (ForceType.a, pnt1, pnt2);
					 break;
			case cm_oc_a :  totalForce = totalContourMiddleAttraction_PointOnContour(distance); 
			   		    	xyForceDirection = getForceDirection (ForceType.cm_oc_a, pnt1, pnt2);
			   		    	break;
			case cm_noc_a : totalForce = totalContourMiddleAttraction_PointNotOnContour(distance); 
   		    				xyForceDirection = getForceDirection (ForceType.cm_noc_a, pnt1, pnt2);
   		    				break;
			case zm_iz_a :  totalForce = totalZoneMiddleAttraction_PointInZone(distance); 
   							xyForceDirection = getForceDirection (ForceType.zm_iz_a, pnt1, pnt2);
   							break;				            
			case zm_niz_a : totalForce = totalZoneMiddleAttraction_PointNotInZone(distance); 
   		    				xyForceDirection = getForceDirection (ForceType.zm_niz_a, pnt1, pnt2);
   		    				break;		
   		    				
			case r : totalForce = totalRepulsion(distance); 
					 xyForceDirection = getForceDirection (ForceType.r, pnt1, pnt2);
					 break;
			case cm_oc_r :  totalForce = totalContourMiddleRepulsion_PointOnContour(distance); 
   		    		    	xyForceDirection = getForceDirection (ForceType.cm_oc_r, pnt1, pnt2);
   		    		        break;
			case cm_noc_r : totalForce = totalContourMiddleRepulsion_PointNotOnContour(distance); 
   		    				xyForceDirection = getForceDirection (ForceType.cm_noc_r, pnt1, pnt2);
   		    				break;
			case zm_niz_r :  totalForce = totalZoneMiddleRepulsion_PointInZone(distance); 
   							xyForceDirection = getForceDirection (ForceType.zm_iz_r, pnt1, pnt2);
   							break;   
			case zm_iz_r : totalForce = totalZoneMiddleRepulsion_PointNotInZone (distance);
							boolean found = false;
							ArrayList<ArrayList<Polygon>> orig_zspolys = (ArrayList<ArrayList<Polygon>>) ozspolys.clone();
							//if (orig_zspolys.size()>0) orig_zspolys.remove(0);
							for (ArrayList<Polygon> orig_zpolys : orig_zspolys){
								for (Polygon orig_zpoly : orig_zpolys){
									if (orig_zpoly.contains(pnt1)){
										Point p = HybridGraph.findMiddlePointInsidePolygon(orig_zpoly);
										xyForceDirection = getForceDirection (ForceType.zm_niz_r, pnt1, new Point2D.Double(new Double(p.x), new Double(p.y)));
										found = true;
										break;
									}
								}
								if (found) break;
							}
							if (found = false){
								xyForceDirection = getForceDirection (ForceType.zm_niz_r, pnt1, pnt2);
							}
							break;   		    		    
		}
		
		xyForce.x += xyForceDirection.x * (xyDistance.x/(xyDistance.x+xyDistance.y)) * totalForce;
		xyForce.y += xyForceDirection.y * (xyDistance.y/(xyDistance.x+xyDistance.y)) * totalForce;
		
		return xyForce;
	}
	
	
	/// Return force direction that -1 (for -ve -> move left/up) or +1 (for +ve -> move right/down)
	protected Point2D.Double getForceDirection (ForceType ft, Point2D.Double p1, Point2D.Double p2){
		Point2D.Double xyForceDirection = new Point2D.Double (1,1);
		
		Point2D.Double pnt1 = new Point2D.Double();
		Point2D.Double pnt2 = new Point2D.Double();
		
		switch (ft){
			case a:
			case cm_oc_a:
			case cm_noc_a:
			case zm_iz_a:
			case zm_niz_a:
			case zm_iz_r:   pnt1 = p2; 
					  	    pnt2 = p1;
					        break;
			case r:  
			case cm_oc_r:
			case cm_noc_r:
			case zm_niz_r:  pnt1 = p1; 
			  		        pnt2 = p2;
			  		        break;
			//case zm_niz_r:
		}
			
		if ((pnt1.x-pnt2.x) < 0) 
			xyForceDirection.x = -1;
	
		if ((pnt1.y-pnt2.y) < 0)
			xyForceDirection.y = -1;
		
		return xyForceDirection;
	}
	
	
	/// Update the position of the nodes after the forces are considered 
	protected Point2D.Double getNewPntPos (Point2D.Double xyForce, Point2D.Double pnt){
		
		Point2D.Double newPos = (Point2D.Double) xyForce.clone();
		
		newPos.x = Math.rint(m * newPos.x) + pnt.x; 
		newPos.y = Math.rint(m * newPos.y) + pnt.y;
		
		return (newPos);
	}
	

	
	
	/// Calculate & return the absolute distance between the nodes on the x and y axis
	protected Point2D.Double getxyAbsDistances (Point2D.Double pnt1, Point2D.Double pnt2){
		Point2D.Double xyAbsDistances = new Point2D.Double();
		
		xyAbsDistances.x = Math.abs(pnt1.x-pnt2.x);
		xyAbsDistances.y = Math.abs(pnt1.y-pnt2.y);
		
		return xyAbsDistances;
	}

	/// Calculate & return the absolute distance between the nodes 
	///     considering it as a hypotenuse of a triangle
	protected Double getAbsHypotenuse (Point2D.Double xyDistance){
		return (Math.sqrt(Math.pow(xyDistance.x, 2)+ Math.pow(xyDistance.y, 2)));
	}
	
}
