package hr.fer.zemris.java.hw16.search;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import hr.fer.zemris.java.hw16.search.DocumentSearchEngine.SearchResults;

/**
 * A program that functions as a simple search engine. It goes through
 * text files on the hard drive and builds a vocabulary of words contained
 * within those text files. The user can then query search terms, while the
 * program finds the closest matching files to the given search terms and
 * prints them to standard output.
 * 
 * <p>The available commands are:

 * <p>{@code query [search terms]} - performs a search using the given search terms. Search
 * terms are separated by spaces. It is possible for a query to result with an 
 * empty query if the none of the search terms are within the defined vocabulary.
 * <br>- example use: {@code query hrvatska dubrovnik}
 * 
 * <p>{@code type [index]} - prints the content of the text file with the given index to 
 * standard output. A query must first be performed before using this command. Valid 
 * indexes are those as output by the last result.
 * <br>- example use: {@code type 0}
 * 
 * <p>{@code results} - prints the search result of the last performed query. A query must 
 * first be performed before using the command.
 * 
 * <p>{@code exit} - exits the program.
 * 
 * The program takes a single argument: path to directory with text files to index and search.
 * This program also expects a text file with stop words defined in each single line to be
 * found in its resources directory.
 * 
 * @author Vice Ivušić
 *
 */
public class Console {

	/**
	 * The last retrieved search results. May be null if no query has yet been performed.
	 */
	private static SearchResults results;
	
	/**
	 * The search engine used for querying search terms.
	 */
	private static DocumentSearchEngine engine;
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; expects one argument: path to directory with text files
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Expected path to directory with text documents! Try typing 'clanci'.");
			return;
		}
		
		try {
			checkFiles(args[0], "src/main/resources/hrvatski_stoprijeci.txt");
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		
		try {
			engine = new DocumentSearchEngine(args[0], "src/main/resources/hrvatski_stoprijeci.txt");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		
		System.out.println("Vocabulary size is "+engine.getVocabularySize()+".");
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("\nEnter command > ");
			
			String inputString = sc.nextLine().toLowerCase().trim();
			if (inputString.equals("exit") || inputString.equals("quit")) {
				System.out.println("Goodbye!");
				break;
			}
			
			String[] tokens = inputString.split("\\s");
			
			if (tokens[0].equals("query")) {
				if (tokens.length < 2) {
					System.out.println("Command 'query' must take at least one argument!");
					continue;
				}
				
				inputString = inputString.replaceFirst("query(.*)", "$1").trim();
				try {
					processQuery(inputString);
				} catch (RuntimeException ex) {
					System.out.println(ex.getMessage());
				}
				
				continue;
			}
			
			if (tokens[0].equals("type")) {
				if (tokens.length != 2) {
					System.out.println("Command 'type' must take one argument!");
					continue;
				}
				
				inputString = inputString.replaceFirst("type(.*)", "$1").trim();
				try {
					processType(inputString);
				} catch (RuntimeException ex) {
					System.out.println(ex.getMessage());
				}
				
				continue;
			}
			
			if (tokens[0].equals("results")) {
				try {
					processResults();
				} catch (RuntimeException ex) {
					System.out.println(ex.getMessage());
				}
				
				continue;
			}
			
			System.out.println("Unrecognized command! Valid commands are 'query', 'type', 'results' and 'exit'.");
		}
		
		sc.close();
	}

	/**
	 * Helper method for the 'query' command.
	 * 
	 * @param inputString input string containing only the arguments
	 */
	private static void processQuery(String inputString) {
		String[] tokens = inputString.split("\\s");
		
		results = engine.search(tokens);
		System.out.println("Query is: "+results.getQuery());
		
		if (results.count() == 0) {
			System.out.println("Search resulted with no similar documents!");
			return;
		}
		
		System.out.println("Best "+results.count()+" results:");
	
		
		processResults();
	}

	/**
	 * Helper method for the 'type' command.
	 * 
	 * @param inputString input string containing only the arguments
	 */
	private static void processType(String inputString) {
		if (results == null) {
			throw new RuntimeException("Must successfully call 'query' before calling 'type'!");
		}
		
		if (results.count() == 0) {
			System.out.println("No search results were found! Make another query.");
			return;
		}
		
		int index;
		try {
			index = Integer.parseInt(inputString);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Argument "+inputString+" is not a non-negative integer!");
		}
		
		if (index < 0 || index > results.count()-1) {
			throw new RuntimeException("Index must be between 0 and "+(results.count()-1)+" (inclusive)!");
		}
		
		String docPath = results.getDocumentPath(index).toString();
		System.out.println("Document: "+docPath);
		for (int i = 0, n = docPath.length(); i < n; i++) {
			System.out.print("-");
		}
		System.out.println();
		
		String documentText;
		try {
			documentText = new String(
				Files.readAllBytes(results.getDocumentPath(index)),
				StandardCharsets.UTF_8
			);
		} catch (IOException ex) {
			documentText = "ERROR! Could not load document text!";
		}
		System.out.println(documentText);
		
		for (int i = 0, n = docPath.length(); i < n; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	/**
	 * Helper method for the 'results' command.
	 */
	private static void processResults() {
		if (results == null) {
			throw new RuntimeException("Must successfully call 'query' before calling 'results'!");
		}
		
		if (results.count() == 0) {
			System.out.println("No search results were found! Make another query.");
			return;
		}
		
		for (int i = 0, n = results.count(); i < n; i++) {
			System.out.printf("[%2d] (%.4f) %s%n", i, results.getSimilarityFactor(i), results.getDocumentPath(i));
		}
		
	}

	/**
	 * Helper method for checking the availability of the directory with text files 
	 * and the stop words definition file.
	 * 
	 * @param directoryString path to directory with text files
	 * @param stopWordsString path to stop words definition file
	 * @throws RuntimeException if any of the checks fails
	 */
	private static void checkFiles(String directoryString, String stopWordsString) {
		Path directoryPath;
		try {
			directoryPath = Paths.get(directoryString);
		} catch (InvalidPathException ex) {
			throw new RuntimeException(directoryString + " is not a valid path!");
		}
		
		if (Files.notExists(directoryPath)) {
			throw new RuntimeException(directoryString + " does not exist!");
		}
		
		if (!Files.isDirectory(directoryPath)) {
			throw new RuntimeException(directoryString + " is not a directory!");
		}		
		
		Path stopWordspath;
		try {
			stopWordspath = Paths.get(stopWordsString);
		} catch (InvalidPathException ex) {
			throw new RuntimeException(stopWordsString + " is not a valid path!");
		}
		
		if (Files.notExists(stopWordspath)) {
			throw new RuntimeException(stopWordsString + " does not exist!");
		}
		
		if (Files.isDirectory(stopWordspath)) {
			throw new RuntimeException(stopWordsString + " is a directory!");
		}		
		
		if (!Files.isReadable(stopWordspath)) {
			throw new RuntimeException(stopWordsString + " cannot be read!");
		}
	}
		
}
