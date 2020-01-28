package hr.fer.zemris.java.hw15.dao;

import java.util.List;

import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Interface modeling a data access object.
 * 
 * @author Vice Ivušić
 *
 */
public interface DAO {

	/**
	 * Returns the BlogEntry with the specified id. Returns null if no such
	 * entry exists. Expects the user to pass on a non-null value.
	 * 
	 * @param id id of the entry in question
	 * @return blog entry with the specified id or null if no such entry exists
	 * @throws DAOException if an error occurs during communication with database
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Returns the BlogUser with the specified nickname. Returns null if
	 * no such user exists. Expects the user to pass on a non-null value.
	 * 
	 * @param nick nickname of the user in question
	 * @return BlogUser object with the specified nickname
	 * @throws DAOException if an error occurs during communication with database
	 */
	public BlogUser getBlogUser(String nick) throws DAOException;

	/**
	 * Returns a list of registered blog users. Returns an empty list
	 * if there are no registered users.
	 * 
	 * @return list of registered blog users
	 * @throws DAOException if an error occurs during communication with database
	 */
	public List<BlogUser> getRegisteredUsers() throws DAOException;

	/**
	 * Saves the specified Object into the database. This method expects the
	 * Object to be properly marked as an entity and it expects a non-null
	 * value to be passed.
	 * 
	 * @param obj object marked as an entity to save into the database
	 * @throws DAOException if an error occurs during communication with database
	 */
	public void save(Object obj) throws DAOException;

	/**
	 * Updates the version of the blog entry within the database with the specified
	 * version. Expects the user to pass a non-null value.
	 * 
	 * @param existingEntry blog entry to update with
	 * @throws DAOException if an error occurs during communication with database
	 */
	public void updateEntry(BlogEntry existingEntry) throws DAOException;

}
