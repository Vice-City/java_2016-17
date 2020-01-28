package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

/**
 * Represents an {@link JLabel} object which takes the locale of
 * the provided localization provider object into account, as well
 * as updating its display text when the localization settings change.
 * 
 * @author Vice Ivušić
 *
 */
public class LocalizableJLabel extends JLabel {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/** the key which this label uses to retrieve locale sensitive strings */
	protected String key;
	
	/** the localization provider this label prompts for locale sensitive strings */
	protected ILocalizationProvider lp;
	
	/** the listener this label uses to get notified about localization updates */
	protected ILocalizationListener listener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			String newText = LocalizableJLabel.this.getText();
			
			/*
			 * This is so the status labels (which use this class in
			 * the scope of this project) retain their data when a
			 * localization update occurs, instead of becoming blank.
			 * E.g. length:4 will become veličina:4 instead of just veličina.
			 */
			Pattern p = Pattern.compile(".*(:.*)");
			Matcher m = p.matcher(newText);
			
			String localizedPart = lp.getString(key);
			if (m.matches()) {
				newText = localizedPart+m.group(1);
			} else {
				newText = localizedPart;
			}
			
			LocalizableJLabel.this.setText(newText);
		}
	};
	
	/**
	 * Constructs a new LocalizableJLabel for the specified key, using the specified
	 * localization provider to receive locale sensitive strings for the provided key.
	 * 
	 * @param key the key which this label will use to retrieve locale sensitive strings
	 * @param lp the localization provider this label will prompt for locale sensitive strings
	 * @throws NullPointerException if either of the arguments is null
	 */
	public LocalizableJLabel(String key, ILocalizationProvider lp) {
		if (key == null || lp == null) {
			throw new NullPointerException("None of the arguments may be null!");
		}
		
		this.key = key;
		this.lp = lp;
		
		lp.addLocalizationListener(listener);
		
		setText(lp.getString(key));
	}
}
