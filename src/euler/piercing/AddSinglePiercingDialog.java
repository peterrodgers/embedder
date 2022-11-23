package euler.piercing;

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

public class AddSinglePiercingDialog extends JDialog implements ActionListener {

	JPanel panel;	
	JPanel parentPanel;	
	String curveToAddText;
	JTextField curveToAddField;
	String curvePiercedToText;
	JTextField curvePiercedToField;
	String outerCurveText;
	JTextField outerCurveField;
	JButton okButton;
	JButton cancelButton;
	PiercingDiagramWindow piercingDiagramWindow;

/** Edge list must have at least one element */
	public AddSinglePiercingDialog(PiercingDiagramWindow pdw, Frame containerFrame) {

		super(containerFrame,"Search Diagram",true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(containerFrame);		
		parentPanel = pdw.getPiercingDiagramPanel();
		piercingDiagramWindow = pdw;
		panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		panel.setLayout(gridbag);
		addWidgets(panel,gridbag);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	protected void addWidgets(JPanel widgetPanel, GridBagLayout gridbag) {
		
		curveToAddField = new JTextField(32);
		curveToAddField.setText(curveToAddText);
		curveToAddField.setCaretPosition(0);
		
	
		JLabel curveToAdd = new JLabel("Curve Label: ", SwingConstants.LEFT);
		
		curvePiercedToField = new JTextField(32);
		curvePiercedToField.setText(curvePiercedToText);
		curvePiercedToField.setCaretPosition(0);
		
		JLabel curvePiercedTo = new JLabel("Pierced to: ", SwingConstants.LEFT);
		
		
		outerCurveField = new JTextField(32);
		outerCurveField.setText(outerCurveText);
		outerCurveField.setCaretPosition(0);		
		JLabel outerCurves = new JLabel("Outer Curves (no space): ", SwingConstants.LEFT);
		
		
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
		gridbag.setConstraints(curveToAdd,c);
		widgetPanel.add(curveToAdd);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(curveToAddField,c);
		widgetPanel.add(curveToAddField);

		yLevel++;
		
		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(curvePiercedTo,c);
		widgetPanel.add(curvePiercedTo);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(curvePiercedToField,c);
		widgetPanel.add(curvePiercedToField);

		yLevel++;
		
		c.gridx = 0;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(outerCurves,c);
		widgetPanel.add(outerCurves);

		c.gridx = 1;
		c.gridy = yLevel;
		c.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(outerCurveField,c);
		widgetPanel.add(outerCurveField);

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
		String curveLabel = curveToAddField.getText();
		String piercedTo = curvePiercedToField.getText();
		String outer = outerCurveField.getText();
		if(piercingDiagramWindow.getPiercingDiagram().getCurve(piercedTo)==null){
			JOptionPane.showMessageDialog(parentPanel, "Error, pierced to a curve that doesn't exist in the diagram!","",JOptionPane.PLAIN_MESSAGE);
			dispose();
		}
		else{
			if(curveLabel!= null){
				if(outer==null)
					outer="";
				SinglePiercingCurve spc = new SinglePiercingCurve(curveLabel,piercedTo,outer);
				PiercingDiagram pd = piercingDiagramWindow.getPiercingDiagram();
				pd.addNewSinglePiercing(spc);
				PiercingDiagramPanel edp = piercingDiagramWindow.getPiercingDiagramPanel();
				//piercingDiagramWindow.setPiercingDiagram(pd);
				ConstructedConcreteDiagram ccd = pd.generateConstructedConcreteDiagram();
				 if(ccd!=null){				
					 edp.setConstructedConcreteDiagram(ccd);
					 //piercingDiagramWindow.setPiercingDiagram(pd);
					 edp.setPiercingDiagram(pd);
					 edp.update(edp.getGraphics());
					 dispose();
				 }
				}
			else{
				JOptionPane.showMessageDialog(parentPanel, "Error, invalid curve label!","",JOptionPane.PLAIN_MESSAGE);
				dispose();
			}
		}	
	}
	public void cancelButton(ActionEvent event) {
		dispose();
	}

}



