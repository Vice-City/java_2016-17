package hr.fer.zemris.java.hw04.db;

/**
 * Represents a single student record containing a student's
 * JMBAG, last name, first name and final grade.
 * 
 * @author Vice Ivušić
 *
 */
public class StudentRecord {

	/** student's JMBAG **/
	private String jmbag;
	/** student's last name **/
	private String lastName;
	/** student's first name **/
	private String firstName;
	/** student's final grade **/
	private int finalGrade;
	
	/**
	 * Creates a new StudentRecord with the specified parameters.
	 * 
	 * @param jmbag student's JMBAG
	 * @param lastName student's last name
	 * @param firstName student's first name
	 * @param finalGrade student's final grade
	 * @throws IllegalArgumentException if any of the arguments is null
	 * 		   or if the final grade isn't between 1 and 5
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		if (jmbag==null || lastName==null || firstName==null) {
			throw new IllegalArgumentException("At least one argument is null!");
		}
		if (finalGrade < 1 || finalGrade > 5) {
			throw new IllegalArgumentException("Grade must be between 1 and 5!");
		}
		
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Returns this record's JMBAG.
	 * 
	 * @return student's JMBAG
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Returns this record's last name.
	 * 
	 * @return student's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns this record's first name.
	 * 
	 * @return student's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns this record's final grade.
	 * 
	 * @return student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	/**
	 * Parses and returns a new StudentRecord from the given formatted string.
	 * The string has to have four tab separated values, representing in order:
	 * student's JMBAG, student's last name, student's first name, and student's
	 * final grade. 
	 *  
	 * @param record tab separated string with student attributes
	 * @return StudentRecord built from specified string
	 * @throws IllegalArgumentException if the specified record is
	 * 		   null or not formatted properly
	 */
	public static StudentRecord parseAndBuildRecord(String record) {
		if (record == null) {
			throw new IllegalArgumentException("Record must not be null!");
		}
		
		String jmbag;
		String lastName;
		String firstName;
		int grade;
		
		String[] tokens = record.split("\\t");
		
		if (tokens.length != 4) {
			throw new IllegalArgumentException(
					"At least one record not formatted properly! Record: " + record
			);
		}
		
		jmbag = tokens[0];
		lastName = tokens[1];
		firstName = tokens[2];
		try {
			grade = Integer.parseInt(tokens[3]);
			if (grade < 1 || grade > 5) {
				throw new IllegalArgumentException("Grade must be between 1 and 5!");
			}
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Grade is not an integer value!");
		}
		
		return new StudentRecord(
				jmbag, 
				lastName, 
				firstName,
				grade
		);
	}
	
	@Override
	public int hashCode() {
		return jmbag.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof StudentRecord)) return false;
		
		StudentRecord other = (StudentRecord) obj;
		
		return jmbag.equals(other.jmbag);
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s, %d", jmbag, lastName, firstName, finalGrade);
	}
}
