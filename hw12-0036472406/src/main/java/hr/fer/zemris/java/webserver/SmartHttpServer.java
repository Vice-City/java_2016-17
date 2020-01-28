package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * A program which can start and stop a local server on the address
 * and port specified in config/server.properties. Along with server.properties,
 * this server expects mime.properties and workers.properties to be configured
 * in the same config/ directory. Behavior of the server in case these files
 * aren't supplied is not defined.
 * 
 * <p>The server can send files locates in its webroot, such as fruits.png,
 * index.html and sample.txt. It can also dinamically execute scripts located in its
 * scripts directory, such as scripts/brojPoziva.smscr and scripts/fibonacci.smscr.
 * 
 * <p>The server can also dynamically generate circles, tables, a hello world text
 * and add two arguments together. Arguments can be added by specifying name=value
 * pairs, separated by an ampersand (&). The first argument must be separated from
 * the rest of the url by a question mark (?).
 * 
 * <p>The server is started by typing in {@code start}; it is stopped by typing in
 * {@code stop}; and the program is exited by typing in {@code exit}.
 * 
 * @author Vice Ivušić
 *
 */
public class SmartHttpServer {
	/** the address this server can be reached at */
	private String address;
	/** the port where this server listens for requests */
	private int port;
	/** the amount of worker threads available at any given point */
	private int workerThreads;
	/** the amount of time before a session is considered as timed out */
	private int sessionTimeout;
	/** a map of mime types this server knows about */
	private Map<String, String> mimeTypes = new HashMap<>();
	/** the thread which runs the server and takes requests */
	private ServerThread serverThread;
	/** the thread pool used for worker threads */
	private ExecutorService threadPool;
	/** the root to the directory where the server's files are stored */
	private Path documentRoot;
	
	/** a worker map, mapping short names to actual workers */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	
	/** a map of sessions, mapping SIDs to session entries */
	private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap<>();
	/** a Random object used for generating random numbers */
	private Random sessionRandom = new Random();
	
	/** a flag used for killing the serverThread */
	private boolean keepServerAlive;
	
	/**
	 * Creates a new SmartHttpServer from the specified configuration file.
	 * 
	 * @param configFileName configuration file holding server properties
	 * @throws NullPointerException if the specified configuration file is null
	 */
	public SmartHttpServer(String configFileName) {
		if (configFileName == null) {
			throw new NullPointerException("Argument configFileName cannot be null!");
		}
		
		Properties serverProp = loadAndInitProperties(configFileName);
		
		address = serverProp.getProperty("server.address");
		port = Integer.parseInt(serverProp.getProperty("server.port"));
		workerThreads = Integer.parseInt(serverProp.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(serverProp.getProperty("session.timeout"));
		documentRoot = Paths.get(serverProp.getProperty("server.documentRoot")).toAbsolutePath();
		
		Properties mimeProp = loadAndInitProperties(serverProp.getProperty("server.mimeConfig"));
		
		for (Map.Entry<Object, Object> entry : mimeProp.entrySet()) {
			String extension = (String) entry.getKey();
			String mimeType = (String) entry.getValue();
			
			mimeTypes.put(extension, mimeType);
		}
		
		Properties workerProp = loadAndInitProperties(serverProp.getProperty("server.workers"));
		
		for (Map.Entry<Object, Object> entry : workerProp.entrySet()) {
			String path = (String) entry.getKey();
			String fqcnToken = (String) entry.getValue();
			
			if (workersMap.containsKey(path)) {
				throw new RuntimeException(
					"workers.properties file has same path mapping for multiple workers!"
				);
			}
			
			IWebWorker iww = loadWebWorker(fqcnToken);
			workersMap.put(path, iww);
		}
	}

	/**
	 * Helper method for loading a web worker from the specified
	 * fully qualified class name token.
	 * 
	 * @param fqcnToken fully qualified class name token
	 * @return an instance of the specified class
	 */
	private IWebWorker loadWebWorker(String fqcnToken) {
		IWebWorker iww;
		try {
			Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcnToken);
			Object newObject = referenceToClass.newInstance();
			iww = (IWebWorker) newObject;
		} catch (Exception ex) {
			throw new RuntimeException("Could not load class: "+fqcnToken);
		}
		
		return iww;
	}

	/**
	 * Helper method for loading and initializing a Properties object
	 * from the specified configuration file.
	 * 
	 * @param configFileName configuration file to read properties from
	 * @return a Properties object with properties from the specified
	 * 		   configuration file
	 */
	private static Properties loadAndInitProperties(String configFileName) {
		Path propPath = Paths.get(configFileName);
		
		Properties prop = new Properties();
		try {
			prop.load(Files.newBufferedReader(propPath, StandardCharsets.ISO_8859_1));
		} catch (IOException e) {
			throw new RuntimeException("Could not load: "+propPath.toAbsolutePath());
		}
		
		return prop;
	}
	

	/**
	 * Starts the server. If the server is already running, does nothing.
	 * 
	 */
	protected synchronized void start() {
		if (serverThread != null) {
			return;
		}
		
		serverThread = new ServerThread();
		keepServerAlive = true;
		threadPool = Executors.newFixedThreadPool(workerThreads);
		
		serverThread.start();
		
		Thread oldSessionCollector = new Thread(() -> {
			final int SECONDS_IN_FIVE_MINUTES = 60 * 5;
			while (true) {
				for (Entry<String, SessionMapEntry> entry : sessions.entrySet()) {
					if (entry.getValue().validUntil - new Date().getTime()/1000 < 0) {
						sessions.remove(entry.getKey());
					}
				}
				
				try {
					Thread.sleep(1000*SECONDS_IN_FIVE_MINUTES);
				} catch (InterruptedException ignorable) {}
			}
		});
		oldSessionCollector.setDaemon(true);
		oldSessionCollector.start();
		
	}

	/**
	 * Stops the server. If the served isn't running, does nothing.
	 * 
	 */
	protected synchronized void stop() {
		if (serverThread == null) {
			return;
		}
		
		keepServerAlive = false;
		threadPool.shutdown();
		sessions.clear();
		
		while (serverThread.isAlive()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignorable) {}
		}
		
		serverThread = null;
	}

	/**
	 * Models the thread which runs the server and listens for
	 * reqests.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	protected class ServerThread extends Thread {
		
		@Override
		public void run() {
			try (ServerSocket serverSocket = new ServerSocket()) {
				/*
				 * The server will block on accept only for 1 second
				 * at most, after which a SocketTimeoutException is
				 * thrown. This is so the server can reliably be
				 * stopped at any moment.
				 */
				serverSocket.setSoTimeout(1000);
				serverSocket.bind(new InetSocketAddress(
					(InetAddress) null,
					port
				));
				
				while (keepServerAlive) {
					Socket client;
					try {
						client = serverSocket.accept();
					} catch (SocketTimeoutException ex) {
						continue;
					}
					
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				throw new RuntimeException("Could not open server socket!");
			}
			
		}
	}

	/**
	 * Represents a worker which parses and executes a client's request.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/** socket through which the client's request came in */
		private Socket clientSocket;
		/** input stream from client */
		private PushbackInputStream inputStream;
		/** output stream toward client */
		private OutputStream outputStream;
		/** HTTP protocol version being used */
		private String version;
		/** HTTP method being used */
		private String method;
		/** map of parameters */
		private Map<String, String> params = new HashMap<>();
		/** map of temporary parameters */
		private Map<String, String> tempParams = new HashMap<>();
		/** map of persistent parameters */
		private Map<String, String> permParams = new HashMap<>();
		/** list of cookies */
		private List<RCCookie> outputCookies = new ArrayList<>();
		/** unique SID for current client */
		private String SID;
		
		/** context object for current client */
		private RequestContext context;

		/**
		 * Creates a new ClientWorker from the specified client socket.
		 * 
		 * @param clientSocket client socket for current worker
		 * @throws NullPointerException if the specified client socket is null
		 */
		public ClientWorker(Socket clientSocket) {
			if (clientSocket == null) {
				throw new NullPointerException("Argument clientSocket cannot be null!");
			}
			
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			getSocketStreams();
			
			List<String> request = readRequest();
			if (request == null || request.size() < 1) {
				sendError(400, "Header is invalid!");
				killWorker();
				return;
			}
			
			String firstLine = request.get(0).trim();
			checkSession(request);
			
			String[] tokens = firstLine.split(" ");
			if (tokens.length != 3) {
				sendError(400, "Invalid http request!");
				killWorker();
				return;
			}
			
			method = tokens[0];
			String requestedPath = tokens[1];
			version = tokens[2];
			
			if (!method.equals("GET")) {
				sendError(400, "Invalid method.");
				killWorker();
				return;
			}
			
			if (!version.equals("HTTP/1.0") && !version.equals("HTTP/1.1")) {
				sendError(400, "Invalid http version.");
				killWorker();
				return;
			}
			
			if (requestedPath.equals("/")) {
				sendWelcomeMessage();
				killWorker();
				return;
			}
			
			String[] pathTokens = requestedPath.split("[?]");
			
			String requestedUrlString = pathTokens[0];
			
			if (pathTokens.length > 1) {
				String paramString = pathTokens[1];
				parseParameters(paramString);
			}
			
			try {
				internalDispatchRequest(requestedUrlString, true);
			} catch (Exception e) {
				sendError(400, "Bad request.");
				throw new RuntimeException(
					"An error occured during request fulfilment! Stack trace: \n"+e.getStackTrace()
				);
			}

			killWorker();
		}
		
		/**
		 * Helper method for loading the socket streams from the socket
		 * opened toward the client.
		 */
		private void getSocketStreams() {
			try {
				inputStream = new PushbackInputStream(new BufferedInputStream(
					clientSocket.getInputStream()
				));
				outputStream = new BufferedOutputStream(
					clientSocket.getOutputStream()
				);
			} catch (IOException e) {
				throw new RuntimeException("Could not obtain input and output stream from socket!");
			}
		}

		/**
		 * Helper method for reading the header of a client's HTTP request.
		 * 
		 * @return list of lines that the header consists of
		 */
		private List<String> readRequest() {
			List<String> headerLines = new ArrayList<>();
			final int CARRIAGE_RETURN = 13;
			final int NEW_LINE = 10;
			
			boolean readCarriageReturn = false;
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while (true) {
					int read = inputStream.read();
					
					if (read == -1) {
						return null;
					}
					
					bos.write(read);
					
					if (read == CARRIAGE_RETURN) {
						readCarriageReturn = true;
					} else if (read != NEW_LINE ){
						readCarriageReturn = false;
					}
					
					if (read == NEW_LINE) {
						if (readCarriageReturn) {
							bos.flush();
							byte[] headerLineData = bos.toByteArray();
							bos.reset();
							
							String headerLine = new String(headerLineData, StandardCharsets.ISO_8859_1);
							headerLines.add(headerLine);
							
							if (headerLine.equals("\r\n")) {
								break;
							}
						}
						
						readCarriageReturn = false;
					} 
					
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			
			return headerLines;
		}

		/**
		 * Helper method for checking whether the server has a cookie
		 * stored for the client it is currently serving.
		 * 
		 * @param headerLines list of header lines sent by client
		 */
		private void checkSession(List<String> headerLines) {
			synchronized (ClientWorker.class) {
				String host = null;
				String sidCandidate = null;
				for (String line : headerLines) {
					if (line.startsWith("Host:")) {
						String[] tokens = line.split("[:]");
						host = tokens[1].trim();
						continue;
					}
					
					if (line.startsWith("Cookie:")) {
						if (!line.contains("sid=")) {
							continue;
						}
						
						sidCandidate = line.replaceFirst(".*sid[=][\"](.*)[\"].*", "$1").trim();
					}
				}
				
				if (sidCandidate != null) {
					determinePotentialSID(sidCandidate);
				}
				
				if (SID == null) {
					generateNewSID(host);
				}
				
				permParams = sessions.get(SID).permParamMap;
			}
			
		}

		/**
		 * Helper method for determining whether the specified SID has a
		 * mapped session entry that is still valid for current client or not.
		 * 
		 * @param sidCandidate SID being checked
		 */
		private void determinePotentialSID(String sidCandidate) {
			SessionMapEntry existingEntry = sessions.get(sidCandidate);
			
			if (existingEntry == null) {
				SID = null;
				return;
			} 
			
			if (existingEntry.validUntil - currentTimeInSeconds() < 0) {
				sessions.remove(SID);
				SID = null;
				return;
			}
			
			existingEntry.validUntil = currentTimeInSeconds() + sessionTimeout;
			SID = sidCandidate;
		}

		/**
		 * Helper method for generating a new SID for specified host.
		 * 
		 * @param host host that the SID is being generated for
		 */
		private void generateNewSID(String host) {
			final int UPPERCASE_LETTER_COUNT = 26;
			final int ASCII_UPPERCASE_OFFSET = 65;
			final int SID_LENGTH = 20;
			
			char[] sidChars = new char[SID_LENGTH];
			for (int i = 0; i < sidChars.length; i++) {
				sidChars[i] = (char) (Math.abs(sessionRandom.nextInt()) 
									  % UPPERCASE_LETTER_COUNT 
									  + ASCII_UPPERCASE_OFFSET);
			}
			
			SID = new String(sidChars);
			permParams.put("brojPoziva", "1");
			SessionMapEntry sessionEntry = new SessionMapEntry(
				SID,
				currentTimeInSeconds() + sessionTimeout,
				permParams
			);
			
			sessions.put(SID, sessionEntry);
			
			RequestContext rc = getContext();
			RCCookie cookie = new RCCookie(
				"sid",
				SID,
				null,
				host == null ? address : host,
				"/"
			);
			cookie.setHttpOnly(true);
			rc.addRCCookie(cookie);
		}
		
		/**
		 * Helper method for parsing the parameters contained in a URL string.
		 * 
		 * @param paramString the parameter part of a URL string (everything
		 * 		  after a question mark)
		 */
		private void parseParameters(String paramString) {
			String[] tokens = paramString.split("&");
			
			for (String token : tokens) {
				String[] keyValue = token.split("=");
				
				if (keyValue.length != 2) {
					continue;
				}
				
				params.put(keyValue[0], keyValue[1]);
			}
		}

		/**
		 * Helper method for serving a request for the specified parameters.
		 * 
		 * @param urlString url string for current request
		 * @param directCall flag indicating whether this method was called from within
		 * 		  this class or from outside it
		 * @throws Exception if any kind of exception occurs during dispatching of request
		 */
		private void internalDispatchRequest(String urlString, boolean directCall) throws Exception {
			if (urlString.startsWith("/private") && directCall == true) {
				sendError(404, "Not allowed access!");
				return;
			}
			
			if (urlString.matches("^[/]ext[/][^/]*$")) {
				serveExtWorker(urlString);
				return;
			}
			
			if (workersMap.containsKey(urlString)) {
				workersMap.get(urlString).processRequest(getContext());
				return;
			}
			
			Path urlPath = Paths.get(documentRoot.toString()+urlString);
			if (Files.notExists(urlPath) || Files.isDirectory(urlPath) || !Files.isReadable(urlPath)) {
				sendError(404, "File not found!");
				return;
			}
			
			String extension = urlPath.toString().replaceFirst(".*\\.(.*)$", "$1");
			
			if (extension.equals("smscr")) {
				serveSmartScript(urlPath);
				return;
			}
			
			String currentMimeType = mimeTypes.get(extension);
			if (currentMimeType == null) {
				currentMimeType = "application/octet-stream";
			}
			
			RequestContext rc = getContext();
			rc.setMimeType(currentMimeType);
			rc.setStatusCode(200);
			
			byte[] data = Files.readAllBytes(urlPath);
			rc.setByteLength(data.length);
			rc.write(data);
		}

		/**
		 * Helper method for serving a specific worker, when the urlString
		 * starts with /ext/.
		 * 
		 * @param urlString url string starting with /ext/
		 * @throws Exception if any kind of exception occurs during serving of request
		 */
		private void serveExtWorker(String urlString) throws Exception {
			String fqcnToken = "hr.fer.zemris.java.webserver.workers.";
			String className = urlString.replaceFirst("^[/]ext[/]([^/]*)$", "$1");
			
			fqcnToken = fqcnToken+className;
			
			IWebWorker iww = loadWebWorker(fqcnToken);
			
			iww.processRequest(getContext());
		}

		/**
		 * Helper method for executing and serving a smart script file.
		 * 
		 * @param urlPath path to smart script file
		 * @throws Exception if any kind of exception occurs during serving of request
		 */
		private void serveSmartScript(Path urlPath) throws Exception {
			String smartScript = new String(Files.readAllBytes(urlPath), StandardCharsets.UTF_8);
			DocumentNode node = new SmartScriptParser(smartScript).getDocumentNode();
			
			SmartScriptEngine engine = new SmartScriptEngine(
				node,
				getContext()
			);
			
			engine.execute();
		}

		/**
		 * Helper method for closing a client socket.
		 */
		private void killWorker() {
			while (!clientSocket.isClosed()) {
				try {
					clientSocket.close();
				} catch (IOException e) {
					continue;
				}
			}
		}

		/**
		 * Helper method for retrieving current client's context. Creates a new
		 * context if no context has yet been created.
		 * 
		 * @return current client's context
		 */
		private RequestContext getContext() {
			if (context != null) {
				return context;
			}
			
			context = new RequestContext(outputStream, params, permParams, outputCookies, tempParams, this);
			return context;
		}

		/**
		 * Helper method for sending a welcome message when the server
		 * is accessed using only the host and port name.
		 */
		private void sendWelcomeMessage() {
			String welcomeMessage = "Hello! You've reached my awesome, custom server.";
			
			RequestContext rc = getContext();
			rc.setMimeType(mimeTypes.get("txt"));
			
			byte[] data = welcomeMessage.getBytes(StandardCharsets.UTF_8);
			rc.setByteLength(data.length);
			
			try {
				rc.write(data);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Helper method for sending an error header and message to client.
		 * 
		 * @param statusCode wanted status code
		 * @param statusText wanted status text
		 */
		private void sendError(int statusCode, String statusText)  {
			RequestContext rc = getContext();
			rc.setStatusCode(statusCode);
			rc.setStatusText(statusText);
			rc.setMimeType("text/plain");
			
			try {
				rc.write("Whoops! Something went wrong.");
			} catch (IOException e) {
				throw new RuntimeException("Could not send error message!");
			}
		}

		/**
		 * Returns the current runtime time in seconds.
		 * 
		 * @return current time in seconds
		 */
		private long currentTimeInSeconds() {
			return new Date().getTime() / 1000;
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);			
		}
	}
	
	/**
	 * Helper class for storing information about a session.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class SessionMapEntry {
		/** sid associated with the session */
		@SuppressWarnings("unused")
		private String sid;
		/** time the session is valid for */
		private long validUntil;
		/** map of persistent parameters */
		private Map<String, String> permParamMap;
		
		/**
		 * Creates a new SessionMapEntry with the specified arguments.
		 * 
		 * @param sid sid associated with the session
		 * @param validUntil time the session is valid for
		 * @param permParamMap map of persistent parameters
		 */
		public SessionMapEntry(String sid, long validUntil, Map<String, String> permParamMap) {
			this.sid = sid;
			this.validUntil = validUntil;
			this.permParamMap = permParamMap;
		}
	}
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		SmartHttpServer server;
		try {
			server = new SmartHttpServer("config/server.properties");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		System.out.println("Welcome to server control! Valid commands are start, stop and exit.");
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("Input > ");
			String token = sc.next().toLowerCase().trim();
			
			if (token.equals("exit")) {
				server.stop();
				break;
			}
			
			if (token.equals("start")) {
				System.out.println(
					"Starting up server on "+server.address+":"+server.port+" if not already started..."
				);
				
				try {
					server.start();
				} catch (RuntimeException ex) {
					System.out.println();
					System.out.println(ex.getMessage());
				}
				
				continue;
			}
			
			if (token.equals("stop")) {
				System.out.println("Stopping server if it has been started...");
				server.stop();
				continue;
			}
			
			System.err.println("Only valid commands are start, stop and exit!");
		}
		sc.close();
	}
}