package hr.fer.zemris.java.hw04.db;

/**
 * Represents a function which takes a StudentRecord
 * object and returns one of its properties.
 * 
 * @author Vice Ivušić
 *
 */
public interface IFieldValueGetter {

	/**
	 * Takes a StudentRecord object and returns one of
	 * its properties.
	 * 
	 * @param record StudentRecord object
	 * @return one of its properties dependent on the implementation
	 * @throws IllegalArgumentException if the specified record is null
	 */
	String get(StudentRecord record);
}
