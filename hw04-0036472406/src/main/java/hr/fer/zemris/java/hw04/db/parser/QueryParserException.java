package hr.fer.zemris.java.hw04.db.parser;

/**
 * Thrown when an error occurs during parsing.
 * 
 * @author Vice Ivušić
 *
 */
public class QueryParserException extends RuntimeException {

	/** default serial version UID **/
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a QueryParserException.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Creates a QueryParserException with the specified message.
	 * 
	 * @param message message with the error details
	 */
	public QueryParserException(String message) {
		super(message);
	}
}