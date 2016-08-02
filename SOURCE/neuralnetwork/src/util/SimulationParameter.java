package util;

import geographic.Coordinate;
import geographic.GeoMap;
import network.interafaces.NetworkManager.PropagationType;

public class SimulationParameter {
	
	protected final boolean isDistributed;
	
	protected final boolean isRawData;
	
	protected final PropagationType pType;
	
	protected final double trainingThreshold;
	
	protected final double alghThreshold;
	
	protected final double cellSide;
	
	protected final int H;
	
	protected final GeoMap mapDimension;
	
	private SimulationParameter(SimulationParamBuilder params){
		this.isDistributed = params.isDistributed;
		this.isRawData = params.isRawData;
		this.pType = params.pType==null?PropagationType.RESILIENT:params.pType;
		this.trainingThreshold = params.alghThreshold==0?0.1:params.trainingThreshold;
		this.alghThreshold = params.alghThreshold==0?0.5:params.alghThreshold;
		this.cellSide = params.cellSide==0?10:params.cellSide;
		this.H = params.H==0?8:params.H;
		this.mapDimension = params.mapDimension==null?new GeoMap(new Coordinate(0,0), new Coordinate(600,600)):params.mapDimension;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Approccio: ");
		if (this.isDistributed)
			sb.append("Distribuito\n");
		else
			sb.append("Centralizzato\n");
		
		sb.append("Propagazione: ");
		String prop = null;
		switch (this.pType) {
		case RESILIENT:
			prop = "Resilient \n";
			break;
		case BACKPROPAGATION:
			prop = "Backpropagation \n";
			break;
		case QUICK:
			prop = "Quick \n";
			break;
		case MANHATTAN:
			prop = "Manhattan \n";
		}
		sb.append(prop);
		
		sb.append("Training Threshold: "+this.trainingThreshold+ "\n");
		sb.append("Algorithm Threshold: "+this.alghThreshold+" \n");
		sb.append("H: "+this.H+"\n");
		sb.append("Dimensione cella: "+this.cellSide+"\n");
		sb.append("Dimensione Mappa: "+this.mapDimension+"\n");
		
		return sb.toString();
	}
	
	public static class SimulationParamBuilder{
		
		private boolean isDistributed;
		
		private boolean isRawData;
		
		private PropagationType pType;
		
		private double trainingThreshold;
		
		private double alghThreshold;
		
		private double cellSide;
		
		private int H;
		
		private GeoMap mapDimension;
		
		public SimulationParamBuilder(){}
		
		public SimulationParamBuilder isDistributed(boolean distributed){
			this.isDistributed = distributed;
			return this;
		}
		
		public SimulationParamBuilder isRawData(boolean isRawData){
			this.isRawData = isRawData;
			return this;
		}
		
		public SimulationParamBuilder setPropagationType(PropagationType pType){
			this.pType = pType;
			return this;
		}
		
		public SimulationParamBuilder setTrainingThreshold(double trainingThreshold){
			this.trainingThreshold = trainingThreshold;
			return this;
		}
		
		public SimulationParamBuilder setAlghThreshold(double threshold){
			this.alghThreshold = threshold;
			return this;
		}
		
		public SimulationParamBuilder setMapDimension(GeoMap dimension){
			this.mapDimension = dimension;
			return this;
		}
		
		public SimulationParamBuilder setH(int H){
			this.H = H;
			return this;
		}
		
		public SimulationParamBuilder setCellSide(double cellSide){
			this.cellSide = cellSide;
			return this;
		}
		
		public SimulationParamBuilder setRawData(boolean isRawData){
			this.isRawData = isRawData;
			return this;
		}
		
		public SimulationParameter build(){
			return new SimulationParameter(this);
		}
		
	}
	
}
