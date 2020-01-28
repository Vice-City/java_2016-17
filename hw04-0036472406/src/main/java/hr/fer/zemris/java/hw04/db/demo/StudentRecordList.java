package hr.fer.zemris.java.hw04.db.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.java.hw04.db.StudentRecord;

/**
 * Helper class which stores student records. It keeps track of
 * particular details of the student records stored in it and
 * it offers a method which generates a table of all the 
 * student records stored in the list. Does not offer an index
 * of records or anything fancy like that.
 * 
 * <p>Records can only be added to this list. Once they're inside,
 * they aren't coming out.
 * 
 * @author Vice Ivušić
 *
 */
public class StudentRecordList implements Iterable<StudentRecord> {
	
	/** length of the longest JMBAG **/
	private int longestJMBAG;
	/** length of the longest last name **/
	private int longestLastName;
	/** length of the longest first name **/
	private int longestFirstName;
	/** length of longest grade **/
	private int longestGrade;
	
	/** internal list for storage of student records **/
	private List<StudentRecord> records;
	
	/**
	 * Creates a new empty StudentRecordList.
	 */
	public StudentRecordList() {
		records = new ArrayList<>();
	}
	
	/**
	 * Adds the specified record to the list.
	 * 
	 * @param record student record to be added
	 */
	public void add(StudentRecord record) {
		int jmbagLength = record.getJmbag().length();
		if (jmbagLength > longestJMBAG) {
			longestJMBAG = jmbagLength;
		}
		
		int lastNameLength = record.getLastName().length();
		if (lastNameLength > longestLastName) {
			longestLastName = lastNameLength;
		}
		
		int firstNameLength = record.getFirstName().length();
		if (firstNameLength > longestFirstName) {
			longestFirstName = firstNameLength;
		}
		
		int gradeLength = String.valueOf(record.getFinalGrade()).length();
		if (gradeLength > longestGrade) {
			longestGrade = gradeLength;
		}
		
		records.add(record);
	}
	
	/**
	 * Returns number of records stored in this list.
	 * 
	 * @return number of stored records
	 */
	public int size() {
		return records.size();
	}

	@Override
	public Iterator<StudentRecord> iterator() {
		return records.iterator();
	}
	
	/**
	 * Returns a string representation of the contents of this
	 * list in the form of a neatly formatted table.
	 * 
	 * @return string representation of the list as a table
	 */
	public String generateTable() {
		
		StringBuilder tableFrame = new StringBuilder();
		tableFrame.append('+');
		for (int i = 0; i < longestJMBAG+2; i++) {
			tableFrame.append('=');
		}
		tableFrame.append('+');
		for (int i = 0; i < longestLastName+2; i++) {
			tableFrame.append('=');
		}
		tableFrame.append('+');
		for (int i = 0; i < longestFirstName+2; i++) {
			tableFrame.append('=');
		}
		tableFrame.append('+');
		for (int i = 0; i < longestGrade+2; i++) {
			tableFrame.append('=');
		}
		tableFrame.append("+\n");
		
		StringBuilder sb = new StringBuilder();
		sb.append(tableFrame);
		
		for (StudentRecord record : records) {
			sb.append("| ");
			sb.append(record.getJmbag());
			for (int i = record.getJmbag().length(); i < longestJMBAG; i++) {
				sb.append(" ");
			}
			sb.append(" | ");
			
			sb.append(record.getLastName());
			for (int i = record.getLastName().length(); i < longestLastName; i++) {
				sb.append(" ");
			}
			sb.append(" | ");
			
			sb.append(record.getFirstName());
			for (int i = record.getFirstName().length(); i < longestFirstName; i++) {
				sb.append(" ");
			}
			sb.append(" | ");
			
			sb.append(record.getFinalGrade());
			for (int i = String.valueOf(record.getFinalGrade()).length(); i < longestGrade; i++) {
				sb.append(" ");
			}
			sb.append(" |\n");
		}
		
		sb.append(tableFrame);
		
		return sb.toString();
	}
	
}