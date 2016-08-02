package network.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;

import geographic.interfaces.Parser;
import geographic.interfaces.Parser.NN1Entry;
import geographic.interfaces.Parser.NN2Entry;
import network.data.interfaces.DataProcessor;

/**
 * Questa classe non esegue la statistica
 * sui dati
 * @author antonio
 *
 */
public class BasicDataProcessor extends DataProcessor{
	
	
	private Map<NN1Key,List<Parser.NN1Entry>> nn1Map;
	
	
	private Map<NN2Key,List<Parser.NN2Entry>> nn2Map;


	
	
	public BasicDataProcessor(RawData data, int cellNumber, int directionNumber) {
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

	
	/**
	 * Leggo i dati e li metto senza eseguire controlli
	 * 
	 */
	@Override
	public MLDataSet normalizeNN1() {
		MLDataSet dataset = new BasicMLDataSet();
		double[] input = new double[2];
		for(NN1Key key : nn1Map.keySet()){
			List<Parser.NN1Entry> entries = nn1Map.get(key);
			//itero per ogni entry e genero il dataPair
			for(NN1Entry entry : entries){

				//normalizzo i dati
				input[0] = normalizedId.normalize(entry.idCell);
				input[1] = normalizedDir.normalize(entry.inDir);

				//creo gli oggetti di input e di output
				MLData inputData = new BasicMLData(input);
				MLData outputData = new BasicMLData(converter.convert(entry.outDir));
				
				dataset.add(new BasicMLDataPair(inputData, outputData));
			}
			
		}
		
		return dataset;
	}


	/**
	 * Splitto il dataset per ogni id di cella
	 */
	@Override
	public Map<Integer, MLDataSet> normalizeDistributedNN1() {
		Map<Integer,MLDataSet> datasets = new HashMap<>();
		
		//array che conterrà la direzione di ingresso
		double[] inputArray = new double[1];
		
		for(NN1Key key : nn1Map.keySet()){
			List<Parser.NN1Entry> entries = nn1Map.get(key);
			MLDataSet cellDataset = null;
			if((cellDataset=datasets.get(key.idCell))==null){
				cellDataset = new BasicMLDataSet();
			}
			
			for(Parser.NN1Entry entry : entries){
				inputArray[0] = normalizedDir.normalize(entry.inDir);
				MLData input = new BasicMLData(inputArray);
				MLData output = new BasicMLData(converter.convert(entry.outDir));
				
				cellDataset.add(new BasicMLDataPair(input,output));
			}
			
			datasets.put(key.idCell, cellDataset);
		}
		
		return datasets;
	}

	
	/**
	 * Creo il dataset per la NN2 senza eseguire
	 * controlli
	 */
	@Override
	public Map<Integer, MLDataSet> normalizeNN2() {
		Map<Integer,MLDataSet> dataSets = new HashMap<>();
		
		int i;
		NN2Key key = new NN2Key();

		for( i = 0 ; i < cellNumber ; i++ ){
			//pair relativi alla cella i
			key.idCell = i;

			if(nn2Map.containsKey(key)){
				List<Parser.NN2Entry> entries = nn2Map.get(key);

				//inizializzo un nuovo dataset
				MLDataSet cellDataset = new BasicMLDataSet();
				
				
				for(NN2Entry entry : entries){
					cellDataset.add(NN2MLDataPair.createPair(entry.inSide, entry.outSide, directionNumber));
				}

				//inserisco il dataset delle cella nella mappa
				dataSets.put(new Integer(i), cellDataset);	
			}
			
		}
		
		return dataSets;
	}
	
}
