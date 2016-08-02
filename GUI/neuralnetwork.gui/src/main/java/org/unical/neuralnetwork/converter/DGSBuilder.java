package org.unical.neuralnetwork.converter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;

import geographic.Coordinate;

public class DGSBuilder implements Builder {

	private static final byte CREATED = 0;
	private static final byte READY = 1;
	private String filePath;
	private StringBuilder doc = new StringBuilder("DGS004\n");
	private byte state = CREATED;
	private Coordinate[] bounds;

	public DGSBuilder(String path) {
		filePath = path;
		bounds = new Coordinate[2];
		bounds[0] = new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE);
		bounds[1] = new Coordinate(-1 * Double.MAX_VALUE, -1 * Double.MAX_VALUE);
	}

	public void buildNode(String[] att) {
		if (state != READY)
			throw new IllegalStateException("Bisogna settare l'header");
		if (att != null) {
			updateBounds(att);
			normalize(att);
			if (att[3] != null) {
				// eh bho....
				addNode(att[0], att[1], att[2]);
			} else {
				addNode(att[0], att[1], att[2]);
			}
		}
	}
	
	private void updateBounds(String[] att) {
		double x = Double.parseDouble(att[1]);
		double y = Double.parseDouble(att[2]);
		if(x < bounds[0].getxC()){
			bounds[0] = new Coordinate(x, bounds[0].getyC());
		}
		if(y < bounds[0].getyC()){
			bounds[0] = new Coordinate(bounds[0].getxC(), y);
		}
		if(x > bounds[1].getxC()){
			bounds[1] = new Coordinate(x, bounds[1].getyC());
		}
		if(y > bounds[1].getyC()){
			bounds[1] = new Coordinate(bounds[1].getxC(), y);
		}
	}

	public void buildEdge(String[] att) {
		if (state != READY)
			throw new IllegalStateException("Bisogna settare l'header");
		if (att != null) {
			normalize(att);
			if (att[3] != null) {
				// nodi
				int id = 0;
				String edgeid = att[0];
				String from = att[1];
				String fromIndex = null;
				StringTokenizer st1 = new StringTokenizer(att[3], " ");
				while (st1.hasMoreTokens()) {
					String token = st1.nextToken();
					StringTokenizer st2 = new StringTokenizer(token, ",");
					if (st2.countTokens() < 2) {
						addEdge(edgeid, from, att[2]);
						// file malformato
						System.out.println("DSGBUILDER::WARNING: File Malformed");
						return;
					} else if (st2.countTokens() >= 2) {
						fromIndex = String.format("%s-N-%d", edgeid, id);
						String newIdEdge = edgeid + "-" + id;
						id++;
						addNode(fromIndex, st2.nextToken(), st2.nextToken());
						addEdge(newIdEdge, from, fromIndex);
						from = fromIndex;
					}
				}
				addEdge(edgeid + "-" + id, from, att[2]);
			} else {
				addEdge(att[0], att[1], att[2]);
			}
		}
	}

	public void setHeaderInfo(String name, int numberOfStep, int numberOfEvent) {
		if (state != CREATED)
			throw new IllegalStateException("Header già settato");
		name = name.replace(' ', '_');
		doc.append(String.format("%s %d %d\n", name, numberOfStep, numberOfEvent));
		doc.append("cl\n");
		state = READY;
	}

	public File getProduct() {
		File res = null;
		Writer w = null;
		try {
			res = new File(filePath);
			w = new PrintWriter(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(res))), true);
			w.write(doc.toString());
			w.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		return res;
	}

	private void normalize(String[] att) {
		att[0] = att[0].replace('#', '-');
	}

	private void addNode(String id, String x, String y) {
		doc.append(String.format("an %s x=%s y=%s", id, x, y)).append("\n");
	}

	private void addEdge(String id, String from, String to) {
		doc.append(String.format("ae %s %s %s", id, from, to)).append("\n");
	}

	@Override
	public Coordinate[] getBounds() {
		Coordinate[] res = new Coordinate[2];
		res[0] = new Coordinate(Math.ceil(bounds[0].getxC()), Math.ceil(bounds[0].getyC()));
		res[1] = new Coordinate(Math.floor(bounds[1].getxC()), Math.floor(bounds[1].getyC()));
		return res;
	}
}
