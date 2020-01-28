package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.model.GeometricalObjectListener;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectInfoExtractor;

/**
 * Represents a geometrical object. Interested geometrical object
 * listeners can register themselves for updates to changes in each
 * geometrical object. Additionally, each geometrical object knows
 * how to calculate its bounding box and accept ObjectInfoExtractor
 * objects.
 * 
 * @author Vice Ivušić
 *
 */
public abstract class GeometricalObject {

	/**
	 * List of registered geometrical object listeners.
	 */
	private List<GeometricalObjectListener> listeners = new ArrayList<>();
	
	/**
	 * @return this objects bounding box, i.e. a minimal box that encompasses the whole object
	 */
	public abstract Rectangle getBoundingBox();
	
	/**
	 * Adds the specified listener to this model's registered listeners.
	 * 
	 * @param l listener to add
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		if (l == null || listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}
	
	/**
	 * Removes the specified listener from this model's registered listeners.
	 * 
	 * @param l listener to remove
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l ) {
		if (l == null || !listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);

	}
	
	/**
	 * Helper method for notifying listeners when a change occurs.
	 */
	protected void update() {
		for (GeometricalObjectListener l : listeners) {
			l.geometricalObjectChanged(this);
		}
	}
	
	/**
	 * Scales and *changes* this geometrical object with the specified point.
	 * The implementation of this method is heavily object-dependent and should
	 * only be used for drawing purposes when the object still isn't in a
	 * finalized state.
	 * 
	 * @param p the point to which to scale this object
	 */
	public abstract void scaleTo(Point p);
	
	/**
	 * Accepts the specified ObjectInfoExtractor and calls its appropriate
	 * implementation dependent method.
	 * 
	 * @param extractor ObjectInfoExtractor object
	 */
	public abstract void accept(ObjectInfoExtractor extractor);
	
}

