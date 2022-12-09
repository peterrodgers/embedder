package jgraphtDemo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.jgrapht.*;
import org.jgrapht.alg.planar.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

public class Tester {

    Graph<String, DefaultEdge> stringGraph;
    
    
    public static void main(String[] args) throws URISyntaxException, IOException {
            Graph<String, DefaultEdge> stringGraph = createStringGraph();

            // note undirected edges are printed as: {<v1>,<v2>}
            System.out.println("-- toString output");
            System.out.println(stringGraph.toString());
            System.out.println();

            BoyerMyrvoldPlanarityInspector<String, DefaultEdge> planarityInspector = new BoyerMyrvoldPlanarityInspector<String, DefaultEdge>(stringGraph);

            System.out.println("planar "+planarityInspector.isPlanar());
            
            graphToPNG(stringGraph,"OutfileName");
        }
    
    
    /**
     * Create a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static Graph<String, DefaultEdge> createStringGraph()
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";
        String v5 = "v5";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v1, v3);
        g.addEdge(v1, v4);
        g.addEdge(v1, v5);
        g.addEdge(v2, v3);
        g.addEdge(v2, v4);
        g.addEdge(v2, v5);
        g.addEdge(v3, v4);
        g.addEdge(v3, v5);
        g.addEdge(v4, v5);

        return g;
    }
    
    
	public static void graphToPNG(Graph<String, DefaultEdge> g, String fileName) throws IOException {
	
		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());
		
		BufferedImage image =  mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
		File imgFile = new File(fileName+".png");
		ImageIO.write(image, "PNG", imgFile);
	
	}
    
}
	
