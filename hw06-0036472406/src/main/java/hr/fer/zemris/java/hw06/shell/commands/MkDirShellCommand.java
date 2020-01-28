package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which creates a directory structure.
 * Any nonexistent directories are created along with the final
 * directory. Expects exactly one argument: desired directory structure.
 * 
 * @author Vice Ivušić
 *
 */
public class MkDirShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new MkDirShellCommand.
	 */
	public MkDirShellCommand() {
		commandName = "mkdir";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Creates the specified directory structure. Any");
		commandDesc.add("nonexistent directories will be created. Expects");
		commandDesc.add("exactly argument: desired directory structure.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    mkdir ./copies/");
		commandDesc.add("    mkdir C:/java/hr/fer/zemris/java/hw06/");
		commandDesc.add("    cat \"./hello world/\"");
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
		
		if (args.size() != 1) {
			env.writeln(ShellUtil.invalidArgumentsMessage(commandName));
			return ShellStatus.CONTINUE;
		}
		
		Path directory;
		String directoryToken = args.get(0);
		try {
			directory = Paths.get(directoryToken);
		} catch (InvalidPathException ex) {
			env.writeln(ShellUtil.pathConversionMessage(directoryToken));
			return ShellStatus.CONTINUE;
		}
		
		if (Files.exists(directory)) {
			env.writeln("Specified directory or file already exists!");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.createDirectories(directory);
		} catch (IOException e) {
			env.writeln("Could not create specified directory structure!");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Successfully created directory structure: "+directory.toAbsolutePath());
		return ShellStatus.CONTINUE;
	}

}
