package org.unical.neuralnetwork.converter.test;

import static org.unical.neuralnetwork.converter.test.UtilityTestFile.*;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import org.unical.neuralnetwork.converter.Builder;
import org.unical.neuralnetwork.converter.DGSBuilder;
import org.unical.neuralnetwork.converter.Parser;
import org.unical.neuralnetwork.converter.ParsingException;
import org.unical.neuralnetwork.converter.SUMOParser;

import geographic.Coordinate;

public class FullCreationTest {

	@Test
	public void test() throws ParsingException {
		Parser parser = new SUMOParser(new File(NODE_FILE_PATH), new File(EDGE_FILE_PATH));
		Builder builder = new DGSBuilder(DGS_FILE_PATH);
		Iterator<String[]> it1 = parser.getNodeIterator();
		Iterator<String[]> it2 = parser.getEdgeIterator();
		builder.setHeaderInfo("hdb2", 0, parser.getNumberOfNode() + parser.getNumberOfEdge());
		while (it1.hasNext()) {
			String[] elem = it1.next();
			builder.buildNode(elem);
		}
		while (it2.hasNext()) {
			String[] elem = it2.next();
			builder.buildEdge(elem);
		}
		Coordinate[] bounds = builder.getBounds();
		System.out.println(Arrays.toString(bounds));
		//builder.getProduct();
	}

}
