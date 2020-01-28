package hr.fer.zemris.java.hw04.db;

/**
 * Represents a conditional expression which takes a
 * single attribute and compares its value to a given
 * String.
 * 
 * @author Vice Ivušić
 *
 */
public class ConditionalExpression {

	/** object containing the function for getting the attribute value **/
	private IFieldValueGetter getter;
	/** String that the attribute value is being compared against **/
	private String value;
	/** object containing the function with the comparison logic **/
	private IComparisonOperator operator;
	
	/**
	 * Creates a ConditionalExpression with the specified parameters.
	 * 
	 * @param getter IFieldValuegetter object for getting the attribute value
	 * @param value value the attribute value is being compared against
	 * @param operator IComparisonOperator object for applying the comparison logic
	 * @throws IllegalArgumentException if any of the specified parameters is null
	 */
	public ConditionalExpression(IFieldValueGetter getter, String value, IComparisonOperator operator) {
		if (getter==null || value==null || operator==null) {
			throw new IllegalArgumentException("At least one constructor argument is null!");
		}
		
		this.getter = getter;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * Returns the expression's getter object.
	 * 
	 * @return IFieldValueGetter object
	 */
	public IFieldValueGetter getGetter() {
		return getter;
	}

	/**
	 * Returns the expression's comparison value.
	 * 
	 * @return comparison value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the expression's comparison logic.
	 * 
	 * @return IComparisonOperator object
	 */
	public IComparisonOperator getOperator() {
		return operator;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", getter, value, operator);
	}
	
}
