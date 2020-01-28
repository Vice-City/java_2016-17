package hr.fer.zemris.java.webserver;

/**
 * Specified an object which knows how to process a request
 * using a specified RequestContext object.
 * 
 * @author Vice Ivušić
 *
 */
public interface IWebWorker {

	/**
	 * Processes a request using the specified context object.
	 * 
	 * @param context context object to use for processing request
	 * @throws Exception if any kind of exception occurs during processing
	 * 		   of request
	 */
	public void processRequest(RequestContext context) throws Exception;
}
