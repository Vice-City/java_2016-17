package hr.fer.zemris.java.hw06.shell.lexer;

/**
 * Represents a text tokenizer. ShellLexer goes through text character
 * by character and generates a sequence of tokens which can be
 * retrieved through its API. The tokens generated are those
 * representing a string of characters, and an end of line indicator.
 * 
 * <p>Offers methods for generating the next token and for retrieving
 * the last generated token.
 * 
 * @author Vice Ivušić
 *
 */
public class ShellLexer {
	
	/** array containing all the characters the text is composed of **/
	private char[] data;
	/** last generated token **/
	private int currentIndex;
	/** index of the last unprocessed character **/
	private ShellToken token;
	/** flag indicating the last generated token was enclosed in double quotes */
	private boolean lastTokenWasQuoted = false;
	
	/**
	 * Creates a Lexer tokenizer from the specified text.
	 * 
	 * @param text text to be tokenized
	 * @throws IllegalArgumentException if the specified text is null
	 */
	public ShellLexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Argument text cannot be null!");
		}
		data = text.toCharArray();
	}
	
	/**
	 * Generates and returns the next token.
	 * 
	 * @return generated token
	 * @throws ShellLexerException if there was an error during tokenizing
	 */
	public ShellToken nextToken() {
		extractNextToken();
		return token;
	}
	
	/**
	 * Returns the last generated token. Does <b>not</b> generate a new token!
	 * 
	 * @return last generated token
	 */
	public ShellToken getToken() {
		return token;
	}
	
	/**
	 * Helper method which generates and stores the next token.
	 */
	private void extractNextToken() {
		if (token != null && token.getType() == ShellTokenType.EOL) {
			throw new ShellLexerException("Lexer has reached EOL; nothing more to read!");
		}
		
		if (lastTokenWasQuoted && currentIndex != data.length) {
			char c = data[currentIndex];
			
			if (!Character.isWhitespace(c)) {
				throw new ShellLexerException(
						"Arguments enclosed in quotes must be followed "
						+ "either by whitespace or nothing!"
				);
			}
		}
		
		skipWhitespace();
		
		if (currentIndex == data.length) {
			token = new ShellToken(ShellTokenType.EOL, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '"') {
			currentIndex++;
			tokenizeQuotedString();
			lastTokenWasQuoted = true;
			return;
		}
		
		tokenizeString();
		lastTokenWasQuoted = false;
		
	}
	
	/**
	 * Helper method which tokenizes a quoted string.
	 */
	private void tokenizeQuotedString() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		boolean readBackslash = false;
		boolean foundClosingQuotes = false;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (c == '\\') {
				if (readBackslash) {
					sb.append(c);
					readBackslash = false;
				} else {
					readBackslash = true;
				}
				currentIndex++;
				continue;
			}
			
			if (c == '"') {
				if (readBackslash) {
					sb.append(c);
					readBackslash = false;
					currentIndex++;
					continue;
				}
				
				foundClosingQuotes = true;
				currentIndex++;
				break;
			}
			
			if (readBackslash) {
				sb.append("\\" + c);
				readBackslash = false;
				currentIndex++;
				continue;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		if (!foundClosingQuotes) {
			throw new ShellLexerException("Argument wasn't closed with closing quotes!");
		}
		
		token = new ShellToken(ShellTokenType.ARGUMENT, sb.toString());
	}

	/**
	 * Helper method which tokenizes an unquoted string.
	 */
	private void tokenizeString() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (Character.isWhitespace(c)) {
				break;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		token = new ShellToken(ShellTokenType.ARGUMENT, sb.toString());
	}

	/**
	 * Helper method which skips whitespace in current data.
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