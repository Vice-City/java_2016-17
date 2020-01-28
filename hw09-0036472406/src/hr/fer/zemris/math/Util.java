package hr.fer.zemris.math;

/**
 * Contains helper methods for working with double values.
 * 
 * @author Vice Ivušić
 *
 */
public class Util {

	/**
	 * Compares two double values; returns -1 if the first value is 
	 * lesser than the second value, 0 if they are equal to within 9 decimals,
	 * and 1 if the first value is greater than the second value.
	 * 
	 * @param d1 first value to compare
	 * @param d2 second value to compare
	 * @return -1 if first is lesser than second; 0 if they are equal to within
	 * 		   9 decimals; and 1 if first is greater than second
	 */
	public static int compareDoubles(double d1, double d2) {
		if (Math.abs(d1 - d2) < 10e-9) return 0;
		
		return Double.compare(d1, d2);
	}
}
