package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexDumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkDirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * A shell program. MyShell offers several commands to the user:
 * 
 * <p><ul>
 * <li><code>cat</code> - used for printing file content onto standard output.
 * <li><code>charsets</code> - lists all available charsets.
 * <li><code>copy</code> - used for copying files.
 * <li><code>exit</code> - exits MyShell.
 * <li><code>help</code> - used for retrieving additional info about each command.
 * <li><code>hexdump</code> - prints a the content of a file in hexadecimal numbers.
 * <li><code>ls</code> - used for printing the content of a directory.
 * <li><code>mkdir</code> - used for creating new directory structures.
 * <li><code>symbol</code> - used for changing the symbols used by MyShell.
 * <li><code>tree</code> - used for printing a directory's complete structure.
 * </ul><p>
 * 
 * Commands and arguments may be split into multiple lines by writing a multiline
 * symbol at the end of the line; a single backslash ( \ ) is used by default,
 * but it can be changed using the <code>symbol</code> command. For additional
 * information about each of the commands, type 'help [command]', for example:
 * <code>help hexdump</code>.
 * 
 * <p>Each command's arguments may be enclosed in double quotes ( " ). This is
 * useful when passing an argument containing spaces, such as a file name
 * or a path containing spaces. Inside double quotes, the following escape
 * mechanisms are valid: \\ is a single backslash, while \" is a single
 * (non-terminating) double quote. Note that writing a single backslash
 * followed by any other character is fine as well, so arguments such as
 * "C:\Users" will be interpreted correctly. Please be careful with ending
 * backslashes followed by double quotes, however, as \" will be interpreted as a 
 * single double quote; meaning you would have to type "C:\Users\\" for
 * the path to be interpreted correctly as a single argument.
 * 
 * @author Vice Ivušić
 *
 */
public class MyShell {
	
	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		/*
		 * Closing the reader and writer isn't important because
		 * the used resources are System.in and System.out which
		 * die when the program itself dies.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		Environment env = new MyShellEnvironment(reader, writer);

		env.writeln("Welcome to MyShell v1.0");
		while (true) {
			try {
				env.write(String.format("%s ", env.getPromptSymbol()));
			} catch (ShellIOException ex) {
				System.err.println("Critical error; exiting MyShell.");
				break;
			}
			
			String input = getCompleteInput(env);
			
			String commandName = extractCommandName(input);
			String arguments = extractArguments(input);
			
			ShellCommand command = env.commands().get(commandName);
			if (command == null) {
				env.writeln(
						"Command '"+commandName+"' does not exist! "
								+ "For a list of commands, please type: help"
				);
				continue;
			}
			
			ShellStatus status ;
			try {
				status = command.executeCommand(env, arguments);
			} catch (ShellIOException ex) {
				System.err.println("Critical error; exiting MyShell.");
				break;
			}
			
			if (status == ShellStatus.TERMINATE) break;
		}
		
	}
	
	/**
	 * Helper method which extracts the command name from a whole
	 * command, omitting all of the command's arguments.
	 * 
	 * @param input whole command from which to extract command name
	 * @return String containing only the command's name
	 */
	private static String extractCommandName(String input) {
		String[] tokens = input.split("\\s");
		return tokens[0];
	}

	/**
	 * Helper method which extracts arguments from a whole command,
	 * omitting the command name itself.
	 * 
	 * @param input whole command from which to extract arguments
	 * @return String containing only the arguments of a command
	 */
	private static String extractArguments(String input) {
		String[] tokens = input.split("\\s");
		tokens = Arrays.copyOfRange(tokens, 1, tokens.length);
		
		return String.join(" ", tokens);
	}

	/**
	 * Helper method which gets the next complete command from
	 * the environment's input. The returned String will not 
	 * contain any MULTILINE symbols that may have been
	 * used during input.
	 * 
	 * @param env reference to MyShell's environment
	 * @return String containing a whole command from input
	 */
	private static String getCompleteInput(Environment env) {
		StringBuilder sb = new StringBuilder();
		
		while (true) {
			// this replaceAll removes only trailing whitespaces
			String line = env.readLine().replaceAll("\\s+$", "");
			
			if (line.equals("")) break;
			
			Character lastChar = line.charAt(line.length()-1);
			if (lastChar.equals(env.getMoreLinesSymbol())) {
				String lineWithoutLastChar = line.substring(0, line.length()-1);
				sb.append(lineWithoutLastChar);
				
				env.write(String.format("%s ", env.getMultilineSymbol()));
				continue;
			}
			
			sb.append(line);
			break;
		}
		
		return sb.toString().trim();
	}

	/**
	 * Implementation of the Environment interface for the MyShell program.
	 * 
	 * @see Environment
	 * @author Vice Ivušić
	 *
	 */
	private static class MyShellEnvironment implements Environment {

		/** reader to be used for receiving input */
		private BufferedReader reader;
		/** writer to be used for printing output */
		private BufferedWriter writer;
		
		/** default MULTILINE symbol */
		private Character multilineSymbol = '|';
		/** default PROMPT symbol */
		private Character promptSymbol = '>';
		/** default MORELINES symbol */
		private Character moreLinesSymbol = '\\';
		
		/** map of ShellCommands mapped to their names */
		private static final SortedMap<String, ShellCommand> commandMap;
		
		static {
			commandMap = new TreeMap<>();
			
			commandMap.put("symbol", new SymbolShellCommand());
			commandMap.put("charsets", new CharsetsShellCommand());
			commandMap.put("cat", new CatShellCommand());
			commandMap.put("ls", new LsShellCommand());
			commandMap.put("tree", new TreeShellCommand());
			commandMap.put("copy", new CopyShellCommand());
			commandMap.put("mkdir", new MkDirShellCommand());
			commandMap.put("hexdump", new HexDumpShellCommand());
			commandMap.put("help", new HelpShellCommand());
			commandMap.put("exit", new ExitShellCommand());
		}
		
		/**
		 * Creates a new MyShellEnvironment with the specified reader and writer.
		 * 
		 * @param reader a BufferedReader for input
		 * @param writer a BufferedWriter for output
		 */
		private MyShellEnvironment(BufferedReader reader, BufferedWriter writer) {
			this.reader = reader;
			this.writer = writer;
		}
		
		@Override
		public String readLine() throws ShellIOException {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new ShellIOException("Could not read next line!");
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			try {
				writer.write(text);
				writer.flush();
			} catch (IOException e) {
				throw new ShellIOException("Could not write text: "+text);
			}
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			try {
				writer.write(text);
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				throw new ShellIOException("Could not write text: "+text);
			}
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return Collections.unmodifiableSortedMap(commandMap);
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multilineSymbol = symbol;
			
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			promptSymbol = symbol;
			
		}

		@Override
		public Character getMoreLinesSymbol() {
			return moreLinesSymbol;
		}

		@Override
		public void setMoreLinesSymbol(Character symbol) {
			moreLinesSymbol = symbol;
			
		}
		
	}
}
