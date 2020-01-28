package hr.fer.zemris.java.hw16.jvdraw.objects.util;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Represents an object which knows how to handle GeometricalObject objects.
 * 
 * @author Vice Ivušić
 *
 */
public interface ObjectInfoExtractor {

	/**
	 * Extracts information from Line objects.
	 * 
	 * @param line Line object
	 */
	void extract(Line line);
	
	/**
	 * Extracts information from Circle objects.
	 * 
	 * @param circle Circle object
	 */
	void extract(Circle circle);
	
	/**
	 * Extracts information from FilledCircle objects.
	 * 
	 * @param filledCircle filledCircle object
	 */
	void extract(FilledCircle filledCircle);
	
}
