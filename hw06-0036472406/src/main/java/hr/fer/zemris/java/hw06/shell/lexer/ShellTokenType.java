package hr.fer.zemris.java.hw06.shell.lexer;

/**
 * Enumerations of the type of tokens the text is composed of.
 * 
 * @author Vice Ivušić
 *
 */
public enum ShellTokenType {
	
	/** signals there are no more tokens to generate */
	EOL,
	/** represents an argument as a string of characters */
	ARGUMENT
}