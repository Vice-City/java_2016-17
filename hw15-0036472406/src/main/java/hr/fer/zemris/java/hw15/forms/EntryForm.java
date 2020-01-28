package hr.fer.zemris.java.hw15.forms;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * Form closely following the BlogEntry class.
 * 
 * @author Vice Ivušić
 *
 */
public class EntryForm extends AbstractForm {

	/**
	 * ID of the entry in question.
	 */
	private String id;
	
	/**
	 * Title of the entry in question.
	 */
	private String title;
	
	/**
	 * Text content of the entry in question.
	 */
	private String text;
	
	/**
	 * Fills this form with parameters obtained from the specified
	 * HttpServletRequest.
	 * 
	 * @param req HttpServletRequest containing parameters.
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		id = prepare(req.getParameter("id"));
		title = prepare(req.getParameter("title"));
		text = prepare(req.getParameter("text"));
	}
	
	/**
	 * Fills this form with available data in the specified object.
	 * 
	 * @param entry object containing relevant data for this form
	 */
	public void fillFromEntry(BlogEntry entry) {
		if (entry.getId() == null) {
			id = "";
		} else {
			id = entry.getId().toString();
		}
		
		title = entry.getTitle();
		text = entry.getText();
		
	}
	
	/**
	 * Fills the specified object with data relevant to this form.
	 * 
	 * @param entry object to be filled with data relevant to this form
	 */
	public void fillEntry(BlogEntry entry) {
		if (id.isEmpty()) {
			entry.setId(null);
		} else {
			entry.setId(Long.parseLong(id));
		}
		
		entry.setTitle(title);
		entry.setText(text);
		
	}
	
	@Override
	public void validate() {
		errors.clear();
		
		if (!id.isEmpty()) {
			try {
				Long.parseLong(id);
			} catch (NumberFormatException ex) {
				errors.put("id", "ID number is invalid!");
			}
		}
		
		
		if (title.isEmpty()) {
			errors.put("title", "Title cannot be empty!");
		} 
		
		if (text.isEmpty()) {
			errors.put("text", "Text cannot be empty!");
		}

	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	
}
