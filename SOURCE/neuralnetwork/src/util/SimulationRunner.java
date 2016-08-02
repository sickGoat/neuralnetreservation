package util;

import java.io.File;

public interface SimulationRunner {
	
	/**
	 * Metodo inizializza tutte le tutte gli
	 * oggetti necessari alla simulazione
	 * 
	 */
	public void init();
	
	
	/**
	 * Metodo che serve ad avviare il training
	 * della rete
	 * 
	 * @param file contentente le tracce per il training
	 */
	public void trainNetwork(File f);
	
	
	/**
	 * Metodo che esegue la simulazione
	 * 
	 * @param file contentente le tracce con cui eseguire la simulazione
	 * 
	 * @return
	 */
	public SimulationResult start(File f);
	
	
}
