package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectInfoExtractor;

/**
 * A geometrical object representing a circle with a center point, radius and
 * outline color.
 * 
 * @author Vice Ivušić
 *
 */
public class Circle extends GeometricalObject {

	/**
	 * This circle's center point.
	 */
	protected Point center;
	
	/**
	 * This circle's radius.
	 */
	protected double radius;
	
	/**
	 * This circle's outline color.
	 */
	protected Color color;
	
	/**
	 * Counter tracking how many Circle objects have been created.
	 */
	private static int counter;
	
	/**
	 * ID of this object based off the counter.
	 */
	private int id;
	
	/**
	 * Creates a new circle with the specified parameters. Expects
	 * the user to pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param center the circle's center point
	 * @param radius the circle's radius
	 * @param color the circle's color
	 */
	public Circle(Point center, double radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.color = color;
		
		id = ++counter;
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}


	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets this circle's parameters to the specified ones. Expects the
	 * user to pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param x x coordiate of the center
	 * @param y y coordinate of the center
	 * @param radius radius
	 * @param color outline color
	 */
	public void setParameters(int x, int y, double radius, Color color) {
		center.x = x;
		center.y = y;
		this.radius = radius;
		this.color = color;
		
		update();
	}

	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(
			center.x-(int)radius,
			center.y-(int)radius,
			(int)radius*2, 
			(int)radius*2
		);
	}

	@Override
	public void accept(ObjectInfoExtractor extractor) {
		extractor.extract(this);
	}

	@Override
	public void scaleTo(Point p) {
		radius = Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2));
		
	}

	@Override
	public String toString() {
		return String.format("Circle %d", id);
	}
}
