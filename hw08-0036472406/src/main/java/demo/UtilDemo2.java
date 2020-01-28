package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * Demo progam for checking the results of Task 7.
 * 
 * @author Marko Čupić
 *
 */
public class UtilDemo2 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		
		Node expression = new Parser("NOT A AND NOT B AND (NOT C OR D) OR A AND C").getExpression();
		
		VariablesGetter getter = new VariablesGetter();
		expression.accept(getter);
		
		List<String> variables = getter.getVariables();
		System.out.println("Mintermi f("+variables+"): " + Util.toSumOfMinterms(variables, expression));
		
		List<String> variables2 = new ArrayList<>(variables);
		Collections.reverse(variables2);
		System.out.println("Mintermi f("+variables2+"): " + Util.toSumOfMinterms(variables2, expression));
	}
	
}
