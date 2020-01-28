package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Represents a text tokenizer. Lexer goes through text character
 * by character and generates a sequence of tokens which can be
 * retrieved through its API. The tokens generated are those
 * representing attribute names, comparison operators, logical
 * operators and string values.
 * 
 * <p>Offers methods for generating the next token and for retrieving
 * the last generated token.
 * 
 * @author Vice Ivušić
 *
 */
public class QueryLexer {

	/** array containing all the characters the text is composed of **/
	private char[] data;
	/** last generated token **/
	private Token token;
	/** index of the last unprocessed character **/
	private int currentIndex;
	
	/**
	 * Creates a Lexer tokenizer from the specified text.
	 * 
	 * @param text text to be tokenized
	 * @throws IllegalArgumentException if the specified text is null
	 */
	public QueryLexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Cannot create Lexer with text set to null!");
		}
		data = text.toCharArray();
	}
	
	/**
	 * Generates and returns the next token.
	 * 
	 * @return generated token
	 * @throws QueryLexerException if there was an error during tokenizing
	 */
	public Token nextToken() {
		extractNextToken();
		return token;
	}
	
	/**
	 * Returns the last generated token. Does <b>not</b> generate a new token!
	 * 
	 * @return last generated token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Helper method which generates and stores the next token.
	 */
	private void extractNextToken() {
		if (token != null && token.type == TokenType.EOF) {
			throw new QueryLexerException("Lexer has reached EOF; nothing more to read!");
		}
		
		skipWhitespace();
		
		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '<' || c == '=' || c == '>' || c == '!') {
			if (currentIndex < data.length-1) {
				char c2 = data[currentIndex+1];
				
				if (c2 == '<' || c2 == '=' || c2 == '>') {
					token = new Token(
							TokenType.COMPARISON_OPERATOR, 
							Character.toString(c) + Character.toString(c2)
					);
					currentIndex += 2;
					return;
				}
			}
			
			token = new Token(TokenType.COMPARISON_OPERATOR, Character.toString(c));
			currentIndex++;
			return;
		}
		
		if (c == '"') {
			currentIndex++;
			tokenizeString();
			return;
		}
		
		
		String word = getWord();
		
		if (word.toLowerCase().equals("and")) {
			token = new Token(TokenType.LOGICAL_OPERATOR, "and");
			return;
		}
		
		if (word.toUpperCase().equals("LIKE")) {
			token = new Token(TokenType.COMPARISON_OPERATOR, word);
			return;
		}
		
		token = new Token(TokenType.ATTRIBUTE_NAME, word);
	}
	
	/**
	 * Helper method which parses and returns the next whole word.
	 * 
	 * @return the next whole parsed word
	 */
	private String getWord() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (!Character.isLetter(c) && !Character.isDigit(c)) {
				break;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		return sb.toString();
	}

	/**
	 * Helper method which generates and stores the next String token.
	 */
	private void tokenizeString() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		boolean foundClosingQuotes = false;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (c == '"') {
				foundClosingQuotes = true;
				currentIndex++;
				break;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		if (!foundClosingQuotes) {
			throw new QueryLexerException("String was never closed with quotes!");
		}
		
		String stringToken = sb.toString();
		token = new Token(TokenType.STRING_VALUE, stringToken);
		return;
	}

	/**
	 * Helper method which skips over any whitespace in the text.
	 */
	private void skipWhitespace() {
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (Character.isWhitespace(c)) {
				currentIndex++;
			} else {
				break;
			}
		}
	}
}
