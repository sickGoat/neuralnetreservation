package org.unical.neuralnetwork.grid.test;

import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.unical.neuralnetwork.gui.grid.GridController;
import org.unical.neuralnetwork.gui.grid.HexagonGridController_v2;

import geographic.Coordinate;
import geographic.GeoMap;
import geographic.HexMapManager;
import geographic.interfaces.Cell;

public class MainGridTest {
	
	public static void main(String[] args) {
		Graph g = new MultiGraph("Cozza");
		Coordinate min = new Coordinate(0, 0);
		Coordinate max = new Coordinate(1000, 1000);
		GeoMap map =  new GeoMap(min, max);
		HexMapManager manager = HexMapManager.getInstance(map, 800);
		List<Cell> l = new LinkedList<>();
		for(Cell c : manager.getCells()){
			l.add(c);
		}
		GridController c = new HexagonGridController_v2();
		c.draw(g, l, min, max);
		g.addAttribute("ui.stylesheet", "url('file:src/main/graph.css')");
		g.display(false);
		System.out.println(manager.getCells().size());
	}

}
