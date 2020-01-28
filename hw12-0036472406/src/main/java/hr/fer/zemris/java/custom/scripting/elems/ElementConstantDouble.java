package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Predstavlja jedan decimalni broj ograničene preciznosti.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementConstantDouble extends Element {

	/** decimalna vrijednost ovog elementa **/
	private double value;
	
	/**
	 * Stvara <code>ElementConstantDouble</code> element iz navedene
	 * <code>double</code> vrijednosti.
	 * 
	 * @param value vrijednost ovog elementa
	 */
	public ElementConstantDouble(double value) {
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
		if (!(obj instanceof ElementConstantDouble)) return false;
		
		ElementConstantDouble other = (ElementConstantDouble) obj;
		
		return Math.abs(value - other.value) < 10e-6 ? true : false;
	}
	
	@Override
	public String toString() {
		return Double.toString(value);
	}
}
