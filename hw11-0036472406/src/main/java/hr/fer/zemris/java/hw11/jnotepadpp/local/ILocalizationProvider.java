package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.MissingResourceException;

/**
 * An interface which models an object which knows how to retrieve
 * locale-sensitive strings for a specified key. This interface
 * also models the subject of the Observer pattern and expects classes to implement
 * methods for registering and unregistering {@link ILocalizationListener}
 * objects.
 * 
 * @author Vice Ivušić
 *
 */
public interface ILocalizationProvider {

	/**
	 * Registers the specified listener object for changes in localization settings.
	 * Does nothing if the specified listener is already registered or is null.
	 * 
	 * @param l listener to register for localization updates
	 */
	void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Unregisters the specified listener for updates in localization settings.
	 * Does nothing if the specified listener wasn't registered or is null.
	 * 
	 * @param l listener to unregister for localization updates
	 */
	void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * Returns a locale-sensitive string mapped to the specified key.
	 * Does nothing of the specified key is null. Returns null if the 
	 * specified key doesn't have an appropriate mapping.
	 * 
	 * @param key key to retrieve a mapping for
	 * @return locale-sensitive string mapped to the specified string
	 * @throws NullPointerException if the specified key is null
	 * @throws MissingResourceException if no object for the given key can be found
	 * @throws ClassCastException if the specified key isn't a string
	 */
	String getString(String key);
}
