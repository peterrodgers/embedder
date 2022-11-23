package euler.library;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import euler.construction.ConstructedConcreteDiagram;


public class SearchDiagramDialog extends JDialog implements ActionListener {

	
	JPanel parentPanel;	
	String abstractDescriptionText;	
	JPanel panel;
	JTextField abstractDescriptionField;
	JButton okButton;
	JButton cancelButton;
	EulerDiagramWindow ew;

/** Edge list must have at least one element */
	public SearchDiagramDialog(EulerDiagramWindow ew, Frame containerFrame) {

		super(containerFrame,"Search Diagram",true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);		
		parentPanel = ew.getEulerDiagramPanel();
		this.ew = ew;
		panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {
		
		abstractDescriptionField = new JTextField(32);
		abstractDescriptionField.setText(abstractDescriptionText);
		abstractDescriptionField.setCaretPosition(0);
	//	abstractDescriptionField.moveCaretPosition(abstractDescriptionText.length());
		JLabel absLabel = new JLabel("Abstract Description: ", SwingConstants.LEFT);		
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
		gridbag.setConstraints(abstractDescriptionField,c);
		widgetPanel.add(abstractDescriptionField);

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

		String abstractDescription = abstractDescriptionField.getText();
		
		if(abstractDescription != null){
			Library library = ew.getLibrary();
			if(library!=null){
				 ConstructedConcreteDiagram ccd = library.getDiagram(abstractDescription);
				 if(ccd!=null){
					 EulerDiagramPanel edp = ew.getEulerDiagramPanel();
					 edp.setConstructedConcreteDiagram(ccd);
					 edp.update(edp.getGraphics());
					 dispose();
				 }
			}	
		}		
		else{
			JOptionPane.showMessageDialog(parentPanel, "Failed to find path\n"+abstractDescriptionText,"Error",JOptionPane.PLAIN_MESSAGE);
			dispose();
		} 
	}
	public void cancelButton(ActionEvent event) {
		dispose();
	}

}



