package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class StudentRecordTest {

	@Test
	public void testConstructor() {
		StudentRecord jasenka = new StudentRecord(
				"0036472490",
				"Maletić",
				"Jasenka",
				2
		);
		
		assertTrue(jasenka.getJmbag().equals("0036472490"));
		assertTrue(jasenka.getLastName().equals("Maletić"));
		assertTrue(jasenka.getFirstName().equals("Jasenka"));
		assertTrue(jasenka.getFinalGrade() == 2);
	}

	@Test
	public void testEquals() {
		StudentRecord jasenka = new StudentRecord(
				"0036472490",
				"Maletić",
				"Jasenka",
				2
		);
		
		StudentRecord jasenka2 = new StudentRecord(
				"0036472490",
				"Maletić",
				"Jasenka",
				4
		);
		
		StudentRecord jasenka3 = new StudentRecord(
				"0000000001",
				"Maletić",
				"Jasenka",
				2
		);
		
		assertTrue(jasenka.equals(jasenka2));
		assertFalse(jasenka.equals(jasenka3));
		assertFalse(jasenka2.equals(jasenka3));
		
		Object obj = new Object();
		
		assertFalse(jasenka.equals(obj));
	}
}
