package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * An HttpServlet which prepares the voting results for rendering inside a
 * JSP page. 
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeRezultatiServlet", urlPatterns={"/servleti/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollIdParam = req.getParameter("pollId");
		long pollId = pollIdParam == null ? 1L : Long.parseLong(pollIdParam);
		List<PollOption> results;
		try {
			results = DAOProvider.getDao().getPollOptions(pollId);
		} catch (DAOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		results.sort((opt1, opt2) -> - Long.compare(opt1.getVotesCount(), opt2.getVotesCount()));
		List<PollOption> winningResults = createWinningResults(results);
		
		req.setAttribute("pollId", pollId);
		req.setAttribute("results", results);
		req.setAttribute("winningResults", winningResults);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	} 
	
	/**
	 * Helper method for creating a list of winning poll options. It expects
	 * the input list of PollOptions to be sorted by the amount of votes
	 * each PollOption received in descending order (highest votes first).
	 * 
	 * @param results list of all poll options, ordered by received votes
	 * @return list of winning poll options
	 */
	private List<PollOption> createWinningResults(List<PollOption> results) {
		List<PollOption> winningList = new ArrayList<>();
		
		Long maximumVoteCount = null;
		for (PollOption opt : results) {
			if (maximumVoteCount == null) {
				maximumVoteCount = opt.getVotesCount();
			}
			
			if (maximumVoteCount > opt.getVotesCount()) {
				break;
			}
			
			winningList.add(opt);
		}
		
		return winningList;
	}

}
