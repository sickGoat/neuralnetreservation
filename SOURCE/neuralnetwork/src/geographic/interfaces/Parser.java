package geographic.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Pattern;

import network.data.RawData;

public abstract class Parser {
	
	
	protected File tclFile;
	
	protected static String NEWNODE = "#Node";
	
	protected static String MOVEMENT = "#Path";
	
	protected static String REGEX = "[\\s]+";
	
	protected final Pattern pattern = Pattern.compile("\\((.*?)\\)",Pattern.DOTALL);
	
	protected BufferedReader reader;
	
	protected MapManager<?> mapManager;
	
	
	
	public Parser(File input,MapManager<?> manager) {
		this.mapManager = manager;
		this.tclFile = input;
		try {
			reader = new BufferedReader(new FileReader(tclFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Esegue la parserizzazione
	 */
	public abstract RawData parse();	
	
	
	
	/**
	 * La classe serve a trasformare il
	 * file letto in delle entry che servono
	 * al training per la rete neurale NN1
	 * 
	 * @author antonio
	 *
	 */
	public static class NN1Entry{
		
		/*
		 * id della cella
		 */
		public int idCell;
		
		
		/*
		 * direzione di partenza
		 * all'interno della cella 
		 */
		public int inDir;
		
		
		/*
		 * direzione di uscita dalla
		 * cella
		 */
		public int outDir;
		
		
		public NN1Entry(){}
		
		
		public NN1Entry(int idCell,int inDir,int outDir){
			this.idCell = idCell;
			this.inDir = inDir;
			this.outDir = outDir;
		}
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + idCell;
			result = prime * result + inDir;
			result = prime * result + outDir;
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
			NN1Entry other = (NN1Entry) obj;
			if (idCell != other.idCell)
				return false;
			if (inDir != other.inDir)
				return false;
			if (outDir != other.outDir)
				return false;
			return true;
		}

		
		@Override
		public String toString() {
			return idCell+","+inDir+","+outDir;
		}
	}
	
	
	public static class NN2Entry{
		
		
		/*L'id della cella non è un
		 * campo necessario al training
		 * ma per comodita lo manteniamo nella
		 * entry per capire a quale Cell appartiene
		 * la entry */
		public int idCell;
		
		
		/*
		 * direzione di ingresso nella cella
		 */
		public int inSide;
		

		/*
		 * direzione di uscita dalla cella
		 */
		public int outSide;
		
		
		
		public NN2Entry(){}
		
		
		public NN2Entry(int idCell,int inSide,int outSide){
			this.idCell = idCell;
			this.inSide = inSide;
			this.outSide = outSide;
		}
	
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + inSide;
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
			NN2Entry other = (NN2Entry) obj;
			if (inSide != other.inSide)
				return false;
			return true;
		}
		
		
		@Override
		public String toString() {
			return idCell+","+inSide+","+outSide;
		}
		
	}
	
	
	/**
	 * L'oggetto rappresenta il percorso
	 * di un utente
	 * @author antonio
	 *
	 */
	public static class Trace {
		
		/*
		 * id della traccia
		 */
		protected int idTrace;
		
		
		/*
		 * coordinata x di partenza
		 */
		public double xC;
		
		
		/*
		 * coordinata y di partenza
		 */
		public double yC;
		
		
		public Trace(int idTrace,double xC,double yC){
			this.idTrace = idTrace;
			this.xC = xC;
			this.yC = yC;
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + idTrace;
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
			Trace other = (Trace) obj;
			if (idTrace != other.idTrace)
				return false;
			return true;
		}


		@Override
		public String toString() {
			return "idNode = "+idTrace+" X = "+xC+" Y = "+yC;
		}
		
		
	}

	
}
