package euler.maxrectangle;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MaxRectangleFinder {
	
	Rectangle maxRectangle;	
	Polygon polygon;
	Grid Grid;
	int maxLevel;
	ArrayList<GridUnit> grids;
	
	public MaxRectangleFinder(Polygon pol){
		
		polygon = pol;
		int gridSize = 0;
		Rectangle boundingBox = pol.getBounds();		
		gridSize = (int)((boundingBox.height+boundingBox.width)/50);
		if(gridSize == 0) {
			gridSize = 1;
		}
		Grid = new Grid(boundingBox,gridSize);
		grids = generatePolygonGrids(polygon);
		maxLevel = 0;	
		
	}
	
	public void setGridsLevel(){
	
		ArrayList<Line2D> edgeList = getEdgeList(polygon);
		//set zero grid
		for(GridUnit grid : grids){			
			Rectangle rect = new Rectangle(grid.getStartX(), grid.getStartY(), grid.getSize(),grid.getSize());
			for(Line2D l : edgeList){
				if(l.intersects(rect)){
			    	grid.setLevel(0);
			    }
		    }
		}
		int currentLevel = 1;
		int noOfUnitsChanged = 0;
		do{
			noOfUnitsChanged = 0;
			for(GridUnit grid : grids){	
				if(grid.getLevel() == currentLevel-1){
					ArrayList<GridUnit> neighbours = getNeighbourGrids(grid.getRowIndex(), grid.getColIndex());
					for(GridUnit neighbour :neighbours){
						if(neighbour.getLevel()== -1){
							neighbour.setLevel(currentLevel);
							noOfUnitsChanged ++;
						}
					}
				}	
			}
			maxLevel = currentLevel-1;
			currentLevel ++;
		}while(noOfUnitsChanged != 0);
		
/*		for(GridUnit grid: grids){			
			grid.print();
		}	*/
	}
    public ArrayList<Line2D> getEdgeList(Polygon pol){
        
    	ArrayList<Line2D> ret = new ArrayList<Line2D>();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        
        for(int i = 0 ; i< pol.npoints; i++){
        	int x = pol.xpoints[i];
        	int y = pol.ypoints[i];
        	Point2D p = new Point2D.Double(x,y);
        	points.add(p);
        }        
        Point2D a,b;
        Line2D e;
        a = points.get(pol.npoints-1);
        for(int i=0; i<pol.npoints; i++){
            b = points.get(i);      
            e = new Line2D.Double(a,b);
            ret.add(e);
            a = b;
        }
        return ret;
    }
    
	public ArrayList<GridUnit> generatePolygonGrids(Polygon pol){
		ArrayList<GridUnit> ret = new ArrayList<GridUnit>();
		for(GridUnit grid : Grid.getGrids()){			
			Rectangle rect = new Rectangle(grid.getStartX(), grid.getStartY(), grid.getSize(),grid.getSize());
			if(pol.intersects(rect)){
				ret.add(grid);
			}
		}
		return ret;
	}

	public ArrayList<GridUnit> getNeighbourGrids(int rowIndex, int colIndex){
		
		ArrayList<GridUnit> ret = new ArrayList<GridUnit>();
		GridUnit grid = getGridUnit(rowIndex,colIndex);		
		if(grid!=null){
			GridUnit grid1 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex()-1);
			GridUnit grid2 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex());
			GridUnit grid3 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex()+1);
			GridUnit grid4 = getGridUnit(grid.getRowIndex(),grid.getColIndex()-1);
			GridUnit grid5 = getGridUnit(grid.getRowIndex(),grid.getColIndex()+1);
			GridUnit grid6 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex()-1);
			GridUnit grid7 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex());
			GridUnit grid8 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex()+1);
			if(grid1!=null)
				ret.add(grid1);
			if(grid2!=null)
				ret.add(grid2);		
			if(grid3!=null)
				ret.add(grid3);
			if(grid4!=null)
				ret.add(grid4);				
			if(grid5!=null)
				ret.add(grid5);
			if(grid6!=null)
				ret.add(grid6);				
			if(grid7!=null)
				ret.add(grid7);
			if(grid8!=null)
				ret.add(grid8);		
		}		
		return ret;
	}
	
	public GridUnit getGridUnit(int rowIndex, int colIndex){
		for(GridUnit grid : grids){			
			if(grid.getColIndex() == colIndex && grid.getRowIndex() == rowIndex)
				return grid;
		}
		return null;
	}
	
	public Rectangle getMaxSquare(){
		
		setGridsLevel();
		GridUnit maxGrid = null;
		
		for(GridUnit grid: grids){
			if(grid.getLevel()==maxLevel)
				maxGrid = grid;
		}
		int x = maxGrid.getStartX()-(maxLevel-1)*maxGrid.getSize();
		int y = maxGrid.getStartY()-(maxLevel-1)*maxGrid.getSize();
		int maxHeight= maxGrid.getSize()*(maxLevel*2 - 1);
		int maxWidth = maxGrid.getSize()*(maxLevel*2 - 1);
		
		Rectangle maxSquare = new Rectangle(x,y,maxWidth,maxHeight);
		
		return maxSquare;
	}
	
	public Rectangle getMaxRegularRectangle(){
		
		GridUnit maxGrid = null;
		
		for(GridUnit grid: grids){
			if(grid.getLevel()==maxLevel)
				maxGrid = grid;
		}
		int x = maxGrid.getStartX()-(maxLevel-1)*maxGrid.getSize();
		int y = maxGrid.getStartY()-(maxLevel-1)*maxGrid.getSize();
		int maxHeight= maxGrid.getSize()*(maxLevel*2 - 1);
		int maxWidth = maxGrid.getSize()*(maxLevel*2 - 1);
		
		Rectangle maxSquare = new Rectangle(x,y,maxWidth,maxHeight);
		
		return maxSquare;
	}
	
	
	public boolean extendable(int rowIndex, int colIndex){
	
		GridUnit grid = getGridUnit(rowIndex,colIndex);		
		if(grid ==null){
			return false; 
		}
		else{
			GridUnit grid1 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex()-1);
			GridUnit grid2 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex());
			GridUnit grid3 = getGridUnit(grid.getRowIndex()-1,grid.getColIndex()+1);
			GridUnit grid4 = getGridUnit(grid.getRowIndex(),grid.getColIndex()-1);
			GridUnit grid5 = getGridUnit(grid.getRowIndex(),grid.getColIndex()+1);
			GridUnit grid6 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex()-1);
			GridUnit grid7 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex());
			GridUnit grid8 = getGridUnit(grid.getRowIndex()+1,grid.getColIndex()+1);
			if(grid1==null||grid1.getLevel()==0)
				return false;
			if(grid2==null||grid2.getLevel()==0)
				return false;	
			if(grid3==null||grid3.getLevel()==0)
				return false;
			if(grid4==null||grid4.getLevel()==0)
				return false;			
			if(grid5==null||grid5.getLevel()==0)
				return false;
			if(grid6==null||grid6.getLevel()==0)
				return false;		
			if(grid7==null||grid7.getLevel()==0)
				return false;
			if(grid8==null||grid8.getLevel()==0)
				return false;
			return true;
		}
	}
	
}
