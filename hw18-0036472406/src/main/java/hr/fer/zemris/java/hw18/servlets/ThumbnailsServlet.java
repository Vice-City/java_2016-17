package hr.fer.zemris.java.hw18.servlets;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw18.model.Picture;

/**
 * An HttpServlet which loads a list of all the available thumbnail URLs for
 * the given tag and delivers it to the callee in JSON format.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="thumbnailsServlet", urlPatterns={"/servlets/thumbnails"})
public class ThumbnailsServlet extends HttpServlet {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<Picture> pictures = (List<Picture>) req.getServletContext().getAttribute("pictures");
		String tag = req.getParameter("tag");
		
		if (pictures == null) {
			pictures = TagsServlet.loadPictures(req, resp);
		}
		
		Set<String> urls = new LinkedHashSet<>();
		
		for (Picture p : pictures) {
			for (String t : p.getTags()) {
				if (t.equals(tag)) {
					urls.add(p.getUrl());
					break;
				}
			}
		}
		
		resp.setContentType("application/json");
		resp.getWriter().write(new Gson().toJson(urls));
		resp.getWriter().flush();
	}
}
