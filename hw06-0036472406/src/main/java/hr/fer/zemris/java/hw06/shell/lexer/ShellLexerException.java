package hr.fer.zemris.java.hw06.shell.lexer;

/**
 * Thrown when an error occurs during tokenization.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellLexerException extends RuntimeException {
	
	/** default serial version UID **/
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a ShellLexerException.
	 */
	public ShellLexerException() {
		super();
	}
	
	/**
	 * Creates a ShellLexerException with the specified message.
	 * 
	 * @param message message with error details
	 */
	public ShellLexerException(String message) {
		super(message);
	}
}