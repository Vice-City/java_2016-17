package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Represents a visitor implementing the NodeVisitor interface.
 * An instance of this class will go through an accepting
 * Node's structure, as well as the structure of its accompanying 
 * nodes, and will print to standard output the Node's structure while
 * keeping in mind proper indentation levels. Boolean constants
 * are represented as ones (true) and zeros (false).
 * 
 * @author Vice Ivušić
 *
 */
public class ExpressionTreePrinter implements NodeVisitor {

	/** current indendation level */
	private int level;
	
	/**
	 * Indents the output by two spaces for each indentation level.
	 */
	private void indent() {
		for (int i = 0; i < level; i++) {
			System.out.print("  ");
		}
	};
	
	@Override
	public void visit(ConstantNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		indent();
		System.out.println(node.getValue() ? "1" : "0");
	}

	@Override
	public void visit(VariableNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		indent();
		System.out.println(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		indent();
		System.out.println(node.getName());
		
		level++;
		node.getChild().accept(this);
		level--;
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null!");
		}
		
		indent();
		System.out.println(node.getName());
		
		level++;
		node.getChildren().forEach(child -> child.accept(this));
		level--;
	}

	
}
