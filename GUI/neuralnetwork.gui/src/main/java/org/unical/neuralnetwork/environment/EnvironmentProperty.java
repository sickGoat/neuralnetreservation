package org.unical.neuralnetwork.environment;

public enum EnvironmentProperty {
	
	HOME_PATH("nn.home"),
	PLAIN_PATH("nn.plain"),
	DGS_PATH("nn.dgs"),
	LOG_PATH("nn.log"),
	NETCONVERT_PATH("nn.nc");
	
	
	private final String propertyValue;
	
	private EnvironmentProperty(String value){
		propertyValue = value;
	}
	
	public String getValue(){
		return propertyValue;
	}
}
