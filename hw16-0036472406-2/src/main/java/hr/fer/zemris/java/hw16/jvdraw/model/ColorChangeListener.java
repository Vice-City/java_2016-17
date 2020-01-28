package hr.fer.zemris.java.hw16.jvdraw.model;

import java.awt.Color;

/**
 * Represents a listener interested in receving updates to an IColorProvider's
 * colors.
 * 
 * @author Vice Ivušić
 *
 */
public interface ColorChangeListener {

	/**
	 * Called when a color change occurs.
	 * 
	 * @param source reference to the provider which triggered the change
	 * @param oldColor old color
	 * @param newColor new color
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
