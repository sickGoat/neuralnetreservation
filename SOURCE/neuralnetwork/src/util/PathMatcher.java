package util;

import java.util.LinkedList;
import java.util.List;

import geographic.interfaces.Cell;

public class PathMatcher {
	
	private int pathId;
	
	private List<Cell> predicted;
	
	private List<Cell> realPath;
	
	public PathMatcher(){}
	
	public PathMatcher(int pathId, List<Cell> predicted,List<Cell> realPath){
		this.pathId = pathId;
		this.predicted = predicted;
		this.realPath = realPath;
	}
	

	public int getPathId() {
		return pathId;
	}
	
	public void setPathId(int pathId) {
		this.pathId = pathId;
	}
	
	
	public List<Cell> getPredicted() {
		List<Cell> predictedUnique = new LinkedList<>();
		for (Cell c: predicted){
			if(!predictedUnique.contains(c)){
				predictedUnique.add(c);
			}
		}
		predicted = predictedUnique;
		return predictedUnique;
	}
	
	public void setPredicted(List<Cell> predicted) {
		this.predicted = predicted;
	}
	
	public List<Cell> getRealPath() {
		return realPath;
	}
	
	public void setRealPath(List<Cell> realPath) {
		this.realPath = realPath;
	}
	
	public List<Cell> getWaste(){
		List<Cell> wasted = new LinkedList<>();
		for(Cell c:predicted)
			if(!realPath.contains(c))
				wasted.add(c);
		
		return wasted;
	}
	
	public List<Cell> getRightPredicted(){
		List<Cell> rights = new LinkedList<>();
		for(Cell c:realPath)
			if(predicted.contains(c))
				rights.add(c);
		
		return rights;
	}
	
	
	public List<Cell> getNotPredicted(){
		List<Cell> notPredicted = new LinkedList<>();
		for(Cell c:realPath)
			if(!predicted.contains(c))
				notPredicted.add(c);
		
		return notPredicted;
	}
	
}
