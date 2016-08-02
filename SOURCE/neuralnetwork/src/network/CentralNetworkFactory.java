package network;

import geographic.interfaces.MapManager;
import network.interafaces.NetworkBuilder;
import network.interafaces.NetworkBuilder.NetworkClass;
import network.interafaces.NetworkBuilder.NetworkType;
import network.interafaces.NetworkFactory;
import network.interafaces.NetworkManager;

public class CentralNetworkFactory implements NetworkFactory{

	@Override
	public NetworkManager getNetworkManagerInstance(MapManager<?> manager, double maxError,
			int hiddenLayer, int neuronsPerLayer) {
		NetworkBuilder nn1 = new NetBuilder();
		nn1.setClass(NetworkClass.NN1).setType(NetworkType.CENTRAL)
			.setDirectionNumber(manager.getDirectionNumber()).setHiddenLayer(hiddenLayer)
			.setNeuronPerLayer(neuronsPerLayer);
		
		NetworkBuilder nn2 = new NetBuilder();
		nn2.setClass(NetworkClass.NN2).setDirectionNumber(manager.getDirectionNumber())
			.setHiddenLayer(hiddenLayer).setNeuronPerLayer(neuronsPerLayer);
		
		return new CentralNetworkManager(manager, nn1, nn2, maxError);
	}
	
	
}
