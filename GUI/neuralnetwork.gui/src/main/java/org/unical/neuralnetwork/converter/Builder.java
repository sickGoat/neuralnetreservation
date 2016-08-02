package org.unical.neuralnetwork.converter;

import java.io.File;

import geographic.Coordinate;

public interface Builder {
	void buildNode(String[] att);
	void buildEdge(String[] att);
	File getProduct();
	void setHeaderInfo(String name, int numberOfStep, int numberOfEvent);
	Coordinate[] getBounds();
}
