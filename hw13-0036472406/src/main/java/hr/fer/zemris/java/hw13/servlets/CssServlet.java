package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which prepares attributes necessary for dynamic
 * configuration of a CSS stylesheet.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="cssServlet", urlPatterns={"/style"})
public class CssServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/** default color when no color is set */
	private static final String DEFAULT_COLOR = "white";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String color = (String) req.getSession().getAttribute("pickedBgColor");
		
		if (color == null) {
			color = DEFAULT_COLOR;
		}
		
		req.getSession().setAttribute("pickedBgColor", color);
		
		req.
			getRequestDispatcher("/WEB-INF/config/style.jsp")
			.forward(req, resp);
	}
}
