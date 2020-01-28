package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents an object interested in keeping up with state
 * changes of an IntegerStorage object. An IntegerStorageObserver
 * offers a single method through which it expects to receive
 * a reference to an IntegerStorageChange object when said object's
 * state changes, i.e. when a new integer is set for it.
 * 
 * @author Vice Ivušić
 *
 */
public interface IntegerStorageObserver {
	
	/**
	 * Prints to standard output each time the 
	 * IntegerStorage object contained in the
	 * IntegerStorageChange object changes its state. 
	 * 
	 * @param istorage reference to the IntegerStorageChange object
	 * 		  		   of the observed IntegerStorage object
	 * @throws IllegalArgumentException if the specified istorage is null
	 */
	public void valueChanged(IntegerStorageChange istorage);
}
