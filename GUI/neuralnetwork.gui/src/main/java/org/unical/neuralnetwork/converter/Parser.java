package org.unical.neuralnetwork.converter;

import java.util.Iterator;

public interface Parser {
	int getNumberOfNode();
	int getNumberOfEdge();
	Iterator<String[]> getNodeIterator();
	Iterator<String[]> getEdgeIterator();
}
