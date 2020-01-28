package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3Test {

	@Test
	public void testConstructor() {
		Vector3 vec = new Vector3(1.0, 2.0, 3.0);
		
		assertTrue(doublesEqual(vec.getX(), 1.0));
		assertTrue(doublesEqual(vec.getY(), 2.0));
		assertTrue(doublesEqual(vec.getZ(), 3.0));
	}
	
	@Test
	public void testNorm() {
		Vector3 vec = new Vector3(1.0, 2.0, 3.0);

		assertTrue(doublesEqual(vec.norm(), 3.74166));
	}
	
	@Test
	public void testNormalize() {
		Vector3 vec = new Vector3(1.0, 2.0, 3.0).normalized();
		
		assertTrue(doublesEqual(vec.norm(), 1.0));
	}
	
	@Test
	public void testAdd() {
		Vector3 vec2 = new Vector3(1.0, 2.0, 3.0);
		Vector3 vec = vec2.add(vec2);

		assertTrue(doublesEqual(vec.norm(), 7.48331));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNull() {
		new Vector3(0, 0, 0).add(null);
	}
	
	@Test
	public void testSub() {
		Vector3 vec2 = new Vector3(1.0, 2.0, 3.0);
		Vector3 vec = vec2.sub(vec2);

		assertTrue(doublesEqual(vec.norm(), 0.0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubNull() {
		new Vector3(0, 0, 0).sub(null);
	}
	
	@Test
	public void testDot() {
		Vector3 vec2 = new Vector3(1.0, 2.0, 3.0);
		double dot = vec2.dot(vec2);

		assertTrue(doublesEqual(dot, 14.0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDotNull() {
		new Vector3(0, 0, 0).dot(null);
	}
	
	@Test
	public void testCross() {
		Vector3 vec2 = new Vector3(1.0, 2.0, 3.0);
		Vector3 vec = vec2.cross(new Vector3(-1.0, 0.0, 1.0));

		assertTrue(doublesEqual(vec.getX(), 2.0));
		assertTrue(doublesEqual(vec.getY(), -4.0));
		assertTrue(doublesEqual(vec.getZ(), 2.0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCrossNull() {
		new Vector3(0, 0, 0).cross(null);
	}
	
	@Test
	public void testCosAngle() {
		Vector3 vec2 = new Vector3(1.0, 2.0, 3.0);
		double cos = vec2.cosAngle(new Vector3(-1.0, 0.0, 1.0));

		assertTrue(doublesEqual(cos, 1.0/Math.sqrt(7)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCosAngleNull() {
		new Vector3(0, 0, 0).cosAngle(null);
	}
	
	private static boolean doublesEqual(double d1, double d2) {
		return Math.abs(d1-d2) < 10e-6 ? true : false;
	}

}
