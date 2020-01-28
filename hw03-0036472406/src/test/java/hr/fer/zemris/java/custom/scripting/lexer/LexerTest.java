package hr.fer.zemris.java.custom.scripting.lexer;

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
		// must throw!
		new Lexer(null);
	}
	
	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		
		assertEquals("Empty input must generate only EOF token.", TokenType.EOF, lexer.nextToken().getType());
	}

	
	@Test(expected=LexerException.class)
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		lexer.nextToken();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetStateNull() {
		Lexer lexer = new Lexer("kila kolača");
		
		lexer.setState(null);
	}

	@Test
	public void testText() {
		Lexer lexer = new Lexer(" \\\\Krav-!*?#ica je \\{FORA!}\n");
		Token expected = new Token(TokenType.TEXT, " \\Krav-!*?#ica je {FORA!}\n");
		
		checkToken(lexer.nextToken(), expected);
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTextBackslash() {
		Lexer lexer = new Lexer("Kravica. \\KRAVICA.");
		
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTextBackslashAtEnd() {
		Lexer lexer = new Lexer("Kravica. KRAVICA.\\");
		
		lexer.nextToken();
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTextCurlyBracket() {
		Lexer lexer = new Lexer("Krav{ica. KRAVICA.");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTextCurlyBracketAtEnd() {
		Lexer lexer = new Lexer("Kravica. KRAVICA.{");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test
	public void testForLoopTag() {
		Lexer lexer = new Lexer("{$ FOR j k \"10\" 10.1 $}");
		
		Token[] correctData = new Token[] {
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "for"),
				new Token(TokenType.VARIABLE, "j"),
				new Token(TokenType.VARIABLE, "k"),
				new Token(TokenType.STRING_VALUE, "10"),
				new Token(TokenType.DOUBLE_VALUE, 10.1d),
				new Token(TokenType.TAG_END, null)
		};
		
		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testEchoTag() {
		Lexer lexer = new Lexer("{$    = var32_-32.54- + *2123-\"\\\"int\\\\\"@f@e23-21 3k $}");
		
		Token correctData[] = new Token[] {
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "="),
				new Token(TokenType.VARIABLE, "var32_"),
				new Token(TokenType.DOUBLE_VALUE, -32.54d),
				new Token(TokenType.OPERATOR, "-"),
				new Token(TokenType.OPERATOR, "+"),
				new Token(TokenType.OPERATOR, "*"),
				new Token(TokenType.INT_VALUE, 2123),
				new Token(TokenType.OPERATOR, "-"),
				new Token(TokenType.STRING_VALUE, "\"int\\"),
				new Token(TokenType.FUNCTION, "f"),
				new Token(TokenType.FUNCTION, "e23"),
				new Token(TokenType.INT_VALUE, -21),
				new Token(TokenType.INT_VALUE, 3),
				new Token(TokenType.VARIABLE, "k"),
				new Token(TokenType.TAG_END, null)
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testMixed() {
		Lexer lexer = new Lexer(
			"{$ FOR i 1 \"10\" -1.0 $}" + 
			"\r\n This \\\\ is" +
			"{$= i $}" +
			"-th time this \\{} message is generated.\r\n" +
			"{$END$}" +
			"\r\n" +
			"{$= i i * @sin   \"0.000\" @decfmt $}" + 
			"\r\n" +
			"{$END$}"
		);
		
		Token[] correctData = new Token[] {
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "for"),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.INT_VALUE, 1),
				new Token(TokenType.STRING_VALUE, "10"),
				new Token(TokenType.DOUBLE_VALUE, -1.0d),
				new Token(TokenType.TAG_END, null),
				new Token(TokenType.TEXT, "\r\n This \\ is"),
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "="),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.TAG_END, null),
				new Token(TokenType.TEXT, "-th time this {} message is generated.\r\n"),
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "end"),
				new Token(TokenType.TAG_END, null),
				new Token(TokenType.TEXT, "\r\n"),
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "="),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.VARIABLE, "i"),
				new Token(TokenType.OPERATOR, "*"),
				new Token(TokenType.FUNCTION, "sin"),
				new Token(TokenType.STRING_VALUE, "0.000"),
				new Token(TokenType.FUNCTION, "decfmt"),
				new Token(TokenType.TAG_END, null),
				new Token(TokenType.TEXT, "\r\n"),
				new Token(TokenType.TAG_START, null),
				new Token(TokenType.TAG_NAME, "end"),
				new Token(TokenType.TAG_END, null)
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidVariableNameUnderscore() {
		Lexer lexer = new Lexer("{$FOR _i j k l $}");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidStringEscape() {
		Lexer lexer = new Lexer("{$FOR i j \"Hello\\!\" $}");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidDot() {
		Lexer lexer = new Lexer("{$FOR i j .245 $}");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidCharacter() {
		Lexer lexer = new Lexer("{$FOR i j k % $}");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	@Test(expected=LexerException.class)
	public void testInvalidTagEnding() {
		Lexer lexer = new Lexer("{$FOR i j k } this is a tag");
		
		while (lexer.nextToken().getType() != TokenType.EOF) {
			checkAndSetState(lexer);
		}
	}
	
	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			
			checkAndSetState(lexer);
				
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getType(), actual.getType());
			assertEquals(msg, expected.getValue(), actual.getValue());
			counter++;
		}
	}
	
	private void checkToken(Token actual, Token expected) {
		String msg = "Tokens are not equal.";
		assertEquals(msg, expected.getType(), actual.getType());
		assertEquals(msg, expected.getValue(), actual.getValue());
	}
	
	private void checkAndSetState(Lexer lexer) {
		if (lexer.getToken().getType() == TokenType.TAG_START) {
			lexer.setState(LexerState.TAG);
		} else if (lexer.getToken().getType() == TokenType.TAG_END) {
			lexer.setState(LexerState.TEXT);
		}
	}
}
