package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Color;

/**
 * Represents an objects capable of registering interested color change listeners
 * and capable of providing its current color.
 * 
 * @author Vice Ivušić
 *
 */
public interface IColorProvider {

	/**
	 * @return currently set color
	 */
	Color getCurrentColor();
	
	/**
	 * Adds the specified listener to this model's registered listeners.
	 * 
	 * @param l listener to add
	 */
	void addColorChangeListener(ColorChangeListener l);
	
	/**
	 * Removes the specified listener from this model's registered listeners.
	 * 
	 * @param l listener to remove
	 */

	void removeColorChangeListener(ColorChangeListener l);
}
