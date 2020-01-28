package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Test;

public class ObjectMultistackTest {

	@Test
	public void testPush() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		stackMap.push("year", new ValueWrapper(1993));
		assertTrue(stackMap.peek("year").getValue().equals(1993));
		assertFalse(stackMap.isEmpty("year"));
		
		stackMap.push("year", new ValueWrapper(2012));
		assertTrue(stackMap.peek("year").getValue().equals(2012));
		
		stackMap.pop("year");
		stackMap.pop("year");
		assertTrue(stackMap.isEmpty("year"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushKeyNull() {
		new ObjectMultistack().push(null, new ValueWrapper(2012));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPushValueNull() {
		new ObjectMultistack().push("year", null);
	}
	
	@Test
	public void testPop() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		stackMap.push("year", new ValueWrapper(1993));
		stackMap.push("year", new ValueWrapper(2012));
		
		assertTrue(stackMap.pop("year").getValue().equals(2012));
		assertTrue(stackMap.pop("year").getValue().equals(1993));
		
		stackMap.push("month", new ValueWrapper("February"));
		assertTrue(stackMap.pop("month").getValue().equals("February"));
		
	}

	@Test(expected=EmptyStackException.class)
	public void testPopEmpty1() {
		new ObjectMultistack().pop("year");
	
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPopEmpty2() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		stackMap.push("year", new ValueWrapper(1993));
		stackMap.push("year", new ValueWrapper(2012));
		stackMap.pop("year");
		stackMap.pop("year");
		stackMap.pop("year");
	
	}
	
	@Test
	public void testPeek() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		stackMap.push("year", new ValueWrapper(1993));
		stackMap.push("year", new ValueWrapper(2012));
		
		assertTrue(stackMap.peek("year").getValue().equals(2012));
		assertTrue(stackMap.peek("year").getValue().equals(2012));
		
		stackMap.pop("year");
		assertTrue(stackMap.peek("year").getValue().equals(1993));
		assertTrue(stackMap.peek("year").getValue().equals(1993));

		
		stackMap.push("month", new ValueWrapper("February"));
		assertTrue(stackMap.peek("month").getValue().equals("February"));
	}
	
	@Test(expected=EmptyStackException.class)
	public void testPeekEmpty() {
		new ObjectMultistack().peek("year");
	}
	
	@Test
	public void testIsEmpty() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		assertTrue(stackMap.isEmpty("year"));
		assertTrue(stackMap.isEmpty("month"));
		
		stackMap.push("year", new ValueWrapper(1993));
		stackMap.push("year", new ValueWrapper(2012));
		assertFalse(stackMap.isEmpty("year"));
		assertTrue(stackMap.isEmpty("month"));

		stackMap.pop("year");
		assertFalse(stackMap.isEmpty("year"));
		assertTrue(stackMap.isEmpty("month"));
		
		stackMap.push("month", new ValueWrapper("February"));
		assertFalse(stackMap.isEmpty("year"));
		assertFalse(stackMap.isEmpty("month"));
		
		stackMap.pop("year");
		stackMap.pop("month");

		assertTrue(stackMap.isEmpty("year"));
		assertTrue(stackMap.isEmpty("month"));
	}
	
	@Test
	public void testInteractionWithValueWrapperMethods() {
		ObjectMultistack stackMap = new ObjectMultistack();
		
		stackMap.push("year", new ValueWrapper(1993));
		
		stackMap.peek("year").add(-5);
		assertTrue(stackMap.peek("year").getValue().equals(1988));
		
		stackMap.peek("year").add("2.5");
		assertTrue(stackMap.peek("year").getValue().equals(1990.5));
		
		stackMap.peek("year").subtract("0.005e2");
		assertTrue(stackMap.peek("year").getValue().equals(1990.0));
		
	}

}
