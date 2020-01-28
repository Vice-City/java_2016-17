package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Represents an {@link Action} object which takes the locale of
 * the provided localization provider object into account, as well
 * as updating its display text when the localization settings change.
 * 
 * @author Vice Ivušić
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/** the key which this action uses to retrieve locale sensitive strings */
	private String key;
	
	/** the localization provider this action prompts for locale sensitive strings */
	private ILocalizationProvider lp;
	
	/** the listener this action uses to get notified about localization updates */
	private ILocalizationListener listener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			LocalizableAction.this.putValue(NAME, lp.getString(key+"Name"));
			LocalizableAction.this.putValue(SHORT_DESCRIPTION, lp.getString(key+"Description"));
		}
	};
	
	/**
	 * Constructs a new LocalizableAction for the specified key, using the specified
	 * localization provider to receive locale sensitive strings for the provided key.
	 * 
	 * @param key the key which this action will use to retrieve locale sensitive strings
	 * @param lp the localization provider this action will prompt for locale sensitive strings
	 * @throws NullPointerException if either of the arguments is null
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		if (key == null || lp == null) {
			throw new NullPointerException("None of the arguments may be null!");
		}
		
		this.key = key;
		this.lp = lp;
		
		lp.addLocalizationListener(listener);
		
		putValue(NAME, lp.getString(key+"Name"));
		putValue(SHORT_DESCRIPTION, lp.getString(key+"Description"));
	}

}
