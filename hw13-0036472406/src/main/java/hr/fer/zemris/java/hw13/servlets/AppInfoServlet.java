package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which calculates the running time of the webapp it is deployed in. Expects
 * a {@linkplain RunningListener} class to be implemented and active in order for the servlet
 * to be able to calculate the running time.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="appInfoServlet", urlPatterns={"/appinfo.jsp"})
public class AppInfoServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long startUpTime = (long) req.getServletContext().getAttribute("runningTime");
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - startUpTime;
		
		long seconds = elapsedTime / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		
		String result = String.format(
			"%d days, %d hours, %d minutes, %d seconds and %d miliseconds",
			days,
			hours % 24,
			minutes % 60,
			seconds % 60,
			elapsedTime % 1000
		);
		
		req.setAttribute("elapsedTime", result);
		
		req.getRequestDispatcher("/WEB-INF/pages/appinfo.jsp").forward(req, resp);
	}
	

}
