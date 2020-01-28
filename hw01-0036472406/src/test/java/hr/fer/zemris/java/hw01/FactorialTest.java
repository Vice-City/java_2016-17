package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

public class FactorialTest {

	@Test
	public void faktorijelZa1() {
		long factorial = Factorial.calculateFactorial(1);
		assertEquals(1L, factorial);
	}
	
	@Test
	public void faktorijelZa4() {
		long factorial = Factorial.calculateFactorial(4);
		assertEquals(24L, factorial);
	}
	
	@Test
	public void faktorijelZa20() {
		long factorial = Factorial.calculateFactorial(20);
		assertEquals(2432902008176640000L, factorial);
	}

	@Test
	public void faktorijelZa0() {
		long factorial = Factorial.calculateFactorial(0);
		assertEquals(-1L, factorial);
	}
	
	@Test
	public void faktorijelZa21() {
		long factorial = Factorial.calculateFactorial(21);
		assertEquals(-1L, factorial);
	}
}
