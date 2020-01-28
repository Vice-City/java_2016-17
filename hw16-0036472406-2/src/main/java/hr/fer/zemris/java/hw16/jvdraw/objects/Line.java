package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectInfoExtractor;

/**
 * Represents a straight line with two endpoints and a color.
 * 
 * @author Vice Ivušić
 *
 */
public class Line extends GeometricalObject {

	/**
	 * Starting coordinate of this line.
	 */
	private Point p1;
	
	/**
	 * End coordinate of this line.
	 */
	private Point p2;
	
	/**
	 * This line's color.
	 */
	private Color color;
	
	/**
	 * Counter tracking how many Line objects have been created.
	 */
	private static int counter;
	
	/**
	 * ID of this object based off the counter.
	 */
	private int id;
	
	/**
	 * Creates a new Line with the specified parameters. Expects the user
	 * to pass on non-null values and gives no warnings otherwise.
	 * 
	 * @param p1 starting coordinate of the line
	 * @param p2 end coordinate of the line
	 * @param color this line's color
	 */
	public Line(Point p1, Point p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		
		id = ++counter;
	}
	
	/**
	 * @return the p1
	 */
	public Point getP1() {
		return p1;
	}

	/**
	 * @return the p2
	 */
	public Point getP2() {
		return p2;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets this line's parameters to the specified ones. Expects the user to
	 * pass on non null values and gives no warnings otherwise.
	 * 
	 * @param x1 x coordinate of the starting point
	 * @param y1 y coordinate of the starting point
	 * @param x2 x coordinate of the end point
	 * @param y2 y coordinate of the end point
	 * @param color desired color
	 */
	public void setParameters(int x1, int y1, int x2, int y2, Color color) {
		p1.x = x1;
		p1.y = y1;
		p2.x = x2;
		p2.y = y2;
		this.color = color;
		
		update();
	}
	
	@Override
	public Rectangle getBoundingBox() {
		return new Rectangle(
			p1.x < p2.x ? p1.x : p2.x,
			p1.y < p2.y ? p1.y : p2.y,
			Math.abs(p1.x - p2.x),
			Math.abs(p1.y - p2.y)
		);
	}
	
	
	@Override
	public void accept(ObjectInfoExtractor extractor) {
		extractor.extract(this);
	}
	
	@Override
	public void scaleTo(Point p) {
		p2 = p;
	}
	
	@Override
	public String toString() {
		return String.format("Line %d", id);
	}
	
}
