package hr.fer.zemris.java.hw14.model;

/**
 * Data model for a Poll description object.
 * 
 * @author Vice Ivušić
 *
 */
public class Poll {

	/** id of the poll */
	private Long id;
	/** poll's title */
	private String title;
	/** poll's message */
	private String message;
	
	/**
	 * Creates a new Poll with the specified parameters.
	 * 
	 * @param id id of the poll
	 * @param title poll's title
	 * @param message poll's message
	 */
	public Poll(Long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
