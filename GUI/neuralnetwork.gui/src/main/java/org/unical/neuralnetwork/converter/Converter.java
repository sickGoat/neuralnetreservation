package org.unical.neuralnetwork.converter;

import java.io.File;
import java.util.Iterator;

public class Converter {
	
	public static File convert(Parser p, Builder b, String nameConvertion){
		b.setHeaderInfo(nameConvertion, 2, p.getNumberOfEdge() + p.getNumberOfNode());
		Iterator<String[]> nodeit = p.getNodeIterator();
		Iterator<String[]> edgeit = p.getEdgeIterator();
		while(nodeit.hasNext())
			b.buildNode(nodeit.next());
		while(edgeit.hasNext())
			b.buildEdge(edgeit.next());
		return b.getProduct();
	}

}
