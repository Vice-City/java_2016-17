package hr.fer.zemris.java.hw18.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw18.model.Picture;

/**
 * And HTTPServlet which delivers information about a picture in JSON format,
 * looking at the url parameters for information about which picture is needed.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="pictureInfoServlet", urlPatterns={"/servlets/pictureInfo"})
public class PictureInfoServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<Picture> pictures = (List<Picture>) req.getServletContext().getAttribute("pictures");
		String url = req.getParameter("url");
		
		if (pictures == null) {
			pictures = TagsServlet.loadPictures(req, resp);
		}
		
		for (Picture p : pictures) {
			if (p.getUrl().equals(url)) {
				resp.setContentType("application/json");
				resp.getWriter().write(new Gson().toJson(p));
				resp.getWriter().flush();
				return;
			}
		}
	}
}
