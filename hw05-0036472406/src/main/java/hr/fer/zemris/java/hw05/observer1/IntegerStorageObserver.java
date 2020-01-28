package hr.fer.zemris.java.hw05.observer1;

/**
 * Represents an object interested in keeping up with state
 * changes of an IntegerStorage object. An IntegerStorageObserver
 * offers a single method through which it expects to receive
 * a reference to a whole IntegerStorage object when said object's
 * state changes, i.e. when a new integer is set for it.
 * 
 * @author Vice Ivušić
 *
 */
public interface IntegerStorageObserver {
	
	/**
	 * Prints to standard output each time the specified 
	 * IntegerStorage object has its state changed. 
	 * 
	 * @param istorage reference to the observed IntegerStorage object
	 * @throws IllegalArgumentException if the specified istorage is null
	 */
	public void valueChanged(IntegerStorage istorage);
}
