package forcedirected.Trash;

import java.util.ArrayList;

import euler.*;
import euler.utilities.DiagramUtilityRandomWellformedDiagram;


public class ForceDirectedDiagramWindowGenerator {

	public static void main(String[] args) {
		
		DiagramPanel dp = new DiagramPanel();
		
		DiagramUtilityRandomWellformedDiagram wfd = new DiagramUtilityRandomWellformedDiagram(3);
		wfd.setDiagramPanel(dp);
		wfd.apply();
		
		ConcreteDiagram cd = dp.getConcreteDiagram();
		ArrayList<ConcreteContour> ccs = cd.getConcreteContours();
		ESE_rep_ext2 diagramDrawerForceDirected = new ESE_rep_ext2();
		diagramDrawerForceDirected.setConcreteContours(ccs);
		diagramDrawerForceDirected.setDiagramPanel(dp);
	}
}
