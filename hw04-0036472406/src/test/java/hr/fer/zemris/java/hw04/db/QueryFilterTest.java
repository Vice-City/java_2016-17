package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.parser.QueryParser;

public class QueryFilterTest {

	private static List<ConditionalExpression> expressions;
	private static StudentDatabase database;
	
	static {
		QueryParser parser = new QueryParser("firstName LIKE \"A*\" and jmbag>\"0000000040\"");
		expressions = parser.getQuery();
		
		database = StudentDatabase.loadDefaultDatabase();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNull() {
		new QueryFilter(null);
	}
	
	@Test
	public void testAcceptMethod() {
		QueryFilter filter = new QueryFilter(expressions);
		
		assertTrue(filter.accepts(database.forJMBAG("0000000050"))); //Alen Sikirica
		assertTrue(filter.accepts(database.forJMBAG("0000000051"))); //Andro Skočir
		assertFalse(filter.accepts(database.forJMBAG("0000000036"))); //Ante Markić
		assertFalse(filter.accepts(database.forJMBAG("0000000001"))); //Marin Akšamović
	}

}
