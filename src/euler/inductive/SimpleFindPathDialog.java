package euler.inductive;

import javax.swing.*;
import javax.swing.border.Border;

import euler.*;

import pjr.graph.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;


/**
 * @author Peter Rodgers
 */
public class SimpleFindPathDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected InductiveWindow iw;
	protected JPanel parentPanel;
	
	protected HybridGraph hg;
	protected String label;
	protected String splitText = "";
	protected String containedText = "";
	protected Vector<String> compLabels;
	protected String allZones = null;
	protected boolean OKPressed = false;
	/** Set to true if the path was found and euler graph created. */
	protected boolean success = false;
	protected long pathTime = 0;
	
	protected JPanel panel;
	protected JTextField containedField;
	protected JTextField splitField;
	protected JTextField labelField;
	protected JList compList;
	protected JButton okButton;
	protected JButton cancelButton;



/** Edge list must have at least one element */
	public SimpleFindPathDialog(HybridGraph hg, String label, InductiveWindow iw, ArrayList<String> zoneList, Frame containerFrame) {

		super(containerFrame,"Find Path",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		allZones = "";
		AbstractDiagram.sortZoneList(zoneList);
		for(String z : zoneList) {
			allZones += z+" ";
		}
		allZones.trim();
		
		this.iw = iw;
		parentPanel = iw.getDiagramPanel();
		this.hg = hg;
		this.label = label;
		OKPressed = false;

		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);

		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	public boolean getOKPressed() {return OKPressed;}
	public boolean getSuccess() {return success;}
	public long getPathTime() {return pathTime;}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {
		
		JLabel zonesLabel = new JLabel("Current Zones: "+allZones, SwingConstants.LEFT);

		splitField = new JTextField(32);
		splitField.setText(splitText);
		splitField.setCaretPosition(0);
		splitField.moveCaretPosition(splitText.length());
		JLabel splitLabel = new JLabel("Zones To Split: ", SwingConstants.LEFT);
		
		containedField = new JTextField(32);
		containedField.setText(containedText);
		containedField.setCaretPosition(0);
		containedField.moveCaretPosition(containedText.length());
		JLabel containedLabel = new JLabel("Zones To Be Contained: ", SwingConstants.LEFT);

		labelField = new JTextField(6);
		labelField.setText(label);
		JLabel labelLabel = new JLabel("New Contour Label: ", SwingConstants.LEFT);
		
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);

		JLabel compLabel = new JLabel("Comparator: ", SwingConstants.LEFT);
		
		compLabels = new Vector<String>();
		compLabels.add("First Path Found");
		for(String c : HybridGraph.compStrings()) {
			compLabels.add(c);
		}
		compList = new JList(compLabels);
		compList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		compList.setSelectedIndex(0);

		JScrollPane scrollBar = new JScrollPane(compList);
		scrollBar.setPreferredSize(new Dimension(150, 100));
		scrollBar.setBorder(lineBorder);
		
		
		okButton = new JButton("OK");
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okButton(event);
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelButton(event);
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		c.ipadx = 5;
		c.ipady = 5;

		int yLevel = 0;

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(zonesLabel,c);
		widgetPanel.add(zonesLabel);

		yLevel++;
		
		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(splitLabel,c);
		widgetPanel.add(splitLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(splitField,c);
		widgetPanel.add(splitField);
		labelField.requestFocus();

		yLevel++;
		
		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(containedLabel,c);
		widgetPanel.add(containedLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(containedField,c);
		widgetPanel.add(containedField);
		containedField.requestFocus();

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(labelLabel,c);
		widgetPanel.add(labelLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(labelField,c);
		widgetPanel.add(labelField);
		labelField.requestFocus();

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(compLabel,c);
		widgetPanel.add(compLabel);
		c.gridx = 1;
		
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(compList,c);
		widgetPanel.add(compList);
		labelField.requestFocus();


		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(cancelButton,c);
   		widgetPanel.add(cancelButton);

	}

	public void actionPerformed(ActionEvent event) {
	}


	public void okButton(ActionEvent event) {
		
		if((event.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
    
// if the button has been initiated by a non button press, and the
// cancel button has the focus, redirect to the cancel 
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}

		String splitText = splitField.getText();
		String containedText = containedField.getText();
		String label = labelField.getText();
		
		ArrayList<String> splitZones = new ArrayList<String>();
		StringTokenizer splitTokenizer = new StringTokenizer(splitText,"	 ,;[]");
		while (splitTokenizer.hasMoreTokens()) {
			String zone = splitTokenizer.nextToken();
			if(zone.equals("") || zone.equals("O") || zone.equals("0")) {
				zone = "";
			}
			splitZones.add(zone);
		}
		Collections.sort(splitZones);
		
		ArrayList<String> containedZones = new ArrayList<String>();
		StringTokenizer containedTokenizer = new StringTokenizer(containedText,"	 ,;[]");
		while (containedTokenizer.hasMoreTokens()) {
			String zone = containedTokenizer.nextToken();
			containedZones.add(zone);
		}
		Collections.sort(containedZones);
		
		boolean repeatedZone = false;
		for(String z : splitZones) {
			if(containedZones.contains(z)) {
				repeatedZone = true;
				break;
			}
		}
		
		if(repeatedZone) {
			JOptionPane.showMessageDialog(parentPanel, "Split Zones and Contained Zones have duplicated zones.","Error",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		success = false;
		int count = 0;
		ArrayList<Edge> path = null;
		while(!success) {
System.out.println("Path search iteration "+(count+1));
			long startPathTime = System.currentTimeMillis();

			int index = compList.getSelectedIndex();
			boolean findFirstPath = false;
			if(index == 0) {
				findFirstPath = true;
System.out.println("find first path, split: "+splitZones+", contained: "+containedZones);
			} else {
				String compString = compLabels.get(index);
				hg.setCompString(compString);
System.out.println("comparator "+compString+", split: "+splitZones+", contained: "+containedZones);
			}

			path = hg.findSimplePath(splitZones,containedZones,count,findFirstPath);

			long endPathTime = System.currentTimeMillis();
			pathTime = endPathTime-startPathTime;
System.out.println("FindPathDialog findSimplePath "+(pathTime)/1000.0);

			if(path == null) {
				JOptionPane.showMessageDialog(parentPanel, "FAILED to find path for splitZones: "+splitText+" and containedZones: "+containedText+". A simple path may not exist.","Error",JOptionPane.PLAIN_MESSAGE);
				return;
			}

			DualGraph eg = hg.eulerGraphWithEdgePath(label,path);

			if(eg == null) {
				System.out.println("FAILED to create new contour due failure to create Euler graph with path "+path);
				count++;
				continue;
			}

			ArrayList<String> zoneList = HybridGraph.findZoneList(hg.getLargeGraph());
			for(String splitZone : splitZones){
				zoneList.add(splitZone+label);
			}
			for(String containedZone : containedZones) {
				zoneList.remove(containedZone);
				zoneList.add(containedZone+label);
			}
			AbstractDiagram.sortZoneList(zoneList);
			String zones = "";
			for(String z : zoneList) {
				zones += z+" ";
			}
			zones.trim();

			new EulerGraphWindow("Euler Graph for: "+zones,eg);
			
			success = true;
			
		}
		
		if(!success) {
			JOptionPane.showMessageDialog(parentPanel, "Failed to find contour for split zones: "+splitText+" and contained zones: "+containedText,"Error",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		OKPressed = true;
		
		dispose();
	}


	public void cancelButton(ActionEvent event) {
		OKPressed = false;
		dispose();
	}

}



