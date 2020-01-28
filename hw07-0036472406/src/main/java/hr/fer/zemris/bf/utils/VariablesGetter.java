package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Represents a visitor implementing the NodeVisitor interface.
 * An instance of this class will go through an accepting
 * Node's structure and gradually build a list of all the
 * unique variables it finds.
 * 
 * <p>Offers a method for retrieving a list of variable names contained
 * in the visited expression, sorted alphabetically (descending order).
 * 
 * @author Vice Ivušić
 *
 */
public class VariablesGetter implements NodeVisitor {

	/** accumulates and orders each unique variable */
	private Set<String> variables = new TreeSet<>();
	
	/**
	 * Retrieves a list of unique variable names pulled from
	 * the visited expression, sorted alphabetically. Returns
	 * an empty list if the expression contained no variables,
	 * or if called before having sent the visitor to an expression's
	 * Node structure.
	 * 
	 * @return list of variable names contained in visited expression,
	 * 		   sorted alphabetically (descending order)
	 */
	public List<String> getVariables() {
		return new ArrayList<>(variables);
	}
	
	@Override
	public void visit(ConstantNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		return;
	}

	@Override
	public void visit(VariableNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		variables.add(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		node.getChildren().forEach(child -> child.accept(this));
	}

	
}
