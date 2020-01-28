package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * A class implementing the {@link ILocalizationProvider} interface. Even though
 * this class implements the specified interface, it does not provide localization
 * querying functionality on its own. Its constructor expects a reference to
 * another object implementing the interface, such as the sole instance of the
 * {@link ILocalizationProvider} class, and acts as a bridge for potential
 * listeners wishing to register themselves for localization changes.
 * 
 * <p>By using this design, memory leak problems can be averted.
 * 
 * @author Vice Ivušić
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * flag indicating whether this bridge has registered itself
	 * for updates from another ILocalizationProvider object
	 */
	private boolean connected;
	/** provider object being used for this bridge */
	private ILocalizationProvider lp;
	/** listener object this bridge will use to register itself for updates from a provider object */
	private ILocalizationListener lpListener;
	
	/**
	 * Creates a new LocalizationProviderBridge which uses the specified
	 * provider object to receive localization updates.
	 * 
	 * @param lp ILocalizationProvider object to use for localization updates
	 * @throws NullPointerException if the specified provider is null
	 */
	public LocalizationProviderBridge(ILocalizationProvider lp) {
		if (lp == null) {
			throw new NullPointerException("Argument lp cannot be null!");
		}
		this.lp = lp;
	}
	
	/**
	 * Connects, i.e. registers this bridge for localization updates, to
	 * this bridge's provider object. Does nothing the the bridge is
	 * already connected.
	 */
	public void connect() {
		if (connected) {
			return;
		}
		
		lpListener = new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
//				System.out.println("In LocalizationProviderBridge, notifying my children");
				LocalizationProviderBridge.this.fire();
			}
		};
		
		lp.addLocalizationListener(lpListener);
		connected = true;
	}
	
	/**
	 * Disconnects, i.e. unregisters this bridge from localization updates, from
	 * this bridge's provider object. Does nothing the the bridge is
	 * already disconnected.
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}
		
		lp.removeLocalizationListener(lpListener);
		connected = false;
	}
	
	@Override
	public String getString(String key) {
		return lp.getString(key);
	}

}
