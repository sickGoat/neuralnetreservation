import java.io.File;

import geographic.Coordinate;
import geographic.GeoMap;
import network.interafaces.NetworkManager.PropagationType;
import util.Runner;
import util.SimulationParameter;
import util.SimulationResult;
import util.SimulationParameter.SimulationParamBuilder;
import util.SimulationRunner;

public class Main {
	
	public static void main(String[] args) {
		
		SimulationParameter params = getSimulationParameter(args);
		System.out.println("Simulation Parameter");
		System.out.println(params);
		File trainingFile = getFile(args[6]);
		File simFile = getFile(args[7]);
		SimulationRunner runner = new Runner(params);
		runner.init();
		runner.trainNetwork(trainingFile);
		SimulationResult result = runner.start(simFile);
		result.getResults().forEach(pm -> {
			StringBuilder sb = new StringBuilder();
			sb.append("\nPath id: "+pm.getPathId()+" \n");
			sb.append("Real Path: ");
			pm.getRealPath().forEach(c->sb.append(c.getId()+", "));
			sb.append(" numero celle: "+pm.getRealPath().size());
			sb.append("\n Predicted: ");
			pm.getPredicted().forEach(c->sb.append(c.getId()+", "));
			sb.append(" numero celle: "+pm.getPredicted().size());
			sb.append("\n Right predicted"+pm.getRightPredicted().size());
			sb.append("\n Waste predicted"+pm.getWaste().size());
			sb.append("\n Not predicted"+pm.getNotPredicted().size());
			System.out.println(sb.toString());
		});
		
		StringBuilder sb = new StringBuilder();
		System.out.println("");
		sb.append("Accuracy: "+result.getAccuracy()+" ");
		sb.append("Waste: "+result.getWaste());
		System.out.println(sb.toString());
	}
	
	private static SimulationParameter getSimulationParameter(String[]args){
		SimulationParamBuilder builder = new SimulationParamBuilder();
		builder.isDistributed(args[0].equalsIgnoreCase("t"))
			.setPropagationType(getpType(args[1]))
			.isRawData(true)
			.setCellSide(Double.parseDouble(args[2]))
			.setAlghThreshold(Double.parseDouble(args[3]))
			.setTrainingThreshold(Double.parseDouble(args[4]))
			.setH(Integer.parseInt(args[8]))
			.setMapDimension(new GeoMap(new Coordinate(0, 0), new Coordinate(Double.parseDouble(args[5]),Double.parseDouble(args[5]))));
		
		return builder.build();
	}
	
	private static PropagationType getpType(String p){
		switch (p) {
		case "B":
			return PropagationType.BACKPROPAGATION;
		case "R":
			return PropagationType.RESILIENT;
		case "Q":
			return PropagationType.QUICK;
		case "M":
			return PropagationType.MANHATTAN;
		default:
			return PropagationType.RESILIENT;
		}
	}
	
	private static File getFile(String path){
		File f = new File(path);
		if (!f.exists()){
			throw new RuntimeException("File "+path+" inesistente");
		}
		return f;
	}
}
