package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class implementing the {@link ILocalizationProvider} interface
 * and implementing Observer design pattern functionality for registering
 * and unregistering {@link ILocalizationListener} objects. The only
 * unimplemented method is the getString method.
 * 
 * @author Vice Ivušić
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

	/** list of registered listeners for localization updates */
	private List<ILocalizationListener> listeners = new ArrayList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		if (listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		if (!listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}
	
	/**
	 * Called by current object each time a localization update occurs in
	 * order to inform all registered observers by calling localizationChanged
	 * on each of them.
	 */
	public void fire() {
		for (ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}

}
