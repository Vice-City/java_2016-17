package hr.fer.zemris.java.hw04.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains a number of static final objects which implement the
 * IComparisonOperator interface, i.e. a boolean valued function
 * of two String arguments.
 * 
 * @author Vice Ivušić
 *
 */
public class ComparisonOperators {

	/**
	 * Object whose accept method returns true if the first
	 * String is smaller than the second String. Returns
	 * false if either of the parameters is null.
	 */
	public static final IComparisonOperator LESS = (value1, value2) -> {
		if (value1==null || value2==null) return false;
			
		return value1.compareTo(value2) < 0;
	};
			
	/**
	 * Object whose accept method returns true if the first
	 * String is smaller or equal to the second String. Returns
	 * true if both parameters are null, or false if only one
	 * of them is.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> {
		if (value1==null && value2==null) return true;	
		if (value1==null || value2==null) return false;
			
		return value1.compareTo(value2) <= 0;
	};
		
	/**
	 * Object whose accept method returns true if the first
	 * String is greater than the second String. Returns false
	 * if either of the parameters is null.
	 */
	public static final IComparisonOperator GREATER = (value1, value2) -> {
		if (value1==null || value2==null) return false;
			
		return value1.compareTo(value2) > 0;
	};
					
	/**
	 * Object whose accept method returns true if the first
	 * String is greater or equal to the second String.
	 * Returns true if both parameters are null, or false 
	 * if only one of them is.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> {
		if (value1==null && value2==null) return true;	
		if (value1==null || value2==null) return false;
			
		return value1.compareTo(value2) >= 0;
	};
			
	/**
	 * Object whose accept method returns true if the first
	 * String is equal to the second String. Returns true
	 * if both parameters are null, or false if only one
	 * of them is.
	 */
	public static final IComparisonOperator EQUALS = (value1, value2) -> {
		if (value1==null && value2==null) return true;	
		if (value1==null || value2==null) return false;
			
		return value1.compareTo(value2) == 0;
	};
			
	/**
	 * Object whose accept method returns true if the first
	 * String is not equal to the second String. Returns true
	 * if either of the parameters is null, or false if both
	 * of them are.
	 */
	public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> {
		if (value1==null && value2==null) return false;	
		if (value1==null || value2==null) return true;
			
		return value1.compareTo(value2) != 0;
	};
			
	/**
	 * Object whose accept method returns true if the first
	 * String is a part of the second String. Returns true
	 * if both of the parameters are null, or false if only
	 * one of them is.
	 */
	public static final IComparisonOperator LIKE = new IComparisonOperator() {
		
		@Override
		public boolean satisfied(String value1, String value2) {
			if (value1==null && value2==null) return true;	
			if (value1==null || value2==null) return false;
			
			// the wildcard character is actually .* in regular expressions
			String regex = value2.replaceAll("\\*", ".*");
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(value1);
			
			return matcher.matches();
		}
	};
	
}
