package hr.fer.zemris.java.hw04.db;

/**
 * Represents a boolean valued function which takes a
 * StudentRecord object.
 * 
 * @author Vice Ivušić
 *
 */
public interface IFilter {

	/**
	 * Returns true if the specified StudentRecord object
	 * satisfies a given criteria.
	 * 
	 * @param record StudentRecord object
	 * @return true if the object satisfies the defined criteria
	 */
	boolean accepts(StudentRecord record);
}
