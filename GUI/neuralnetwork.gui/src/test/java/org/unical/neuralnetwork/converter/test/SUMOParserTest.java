package org.unical.neuralnetwork.converter.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;
import org.unical.neuralnetwork.converter.ParsingException;
import org.unical.neuralnetwork.converter.SUMOParser;

public class SUMOParserTest{
	
	public static String NODE_PATH = "/Users/valeriorusso/Desktop/neuralnetwork_gui/sumo_convert/hdb2_part_.nod.xml";
	public static String EDGE_PATH = "/Users/valeriorusso/Desktop/neuralnetwork_gui/sumo_convert/hdb2_part_.edg.xml";
	
	@Test
	public void testBuild() throws ParsingException{
		SUMOParser parser = new SUMOParser(new File(NODE_PATH), new File(EDGE_PATH));
		System.out.println(String.format("Numero di nodi:%d", parser.getNumberOfNode()));
		System.out.println(String.format("Numero di archi:%d", parser.getNumberOfEdge()));
		Iterator<String[]> nodei = parser.getNodeIterator();
		Iterator<String[]> edgei = parser.getEdgeIterator();
		int it = 0;
		while(nodei.hasNext()){
			String[] i = nodei.next();
			if(i != null)
				System.out.println(String.format("%s, %s, %s, %s", i[0], i[1], i[2], i[3]));
			it++;
		}
		assertTrue(it == parser.getNumberOfNode());
		while(edgei.hasNext()){
			String[] i = edgei.next();
			if(i != null)
				System.out.println(String.format("%s, %s, %s, %s", i[0], i[1], i[2], i[3]));
		}
	}
}
