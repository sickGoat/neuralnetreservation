package network.interafaces;

import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.util.arrayutil.NormalizedField;

import geographic.interfaces.MapManager;
import network.data.interfaces.DataProcessor;
import util.SideConverter;

public abstract class NetworkManager{
	
	
	public static enum PropagationType{
		
		RESILIENT,
		
		BACKPROPAGATION,
		
		QUICK,
		
		MANHATTAN
	}
	
	
	protected MapManager<?> mapManager;
	

	protected double maxError;
	
	
	
	/**
	 * Il converter serve a convertire l'input
	 * ricevuto dall'algoritmo (un intero)
	 * nell'array corrispondente al lato
	 */
	protected final SideConverter converter;
	
	
	protected NetworkManager(MapManager<?> manager, double maxError,
			NetworkBuilder nn1,NetworkBuilder nn2) {
		super();
		this.mapManager = manager;
		this.maxError = maxError;
		converter = new SideConverter(manager.getDirectionNumber());
		createNetwork(nn1, nn2);
	}
	
	
	
	/**
	 * Setto il normalizer da utilizzare in fase di prediction
	 * @param dirNormalizer
	 */
	protected abstract void setDirectionNormalizer(NormalizedField dirNormalizer);
	
	/**
	 * Il metdo lo implemento nella super classe perche 
	 * la sottoclasse distribuita non ha bisogno di questo normilizer
	 * @param idNormalizer
	 */
	protected void setIdCellNormalizer(NormalizedField idNormalizer){}
	
	/**
	 * Crea le varie neural network
	 */
	public abstract void createNetwork(NetworkBuilder nn1,NetworkBuilder nn2);

	/**
	 * Esegue il training della prima Network
	 * @param dataset
	 */
	public abstract void trainNN1(PropagationType propagation,DataProcessor data);
	
	
	/**
	 * Esegue il training sulla seconda Network
	 * @param datasets
	 */
	public abstract void trainNN2(PropagationType propagation,DataProcessor data);
	
	
	/**
	 * Esegue compute sulla prima Network.
	 * Questo metodo verrà implementato
	 * solo dalla sottoclasse che lavora
	 * con la NN1 centrallizzata
	 * @param input
	 * @return
	 */
	public abstract double[] predictNN1(double[] input);	
	
	
	/**
	 * Esegue compute sulla seconda Network
	 * @param idCell
	 * @param direction : intero corrispondente alla direzione di ingresso
	 * @return
	 */
	public abstract double[] predictNN2(int idCell,int direction);
	
	
	
	
	protected int trainNetwork(Propagation propagation){
		int epoch = 0;
		do{
			propagation.iteration();
			epoch++;
		}while(propagation.getError() > maxError && epoch<10000);
		
		propagation.finishTraining();
		
		return epoch;
	}
	
	
}
