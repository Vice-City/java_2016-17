package hr.fer.zemris.java.hw15.forms;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class implementing error specifying functionality
 * for form classes.
 * 
 * @author Vice Ivušić
 *
 */
public abstract class AbstractForm {

	/**
	 * Map mapping property names to their error descriptions.
	 */
	protected Map<String, String> errors = new HashMap<>();

	/**
	 * Returns the error message for the specified property name.
	 * Returns null if no such property exists.
	 * 
	 * @param propertyName property name for which to return the error message for
	 * @return error message for specified property name, or null if no such property exists
	 */
	public String getErrorMessage(String propertyName) {
		return errors.get(propertyName);
	}
	
	/**
	 * Sets the error message to the specified one for the specified
	 * property name.
	 * 
	 * @param propertyName property name to set error message for
	 * @param errorMessage error message for specified property name
	 */
	public void setErrorMessage(String propertyName, String errorMessage) {
		errors.put(propertyName, errorMessage);
	}
	
	/**
	 * Returns true if any errors are present.
	 * 
	 * @return true if any error are present
	 */
	public boolean errorsPresent() {
		return !errors.isEmpty();
	}
	
	/**
	 * Returns true if the specified property has an error associated with it.
	 * 
	 * @param propertyName name of the property whose error is being searched for
	 * @return true if the specified property has an error associated with it
	 */
	public boolean hasError(String propertyName) {
		return errors.containsKey(propertyName);
	}
	
	/**
	 * Returns an empty string for null values, or a trimmed string otherwise.
	 * 
	 * @param s string to prepare for saving in a form
	 * @return empty string for null values, or a trimmed string otherwise
	 */
	protected String prepare(String s) {
		if (s == null) return "";
		
		return s.trim();
	}
	
	/**
	 * Goes through each property and writes error messages if any errors are
	 * determined.
	 * 
	 */
	public abstract void validate();

}
