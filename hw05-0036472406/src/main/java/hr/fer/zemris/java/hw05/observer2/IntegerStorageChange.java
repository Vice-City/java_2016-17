package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents a wrapper of an IntegerStorage object containing all
 * information relevant to a change of said object's state, i.e. its
 * reference, its old integer value and its new integer value.
 * 
 * @author Vice Ivušić
 *
 */
public class IntegerStorageChange {

	/** reference to the relevant IntegerStorage object whose state has changed **/
	private IntegerStorage storage;
	/** integer value the IntegerStorage object had before the state change **/
	private int oldValue;
	/** integer value the IntegerStorage object has after the state change, i.e. currently **/
	private int newValue;
	
	/**
	 * Creates a new IntegerStorageChange object with the specified arguments.
	 * 
	 * @param storage reference to the IntegerStorage object whose state has changed
	 * @param oldValue old integer value of the IntegerStorage object
	 * @param newValue current integer value of the IntegerStorage object
	 * @throws IllegalArgumentException if the specified storage is null
	 */
	public IntegerStorageChange(IntegerStorage storage, int oldValue, int newValue) {
		if (storage == null) {
			throw new IllegalArgumentException("Argument storage must not be null!");
		}

		this.storage = storage;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	/**
	 * Returns the reference to the relevant IntegerStorage object.
	 * 
	 * @return reference to the IntegerStorage object
	 */
	public IntegerStorage getStorage() {
		return storage;
	}
	
	/**
	 * Returns the integer value before the state change.
	 * 
	 * @return integer value before the state change
	 */
	public int getOldValue() {
		return oldValue;
	}
	
	/**
	 * Returns the intefer value after the state change, i.e. the current value.
	 * 
	 * @return integer value after the state change
	 */
	public int getNewValue() {
		return newValue;
	}
}
