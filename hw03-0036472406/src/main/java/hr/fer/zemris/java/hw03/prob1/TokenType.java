package hr.fer.zemris.java.hw03.prob1;

/**
 * Enumeracije vrsta tokena od kojih se sastoji dokument.
 * 
 * @author Vice Ivušić
 *
 */
public enum TokenType {

	/** označava da više nema tokena za dohvat **/
	EOF,
	/** riječ **/
	WORD,
	/** cijeli broj **/
	NUMBER,
	/** simbol **/
	SYMBOL
}
