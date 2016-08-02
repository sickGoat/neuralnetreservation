package network.interafaces;

import geographic.interfaces.MapManager;

public interface NetworkFactory {
	
	NetworkManager getNetworkManagerInstance(MapManager<?> manager,double maxError,
			int hiddenLayer,int neuronsPerLayer);
	
}
