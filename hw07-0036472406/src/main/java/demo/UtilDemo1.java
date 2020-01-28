package demo;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.Util;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * Demo progam for checking the results of Task 6.
 * 
 * @author Marko Čupić
 *
 */
public class UtilDemo1 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		
		Node expression = new Parser("A and b or C").getExpression();
		
		VariablesGetter getter = new VariablesGetter();
		expression.accept(getter);
		
		List<String> variables = getter.getVariables();
		for(boolean[] values : Util.filterAssignments(variables, expression, true)) {
			System.out.println(Arrays.toString(values).replaceAll("true", "1").replaceAll("false", "0"));
		}
	}
	
}
