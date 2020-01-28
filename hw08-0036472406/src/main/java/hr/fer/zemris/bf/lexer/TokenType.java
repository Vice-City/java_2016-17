package hr.fer.zemris.bf.lexer;

/**
 * Enumerations of the type of tokens the text is composed of.
 * 
 * @author Vice Ivušić
 *
 */
public  enum TokenType {

	/** indicates there are no more tokens to retrieve */
	EOF,
	/** represents a boolean variable */
	VARIABLE,
	/** represents a boolean constant */
	CONSTANT,
	/** represents a boolean operator */
	OPERATOR,
	/** represents an open bracket */
	OPEN_BRACKET,
	/** represents a closed bracket */
	CLOSED_BRACKET
	
}

