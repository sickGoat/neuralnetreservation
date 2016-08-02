package network.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

import geographic.interfaces.Parser;
import geographic.interfaces.Parser.NN1Entry;
import geographic.interfaces.Parser.NN2Entry;
import network.data.interfaces.DataProcessor;

public class StatsDataProcessor extends DataProcessor {

	/*
	 * Usa una list al posto dell Set perche potrei avere
	 * delle entry replicate e col set le perderei
	 */
	private Map<NN1Key,List<Parser.NN1Entry>> nn1Map;
	
	
	/*
	 * Usa una list al posto dell Set perche potrei avere
	 * delle entry replicate e col set le perderei
	 */
	private Map<NN2Key,List<Parser.NN2Entry>> nn2Map;
	
	
	
	
	public StatsDataProcessor(RawData data, int cellNumber, int directionNumber) {
		super(data, cellNumber, directionNumber);
	}

	
	
	
	@Override
	protected void init(RawData data) {
		List<NN1Entry> entriesNN1 = data.getEntriesNN1();
		List<NN2Entry> entriesNN2 = data.getEntriesNN2();
		
		nn1Map = new HashMap<>();
		nn2Map = new HashMap<>();
		
		//inserisco elementi nella map di NN1
		entriesNN1.forEach(entry -> {
			NN1Key key = new NN1Key();
			key.idCell = entry.idCell;
			key.inDir = entry.inDir;
			List<Parser.NN1Entry> set = null;
			if((set=nn1Map.get(key))==null){
				set = new LinkedList<>();
				set.add(entry);
				nn1Map.put(key, set);
			}else{
				set.add(entry);
			}
		});
				
		//inserisco elementi nella map di NN2
		entriesNN2.forEach(entry -> {
			NN2Key key2 = new NN2Key();
			key2.idCell = entry.idCell;
			List<Parser.NN2Entry> list = null;
			if((list=nn2Map.get(key2))==null){
				list = new LinkedList<>();
				list.add(entry);
				nn2Map.put(key2, list);
			}else{
				list.add(entry);
			}
		});
				
	}

			

	@Override
	public MLDataSet normalizeNN1() {
		double[][] input = new double[cellNumber*directionNumber][2];
		double[][] output = new double[cellNumber*directionNumber][directionNumber];
		double[] directions = new double[6];
		
		int i=0,j=0;
		for(i=0; i<cellNumber; i++){
			for(j=0; j < directionNumber ; j++){
				//genero una chiave con id cella e direzione (indicata da j)
				NN1Key key = new NN1Key(i,j);
				if(nn1Map.containsKey(key)){
					
					//normalizzo l'input e genero i dati
					input[key.idCell*directionNumber+key.inDir][0] = normalizedId.normalize(key.idCell);
					input[key.idCell*directionNumber+key.inDir][1] = normalizedDir.normalize(key.inDir);
				
					List<Parser.NN1Entry> entries = nn1Map.get(key);
					for(Parser.NN1Entry entry : entries){
						directions[entry.outDir]++;
					}
					
					//calcolo la statistica sull'array
					normalize(directions);
					
					//i+j è punta alla riga data dall'id della cella 
					//e la direzione
					for( int k = 0 ; k < directions.length ; k++ ){
						output[i*directionNumber+j][k] = directions[k];
					}
				}else{
					//costruisco input ad hoc
					input[i*directionNumber+j][0] = normalizedId.normalize(i);
					input[i*directionNumber+j][1] = normalizedDir.normalize(j);
					
					for(int k = 0 ; k < directionNumber; k++)
						output[i*directionNumber+j][k] = 0.0;
				}
				//resetto il vettore
				clear(directions);
			}
		}

		return new BasicMLDataSet(input, output);
	}
	
	
	
	/**
	 * Metodo utilizzato per creare il dataset per l'addestramento
	 * della rete neurale distribuita.
	 * Il meccanismo è lo stesso del metodo normalizeNN1() con
	 * l'unica differenza che i dati vengono splittati
	 * per associare ogni dataset a una cella
	 */
	@Override
	public Map<Integer, MLDataSet> normalizeDistributedNN1() {
		Map<Integer,MLDataSet> datasets = new HashMap<>();
		double[][] outputMat = new double[directionNumber][directionNumber];
		
		for(NN1Key key : nn1Map.keySet()){
			List<Parser.NN1Entry> entries = nn1Map.get(key);
			for(Parser.NN1Entry entry : entries){
				outputMat[entry.inDir][entry.outDir]++;
			}
			
			//per ogni riga della matrice eseguo la normalizzazione
			for(int k = 0 ; k < outputMat.length ; k++ ){
				normalize(outputMat,k);
			}
			
			//double[][] noZero = dropZero(outputMat);
			datasets.put(key.idCell, new BasicMLDataSet(createListMLDataPair(outputMat)));
			
			//resetto la matrice
			clear(outputMat);
		}
			
		return datasets;
	}
	
	
	
	/**
	 * Il metodo restiuisce una map in cui la chiave
	 * è l'id della cella a cui il dataset si riferisce
	 * @return
	 */
	@Override
	public Map<Integer, MLDataSet> normalizeNN2() {
		Map<Integer,MLDataSet> dataSets = new HashMap<>();

		int i,k;
		//la riga della matrice indica la direzione di ingresso
		//le colonne indicano quella di uscita
		double[][] outputMat = new double[directionNumber][directionNumber];
		
		NN2Key key = new NN2Key();

		for( i = 0 ; i < cellNumber ; i++ ){
			//pair relativi alla lecca i
			key.idCell = i;

			if(nn2Map.containsKey(key)){
				List<Parser.NN2Entry> entries = nn2Map.get(key);
				
				//calcolo la matrice
				entries.forEach(entry -> {
					outputMat[entry.inSide][entry.outSide]++;
				});

				//per ogni riga della matrice eseguo la normalizzazione
				for( k = 0 ; k < outputMat.length ; k++ ){
					normalize(outputMat,k);
				}
			
				dataSets.put(new Integer(i), new BasicMLDataSet(NN2MLDataPair.createListPair(outputMat,directionNumber)));	

			}
			
			clear(outputMat);
		}
		
		return dataSets;
	}
	
	
	/**
	 * Il metodo crea una lista di MLDataPair a partire da una matrice
	 * che contiene i dati statisticizzati
	 * il numero di riga corrisponde a un lato di ingresso mentre 
	 * le colonne corrispondono al lato di uscita
	 * @param outputMat
	 * @return
	 */
	private List<MLDataPair> createListMLDataPair(double[][] outputMat){
		List<MLDataPair> list = new LinkedList<>();
		boolean allZero = true;
		for( int i = 0 ; i < outputMat.length ; i++){
			for(int j = 0 ; j < outputMat[0].length && allZero; j++)
				if(outputMat[i][j]!=0)
					allZero = false;
			if(!allZero){
				MLData input = new BasicMLData(new double[]{normalizedDir.normalize(i)});
				MLData output = new BasicMLData(extractArray(outputMat,i));
				list.add(new BasicMLDataPair(input,output));
			}
			allZero = true;
		}
		
		return list;
	}
	
	
	/**
	 * Il metodo estrae un vettore dalla matrice 
	 * @param matrix
	 * @param index
	 * @return
	 */
	private double[] extractArray(double[][] matrix,int index){
		double[] extracted = new double[matrix[0].length];
		for(int i = 0 ; i < matrix[0].length ; i++ )
			extracted[i] = matrix[index][i];
		
		return extracted;
	}
	
}
