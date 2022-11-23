package pjr.graph;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


/**
 */
public class TreasureXML extends DefaultHandler {
	
	protected Graph graph;
	
	protected String graphId = "";
	protected String graphCollection = "treasure_graphs";
	protected String gameName = "TreasureCollectionGame";
	
	
	public static void main (String args[]) {
		File currentFile = new File("c:\\code\\test.xml");
		Graph graph = new Graph();

		TreasureXML xmlReader = new TreasureXML(graph);
		xmlReader.loadGraph(currentFile);
		
		File outFile = new File("c:\\code\\testout.xml");
		xmlReader.saveGraph(outFile);

		System.out.println(graph);
	}
	
	public TreasureXML (Graph graph) {
		super();
		this.graph = graph;
	}
	
	
	public Graph getGraph() {return graph;}
	public String getGameName() {return gameName;}
	public String getGraphCollection() {return graphCollection;}
	public String getGraphId() {return graphId;}
	
	public void setGameName(String name) {gameName = name;}
	public void setGraphCollection(String collection) {graphCollection = collection;}
	public void setGraphId(String id) {graphId = id;}
	
	public boolean loadGraph(File file) {
		graph.clear();
		try {
// cant reliably get an XML reader without using the depreciated SAXParser
// this
//			XMLReader parser = new org.apache.crimson.parser.XMLReaderImpl();
// replaced by
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLReader parser = saxParser.getXMLReader();
// to here
//			 this also fails
//			XMLReader parser = XMLReaderFactory.createXMLReader();
			
			parser.setContentHandler(this);
			parser.setErrorHandler(this);
			FileReader r = new FileReader(file);
			parser.parse(new InputSource(r));
		} catch(Exception e) {
			System.out.println("An exception occured "+e+"\n");
			return false;
		}
		return true;
	}

	public void startDocument () {
//		System.out.println("Start document");
	}

	public void endDocument () {
//		System.out.println("End document");
	}
	
	public void startElement(String uri, String name, String qName, Attributes atts) {
/*
System.out.println("Start element: " + qName);
for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
	System.out.print(atts.getQName(attIndex)+"-"+atts.getValue(attIndex)+"\n");
}
*/
		if(qName.equals("graph")) {
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("id")) {
					graphId = attValue;
				}
				if(attName.equals("collection")) {
					graphCollection = attValue;
				}
			}

		}
		if(qName.equals("game")) {
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("name")) {
					gameName = attValue;
				}
			}

		}
		if(qName.equals("vertex")) {
			Node n = new Node();
			n.setScore(-1);
			n.setMatch("");
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("id")) {
					n.setScore(Integer.parseInt(attValue));
				}
				if(attName.equals("x")) {
					n.setX(Integer.parseInt(attValue));
				}
				if(attName.equals("y")) {
					n.setY(Integer.parseInt(attValue));
				}
				if(attName.equals("value")) {
					n.setLabel(attValue);
				}
				if(attName.equals("start") && attValue.equals("y")) {
					n.setLabel("start");
				}
				if(attName.equals("end") && attValue.equals("y")) {
					n.setLabel("end");
				}
			}
			graph.addNode(n);
		}

		if(qName.equals("edge")) {
			double nodeId1 = 0; 
			double nodeId2 = 0; 
			String edgeLabel = "";
			for(int attIndex = 0; attIndex < atts.getLength(); attIndex++){
				String attName = atts.getQName(attIndex);
				String attValue = atts.getValue(attIndex);
				if(attName.equals("v1")) {
					nodeId1 = Double.parseDouble(attValue);
				} 
				if(attName.equals("v2")) {
					nodeId2 = Double.parseDouble(attValue);
				} 
				if(attName.equals("value")) {
					edgeLabel = attValue;
				}
			}

			Node node1 = (Node)graph.findNodesWithScore(nodeId1).get(0);
			Node node2 = (Node)graph.findNodesWithScore(nodeId2).get(0);
			Edge e = new Edge(node1,node2,edgeLabel);
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
			b.write(GeneralXML.getStartElementString("graph"));
			b.write(" "+GeneralXML.getAttributeString("id",graphId));
			b.write(" "+GeneralXML.getAttributeString("collection",graphCollection));
			b.write(GeneralXML.getEndAttributesString());
			b.newLine();
			//write the game info
			b.write(GeneralXML.getStartElementString("game"));
			b.write(" "+GeneralXML.getAttributeString("name",gameName));
			b.write(GeneralXML.getEndEmptyElementString());

			b.newLine();
		  
			//write the nodes
			for(Node n : graph.getNodes()) {
	
				b.write(GeneralXML.getStartElementString("vertex"));
				b.write(" "+GeneralXML.getAttributeString("id",Integer.toString((int)n.getScore())));
				b.write(" "+GeneralXML.getAttributeString("x",Integer.toString(n.getX())));
				b.write(" "+GeneralXML.getAttributeString("y",Integer.toString(n.getY())));

				
				if(n.getLabel().equals("start") || n.getLabel().equals("end")) {
					b.write(" "+GeneralXML.getAttributeString(n.getLabel(),"y"));
				} else {
					b.write(" "+GeneralXML.getAttributeString("value",n.getLabel()));
				}
				b.write(GeneralXML.getEndEmptyElementString());
				
				b.newLine();

			}

			//write the edges
			for(Edge e : graph.getEdges()){
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				b.write(GeneralXML.getStartElementString("edge"));
				b.write(" "+GeneralXML.getAttributeString("v1",Integer.toString((int)n1.getScore())));
				b.write(" "+GeneralXML.getAttributeString("v2",Integer.toString((int)n2.getScore())));
				b.write(GeneralXML.getEndEmptyElementString());

				b.newLine();
			}
			b.write(GeneralXML.getEndElementString("graph"));
			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when executing TreasureXML.saveGraph("+file.getName()+") in Graph.java: "+e+"\n");
			return false;
		}
		return true;

	}

}

