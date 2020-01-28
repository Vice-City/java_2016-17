package hr.fer.zemris.java.hw16.jvdraw.objects.util;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Utility class for finding out the joint bounding box of a collection
 * of geometrical objects and for drawing those objects onto a Graphics
 * object using a specified bounding box.
 * 
 * @author Vice Ivušić
 *
 */
public class BoundingBoxShifter implements ObjectInfoExtractor {

	/**
	 * Bounding box used for drawing objects.
	 */
	private Rectangle boundingBox;
	
	/**
	 * ObjectPainter object for drawing objects.
	 */
	private ObjectPainter painter;
		
	/**
	 * Creates a new BoundingBoxShifter from the specified parameters.
	 * 
	 * @param boundingBox the bounding box within which objects will be drawn
	 * @param g2d a graphics object onto which objects will be drawn
	 */
	public BoundingBoxShifter(Rectangle boundingBox, Graphics2D g2d) {
		this.boundingBox = boundingBox;
		
		painter = new ObjectPainter(g2d);
	}
	
	/**
	 * Computes the joint bounding box for the specified list of objects.
	 * 
	 * @param objects list of geometrical objects to compute the bounding box for
	 * @return bounding box for the specified list of objects
	 */
	public static Rectangle computeBoundingBox(List<GeometricalObject> objects) {
		int x1 = 0;
		int y1 = 0;
		
		int x2 = 0;
		int y2 = 0;
		
		boolean firstPass = true;
		for (GeometricalObject obj : objects) {
			Rectangle otherBox = obj.getBoundingBox();
			
			int cx1 = otherBox.x;
			int cy1 = otherBox.y;
			int cx2 = otherBox.x+otherBox.width;
			int cy2 = otherBox.y+otherBox.height;
			
			if (firstPass) {
				x1 = cx1;
				y1 = cy1;
				x2 = cx2;
				y2 = cy2;
				firstPass = false;
				continue;
			}
			
			if (x1 > cx1) {
				x1 = cx1;
			}
			
			if (y1 > cy1) {
				y1 = cy1;
			}
			
			if (x2 < cx2) {
				x2 = cx2;
			}
			
			if (y2 < cy2) {
				y2 = cy2;
			}
		}
		
		return new Rectangle(x1, y1, x2-x1, y2-y1);
	}
	
	@Override
	public void extract(Line line) {
		Line l = new Line(
			new Point(line.getP1().x-boundingBox.x, line.getP1().y-boundingBox.y), 
			new Point(line.getP2().x-boundingBox.x, line.getP2().y-boundingBox.y), 
			line.getColor()
		);
		
		l.accept(painter);
	}

	@Override
	public void extract(Circle circle) {
		Circle c = new Circle(
			new Point(circle.getCenter().x-boundingBox.x, circle.getCenter().y-boundingBox.y),
			circle.getRadius(),
			circle.getColor()
		);
		
		c.accept(painter);
	}

	@Override
	public void extract(FilledCircle filledCircle) {
		FilledCircle fc = new FilledCircle(
			new Point(filledCircle.getCenter().x-boundingBox.x, filledCircle.getCenter().y-boundingBox.y),
			filledCircle.getRadius(),
			filledCircle.getColor(),
			filledCircle.getFillColor()
		);
		
		fc.accept(painter);
	}
	
}
