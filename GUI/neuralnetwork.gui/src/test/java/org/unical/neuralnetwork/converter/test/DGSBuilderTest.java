package org.unical.neuralnetwork.converter.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.unical.neuralnetwork.converter.DGSBuilder;

public class DGSBuilderTest {

	private static DGSBuilder builder = new DGSBuilder("/Users/valeriorusso/Desktop/neuralnetwork_gui/sumo_convert/hdb2.dgs");

	@BeforeClass
	public static void init() {
		builder.setHeaderInfo("ciccio cozza", 0, 0);
	}

	// @Ignore
	@Test
	public void testBuild() {
		// build node
		String[] node1 = { "1", "3.4", "5.4", null };
		String[] node2 = { "2", "3.4", "7.4", null };
		builder.buildNode(node1);
		builder.buildNode(node2);
		// build edge
		String[] edge = { "3", "1", "2", "2.4,6.4 2.3,6.8 3.2,7.1" };
		builder.buildEdge(edge);
		builder.getProduct();
	}

}
