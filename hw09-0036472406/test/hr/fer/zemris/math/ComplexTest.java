package hr.fer.zemris.math;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ComplexTest {

	@Test
	public void constructorTest() {
		Complex num = new Complex(-3.0, 2.5);
		assertTrue(doublesEqual(-3.0, num.getRe()));
		assertTrue(doublesEqual(2.5, num.getIm()));
	}
	
	@Test
	public void getRealTest() {
		Complex num = new Complex(9.0, 2);
		assertTrue(doublesEqual(9.0, num.getRe()));
	}
	
	@Test
	public void getImaginaryTest() {
		Complex num = new Complex(2.0, 1.0);
		assertTrue(doublesEqual(1.0, num.getIm()));
	}
	
	@Test
	public void addTest() {
		Complex num = new Complex(1.0, 1.0).add(new Complex(9.0, -2.0));
		assertTrue(doublesEqual(10.0, num.getRe()));
		assertTrue(doublesEqual(-1.0, num.getIm()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addNulltest() {
		Complex.ONE.add(null);
	}
	
	@Test
	public void subTest() {
		Complex num = new Complex(5.0, 3.0).sub(new Complex(9.0, -2.0));
		assertTrue(doublesEqual(-4.0, num.getRe()));
		assertTrue(doublesEqual(5.0, num.getIm()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void subNulltest() {
		Complex.ONE.sub(null);
	}
	
	@Test
	public void multiplyTest() {
		Complex num = new Complex(5.0, 3.0).multiply(new Complex(9.0, -2.0));
		assertTrue(doublesEqual(51.0, num.getRe()));
		assertTrue(doublesEqual(17.0, num.getIm()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void multiplyNulltest() {
		Complex.ONE.multiply(null);
	}
	
	@Test
	public void divideTest() {
		Complex num = new Complex(5.0, 3.0).divide(new Complex(9.0, -2.0));
		assertTrue(doublesEqual(0.4588235294, num.getRe()));
		assertTrue(doublesEqual(0.4352941176, num.getIm()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void divideNulltest() {
		Complex.ONE.divide(null);
	}
	
	@Test
	public void powerTest() {
		Complex num = new Complex(5.0, 3.0).power(4);
		assertTrue(doublesEqual(-644.0, num.getRe()));
		assertTrue(doublesEqual(960.0, num.getIm()));
		
		
		num = num.power(0);
		assertTrue(doublesEqual(1.0, num.getRe()));
		assertTrue(doublesEqual(0.0, num.getIm()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void powerIllegalTest() {
		Complex.ONE.power(-1);
	}
	
	@Test
	public void rootTest() {
		Complex num = new Complex(5.0, 3.0);
		List<Complex> roots = num.root(2);
		
		System.out.println(roots.get(0));
		
		assertTrue(doublesEqual(2.3271175, roots.get(0).getRe()));
		assertTrue(doublesEqual(0.6445742, roots.get(0).getIm()));
		
		assertTrue(doublesEqual(-2.3271175, roots.get(1).getRe()));
		assertTrue(doublesEqual(-0.6445742, roots.get(1).getIm()));

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void rootIllegalTest() {
		Complex.ONE.root(0);
	}

	private static boolean doublesEqual(double d1, double d2) {
		return Math.abs(d1-d2) < 10e-6 ? true : false;
	}
	
}
