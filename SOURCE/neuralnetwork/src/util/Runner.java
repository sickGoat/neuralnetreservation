package util;

import java.io.File;
import java.util.List;

import alghoritm.Path;
import alghoritm.PathPredictor;
import alghoritm.ThresholdPathPredictor;
import geographic.GeoMap;
import geographic.HexMapManager;
import geographic.HexagonCell;
import geographic.ParserImpl;
import geographic.TrackParserImpl;
import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import geographic.interfaces.Parser;
import network.NetworkFactoryProvider;
import network.NetworkFactoryProvider.FactoryType;
import network.data.BasicDataProcessor;
import network.data.RawData;
import network.data.StatsDataProcessor;
import network.data.interfaces.DataProcessor;
import network.interafaces.NetworkFactory;
import network.interafaces.NetworkManager;

public class Runner implements SimulationRunner {
	
	private final SimulationParameter params;
	
	private PathPredictor alghoritm;
	
	private NetworkManager netManager;
	
	public MapManager<HexagonCell> mapManager;
	
	public Runner(SimulationParameter params){
		this.params = params;
	}
	
	
	@Override
	public void init() {
		GeoMap map = params.mapDimension;
		mapManager = HexMapManager.getInstance(map, params.cellSide);
		NetworkFactory factory = null;
		if(params.isDistributed)
			factory = NetworkFactoryProvider.getFactoryInstance(FactoryType.DISTRIBUTED);
		else
			factory = NetworkFactoryProvider.getFactoryInstance(FactoryType.CENTRALIZED);
		
		netManager = factory.getNetworkManagerInstance(mapManager, params.trainingThreshold, 1, 20);
		alghoritm = new ThresholdPathPredictor(netManager, mapManager, params.alghThreshold, params.H);
	}
	

	@Override
	public void trainNetwork(File f) {
		Parser parser = new ParserImpl(f, mapManager);
		RawData data = parser.parse();
		final DataProcessor processor;
		if(params.isRawData)
			processor = new BasicDataProcessor(data, mapManager.getCellNumber(), 
					mapManager.getDirectionNumber());
		else
			processor = new StatsDataProcessor(data, mapManager.getCellNumber(), mapManager.getDirectionNumber());
		
		//train network
		netManager.trainNN1(params.pType, processor);
		System.out.println("Training NN1 is ended");
		netManager.trainNN2(params.pType, processor);
		System.out.println("Training is ended");
		
	}

	@Override
	public SimulationResult start(File f) {
		List<Path> paths = new TrackParserImpl(f, mapManager).parse();
		final SimulationResult result = new SimulationResult(mapManager.getCellNumber());
		paths.forEach(p -> {
			List<Cell> predicted = alghoritm.predict(p.getCells().get(0), p.getInDirection(), params.H);
			
			PathMatcher pm = new PathMatcher();
			pm.setPathId(p.getId());
			pm.setPredicted(predicted);
			pm.setRealPath(p.getCells());
			
			result.addPathMatcher(pm);
		});
		
		return result;
	}

}
