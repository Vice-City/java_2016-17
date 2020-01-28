package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.TokenType;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw04.db.lexer.Token;

public class QueryLexerTest {

	@Test
	public void testNotNull() {
		QueryLexer lexer = new QueryLexer("");
		
		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInput() {
		// must throw!
		new QueryLexer(null);
	}
	
	@Test
	public void testEmpty() {
		QueryLexer lexer = new QueryLexer("");
		
		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getType());
	}

	
	@Test(expected=QueryLexerException.class)
	public void testRadAfterEOF() {
		QueryLexer lexer = new QueryLexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}
	
	@Test
	public void testExpressions() {
		QueryLexer lexer = new QueryLexer(
				"    whatever=!\"Foo\\bar\"OR and AND ANDREA \"yesss\""
		);
		
		Token[] correctData = new Token[] {
				new Token(TokenType.ATTRIBUTE_NAME, "whatever"),
				new Token(TokenType.COMPARISON_OPERATOR, "="),
				new Token(TokenType.COMPARISON_OPERATOR, "!"),
				new Token(TokenType.STRING_VALUE, "Foo\\bar"),
				new Token(TokenType.ATTRIBUTE_NAME, "OR"),
				new Token(TokenType.LOGICAL_OPERATOR, "and"),
				new Token(TokenType.LOGICAL_OPERATOR, "and"),
				new Token(TokenType.ATTRIBUTE_NAME, "ANDREA"),
				new Token(TokenType.STRING_VALUE, "yesss")

		};
		
		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testEchoTag() {
		QueryLexer lexer = new QueryLexer(
				"jmbag>=\"0000000020\" and firstName!=\"Matija\" and lastName LIKE \"A*\""
		);
		
		Token correctData[] = new Token[] {
				new Token(TokenType.ATTRIBUTE_NAME, "jmbag"),
				new Token(TokenType.COMPARISON_OPERATOR, ">="),
				new Token(TokenType.STRING_VALUE, "0000000020"),
				new Token(TokenType.LOGICAL_OPERATOR, "and"),
				new Token(TokenType.ATTRIBUTE_NAME, "firstName"),
				new Token(TokenType.COMPARISON_OPERATOR, "!="),
				new Token(TokenType.STRING_VALUE, "Matija"),
				new Token(TokenType.LOGICAL_OPERATOR, "and"),
				new Token(TokenType.ATTRIBUTE_NAME, "lastName"),
				new Token(TokenType.COMPARISON_OPERATOR, "LIKE"),
				new Token(TokenType.STRING_VALUE, "A*"),
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test(expected=QueryLexerException.class)
	public void testUnclosedString() {
		QueryLexer lexer = new QueryLexer("firstName=\"Helloo");
		
		while (lexer.nextToken().getType() != TokenType.EOF);
	}
	
	private void checkTokenStream(QueryLexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			
				
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getType(), actual.getType());
			assertEquals(msg, expected.getValue(), actual.getValue());
			counter++;
		}
	}
	
	
	@SuppressWarnings("unused")
	private void checkToken(Token actual, Token expected) {
		String msg = "Tokens are not equal.";
		assertEquals(msg, expected.getType(), actual.getType());
		assertEquals(msg, expected.getValue(), actual.getValue());
	}
	

}
