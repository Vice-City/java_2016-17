package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.ShellUtil;
import hr.fer.zemris.java.hw06.shell.parser.ShellParser;
import hr.fer.zemris.java.hw06.shell.parser.ShellParserException;

/**
 * Represents a command which prints or changes a shell's special
 * print symbols. Expects at most two arguments. If its executeCommand
 * method receives only argument, it interprets it as a symbol identifier
 * (one of either PROMPT, MORELINES or MULTILINE) and prints out the 
 * currently used symbol for the identifier. If it receives a second argument
 * as well, it interprets it as the new symbol to be used for the specified
 * identifier.
 * 
 * @author Vice Ivušić
 *
 */
public class SymbolShellCommand extends AbstractShellCommand {

	/**
	 * Creates a new SymbolShellCommand.
	 */
	public SymbolShellCommand() {
		commandName = "symbol";
		commandDesc = new ArrayList<>();
		
		commandDesc.add("Prints or changes a shell's special print symbols.");
		commandDesc.add("Expects at most one argument.");
		commandDesc.add("");
		commandDesc.add("If called only with the symbol identifier argument");
		commandDesc.add("(one of either PROMPT, MORELINES or MULTILINE), the");
		commandDesc.add("currently used symbol for the specified identifier");
		commandDesc.add("is printed. If called with both an identifier and a");
		commandDesc.add("single character symbol, the specified symbol is set");
		commandDesc.add("to be used for the specified identifier.");
		commandDesc.add("");
		commandDesc.add("A few examples of correct usage:");
		commandDesc.add("    symbol PROMPT");
		commandDesc.add("    symbol MORELINES /");
		commandDesc.add("    symbol MULTILINE \"\\\"\"");
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
		
		Character newSymbol = null;
		if (args.size() == 2) {
			String newSymbolToken = args.get(1);
			
			if (newSymbolToken.length() != 1) {
				env.writeln("New symbol must be one character long!");
				return ShellStatus.CONTINUE;
			}
			
			newSymbol = newSymbolToken.charAt(0);
		}
		
		String symbolIdentifier = args.get(0);
		Character oldSymbol = null;
		switch (symbolIdentifier) {
		case "PROMPT":
			oldSymbol = env.getPromptSymbol();
			if (newSymbol != null) {
				env.setPromptSymbol(newSymbol);
			}
			break;
			
		case "MORELINES":
			oldSymbol = env.getMoreLinesSymbol();
			
			if (newSymbol != null) {
				if (newSymbol == '"') {
					env.writeln("Double quotes are reserved by MyShell"
							+ " for input purposes; sorry!");
					return ShellStatus.CONTINUE;
				}
				
				if (Character.isWhitespace(newSymbol)) {
					env.writeln("MORELINES symbol cannot be a whitespace character!");
					return ShellStatus.CONTINUE;
				}
				
				env.setMoreLinesSymbol(newSymbol);
			}
			
			break;
			
		case "MULTILINE":
			oldSymbol = env.getMultilineSymbol();
			if (newSymbol != null) {
				env.setMultilineSymbol(newSymbol);
			}
			break;
			
		default:
			env.writeln(
					"Only legal identifiers for command 'symbol' are PROMPT, MORELINES"
					+ " and MULTILINE! Case sensitive!"
			);
			return ShellStatus.CONTINUE;
		}
		
		if (newSymbol != null) {
			env.writeln(String.format(
					"Symbol for %s changed from '%s' to '%s'", 
					symbolIdentifier,
					oldSymbol,
					newSymbol)
			);
			
		} else {
			env.writeln(String.format(
					"Symbol for %s is '%s'", 
					symbolIdentifier, 
					oldSymbol)
			);
		}
		
		return ShellStatus.CONTINUE;
	}

}
