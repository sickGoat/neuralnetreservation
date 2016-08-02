package util;

/**
 * Classe che trasforma una direzione
 * in un array di double
 * @author antonio
 *
 */
public class SideConverter {
	
	private int sideNumber;
	
	public SideConverter(int sideNumber){
		this.sideNumber = sideNumber;
	}
	
	public double[] convert(int direction){
		if(direction>sideNumber)
			throw new RuntimeException("La direzione è maggiore del numero di lati della cella");
		
		double[] arrayDir = new double[sideNumber];
		arrayDir[direction] = 1;
		
		return arrayDir;
	}
}
