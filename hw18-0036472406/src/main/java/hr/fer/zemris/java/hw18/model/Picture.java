package hr.fer.zemris.java.hw18.model;

/**
 * Data model of a picture with its URL, description and tags describing it.
 * @author Vice Ivušić
 *
 */
public class Picture {

	/**
	 * The relative URL where the picture is stored.
	 */
	private String url;
	
	/**
	 * The picture's description.
	 */
	private String description;
	
	/**
	 * The tags describing the picture.
	 */
	private String[] tags;
	
	/**
	 * Creates a new Picture with the specified parameters. Expects the user to
	 * pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param url the relative URL where the picture is stored
	 * @param description the picture's description
	 * @param tags the tags describing the picture
	 */
	public Picture(String url, String description, String[] tags) {
		this.url = url;
		this.description = description;
		this.tags = tags;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}
	
	
}
