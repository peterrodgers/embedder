package euler.library;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import euler.AbstractDiagram;
import euler.ConcreteContour;
import euler.construction.ConstructedConcreteDiagram;
import euler.polygon.RegularPolygon;


public class ThreeSetDiagramLibrary extends JFrame{
	
	public final String FILESTARTABSTRACTDESCRIPTION  = "ABSTRACTDESCRIPTION";
	public final String FILESTARTCONTOURS = "CONTOURS";
	public final char FILESEPARATOR = '|';
	protected String abstractDescription = null;
	protected ArrayList<ConcreteContour> contours = new ArrayList<ConcreteContour>();
	protected ArrayList<String> abstractDescriptions = new ArrayList<String>();
	ArrayList<ConstructedConcreteDiagram> concreteDiagrams = new ArrayList<ConstructedConcreteDiagram>();
	
	public static void main(String[] args) {
		ThreeSetDiagramLibrary m = new ThreeSetDiagramLibrary();
		m.setVisible(true);	 
	}
	
	class TestPanel extends JPanel{		
	 
		 protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.red);	
				ThreeSetDiagramLibrary m = new ThreeSetDiagramLibrary();
				m.loadDefaultThreeSetDiagrams();
		//		File file = new File("3SetLib.txt");
		//		m.saveAll(file);
				ConstructedConcreteDiagram cd = m.concreteDiagrams.get(1);
				for(ConcreteContour cc:cd.getConcreteContours()){
					System.out.println(cc.getAbstractContour());
					g2d.draw(cc.getPolygon());
				}
		/*		ArrayList<Curve> curves = m.getDiagram("c ac bc abc");
				if(curves!=null){
					for(Curve c: curves){
						g2d.draw(c.getPolygon());
						g2d.drawString(c.getLabel(),(float)c.getCentreX(), (float)(c.getCentreY()-c.getRadius()-3));
					}
				}		*/	  
		 	}
	 	}
	
	public ThreeSetDiagramLibrary(){		
	
		this.setTitle("Three set library test");
	    this.setSize(800, 800);
		this.getContentPane().add(new TestPanel());
	}		


	public void loadDefaultThreeSetDiagrams(){
		//diagram 0
		concreteDiagrams = new ArrayList<ConstructedConcreteDiagram> ();
		contours = new ArrayList<ConcreteContour>();
		String ad = "";
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
		
		//diagram 1 - "a"
		ad = "a";
		Polygon p1 = RegularPolygon.generateRegularPolygon(400,400,200,30);
		ConcreteContour cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
		
		//diagram 2 - "a b"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b";		
		p1 = RegularPolygon.generateRegularPolygon(200,400,150,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		Polygon p2 = RegularPolygon.generateRegularPolygon(550,400,150,30);
		ConcreteContour cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));		
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

		//diagram 9 - "ab ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac";
		p1 = RegularPolygon.generateRegularPolygon(200,400,200,30);
		cc1 = new ConcreteContour("a",p1);		
		contours.add(cc1);		
		p2 = RegularPolygon.halfPolygons(p1)[0];
		cc2 = new ConcreteContour("b",p2);		
		contours.add(cc2);	
		p3 = RegularPolygon.halfPolygons(p1)[1];
		cc3 = new ConcreteContour("c",p3);		
		contours.add(cc3);	
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));

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
		ConcreteContour cc4 = new ConcreteContour("b",p3);
		contours.add(cc4);
		abstractDescriptions.add(ad);
		concreteDiagrams.add(new ConstructedConcreteDiagram(abstractDescription,contours));
/*
		//diagram 15 - "a b ac bc"
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac bc";		
		c1 = new Curve(200,400,150,"a");
		c2 = new Curve(200,400,50,"c");
		c3 = new Curve(550,400,150,"b");
		c4 = new Curve(550,400,155,"c");
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);	
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 16 - "ab bc ac"
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab bc";		
		c1 = new Curve(150,400,100,"a");
		c2 = new Curve(150,400,100,"c");
		c3 = new Curve(350,400,100,"a");
		c4 = new Curve(350,400,105,"b");
		Curve c5 = new Curve(550,400,105,"b");
		Curve c6 = new Curve(550,400,100,"c");
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 17 - "a ab ac bc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac bc";		
		c1 = new Curve(250,400,200,"a");
		c2 = new Curve(150,400,50,"b");
		c3 = new Curve(350,400,50,"c");
		c4 = new Curve(600,400,100,"b");
		c5 = new Curve(600,400,105,"c");	
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		curves.add(c5);	
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 18 - " a b ab ac bc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac bc";		
		c1 = new Curve(250,400,200,"a");
		c2 = new Curve(550,400,200,"b");
		c3 = new Curve(150,400,50,"c");
		c4 = new Curve(650,400,50,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 19 = " a b c ab bc ac"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab bc ac";		
		c1 = new Curve(200,500,150,"a");
		c2 = new Curve(400,500,150,"b");
		c3 = new Curve(325,275,150,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);	
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 20 - "abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "abc";		
		c1 = new Curve(400,400,200,"a");
		c2 = new Curve(400,400,205,"b");
		c3 = new Curve(400,400,210,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 21 - "a abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a abc";		
		c1 = new Curve(400,400,200,"a");
		c2 = new Curve(400,400,100,"b");
		c3 = new Curve(400,400,105,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 22 - "a b abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b abc";		
		c1 = new Curve(300,400,200,"a");
		c2 = new Curve(300,400,100,"b");
		c3 = new Curve(300,400,105,"c");
		c4 = new Curve(600,400,100,"b");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 23 - " a b c abc"		
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c abc";		
		c1 = new Curve(400,400,145,"a");
		c2 = new Curve(400,400,150,"b");
		c3 = new Curve(400,400,155,"c");
		c4 = new Curve(150,400,100,"a");
		c5 = new Curve(650,400,100,"b");
		c6 = new Curve(400,150,100,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		curves.add(c5);
		curves.add(c6);		
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 24 - "ab abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "ab abc";		
		c1 = new Curve(400,400,200,"a");
		c2 = new Curve(400,400,205,"b");
		c3 = new Curve(400,400,100,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 25 - "a ab abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab abc";		
		c1 = new Curve(400,400,250,"a");
		c2 = new Curve(400,400,150,"b");
		c3 = new Curve(400,400,50,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);	
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 26 - "a b ab abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab abc";		
		c1 = new Curve(250,400,200,"a");
		c2 = new Curve(450,400,200,"b");
		c3 = new Curve(350,400,50,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 27 - "a b ac abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac abc";		
		c1 = new Curve(300,400,200,"a");
		c2 = new Curve(350,400,45,"b");
		c3 = new Curve(350,400,50,"c");
		c4 = new Curve(250,400,50,"c");
		c5 = new Curve(600,200,100,"b");	
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		curves.add(c5);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 28 - "ab ac abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac abc";		
		c1 = new Curve(200,400,155,"a");
		c2 = new Curve(200,400,150,"b");
		c3 = new Curve(450,400,155,"a");
		c4 = new Curve(450,400,150,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 29 - "a b c ab abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab abc";		
		c1 = new Curve(300,500,200,"a");
		c2 = new Curve(500,500,200,"b");
		c3 = new Curve(400,500,50,"c");
		c4 = new Curve(400,200,50,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 30- "a ab ac abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac abc";		
		c1 = new Curve(400,400,250,"a");
		c2 = new Curve(350,400,100,"b");
		c3 = new Curve(450,400,100,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 31 - "a b ab ac abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac abc";		
		c1 = new Curve(300,400,200,"a");
		c2 = new Curve(500,400,200,"b");
		c3 = new Curve(300,400,100,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);	
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 32 - "a b c ab ac abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab ac abc";		
		c1 = new Curve(375,400,200,"a");
		c2 = new Curve(250,400,150,"b");
		c3 = new Curve(500,400,150,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 33 - "a bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a bc abc";		
		c1 = new Curve(300,400,150,"a");
		c2 = new Curve(450,400,150,"b");
		c3 = new Curve(450,400,155,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 34 - "a ab bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ac abc";		
		c1 = new Curve(250,400,200,"a");
		c2 = new Curve(450,400,200,"b");
		c3 = new Curve(450,400,195,"c");
		c4 = new Curve(150,400,50,"b");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 35 - "a b ac bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab bc abc";		
		c1 = new Curve(300,400,200,"a");
		c2 = new Curve(500,400,200,"b");
		c3 = new Curve(400,400,175,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);	
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 36 - "ab ac bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "ab ac bc abc";		
		c1 = new Curve(200,500,155,"a");
		c2 = new Curve(200,500,150,"b");
		c3 = new Curve(350,400,155,"a");
		c4 = new Curve(350,400,150,"b");
		c5 = new Curve(550,200,100,"b");
		c6 = new Curve(550,200,100,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		curves.add(c5);
		curves.add(c6);	
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 37 - "a ab ac bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a ab ac bc abc";		
		c1 = new Curve(300,500,250,"a");
		c2 = new Curve(250,500,100,"b");
		c3 = new Curve(350,500,100,"c");
		c4 = new Curve(600,200,100,"c");
		c5 = new Curve(600,200,105,"b");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		curves.add(c4);
		curves.add(c5);	
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 38 - "a b ab ac bc abc"		
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b ab ac bc abc";		
		c1 = new Curve(300,400,200,"a");
		c2 = new Curve(500,400,200,"b");
		c3 = new Curve(400,400,125,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));

		//diagram 39 - "a b c ab ac bc abc"
		curves = new ArrayList<Curve>();
		contours = new ArrayList<ConcreteContour>();
		ad = "a b c ab ac bc abc";		
		c1 = new Curve(400,500,150,"a");
		c2 = new Curve(300,300,150,"b");
		c3 = new Curve(500,300,150,"c");
		curves.add(c1);
		curves.add(c2);
		curves.add(c3);
		abstractDescriptions.add(ad);
		diagrams.put(ad, curves);
		concreteDiagrams.add(new ConstructedConcreteDiagram(contours));*/
		
	}
	public boolean saveAll(File file){		
		if(concreteDiagrams==null){
			return false;
		}
		ConstructedConcreteDiagram cd0 = concreteDiagrams.get(0);
		if(!cd0.saveToFile(file)){
			return false;
		}
		for(int i = 1; i< concreteDiagrams.size(); i++){
			ConstructedConcreteDiagram cd = concreteDiagrams.get(i);
			if(!cd.appendToFile(file))
				return false;
		}
		
		return true;
	}

	
}
