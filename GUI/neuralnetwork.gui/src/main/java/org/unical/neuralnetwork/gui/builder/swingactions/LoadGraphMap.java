package org.unical.neuralnetwork.gui.builder.swingactions;

import java.io.IOException;

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.stream.GraphParseException;
import org.unical.neuralnetwork.gui.builder.BuildingProgress;

public class LoadGraphMap extends UpdateProgress {

	private Graph g;
	private String path;

	public LoadGraphMap(BuildingProgress bp, String string, int value, Graph g, String filePath) {
		super(bp, string, value);
		this.g = g;
		this.path = filePath;
	}

	@Override
	protected void makeAction() {
		super.makeAction();
		try {
			g.read(path);
			g.addAttribute("ui.stylesheet",CssStyleMap.STYLE);
			g.addAttribute("ui.quality");
			g.addAttribute("ui.antialias");
		} catch (ElementNotFoundException | IOException | GraphParseException e) {
			e.printStackTrace();
		}
	}
}
