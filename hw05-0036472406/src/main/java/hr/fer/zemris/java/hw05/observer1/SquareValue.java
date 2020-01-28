package hr.fer.zemris.java.hw05.observer1;

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
	public void valueChanged(IntegerStorage istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Argument istorage must not be null!");
		}
		
		int value = istorage.getValue();
		
		System.out.printf(
				"Provided new value: %d, square is %d%n", 
				value,
				value * value
		);
	}

}
