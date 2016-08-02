package network;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import network.interafaces.NetworkBuilder;

public class NetBuilder implements NetworkBuilder {

	private NetworkType type;
	
	private NetworkClass c;
	
	private int hiddenNeurons;
	
	private int hiddenLayer;
	
	private int directionNumber;
	
	public NetBuilder() {}
	
	
	@Override
	public NetworkBuilder setDirectionNumber(int directionNumber) {
		this.directionNumber = directionNumber;
		return this;
	}
	
	@Override
	public NetworkBuilder setClass(NetworkClass c) {
		this.c = c;
		return this;
	}
	
	@Override
	public NetworkBuilder setType(NetworkType type) {
		this.type = type;
		return this;
	}

	@Override
	public NetworkBuilder setNeuronPerLayer(int hiddenNeurons) {
		this.hiddenNeurons = hiddenNeurons;
		return this;
	}

	@Override
	public NetworkBuilder setHiddenLayer(int hiddenLayers) {
		this.hiddenLayer = hiddenLayers;
		return this;
	}

	
	@Override
	public BasicNetwork build() {
		BasicNetwork network = new BasicNetwork();
		
		if( c == NetworkClass.NN1 ){
		
			if(type == NetworkType.DISTRIBUITED)
				network.addLayer(new BasicLayer(null, true, 1));
			else
				network.addLayer(new BasicLayer(null,true,2));

		}else{
			network.addLayer(new BasicLayer(null,false,6));
		}
		
		int count = 0;
		while(count < hiddenLayer){
			network.addLayer(new BasicLayer(new ActivationSigmoid(),true,hiddenNeurons));
			count++;
		}
		
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,directionNumber));
		network.getStructure().finalizeStructure();
		network.reset();
		
		return network;
	}

}
