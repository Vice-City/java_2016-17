package hr.fer.zemris.java.hw03.prob1;

/**
 * Predstavlja jedan token ulaznog teksta.
 * 
 * @author Vice Ivušić
 *
 */
public class Token {

	/** vrsta tokena **/
	TokenType type;
	/** vrijednost tokena **/
	Object value;
	
	/**
	 * Stvara <code>Token</code> navedene <code>TokenType</code> vrste
	 * i navedene <code>Object</code> vrijednosti.
	 * 
	 * @param type vrsta tokena
	 * @param value vrijednost tokena
	 * @throws NullPointerException ako je predan null za vrijednost
	 */
	public Token(TokenType type, Object value) {
		if (type == null) throw new NullPointerException("TokenType ne smije biti null!");
		
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Dohvaća <code>Object</code> vrijednost tokena.
	 * 
	 * @return vrijednost tokena
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Dohvaća <code>TokenType</code> vrstu tokena.
	 * 
	 * @return vrsta tokena
	 */
	public TokenType getType() {
		return type;
	}
}
