package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

public class RectangleTest {
	static final double DELTA_ERROR_MARGIN = 10e-6;
	
	@Test
	public void opsegZa1i1() {
		double perimeter = Rectangle.calculatePerimeter(1.0d, 1.0d);
		assertEquals(4.0d, perimeter, DELTA_ERROR_MARGIN);
	}
	
	@Test
	public void opsegZa5i10() {
		double perimeter = Rectangle.calculatePerimeter(5.0d, 10.0d);
		assertEquals(30.0d, perimeter, DELTA_ERROR_MARGIN);
	}
	
	@Test
	public void opsegZa1iMinus1() {
		double perimeter = Rectangle.calculateArea(1.0d, -1.0d);
		assertEquals(-1.0d, perimeter, DELTA_ERROR_MARGIN);
	}
	
	
	@Test
	public void povrsinaZa1i1() {
		double area = Rectangle.calculateArea(1.0d, 1.0d);
		assertEquals(1.0d, area, DELTA_ERROR_MARGIN);
	}
	
	@Test
	public void povrsinaZa4i6() {
		double area = Rectangle.calculateArea(4.0d, 6.0d);
		assertEquals(24.0d, area, DELTA_ERROR_MARGIN);
	}
	
	@Test
	public void povrsinaZa2iMinus2() {
		double area = Rectangle.calculateArea(2.0d, -2.0d);
		assertEquals(-1.0d, area, DELTA_ERROR_MARGIN);
	}
	
}
