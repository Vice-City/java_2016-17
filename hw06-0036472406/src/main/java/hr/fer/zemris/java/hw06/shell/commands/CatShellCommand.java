package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
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
 * Represents a command for printing files to a shell's
 * output. Expects at most two arguments for its executeCommand
 * method: a path to file and an optional argument specifying 
 * which charset to use while decoding the specified file.
 * 
 * <p>It is possible that the specified file cannot be interpreted
 * with the specified charset. It is also possible that, depending
 * on where the program is being run (such as inside Windows's
 * Command Prompt), the environment may be using a charset different
 * from the charset of the decoded file, which may result in
 * faulty characters being printed.
 * 
 * @author Vice Ivušić
 *
 */
public class CatShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new CatShellCommand.
	 */
	public CatShellCommand() {
		commandName = "cat";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints the specified file to the environment's output.");
		commandDesc.add("");
		commandDesc.add("First argument is path to file to be printed, while");
		commandDesc.add("the second argument is optional and specifies the");
		commandDesc.add("desired character set to interpret the file with.");
		commandDesc.add("It is possible that the specified file cannot be");
		commandDesc.add("interpreted with the specified charset.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    cat ./text.txt");
		commandDesc.add("    cat C:/text.txt UTF-8");
		commandDesc.add("    cat \"./hello world/helloworld.txt\"");
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
		
		if (args.size() != 1 && args.size() != 2) {
			env.writeln(ShellUtil.invalidArgumentsMessage(commandName));
			return ShellStatus.CONTINUE;
		}
		
		Charset charset;
		if (args.size() == 2) {
			String charsetToken = args.get(1);
			try {
				charset = Charset.forName(charsetToken);
				
			} catch (IllegalCharsetNameException ex) {
				env.writeln("Charset "+charsetToken+" does not exist!");
				return ShellStatus.CONTINUE;
				
			} catch (UnsupportedCharsetException ex) {
				env.writeln("Charset "+charsetToken+" is not supported on this JVM!");
				return ShellStatus.CONTINUE;
			}
			
		} else {
			charset = Charset.defaultCharset();
		}
		
		Path srcFile;
		String inputFileToken = args.get(0);
		try {
			srcFile = Paths.get(inputFileToken);
		} catch (InvalidPathException ex) {
			env.writeln(ShellUtil.pathConversionMessage(inputFileToken));
			return ShellStatus.CONTINUE;
		}
		
		if (!Files.exists(srcFile)) {
			env.writeln(ShellUtil.pathDoesntExistMessage(srcFile.toString()));
			return ShellStatus.CONTINUE;
		}
		
		if (Files.isDirectory(srcFile)) {
			env.writeln(ShellUtil.pathIsDirectoryMessage(srcFile.toString()));
			return ShellStatus.CONTINUE;
		}

		try (BufferedReader reader = Files.newBufferedReader(srcFile, charset)) {
			while (true) {
				String nextLine = reader.readLine();
				
				if (nextLine == null) break;
				
				env.writeln(nextLine);
			}
			
		} catch (IOException e) {
			env.writeln("Could not read specified file using charset "+charset+"!");
			return ShellStatus.CONTINUE;
		}	
		
		return ShellStatus.CONTINUE;
	}

}
