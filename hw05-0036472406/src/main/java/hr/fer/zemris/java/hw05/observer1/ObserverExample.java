package hr.fer.zemris.java.hw05.observer1;

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
		
		IntegerStorageObserver observer = new SquareValue();
		
		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		System.out.println();
		
		istorage.removeObserver(observer);
		
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));
		
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}