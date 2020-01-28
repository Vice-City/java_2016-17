package hr.fer.zemris.java.hw03.prob1;

/**
 * Predstavlja tokenizator teksta. Lexer prolazi kroz tekst
 * i generira niz tokena koje pozivatelj može dohvatiti. Može
 * tokenizirati u stanju osnovnom ili proširenom stanju.
 * U osnovnom stanju generira tokene riječi, brojeva i simbola,
 * dok u proširenom stanju generira samo tokene riječi koje su
 * odvojene prazninama.
 * 
 * Tijekom tokeniziranja u osnovnom stanju, prihvaća \\ te \[broj] za escape-anje.
 * Tijekom tokeniziranja u proširenom stanju, ne prihvaća nikakav oblik escape-anja.
 * 
 * Nudi metode za generiranje sljedećeg tokena, za dohvat zadnje
 * generiranog tokena, te za postavljanje stanja.
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
	
	/**
	 * Stvara <code>Lexer</code> tokenizator iz navedenog <code>String</code> teksta.
	 * 
	 * @param text tekst koji se tokenizira
	 * @throws IllegalArgumentException ako je predan null za tekst
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Cannot make Lexer from null text!");
		}
		data = text.toCharArray();
		state = LexerState.BASIC;
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
	 * Vraća zadnje generirani token. *NE STVARA* novi token!
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
			throw new IllegalArgumentException("Cannot set Lexer state to null!");
		}
		this.state = state;
	}
	
	/**
	 * Pomoćna metoda koja generira i sprema sljedeći token.
	 */
	private void extractNextToken() {
		if (token != null && token.type == TokenType.EOF) {
			throw new LexerException("Reached EOF; can't read any more!");
		}
		
		if (state == LexerState.BASIC) {
			tokenizeBasic();
		} else {
			tokenizeExtended();
		}
	}
	
	/**
	 * Ponoćna metoda koja tokenizira tekst u osnovnom stanju.
	 */
	private void tokenizeBasic() {
		skipWhitespace();
		
		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '#') {
			token = new Token(TokenType.SYMBOL, c);
			currentIndex++;
			return;
		}
		
		if (Character.isDigit(c)) {
			tokenizeNumber();
			return;
		}

		if (Character.isLetter(c) || c == '\\') {
			tokenizeWord();
			return;
		}
		
		token = new Token(TokenType.SYMBOL, c);
		currentIndex++;
	}
	
	/**
	 * Pomoćna metoda koja tokenizira jednu cijeli riječ.
	 */
	private void tokenizeWord() {
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
			
			if (Character.isDigit(c)) {
				if (readBackslash) {
					sb.append(c);
					readBackslash = false;
					currentIndex++;
					continue;
				}
				break;
			}
			
			if (!Character.isLetter(c) && c != '\\') {
				break;
			}
			
			if (readBackslash) {
				throw new LexerException("'\\" + c + "' is not allowed!!");
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		if (readBackslash) {
			throw new LexerException("Word cannot end with '\\'!");
		}
		
		String wordToken = sb.toString();
		token = new Token(TokenType.WORD, wordToken);
	}

	/**
	 * Pomoćna metoda koja tokenizira jedan cijeli broj,
	 */
	private void tokenizeNumber() {
		StringBuilder sb = new StringBuilder();
		
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (!Character.isDigit(c)) break;
			
			sb.append(c);
			currentIndex++;
		}
		
		String numberToken = sb.toString();
		
		Long value;
		try {
			value = Long.parseLong(numberToken);
		} catch (NumberFormatException ex) {
			throw new LexerException(numberToken + " cannot be parsed into Long.");
		}
		
		token = new Token(TokenType.NUMBER, value);
	}

	/**
	 * Pomoćna metoda koja tokenizira tekst u proširenom stanju.
	 */
	private void tokenizeExtended() {
		skipWhitespace();
		
		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		char c = data[currentIndex];
		
		if (c == '#') {
			token = new Token(TokenType.SYMBOL, c);
			currentIndex++;
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (c == '#') {
				break;
			}
			
			if (c == '\r' || c == '\t' || c == '\n' || c == ' ') {
				break;
			}
			
			sb.append(c);
			currentIndex++;
		}
		
		String tokenExtended = sb.toString();
		token = new Token(TokenType.WORD, tokenExtended);
	}

	/**
	 * Pomoćna metoda koja preskače bjeline unutar teksta.
	 */
	private void skipWhitespace() {
		char c;
		while (currentIndex < data.length) {
			c = data[currentIndex];
			
			if (c == '\r' || c == '\n' || c == '\t' || c == ' ') {
				currentIndex++;
			} else {
				break;
			}
		}
	}
}
