package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * Represents a unary logical operator, such as not. 
 * Each UnaryOperatorNode contains the operand and boolean function
 * that is applied onto the operand.
 * 
 * @author Vice Ivušić
 *
 */
public class UnaryOperatorNode implements Node {

	/** this node's name */
	private String name;
	/** this node's operand */
	private Node child;
	/** boolean function that is applied onto this node's child */
	private UnaryOperator<Boolean> operator;
	
	/**
	 * Creates a new UnaryOperatorNode with the specified parameters.
	 * 
	 * @param name this node's name
	 * @param child this node's child i.e. operand
	 * @param operator a binary boolean function
	 * @throws IllegalArgumentException if any of the specified arguments is null
	 */
	public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
		if (name == null || child == null || operator == null) {
			throw new IllegalArgumentException("None of the arguments may be null!");
		}
		
		this.name = name;
		this.child = child;
		this.operator = operator;
	}
	
	/**
	 * Returns this node's name.
	 * 
	 * @return this node's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns this node's child node.
	 * 
	 * @return this node's child node
	 */
	public Node getChild() {
		return child;
	}
	
	/**
	 * Returns this node's unary boolean function.
	 * 
	 * @return this node's unary boolean function
	 */
	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}
	
	@Override
	public void accept(NodeVisitor visitor) {
		if (visitor == null) {
			throw new IllegalArgumentException("Argument visitor cannot be null!");
		}
		
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return String.format("(not %s)", child);
	}
}
