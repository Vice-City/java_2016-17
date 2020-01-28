package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * An HttpServlet which prompts the user to vote for a predefined set
 * of voting options. This servlet expects to receive the poll ID
 * of the poll options as its parameter; if none is received, it defaults
 * to the first defined poll.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeServlet", urlPatterns={"/servleti/glasanje"})
public class GlasanjeServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollId = Long.parseLong(req.getParameter("pollId"));
		
		Poll poll;
		List<PollOption> pollOptions;
		try {
			poll = DAOProvider.getDao().getPollWithId(pollId);
			pollOptions = DAOProvider.getDao().getPollOptions(pollId);
		} catch (DAOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("poll", poll);
		req.setAttribute("pollOptions", pollOptions);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
