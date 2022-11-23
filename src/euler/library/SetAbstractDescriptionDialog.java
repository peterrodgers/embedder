package euler.library;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import pjr.graph.Node;


public class SetAbstractDescriptionDialog extends JDialog implements ActionListener {

	EulerDiagramWindow ew;
	JPanel parentPanel;	
	String abstractDescriptionText;	
	JPanel panel;
	JTextField abstractDescriptionField;
	JButton okButton;
	JButton cancelButton;


/** Edge list must have at least one element */
	public SetAbstractDescriptionDialog(String abText, EulerDiagramWindow ew, Frame containerFrame) {

		super(containerFrame,"Set Abstract Description",true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);

		this.ew = ew;
		parentPanel = ew.getEulerDiagramPanel();

		this.abstractDescriptionText = abText;
		panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}


	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {
	
		abstractDescriptionField = new JTextField(60);
		abstractDescriptionField.setText(abstractDescriptionText);
		abstractDescriptionField.setCaretPosition(0);
		abstractDescriptionField.moveCaretPosition(abstractDescriptionText.length());
		JLabel pathLabel = new JLabel("Abstract Description: ", SwingConstants.LEFT);

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
		yLevel++;

		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
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
			if(cancelButton.isFocusOwner()) {
				cancelButton(event);
				return;
			}
		}
		abstractDescriptionText = abstractDescriptionField.getText();
	}
	public String getAbstractDescription(){
		return abstractDescriptionText;
	}


	public void cancelButton(ActionEvent event) {
		dispose();
	}

}



