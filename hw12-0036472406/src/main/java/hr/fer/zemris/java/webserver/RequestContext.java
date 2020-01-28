package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an object that knows how to generate and HTTP header
 * and append any kind of bytes as the body of an HTTP message.
 * 
 * <p>Each RequestContext must be configured with a valid output stream
 * object, which the context will use for output of its messages. All the
 * other parameters will use default values, unless otherwise configured
 * through its methods.
 * 
 * <p>Also offers methods for writing bytes or a String into the configured
 * output stream.
 * 
 * @author Vice Ivušić
 *
 */
public class RequestContext {

	/** the output stream used for output of HTTP messages */
	private OutputStream outputStream;
	/** the charset to be used for encoding Strings passed onto the write method */
	private Charset charset;
	
	/** default encoding of UTF-8 */
	private static final String DEFAULT_ENCODING = "UTF-8";
	/** default status code of 200 */
	private static final int DEFAULT_STATUS_CODE = 200;
	/** default status text of OK */
	private static final String DEFAULT_STATUS_TEXT = "OK";
	/** default mime type of text/html */
	private static final String DEFAULT_MIME_TYPE = "text/html";
	
	/** the charset used for encoding strings passed onto the write method */
	private String encoding = DEFAULT_ENCODING;
	/** the status code to be used in the HTTP header */
	private Integer statusCode = DEFAULT_STATUS_CODE;
	/** the status text to be used in the HTTP header */
	private String statusText = DEFAULT_STATUS_TEXT;
	/** the mime type to be used in the HTTP header */
	private String mimeType = DEFAULT_MIME_TYPE;
	/** the length of the HTTP message's body in bytes; optional */
	private Integer byteLength;
	
	/** map of parameters */
	private Map<String, String> parameters;
	/** map of temporary parameters */
	private Map<String, String> temporaryParameters;
	/** map of persistent parameters */
	private Map<String, String> persistentParameters;
	
	/** list of cookies */
	private List<RCCookie> outputCookies;
	
	/** flag indicating a header has already been output by calling the write method */
	private boolean headerGenerated;
	
	/** an object which knows how to handle dispatch requests; optional */
	private IDispatcher dispatcher;
	
	/**
	 * Creates a new RequestContext from the specified parameters.
	 * 
	 * @param outputStream the output stream used for output of HTTP messages
	 * @param parameters map of parameters; will be empty if null is passed
	 * @param persistentParameters map of persistent parameters; will be empty if null is passed
	 * @param outputCookies list of cookies; will be empty if null is passed
	 * @throws NullPointerException if argument outputStream is null
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
						  Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
		//may throw NullPointerException
		this(outputStream, parameters, persistentParameters, outputCookies, new HashMap<>(), null);
	}
	
	/**
	 * Creates a new RequestContext from the specified parameters. This constructor
	 * additionally takes a temporaryParameters and dispatcher argument.
	 * 
	 * @param outputStream the output stream used for output of HTTP messages
	 * @param parameters map of parameters; will be empty if null is passed
	 * @param persistentParameters map of persistent parameters; will be empty if null is passed
	 * @param outputCookies list of cookies; will be empty if null is passed
	 * @param temporaryParameters map of temporary parameters; will be empty if null is passed
	 * @param dispatcher an object which knows how to handle dispatch requests; may be null
	 * @throws NullPointerException if argument outputStream is null
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
						  Map<String, String> persistentParameters, List<RCCookie> outputCookies,
						  Map<String, String> temporaryParameters, IDispatcher dispatcher) {
		if (outputStream == null) {
			throw new NullPointerException("Argument outputStream cannot be null!");
		}
		
		this.outputStream = outputStream;
		
		this.parameters = parameters == null
						  ? new HashMap<>()
						  : Collections.unmodifiableMap(parameters);
						  
		this.persistentParameters = persistentParameters == null
									? new HashMap<>()
									: persistentParameters;
									
		this.outputCookies = outputCookies == null
						 	 ? new ArrayList<>()
						 	 : outputCookies;
						 	 
		this.temporaryParameters = temporaryParameters == null
								   ? new HashMap<>()
								   : temporaryParameters;
								   
		this.dispatcher = dispatcher;
	}
	
	/**
	 * Sets this context's encoding to the specified encoding. If the encoding
	 * is null or if it is not a supported encoding, a default encoding of
	 * UTF-8 will be used.
	 * 
	 * @param encoding desired encoding
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	public void setEncoding(String encoding) {
		// may throw IllegalStateException
		checkHeaderGenerated();
		
		if (encoding == null || !Charset.isSupported(encoding)) {
			this.encoding = DEFAULT_ENCODING;
			return;
		}
		
		this.encoding = encoding;
	}
	
	/**
	 * Sets this context's status code to the specified status code. If the
	 * status code is null, a default status code of 200 will be used.
	 * 
	 * @param statusCode desired status code
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	public void setStatusCode(Integer statusCode) {
		// may throw IllegalStateException
		checkHeaderGenerated();
		
		if (statusCode == null) {
			this.statusCode = DEFAULT_STATUS_CODE;
			return;
		}
		
		this.statusCode = statusCode;
	}
	
	/**
	 * Sets this context's status text to the specified status text. If the
	 * status text is null, a default status text of "OK" will be used.
	 * 
	 * @param statusText desired status text
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	public void setStatusText(String statusText) {
		// may throw IllegalStateException
		checkHeaderGenerated();
		
		if (statusText == null) {
			this.statusText = DEFAULT_STATUS_TEXT;
			return;
		}
		
		this.statusText = statusText;
	}
	
	/**
	 * Sets this context's mime type to the specified mime type. If the
	 * specified mime type is null, a default mime type of text/html will be used.
	 * 
	 * @param mimeType desired mime type
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	public void setMimeType(String mimeType) {
		// may throw IllegalStateException
		checkHeaderGenerated();
		
		if (mimeType == null) {
			this.mimeType = DEFAULT_MIME_TYPE;
			return;
		}
		
		this.mimeType = mimeType;
	}
	
	/**
	 * Sets this context's byte length to the specified length. If the specified
	 * length is less than zero, the byte length is considered not to be specified
	 * at all, and the context will not generate a Content-Length tag.
	 * 
	 * @param byteLength desired byte length of this context's HTTP message body
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	public void setByteLength(Integer byteLength) {
		// may throw IllegalStateException
		checkHeaderGenerated();

		if (byteLength < 0) {
			this.byteLength = null;
			return;
		}
		
		this.byteLength = byteLength;
	}
	
	/**
	 * Helper method for checking whether a header has already been generated
	 * and output by calling one of the write methods.
	 * 
	 * @throws IllegalStateException if header has already been generated 
	 * 		   by calling {@linkplain #write(byte[])}
	 */
	private void checkHeaderGenerated() {
		if (headerGenerated) {
			throw new IllegalStateException("Cannot change property after calling write even once!");
		}
	}
	
	/**
	 * Returns the configured dispatcher object for this context. May return
	 * null if no dispatcher was configured.
	 * 
	 * @return configured dispatcher object for this context
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	
	/**
	 * Returns the value for the parameter with the specified name. May
	 * return null if no such value exists.
	 * 
	 * @param name name of the parameter to search for
	 * @return value of the parameter
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Returns a set of parameter names for this context. May be empty
	 * if no parameters were configured.
	 * 
	 * @return set of parameter names for this context
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}
	
	/**
	 * Returns the value for the persistent parameter with the specified name. May
	 * return null if no such value exists.
	 * 
	 * @param name name of the persistent parameter to search for
	 * @return value of the persistent parameter
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Returns a set of persistent parameter names for this context. May be empty
	 * if no persistent parameters were configured.
	 * 
	 * @return set of persistent parameter names for this context
	 */
	public Set<String> getPersistentParameterNames() {
		return Collections.unmodifiableSet(persistentParameters.keySet());
	}
	
	/**
	 * Maps the specified name to the specified value and puts the mapping
	 * into the persistent parameters map for this context. Both arguments
	 * may be null.
	 * 
	 * @param name name of the persistent parameter
	 * @param value value of the persistent parameter
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes the persistent parameter with the specified name. Does nothing
	 * if no such parameter exists.
	 * 
	 * @param name name of the persistent parameter to remove
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Returns the value for the temporary parameter with the specified name. May
	 * return null if no such value exists.
	 * 
	 * @param name name of the temporary parameter to search for
	 * @return value of the temporary parameter
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Returns a set of temporary parameter names for this context. May be empty
	 * if no temporary parameters were configured.
	 * 
	 * @return set of temporary parameter names for this context
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}
	
	/**
	 * Maps the specified name to the specified value and puts the mapping
	 * into the temporary parameters map for this context. Both arguments
	 * may be null.
	 * 
	 * @param name name of the temporary parameter
	 * @param value value of the temporary parameter
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes the temporary parameter with the specified name. Does nothing
	 * if no such parameter exists.
	 * 
	 * @param name name of the temporary parameter to remove
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * Writes the specified bytes into the configured output stream as
	 * part of an HTTP message's body. Will generate a header if called
	 * for the first time. Subsequent calls will not generate a header.
	 * Does nothing if the specified array of bytes is null.
	 * 
	 * @param data bytes to write
	 * @return current context
	 * @throws IOException if an error occured during writing to this
	 * 		   context's output stream
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (data == null) {
			return this;
		}
		
		if (!headerGenerated) {
			generateHeader();
		}
		
		outputStream.write(data);
		outputStream.flush();
		
		return this;
	}
	
	/**
	 * Writes the specified text into the configured output stream as
	 * part of an HTTP message's body, using the context's configured
	 * encoding. Will generate a header if called for the first time. 
	 * Subsequent calls will not generate a header. Does nothing
	 * if the specified text is null.
	 * 
	 * @param text text to write
	 * @return current context
	 * @throws IOException if an error occured during writing to this
	 * 		   context's output stream
	 */
	public RequestContext write(String text) throws IOException {
		if (text == null) {
			return this;
		}
		
		if (!headerGenerated) {
			generateHeader();
		}
		
		// generateHeader makes sure charset isn't null
		outputStream.write(text.getBytes(charset));
		outputStream.flush();
		
		return this;
	}
	
	/**
	 * Helper method for generating an HTTP header using the context's
	 * configured parameters.
	 * 
	 * @throws IOException if an error occured during writing to this
	 * 		   context's output stream
	 */
	private void generateHeader() throws IOException {
		// won't throw exceptions; all checks made during setting of charset
		charset = Charset.forName(encoding);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("HTTP/1.1 "+statusCode+" "+statusText+"\r\n");
		
		if (byteLength != null) {
			sb.append("Content-Length: "+byteLength+"\r\n");
		}
		
		sb.append("Content-Type: "+mimeType);
		if (mimeType.startsWith("text/")) {
			sb.append("; charset="+charset);
		}
		sb.append("\r\n");
		
		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: "+cookie.name+"=\""+cookie.value+"\"");
			
			if (cookie.domain != null) {
				sb.append("; Domain="+cookie.domain);
			}
			
			if (cookie.path != null) {
				sb.append("; Path="+cookie.path);
			}
			
			if (cookie.maxAge != null) {
				sb.append("; Max-Age="+cookie.maxAge);
			}
			
			if (cookie.httpOnly) {
				sb.append("; Http-Only");
			}
			
			sb.append("\r\n");
		}
		
		sb.append("\r\n");
		
		byte[] headerData = sb.toString().getBytes(StandardCharsets.ISO_8859_1);
		// may throw IOException
		outputStream.write(headerData);
		headerGenerated = true;
	}

	/**
	 * Represents a cookie which RequestContext knows how to use. Each
	 * cookie must have a name and an associated value, and optionally may
	 * have a domain, path, maximum age and the http-only flag set.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class RCCookie {
		
		/** name of this cookie */
		private String name;
		/** value of this cookie */
		private String value;
		/** domain this cookie should be associated with */
		private String domain;
		/** path this cookie should be associated with */
		private String path;
		
		/** maximum living time of this cookie */
		private Integer maxAge;
		
		/** flag indicating whether this is an http-only cookie or not */
		private boolean httpOnly;

		/**
		 * Creates a new RCCookie from the specified parameters.
		 * 
		 * @param name name of this cookie
		 * @param value value of this cookie
		 * @param maxAge maximum living time of this cookie
		 * @param domain domain this cookie should be associated with
		 * @param path path this cookie should be associated with
		 * @throws NullPointerException if either argument name or value is null
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			if (name == null || value == null) {
				throw new NullPointerException("Neither name nor value may be null!");
			}
			
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Returns this cookie's name.
		 * 
		 * @return this cookie's name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns this cookie's value.
		 * 
		 * @return this cookie's value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Returns this cookie's domain. May be null if it was not configured.
		 * 
		 * @return this cookie's domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Returns this cookie's path. May be null if it was not configured.
		 * 
		 * @return this cookie's path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Returns this cookie's maximum age. May be null if it was not configured.
		 * 
		 * @return this cookie's maximum age
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		/**
		 * Sets this cookie's http-only flag to the specified value.
		 * 
		 * @param httpOnly desired http-only flag
		 */
		public void setHttpOnly(boolean httpOnly) {
			this.httpOnly = httpOnly;
		}
	}

	/**
	 * Adds the specified rcCookie to this context's list of cookies.
	 * 
	 * @param rcCookie desired cookie to add
	 * @throws NullPointerException if argument rcCookie is null
	 */
	public void addRCCookie(RCCookie rcCookie) {
		if (rcCookie == null) {
			throw new NullPointerException("Argument rcCookie cannot be null!");
		}
		outputCookies.add(rcCookie);
	}
	
}
