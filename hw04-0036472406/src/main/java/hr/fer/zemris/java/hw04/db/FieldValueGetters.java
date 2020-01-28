package hr.fer.zemris.java.hw04.db;

/**
 * Contains a number of static final objects which implement the
 * IFieldValueGetter interface, i.e. a function which takes a StudentRecord
 * and returns one of its attribute values.
 * 
 * @author Vice Ivušić
 *
 */
public class FieldValueGetters {

	/**
	 * Object whose get method returns the specified
	 * record's first name attribute.
	 */
	public static final IFieldValueGetter FIRST_NAME = record -> {
		if (record == null) {
			throw new IllegalArgumentException("Cannot get first name from null!");
		}
		return record.getFirstName();
	};
		
	/**
	 * Object whose get method returns the specified
	 * record's last name attribute.
	 */
	public static final IFieldValueGetter LAST_NAME = record -> {
		if (record == null) {
			throw new IllegalArgumentException("Cannot get last name from null!");
		}
		return record.getLastName();
	};
	
	/**
	 * Object whose get method returns the specified
	 * record's jmbag attribute.
	 */
	public static final IFieldValueGetter JMBAG = record -> {
		if (record == null) {
			throw new IllegalArgumentException("Cannot get JMBAG from null!");
		}
		return record.getJmbag();
	};
}
