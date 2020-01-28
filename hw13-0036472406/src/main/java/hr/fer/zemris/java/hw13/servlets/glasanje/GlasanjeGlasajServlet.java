package hr.fer.zemris.java.hw13.servlets.glasanje;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which processes a user's vote for a predefined set
 * of bands. This servlet expects the file glasanje-rezultati.txt to be
 * located in /WEB-INF/. Each line of the file has to have 2 tab separated
 * values: ID of the band and the votes the band received. If the specified
 * file doesn't exist, an empty result file will be created using
 * glasanje-definicija.txt located in the same folder.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeGlasajServlet", urlPatterns={"/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path filePath = Paths.get(fileName);
		
		if (Files.notExists(filePath)) {
			String emptyResults;
			try {
				emptyResults = GlasanjeServlet.createEmptyResults(
					req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt")
				);
				
				Files.write(
					filePath,
					emptyResults.getBytes(StandardCharsets.UTF_8)
				);
			} catch (IOException ex) {
				req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
				return;
			}
		}
		
		
		Map<String, Integer> results;
		try {
			results = GlasanjeServlet.parseResults(fileName);
		} catch (IOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		String id = req.getParameter("id");
		Integer oldValue = results.get(id);
		
		if (oldValue == null) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		results.put(id, oldValue+1);
		
		String newResults = GlasanjeServlet.createResults(results);
		
		try {
			Files.write(
				filePath,
				newResults.getBytes(StandardCharsets.UTF_8)
			);
		} catch (IOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
}
