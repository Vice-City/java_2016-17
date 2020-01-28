package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents a counter which counts the number of times an
 * IntegerStorage object has had its state changed since the
 * moment this counter registered itself as an observer.
 * 
 * @author Vice Ivušić
 *
 */
public class ChangeCounter implements IntegerStorageObserver {

	/** number of times a change of state has occured **/
	private int counter;
	
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Argument istorage must not be null!");
		}
		
		counter++;
		System.out.printf("Number of value changes since tracking: %d%n", counter);
	}

}
