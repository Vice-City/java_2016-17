package hr.fer.zemris.java.hw13.servlets.glasanje;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which prepares the voting results for rendering inside a
 * JSP page. This servlet expects the file glasanje-rezultati.txt to be
 * located in /WEB-INF/. Each line of the file has to have 2 tab separated
 * values: ID of the band and the votes the band received. If the specified
 * file doesn't exist, an empty result file will be created using
 * glasanje-definicija.txt located in the same folder.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeRezultatiServlet", urlPatterns={"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bandsFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		String resultsName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path resultsPath = Paths.get(resultsName);
		
		if (Files.notExists(resultsPath)) {
			String emptyResults = null;
			try {
				emptyResults = GlasanjeServlet.createEmptyResults(resultsName);
				
				Files.write(
					resultsPath,
					emptyResults.getBytes(StandardCharsets.UTF_8)
				);
			} catch (IOException ex) {
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
				return;
			}
		}
		
		Map<String, Integer> results = null;
		List<BandInfo> bands = null;
		try {
			results = GlasanjeServlet.parseResults(resultsName);
			bands = GlasanjeServlet.parseBands(bandsFileName);
		} catch (IOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		Map<BandInfo, Integer> bandToVoteMap = GlasanjeServlet.createSortedBandToVoteMap(bands, results);
		List<BandInfo> winningBands = createWinnerList(bands, bandToVoteMap);
		
		req.setAttribute("bandToVoteMap", bandToVoteMap);
		req.setAttribute("winningBands", winningBands);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Helper method for creating a list of bands with the most votes. Expects the user
	 * to pass a list of all bands, already sorted by the amount of votes they've received
	 * (in descending order; highest votes is first in list).
	 * 
	 * @param sortedBands list of bands sorted by amount of votes
	 * @param results map mapping bands to the amount of votes they've received
	 * @return list of bands with the winning amount of votes
	 */
	private static List<BandInfo> createWinnerList(List<BandInfo> sortedBands, Map<BandInfo, Integer> results) {
		int highestVote = results.get(sortedBands.get(0));
		
		List<BandInfo> winnerList = new ArrayList<>();
		for (BandInfo band : sortedBands) {
			if (results.get(band) != highestVote) {
				continue;
			}
			
			winnerList.add(band);
		}
		
		return winnerList;
	}
}
