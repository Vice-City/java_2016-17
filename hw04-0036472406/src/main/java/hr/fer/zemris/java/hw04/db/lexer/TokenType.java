package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Enumerations of the type of tokens the text is composed of.
 * 
 * @author Vice Ivušić
 *
 */
public  enum TokenType {
	/** signals there are no more tokens to generate **/
	EOF,
	/** attribute name **/
	ATTRIBUTE_NAME,
	/** a comparison operator **/
	COMPARISON_OPERATOR,
	/** a string value **/
	STRING_VALUE,
	/** a logical operator **/
	LOGICAL_OPERATOR
}

