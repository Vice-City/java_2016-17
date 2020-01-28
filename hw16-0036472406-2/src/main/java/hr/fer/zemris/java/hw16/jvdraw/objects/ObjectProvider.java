package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Point;

/**
 * Represents an object which knows how to generate appropriate geometrical
 * objects.
 * 
 * @author Vice Ivušić
 *
 */
public interface ObjectProvider {

	/**
	 * Generates a new geometrical object initialized with the specified point.
	 * 
	 * @param p initial point
	 * @return new GeometricalObject
	 */
	GeometricalObject getObject(Point p);
}
