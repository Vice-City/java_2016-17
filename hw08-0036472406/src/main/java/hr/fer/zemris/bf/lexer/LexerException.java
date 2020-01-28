package hr.fer.zemris.bf.lexer;

/**
 * Thrown when an error occurs during tokenization.
 * 
 * @author Vice Ivušić
 *
 */
public class LexerException extends RuntimeException {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new LexerException.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Creates a new LexerException with the specified message.
	 * 
	 * @param message message with error details
	 */
	public LexerException(String message) {
		super(message);
	}
}