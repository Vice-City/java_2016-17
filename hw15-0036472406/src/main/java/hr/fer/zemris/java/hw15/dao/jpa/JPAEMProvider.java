package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.hw15.dao.DAOException;

/**
 * Class which can provide EntityManager objects. Each time communication
 * with the database is needed, the EntityManager object provided by this
 * class is to be used for said communication.
 * 
 * @author Vice Ivušić
 *
 */
public class JPAEMProvider {

	/**
	 * Map mapping threads to appropriate connections.
	 */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Provides an EntityManager object and begins database transaction.
	 * 
	 * @return EntityManager object
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Commits transaction and closes used EntityManager.
	 * 
	 * @throws DAOException if an error occurs during communication with database
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
	/**
	 * Helper class wrapping an EntityManager.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class LocalData {
		/**
		 * An EntityManager object.
		 */
		EntityManager em;
	}
	
}