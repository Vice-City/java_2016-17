package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.parser.QueryParser;
import hr.fer.zemris.java.hw04.db.parser.QueryParserException;;

public class QueryParserTest {

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNull() {
		new QueryParser(null);
	}
	
	@Test(expected=QueryParserException.class)
	public void testEmptyQuery() {
		new QueryParser("");
	}
	
	@Test
	public void testGoodQuery() {
		QueryParser parser = new QueryParser(
				"firstName>\"A\" anD \t\nlastName LIKE\"*a\" and \rjmbag!=\"0000000001\""
		);
		
		List<ConditionalExpression> expressions = parser.getQuery();
		
		assertTrue(expressions.size() == 3);
		assertFalse(parser.isDirectQuery());
		
		assertTrue(expressions.get(0).getGetter() == FieldValueGetters.FIRST_NAME);
		assertTrue(expressions.get(0).getOperator() == ComparisonOperators.GREATER);
		assertTrue(expressions.get(0).getValue().equals("A"));
		
		assertTrue(expressions.get(1).getGetter() == FieldValueGetters.LAST_NAME);
		assertTrue(expressions.get(1).getOperator() == ComparisonOperators.LIKE);
		assertTrue(expressions.get(1).getValue().equals("*a"));
		
		assertTrue(expressions.get(2).getGetter() == FieldValueGetters.JMBAG);
		assertTrue(expressions.get(2).getOperator() == ComparisonOperators.NOT_EQUALS);
		assertTrue(expressions.get(2).getValue().equals("0000000001"));
	}
	
	@Test
	public void testDirectQuery() {
		QueryParser parser = new QueryParser(
				"jmbag=\"0000000001\""
		);
		
		List<ConditionalExpression> expressions = parser.getQuery();
		
		assertTrue(expressions.size() == 1);
		assertTrue(parser.isDirectQuery());
		
		assertTrue(expressions.get(0).getGetter() == FieldValueGetters.JMBAG);
		assertTrue(expressions.get(0).getOperator() == ComparisonOperators.EQUALS);
		assertTrue(expressions.get(0).getValue().equals("0000000001"));
	}
	
	@Test
	public void testQueryWithSingleExpression() {
		QueryParser parser = new QueryParser(
				"firstName=\"Ante\""
		);
		
		List<ConditionalExpression> expressions = parser.getQuery();
		
		assertTrue(expressions.size() == 1);
		assertFalse(parser.isDirectQuery());
		
		assertTrue(expressions.get(0).getGetter() == FieldValueGetters.FIRST_NAME);
		assertTrue(expressions.get(0).getOperator() == ComparisonOperators.EQUALS);
		assertTrue(expressions.get(0).getValue().equals("Ante"));
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidAttributeName() {
		new QueryParser("foobar=\"0000000010\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidLowercaseAttributeName() {
		new QueryParser("firstname=\"Ante\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOperator() {
		new QueryParser("foobar==\"0000000010\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOperator2() {
		new QueryParser("foobar=>\"0000000010\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOperator3() {
		new QueryParser("foobar!\"0000000010\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidLikeComparisonString() {
		new QueryParser("firstName LIKE \"an*te*");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOrdering() {
		new QueryParser("foobar= LIKE \"0000000010\"");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOrdering2() {
		new QueryParser("foo= bar");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOrdering3() {
		new QueryParser("\"0000000010\" LIKE firstName");
	}
	
	@Test(expected=QueryParserException.class)
	public void testInvalidOrdering4() {
		new QueryParser("foobar=\"0000000010\" and and");
	}
	
	@Test(expected=QueryParserException.class)
	public void testUnclosedString() {
		new QueryParser("foobar=\"0000000010");
	}
	
	@Test(expected=QueryParserException.class)
	public void testLogicalOperatorAtEnd() {
		new QueryParser("foobar=\"0000000010\" and");
	}
	
}
