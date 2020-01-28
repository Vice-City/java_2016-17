package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparisonOperatorsTest {

	@Test
	public void testLessComparator() {
		assertTrue(ComparisonOperators.LESS.satisfied("ana", "banana"));
		assertTrue(ComparisonOperators.LESS.satisfied("ana", "ananas"));
		assertTrue(ComparisonOperators.LESS.satisfied("zana", "zena"));
		assertFalse(ComparisonOperators.LESS.satisfied("ana", "ana"));
		assertFalse(ComparisonOperators.LESS.satisfied("ana", "aba"));

		assertFalse(ComparisonOperators.LESS.satisfied(null, "ana"));
		assertFalse(ComparisonOperators.LESS.satisfied(null, null));
	}
	
	@Test
	public void testLessOrEqualComparator() {
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("ana", "banana"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("ana", "ananas"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("zana", "zena"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("ana", "ana"));
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("ana", "aba"));

		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied(null, "ana"));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(null, null));
	}
	
	@Test
	public void testEqualComparator() {
		assertFalse(ComparisonOperators.EQUALS.satisfied("ana", "banana"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("ana", "ananas"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("zana", "zena"));
		assertTrue(ComparisonOperators.EQUALS.satisfied("ana", "ana"));
		assertFalse(ComparisonOperators.EQUALS.satisfied("ana", "aba"));

		assertFalse(ComparisonOperators.EQUALS.satisfied(null, "ana"));
		assertTrue(ComparisonOperators.EQUALS.satisfied(null, null));
	}
	
	@Test
	public void testGreaterOrEqualComparator() {
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("banana", "ana"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("ananas", "ana"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("zena", "ana"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("ana", "ana"));
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("aba", "ana"));
		
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied(null, "ana"));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(null, null));
	}
	
	@Test
	public void testGreaterComparator() {
		assertTrue(ComparisonOperators.GREATER.satisfied("banana", "ana"));
		assertTrue(ComparisonOperators.GREATER.satisfied("ananas", "ana"));
		assertTrue(ComparisonOperators.GREATER.satisfied("zena", "ana"));
		assertFalse(ComparisonOperators.GREATER.satisfied("ana", "ana"));
		assertFalse(ComparisonOperators.GREATER.satisfied("aba", "ana"));
		
		assertFalse(ComparisonOperators.GREATER.satisfied(null, "ana"));
		assertFalse(ComparisonOperators.GREATER.satisfied(null, null));
	}
	
	@Test
	public void testNotEqualsComparator() {
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("banana", "ana"));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("ananas", "ana"));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("zena", "ana"));
		assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("ana", "ana"));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("aba", "ana"));

		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(null, "ana"));
		assertFalse(ComparisonOperators.NOT_EQUALS.satisfied(null, null));
	}
	
	@Test
	public void testLikeComparator() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Ana", "Ana"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Analana", "Ana*"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Lana", "*ana"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Analana", "A*a"));
		
		assertFalse(ComparisonOperators.LIKE.satisfied("Ana", "ana"));
		assertFalse(ComparisonOperators.LIKE.satisfied("Ana", "*ana"));
		assertFalse(ComparisonOperators.LIKE.satisfied("Ana", "a*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("na", "*ana"));
		assertFalse(ComparisonOperators.LIKE.satisfied("Analana", "ab*a"));

		assertFalse(ComparisonOperators.LIKE.satisfied(null, "ana"));
		assertTrue(ComparisonOperators.LIKE.satisfied(null, null));
	}

}
