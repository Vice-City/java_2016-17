package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents a square function. Each time an IntegerStorage
 * object this square function is registered to changes its state,
 * the square function squares the newly arrived integer value.
 * 
 * @author Vice Ivušić
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Argument istorage must not be null!");
		}
		
		int value = istorage.getNewValue();
		
		System.out.printf(
				"Provided new value: %d, square is %d%n", 
				value,
				value * value
		);
	}

}
