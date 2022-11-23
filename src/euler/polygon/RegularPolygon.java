package euler.polygon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RegularPolygon extends JFrame{
	/**
	 * Creates a regular polygon with the required number of corners
	 * and outer radius. The polygon is centred
	 * on the origin.
	 */
		class TestPanel extends JPanel{
	 
		 protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				  //test case for inside circle finder    
		       	Polygon small1 = new Polygon();
		    	small1.addPoint(80,90);
		    	small1.addPoint(100,70);
		    	small1.addPoint(140,90);
		    	small1.addPoint(110,130);
		    	small1.addPoint(90, 120);
		    	
		    	Polygon big1 = new Polygon();
		    	big1.addPoint(0,90);
		    	big1.addPoint(70,0);
		    	big1.addPoint(170,30);
		    	big1.addPoint(140,90);
		    	big1.addPoint(180,140);
		    	big1.addPoint(110,170);   	
		   
		   //     small1.translate(200, 200);
		   //     big1.translate(200, 200);   	
	
		           g2d.setColor(Color.lightGray);
		       //    Polygon circle1 = RegularPolygon.insideCircle(big1, small1, g2d);
		           
		           g2d.setColor(Color.red);
		       //    if(circle1!=null)
		     //  	   g2d.draw(circle1);
		     //      g2d.draw(small1);
		     //      g2d.draw(big1);
		          
		 
		           	Polygon small2 = new Polygon();
		        	small2.addPoint(90,100);
		        	small2.addPoint(110,80);
		        	small2.addPoint(130,100);
		        	small2.addPoint(130,110);
		        	small2.addPoint(120, 150);
		        	small2.addPoint(87, 120);
		        	
		        	Polygon big2 = new Polygon();
		        	big2.addPoint(0,75);
		        	big2.addPoint(80,0);
		        	big2.addPoint(170,50);
		        	big2.addPoint(170,140);
		        	big2.addPoint(80,170);
		        	big2.addPoint(85,110);   	
		        	      
		            small2.translate(400, 400);
		            big2.translate(400, 400);
		           

		            g2d.setColor(Color.lightGray);
		            Polygon circle2 = RegularPolygon.insideCircle(big2, small2, g2d);
		            g2d.setColor(Color.red);
		            if(circle2!=null)
		         	   g2d.draw(circle2);
		            g2d.draw(small2);
		            g2d.draw(big2);
		            
		            
		          	Polygon small3 = new Polygon();
		           	small3.addPoint(100,48);
		        	small3.addPoint(110,70);
		        	small3.addPoint(90,95);
		        	small3.addPoint(66, 85);
		        	small3.addPoint(40, 60);
		      		        	
		        	Polygon big3 = new Polygon();
		        	big3.addPoint(10,120);
		        	big3.addPoint(50,0);
		        	big3.addPoint(150,70);
		        	big3.addPoint(100,150);
		        	

		            g2d.setColor(Color.lightGray);
		            Polygon circle3 = RegularPolygon.insideCircle(big3, small3, g2d);
		            g2d.setColor(Color.red);
		            if(circle3!=null)
		         	   g2d.draw(circle3);
		            g2d.draw(small3);
		            g2d.draw(big3);  
		            
		            
		          	Polygon small4 = new Polygon();
		           	small4.addPoint(100,48);
		        	small4.addPoint(110,70);
		        	small4.addPoint(90,95);
		        	small4.addPoint(65, 85);
		        	small4.addPoint(40, 60);
		      		        	
		        	Polygon big4 = new Polygon();
		        	big4.addPoint(10,120);
		        	big4.addPoint(50,0);
		        	big4.addPoint(150,70);
		        	big4.addPoint(100,150);
		        	
		        	small4.translate(250,0);
		        	big4.translate(250, 0);

		            g2d.setColor(Color.lightGray);
		            //Polygon circle4 = RegularPolygon.insideCircle(big4, small4, g2d);
		            g2d.setColor(Color.red);
		           // if(circle4!=null)
		         	//   g2d.draw(circle4);
		          //  g2d.draw(small4);
		          //  g2d.draw(big4);      	
		            
		            Polygon small5 = new Polygon();
		           	small5.addPoint(200,96);
		        	small5.addPoint(220,140);
		        	small5.addPoint(180,190);
		        	small5.addPoint(132, 170);
		        	small5.addPoint(59, 120);
		        	
		        	Polygon big5 = new Polygon();
		        	big5.addPoint(20,240);
		        	big5.addPoint(100,0);
		        	big5.addPoint(300,140);
		        	big5.addPoint(200,300);      
		        	
		        	

		            g2d.setColor(Color.lightGray);
		            Polygon circle5 = RegularPolygon.insideCircle(big5, small5, g2d);
		            g2d.setColor(Color.green);
		            if(circle5!=null)
		         	   g2d.draw(circle5);
		            g2d.draw(small5);
		            g2d.draw(big5);      	
		        	
		        	
		        	
		            Polygon small6 = getCopy(small5);
		            Polygon big6 = getCopy(big5);
		            small6.addPoint(50,100);
		            
		            small6.translate(400, 0);
		            big6.translate(400, 0);
		            
		            g2d.setColor(Color.lightGray);
		            Polygon circle6 = RegularPolygon.insideCircle(big6, small6, g2d);
		            g2d.setColor(Color.green);
		            if(circle6!=null)
		         	   g2d.draw(circle6);
		            g2d.draw(small6);
		            g2d.draw(big6);      	     
		        	
		            Polygon small7 = getCopy(small5);
		            Polygon big7 = getCopy(big5);
		            small7.addPoint(100,30);
		           
		            
		            small7.translate(0, 300);
		            big7.translate(0, 300);
		            
		            g2d.setColor(Color.lightGray);
		            Polygon circle7 = RegularPolygon.insideCircle(big7, small7, g2d);
		            g2d.setColor(Color.green);
		            if(circle7!=null)
		         	   g2d.draw(circle7);
		            g2d.draw(small7);
		            g2d.draw(big7);  
		            
		 	}
	
	 	}
	 	public static Polygon generateRegularPolygon(int centre_x, int centre_y,
            int outerRadius, int numOfCorners) {
	        double[] x = new double[numOfCorners];
	        double[] y = new double[numOfCorners];
	
	        // centre is starting point of drawing, translation to the correct
	        // position will happen in the end
	        x[0] = centre_x;
	        y[0] = centre_y;
	
	        // length of one side
	        double sideLength = 2 * outerRadius * Math.sin(Math.PI / numOfCorners);
	
	        // outer angle
	        double angle = (2 * Math.PI) / numOfCorners;
	
	        // second corner
	        x[1] = x[0] + (Math.cos((Math.PI - angle) / 2) * sideLength);
	        y[1] = y[0] + (Math.sin((Math.PI - angle) / 2) * sideLength);
	
	        // Direction vector
	        double[] vec = new double[2];
	        vec[0] = x[1] - x[0];
	        vec[1] = y[1] - y[0];
	
	        // helper variables
	        double x_afterRot, y_afterRot;
	
	        for (int i = 2; i < numOfCorners; i++) {
	
	            // translation vector calculation
	            x_afterRot = vec[0] * Math.cos(-angle) - vec[1] * Math.sin(-angle);
	            y_afterRot = vec[1] * Math.cos(-angle) + vec[0] * Math.sin(-angle);
	            vec[0] = x_afterRot;
	            vec[1] = y_afterRot;
	
	            // new corner = old corner + translation vector after rotation
	            x[i] = x[i - 1] + vec[0];
	            y[i] = y[i - 1] + vec[1];
	
	            // new direction vector
	            vec[0] = x[i] - x[i - 1];
	            vec[1] = y[i] - y[i - 1];
	        }
	
	        // Double to integer to match Polygon constructor
	        int[] final_x = new int[numOfCorners];
	        int[] final_y = new int[numOfCorners];
	        for (int i = 0; i < numOfCorners; i++) {
	            // translation to centre of circle
	            final_x[i] = (int) Math.round(x[i]) - outerRadius ;
	            final_y[i] = (int) Math.round(y[i]);
	        }
	
	        Polygon pol = new Polygon(final_x, final_y, numOfCorners);
	        return pol;
	 	}
	
		/**
		 * draw a maximised polygon in a given rectangle. 
		 * This does not change the aspect ratio of the graph, 
		 * it is either maximised in x or y directions.
		 */
		public static Polygon generateRegularPolygonInsideRectangle(
	             int numOfCorners, Rectangle rectangle){
			
			int outerRadius = 0;
			
			int width = (int)rectangle.getWidth();
			int height = (int)rectangle.getHeight();
			int centre_x = (int)rectangle.getCenterX();
			int centre_y = (int)rectangle.getCenterY();
			
			//outerRadius equals the smaller one between width and height
			if(width > height)
				outerRadius = (int)(height/2);
			else
				outerRadius = (int)(width/2);
			
			//System.out.println("outer radius" + outerRadius);
			
			Polygon pol = generateRegularPolygon(centre_x, centre_y, outerRadius, numOfCorners);
			return pol;
		}	
		/**
		 * draw a maximised polygon outside a given rectangle. 
		 * This does not change the aspect ratio of the graph, 
		 * it is either maximised in x or y directions.
		 */
		public static Polygon generateRegularPolygonOutsideRectangle(
	             int numOfCorners, Rectangle rectangle){
			
				
			//int width = (int)rectangle.getWidth();
			//int height = (int)rectangle.getHeight();
			int centre_x = (int)rectangle.getCenterX();
			int centre_y = (int)rectangle.getCenterY();
			int radius = (int)(Math.sqrt(rectangle.getWidth()*rectangle.getWidth() + rectangle.getHeight()*rectangle.getHeight())/2+1);
			
			Polygon pol = generateRegularPolygon(centre_x, centre_y, radius, numOfCorners);
			return pol;
		}	
		
		public static Polygon insideCircle(Polygon big, Polygon small){
			
			Rectangle rect1 = small.getBounds();
			Rectangle rect2 = big.getBounds();
			
			
			Polygon circle = generateRegularPolygonOutsideRectangle(20, rect1);
			
			double radius = circle.getBounds().getHeight()/2;		
			double minRadius = 0;			
			double diffSize = 0;
			double diffRadius = 0;
			
			if(rect1.getWidth()<rect1.getHeight()){
				minRadius = rect1.getHeight()/2;
				diffRadius = radius - minRadius;
				if(rect2.getWidth()<rect2.getHeight()){
					diffSize = rect2.getHeight()/2 - rect1.getHeight()/2 - diffRadius;
				}
				else
					diffSize = rect2.getWidth()/2 - rect1.getHeight()/2 - diffRadius;
			}
			else{
				minRadius = small.getBounds().getWidth()/2;
				diffRadius = radius - minRadius;
				if(rect2.getWidth()<rect2.getHeight()){
					diffSize = rect2.getHeight()/2 - rect1.getWidth()/2 - diffRadius;
				}
				else
					diffSize = rect2.getWidth()/2 - rect1.getWidth()/2 - diffRadius;
			}
			
			//System.out.println("diffSize = " + diffSize);
			
			double radiusRange = radius - minRadius;
			int iteration = 1;
			
	
			Polygon currentCircle = circle;
			double currentFitness =  getFintness(circle, small, big);
			
			if(currentFitness == 0){
				//System.out.println("iteration 0 " ) ;
				return circle;
			}
			else{
				int deltaX =  1;
				int deltaY =  1;
				
				
				while(iteration < 20 && deltaX <= diffSize){		
	
					ArrayList<Polygon> neighbours = getNeighbours(currentCircle,deltaX,deltaY);
					
					boolean changed = false;
					
					for(int i = 0; i < neighbours.size(); i++){
						Polygon neighbour = neighbours.get(i);			
						if(radiusRange > 0) {
							Polygon newCircle = neighbour;
							for(int k = 0 ; k < radiusRange +1; k++){
							//	System.out.println("shrink : " + k);
							if(contains(newCircle,small)){
								newCircle = shrink(k, neighbour);

							}
							
							for(int j=0; j<(int)radiusRange+1; j++){
								double tempFitness = getFintness(newCircle, small, big); 
								if(tempFitness == 0){
									return newCircle;
								}
								else if(tempFitness > currentFitness){
									changed  = true;
									currentCircle = neighbours.get(i);
									currentFitness = tempFitness;								
								}
							}								
						}
					}			
				}	
					
				if(!changed){
					//System.out.println("not changed");
					return null;
				}			
					
				//System.out.println("iteration " + iteration);
				//System.out.println("delta = " + deltaX);
				iteration++;
				deltaX++;
				deltaY++;
				}		
			}
			System.out.println("no circle can be fitted between two polygons");
			return null;
		}
		private static Polygon insideCircle(Polygon big, Polygon small, Graphics2D g2d){
		
			Rectangle rect1 = small.getBounds();
			Rectangle rect2 = big.getBounds();
			
			
			Polygon circle = generateRegularPolygonOutsideRectangle(20, rect1);
			
			double radius = circle.getBounds().getHeight()/2;		
			double minRadius = 0;			
			double diffSize = 0;
			double diffRadius = 0;
			
			if(rect1.getWidth()<rect1.getHeight()){
				minRadius = rect1.getHeight()/2;
				diffRadius = radius - minRadius;
				if(rect2.getWidth()<rect2.getHeight()){
					diffSize = rect2.getHeight()/2 - rect1.getHeight()/2 - diffRadius;
				}
				else
					diffSize = rect2.getWidth()/2 - rect1.getHeight()/2 - diffRadius;
			}
			else{
				minRadius = small.getBounds().getWidth()/2;
				diffRadius = radius - minRadius;
				if(rect2.getWidth()<rect2.getHeight()){
					diffSize = rect2.getHeight()/2 - rect1.getWidth()/2 - diffRadius;
				}
				else
					diffSize = rect2.getWidth()/2 - rect1.getWidth()/2 - diffRadius;
			}
			
			//System.out.println("diffSize = " + diffSize);
			
			double radiusRange = radius - minRadius;
			int iteration = 1;
			
	
			Polygon currentCircle = circle;
			double currentFitness =  getFintness(circle, small, big);
			
			if(currentFitness == 0){
				//System.out.println("iteration 0 " ) ;
				return circle;
			}
			else{
				int deltaX =  1;
				int deltaY =  1;
				
				
				while(iteration < 20 && deltaX <= diffSize){		
	
					ArrayList<Polygon> neighbours = getNeighbours(currentCircle,deltaX,deltaY);
					
					boolean changed = false;
					
					for(int i = 0; i < neighbours.size(); i++){
						Polygon neighbour = neighbours.get(i);			
						if(radiusRange > 0) {
							Polygon newCircle = neighbour;
							for(int k = 0 ; k < radiusRange +1; k++){
							//	System.out.println("shrink : " + k);
							if(contains(newCircle,small)){
								newCircle = shrink(k, neighbour);
								g2d.draw(newCircle);
							}
							
							for(int j=0; j<(int)radiusRange+1; j++){
								double tempFitness = getFintness(newCircle, small, big); 
								if(tempFitness == 0){
									return newCircle;
								}
								else if(tempFitness > currentFitness){
									changed  = true;
									currentCircle = neighbours.get(i);
									currentFitness = tempFitness;								
								}
							}								
						}
					}			
				}	
					
				if(!changed){
					//System.out.println("not changed");
					return null;
				}			
					
				//System.out.println("iteration " + iteration);
				//System.out.println("delta = " + deltaX);
				iteration++;
				deltaX++;
				deltaY++;
				}		
			}
			System.out.println("no circle can be fitted between two polygons");
			return null;
		}
		public static Polygon shrink(int shrinkSize, Polygon pol){
			
			Rectangle rect = pol.getBounds();
			int startX = (int)rect.getX() + shrinkSize;
			int startY = (int)rect.getY() + shrinkSize;
			Rectangle rect1 = new Rectangle(startX,startY,rect.width-shrinkSize,rect.height-shrinkSize);
	
			Polygon ret = generateRegularPolygonInsideRectangle(20,rect1);
			return ret;
		}
		
		public static ArrayList<Polygon> getNeighbours(Polygon pol, int deltaX, int deltaY){
			ArrayList<Polygon> neighbours = new ArrayList<Polygon>();
			Polygon pol1 = getCopy(pol);
			pol1.translate(deltaX, 0);
			Polygon pol2 = getCopy(pol);
			pol2.translate(-deltaX, 0);		
			Polygon pol3 = getCopy(pol);
			pol3.translate(0, deltaY);
			Polygon pol4 = getCopy(pol);
			pol4.translate(0, -deltaY);	
			Polygon pol5 = getCopy(pol);
			pol5.translate(deltaX, deltaY);
			Polygon pol6 = getCopy(pol);
			pol6.translate(-deltaX, deltaY);		
			Polygon pol7 = getCopy(pol);
			pol7.translate(deltaX, -deltaY);
			Polygon pol8 = getCopy(pol);
			pol8.translate(-deltaX, -deltaY);	
			neighbours.add(pol1);
			neighbours.add(pol2);
			neighbours.add(pol3);
			neighbours.add(pol4);	
			neighbours.add(pol5);
			neighbours.add(pol6);	
			neighbours.add(pol7);
			neighbours.add(pol8);			
			return neighbours;
		}
		public static double getFintness(Polygon circle, Polygon small, Polygon big){
			
			double fitness = 0;
			double smallArea = PolygonArea(small);
			//double bigArea = PolygonArea(big);
			double circleArea = PolygonArea(circle);
			double smallIntersectArea = PolygonIntersect.intersectionArea(circle, small);
			double bigIntersectArea = PolygonIntersect.intersectionArea(big, circle);
			double diffSmall = Math.abs(smallIntersectArea - smallArea);
			double diffBig = Math.abs(bigIntersectArea - circleArea);
			fitness = - (diffSmall + diffBig);			
			return fitness;
		}
		
		public static Polygon getCopy(Polygon pol){
			
			Polygon copy = new Polygon();
			for(int i = 0 ; i < pol.npoints; i++){
				int x = pol.xpoints[i];
				int y = pol.ypoints[i];
				copy.addPoint(x, y);	
			}		
			return copy;
		}
		
		
		public static int PolygonArea(Polygon pol){
			
			Point2D[] polyPoints = new Point2D[pol.npoints];
			for(int i = 0 ; i < pol.npoints; i++){
				polyPoints[i] = new Point2D.Double(pol.xpoints[i], pol.ypoints[i]);
			}
			float area = (float)PolygonArea(polyPoints);
			return Math.round(area);
		}
		
		public static double PolygonArea(Point2D[] polyPoints) {
			int i, j, n = polyPoints.length;
			double area = 0;
	
			for (i = 0; i < n; i++) {
				j = (i + 1) % n;
				area += polyPoints[i].getX() * polyPoints[j].getY();
				area -= polyPoints[j].getX() * polyPoints[i].getY();
			}
			area /= 2.0;
			return Math.abs(area);
		}
		public static Polygon[] halfPolygons(int centre_x, int centre_y,
	            int outerRadius, int numOfCorners){
		
			Polygon pol = generateRegularPolygon(centre_x, centre_y, outerRadius, numOfCorners);
			Polygon[] ret = new Polygon[2];
			int halfPoint =	pol.npoints/2;
			for(int i = 0 ; i <= halfPoint; i++){
				ret[0].addPoint(pol.xpoints[i], pol.ypoints[i]);			
			}
			for(int j = halfPoint; j< pol.npoints;j++){
				ret[1].addPoint(pol.xpoints[j],pol.ypoints[j]);
			}
			ret[1].addPoint(pol.xpoints[0], pol.ypoints[0]);
			 return ret;
		}
		public static Polygon[] halfPolygons(Polygon pol){
			
			Polygon[] ret = new Polygon[2];
			ret[0] = new Polygon();
			ret[1] = new Polygon();
			int halfPoint =	pol.npoints/2;
			for(int i = 0 ; i <= halfPoint; i++){
				ret[0].addPoint(pol.xpoints[i], pol.ypoints[i]);			
			}
			for(int j = halfPoint; j< pol.npoints;j++){
				ret[1].addPoint(pol.xpoints[j],pol.ypoints[j]);
			}
			ret[1].addPoint(pol.xpoints[0], pol.ypoints[0]);
			 return ret;
		}
		
		
		public RegularPolygon() {
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    this.setTitle("RegularPolygon test");
		    this.setSize(800, 800);
		    this.getContentPane().add(new TestPanel());
		}
		public static boolean contains(Polygon big, Polygon small){
			for(int i = 0 ; i < small.npoints; i++){
				double x = small.xpoints[i];
				double y = small.ypoints[i];
				if(!big.contains(new Point2D.Double(x,y))){
					return false;
				}
			}
			
			
			return true;
		}
		public static void main(String[] args) {
			RegularPolygon m = new RegularPolygon();
		    m.setVisible(true);

}


	
	
	
	
	
	
	
	

}
