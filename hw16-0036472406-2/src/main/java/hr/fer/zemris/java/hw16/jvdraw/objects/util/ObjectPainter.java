package hr.fer.zemris.java.hw16.jvdraw.objects.util;

import java.awt.Graphics2D;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Represents an objects which knows how to draw geometrical objects
 * onto a configured graphics object.
 * 
 * @author Vice Ivušić
 *
 */
public class ObjectPainter implements ObjectInfoExtractor {

	/**
	 * Graphics object onto which objects will be drawn.
	 */
	private Graphics2D g2d;
	
	/**
	 * Creates a new ObjectPainter which will draw objects onto
	 * the specified graphics object.
	 * 
	 * @param g2d graphics object onto which objects will be drawn
	 */
	public ObjectPainter(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	@Override
	public void extract(Line line) {
		g2d.setColor(line.getColor());
		g2d.drawLine(line.getP1().x, line.getP1().y, line.getP2().x, line.getP2().y);
	}

	@Override
	public void extract(Circle circle) {
		g2d.setColor(circle.getColor());
		g2d.drawOval(
			(int) (circle.getCenter().x - circle.getRadius()),
			(int) (circle.getCenter().y - circle.getRadius()),
			(int) (circle.getRadius() * 2),
			(int) (circle.getRadius() * 2)
		);
	}

	@Override
	public void extract(FilledCircle filledCircle) {
		g2d.setColor(filledCircle.getFillColor());
		g2d.fillOval(
			(int) (filledCircle.getCenter().x - filledCircle.getRadius()),
			(int) (filledCircle.getCenter().y - filledCircle.getRadius()),
			(int) (filledCircle.getRadius() * 2),
			(int) (filledCircle.getRadius() * 2)
		);
		
		extract((Circle) filledCircle);
	}
}
