package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
 * Represents a command for copying a file's content. Expects exactly
 * two arguments for its executeCommand method: a path to the file to
 * be copied, and the name and path for the copied file. The path for the
 * copied file may be a directory, in which case the file is copied to the
 * specified directory with its current name. The command also checks whether
 * a file with the specified name already exists in the specified directory,
 * in which case it prompts the user to either skip copying or overwrite
 * the existing file.
 * 
 * @author Vice Ivušić
 *
 */
public class CopyShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new CopyShellCommand.
	 */
	public CopyShellCommand() {
		commandName = "copy";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Copies the specified file to the specified file path.");
		commandDesc.add("If the specified path is a directory, the file is");
		commandDesc.add("copied into the specified directory with its current");
		commandDesc.add("name. Otherwise, the file is copied with the specified");
		commandDesc.add("name. If the specified file already exists, the program");
		commandDesc.add("asks the user whether they would like to overwrite or not.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    copy ./text.txt ./copies");
		commandDesc.add("    copy C:/text.txt C:/textCopy.txt");
		commandDesc.add("    copy ./text.txt \"./hello world/text.txt\"");
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
		
		if (args.size() != 2) {
			env.writeln(ShellUtil.invalidArgumentsMessage(commandName));
			return ShellStatus.CONTINUE;
		}
		
		Path srcFile;
		String srcFileToken = args.get(0);
		try {
			srcFile = Paths.get(srcFileToken);
		} catch (InvalidPathException ex) {
			env.writeln(ShellUtil.pathConversionMessage(srcFileToken));
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
		
		Path dstPath;
		String dstPathToken = args.get(1);
		try {
			dstPath = Paths.get(dstPathToken);
		} catch (InvalidPathException ex) {
			env.writeln(ShellUtil.pathConversionMessage(dstPathToken));
			return ShellStatus.CONTINUE;
		}
		
		if (Files.isDirectory(dstPath)) {
			// the "/" ensures correct copying if user gives path without ending slash
			dstPath = Paths.get(dstPath + "/" + srcFile.getFileName());
		}
		
		if (Files.exists(dstPath)) {
			env.writeln("File with same name already exists in specified directory. Overwrite? (Y/N)");
			
			String confirmation;
			while (true) {
				confirmation = env.readLine().trim();
				
				if (confirmation.toUpperCase().equals("Y")) {
					break;
				}
				
				if (confirmation.toUpperCase().equals("N")) {
					env.writeln("File wasn't copied.");
					return ShellStatus.CONTINUE;
				}
				
				env.writeln("Only valid answers are 'Y' and 'N'! Overwrite? (Y/N)");
			}
		}
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(srcFile));
			 OutputStream os = new BufferedOutputStream(Files.newOutputStream(dstPath))) {
			byte[] buffer = new byte[4096];
			
			while (true) {
				int readBytes = is.read(buffer);
				
				if (readBytes == -1) break;
				
				os.write(buffer, 0, readBytes);
			}
			os.flush();
			
		} catch (IOException e) {
			env.writeln("Could not copy specified file!");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Successfully copied "+srcFile.getFileName()+" to: "+dstPath+".");
		return ShellStatus.CONTINUE;
	}

}
