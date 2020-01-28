package hr.fer.zemris.java.custom.collections;

/**
 * Baca se kako bi se ukazalo na činjenicu da se nad praznim stogom
 * zove metoda koja zahtijeva da na stogu postoji barem jedan element.
 * 
 * @author Vice Ivušić
 *
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Stvara <code>EmptyStackException</code>.
	 */
	public EmptyStackException() {
		super();
	}
	
	/**
	 * Stvara <code>EmptyStackException</code> s navedenom porukom.
	 * 
	 * @param message poruka s detaljima o pogrešci
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
