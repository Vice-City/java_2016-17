package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class which implements {@linkplain IWebWorker}. Generates a table
 * of name and values of parameters of configured context object.
 * 
 * @author Vice Ivušić
 *
 */
public class EchoParams implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) {
		
		context.setMimeType("text/html");
		
		try {
			context.write("<html><body>");
			context.write("<h1>Table</h1>");
			
			context.write("<table style=\"width:50%\" border =\"1\">");
			for (String name : context.getParameterNames()) {
				context.write("<tr>");
				
				context.write("<th>");
				context.write(name);
				context.write("</th>");
				
				context.write("<th>");
				context.write(context.getParameter(name));
				context.write("</th>");
				
				context.write("</tr>");
			}
			context.write("</table>");
			
			context.write("</body></html>");
		} catch (IOException ex) {
			throw new RuntimeException("EchoParams could not write to output stream!");
		}
	}
}