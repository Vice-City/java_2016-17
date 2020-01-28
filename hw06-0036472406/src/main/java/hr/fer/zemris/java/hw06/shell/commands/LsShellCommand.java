package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which prints out the content of a
 * directory. Expects only one argument: path to directory.
 * Prints information about the entry's readability, writability,
 * executability and whether it is a directory or file, in addition
 * to printing an entry's size (in bytes; only for files), its
 * date and time of creation and its name.
 * 
 * @author Vice Ivušić
 *
 */
public class LsShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new LsShellCommand.
	 */
	public LsShellCommand() {
		commandName = "ls";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints the content of a directory.");
		commandDesc.add("Expects only one argument: path to directory.");
		commandDesc.add("");
		commandDesc.add("In addition to the names of the entries, each entry contains");
		commandDesc.add("information in the first column about whether the entry");
		commandDesc.add("is a directory (d), is readable (r), is writable (w) and");
		commandDesc.add("if it is executable (x); if one of these isn't applicable");
		commandDesc.add("to the entry, a dash is printed instead (-).");
		commandDesc.add("");
		commandDesc.add("The size of the entry (in bytes) is also visible in the second");
		commandDesc.add("column, but only if the entry is a file. If it is a directory,");
		commandDesc.add("a dash is displayed instead.");
		commandDesc.add("");
		commandDesc.add("The third column holds information about the entry's date and");
		commandDesc.add("time of creation, while the fourth column states the entry's name.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    ls ./");
		commandDesc.add("    ls C:/java/");
		commandDesc.add("    ls \"./hello world/\"");
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
		
		List<Path> children;
		try {
			children = Files.list(directory).collect(Collectors.toList());
		} catch (IOException ex) {
			env.writeln("Could not read children files and directories of specified directory!");
			return ShellStatus.CONTINUE;
		}
		
		String output;
		try {
			output = generateOutput(children);
		} catch (IOException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		env.write(output);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Helper method which generates a string containing the whole
	 * output of the ls command.
	 * 
	 * @param children list of Path objects in current directory
	 * @return string containing the output of the ls command
	 * @throws IOException if an error occured while trying to determine
	 * 		   a file's size
	 */
	private static String generateOutput(List<Path> children) throws IOException {
		if (children.size() == 0) {
			return String.format(
					"Queried directory doesn't contain any files or directories.%n"
			);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (Path path : children) {
			generateSingleEntry(path, sb);
		}
		
		return sb.toString();
	}

	/**
	 * Helper method which fills the specified StringBuilder objects
	 * with a single line of the output.
	 * 
	 * @param path Path object of entry whose output is being built
	 * @param sb StringBuilder to fill with this method's strings
	 * @throws IOException if an error occured while trying to determine
	 * 		   a file's size
	 */
	private static void generateSingleEntry(Path path, StringBuilder sb) throws IOException {
		buildAttributes(path, sb);
		buildSize(path, sb);
		buildDateAndTime(path, sb);
		
		String entryName = path.getFileName().toString();
		sb.append(String.format("%s%n", entryName));
	}
	
	/**
	 * Helper method which builds the directory, writable, readable
	 * and executable flags for the current entry.
	 * 
	 * @param path Path object of entry whose output is being built
	 * @param sb StringBuilder to fill with this method's strings
	 */
	private static void buildAttributes(Path path, StringBuilder sb) {
		if (Files.isDirectory(path)) {
			sb.append('d');
		} else {
			sb.append('-');
		}
		
		if (Files.isReadable(path)) {
			sb.append('r');
		} else {
			sb.append('-');
		}
		
		if (Files.isWritable(path)) {
			sb.append('w');
		} else {
			sb.append('-');
		}
		
		if (Files.isExecutable(path)) {
			sb.append('x');
		} else {
			sb.append('-');
		}
		
		sb.append(String.format(" "));
	}
	
	/**
	 * Helper method which builds the size of the current entry
	 * in bytes.
	 * 
	 * @param path Path object of entry whose output is being built
	 * @param sb StringBuilder to fill with this method's strings
	 * @throws IOException if an error occured while trying to determine
	 * 		   a file's size
	 */
	private static void buildSize(Path path, StringBuilder sb) throws IOException {
		String size;
		try {
			/*
			 * I'm quite sure prof. Čupić mentioned in class not to have
			 * ls and similar commands recursively calculate the size of
			 * all of a directory's subtrees since it would take a while depending
			 * on the directory being queried (such as the root directory).
			 */
			if (Files.isDirectory(path)) {
				size = "-";
			} else {
				size = Long.toString(Files.size(path));
			}
			
		} catch (IOException e) {
			throw new IOException("Could not parse size of file!");
		}
		
		for (int i = 10; i > 0; i--) {
			if (i > size.length()) {
				sb.append(" ");
			}
		}
		
		sb.append(size + " ");
	} 
	
	/**
	 * Helper method which builds the date and creation time of 
	 * the current entry.
	 * 
	 * @param path Path object of entry whose output is being built
	 * @param sb StringBuilder to fill with this method's strings
	 */
	private static void buildDateAndTime(Path path, StringBuilder sb) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		BasicFileAttributeView faView = Files.getFileAttributeView(
				path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
		);
		
		BasicFileAttributes attributes;
		try {
			attributes = faView.readAttributes();
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to read file attributes!");
		}
		
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		sb.append(formattedDateTime + " ");
	}

}
