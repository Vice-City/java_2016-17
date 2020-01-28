package hr.fer.zemris.bf.lexer;

/**
 * Represents a single token of the input text.
 * 
 * @author Vice Ivušić
 *
 */
public class Token {
	
	/** token type */
	private TokenType type;
	/** value of the token */
	private Object value;
	
	/**
	 * Creates a Token of the specified {@link TokenType} and the
	 * specified value.
	 * 
	 * @param type token type
	 * @param value token value
	 * @throws IllegalArgumentException if the specified type is null
	 */
	public Token(TokenType type, Object value) {
		if (type == null) {
			throw new IllegalArgumentException("Argument type cannot be null!");
		}
		
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the token's type.
	 * 
	 * @return token type
	 */
	public TokenType getTokenType() {
		return type;
	}

	/**
	 * Retruns the token's value.
	 * 
	 * @return token value
	 */
	public Object getTokenValue() {
		return value;
	}
	
	@Override
	public String toString() {
		if (value == null) {
			return String.format(
					"Type: %s, Value: %s", 
					type, 
					value
			);
		}
		
		return String.format(
				"Type: %s, Value: %s, Value is instance of %s", 
				type, 
				value,
				value.getClass()
		);
	}
}