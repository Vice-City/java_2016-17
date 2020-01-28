package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Predstavlja tokenizator teksta. Lexer prolazi kroz tekst
 * i generira niz tokena koje pozivatelj može dohvatiti. Može
 * tokenizirati u stanju teksta ili u stanju tag-a. U stanju
 * teksta generira samo tekstualne tokene, dok u stanju tag-a
 * generira razne tokene ovisno o semantici tag-a. Početno
 * stanje tokenizatora je stanje teksta.
 * 
 * <p>Tijekom tokeniziranja normalnog teksta, prihvaća \\ te \{ za escape-anje.
 * Tijekom tokeniziranja String-a unutar taga, prihvaća \\ te \" za escape-anje.
 * 
 * <p>Nudi metode za generiranje sljedećeg tokena, za dohvat zadnje
 * generiranog tokena, za postavljanje stanja te za dohvat
 * broja retka na kojemu se generirao zadnji token.
 * 
 * @author Vice Ivušić
 *
 */
public class Lexer {

	/** polje koje sadrži sve znakove od kojih se sastoji dokument koji se tokenizira **/
	private char[] data;
	/** zadnje generirani token **/
	private Token token;
	/** indeks zadnjeg neobrađenog znaka **/
	private int currentIndex;
	/** stanje u kojemu se trenutno nalazi tokenizator **/
	private LexerState state;
	/** broj retka na kojemu se generirao zadnji token **/
	private int line;
	
	/**
	 * Stvara <code>Lexer</code> tokenizator iz navedenog <code>String</code> teksta.
	 * 
	 * @param text tekst koji se tokenizira
	 * @throws IllegalArgumentException ako je predan null za tekst
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Cannot create Lexer with text set to null!");
		}
		data = text.toCharArray();
		state = LexerState.TEXT;
		line = 1;
	}
	
	/**
	 * Generira i vraća sljedeći token.
	 * 
	 * @return generirani token
	 * @throws LexerException ako je došlo do pogreške tijekom tokenizacije
	 */
	public Token nextToken() {
		extractNextToken();
		return token;
	}
	
	/**
	 * Dohvaća broj retka na kojemu se generirao zadnji token.
	 * 
	 * @return broj retka
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Vraća zadnje generirani token. <b>Ne</b> stvara novi token!
	 * 
	 * @return zadnje generirani token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Postavlja stanje tokenizatora u navedeno <code>LexerState</code> stanje.
	 * 
	 * @param state stanje tokenizatora
	 * @throws IllegalArgumentException ako je predan null za stanje
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new IllegalArgumentException("Cannot set Lexer's state to null!");
		}
		this.state = state;
	}
	
	/**
	 * Pomoćna metoda koja generira i sprema sljedeći token.
	 */
	private void extractNextToken() {
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Lexer has reached EOF; nothing more to read!");
		}
		
		if (state == LexerState.TEXT) {
			tokenizeText();
		} else {
			tokenizeTag();
		}
	}
	
	/**
	 * Pomoćna metoda koja tokenizira čisti tekst.
	 */
	private void tokenizeText() {
		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '{') {
			if (currentIndex == data.length-1) {
				throw new LexerException(
						addInfo("'{' must be followed by '$ to open tag!")
				);
			} 
			
			currentIndex++;
			c = data[currentIndex];
			if (c != '$') {
				throw new LexerException(
						addInfo("'{' must be followed by '$' to open tag!")
				);
			}
			
			token = new Token(TokenType.TAG_START, null);
			currentIndex++;
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		boolean readBackslash = false;
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
			
			if (c == '{') {
				if (readBackslash) {
					sb.append(c);
					readBackslash = false;
					currentIndex++;
					continue;
				}
				break;
			}
			
			if (readBackslash) {
				throw new LexerException(
						addInfo("'\\' must be followed either by '\\' or '{' inside text!")
				);
			}
			
			if (c == '\n') {
				line++;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		if (readBackslash) {
			throw new LexerException(
					addInfo("'\\' must be followed either by '\\' or '{' inside text!")
			);
		}
		
		String textToken = sb.toString();
		token = new Token(TokenType.TEXT, textToken);
	}
	
	/**
	 * Pomoćna metoda koja tokenizira sadržaj unutar tag-a.
	 */
	private void tokenizeTag() {
		skipWhitespace();

		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '$') {
			if (currentIndex == data.length-1) {
				throw new LexerException(
						addInfo("Tag must be closed with '$}', not just '$'!")
				);
			}
			
			currentIndex++;
			c = data[currentIndex];
			if (c != '}') {
				throw new LexerException(
						addInfo("Tag must be closed with '$}'!")
				);
			}
			
			token = new Token(TokenType.TAG_END, null);
			currentIndex++;
			return;
		}
		
		if (c == '"') {
			currentIndex++;
			tokenizeTagString();
			return;
		}
		
		if (c == '@') {
			currentIndex++;
			String functionNameToken = getTagWord();
			
			if (functionNameToken.equals("")) {
				throw new LexerException(
						addInfo("Function name must not be empty!")
				);
			}
			
			token = new Token(TokenType.FUNCTION, functionNameToken);
			return;
		}
		
		/*
		 * Provjerava je li '-' dio broja ili nije. Ako nije, tumači se kao operator.
		 */
		if (c == '-') {
			if (currentIndex == data.length-1 || !Character.isDigit(data[currentIndex+1])) {
				token = new Token(TokenType.OPERATOR, "-");
				currentIndex++;
				return;
			}
		}
		
		if (c=='+' || c=='*' || c=='/' || c=='^') {
			token = new Token(TokenType.OPERATOR, Character.toString(c));
			currentIndex++;
			return;
		}
		
		if (Character.isDigit(c) || c == '-') {
			tokenizeTagNumber();
			return;
		}
		
		if (c == '=') {
			if (token.getType() != TokenType.TAG_START) {
				throw new LexerException(
						addInfo("'=' can only be a tag name!")
				);
			}
			token = new Token(TokenType.TAG_NAME, "=");
			currentIndex++;
			return;
		}
		
		if (Character.isLetter(c)) {
			if (token.getType() == TokenType.TAG_START) {
				String tagNameLowercase = getTagWord().toLowerCase();
				
				token = new Token(TokenType.TAG_NAME, tagNameLowercase);
				return;
			}
			
			String variableNameToken = getTagWord();
			token = new Token(TokenType.VARIABLE, variableNameToken);
			return;
		}
		
		throw new LexerException(
				addInfo("tag contains illegal character: '" + c + "'")
		);
	}
	
	/**
	 * Pomoćna metoda koja tokenizira <code>String</code> unutar tag-a.
	 */
	private void tokenizeTagString() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		boolean readBackslash = false;
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
				currentIndex++;
				break;
			}
			
			if (readBackslash) {
				switch (c) {
				case 'n':
					sb.append('\n');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'r':
					sb.append('\r');
					break;
				default:
					throw new LexerException(
							addInfo("'\\' must be followed either by '\\' or '\"' inside string!")
							);
				}
				
				readBackslash = false;
				currentIndex++;
				continue;
			}
		
			sb.append(c);
			currentIndex++;
		}
		
		if (readBackslash) {
			throw new LexerException(
					addInfo("'\\' must be followed either by '\\' or '\"' inside string!")
			);
		}
		
		token = new Token(TokenType.STRING_VALUE, sb.toString());
	}

	/**
	 * Pomoćna metoda koja tokenizira slijed brojeva unutar tag-a.
	 */
	private void tokenizeTagNumber() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		boolean readDot = false;
		int startIndex = currentIndex;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (currentIndex == startIndex) {
				if (c == '-') {
					sb.append(c);
					currentIndex++;
					continue;
				}
			}
			
			if (!Character.isDigit(c) && c != '.') {
				break;
			}
			
			if (c == '.') {
				if (readDot) {
					break;
				}
				else {
					readDot = true;
				}
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		String tagNumberToken = sb.toString();
		
		if (readDot) {
			double doubleValue;
			try {
				doubleValue = Double.parseDouble(tagNumberToken);
			} catch (NumberFormatException ex) {
				throw new LexerException(
						addInfo(tagNumberToken + " is not a parsable double!")
				);
			}
			
			token = new Token(TokenType.DOUBLE_VALUE, doubleValue);
			
		} else {
			int intValue;
			try {
				intValue = Integer.parseInt(tagNumberToken);
			} catch (NumberFormatException ex) {
				throw new LexerException(
						addInfo(tagNumberToken + " is not a parsable integer!")
				);
			}
			
			token = new Token(TokenType.INT_VALUE, intValue);
		}
	}
	
	/**
	 * Pomoćna metoda koja dohvaća sljedeću cijelu riječ unutar tag-a.
	 * Riječ ne smije započinjati s podvlakom ili s brojem.
	 * 
	 * @return cijela riječ unutar tag-a
	 */
	private String getTagWord() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		int startIndex = currentIndex;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (startIndex == currentIndex) {
				if (Character.isDigit(c) || c == '_') {
					break;
				}
			}
			
			if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
				break;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		String wordToken = sb.toString();
		
		return wordToken;		
	}
	
	/**
	 * Pomoćna metoda koja preskače praznine unutar teksta.
	 */
	private void skipWhitespace() {
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (c == '\r' || c == '\n' || c == '\t' || c == ' ') {
				if (c == '\n') {
					line++;
				}
				currentIndex++;
			} else {
				break;
			}
		}
	}
	
	/**
	 * Pomoćna metoda koja dodaje predanom Stringu informaciju
	 * o broju retka teksta na kojemu je zadnji token napravljen.
	 * 
	 * @param message poruka
	 * @return poruka s dodanim brojem retka
	 */
	private String addInfo(String message) {
		return String.format("Line %d: %s", line, message);
	}
	
}