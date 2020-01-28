package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Represents a localization provider implementing the {@link ILocalizationProvider}
 * interface. This class expects a set of .properties locale files to be present
 * in the same package as itself. Each .properties file has to be named
 * {@code translations_xx.properties}, where {@code xx} should be a language
 * identifier, such as en, de, or hr. Each .properties file should define
 * the same set of keys and appropriate translations for each key.
 * 
 * <p>An instance of this class can be retrieved by using the getInstance static method.
 * The instance offers methods for retrieving and setting the current language,
 * as well as for retrieving locale-sensitive strings based on a specified key.
 * 
 * <p>This localization provider provides the English, Croatian and German locales.
 * 
 * @author Vice Ivušić
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/** the only living instance of this class */
	private static final LocalizationProvider INSTANCE = new LocalizationProvider();
	
	/** the currently set language */
	private String language;
	/** the resource used for loading and translating locale-sensitive strings */
	private ResourceBundle bundle;
	
	/**
	 * Constructs a new LocalizationProvider object and stores it as a static variable.
	 */
	private LocalizationProvider() {
		language = "en";
		bundle = ResourceBundle.getBundle(
			"hr.fer.zemris.java.hw11.jnotepadpp.local.translations",
			Locale.forLanguageTag(language)
		);
	}
	
	/**
	 * Returns an instance of this class which offers methods for retrieving
	 * and setting the current language, as well as for retrieving locale-sensitive
	 * strings.
	 * 
	 * @return an instance of the {@link LocalizationProvider} class
	 */
	public static LocalizationProvider getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Sets the current language to the specified language.
	 * 
	 * @param language desired language to set locale to
	 * @throws NullPointerException if specified language is null
	 * @throws MissingResourceException if a resource bundle cannot be built
	 * 		   for the specified language
	 */
	public void setLanguage(String language) {
		if (language == null) {
			throw new NullPointerException("Argument language cannot be null!");
		}
		
		bundle = ResourceBundle.getBundle(
			"hr.fer.zemris.java.hw11.jnotepadpp.local.translations",
			Locale.forLanguageTag(language)
		);
		
		this.language = language;
		fire();
	}
	
	/**
	 * Retrieves the currently set language.
	 * 
	 * @return the currently set language
	 */
	public String getCurrentLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

}
