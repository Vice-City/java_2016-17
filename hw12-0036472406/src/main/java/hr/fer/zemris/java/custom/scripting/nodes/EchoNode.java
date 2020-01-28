package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Predstavlja čvor grafa koji sadrži informacije o <code>Echo</code> tag-u.
 * 
 * @author Vice Ivušić
 *
 */
public class EchoNode extends Node {

	/** polje elemenata od kojih se sastoji čvor **/
	private Element[] elements;
	
	/**
	 * Stvara <code>EchoNode</code> s navedenim <code>Element</code> elementima.
	 * 
	 * @param elements elementi čvora
	 * @throws NullPointerException ako je predan null za elements
	 */
	public EchoNode(Element[] elements) {
		if (elements == null) {
			throw new NullPointerException("elements must not be null!");
		}
		this.elements = elements;
	}
	
	/**
	 * Vraća polje <code>Element</code> elemenata ovog čvora.
	 * 
	 * @return elementi ovog čvora
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visit(this);
	}

}
