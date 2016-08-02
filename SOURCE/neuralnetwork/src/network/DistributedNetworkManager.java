package network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.quick.QuickPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.arrayutil.NormalizedField;

import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import network.data.interfaces.DataProcessor;
import network.interafaces.NetworkBuilder;
import network.interafaces.NetworkManager;

public class DistributedNetworkManager extends NetworkManager{
	
	

	/*
	 * Questo Array lo restituiamo quando incorro
	 * in una cella non trainata, quindi non presente
	 * nella map
	 */
	private static double[] NOPREDICTION = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	
	private Map<Integer,BasicNetwork> nn1;
	
	private Map<Integer,BasicNetwork> nn2;
	
	private NormalizedField normalizedDir;
	
	
	
	protected DistributedNetworkManager(MapManager<?> manager, NetworkBuilder builderNN1, NetworkBuilder builderNN2,
			double maxError) {
		super(manager, maxError,builderNN1,builderNN2);
	}

	
	@Override
	public void createNetwork(NetworkBuilder nn1,NetworkBuilder nn2) {
		this.nn1 = new HashMap<>();
		this.nn2 = new HashMap<>();
		
		for(Cell c:mapManager.getCells()){
			this.nn1.put(c.getId(), nn1.build());
			this.nn2.put(c.getId(), nn2.build());
		}
		
	}

	
	@Override
	public void setDirectionNormalizer(NormalizedField dirNormalizer) {
		this.normalizedDir = dirNormalizer;
	}
	
	
	
	/**
	 * Devo splittare il dataset ricevuto
	 * e crearne uno per ogni cella.
	 * Usiamo questo approccio per evitare
	 * di riscrivere RawDataMap.
	 */
	@Override
	public void trainNN1(PropagationType type,DataProcessor data) {
		Map<Integer,MLDataSet> datasets = data.normalizeDistributedNN1();
		Propagation propagation = null;
		int epoch = 0;
		
		Set<Entry<Integer,BasicNetwork>> entrySet = nn1.entrySet();
		Iterator<Entry<Integer,BasicNetwork>> it = entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer,BasicNetwork> entry = it.next();
			MLDataSet dataset = null;
			//controllo se la map di DataSet contenga l'entry relativa all'id della cella
			if((dataset = (MLDataSet)datasets.get(entry.getKey())) == null){
				System.out.println(entry.getKey() +" has null dataset");
				it.remove();
				continue;
			}
			
			BasicNetwork network = entry.getValue();
			
			switch(type){
			case RESILIENT:
				propagation = new ResilientPropagation(network, dataset);
				break;
			
			case BACKPROPAGATION:
				propagation = new Backpropagation(network, dataset);
				break;
			
			case QUICK:
				propagation = new QuickPropagation(network, dataset);
				break;
			
			case MANHATTAN:
				propagation = new ManhattanPropagation(network, dataset, 0.00001);
				break;
			
			default:
				propagation = new ResilientPropagation(network, dataset);
			
			}
		
			epoch = trainNetwork(propagation);
			
			System.out.println("Training nn1: " + entry.getKey() +" finito in epoche: "+epoch+
					" con errore: "+propagation.getError());
		}
		
		//setto il normalizer della direzione
		setDirectionNormalizer(data.getDirNormalizer());
		
	}
	
	/**
	 * L'oggetto dataset non contiene
	 * come chiave un id di cella
	 * per cui non si siano raccolti
	 * i dati
	 */
	@Override
	public void trainNN2(PropagationType type,DataProcessor data) {
		Map<Integer,MLDataSet> datasets = data.normalizeNN2();
		Propagation propagation = null;
		int epoch = 0;
		Set<Entry<Integer,BasicNetwork>> entrySet = nn2.entrySet();
		Iterator<Entry<Integer,BasicNetwork>> it = entrySet.iterator();
		
		while(it.hasNext()){
			Entry<Integer,BasicNetwork> entry = it.next();
			MLDataSet dataset = null;
			if((dataset = datasets.get(entry.getKey()))==null){
				it.remove();
				continue;
			}
			
			BasicNetwork network = entry.getValue();
			
			switch(type){
			case RESILIENT:
				propagation = new ResilientPropagation(network, dataset);
				break;
			
			case BACKPROPAGATION:
				propagation = new Backpropagation(network, dataset);
				break;
			
			case QUICK:
				propagation = new QuickPropagation(network, dataset);
				break;
			
			case MANHATTAN:
				propagation = new ManhattanPropagation(network, dataset, 0.00001);
				break;
			
			default:
				propagation = new ResilientPropagation(network, dataset);
			}
			
			epoch = trainNetwork(propagation);
			
			System.out.println("Training nn2: " + entry.getKey() +" finito in epoche: "+epoch+
					" con errore: "+propagation.getError());
		}
	}
	
	/**
	 * Dall'oggetto input
	 * mi ricavo l'id della cella 
	 * a cui somministrarlo
	 * @param input
	 */
	@Override
	public double[] predictNN1(double[] input) {
		int idCell = (int)input[0];
		BasicNetwork network = nn1.get(idCell);
		if(network == null){
			return NOPREDICTION;
		}
		
		double[] netInput = { normalizedDir.normalize(input[1]) };
		MLData inputData = new BasicMLData(netInput);
		
		MLData output = network.compute(inputData);
		
		return output.getData();
	}

	
	@Override
	public double[] predictNN2(int idCell, int direction) {
		BasicNetwork network = nn2.get(idCell);
		
		//se la rete non si trova nella mappa
		//vuol dire che è stata rimossa in fase di training
		//perche non c'erano i dati necessari ad eseguirlo
		if(network == null){
			return NOPREDICTION;
		}
		
		//trasformo la direzione in un array
		MLData input = new BasicMLData(converter.convert(direction));
		//sottometto l'input alla rete
		MLData output = network.compute(input);
		
		return output.getData();
	}


	
}
