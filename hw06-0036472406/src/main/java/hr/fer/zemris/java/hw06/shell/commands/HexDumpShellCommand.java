package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which prints the hexadecimal representation
 * of a file's content. Expects exactly one argument: path to file.
 * A normal subset of bytes will be represented with the byte's ASCII
 * representation, while bytes which cannot be interpreted by ASCII
 * will be printed as a single dot.
 * 
 * @author Vice Ivušić
 *
 */
public class HexDumpShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new HexDumpShellCommand.
	 */
	public HexDumpShellCommand() {
		commandName = "hexdump";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints a hexadecimal representation of the specified file.");
		commandDesc.add("Expects only one argument: path to file. A normal subset of");
		commandDesc.add("bytes will be represented with its ASCII representation,");
		commandDesc.add("while bytes with no representation will be printed as a single dot.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    hexdump ./text.txt");
		commandDesc.add("    hexdump \"./hello world/helloworld.txt\"");
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
		
		String output;
		try {
			output = generateOutput(srcFile);
		} catch (IOException ex) {
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		env.write(output);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Helper method which takes path to a file and builds a string
	 * representing the specified file's hexadecimal content.
	 * 
	 * @param file Path to file
	 * @return String representing the file's hexadecimal dontent
	 * @throws IOException if the content of the file could not be read
	 */
	private static String generateOutput(Path file) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(file))) {
			byte[] buffer = new byte[16];
			int lineCounter = 0;
			
			while (true) {
				int readBytes = is.read(buffer);
				
				if (readBytes == -1) break;
				
				buildSingleLine(buffer, readBytes, lineCounter, sb);
				
				// counter has to count hexadecimally!
				lineCounter += 16;
			}
			
		} catch (IOException e) {
			throw new IOException("Could not build hex representation!");
		}

		return sb.toString();
	}

	/**
	 * Helper method which builds a single line of the output by
	 * filling the specified StringBuilder object's buffer. The
	 * hexadecimal characters are parsed from the specified array
	 * of bytes, up to the specified amount of read bytes.
	 * 
	 * @param buffer array with the bytes to be parsed
	 * @param readBytes amount of bytes to be parsed from the specified buffer
	 * @param lineCounter current line being built
	 * @param sb StringBuilder object to be filled with the method's strings
	 */
	private static void buildSingleLine(byte[] buffer, int readBytes, int lineCounter, StringBuilder sb) {
		// "%08X" interprets integers as hexadecimals and prints with 8 leading zeros
		sb.append(String.format("%08X: ", lineCounter));
		
		for (int i = 0; i < buffer.length; i++) {
			// takes care of empty slots and the middle separator among empty slots
			if (i >= readBytes) {
				if (i == 7) {
					sb.append("  |");
				} else {
					sb.append("   ");
				}
				continue;
			}
			
			sb.append(Util.singleByteToHex(buffer[i]).toUpperCase());
			
			// adds padding between the hexadecimal characters
			if (i == 7) {
				sb.append('|');
			} else {
				sb.append(' ');
			}
		}
		
		sb.append("| ");
		byte b;
		for (int i = 0; i < readBytes; i++) {
			b = buffer[i];
			
			// byte is never greater than 127
			if (b < (byte) 32) {
				sb.append('.');
				
			} else {
				// casting byte into char to get its ASCII representation
				sb.append((char) b);
			}
		}
		
		sb.append(String.format("%n"));
	}

}
