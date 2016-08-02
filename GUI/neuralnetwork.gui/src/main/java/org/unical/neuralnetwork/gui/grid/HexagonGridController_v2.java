package org.unical.neuralnetwork.gui.grid;

import static java.lang.Math.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import geographic.Coordinate;
import geographic.interfaces.Cell;
import util.PathMatcher;

public class HexagonGridController_v2 implements GridController {

	private class MutableLong {
		private long value;

		public MutableLong(long value) {
			this.value = value;
		}

		public void increment() {
			value++;
		}

		@Override
		public String toString() {
			return Long.toString(value);
		}

		public String getIdString() {
			String res = Long.toString(value);
			increment();
			return res;
		}
	}

	private Map<Cell, List<Edge>> map = new HashMap<>();
	private boolean visibility = true;
	private PathMatcher lastPath = null;

	@Override
	public void draw(Graph g, List<Cell> cells, Coordinate lower, Coordinate upper) {
		MutableLong id = new MutableLong(0L);
		Coordinate v1 = null;
		Coordinate v2 = null;
		List<Edge> edges = new LinkedList<>();
		for (Cell c : cells) {
			Coordinate[] vertex = c.getVertex();
			v1 = vertex[0];
			for (int i = vertex.length - 1; i >= 0; i--) {
				v2 = vertex[i];
				// inizio blocco
				Edge e = buildEdge(g, v1, v2, lower, upper, edges, id);
				if (e != null)
					putInMap(c, e);
				// fine blocco
				v1 = v2;
			}
		}
		buildRectangle(g, lower, upper, id);
		resign(map.keySet());
	}

	@Override
	public void showOrHide() {
		GridColorStyle gcs = null;
		if (visibility) {
			gcs = GridColorStyle.HIDE;
		} else {
			gcs = GridColorStyle.NORMAL;
		}
		setCellsStyle(map.keySet(), gcs);
		visibility = !visibility;
	}

	@Override
	public void setStyle(GridColorStyle cs) {
		setCellsStyle(map.keySet(), cs);
	}

	@Override
	public void lightPath(PathMatcher pm) {
		if (lastPath != null) {
			resign(lastPath);
		}
		setCellsStyle(pm.getWaste(), GridColorStyle.WASTE);
		setCellsStyle(pm.getNotPredicted(), GridColorStyle.NOT_PREDICTED);
		setCellsStyle(pm.getRightPredicted(), GridColorStyle.PREDICTED);
		lastPath = pm;
	}

	protected void setCellsStyle(Collection<Cell> cells, GridColorStyle gcs) {
		for (Cell c : cells)
			setCellStyle(c, gcs);
	}

	protected void setCellStyle(Cell c, GridColorStyle gcs) {
		setEdgesStyle(map.get(c), gcs.getCssValue());
	}

	protected void setEdgesStyle(Collection<Edge> edges, String style) {
		for (Edge e : edges)
			e.setAttribute("ui.class", style);
	}

	protected void resign(PathMatcher p) {
		List<Cell> res = new LinkedList<>();
		res.addAll(p.getRightPredicted());
		res.addAll(p.getNotPredicted());
		res.addAll(p.getWaste());
		resign(res);
	}

	protected void resign(Collection<Cell> cells) {
		setCellsStyle(cells, GridColorStyle.NORMAL);
	}

	protected Edge buildEdge(Graph g, Coordinate v1, Coordinate v2, Coordinate l, Coordinate u, Collection<Edge> edges,
			MutableLong id) {
		Edge res = null;
		if (inBounds(v1, l, u)) {
			if (inBounds(v2, l, u)) {
				res = buildEdge(g, v1, v2, edges, id);
			} else {
				Coordinate[] ivertex = buildInternalInterceptions1(v1, v2, l, u);
				res = buildEdge(g, ivertex[0], ivertex[1], edges, id);
			}
		} else if (inBounds(v2, l, u)) {
			Coordinate[] ivertex = buildInternalInterceptions1(v2, v1, l, u);
			res = buildEdge(g, ivertex[0], ivertex[1], edges, id);
		} else {
			Coordinate[] ivertex = buildInternalInterceptions2(v2, v1, l, u);
			if (ivertex != null)
				res = buildEdge(g, ivertex[0], ivertex[1], edges, id);
		}
		return res;
	}

	// todo
	protected Coordinate[] buildInternalInterceptions1(Coordinate in, Coordinate out, Coordinate l, Coordinate u) {
		Coordinate[] interceptions = buildInterceptions(in, out, l, u);
		Coordinate[] ivertex = new Coordinate[2];
		Coordinate[] newBounds = getNewBounds(in, out);
		ivertex[0] = in;
		double distance = Double.MAX_VALUE;
		double dtmp = 0.0;
		for (Coordinate c : interceptions) {
			if (inBounds(c, newBounds[0], newBounds[1]) && (dtmp = distance(in, c)) < distance) {
				ivertex[1] = c;
				distance = dtmp;
			}
		}
		if (ivertex[1] == null)
			return null;
		return ivertex;
	}

	// può retsituire null
	protected Coordinate[] buildInternalInterceptions2(Coordinate in, Coordinate out, Coordinate l, Coordinate u) {
		Coordinate[] interceptions = buildInterceptions(in, out, l, u);
		Coordinate[] ivertex = new Coordinate[2];
		Coordinate[] newBounds = getNewBounds(in, out);
		int i = 0;
		for (Coordinate c : interceptions) {
			if (inBounds(c, l, u) && inBounds(c, newBounds[0], newBounds[1]))
				ivertex[i++] = c;
		}
		if (i < 2)
			return null;
		return ivertex;
	}

	protected Coordinate[] getNewBounds(Coordinate v1, Coordinate v2) {
		Coordinate[] res = new Coordinate[2];
		res[0] = new Coordinate(min(v1.getxC(), v2.getxC()), min(v1.getyC(), v2.getyC()));
		res[1] = new Coordinate(max(v1.getxC(), v2.getxC()), max(v1.getyC(), v2.getyC()));
		return res;
	}

	protected double distance(Coordinate v1, Coordinate v2) {
		return sqrt(pow(v2.getxC() - v1.getxC(), 2) + pow(v2.getyC() - v1.getyC(), 2));
	}

	protected Coordinate[] buildInterceptions(Coordinate v1, Coordinate v2, Coordinate l, Coordinate u) {
		double m = (v1.getyC() - v2.getyC()) / (v1.getxC() - v2.getxC());
		double q = v2.getyC() - m * v2.getxC();
		Coordinate[] inter = null;
		if (m != 0) {
			inter = new Coordinate[4];
			inter[2] = new Coordinate((l.getyC() - q) / m, l.getyC());
			inter[3] = new Coordinate((u.getyC() - q) / m, u.getyC());
		} else {
			inter = new Coordinate[2];
		}
		inter[0] = new Coordinate(l.getxC(), m * l.getxC() + q);
		inter[1] = new Coordinate(u.getxC(), m * u.getxC() + q);
		return inter;
	}

	protected Edge buildEdge(Graph g, Coordinate v1, Coordinate v2, Collection<Edge> edges, MutableLong id) {
		Edge res = contains(v1, v2, edges);
		if (res == null) {
			res = buildEdgeDirectly(g, v1, v2, id);
			edges.add(res);
		}
		return res;
	}

	protected Edge contains(Coordinate v1, Coordinate v2, Collection<Edge> edges) {
		for (Edge e : edges) {
			Node n0 = e.getNode0();
			Node n1 = e.getNode1();
			double[] cn0 = new double[] { n0.getAttribute("x"), n0.getAttribute("y") };
			double[] cn1 = new double[] { n1.getAttribute("x"), n1.getAttribute("y") };
			boolean c1 = (cn0[0] == v1.getxC() && cn0[1] == v1.getyC())
					|| (cn0[0] == v2.getxC() && cn0[1] == v2.getyC());
			boolean c2 = (cn1[0] == v1.getxC() && cn1[1] == v1.getyC())
					|| (cn1[0] == v2.getxC() && cn1[1] == v2.getyC());
			if (c1 && c2)
				return e;
		}
		return null;
	}

	private Edge buildEdgeDirectly(Graph g, Coordinate v1, Coordinate v2, MutableLong id) {
		Node n1 = buildNode(g, v1, id);
		Node n2 = buildNode(g, v2, id);
		Edge e = g.addEdge(id.getIdString(), n1, n2);
		return e;
	}

	protected Node buildNode(Graph g, Coordinate c, MutableLong id) {
		Node n = g.addNode(id.getIdString());
		n.setAttribute("x", c.getxC());
		n.setAttribute("y", c.getyC());
		return n;
	}

	protected boolean inBounds(Coordinate c, Coordinate l, Coordinate u) {
		return c.getxC() >= l.getxC() && c.getxC() <= u.getxC() && c.getyC() >= l.getyC() && c.getyC() <= u.getyC();
	}

	protected void putInMap(Cell c, Edge e) {
		if (map.containsKey(c)) {
			map.get(c).add(e);
		} else {
			LinkedList<Edge> l = new LinkedList<>();
			l.add(e);
			map.put(c, l);
		}
	}

	protected void buildRectangle(Graph g, Coordinate l, Coordinate u, MutableLong id) {
		Coordinate c0 = new Coordinate(u.getxC(), l.getyC());
		Coordinate c1 = new Coordinate(l.getxC(), u.getyC());
		List<Edge> li = new LinkedList<>();
		li.add(buildEdgeDirectly(g, l, c0, id));
		li.add(buildEdgeDirectly(g, c0, u, id));
		li.add(buildEdgeDirectly(g, u, c1, id));
		li.add(buildEdgeDirectly(g, c1, l, id));
		setEdgesStyle(li, "grid_rectangle");
	}
}
