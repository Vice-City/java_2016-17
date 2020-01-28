package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Represents a drawing model which can hold multiple GeometricalObject
 * objects. The objects' order matters for purposes of drawing the objects.
 * Offers methods for querying the amount of currently stored objects,
 * adding new objects, removing existing objects and clearing the model
 * of objects altogether. Also offers methods for registration and
 * deregistration of DrawingModelListener objects.
 * 
 * @author Vice Ivušić
 *
 */
public interface DrawingModel {
	
	/**
	 * @return amount of currently stored geometrical objects
	 */
	int getSize();
	
	/**
	 * Returns the geometrical object at the specified index.
	 * 
	 * @param index index of the object in question
	 * @return geometrical object at the specified index
	 * @throws IndexOutOfBoundsException if the index isn't between zero
	 * 		   and the amount of currently stored geometrical objects
	 */
	GeometricalObject getObject(int index);
	
	/**
	 * Adds the specified object to the drawing model. Expects the user
	 * to pass non-null values and gives no warnings otherwise.
	 * 
	 * @param object geometrical object to add
	 */
	void add(GeometricalObject object);
	
	/**
	 * Removes the specified object from the drawing model. Does nothing
	 * if the object is null or if it isn't currently a part of the model.
	 * 
	 * @param object geometrical object to remove
	 */
	void remove(GeometricalObject object);
	
	/**
	 * Adds the specified listener to this model's registered listeners.
	 * 
	 * @param l listener to add
	 */
	void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes the specified listener from this model's registered listeners.
	 * 
	 * @param l listener to remove
	 */
	void removeDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Clears this model of all currently stored objects.
	 */
	void clear();
}