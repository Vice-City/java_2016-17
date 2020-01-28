package hr.fer.zemris.java.hw06.shell;

/**
 * Thrown when an error occurs during reading or writing to the
 * Shell's standard input or output.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellIOException extends RuntimeException {

	/** default serial version UID **/
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new ShellIOException.
	 */
	public ShellIOException() {
		super();
	}
	
	/**
	 * Constructs a new ShellIOException with the specified message.
	 * 
	 * @param message message with details of the error
	 */
	public ShellIOException(String message) {
		super(message);
	}

}
