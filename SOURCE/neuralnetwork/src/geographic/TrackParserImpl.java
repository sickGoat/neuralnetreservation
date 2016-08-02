package geographic;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import alghoritm.Path;
import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import geographic.interfaces.TrackParser;

/**
 * Parser che serve a leggere il file con i percorsi
 * che saranno utilizzati per eseguire le simulazioni.
 * 
 * @author antonio
 *
 * @param <T>
 */
public class TrackParserImpl extends TrackParser  {

	
	
	public TrackParserImpl(File input,MapManager<?> manager) {
		super(input,manager);
	}

	@Override
	public List<Path> parse() {
		try{
			LinkedList<Track> tracks = new LinkedList<TrackParser.Track>();
			String s;
			String[] splitted;
			Matcher matcher;
			Cell previous,actual;
			Coordinate prevCoord,actualCoord;
			double actualSpeed;
			
			while(true){
				s = reader.readLine();
				if(s == null){
					break;
				}
				if(s.startsWith(NEWNODE)){
					//sono all'inizio del file in sono definiti
					//i path
					tracks.add(getTrack(s));
					
				}else if(s.startsWith(MOVEMENT)){
					//leggo il numero del nodo e ricavo l'oggetto track
					splitted = s.split(REGEX);
					matcher = pattern.matcher(splitted[1]);
					int idTrack =-1;
					if(matcher.find()){
						idTrack = Integer.parseInt(matcher.group().replaceAll("[()]",""));
					}
					
					Track actualTrack = tracks.removeFirst();
					if(actualTrack.id != idTrack){
						throw new RuntimeException("Id diversi");
					}
					
					//comincio a leggere i record della traccia
					prevCoord = actualTrack.firstCoordinate;
					previous = actualTrack.visitedCells.get(0);
					while(true){
						s = reader.readLine();
						if(s==null||s.length()==0){
							break;
						}
						//ricavo le coordinate e la cella della linea attuale
						actualCoord = getCoordinate(s);
						actual = mapManager.getCell(actualCoord);
						
						//ricavo la velocita dalla stringa
						actualSpeed = getSpeed(s);
						actualTrack.newSample(actualSpeed);
						//ricavo la direzione 
						if(actualTrack.direction == -1)
							actualTrack.direction = mapManager.getDirection(prevCoord, actualCoord);
						//controllo se lo spostamento mi abbia fatto cambiare cella
						if(previous!=actual){
							actualTrack.addCell(actual);
							previous = actual;
						}
					}//while 2
					
					tracks.addLast(actualTrack);
				}//else
			}//while 1
			
			return Path.createPaths(tracks);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("ERRORE: Nessun Path Ritornato a causa di un'eccezione");
		return null;
		
	}	
	
	private Track getTrack(String s) throws IOException{
		String[] splitted;
		splitted = s.split(REGEX);
		int idTrace = Integer.parseInt(splitted[1]);
		s = reader.readLine();
		splitted = s.split(REGEX);
		double x = Double.parseDouble(splitted[3]);
		s = reader.readLine();
		splitted = s.split(REGEX);
		double y = Double.parseDouble(splitted[3]);
		Coordinate c = new Coordinate(x, y);
		Cell cell = mapManager.getCell(c);
		Track track = new Track(idTrace);
		track.firstCoordinate = c;
		track.addCell(cell);
		
		return track;
	}
	
	/**
	 * Riceve una stringa e restituisce un oggetto
	 * di tipo coordinate
	 * 
	 * @param s
	 * @return
	 */
	private Coordinate getCoordinate(String s){
		String[] splitted = s.split(REGEX);
		double xC = Double.parseDouble(splitted[5]);
		double yC = Double.parseDouble(splitted[6]);
		
		return new Coordinate(xC,yC);
	}

	/**
	 * Riceve una stringa e restituisce e
	 * restituisce la velocità
	 * 
	 * @param s
	 * @return
	 */
	private double getSpeed(String s){
		String[] splitted = s.split(REGEX);
		return Double.parseDouble(splitted[7].substring(0,splitted[7].length()-2));

	}
}


