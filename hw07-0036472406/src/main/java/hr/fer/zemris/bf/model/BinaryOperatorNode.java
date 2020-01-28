package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Represents a binary logical operator, such as and, or and xor. Each
 * BinaryOperatorNode contains its operands and the boolean function that is
 * applied onto the operands.
 * 
 * @author Vice Ivušić
 *
 */
public class BinaryOperatorNode implements Node {

	/** name of this node */
	private String name;
	/** this node's operands */
	private List<Node> children;
	/** boolean function that is applied onto this node's children */
	private BinaryOperator<Boolean> operator;
	
	/**
	 * Creates a new BinaryOperatorNode with the specified parameters.
	 * 
	 * @param name this node's name
	 * @param children a list of this node's children i.e. operands
	 * @param operator a binary boolean function
	 * @throws IllegalArgumentException if any of the arguments is null or
	 * 		   if the specified list doesn't have at least two children nodes
	 */
	public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
		if (name == null || children == null || operator == null) {
			throw new IllegalArgumentException("None of the arguments may be null!");
		}
		
		if (children.size() < 2) {
			throw new IllegalArgumentException(
					"BinaryOperatorNode must have at least two children nodes!"
			);
		}
		
		this.name = name;
		this.children = children;
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
	 * Returns a list of this node's children nodes.
	 * 
	 * @return a list of this node's children nodes
	 */
	public List<Node> getChildren() {
		return children;
	}
	
	/**
	 * Returns this node's binary boolean function.
	 * 
	 * @return this node's binary boolean function
	 */
	public BinaryOperator<Boolean> getOperator() {
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
		StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		for (int i = 0, n = children.size(); i < n; i++) {
			if (i != n-1) {
				sb.append(String.format("%s %s ", children.get(i), name));
			} else {
				sb.append(String.format("%s", children.get(i)));
			}
		}
		sb.append(']');
		
		return sb.toString();
	}
}
