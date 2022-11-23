package euler.library;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import euler.AbstractDiagram;
import euler.DiagramPanel;
import euler.DualGraph;
import euler.drawers.DiagramDrawer;
import euler.utilities.DiagramUtility;
import euler.views.DiagramView;

public class LibraryWindow extends JFrame implements ActionListener{
	
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	protected DiagramPanel dp = null;
	protected int width = WIDTH;
	protected int height = HEIGHT;	
	
	public LibraryWindow() {
		super("Library Test");
		setup();
	}	

	
	protected void setup() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width,height);
		DualGraph dg = new DualGraph(new AbstractDiagram("0 a b ab"));
		dp = new DiagramPanel(dg);		
		getContentPane().add(dp);
		initView();
		initUtility();
		initLayout();
		setVisible(true);
		dp.requestFocus();
	}

	private void initView() {
		
	}

	
	private void initUtility() {

	}


	private void initLayout() {
		}
	

	public void actionPerformed(ActionEvent event) {
		JMenuItem source = (JMenuItem)(event.getSource());

		for(DiagramView v : dp.getDiagramViewList()) {
			if (v.getMenuText().equals(source.getText())) {
				v.view();
				repaint();
				return;
			}
		}
		for(DiagramDrawer d : dp.getDiagramDrawerList()) {
			if (d.getMenuText().equals(source.getText())) {
				d.layout();
				repaint();
				return;
			}
		}

		for(DiagramUtility u : dp.getDiagramUtilityList()) {
			if (u.getMenuText().equals(source.getText())) {
				u.apply();
				return;
			}
		}

	}
}
