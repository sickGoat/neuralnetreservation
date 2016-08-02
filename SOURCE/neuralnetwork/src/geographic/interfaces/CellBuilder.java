package geographic.interfaces;

import geographic.Coordinate;

public interface CellBuilder<T extends Cell>{
	
	T build(double side,Coordinate baryCentre);
	
}
