package org.unical.neuralnetwork.gui.builder.swingactions;

public abstract class AbstractSwingAction implements SwingAction {

	private boolean done = false;
	
	@Override
	public void make() {
		if(!done){
			makeAction();
			done = true;
		}
	}
	
	protected abstract void makeAction();

}
