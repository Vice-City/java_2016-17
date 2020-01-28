package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * Represents a database of student records.
 * 
 * <p>Offers methods for getting a student record according to the
 * student's JMBAG (executed in O(1) complexity) and it offers a 
 * method which returns a list of student records which satisfy
 * a given criteria.
 * 
 * <p>Also offers static methods for loading and retrieving a student database
 * from a text file, and for retrieving a default database of 63
 * students packaged in this project.
 * 
 * @author Vice Ivušić
 *
 */
public class StudentDatabase {

	/** list of student records **/
	private List<StudentRecord> database;
	/** index of student records, mapped to a student's JMBAG **/
	private SimpleHashtable<String, StudentRecord> index;
	
	/**
	 * Creates a new StudentDatabase from the specified list of student 
	 * records. The list of records has to contain one whole record
	 * for each of its entries. Each record has to have four tab
	 * separated values (jmbag, last name, first name and final grade).
	 * 
	 * @param records list of student records
	 * @throws IllegalArgumentException if the specified records are
	 * 		   null or not formatted properly
	 */
	public StudentDatabase(List<String> records) {
		if (records == null) {
			throw new IllegalArgumentException("Student records must not be null!");
		}
		
		database = new ArrayList<>(records.size());
		index = new SimpleHashtable<>(records.size());
		
		for (String record : records) {
			StudentRecord studentRecord = StudentRecord.parseAndBuildRecord(record);
			
			if (index.containsKey(studentRecord.getJmbag())) {
				throw new IllegalArgumentException(
						"Student database may contain only one record per student!"
				);
			}
			
			database.add(studentRecord);
			index.put(studentRecord.getJmbag(), studentRecord);
		}
	}
	
	/**
	 * Returns the student record for the specified JMBAG. Returns
	 * null if the record with the specified JMBAG does not exist
	 * within the student database.
	 * 
	 * @param jmbag JMBAG of the record in question
	 * @return student record of the student with the specified
	 * 		   JMBAG, or null if the record does not exist.
	 * @throws IllegalArgumentException if the specified JMBAG is null
	 */
	public StudentRecord forJMBAG(String jmbag) {
		if (jmbag == null) {
			throw new IllegalArgumentException("cannot get record for null!");
		}
		return index.get(jmbag);
	}
	
	/**
	 * Returns a list of student records which satisfy the specified
	 * IFilter object's accept method. Returns an empty list if none
	 * of the student records satisfy the given filter.
	 * 
	 * @param filter an object implementing the IFilter interface
	 * @return a list of the student records that satisfy the specified filter
	 */
	public List<StudentRecord> filter(IFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("cannot set filter to null!");
		}
		
		List<StudentRecord> filteredRecords = new ArrayList<>();
		
		for (StudentRecord record : database) {
			if (filter.accepts(record)) {
				filteredRecords.add(record);
			}
		}
		
		return filteredRecords;
	}
	
	/**
	 * Loads and returns the default student database from the text
	 * file located in the scope of this homework's project. Returns
	 * null if the default database's text file is not located at
	 * src/main/resources/database.txt.
	 * 
	 * @return default student database
	 */
	public static StudentDatabase loadDefaultDatabase() {
		try {
			return loadDatabase("src/main/resources/database.txt");
		} catch (IOException ignorable) {
			//this should never be thrown for the default database
			return null;
		}
	}

	/**
	 * Loads and returns the student database from the specified
	 * path.
	 * 
	 * @param path path to the location of the student database text file
	 * @return student database generated from the loaded text file
	 * @throws IllegalArgumentException if the specified path is null
	 * 		   or if the text file contains improperly formatted records
	 * @throws IOException if the text file cannot be read from the
	 * 		   specified path
	 */
	public static StudentDatabase loadDatabase(String path) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("Cannot load database from null path!");
		}
		
		List<String> records;
		
		records = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
		
		return new StudentDatabase(records);
	}
	
}
