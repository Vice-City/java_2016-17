package hr.fer.zemris.java.hw15.dao.jpa;

import java.util.List;

import javax.persistence.NoResultException;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * A class which implements the DAO interface.
 * 
 * @author Vice Ivušić
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		try {
			BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
			return blogEntry;
		} catch (Exception ex) {
			throw new DAOException("Could not find blog entry with ID "+id+"!", ex);
		}
	}
	
	@Override
	public BlogUser getBlogUser(String nick) {
		try {
			BlogUser blogUser;
			try {
				blogUser = (BlogUser) JPAEMProvider.getEntityManager()
						.createQuery("SELECT user FROM BlogUser AS user WHERE user.nick=:userNick")
						.setParameter("userNick", nick)
						.getSingleResult();
			} catch (NoResultException ex) {
				blogUser = null;
			}
			
			return blogUser;
		} catch (Exception ex) {
			throw new DAOException("Could not find blog user with nickname "+nick+"!", ex);
		}

		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BlogUser> getRegisteredUsers() {
		try {
			List<BlogUser> registeredUsers = (List<BlogUser>) JPAEMProvider.getEntityManager()
					.createQuery("SELECT user FROM BlogUser AS user")
					.getResultList();
			
			return registeredUsers;
		} catch (Exception ex) {
			throw new DAOException("Could not load list of registered users!", ex);
		}
	}
	
	@Override
	public void save(Object obj) {
		try {
			JPAEMProvider.getEntityManager().persist(obj);
		} catch (Exception ex) {
			throw new DAOException("Could not save object"+obj+"!", ex);
		}

	}
	
	@Override
	public void updateEntry(BlogEntry existingEntry) {
		try {
			JPAEMProvider.getEntityManager().merge(existingEntry);
		} catch (Exception ex) {
			throw new DAOException("Could not update blog entry with ID "+existingEntry.getId()+"!", ex);
		}
	}
	
}