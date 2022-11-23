package euler.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import euler.ConcreteContour;
import euler.DiagramPanel;
import euler.construction.ConstructedConcreteDiagram;
import euler.piercing.PiercingDiagramPanel;



public class EditContourDialog extends JDialog implements ActionListener {

	protected DiagramPanel parentPanel;	
	protected String contourLabeltext;
	protected String xText;
	protected String yText;
	protected JPanel panel;
	protected JTextField contourlabelField;
	protected JTextField xField;
	protected JTextField yField;
	protected JButton okButton;
	protected JButton cancelButton;
	protected ConcreteContour selectedContour;
	protected boolean isEuler = false;
	protected boolean isPiercing = false;


/** Edge list must have at least one element */
	public EditContourDialog(EulerDiagramPanel ep, Frame containerFrame) {

		super(containerFrame,"Edit Contour Diagram",true);
		isEuler = true;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);		
		parentPanel = ep;
		panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
		
	}
	public EditContourDialog(PiercingDiagramPanel ep, Frame containerFrame) {

		super(containerFrame,"Edit Contour Diagram",true);
		isPiercing = true;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);		
		parentPanel = ep;
		panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	public ConcreteContour getSelectedContour(){return selectedContour;}

	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {

		contourlabelField = new JTextField(32);
		contourlabelField.setText(contourLabeltext);
		contourlabelField.setCaretPosition(0);
	//	abstractDescriptionField.moveCaretPosition(abstractDescriptionText.length());
		JLabel absLabel = new JLabel("Contour Label: ", SwingConstants.LEFT);
		xField = new JTextField(10);
		yField = new JTextField(10);
		xField.setText(xText);
		yField.setText(yText);
		JLabel xLabel = new JLabel(" X scale %: ", SwingConstants.LEFT);
		JLabel yLabel = new JLabel(" Y scal %: ", SwingConstants.LEFT);
		
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
		gridbag.setConstraints(absLabel,c);
		widgetPanel.add(absLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(contourlabelField,c);
		widgetPanel.add(contourlabelField);
		
		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(xLabel,c);
		widgetPanel.add(xLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(xField,c);
		widgetPanel.add(xField);
		xField.requestFocus();		

		yLevel++;		
		
		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(yLabel,c);
		widgetPanel.add(yLabel);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(yField,c);
		widgetPanel.add(yField);
		yField.requestFocus();

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
   		yLevel++;	

   	
	}
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
	public void actionPerformed(ActionEvent event) {
	}

	public void okButton(ActionEvent event) {
		
		selectedContour = null;
		if((event.getModifiers() & InputEvent.BUTTON1_MASK) == 0) {    
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}
		String label = contourlabelField.getText();
		if(label != null){
			ConstructedConcreteDiagram ccd;
			if(isEuler){
				EulerDiagramPanel ep = (EulerDiagramPanel)parentPanel;
				ccd =ep.getConstructedConcreteDiagram();
			}
			else{
				PiercingDiagramPanel pp = (PiercingDiagramPanel)parentPanel;
				ccd =pp.getConstructedConcreteDiagram();
			}	
			for(ConcreteContour cc: ccd.getConcreteContours()){
				if(cc.getAbstractContour().compareTo(label)==0)
					selectedContour = cc;
			}
			if(selectedContour == null){
				JOptionPane.showMessageDialog(parentPanel, "Failed to find contour\n"+contourLabeltext,"Error",JOptionPane.PLAIN_MESSAGE);
			}
			dispose();				
		}	
		if(xField.getText().length()!=0 && yField.getText().length()!=0){
			double xScale = Double.parseDouble(xField.getText())*0.01;
			double yScale = Double.parseDouble(yField.getText())*0.01;
			System.out.println("x = " + xScale);
			if(selectedContour!=null)
			selectedContour.scale(xScale, yScale);			
		}
	
		
		this.getParent().repaint();
		
		
		
	}
	public void cancelButton(ActionEvent event) {
		dispose();
	}


	

}




