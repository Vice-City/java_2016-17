package hr.fer.zemris.java.hw04.db;

import java.util.List;

/**
 * Implements the IFilter interface. This class takes a list
 * of ConditionalExpression objects, which can then be checked
 * against a record using its accept method. 
 * 
 * @author Vice Ivušić
 *
 */
public class QueryFilter implements IFilter {

	/** list of expressions to check a StudentRecord against **/
	private List<ConditionalExpression> expressions;
	
	/**
	 * Creates a QueryFilter with the specified list of expressions.
	 * @param expressions logical expressions to check against
	 * @throws IllegalArgumentException if the specified expressions are null
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		if (expressions == null) {
			throw new IllegalArgumentException("List of expressions cannot be null!");
		}
		
		this.expressions = expressions;
	}
	
	/**
	 * Returns true if the specified record satisfied each
	 * and every expression QueryFilter checks against.
	 * 
	 * @return true if the record satsifies each and every expression
	 * @throws IllegalArgumentException if the specified record is null
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		if (record == null) {
			throw new IllegalArgumentException("Student record cannot be null!");
		}
		
		for (ConditionalExpression expr : expressions) {
			String var = expr.getValue();
			IComparisonOperator oper = expr.getOperator();
			IFieldValueGetter getter = expr.getGetter();
			
			if (!oper.satisfied(getter.get(record), var)) {
				return false;
			}
		}
		
		return true;
	}

}
