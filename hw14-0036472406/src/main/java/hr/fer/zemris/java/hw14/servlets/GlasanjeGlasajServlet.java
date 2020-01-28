package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * An HttpServlet which processes a user's vote for a predefined set
 * of voting options. This servlet expects to receive the poll ID
 * of the poll options as its parameter; if none is received, it defaults
 * to the first defined poll.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeGlasajServlet", urlPatterns={"/servleti/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String idParam = req.getParameter("id");
		
		if (idParam == null) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		long id = idParam == null ? 1L : Long.parseLong(idParam);
		
		long pollId;
		try {
			pollId = DAOProvider.getDao().addVote(id);
		} catch (DAOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		req.getServletContext().setAttribute("lastVotedPollId", pollId);
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollId="+pollId);
	}
	
}
