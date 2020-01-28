package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeracije vrsta tokena od kojih se sastoji dokument.
 * 
 * @author Vice Ivušić
 *
 */
public enum TokenType {

	/** označava da više nema tokena za dohvat **/
	EOF,
	/** bilo kakav tekst koji se nalazi izvan tag-a **/
	TEXT,
	/** početak tag-a **/
	TAG_START,
	/** završetak tag-a **/
	TAG_END,
	/** ime tag-a **/
	TAG_NAME,
	/** ime varijable **/
	VARIABLE,
	/** ime funkcije **/
	FUNCTION,
	/** aritmetički operator **/
	OPERATOR,
	/** cjelobrojna vrijednost **/
	INT_VALUE,
	/** decimalna vrijednost ograničene preciznosti **/
	DOUBLE_VALUE,
	/** <code>String</code> vrijednost **/
	STRING_VALUE
}
