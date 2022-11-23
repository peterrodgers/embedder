package euler.library;

import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import euler.AbstractDiagram;
import euler.ConcreteContour;
import euler.construction.ConstructedConcreteDiagram;
import euler.polygon.RegularPolygon;


public class Library {

	public final String FILESTARTDIAGRAM = "DIAGRAM";
	public final String FILESTARTABSTRACTDESCRIPTION  = "ABSTRACTDESCRIPTION";
	public final String FILESTARTCONTOURS = "CONTOURS";
	public final char FILESEPARATOR = '|';
	protected String abstractDescription = "";
	protected ArrayList<ConcreteContour> contours = new ArrayList<ConcreteContour>();	
	protected ArrayList<ConstructedConcreteDiagram> constructedConcreteDiagrams = new ArrayList<ConstructedConcreteDiagram>();
	protected ArrayList<String> abstractDescriptions = new ArrayList<String>();
	protected HashMap<String, ConstructedConcreteDiagram> diagrams = new HashMap<String, ConstructedConcreteDiagram>();
	public static void main(String[] args) {
		
		Library l = new Library();
		l.loadDefaultThreeSetDiagrams();	
		l.saveAll(new File("3SetLib"));
		String s = "a b ab";
		ConstructedConcreteDiagram ccd = l.getDiagram(s);
		ArrayList<ConcreteContour> ccs = ccd.getConcreteContours();		
		EulerDiagramWindow cdw = new EulerDiagramWindow(s,ccs);
		cdw.getEulerDiagramPanel().setShowEdgeDirection(false);
		cdw.getEulerDiagramPanel().setShowEdgeLabel(true);
		cdw.getEulerDiagramPanel().setShowGraph(true);
		cdw.getEulerDiagramPanel().setShowContour(true);
		cdw.getEulerDiagramPanel().setShowContourLabel(true);
		cdw.getEulerDiagramPanel().setShowTriangulation(false);
		
	}		
	public Library(){loadDefaultThreeSetDiagrams();}

	public ArrayList<ConstructedConcreteDiagram> getConstructedConcreteDiagrams(){return constructedConcreteDiagrams;}
	
	
	public boolean loadLibrary(File fileName){	
		
		constructedConcreteDiagrams = new ArrayList<ConstructedConcreteDiagram> ();
		diagrams = new HashMap<String, ConstructedConcreteDiagram>();
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));			
			Character c = new Character(FILESEPARATOR);
			String separatorString = new String(c.toString());
			contours = new ArrayList<ConcreteContour>();			 
			boolean readingAbstractDescription = false;
			boolean readingContours = false;
			
			String line = b.readLine();
			
			while(line != null) { 
				if(line.equals("")) {
					line = b.readLine();
					continue;
				}
				if(line.compareTo(FILESTARTDIAGRAM)==0) {
					line = b.readLine();
					if(contours.size()!=0){
						constructedConcreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
						ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(abstractDescription,contours);
						constructedConcreteDiagrams.add(ccd);
						diagrams.put(abstractDescription,ccd);	
						contours = new ArrayList<ConcreteContour>();
						abstractDescription = null;
					
					}
					readingAbstractDescription = true;
					readingContours = false;					
				}
				if(readingAbstractDescription && line.compareTo(FILESTARTABSTRACTDESCRIPTION)!=0 && line.compareTo(FILESTARTCONTOURS)!=0) {
					abstractDescription = line;
			
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
					
					contours.add(cc);
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
			constructedConcreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
			ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(abstractDescription,contours);
			constructedConcreteDiagrams.add(ccd);
			diagrams.put(abstractDescription,ccd);	
			b.close();

		} catch(IOException e){
			System.out.println("An IO exception occured when executing loadAdjacencyFile("+fileName+") in ThreeSetDiagramLibrary.java: "+e+"\n");
			System.exit(1);
		}
		return(true);		
	}
	public ConstructedConcreteDiagram getDiagram(String ad){		
		
		for(ConstructedConcreteDiagram ccd: constructedConcreteDiagrams){
			String s = ccd.getAbstractDescription();
			
			AbstractDiagram d1 = new AbstractDiagram(s);
			AbstractDiagram d2 = new AbstractDiagram(ad);				
							
				if(d1.isomorphicTo(d2)){						
					ConstructedConcreteDiagram newDiagram;					
					newDiagram = diagrams.get(s);
					if(newDiagram != null)
					//convert labels 
					if(s.compareTo(ad)!=0){	
						d2.normalize();
						HashMap<String,String> map = d2.getcontourLabelMap();
						ArrayList<ConcreteContour> tempDiagram = new ArrayList<ConcreteContour>();
						for(String key: map.keySet()){
							String value = map.get(key);
							//System.out.println(key+" " + value);
							for(ConcreteContour c: newDiagram.getConcreteContours()){
								if(c.getAbstractContour().compareTo(value)==0){
									ConcreteContour newC = c.clone();
									newC.setLabel(key);
									tempDiagram.add(newC);
								}								
							}						
						}
						newDiagram = new ConstructedConcreteDiagram(ad,tempDiagram);					
					}
					return newDiagram;
				}	
			}
			System.out.println("diagram not found");
				return null;
	}	
	public ConstructedConcreteDiagram getDiagram(int idx){
		return  constructedConcreteDiagrams.get(idx+1);
	}
	public void loadDefaultThreeSetDiagrams(){
		diagrams = new HashMap<String, ConstructedConcreteDiagram>();
		
		//diagram 0
		constructedConcreteDiagrams = new ArrayList<ConstructedConcreteDiagram> ();
		contours = new ArrayList<ConcreteContour>();
		String ad = "";
		ConstructedConcreteDiagram ccd = new ConstructedConcreteDiagram(ad,contours);
		//constructedConcreteDiagrams.add(ccd);
		//diagrams.put(ad,ccd);	
	
		//diagram 1 - "a"
		ad = "a";		
		Polygon p1 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		ConcreteContour cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		
		//diagram 2 - "a b"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b";		
		p1 = RegularPolygon.generateRegularPolygon(250,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		Polygon p2 = RegularPolygon.generateRegularPolygon(600,400,150,30);
		ConcreteContour cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 3 - "a b c"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c";
		p1 = RegularPolygon.generateRegularPolygon(150,400,100,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		Polygon p3 = RegularPolygon.generateRegularPolygon(650,400,100,30);
		ConcreteContour cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);	
		abstractDescriptions.add(ad);	
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 4 - " ab"		
		contours = new ArrayList<ConcreteContour>();
		ad = "ab";			
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 5 - "a ab"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab";		
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);

		//diagram 6 - "a b ab"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab";	
		p1 = RegularPolygon.generateRegularPolygon(250,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(450,400,150,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		// diagram 7 - " a b ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac";		
		p1 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(550,400,150,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(200,400,50,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 8 - "a b c ab"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab";	
		p1 = RegularPolygon.generateRegularPolygon(200,400,100,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(350,400,100,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(600,400,100,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);	
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 9 - "ab ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac";
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);
		Polygon p11 = RegularPolygon.generateRegularPolygon(400,400,200,30);	
		p2 = RegularPolygon.halfPolygons(p11)[0];
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.halfPolygons(p11)[1];
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);	
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 10 - "a ab ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac";	
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(300,400,50,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(500,400,50,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 11- "a c ab ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac";	
		p1 = RegularPolygon.generateRegularPolygon(250,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(500,400,150,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(200,400,50,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 12 - "a b c ab ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab ac";	
		p1 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(600,400,150,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 13- "a bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a bc";
		p1 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.generateRegularPolygon(550,400,150,30);
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.generateRegularPolygon(550,400,150,30);
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);	
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 14 - "a ab bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab bc";	
		p1 = RegularPolygon.generateRegularPolygon(400,500,200,30);
		cc1 = new ConcreteContour("a",p1);	
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc2 = new ConcreteContour("b",p2);	
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,200,100,30);		
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		Polygon p4 = RegularPolygon.generateRegularPolygon(400,200,100,30);	
		ConcreteContour cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		
		//diagram 15 - "a b ac bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac bc";		
		p1 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p3 = RegularPolygon.generateRegularPolygon(550,400,150,30);
		cc3 = new ConcreteContour("b",p3);
		contours.add(cc3);
		p2 = RegularPolygon.generateRegularPolygon(200,400,50,30);
		cc2 = new ConcreteContour("c",p2);
		contours.add(cc2);	
		p4 = RegularPolygon.generateRegularPolygon(550,400,50,30);
		cc4 = new ConcreteContour("c",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);
		
		//diagram 16 - "ab bc ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab bc ac";		
		p1 = RegularPolygon.generateRegularPolygon(150,400,100,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(150,400,100,30);
		cc2 = new ConcreteContour("c",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,100,30);
		cc3 = new ConcreteContour("a",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(350,400,100,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		Polygon p5 = RegularPolygon.generateRegularPolygon(550,400,100,30);
		ConcreteContour cc5 = new ConcreteContour("b",p5);
		contours.add(cc5);
		Polygon p6 = RegularPolygon.generateRegularPolygon(550,400,100,30);
		ConcreteContour cc6 = new ConcreteContour("c",p6);
		contours.add(cc6);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 17 - "a ab ac bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac bc";
		p1 = RegularPolygon.generateRegularPolygon(250,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(150,400,50,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(600,400,100,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		p5 = RegularPolygon.generateRegularPolygon(600,400,100,30);
		cc5 = new ConcreteContour("c",p5);
		contours.add(cc5);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
		
		//diagram 18 - " a b ab ac bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac bc";	
		p1 = RegularPolygon.generateRegularPolygon(250,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(550,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(150,400,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(650,400,50,30);
		cc4 = new ConcreteContour("c",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
		

	//diagram 19 = " a b c ab bc ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab bc ac";	
		p1 = RegularPolygon.generateRegularPolygon(200,500,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,500,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(325,275,150,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 20 - "abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "abc";		
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 21 - "a abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a abc";		
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);		

		
		//diagram 22 - "a b abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b abc";		
		p1 = RegularPolygon.generateRegularPolygon(300,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(300,400,100,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(300,400,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(600,400,100,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
		

		//diagram 23 - " a b c abc"		
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c abc";
		p1 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(150,400,100,30);
		cc4 = new ConcreteContour("a",p4);
		contours.add(cc4);
		p5 = RegularPolygon.generateRegularPolygon(650,400,100,30);
		cc5 = new ConcreteContour("b",p5);
		contours.add(cc5);
		p6 = RegularPolygon.generateRegularPolygon(400,150,100,30);
		cc6 = new ConcreteContour("c",p6);
		contours.add(cc6);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 24 - "ab abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab abc";		
		p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);		
	
		//diagram 25 - "a ab abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab abc";
		p1 = RegularPolygon.generateRegularPolygon(400,400,250,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(400,400,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
	
		//diagram 26 - "a b ab abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab abc";	
		p1 = RegularPolygon.generateRegularPolygon(250,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(450,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
	
		//diagram 27 - "a b ac abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac abc";	
		p1 = RegularPolygon.generateRegularPolygon(300,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(350,400,50,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(250,400,50,30);
		cc4 = new ConcreteContour("c",p4);
		contours.add(cc4);
		p5 = RegularPolygon.generateRegularPolygon(600,200,100,30);
		cc5 = new ConcreteContour("b",p5);
		contours.add(cc5);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
		
		//diagram 28 - "ab ac abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac abc";	
		p1 = RegularPolygon.generateRegularPolygon(200,400,155,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(450,400,155,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(450,400,150,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 29 - "a b c ab abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab abc";	
		p1 = RegularPolygon.generateRegularPolygon(300,500,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(500,500,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,500,50,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(400,200,50,30);
		cc4 = new ConcreteContour("c",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
	
		//diagram 30- "a ab ac abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac abc";		
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac abc";		
		p1 = RegularPolygon.generateRegularPolygon(400,400,250,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(350,400,100,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(450,400,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		
		//diagram 31 - "a ab ac abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac abc";	
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(300,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(500,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(300,400,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 32 - "a b c ab ac abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab ac abc";	
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(375,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(250,400,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(500,400,150,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
	
		//diagram 33 - "a bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a bc abc";	
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(300,400,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(450,400,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(450,400,150,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
	
		//diagram 34 - "a ab bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab bc abc";	
		p1 = RegularPolygon.generateRegularPolygon(250,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(450,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(450,400,200,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(150,400,50,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 35 - "a b ac bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac bc abc";	
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(250,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(450,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,175,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 36 - "ab ac bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac bc abc";	
		p1 = RegularPolygon.generateRegularPolygon(200,500,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(200,500,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,400,150,30);
		cc3 = new ConcreteContour("a",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(350,400,150,30);
		cc4 = new ConcreteContour("b",p4);
		contours.add(cc4);
		p5 = RegularPolygon.generateRegularPolygon(550,200,100,30);
		cc5 = new ConcreteContour("b",p5);
		contours.add(cc5);
		p6 = RegularPolygon.generateRegularPolygon(550,200,100,30);
		cc6 = new ConcreteContour("c",p6);
		contours.add(cc6);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);				
	
		//diagram 37 - "a ab ac bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac bc abc";	
		p1 = RegularPolygon.generateRegularPolygon(300,500,250,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(250,500,100,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(350,500,100,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		p4 = RegularPolygon.generateRegularPolygon(600,200,100,30);
		cc4 = new ConcreteContour("c",p4);
		contours.add(cc4);
		p5 = RegularPolygon.generateRegularPolygon(600,200,100,30);
		cc5 = new ConcreteContour("b",p5);
		contours.add(cc5);		
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 38 - "a b ab ac bc abc"		
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac bc abc";	
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(300,400,200,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(500,400,200,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(400,400,125,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	

		//diagram 39 - "a b c ab ac bc abc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab ac bc abc";		
		contours = new ArrayList<ConcreteContour>();
		p1 = RegularPolygon.generateRegularPolygon(400,500,150,30);
		cc1 = new ConcreteContour("a",p1);
		contours.add(cc1);
		p2 = RegularPolygon.generateRegularPolygon(300,300,150,30);
		cc2 = new ConcreteContour("b",p2);
		contours.add(cc2);
		p3 = RegularPolygon.generateRegularPolygon(500,300,150,30);
		cc3 = new ConcreteContour("c",p3);
		contours.add(cc3);
		abstractDescriptions.add(ad);
		ccd = new ConstructedConcreteDiagram(ad,contours);
		constructedConcreteDiagrams.add(ccd);
		diagrams.put(ad,ccd);	
		
	}
		public boolean saveAll(File file){			
			file.delete();
			if(constructedConcreteDiagrams==null){
				return false;
			}
			for(ConstructedConcreteDiagram cd : constructedConcreteDiagrams){
				if(!cd.appendToFile(file))
					return false;
			}		
		return true;
		}	

		public void print(){
			System.out.println("library---------------");
			for(String s : abstractDescriptions){
				System.out.println(s);
			}
			System.out.println("---------------------");
		}
}
