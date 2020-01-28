package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectInfoExtractor;

/**
 * Represents a circle with both an outline and a fill color, in addition
 * to its center point and radius.
 * 
 * @author Vice Ivušić
 *
 */
public class FilledCircle extends Circle {

	/**
	 * Fill color of this circle.
	 */
	private Color fillColor;
	
	/**
	 * Counter tracking how many FilledCircle objects have been created.
	 */
	private static int counter;
	
	/**
	 * ID of this object based off the counter.
	 */
	private int id;
	
	/**
	 * Creates a new filled circle with the specified parameters. Expects
	 * the user to pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param center the circle's center point
	 * @param radius the circle's radius
	 * @param outlineColor the circle's outline color
	 * @param fillColor the circle's fill color
	 */
	public FilledCircle(Point center, double radius, Color outlineColor, Color fillColor) {
		super(center, radius, outlineColor);
		this.fillColor = fillColor;
		
		id = ++counter;
	}
	
	/**
	 * @return the fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets this filled circle's parameters to the specified ones. Expects the
	 * user to pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param x x coordiate of the center
	 * @param y y coordinate of the center
	 * @param radius radius
	 * @param outlineColor outline color
	 * @param fillColor fill color
	 */
	public void setParameters(int x, int y, double radius, Color outlineColor, Color fillColor) {
		this.fillColor = fillColor;
		super.setParameters(x, y, radius, outlineColor);
	}
	
	@Override
	public void accept(ObjectInfoExtractor extractor) {
		extractor.extract(this);
	}
	
	@Override
	public String toString() {
		return String.format("FCircle %d", id);
	}
	
}
