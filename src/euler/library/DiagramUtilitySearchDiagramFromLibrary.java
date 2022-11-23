package euler.library;

import java.util.ArrayList;

import euler.inductive.AddPathDialog;
import euler.inductive.HybridGraph;
import euler.inductive.InductiveWindow;
import euler.utilities.DiagramUtility;

public class DiagramUtilitySearchDiagramFromLibrary extends DiagramUtility {
	
	protected EulerDiagramWindow ew;
	
/** Trivial constructor. */
	public DiagramUtilitySearchDiagramFromLibrary(int inAccelerator, String inMenuText, int inMnemonic, EulerDiagramWindow ew) {
		super(inAccelerator, inMenuText, inMnemonic);
		this.ew = ew;
	}
	

	public void apply() {

		new SearchDiagramDialog(ew, null);
	}
		
}

	