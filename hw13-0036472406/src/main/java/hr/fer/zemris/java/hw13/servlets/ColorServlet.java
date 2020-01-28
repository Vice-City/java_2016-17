package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * An HttpServlet which parses a color parameter and sets the pickedBgColor
 * session attribute, available to other webapp resources.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="colorServlet", urlPatterns={"/setcolor"})
public class ColorServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String colorToken = req.getParameter("color").toLowerCase();
		HttpSession currentSession = req.getSession();

		switch(colorToken) {
		case "white":
			currentSession.setAttribute("pickedBgColor", "white");
			break;
		case "red":
			currentSession.setAttribute("pickedBgColor", "red");
			break;
		case "green":
			currentSession.setAttribute("pickedBgColor", "green");
			break;
		case "cyan":
			currentSession.setAttribute("pickedBgColor", "cyan");
		}
		
		req.
			getRequestDispatcher("/colors.jsp")
			.forward(req, resp);
	}
}
