package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Predstavlja čvor grafa koji sadrži dokument.
 * 
 * @author Vice Ivušić
 *
 */
public class DocumentNode extends Node {

	/**
	 * Stvara <code>DocumentNode</code>.
	 */
	public DocumentNode() {
		super();
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visit(this);
	}
}
