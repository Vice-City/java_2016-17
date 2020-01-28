package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Returns a list of polls from the underlying database. Returns an
	 * empty list if no such polls exist.
	 * 
	 * @return list of polls from the underlying database
	 */
	List<Poll> getPolls();

	/**
	 * Adds a vote for the poll option with the specified ID.
	 * 
	 * @param requestedId poll option ID to add a vote to
	 * @return ID of the poll that the voted for poll option belongs to
	 */
	long addVote(long requestedId);

	/**
	 * Returns the poll with the specified poll ID. Returns null
	 * if a poll with the specified ID doesn't exist.
	 * 
	 * @param requestedPollId poll ID of the requested poll
	 * @return poll with the requested poll ID
	 */
	Poll getPollWithId(long requestedPollId);

	/**
	 * Returns a list of PollOptions for the specified poll ID. Returns an
	 * empty list of no such poll options exist.
	 * 
	 * @param requestedPollId poll ID of the requested poll options
	 * @return list of poll options for the specified poll ID
	 */
	List<PollOption> getPollOptions(long requestedPollId);

	
}
