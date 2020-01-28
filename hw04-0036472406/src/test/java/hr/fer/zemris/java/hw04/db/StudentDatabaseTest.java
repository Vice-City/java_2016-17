package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class StudentDatabaseTest {

	private static StudentDatabase database = StudentDatabase.loadDefaultDatabase();
	
	@Test
	public void testForJMBAG() {
		StudentRecord first = database.forJMBAG("0000000001");
		assertTrue(first.getJmbag().equals("0000000001"));
		
		StudentRecord vice = database.forJMBAG("0000000058");
		assertTrue(vice.getFirstName().equals("Vice"));
		
		StudentRecord empty = database.forJMBAG("0");
		assertTrue(empty == null);
		
	}
	
	@Test
	public void testFiltering() {
		assertTrue(database.filter(rec -> true).size() == 63);
		assertTrue(database.filter(rec -> false).size() == 0);
		
		assertTrue(database.filter(rec -> rec.getFirstName().compareTo("Vice") == 0).size() == 1);
		
	}
}
