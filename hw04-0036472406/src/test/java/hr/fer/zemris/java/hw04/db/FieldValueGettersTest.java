package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldValueGettersTest {

	private static StudentDatabase db = StudentDatabase.loadDefaultDatabase();
	
	@Test
	public void testFirstNameGetter() {
		String tomislav = FieldValueGetters.FIRST_NAME.get(db.forJMBAG("0000000032"));
		assertTrue(tomislav.equals("Tomislav"));
		
		String zrinka = FieldValueGetters.FIRST_NAME.get(db.forJMBAG("0000000040"));
		assertTrue(zrinka.equals("Zrinka"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFirstNameNull() {
		FieldValueGetters.FIRST_NAME.get(null);
	}
	
	@Test
	public void testLastNameGetter() {
		String tomislav = FieldValueGetters.LAST_NAME.get(db.forJMBAG("0000000032"));
		assertTrue(tomislav.equals("Lučev"));
		
		String zrinka = FieldValueGetters.LAST_NAME.get(db.forJMBAG("0000000040"));
		assertTrue(zrinka.equals("Mišura"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testLastNameNull() {
		FieldValueGetters.LAST_NAME.get(null);
	}
	
	@Test
	public void testJmbagGetter() {
		String tomislav = FieldValueGetters.JMBAG.get(db.forJMBAG("0000000032"));
		assertTrue(tomislav.equals("0000000032"));
		
		String zrinka = FieldValueGetters.JMBAG.get(db.forJMBAG("0000000040"));
		assertTrue(zrinka.equals("0000000040"));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testJmbagNull() {
		FieldValueGetters.JMBAG.get(null);
	}

}
