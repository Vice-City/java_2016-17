package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Class which can provide an instance of an
 * EntityManagerFactory class, which can then be used to access
 * EntityManager objects.
 * 
 * @author Vice Ivušić
 *
 */
public class JPAEMFProvider {

	/** 
	 * unique instance of the EntityManagerFactory class 
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Returns an instance of the EntityManagerFactory class.
	 * 
	 * @return instance of the EntityManagerFactory class
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Sets this class's EntityManagerFactory to the specified one.
	 * Expects the user to pass on a non-null value.
	 * 
	 * @param emf instance of the EntityManagerFactory class
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}