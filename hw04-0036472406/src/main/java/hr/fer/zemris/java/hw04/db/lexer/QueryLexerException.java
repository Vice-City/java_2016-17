package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Thrown when an error occurs during tokenization.
 * 
 * @author Vice Ivušić
 *
 */
public class QueryLexerException extends RuntimeException {

	/** default serial version UID **/
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a QueryLexerException.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Creates a QueryLexerException with the specified message.
	 * 
	 * @param message message with error details
	 */
	public QueryLexerException(String message) {
		super(message);
	}
}