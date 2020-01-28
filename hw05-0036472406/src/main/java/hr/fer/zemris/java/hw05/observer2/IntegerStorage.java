package hr.fer.zemris.java.hw05.observer2;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a container of a single integer value. Offers methods
 * for setting a new integer value and for retrieving the current 
 * integer value.
 * 
 * Also offers methods for observers to register and unregister
 * themselves if they wish to track the changes this container
 * undergoes. 
 * 
 * @author Vice Ivušić
 *
 */
public class IntegerStorage {
	
	/** currently stored integer in the container **/
	private int value;
	/** list of all observers registered for keeping track of state changes **/
	private List<IntegerStorageObserver> observers; 
	
	/**
	 * Creates a new IntegerStorage object with the specified integer value.
	 * 
	 * @param initialValue integer value this container will hold
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Returns the currently stored integer value.
	 * 
	 * @return currently stored integer value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the container's stored integer value to the specified value.
	 * Does nothing if the specified value is the same as the currently
	 * stored value.
	 * 
	 * @param value desired integer storage value
	 */
	public void setValue(int value) {
		if (this.value == value) {
			return;
		}
		
		int oldValue = this.value;
		this.value = value;
		
		if (observers != null) {
			IntegerStorageChange notification = new IntegerStorageChange(this, oldValue, value);
			
			for (IntegerStorageObserver observer : observers) {
				observer.valueChanged(notification);
			}
		}
	}

	/**
	 * Adds the specified observer to this object's subscribed observers.
	 * Each time this object undergoes a change of state, the subscribed
	 * observers will be notified of this change. Does nothing if the 
	 * specified observer is already a subscribed observer.
	 * 
	 * @param observer reference to the IntegerStorageObserver object
	 * 		  that needs registering
	 * @throws IllegalArgumentException if the specified observer is null
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException("Argument observer must not be null!");
		}
		
		if (observers == null) {
			observers = new ArrayList<>();
		}
		
		if (observers.contains(observer)) {
			return;
		}

		/*
		 * In case a new observer is added while subject is notifying
		 * its existing observers; subject still has old reference.
		 */
		observers = new ArrayList<>(observers);
		observers.add(observer);
	}

	/**
	 * Removes the specified observer from this object's subscribed observers.
	 * Each time this object undergoes a change of state, the subscribed
	 * observers will be notified of this change. Does nothing if the 
	 * specified observer isn't a subscribed observer or if the specified
	 * observer is null.
	 * 
	 * @param observer reference to the IntegerStorageObserver object
	 * 		  that needs unregistering
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if (observer == null || observers == null) {
			return;
		}
		
		/*
		 * In case a new observer is removed while subject is notifying
		 * its existing observers; subject still has old reference.
		 */
		observers = new ArrayList<>(observers);
		observers.remove(observer);
	}

	/**
	 * Removes all subsribed observers from this object's subscription list.
	 */
	public void clearObservers() {
		observers = null;
	}
}