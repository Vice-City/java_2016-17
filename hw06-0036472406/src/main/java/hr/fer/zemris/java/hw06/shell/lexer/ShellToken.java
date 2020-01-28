package hr.fer.zemris.java.hw06.shell.lexer;

/**
 * Represents a single token of the input text.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellToken {
	
	/** token type **/
	private ShellTokenType type;
	/** value of the token **/
	private Object value;
	
	/**
	 * Creates a ShellToken of the specified TokenType and the
	 * specified value.
	 * 
	 * @param type token type
	 * @param value token value
	 * @throws IllegalArgumentException if the specified type is null
	 */
	public ShellToken(ShellTokenType type, Object value) {
		if (type == null) throw new IllegalArgumentException("type cannot be null!");
		
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Retruns the token's value.
	 * 
	 * @return token value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the token's type.
	 * 
	 * @return token type
	 */
	public ShellTokenType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s]", type, value);
	}
}