package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.Stack;

/**
 * Represents a complex polynomial f(z) = (z-z1)*...*(z-zn), with
 * z1 ... zn being the polynomial's roots. Each root is assigned
 * an index according to the order in which the polynomial's roots 
 * were defined during the polynomial's creation; e.g, for the roots
 * 1 and i: 1 will have index 1, while i will have index 2.
 * 
 * <p>Offers methods for calculating the polynomial's value for a
 * given complex number, for converting the polynomial from a rooted
 * form to a factorized form, and for finding out the index of the
 * root closest to a given complex number for a specified threshold.
 * 
 * @author Vice Ivušić
 *
 */
public class ComplexRootedPolynomial {

	/** array of roots the polynomial consists of */
	private Complex[] roots;
	
	/**
	 * Constructs a new ComplexRootedPolynomial from the specified roots.
	 * 
	 * @param roots complex roots of this polynomial
	 * @throws IllegalArgumentException if argument roots is null or
	 * 		   if no roots were specified
	 */
	public ComplexRootedPolynomial(Complex ... roots) {
		if (roots == null) {
			throw new IllegalArgumentException("Argument roots cannot be null!");
		}
		
		if (roots.length < 1) {
			throw new IllegalArgumentException("Must specify at least one root!");
		}
		
		this.roots = Arrays.copyOf(roots, roots.length);
	}
	
	/**
	 * Returns a new Complex number calculated by applying the specified
	 * complex number z to current polynomial.
	 * 
	 * @param z complex number to apply to current polynomial
	 * @return complex value for specified number for current polynomial
	 * @throws IllegalArgumentException if the specified complex number is null
	 */
	public Complex apply(Complex z) {
		if (z == null) {
			throw new IllegalArgumentException("Argument z cannot be null!");
		}
		
		Stack<Complex> stack = new Stack<>();
		
		for (Complex root : roots) {
			stack.push(z.sub(root));
		}
		
		while (stack.size() != 1) {
			Complex c1 = stack.pop();
			Complex c2 = stack.pop();
			
			stack.push(c1.multiply(c2));
		}
		
		return stack.pop();
	}
	
	/**
	 * Returns a ComplexPolynomial equivalent to current polynomial.
	 * 
	 * @return ComplexPolynomial equivalent to current polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(
			Complex.ZERO.sub(roots[0]), Complex.ONE
		);
		
		for (int i = 1; i < roots.length; i++) {
			ComplexPolynomial newPoly = new ComplexPolynomial(
				Complex.ZERO.sub(roots[i]), Complex.ONE
			);
			result = result.multiply(newPoly);
		}
		
		return result;
	}
	
	/**
	 * Returns index of closest root for specified complex number z
	 * that is within the specified threshold. Indexes are in range
	 * of 1 to numberOfRoots. Returns -1 if no such index exists.
	 * 
	 * @param z complex number to find index of closest root for
	 * @param threshold maximum allowed threshold for closest root
	 * @return index of closest root for specified parameters
	 * @throws IllegalArgumentException if specified complex number is null
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		if (z == null) {
			throw new IllegalArgumentException("Argument z cannot be null!");
		}
		
		threshold = Math.abs(threshold);
		OptionalInt opt = OptionalInt.empty();
		
		for (int i = 0; i < roots.length; i++) {
			Complex currentRoot = roots[i];
			double currentDelta = z.sub(currentRoot).module();
			
			if (Util.compareDoubles(currentDelta, threshold) > 0) {
				continue;
			}
			
			if (!opt.isPresent()) {
				opt = OptionalInt.of(i);
				continue;
			}
			
			double existingDelta = roots[opt.getAsInt()].sub(currentRoot).module();
			if (Util.compareDoubles(currentDelta, existingDelta) < 0) {
				opt = OptionalInt.of(i);
			}
		}
		
		// !!! return index+1
		return opt.isPresent() ? opt.getAsInt()+1 : -1;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(roots);
	}
	
}
