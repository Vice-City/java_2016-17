package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Predstavlja čvor grafa koji sadrži informacije o tekstu.
 * 
 * @author Vice Ivušić
 *
 */
public class TextNode extends Node {

	/** tekst ovog čvora **/
	private String text;
	
	/**
	 * Stvara <code>TextNode</code> s navedenim tekstom.
	 * 
	 * @param text tekst za ovaj čvor
	 * @throws NullPointerException ako je predani tekst null
	 */
	public TextNode(String text) {
		if (text == null) {
			throw new NullPointerException("text must not be null!");
		}
		this.text = text;
	}
	
	/**
	 * Dohvaća <code>String</code> tekst ovog elementa.
	 * 
	 * @return tekst ovog elementa
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (char c : text.toCharArray()) {
			if (c == '{' || c == '\\') {
				sb.append('\\');
			}
			sb.append(c);
		}
		
		return sb.toString();
	}
}
