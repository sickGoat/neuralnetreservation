package org.unical.neuralnetwork.gui.builder.swingactions;

import java.util.List;

import org.graphstream.graph.Graph;
import org.unical.neuralnetwork.gui.builder.BuildingProgress;
import org.unical.neuralnetwork.gui.grid.GridController;
import geographic.Coordinate;
import geographic.interfaces.Cell;

public class GridDrawing extends UpdateProgress {

	private GridController drawer;
	private Coordinate[] bounds;
	private Graph graph;
	private List<Cell> cells;
	
	public GridDrawing(BuildingProgress bp, String mex, int value, Graph graph, GridController drawer, Coordinate[] bounds, List<Cell> cells) {
		super(bp, mex, value);
		this.drawer = drawer;
		this.bounds = bounds;
		this.graph = graph;
		this.cells = cells;
	}

	@Override
	protected void makeAction() {
		super.makeAction();
		drawer.draw(graph, cells, bounds[0], bounds[1]);
	}

}
