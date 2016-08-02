package org.unical.neuralnetwork.gui.builder.swingactions;

import org.unical.neuralnetwork.gui.builder.BuildingProgress;

public class UpdateProgress extends AbstractSwingAction{

	private String string;
	private int value;
	private BuildingProgress bp;
	
	public UpdateProgress(BuildingProgress bp, String string, int value) {
		this.string = string;
		this.value = value;
		this.bp = bp;
	}

	@Override
	protected void makeAction() {
		bp.update(string, value);
	}

}
