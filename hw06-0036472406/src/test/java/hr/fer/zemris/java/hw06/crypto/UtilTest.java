package hr.fer.zemris.java.hw06.crypto;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UtilTest {

	@Test
	public void testHexToByte() {
		String hex = "0123456789ABCDEF";
		
		byte[] result = Util.hexToByte(hex);
		
		// typed into Windows Calculator, Programmer mode, changed QWORD -> BYTE
		byte[] expected = new byte[] {
				1, 35, 69, 103, -119, -85, -51, -17
		};
		
		assertTrue(Arrays.equals(expected, result));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHexToByteIllegalChar() {
		Util.hexToByte("MD");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHexToByteUnevenHexadecimals() {
		Util.hexToByte("0");
	}
	
	@Test
	public void testByteToHex() {
		byte[] bytes = new byte[] {
				-128, -100, -50, -10, -5, -1, 0, 1, 2, 25, 100, 127
		};
		
		String result = Util.byteToHex(bytes);
		String expected = "809ccef6fbff00010219647f";
		
		assertTrue(result.equals(expected));
	}
	
	@Test
	public void singleByteTohex() {
		assertTrue("00".equals(Util.singleByteToHex((byte) 0)));
		assertTrue("ff".equals(Util.singleByteToHex((byte) -1)));
		assertTrue("80".equals(Util.singleByteToHex((byte) -128)));
	}

}
