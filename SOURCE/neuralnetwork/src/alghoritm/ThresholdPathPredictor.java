package alghoritm;

import java.util.LinkedList;
import java.util.List;

import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import network.interafaces.NetworkManager;

public class ThresholdPathPredictor extends PathPredictor{

	private double threshold;


	public ThresholdPathPredictor(NetworkManager netManager,
			MapManager<?> mapManager, double threshold, int simulationTime) {
		super(netManager, mapManager,simulationTime);
		this.threshold = threshold;
		
	}

	
	@Override
	public List<Cell> predict(Cell cell, int inDirection, double speed) {
		// determino numero di celle probabilmente visitate
		List<Cell> predictedCells = new LinkedList<>();
		predictedCells.add(cell);
		int H = (int)speed;

		double[] inputPattern = { cell.getId(), inDirection };
		double[] prediction = netManager.predictNN1(inputPattern);
		
		for (int i = 0; i < prediction.length; i++) {
			if (prediction[i] >= threshold) {
				Cell next = next(mapManager,cell,i);
				predictedCells.add(next);
				recursivePrediction(H, next, mapManager.shiftDirection(i), predictedCells);
			}
		}
		
		return predictedCells;
	}

	private int visitedCells(double speed) {
		return (int) (speed * simulationTime / mapManager.getCell(0)
				.getDimension());
	}

	
	private void recursivePrediction(int count,Cell cell,int dir,List<Cell> toAppend){
		if(count == 0)
			return;
		
		//calcolo l'array con le predizioni per ogni lato
		double[] prediction = netManager.predictNN2(cell.getId(), dir);

		//itero per ogni direzione e lancio il processo di predizione per ogni
		//elemento maggiore della soglia
		for(int i = 0 ; i < prediction.length ; i++){
			if(prediction[i]>threshold){
				Cell nextCell = next(mapManager,cell,i);
				//controllo necessario a verificare che non sia finito fuori dalla mappa
				if(nextCell != null && nextCell!=cell){
					toAppend.add(nextCell);
					recursivePrediction(count-1, nextCell, mapManager.shiftDirection(i), toAppend);
				}
			}
		}
	}


	private Cell next(MapManager<?> mapManager,Cell cell, int direction) {
		return mapManager.nextCell(cell, direction);
	}

}
