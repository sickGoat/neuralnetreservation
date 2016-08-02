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

public class CentralNetworkManager extends NetworkManager {
	
	
	/*
	 * Questo Array lo restituiamo quando incorro
	 * in una cella non trainata, quindi non presente
	 * nella map
	 */
	private static double[] NOPREDICTION = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
	
	
	public Map<Integer,BasicNetwork> nn2;
	
	public BasicNetwork nn1;
	
	private NormalizedField normalizedId;
	
	private NormalizedField normalizedDir;
	
	
	
	protected CentralNetworkManager(MapManager<?> manager,
			NetworkBuilder builderNN1,NetworkBuilder builderNN2,double maxError) {
		super(manager,maxError,builderNN1,builderNN2);
		//lanciare eccezione se il builder della NN1 non è distribuito
	}
	
	
	@Override
	public void createNetwork(NetworkBuilder nn1,NetworkBuilder nn2) {
		this.nn1 = nn1.build();
		this.nn2 = new HashMap<>();
		for(Cell c:mapManager.getCells()){
			this.nn2.put(c.getId(), nn2.build());
		}
	}
	

	@Override
	protected void setDirectionNormalizer(NormalizedField dirNormalizer) {
		this.normalizedDir = dirNormalizer;
	}
	
	@Override
	protected void setIdCellNormalizer(NormalizedField idNormalizer) {
		this.normalizedId = idNormalizer;
	}
	
	
	@Override
	public void trainNN1(PropagationType type,DataProcessor data) {
		MLDataSet dataset = data.normalizeNN1();
		Propagation propagation = null;
		switch(type){
		case RESILIENT:
			propagation = new ResilientPropagation(nn1, dataset);
			break;
		case BACKPROPAGATION:
			propagation = new Backpropagation(nn1, dataset);
			break;
		case MANHATTAN:
			propagation = new ManhattanPropagation(nn1, dataset, 0.00001);
			break;
		case QUICK:
			propagation = new QuickPropagation(nn1, dataset, 2.0);
			break;
			
		default:
			propagation = new ResilientPropagation(nn1, dataset);
			
		}
		
		int epoch = trainNetwork(propagation);
		System.out.println("Training nn1 finito in epoche: "+epoch+
				" con errore: "+propagation.getError());
		
		//setto i normalizer che mi serviranno poi in fase di predict
		this.normalizedDir = data.getDirNormalizer();
		this.normalizedId = data.getCellIdNormalizer();
	}


	@Override
	public void trainNN2(PropagationType type,DataProcessor data) {
		Map<Integer,MLDataSet> datasets = data.normalizeNN2();
		Propagation propagation = null;
		int count = 0;
		int epoch = 0;
		Set<Entry<Integer, BasicNetwork>> entrySet = nn2.entrySet();
		Iterator<Entry<Integer,BasicNetwork>> it = entrySet.iterator();
		while(it.hasNext()){
			Entry<Integer,BasicNetwork> entry = it.next();
			MLDataSet dataset = null;
			if((dataset = (MLDataSet)datasets.get(entry.getKey()))==null){
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
			System.out.println("Training nn2 ="+entry.getKey() +"finito in epoche: "+epoch+
					" con errore: "+propagation.getError());
		}
	}

	
	/**
	 * Prendo l'array di input e lo rasformo
	 * in un MLData, previa normalizzazione
	 * dei dati
	 */
	@Override
	public double[] predictNN1(double[] input) {
		//devo denormalizzare l'output
		
		input[0] = normalizedId.normalize(input[0]);
		input[1] = normalizedDir.normalize(input[1]);
		
		MLData inputData = new BasicMLData(input);
		MLData output = nn1.compute(inputData);

		return output.getData();
	}

	
	/**
	 * Prendo l'array di input e lo trasformo
	 * in un oggetto MLData. In questo caso non devo normalizzare
	 * ma devo solo richiamare il converter
	 */
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
