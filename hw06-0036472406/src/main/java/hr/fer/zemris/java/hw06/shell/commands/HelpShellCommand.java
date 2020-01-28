package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which prints helpful information about
 * the shell. Expects at most one argument: the name of a
 * command offered by the shell, in which case it prints out
 * the command's description and usage tips. If used with no
 * arguments, it prints out all the commands available in the shell.
 * 
 * @author Vice Ivušić
 *
 */
public class HelpShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new HelpShellCommand.
	 */
	public HelpShellCommand() {
		commandName = "help";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints MyShell's available commands. Optionally");
		commandDesc.add("takes an argument representing a command's name");
		commandDesc.add("and prints out additional information about that command.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    help");
		commandDesc.add("    help hexdump");
		commandDesc.add("    help help");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args;
		try {
			ShellParser parser = new ShellParser(arguments);
			args = parser.getArguments();
		} catch (ShellParserException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if (args.size() != 0 && args.size() != 1) {
			env.writeln(ShellUtil.invalidArgumentsMessage(commandName));
			return ShellStatus.CONTINUE;
		}
		
		if (args.size() == 0) {
			env.writeln("The following commands are available: ");
			
			for (ShellCommand command : env.commands().values()) {
				env.writeln("  " + command.getCommandName());
			}
			
			env.writeln(String.format(
					"%nFor additional information about each command, type: help [command]")
			);
			return ShellStatus.CONTINUE;
		}
		
		String commandNameToken = args.get(0);
		ShellCommand queriedCommand = env.commands().get(commandNameToken);
		if (queriedCommand == null) {
			env.writeln(
					"Command '"+commandNameToken+"' does not exist! "
					+ "Type 'help' for a list of all commands."
			);
			return ShellStatus.CONTINUE;
		}
		
		for (String line : queriedCommand.getCommandDescription()) {
			env.writeln(line);
		}
		
		return ShellStatus.CONTINUE;
	}

}
