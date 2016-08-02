package geographic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;

import geographic.interfaces.Cell;
import geographic.interfaces.MapManager;
import geographic.interfaces.Parser;
import network.data.RawData;


/**
 * Il genrico HexagonCell lo specifico in modo che
 * il parser sappia con quali celle sta lavorando.
 * Questa informazione è necessaria per accedere
 * alle enum definite all'interno delle sottoclassi
 * di cell
 * 
 * @author antonio
 *
 */
public class ParserImpl extends Parser{

	public List<Coordinate> c = new ArrayList<>();

	public ParserImpl(File input,MapManager<?> manager) {
		super(input,manager);
	}
	

	
	@Override
	public RawData parse() {
		try{
			Stack<NN1Entry> stackNN1 = new Stack<>();
			Stack<NN2Entry> stackNN2 = new Stack<>();
			ArrayList<Trace> traces = new ArrayList<>();
			
			int direction = -1;
			String s;
			String[] splitted;
			Matcher matcher;
			Cell previous,actual;
			Coordinate prevCoord,actualCoord;
			while(true){
				s = reader.readLine();
				if(s == null){
					break;
				}
				if(s.startsWith(NEWNODE)){
					//sono all'inizio del file in sono definiti
					//i path
					traces.add(getTrace(s));
				
				}else if(s.startsWith(MOVEMENT)){
					//leggo il numero del nodo e ricavo l'oggetto trace
					splitted = s.split(REGEX);
					matcher = pattern.matcher(splitted[1]);
					int idTrace =-1;
					if(matcher.find()){
						idTrace = Integer.parseInt(matcher.group().replaceAll("[()]",""));
					}
					
					Trace actualTrace = null;
					Trace queryTrace = new Trace(idTrace,0,0);
					if(traces.contains(queryTrace)){
						actualTrace = traces.get(traces.indexOf(queryTrace));
					}
					
					//comincio a leggere i record della traccia
					prevCoord = new Coordinate(actualTrace.xC,actualTrace.yC);
					previous = mapManager.getCell(prevCoord);
					if(!previous.contains(prevCoord))
						throw new RuntimeException("la cella non contiene le coordinate "+previous+" "+prevCoord);
					boolean firstMovement = true;
					direction = -1;
					while(true){
						s = reader.readLine();
						if(s==null||s.length()==0){
							//se arrivo alla fine del file o alla fine del percorso
							//rimuovo dallo stack
							if(!stackNN2.isEmpty())
								stackNN2.pop();
							break;
						}
						//ricavo le coordinate e la cella della linea attuale
						actualCoord = getCoordinate(s);
						c.add(actualCoord);
						actual = mapManager.getCell(actualCoord);
						//controllo se lo spostamento mi abbia fatto cambiare cella
						if(previous!=actual && firstMovement){
							//salvo la entry per la nn1 e quella per la nn2
							firstMovement = !firstMovement;
							NN1Entry nn1 = new NN1Entry();
							nn1.idCell = previous.getId();
							
							//controllo che la direzione non sia gia stata calcolata
							direction = direction != -1 ? direction : mapManager.getDirection(prevCoord, actualCoord);
							nn1.inDir = direction;
							
							//calcolo la direzione di uscita
							int out = mapManager.getDirection(previous.getBaryCentre(), actual.getBaryCentre());
							nn1.outDir = out;
							stackNN1.push(nn1);
							//creo la entry per la nn2
							NN2Entry nn2 = new NN2Entry();
							nn2.idCell = actual.getId();
							nn2.inSide = mapManager.shiftDirection(direction); 
							stackNN2.push(nn2);
							
							//cambio valore alla variabile previous
							previous = actual;
						}else if(previous!=actual){
							//creo una entry solo per la nn2
							//prelevandola dallo stack
							NN2Entry nn2 = stackNN2.peek();
							int outDirection = mapManager.getDirection(previous.getBaryCentre(),actual.getBaryCentre());
							nn2.outSide = outDirection;
							
							//creo una nuova entry per la cella successiva (actual)
							nn2 = new NN2Entry();
							nn2.idCell = actual.getId();
							nn2.inSide = mapManager.shiftDirection(outDirection);
							stackNN2.push(nn2);
							
							previous = actual;
							
						}else if(direction == -1 && firstMovement ){
							direction = mapManager.getDirection(prevCoord,actualCoord);
						}
					}
					//marker fine traccia
					c.add(new Coordinate(0,0));
				}
			}
			
			return new RawData(new LinkedList<>(stackNN1),new LinkedList<>(stackNN2));
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Per estrapolare la traccia occorre leggere tre 
	 * righe consecutive del file
	 * @param s
	 * @return
	 * @throws IOException
	 */
	private Trace getTrace(String s) throws IOException{
		String[] splitted;
		splitted = s.split(REGEX);
		int idTrace = Integer.parseInt(splitted[1]);
		s = reader.readLine();
		splitted = s.split(REGEX);
		double x = Double.parseDouble(splitted[3]);
		s = reader.readLine();
		splitted = s.split(REGEX);
		double y = Double.parseDouble(splitted[3]);

		return new Trace(idTrace,x,y);
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





}
