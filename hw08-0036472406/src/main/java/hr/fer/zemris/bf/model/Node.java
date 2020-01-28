package hr.fer.zemris.bf.model;

/**
 * Represents a boolean expression node. Each node implementing
 * this interface must offer a method for accepting an object
 * implementing the {@link NodeVisitor} interface.
 * 
 * @author Vice Ivušić
 *
 */
public interface Node {

	/**
	 * Accepts the specified NodeVisitor object and calls the appropriate
	 * visitor's method for handling the Node that accepted the visitor.
	 * 
	 * @param visitor NodeVisitor object
	 * @throws IllegalArgumentException if the specified visitor is null
	 */
	void accept(NodeVisitor visitor);
}
