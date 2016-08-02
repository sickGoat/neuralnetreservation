package network;

import geographic.interfaces.MapManager;
import network.interafaces.NetworkBuilder;
import network.interafaces.NetworkBuilder.NetworkClass;
import network.interafaces.NetworkFactory;
import network.interafaces.NetworkManager;

public class DistributedNetworkFactory 
							implements NetworkFactory{

	@Override
	public NetworkManager getNetworkManagerInstance(MapManager<?> manager,double maxError
			,int hiddenLayers,int neuronsPerLayer) {
		NetworkBuilder nn1 = new NetBuilder();
		nn1.setClass(NetworkBuilder.NetworkClass.NN1)
			.setType(NetworkBuilder.NetworkType.DISTRIBUITED)
			.setDirectionNumber(manager.getDirectionNumber())
			.setHiddenLayer(hiddenLayers).setNeuronPerLayer(neuronsPerLayer);
		
		NetworkBuilder nn2 = new NetBuilder();
		nn2.setClass(NetworkClass.NN2).setDirectionNumber(manager.getDirectionNumber())
			.setHiddenLayer(hiddenLayers).setNeuronPerLayer(neuronsPerLayer);
		
		return new DistributedNetworkManager(manager, nn1, nn2, maxError);
	}

}
