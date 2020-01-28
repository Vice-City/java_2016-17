package hr.fer.zemris.bf.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LexerTest {

	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");
		
		assertNotNull("Token was expected but null was returned.", lexer.nextToken());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInput() {
		new Lexer(null);
	}
	
	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		
		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getTokenType());
	}

	
	@Test(expected=LexerException.class)
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");

		lexer.nextToken();
		lexer.nextToken();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testGetTokenBeforeNextToken() {
		new Lexer("").getToken();
	}
	
	@Test(expected=LexerException.class)
	public void testSingleColon() {
		new Lexer(":   ").nextToken();
	}	
	
	@Test(expected=LexerException.class)
	public void testDoubleColon() {
		new Lexer("::   ").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testColonAndPlus() {
		new Lexer(":+   ").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testSingleColonWithoutWhitespace() {
		new Lexer(":").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testDoubleColonWithoutWhitespace() {
		new Lexer("::").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testColonAndPlusWithoutWhitespace() {
		new Lexer(":+").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidXor() {
		new Lexer(":*:").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidNumber() {
		new Lexer("2").nextToken();
	}	
	
	@Test(expected=LexerException.class)
	public void testInvalidNumberOneZero() {
		new Lexer("10").nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidCharacter() {
		Lexer lexer = new Lexer("a . b");

		lexer.nextToken();
		lexer.nextToken();
	}
	
	@Test
	public void testWordTokens() {
		Lexer lexer = new Lexer("And OR xOr not truE FALSe XORO");
		
		Token[] correctData = new Token[] {
				new Token(TokenType.OPERATOR, "and"),
				new Token(TokenType.OPERATOR, "or"),
				new Token(TokenType.OPERATOR, "xor"),
				new Token(TokenType.OPERATOR, "not"),
				new Token(TokenType.CONSTANT, true),
				new Token(TokenType.CONSTANT, false),
				new Token(TokenType.VARIABLE, "XORO"),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testSymbolTokens() {
		Lexer lexer = new Lexer("*+:+:!1(0)");
		
		Token[] correctData = new Token[] {
				new Token(TokenType.OPERATOR, "and"),
				new Token(TokenType.OPERATOR, "or"),
				new Token(TokenType.OPERATOR, "xor"),
				new Token(TokenType.OPERATOR, "not"),
				new Token(TokenType.CONSTANT, true),
				new Token(TokenType.OPEN_BRACKET, '('),
				new Token(TokenType.CONSTANT, false),
				new Token(TokenType.CLOSED_BRACKET, ')'),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testMixedInput() {
		Lexer lexer = new Lexer("not!a oR fAlse*b:+:(1+0)and true xor C");
		
		Token[] correctData = new Token[] {
				new Token(TokenType.OPERATOR, "not"),
				new Token(TokenType.OPERATOR, "not"),
				new Token(TokenType.VARIABLE, "A"),
				new Token(TokenType.OPERATOR, "or"),
				new Token(TokenType.CONSTANT, false),
				new Token(TokenType.OPERATOR, "and"),
				new Token(TokenType.VARIABLE, "B"),
				new Token(TokenType.OPERATOR, "xor"),
				new Token(TokenType.OPEN_BRACKET, '('),
				new Token(TokenType.CONSTANT, true),
				new Token(TokenType.OPERATOR, "or"),
				new Token(TokenType.CONSTANT, false),
				new Token(TokenType.CLOSED_BRACKET, ')'),
				new Token(TokenType.OPERATOR, "and"),
				new Token(TokenType.CONSTANT, true),
				new Token(TokenType.OPERATOR, "xor"),
				new Token(TokenType.VARIABLE, "C"),
				new Token(TokenType.EOF, null)
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getTokenType(), actual.getTokenType());
			assertEquals(msg, expected.getTokenValue(), actual.getTokenValue());
			counter++;
		}
	}
	
}