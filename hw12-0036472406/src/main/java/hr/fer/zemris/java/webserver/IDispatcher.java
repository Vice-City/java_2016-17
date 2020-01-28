package hr.fer.zemris.java.webserver;

/**
 * Specifies an object which knows how to handle a request for
 * a specified url string.
 * 
 * @author Vice Ivušić
 *
 */
public interface IDispatcher {

	/**
	 * Handles the request for the specified url string.
	 * 
	 * @param urlPath url to handle
	 * @throws Exception if any kind of exception occurs during handling
	 * 		   of request
	 */
	void dispatchRequest(String urlPath) throws Exception;
	
}
