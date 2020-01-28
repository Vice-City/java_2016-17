package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

/**
 * Form closely for allowing users to log in.
 * 
 * @author Vice Ivušić
 *
 */
public class LoginForm extends AbstractForm {

	/**
	 * User's nickname.
	 */
	private String nick;
	
	/**
	 * User's password.
	 */
	private String password;
	
	/**
	 * Fills this form with parameters obtained from the specified
	 * HttpServletRequest.
	 * 
	 * @param req HttpServletRequest containing parameters.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		nick = prepare(req.getParameter("nick"));
		password = prepare(req.getParameter("password"));
	}
	
	@Override
	public void validate() {
		errors.clear();
		
		if (nick.isEmpty()) {
			errors.put("nick", "Nickname cannot be empty!");
		}

		if (password.isEmpty()) {
			errors.put("password", "Password cannot be empty!");
		}

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
