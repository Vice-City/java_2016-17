package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

// I asked the professor it's OK to make an abstract class implementing the interface

/**
 * Implements the ShellCommand interface and offers default implementations
 * of methods for retrieving the command's name and description.
 * 
 * @author Vice Ivušić
 *
 */
public abstract class AbstractShellCommand implements ShellCommand {

	/** current command's name */
	protected String commandName;
	/** current command's description as a list of string lines */
	protected List<String> commandDesc;
	
	@Override
	public abstract ShellStatus executeCommand(Environment env, String arguments);

	@Override
	public String getCommandName() {
		return commandName;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(commandDesc);
	}

}
