package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.forms.CommentForm;
import hr.fer.zemris.java.hw15.forms.EntryForm;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * A web servlet which processes all requests headed to /servleti/author/*.
 * This servlet prepares blog user data as well as relevant user's
 * blog entries and associated comments.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="authorServlet", urlPatterns={"/servleti/author/*"})
public class AuthorServlet extends HttpServlet {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainMethod(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		mainMethod(req, resp);
	}

	/**
	 * Helper method that processes both GET and POST http requests.
	 * 
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void mainMethod(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo().replaceFirst("[/]",	"").replaceAll("[/]$", "");
		String[] pathTokens = pathInfo.split("[/]");
		
		req.setCharacterEncoding("UTF-8");
		
		if (pathTokens.length > 0) {
			String nickname = pathTokens[0];
			
			try {
				BlogUser user = DAOProvider.getDAO().getBlogUser(nickname);
				if (user == null) {
					req.setAttribute("errorMessage", "User with specified nickname does not exist!");
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
					return;
				}
				
			} catch (DAOException ex) {
				req.setAttribute("errorMessage", ex.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}

		}
		
		if (pathTokens.length == 1) {
			serveNicknameEntries(pathTokens[0], req, resp);
			return;
		}
		
		if (pathTokens.length == 2) {
			String token = pathTokens[1].toLowerCase();
			
			if (token.equals("new")) {
				serveNewEntry(pathTokens[0], req, resp);
				return;
			}
			
			if (token.equals("edit")) {
				serveEditEntry(pathTokens[0], req, resp);
				return;
			}
			
			if (token.equals("save")) {
				serveSaveEntry(pathTokens[0], req, resp);
				return;
			}
			
			serveEntryAndComments(pathTokens[0], token, req, resp);
			return;

		}
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

	/**
	 * Helper method for serving requests headed for servleti/servlet/author/NICK/.
	 * 
	 * @param nick nickname that the servlet was requested with
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void serveNicknameEntries(String nick, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogEntry> entriesByNick; 
		try {
			entriesByNick = DAOProvider.getDAO().getBlogUser(nick).getBlogEntries();
		} catch (DAOException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		req.setAttribute("entriesByNick", entriesByNick);
		req.setAttribute("servingNick", nick);
		
		req.getRequestDispatcher("/WEB-INF/pages/entries.jsp").forward(req, resp);
	}

	/**
	 * Helper method for serving requests headed for servleti/servlet/author/NICK/new.
	 * 
	 * @param nick nickname that the servlet was requested with
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void serveNewEntry(String nick, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!nick.equals(req.getSession().getAttribute("currentUserNick"))) {
			req.setAttribute("errorMessage", "Insufficient permissions to create new blog entry!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		EntryForm entryForm = new EntryForm();
		entryForm.fillFromEntry(new BlogEntry());
		req.setAttribute("entryForm", entryForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/submitEntry.jsp").forward(req, resp);
	}

	/**
	 * Helper method for serving requests headed for servleti/servlet/author/NICK/edit.
	 * 
	 * @param nick nickname that the servlet was requested with
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void serveEditEntry(String nick, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!nick.equals(req.getSession().getAttribute("currentUserNick"))) {
			req.setAttribute("errorMessage", "Insufficient permissions to edit blog entry!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		Long entryId;
		try {
			entryId = Long.parseLong(req.getParameter("entryId"));
		} catch (NumberFormatException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		BlogEntry existingEntry;
		try {
			existingEntry = DAOProvider.getDAO().getBlogEntry(entryId);
		} catch (DAOException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		if (existingEntry == null) {
			req.setAttribute("errorMessage", "Entry with specified entry ID does not exist!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}

		EntryForm entryForm = new EntryForm();
		entryForm.fillFromEntry(existingEntry);
		
		req.setAttribute("servingNick", nick);
		req.setAttribute("entryForm", entryForm);
		
		req.getRequestDispatcher("/WEB-INF/pages/submitEntry.jsp").forward(req, resp);
	}

	/**
	 * Helper method for serving requests headed for servleti/servlet/author/NICK/save.
	 * 
	 * @param nick nickname that the servlet was requested with
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void serveSaveEntry(String nick, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!nick.equals(req.getSession().getAttribute("currentUserNick"))) {
			req.setAttribute("errorMessage", "Insufficient permissions to edit blog entries!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		String redirectPath = req.getContextPath() + "/servleti/author/"+nick;
		
		String method = req.getParameter("method");
		if ("Save".equals(method)) {
			EntryForm entryForm = new EntryForm();
			entryForm.fillFromHttpRequest(req);
			entryForm.validate();
			
			System.out.println("Serving save...");
			
			if (entryForm.errorsPresent()) {
				req.setAttribute("entryForm", entryForm);
				req.getRequestDispatcher("/WEB-INF/pages/submitEntry.jsp").forward(req, resp);
				return;
			}
			
			System.out.println("No errors are present...");
			System.out.println("Entry ID = "+entryForm.getId());
			
			if (entryForm.getId().isEmpty()) {
				System.out.println("Creating new entry!");
				BlogEntry blogEntry = new BlogEntry();
				entryForm.fillEntry(blogEntry);
				
				blogEntry.setCreatedAt(new Date());
				try {
					blogEntry.setCreator(DAOProvider.getDAO().getBlogUser(nick));
					DAOProvider.getDAO().save(blogEntry);
				} catch (DAOException ex) {
					req.setAttribute("errorMessage", ex.getMessage());
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
					return;
				}
				
				redirectPath = req.getContextPath() + "/servleti/author/"+nick;
				
			} else {
				BlogEntry existingEntry;
				try {
					existingEntry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(entryForm.getId()));
					entryForm.fillEntry(existingEntry);
					existingEntry.setLastModifiedAt(new Date());
					DAOProvider.getDAO().updateEntry(existingEntry);
				} catch (DAOException ex) {
					req.setAttribute("errorMessage", ex.getMessage());
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
					return;
				}
				
				
				redirectPath = req.getContextPath() + "/servleti/author/"+nick+"/"+existingEntry.getId();
			}
		}
		
		resp.sendRedirect(redirectPath);
	}

	/**
	 * Helper method for serving requests headed for servleti/servlet/author/NICK/EID.
	 * 
	 * @param nick nickname that the servlet was requested with
	 * @param entryIdToken ID of the entry that was requested
	 * @param req the HttpServletRequest object received by the http request
	 * @param resp the HttpServletResponse object received by the http request
	 * @throws ServletException if an error occurs with the servlet connection
	 * @throws IOException if an input/output error occurs
	 */
	private void serveEntryAndComments(String nick, String entryIdToken, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long entryId;
		try {
			entryId = Long.parseLong(entryIdToken);
		} catch (NumberFormatException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		BlogEntry currentEntry;
		BlogUser user;
		try {
			currentEntry = DAOProvider.getDAO().getBlogEntry(entryId);
			user = DAOProvider.getDAO().getBlogUser(nick);
		} catch (DAOException ex) {
			req.setAttribute("errorMessage", ex.getMessage());
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		if (currentEntry == null) {
			req.setAttribute("errorMessage", "Entry with specified ID does not exist!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		if (!user.equals(currentEntry.getCreator())) {
			req.setAttribute("errorMessage", "Entry with specified ID does not belong to specified user!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		List<BlogComment> blogComments = currentEntry.getComments();
		
		req.setAttribute("servingNick", nick);
		req.setAttribute("currentEntry", currentEntry);
		req.setAttribute("blogComments", blogComments);
		
		String method = req.getParameter("method");
		if ("Post".equals(method)) {
			CommentForm commentForm = new CommentForm();
			commentForm.fillFromHttpRequest(req);
			commentForm.validate();
			
			if (commentForm.errorsPresent()) {
				req.setAttribute("commentForm", commentForm);
				req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
				return;
			}
			
			BlogComment blogComment = new BlogComment();
			commentForm.fillComment(blogComment);
			blogComment.setBlogEntry(currentEntry);
			blogComment.setPostedOn(new Date());
			
			try {
				DAOProvider.getDAO().save(blogComment);
			} catch (DAOException ex) {
				req.setAttribute("errorMessage", ex.getMessage());
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				return;
			}
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/"+nick+"/"+entryIdToken+"/");
			return;
		} else {
			CommentForm commentForm = new CommentForm();
			commentForm.fillFromComment(new BlogComment());
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
	}
}
