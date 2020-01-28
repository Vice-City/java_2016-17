package hr.fer.zemris.bf.model;

/**
 * Represents a boolean variable, which may represent 
 * a true or false constant.
 * 
 * @author Vice Ivušić
 *
 */
public class VariableNode implements Node {

	/** this node's name */
	private String name;
	
	/**
	 * Creates a new VariableNode with the specified name.
	 * 
	 * @param name this node's name
	 */
	public VariableNode(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Argument name cannot be null!");
		}
		
		this.name = name;
	}
	
	/**
	 * Returns this node's variable name.
	 * 
	 * @return this node's variable name
	 */
	public String getName() {
		return name;
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
		return name;
	}

}
