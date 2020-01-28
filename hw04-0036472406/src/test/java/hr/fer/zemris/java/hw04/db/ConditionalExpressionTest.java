package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

public class ConditionalExpressionTest {

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorGetterNull() {
		new ConditionalExpression(null, "Hello", ComparisonOperators.EQUALS);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorStringNull() {
		new ConditionalExpression(FieldValueGetters.FIRST_NAME, null, ComparisonOperators.EQUALS);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorComparatorNull() {
		new ConditionalExpression(FieldValueGetters.FIRST_NAME, null, null);
	}
}
