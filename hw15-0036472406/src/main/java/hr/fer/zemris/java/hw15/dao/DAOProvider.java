package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.dao.jpa.JPADAOImpl;

/**
 * Singleton class which can provide a database access object
 * for any actions related to database communication.
 * 
 * @author Vice Ivušić
 *
 */
public class DAOProvider {

	/**
	 * unique instance of the DAO class
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Returns a DAO object which knows how to communicate with the database.
	 * 
	 * @return DAO object which knows how to communicate with the database
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}