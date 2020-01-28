package hr.fer.zemris.java.hw06.shell.parser;

/**
 * Thrown when an error occurs during parsing.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellParserException extends RuntimeException {
	
	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ShellLexerException.
	 */
	public ShellParserException() {
		super();
	}
	
	/**
	 * Creates a new ShellParserException with the specified message.
	 * 
	 * @param message message with the error details
	 */
	public ShellParserException(String message) {
		super(message);
	}
}