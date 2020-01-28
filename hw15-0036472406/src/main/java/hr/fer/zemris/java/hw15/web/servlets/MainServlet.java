package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.LoginForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * An HttpServlet which serves requests headed for /servleti/main.
 * It offers a log in page as well as listing available blogs.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="mainServlet", urlPatterns={"/servleti/main"})
public class MainServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainMethod(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainMethod(req, resp);
	}

	/**
	 * Helper method that processes both GET and POST http requests.
	 * 
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void mainMethod(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		List<BlogUser> registeredUsers;
		try {
			registeredUsers = DAOProvider.getDAO().getRegisteredUsers();
		} catch (DAOException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("registeredUsers", registeredUsers);
		
		Long userId = (Long) req.getSession().getAttribute("currentUserId");
		if (userId != null) {
			req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
			return;
		}
		
		String method = req.getParameter("method");
		if ("Login".equals(method)) {
			LoginForm loginForm = new LoginForm();
			
			loginForm.fillFromHttpRequest(req);
			loginForm.validate();
			
			BlogUser blogUser;
			try {
				blogUser = DAOProvider.getDAO().getBlogUser(loginForm.getNick());
			} catch (DAOException ex) {
				req.setAttribute("errorMessage", ex.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			
			if (blogUser == null) {
				if (!loginForm.getNick().isEmpty()) {
					loginForm.setErrorMessage("nick", "User with given nickname doesn't exist!");
					loginForm.setErrorMessage("password", "Cannot check password for nonexistent user!");
				}
			} else {
				if (checkPasswordInvalid(loginForm.getPassword(), blogUser.getPasswordHash())) {
					loginForm.setErrorMessage("password", "Invalid password!");
				}
			}
			
			if (loginForm.errorsPresent()) {
				loginForm.setPassword("");
				req.setAttribute("loginForm", loginForm);
				req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
				return;
			}
			
			req.getSession().setAttribute("currentUserId", blogUser.getId());
			req.getSession().setAttribute("currentUserFirstName", blogUser.getFirstName());
			req.getSession().setAttribute("currentUserLastName", blogUser.getLastName());
			req.getSession().setAttribute("currentUserNick", blogUser.getNick());
			
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		}
		
		LoginForm loginForm = new LoginForm();
		loginForm.setNick("");
		loginForm.setPassword("");
		req.setAttribute("loginForm", loginForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

	/**
	 * Helper method for determining if the specified password has the same
	 * hash value as the specified hash.
	 * 
	 * @param password password to compare hash value for
	 * @param passwordHash hash value to compare with
	 * @return true if the the password's hash value is the same as the specified hash value
	 */
	private boolean checkPasswordInvalid(String password, String passwordHash) {
		return !Util.hashPassword(password).equals(passwordHash);
	}
	
}
