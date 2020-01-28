package hr.fer.zemris.java.hw16.jvdraw.objects.util;

import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;

/**
 * Represents an object which knows how to save geometrical objects
 * as parsable string data.
 * 
 * @author Vice Ivušić
 *
 */
public class ObjectSaver implements ObjectInfoExtractor {

	/**
	 * StringBuilder to use for creating parsable strings.
	 */
	private StringBuilder sb = new StringBuilder();
	
	@Override
	public void extract(Line line) {
		sb.append(String.format(
			"LINE %d %d %d %d %d %d %d%n",
			line.getP1().x,
			line.getP1().y,
			line.getP2().x,
			line.getP2().y,
			line.getColor().getRed(),
			line.getColor().getGreen(),
			line.getColor().getBlue()
		));
		
	}

	@Override
	public void extract(Circle circle) {
		sb.append(String.format(
			"CIRCLE %d %d %d %d %d %d %n",
			circle.getCenter().x,
			circle.getCenter().y,
			(int)circle.getRadius(),
			circle.getColor().getRed(),
			circle.getColor().getGreen(),
			circle.getColor().getBlue()
		));		
	}

	@Override
	public void extract(FilledCircle fCircle) {
		sb.append(String.format(
			"FCIRCLE %d %d %d %d %d %d %d %d %d%n",
			fCircle.getCenter().x,
			fCircle.getCenter().y,
			(int)fCircle.getRadius(),
			fCircle.getColor().getRed(),
			fCircle.getColor().getGreen(),
			fCircle.getColor().getBlue(),
			fCircle.getFillColor().getRed(),
			fCircle.getFillColor().getGreen(),
			fCircle.getFillColor().getBlue()
		));		
	}
	
	/**
	 * Computes the geometrical object data as a single string.
	 * 
	 * @return the data as a single string
	 */
	public String computeStringData() {
		return sb.toString();
	}
	

}
