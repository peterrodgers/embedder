package euler;

import java.util.*;
import java.awt.*;
import java.awt.geom.*;

import euler.construction.*;
import euler.display.*;
import euler.polygon.RegularPolygon;
import pjr.graph.*; 

/** Draw any dual graph */
public class GeneralConcreteDiagram extends ConcreteDiagram {
	
	
	/** gives the number of CP in each contour */
	protected HashMap<String,Integer> pointsInContour = null;
	/** gives the number of CP on a TE in each contour */
	protected HashMap<String,Integer> tePointsInContour = null;

	
	public static void main(String[] args) {
		
		ArrayList<ConcreteContour> contours = new ArrayList<ConcreteContour>();
		Polygon pol1 = new Polygon();
		pol1.addPoint(20, 20);
		pol1.addPoint(300, 20);
		pol1.addPoint(300, 300);
		pol1.addPoint(20,300);		
		pol1.translate(100, 100);
		ConcreteContour cc1 = new ConcreteContour("a", pol1);
		contours.add(cc1);		
	
		Polygon pol2 = new Polygon();
		pol2.addPoint(140, 140);		
		pol2.addPoint(400,140);
		pol2.addPoint(400,400);
		pol2.addPoint(140, 400);
		pol2.translate(100, 100);
		ConcreteContour cc2 = new ConcreteContour("b",pol2);
		contours.add(cc2);	

		
		ArrayList<Polygon> ps = new ArrayList<Polygon>();
		ArrayList<String> ss = new ArrayList<String>();
		Polygon p;
		
		p = new Polygon();
		p.addPoint(200, 100);
		p.addPoint(200, 300);
		p.addPoint(450, 300);
		p.translate(80, 80);
		ps.add(p);
		ss.add("a");
	
		ArrayList<ConcreteContour> ccs = new ArrayList<ConcreteContour>();
		Iterator<String> sI = ss.iterator();
		for(Polygon polygon : ps) {
			String s = sI.next();
			System.out.println(s);
			ccs.add(new ConcreteContour(s,polygon));
		}	
	}
	
	
	public GeneralConcreteDiagram(DualGraph dualGraph) {
		super(dualGraph);
		this.generateContours();
		//generateEulerGraph(concreteContours);
	}
	public GeneralConcreteDiagram(ArrayList<ConcreteContour> ccs){
		super(ccs);
	}
	
	/**
	 * This must be called before accessing concrete contours.
	 */
	public void generateContours() {
		
		dualGraph.labelEdgesWithContours();
		
//		dualGraph.addAllFaceSplits();

		dualGraph.formFaces();
		
		dualGraph.renameDisconnectedContours();
		
		cloneGraph = dualGraph.clone();
		cloneGraph.formFaces();
		Face outerFace = cloneGraph.getOuterFace();
		ArrayList<Node> outerNodes;
		if(outerFace == null) {
			// assume that there is no outer face because the dual forms a straight
			// line or is empty
			outerNodes = cloneGraph.getNodes();
		} else {
			outerNodes = outerFace.getNodeList();
		}
		ConcreteDiagram.addBoundingNodes(cloneGraph,OUTER_FACE_TRIANGULATION_BOUNDARY,outerNodes);

		// take out the bounding nodes and edges to allow triangulation
		ArrayList<Node> removedNodes = GeneralConcreteDiagram.getBoundingNodes(cloneGraph);
		ArrayList<Edge> removedEdges = GeneralConcreteDiagram.getBoundingEdges(cloneGraph);
		ConcreteDiagram.removeBoundingNodes(cloneGraph);
		
		cloneGraph.formFaces();
		Face cloneOuterFace = cloneGraph.getOuterFace();
		cloneGraph.triangulate();
		
		// add the bounding nodes and edges back in
		for(Node n : removedNodes) {
			cloneGraph.addNode(n);
		}
		for(Edge e : removedEdges) {
			cloneGraph.addEdge(e);
		}
		ConcreteDiagram.triangulateBoundingFace(cloneGraph,cloneOuterFace);
		routeContours();
	}
	
	
	/**
	 * Assumes a triangulation, assigns order to the TEs and builds contours
	 */
	public void routeContours() {
		
		// clear the cps for all TEs
		resetTriangulationEdges();

		// first order the contours on every triangulation edge
		assignTriangulationEdgeOrder();

		// then connect up the contour cycles in the TEs
		assignTriangulationEdgeConnectivity(); 
		
		if(optimizeContourAngles || optimizeMeetingPoints) {
		// remove the kinks in the graph
			optimiseContourLines();
		}
		
		//build polygons based on TE CP loops
		buildContours();
		
		if(fitCircles) {
System.out.println("Fit Circles");
		// try and place regular polygons in allowed regions
			fitRegularPolygons();
		}
	}
	
	
	/**
	 * Finds regular polygons that can be found for any of the contours.
	 */
	public void fitRegularPolygons() {
		for(ConcreteContour cc : getConcreteContours()) {
			
System.out.println("Concrete contour "+cc.getAbstractContour());
			Polygon p = findRegularPolygonInAllowedRegion(cc);
			if(p != null) {
System.out.println("Concrete contour "+cc.getAbstractContour()+" drawn with circle ");
				cc.setPolygon(p);
			}
		}
	}
	/**
	 * Find a regular polygon between the min and max of the contour cps.
	 * @return a regular polygon that fits, or null if one cannot be found.
	 */
	public Polygon findRegularPolygonInAllowedRegion(ConcreteContour cc) {
		
		String contour = cc.getAbstractContour();
		TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
		ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);

		// find the allowed range for the contour
		ContourLink cl = null;

		while(startCL != cl) {
			if(cl == null) {
				cl = startCL;
			}
			CutPoint cp = cl.getCutPoint();
			startTE.assignCPRange(cp,0.1);
			
			cl = cl.getNext();
		}	
		Polygon minP = cc.findMinPolygon(cloneGraph);
		Polygon maxP = cc.findMaxPolygon(cloneGraph);		

		Polygon ret = RegularPolygon.insideCircle(maxP,minP);
		
		return ret;
	}

	
	
	/**
	 * Takes the TEs after they have been assigned cut point order
	 * and finds ContourLink connectivity
	 */
	public void assignTriangulationEdgeConnectivity() {
		
		
		for(String contour : cloneGraph.findAbstractDiagram().getContours()) {
			TriangulationEdge startTE = cloneGraph.firstTriangulationEdgeWithContour(contour);
			TriangulationEdge currentTE = startTE;
			TriangulationFace currentTF = startTE.getTriangulationFaceList().get(0);
			ContourLink startCL = startTE.contourLinksWithContour(contour).get(0);
			ContourLink currentCL = startCL;
			
//			ContourLink nextCL = null;
			
			boolean start = true;
			
			while(start || currentCL != startCL) {
				// TF with meeting point is easy
				
				ArrayList<ContourLink> cls = currentTF.getTE1().contourLinksWithContour(contour);
				cls.addAll(currentTF.getTE2().contourLinksWithContour(contour));
				cls.addAll(currentTF.getTE3().contourLinksWithContour(contour));
				
				if(currentTF.getMeetingPoint() != null) {
					// route the contour to the meeting point
					// middle CL becomes currentCL and the other CL
					// on a TE with the contour label becomes the nextCL
					currentCL = routeThroughMiddleCL(currentCL,currentTF);
				} else if(cls.size() == 2) {
					// two CP TF is easy
					// cant just check for the current CL, as it may be at a meeting point
					ContourLink cl1 = cls.get(0);
					ContourLink cl2 = cls.get(1);
					if(start) {
						if(cl1 == startCL) {
							cl1.setNext(cl2);
							cl2.setPrev(cl1);
							currentCL = cl2;
						} else {
							cl2.setNext(cl1);
							cl1.setPrev(cl2);
							currentCL = cl1;
						}
					} else {
						if(cl1.getPrev() == null) {
							cl2.setNext(cl1);
							cl1.setPrev(cl2);
							currentCL = cl1;
						} else {
							cl1.setNext(cl2);
							cl2.setPrev(cl1);
							currentCL = cl2;
						}
					}
				} else if(cls.size() == 4) {
					// four CP TF
					ArrayList<ContourLink> clPairs = findCLPairs(currentTF,contour);
					
					ContourLink pair1a = clPairs.get(0);
					ContourLink pair1b = clPairs.get(1);
					ContourLink pair2a = clPairs.get(2);
					ContourLink pair2b = clPairs.get(3);
					if(pair1a == currentCL) {
						pair1a.setNext(pair1b);
						pair1b.setPrev(pair1a);
						currentCL = pair1b;
					} else if(pair1b == currentCL) {
						pair1b.setNext(pair1a);
						pair1a.setPrev(pair1b);
						currentCL = pair1a;
					} else if(pair2a == currentCL) {
						pair2a.setNext(pair2b);
						pair2b.setPrev(pair2a);
						currentCL = pair2b;
					} else if(pair2b == currentCL) {
						pair2b.setNext(pair2a);
						pair2a.setPrev(pair2b);
						currentCL = pair2a;
					} else {
						System.out.println("PROBLEM TF with "+cls.size()+ " ContourLinks for contour "+contour+" found");
					}
				}
				
				currentTE = currentCL.getCutPoint().getTriangulationEdge();
				
				if(currentTE.getTriangulationFaceList().size() < 2) {
					// connects to a outer face node so we have a problem
					// this may happen when bounding nodes are not added to
					// the graph
					System.out.println("Problem in assignTriangulationEdgeConnectivity: current TE has only one TF");
					break;
				}
				TriangulationFace nextTF = currentTE.getTriangulationFaceList().get(0);
				if(nextTF == currentTF) {
					nextTF = currentTE.getTriangulationFaceList().get(1);
				}
				currentTF = nextTF;
				
				start = false;

			}

		}
	}


	
	public ArrayList<ContourLink> findCLPairs(TriangulationFace tf, String contour) {
		
		TriangulationEdge te1 = tf.getTE1();
		TriangulationEdge te2 = tf.getTE2();
		TriangulationEdge te3 = tf.getTE3();
		ArrayList<ContourLink> cls1 = te1.contourLinksWithContour(contour);
		ArrayList<ContourLink> cls2 = te2.contourLinksWithContour(contour);
		ArrayList<ContourLink> cls3 = te3.contourLinksWithContour(contour);
		
		TriangulationEdge TE1CLa = null;
		TriangulationEdge TE1CLb = null;
		TriangulationEdge TE2CLa = null;
		TriangulationEdge TE2CLb = null;
		
		// either two 2 CL TEs or one 2 CL and two 1 CL
		if(cls1.size() == 1) {
			TE1CLa = te1;
		}
		if(cls1.size() == 2) {
			TE2CLa = te1;
		}
		if(cls2.size() == 1) {
			if(TE1CLa == null) {
				TE1CLa = te2;
			} else {
				TE1CLb = te2;
			}
		}
		if(cls2.size() == 2) {
			if(TE2CLa == null) {
				TE2CLa = te2;
			} else {
				TE2CLb = te2;
			}
		}
		if(cls3.size() == 1) {
			if(TE1CLa == null) {
				TE1CLa = te3;
			} else {
				TE1CLb = te3;
			}
		}
		if(cls3.size() == 2) {
			if(TE2CLa == null) {
				TE2CLa = te3;
			} else {
				TE2CLb = te3;
			}
		}
		
		ArrayList<ContourLink> ret = new ArrayList<ContourLink>();

		if(TE2CLb == null) {
			// one 2 CL and two 1 CL

			// find the 1 CL that connects to the from node of the 2 CL
			TriangulationEdge firstTE = TE1CLa;
			TriangulationEdge secondTE = TE1CLb;
			if(TE1CLb.getTo() == TE2CLa.getFrom() || TE1CLb.getFrom() == TE2CLa.getFrom()) {
				firstTE = TE1CLb;
				secondTE = TE1CLa;
			}
			
			ContourLink pair1a = TE2CLa.contourLinksWithContour(contour).get(0);
			ContourLink pair2a = TE2CLa.contourLinksWithContour(contour).get(1);
			
			ContourLink pair1b = firstTE.contourLinksWithContour(contour).get(0);
			ContourLink pair2b = secondTE.contourLinksWithContour(contour).get(0);
			
			ret.add(pair1a);
			ret.add(pair1b);
			ret.add(pair2a);
			ret.add(pair2b);
			
		} else {
			// two 2 CL TEs
			
			ContourLink pair1a = null;
			ContourLink pair1b = null;
			ContourLink pair2a = null;
			ContourLink pair2b = null;

			ArrayList<ContourLink> lista = TE2CLa.contourLinksWithContour(contour);
			ArrayList<ContourLink> listb = TE2CLb.contourLinksWithContour(contour);
			if(TE2CLa.getFrom() == TE2CLb.getFrom()) {
				pair1a = lista.get(0);
				pair1b = listb.get(0);
				pair2a = lista.get(1);
				pair2b = listb.get(1);
			} else if(TE2CLa.getTo() == TE2CLb.getFrom()) {
				pair1a = lista.get(1);
				pair1b = listb.get(0);
				pair2a = lista.get(0);
				pair2b = listb.get(1);
			} else if(TE2CLa.getFrom() == TE2CLb.getTo()) {
				pair1a = lista.get(0);
				pair1b = listb.get(1);
				pair2a = lista.get(1);
				pair2b = listb.get(0);
			} else if(TE2CLa.getTo() == TE2CLb.getTo()) {
				pair1a = lista.get(1);
				pair1b = listb.get(1);
				pair2a = lista.get(0);
				pair2b = listb.get(0);
			}

			ret.add(pair1a);
			ret.add(pair1b);
			ret.add(pair2a);
			ret.add(pair2b);
		}

		return ret;
	}


	/**
	 * Case where the TF is has a meeting point. Assign the route in and out of a
	 * new middle CL.
	 */
	protected ContourLink routeThroughMiddleCL(ContourLink currentCL, TriangulationFace tf) {
		
		String contour = currentCL.getContour();
	
		// create CL in meeting point
		ContourLink middleCL = new ContourLink(contour,tf.getMeetingPoint(),null,null);

		middleCL.setPrev(currentCL);
		currentCL.setNext(middleCL);
		
		ContourLink nextCL = null;
		ArrayList<ContourLink> clList = tf.getTE1().contourLinksWithContour(currentCL.getContour());
		clList.addAll(tf.getTE2().contourLinksWithContour(contour));
		clList.addAll(tf.getTE3().contourLinksWithContour(contour));
		for(ContourLink cl : clList) {
			if(cl != currentCL) {
				nextCL = cl;
			}
		}
		
		if(nextCL == null) {
			// should never get here
			System.out.println("Could not find a contour link for contourlink "+currentCL);
			return null;
		}
		
		nextCL.setPrev(middleCL);
		middleCL.setNext(nextCL);
		

		return nextCL;
	}

	
	/** Easy if there is just other connection, but there may be more than
	 * one in which case pick the one that causes least (should be zero)
	 * crosses
	 */
	protected ContourLink findConnectingCLInTF(DualGraph dg, ContourLink cl, TriangulationFace tf, boolean towardsMiddle) {
		CutPoint cp = cl.getCutPoint();
		TriangulationEdge te = cp.getTriangulationEdge();
		
		// Find the other two TEs
		TriangulationEdge te1 = tf.getTE1();
		TriangulationEdge te2 = tf.getTE2();
		if(te == te1) {
			te1 = tf.getTE3();
		}
		if(te == te2) {
			te2 = tf.getTE3();
		}
		ArrayList<ContourLink> clList = te1.contourLinksWithContour(cl.getContour());
		clList.addAll(te2.contourLinksWithContour(cl.getContour()));
		
		if(clList.size() == 1) {
			ContourLink nextCL = clList.get(0);
			cl.setNext(nextCL);
			nextCL.setPrev(cl);
			return nextCL;
		}

		// if there is more than one CL, pick the most appropriate
		// the one which linking to causes least crossing (should be zero crosses)
		// or if there are two with zero crosses, pick the one not in the same TE
		ArrayList<CutPoint> cycleList = tf.findCycleCutPoints();

		ArrayList<ContourLink> crossesForward = findCrossingCLsFromCLToSameContour(cl,cycleList);
		ContourLink clForward = crossesForward.get(crossesForward.size()-1);
		
		Collections.reverse(cycleList);
		ArrayList<ContourLink> crossesBackward = findCrossingCLsFromCLToSameContour(cl,cycleList);
		ContourLink clBackward = crossesBackward.get(crossesBackward.size()-1);
		ContourLink nextCL = clBackward;
		if(crossesForward.size() == crossesBackward.size()) {
			// if there are two with zero crosses
			// if one is assigned, use the other
			
			if(clBackward.getPrev() != null) {
				nextCL = clForward;
			} else {
				// pick the one not on the same TE
				TriangulationEdge teBackward = clBackward.getCutPoint().getTriangulationEdge();
				if(teBackward == te) {
					nextCL = clForward;
				} else {
					// here we have two possible CLs, both not causing crosses
					// and unassigned, on different TEs
				
					// find out which one leads to a
					// TF with a middle CP without crossing out of the face
					// Note this means the one that can find a TF with a middle
					// CP without crossing a TE with an edge, and without crossing
					// any other contour
					
					//if(hasPathToMiddleOrFaceEdge(dg, tf, clForward, towardsMiddle)) {
					//	nextCL = clForward;
					//}
					
				}
			}
		} else {
			if(crossesForward.size() < crossesBackward.size()) {
				nextCL = clForward;
			}
		}
		cl.setNext(nextCL);
		nextCL.setPrev(cl);
		return nextCL;
	}
	

	/**
	 * Find out if the startCL has a path that leads to the right
	 * CP, middle point or one with a face edge, the type of target is defined
	 * by the boolean. This performs a breadth first search.
	 */
	public boolean hasPathToMiddleOrFaceEdge(DualGraph dg, TriangulationFace startTF, ContourLink startCL, boolean towardsMeetingPoint) {

		dg.setTriangulationEdgesVisited(false);
		
		if(startTF.getMeetingPoint() != null) {
			return true;
		}
		
		TriangulationEdge startTE = startCL.getCutPoint().getTriangulationEdge();
		startTE.setVisited(true);
		if(startTE.getEdge() != null) {
			return false;
		}

		boolean start = true;
		ArrayList<ContourLink> queue = new ArrayList<ContourLink>();
		queue.add(startCL);
		TriangulationFace currentTF = startTF;
		while(queue.size() != 0) {
			ContourLink currentCL = queue.get(0);


			queue.remove(0);
			CutPoint currentCP = currentCL.getCutPoint();
			TriangulationEdge currentTE = currentCP.getTriangulationEdge();
			ArrayList<TriangulationFace> faceList = currentTE.getTriangulationFaceList();
			if(start) {
				currentTF = startTF;
				start = false;
			}
			if(faceList.get(0) == currentTF && faceList.size()>1) {
				currentTF = faceList.get(1);
			} else {
				currentTF = faceList.get(0);
			}
			if(currentTF == startTF) {
				return false;
			}
			
			if(towardsMeetingPoint && currentTF.getMeetingPoint() != null) {
				// if we are going to a meeting point and found one, then
				// there is a route to a meeting point
				return true;
			}
			if(!towardsMeetingPoint && currentTE.getEdge() != null) {
				// if we are going to a face edge and found one, then
				// there is a route to a face edge point
				return true;
			}
			
			ArrayList<CutPoint> cycleList = currentTF.findCycleCutPoints();
			ArrayList<ContourLink> crossesForward = findCrossingCLsFromCLToSameContour(currentCL,cycleList);
			ContourLink clForward = crossesForward.get(crossesForward.size()-1);
			
			Collections.reverse(cycleList);
			ArrayList<ContourLink> crossesBackward = findCrossingCLsFromCLToSameContour(currentCL,cycleList);
			ContourLink clBackward = crossesBackward.get(crossesBackward.size()-1);

			if(crossesForward.size() == 1) {
				TriangulationEdge thisTE = clForward.getCutPoint().getTriangulationEdge();
				if(clForward.getPrev() == null && !thisTE.getVisited() && thisTE != currentTE && thisTE.getEdge() == null) {
					queue.add(clForward);
					thisTE.setVisited(true);
				}
			}
			if(crossesBackward.size() == 1) {
				TriangulationEdge thisTE = clBackward.getCutPoint().getTriangulationEdge();
				if(clBackward.getPrev() == null && !thisTE.getVisited() && thisTE != currentTE && thisTE.getEdge() == null) {
					queue.add(clBackward);
					thisTE.setVisited(true);
				}
			}	
		}
		
		return false;
	}


	/**
	 * Find cl in the list, then find the CLs that it has to cross to join
	 * the next CL in the list, the CL with the same contour is always the last in
	 * the list.
	 */
	ArrayList<ContourLink> findCrossingCLsFromCLToSameContour(ContourLink cl,ArrayList<CutPoint> cycleCPList) {
		ArrayList<CutPoint> cpList = new ArrayList<CutPoint>(cycleCPList);
		CutPoint cp = cl.getCutPoint();
		while(cpList.get(0) != cp) {
			// rotate till this cl is at the start of the list
			CutPoint firstCL = cpList.get(0);
			cpList.add(firstCL);
			cpList.remove(0);
		}
		// remove the CP with the desired CL
		cpList.remove(0);
		
		String contour = cl.getContour();

		// find the required CL with the same contour and
		// get  the list of CPs upto, but not including it
		ArrayList<CutPoint> inbetweenCPs = new ArrayList<CutPoint>();
		ContourLink joiningCL = null;
		for(CutPoint tryCP : cpList) {
			for(ContourLink tryCL : tryCP.getContourLinks()) {
				if(tryCL.getContour().equals(contour)) {
					joiningCL = tryCL;
					break;
				}
			}
			if(joiningCL != null) {
				break;
			} else {
				inbetweenCPs.add(tryCP);
			}
		}
		
		// now get a list of contour links with non duplicated contours
		ArrayList<ContourLink> ret = new ArrayList<ContourLink>();
		for(CutPoint tryCP : inbetweenCPs) {
			for(ContourLink tryCL : tryCP.getContourLinks()) {
				String tryContour = tryCL.getContour();
				ContourLink duplicatedCL = null;
				
				for(ContourLink testDuplicateCL : ret) {
					if(testDuplicateCL.getContour().equals(tryContour)) {
						duplicatedCL = testDuplicateCL;
					}
				}
				if(duplicatedCL != null) {
					ret.remove(duplicatedCL);
				} else {
					ret.add(tryCL);
				}
			}
		}
		
		ret.add(joiningCL);
		
		return ret;
	}
	

	/**
	 * Orders the contours in each triangulation edge. Find a single
	 * meeting point for all the contours. Find a triangulation face
	 * containing all the contours in the face and place the 
 	 * meeting point at the centre of the TF. All other TFs have
 	 * no crossing points.
 	 */
	public void assignTriangulationEdgeOrder() {
		ArrayList<TriangulationEdge> teList = cloneGraph.findTriangulationEdges();
		ArrayList<TriangulationFace> tfList = cloneGraph.getTriangulationFaces();
		ArrayList<Face> fList = cloneGraph.getFaces();

		// find the 0 contour edges and assign no coordinates (these are in
		// the triangulation of the outer faces)
		for (TriangulationEdge te : teList) {
			if (te.getCutPoints() != null) {
				// don't want to deal with any edges already assigned coordinates
				continue;
			}
			if (te.getLabel().length() == 0) {
				ArrayList<String> contours = new ArrayList<String>();
				te.assignCutPointsBetweenNodes(contours);
			}
		}

		// all TEs with an associated graph edge have one ContourLink
		for (TriangulationEdge te : teList) {
			if (te.getCutPoints() != null) {
				// don't want to deal with any edges already assigned coordinates
				continue;
			}
			Edge e = te.getEdge();
			if (e != null) {
				ArrayList<String> contours = new ArrayList<String>();
				contours.add(e.getLabel());
				te.assignCutPointsBetweenNodes(contours);
			}
		}
		
		// all TEs that have one cut point are trivial to assign
		// these have one contour or one parallel contour running through them
		for (TriangulationEdge te : teList) {
			if (te.getCutPoints() != null) {
				// don't want to deal with any edges already assigned coordinates
				continue;
			}
			String teLabel = te.getLabel();
			if (faceHasEdgeWithLabel(te.getFaceList().get(0),teLabel)) {
				ArrayList<String> contours = new ArrayList<String>();
				contours.add(teLabel);
				te.assignCutPointsBetweenNodes(contours);
			}
		}
		
		
		// find a suitable TF for the meeting point of each face, 
		// the one that is most central of the ones with the most
		// contours passing through
		for(Face f : fList) {
			// first find the list of TFs with the max number of contours
			int maxContourSize = 0;
			ArrayList<TriangulationFace> maxTFList = new ArrayList<TriangulationFace>(); 
			for(TriangulationFace tf : tfList) {
				if(tf.getFace() != f) {
					continue;
				}
				ArrayList<String> contours = tf.findContourList();
				if(contours.size() > maxContourSize) {
					maxContourSize = contours.size();
					maxTFList = new ArrayList<TriangulationFace>();
					maxTFList.add(tf);
				}
				if(contours.size() == maxContourSize) {
					maxTFList.add(tf);
				}
			}
			
			// find the most central TF of those with the max number of contours
			Rectangle faceBounds = f.getPolygon().getBounds();
			Point faceCentrePoint = new Point(faceBounds.x + faceBounds.width/2,faceBounds.y + faceBounds.height/2);

			TriangulationFace centreTF = null;
			double minDistance = Double.MAX_VALUE;
			for(TriangulationFace tf : maxTFList) {
				Point tfCentrePoint = tf.centroid();
				double currentDistance = pjr.graph.Util.distance(tfCentrePoint, faceCentrePoint);
				if(currentDistance < minDistance) {
					minDistance = currentDistance;
					centreTF = tf;
				}
				
			}

			if(centreTF == null) {
				// should never get here
				System.out.println("Could not find a TriangulationFace for face "+f);
				continue;
			}
			
			f.setMeetingTF(centreTF);
			
			// TODO not complete
			// fix things so all contours route through the meeting point
			ArrayList<String> meetingContours = f.getMeetingTF().getTE1().findContourList();
			meetingContours.addAll(f.getMeetingTF().getTE2().findContourList());
			meetingContours.addAll(f.getMeetingTF().getTE3().findContourList());
			Collections.sort(meetingContours);
			AbstractDiagram.removeDuplicatesFromSortedList(meetingContours);
//DiagramPanel.areas.add(new Area(centreTF.generateTriangle()));
			if(f.findContours().size() > meetingContours.size()) {
				ArrayList<String> missingContours = new ArrayList<String>();
				for(String c : f.findContours()) {
					if(!meetingContours.contains(c)) {
						missingContours.add(c);
					}
				}
				
			//TODO make the missingContours meet at the meetingTF
			// note two cases, disconnected components (0 a b) and
			// cases without extra eddges (0 a ab b bc c)
			//
			// Note this is only important if we want to accurately
			// represent the embedding of the Euler graph in non
			// atomic cases and cases where extra edges have not
			// been added.
			}

			
			if(!isSimpleTwoCross(f)) {
				// if the crossing is just two sets of contours crossing, then
				// dont create a new cut point in the middle of the TF, they cross
				// without needing such a point, otherwise
				// put the meeting point in the chosen TF
				Point centroid = centreTF.centroid();
				CutPoint cp = new CutPoint(centreTF, new ArrayList<ContourLink>(),centroid);
				centreTF.setMeetingPoint(cp);
			}
		}
		
		boolean unassignedTEs = true;
		while (unassignedTEs) { // keep going till all TEs are ordered. That is,
								// we have no TEs with 1 unassigned face
			unassignedTEs = false;
			for (TriangulationFace tf : tfList) {
				if(tf.isMeetingTF()) {
					continue; // dont set the TEs of the meeting TF, let the other TFs set the coordinates
				}

				TriangulationEdge te1 = tf.getTE1();
				TriangulationEdge te2 = tf.getTE2();
				TriangulationEdge te3 = tf.getTE3();
				ArrayList<CutPoint> cc1 = te1.getCutPoints();
				ArrayList<CutPoint> cc2 = te2.getCutPoints();
				ArrayList<CutPoint> cc3 = te3.getCutPoints();
				if (cc1 == null && cc2 != null && cc3 != null) {
					assignTFs(tf, te1, te2, te3);
					unassignedTEs = true;
				} else if (cc1 != null && cc2 == null && cc3 != null) {
					assignTFs(tf, te2, te1, te3);
					unassignedTEs = true;
				} else if (cc1 != null && cc2 != null && cc3 == null) {
					assignTFs(tf, te3, te1, te2);
					unassignedTEs = true;
				}
			}
			

		}
		// repair any contours that do not reach the meeting triangle
		for(Face f : cloneGraph.getFaces()) {
			ArrayList<String> missingContours = new ArrayList<String>();
			TriangulationFace meetingTF = f.getMeetingTF();
			ArrayList<String> meetingContours = meetingTF.findContourList();
//System.out.println("face contours "+f.findContours());
//System.out.println("meeting contours "+meetingContours);
//System.out.println("meeting te1 "+meetingTF.getTE1().getLabel());
//System.out.println("meeting te2 "+meetingTF.getTE2().getLabel());
//System.out.println("meeting te3 "+meetingTF.getTE3().getLabel());
			for(String c : f.findContours()) {
				if(!meetingContours.contains(c)) {
					missingContours.add(c);
				}
			}
//if(missingContours.size() != 0) {
//DiagramPanel.areas.add(new Area(meetingTF.generateTriangle()));
//}
			
//System.out.println("missing "+missingContours);
		}
		
		
	}
	
	
	/**
	 * Find out if the face has an edge with the given label
	 */
	public boolean faceHasEdgeWithLabel(Face face, String label) {
		if(face == null) {
			return false;
		}
		for(Edge e : face.findEdgeList()) {
			if(label.equals(e.getLabel())) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Find out if the face has two sets of contours that cross.
	 * This means they can cross in a straight line.
	 */
	public boolean isSimpleTwoCross(Face face) {
		ArrayList<String> edgeLabelList = new ArrayList<String>();
		for(FaceEdge fe : face.getFaceEdgeList()) {
			edgeLabelList.add(fe.getEdge().getLabel());
		}
		if(edgeLabelList.size() != 4) {
			return false;
		}
		if(!edgeLabelList.get(0).equals(edgeLabelList.get(2))) {
			return false;
		}
		if(!edgeLabelList.get(1).equals(edgeLabelList.get(3))) {
			return false;
		}
			
		return true;
	}
	
	/**
	 * Order the unassigned TE in a TF with two assigned TE
	 * or use as the Face meeting TF if the TF contains all
	 * the contours and has not previously been assigned.
	 */
	public void assignTFs(TriangulationFace tf,
			TriangulationEdge unassignedTE, TriangulationEdge assignedTE1,
			TriangulationEdge assignedTE2) {
		
		
		// assign TE CPs so that they all go through the unassigned
		// TE without crossing
		Node from = unassignedTE.getFrom();
		Node to = unassignedTE.getTo();
		TriangulationEdge te1 = tf.findOtherConnectingTE(to, unassignedTE);
		TriangulationEdge te2 = tf.findOtherConnectingTE(from, unassignedTE);
		
		ArrayList<CutPoint> ccList1 = new ArrayList<CutPoint>(te1.getCutPoints());
		ArrayList<CutPoint> ccList2 = new ArrayList<CutPoint>(te2.getCutPoints());
		
		// make sure the lists are pointing the right way,
		// they should be assigned the reverse of the list
		// derived from starting at the unassigned
		// from node ccList then followed by the next ccList.
		if (to == te1.getTo()) {
			Collections.reverse(ccList1);
		}
		if (from == te2.getFrom()) {
			Collections.reverse(ccList2);
		}

		ArrayList<CutPoint> contourCycle = new ArrayList<CutPoint>(ccList1);
		contourCycle.addAll(ccList2);
		Collections.reverse(contourCycle);
		
		// go round the face and pick the contours in order
		// use that order for the unassigned face
		ArrayList<String> contourOrder = new ArrayList<String>();

		for (CutPoint cp : contourCycle) {
			String contours = "";
			for(ContourLink cl : cp.getContourLinks()) {
				contours += cl.getContour();
			}
			contourOrder.add(contours);
		}
		unassignedTE.assignCutPointsBetweenNodes(contourOrder);
			
	}

	
	
	/**
	 * Find the ideal angle for each cut point.
	 * Triangulation Edges should already have been assigned.
	 */
	public void optimiseContourLines() {

		generatePointsInContour();
		ArrayList<CutPoint> cutPoints = getCloneGraph().findCutPoints();
		ArrayList<CutPoint> reversedCutPoints = new ArrayList<CutPoint>(cutPoints);
		Collections.reverse(reversedCutPoints);
		for(int tries = 0;tries <= 4 ; tries++) {
//for(int tries = 0;tries <= 1 ; tries++) {
			// do this several times so that earlier assigned contours can try freed up space from later contour assignments
			for(int i =0; i < CP_OPTIMIZATION_ITERATIONS; i++) {
				double moveDistance = CP_OPTIMIZATION_ITERATIONS-i;
//for(int i =0; i < 1; i++) {
//double moveDistance = 10;

				// one direction
				for(CutPoint cp : cutPoints) {
					optimizeCutPoint(cp,moveDistance);
				}
				// then the other
				for(CutPoint cp : reversedCutPoints) {
					optimizeCutPoint(cp,moveDistance);
				}
			}
		}
	}
	


	public void optimizeCutPoint(CutPoint cp, double moveDistance) {
		
		TriangulationEdge te = cp.getTriangulationEdge();
		
		if(te == null) { // CP not on a TE
			if(optimizeMeetingPoints) {
				TriangulationFace tf = cp.getTriangulationFace();
				Polygon p = tf.generateTriangle();
				for(Point newP : getSurroundingPoints(cp.coordinate,moveDistance)) {
					if(p.contains(newP)) {
						double oldQuality = findCPStraightnessQuality(cp);
						Point oldP = new Point(cp.getCoordinate());
						cp.setCoordinate(newP);
						double newQuality = findCPStraightnessQuality(cp);
						if(newQuality >= oldQuality) { // reset coordinate if the new quality is worse
							cp.setCoordinate(oldP);
						}
					}
				}
			}
		} else if(optimizeContourAngles) { // CP on a TE
			// find the allowed range for the cut point
			te.assignCPRange(cp,CONTOUR_GAP_PERCENT);
			
			// find first new quality
			Point[] newPoints = getNewCLPointsOnTE(cp, moveDistance);
			Point p1 = newPoints[0];
			if(p1 != null) {
				double oldQuality = findCPQuality(cp);
				Point oldP = new Point(cp.getCoordinate());
				cp.setCoordinate(p1);
				double newQuality = findCPQuality(cp);
				if(newQuality >= oldQuality) { // reset coordinate if the new quality is worse
					cp.setCoordinate(oldP);
				}
			}
			Point p2 = newPoints[1];
			if(p2 != null) {
				double oldQuality = findCPQuality(cp);
				Point oldP = new Point(cp.getCoordinate());
				cp.setCoordinate(p2);
				double newQuality = findCPQuality(cp);
				if(newQuality >= oldQuality) { // reset coordinate if the new quality is worse
					cp.setCoordinate(oldP);
				}
			}
			
		}
		
			
	}


	/**
	 * Measure the straightness of lines that meet at the cut point.
	 * Sum of square of difference from PI for all the angles at the point.
	 */
	public double findCPStraightnessQuality(CutPoint cp) {
		double ret = 0.0;
		for(ContourLink cl : cp.getContourLinks()) {
			double angle = clAngle(cl);
			double diff = Math.PI - angle;
			double quality = diff*diff;
			ret += quality;

		}
		return ret;

	}



	/**
	 * Given a CP, find the quality of it by adding up the
	 * quality of all the CLs in it. generatePointsInContour needs to
	 * be called before this is executed.
	 */
	public double findCPQuality(CutPoint cp) {
		double ret = 0.0;
		for(ContourLink cl : cp.getContourLinks()) {
			// use tePointsInContour, as the meeting points are optimized as straight lines
			// int clTotal = tePointsInContour.get(cl.getContour()); 
			int clTotal = tePointsInContour.get(cl.getContour()); 
			
			double averageAngle = Math.PI*(clTotal-2)/(double)clTotal;
			double clQuality = clQuality(cl, averageAngle);
			ret += clQuality;
		}
		return ret;
	}



	/**
	 * Get the 8 points surrounding coordinate on a square that has
	 * the given distance from the edge to the centre.
	 */
	public static ArrayList<Point> getSurroundingPoints(Point coordinate, double inDistance) {
		int x = coordinate.x;
		int y = coordinate.y;
		
		int distance = pjr.graph.Util.convertToInteger(inDistance);
		ArrayList<Point> ret = new ArrayList<Point>();
		ret.add(new Point(x+distance,y));
		ret.add(new Point(x+distance,y+distance));
		ret.add(new Point(x,y+distance));
		ret.add(new Point(x+distance,y-distance));
		ret.add(new Point(x,y-distance));
		ret.add(new Point(x-distance,y-distance));
		ret.add(new Point(x-distance,y));
		ret.add(new Point(x-distance,y+distance));
		
		return ret;
	}



	/**
	 * Finds two new points for the CP on the TE moveDistance along. If a point
	 * is outside the limit, null is returned.
	 */
	public Point[] getNewCLPointsOnTE(CutPoint cp, double moveDistance) {
		TriangulationEdge te = cp.getTriangulationEdge();
		Node from = te.getFrom();
		Node to = te.getTo();

		double distanceX = Math.abs(to.getX()-from.getX());
		double distanceY = Math.abs(to.getY()-from.getY());

		double shareX = distanceX/(distanceX+distanceY);
		double shareY = distanceY/(distanceX+distanceY);
		
		double moveX = moveDistance*shareX;
		if(to.getX() < from.getX()) {
			moveX = 0-moveX;
		}

		double moveY = moveDistance*shareY;
		if(to.getY() < from.getY()) {
			moveY = 0-moveY;
		}
		
		Point currentP = cp.getCoordinate();
		
		// two new points to try
		Point2D.Double pd1 = new Point2D.Double(currentP.x+moveX,currentP.y+moveY);
		Point2D.Double pd2 = new Point2D.Double(currentP.x-moveX,currentP.y-moveY);
		int p1x = pjr.graph.Util.convertToInteger(pd1.x);
		int p1y = pjr.graph.Util.convertToInteger(pd1.y);
		int p2x = pjr.graph.Util.convertToInteger(pd2.x);
		int p2y = pjr.graph.Util.convertToInteger(pd2.y);
		Point p1 = new Point(p1x,p1y);
		Point p2 = new Point(p2x,p2y);
		// deal with rounding errors and make sure the points are on the TE
		p1 = pjr.graph.Util.perpendicularPoint(p1,from.getCentre(),to.getCentre());
		p2 = pjr.graph.Util.perpendicularPoint(p2,from.getCentre(),to.getCentre());
		
		if(!pjr.graph.Util.pointIsWithinBounds(p1, cp.getMinLimit(), cp.getMaxLimit())) {
			p1 = null;
		}
		
		if(!pjr.graph.Util.pointIsWithinBounds(p2, cp.getMinLimit(), cp.getMaxLimit())) {
			p2 = null;
		}
		Point[] ret = new Point[2];
		ret[0] = p1;
		ret[1] = p2;
		
		return ret;
	}
	
	
		
	/**
	 * Quality of the given point for the CL, plus the one before and one after.
	 */
	public double clQuality(ContourLink cl, double averageAngle) {
		ContourLink cl1 = cl.getPrev();
		ContourLink cl3 = cl.getNext();
		
		double quality1 = singlePointQuality(cl1,averageAngle);
		double quality2 = singlePointQuality(cl,averageAngle);
		double quality3 = singlePointQuality(cl3,averageAngle);

		double quality = quality1*quality1+quality2*quality2+quality3*quality3;

		return quality;
	}
	

	/**
	 * Quality is the square of the difference from required
	 * angle, with an extra penalty for convex points. Returns
	 * low numbers for good quality, high numbers for poor quality.
	 */
	public double singlePointQuality(ContourLink cl, double averageAngle) {
		double angle = clAngle(cl);
		double diff = angle-averageAngle;
		double pointQuality = diff*diff;
		// penalize concave points more heavily
		if(angle > Math.PI) {
			pointQuality = pointQuality*10;
		}
		pointQuality = pointQuality*pointQuality;
		return pointQuality;
	}
	
	
	/**
	 * The angle made by a contour link.
	 */
	public double clAngle(ContourLink cl) {
		return clAngle(cl,cl.getCutPoint().getCoordinate());
	}
	
	
	/**
	 * The angle made by a contour link if it were given the argument coordinate
	 */
	public double clAngle(ContourLink cl, Point coordinate) {
		ContourLink cl1 = cl.getPrev();
		ContourLink cl3 = cl.getNext();
		Point p1 = cl1.getCutPoint().getCoordinate();
		Point p3 = cl3.getCutPoint().getCoordinate();

		TriangulationEdge te = cl.getCutPoint().getTriangulationEdge();
		
		// we know the node with the contour label is inside the contour
		Node insideNode = null;
		if(te == null) {
			TriangulationFace tf = cl.getCutPoint().getTriangulationFace();
//System.out.println(cl.getContour()+" n1 "+tf.getNode1().getLabel()+" n2 "+tf.getNode2().getLabel()+" n3 "+tf.getNode3().getLabel());
			if(tf.getNode1().getLabel().contains(cl.getContour())) {
				insideNode = tf.getNode1();
			} else if(tf.getNode2().getLabel().contains(cl.getContour())) {
				insideNode = tf.getNode2();
			} else {
				//TODO sometimes no node in the triangle contains the contour in which case it defaults to this
				insideNode = tf.getNode3();
			}
		} else {
			Node from = te.getFrom();
			Node to = te.getTo();
			insideNode = from;
			if(to.getLabel().contains(cl.getContour())) {
				insideNode = to;
			}
		}
		
		double angle = pjr.graph.Util.getRelativeAngle(p1, coordinate, p3, insideNode.getCentre());
		return angle;
	}



	
	/**
	 * Calculates the number of points in each contour, to avoid
	 * recalculation when finding the average angle of a point in a contour.
	 */
	public void generatePointsInContour() {
		// find the number of points in each contour, this will be used to find
		// the required angle when optimizing the position of cutpoints
		pointsInContour = new HashMap<String,Integer>();
		tePointsInContour = new HashMap<String,Integer>();
		for(String c : getCloneGraph().findAbstractDiagram().getContours()) {
			ArrayList<ContourLink> cls = cloneGraph.findCLsForContour(c);
			Integer count = cls.size();
			pointsInContour.put(c,count);

			int teCount = 0;
			for(ContourLink cl : cls) {
				if(cl.getCutPoint().getTriangulationEdge() != null) {
					teCount++;
				}
			}
			tePointsInContour.put(c,teCount);
		}
		
	}
	
}


