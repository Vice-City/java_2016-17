package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Baca se kada dođe do pogreške tijekom sintaksne analize dokumenta.
 * 
 * @author Vice Ivušić
 *
 */
public class SmartScriptParserException extends RuntimeException {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Stvara <code>SmartScriptParserException</code>.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Stvara <code>SmartScriptParserException</code> s navedenom porukom.
	 * 
	 * @param message poruka s detaljima o pogrešci
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
}
