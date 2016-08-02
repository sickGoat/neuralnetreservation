package geographic.interfaces;

import geographic.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import alghoritm.Path;

public abstract class TrackParser {
	
	protected File tclFile;
	
	protected static String NEWNODE = "#Node";
	
	protected static String MOVEMENT = "#Path";
	
	protected static String REGEX = "[\\s]+";
	
	protected final Pattern pattern = Pattern.compile("\\((.*?)\\)",Pattern.DOTALL);
	
	protected BufferedReader reader;
	
	protected MapManager<?> mapManager;
	
	
	
	public TrackParser(File input,MapManager<?> manager) {
		this.mapManager = manager;
		this.tclFile = input;
		try {
			reader = new BufferedReader(new FileReader(tclFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Leggo il file e genero le tracce di test
	 * @return
	 */
	public abstract List<Path> parse();
	
	
	public static class Track {
		
		
		public final int id;
		
		public List<Cell> visitedCells = new LinkedList<>();
		
		public Coordinate firstCoordinate;
		
		public double speed;
		
		public int direction = -1;
		
		private int sampleCount = 0;
		
		public Track(int id){
			this.id = id;
		}
		
		
		public void addCell(Cell c){
			visitedCells.add(c);
		}
		
		public void newSample(double speed){
			this.speed += speed;
			sampleCount++;
		}
		
		public double getAvgSpeed(){
			return (double) speed/sampleCount;
		}
		
		public int getSampleCount() {
			return sampleCount;
		}
		
		public void setDirection(int direction) {
			this.direction = direction;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
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
			Track other = (Track) obj;
			if (id != other.id)
				return false;
			return true;
		}
		
	}
	
}
