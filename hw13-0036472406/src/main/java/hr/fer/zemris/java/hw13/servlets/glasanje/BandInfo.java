package hr.fer.zemris.java.hw13.servlets.glasanje;

/**
 * Helper class for storing the ID, name and link to a band's song
 * for purposes of easier rendering inside a JSP.
 * 
 * @author Vice Ivušić
 *
 */
public class BandInfo {
	/** id of the band */
	private String id;
	/** name of the band */
	private String name;
	/** link to a popular song by this band */
	private String link;
	
	/**
	 * Creates a new BandInfo with the specified arguments. Expects the user to
	 * pass on legal values (i.e. non-null, IDs representing positive integers)
	 * and gives no warnings otherwise.
	 * 
	 * @param id id of the band
	 * @param name name of the band
	 * @param link link to a popular song by this band
	 */
	public BandInfo(String id, String name, String link) {
		this.id = id;
		this.name = name;
		this.link = link;
	}
	
	/**
	 * Returns the band's ID.
	 * 
	 * @return the band's ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the band's name.
	 * 
	 * @return the band's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a link to a popular song by this band.
	 * 
	 * @return link to a popular song by this band
	 */
	public String getLink() {
		return link;
	}
	
}