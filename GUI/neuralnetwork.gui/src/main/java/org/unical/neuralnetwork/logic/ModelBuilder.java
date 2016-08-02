package org.unical.neuralnetwork.logic;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.graphstream.graph.Graph;
import org.unical.neuralnetwork.converter.DGSBuilder;
import org.unical.neuralnetwork.converter.Parser;
import org.unical.neuralnetwork.converter.ParserFactory;
import org.unical.neuralnetwork.environment.Environment;
import org.unical.neuralnetwork.environment.EnvironmentProperty;
import org.unical.neuralnetwork.gui.MainView;
import org.unical.neuralnetwork.gui.builder.BuildingProgress;
import org.unical.neuralnetwork.gui.builder.swingactions.GridDrawing;
import org.unical.neuralnetwork.gui.builder.swingactions.LoadGraphMap;
import org.unical.neuralnetwork.gui.builder.swingactions.SwingAction;
import org.unical.neuralnetwork.gui.builder.swingactions.UpdateProgress;
import org.unical.neuralnetwork.gui.grid.GridController;
import org.unical.neuralnetwork.gui.grid.HexagonGridController_v2;

import geographic.Coordinate;
import geographic.GeoMap;
import geographic.interfaces.Cell;
import network.interafaces.NetworkManager.PropagationType;
import util.PathMatcher;
import util.Runner;
import util.SimulationParameter;
import util.SimulationParameter.SimulationParamBuilder;
import util.SimulationResult;

public class ModelBuilder extends SwingWorker<Model, SwingAction> {

	public enum NNType {
		DISTRIBUITED, CENTRAL
	}

	private File map;
	private File ts;
	private File ss;
	private int cellCount = 20;
	private NNType type;
	private SimulationParamBuilder builder;
	// Gui env
	private MainView mv;
	private Graph g;
	private BuildingProgress bp;

	public SimulationParamBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(SimulationParamBuilder builder) {
		this.builder = builder;
	}

	public void setCellCount(int cellCount) {
		this.cellCount = cellCount;
	}

	public void setType(NNType type) {
		this.type = type;
	}

	public File getMap() {
		return map;
	}

	public void setMap(File map) {
		this.map = map;
	}

	public File getTs() {
		return ts;
	}

	public void setTs(File ts) {
		this.ts = ts;
	}

	public File getSs() {
		return ss;
	}

	public void setSs(File ss) {
		this.ss = ss;
	}

	public int getCellCount() {
		return cellCount;
	}

	public NNType getType() {
		return type;
	}

	public boolean isReady() {
		if (builder == null)
			return map != null && ts != null && cellCount != 0 && type != null && ss != null;
		else
			return map != null && ts != null && ss != null;
	}

	public void setViewEnviroment(Graph g, BuildingProgress bp, MainView mv) {
		this.g = g;
		this.bp = bp;
		this.mv = mv;
	}

	@Override
	protected Model doInBackground() throws Exception {
		modify("Conversione del file SUMO...", 14);
		Parser parser = ParserFactory.getParser(map);
		String mapName = map.getName().substring(0, map.getName().indexOf('.'));
		DGSBuilder builder = new DGSBuilder(
				Environment.getProperty(EnvironmentProperty.DGS_PATH) + "/" + mapName + ".dgs");

		modify("Conversione in DGS...", 7);
		Iterator<String[]> itnode = parser.getNodeIterator();
		Iterator<String[]> itedge = parser.getEdgeIterator();
		builder.setHeaderInfo(mapName, 1, parser.getNumberOfEdge() + parser.getNumberOfNode());
		while (itnode.hasNext())
			builder.buildNode(itnode.next());
		modify("Conversione in DGS...", 7);
		while (itedge.hasNext())
			builder.buildEdge(itedge.next());

		modify("Scrittura del file DGS...", 14);
		File dgs = builder.getProduct();

		modify("Carimento grafo...", 14, dgs.getAbsolutePath());

		Model res = new Model(new HexagonGridController_v2(), null);
		Coordinate[] bounds = builder.getBounds();

		modify("Creazione...", 0);
		SimulationParamBuilder netBuilder = null;
		System.out.printf("Bounds: %s\n", Arrays.toString(bounds));
		if (this.builder == null) {
			netBuilder = new SimulationParamBuilder();
			netBuilder.isDistributed(this.type == NNType.DISTRIBUITED).isRawData(true)
					.setPropagationType(PropagationType.RESILIENT).setMapDimension(new GeoMap(bounds[0], bounds[1]))
					.setH(6).setTrainingThreshold(0.05).setAlghThreshold(0.35).setCellSide(computeSide(bounds));
		} else {
			this.builder.setMapDimension(new GeoMap(bounds[0], bounds[1]));
			netBuilder = this.builder;
		}
		SimulationParameter sp = netBuilder.build();
		System.out.println(sp);
		Runner r = new Runner(sp);
		r.init();

		modify("Creazione griglia...", 14, /* clonato */ builder.getBounds(), res.getGridController(),
				r.mapManager.getCells());

		modify("Training...", 14);
		r.trainNetwork(ts);

		modify("Simulazione della rete...", 14);
		res.setResult(r.start(ss));

		modify("End :) !", 50);
		return res;
	}

	@Override
	protected void done() {
		Model m = null;
		try {
			m = get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (m != null) {
			mv.setModel(m);
			mv.addAccuracy(m.getResult().getAccuracy());
			m.getResult().getWaste();
			mv.addWaste(m.getResult().getWaste());
		}
	}

	@Override
	protected void process(List<SwingAction> chunks) {
		for (SwingAction sa : chunks) {
			sa.make();
		}
	}

	private double computeSide(Coordinate[] bounds) {
		double b = bounds[1].getxC() - bounds[0].getxC();
		double h = bounds[1].getyC() - bounds[0].getyC();
		return Math.sqrt(b * h / (38 * Math.sqrt(3) / 2 * 3));
	}

	private void modify(String mx, int value) {
		publish(new UpdateProgress(bp, mx, value));
	}

	private void modify(String mx, int value, Coordinate[] c, GridController gridController, List<Cell> cells) {
		publish(new GridDrawing(bp, mx, value, g, gridController, c, cells));
	}

	private void modify(String mx, int value, String fileGraphPath) {
		publish(new LoadGraphMap(bp, mx, value, g, fileGraphPath));
	}
}
