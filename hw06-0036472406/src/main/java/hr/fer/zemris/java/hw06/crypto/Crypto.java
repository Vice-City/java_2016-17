package hr.fer.zemris.java.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program which calculates the SHA-256 checksum of the specified
 * file, or encrypts/decrypts the specified file using AES algorithm.
 * 
 * <p>The program takes multiple arguments. The first argument is one of
 * the following self-explanatory commands: checksha, encrypt or decrypt.
 * If checksha is specified, the program takes an additional argument
 * representing the path to the file for which to calculate a message digest. 
 * If either encrypt or decrypt are specified, the program takes two
 * additional arguments: first argument is path to the file to encrypt
 * or decrypt, while the second argument is path to the file which has been
 * encrypted or decrypted.
 * 
 * <p>The program asks for additional parameters from the users, such
 * as the password and initialization vector.
 * 
 * <p>Some examples of proper usage:
 * <br><code>checksha hw06test.bin</code>
 * <br><code>encrypt hw06.pdf hw06crypted.bin</code>
 * <br><code>decrypt hw06test.bin hw06test.pdf</code>
 * @author Vice Ivušić
 *
 */
public class Crypto {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Expected a command!");
			return;
		}
		
		String command = args[0];
		
		switch (command) {
		case "checksha":
			if (args.length < 2) {
				System.out.println("Command checksha takes path to file!");
				return;
			}
			checkSHA(args[1]);
			break;
		case "encrypt":
			if (args.length < 3) {
				System.out.println("Command encrypt takes paths to input file and output file!");
				return;
			}
			encrypt(args[1], args[2]);
			break;
		case "decrypt":
			if (args.length < 3) {
				System.out.println("Command decrypt takes paths to input file and output file!");
				return;
			}
			decrypt(args[1], args[2]);
			break;
		default:
			System.out.println("Only valid commands are: checksha, encrypt and decrypt!");
			return;
		}
	}

	/**
	 * Helper method which delegates encryption to another method. Terminates
	 * the program in case of an error.
	 * 
	 * @param inputString path to file to be encrypted
	 * @param outputString path for encrypted file
	 */
	private static void encrypt(String inputString, String outputString) {
		encryptOrDecrypt(inputString, outputString, true);
		
	}

	/**
	 * Helper method which delegates decryption to another method. Terminates
	 * the program in case of an error.
	 * 
	 * @param inputString path to file to be decrypted
	 * @param outputString path for decrypted file
	 */
	private static void decrypt(String inputString, String outputString) {
		encryptOrDecrypt(inputString, outputString, false);
	}

	/**
	 * Helper method which encrypts or decrypts the input file, depending
	 * on the specified flag. Terminates the program in case of an error.
	 * 
	 * @param inputString path to file which is being encrypted or decrypted
	 * @param outputString path for encrypted or decrypted file
	 * @param isEncryptSet <b>true</b> is encryption is desired, <b>false</b> otherwise
	 */
	private static void encryptOrDecrypt(String inputString, String outputString, boolean isEncryptSet) {
		Path input = null;
		Path output = null;
		try {
			input = Paths.get(inputString);
			output = Paths.get(outputString);
		} catch (InvalidPathException ex) {
			System.out.println("Couldn't convert a path!");
			System.exit(-1);
		}
		
		Scanner sc = new Scanner(System.in);
		String keyText = inputArgument(
				"Please provide password for hex-encoded text (16 bytes, i.e. 32 hex-digits)",
				sc
		);
		String ivText = inputArgument(
				"Please provide initialization vector as hex-encoded text (32 hex-digits)", 
				sc
		);
		
		SecretKeySpec keySpec = new SecretKeySpec(Util.hexToByte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hexToByte(ivText));
		
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Specified algorithm does not exist!");
			System.exit(-1);
		} catch (NoSuchPaddingException e) {
			System.out.println("Specified padding does not exist!");
			System.exit(-1);
		}
		
		try {
			cipher.init(isEncryptSet ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (InvalidKeyException e) {
			System.out.println("Specified cipher key is invalid!");
			System.exit(-1);
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("Specified cipher algorithm is invalid!");
			System.exit(-1);
		}
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(input));
			 OutputStream os = new BufferedOutputStream(Files.newOutputStream(output))) {
				 
			byte[] buffer = new byte[4096];
			
			while (true) {
				int readBytes = is.read(buffer);
				
				if (readBytes == -1) {
					break;
				}
				
				// important to only send filled data!
				byte[] cipheredBytes = cipher.update(buffer, 0, readBytes);
				os.write(cipheredBytes);
			}
			
			try {
				os.write(cipher.doFinal());
			} catch (IllegalBlockSizeException e) {
				System.out.printf(
						"There was an error during %s!",
						isEncryptSet ? "encryption" : "decryption"
				);
				System.exit(-1);
			} catch (BadPaddingException e) {
				System.out.printf(
						"There was an error during %s!",
						isEncryptSet ? "encryption" : "decryption"
				);
				System.exit(-1);
			}
			
			os.flush();
					
		} catch (IOException e) {
			System.out.printf(
					"There was an error during %s!",
					isEncryptSet ? "encryption" : "decryption"
			);
			System.exit(-1);
		}
		
	}

	/**
	 * Helper method which calculates the SHA-256 message digest for the 
	 * specified file. Terminates the program in case of an error.
	 * 
	 * @param file path to file to calculate digest for
	 */
	private static void checkSHA(String file) {
		Path input = null;
		try {
			input = Paths.get(file);
		} catch (InvalidPathException ex) {
			System.out.println("Couldn't convert "+file+" to path!");
			System.exit(-1);
		}
		
		if (!Files.exists(input)) {
			System.out.println("Given input file does not exist!");
			System.exit(-1);
		}
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("sha-256");
		} catch (NoSuchAlgorithmException ignorable) { 
			System.exit(-1);
		}	
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(input))) {
			byte[] buffer = new byte[4096];
			
			while (true) {
				int readBytes = is.read(buffer);
				
				if (readBytes == -1) {
					break;
				}
				
				// important to only send filled data!
				digest.update(buffer, 0, readBytes);
			}
					
		} catch (IOException e) {
			System.out.println("There was an error during calculation of message digest!");
			System.exit(-1);
		}
		
		byte[] hash = digest.digest();
		
		String expectedHash = inputArgument(
				"Please provide expected sha-256 digest for " + input, 
				new Scanner(System.in)
		);
		
		boolean hashesAreEqual = Arrays.equals(hash, Util.hexToByte(expectedHash));
		
		if (hashesAreEqual) {
			System.out.println(
					"Digesting completed. Digest of "+input+" matches expected digest."
			);
		} else {
			System.out.printf("Digesting completed. Digest of %s does not match "
					+ "the expected digest. Digest was: %s%n",
					input,
					Util.byteToHex(hash)
			);
		}
		
	}
	
	/**
	 * Helper method for input of a user defined argument.
	 * 
	 * @param message informative message to be printed to standard output
	 * @param sc Scanner object with System.in as its parameter
	 * @return scanned argument as a String
	 */
	private static String inputArgument(String message, Scanner sc) {
		System.out.printf("%s:%n> ", message);
		
		return sc.next().trim();
	}
}
