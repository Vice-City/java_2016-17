package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Baca se kada dođe do pogreške tijekom leksičke analize teksta.
 * 
 * @author Vice Ivušić
 *
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Stvara <code>LexerException</code>.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Stvara <code>LexerException</code> s navedenom porukom.
	 * 
	 * @param message poruka s detaljima o pogrešci
	 */
	public LexerException(String message) {
		super(message);
	}
}
