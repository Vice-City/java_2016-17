package hr.fer.zemris.java.hw02;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw02.ComplexNumber;

public class ComplexNumberTest {

	private static boolean doublesEqual(double d1, double d2) {
		return Math.abs(d1-d2) < 10e-6 ? true : false;
	}
	
	@Test
	public void constructorTest() {
		ComplexNumber num = new ComplexNumber(-3.0, 2.5);
		assertTrue(doublesEqual(-3.0, num.getReal()));
		assertTrue(doublesEqual(2.5, num.getImaginary()));
	}
	
	@Test
	public void fromRealTest() {
		ComplexNumber num = ComplexNumber.fromReal(5.0);
		assertTrue(doublesEqual(5.0, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
	}
	
	@Test
	public void fromImaginaryTest() {
		ComplexNumber num = ComplexNumber.fromImaginary(1.2);
		assertTrue(doublesEqual(0.0, num.getReal()));
		assertTrue(doublesEqual(1.2, num.getImaginary()));
	}
	
	@Test
	public void fromMagnitudeAndAngleTest() {
		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(6, Math.PI/6);
		assertTrue(doublesEqual(3*Math.sqrt(3), num.getReal()));
		assertTrue(doublesEqual(3.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.fromMagnitudeAndAngle(-2.5, Math.PI);
		} catch (IllegalArgumentException ex) { }
	}
	
	@Test
	public void parseTest() {
		ComplexNumber num = ComplexNumber.parse("3.51");
		assertTrue(doublesEqual(3.51, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
		
		num = ComplexNumber.parse("-3.17");
		assertTrue(doublesEqual(-3.17, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
		
		num = ComplexNumber.parse("-2.71i");
		assertTrue(doublesEqual(0.0, num.getReal()));
		assertTrue(doublesEqual(-2.71, num.getImaginary()));
		
		num = ComplexNumber.parse("i");
		assertTrue(doublesEqual(0.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		num = ComplexNumber.parse("-i");
		assertTrue(doublesEqual(0.0, num.getReal()));
		assertTrue(doublesEqual(-1.0, num.getImaginary()));
		
		num = ComplexNumber.parse("1");
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
		
		num = ComplexNumber.parse("-2.71-3.15i");
		assertTrue(doublesEqual(-2.71, num.getReal()));
		assertTrue(doublesEqual(-3.15, num.getImaginary()));
		
		num = ComplexNumber.parse("+8.265i+7");
		assertTrue(doublesEqual(7.0, num.getReal()));
		assertTrue(doublesEqual(8.265, num.getImaginary()));
		
		num = new ComplexNumber(1.0, 1.0);
		
		try {
			num = ComplexNumber.parse("");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.parse("-4.2+24");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.parse("9i+1i");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.parse("21+4.i4");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.parse("-3i+2-0");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));
		
		try {
			num = ComplexNumber.parse("asi");
		} catch (NumberFormatException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(1.0, num.getImaginary()));		
	}
	
	@Test
	public void getRealTest() {
		ComplexNumber num = new ComplexNumber(9.0, 2);
		assertTrue(doublesEqual(9.0, num.getReal()));
	}
	
	@Test
	public void getImaginaryTest() {
		ComplexNumber num = new ComplexNumber(2.0, 1.0);
		assertTrue(doublesEqual(1.0, num.getImaginary()));
	}
	
	@Test
	public void getMagnitudeTest() {
		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(3.324, Math.PI/3);
		assertTrue(doublesEqual(3.324, num.getMagnitude()));
	}
	
	@Test
	public void getAngleTest() {
		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(1.0, 0.0);
		assertTrue(doublesEqual(0.0, num.getAngle()));
		
		num = ComplexNumber.fromMagnitudeAndAngle(1.0, Math.PI/4);
		assertTrue(doublesEqual(Math.PI/4, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, Math.PI/2);
		assertTrue(doublesEqual(Math.PI/2, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, 3*Math.PI/4);
		assertTrue(doublesEqual(3*Math.PI/4, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, Math.PI);
		assertTrue(doublesEqual(Math.PI, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, 5*Math.PI/4);
		assertTrue(doublesEqual(-3*Math.PI/4, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, 3*Math.PI/2);
		assertTrue(doublesEqual(-Math.PI/2, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, 7*Math.PI/4);
		assertTrue(doublesEqual(-Math.PI/4, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(1.0, 2*Math.PI);
		assertTrue(doublesEqual(0.0, num.getAngle()));

		num = ComplexNumber.fromMagnitudeAndAngle(0.0, Math.PI);
		assertTrue(doublesEqual(0.0, num.getAngle()));

	}
	
	@Test
	public void addTest() {
		ComplexNumber num = ComplexNumber.fromReal(5.0).add(new ComplexNumber(9.0, -2.0));
		assertTrue(doublesEqual(14.0, num.getReal()));
		assertTrue(doublesEqual(-2.0, num.getImaginary()));
	}
	
	@Test
	public void subTest() {
		ComplexNumber num = new ComplexNumber(5.0, 3.0).sub(new ComplexNumber(9.0, -2.0));
		assertTrue(doublesEqual(-4.0, num.getReal()));
		assertTrue(doublesEqual(5.0, num.getImaginary()));
	}
	
	@Test
	public void mulTest() {
		ComplexNumber num = new ComplexNumber(5.0, 3.0).mul(new ComplexNumber(9.0, -2.0));
		assertTrue(doublesEqual(51.0, num.getReal()));
		assertTrue(doublesEqual(17.0, num.getImaginary()));
	}
	
	@Test
	public void divTest() {
		ComplexNumber num = new ComplexNumber(5.0, 3.0).div(new ComplexNumber(9.0, -2.0));
		assertTrue(doublesEqual(0.4588235294, num.getReal()));
		assertTrue(doublesEqual(0.4352941176, num.getImaginary()));
		
		try {
			num = num.div(new ComplexNumber(0.0, 0.0));
		} catch (IllegalArgumentException ex) { }
		assertTrue(doublesEqual(0.4588235294, num.getReal()));
		assertTrue(doublesEqual(0.4352941176, num.getImaginary()));
	}
	
	@Test
	public void powerTest() {
		ComplexNumber num = new ComplexNumber(5.0, 3.0).power(4);
		assertTrue(doublesEqual(-644.0, num.getReal()));
		assertTrue(doublesEqual(960.0, num.getImaginary()));
		
		
		num = num.power(0);
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
		
		try {
			num = num.power(-1);
		} catch (IllegalArgumentException ex) { }
		assertTrue(doublesEqual(1.0, num.getReal()));
		assertTrue(doublesEqual(0.0, num.getImaginary()));
	}
	
	@Test
	public void rootTest() {
		ComplexNumber[] nums = new ComplexNumber(5.0, 3.0).root(7);
		assertTrue(doublesEqual(1.2826135, nums[0].getReal()));
		assertTrue(doublesEqual(0.0992185, nums[0].getImaginary()));
		
		nums = nums[0].root(1);
		assertTrue(doublesEqual(1.2826135, nums[0].getReal()));
		assertTrue(doublesEqual(0.0992185, nums[0].getImaginary()));
		
		try {
			nums = nums[0].root(0);
		} catch (IllegalArgumentException ex) { }
		assertTrue(doublesEqual(1.2826135, nums[0].getReal()));
		assertTrue(doublesEqual(0.0992185, nums[0].getImaginary()));
		
		
	}
	
	

}
