package org.unical.neuralnetwork.gui.test;

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.unical.neuralnetwork.converter.test.UtilityTestFile.*;

import java.io.IOException;

public class MapTest {
	@Test
	public void testMap(){
		Graph graph = new MultiGraph("hdb2");
		try {
			graph.read(DGS_FILE_PATH);
		} catch (ElementNotFoundException | IOException | GraphParseException e) {
			e.printStackTrace();
		}
		graph.display(false);
		assertTrue(graph.getNodeSet().size() == 222);
	}
}
