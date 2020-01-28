package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Represents a listener interested in receving updates when a change occurs in a geometrical object.
 * 
 * @author Vice Ivušić
 *
 */
public interface GeometricalObjectListener {

	/**
	 * Called when a change occurs in a geometrical object.
	 * 
	 * @param obj the geometrical object where the change occured
	 */
	void geometricalObjectChanged(GeometricalObject obj);
}
