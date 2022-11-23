package euler.piercing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import euler.AbstractDiagram;
import euler.ConcreteContour;
import euler.DualGraph;
import euler.construction.ConstructedConcreteDiagram;
import euler.polygon.RegularPolygon;


public class PiercingDiagram {
	
	protected String abstractDescription = null;	
	protected ArrayList<SinglePiercingCurve> singleCurves = new ArrayList<SinglePiercingCurve>();
	protected ArrayList<PiercingCurve> curves = new ArrayList<PiercingCurve>(); 
	public final String LABELFONTNAME = "Arial";
	public final int LABELFONTSTYLE = Font.BOLD;
	public final int LABELFONTSIZE = 18;
	public static final Color SELECTEDPANELAREACOLOR = Color.gray;
    public static final BasicStroke SELECTEDPANELAREASTROKE = new BasicStroke(1.0f);
    public static final Color PANELBACKGROUNDCOLOR = Color.white;
	protected Color panelBackgroundColor = PANELBACKGROUNDCOLOR;
	protected Color selectedPanelAreaColor = SELECTEDPANELAREACOLOR;
	protected BasicStroke selectedPanelAreaStroke = SELECTEDPANELAREASTROKE;
	protected DualPiercingCurve nextDualCurve = null;
	public static Color c1 = new Color(200,100,100); 
	public static Color c2 = new Color(0,200,100);
	public static Color c3 = new Color(100,0,200); 
	public static Color c4 = new Color(200,100,0); 
	public static Color contourColors[] = {Color.RED,Color.GREEN,Color.BLUE,
			Color.ORANGE,Color.MAGENTA,Color.CYAN,Color.PINK,Color.LIGHT_GRAY,
			Color.MAGENTA,c1,c2,c3,c4};
	protected int unitSize =50;
	protected ArrayList<PiercingCurve> curvesAdded = null;
	protected ConstructedConcreteDiagram concreteDiagram =null;
	protected ArrayList<PiercingCurve> piercingCurves = null;
	protected boolean isPiercingDiagram = false;
	protected AbstractDiagram newDiagram;


	public static void main(String[] args) {
		/*AbstractDiagram ad = new AbstractDiagram("a b ab c ac d ad e ae f af g ag h ah i ai j aj k ak l al m am n an o ao p ap q aq r ar s as t at u au v av w aw x ax y ay z az"); 	 	
	  	ad = new AbstractDiagram("a b ab c bc d cd e de f ef g ge h eh i hi k ki"); 
	  	ad = new AbstractDiagram("a b c ab ac bc abc d ad bd abd e ae ce ace k ak l bl m am");
	  	ad = new AbstractDiagram("a b ab");*/
	  	PiercingDiagram pd = new PiercingDiagram("a b c ab ac bc abc d ad bd abd e ae ce ace k ak l bl m am");
		//pd = new PiercingDiagram("a b ab c ac d ad e ae f af g ag h ah i ai j aj k ak l al m am n an o ao p ap q aq r ar s as t at u au v av w aw x ax y ay z az");
	  	//pd = new PiercingDiagram("a b ab c bc d cd e de f ef g ge h eh i ci"); 
	  	PiercingDiagramWindow pdw = new PiercingDiagramWindow(pd);
	   	pdw.setVisible(true);
	}	
	public PiercingDiagram(String abstractDescription){
		this.abstractDescription = abstractDescription;
		isPiercingDiagram = IsPiercingDiagram();	
	}
	public String getAbstractDescription(){return abstractDescription;}
	public boolean getIsPiercingDiagram(){return isPiercingDiagram;}
	public static AbstractDiagram removeCurve(String s, AbstractDiagram ad){
		ArrayList<String> temp = new ArrayList<String>();
		for(String s1: ad.getZoneList()){
			if(!s1.contains(s)){
				temp.add(s1);
			}
		}
		return new AbstractDiagram(temp);		
	}		
	public static AbstractDiagram removeCurve(ArrayList<String> zoneList, AbstractDiagram ad){
		ArrayList<String> temp = new ArrayList<String>();
		for(String s : ad.getZoneList()){
			if(!zoneList.contains(s))
				temp.add(s);
		}
		return new AbstractDiagram(temp);		
	}		
	public AbstractDiagram removePiercing(AbstractDiagram ad,  AbstractDiagram original,int idx){
		
		String outer = "";	
		ArrayList<String> zones;

		//1. check if there is any single piercing curve		
		for(String curveLabel : ad.getContours()){
			zones = new ArrayList<String>();
			for(String s1: ad.getZoneList()){				
				if(s1.contains(curveLabel)){
					zones.add(s1);
				}
			}
			if(zones.size()==2){
				String s2 = zones.get(0);
				String s3 = zones.get(1);
				String diff = DualGraph.findLabelDifferences(s2,s3);
				if(diff.length() == 1){	
					AbstractDiagram temp = removeCurve(curveLabel,ad);
					String s4 = curveLabel+diff;
					String outerCurves;
					if(s2.length()>s3.length()){
						outerCurves = DualGraph.findLabelDifferences(s2,s4);
					}
					else{
						outerCurves = DualGraph.findLabelDifferences(s3,s4);
					}
					SinglePiercingCurve curve = new SinglePiercingCurve(curveLabel, diff, outerCurves);
					curve.setIsDualPiercing(false);
					curves.add(curve);
					singleCurves.add(curve);	
					return temp;					
				}
			}
			//2. check if there is any dual piercing curve
			if(zones.size() == 4){	
				//System.out.println(curveLabel +" "+ zones.size());
				ArrayList<String> cc = AbstractDiagram.findContoursFromZones(zones);				 			
				 for(String c :cc){					 
					 if(allContainsLabel(zones,c) && c.compareTo(curveLabel)!=0&&!outer.contains(c)){
						 outer+=c;
					 }				 
				 }
				ArrayList<String> newZones = new ArrayList<String>();
				for(String z: zones){						
					String z0 = removeString(z,outer);
					newZones.add(removeString(z0,curveLabel));				
				}
				
				if(AbstractDiagram.findContoursFromZones(newZones).size()==2){
					ArrayList<String> zoneList = original.getZoneList();
					zoneList.add("");
					if(zoneList.containsAll(newZones)){	
						ArrayList<String> cs = AbstractDiagram.findContoursFromZones(newZones);				 			
						nextDualCurve = new DualPiercingCurve(curveLabel,cs.get(0), cs.get(1),outer);
						nextDualCurve.setIsDualPiercing(true);
			 			curves.add(nextDualCurve);			 			
			 			return removeCurve(zones,ad);
				 	}
				}
			}				
		}
		return null;
	}
	
	public boolean IsPiercingDiagram(){	
		
		newDiagram = new AbstractDiagram(abstractDescription);
		curves = new ArrayList<PiercingCurve>();
		if(newDiagram.getContours().size() ==0){				
			return true;
		}
		if(newDiagram.getContours().size() ==1){
			PiercingCurve c0 = new SinglePiercingCurve(abstractDescription,"","");
			curves.add(c0);		
			return true;
		}
		curves = new ArrayList<PiercingCurve>();
		boolean stop = false;
		int idx = 0;
		while(!stop){
			AbstractDiagram temp;
			if(newDiagram.getContours().size()==1){
				//stop removing curves
				stop = true;	 
				//add the last curve				
				SinglePiercingCurve c0 = singleCurves.get(singleCurves.size()-1);
				SinglePiercingCurve lastCurve = new SinglePiercingCurve(c0.getPiercedTo(), c0.getlabel(),c0.getOuterCurves());			
				singleCurves.add(lastCurve);
				lastCurve.setIsDualPiercing(false);
				curves.add(lastCurve);
				for(PiercingCurve cur0 : curves){
					String outerCurves = cur0.getOuterCurves();
				 	for(PiercingCurve cur: curves){
						if(outerCurves.length() !=0){
							if(outerCurves.contains(cur.getlabel())){
								cur.addInsideCurve(cur0.getlabel());
							}
						}
					}
				}
				
				for(SinglePiercingCurve cur1 : singleCurves){
					String piercing = cur1.getPiercedTo();
					cur1.addPiercingCurve(piercing);
					for(SinglePiercingCurve cur : singleCurves){
						if(cur.getlabel().compareTo(piercing)==0){
							cur.addPiercingCurve(cur1.getlabel());						
						}
					}
					cur1.setNoOfUnits(cur1.getInsideCurves().length()+cur1.getPiercingCurves().length());
				}				
				return true;
			}
			else{
				temp = this.removePiercing(newDiagram,new AbstractDiagram(abstractDescription),idx);
				if(temp != null){
					newDiagram = temp;		
				}
				else{
					stop = true;
				}
			}		
			idx++;
		}		
		return false;
	}

	public ArrayList<PiercingCurve> getPiercingCurves(){
		if(!isPiercingDiagram){
			if(curves.size()!=0){
				PiercingCurve pc = curves.get(curves.size()-1);
				if(pc.isDualPiercing){
					DualPiercingCurve c = (DualPiercingCurve)pc;
					String d1 = c.getDual1();
					String d2 = c.getDual2();
					if(getCurve(d1)==null ){
						SinglePiercingCurve lastCurve = new SinglePiercingCurve(d1, d2,c.getOuterCurves());			
						singleCurves.add(lastCurve);
						lastCurve.setIsDualPiercing(false);
						curves.add(lastCurve);					
					}
					else if(getCurve(d2)==null){
						SinglePiercingCurve lastCurve = new SinglePiercingCurve(d2, d1,c.getOuterCurves());			
						singleCurves.add(lastCurve);
						lastCurve.setIsDualPiercing(false);
						curves.add(lastCurve);	
					}
					else{
						System.out.println("error!!");
					}

				}
				else{
					SinglePiercingCurve c = (SinglePiercingCurve)pc;
					SinglePiercingCurve lastCurve = new SinglePiercingCurve(c.getPiercedTo(), c.getlabel(),c.getOuterCurves());			
					singleCurves.add(lastCurve);
					lastCurve.setIsDualPiercing(false);
					curves.add(lastCurve);
				}
			}
		}
		return curves;
	}
	public String removeChar(String s, Character c){
		String ret ="";		
		for(int i = 0; i< s.length(); i++){
			Character c0 = s.charAt(i);
			if(c0!=c)
				ret+=c0;
		}
		return ret;
	}
	
	public String removeString(String s, String toRemove){
		String ret = "";
		for(int i = 0; i< s.length(); i++){
			Character c0 = s.charAt(i);
			if(!toRemove.contains(c0.toString()))
				ret+=c0;
		}
		return ret;
	}	
	public boolean allContainsLabel(ArrayList<String> labels, String label){		
		for(String s : labels){
			if(!s.contains(label))
				return false;
		}
		return true;		
	}
	public PiercingCurve getCurve(String label){
		for(PiercingCurve c: curves){
			if(c.getlabel().compareTo(label)==0){
				return c;
			}
		}
		return null;
	}
	public ConstructedConcreteDiagram generateConstructedConcreteDiagram(){
		ArrayList<ConcreteContour> ccs = new ArrayList<ConcreteContour>();
		if(curvesAdded!=null){
			for(PiercingCurve pc: curvesAdded){
				Polygon pol = RegularPolygon.generateRegularPolygon((int)pc.getCentreX(),(int)pc.getCentreY(),(int)pc.getRadius(),50);
				ccs.add(new ConcreteContour(pc.getlabel(),pol));
			}
			return new ConstructedConcreteDiagram(abstractDescription,ccs);
		}
		return null;
	}
	
	
	public void addCurves(){
		if(curves.size()<5)
			unitSize = 80;
		else{
			unitSize = 500/ curves.size();
		}
		curvesAdded = new ArrayList<PiercingCurve>();
		if(curves.size() != 0){
			if(curves.size()==1){
				PiercingCurve c = curves.get(0);
				SinglePiercingCurve c0 = (SinglePiercingCurve)c;
				c0.setCentre(200, 200);
				c0.setRadius(100);
				curvesAdded.add(c0);
			}
			else{
				curvesAdded = new ArrayList<PiercingCurve>();
				PiercingCurve c = curves.get(curves.size()-1);
				SinglePiercingCurve startCurve = (SinglePiercingCurve)c;	
				int radius = startCurve.getNumberOfUnits()*unitSize;
				startCurve.setRadius(radius);
				startCurve.setCentre(radius+unitSize+100, radius+unitSize);		
				startCurve.setUnitDegree(Math.PI*2/(startCurve.getNumberOfUnits()));	
				curvesAdded.add(startCurve);
				for(int i = curves.size()-2; i>=0; i--){
					PiercingCurve pc = curves.get(i);
					addCurve(pc);
				}
			}
		}
	}
	public boolean addCurve(PiercingCurve c){
		
		String outer = c.getOuterCurves();
		if(outer.compareTo("")!=0){
			for(int i = 0; i< outer.length(); i++){
				Character o0 = outer.charAt(i);
				String o = o0.toString();
				PiercingCurve out = getCurve(o);
				if(out!=null )
					out.addInsideCurve(c.getlabel());
			}			
			return addInsideCurve(c);
		}
		else{
			if(c.isDualPiercing){		
				DualPiercingCurve curveToAdd = (DualPiercingCurve)c;
				return addDualPiercingCurve(curveToAdd);
			}
			else {
				SinglePiercingCurve curveToAdd = (SinglePiercingCurve)c;
				return addSinglePiercingCurve(curveToAdd);	
			}
		}
	}
	
	public ArrayList<Pie> generateAvaliableSections(SinglePiercingCurve curveToAdd, ArrayList<Pie> occupiedSections){
		
		ArrayList<Pie> avaliableSections = new ArrayList<Pie>();
		
		if(occupiedSections.size()==0){
			avaliableSections.add(new Pie(0,Math.PI));
			avaliableSections.add(new Pie(Math.PI,Math.PI*2));
		}
		else if(occupiedSections.size() == 1){
			Pie pie0 = occupiedSections.get(0);
			if(pie0.getEndDegree()>pie0.getStartDegree()){
				avaliableSections.add(new Pie ( 0, pie0.getStartDegree()));
				avaliableSections.add(new Pie( pie0.getEndDegree(),Math.PI*2));
			}
			else{
				avaliableSections.add(new Pie (pie0.getEndDegree(),Math.PI));
				avaliableSections.add(new Pie( Math.PI,pie0.getStartDegree()));
			}
		}
		else{
			Pie first = occupiedSections.get(0), last = occupiedSections.get(0);
			for(Pie p : occupiedSections){
				if(p.getStartDegree() < first.getStartDegree())
					first = p;
				if(p.getEndDegree() < last.getStartDegree())
					last = p;
			}
			for(Pie p0: occupiedSections){
				Pie next = occupiedSections.get(occupiedSections.size()-1); 
				double dis = Math.PI*2;
				if(p0!=last){
					for(Pie p1 : occupiedSections){
						if(p1.getStartDegree()> p0.getStartDegree() && p1.getStartDegree() - p0.getStartDegree()<dis){
							next = p1;
							dis = p1.getStartDegree()-p0.getStartDegree();
							}
					}
					avaliableSections.add(new Pie(p0.getEndDegree(), next.getStartDegree()));
				}
			}
		}
			return avaliableSections;		
	}

	
	public boolean addSinglePiercingCurve(SinglePiercingCurve curveToAdd){
		
		String s = curveToAdd.getPiercedTo();
		PiercingCurve piercedTo = getCurve(s);
		if(!piercedTo.isDual()){
			SinglePiercingCurve pc = (SinglePiercingCurve)piercedTo;
			if(pc.getPiercedTo().compareTo("")==0){
				pc.setPiercedTo(curveToAdd.getlabel());
				piercedTo = pc;
				piercedTo.setNoOfUnits(piercedTo.getNumberOfUnits()+1);
				System.out.println("added");
			}
		}
		double unitDegree = Math.PI/(piercedTo.getNumberOfUnits()+1);
		double r = 0;
		double k = (double)(curveToAdd.getNumberOfUnits()+1)/(double)(piercedTo.getNumberOfUnits()+1);
		r = k*piercedTo.getRadius();
		curveToAdd.setRadius(r);	
		
		double x = 0, y = 0;
		double degree = 0;
		ArrayList<Pie> occupiedSections = new ArrayList<Pie>();
		
		for(PiercingCurve c: curvesAdded){
			if(c.getlabel().compareTo(s)!=0 && c.getOuterCurves()==""){	
				Pie pie = getInterSectionPie(c,piercedTo);
				if(pie!=null){
				addSection(occupiedSections,pie);
				}
			}
		}
		ArrayList<Pie> avaliableSections = generateAvaliableSections(curveToAdd,occupiedSections);	
		Pie p0 = getMostSuitableSection(avaliableSections, unitDegree);
		if(p0.getSize()>unitDegree ){				
			degree = (p0.getStartDegree() +p0.getSize()/2);			
		}
		else{
			double d = p0.getSize()/2;
			double newRadius = Math.abs(curveToAdd.getRadius()*Math.cos(d)/2);
			curveToAdd.setRadius(newRadius);
			degree = (p0.getStartDegree() + d);
		}		
		if(piercedTo.getRadius()/4>= curveToAdd.radius){
			 x = piercedTo.getCentreX() + piercedTo.getRadius()*Math.cos(degree);
			 y = piercedTo.getCentreY() + piercedTo.getRadius()*Math.sin(degree);
		}
		else{
			x = piercedTo.getCentreX() + (piercedTo.getRadius()/4*3+curveToAdd.radius)*Math.cos(degree);
			y = piercedTo.getCentreY() + (piercedTo.getRadius()/4*3+curveToAdd.radius)*Math.sin(degree);
		}
		curveToAdd.setCentre((int)x, (int)y);
		
		adjustSinglePiercingCurve(curveToAdd, piercedTo,p0);
		
		if(!validSinglePiercingCurve(curveToAdd,piercedTo, curvesAdded))
				return false;
		
		curvesAdded.add(curveToAdd);		
		return true;		
			
	}	
	
	public Pie getInterSectionPie(PiercingCurve c, PiercingCurve piercedTo){
		
		double is[][] = PiercingCurve.intersect(c, piercedTo);
		if(is!=null){
			double x1 = piercedTo.getCentreX();
			double y1 = piercedTo.getCentreY();
			double a1 =  PiercingCurve.angle(x1,y1,is[0][0],is[0][1]);
			double a2 =  PiercingCurve.angle(x1,y1,is[1][0],is[1][1]);
			Pie pie;
			if(a1<a2){
				if(a2-a1<Math.PI){
					pie = new Pie(a1,a2);
							}
				else{
					pie = new Pie(a2,a1);
				}
			}
			else{
				if(a1-a2<Math.PI){
					pie = new Pie(a2,a1);
							}
				else{
					pie = new Pie(a1,a2);
				}
			}
			return pie;			
		}
		return null;
	}
	public boolean addInsideCurve(PiercingCurve c){
		
		String s = c.getOuterCurves();
		
		if(s!=""){
			Character o = s.charAt(s.length()-1);
			String out = o.toString();
			PiercingCurve outerCurve = getCurve(out);
			c.setRadius(outerCurve.getRadius()*0.8);
			
			if(outerCurve!=null){
				if(c.isDualPiercing){
					DualPiercingCurve curveToAdd = (DualPiercingCurve)c;
					String d1 = curveToAdd.getDual1();
					String d2 = curveToAdd.getDual2();
					PiercingCurve p1 = getCurve(d1);
					PiercingCurve p2 = getCurve(d2);
					double is[][] = PiercingCurve.intersect(p1, p2);					
					
					if(p1!= null && p2!=null && is!=null){						
						Polygon pol = RegularPolygon.generateRegularPolygon(outerCurve.getCentreX(), outerCurve.getCentreY(), (int)outerCurve.getRadius(), 50);
							if(pol.contains(is[0][0], is[0][1])){
								curveToAdd.setCentre((int)is[0][0], (int)is[0][1]);						
							}
							else if(pol.contains(is[1][0], is[1][1])){
								curveToAdd.setCentre((int)is[1][0], (int)is[1][1]);						
							}
							else{							
								System.out.println("error, no cross point inside outer curve");
								return false;
							}
							adjustInnerDualCurve(curveToAdd,outerCurve);
							if(!validDualPiercingCurve(curveToAdd))
								return false;					
							curvesAdded.add(curveToAdd);		
								return true;
					}
					else{
							System.out.println(" dual1 or dual2 does not exist!");
						}
					}
				else{
					SinglePiercingCurve curveToAdd = (SinglePiercingCurve)c;					
					PiercingCurve piercedTo = getCurve(curveToAdd.getPiercedTo());
					
					if(piercedTo!=null){
						double is[][] = PiercingCurve.intersect(piercedTo,outerCurve);
						if(is!=null){
							double x = (is[0][0]+is[1][0])/2;
							double y = (is[0][1]+is[1][1])/2;
							Line2D.Double line = new Line2D.Double(x,y,outerCurve.getCentreX(),outerCurve.getCentreY());
							Polygon pol = RegularPolygon.generateRegularPolygon(piercedTo.getCentreX(),
							piercedTo.getCentreY(), (int)piercedTo.getRadius(),50);
							Point2D.Double point = pjr.graph.Util.intersectionPointOfPolygonAndLine(pol, line);
							double dis = pjr.graph.Util.distance(x, y, point.getX(), point.getY());
							curveToAdd.setCentre((int)point.getX(), (int)point.getY());
							curveToAdd.setRadius(dis);
							adjustInnerSinglePiercingCurve(curveToAdd, piercedTo, outerCurve);
							if(!validSinglePiercingCurve(curveToAdd, piercedTo, curvesAdded))
								return false;					
							curvesAdded.add(curveToAdd);		
							return true;
						}
						
						
						
						
						
						
				/*		ArrayList<Pie> occupiedSections = new ArrayList<Pie>();
						for(PiercingCurve pc :curvesAdded){
							if(!pc.isDualPiercing){
								if(pc.getOuterCurves().contains(out)){
									Pie pie = this.getInterSectionPie(pc, piercedTo);
									if(pie!=null)
										addSection(occupiedSections,pie);
								}
							}						
						}						
						if(occupiedSections.size()==0){
							curveToAdd.setCentre(outerCurve.getCentreX(), outerCurve.getCentreY());
							adjustInnerSinglePiercingCurve(curveToAdd, piercedTo, outerCurve);
							if(!validSinglePiercingCurve(curveToAdd, piercedTo, curvesAdded))
								return false;					
							curvesAdded.add(curveToAdd);		
							return true;
						}
						else{
							
						}	*/		
					}
				}				
			}
		}
		return false;
	}

	public ArrayList<Pie> addSection(ArrayList<Pie> sections, Pie pie){
		boolean inside = false;
		for(Pie s: sections){
			if(s.contains(pie.getStartDegree())){
				s.setEndDegree(pie.getEndDegree());
				inside = true;
			}
			if(s.contains(pie.getEndDegree())){
				s.setStartDegree(pie.getStartDegree());
				inside = true;
			}	
		}
		if(!inside){
			sections.add(pie);
		}
		return sections;
	}
	public boolean adjustSinglePiercingCurve(PiercingCurve curveToAdd, PiercingCurve piercedTo, Pie section){
	
		double radius = curveToAdd.getRadius();
		double degree = section.getSize()/20;		
		boolean valid = false;
		for(int i = 0 ; i < 20; i++){
			curveToAdd.setRadius(radius - radius/20*i);
			valid = validSinglePiercingCurve(curveToAdd,piercedTo, curvesAdded);
			if(valid)
				return true;
			for(int j = 0; j < 20; j++){
				moveCurveAroundCircle(curveToAdd, piercedTo,degree*j);
				valid = validSinglePiercingCurve(curveToAdd,piercedTo, curvesAdded);
				if(valid)
					return true;
			}
		} 
		System.out.println("degree = " + degree +" radius = " + radius );
		System.out.println( "section " + section.getStartDegree() 
				+" , " + section.getEndDegree() +" no valid curve");
		return false;		
	}
	
	public void moveCurveAroundCircle(PiercingCurve curveToAdd, PiercingCurve piercedTo, double degree){
		
		double x = 0, y = 0 ;
		if(piercedTo.getRadius()/4>= curveToAdd.radius){
			 x = piercedTo.getCentreX() + piercedTo.getRadius()*Math.cos(degree);
			 y = piercedTo.getCentreY() + piercedTo.getRadius()*Math.sin(degree);
		}
		else{
			x = piercedTo.getCentreX() + (piercedTo.getRadius()/4*3+curveToAdd.radius)*Math.cos(degree);
			y = piercedTo.getCentreY() + (piercedTo.getRadius()/4*3+curveToAdd.radius)*Math.sin(degree);
		}
		curveToAdd.setCentre((int)x, (int)y);
	}
	 
	public boolean validSinglePiercingCurve(PiercingCurve curveToAdd, PiercingCurve piercedTo, ArrayList<PiercingCurve> curves){
		int numOfInter = 0;
		Polygon pol1 = RegularPolygon.generateRegularPolygon(curveToAdd.getCentreX(),curveToAdd.getCentreY(),(int)curveToAdd.getRadius(),50);
		for(PiercingCurve c0: curves){
			Polygon pol2 = RegularPolygon.generateRegularPolygon(c0.getCentreX(),c0.getCentreY(),(int)c0.getRadius(),50);
			if(PiercingCurve.intersect(curveToAdd,c0)!=null)
				if(c0.getlabel().compareTo(piercedTo.getlabel())!=0)
					return false;
				else
					numOfInter ++;
			if(insidePolygon(pol1, pol2)&& !curveToAdd.getOuterCurves().contains(c0.getlabel()))
				return false;	
			if(insidePolygon(pol2,pol1)&& !c0.getOuterCurves().contains(curveToAdd.getlabel()))
				return false;
		}
		if(numOfInter!=1)	
			return false;	
		else
			return true;		
	}
	public Pie getMostSuitableSection(ArrayList<Pie> sections, double size){
		Pie ret = null;
		double div = Math.PI*2;
		if(sections.size()!=0){
			for(Pie p :sections){
				if(Math.abs(size - p.getSize())<div){
					div = Math.abs(size-p.getSize());
					ret = p;
				}
			}		
		}	
		else{
			System.out.println("no avaliabel sections");
		}
		ret = new Pie(ret.getStartDegree()*1.02, ret.getEndDegree()*0.98);	
		return ret;
	}

	public boolean containsSection(ArrayList<Pie> sections, Pie pie){
		for(Pie p : sections){
			if(p.getStartDegree()==pie.getStartDegree() && p.getEndDegree() == pie.getEndDegree())
				return true;
		}
		return false;
	}	
	
	public boolean addDualPiercingCurve(DualPiercingCurve curveToAdd){
	
		String d1 = curveToAdd.getDual1();
		String d2 = curveToAdd.getDual2();
		
		PiercingCurve pc1 = getCurve(d1);
		PiercingCurve pc2 = getCurve(d2);
		
		double is[][] = PiercingCurve.intersect(pc1,pc2);
	
		if(is == null){
			System.out.println("no intersection");
			return false;
		}
		else{
			double dis = pjr.graph.Util.distance(is[0][0], is[0][1], is[1][0], is[1][1])*2/5;
			double radius;
			if(dis!=0){
				radius = dis;
			}
			else{
				radius = pc1.getRadius();
			}
			curveToAdd.setRadius(radius);
			boolean occupied1 = false;
			boolean occupied2 = false;
			for(PiercingCurve pc: curvesAdded){
				Polygon c = RegularPolygon.generateRegularPolygon(pc.getCentreX(),pc.getCentreY(),(int)pc.getRadius(),50);;
				if(curveToAdd.getDual1().compareTo(pc.getlabel())!=0 
						&& curveToAdd.getDual2().compareTo(pc.getlabel())!=0)
				{
					if(c.contains(new Point((int)is[0][0],(int)is[0][1])))
						occupied1 = true;		
					if(c.contains(new Point((int)is[1][0],(int)is[1][1])))
						occupied2 = true;		
				}				
				else if(pc.getCentreX()==is[0][0]&& pc.getCentreY() == is[0][1]				                                  
				      && !pc.getInsideCurves().contains(curveToAdd.getlabel())){
					occupied1 = true;
				}
				else if(pc.getCentreX()==is[1][0]&& pc.getCentreY() == is[1][1]				                                  
				      && !pc.getInsideCurves().contains(curveToAdd.getlabel())){
				  	occupied2 = true;
				                                       				}
				if(curveToAdd.getOuterCurves().contains(pc.getlabel())&&pc.getRadius()<radius)
					radius = pc.getRadius()*4/5;			
			}
			if(occupied1&&occupied2){
				System.out.println("error adding dual piercing... no position avaliable");
				return false;
			}
			if(!occupied1){
				curveToAdd.setCentre((int)is[0][0], (int)is[0][1]);
			}
			else{
				curveToAdd.setCentre((int)is[1][0], (int)is[1][1]);
			}		
			boolean valid = validDualPiercingCurve(curveToAdd);
			while(!valid){
				curveToAdd.setRadius(curveToAdd.getRadius()*0.8);				
				valid = validDualPiercingCurve(curveToAdd);
			}			
			curvesAdded.add(curveToAdd);
			return true;	
		}
	}
	
	public boolean validDualPiercingCurve(DualPiercingCurve curveToAdd){
		int numInter = 0;
		for(PiercingCurve c: curvesAdded){
			double is[][] = PiercingCurve.intersect(curveToAdd, c);
			if(is!=null){
				if(c.getlabel().compareTo(curveToAdd.getDual1())!=0 
						&& c.getlabel().compareTo(curveToAdd.getDual2())!=0){
							return false;
					}
				else{
					numInter++;
				}	
			}	
		}		if(numInter != 2){
			return false;
		}
		return true;	
	}

 	public boolean adjustInnerSinglePiercingCurve(SinglePiercingCurve curveToAdd, PiercingCurve piercedTo, PiercingCurve outer){
 			
 		boolean valid = false;
 		double radius = curveToAdd.getRadius();
 		double dis = radius/40;  		
 		for(int i = 0 ; i < 40; i++){
 			curveToAdd.setRadius(radius - dis*i);
 			valid = validSinglePiercingCurve(curveToAdd, piercedTo,curvesAdded);
 			if(valid){
 				double possibleRadius = curveToAdd.getRadius();
 				if(curveToAdd.getRadius() < outer.radius/2){ 					
 					for(int j = 0 ; j < 5; j++){
 						curveToAdd.setRadius(5*j);
 							if(validSinglePiercingCurve(curveToAdd, piercedTo,curvesAdded))
 								 possibleRadius = 5*j; 					
 					}
 				curveToAdd.setRadius(possibleRadius);					
 				}
 				return true;
 			}
 		}
 		return false;		
 	}
 		
 		
	public boolean adjustInnerDualCurve(DualPiercingCurve curveToAdd, PiercingCurve outer){
		boolean valid = false;
		double radius = outer.getRadius();
		for(int i = 0 ; i < 20; i++){
			curveToAdd.setRadius(radius - radius/20*i);
			valid = this.validDualPiercingCurve(curveToAdd);
			if(valid)
				return true;
		}
		return false;		
	}
	
	public boolean addNewSinglePiercing(SinglePiercingCurve c){		
		String s = c.getPiercedTo();
		PiercingCurve pc = getCurve(s);	
		if(pc!=null){
			if(addCurve(c)){
				abstractDescription+=" " + c.getlabel()+c.getOuterCurves() +" " +s+c.getlabel()+c.getOuterCurves();
				System.out.println(abstractDescription);
				curves.add(c);
				return true;
			}
			System.out.println("curve not added");
		}		
		return false;
		
	}
	public boolean addNewDualPiercing(DualPiercingCurve c){
		
		c.print();		
		String s1 = c.getDual1();
		String s2 = c.getDual2();
		PiercingCurve d1 = getCurve(s1);
		PiercingCurve d2 = getCurve(s2);
		String s = c.getOuterCurves();
		System.out.println("outer " + s);
   
		if(d1!=null&&d2!=null){
			if(addCurve(c)){
				abstractDescription+=" " + c.getlabel()+c.getOuterCurves() 
				+" " +s1+c.getlabel()+c.getOuterCurves()
				+" " + s2+c.getlabel()+c.getOuterCurves()
				+" " + s1+s2+c.getlabel()+c.getOuterCurves();				
				System.out.println(abstractDescription);	
				curves.add(c);
				return true;
			}
		}		
		return false;
	}
	
	public static boolean insidePolygon(Polygon small1, Polygon p2){
		for(int i = 0; i< small1.npoints; i++){
	
			if(!p2.contains(small1.xpoints[i], small1.ypoints[i]))
				return false;
		}
		return true;
	}
  }   


