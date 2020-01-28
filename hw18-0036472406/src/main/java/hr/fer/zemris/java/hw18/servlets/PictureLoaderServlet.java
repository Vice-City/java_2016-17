package hr.fer.zemris.java.hw18.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HTTPServlet which loads a picture's content from the URL
 * passed on with a parameter.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="pictureServlet", urlPatterns={"/servlets/picture"})
public class PictureLoaderServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getParameter("url");
		
		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/pictures/"+url));
			
		resp.setContentType("image/png");
		resp.getOutputStream().write(Files.readAllBytes(path));
		resp.getOutputStream().flush();
	}
}
