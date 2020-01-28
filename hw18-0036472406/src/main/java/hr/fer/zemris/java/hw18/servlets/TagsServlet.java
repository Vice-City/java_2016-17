package hr.fer.zemris.java.hw18.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw18.model.Picture;

/**
 * An HTTPServlet which loads all the tags the picture database consists of
 * and delivers them to the callee in JSON format.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="tagsServlet", urlPatterns={"/servlets/tags"})
public class TagsServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<Picture> pictures = (List<Picture>) req.getServletContext().getAttribute("pictures");
		
		if (pictures == null) {
			pictures = loadPictures(req, resp);
		}
		
		Set<String> tags = new TreeSet<>();
		
		for (Picture p : pictures) {
			for (String tag : p.getTags()) {
				tags.add(tag);
			}
		}
		
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().write(new Gson().toJson(tags));
		resp.getWriter().flush();
	}

	/**
	 * Loads information about each picture and returns it as a list of
	 * Picture objects.
	 * 
	 * @param req an HttpServletRequest object of the callee servlet
	 * @param resp an HttpServletResponse object of the callee servlet
	 * @return a list of loaded Picture objects
	 * @throws IOException if an I/O error occurs during loading of pictures
	 */
	public static List<Picture> loadPictures(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Picture> pictures = new ArrayList<>();
		
		List<String> lines = Files.readAllLines(Paths.get(
			req.getServletContext().getRealPath("/WEB-INF/opisnik.txt")
		));
		
		for (int i = 0, n = lines.size(); i < n; i += 3) {
			String url = lines.get(i).trim();
			String description = lines.get(i+1).trim();
			String[] tags = lines.get(i+2).trim().split("[,]|[,]//s");
			
			pictures.add(new Picture(url, description, tags));
		}
		
		req.getServletContext().setAttribute("pictures", pictures);
		
		return pictures;
	}
}
