package network.interafaces;

import org.encog.neural.networks.BasicNetwork;

public interface NetworkBuilder {
	
	
	/**
	 * In base al valore di questa enum
	 * decidiamo se avere due input di ingresso
	 * o soltanto uno
	 * @author antonio
	 *
	 */
	public enum NetworkType{
		
		DISTRIBUITED,
		
		CENTRAL
	}
	
	
	/**
	 * Serve a distinguere quale rete neurale
	 * costruire
	 * @author antonio
	 *
	 */
	public enum NetworkClass{

		NN1,
		
		NN2
	}
	
	
	public NetworkBuilder setDirectionNumber(int directionNumber);
	
	public NetworkBuilder setClass(NetworkClass c);
	
	public NetworkBuilder setType(NetworkType type);
	
	public NetworkBuilder setNeuronPerLayer(int hiddenNeurons);
	
	public NetworkBuilder setHiddenLayer(int hiddenLayers);
	
	public BasicNetwork build();
}
