package network.data.interfaces;

import java.util.Map;

import org.encog.ml.data.MLDataSet;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import network.data.RawData;
import util.SideConverter;

/**
 * Super classe dei processatori di
 * dati.
 * Gli oggetti prendono l'output del
 * parser e lo strasformano in un
 * dataset per la rete
 *
 * @author antonio
 *
 */
public abstract class DataProcessor {
	
	
	
	/*
	 * Numero di celle sulla mappa
	 */
	protected int cellNumber;
	
	
	/*
	 * Numero di direzioni (questo
	 * dipende dal tipo di cella disgnato
	 * sulla mappa)
	 */
	protected int directionNumber;
	
	
	/*
	 * Normalizzatore dell'id di cella
	 */
	protected final NormalizedField normalizedId;
	

	/*
	 * Normalizzatore delle direzioni
	 */
	protected final NormalizedField normalizedDir;
	
	
	protected final SideConverter converter;

	
	
	
	public DataProcessor(RawData data,
			int cellNumber, int directionNumber) {
		this.cellNumber = cellNumber;
		this.directionNumber = directionNumber;
		
		normalizedId = new NormalizedField(NormalizationAction.Normalize,
				"idCell",cellNumber,0,1.0,-1.0);
		normalizedDir = new NormalizedField(NormalizationAction.Normalize,
				"dir",directionNumber,0,1.0,-1.0);
		
		converter = new SideConverter(directionNumber);
		
		init(data);

	}


	/**
	 * 
	 * Metodi di utilita 
	 * 
	 */
	
	/**
	 * Azzera gli elementi della marice
	 * 
	 * @param matrix
	 */
	protected void clear(double[][] matrix){
		for(int i = 0 ; i < matrix.length ; i++)
			for(int j = 0 ; j < matrix[0].length ; j++)
				matrix[i][j] = 0;
	}
	
	
	
	/**
	 * Azzera gli elementi dell'array
	 * @param array
	 */
	protected void clear(double[] array){
		for(int i = 0 ; i < array.length ; i++)
			array[i] = 0;
	}
	
	
	
	/**
	 * Metodo che calcola la statistica sulla riga
	 * della matrice 
	 * @param matrix
	 * @param index
	 * @return
	 */
	protected void normalize(double[][] matrix,int index){
		int k=0;
		double total = 0;
		
		for(;k<matrix[index].length;k++){
			total += matrix[index][k];
		}
		
		if(total == 0){
			//la cella non è mai stata attraversata
			for( k=0;k<matrix[index].length;k++){
				matrix[index][k] = 0.0;
			}
		}else{
			total = 1/total;
			for( k=0;k<matrix[index].length;k++){
				matrix[index][k] = matrix[index][k]*total;
			}
			
		}
		
	}
	
	
	
	/**
	 * Calcola la statistica sull'array
	 * @param array
	 */
	protected void normalize(double[]array){
		int k=0;
		double total = 0;
		
		for(;k<array.length;k++){
			total += array[k];
		}
		
		if(total == 0){
			//la cella non è mai stata attraversata
			for( k=0;k<array.length;k++){
				array[k] = (double)1/6;
			}
		}

		total = 1/total;
		
		for( k=0;k<array.length;k++){
			array[k] = array[k]*total;
		}
		
	}
	
	
	/**
	 * 
	 * Metodi pubblici e astratti
	 * 
	 */
	
	
	/**
	 * Ottengo il normalizzatore dei dati
	 * utilizzato per l'id della cella
	 * @return
	 */
	public NormalizedField getCellIdNormalizer(){
		return this.normalizedId;
	}
	
	
	/**
	 * Ottendo il normalizzatore dei
	 * dati utilizzato per la direzione
	 * @return
	 */
	public NormalizedField getDirNormalizer(){
		return this.normalizedDir;
	}
	
	
	
	/*
	 * Metodo che sara implementato dalle sottoclassi per inizializzare le proprie
	 * strutture dati 
	 */
	protected abstract void init(RawData data);
		
	
	
	/**
	 * Metodo utilizzato per calcolare il trainingset della rete NN1 a 
	 * partire dai dati di input
	 * @return
	 */
	public abstract MLDataSet normalizeNN1();
	
	
	
	/**
	 * Il metodo crea una mappa la cui chiave 
	 * è l'id di una cella e il valore è il
	 * dataset per il training di tale cella
	 *  
	 * @return
	 */
	public abstract Map<Integer,MLDataSet> normalizeDistributedNN1();
	
	
	/**
	 * Il metodo restituisce una map in cui la chiave rappresenta
	 * l'id della cella e il valore si riferisce al suo training set
	 * 
	 * @return
	 */
	public abstract Map<Integer, MLDataSet> normalizeNN2();
	

	
	
	
	
	
	/**
	 * Inner class necessario all'ordinamento
	 * della mappa di entry
	 * 
	 * @author antonio
	 *
	 */
	public class NN1Key {
		
		public int idCell;
		
		public int inDir;
		
		public NN1Key(){}
		
		public NN1Key(int idCell,int inDir){
			this.idCell = idCell;
			this.inDir = inDir;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + idCell;
			result = prime * result + inDir;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NN1Key other = (NN1Key) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (idCell != other.idCell)
				return false;
			if (inDir != other.inDir)
				return false;
			return true;
		}

		private DataProcessor getOuterType() {
			return DataProcessor.this;
		}

		
		
		
	}
	
	
	/**
	 * Key per la map contentene
	 * i dati di training della NN2
	 * @author antonio
	 *
	 */
	public class NN2Key {
		
		public int idCell;
		
		
		public NN2Key(){};
		
		
		public NN2Key(int idCell) {
			this.idCell = idCell;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + idCell;
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NN2Key other = (NN2Key) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (idCell != other.idCell)
				return false;
			return true;
		}


		private DataProcessor getOuterType() {
			return DataProcessor.this;
		}
		
	}
}