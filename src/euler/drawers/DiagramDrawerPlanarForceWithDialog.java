package euler.drawers;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import euler.*;



/**
 * A version of Eades spring embedder for laying out graphs
 * Selected nodes are not moved, but still participate in the
 * force calculation
 *
 * @author Peter Rodgers
 */

public class DiagramDrawerPlanarForceWithDialog extends DiagramDrawer implements ActionListener {

	PlanarForceLayout pfl = new PlanarForceLayout();

	JFrame frame;
	JPanel panel;
	JTextField kField;
	JTextField rField;
	JTextField qField;
	JTextField fField;
	JTextField iterationsField;

	JButton okButton;


/** Trivial constructor. */
	public DiagramDrawerPlanarForceWithDialog(int key, String s, int accelerator) {
		super(key,s,accelerator);
	}



/** Draws the graph. */
	public void layout() {
		createFrame();
	}
	
	protected void createFrame() {

		frame = new JFrame("Planar Forced Layout Options");
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();

		panel.setLayout(gridbag);

		addWidgets(panel,gridbag);

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}
	
	
	protected void drawGraph() {
		DiagramPanel dp = getDiagramPanel();
		pfl.setDiagramPanel(dp);
		pfl.drawGraph();

	}



	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {


		kField = new JTextField(6);
		kField.setText(Double.toString(pfl.getK()));
		JLabel kLabel = new JLabel("Strength of Connected Node Attraction, K (SE): ", SwingConstants.LEFT);

		rField = new JTextField(6);
		rField.setText(Double.toString(pfl.getR()));
		JLabel rLabel = new JLabel("Strength of Node-Node Repulsion, R (SE): ", SwingConstants.LEFT);

		qField = new JTextField(6);
		qField.setText(Double.toString(pfl.getQ()));
		JLabel qLabel = new JLabel("Strength of Node-Edge Repulsion, Q: ", SwingConstants.LEFT);

		fField = new JTextField(6);
		fField.setText(Double.toString(pfl.getF()));
		JLabel fLabel = new JLabel("Total Force Multiplier, F: ", SwingConstants.LEFT);

		iterationsField = new JTextField(6);
		iterationsField.setText(Integer.toString(pfl.getIterations()));
		JLabel iterationsLabel = new JLabel("Iterations: ", SwingConstants.LEFT);


		okButton = new JButton("OK");
		frame.getRootPane().setDefaultButton(okButton);

		okButton.addActionListener(this);

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;

		int yLevel = 0;


		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(kLabel,c);
		widgetPanel.add(kLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(kField,c);
		widgetPanel.add(kField);
		kField.requestFocus();

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(rLabel,c);
		widgetPanel.add(rLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(rField,c);
		widgetPanel.add(rField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(qLabel,c);
		widgetPanel.add(qLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(qField,c);
		widgetPanel.add(qField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(fLabel,c);
		widgetPanel.add(fLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(fField,c);
		widgetPanel.add(fField);

		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(iterationsLabel,c);
		widgetPanel.add(iterationsLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		gridbag.setConstraints(iterationsField,c);
		widgetPanel.add(iterationsField);

		yLevel++;


		c.gridx = 0;
		c.gridy = yLevel;
		gridbag.setConstraints(okButton,c);
   		widgetPanel.add(okButton);

	}


	public void actionPerformed(ActionEvent event) {

		double k = (Double.parseDouble(kField.getText()));
		pfl.setK(k);
		double r = (Double.parseDouble(rField.getText()));
		pfl.setR(r);
		double q = (Double.parseDouble(qField.getText()));
		pfl.setQ(q);
		double f = (Double.parseDouble(fField.getText()));
		pfl.setF(f);
		double iterations = (Integer.parseInt(iterationsField.getText()));
		pfl.setIterations(pjr.graph.Util.convertToInteger(iterations));

		frame.dispose();
		getDiagramPanel().update(getDiagramPanel().getGraphics());
		getDiagramPanel().requestFocus();
		
		drawGraph();
	}

}
