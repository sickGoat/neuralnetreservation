package org.unical.neuralnetwork.grid.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import geographic.Coordinate;
import geographic.GeoMap;
import geographic.HexMapManager;
import geographic.HexagonCell;
import geographic.HexagonCell.HexagonBuilder;
import geographic.interfaces.MapManager;

public class GridTest {

	@Ignore
	@Test
	public void test() {
		assertTrue(true);
		GeoMap map = new GeoMap(new Coordinate(0, 0), new Coordinate(500, 500));
		MapManager<HexagonCell> manager = HexMapManager.getInstance(map, 1);
		int cellnum = manager.getCellNumber();
		System.out.println(cellnum);
		for (int i = 0; i < cellnum; i++) {
			//Cell c = manager.getCell(i);
//			for (Coordinate v : c.vertex) {
//				System.out.printf("%.6f %.6f%n", v.getxC(), v.getyC());
//			}
			System.out.println(" ");
		}
	}

	//@Ignore
	@Test
	public void testCreation() {
		double side = 50;
		double apothem = Math.sqrt(3) / 2 * side;
		Coordinate low = new Coordinate(0, 0);
		Coordinate up = new Coordinate(500, 500);
		List<HexagonCell> cells = new LinkedList<>();
		HexagonBuilder builder = new HexagonBuilder();
		Coordinate first = low;
		boolean traslation = true;
		while (first.getyC() <= up.getyC() + apothem) {
			Coordinate centre = new Coordinate(first.getxC(), first.getyC());
			while (centre.getxC() <= up.getxC() + apothem) {
				cells.add(builder.build(side, centre));
				centre = new Coordinate(centre.getxC() + 3 * side, centre.getyC());
			}
			if (traslation) {
				first = new Coordinate(first.getxC() + side * (double) 3 / 2, first.getyC() + apothem);
				traslation = false;
			} else {
				first = new Coordinate(first.getxC() - side * (double) 3 / 2, first.getyC() + apothem);
				traslation = true;
			}
		}
//		for(Cell c: cells){
//			for(Coordinate cor: c.vertex){
//				System.out.printf("%.6f %.6f%n", cor.getxC(), cor.getyC());
//			}
//		}
	}
}
