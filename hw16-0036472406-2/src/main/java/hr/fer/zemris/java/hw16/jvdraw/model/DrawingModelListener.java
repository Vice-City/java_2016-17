package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * Represents a listener interested in receiving updates to a drawing model's contents.
 * 
 * @author Vice Ivušić
 *
 */
public interface DrawingModelListener {

	/**
	 * Called when geometrical objects are added.
	 * 
	 * @param source drawing model where the objects were added
	 * @param index0 start index 
	 * @param index1 end index
	 */
	void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * Called when geometrical objects are removed.
	 * 
	 * @param source drawing model where the objects were removed
	 * @param index0 start index 
	 * @param index1 end index
	 */
	void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Called when geometrical objects are changed.
	 * 
	 * @param source drawing model where the objects were changed
	 * @param index0 start index 
	 * @param index1 end index
	 */
	void objectsChanged(DrawingModel source, int index0, int index1);
}
