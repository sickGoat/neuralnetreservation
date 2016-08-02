package org.unical.neuralnetwork.gui.grid;

import java.util.List;

import org.graphstream.graph.Graph;

import geographic.Coordinate;
import geographic.interfaces.Cell;
import util.PathMatcher;

public interface GridController {
	
	public enum GridColorStyle{
		NORMAL("grid_normal"),
		HIDE("grid_hide"),
		PREDICTED("grid_foretold"),
		NOT_PREDICTED("grid_no_foretold"),
		WASTE("grid_waste");
		
		private final String cssValue;
		
		private GridColorStyle(String value){
			cssValue = value;
		}
		
		public String getCssValue(){
			return cssValue;
		}
	}

	void draw(Graph g, List<Cell> cells, Coordinate lower, Coordinate upper);
	
	void showOrHide();
	
	void setStyle(GridColorStyle cs);
	
	void lightPath(PathMatcher pm);
	
}
