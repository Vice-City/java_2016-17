package hr.fer.zemris.java.hw16.jvdraw.objects.util;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Class containing a method for parsing a geometrical object
 * in text form.
 * 
 * @author Vice Ivušić
 *
 */
public class ObjectParserUtil {

	/**
	 * Parses a geometrical object from the specified string.
	 * 
	 * @param data string containing the parsable data
	 * @return new GeometricalObject from the parsed data
	 * @throws RuntimeException if the object could not be parsed
	 */
	public static GeometricalObject parse(String data) {
		String[] tokens = data.split("\\s");
		
		String type = tokens[0].toLowerCase();
		data = data.replaceFirst("[^\\s]* (.*)", "$1").trim();
		
		if (type.equals("line")) {
			return parseLine(data);
		}
		
		if (type.equals("circle")) {
			return parseCircle(data);
		}
		
		if (type.equals("fcircle")) {
			return parseFCircle(data);
		}
		
		throw new RuntimeException("Undefined object!");
	}

	/**
	 * Helper method for parsing Line objects.
	 * 
	 * @param data string containing the parsable data
	 * @return new parsed Line object
	 * @throws RuntimeException if the object could not be parsed
	 */
	private static Line parseLine(String data) {
		String[] tokens = data.split("\\s");
		
		if (tokens.length != 7) {
			throw new RuntimeException("Line takes 7 parameters!");
		}
		
		return new Line(
			new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])),
			new Point(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])),
			new Color(Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]))
		);
		
	}
	
	/**
	 * Helper method for parsing Circle objects.
	 * 
	 * @param data string containing the parsable data
	 * @return new parsed Circle object
	 * @throws RuntimeException if the object could not be parsed
	 */
	private static Circle parseCircle(String data) {
		String[] tokens = data.split("\\s");
		
		if (tokens.length != 6) {
			throw new RuntimeException("Circle takes 6 parameters!");
		}
		
		return new Circle(
			new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])),
			Integer.parseInt(tokens[2]),
			new Color(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]))
		);
		
	}
	
	/**
	 * Helper method for parsing FilledCircle objects.
	 * 
	 * @param data string containing the parsable data
	 * @return new parsed FilledCircle object
	 * @throws RuntimeException if the object could not be parsed
	 */
	private static FilledCircle parseFCircle(String data) {
		String[] tokens = data.split("\\s");
		
		if (tokens.length != 9) {
			throw new RuntimeException("FCircle takes 9 parameters!");
		}
		
		return new FilledCircle(
			new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])),
			Integer.parseInt(tokens[2]),
			new Color(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])),
			new Color(Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8]))
		);
		
	}
	
}
