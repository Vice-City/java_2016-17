package hr.fer.zemris.java.hw05.demo4;

/**
 * Represents a student's record containing the student's JMBAG,
 * last name, first name, midterm exam points, final exam points,
 * laboratory exercise points and final grade.
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
	
	/** student's midterm exam score **/
	private double midtermExamPoints;
	/** student's final exam score **/
	private double finalExamPoints;
	/** student's laboratory exercises score **/
	private double labPoints;
	
	/** student's final grade in class **/
	private int finalGrade	;

	/**
	 * Creates a new StudentRecord with the specified parameters.
	 * 
	 * @param jmbag student's jmbag
	 * @param lastName student's last name
	 * @param firstName student's first name
	 * @param midtermExamPoints student's midterm exam points
	 * @param finalExamPoints student's final exam points
	 * @param labPoints student's laboratory exercises points
	 * @param finalGrade student's final grade
	 * 
	 * @throws IllegalArgumentException if any of the arguments are null or
	 * 		   if the grade isn't between 1 and 5
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, 
			double midtermExamPoints, double finalExamPoints, double labPoints, 
			int finalGrade) {
		if (jmbag == null || lastName == null || firstName == null) {
			throw new IllegalArgumentException("At least one string argument is null!");
		}
		
		if (finalGrade < 1 || finalGrade > 5) {
			throw new IllegalArgumentException("Grade must be between 1 and 5!");
		}
		
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midtermExamPoints = midtermExamPoints;
		this.finalExamPoints = finalExamPoints;
		this.labPoints = labPoints;
		this.finalGrade = finalGrade;
	}

	/**
	 * Returns this record's JMBAG.
	 * 
	 * @return this record's JMBAG
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Returns this record's last name.
	 * 
	 * @return this record's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns this record's first name.
	 * 
	 * @return this record's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns this record's midterm exam points.
	 * 
	 * @return this record's midterm exam points
	 */
	public double getMidtermExamPoints() {
		return midtermExamPoints;
	}

	/**
	 * Returns this record's final exam points.
	 * 
	 * @return this record's final exam points
	 */
	public double getFinalExamPoints() {
		return finalExamPoints;
	}

	/**
	 * Returns this record's lab exercise points.
	 * 
	 * @return this record's lab exercise points
	 */
	public double getLabPoints() {
		return labPoints;
	}

	/**
	 * Returns this record's total points, i.e. the points
	 * achieved in the midterm exam, the final exam and
	 * the laboratory exercises all added up.
	 * 
	 * @return this record's total points
	 */
	public double getTotalPoints() {
		return midtermExamPoints + finalExamPoints + labPoints;
	}

	/**
	 * Returns this record's final grade.
	 * 
	 * @return this record's final grades
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	@Override
	public String toString() {
		return String.format(
				"%s, %s, %s, %.3f, %.3f, %.3f, %.3f, %d",
				jmbag,
				lastName,
				firstName,
				midtermExamPoints,
				finalExamPoints,
				labPoints,
				getTotalPoints(),
				finalGrade
		);
	}
}
