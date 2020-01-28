package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Predstavlja jednu aritmetičku operaciju.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementOperator extends Element {

	/** simbol koji predstavlja ovu aritmetičku operaciju **/
	private String symbol;
	
	/**
	 * Stvara <code>ElementOperator</code> navedene aritmetičke operacije.
	 * 
	 * @param symbol simbol koji predstavlja ovu aritmetičku operaciju
	 * @throws IllegalArgumentException ako je predana null vrijednost
	 */
	public ElementOperator(String symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("symbol must not be null!");
		}
		this.symbol = symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ElementOperator)) return false;
		
		ElementOperator other = (ElementOperator) obj;
		
		return symbol.equals(other.symbol);
	}
	
	@Override
	public String toString() {
		return symbol;
	}
	
}
