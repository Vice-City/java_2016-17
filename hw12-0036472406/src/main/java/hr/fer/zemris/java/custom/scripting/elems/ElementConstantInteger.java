package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Predstavlja jedan cijeli broj.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementConstantInteger extends Element {

	/** cjelobrojna vrijednost ovog elementa **/
	private int value;
	
	/**
	 * Stvara <code>ElementConstantInteger</code> navedene <code>int</code> vrijednosti.
	 * 
	 * @param value vrijednost ovog elementa
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ElementConstantInteger)) return false;
		
		ElementConstantInteger other = (ElementConstantInteger) obj;
		
		return value == other.value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}

}
