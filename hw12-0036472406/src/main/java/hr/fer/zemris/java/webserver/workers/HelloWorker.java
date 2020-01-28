package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class which implements {@linkplain IWebWorker}. Generates text
 * that is different depending on if a parameter was set. If a parameter
 * was set, it displays how many letters the parameter's value consists of.
 * 
 * @author Vice Ivušić
 *
 */

public class HelloWorker implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		
		context.setMimeType("text/html");
		String name = context.getParameter("name");
		
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<p>Now is: " + sdf.format(now) + "</p>");
			
			if (name == null || name.trim().isEmpty()) {
				context.write("<p>You did not send me your name!</p>");
			} else {
				context.write("<p>Your name has " + name.trim().length() + " letters.</p>");
			}
			
			context.write("</body></html>");
		} catch (IOException ex) {
			throw new RuntimeException("HelloWorker could not write to output stream!");
		}
	}
}