package euler.utilities;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import euler.*;
import euler.display.*;
import euler.drawers.*;
import euler.simplify.GenerateJson;
import euler.simplify.Simplify;
import pjr.graph.*;

/**
 *
 * @author Peter Rodgers
 */

public class DiagramUtilityJsonOutput extends DiagramUtility {
	
/** Trivial constructor. */
	public DiagramUtilityJsonOutput() {
		super(KeyEvent.VK_J,"JSON Output",KeyEvent.VK_J);
	}

/** Trivial constructor. */
	public DiagramUtilityJsonOutput(int inAccelerator, String inMenuText, int inMnemonic) {
		super(inAccelerator, inMenuText, inMnemonic);
	}
	


	

	public void apply() {
		
		DualGraph dg = getDualGraph();

		Simplify simplify = new Simplify(dg);
		
		GenerateJson generator = new GenerateJson(simplify);
		
		String jsonString = generator.jsonOutput();
		
		System.out.println(jsonString);

	}
	
	
	
	
}
