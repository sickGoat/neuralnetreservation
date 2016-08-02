package org.unical.neuralnetwork.converter;

import static org.unical.neuralnetwork.environment.Environment.getProperty;
import static org.unical.neuralnetwork.environment.EnvironmentProperty.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.unical.neuralnetwork.environment.EnvironmentException;

public class ParserFactory {

	private ParserFactory() {
	}

	public static Parser getParser(File toParse) throws EnvironmentException {
		if (toParse.getName().endsWith(".net.xml")) {
			return sumoParser(toParse);
		}
		return null;
	}

	private static Parser sumoParser(File toParse) throws EnvironmentException {
		String fileName = toParse.getName().substring(0, toParse.getName().indexOf('.'));
		String[] params = { getProperty(NETCONVERT_PATH) ,
					"-s",
					String.format("%s", toParse.getAbsolutePath()),
					"--ignore-errors",
					"true",
					"--plain-output-prefix",
					String.format("%s/%s",
					getProperty(PLAIN_PATH), fileName) };
		ProcessBuilder pb = new ProcessBuilder(params);
		Parser res = null;
		try {
			Process p = pb.start();
			Scanner error = new Scanner(p.getErrorStream());
			Scanner out = new Scanner(p.getInputStream());
			int state = p.waitFor();
			if (state == 0) {
				while (out.hasNextLine())
					System.out.println(String.format("NetConvert::%s",out.nextLine()));
				// e quindi?
				File nodes = new File(String.format("%s/%s.nod.xml", getProperty(PLAIN_PATH), fileName));
				File edges = new File(String.format("%s/%s.edg.xml", getProperty(PLAIN_PATH), fileName));
				res = new SUMOParser(nodes, edges);
			} else {
				while (error.hasNextLine())
					System.out.println(String.format("NetConvert::%s",error.nextLine()));
				error.close();
				out.close();
				throw new EnvironmentException("File malformato");
			}
			error.close();
			out.close();
		} catch (IOException | InterruptedException e) {
			throw new EnvironmentException(e.getMessage());
		}
		return res;
	}
}
