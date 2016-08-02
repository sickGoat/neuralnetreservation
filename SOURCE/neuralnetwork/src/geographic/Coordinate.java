package geographic;


public class Coordinate {
	
	protected final double xC;
	
	protected final double yC;
	
	public Coordinate(double xC,double yC) {
		this.xC = xC;
		this.yC = yC;
	}

	public double getxC() {
		return xC;
	}


	public double getyC() {
		return yC;
	}
	
	@Override
	public String toString() {
		return "x="+this.xC+" ; y="+this.yC;
	}

	
}
