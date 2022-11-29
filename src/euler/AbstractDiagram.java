package euler;

import java.util.*;

import pjr.graph.*;

import euler.comparators.*;
import euler.enumerate.*;

/**
 * 
 * @author Peter Rodgers
 * @author Leishi Zhang
 */

public class AbstractDiagram implements Comparable<AbstractDiagram>, Cloneable {


	protected ArrayList<String> zoneList = null;
//	protected String diagramString = null;
	public static Random random = new Random(System.currentTimeMillis());

	/** measures the usage of the brute force part of the isomorphism test */
	public long bruteForceCount = 0;	
	/** how long the isomorphsim brute force test took */
	public long bruteForceTime = 0;
	/** indicating that the brute force part of the isomorphism test was used */
	protected boolean bruteForceApplied = false;
	protected HashMap<String,String> contourLabelMap = new HashMap<String,String>();
public static long timer1 = 0;
public static long timer2 = 0;
public static long timer3 = 0;
public static long timer4 = 0;
	
	public long getBruteForceCount() {return bruteForceCount;}
	public long getBruteForceTime() {return bruteForceTime;}
	public boolean getBruteForceApplied() {return bruteForceApplied;}

	
	public static void main(String[] args) {
 		AbstractDiagram ad1,ad2;
	/*	ad1 = new AbstractDiagram("a b ");
		ad2 = new AbstractDiagram("c");
		System.out.println(ad1.isomorphicTo(ad2));
		System.out.println(failOnZoneSizeCount);*/
 		
 		
 		//ad1 = new AbstractDiagram("a b ab abc ac d bd e ae");
 		//ad1 = new AbstractDiagram("a b ab abc bc bcd bd");
 		ad1 = new AbstractDiagram("a b ab abc bc bd bcd be bce abcf abf");
 		//ad1 = new AbstractDiagram("a b ba");
 		//ad1 = new AbstractDiagram("a b ab bc abc bcd abcd bcde bce bf f g bg bh bgh bghi bhi bgj gj");
 		
 		//ad1=new AbstractDiagram("0 a b c d f ab ac ad af"); 
 		//ad1.checkInductivePiercingDiagram();
 		
	}
	
	
	/**
	 * Takes a space delimited string and produces the abstract graph. Note
	 * that "0" is a special character that gives the empty zone.
	 */
	public AbstractDiagram(String zoneListString) {

		zoneList = findZoneList(zoneListString);
	}
	
	/**
	 * Takes a list of zones and produces the abstract graph. Note
	 * that "0" is a special character that gives the empty zone.
	 */
	public AbstractDiagram(ArrayList<String> zones) {

		zoneList = constructZoneList(zones);
	}
	
	
	/**
	 * Produces a duplicate of the given abstract diagram by copying the zonelist.
	 */
	public AbstractDiagram(AbstractDiagram ad) {
//		diagramString = ad.getDiagramString();
		zoneList = new ArrayList<String>(ad.zoneList);
	}	
	
	
	public ArrayList<String> getZoneList() {return zoneList;}
	public void setZoneList(ArrayList<String> zoneList) {this.zoneList = zoneList;}
	
	public String getAbstractDescription(){
		String ret = "";
		for(String s: zoneList){
			ret+=s;
		}	
		return ret;
	}

	
	public static AbstractDiagram VennFactory(int numberOfContours) {
		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		StringBuffer adZones = new StringBuffer();
		adZones.append("0");
		for(String z: zones) {
			adZones.append(" "+z);
		}
		AbstractDiagram ad = new AbstractDiagram(adZones.toString());
		//ad.addZone("");
		return ad;
	}
	
	
	public static AbstractDiagram randomDiagramFactory(int numberOfContours) {
		return randomDiagramFactory(numberOfContours, true,0.5);
	}
	
	
	public static AbstractDiagram randomDiagramFactory(int numberOfContours, boolean includeNull, double chanceOfZoneAddition) {

		ArrayList<String> zones = Enumerate.findAllZones(numberOfContours);
		StringBuffer adZones = new StringBuffer();
		for(String z: zones) {
			if(random.nextDouble() < chanceOfZoneAddition) {
				adZones.append(" "+z);
			}
		}
		
		AbstractDiagram ad = new AbstractDiagram(adZones.toString());
		ad.addZone("");
		return ad;
	}
	
	
	/**
	 * Takes a space delimited string and produces an sorted arraylist
	 * of strings. "0" is treated as the empty zone, "". Sorts each string
	 * lexographically. Returns null if a contour is specified more than once
	 * in any single zone or if there are duplicate zones.
	 */
	public static ArrayList<String> findZoneList(String diagramString) {
		ArrayList<String> ret = new ArrayList<String>();
		String[] splitString = diagramString.split(" ");
		
		for(int i = 0; i< splitString.length; i++) {
			String zoneString = splitString[i];
			if(zoneString.length() > 0) {
				if(zoneString.equals("0")) { // deal with "0" being the empty set
					zoneString = "";
				}
				String orderedZone = orderZone(zoneString);
				if(orderedZone == null) {
					Util.outError("findZoneList("+diagramString+"): duplicate contour in zone "+zoneString);
					return null;
				}
				ret.add(orderedZone);
			}
		}
		
		// sort zones
		if(!sortZoneList(ret)) {
			Util.outError("findZoneList("+diagramString+"): duplicate zone found");
			return null;
		}
				
		return ret;
	}
	
	
	/**
	 * return a sorted list. Sort each element lexographically, then sort the list of strings.
	 */
	public ArrayList<String> constructZoneList(ArrayList<String> zones) {
		
		ArrayList<String> ret = new ArrayList<String>(zones.size());
		
		for(String zoneString : zones) {
			if(zoneString.equals("0")) { // deal with "0" being the empty set
				zoneString = "";
			}
			String orderedZone = orderZone(zoneString);
			if(orderedZone == null) {
				Util.outError("constructZoneList("+zones+"): duplicate contour in zone "+zoneString);
				return null;
			}
			ret.add(orderedZone);
		}
		
		// sort zones
		if(!sortZoneList(ret)) {
			Util.outError("constructZoneList("+zones+"): duplicate zone found");
			return null;
		}
				
		return ret;
	}
	
	
	/**
	 * Takes a space delimited string and produces an sorted arraylist
	 * of strings. "0" is treated as the empty zone, "". Sorts each string
	 * lexographically. Returns null if a contour is specified more than once
	 * in any single zone or if there are duplicate zones.
	 */
	public String findCopyDiagramString(String diagramString) {		
		String ret = new String();
		String[] splitString = diagramString.split(" ");
		
		for(int i = 0; i< splitString.length; i++) {
			String zoneString = splitString[i];
			zoneString += "-";
		    ret += zoneString;
		    ret += " ";
		}
		return ret;
	}	
	
	
	/** 
	 * Orders the zone string and detects duplicates. Returns null on duplicate.
	 */
	public static String orderZone(String zoneString) {
		
		ArrayList<String> splitZoneList = findContourList(zoneString);
		Collections.sort(splitZoneList);
		// check for duplicates
		if(hasDuplicatesInSortedList(splitZoneList)) {
			return null;
		}		
		// rebuild the string
		StringBuffer sortedZoneStringBuffer = new StringBuffer();
		for(String s: splitZoneList) {
			sortedZoneStringBuffer.append(s);
		}

		return sortedZoneStringBuffer.toString();
	}

	

	
	/**
	 * Finds the duplicates in the String.
	 */
	public static ArrayList<String> findDuplicateContours(String contours) {
		
		ArrayList<String> contourList = findContourList(contours);
		Collections.sort(contourList);
		
		ArrayList<String> ret = new ArrayList<String>();
		String last = null;
		ListIterator<String> li = contourList.listIterator();
		while(li.hasNext()) {
			String s = li.next();
			if(last == null) {
				// first iteration
				last = s;
			} else {
				String current = s;
				if(last.equals(current)) {
					ret.add(current);
				}
				last = current;
			}
		}		
		removeDuplicatesFromSortedList(ret);
		
		return ret;
	}

	
	
	/**
	 * Gets the contours in the diagram. This returns a sorted list.
	 */
	public ArrayList<String> getContours() {
		return findContoursFromZones(zoneList);
	}

	
	/**
	 * Takes a list of zones and returns a sorted list of contours
	 * appearing in the zones.
	 */
	public static ArrayList<String> findContoursFromZones(ArrayList<String> zones) {
		ArrayList<String> contours = new ArrayList<String>(); 
		for(String zone: zones) {
			ArrayList<String> zoneContours = findContourList(zone);
			contours.addAll(zoneContours);
		}
		Collections.sort(contours);
		removeDuplicatesFromSortedList(contours);
		return contours;
	}
	
	
	
	/**
	 * Takes a list of zones and returns a sorted list of contours
	 * appearing in the zones.
	 */
	public static ArrayList<String> findSortedContoursFromZones(ArrayList<String> zones) {
		
		ArrayList<String> contours = findContoursFromZones(zones);
		
		int [] levels = new int[contours.size()];
		for(int i = 0; i< contours.size(); i++){
			String s = contours.get(i);
			int level = contours.size();
			for(String z : zones){
				if(z.contains(s)&& z.length()<level)
					level = z.length();
			}		
			levels[i] = level;
		}
		ArrayList<String> temp = new ArrayList<String>();
		for(int j = 0 ; j < contours.size(); j++){
			for(int k = 0; k < contours.size(); k++){
				if(levels[j] == k)
				temp.add(contours.get(j));
			}		
		}
		contours = new ArrayList<String>();
		for(int l = 0 ; l < temp.size(); l++){			
			contours.add(temp.get(temp.size()-l-1));			
		}
		return contours;
	}
	

	/** Takes a string and returns the list of characters in the string */
	public static ArrayList<String> findContourList(String zoneLabel) {
		String[] zones = zoneLabel.split("");
		ArrayList<String> zoneList = new ArrayList<String>(Arrays.asList(zones));
		zoneList.remove(""); // split adds a blank entry in index 0
		return zoneList;
	}
	
	
	/**
	 * Generate the containment graph with only contours linking to
	 * its immediate parents. Each contour is a node,
	 * and an edge goes from a contour to another contour if
	 * the contour is inside it, without intersecting it. Concurrent
	 * contours are inside each other. 
	 */
	public Graph generateImmediateContainmentGraph() {
		Graph ret = generateContainmentGraph();
		
		ArrayList<Edge> deleteEdges = new ArrayList<Edge>();
		
		for(Edge e : ret.getEdges()) {
			if(alternativePath(ret, e)) {
				deleteEdges.add(e);
			}
		}
		
		ret.removeEdges(deleteEdges);
		
		return ret;
		
	}
	
	
	/**
	 * Find out if there is an alternative path from source to target
	 * without using e or any edges where there is another edge going
	 * in the opposite direction.
	 */
	public static boolean alternativePath(Graph g, Edge e) {
		
		Node source = e.getFrom();
		Node target = e.getTo();

		g.setEdgesVisited(false);
		e.setVisited(true);
		

		ArrayList<Edge> queue = new ArrayList<Edge>();
		queue.addAll(source.getEdgesFrom());
		
		queue.remove(e); // just in case e is in the from list of source
		
		// dont use edges that have a parallel in the opposite direction
		for(Edge e1 : g.getEdges()) {
			Node from1 = e1.getFrom();
			Node to1 = e1.getTo();
			for(Edge e2 : g.getEdges()) {
				Node from2 = e2.getFrom();
				Node to2 = e2.getTo();
				if(from1 == to2 && to1 == from2) {
					e1.setVisited(true);
					e2.setVisited(true);
					queue.remove(e1); // in case the edges are currently in the queue
					queue.remove(e2);
				}
			}
		}


		g.setEdgesVisited(source.getEdgesFrom(),true);
		Edge current = null;
		while(!queue.isEmpty()) {

			current = queue.get(0);
			queue.remove(0);
			
			
			if(current.getTo() == target) {
				return true;
			}

			for(Edge fromEdge : current.getTo().getEdgesFrom()) {
				if(!fromEdge.getVisited()) {
					queue.add(fromEdge);
					fromEdge.setVisited(true);
				}
			}
		}

		return false;
	}



	/**
	 * Generate the containment graph, here each contour is a node,
	 * and an edge goes from a contour to another contour if
	 * the contour is inside it, without intersecting it. Concurrent
	 * contours are inside each other. 
	 */
	public Graph generateContainmentGraph() {
		Graph ret = new Graph();
		
		// first create all the nodes
		for(String c : getContours()) {
			Node n = new Node(c);
			ret.addNode(n);
		}

		// now add appropriate edges
		for(String zone : zoneList) {
			ArrayList<String> contours = findContourList(zone);
			for(int i = 0; i< contours.size(); i++) {
				String ci = contours.get(i);
				for(int j = i+1; j< contours.size(); j++) {
					String cj = contours.get(j);
					if(contourContainment(ci, cj)) {
						addUniqueEdge(ret, cj, ci);
					}
					if(contourContainment(cj, ci)) {
						addUniqueEdge(ret, ci, cj);
					}
				}
			}
		}
		return ret;
	}
	

	/**
	 * Add an edge from node with label1 to node with label2 if one
	 * does not already exist.
	 * @return true if an edge is added.
	 */
	public boolean addUniqueEdge(Graph graph, String label1, String label2) {
		Node n1 = graph.firstNodeWithLabel(label1);
		Node n2 = graph.firstNodeWithLabel(label2);
		
		ArrayList<Edge> out1 = n1.getEdgesFrom();
		for(Edge e : out1) {
			if(e.getTo() == n2) {
				return false;
			}
		}
		
		Edge newEdge = new Edge(n1,n2);

		return graph.addEdge(newEdge);
	}


	/**
	 * Find the groups of intersecting contours. No concurrency considered
	 */
	public ArrayList<String> findIntersectionGroups() {
		
		// first get the pairs of intersecting contours
		ArrayList<String> intersectingPairs = new ArrayList<String>();
		for(String zone : zoneList) {
			ArrayList<String> contours = findContourList(zone);
			for(int i = 0; i< contours.size(); i++) {
				String ci = contours.get(i);
				for(int j = i+1; j< contours.size(); j++) {
					String cj = contours.get(j);
					
					if(!intersectingPairs.contains(ci+cj) && !intersectingPairs.contains(cj+ci)) {
						intersectingPairs.add(ci+cj);
					}
				}
			}
		}

		
		ArrayList<StringBuffer> intersectionGroups = new ArrayList<StringBuffer>();
		// put the single element zones into intersectionGroups
		for(String zone : zoneList) {
			if(zone.length() == 1) {
				StringBuffer newGroup = new StringBuffer(zone);
				intersectionGroups.add(newGroup);
			}
		}
		
		// now either merge groups, add contours to groups or add new groups
		for(String pair : intersectingPairs) {
			String c1 = Character.toString(pair.charAt(0));
			String c2 = Character.toString(pair.charAt(1));
			
			StringBuffer c1Group = null;
			StringBuffer c2Group = null;
			for(StringBuffer group : intersectionGroups) {
				String groupString = group.toString();
				if(groupString.contains(c1)) {
					c1Group = group;
				}
				if(groupString.contains(c2)) {
					c2Group = group;
				}
			}
			
			if(contourContainment(c1, c2) || contourContainment(c2, c1)) {
				// dont merge two contours if one contains the other
				if(c1Group == null) {
					StringBuffer newGroup = new StringBuffer(c1);
					intersectionGroups.add(newGroup);
				}
				if(c2Group == null) {
					StringBuffer newGroup = new StringBuffer(c2);
					intersectionGroups.add(newGroup);
				}
			} else {
			
				if(c1Group == null && c2Group == null) {
					// if neither contour is in a group, create a new group
					StringBuffer newGroup = new StringBuffer(pair);
					intersectionGroups.add(newGroup);
				}
				if(c1Group != null && c2Group == null) {
					// if one contour is already in a group, add the other
					c1Group.append(c2);
				}
				if(c1Group == null && c2Group != null) {
					// if one contour is already in a group, add the other
					c2Group.append(c1);
				}
				if(c1Group != null && c2Group != null && c1Group != c2Group) {
					// if both contours are already in a group, join up the two groups
					intersectionGroups.remove(c2Group);
					c1Group.append(c2Group);
				}
			}
		}


		ArrayList<String> ret = new ArrayList<String>();
		for(StringBuffer sb : intersectionGroups) {
			String group = sb.toString();
			group = orderZone(group);
			ret.add(group);
		}
		
		sortZoneList(ret);
		
		return ret;
	}
	
	
	public AtomicAbstractDiagram generateAtomicDiagrams() {
		
		// find the containments and intersections
		ArrayList<String> intersections = findIntersectionGroups();
		Graph immediateContainmentGraph = generateImmediateContainmentGraph();
		Graph fullContainmentGraph = generateContainmentGraph();
		
		// first find any parallel edges that go in opposite
		// directions, indicating concurrent contours, and
		// merge the intersections
		ArrayList<Edge> deleteEdges = new ArrayList<Edge>();
		for(Edge e1 : immediateContainmentGraph.getEdges()) {
			Node from1 = e1.getFrom();
			Node to1 = e1.getTo();
			for(Edge e2 : immediateContainmentGraph.getEdges()) {
				Node from2 = e2.getFrom();
				Node to2 = e2.getTo();
				if(from1 == to2 && to1 == from2) {
					mergeIntersectionGroups(intersections, from1.getLabel(), to1.getLabel());
					deleteEdges.add(e1);
					deleteEdges.add(e2);
				}
			}
			
		}
		// remove the parallel edges, leaving a DAG
		immediateContainmentGraph.removeEdges(deleteEdges);
		
		// For each node, find the total containment
		// for it, if that zone exists merge node with
		// immediate parents and remove the containment edge
		ArrayList<String> completeZoneList = getZoneList();
		deleteEdges.clear();
		for(Node n : immediateContainmentGraph.getNodes()) {
			String contour = n.getLabel();
			String containment = findAllParents(immediateContainmentGraph, n);
			if(!completeZoneList.contains(containment)) {
				for(Edge e : n.getEdgesFrom()) {
					Node parent = e.getTo();
					String parentC = parent.getLabel();
					mergeIntersectionGroups(intersections, contour, parentC);

					deleteEdges.add(e);
				}
			}
		}
		// remove the edges of merged nodes
		immediateContainmentGraph.removeEdges(deleteEdges);
		
		// build the intersection containment tree
		// nodes are intersections, edges are those derived from the
		// containment graph
		DualGraph intersectionTree = new DualGraph();
		for(String intersection : intersections) {
			intersectionTree.addNode(new Node(intersection));
			for(Edge e : immediateContainmentGraph.getEdges()) {
				Node from = intersectionTree.firstNodeContainingLabel(e.getFrom().getLabel());
				Node to = intersectionTree.firstNodeContainingLabel(e.getTo().getLabel());
				if(from == to) {
					// dont add self sourcing edge
					continue;
				}
				// add an edge if there is not one in the tree
				intersectionTree.addUniqueEdge(from,to);
			}
		}
		
		// Build the atomic diagram

		// top level is the empty diagram
		String addedContours = "";
		AtomicAbstractDiagram root = new AtomicAbstractDiagram(new AbstractDiagram("0"),null,null);
		ArrayList<AtomicAbstractDiagram> queue = new ArrayList<AtomicAbstractDiagram>();
		for(Node n : intersectionTree.getNodes()) {
			String atomicContours = n.getLabel();
			if(n.getEdgesFrom().size() == 0) {
				// if the diagram is a top level diagram
				
				AbstractDiagram ad = findAtomicDiagram(atomicContours);
				AtomicAbstractDiagram aad = new AtomicAbstractDiagram(ad,root,"0");
				queue.add(aad);
				addedContours += atomicContours;
			}
		}
		
		while(queue.size() != 0) {
			AtomicAbstractDiagram parentDiagram = queue.get(0);
			ArrayList<String> parentContours = parentDiagram.getAtomicDiagram().getContours();
			queue.remove(0);
			
			for(String parentContour : parentContours) {
				Node parentNode = intersectionTree.firstNodeContainingLabel(parentContour);
				for(Edge e : parentNode.getEdgesTo()) {
					Node childNode = e.getFrom();
					String childAtomicContours = childNode.getLabel();
					ArrayList<String> childContourList = findContourList(childAtomicContours);
					if(!addedContours.contains(childContourList.get(0))) {
						// child is not yet in the tree
						AbstractDiagram ad = findAtomicDiagram(childAtomicContours);
						// use the fullContainmentGraph to find the parent diagram zone containing the child contour
						// we need to find the containment nodes from the node that are
						// in the parent diagram
						String parentZone = "";
						for(String childContour : childContourList) {
							Node fullChildNode = fullContainmentGraph.firstNodeWithLabel(childContour);
							for(Edge parentE : fullChildNode.getEdgesFrom()) {
								Node parentZoneNode = parentE.getTo();
								for(String parentTestContour : parentDiagram.getAtomicDiagram().getContours()) {
									if(parentTestContour.equals(parentZoneNode.getLabel()) && !parentZone.contains(parentZoneNode.getLabel())) {
										// if the a parent zone contains the child diagram, add it to the zone that contains the child
										parentZone += parentZoneNode.getLabel();
										break;
									}
								}
							}
						}
						parentZone = orderZone(parentZone);
						AtomicAbstractDiagram aad = new AtomicAbstractDiagram(ad,parentDiagram, parentZone);
						queue.add(aad);
						addedContours += childAtomicContours;
					}
					
				}
			}
			
		}

		return root;
	}
	

	/**
	 * This returns zones containing the contours, and
	 * strips the zones of contours not in the argument, so
	 * the argument must consist of contours in an atomic diagram.
	 */
	public AbstractDiagram findAtomicDiagram(String atomicContours) {
		
		ArrayList<String> zones = findZonesContainingContours(atomicContours);
		ArrayList<String> contours =  findContourList(atomicContours);
		Collections.sort(contours);
		
		ArrayList<String> zoneList = new ArrayList<String>();
		zoneList.add("0");
		
		for(String zone : zones) {
			StringBuffer strippedZone = new StringBuffer();
			for(String contour : contours) {
				if(zone.contains(contour)) {
					strippedZone.append(contour);
				}
			}
			if(strippedZone.length() > 0) {
				zoneList.add(strippedZone.toString());
			}
		}
		
		sortZoneList(zoneList);
		removeDuplicatesFromSortedList(zoneList);
		
		StringBuffer zoneBuff = new StringBuffer("");
		for(String zone : zoneList) {
			zoneBuff.append(zone);
			zoneBuff.append(" ");
		}
		
		AbstractDiagram ret = new AbstractDiagram(zoneBuff.toString());
		return ret;

	}


	/**
	 * Return the zones contain that any of the contours in the argument.
	 */
	public ArrayList<String> findZonesContainingContours(String contours) {
		
		ArrayList<String> ret = new ArrayList<String>();
		ArrayList<String> splitContourList = findContourList(contours);
		
		for(String zone : getZoneList()) {
			for(String contour : splitContourList) {
				if(zone.contains(contour)) {
					ret.add(zone);
				}
			}
		}
		
		sortZoneList(ret);
		removeDuplicatesFromSortedList(ret);
		
		return ret;

	}


	/**
	 * Find the two groups with c1 and c2 in and merge them if they are
	 * different.
	 */
	private void mergeIntersectionGroups(ArrayList<String> groups, String c1, String c2) {

		int group1 = -1;
		int group2 = -1;
		for(int i = 0; i < groups.size(); i++) {
			String group = groups.get(i);
			if(group.contains(c1)) {
				group1 = i;
			}
			if(group.contains(c2)) {
				group2 = i;
			}
		}
		
		if(group1 == -1) {
			// should never get here
			return;
		}
		if(group2 == -1) {
			// should never get here
			return;
		}
		
		if(group1 == group2) {
			// groups are same, so do nothing, but return true as the nodes should be merged
			return;
		}

		String g1s = groups.get(group1);
		String g2s = groups.get(group2);
		String mergedGroup = g1s+g2s;
		
		mergedGroup = orderZone(mergedGroup);
		
		groups.remove(g1s);
		groups.remove(g2s);
		groups.add(mergedGroup);
		
		sortZoneList(groups);
		
		return;
	}
	
	
	/** Merge the labels in all parents of the given node, assumes a DAG. */
	public static String findAllParents(Graph g, Node n) {
		
		String ret = "";
		
		ArrayList<Edge> queue = new ArrayList<Edge>();
		queue.addAll(n.getEdgesFrom());
		
		while(queue.size() > 0) {
			Edge currentE = queue.get(0);
			queue.remove(0);
			Node currentN = currentE.getTo();
			if(!ret.contains(currentN.getLabel())) {
				ret = ret+currentN.getLabel();
			}
			queue.addAll(currentN.getEdgesFrom());
		}
		
		ret = orderZone(ret);
		
		return ret;
		
	}


	/**
	 * See if c2 is entirely inside c1.
	 * c1 and c2 must be in the diagram.
	 * Concurrent contours contain each other.
	 */
	public boolean contourContainment(String c1, String c2) {
		for(String zone : zoneList) {
			if(!zone.contains(c1) && zone.contains(c2)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * See if c1 is in exactly the same zones as c2.
	 * c1 and c2 must be in the diagram.
	 */
	public boolean contoursConcurrent(String c1, String c2) {
		for(String zone : zoneList) {
			if(zone.contains(c1) && !zone.contains(c2)) {
				return false;
			}
			if(!zone.contains(c1) && zone.contains(c2)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * See if c1 shares a zone with c2.
	 * c1 and c2 must be in the diagram.
	 */
	public boolean contoursIntersect(String c1, String c2) {
		for(String zone : zoneList) {
			if(zone.contains(c1) && zone.contains(c2)) {
				return true;
			}
		}
		return false;
	}


	
	/**
	 * Removes duplicates from the sorted List, returns true
	 * if duplicates found, false if not.
	 */
	public static boolean removeDuplicatesFromSortedList(List list) {
		Object last = null;
		boolean found = false;
		ListIterator li = list.listIterator();
		while(li.hasNext()) {
			Object o = (Object)li.next();
			if(last == null) {
				// first iteration
				last = o;
			} else {
				Object current = o;
				if(last.equals(current)) {
					li.remove();
					found = true;
				}
				last = current;
			}
		}
		return found;
	}
	
	
	/**
	 * Finds duplicates in a the sorted List, returns true
	 * if duplicates found, false if not.
	 */
	public static boolean hasDuplicatesInSortedList(List list) {
		Object last = null;
		ListIterator li = list.listIterator();
		while(li.hasNext()) {
			Object o = li.next();
			if(last == null) {
				// first iteration
				last = o;
			} else {
				Object current = o;
				if(last.equals(current)) {
					return true;
				}
				last = current;
			}
		}
		return false;
	}
	
	
	/**
	 * Sorts the list of strings and checks for duplicates. Returns
	 * true if there are no duplicates, false if there are. The
	 * list is still sorted on a false return.
	 */
	public static boolean sortZoneList(ArrayList<String> zoneList) {
		ZoneStringComparator zComp = new ZoneStringComparator();
		Collections.sort(zoneList,zComp);
		
		if(hasDuplicatesInSortedList(zoneList)) {
			return false;
		}

		return true;

	}
	
	
	/**
	 * Changes the zone strings according to the map. A check
	 * is made to see if the result will lead to ambiguity
	 * so that a->b in ab ac abc will result in false being returned.
	 * a->b, b->a is dealt with.
	 */
	public boolean remapContourStrings(HashMap<String,String> map) {

		ArrayList<String> newZoneList = new ArrayList<String>();
		for(String z:zoneList) {
			
			// we need to do it like this because of maps
			// like a->b, b->a
			
			// first split the zone and convert it into a list that can be changed
			ArrayList<String> splitZoneList = findContourList(z);

			String newZone = "";
			// find occurences of keys and replace them in the new zone
			for(String contourString: splitZoneList) {
				if(map.containsKey(contourString)) {
					newZone += map.get(contourString);
				} else {
					newZone += contourString;
				}
			}
			
			// newZone might need to be reordered
			newZone = orderZone(newZone);
			if(newZone == null) {
				Util.outError("remapContourStrings(): duplicate contour in zone "+z+" new zone "+newZone+" caused by map");
				return false;
			}
			

			// add to the new zone list
			newZoneList.add(newZone);
		}

		// sort zones
		if(!sortZoneList(newZoneList)) {
			Util.outError("remapContourStrings(): duplicate zone found. old zone list is "+zoneList+" new zone list is "+newZoneList);
			return false;
		}
		
		zoneList = newZoneList;
		
		return true;
	}
	
	
	public String toString() {
		String ret = "";
		Iterator<String> i = zoneList.iterator();
		while(i.hasNext()) {
			String zone = i.next();
			if(zone.equals("")) {
				ret += "0";
			} else {
				ret += zone;
			}
			if(i.hasNext()) {
				ret += " ";
			}
		}
		return ret;

	}

	
	/**
	 * The zone labels must be equal, so its not
	 * an isomorphism test. E.g. "a b ab" is not equal to "x z xz"
	 */
	public int compareTo(AbstractDiagram ad) {

		ArrayList<String> zoneList1 = zoneList;
		ArrayList<String> zoneList2 = ad.zoneList;

		// the diagram with the most zones is greatest (a b c is greater than a b)
		if(zoneList1.size() > zoneList2.size()) {
			return 1;
		}
		if(zoneList1.size() < zoneList2.size()) {
			return -1;
		}
		
		Iterator<String> zi1 = zoneList1.iterator();
		Iterator<String> zi2 = zoneList2.iterator();
		while(zi1.hasNext()) {
			String z1 = zi1.next();
			String z2 = zi2.next();
			
			// the diagram with the most outer zones is greatest (zone a is greater than ab)
			if(z1.length() < z2.length()) {
				return 1;
			}
			if(z1.length() > z2.length()) {
				return -1;
			}
			// first lexographic difference
			int lexCompare = z1.compareTo(z2);
			if(lexCompare != 0) {
				return lexCompare;
			}
		}
		
		return 0;
	}
	
	protected static long longestSuccessTime = 0;
	protected static long longestFailTime = 0;
	protected static int longestSuccessBruteForce = 0;
	protected static int longestFailBruteForce = 0;
	public static int failOnZoneSizeCount = 0;
	public static int failOnContourSizeCount = 0;
	public static int failOnZonePartitionCount = 0;
	public static int failOnContourPartitionCount = 0;
	public static int failOnContourZonePartitionCount = 0;
	public static int succeedAfterMapping = 0;
	public static int failAfterMapping = 0;
	
	
	public static void resetIsomorphismCounts() {
		longestSuccessTime = 0;
		longestFailTime = 0;

		longestSuccessBruteForce = 0;
		longestFailBruteForce = 0;

		failOnZoneSizeCount = 0;
		failOnContourSizeCount = 0;
		failOnZonePartitionCount = 0;
		failOnContourPartitionCount = 0;
		succeedAfterMapping = 0;
		failAfterMapping = 0;
	}
 	

	/**
	 * Test all contour combinations to test structural equality
	 */
	public boolean isomorphicTo(AbstractDiagram ad2) {
		
		//long startTime = System.currentTimeMillis();
		bruteForceCount = 0;
		bruteForceTime = 0;
timer1 = 0;
timer2 = 0;
timer3 = 0;
timer4 = 0;
		bruteForceApplied = false;
		
		AbstractDiagram ad1 = new AbstractDiagram(this);

long startTimer1 = System.currentTimeMillis();
		if(IsomorphismInvariants.zoneSizes(ad1, ad2)) {
			failOnZoneSizeCount++;
			return false;
		}
		
		if(IsomorphismInvariants.labelSizes(ad1, ad2)) {
			failOnContourSizeCount++;
			return false;
		}

		
		if(IsomorphismInvariants.zonePartition(ad1, ad2)) {
			failOnZonePartitionCount++;
timer1 = System.currentTimeMillis()-startTimer1;
			return false;
		}
		
		if(IsomorphismInvariants.labelPartition(ad1, ad2)) {
			failOnContourPartitionCount++;
timer1 = System.currentTimeMillis()-startTimer1;
			return false;
		}
timer1 = System.currentTimeMillis()-startTimer1;

long startTimer3 = System.currentTimeMillis();
		// switch to complements if there are a lot of zones
		double maxNumberOfZones = (int)Math.pow(2,ad1.getContours().size()-1)-1;
		if(ad1.getZoneList().size() > (2*maxNumberOfZones)/3) { // try complement when zone list is 2 3rds of max
			ad1 = ad1.complement();
			ad2 = ad2.complement();
		}
timer3 = System.currentTimeMillis()-startTimer3;

long startTimer2 = System.currentTimeMillis();

		// test the occurrences of contours in zones makes the diagrams equal
		// this groups contours by the number of occurrences in all zone sizes
		// so that for 0 a ab abc abd
		// results in a:[0,1,1,2], b:[0,0,1,1], c:[0,0,0,1], d:[0,0,0,1]
		// so that c and d are grouped
		HashMap<String,String> singleMaps = new HashMap<String,String>();
		ArrayList<Integer> listSizes = new ArrayList<Integer>();
		ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>> pairsForTesting = new ArrayList<ArrayList<ArrayList<ContourZoneOccurrence>>>();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps1 = ad1.findContourMaps();
		ArrayList<ArrayList<ContourZoneOccurrence>> contourMaps2 = ad2.findContourMaps();
		for(ArrayList<ContourZoneOccurrence> czos1: contourMaps1) {
			ContourZoneOccurrence czo1 = czos1.get(0);			
			boolean foundMatch = false;
			for(ArrayList<ContourZoneOccurrence> czos2: contourMaps2) {
				if(czos1.size() != czos2.size()) {
					continue;
				}
				ContourZoneOccurrence czo2 = czos2.get(0);
				if(czo1.compareLists(czo2) == 0) {
					foundMatch = true;
					if(czos1.size() == 1) {
						// if there is only one contour, just set the mapping
						singleMaps.put(czo2.getContour(), czo1.getContour());
					} else {
						ArrayList<ArrayList<ContourZoneOccurrence>> pair = new ArrayList<ArrayList<ContourZoneOccurrence>>();
						pair.add(czos1);
						pair.add(czos2);
						pairsForTesting.add(pair);
						listSizes.add(czos1.size());
					}

					break;
				}
			}
			if(!foundMatch) {
timer2 = System.currentTimeMillis()-startTimer2;
				failOnContourZonePartitionCount++;
				return false;
			}
			
		}
timer2 = System.currentTimeMillis()-startTimer2;
		

//for(ArrayList<ArrayList<ContourZoneOccurrence>> pair : pairsForTesting) {
//System.out.println(pair.get(0)+" -- "+pair.get(1));
//}
		
		// here there is no choice in the mapping
		if(pairsForTesting.size() == 0) {
			AbstractDiagram adCopy = new AbstractDiagram(ad2);
			adCopy.remapContourStrings(singleMaps);
			if(ad1.compareTo(adCopy) == 0) {

				succeedAfterMapping++;
				return true;
			}
			return false;
		}
		
		// now we need to try all the contour mappings to test
		// if any mapping make the diagrams equal.
		// Its only neccessary to test contour mappings between
		// contours which have the same occurrence information
		// for all zone sizes.
		
		// this tests all contour mappings in a brute force approach
		
		
		long startBruteForceTime = System.currentTimeMillis();
		bruteForceApplied = true;
		
		ArrayList<GroupMap> combination = firstCombination(listSizes);
		boolean loop = true;
		while(loop) {
			
			bruteForceCount++;

			HashMap<String,String> mapping = new HashMap<String,String>(singleMaps);

			for(int i = 0; i < combination.size(); i++) {
				GroupMap gm = combination.get(i);
				int[] permutation = gm.getMapping();
				
				for (int j = 0; j < permutation.length; ++j) {
					ArrayList<ArrayList<ContourZoneOccurrence>> pair = pairsForTesting.get(i);
					ArrayList<ContourZoneOccurrence> list1 = pair.get(0);
					ArrayList<ContourZoneOccurrence> list2 = pair.get(1);
					ContourZoneOccurrence czo1 = list1.get(j);
					ContourZoneOccurrence czo2 = list2.get(permutation[j]);
					String contour1 = czo1.getContour();
					String contour2 = czo2.getContour();
					mapping.put(contour2,contour1);

				}

				
			}

			AbstractDiagram adCopy = new AbstractDiagram(ad2);
			adCopy.remapContourStrings(mapping);
			if(ad1.compareTo(adCopy) == 0) {

				bruteForceTime = System.currentTimeMillis()-startBruteForceTime;
				succeedAfterMapping++;
				return true;
			}

			loop = nextCombination(combination);
		}
		
		bruteForceTime = System.currentTimeMillis()-startBruteForceTime;
		
		
		
		failAfterMapping++;

//System.out.println("FALSE because no mapping found");
		return false;
	}
	
	
	
	public static ArrayList<GroupMap> firstCombination(ArrayList<Integer> intList) {
		ArrayList<GroupMap> ret = new ArrayList<GroupMap>();
		for(Integer i : intList) {
			GroupMap gm = new GroupMap(i);
			ret.add(gm);
		}
		return ret;
	}
	

	/**
	 * Given a list of triples, where the first number is the quantity
	 * of combinations at that index, the second number is the current lhs
	 * index and the third number is the current rhs index, increment the
	 * combinations by one, or return false if at the end.
	 */
	public static boolean nextCombination(ArrayList<GroupMap> current) {
		
		if(current.size() == 0) {
			return false;
		}
		
		int index = 0;
		boolean overflow = true;
		while(overflow) {
			GroupMap gm = current.get(index);
			
			overflow = gm.nextMapping();
			
			if(overflow) {
				index++;
				if(index >= current.size()) {
					// last combination
					return false;
				}
			}
		}		
		return true;		
	}

	
	

	/** Group the contours in the zone list into those that cannot be disinguished */
	public ArrayList<ArrayList<ContourZoneOccurrence>> findContourMaps() {
		
		ArrayList<ArrayList<ContourZoneOccurrence>> ret = new ArrayList<ArrayList<ContourZoneOccurrence>>();
		
		ArrayList<String> zones = getZoneList();
		ArrayList<String> contours = findContoursFromZones(zones);
		
		String largestZone = "";
		HashMap<String,Integer> sizeMap = new HashMap<String,Integer>(contours.size()*10);
		for(String z : zones) {
			
			int zoneSize = z.length();
			
			ArrayList<String> splitZoneList = findContourList(z);
			for(String c : splitZoneList) {
				String key = c+zoneSize; // key is the contour and size of zone
				Integer occurrences = sizeMap.get(key);
				if(occurrences == null) {
					occurrences = 1;
				} else {
					occurrences++;
				}
				sizeMap.put(key, occurrences);
			}
			
			// find the largest zone
			if(largestZone.length() < z.length()) {
				largestZone = z;
			}
		}

		// now build up the occurrences
		HashMap<String,ArrayList<Integer>> contourOccurrences = new HashMap<String, ArrayList<Integer>>(contours.size());
		for(String c : contours) {
			ArrayList<Integer> occurrenceList = new ArrayList<Integer>(largestZone.length()+1);
			occurrenceList.add(0); // no occurrences at size 0
			for(int i = 1; i <= largestZone.length(); i++) {
				String key = c+i;
				Integer occurrences = sizeMap.get(key);
				if(occurrences == null) {
					occurrenceList.add(0);
				} else {
					occurrenceList.add(occurrences);
				}
			}
			
			contourOccurrences.put(c,occurrenceList);
		}

		for(String contour : contourOccurrences.keySet()) {
			ArrayList<Integer> list = contourOccurrences.get(contour);
			ContourZoneOccurrence czo = new ContourZoneOccurrence(contour,list);
			boolean foundGroup = false;
			for(ArrayList<ContourZoneOccurrence> contourGroup : ret) {
				String tryContour = contourGroup.get(0).getContour();
				ArrayList<Integer> tryList = contourOccurrences.get(tryContour);
				
				if(compareOccurrences(czo.getOccurrences(),tryList)) {
					contourGroup.add(czo);
					foundGroup = true;
					break;
				}
			}

			if(!foundGroup) {
				ArrayList<ContourZoneOccurrence> newGroup = new ArrayList<ContourZoneOccurrence>();
				newGroup.add(czo);
				ret.add(newGroup);
			}
		}

		return ret;
	}
	
	
	
	
	/**
	 * Compare two lists that consist of contour occurrences at zone sizes
	 */
	public static boolean compareOccurrences(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		if(list1.size() != list2.size()) {
			return false;
		}
		Iterator<Integer> zi1 = list1.iterator();
		Iterator<Integer> zi2 = list2.iterator();
		while(zi1.hasNext()) {
			Integer i1 = zi1.next();
			Integer i2 = zi2.next();
			
			if(i1 != i2) {
				return false;
			}
		}
		
		return true;

	}
	
	
	
	/**
	 * returns, for each contour and each zone size, the neighbouring contours.
	 * That is, the other contours that appear in the same zone as the contour.
	 * The contours are sorted by label in the sublists.
	 * NOT USED AT THE MOMENT
 	 */
	public HashMap<String, HashMap<Integer, ArrayList<String>>> findContourNeighboursByZoneSize() {
		
		HashMap<String,HashMap<Integer,ArrayList<String>>> ret = new HashMap<String,HashMap<Integer,ArrayList<String>>>();
		
		ArrayList<String> contours = getContours();

		// iterate through the zones, collecting zones of the same size
		HashMap<Integer,ArrayList<String>> zoneSizeToZones = new HashMap<Integer,ArrayList<String>>();
		for(String zone: zoneList) {
			int zoneSize = zone.length();
			
			ArrayList<String> zoneList = zoneSizeToZones.get(zoneSize);
			if(zoneList == null) {
				zoneList = new ArrayList<String>();
				zoneList.add(zone);
				zoneSizeToZones.put(zoneSize,zoneList);
			} else {
				zoneList.add(zone);
				Collections.sort(zoneList);
			}
		}
		
		for(String contour:contours) {
			Set<Integer> sizeSet = zoneSizeToZones.keySet();
			
			HashMap<Integer,ArrayList<String>> neighboursAtSize = new HashMap<Integer,ArrayList<String>>();
			for(Integer size:sizeSet) {
				ArrayList<String> zonesAtSize = zoneSizeToZones.get(size);
				ArrayList<String> zonesContainingContour = new ArrayList<String>();
				for(String zoneAtSize:zonesAtSize) {
					if(zoneAtSize.indexOf(contour) != -1) {
						zonesContainingContour.add(zoneAtSize);
					}
				}
				ArrayList<String> neighbours = findContoursFromZones(zonesContainingContour);
				neighbours.remove(contour);
				if(neighbours.size() != 0) {
					neighboursAtSize.put(size,neighbours);
				}
			}
			ret.put(contour,neighboursAtSize);
		}
		
		return ret;
	}
	
	
	/**
	 * Returns an ordered list of lists of contours, based on
	 * the ordering in the HashMap, with contours having the
	 * same order in the same list. The list must be sorted on the hashmap values.
	 */
	public static ArrayList<ArrayList<String>> groupContoursByMap(ArrayList<String> contours, HashMap<String,Integer> map) {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		if(contours.size() == 0) {
			return ret;
		}

		int addV = -1;
		boolean start = true;
		ArrayList<String> cList = new ArrayList<String>();
		for(String c: contours) {
			int v = map.get(c);
			if(start) {
				cList.add(c);
				addV = v;
				start = false;
				continue;
			}
			if(addV == v) {
				cList.add(c);
			} else {
				Collections.sort(cList);
				ret.add(cList);
				cList = new ArrayList<String>();
				cList.add(c);
				addV = v;
			}
		}
		Collections.sort(cList);
		ret.add(cList);
		
		return ret;
	}

	
	/**
	 * Find the complement diagram.
	 */
	public AbstractDiagram complement() {
		
		ArrayList<String> contours = getContours();
		ArrayList<String> zones = getZoneList();
		
		AbstractDiagram venn = VennFactory(contours.size());

		// we need to map venn to the contours in the diagram
		HashMap<String,String> mapping = new HashMap<String,String>(contours.size());
		Iterator<String> it1 = contours.iterator();
		Iterator<String> it2 = venn.getContours().iterator();
		while(it1.hasNext()) {
			// coiteration, because both should have the same number of contours
			String c1 = it1.next();
			String c2 = it2.next();
			mapping.put(c2,c1);
		}
		venn.remapContourStrings(mapping);
		
		ArrayList<String> retZones = new ArrayList<String>();
		
		for(String z : venn.getZoneList()) {
			if(!zones.contains(z)) {
				retZones.add(z);
			}
		}
		
		AbstractDiagram ret = new AbstractDiagram(retZones);
		
		return ret;
		
	}
		
		/**
		 * remap the contour labels so that they are in an order,
		 * with 'a' the highest value label in this diagram, 'b'
		 * the next and so on. The effect is that many
		 * isomorphic diagrams will have the same label ordering.
		 * The labels are compared by checking each zone size.
		 * A label occuring in a smaller size zone size gets
		 * higher priority. Where there is no difference in this
		 * then a label occuring adjacent to a higher ordered
		 * label gets highest priority.
		 */
		public void normalize() {
			
		ArrayList<String> contours = new ArrayList<String>(getContours());

		// set up the contours by the size of zones they occur in
		HashMap<Integer,ArrayList<String>> zoneSizeToContours = new HashMap<Integer,ArrayList<String>>();
		for(String zone: zoneList) {
			ArrayList<String> splitZoneList = findContourList(zone);
			
			int zoneSize = splitZoneList.size();
			ArrayList<String> contourList = zoneSizeToContours.get(zoneSize);
			if(contourList == null) {
				contourList = new ArrayList<String>(splitZoneList);
				zoneSizeToContours.put(zoneSize,contourList);
			} else {
				contourList.addAll(splitZoneList);
				Collections.sort(contourList);
//				removeDuplicatesFromSortedList(contourList); // cant do this here because we need the duplicates later for a count
			}
		}
		

		// find the number of contour occurences at each zone size
		// test if there are different numbers of contours at
		// different zone sizes.
		// We can iterate over just one of the sets and pull out
		// the contents of both on that key in safety, because previous
		// tests have detected if they have non existent (ie. different size)
		// zones.
		
		HashMap<String,Integer> contourValueMap = new HashMap<String,Integer>(); // stores a numeric value indicating the number of the contour based on occurences at each zone size
		Set<Integer> zoneSizeSet = zoneSizeToContours.keySet();
		for(Integer zoneSize: zoneSizeSet) {
			ArrayList<String> sizeContourList = zoneSizeToContours.get(zoneSize);
			Collections.sort(sizeContourList);
	
			int count = 0;
			String countContour = sizeContourList.get(0);
			Iterator<String> sclIt = sizeContourList.iterator();
			while(sclIt.hasNext()) {
				String contour = sclIt.next();
				if(!contour.equals(countContour)) { // if we have reached the end of a sequence of contours

					// this code is repeated after the loop
					int valueToAdd = count*(int)Math.pow(zoneList.size()-zoneSize,contours.size()-1);
					Integer currentContourValue = contourValueMap.get(countContour);
					if(currentContourValue == null) {
						contourValueMap.put(countContour,valueToAdd);
					} else {
						contourValueMap.put(countContour,currentContourValue+valueToAdd);
					}
					countContour = contour;
					count = 1;
				} else {
					count++;
				}
				
			}
			// repeat the command thats inside the loop for the last contour
			int valueToAdd = count*(int)Math.pow(zoneList.size()-zoneSize,contours.size()-1);
			Integer currentContourValue = contourValueMap.get(countContour);
			if(currentContourValue == null) {
				contourValueMap.put(countContour,valueToAdd);
			} else {
				contourValueMap.put(countContour,currentContourValue+valueToAdd);
			}
		}
		
		// sort the contours
		ContourValueMapComparator cvMapComp = new ContourValueMapComparator(contourValueMap);
		Collections.sort(contours,cvMapComp);
		Collections.reverse(contours);
		
		ArrayList<ArrayList<String>> contourOrderList = groupContoursByMap(contours,contourValueMap);

		// TODO
		// try iterating through the contourOrderList, attempting to disambiguate
		// members of each list by seeing if any neighbours at each size
		// have a different value. Keep propagating changes until no
		// more changes found.
		// HashMap<String,HashMap<Integer,ArrayList<String>>> neighbourMap = findContourNeighboursByZoneSize();
		

		contourLabelMap = new HashMap<String,String>();
		char currentContourChar = 'a';
		for(ArrayList<String> cs: contourOrderList) {
			ArrayList<String> equalContours = new ArrayList<String>(cs);
			while(equalContours.size() != 0) {
				String currentContour = Character.toString(currentContourChar);
				String originalContour = "";
				if(equalContours.contains(currentContour)) {
					originalContour = currentContour;
				} else {
					originalContour = equalContours.get(0);
				}
				contourLabelMap.put(originalContour,currentContour);
//System.out.println(originalContour+" "+currentContour);
				equalContours.remove(originalContour);
				currentContourChar++;
			}

		}
		remapContourStrings(contourLabelMap);

	}
	public	HashMap<String,String> getcontourLabelMap(){
		
		return contourLabelMap;
	}
	
	/**
	 * Finds the contours a particular contour is
	 * entirely contained in
	 */
	public String containingZones(String contour) {
	
		ArrayList<String> cZoneList = new ArrayList<String>();
		ArrayList<String> contours = getContours();
		String zones = "";
		for(String zone : zoneList){	
			if(zone.contains(contour)){				
				cZoneList.add(zone);
			}
		}
		for(String con: contours){
				if(!con.equals(contour)&& contains(cZoneList, con))
					zones += con;			
		}
		return zones;
	}
	/**
	 * Finds the contours a particular contour is
	 * entirely contained in
	 */	
	public boolean contains(ArrayList<String> zoneList, String contour){
		
		for(String s: zoneList){
			if(!s.contains(contour))
				return false;
		}
		return true;
	}	
	
	public boolean addZone(String z) {
		String zoneString = z;
		if(zoneString.equals("0")) { // deal with "0" being the empty set
			zoneString = "";
		}

		if(zoneList.contains(zoneString)) {
			return false;
		}
		String orderedZone = orderZone(zoneString);
		zoneList.add(orderedZone);
		sortZoneList(zoneList);
		return true;
	}
	public boolean removeZone(String z) {
		String zoneString = z;
		if(zoneString.equals("0")) { // deal with "0" being the empty set
			zoneString = "";
		}
		
		int index = zoneList.indexOf(zoneString);
		if(index == -1) {
			return false;
		}
		zoneList.remove(index);
		return true;
	}
	
	public AbstractDiagram clone() {
		return new AbstractDiagram(this);
	}
	
	
	/** The number of times the contour occurs in the diagram */
	public int countZonesWithContour(String c) {
		int ret = 0;
		for(String z : getZoneList()) {
			if(z.contains(c)) {
				ret++;
			}
		}
		
		return ret;
	}
	public AbstractDiagram removeCurve(String s){
		ArrayList<String> temp = new ArrayList<String>();
		for(String s1: zoneList){
			if(!s1.contains(s)){
				temp.add(s1);
			}
		}
		return new AbstractDiagram(temp);		
	}
	
	
	public AbstractDiagram removePiercing(){
		for(String s : this.getContours()){
			ArrayList<String> zones = new ArrayList<String>();
			for(String s1: this.getZoneList()){
				if(s1.contains(s)){
					zones.add(s1);
				}
			}
			if(zones.size()==2){
				String s2 = zones.get(0);
				String s3 = zones.get(1);
				String diff = DualGraph.findLabelDifferences(s2,s3);
				if(diff.length() == 1){	
					AbstractDiagram temp = this.removeCurve(s);
					System.out.println("curvec " + s +" is curve " + diff + "'s piercing");
					System.out.println("curve " + s +" removed");
					return temp;
					
				}
			}
		}		
		return null;
	}
	
	public void checkInductivePiercingDiagram(){		
		boolean stop = false;
		AbstractDiagram newDiagram = this.clone();
		
		while(!stop){
			newDiagram = newDiagram.removePiercing();
			if(newDiagram == null){
				stop = true;
			//	System.out.println("not IPD");
				return;
			}
			if(newDiagram.getContours().size()==1){
				stop = true;
			//	System.out.println("is IPD");
			}
		}
		
		
		
	
	}
	
}
