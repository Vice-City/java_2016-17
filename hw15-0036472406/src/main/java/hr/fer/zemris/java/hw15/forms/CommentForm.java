package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogComment;

/**
 * Form closely following the BlogComment class.
 * 
 * @author Vice Ivušić
 *
 */
public class CommentForm extends AbstractForm {

	/**
	 * ID of the comment in question.
	 */
	private String id;
	
	/**
	 * Message content of the comment in question.
	 */
	private String message;
	
	/**
	 * Email associated with the poster of the comment in question.
	 */
	private String usersEMail;
	
	/**
	 * Fills this form with parameters obtained from the specified
	 * HttpServletRequest.
	 * 
	 * @param req HttpServletRequest containing parameters.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		id = prepare(req.getParameter("id"));
		message = prepare(req.getParameter("message"));
		usersEMail = prepare(req.getParameter("usersEMail"));
	}
	
	/**
	 * Fills this form with available data in the specified object.
	 * 
	 * @param comment object containing relevant data for this form
	 */
	public void fillFromComment(BlogComment comment) {
		if (comment.getId() == null) {
			id = "";
		} else {
			id = comment.getId().toString();
		}
		
		message = comment.getMessage();
		usersEMail = comment.getUsersEMail();
		
	}
	
	/**
	 * Fills the specified object with data relevant to this form.
	 * 
	 * @param comment object to be filled with data relevant to this form
	 */
	public void fillComment(BlogComment comment) {
		if (id.isEmpty()) {
			comment.setId(null);
		} else {
			comment.setId(Long.parseLong(id));
		}
		
		comment.setMessage(message);
		comment.setUsersEMail(usersEMail);;
		
	}
	
	@Override
	public void validate() {
		errors.clear();
		
		if (!id.isEmpty()) {
			try {
				Long.parseLong(id);
			} catch (NumberFormatException ex) {
				errors.put("id", "ID number is invalid!");
			}
		}
		
		
		if (message.isEmpty()) {
			errors.put("message", "Message cannot be empty!");
		}
		
		if (usersEMail.isEmpty()) {
			errors.put("usersEMail", "Email cannot be empty!");
		} else if (usersEMail.length() < 3 || !usersEMail.contains("@")) {
			errors.put("usersEMail", "Invalid email address!");
		}

	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the title to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the text
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * @param usersEMail the text to set
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	
}
