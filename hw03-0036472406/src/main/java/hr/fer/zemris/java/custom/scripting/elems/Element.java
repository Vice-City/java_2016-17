package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Predstavlja bilo kakav element s nekom semantikom.
 * 
 * @author Vice Ivušić
 *
 */
public class Element {

	/**
	 * Vraća <code>String</code> reprezentaciju ovog elementa.
	 * @return reprezentacija ovog elementa
	 */
	public String asText() {
		return "";
	}
	
	/**
	 * Vraća hash ovog elementa na temelju njegovih jedinstvenih karakteristika.
	 * 
	 * @return hash ovog elementa
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	/**
	 * Vraća <b>true</b> ako i samo ako je trenutni element
	 * jednak predanom elementu.
	 * 
	 * @return <b>true</b> ako i samo ako je element jednak predanom elementu
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Element)) return false;
		
		return true;
	}
}
