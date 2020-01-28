package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple utility class, with a method for loading a file into memory.
 * 
 * @author Vice Ivušić
 *
 */
public class DemoUtilities {

	/**
	 * Loads the file specified in the given path and returns its
	 * contents as a String, using UTF-8 encoding. Returns null
	 * if an error occurs during loading.
	 * 
	 * @param path string representing the path to file to be loaded
	 * @return String content of the specified file, or null if it 
	 * 		   could not be loaded
	 */
	public static String readFromDisk(String path) {
		// may throw invalid path exception
		Path filePath;
		try {
			filePath = Paths.get(path);
		} catch (InvalidPathException ex) {
			return null;
		}
		
		String text;
		try {
			text = new String(
				Files.readAllBytes(filePath),
				StandardCharsets.UTF_8
			);
		} catch (IOException ex) {
			return null;
		}
		
		return text;
	}
}
