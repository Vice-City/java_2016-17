package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models the abstract observer of the Observer design pattern. Each
 * listener can be registered for updates in an {@link ILocalizationProvider}'s
 * changes in localization settings.
 * 
 * @author Vice Ivušić
 *
 */
public interface ILocalizationListener {

	/**
	 * Method which is called by an observed ILocalizationProvider each time
	 * its localization settings change.
	 */
	void localizationChanged();
}
