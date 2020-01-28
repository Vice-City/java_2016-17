package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Predstavlja čvor grafa.
 * Može se sastojati od drugih <code>Node</code> čvorova s kojima
 * gradi stablastu strukturu.
 * 
 * @author Vice Ivušić
 *
 */
public class Node {

	/** kolekcija drugih <code>Node</code> čvorova koji pripadaju ovom čvoru **/
	ArrayIndexedCollection children;
	
	/**
	 * Dodaje drugi <code>Node</code> čvor ovom elementu.
	 * @param child čvor koji se dodaje
	 * @throws IllegalArgumentException ako je predan null za drugi čvor
	 */
	public void addChildNode(Node child) {
		if (child == null) {
			throw new IllegalArgumentException("child must not be null!");
		}
		
		if (children == null) {
			children = new ArrayIndexedCollection();
		}
		
		children.add(child);
	}
	
	/**
	 * Vraća broj čvorova koji pripadaju ovom čvoru.
	 * 
	 * @return broj čvorova
	 */
	public int numberOfChildren() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}
	
	/**
	 * Vraća <code>Node</code> čvor s navedenim rednim brojem, tj. indeksom,
	 * koji pripada ovom čvoru.
	 * 
	 * @param index indeks traženog čvora
	 * @return traženi čvor
	 * @throws IndexOutOfBoundsException ako je indeks traženog čvora
	 * 			manji od nule ili veći od broja čvorova koji pripadaju ovog čvoru
	 */
	public Node getChild(int index) {
		int numberOfChildren = children == null ? 0 : numberOfChildren();
	
		if (index < 0 || index > numberOfChildren-1) {
			throw new IndexOutOfBoundsException(index + " is out of bounds!");
		}
		
		return (Node) children.get(index);
	}
	
	/**
	 * Vraća <code>String</code> reprezentaciju ovog čvora i njegove djece.
	 * 
	 * @return <code>String</code> reprezentacija čvora
	 */
	@Override
	public String toString() {
		return "";
	}
	
}
