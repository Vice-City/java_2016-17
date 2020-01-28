package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple program which loads the student records available
 * from src/main/resources/students.txt and prints out some
 * information about the students onto the standard output.
 * 
 * <p>In case of improperly formatted student records, the
 * program simply skips said records without any kind of
 * warning. This should be irrelevant for the default
 * student database.
 * 
 * @author Vice Ivušić
 *
 */
public class StudentDemo {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		List<String> lines;
		try {
			lines = Files.readAllLines(
							Paths.get("src/main/resources/students.txt"),
							StandardCharsets.UTF_8
			);
		} catch (IOException ex) {
			System.out.println("Could not load file at src/main/resources/students.txt!");
			return;
		}
		
		List<StudentRecord> records = convert(lines);
		
		
		System.out.printf(
				"Number of students with more than 25 total points: %d%n%n",
				getPointsMoreThan25(records)
		);

		
		System.out.printf(
				"Number of students that have achieved a grade of 5 in class: %d%n%n",
				getNumberOfAGraders(records)
		);
		
		
		System.out.println("List of student records that have achieved a grade of 5 in class:");
		for (StudentRecord record : getStudentsWithGrade5(records)) {
			System.out.println(record);
		}
		System.out.println();
		
		
		System.out.println(
				"List of student records that have achieved a grade of 5 in class, "
				+ "sorted descending by total amount of scored points:"
		);
		for (StudentRecord record : getSortedStudentsWithGrade5(records)) {
			System.out.println(record);
		}
		System.out.println();
		
		
		List<String> failedJMBAGs = getJMBAGsWhichFailedClass(records);
		System.out.println("Number of students that have failed the class: " + failedJMBAGs.size());
		System.out.println("List of JMBAGs of first five students that have failed the class: ");
		for (int i = 0; i < 5; i++) {
			System.out.println(failedJMBAGs.get(i));
		}
		System.out.println();
		
		
		Map<Integer, List<StudentRecord>> studentsByGrade = sortStudentsByGrade(records);
		for (Map.Entry<Integer, List<StudentRecord>> entry : studentsByGrade.entrySet()) {
			System.out.printf(
					"Names of some of the students with grade %d: %s, %s, %s%n", 
					entry.getKey(), 
					entry.getValue().get(0).getFirstName(),
					entry.getValue().get(1).getFirstName(),
					entry.getValue().get(2).getFirstName()
			);
		}
		System.out.println();
		
		
		Map<Integer, Integer> studentsByGrade2 = numberOfStudentsByGrade(records);
		for (Map.Entry<Integer, Integer> entry : studentsByGrade2.entrySet()) {
			System.out.printf(
					"Number of students with grade %d: %d%n", 
					entry.getKey(), 
					entry.getValue()
			);
		}
		System.out.println();
		
		
		Map<Boolean, List<StudentRecord>> studentByPassedOrFailed = sortPassedOrFailed(records);
		for (Map.Entry<Boolean, List<StudentRecord>> entry : studentByPassedOrFailed.entrySet()) {
			System.out.printf(
					"%d students have %s the class%n",
					entry.getValue().size(),
					entry.getKey() == true ? "passed" : "failed"
			);
		}
	}

	/**
	 * Parses a list of strings representing student records and
	 * returns a list of parsed student records. Skips improperly
	 * formatted records.
	 * 
	 * @param lines list of strings representing student records
	 * @return list of parsed student records
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> records = new ArrayList<>();
		Map<String, StudentRecord> index = new HashMap<>();
		
		for (String line : lines) {
			String[] tokens = line.split("\\t");
			
			// improperly formatted records will be skipped, as documented
			if (tokens.length != 7) {
				continue;
			}
			
			String jmbag = tokens[0];
			if (index.containsKey(jmbag)) {
				continue;
			}
			
			String lastName = tokens[1];
			String firstName = tokens[2];
			
			double midtermExamPoints;
			double finalExamPoints;
			double labPoints;
			try {
				midtermExamPoints = Double.parseDouble(tokens[3]);
				finalExamPoints = Double.parseDouble(tokens[4]);
				labPoints = Double.parseDouble(tokens[5]);
			} catch (NumberFormatException ex) {
				continue;
			}
			
			int grade = Integer.parseInt(tokens[6]);
			if (grade < 1 || grade > 5) {
				continue;
			}
			
			StudentRecord record = new StudentRecord(
					jmbag, lastName, firstName,
					midtermExamPoints, finalExamPoints,	labPoints,
					grade
			);
			
			records.add(record);
			index.put(jmbag, record);
		}
		
		return records;
	}
	
	/**
	 * Returns the number of students who have achieved more than
	 * 25 total points during the semester.
	 * 
	 * @param records list of student records
	 * @return number of students with more than 25 points
	 */
	private static long getPointsMoreThan25(List<StudentRecord> records) {
		return records
				.stream()
				.filter(student -> student.getTotalPoints() > 25.0)
				.count()
		;
	}
	
	/**
	 * Returns the number of students that have achieved a grade
	 * of 5 in the class.
	 * 
	 * @param records list of student records
	 * @return number of students that have achieved a grade of 5
	 */
	private static long getNumberOfAGraders(List<StudentRecord> records) {
		return records
				.stream()
				.filter(student -> student.getFinalGrade() == 5)
				.count()
		;
	}
	
	/**
	 * Returns a list of student records for students that have
	 * achieved a grade of 5 in class.
	 * 
	 * @param records list of student records
	 * @return list of student records with a grade of 5
	 */
	private static List<StudentRecord> getStudentsWithGrade5(List<StudentRecord> records) {
		return records
				.stream()
				.filter(student -> student.getFinalGrade() == 5)
				.collect(Collectors.toList())
		;
	}

	/**
	 * Returns a list of student records for students that have
	 * achieved a grade of 5 in class, sorted descending by their
	 * total amount of achieved points during the semester.
	 * 
	 * @param records list of student records
	 * @return list of student records with a grade of 5, sorted
	 * 		   by total number of points
	 */
	private static List<StudentRecord> getSortedStudentsWithGrade5(List<StudentRecord> records) {
		return records
				.stream()
				.filter(student -> student.getFinalGrade() == 5)
				.sorted((stu1, stu2) -> - Double.compare(stu1.getTotalPoints(), stu2.getTotalPoints()))
				.collect(Collectors.toList())
		;
	}
	
	/**
	 * Returns a list of students' JMBAGs for students that have
	 * failed the class, i.e. that have a grade of 1.
	 * 
	 * @param records list of student records
	 * @return list of JMBAGs for students that have failed the class
	 */
	private static List<String> getJMBAGsWhichFailedClass(List<StudentRecord> records) {
		return records
				.stream()
				.filter(student -> student.getFinalGrade() == 1)
				.map(StudentRecord::getJmbag)
				.sorted(String::compareTo)
				.collect(Collectors.toList())
		;
	}
	
	/**
	 * Returns a map with each entry having a list of student records
	 * mapped to the grade which each student has achieved in class.
	 * 
	 * @param records list of student records
	 * @return map with lists of student records mapped to the records' grades
	 */
	private static Map<Integer, List<StudentRecord>> sortStudentsByGrade(List<StudentRecord> records) {
		return records
				.stream()
				.collect(Collectors.groupingBy(StudentRecord::getFinalGrade))
		;
	}
	
	/**
	 * Returns a map with each entry having the number of students
	 * that have achieved a particular grade mapped to the grade 
	 * in question.
	 * 
	 * @param records list of student records
	 * @return map with the amount of grades mapped to the grades themselves
	 */
	private static Map<Integer, Integer> numberOfStudentsByGrade(List<StudentRecord> records) {
		return records
				.stream()                                                
				.collect(Collectors.toMap(StudentRecord::getFinalGrade, s -> 1, (v1, v2) -> v1+v2))
		;                                                                    // ^ Math::addExact also works
	}
	
	/**
	 * Returns a map with each entry having a list of student records
	 * mapped to a boolean value indicating whether the student in question
	 * has passed or failed the class.
	 * 
	 * @param records list of student records
	 * @return map with lists of student records mapped to
	 * 		   whether the student in question failed or passed the class
	 */
	private static Map<Boolean, List<StudentRecord>> sortPassedOrFailed(List<StudentRecord> records) {
		return records
				.stream()
				.collect(Collectors.partitioningBy(student -> student.getFinalGrade() > 1))
		;
	}
}
