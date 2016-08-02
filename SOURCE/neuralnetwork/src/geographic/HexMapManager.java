package geographic;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import geographic.interfaces.Cell;
import geographic.interfaces.Drawer;
import geographic.interfaces.MapManager;

public class HexMapManager implements MapManager<HexagonCell> {
	
	
	
	/**
	 * Il Comparator è utilizzato per ordinare le
	 * chiavi nella mappa. Le coordinate vengono
	 * ordinate secondo le coordinate x
	 * @author antonio
	 *
	 */
	private class CoordinateXComparator implements Comparator<Coordinate>{

		@Override
		public int compare(Coordinate o1, Coordinate o2) {
			return Double.compare(o1.xC, o2.xC);
		}
		
	}
	
	
	
	/**
	 * Il Comparator è utilizzato per ordinare le celle nel set
	 * secondo il valore della coordinata y
	 * @author antonio
	 *
	 */
	private class CoordinateYComparator implements Comparator<HexagonCell>{

		@Override
		public int compare(HexagonCell o1, HexagonCell o2) {
			return Double.compare(o1.getBaryCentre().yC, o2.getBaryCentre().yC);
		}
		
	}
	
	
	
	/**
	 * Comparator necessario ad effettuare le ricerche nel set di cell
	 * tramite l'id della cella
	 * @author antonio
	 *
	 */
	private class CellIdComparator implements Comparator<HexagonCell>{

		@Override
		public int compare(HexagonCell o1, HexagonCell o2) {
			return Integer.compare(o1.getId(), o2.getId());
		}
		
	}
	
	
	
	private final Comparator<Coordinate> xComparator = new CoordinateXComparator();
	private final Comparator<HexagonCell> yComparator = new CoordinateYComparator();
	private final Comparator<HexagonCell> idComparator = new CellIdComparator();
	
	
	/**
	 * Le chiavi sono ordinate sulla base della cordinata x del baricentro
	 */
	private Map<Coordinate,Set<HexagonCell>> map = new TreeMap<>(xComparator);
	
	
	/**
	 * Lista per accedere alla cella tramite il suo id
	 */
	private Set<HexagonCell> cellSet = new TreeSet<>(idComparator);
	
	private final double cellSide;
	
	private HexMapManager(double cellSide){this.cellSide = cellSide;}

	
	/**
	 * Prendo la cella la inserisco nella mappa
	 * @param cell
	 */
	@Override
	public void putCell(HexagonCell cell) {
		Coordinate baryCentre = cell.getBaryCentre();
		Set<HexagonCell> set = null;
		if( (set = map.get(baryCentre))== null){
			set = new TreeSet<>(yComparator);
			set.add(cell);
			map.put(baryCentre, set);
		}else{
			set.add(cell);
		}
		
		cellSet.add(cell);
	}
		
	

	/**
	 * Restituisce la cella in cui ricade la coordinata
	 * passata come parametro
	 * @param pos
	 * @return
	 * 
	 */
	@Override
	public HexagonCell getCell(Coordinate pos) {
		double posX = pos.xC;
		Set<HexagonCell> set = null;
		for( Coordinate key : map.keySet() ){
			double dist = Math.abs(key.xC - posX);
			
			if( dist < cellSide ){
				//eseguo la ricerca sull'asse y
				set = map.get(key);
				for(HexagonCell c:set){
					if(Math.abs(c.getBaryCentre().yC-pos.yC)<cellSide)
						if(c.contains(pos))
							return c;
				}
			}
		}
		
		//se le coordinate non si trovano nella mappa restituisco null
		return null;
	}
		
	
	/**
	 * Restituisce la cella con l'id specificato 
	 * @param idCell
	 * @return
	 */
	@Override
	public HexagonCell getCell(int idCell) {
		HexagonCell res = null;
		for(HexagonCell cell:cellSet){
			if(cell.getId()==idCell){
				res = cell;
				break;
			}
		}
		return res;
	}

	@Override
	public Cell nextCell(Cell cell, int direction) {
		Coordinate nextBaryCentre = null;
		Coordinate cellBaryCentre = cell.getBaryCentre();
		double h = cell.getSide()*Math.sqrt(3);
		double side = cell.getSide();
		HexagonCell.Directions d = HexagonCell.Directions.values()[direction];
		switch (d) {
		case D1: //north
			nextBaryCentre = new Coordinate(cellBaryCentre.xC, cellBaryCentre.yC+h);
			break;
		case D2://north east
			nextBaryCentre = new Coordinate(cellBaryCentre.xC+3/2*side,cellBaryCentre.yC+h/2);
			break;
		case D3://south east
			nextBaryCentre = new Coordinate(cellBaryCentre.xC+3/2*side,cellBaryCentre.yC-h/2);
			break;
		case D4://south
			nextBaryCentre = new Coordinate(cellBaryCentre.xC, cellBaryCentre.yC-h);
			break;
		case D5://south west
			nextBaryCentre = new Coordinate(cellBaryCentre.xC-3/2*side, cellBaryCentre.yC-h/2);
			break;
		case D6://north west
			nextBaryCentre = new Coordinate(cellBaryCentre.xC-3/2*side,cellBaryCentre.yC+h/2);
			
		default:
			break;
		}
		Cell nextCell = getCell(nextBaryCentre);
		return nextCell!=null?nextCell:cell;	
	}

	
	/**
	 * Prende due coordinate e restituisce la direzione
	 * di spostamento attraverso l'arcotangente dell'angolo
	 * @param C1 : coordinata di partenza
	 * @param C2 : coordinata di arrivo
	 * @return
	 */
	@Override
	public int getDirection(Coordinate c1, Coordinate c2) {
		double dx = c2.xC - c1.xC;
		double dy = c2.yC - c1.yC;
		double angle =  Math.atan2(dy, dx) * 180 / Math.PI;
		if(angle>0){
			if(angle < 60)
				//north east
				return HexagonCell.Directions.D2.ordinal();
			if(angle>=60 && angle < 120)
				//north
				return HexagonCell.Directions.D1.ordinal();
			//north west
			return HexagonCell.Directions.D6.ordinal();
		
		}else{
			if(angle>-60)
				//south east
				return HexagonCell.Directions.D3.ordinal();
			if( angle<=-60 && angle > -120)
				//south
				return HexagonCell.Directions.D4.ordinal();
			//south west
			return HexagonCell.Directions.D5.ordinal();
		}
	}
	
	
	/**
	 * Restituisce data una cella un array contentente
	 * tutte le sue adiacenti
	 * @param cell
	 * @return
	 */
	@Override
	public Cell[] getAdjacentCells(Cell cell) {
		int dir = cell.getVertexNum();
		Cell[] adjacents = new HexagonCell[dir];
		for(int i = 0 ; i < dir;i++){
			adjacents[i] = nextCell(cell,HexagonCell.Directions.values()[i].ordinal());
		}
		
		return adjacents;
	}
	
	
	
	@Override
	public int getCellNumber() {
		return cellSet.size();
	}
	
	
	/*
	 * Metodo prende una direzione e la trasforma
	 * nella corrispondete direzione per
	 * la cella adiacente
	 * 
	 * @param direction 
	 * @return
	 */
	@Override
	public int shiftDirection(int direction) {
		int shifted = HexagonCell.Directions.values()[direction].shift().ordinal();
		
		return shifted;
	}
	
	/**
	 * Restituisce il numero di direzioni delle celle gestite
	 * 
	 * @return 
	 */
	@Override
	public int getDirectionNumber() {
		return 6;
	}
	
	
	/**
	 * Factory method per l'inizializzazione del manager
	 * @param map : MappGeografica
	 * @param cellSide : Lunghezza del lato della cella
	 * @return
	 */
	public static HexMapManager getInstance(GeoMap map,double cellSide){
		HexMapManager manager = new HexMapManager(cellSide);
		Drawer<HexagonCell> drawer = new HexDrawer(map);
		List<HexagonCell> cells = drawer.draw(cellSide);
		cells.forEach(c -> manager.putCell(c));
		return manager;
	}


	@Override
	public List<Cell> getCells() {
		LinkedList<Cell> res = new LinkedList<>();
		for(Cell c: cellSet)
			res.add(c.clone());
		return res;
	}
}
