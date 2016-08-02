package geographic.interfaces;

import java.util.List;

import geographic.Coordinate;

public interface MapManager<T> {
	
	public void putCell(T cell);
	
	public Cell getCell(Coordinate pos);
	
	public Cell getCell(int idCell);
	
	public Cell nextCell(Cell cell,int direction);
	
	public int getDirection(Coordinate c1,Coordinate c2);
	
	public Cell[] getAdjacentCells(Cell cell);
	
	public int getCellNumber();
	
	public int shiftDirection(int direction);
	
	public int getDirectionNumber();
	
	public List<Cell> getCells();
	
}
