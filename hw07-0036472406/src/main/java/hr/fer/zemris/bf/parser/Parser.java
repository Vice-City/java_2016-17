package hr.fer.zemris.bf.parser;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Represents a boolean expression parser. This parser uses a lexer
 * which tokenizes a piece of text representing a boolean expression
 * so the parser can determine whether the received tokens are generated
 * in a well defined manner. If they are, it builds a tree of boolean
 * expressions that the text is composed of.
 * 
 * <p>Offers a method for retrieving the top-level boolean expression node
 * of the parsed text.
 * 
 * <p>Valid boolean expressions can be composed of the logical operators
 * {@code and} ({@code *}), {@code or} ({@code +}), {@code xor} ({@code :+:}) 
 * and {@code not} ({@code !}). Expressions may also be grouped
 * by parentheses to override operator precedence. Each expression must
 * contain at least one boolean variable or constant ({@code false}/{@code true} 
 * i.e. {@code 0}/{@code 1}). Operators, variables and constants are all case 
 * insensitive.
 * 
 * <p>Examples of legitimate boolean expressions:
 * <ul>
 *    <li>{@code true}
 *    <li>{@code 0}
 *    <li>{@code a and b}
 *    <li>{@code a or b and c or not d}
 *    <li>{@code (a + !b) * !(c :+: !!d)}
 * </ul>
 * 
 * @author Vice Ivušić
 *
 */
public class Parser {

	/** top-level boolean node which may contain nodes of its own */
	private Node topNode;
	/** lexer used for tokenizing the boolean expression */
	private Lexer lexer;
	
	/**
	 * Creates a new Parser which generates a top-level boolean expression
	 * node, which may contain nodes of its own, from the specified test.
	 * 
	 * @param text text representing a boolean expression
	 * @throws IllegalArgumentException if the specified text is null
	 * @throws ParserException if an error occurs during parsing
	 */
	public Parser(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Argument text cannot be null!");
		}
		
		lexer = new Lexer(text);
		
		topNode = parse();
	}

	/**
	 * Returns the generated top-level boolean expression node as a
	 * {@link Node} object.
	 * 
	 * @return generated top-level boolean expression node
	 */
	public Node getExpression() {
		return topNode;
	}
	
	/**
	 * Helper method which parses the boolean expression and returns
	 * its top-level expression node.
	 * 
	 * @return top-level boolean expression node
	 */
	private Node parse() {
		Node node = null;
		
		getNextToken();
		while (!tokenIsType(TokenType.EOF)) {
			node = parseOr();
		}
		
		return node;
	}
	
	/**
	 * Helper method which parses and returns any potential
	 * boolean expressions representing OR nodes.
	 * 
	 * @return boolean expression node
	 */
	private Node parseOr() {
		List<Node> children = new LinkedList<>();
		
		while (true) {
			children.add(parseXor());
			
			checkPostExpression();
			
			if (tokenIsType(TokenType.OPERATOR) && "or".equals(getTokenValue())) {
				getNextToken();
				continue;
			}
			
			break;
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		return new BinaryOperatorNode(
				"or",
				children,
				Boolean::logicalOr
		);
	}
	
	/**
	 * Helper method which parses and returns any potential
	 * boolean expressions representing XOR nodes.
	 * 
	 * @return boolean expression node
	 */
	private Node parseXor() {
		List<Node> children = new LinkedList<>();
		
		while (true) {
			children.add(parseAnd());
			
			checkPostExpression();
			
			if (tokenIsType(TokenType.OPERATOR) && "xor".equals(getTokenValue())) {
				getNextToken();
				continue;
			}
			
			break;
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		return new BinaryOperatorNode(
				"xor",
				children,
				Boolean::logicalXor
		);
	}
	
	/**
	 * Helper method which parses and returns any potential
	 * boolean expressions representing AND nodes.
	 * 
	 * @return boolean expression node
	 */
	private Node parseAnd() {
		List<Node> children = new LinkedList<>();
		
		while (true) {
			children.add(parseNot());

			checkPostExpression();
			
			if (tokenIsType(TokenType.OPERATOR) && "and".equals(getTokenValue())) {
				getNextToken();
				continue;
			}
			
			break;
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		return new BinaryOperatorNode(
				"and",
				children,
				Boolean::logicalAnd
		);
	}

	/**
	 * Helper method which parses and returns any potential
	 * boolean expressions representing NOT nodes.
	 * 
	 * @return boolean expression node
	 */
	private Node parseNot() {
		Node child = null;
		
		if (tokenIsType(TokenType.OPERATOR) && "not".equals(getTokenValue())) {
			getNextToken();
			
			child = parseNot();
			
			return new UnaryOperatorNode(
					"not", 
					child, 
					bool -> !bool
			);
		}
		
		child = parseExpression();
		
		return child;
	}

	/**
	 * Helper method which parses and returns the next
	 * variable or constant node, or a bracketed expression node.
	 * 
	 * @return variable or constant node, or a bracketed expression node
	 */
	private Node parseExpression() {
		if (tokenIsType(TokenType.OPEN_BRACKET)) {
			getNextToken();
			
			Node node = parseOr();
			
			if (!tokenIsType(TokenType.CLOSED_BRACKET)) {
				throw new ParserException(
						addInfo("Expected ')' but found "+lexer.getToken().getTokenType()+".")
				);
			}
			
			getNextToken();
			return node;
		}
		
		if (tokenIsType(TokenType.CONSTANT)) {
			boolean value = (boolean) getTokenValue();
			getNextToken();
			return new ConstantNode(value);
		}
		
		if (tokenIsType(TokenType.VARIABLE)) {
			String name = (String) getTokenValue();
			getNextToken();
			return new VariableNode(name);
		}
		
		throw new ParserException(
				addInfo("Unexpected token found: "+lexer.getToken())
		);
	}

	/**
	 * Helper method which checks whether the current token is a valid
	 * token in case it is generated right after a stand-alone expression
	 * node, and throws an exception if that is not the case.
	 * 
	 * @throws ParserException if the current token is not a valid token
	 */
	private void checkPostExpression() {
		if (tokenIsType(TokenType.OPERATOR) && "not".equals(getTokenValue()) ||
			!tokenIsType(TokenType.OPERATOR) && 
			!tokenIsType(TokenType.CLOSED_BRACKET) && 
			!tokenIsType(TokenType.EOF)) {
			
			throw new ParserException(
					addInfo("Unexpected token: "+lexer.getToken())
			);
		}
	}

	/**
	 * Helper method which generates the next token and wraps
	 * any LexerException that might have occured during
	 * tokenization into a ParserException.
	 * 
	 * @return the token generated by the lexer
	 */
	private Token getNextToken() {
		try {
			return lexer.nextToken();
		} catch (LexerException ex) {
			throw new ParserException("Lexer has thrown exception: "+ex.getMessage());
		}
	}

	/**
	 * Helper method which returns the value of the current token.
	 * 
	 * @return value of the current token
	 */
	private Object getTokenValue() {
		return lexer.getToken().getTokenValue();
	}

	/**
	 * Helper method which checks if the currently generated token
	 * is of the specified type.
	 * 
	 * @param type the TokenType being checked
	 * @return true iff the current token is of the specified type
	 */
	private boolean tokenIsType(TokenType type) {
		return lexer.getToken().getTokenType() == type;
	}
	
	/**
	 * Helper method which wraps the specified string with information
	 * about the last unprocessed character's index.
	 * 
	 * @param message informative message
	 * @return message with information about the last unprocessed character's index
	 */
	private String addInfo(String message) {
		return String.format("Near character index %d: %s", lexer.getIndex(), message);
	}
}
