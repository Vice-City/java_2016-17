package hr.fer.zemris.java.hw13.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * A ServletContextListener which stores the time when it is first
 * run into the servlet context.
 * 
 * @author Vice Ivušić
 *
 */
@WebListener
public class RunningListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		
		context.setAttribute("runningTime", System.currentTimeMillis());
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		return;
	}

}
