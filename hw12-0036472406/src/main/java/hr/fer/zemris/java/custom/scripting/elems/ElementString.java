package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Predstavlja jedan <code>String</code> element.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementString extends Element {

	/** <code>String</code> vrijednost ovog elementa **/
	private String value;
	
	/**
	 * Stvara <code>ElementString</code> s navedenom <code>String</code> vrijednosti.
	 * 
	 * @param value vrijednost ovog elementa
	 * @throws IllegalArgumentException ako je predana null vrijednost
	 */
	public ElementString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("value must not be null!");
		}
		this.value = value;
	}
	
	@Override
	public String asText() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ElementString)) return false;
		
		ElementString other = (ElementString) obj;
		
		return value.equals(other.value);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		/* 
		 * dodaje navodnike oko String-a i dodaje 
		 * backslash-ove gdje je potrebno 
		 */
		sb.append('"');
		for (char c : value.toCharArray()) {
			if (c == '\n') {
				sb.append("\\n");
				continue;
			}
			if (c == '\t') {
				sb.append("\\t");
				continue;
			}
			if (c == '\r') {
				sb.append("\\r");
				continue;
			}
			
			if (c == '"' || c == '\\') {
				sb.append('\\');
			}
			
			sb.append(c);
		}
		sb.append('"');
		
		return sb.toString();
	}
}
