package hr.fer.zemris.java.hw05.observer2;

/**
 * A simple program which initializes a handful IntegerStorageObserver
 * objects and changes the linked IntegerStorage object's state a few
 * times to demonstrate the observer objects' behavior. The observer
 * objects print their work out onto the standard output. Program
 * doesn't receive any arguments.
 * 
 * @author Vice Ivušić
 *
 */
public class ObserverExample {
	
	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		
		istorage.addObserver(new SquareValue());
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(3));
		
		for (int i = 0; i < 25; i += 5) {
			istorage.setValue(i);
			System.out.println();
		}
	}
}