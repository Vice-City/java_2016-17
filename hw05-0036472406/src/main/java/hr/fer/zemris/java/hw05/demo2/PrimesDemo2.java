package hr.fer.zemris.java.hw05.demo2;

/**
 * A simple program which prints out the Cartesian
 * product of the first two prime numbers. Program 
 * takes no arguments.
 * 
 * @author Vice Ivušić
 *
 */
public class PrimesDemo2 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		
		PrimesCollection primesCollection = new PrimesCollection(2);
		for (Integer prime : primesCollection) {
			for (Integer prime2 : primesCollection) {
				System.out.printf("Got prime pair: %d, %d%n", prime, prime2);
			}
		}
	}
}
