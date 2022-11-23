package euler;

import java.util.*;


/**
 * A diagram consisting of connected components, linking to
 * containing atomic diagrams.
 */
public class AtomicAbstractDiagram {
	
	/** The zones. */
	protected AbstractDiagram atomicDiagram = null;
	/** The atomic diagram containing this one. */
	protected AtomicAbstractDiagram parentDiagram = null;
	/** The zone of the parent atomic diagram containing this one. */
	protected String parentZone = null;
	protected ArrayList<AtomicAbstractDiagram> children = new ArrayList<AtomicAbstractDiagram>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		AbstractDiagram ad;
//		ad = new AbstractDiagram("0 a abcd abce");
//		ad = new AbstractDiagram("0 abc abcd abcej abcek abcdef abcdefg abcdefh abcdefhi");
//		ad = new AbstractDiagram("0 x y xy xyz abc abcd abcej abcek abcdef abcdefg abcdefh abcdefhi");
//		ad = new AbstractDiagram("0 a d x y xy xyz de df ab abc");
//		ad = new AbstractDiagram("0 a b ab ac abc");
//		ad = new AbstractDiagram("0 a b ab bc abc");
		ad = new AbstractDiagram("0 b c ac bc abc");
		System.out.println(ad.generateAtomicDiagrams().countAtomicDiagrams());
	}


	public AtomicAbstractDiagram(AbstractDiagram atomicDiagram, AtomicAbstractDiagram parentDiagram, String parentZone) {
		super();
		this.atomicDiagram = atomicDiagram;
		this.parentDiagram = parentDiagram;
		this.parentZone = parentZone;
		
		if(parentDiagram != null) {
			parentDiagram.getChildren().add(this);
		}
	}
	
	public AbstractDiagram getAtomicDiagram() {return atomicDiagram;}
	public AtomicAbstractDiagram getParentDiagram() {return parentDiagram;}
	public String getParentZone() {return parentZone;}
	public ArrayList<AtomicAbstractDiagram> getChildren() {return children;}

	public void setAtomicDiagram(AbstractDiagram atomicDiagram) {this.atomicDiagram = atomicDiagram;}
	public void setParentDiagram(AtomicAbstractDiagram parent) {this.parentDiagram = parent;}
	public void setParentZone(String parent) {this.parentZone = parent;}
	public void setChildren(ArrayList<AtomicAbstractDiagram> children) {this.children = children;}
	

	/** this must be called on the root AAD */
	public boolean consistentTree() {
		if(!getAtomicDiagram().toString().equals("0")) {return false;}
		if(getParentDiagram() != null) {return false;}
		if(getParentZone() != null) {return false;}
		
		for(AtomicAbstractDiagram aad : getChildren()) {
			if(!aad.consistent()) {
				return false;
			}
		}
		
		return true;
	}
	
	
	/** this can be called on any but the root AAD */
	protected boolean consistent() {
		
		if(getAtomicDiagram().toString() == null) {return false;}
		if(getParentZone() == null) {return false;}
		
		if(!getParentDiagram().getChildren().contains(this)) {return false;}
		for(AtomicAbstractDiagram aad : getChildren()) {
			if(!aad.consistent()) {return false;}
		}

		return true;
	}
	
	
	/** this must be called on the root AAD */
	public int countAtomicDiagrams() {
		
		int ret = 0;
		for(AtomicAbstractDiagram aad : getChildren()) {
			ret ++;
			ret += aad.countAtomicDiagrams();
		}
		
		return ret;
	}
	
	public String toString() {
		return atomicDiagram.toString();
	}

}
