package hr.fer.zemris.java.hw06.shell.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw06.shell.lexer.ShellLexerException;
import hr.fer.zemris.java.hw06.shell.lexer.ShellToken;
import hr.fer.zemris.java.hw06.shell.lexer.ShellTokenType;

/**
 * Represents a text parser. ShellParser uses a lexer which
 * tokenizes a piece of text so the parser can determine
 * whether the tokens received are generated in a well
 * defined manner. If they are, it builds a list of arguments
 * the text is composed of.
 * 
 * <p>Offers method for retrieving the list of arguments
 * built by the parser.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellParser {
	
	/** lexer used for tokenizing the query */
	private ShellLexer lexer;
	/** list of arguments built by the parser */
	private List<String> arguments;
	
	/**
	 * Creates a ShellParser which generates a list of conditional
	 * expressions from the specified query.
	 * 
	 * @param text text to be parsed
	 * @throws IllegalArgumentException if the specified query is null
	 * @throws ShellParserException if an error occurs during parsing
	 */
	public ShellParser(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Argument text cannot be null!");
		}
		
		lexer = new ShellLexer(text);
		arguments = new ArrayList<>();
		
		parse();
	}

	/**
	 * Returns a list of arguments built by this parser. This list
	 * may have zero entries in it if no arguments were parsed.
	 * 
	 * @return list of arguments built by this parser
	 */
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	/**
	 * Helper method which parses the given text.
	 * 
	 * @throws ShellParserException if an error occurs during parsing
	 */
	private void parse() {
		getNextToken();
		
		while (true) {
			if (isTokenOfType(ShellTokenType.EOL)) {
				break;
			}
			
			if (isTokenOfType(ShellTokenType.ARGUMENT)) {
				String argumentValue = lexer.getToken().getValue().toString();
				arguments.add(argumentValue); 
				
				getNextToken();
				continue;
			}
			
			throw new ShellParserException("Expected either EOL or argument!");
		}
		
	}
	
	/**
	 * Helper method which generates the next token and wraps
	 * any {@link ShellLexerException}s that might have occured during
	 * tokenization into a {@link ShellParserException}.
	 * 
	 * @return the token generated by the lexer
	 */
	private ShellToken getNextToken() {
		try {
			ShellToken token = lexer.nextToken();
			return token;
		} catch (ShellLexerException ex) {
			throw new ShellParserException(ex.getMessage());
		}
	}
	
	/**
	 * Helper method which checks if the currently generated token
	 * is of the specified type.
	 * 
	 * @param type the TokenType being checked
	 * @return true iff the current token is of the specified type
	 */
	private boolean isTokenOfType(ShellTokenType type) {
		return lexer.getToken().getType() == type;
	}
}