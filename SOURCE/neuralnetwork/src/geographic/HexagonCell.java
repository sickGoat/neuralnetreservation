package geographic;

import geographic.interfaces.Cell;
import geographic.interfaces.CellBuilder;

public class HexagonCell extends Cell{
	
	/*
	 * Vertici dell'esagono
	 *
	 */
	public static enum Vertex {
	
		V1,//basso a sinistra
		V2,//centro a sinitra
		V3,//alto sinistra
		V4,//alto destra
		V5,//centro destra
		V6 //basso destra
	}
	
	/*
	 * Direzioni dell'esagono
	 */
	public static enum Directions {
		
		D1,//north
		D2,//north east
		D3,//south east
		D4,//south 
		D5,//south west
		D6;//north west
		
		public Directions shift(){
			switch (this) {
			case D1:
				return D4;
			case D2:
				return D5;
			case D3:
				return D6;
			case D4:
				return D1;
			case D5:
				return D2;
			case D6:
				return D3;
			default:
				return this;
			}
		}
	}
	
	
	/*
	 * Builder per le celle esagonali
	 */
	public static class HexagonBuilder implements CellBuilder<HexagonCell>{

		@Override
		public HexagonCell build(double side, Coordinate baryCentre) {
			double h = side*(Math.sqrt(3)/2);
			
			Coordinate[] vertex = new Coordinate[6];
			
			vertex[Vertex.V1.ordinal()] = new Coordinate(baryCentre.xC-side/2,baryCentre.yC-h);
			vertex[Vertex.V2.ordinal()] = new Coordinate(baryCentre.xC-side, baryCentre.yC);
			vertex[Vertex.V3.ordinal()] = new Coordinate(baryCentre.xC-side/2,baryCentre.yC+h);
			vertex[Vertex.V4.ordinal()] = new Coordinate(baryCentre.xC+side/2,baryCentre.yC+h);
			vertex[Vertex.V5.ordinal()] = new Coordinate(baryCentre.xC+side, baryCentre.yC);
			vertex[Vertex.V6.ordinal()] = new Coordinate(baryCentre.xC+side/2,baryCentre.yC-h);
			
			return new HexagonCell(side,baryCentre,vertex);
			
			}
		}
	
	
	
	
	protected HexagonCell(double side, Coordinate baryCentre, Coordinate[] vertex) {
		super(side, baryCentre, vertex);
	}

	
	@Override
	public double getHeight() {
		return side*Math.sqrt(3)/2;
	}
	
	@Override
	public double getDimension() {
		return side*2;
	}
	
	/**
	 * Verifico che la distanza tra il punto
	 * passato come parametro col baricentro sia
	 * minore del lato (utilizzo la circonferenza
	 * circoscritta)
	 */
	@Override
	public boolean contains(Coordinate pos) {
		//calcolo la distanza
		int xDist = (int)Math.abs(pos.xC - this.baryCentre.xC);
		double yDist = (int)Math.abs(pos.yC - this.baryCentre.yC);
		int dist =(int) Math.sqrt(Math.pow(xDist, 2)+Math.pow(yDist,2));
		return dist <= side;
	}
	
}
