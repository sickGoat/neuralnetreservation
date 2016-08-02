package org.unical.neuralnetwork.converter.test;

import java.io.File;

import org.junit.Test;
import org.unical.neuralnetwork.converter.ParserFactory;
import org.unical.neuralnetwork.environment.Environment;
import org.unical.neuralnetwork.environment.EnvironmentException;

public class ConverterTest {

	@Test
	public void convertingTest() throws EnvironmentException{
		Environment.bootstrap();
		File f = new File("/Users/valeriorusso/Desktop/neuralnetwork_gui/sumo_convert/hdb2.net.xml");
		ParserFactory.getParser(f);
	}
}
