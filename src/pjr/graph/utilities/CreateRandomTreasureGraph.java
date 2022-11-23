package pjr.graph.utilities;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import pjr.graph.*;

/**
 * Randomize the location of the nodes in a graph in a given rectangle
 *
 * @author Peter Rodgers
 */

public class CreateRandomTreasureGraph extends GraphUtility implements Serializable, ActionListener {


	public int numberOfTreasureNodes = 7;
	public int numberOfTreasureEdges = 10;
	public int numberOfPlayerToTreasureEdges = 3;
	public int maxTreasureValue = 4;
	public String graphId = "99";
	
	public static Random random = new Random();


	JFrame frame;
	JPanel panel;
	JTextField treasureNodeField;
	JTextField treasureEdgeField;
	JTextField playerEdgeField;
	JTextField maxTreasureField;
	JTextField graphIdField;
	JButton okButton;
	
	public static void main(String[] args) {

		String graphId = "";
		String fileName = "";
		int numberOfTreasureNodes = 0;
		int numberOfTreasureEdges = 0;
		int numberOfPlayerToTreasureEdges = 0;
		int maxTreasureValue = 0;

		if (args.length == 6) {
			fileName = args[0];
			numberOfTreasureNodes = Integer.parseInt(args[1]);
			numberOfTreasureEdges = Integer.parseInt(args[2]);
			numberOfPlayerToTreasureEdges = Integer.parseInt(args[3]);
			maxTreasureValue = Integer.parseInt(args[4]);
			graphId = args[5];
		} else {
			System.out.println("Must have 6 parameters: fileName treasureNodes treasureEdges playerToTreasureEdges maxTreasureValue graphId");
			System.exit(0);
		}
		
		Graph graph = new Graph();
		File file = new File(fileName);
		
		if(!generateConnectedTreasureGraph(graph,numberOfTreasureNodes,numberOfTreasureEdges,numberOfPlayerToTreasureEdges,maxTreasureValue)) {
			System.out.println("Exiting without saving");
			System.exit(0);
		}
		
		TreasureXML xml = new TreasureXML(graph);
		xml.setGraphId(graphId);
		xml.saveGraph(file);
		System.out.println("Saving "+file);
	}


/** Trivial constructor. */
	public CreateRandomTreasureGraph() {
		super(KeyEvent.VK_T,"Create Random Treasure Graph");
	}

/** Trivial constructor. */
	public CreateRandomTreasureGraph(int key, String s) {
		super(key,s);
	}

	public CreateRandomTreasureGraph(int key, String s, int mnemonic) {
		super(key,s,mnemonic);
	}


	public void apply() {
		createFrame();
	}


	protected void createFrame() {

		frame = new JFrame("Create Random Graph");
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();

		panel.setLayout(gridbag);

		addWidgets(panel,gridbag);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		JLabel label;
		
		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		treasureNodeField = new JTextField(4);
		treasureNodeField.setText(Integer.toString(numberOfTreasureNodes));
		label = new JLabel("Number of Treasure Nodes: ", SwingConstants.LEFT);
		c.gridx = 0;
		c.gridy = 0;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		c.gridx = 1;
		c.gridy = 0;
		gridbag.setConstraints(treasureNodeField,c);
		widgetPanel.add(treasureNodeField);
		treasureNodeField.requestFocus();

		treasureEdgeField = new JTextField(4);
		treasureEdgeField.setText(Integer.toString(numberOfTreasureEdges));
		label = new JLabel("Number of Edges Between Treasure Nodes: ", SwingConstants.LEFT);
		c.gridx = 0;
		c.gridy = 1;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		c.gridx = 1;
		c.gridy = 1;
		gridbag.setConstraints(treasureEdgeField,c);
		widgetPanel.add(treasureEdgeField);

		playerEdgeField = new JTextField(4);
		playerEdgeField.setText(Integer.toString(numberOfPlayerToTreasureEdges));
		label = new JLabel("Number of Edges Between a Player and Treasure Nodes: ", SwingConstants.LEFT);
		c.gridx = 0;
		c.gridy = 2;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		c.gridx = 1;
		c.gridy = 2;
		gridbag.setConstraints(playerEdgeField,c);
		widgetPanel.add(playerEdgeField);
		
		maxTreasureField = new JTextField(4);
		maxTreasureField.setText(Integer.toString(maxTreasureValue));
		label = new JLabel("Maximum Treasure Value: ", SwingConstants.LEFT);
		c.gridx = 0;
		c.gridy = 3;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		c.gridx = 1;
		c.gridy = 3;
		gridbag.setConstraints(maxTreasureField,c);
		widgetPanel.add(maxTreasureField);

		graphIdField = new JTextField(4);
		graphIdField.setText(graphId);
		label = new JLabel("Graph Id: ", SwingConstants.LEFT);
		c.gridx = 0;
		c.gridy = 4;
		gridbag.setConstraints(label,c);
		widgetPanel.add(label);
		c.gridx = 1;
		c.gridy = 4;
		gridbag.setConstraints(graphIdField,c);
		widgetPanel.add(graphIdField);

		okButton = new JButton("OK");
		frame.getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(this);

		c.gridx = 0;
		c.gridy = 5;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

	}


	protected void randomizeGraph() {
		Graph graph = getGraph();
		
		generateConnectedTreasureGraph(graph,numberOfTreasureNodes,numberOfTreasureEdges,numberOfPlayerToTreasureEdges,maxTreasureValue);

		getGraphPanel().update(getGraphPanel().getGraphics());
	}


	public static boolean generateConnectedTreasureGraph(Graph graph, int treasureNodes, int treasureEdges, int playerToTreasureEdges, int maxTreasureValue) {
		int maxCount =100;
		int count =1;
		boolean success = false;
		while(!success) {
			if (count >= maxCount) {
				System.out.println("Failed to generate a connected graph after "+count+ " attempts");
				graph.clear();
				return false;
			}
			count++;
			if(generateTreasureGraph(graph, treasureNodes,treasureEdges,playerToTreasureEdges,maxTreasureValue)) {
				if(graph.connected()) {
					success = true;
				}
			}
		}
		
		return true;
	}



	public static boolean generateTreasureGraph(Graph graph, int treasureNodes, int treasureEdges, int playerToTreasureEdges, int maxTreasureValue) {
		final int maxAttempts = 1000;
		
		graph.clear();
		
		if(treasureNodes < 1) {
			System.out.println("Must specify more than 0 treasure nodes");
			return false;
		}
		if(treasureNodes != 1 && treasureEdges < 1) {
			System.out.println("Must specify more than 0 treasure edges");
			return false;
		}
		if(playerToTreasureEdges < 1) {
			System.out.println("Must specify more than 0 player to treasure edges");
			return false;
		}
		if(maxTreasureValue < 1) {
			System.out.println("Max Treasure value must be greater than 0");
			return false;
		}
		if(treasureNodes < playerToTreasureEdges) {
			System.out.println("Must have enough treasure nodes for the treasure edges");
			return false;
		}
		
		graph.clear();
		for(int i = 0; i < treasureNodes; i++) {
			String label = Integer.toString(1+random.nextInt(maxTreasureValue));
			Node n = new Node(label);
			graph.addNode(n);
		}
		for(int j = 0; j < treasureEdges; j++) {
			ArrayList nodeList = graph.getNodes();
			Node firstEnd = (Node)nodeList.get(random.nextInt(nodeList.size()));
			
			ArrayList lowConnectionNodeList = graph.getNodesWithFewEdges(1);
			if(lowConnectionNodeList.size() != 0) {
				firstEnd = (Node)lowConnectionNodeList.get(random.nextInt(lowConnectionNodeList.size()));
			}

			Node otherEnd = null;
			boolean success = false;
			int count = 0;
			while(!success) {
				count++;
				if(count >= maxAttempts) {
					System.out.println("Cant create a new treasure edge after "+count+" attempts");
					return false;
				}
				otherEnd = (Node)nodeList.get(random.nextInt(nodeList.size()));
				if(otherEnd != firstEnd && !firstEnd.connectingNodes().contains(otherEnd)) {
					success = true;	
				}
			}
			Edge e = new Edge(firstEnd,otherEnd);
			graph.addEdge(e);
		}
		
		ArrayList<Node> treasureNodeList = new ArrayList<Node>(graph.getNodes());
		
		Node start = new Node("start");
		Node end = new Node("end");
		
		graph.addNode(start);
		graph.addNode(end);
		
		for(int j = 0; j < playerToTreasureEdges; j++) {
			Node otherEnd = null;
			boolean success = false;
			int count = 0;
			while(!success) {
				count++;
				if(count >= maxAttempts) {
					System.out.println("Cant create a player to treasure edge after "+count+" attempts");
					return false;
				}
				otherEnd = (Node)treasureNodeList.get(random.nextInt(treasureNodeList.size()));
				if(!start.connectingNodes().contains(otherEnd)) {
					success = true;	
				}
			}
			Edge eStart = new Edge(start,otherEnd);
			graph.addEdge(eStart);
			Edge eEnd = new Edge(end,otherEnd);
			graph.addEdge(eEnd);
		}

		graph.randomizeNodePoints(new Point(30,30),350,350);
		return true;
	}

	public void actionPerformed(ActionEvent event) {

		numberOfTreasureNodes = (int)(Double.parseDouble(treasureNodeField.getText()));
		numberOfTreasureEdges = (int)(Double.parseDouble(treasureEdgeField.getText()));
		numberOfPlayerToTreasureEdges = (int)(Double.parseDouble(playerEdgeField.getText()));
		maxTreasureValue = (int)(Double.parseDouble(maxTreasureField.getText()));

		randomizeGraph();

		graphId = graphIdField.getText();
//		GraphWindow gw = (GraphWindow)getGraphPanel().getContainerFrame();
//		gw.treasureXML.setGraphId(graphId);
		

		getGraphPanel().requestFocus();
		frame.dispose();
	}

}
