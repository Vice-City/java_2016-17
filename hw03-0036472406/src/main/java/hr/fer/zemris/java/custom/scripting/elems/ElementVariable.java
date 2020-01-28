package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Predstavlja jednu varijablu.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementVariable extends Element {

	/** ime ove varijable **/
	private String name;
	
	/**
	 * Stvara <code>ElementVariable</code> s navedenom <code>String</code> vrijednosti.
	 * 
	 * @param name vrijednost ovog elementa
	 * @throws NullPointerException ako je predana null vrijednost
	 */
	public ElementVariable(String name) {
		if (name == null) {
			throw new NullPointerException("name must not be null!");
		}
		this.name = name;
	}
	
	@Override
	public String asText() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ElementVariable)) return false;
		
		ElementVariable other = (ElementVariable) obj;
		
		return name.equals(other.name);
	}
}
