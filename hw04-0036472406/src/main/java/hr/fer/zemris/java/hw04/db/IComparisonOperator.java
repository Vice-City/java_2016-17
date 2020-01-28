package hr.fer.zemris.java.hw04.db;

/**
 * Represents a boolean valued function of two strings.
 * 
 * @author Vice Ivušić
 *
 */
public interface IComparisonOperator {

	/**
	 * Compares two strings and returns a boolean value.
	 * 
	 * @param value1 first string
	 * @param value2 second string
	 * @return boolean value dependent on the comparison
	 */
	boolean satisfied(String value1, String value2);
}
