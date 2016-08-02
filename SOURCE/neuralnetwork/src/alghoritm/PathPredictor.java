package alghoritm;

import java.util.List;

import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import network.interafaces.NetworkManager;

public abstract class PathPredictor {
	
	protected NetworkManager netManager;
	
	
	protected MapManager<?> mapManager;
	

	protected int simulationTime;

	
	public PathPredictor(NetworkManager netManager,MapManager<?> mapManager,int simulationTime){
		this.netManager = netManager;
		this.mapManager = mapManager;
		this.simulationTime = simulationTime;
	}
	
	
	/**
	 * Metodo per la predizione delle celle visitate (H)
	 * 
	 * @param idCell cella di partenza
	 * @param inDirection direzione di ingresso cella di partenza
	 * @param speed velocita media del path
	 * @return
	 */
	public abstract List<Cell> predict(Cell cell,int inDirection,double speed);
	
}
