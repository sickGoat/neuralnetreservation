package org.unical.neuralnetwork.gui.grid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import geographic.Coordinate;
import geographic.interfaces.Cell;
import util.PathMatcher;

public class HexagonGridController implements GridController{

	private Map<Cell, List<Edge>> graphicCell = new HashMap<>();
	private boolean visibility = true;
	private static int ID_REF = 0;
	private static final String UI_CLASSES[] = { "grid_normal", "grid_hide", "grid_foretold", "grid_wrong", "grid_lost",
			"grid_rectangle" };
	private PathMatcher last = null;

	public HexagonGridController() {
	}

	public void showOrHide() {
		if (visibility) {
			setStyle(1);
			visibility = false;
		} else {
			setStyle(0);
			visibility = true;
		}
	}
	
	public void lightPath(PathMatcher p){
		if(last != null){
			resign(last);
		}
		List<Cell> predicted = p.getRightPredicted();
		List<Cell> notPredicted = p.getNotPredicted();
		List<Cell> waste = p.getWaste();
		for(Cell c : notPredicted){
			changeStyleCell(c, 4);
		}
		for(Cell c : waste){
			changeStyleCell(c, 3);
		}
		for(Cell c : predicted){
			changeStyleCell(c, 2);
		}
		last = p;
	}

	private void resign(PathMatcher p) {
		for(Cell c : p.getRealPath()){
			changeStyleCell(c, 0);
		}
	}

	public void setStyle(int index) {
		if (index > UI_CLASSES.length - 1) {
			throw new IllegalArgumentException();
		}
		for (Cell c : graphicCell.keySet()) {
			changeStyleCell(c, index);
		}
	}

	private void changeStyleCell(Cell c, int index) {
		for (Edge e : graphicCell.get(c)) {
			e.removeAttribute("ui.class");
			e.addAttribute("ui.class", UI_CLASSES[index]);
		}
	}

	public void draw(Graph g, List<Cell> cells, Coordinate min, Coordinate max) {
		for (Cell cell : cells) {
			Coordinate[] vertex = cell.getVertex();
			Coordinate ctmp = vertex[0];
			for (int i = vertex.length - 1; i >= 0; i--) {
				int b = inBounds(ctmp, vertex[i], min, max);
				if (b == 0) {
					putInMap(cell, createEdge(g, ctmp, vertex[i]));
				} else if (b != 3) {
					Coordinate[] news = buildIntercept(ctmp, vertex[i], min, max, b);
					putInMap(cell, createEdge(g, news[0], news[1]));
				}
				ctmp = vertex[i];
			}
		}
		createRectangle(g, min, max);
	}

	private void createRectangle(Graph g, Coordinate min, Coordinate max) {
		Coordinate c0 = new Coordinate(max.getxC(), min.getyC());
		Coordinate c1 = new Coordinate(min.getxC(), max.getyC());
		createEdge(g, min, c0).addAttribute("ui.class", UI_CLASSES[5]);
		createEdge(g, c0, max).addAttribute("ui.class", UI_CLASSES[5]);
		createEdge(g, max, c1).addAttribute("ui.class", UI_CLASSES[5]);
		createEdge(g, c1, min).addAttribute("ui.class", UI_CLASSES[5]);
	}

	private Coordinate[] buildIntercept(Coordinate start, Coordinate end, Coordinate min, Coordinate max, int out) {
		Coordinate[] res = new Coordinate[2];
		if (out == 1) {
			res[0] = start;
			res[1] = buildIntercept(end, start, min, max);
		} else {
			res[0] = buildIntercept(start, end, min, max);
			res[1] = end;
		}
		return res;
	}

	private Coordinate buildIntercept(Coordinate out, Coordinate in, Coordinate min, Coordinate max) {
		double m = (out.getyC() - in.getyC()) / (out.getxC() - in.getxC());
		double q = in.getyC() - m * in.getxC();
		Coordinate[] inter = new Coordinate[4];
		inter[0] = new Coordinate(min.getxC(), m * min.getxC() + q);
		inter[1] = new Coordinate(max.getxC(), m * max.getxC() + q);
		if (m != 0) {
			inter[2] = new Coordinate((min.getyC() - q) / m, min.getyC());
			inter[3] = new Coordinate((max.getyC() - q) / m, max.getyC());
		}
		double distance = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < inter.length; i++) {
			if (inter[i] != null) {
				double d = sqrt(pow(inter[i].getxC() - in.getxC(), 2) + pow(inter[i].getyC() - in.getyC(), 2));
				if (d < distance) {
					distance = d;
					index = i;
				}
			}
		}
		return inter[index];
	}

	private void putInMap(Cell cell, Edge edge) {
		if (graphicCell.containsKey(cell)) {
			graphicCell.get(cell).add(edge);
		} else {
			LinkedList<Edge> l = new LinkedList<>();
			l.add(edge);
			graphicCell.put(cell, l);
		}
	}

	private Edge createEdge(Graph g, Coordinate start, Coordinate end) {
		Node n1 = createNode(g, start);
		Node n2 = createNode(g, end);
		Edge e = g.addEdge(Integer.toString(ID_REF++), n1, n2);
		e.addAttribute("ui.class", UI_CLASSES[0]);
		return e;
	}

	private Node createNode(Graph g, Coordinate start) {
		Node n = g.addNode(Integer.toString(ID_REF++));
		n.setAttribute("x", start.getxC());
		n.setAttribute("y", start.getyC());
		return n;
	}

	private int inBounds(Coordinate start, Coordinate end, Coordinate min, Coordinate max) {
		boolean inb1 = inBounds(start, min, max);
		boolean inb2 = inBounds(end, min, max);
		if (inb1 && inb2)
			return 0;
		if (inb1 && !isOnBorder(start, min, max))
			return 1;
		if (inb2 && !isOnBorder(end, min, max))
			return 2;
		return 3;
	}

	private boolean inBounds(Coordinate c, Coordinate min, Coordinate max) {
		return c.getxC() >= min.getxC() && c.getxC() <= max.getxC() && c.getyC() >= min.getyC()
				&& c.getyC() <= max.getyC();
	}

	private boolean isOnBorder(Coordinate c, Coordinate min, Coordinate max) {
		return c.getxC() == min.getxC() || c.getxC() == max.getxC() || c.getyC() == min.getyC()
				|| c.getyC() == max.getyC();
	}

	@Override
	public void setStyle(GridColorStyle cs) {
		setStyle(cs.ordinal());
	}
}
