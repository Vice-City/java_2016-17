package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet whose purpose is to redirect users to the servlet which
 * generates an index page.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="indexRedirectServlet", urlPatterns={"/", "/index.html", "/index.htm", "/index.jsp"})
public class IndexRedirectServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(req.getContextPath() + "/servleti/index.html");
	}
}
