package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Predstavlja jednu funkciju.
 * 
 * @author Vice Ivušić
 *
 */
public class ElementFunction extends Element {

	/** ime ove funkcije **/
	private String name;
	
	/**
	 * Stvara <code>ElementFunction</code> s navedenim imenom.
	 * 
	 * @param name ime ovog elementa
	 * @throws NullPointerException ako je predana null vrijednost
	 */
	public ElementFunction(String name) {
		if (name == null) {
			throw new NullPointerException("name must not be null!");
		}
		this.name = name;
	}
	
	@Override
	public String asText() {
		return "@" + name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) return false;
		if (!(obj instanceof ElementFunction)) return false;
		
		ElementFunction other = (ElementFunction) obj;
		
		return name.equals(other.name);
	}
}
