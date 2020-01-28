package hr.fer.zemris.java.hw05.demo2;

/**
 * A simple program which prints out the first five prime numbers
 * to standard output. Program takes no arguments.
 * 
 * @author Vice Ivušić
 *
 */
public class PrimesDemo1 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		
		PrimesCollection primesCollection = new PrimesCollection(5);
		for (Integer prime : primesCollection) {
			System.out.printf("Got prime: %d%n", prime);
		}
	}
}
