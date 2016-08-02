package geographic;

import java.util.LinkedList;
import java.util.List;

import geographic.HexagonCell.HexagonBuilder;
import geographic.interfaces.Drawer;

/**
 * Implementazione drawer per lavorare con celle
 * di forma esagonale
 * @author antonio
 *
 */
public class HexDrawer implements Drawer<HexagonCell> {
	
	private GeoMap map;
	
	
	public HexDrawer(GeoMap map) {
		super();
		this.map = map;
	}


	@Override
	public List<HexagonCell> draw(double shapeSide) {
		List<HexagonCell> cells = new LinkedList<>();
		Coordinate low = map.lowLeftCorner;
		Coordinate up = map.rightUpCorner;
		Coordinate lastBarycentre = low;
		HexagonBuilder builder = new HexagonBuilder();
		double excess = shapeSide*Math.sqrt(3)/2;
		boolean upDirection = true;
		int direction = 1;
		
		while(lastBarycentre.xC < up.xC+shapeSide){
			if(upDirection){
				//muovo il baricentro verso l'alto
				while(lastBarycentre.yC < up.yC+excess){
					cells.add(builder.build(shapeSide, lastBarycentre));
					lastBarycentre = new Coordinate(lastBarycentre.xC, lastBarycentre.yC+excess*2);
				}
				upDirection = !upDirection;
			}else{
				//muovo il baricentro verso il basso
				while(lastBarycentre.yC > low.yC-excess){
					cells.add(builder.build(shapeSide, lastBarycentre));
					lastBarycentre = new Coordinate(lastBarycentre.xC, lastBarycentre.yC-excess*2);
				}
				upDirection = !upDirection;
			}
			
			//aggiorno il baricentro del baricentro in base alla direzione di percorrenza
			direction = upDirection?1:-1;
			lastBarycentre = new Coordinate(lastBarycentre.xC+shapeSide+.5*shapeSide,
									lastBarycentre.yC+direction*excess);
		}
		
		return cells;	
	}
	
	
}
