package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueWrapperTest {

	@Test
	public void testConstructor() {
		ValueWrapper vw = new ValueWrapper(null);
		assertTrue(vw.getValue() == null);
		
		vw = new ValueWrapper(Integer.valueOf(-3));
		assertTrue(vw.getValue().equals(-3));
		
		vw = new ValueWrapper(Double.valueOf(Math.PI));
		assertTrue(vw.getValue().equals(Math.PI));
		
		vw = new ValueWrapper("Foobar!");
		assertTrue(vw.getValue().equals("Foobar!"));
		
		vw = new ValueWrapper(Long.valueOf(4L));
		assertTrue(vw.getValue().equals(4L));
	}
	
	@Test
	public void testAdd() {
		ValueWrapper vw = new ValueWrapper(1);
		vw.add(2);
		assertTrue(vw.getValue().equals(3));
		
		ValueWrapper vw2 = new ValueWrapper(3);
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(6));
		assertTrue(vw2.getValue().equals(3));
		
		
		vw = new ValueWrapper(null);
		vw.add(2.0);
		assertTrue(vw.getValue().equals(2.0));
		
		vw2 = new ValueWrapper(3.5);
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(5.5));
		assertTrue(vw2.getValue().equals(3.5));
		
		
		vw = new ValueWrapper(1);
		vw.add("2");
		assertTrue(vw.getValue().equals(3));
		
		vw2 = new ValueWrapper("3.0");
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper(1.0);
		vw.add(2);
		assertTrue(vw.getValue().equals(3.0));
		
		vw2 = new ValueWrapper("3");
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3"));
		
		
		vw = new ValueWrapper("1");
		vw.add(2);
		assertTrue(vw.getValue().equals(3));
		
		vw2 = new ValueWrapper("3.0");
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		vw = new ValueWrapper("1.5e2");
		vw.add(2);
		assertTrue(vw.getValue().equals(152.0));
		
		vw2 = new ValueWrapper("3.0");
		vw.add(vw2.getValue());
		assertTrue(vw.getValue().equals(155.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddIllegal() {
		ValueWrapper vw = new ValueWrapper(Long.valueOf(1L));
		vw.add(5);

	}
	
	@Test
	public void testSubtract() {
		ValueWrapper vw = new ValueWrapper(1);
		vw.subtract(2);
		assertTrue(vw.getValue().equals(-1));
		
		ValueWrapper vw2 = new ValueWrapper(3);
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(-4));
		assertTrue(vw2.getValue().equals(3));
		
		
		vw = new ValueWrapper(null);
		vw.subtract(2.0);
		assertTrue(vw.getValue().equals(-2.0));
		
		vw2 = new ValueWrapper(3.5);
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(-5.5));
		assertTrue(vw2.getValue().equals(3.5));
		
		
		vw = new ValueWrapper(1);
		vw.subtract("2");
		assertTrue(vw.getValue().equals(-1));
		
		vw2 = new ValueWrapper("3.0");
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(-4.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper(1.0);
		vw.subtract(2);
		assertTrue(vw.getValue().equals(-1.0));
		
		vw2 = new ValueWrapper("3");
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(-4.0));
		assertTrue(vw2.getValue().equals("3"));
		
		
		vw = new ValueWrapper("1");
		vw.subtract(2);
		assertTrue(vw.getValue().equals(-1));
		
		vw2 = new ValueWrapper("3.0");
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(-4.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper("1.5e2");
		vw.subtract(2);
		assertTrue(vw.getValue().equals(148.0));
		
		vw2 = new ValueWrapper("3.0");
		vw.subtract(vw2.getValue());
		assertTrue(vw.getValue().equals(145.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubtractIllegal() {
		ValueWrapper vw = new ValueWrapper(Long.valueOf(1L));
		vw.subtract(5);

	}
	
	@Test
	public void testMultiply() {
		ValueWrapper vw = new ValueWrapper(1);
		vw.multiply(2);
		assertTrue(vw.getValue().equals(2));
		
		ValueWrapper vw2 = new ValueWrapper(3);
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(6));
		assertTrue(vw2.getValue().equals(3));
		
		
		vw = new ValueWrapper(null);
		vw.multiply(2.0);
		assertTrue(vw.getValue().equals(0.0));
		
		vw2 = new ValueWrapper(3.5);
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(0.0));
		assertTrue(vw2.getValue().equals(3.5));
		
		
		vw = new ValueWrapper(1);
		vw.multiply("2");
		assertTrue(vw.getValue().equals(2));
		
		vw2 = new ValueWrapper("3.0");
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper(1.0);
		vw.multiply(2);
		assertTrue(vw.getValue().equals(2.0));
		
		vw2 = new ValueWrapper("3");
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3"));
		
		
		vw = new ValueWrapper("1");
		vw.multiply(2);
		assertTrue(vw.getValue().equals(2));
		
		vw2 = new ValueWrapper("3.0");
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(6.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper("1.5e2");
		vw.multiply(2);
		assertTrue(vw.getValue().equals(300.0));
		
		vw2 = new ValueWrapper("3.0");
		vw.multiply(vw2.getValue());
		assertTrue(vw.getValue().equals(900.0));
		assertTrue(vw2.getValue().equals("3.0"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMultiplyIllegal() {
		ValueWrapper vw = new ValueWrapper(Float.valueOf(1.0f));
		vw.multiply(5);

	}
	
	@Test
	public void testDivide() {
		ValueWrapper vw = new ValueWrapper(8);
		vw.divide(2);
		assertTrue(vw.getValue().equals(4));
		
		ValueWrapper vw2 = new ValueWrapper(3);
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(1));
		assertTrue(vw2.getValue().equals(3));
		
		
		vw = new ValueWrapper(12);
		vw.divide(2.0);
		assertTrue(vw.getValue().equals(6.0));
		
		vw2 = new ValueWrapper(1.5);
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(4.0));
		assertTrue(vw2.getValue().equals(1.5));
		
		
		vw = new ValueWrapper(-12);
		vw.divide("2");
		assertTrue(vw.getValue().equals(-6));
		
		vw2 = new ValueWrapper("3.0");
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(-2.0));
		assertTrue(vw2.getValue().equals("3.0"));
		
		
		vw = new ValueWrapper(12.0);
		vw.divide(2);
		assertTrue(vw.getValue().equals(6.0));
		
		vw2 = new ValueWrapper("3");
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(2.0));
		assertTrue(vw2.getValue().equals("3"));
		
		
		vw = new ValueWrapper("1");
		vw.divide(1);
		assertTrue(vw.getValue().equals(1));
		
		vw2 = new ValueWrapper("0.5");
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(2.0));
		assertTrue(vw2.getValue().equals("0.5"));
		
		vw = new ValueWrapper("1.5e2");
		vw.divide(2);
		assertTrue(vw.getValue().equals(75.0));
		
		vw2 = new ValueWrapper("5.0");
		vw.divide(vw2.getValue());
		assertTrue(vw.getValue().equals(15.0));
		assertTrue(vw2.getValue().equals("5.0"));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDivideIllegal() {
		ValueWrapper vw = new ValueWrapper(Long.valueOf(1L));
		vw.divide(5);

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDivideZero() {
		ValueWrapper vw = new ValueWrapper(Long.valueOf(1L));
		vw.add(null);

	}
	
	@Test
	public void testNumCompare() {
		ValueWrapper vw = new ValueWrapper(null);
		assertTrue(vw.numCompare(2) < 0);
		
		ValueWrapper vw2 = new ValueWrapper(3);
		assertTrue(vw2.numCompare(vw.getValue()) > 0);
		assertTrue(vw.numCompare(vw.getValue()) == 0);
		
		vw = new ValueWrapper(1);
		assertTrue(vw.numCompare(2.0) < 0);
		
		vw2 = new ValueWrapper(3.6982);
		assertTrue(vw2.numCompare(vw.getValue()) > 0);
		assertTrue(vw2.numCompare(vw2.getValue()) == 0);
		
		
		vw = new ValueWrapper(1);
		assertTrue(vw.numCompare("2.0") < 0);
		
		vw2 = new ValueWrapper("3.6982");
		assertTrue(vw2.numCompare(vw.getValue()) > 0);
		assertTrue(vw2.numCompare(vw2.getValue()) == 0);
		
		
		vw = new ValueWrapper("1e2");
		assertTrue(vw.numCompare(200.0) < 0);
		
		vw2 = new ValueWrapper(101);
		assertTrue(vw2.numCompare(vw.getValue()) > 0);
		assertTrue(vw.numCompare(vw.getValue()) == 0);
		
	}

}
