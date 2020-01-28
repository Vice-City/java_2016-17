package demo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.parser.ParserException;
import hr.fer.zemris.bf.qmc.Minimizer;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * A program for minimizing boolean functions using the Quine-McCluskey
 * algorithm. Each function has to specify its variables and the set of
 * minterms and, optionally, don't care products. Each set can be specified
 * either directly (minterm indexes in brackets) or as another boolean expression.
 * 
 * <p>The program is terminated by entering: {@code quit}
 * 
 * <p>Examples of valid input:
 * <code><br>f(A,B,C,D) = NOT A AND NOT B AND (NOT C OR D) OR A AND C | [4,6]
 * <br>f(A,B,C,D) = NOT A AND NOT B AND (NOT C OR D) OR A AND C
 * <br>f(A,B,C) = [0, 4, 6] |  A AND B AND NOT C</code>
 * 
 * @author Vice Ivušić
 *
 */
public class QMC {
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.print("> ");
			String input = sc.nextLine().trim();
			
			if (input.equals("quit")) {
				break;
			}
			
			if (!input.contains("=")) {
				System.out.println(functionError());
				continue;
			}
			
			List<String> variables;
			Set<Integer> mintermSet;
			Set<Integer> dontCareSet;
			try {
				variables = parseLeftHandSide(input);
				mintermSet = parseMinterms(input, variables);
				dontCareSet = parseDontCares(input, variables);
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
				continue;
			}
			
			if (mintermSet.removeAll(dontCareSet) == true) {
				System.out.println(
					"Error: minterm set and don't care set are not disjunctive."
				);
				continue;
			}
			
			Minimizer minimizer = new Minimizer(mintermSet, dontCareSet, variables);
			
			List<String> minimalForms = minimizer.getMinimalFormsAsString();
			
			for (int i = 0, n = minimalForms.size(); i < n; i++) {
				System.out.printf("%d. %s%n", i+1, minimalForms.get(i));
			}
		}
		
		sc.close();
	}

	/**
	 * Helper method for parsing the variables of a boolean function.
	 * Expects the input to at least have an equals sign somewhere in it.
	 * 
	 * @param input the user's whole input string
	 * @return list of parsed variables
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static List<String> parseLeftHandSide(String input) {
		String leftHandSide = input.split("=")[0].trim();
		
		Pattern bracketPattern = Pattern.compile(
			"\\s*[a-zA-Z][a-zA-Z0-9]*\\s*\\((.*)\\)\\s*"
		);
		Matcher matcher = bracketPattern.matcher(leftHandSide);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException(functionError());
		}
		
		return parseVariables(matcher.group(1));
	}

	/**
	 * Helper method for parsing comma separated variables.
	 * 
	 * @param bracketContent comma separated variables
	 * @return list of parsed variables
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static List<String> parseVariables(String bracketContent) {
		List<String> variables = new LinkedList<>();
		
		Pattern pattern = Pattern.compile(
			"\\s*([a-zA-Z][a-zA-Z0-9]*)\\s*,{0,1}\\s*"
		);
		Matcher matcher = pattern.matcher(bracketContent);
		
		while (!matcher.hitEnd()) {
			if (!matcher.find()) {
				throw new IllegalArgumentException(functionError());
			}
			
			String token = matcher.group(1).toUpperCase();
			variables.add(token);
		}
		
		if (variables.isEmpty()) {
			throw new IllegalArgumentException("Expected at least one variable!");
		}
		
		return variables;
	}

	/**
	 * Helper method for parsing a set of minterms from the specified
	 * input and for the specified variables.
	 * 
	 * @param input the user's whole input string
	 * @param variables list of parsed variables
	 * @return set of parsed minterm indexes
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static Set<Integer> parseMinterms(String input, List<String> variables) {
		String[] tokens = input.split("=");
		
		String rightHandSide = tokens[1].trim();
		if (rightHandSide.contains("|")) {
			rightHandSide = rightHandSide.split("\\|")[0].trim();
		}
		
		return rightHandSide.charAt(0) == '['
			   ? parseDirect(rightHandSide, variables)
			   : parseExpression(rightHandSide, variables)
		;
	}

	/**
	 * Helper method for parsing a set of minterms from the specified
	 * input in bracketed form and for the specified variables.
	 * 
	 * @param rightHandSide input to the right of the equals sign
	 * @param variables list of parsed variables
	 * @return set of parsed minterm indexes
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static Set<Integer> parseDirect(String rightHandSide, List<String> variables) {
		if (rightHandSide.charAt(0) != '[' || 
			rightHandSide.charAt(rightHandSide.length()-1) != ']') {
			throw new IllegalArgumentException("Minterms must be surrounded by brackets!");
			
		}
		// gets rid of the brackets
		rightHandSide = rightHandSide.substring(1, rightHandSide.length()-1);
		
		TreeSet<Integer> minterms = new TreeSet<>();
		
		Pattern pattern = Pattern.compile("\\s*([0-9]*)\\s*,{0,1}\\s*");
		Matcher matcher = pattern.matcher(rightHandSide);
		
		while (!matcher.hitEnd()) {
			if (!matcher.find()) {
				throw new IllegalArgumentException("Invalid minterms!");
			}
			
			String token = matcher.group(1);
			if (token.equals("")) {
				continue;
			}
			
			Integer minterm = Integer.parseInt(token);
			minterms.add(minterm);
		}
		
		if (minterms.size() == 0) {
			return minterms;
		}
		
		if (minterms.last() >= (1 << variables.size())) {
			throw new IllegalArgumentException("Minterm is too large for given variables!");
		}
		
		return minterms;
	}

	/**
	 * Helper method for parsing a set of minterms from the specified
	 * input in expression form and for the specified variables.
	 * 
	 * @param rightHandSide input to the right of the equals sign
	 * @param variables list of parsed variables
	 * @return set of parsed minterm indexes
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static Set<Integer> parseExpression(String rightHandSide, List<String> variables) {
		Node expression;
		try {
			Parser parser = new Parser(rightHandSide);
			expression = parser.getExpression();
		} catch (ParserException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		
		VariablesGetter getter = new VariablesGetter();
		expression.accept(getter);
		List<String> expressionVariables = getter.getVariables();
		
		if (expressionVariables.size() > variables.size()) {
			throw new IllegalArgumentException("Incompatible variables!");
		}
		
		for (String exprVar : expressionVariables) {
			if (!variables.contains(exprVar)) {
				throw new IllegalArgumentException("Incompatible variables!");
			}
		}
		
		return Util.toSumOfMinterms(variables, expression);
	}

	/**
	 * Helper method for parsing a set of don't cares from the specified
	 * input and for the specified variables.
	 * 
	 * @param input the user's whole input string
	 * @param variables list of parsed variables
	 * @return set of parsed don't care product indexes
	 * @throws IllegalArgumentException if an error occurs during parsing
	 */
	private static Set<Integer> parseDontCares(String input, List<String> variables) {
		String[] tokens = input.split("=");
		
		String rightHandSide = tokens[1].trim();
		if (!rightHandSide.contains("|")) {
			return Collections.emptySet();
		}
		
		rightHandSide = rightHandSide.split("\\|")[1].trim();
		
		return rightHandSide.charAt(0) == '['
			   ? parseDirect(rightHandSide, variables)
			   : parseExpression(rightHandSide, variables)
		;
	}

	/**
	 * Helper method which returns a predefined general parsing error message.
	 * 
	 * @return general parsing error message
	 */
	private static String functionError() {
		return String.format(
			"%s%n%n%s%n%s%n%s",
			"ERROR! Function must be specified as NAME(VARIABLES) = MINTERMS | DONTCARES",
			"NAME must start with a letter and can contain more letters or numbers.",
			"VARIABLES must be comma separated and start with a letter, e.g. (A,B,C).",
			"Both MINTERMS and DONTCARES can be written as either a boolean expression"
			+ " or in bracketed form listing minterm indexes, e.g. [0, 3]."
		);
	}

}
