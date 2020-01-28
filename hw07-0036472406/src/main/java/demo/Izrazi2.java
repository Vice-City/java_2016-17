package demo;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.parser.ParserException;
import hr.fer.zemris.bf.utils.ExpressionTreePrinter;

/**
 * Demo progam for checking the results of Task 2.
 * 
 * @author Marko Čupić
 *
 */
public class Izrazi2 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		String[] expressions = new String[] {
			"0",
			"tRue",
			"Not a",
			"A aNd b",
			"a or b",
			"a xoR b",
			"A and b * c",
			"a or b or c",
			"a xor b :+: c",
			"not not a",
			"a or b xor c and d",
			"a or b xor c or d",
			"a xor b or c xor d",
			"(a + b) xor (c or d)",
			"(d or b) xor not (a or c)",
			"(c or d) mor not (a or b)",
			"not a not b",
			"a and (b or",
			"a and (b or c",
			"a and 10"
		};
		
		for(String expr : expressions) {
			System.out.println("==================================");
			System.out.println("Izraz: " + expr);
			System.out.println("==================================");
			
			try {
				System.out.println("Stablo:");
				Parser parser = new Parser(expr);
				parser.getExpression().accept(new ExpressionTreePrinter());
			} catch(ParserException ex) {
				System.out.println("Iznimka: " + ex.getClass()+" - " + ex.getMessage());
			}
			System.out.println();
		}
	}
}
