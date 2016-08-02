package org.unical.neuralnetwork.converter;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SUMOParser implements Parser {

	private class XMLIterator implements Iterator<String[]> {

		private NodeList list;
		private int index = 0;

		public int size() {
			return list.getLength();
		}

		private XMLIterator(NodeList list) {
			this.list = list;
		}

		public boolean hasNext() {
			return index < list.getLength();
		}

		public String[] next() {
			Node i = list.item(index);
			String[] res = null;
			if (i.getNodeName().equalsIgnoreCase("node")) {
				res = new String[4];
				if (i.hasAttributes()) {
					NamedNodeMap map = i.getAttributes();
					for (int j = 0; j < map.getLength(); j++) {
						Node attribute = map.item(j);
						if (attribute.getNodeName().equals("id")) {
							res[0] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("x")) {
							res[1] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("y")) {
							res[2] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("shape")) {
							res[3] = attribute.getTextContent();
						}
					}
				}
			} else if (i.getNodeName().equalsIgnoreCase("edge")) {
				res = new String[4];
				if (i.hasAttributes()) {
					NamedNodeMap map = i.getAttributes();
					for (int j = 0; j < map.getLength(); j++) {
						Node attribute = map.item(j);
						if (attribute.getNodeName().equals("id")) {
							res[0] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("from")) {
							res[1] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("to")) {
							res[2] = attribute.getTextContent();
						}
						if (attribute.getNodeName().equals("shape")) {
							res[3] = attribute.getTextContent();
						}
					}
				}
			}
			index++;
			return res;
		}

	}

	private XMLIterator nodei;
	private XMLIterator edgei;

	public SUMOParser(File nodes, File edges) throws ParsingException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document ndoc = builder.parse(nodes);
			Document edoc = builder.parse(edges);
			NodeList list = ndoc.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeName().equalsIgnoreCase("nodes")) {
					nodei = new XMLIterator(n.getChildNodes());
				}
			}
			list = edoc.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n.getNodeName().equalsIgnoreCase("edges")) {
					edgei = new XMLIterator(n.getChildNodes());
				}
			}
			if (this.edgei == null || this.nodei == null) {
				throw new ParsingException("Malformat input::No node or egde found");
			}
		} catch (ParserConfigurationException e) {
			throw new ParsingException(e.getMessage());
		} catch (SAXException e) {
			throw new ParsingException(e.getMessage());
		} catch (IOException e) {
			throw new ParsingException(e.getMessage());
		}
	}

	public int getNumberOfNode() {
		return nodei.size();
	}

	public int getNumberOfEdge() {
		return edgei.size();
	}

	public Iterator<String[]> getNodeIterator() {
		return nodei;
	}

	public Iterator<String[]> getEdgeIterator() {
		return edgei;
	}

}
