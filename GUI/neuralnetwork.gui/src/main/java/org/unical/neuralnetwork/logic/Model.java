package org.unical.neuralnetwork.logic;

import org.unical.neuralnetwork.gui.grid.GridController;

import util.SimulationResult;

public class Model {
	
	private GridController gridController;
	private SimulationResult result;
	
	public Model(){
	}
	
	public Model(GridController gridController, SimulationResult result) {
		this.gridController = gridController;
		this.result = result;
	}

	public GridController getGridController() {
		return gridController;
	}

	public void setGridController(GridController gridController) {
		this.gridController = gridController;
	}

	public SimulationResult getResult() {
		return result;
	}

	public void setResult(SimulationResult result) {
		this.result = result;
	}
}
