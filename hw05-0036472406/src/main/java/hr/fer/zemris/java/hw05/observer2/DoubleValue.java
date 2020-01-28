package hr.fer.zemris.java.hw05.observer2;

/**
 * Represents a multiplier. Each time the IntegerStorage object
 * this multiplier is registered to changes its state, the multiplier
 * multiplies the newly arrived integer value by two. The multiplier
 * will do this only for the specified number of times, after which it
 * automatically unregisters itself as an observer.
 * 
 * @author Vice Ivušić
 *
 */
public class DoubleValue implements IntegerStorageObserver {

	/** number of times this multiplier will keep track of a change in state **/
	private int lifePoints;
	
	/**
	 * Creates a new DoubleValue object with the specified life points, i.e.
	 * number of times this multiplier will keep track of a change in state.
	 * It has to be initialized with at least one life point.
	 * 
	 * @param lifePoints number of times this multiplier will output its result
	 * @throws IllegalArgumentException if the specified life points are less than one
	 */
	public DoubleValue(int lifePoints) {
		if (lifePoints < 1) {
			throw new IllegalArgumentException("Argument lifePoints must be at least 1!");
		}
		this.lifePoints = lifePoints;
	}
	
	@Override
	public void valueChanged(IntegerStorageChange istorage) {
		if (istorage == null) {
			throw new IllegalArgumentException("Argument istorage must not be null!");
		}
		
		System.out.printf("Double value: %d%n", istorage.getNewValue() * 2);
		lifePoints--;
		
		if (lifePoints == 0) {
			istorage.getStorage().removeObserver(this);
		}

	}

}
