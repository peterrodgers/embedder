package euler.maxrectangle;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Grid {
	
	private Rectangle rectangle;
	private int unitSize;
	private ArrayList<GridUnit> grids;
	private int noRows;
	private int noCols;

	public Grid(Rectangle rect, int size){
		rectangle = rect;
		unitSize = size;
		generateGrids();
	
	
		
	}
	
	public Rectangle getRectangle(){return rectangle;}
	public void generateGrids(){		
		
		grids = new ArrayList<GridUnit>();
		noRows = (int)(rectangle.height/unitSize)+1;
		noCols = (int)(rectangle.width/unitSize)+1;
		int x = rectangle.x;
		int y = rectangle.y;
		
	    int startX = x;
	    int startY = y;
	  
	    for ( int rowIdx = 1; rowIdx < noRows+1; rowIdx++ )
	    {
	      startX = x;
	      for ( int colIdx = 1; colIdx < noCols+1; colIdx++ )
		    {
		      GridUnit grid = new GridUnit(unitSize, rowIdx, colIdx, startX, startY);
		      grid.setVisited(false);
		      grids.add(grid);		
		      startX += unitSize ;
		    }
	      
	      startY += unitSize;
	    }
	}
	public ArrayList<GridUnit> getGrids(){
		return grids;		
	}
	public int getGridUnitSize(){return unitSize;}
	
	public void printAll(){
		for(GridUnit grid: grids){
			System.out.println("index:"+grid.getRowIndex() +", "+grid.getColIndex()
					+" level "+ grid.getLevel() + "start point (" + grid.getStartX() +"," + grid.getStartY() +")");
		}
		
	}
	public GridUnit getFirstClosestUnit(Point p,ArrayList<Polygon> polygons){
			for(int i = 0 ; i < grids.size(); i++){
			GridUnit gu = grids.get(i);
			int x1 = gu.getStartX();
			int y1 = gu.getStartY();
			boolean inside = false;
			for(Polygon pol: polygons){
				if(pol.contains(x1,y1)){
					inside = true;
				}
				if(intersect(p,gu.getCentre(),polygons)){
					inside = true;
				}
			}
			if(!inside&&gu.getVisited()==false){
				gu.setVisited(true);
				return gu;
			}
		}
		
		return null;
	}
	
	public Point getClosestGridUnitCentre(Point p,ArrayList<Polygon> polygons){
		
		GridUnit closestGrid = getFirstClosestUnit (p, polygons);
		
		if(!getRectangle().contains(p)||closestGrid==null){
			System.out.println("error, point not inside boundary");
			return null;
		}
		else{
			double x = p.getX();
			double y = p.getY();
			double minDis = pjr.graph.Util.distance(closestGrid.getCentre().getX(),closestGrid.getCentre().getY(), x, y);
			for(int i = 0 ; i < grids.size(); i++){
				GridUnit gu = grids.get(i);
				int x1 = (int)gu.getCentre().getX();
				int y1 = (int)gu.getCentre().getY();
				double dis = pjr.graph.Util.distance(x1, y1, x, y);
				if(dis<minDis){
					boolean inside = false;
					for(Polygon pol: polygons){
						if(pol.contains(x1,y1)){
							inside = true;
						}
						if(intersect(p,new Point(x1,y1),polygons)){
							inside = true;
						}
					}
					if(!inside){				
						minDis = dis;
						gu.setVisited(true);
						closestGrid = gu;						
					}
				}
			}			
			return closestGrid.getCentre();			
		}
	}
	
	public boolean intersect(Point p1, Point p2, ArrayList<Polygon> pols){

		boolean intersect = true;
		for(Polygon pol : pols){
			
			int x = pol.xpoints[pol.npoints-1];
			int y = pol.ypoints[pol.npoints-1];
			int x0 = pol.xpoints[0];
			int y0 = pol.ypoints[0];			
			Point p3 = new Point(x,y);
			Point p4 = new Point(x0,y0);
			Point p5 = pjr.graph.Util.intersectionPointOfTwoLines(p1, p2, p3, p4);
			int maxX, maxY,minX, minY;
			if(x>x0){
				maxX = x;
				minX = x0;
			}
			else{
				maxX = x0;
				minX = x;
			}
			if(y>y0){
				maxY = y;
				minY = y0;
			}
			else{
				maxY = y0;
				minY = y;
			}
		
			if(p5.getX()>maxX||p5.getX()<minX||p5.getY()>maxY||p5.getY()<minY)
				intersect = false;
			
			for(int i = 0 ; i < pol.npoints-1 ; i++){
				x = pol.xpoints[i];
				y = pol.ypoints[i];
				x0 = pol.xpoints[i+1];
				y0 = pol.ypoints[i+1];
				p3 = new Point(x,y);
				p4 = new Point(x0,y0);
				p5 = pjr.graph.Util.intersectionPointOfTwoLines(p1, p2, p3, p4);
				if(x>x0){
					maxX = x;
					minX = x0;
				}
				else{
					maxX = x0;
					minX = x;
				}
				if(y>y0){
					maxY = y;
					minY = y0;
				}
				else{
					maxY = y0;
					minY = y;
				}
				
				if(p5.getX()>maxX||p5.getX()<minX||p5.getY()>maxY||p5.getY()<minY)
					intersect = false;
				
			}				
		}
		return intersect;
			
			
	}

}
