package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * A class extending the {@link LocalizationProviderBridge} class,
 * in turn implementing the {@link ILocalizationProvider} interface.
 * 
 * <p>An instance of this class, in addition to offering all the functionality
 * of the bridge class, takes a JFrame object through its constructor and
 * connects and disconnects itself for updates from a localization provider
 * object, depending on the window's closed state.
 * 
 * @author Vice Ivušić
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Creates a new FormLocalizationProvider for the specified provider
	 * object and the specified frame.
	 * 
	 * @param lp ILocalizationProvider object
	 * @param frame JFrame object to keep track of
	 * @throws NullPointerException if either of the arguments is null
	 */
	public FormLocalizationProvider(ILocalizationProvider lp, JFrame frame) {
		super(lp);
		
		if (frame == null) {
			throw new NullPointerException("Argument frame cannot be null!");
		}
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				FormLocalizationProvider.this.connect();
			}
			
			/*
			 * I am implementing windowClosed since it is possible for a user
			 * to cancel closing the application, which would break localization
			 * changing.
			 */
			@Override
			public void windowClosed(WindowEvent e) {
				FormLocalizationProvider.this.disconnect();
			}
		});
	}

}
