package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.RegisterForm;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * An HttpServlet which offers a registration form for new users.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="registerServlet", urlPatterns={"/servleti/register"})
public class RegisterServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainMethod(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
		
		String method = req.getParameter("method");
		if ("Register".equals(method)) {
			RegisterForm registerForm = new RegisterForm();
			
			registerForm.fillFromHttpRequest(req);
			registerForm.validate();
			
			BlogUser existingUser = DAOProvider.getDAO().getBlogUser(registerForm.getNick());
			if (existingUser != null) {
				registerForm.setErrorMessage("nick", "Nickname is already taken! Please user another nickname.");
			}
			
			if (registerForm.errorsPresent()) {
				req.setAttribute("registerForm", registerForm);
				req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
				return;
			}
			
			String hashedPassword = Util.hashPassword(registerForm.getPassword());
			registerForm.setPassword(hashedPassword);
			
			BlogUser blogUser = new BlogUser();
			registerForm.fillUser(blogUser);
			
			try {
				DAOProvider.getDAO().save(blogUser);
			} catch (DAOException ex) {
				req.setAttribute("errorMessage", ex.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		} else if ("Cancel".equals(method)) {
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		}
		
		RegisterForm registerForm = new RegisterForm();
		registerForm.fillFromUser(new BlogUser());
		req.setAttribute("registerForm", registerForm);
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

}
