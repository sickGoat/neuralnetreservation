package org.unical.neuralnetwork.environment;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	private static final Map<String, String> props = new HashMap<>();

	public static void bootstrap() throws EnvironmentException{
		String homepath = System.getProperty("user.home");
		File[] files = new File[5];
		files[0] = new File(homepath + "/NeuralNetwork");
		makeFolder(homepath, files[0]);
		files[1] = new File(files[0].getAbsolutePath() + "/PlainModel");
		makeFolder(files[0].getAbsolutePath(), files[1]);
		files[2] = new File(files[0].getAbsolutePath() + "/DGSModel");
		makeFolder(files[0].getAbsolutePath(), files[2]);
		files[3] = new File(files[0].getAbsolutePath() + "/Log");
		makeFolder(files[0].getAbsolutePath(), files[3]);
		files[4] = new File("/opt/local/bin/netconvert");
		if(!files[4].exists()){
			throw new EnvironmentException("SUMO.netconvert non trovato");
		}
		for(EnvironmentProperty ep : EnvironmentProperty.values()){
			props.put(ep.getValue(), files[ep.ordinal()].getAbsolutePath());
		}
	}
	
	public static void setSystemOutput(PrintStream ps){
		System.setOut(ps);
		System.setErr(ps);
	}
	
	public static String getProperty(EnvironmentProperty ep){
		return props.get(ep.getValue());
	}
	
	private static void makeFolder(String path, File folder) {
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
			System.out.println(String.format("Directory creata in:%s", folder.getAbsolutePath()));
		}
	}
}
