package hr.fer.zemris.java.hw14.model;

/**
 * Data model for a PollOption description object.
 * 
 * @author Vice Ivušić
 *
 */
public class PollOption {

	/** ID of the poll option */
	private Long id;
	/** option's title */
	private String optionTitle;
	/** option's link */
	private String optionLink;
	/** ID of the poll this option belongs to */
	private Long pollId;
	/** vote count for this option */
	private Long votesCount;
	
	/**
	 * Creates a new PollOption with the specified parameters.
	 * 
	 * @param id ID of the poll option
	 * @param optionTitle option's title
	 * @param optionLink option's link
	 * @param pollId ID of the poll this option belongs to
	 * @param votesCount vote count for this option
	 */
	public PollOption(Long id, String optionTitle, String optionLink, Long pollId, Long votesCount) {
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollId = pollId;
		this.votesCount = votesCount;
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
	 * @return the optionTitle
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * @param optionTitle the optionTitle to set
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * @return the optionLink
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * @param optionLink the optionLink to set
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * @return the pollId
	 */
	public Long getPollId() {
		return pollId;
	}

	/**
	 * @param pollId the pollId to set
	 */
	public void setPollId(Long pollId) {
		this.pollId = pollId;
	}

	/**
	 * @return the votesCount
	 */
	public Long getVotesCount() {
		return votesCount;
	}

	/**
	 * @param votesCount the votesCount to set
	 */
	public void setVotesCount(Long votesCount) {
		this.votesCount = votesCount;
	}

	
}
