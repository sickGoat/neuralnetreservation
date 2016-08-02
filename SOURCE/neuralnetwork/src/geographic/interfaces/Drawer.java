package geographic.interfaces;

import java.util.List;

public interface Drawer<T extends Cell> {
	
	public List<T> draw(double shapeSide);
	
}
