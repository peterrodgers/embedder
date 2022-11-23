package euler.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TestFrame extends JFrame {

    class SomePoly extends JPanel{
        protected void paintComponent(Graphics g) {
           
        	Graphics2D g2d = (Graphics2D) g;
            
            Rectangle rect1 = new Rectangle(0, 0,120, 120);
            //g2d.draw(rect1);

            //Rectangle rect2 = new Rectangle(300, 200, 200, 300);
             //g2d.draw(rect2);
            
            //Rectangle rect3 = new Rectangle(500, 500, 200, 200);
            // g2d.draw(rect3);
            
           // Rectangle rect4 = new Rectangle(600, 0, 300, 200);
            // g2d.draw(rect4);
            
            g2d.setColor(Color.green);
            
          //Polygon pol1 = RegularPolygon.generateRegularPolygonInsideRectangle(10, rect1);            
     
          // Ellipse2D.Double e1 = new Ellipse2D.Double(rect1.getX(),rect1.getY(),rect1.getWidth(),rect1.getHeight());
           g2d.setColor(Color.red);
 //          g2d.draw(e1);
           g2d.setColor(Color.green);
           //Polygon pol2 = RegularPolygon.generateRegularPolygonInsideRectangle(7, rect2);            
          // Ellipse2D.Double e2 = new Ellipse2D.Double(rect2.getX(),rect2.getY(),rect2.getWidth(),rect2.getHeight());
           g2d.setColor(Color.red);
  //         g2d.draw(e2);
           g2d.setColor(Color.green);
           //Polygon pol3 = RegularPolygon.generateRegularPolygonInsideRectangle(9, rect3);            
           //Ellipse2D.Double e3 = new Ellipse2D.Double(rect3.getX(),rect3.getY(),rect3.getWidth(),rect3.getHeight());
           g2d.setColor(Color.red);
 //          g2d.draw(e3);
           g2d.setColor(Color.green);   
           //Polygon pol4 = RegularPolygon.generateRegularPolygonInsideRectangle(5, rect4);          
          // Ellipse2D.Double e4 = new Ellipse2D.Double(rect4.getX(),rect4.getY(),rect4.getWidth(),rect4.getHeight());
           g2d.setColor(Color.red);
  //         g2d.draw(e4);
           g2d.setColor(Color.green);
            
           Polygon pol5 = new Polygon();
            pol5.addPoint(160,10);
            pol5.addPoint(210,120);
            pol5.addPoint(320,180);
            pol5.addPoint(50,280);
            pol5.addPoint(10,120);
            g2d.draw(pol5);
            

      /*      Rectangle rect5 = pol5.getBounds();
            g2d.draw(rect5);  
            Polygon e5 = ConcreteContour.generateRegularPolygonOutsideRectangle(30, rect5);
            g2d.setColor(Color.red);
            g2d.draw(e5);
            g2d.setColor(Color.green);*/
            
            
    /*      	g2d.draw(pol1); 
            MaxRectangleFinder max1 = new MaxRectangleFinder(pol1);
            Rectangle square1 = max1.getMaxSquare();
            g2d.draw(square1);
            
            
            g2d.draw(pol2);  
            MaxRectangleFinder max2 = new MaxRectangleFinder(pol2);
            Rectangle square2 = max2.getMaxSquare();
            g2d.draw(square2); 
            
            
            g2d.draw(pol3);  
            MaxRectangleFinder max3 = new MaxRectangleFinder(pol3);            
            Rectangle square3 = max3.getMaxSquare();
            g2d.draw(square3);
            
            g2d.draw(pol4);  
            MaxRectangleFinder max4 = new MaxRectangleFinder(pol4);     
            Rectangle square4 = max4.getMaxSquare();
            g2d.draw(square4);
            
            g2d.draw(pol5);  
            MaxRectangleFinder max5 = new MaxRectangleFinder(pol5);           
            Rectangle square5 = max5.getMaxSquare();
            g2d.draw(square5); */
            
   /*         Polygon pol6 = new Polygon();
            pol6.addPoint(0,0);
            pol6.addPoint(180,0);
            pol6.addPoint(140,100);
            pol6.addPoint(150,130);
            pol6.addPoint(120,180);
            pol6.addPoint(0,180);
            pol6.addPoint(100,50);
            pol6.translate(300, 300);
            g2d.setColor(Color.blue);
            g2d.draw(pol6); 
            MaxRectangleFinder max6 = new MaxRectangleFinder(pol6);           
            Rectangle square6 = max6.getMaxSquare();
            g2d.draw(square6);     
            Rectangle rect6 = pol6.getBounds();
            g2d.draw(rect6);
            Polygon e6 = ConcreteContour.generateRegularPolygonOutsideRectangle(30, rect6);
            g2d.setColor(Color.red);
            g2d.draw(e6);
            g2d.setColor(Color.green);*/
 
            
            
            
            
 /*           g2d.setColor(Color.red);
            Dimension rect7 = this.getSize();
            int x1 = rect7.width-10;
            int y1 = rect7.height-10;
            System.out.println(rect7.toString());
            
            Polygon pol7 = new Polygon();
            pol7.addPoint(10,10);
            pol7.addPoint(x1,10);
            pol7.addPoint(x1,y1);
            pol7.addPoint(10,y1);            
            MaxRectangleFinder max7 = new MaxRectangleFinder(pol7);
            Rectangle square7 = max7.getMaxSquare();
            g2d.draw(pol7);
            g2d.draw(square7);
            
            g2d.setColor(Color.blue);
            Polygon pol8 = new Polygon();
            pol8.addPoint(0,0);
            pol8.addPoint(1,0);
            pol8.addPoint(1,1);
            pol8.addPoint(0,1);            
            MaxRectangleFinder max8 = new MaxRectangleFinder(pol8);
            Rectangle square8 = max8.getMaxSquare();
            g2d.draw(pol8);
            g2d.draw(square8);
            
            g2d.setColor(Color.orange);
            Polygon pol9 = new Polygon();
            pol9.addPoint(0,0);
            pol9.addPoint(100,0);
            pol9.addPoint(100,100);
            pol9.addPoint(0,100);            
            MaxRectangleFinder max9 = new MaxRectangleFinder(pol9);
            Rectangle square9 = max9.getMaxSquare();
            g2d.draw(pol9);
            g2d.draw(square9);    */   
           
           Polygon polygon1 = new Polygon();
           polygon1.addPoint(0, 0);
           polygon1.addPoint(100, 0);
           polygon1.addPoint(100,100);
           polygon1.addPoint(0, 100);
           
           Polygon polygon2 = new Polygon();
           polygon2.addPoint(30, 30);
           polygon2.addPoint(60, 30);
           polygon2.addPoint(60,60);
           polygon2.addPoint(30, 60);
           
    
           g2d.setColor(Color.red);
           g2d.draw(polygon1);
           g2d.draw(polygon2);

           
           
           Polygon polygon3 = new Polygon();
           polygon3.addPoint(0, 0);
           polygon3.addPoint(150, 0);
           polygon3.addPoint(150,50);
           polygon3.addPoint(0, 50);
           
           Polygon polygon4 = new Polygon();
           polygon4.addPoint(0, 50);
           polygon4.addPoint(50, 0);
           polygon4.addPoint(50,100);
          
           
           Polygon polygon5 = new Polygon();
           polygon5.addPoint(0, 40);
           polygon5.addPoint(20, 0);
           polygon5.addPoint(100,20);
           polygon5.addPoint(40, 40);
           polygon5.addPoint(10, 100);
           
           Polygon polygon6 = new Polygon();
           polygon6.addPoint(20, 20);
           polygon6.addPoint(100, 20);
           polygon6.addPoint(100, 80);
           polygon6.addPoint(20, 80);      
           polygon6.addPoint(20, 40);
           
           Polygon polygon7 = new Polygon();
           polygon7.addPoint(80, 90);
           polygon7.addPoint(100, 90);
           polygon7.addPoint(100, 100);
           
           Polygon polygon8 = new Polygon();
           polygon8.addPoint(0,100);
           polygon8.addPoint(50, 0);
           polygon8.addPoint(100, 100);

           
           Polygon polygon9 = new Polygon();
           polygon9.addPoint(0, 20);
           polygon9.addPoint(100, 20);
           polygon9.addPoint(100, 50);
           polygon9.addPoint(75, 50);
           polygon9.addPoint(50,40);
           polygon9.addPoint(25,50);
           polygon9.addPoint(0, 50); 
           
           //int intersectionArea1 = PolygonIntersect.intersectionArea(polygon1, polygon2);
           //int intersectionArea2 = PolygonIntersect.intersectionArea(polygon1, polygon3);
           //int intersectionArea3 = PolygonIntersect.intersectionArea(polygon1, polygon4);
           //int intersectionArea4 = PolygonIntersect.intersectionArea(polygon5, polygon6);
           //int intersectionArea5 = PolygonIntersect.intersectionArea(polygon5, polygon7);
           //int intersectionArea6 = PolygonIntersect.intersectionArea(polygon6, polygon7);
           //int intersectionArea7 = PolygonIntersect.intersectionArea(polygon8, polygon9);
           
   /*        System.out.println("intersection area 1: " + intersectionArea1);
           System.out.println("intersection area 2: " + intersectionArea2);
           System.out.println("intersection area 3: " + intersectionArea3);
           System.out.println("intersection area 4: " + intersectionArea4);
           System.out.println("intersection area 5: " + intersectionArea5);
           System.out.println("intersection area 6: " + intersectionArea6); 
           System.out.println("intersection area 7: " + intersectionArea7); */
           
  
        }
    }
    
    public TestFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Test");
        this.setSize(1024, 768);
        this.getContentPane().add(new SomePoly());
    }

    public static void main(String[] args) {
        TestFrame m = new TestFrame();
        m.setVisible(true);

    }
}