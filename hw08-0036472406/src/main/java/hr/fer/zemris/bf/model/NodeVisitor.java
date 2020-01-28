package hr.fer.zemris.bf.model;

/**
 * Represents a Node visitor. Each class implementing this interface
 * must be able to handle each type of Node implementing the Node
 * interface.
 * 
 * @author Vice Ivušić
 *
 */
public interface NodeVisitor {

	/**
	 * Receives a ConstantNode object and handles it appropriately.
	 * 
	 * @param node a ConstantNode object
	 * @throws IllegalArgumentException if the specified node is null
	 */
	void visit(ConstantNode node);
	
	/**
	 * Receives a VariableNode object and handles it appropriately.
	 * 
	 * @param node a VariableNode object
	 * @throws IllegalArgumentException if the specified node is null
	 */
	void visit(VariableNode node);
	
	/**
	 * Receives a UnaryOperatorNode object and handles it appropriately.
	 * 
	 * @param node a UnaryOperatorNode object
	 * @throws IllegalArgumentException if the specified node is null
	 */
	void visit(UnaryOperatorNode node);
	
	/**
	 * Receives a BinaryOperatorNode object and handles it appropriately.
	 * 
	 * @param node a BinaryOperatorNode object
	 * @throws IllegalArgumentException if the specified node is null
	 */
	void visit(BinaryOperatorNode node);
}
