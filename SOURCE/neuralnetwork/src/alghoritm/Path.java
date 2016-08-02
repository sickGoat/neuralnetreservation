package alghoritm;

import java.util.LinkedList;
import java.util.List;

import geographic.interfaces.Cell;
import geographic.interfaces.TrackParser.Track;

public class Path {
	
	private int id;
	
	private int inDirection;
	
	private double speed;
	
	
	private List<Cell> cells;
	
	public Path(){};
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getInDirection() {
		return inDirection;
	}
	
	public void setInDirection(int inDirection) {
		this.inDirection = inDirection;
	}
	
	public List<Cell> getCells() {
		return cells;
	}
	
	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	
	public void addCell(Cell c){
		this.cells.add(c);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	
	public static List<Path> createPaths(List<Track> tracks){
		List<Path> paths = new LinkedList<>();
		tracks.forEach(track -> {
			Path path = new Path();
			path.id = track.id;
			path.cells = new LinkedList<Cell>(track.visitedCells);
			path.inDirection = track.direction;
			path.speed = track.getAvgSpeed();
			paths.add(path);
		});
		
		return paths;
	}
}
