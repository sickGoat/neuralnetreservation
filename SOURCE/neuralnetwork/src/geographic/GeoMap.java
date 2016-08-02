package geographic;

public class GeoMap {
	
	protected final Coordinate lowLeftCorner;
	
	protected final Coordinate rightUpCorner;
	
	public GeoMap(Coordinate lolwLeftCorner,Coordinate rightUpCorner) {
		this.lowLeftCorner = lolwLeftCorner;
		this.rightUpCorner = rightUpCorner;
	}

	@Override
	public String toString() {
		return "GeoMap [lowLeftCorner=" + lowLeftCorner + ", rightUpCorner=" + rightUpCorner + "]";
	}
	

}
