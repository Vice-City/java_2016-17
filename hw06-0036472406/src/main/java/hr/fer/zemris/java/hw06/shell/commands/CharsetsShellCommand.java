package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command for printing the available charsets
 * on the current Java platform. Takes no arguments.
 * 
 * @author Vice Ivušić
 *
 */
public class CharsetsShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new CharsetsShellCommand.
	 */
	public CharsetsShellCommand() {
		commandName = "charsets";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints names of supported charsets on current Java platform.");
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
		
		if (args.size() != 0) {
			env.writeln(ShellUtil.invalidArgumentsMessage(commandName));
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Supported charsets for this Java platform are as follows:");
		for (String charset : Charset.availableCharsets().keySet()) {
			env.writeln(charset);
		}
		
		return ShellStatus.CONTINUE;
	}

}
