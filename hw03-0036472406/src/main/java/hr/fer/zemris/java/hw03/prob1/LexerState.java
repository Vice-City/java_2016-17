package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeracija stanja u kojima se tokenizator može nalaziti.
 * 
 * @author Vice Ivušić
 *
 */
public enum LexerState {
	
	/** Stanje u kojemu se tekst interpretira kao riječ, simbol ili broj. **/
	BASIC,
	/** Stanje u kojemu se tekst interpretira samo kao skup riječi. **/
	EXTENDED
}
