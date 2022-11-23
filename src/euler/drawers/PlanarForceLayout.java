package euler.drawers;

import java.util.*;
import java.awt.geom.*;
import euler.*;
import pjr.graph.*;
import pjr.graph.drawers.*;
/**
 * A version of Eades spring embedder for laying out planar graphs
 * without changing the planarity and faces
 *
 * @author Peter Rodgers
 * @author Leishi Zhang
 */
public class PlanarForceLayout {

	/** the strength of the node-edge repulsion */
    protected double q = 1500000;
    /** The strength of a spring */
	protected double k = 0.5;
	/** The strength of the repulsive force */
	protected double r = 3000000.0;
	/** The amount of movement on each iteration */
	protected double f = 0.1;
	/** The maximum time to run for, in milliseconds */
	protected long timeLimit = 100000;
	/** If all node forces are below forceThreshold, then the algorithm stops */
	protected double forceThreshold = 0.1;
//forceThreshold = Double.MAX_VALUE/2; // uncomment this to ensure 1 iteration
	/** Maximum allowed movement for a node on an iteration */
	protected double maxNodeMovement = 3.0;
	/** Set to redraw on each iteration */
	protected boolean animateFlag = true;
	/** How close a node can get to the edge of the panel */
	protected int borderLimit = 40;
	/**
	 * If set the border is maintained by dissallowing nodes over it, otherwise
	 * the graph is fitted to the graphPanel after drawing.
	 */
	protected boolean useBorderLimit = false;
	/** The number of iterations */
	protected int iterations = 300;

	/** The maximum force applied on one iteration */
	protected double maxForce = Double.MAX_VALUE;
	/** The node buffer. This holds copies of node locations */
	protected DrawCoordCollection nodeBuffer = new DrawCoordCollection();
	/** Gives the number of milliseconds the last graph drawing took */
	protected long time = 0;
	/** The nodes selected at the start of the algorithm */
	protected ArrayList<Node> noMoveNodes = new ArrayList<Node>();
	protected ArrayList<Face> faces = null;	
	protected DiagramPanel diagramPanel = null;
	
	protected DualGraph dualGraph = null;
	
	
	/** Trivial constructor. Must set a diagram panel or dual graph after this.*/
	public PlanarForceLayout() {
	}

	/** Trivial constructor. */
	public PlanarForceLayout(DualGraph dg) {
		dualGraph = dg;
	}
	
	/** Trivial constructor. */
	public PlanarForceLayout(DiagramPanel dp) {
		diagramPanel = dp;
		dualGraph = dp.getDualGraph();
	}
	
	public long getTimeLimit() {return timeLimit;}
	public long getTime() {return time;}
	public double getQ() {return q;}
	public double getK() {return k;}
	public double getR() {return r;}
	public double getF() {return f;}
	public boolean getAnimateFlag() {return animateFlag;}
	public int getBorderLimit() {return borderLimit;}
	public boolean getUseBorderLimit() {return useBorderLimit;}
	public int getIterations() {return iterations;}
	public DiagramPanel getDiagramPanel() {return diagramPanel;}
	public DualGraph getDualGraph() {return dualGraph;}


	public void setTimeLimit(int inT) {timeLimit = inT;}
	public void setQ(double inQ) {q = inQ;}
	public void setK(double inK) {k = inK;}
	public void setR(double inR) {r = inR;}
	public void setF(double inF) {f = inF;}
	public void setAnimateFlag(boolean inAnimateFlag) {animateFlag = inAnimateFlag;}
	public void setBorderLimit(int limit) {borderLimit = limit;}
	public void setUseBorderLimit(boolean flag) {useBorderLimit = flag;}
	public void setIterations(int iterations) {this.iterations = iterations;}
	public void setDualGraph(DualGraph dg) {dualGraph = dg;}
	public void setDiagramPanel(DiagramPanel dp) {
		diagramPanel = dp;
		if(dp != null) {
			dualGraph = dp.getDualGraph();
		}
	}

	
	public void drawGraph() {
		
		faces = getDualGraph().formFaces();

		maxForce = Double.MAX_VALUE;
		nodeBuffer.setUpNodes(getDualGraph().getNodes());
		int i = 0;
		long startTime = System.currentTimeMillis();
		while(i < iterations && maxForce-forceThreshold > 0) {
			i++;
			maxForce = 0.0;
			embed();
			if(animateFlag && getDiagramPanel() != null) {
				nodeBuffer.switchOldCentresToNode();
				getDiagramPanel().update(getDiagramPanel().getGraphics());
	
			}
			if((System.currentTimeMillis() - startTime) > timeLimit) {
				System.out.println("PlanarForceLayout exit due to time expiry after "+timeLimit+" milliseconds and "+i+" iterations");
				break;
			}
		}
		//System.out.println("Iterations: "+i+", max force: "+maxForce+", seconds: "+((System.currentTimeMillis() - startTime)/1000.0));

		nodeBuffer.switchOldCentresToNode();
		
		if(!useBorderLimit && getDiagramPanel() != null) {
			getDiagramPanel().fitGraphInPanel();
		}
        //getDiagramPanel().paintContours((Graphics2D)getDiagramPanel().getGraphics(),getDiagramPanel().getDualGraph());
	    //getDiagramPanel().update(getDiagramPanel().getGraphics());
		if(!animateFlag && getDiagramPanel() != null) {
			getDiagramPanel().update(getDiagramPanel().getGraphics());
		}
		
	}
	
	/**
	 * Move all the nodes in the graph.
	 */
	public void embed() {
		for(DrawCoord nb : nodeBuffer.getBufferedNodes()) {
			if (!noMoveNodes.contains(nb.getNode())) {
				Point2D.Double moveTo = force(nb);
				nb.setNewCentre(moveTo);
			}
		}
		nodeBuffer.switchNewCentresToOld();
	}
	
	/**
	 * Finds the new location of a node.
	 */
	public Point2D.Double force(DrawCoord nb) {
		Node n = nb.getNode();
		Point2D.Double p = nb.getOldCentre();

		double xRepulsive = 0.0;
		double yRepulsive = 0.0;
		double xAttractive = 0.0;
		double yAttractive = 0.0;
		double xEdgeRepulsive = 0.0;
		double yEdgeRepulsive = 0.0;
		double [] edgeRepulsive = new double [2];
		
		for(DrawCoord nextNb : nodeBuffer.getBufferedNodes()) {
			
			if(nb != nextNb) {
				Point2D.Double nextP = nextNb.getOldCentre();
				Node nextN = nextNb.getNode();

				double distance = p.distance(nextP);
				double xDistance = p.x - nextP.x;
				double yDistance = p.y - nextP.y;

				double absDistance = Math.abs(distance);
				double absXDistance = Math.abs(xDistance);
				double absYDistance = Math.abs(yDistance);

				double xForceShare = absXDistance/(absXDistance+absYDistance);
				double yForceShare = absYDistance/(absXDistance+absYDistance);

				if (n.connectingNodes().contains(nextN)) {
					if(xDistance > 0) {
						xAttractive -= k*xForceShare*absDistance;
					} else {
						if(xDistance < 0) {
							xAttractive += k*xForceShare*absDistance;
						}
					}

					if(yDistance > 0) {
						yAttractive -= k*yForceShare*absDistance;
					} else {
						if(yDistance < 0) {
							yAttractive += k*yForceShare*absDistance;
						}
					}
				}
				double repulsiveForce = r / (distance * distance);

				if(xDistance > 0) {
					xRepulsive += repulsiveForce*xForceShare;
				} else {
					if(xDistance < 0) {
						xRepulsive -= repulsiveForce*xForceShare;
					}
				}
				if(yDistance > 0) {
					yRepulsive += repulsiveForce*yForceShare;
				} else {
					if(yDistance < 0) {
						yRepulsive -= repulsiveForce*yForceShare;
					}
				}
			}
		}
		
		edgeRepulsive = getEdgeRepulsion(nb);
		xEdgeRepulsive = edgeRepulsive[0];
		yEdgeRepulsive = edgeRepulsive[1];

		double totalXForce = f*(xRepulsive + xAttractive + xEdgeRepulsive);
		double totalYForce = f*(yRepulsive + yAttractive + yEdgeRepulsive);
		
// edge force only
//double totalXForce = f*(xEdgeRepulsive);
//double totalYForce = f*(yEdgeRepulsive);

// attraction and repulsion only
//double totalXForce = f*(xRepulsive + xAttractive);
//double totalYForce = f*(yRepulsive + yAttractive);


		
		double totalForce = Math.sqrt(totalXForce*totalXForce+totalYForce*totalYForce);
		
		if(totalForce > maxForce){
			maxForce = totalForce;
		}
		
		if(totalForce > maxNodeMovement) {
			double forceScale = maxNodeMovement/totalForce;
			totalXForce = totalXForce*forceScale;
			totalYForce = totalYForce*forceScale;
		}

		double newX = p.x + totalXForce;
		double newY = p.y + totalYForce;
		
		// stops the node going over the edge of the graph panel
		if(useBorderLimit && diagramPanel != null) {
			int xLimit = getDiagramPanel().getWidth()-borderLimit;
			int yLimit = getDiagramPanel().getHeight()-borderLimit;
			if(newX < borderLimit) {newX = borderLimit;}
			if(newY < borderLimit) {newY = borderLimit;}
			if(newX > xLimit) {newX = xLimit;}
			if(newY > yLimit) {newY = yLimit;}
		}
		
		Point2D.Double ret = new Point2D.Double(newX,newY);
		return(ret);
	}
	

	
	public double[] getEdgeRepulsion(DrawCoord nb) {
		
		Node node = nb.getNode();
		DualGraph graph = getDualGraph();

		double xEdgeRepulsive = 0.0;
		double yEdgeRepulsive = 0.0;
	
		ArrayList<FaceEdge> usedFaceEdges = new ArrayList<FaceEdge>();
		double[] eRep = new double[2];
		eRep[0] = 0;
		eRep[1] = 0;
		for(int i = 0; i < faces.size(); i++){
			
			Face face = faces.get(i);
			Node nodeInFace = face.getFaceEdgeList().get(0).getFrom();
			if(face.hasNode(node) || !graph.nodesConnected(node,nodeInFace)) {
				// test node against faces that the node is in or
				// faces that the node has no connection to
				for(FaceEdge fe : face.getFaceEdgeList()){
					// dont recalculate force against edges previously used
					if(faceEdgeHasBeenUsed(usedFaceEdges,fe)) {
						continue;
					} else {
						usedFaceEdges.add(fe);
					}
					
					// dont calculate force against edges directly connecting to the node
					Node n1 = fe.getFrom();
					Node n2 =  fe.getTo();
					if((n1 != node && n2 != node)){	
						
						DrawCoord nb1 = nodeBuffer.getBufferedNode(n1);
						DrawCoord nb2 = nodeBuffer.getBufferedNode(n2);

						double angle1 = pjr.graph.Util.angle(nb.getOldCentre(), nb1.getOldCentre(), nb2.getOldCentre());
						double angle2 = pjr.graph.Util.angle(nb1.getOldCentre(), nb2.getOldCentre(), nb.getOldCentre());
									
						if(angle1 >= Math.PI/2 || angle2 >= Math.PI/2){
						}
						else{
							// calculate the repulsion on the node
							Point2D.Double p = nb.getOldCentre();
							Point2D.Double linePoint = pjr.graph.Util.perpendicularPoint(nb.getOldCentre(),nb1.getOldCentre(),nb2.getOldCentre());
							double xDistance = p.x - linePoint.x;
							double yDistance = p.y - linePoint.y;
							
							double distance = pjr.graph.Util.distance(new Point2D.Double(p.x,p.y),linePoint);

							double absXDistance = Math.abs(xDistance);
							double absYDistance = Math.abs(yDistance);

							double xForceShare = absXDistance/(absXDistance+absYDistance);
							double yForceShare = absYDistance/(absXDistance+absYDistance);

							double edgeRepulsiveForce = q / (distance * distance);
							if(xDistance > 0) {
								xEdgeRepulsive += edgeRepulsiveForce*xForceShare;
							} else {
								if(xDistance < 0) {
									xEdgeRepulsive -= edgeRepulsiveForce*xForceShare;
								}
							}
							if(yDistance > 0) {
								yEdgeRepulsive += edgeRepulsiveForce*yForceShare;
							} else {
								if(yDistance < 0) {
									yEdgeRepulsive -= edgeRepulsiveForce*yForceShare;
								}
							}
						}					
					}
				}
			}
		}	
		
		eRep[0] = xEdgeRepulsive;
		eRep[1] = yEdgeRepulsive;
		
		return eRep;
	}
	/**
	 * Checks if the two nodes at the ends of the fe are in
	 * a FaceEdge in the ArrayList.
	 */
	protected boolean faceEdgeHasBeenUsed(ArrayList<FaceEdge> usedFaceEdges, FaceEdge fe) {
		Node from = fe.getFrom();
		Node to = fe.getTo();
		for(FaceEdge nextFE: usedFaceEdges) {
			Node nextFrom = nextFE.getFrom();
			Node nextTo = nextFE.getTo();
			if(from == nextFrom && to == nextTo) {
				return true;
			}
			if(from == nextTo && to == nextFrom) {
				return true;
			}
		}
		return false;
	}

}
