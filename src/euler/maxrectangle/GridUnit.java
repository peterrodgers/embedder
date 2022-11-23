package euler.maxrectangle;

import java.awt.Point;

public class GridUnit{
	
	private int rowIdx;
	private int colIdx;
	private int level;
	private int startX;
	private int startY;
	private int unitSize;
	private Point centre;
	protected boolean visited;
	
	public GridUnit(int size, int rowIndex, int colIndex, int xCoor, int yCoor){
		rowIdx = rowIndex;
		colIdx = colIndex;
		unitSize = size;
		startX = xCoor;
		startY = yCoor;
		level = -1;
		centre = new Point((int)(startX+unitSize/2),(int)(startY+unitSize/2));
		visited = false;

	}

	public int getRowIndex(){return rowIdx;}
	public int getColIndex(){return colIdx;}
	public void setLevel(int alevel){level = alevel;}
	public int getLevel(){return level;}
	public int getStartX(){return startX;}
	public int getStartY(){return startY;}
	public int getSize(){return unitSize;}
	public Point getCentre(){return centre;}
	public void setVisited(boolean v){visited=v;}
	public boolean getVisited(){return visited;}

	public void print(){
		System.out.println("index:"+ getRowIndex()+", "+ getColIndex()
				+" level "+ getLevel() + " start point (" + getStartX() +"," + getStartY() +")");
	}
}