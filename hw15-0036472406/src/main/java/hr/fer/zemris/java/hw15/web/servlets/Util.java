package hr.fer.zemris.java.hw15.web.servlets;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

/*
 * This class is copy/pasted from my 6th homework.
 */

/**
 * Contains static methods for parsing a String representing
 * hexadecimal numbers into an array of bytes, and vice versa.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="utilServlet") // I'm annotating this so its methods can be used within other servlets
public class Util {

	/** map of hexadecimal characters mapped to their byte value **/
	private static final Map<Character, Integer> characterMap;
	/** map of byte values mapped to their lowercase hexadecimal characters **/
	private static final Map<Integer, Character> valueMap;
	
	static {
		characterMap = new HashMap<>();
		
		characterMap.put('0', 0x0);
		characterMap.put('1', 0x1);
		characterMap.put('2', 0x2);
		characterMap.put('3', 0x3);
		characterMap.put('4', 0x4);
		characterMap.put('5', 0x5);
		characterMap.put('6', 0x6);
		characterMap.put('7', 0x7);
		characterMap.put('8', 0x8);
		characterMap.put('9', 0x9);
		
		characterMap.put('a', 0xA);
		characterMap.put('A', 0xA);
		characterMap.put('b', 0xB);
		characterMap.put('B', 0xB);
		characterMap.put('c', 0xC);
		characterMap.put('C', 0xC);
		characterMap.put('d', 0xD);
		characterMap.put('D', 0xD);
		characterMap.put('e', 0xE);
		characterMap.put('E', 0xE);
		characterMap.put('f', 0xF);
		characterMap.put('F', 0xF);

		valueMap = new HashMap<>();
		
		valueMap.put(0x0, '0');
		valueMap.put(0x1, '1');
		valueMap.put(0x2, '2');
		valueMap.put(0x3, '3');
		valueMap.put(0x4, '4');
		valueMap.put(0x5, '5');
		valueMap.put(0x6, '6');
		valueMap.put(0x7, '7');
		valueMap.put(0x8, '8');
		valueMap.put(0x9, '9');
		
		valueMap.put(0xA, 'a');
		valueMap.put(0xB, 'b');
		valueMap.put(0xC, 'c');
		valueMap.put(0xD, 'd');
		valueMap.put(0xE, 'e');
		valueMap.put(0xF, 'f');

	}
	
	/**
	 * Converts a string representing hexadecimal characters into
	 * an array of bytes representing their values. Number of 
	 * hexadecimal characters has to be an even amount. For example,
	 * A1 will be converted into a byte of the following bits: 1010 0001.
	 * 
	 * @param input string of hexadecimal characters to be parsed
	 * @return array of bytes representing the parsed hexadecimal characters
	 * @throws IllegalArgumentException if the input string contains an uneven
	 * 		   amount of hexadecimal characters or if it contains characters
	 * 		   that don't represent hexadecimal characters
	 */
	public static byte[] hexToByte(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Argument input cannot be null!");
		}
		
		if (input.length() % 2 != 0) {
			throw new IllegalArgumentException(
					"Input string must have even number of hexadecimal numbers!"
			);
		}
		
		char[] characters = input.toCharArray();
		byte[] buffer = new byte[characters.length/2];
		
		for (int i = 0; i < characters.length; i += 2) {
			if (!characterMap.containsKey(characters[i]) || !characterMap.containsKey(characters[i+1])) {
				throw new IllegalArgumentException("Input string contains illegal characters!");
			}
			
			int upperByte = characterMap.get(characters[i]).intValue() << 4;
			int lowerByte = characterMap.get(characters[i+1]).intValue();

			int newByte = upperByte | lowerByte;
			
			buffer[i/2] = (byte) newByte;
		}
		
		return buffer;
		
	}
	
	/**
	 * Converts an array of bytes into a string representing the
	 * array's byte values, written in big-endian notation.
	 * For example, an array of [0, 35] will be converted into 
	 * the string "0023". The resulting string will have
	 * lowercase hexadecimal characters!
	 * 
	 * @param bytes array of bytes to be converted
	 * @return string of converted hexadecimal characters
	 */
	public static String byteToHex(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("Argument bytes cannot be null!");
		}
		
		if (bytes.length == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (byte currentByte : bytes) {
			sb.append(singleByteToHex(currentByte));
		}
		
		return sb.toString();
		
	}
	
	/**
	 * Converts a single byte into a string of two hexadecimal
	 * characters representing the byte's value, written in
	 * big-endian notation. The resulting string will have
	 * lowercase hexadecimal characters!
	 * 
	 * @param b byte to be converted
	 * @return string of two converted hexadecimal characters
	 */
	public static String singleByteToHex(byte b) {
		int upperByte = (int) (b & 0x0000_00F0) >> 4;
		int lowerByte = (int) b & 0x0000_000F;
		
		return String.format(
				"%s%s", 
				valueMap.get(upperByte),
				valueMap.get(lowerByte)
		);
	}
	
	/**
	 * Calculates the SHA-1 hash of the specified string.
	 * 
	 * @param password string to calculate password for
	 * @return string representation of the calculated hash
	 */
	public static String hashPassword(String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("sha-1");
		} catch (NoSuchAlgorithmException ignorable) { }	
		
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		
		return Util.byteToHex(hash);
	}
}


