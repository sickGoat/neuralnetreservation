package network;

import network.interafaces.NetworkFactory;

public final class NetworkFactoryProvider {
	
	public enum FactoryType{CENTRALIZED,DISTRIBUTED}
	
	public static NetworkFactory getFactoryInstance(FactoryType fType){
		NetworkFactory factory = null;
		switch (fType) {
		case CENTRALIZED:
			factory = new CentralNetworkFactory();
			break;
		case DISTRIBUTED:
			factory = new DistributedNetworkFactory();
		}
		
		return factory;
	}

}
