package hr.fer.zemris.java.hw18.servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which loads and, if necessary, generates a thumbnail for
 * a picture with the given URL.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="thumbnailServlet", urlPatterns={"/servlets/thumbnail"})
public class ThumbnailGeneratorServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getParameter("url");
		
		Path thumbnailPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails/"));
		
		if (Files.notExists(thumbnailPath)) {
			Files.createDirectory(thumbnailPath);
		}
		
		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/thumbnails/"+url));
		BufferedImage scaledImage;
		if (Files.notExists(path)) {
			Path fullImgPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/pictures/"+url));
			BufferedImage original = ImageIO.read(fullImgPath.toFile());
			
			Image scaled = original.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
			
			scaledImage = new BufferedImage(150, 150, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2d = (Graphics2D) scaledImage.getGraphics();
			g2d.drawImage(scaled, 0, 0, null);
			
			ImageIO.write(scaledImage, "png", path.toFile());
		} else {
			scaledImage = ImageIO.read(path.toFile());
		}
		
		resp.setContentType("image/png");
		ImageIO.write(scaledImage, "png", resp.getOutputStream());
		resp.getOutputStream().flush();
	}

}
