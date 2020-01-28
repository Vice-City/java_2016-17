package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Form closely following the BlogUser class.
 * 
 * @author Vice Ivušić
 *
 */
public class RegisterForm extends AbstractForm {

	/**
	 * ID of the user in question.
	 */
	private String id;
	/**
	 * First name of the user in question.
	 */
	private String firstName;
	/**
	 * Last name of the user in question.
	 */
	private String lastName;
	/**
	 * Nickname of the user in question.
	 */
	private String nick;
	/**
	 * Email of the user in question.
	 */
	private String email;
	/**
	 * Password of the user in question.
	 */
	private String password;
	
	/**
	 * Fills this form with parameters obtained from the specified
	 * HttpServletRequest.
	 * 
	 * @param req HttpServletRequest containing parameters.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		id = prepare(req.getParameter("id"));
		firstName = prepare(req.getParameter("firstName"));
		lastName = prepare(req.getParameter("lastName"));
		nick = prepare(req.getParameter("nick"));
		email = prepare(req.getParameter("email"));
		password = prepare(req.getParameter("password"));
	}
	
	/**
	 * Fills this form with available data in the specified object.
	 * 
	 * @param user object containing relevant data for this form
	 */
	public void fillFromUser(BlogUser user) {
		if (user.getId() == null) {
			id = "";
		} else {
			id = user.getId().toString();
		}
		
		firstName = user.getFirstName();
		lastName = user.getLastName();
		nick = user.getNick();
		email = user.getEmail();
		password = user.getPasswordHash();
	}
	
	/**
	 * Fills the specified object with data relevant to this form.
	 * 
	 * @param user object to be filled with data relevant to this form
	 */
	public void fillUser(BlogUser user) {
		if (id.isEmpty()) {
			user.setId(null);
		} else {
			user.setId(Long.parseLong(id));
		}
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setEmail(email);
		user.setPasswordHash(password);
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
		
		if (firstName.isEmpty()) {
			errors.put("firstName", "First name cannot be empty!");
		}
		
		if (lastName.isEmpty()) {
			errors.put("lastName", "Last name cannot be empty!");
		}
		
		if (nick.isEmpty()) {
			errors.put("nick", "Nickname cannot be empty!");
		}
		
		if (email.isEmpty()) {
			errors.put("email", "Email address cannot be empty!");
		} else if (email.length() < 3 || !email.contains("@")) {
			errors.put("email", "Invalid email address!");
		}
		
		if (password.isEmpty()) {
			errors.put("password", "Password cannot be empty!");
		} else if (password.length() < 6) {
			errors.put("password", "Password must be at least 6 characters long!");
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
