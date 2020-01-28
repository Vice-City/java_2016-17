package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Models a JTextArea with some additional capabilities specifically suited
 * to the {@link JNotepadPP} class. Offers methods for retrieving and setting
 * its updated flag (whether the text in its document model has been modified
 * or not), for retrieving and setting its save path, for determing whether
 * it is saved at all, and for retrieving its statistics in the form of
 * a {@link Statistics} object.
 * 
 * @author Vice Ivušić
 *
 */
public class JTextAreaPP extends JTextArea {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/** represents the path where this text area's document is saved on disk */
	private Path savedPath;
	
	/** 
	 * flag indicating whether there have been changes since the last time
	 * this text area's document was saved on disk
	 */
	private boolean updated;
	
	/**
	 * Returns true if there have been no changes since the last time 
	 * this text area's document was saved on disk. It is the user's
	 * responsibility for ensuring consistency by using the setUpdated method.
	 * 
	 * @return true iff there have been no changes since the last time this
	 * 		   text area's document was saved on disk
	 */
	public boolean isUpdated() {
		return updated;
	}
	
	/**
	 * Sets this text area's updated flag to the specified value. Should
	 * only be set if the user has determined a change in the current text
	 * area's document model has occurred.
	 * 
	 * @param updated desired value for the updated flag
	 */
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
	/**
	 * Returns true if this text area's document has been saved on disk.
	 * It is the user's responsibility to ensure consistency by using 
	 * the setSavedPath method.
	 * 
	 * @return true iff this text area's document has been saved on disk.
	 */
	public boolean isSavedOnDisk() {
		return savedPath != null;
	}
	
	/**
	 * Returns a Path object representing the absolute path where this text
	 * area's document has been saved on disk. Should be used in conjunction 
	 * with the isSavedOnDisk method, as this method will return {@code null}
	 * if this text area's document hasn't been saved anywhere. It is the user's
	 * responsibility to ensure consistency by using the setSavedPath method.
	 * 
	 * @return Path object representing the absolute path where this text area's
	 * 		   document has been saved, or {@code null} if it hasn't been saved anywhere
	 */
	public Path getSavePath() {
		return savedPath;
	}
	
	/**
	 * Sets the saved path for this text area's document to the specified path. The
	 * specified path may be {@code null}.
	 * 
	 * @param savedPath desired save path for this text area's document
	 */
	public void setSavedPath(Path savedPath) {
		this.savedPath = savedPath.toAbsolutePath();
	}
	
	/**
	 * Calculates and returns a {@code Statistics} object for current text area's
	 * document, which can be inquired for further data such as the document's
	 * length, non-blank character count and line count.
	 * 
	 * @return a Statistics object for the current text area's document
	 */
	public Statistics getStatistics() {
		Document doc = getDocument();
		
		String docText = null;
		try {
			docText = doc.getText(0, doc.getLength());
		} catch (BadLocationException ignorable) {}
		
		char[] characters = docText.toCharArray();
		
		int numberOfChars = characters.length;
		
		int numberOfCharsWithoutBlanks = 0;
		int numberOfLines = 0;
		for (char c : characters) {
			if (!Character.isWhitespace(c)) {
				numberOfCharsWithoutBlanks++;
				
			}
			
			if (c == '\n') {
				numberOfLines++;
			}
		}
		
		return new Statistics(numberOfChars, numberOfCharsWithoutBlanks, numberOfLines);
		
	}
	
	/**
	 * Helper class for holding a {@link JTextAreaPP}'s statistics, such as
	 * its length, non-blank character count and line count.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class Statistics {
		
		/** a document's length */
		private int length;
		/** a document's non-blank character count */
		private int nonWhitespaceChars;
		/** a document's line count */
		private int numberOfLines;
		
		/**
		 * Creates a new Statistics object with the specified values.
		 * Assumes the user will pass on valid, i.e. non-negative, values and
		 * gives no warnings otherwise.
		 * 
		 * @param length a document's length
		 * @param nonWhitespaceChars a document's non-blank character count
		 * @param numberOfLines a document's line count
		 */
		private Statistics(int length, int nonWhitespaceChars, int numberOfLines) {
			this.length = length;
			this.nonWhitespaceChars = nonWhitespaceChars;
			this.numberOfLines = numberOfLines;
		}

		/**
		 * Returns this statistic's document length.
		 * 
		 * @return statistic's document length
		 */
		public int getLength() {
			return length;
		}

		/**
		 * Returns this statistic's non-blank character count.
		 * 
		 * @return statistic's non-blank character count
		 */
		public int getNonWhitespaceChars() {
			return nonWhitespaceChars;
		}

		/**
		 * Returns this statistic's line count.
		 * 
		 * @return statistic's line count
		 */
		public int getNumberOfLines() {
			return numberOfLines;
		}
		
	}
}
