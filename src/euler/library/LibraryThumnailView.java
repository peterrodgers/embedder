package euler.library;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import euler.construction.ConstructedConcreteDiagram;


public class LibraryThumnailView extends JFrame {
	
	protected Library library = null;
	protected ThumnailEulerDiagramPanel [] gridDiagrams = null;
	protected ArrayList<ConstructedConcreteDiagram> diagrams = null;
	protected JScrollPane scrollPane;
	protected EulerDiagramWindow ew = null;
	protected int selectedDiagramIdx = 0;
	
	public void setSelectedDiagramIdx(int idx){selectedDiagramIdx = idx;}
	public LibraryThumnailView(Library library) {
		super();		
		this.library = library;	
		this.setTitle("library thumnail view");
		initComponents();
	}


	public LibraryThumnailView(Library library, EulerDiagramWindow ew) {
		super();		
		this.library = library;	
		this.setTitle("library thumnail view");
		this.ew = ew;
		initComponents();
	}

	public void initComponents(){
		//this.setSize(800,800);			
		if(library != null){
			diagrams = library.getConstructedConcreteDiagrams();
			ThumnailEulerDiagramPanel tp = new ThumnailEulerDiagramPanel(diagrams,this);	
			scrollPane = new JScrollPane(tp);
			scrollPane.setPreferredSize(new Dimension(620,800));	
	        getContentPane().add(scrollPane, BorderLayout.CENTER);
	        pack();
	        setVisible(true);
	        setLocation(600, 0);
		}		   
		else{
			 System.out.println("empty library");
		 }
	}		

	 
	  public void updatePareantFrame(int idx){
		  ConstructedConcreteDiagram ccd = library.getConstructedConcreteDiagrams().get(idx);
	         if(ccd!=null){
		        	EulerDiagramPanel edp = ew.getEulerDiagramPanel();
		        	edp.setConstructedConcreteDiagram(ccd);
		        	edp.resetDiagram(ccd);
		        	edp.update(edp.getGraphics());
		        }
	    	
	    }


	
	 
}
