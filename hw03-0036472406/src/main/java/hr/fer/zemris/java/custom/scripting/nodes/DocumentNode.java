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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0, n = numberOfChildren(); i < n; i++) {
			sb.append(getChild(i).toString());
		}
		
		return sb.toString();
	}
}
