package pjr.graph.comparators;

import java.util.*;

import pjr.graph.Node;

/**
 * Orders nodes by their score.
 */
public class NodeScoreComparator implements Comparator<Node> {

	public int compare(Node n1, Node n2) {
		Double s1 = new Double(n1.getScore());
		int ret = s1.compareTo(new Double(n2.getScore()));
		return(ret);
	}
}


