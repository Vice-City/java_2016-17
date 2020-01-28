package hr.fer.zemris.java.hw04.db.demo;

import java.util.Arrays;
import java.util.Scanner;

import hr.fer.zemris.java.hw04.db.QueryFilter;
import hr.fer.zemris.java.hw04.db.StudentDatabase;
import hr.fer.zemris.java.hw04.db.StudentRecord;
import hr.fer.zemris.java.hw04.db.parser.QueryParser;
import hr.fer.zemris.java.hw04.db.parser.QueryParserException;

/**
 * A program which takes user input in the form of an extremely
 * simplified set of SQL commands and prints student records which
 * satisfy the given query.
 * 
 * <p>Every query has to start with the keyword <code>query</code>. A query
 * can contain multiple expressions; each expression must contain
 * a valid attribute name (one of either jmbag, firstName or lastName),
 * a valid operator (one of either <, <=, =, >=, >, !=, or LIKE) and a valid
 * comparison string enclosed in double quotes. If using LIKE, the comparison string may
 * contain at most one wildcard character (*) which matches the wildcard with
 * any other set of characters. Multiple expressions have to be separated
 * by the logical operator <code>and</code>. Every keyword in the program is case
 * sensitive, except for <code>and</code>.
 * 
 * <p>Program is terminated by entering: <code>quit</code>
 * 
 * <p>A couple of examples of valid input:
 * <p> <code>query jmbag="0000000002"</code>
 * <p> <code>query firstName>"Ana" and lastName LIKE "N*" and jmbag<"00000000050"</code>
 * 
 * @author Vice Ivušić
 *
 */
public class StudentDB {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments
	 */
	public static void main(String[] args) {
		
		StudentDatabase database = StudentDatabase.loadDefaultDatabase();
		
		if (database == null) {
			System.out.println("Could not read from src/main/resources/database.txt!");
			return;
		}
		
		// this warning is annoying; the scanner is always properly closed!
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in).useDelimiter("\\n");
		
		while (true) {
			System.out.print("> ");
			String token = sc.next().trim();
			
			if (token.equals("quit")) {
				System.out.println("Goodbye!");
				break;
			}
			
			String[] query = token.split("\\s");
			
			if (!query[0].equals("query")) {
				System.out.println("Only valid command is 'query'!\n");
				continue;
			}
			
			if (query.length == 1) {
				System.out.println("Expected expression(s) after 'query'!\n");
				continue;
			}
			
			// skips the 'query' keyword when putting query back together
			String queryToParse = String.join(" ", Arrays.copyOfRange(query, 1, query.length));
			QueryParser parser;
			try {
				parser = new QueryParser(queryToParse);
			} catch (QueryParserException ex) {
				System.out.println(ex.getMessage() + "\n");
				continue;
			}
			
			StudentRecordList queriedRecords = new StudentRecordList();
			
			if (parser.isDirectQuery()) {
				String jmbag = parser.getQueriedJMBAG();
				
				System.out.println("Using index for record retrieval.");
				StudentRecord directRecord = database.forJMBAG(jmbag);
				
				if (directRecord == null) {
					System.out.println("Record with queried JMBAG does not exist!\n");
					continue;
				}
				
				queriedRecords.add(directRecord);
			
			} else {
				QueryFilter accordingToAndExpressions = new QueryFilter(parser.getQuery());
				for (StudentRecord record : database.filter(accordingToAndExpressions)) {
					queriedRecords.add(record);
				}
			}
			
			int numberOfQueriedRecords = queriedRecords.size();
			if (numberOfQueriedRecords != 0) {
				System.out.print(queriedRecords.generateTable());
			}
			System.out.printf("Records selected: %d%n%n", numberOfQueriedRecords);
			
		}
		
		sc.close();
	}
	
}
