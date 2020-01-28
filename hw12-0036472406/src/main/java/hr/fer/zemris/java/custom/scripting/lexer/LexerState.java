package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracija stanja u kojima se tokenizator može nalaziti.
 * 
 * @author Vice Ivušić
 *
 */
public enum LexerState {
	
	/** Stanje u kojemu je se tekst interpretira kao <code>TokenType.TEXT</code> **/
	TEXT,
	/** Stanje u kojemu se tekst tokenizira u razne <code>TokenType</code> tokene **/
	TAG
}
