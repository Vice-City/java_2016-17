package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which prints the structure of a
 * directory. Expects exactly one argument for its
 * executeCommand method: path to directory.
 * 
 * @author Vice Ivušić
 *
 */
public class TreeShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new TreeShellCommand.
	 */
	public TreeShellCommand() {
		commandName = "tree";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints the structure of the specified directory and");
		commandDesc.add("all of its subtrees. If a file couldn't be read, its");
		commandDesc.add("name will be enclosed in asterisks (*). Expects exactly");
		commandDesc.add("one argument: path to directory.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    tree ./");
		commandDesc.add("    tree C:/Users/");
		commandDesc.add("    tree \"./hello world/\"");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> args;
		try {
			args = new ShellParser(arguments).getArguments();
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
		
		if (!Files.exists(directory)) {
			env.writeln(ShellUtil.pathDoesntExistMessage(directory.toString()));
			return ShellStatus.CONTINUE;
		}
		
		if (!Files.isDirectory(directory)) {
			env.writeln(ShellUtil.pathIsntDirectoryMessage(directory.toString()));
			return ShellStatus.CONTINUE;
		}
		
		TreeWalker visitor = new TreeWalker();
		try {
			Files.walkFileTree(directory, visitor);
		} catch (IOException e) {
			env.writeln("Could not traverse directory structure!");
			return ShellStatus.CONTINUE;
		}
		
		env.write(visitor.getOutput());
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Helper class which implements the FileVisitor interface. Traverses
	 * a directory structure and builds the string representation of the
	 * structure.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class TreeWalker implements FileVisitor<Path> {

		/** current depth level */
		private int level;
		/** StringBuilder object used for building the string representation */
		private StringBuilder sb = new StringBuilder();
		
		/**
		 * Returns the built string representation of a directory structure.
		 * 
		 * @return string representation of a directory structure
		 */
		private String getOutput() {
			return sb.toString();
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			for (int i = 0; i < level; i++) {
				sb.append("  ");
			}
			sb.append(String.format("%s/%n", dir.getFileName()));
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			for (int i = 0; i < level; i++) {
				sb.append("  ");
			}
			sb.append(String.format("%s%n", file.getFileName()));
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			for (int i = 0; i < level; i++) {
				sb.append("  ");
			}
			sb.append(String.format("*%s*%n", file.getFileName()));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}
		
	}

}
