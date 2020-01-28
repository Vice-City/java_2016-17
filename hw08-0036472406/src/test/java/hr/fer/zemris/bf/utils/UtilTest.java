package hr.fer.zemris.bf.utils;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testIndexToByteArray0And1() {
		byte[] result = Util.indexToByteArray(0, 1);
		assertTrue(Arrays.equals(result, new byte[] {0}));
	}
	
	@Test
	public void testIndexToByteArray2And4() {
		byte[] result = Util.indexToByteArray(2, 4);
		assertTrue(Arrays.equals(result, new byte[] {0,0,1,0}));
	}
	
	@Test
	public void testIndexToByteArray100And3() {
		byte[] result = Util.indexToByteArray(100, 3);
		assertTrue(Arrays.equals(result, new byte[] {1,0,0}));
	}
	
	@Test
	public void testIndexToByteArrayMinus2And1() {
		byte[] result = Util.indexToByteArray(-2, 1);
		assertTrue(Arrays.equals(result, new byte[] {0}));
	}
	
	@Test
	public void testIndexToByteArrayMinus2And16() {
		byte[] result = Util.indexToByteArray(-2, 16);
		byte[] expected = new byte[] {
				1,1,1,1,  1,1,1,1,   1,1,1,1,   1,1,1,0
		};
		assertTrue(Arrays.equals(result, expected));
	}
	
	@Test
	public void testIndexToByteArrayMinus2And32() {
		byte[] result = Util.indexToByteArray(-2, 32);
		byte[] expected = new byte[] {
				1,1,1,1,  1,1,1,1,   1,1,1,1,   1,1,1,1,
				1,1,1,1,  1,1,1,1,   1,1,1,1,   1,1,1,0
		};
		assertTrue(Arrays.equals(result, expected));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIndexToByteArrayTooBigN() {
		Util.indexToByteArray(-2, 33);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIndexToByteArrayTooSmallN() {
		Util.indexToByteArray(-2, 0);
	}

}
