package euler.inductive;

import javax.swing.*;

import euler.*;
import euler.display.*;

import pjr.graph.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


/**
 * @author Peter Rodgers
 */
public class AddPathDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	protected InductiveWindow iw;
	protected JPanel parentPanel;
	
	protected HybridGraph hg;
	protected String label;
	protected String pathText;
	
	protected JPanel panel;
	protected JTextField pathField;
	protected JTextField labelField;
	protected JButton okButton;
	protected JButton cancelButton;


/** Edge list must have at least one element */
	public AddPathDialog(HybridGraph hg, String pathText, String label, InductiveWindow iw, Frame containerFrame) {

		super(containerFrame,"Add Path",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		this.iw = iw;
		parentPanel = iw.getDiagramPanel();
		this.hg = hg;
		this.pathText = pathText;
		this.label = label;

		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);

		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		
		pathField = new JTextField(32);
		pathField.setText(pathText);
		pathField.setCaretPosition(0);
		pathField.moveCaretPosition(pathText.length());
		JLabel pathLabel = new JLabel("Path: ", SwingConstants.LEFT);

		labelField = new JTextField(6);
		labelField.setText(label);
		JLabel labelLabel = new JLabel("Contour Label: ", SwingConstants.LEFT);

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

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(pathLabel,c);
		widgetPanel.add(pathLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(pathField,c);
		widgetPanel.add(pathField);
		labelField.requestFocus();

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
		
		DualGraph largeGraph = hg.getLargeGraph();
		
		if((event.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {
    
// if the button has been initiated by a non button press, and the
// cancel button has the focus, redirect to the cancel 
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}

		String pathText = pathField.getText();
		String label = labelField.getText();
		
		ArrayList<Node> path = new ArrayList<Node>();
		
		StringTokenizer st = new StringTokenizer(pathText,"	 ,;[]");

		while (st.hasMoreTokens()) {
			String nodeLabel = st.nextToken();
			Node n = largeGraph.firstNodeWithLabel(nodeLabel);
			path.add(n);
		}
		

		DualGraph eg = HybridGraph.eulerGraphWithNodePath(label,path,largeGraph);
		if(eg == null) {
			JOptionPane.showMessageDialog(parentPanel, "Failed to find path\n"+pathText,"Error",JOptionPane.PLAIN_MESSAGE);
		} else {
			HybridGraph newHG = new HybridGraph(eg);
			iw.setHybridGraph(newHG);
			iw.getDiagramPanel().requestFocus();
			parentPanel.update(parentPanel.getGraphics());
System.out.println("A hg minY "+newHG.findMinimumY()+" height "+newHG.findMinimumY()+" centre "+newHG.findCentre());
			dispose();
		}
		
	}


	public void cancelButton(ActionEvent event) {
		dispose();
	}

}



