package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

/**
 * Represents a shell's environment. Offers methods for writing to 
 * standard output as well as reading from standard output. Offers
 * a method for retrieving a map of all commands offered by the
 * shell as well as methods for retrieving and setting special
 * symbols used by the shell. 
 * 
 * @author Vice Ivušić
 *
 */
public interface Environment {

	/**
	 * Reads a single line (a string terminated by the Environment's
	 * new line character) from standard input and returns it as a string.
	 * 
	 * @return String read from standard input
	 * @throws ShellIOException if reading fails
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes the specified string to standard output.
	 * 
	 * @param text string to be written to standard output
	 * @throws ShellIOException if writing fails
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes the specified string to standard output and prints
	 * a system-dependent new line character.
	 * 
	 * @param text string to be written to standard output
	 * @throws ShellIOException if writing fails
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * An unmodifiable map with ShellCommand objects mapped to the object's names,
	 * sorted descending by the names.
	 * 
	 * @return unmodifiable map with ShellCommand objects mapped to their names
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the currently set PROMPT symbol.
	 * 
	 * @return currently set PROMPT symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets this Environment's PROMPT symbol to the specified symbol.
	 * 
	 * @param symbol the desired symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns the currently set MORELINES symbol.
	 * 
	 * @return currently set MORELINES symbol
	 */
	Character getMoreLinesSymbol();
	
	/**
	 * Sets this Environment's MORELINES symbol to the specified symbol.
	 * 
	 * @param symbol the desired symbol
	 */
	void setMoreLinesSymbol(Character symbol);

	/**
	 * Returns the currently set MULTILINE symbol.
	 * 
	 * @return currently set MULTILINE symbol
	 */
	Character getMultilineSymbol();

	/**
	 * Sets this Environment's MULTILINE symbol to the specified symbol.
	 * 
	 * @param symbol the desired symbol
	 */
	void setMultilineSymbol(Character symbol);
}
