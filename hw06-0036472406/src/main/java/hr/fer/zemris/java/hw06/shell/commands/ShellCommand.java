package hr.fer.zemris.java.hw06.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * An interface modeling a shell command. Every command must have
 * a method which implements the main function of the command and
 * interacts with the specified shell environment, as well as parsing
 * the specified arguments according to the command's own rules.
 * Each command must also implement methods for retrieving the command's
 * name and description.
 * 
 * @author Vice Ivušić
 *
 */
public interface ShellCommand {
	
	/**
	 * Method implementing the command's main functionality. This method
	 * is expected to process all exceptions which may occur within it;
	 * it is not allowed to propagate those exceptions back to the callee,
	 * except for {@link ShellIOException}s which may be propagated back.
	 * 
	 * @param env the shell environment this command is working with
	 * @param arguments a string of unparsed arguments
	 * @return a ShellStatus flag signaling whether the callee shell should
	 * 		   terminate or continue its work
	 * @throws ShellIOException if an error occurs during reading and writing
	 * 		   in the shell environment
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns this command's name.
	 * @return this command's name
	 */
	String getCommandName();
	
	/**
	 * Returns a list of string lines containing this command's description.
	 * @return list of strings containing this command's description
	 */
	List<String> getCommandDescription();
}
