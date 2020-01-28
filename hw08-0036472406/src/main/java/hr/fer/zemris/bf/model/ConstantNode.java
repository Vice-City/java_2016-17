package hr.fer.zemris.bf.model;

/**
 * Represents a boolean constant, i.e. true or false.
 * 
 * @author Vice Ivušić
 *
 */
public class ConstantNode implements Node {

	/** this node's boolean constant */
	private boolean value;
	
	/**
	 * Creates a new ConstantNode with the specified boolean value
	 * 
	 * @param value this node's boolean value
	 */
	public ConstantNode(boolean value) {
		this.value = value;
	}
	
	/**
	 * Returns this node's boolean constant.
	 * 
	 * @return this node's boolean constant
	 */
	public boolean getValue() {
		return value;
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
		return Boolean.toString(value);
	}

}
