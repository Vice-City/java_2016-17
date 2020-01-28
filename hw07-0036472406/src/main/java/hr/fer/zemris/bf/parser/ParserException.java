package hr.fer.zemris.bf.parser;

/**
 * Thrown when an error occurs during parsing.
 * 
 * @author Vice Ivušić
 *
 */
public class ParserException extends RuntimeException {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a QueryParserException.
	 */
	public ParserException() {
		super();
	}
	
	/**
	 * Creates a QueryParserException with the specified message.
	 * 
	 * @param message message with the error details
	 */
	public ParserException(String message) {
		super(message);
	}
}