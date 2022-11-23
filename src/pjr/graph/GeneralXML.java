package pjr.graph;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 */
public class GeneralXML extends DefaultHandler {
	
	protected Graph graph;
	
	protected ArrayList<Node> nodeList = new ArrayList<Node>();
	
	
	public static void main (String args[]) {
		File currentFile = new File("c:\\code\\test.xml");
		Graph graph = new Graph();

		GeneralXML xmlReader = new GeneralXML(graph);
		xmlReader.loadGraph(currentFile);
		
		File outFile = new File("c:\\code\\testout.xml");
		xmlReader.saveGraph(outFile);

		System.out.println(graph);
	}
	
	public GeneralXML (Graph graph) {
		super();
		this.graph = graph;
	}
	
	
	public Graph getGraph() {return graph;}
	
	public boolean loadGraph(File file) {
	    try {
		    XMLReader parser = getXMLReader();
		    if(parser == null) {
		        return false;
		    }
			
			graph.clear();
			
			parser.setContentHandler(this);
			parser.setErrorHandler(this);
			FileReader r = new FileReader(file);
			parser.parse(new InputSource(r));
		} catch(Exception e) {
			System.out.println("An exception occured "+e+"\n");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public XMLReader getXMLReader() {
	    XMLReader parser = null;
	    try {
		    
// cant reliably get an XML reader without using the depreciated SAXParser
// this
// 			XMLReader parser = new org.apache.crimson.parser.XMLReaderImpl();
// replaced by
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			parser = saxParser.getXMLReader();
// to here
// this also fails
//			XMLReader parser = XMLReaderFactory.createXMLReader();
		} catch(Exception e) {
			System.out.println("An exception occured "+e+"\n");
			e.printStackTrace();
			return null;
		}
		return parser;
	}
	
	

	public void startDocument () {
	}

	public void endDocument () {
//		System.out.println("End document");
	}
	
	public void startElement(String uri, String name, String qName, Attributes atts) {

		if(!qName.equals("graph") && !qName.equals("node") && !qName.equals("edge")) {
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				System.out.println("non graph elements "+attName+" "+attValue);
			}
		}
		
		if(qName.equals("graph")) {
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("label")) {
					graph.setLabel(attValue);
				}
			}
		}

		if(qName.equals("node")) {
			Node n = new Node();
			int index = -1;
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("id")) {
				    index = Integer.parseInt(attValue);
					n.setMatch(new Integer(index));
				} else if(attName.equals("x")) {
					n.setX(Integer.parseInt(attValue));
				} else if(attName.equals("y")) {
					n.setY(Integer.parseInt(attValue));
				} else if(attName.equals("label")) {
					n.setLabel(attValue);
				}
			}
			graph.addNode(n);
			while(nodeList.size() <= index) {
				nodeList.add(null);
			}
			nodeList.set(index,n);
		}

		if(qName.equals("edge")) {
			Integer i1 = null; 
			Integer i2 = null; 
			String edgeLabel = "";
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("from")) {
					i1 = new Integer(Integer.parseInt(attValue));
				} else if(attName.equals("to")) {
					i2 = new Integer(Integer.parseInt(attValue));
				} else if(attName.equals("label")) {
					edgeLabel = attValue;
				}
			}

			Edge e = new Edge((Node)nodeList.get(i1.intValue()),(Node)nodeList.get(i2.intValue()),edgeLabel);
			graph.addEdge(e);
		}
	}

	public void endElement(String uri, String name, String qName) {
	}
	
	public boolean saveGraph(File file) {
		// first assign unique scores to the nodes, iterate up to
		// id of 10000, should be enough
		ArrayList<Node> reassignNodes = graph.findNodesWithScore(0.0);
		int i = 1;
		while(i< 10000 || reassignNodes.size() >0) {
			ArrayList<Node> nodes = graph.findNodesWithScore(i);
			if(nodes.size() == 0) {
				if(reassignNodes.size() > 0) {
					Node changeScore = (Node)reassignNodes.get(0);
					reassignNodes.remove(0);
					changeScore.setScore(i);
				}
			}
			if(nodes.size() > 1) {
				ArrayList<Node> nextReassignList = new ArrayList<Node>(nodes);
				nextReassignList.remove(0);
				reassignNodes.addAll(nextReassignList);
			}
			i++;
		}

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

			//write the graph info
			b.write(getStartElementString("graph"));
			b.write(getEndAttributesString());
			b.newLine();

			b.newLine();
		  
			//write the nodes
			int index = 0;
			for(Node n : graph.getNodes()) {
				n.setMatch(new Integer(index));
	
				b.write(getStartElementString("node"));

				b.write(" "+getAttributeString("id",n.getMatch().toString()));
				b.write(" "+getAttributeString("label",n.getLabel()));
				b.write(" "+getAttributeString("x",Integer.toString(n.getX())));
				b.write(" "+getAttributeString("y",Integer.toString(n.getY())));

				b.write(getEndEmptyElementString());
				b.newLine();
				index++;
			}

			//write the edges
			for(Edge e : graph.getEdges()){
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				b.write(getStartElementString("edge"));
				b.write(" "+getAttributeString("from",n1.getMatch().toString()));
				b.write(" "+getAttributeString("to",n2.getMatch().toString()));
				b.write(" "+getAttributeString("label",e.getLabel()));
				b.write(getEndEmptyElementString());
				b.newLine();
			}
			b.write(getEndElementString("graph"));
			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing TreasureXML.saveGraph("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;

	}
	
	public static String getAttributeString(String attName,String attValue) {
		return(attName+"='"+attValue+"'");
	}
	
	public static String getStartElementString(String name) {
		return("<"+name);
	}

	public static String getEndElementString(String name) {
		return("</"+name+">");
	}

	public static String getEndEmptyElementString() {
		return("/>");
	}
	
	public static String getEndAttributesString() {
		return(">");
	}

}

