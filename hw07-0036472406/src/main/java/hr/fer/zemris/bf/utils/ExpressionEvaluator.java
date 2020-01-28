package hr.fer.zemris.bf.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Represents a visitor implementing the NodeVisitor interface.
 * An instance of this class will go through an accepting
 * Node's structure, as well as the structure of its accompanying 
 * nodes, and evaluate the expression while plugging in the specified
 * boolean values for each appropriate variable.
 * 
 * <p>It is expected of the user to further program an instance
 * of this evaluator after its creation by specifying an array of boolean 
 * values which will correspond to the variables given during the
 * evaluator's creation. If no boolean values are specified, the evaluator
 * will use default false boolean values for each variable.
 * 
 * <p>If the evaluator encounters a variable in the visiting expression
 * which was not declared during the evaluator's creation, an
 * IllegalStateException is thrown. Likewise, an IllegalStateException
 * is thrown if a result is requested of the evaluator before the
 * evaluator has been sent to visit and evaluate an expression.
 * 
 * @author Vice Ivušić
 *
 */
public class ExpressionEvaluator implements NodeVisitor {

	/** array of boolean values that correspond to given variables */
	private boolean[] values;
	/** 
	 *  map with variable names mapped to the indexes
	 *  where they hold their corresponding values inside
	 *  the array of boolean values
	 */
	private Map<String, Integer> positions;
	/** helper data structure for resolving boolean expressions */
	private Stack<Boolean> stack = new Stack<>();
	
	/**
	 * Creates a new ExpressionEvaluator with the specified list
	 * of variable names. Each variable is mapped to a boolean value
	 * of false by default.
	 * 
	 * @param variables list of variable names
	 * @throws IllegalArgumentException if the specified list of variables is null
	 */
	public ExpressionEvaluator(List<String> variables) {
		if (variables == null) {
			throw new IllegalArgumentException("List of variable names cannot be null!");
		}
		
		int amountOfVariables = variables.size();
		positions = new HashMap<>(amountOfVariables);
		values = new boolean[amountOfVariables];
		
		for (int i = 0; i < amountOfVariables; i++) {
			// e.g. variable A at index 0 will have "A" mapped to integer 0
			positions.put(variables.get(i).toUpperCase(), i);
		}
	}
	
	/**
	 * Sets the boolean values for the corresponding variables
	 * to the specified array of boolean values. Each variable
	 * corresponds to the boolean value in the same order as
	 * it was defined; so for an ordered list of variables of B, A, and C,
	 * and an array of boolean values of false, true, false: B will
	 * correspond to false; A will correspond to true; and C will
	 * correspond to false.
	 * 
	 * <p>Automatically clears the last result of this evaluator.
	 * 
	 * @param values array of boolean values for the corresponding variables
	 * @throws IllegalArgumentException if the specified array of values is null
	 * 		   or if the number of boolean values isn't the same as the number
	 * 		   of declared variables
	 */
	public void setValues(boolean[] values) {
		if (values == null) {
			throw new IllegalArgumentException("Array of boolean values cannot be null!");
		}
		
		if (values.length != positions.size()) {
			throw new IllegalArgumentException(
					"Array of boolean values must be same size as number of variables!"
			);
		}
		
		start();
		this.values = Arrays.copyOf(values, values.length);
	}
	
	/**
	 * Retrieves the last calculated result of the evaluated
	 * boolean expression for the given set of boolean values.
	 * May be called multiple times without changing the result.
	 * 
	 * @return last calculated result of the evaluated expression
	 * @throws IllegalStateException if called before having sent
	 * 		   this evaluator to evaluate a boolean expression, or
	 * 		   if the last result was cleared by calling start()
	 */
	public boolean getResult() {
		if (stack.size() != 1) {
			throw new IllegalStateException(
					"Must send evaluator to evaluate expression first!"
			);
		}
		
		return stack.peek();
	}

	/**
	 * Prepares this evaluator for the next evaluation by
	 * clearing the results of the last evaluation.
	 */
	public void start() {
		stack.clear();
	}

	@Override
	public void visit(ConstantNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		stack.push(node.getValue());
	}

	/**
	 * @throws IllegalStateException if the visited node contains
	 * 		   an undeclared variable
	 */
	@Override
	public void visit(VariableNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		String variableName = node.getName();
		
		if (!positions.containsKey(variableName)) {
			throw new IllegalStateException("Variable "+variableName+"was undeclared!");
		}
		
		stack.push(values[positions.get(variableName)]);
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		node.accept(this);
		
		boolean operand = stack.pop();
		stack.push(node.getOperator().apply(operand));
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		node.getChildren().forEach(child -> child.accept(this));
		
		boolean oper1, oper2;
		for (int i = 0, n = node.getChildren().size()-1; i < n; i++) {
			oper1 = stack.pop();
			oper2 = stack.pop();
			stack.push(node.getOperator().apply(oper1, oper2));
		}
	}

}
