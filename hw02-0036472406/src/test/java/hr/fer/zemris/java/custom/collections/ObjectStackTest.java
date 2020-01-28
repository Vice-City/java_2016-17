package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectStackTest {
	
	private ObjectStack initializeZeroOne() {
		ObjectStack stack = new ObjectStack();
		stack.push(new Integer(0));
		stack.push(new Integer(1));
		
		return stack;
	}
	
	@Test
	public void constructorTest() {
		ObjectStack stack = new ObjectStack();
		stack.push(new Integer(0));
		stack.push(new Integer(1));
		assertEquals(2, stack.size());
	}
	
	@Test
	public void sizeTest() {
		ObjectStack stack = initializeZeroOne();
		assertEquals(2, stack.size());
		
		stack.push(new Integer(1));
		assertEquals(3, stack.size());
		
		stack.pop();
		stack.pop();
		assertEquals(1, stack.size());
	}
	
	@Test
	public void pushTest() {
		ObjectStack stack = initializeZeroOne();
		
		stack.push("bok");
		assertEquals("bok", stack.peek());
		
		stack.push(new Double(1.6));
		assertEquals(Double.valueOf(1.6), stack.peek());
		
		try {
			stack.push(null);
		} catch (IllegalArgumentException ex) { }
		assertEquals(4, stack.size());
	}
	
	@Test
	public void peekTest() {
		ObjectStack stack = initializeZeroOne();
		
		stack.push("foo");
		assertEquals("foo", stack.peek());
		
		stack.pop(); stack.pop(); stack.pop();
		assertEquals(0, stack.size());
		
		try {
			stack.peek();
		} catch (EmptyStackException ex) {
			stack.push("bar");
		}
		assertEquals("bar", stack.peek());
		
	}
	
	@Test
	public void popTest() {
		ObjectStack stack = initializeZeroOne();
		
		stack.push("foo");
		assertEquals("foo", stack.peek());
		
		stack.pop(); stack.pop(); stack.pop();
		assertEquals(0, stack.size());
		
		try {
			stack.pop();
		} catch (EmptyStackException ex) {
			stack.push("bar");
		}
		assertEquals("bar", stack.pop());
		assertEquals(0,  stack.size());
		
	}
	
	@Test
	public void clearTest() {
		ObjectStack stack = initializeZeroOne();
		assertEquals(2, stack.size());
		
		stack.clear();
		assertEquals(0, stack.size());
	}
	
	@Test
	public void isEmptyTest() {
		ObjectStack stack = initializeZeroOne();
		assertFalse(stack.isEmpty());
		
		stack.pop();
		assertFalse(stack.isEmpty());
		
		stack.pop();
		assertTrue(stack.isEmpty());
	}
	
}
