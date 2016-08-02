package geographic.interfaces;

import geographic.Coordinate;

public abstract class Cell implements Cloneable {
	
	
	private static int ID_C = 0;
	
	protected final int id;
	
	protected final double side;
	
	protected final Coordinate baryCentre;
	
	protected Coordinate[] vertex;
	
	protected Cell(double side,Coordinate baryCentre,Coordinate[]vertex){
		this.side = side;
		this.vertex = vertex;
		this.baryCentre = baryCentre;
		this.id = ID_C++;
	}
	
	public Coordinate[] getVertex(){
		return this.vertex;
	}
	
	public int getId(){
		return this.id;
	}
	
	public double getSide(){
		return this.side;
	}
	
	public Coordinate getBaryCentre(){
		return baryCentre;
	}
	
	public int getVertexNum(){
		return vertex.length;
	}
	
	/**
	 * Metodo che restituisce la dimensione
	 * della cella
	 * @return
	 */
	public abstract double getDimension();


	public abstract double getHeight();
	
	public abstract boolean contains(Coordinate pos);
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ID="+this.id+"\n");
		builder.append("barycentre="+this.baryCentre+"\n");
		for( int i = 0 ; i < getVertexNum() ; i++ ){
			builder.append("V"+i+" = "+this.vertex[i]);
			builder.append("\n");
		}
		
		return builder.toString();
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
		Cell other = (Cell) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Cell clone(){
		Cell res = null;
		try{
			res = (Cell) super.clone();
		}catch(CloneNotSupportedException e){}
	
		return res;
	}
	
}
