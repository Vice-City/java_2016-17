package hr.fer.zemris.math;

import java.util.Arrays;

/**
 * Represents a complex polynomial f(z) = zn*z^n + ... + z1*z + z0
 * with z0 ... zn being the factors of each power. The order of the
 * polynomial is n.
 * 
 * <p>Offers methods for calculating the polynomial's value for a
 * given complex number, for retrieving the polynomial's order, for
 * multiplying the it with another polynomial and for finding
 * the derivation of the polynomial.
 *  
 * @author Vice Ivušić
 *
 */
public class ComplexPolynomial {

	/** array of factors the polynomial consists of */
	private Complex[] factors;
	
	/**
	 * Constructs a new ComplexPolynomial from the specified factors.
	 * 
	 * @param factors complex factors of this polynomial
	 * @throws IllegalArgumentException if argument factors is null or
	 * 		   if less than two factors were specified
	 */
	public ComplexPolynomial(Complex ... factors) {
		if (factors == null) {
			throw new IllegalArgumentException("Argument factors cannot be null!");
		}
		
		if (factors.length < 2) {
			throw new IllegalArgumentException("Must specify at least two factors!");
		}
		
		this.factors = Arrays.copyOf(factors, factors.length);
	}
	
	/**
	 * Returns the order of current polynomial.
	 * 
	 * @return order of current polynomial
	 */
	public short order() {
		return (short) (factors.length-1 < 0 ? 0 : factors.length-1);
	}
	
	/**
	 * Returns a new ComplexPolynomial calculated by multiplying
	 * the current and specified polynomial.
	 * 
	 * @param p polynomial to multiply with
	 * @return multipled complex polynomial
	 * @throws IllegalArgumentException if argument p is null
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		if (p == null) {
			throw new IllegalArgumentException("Argument p cannot be null!");
		}
		
		int multipliedSize = factors.length + p.factors.length - 1;
		
		Complex[] multipliedPoly = new Complex[multipliedSize];
		
		for (int i = 0; i < factors.length; i++) {
			for (int j = 0; j < p.factors.length; j++) {
				int polyOrder = i + j;
				
				if (multipliedPoly[polyOrder] == null) {
					multipliedPoly[polyOrder] = Complex.ZERO;
				}
				
				Complex cMultiplied = factors[i].multiply(p.factors[j]);
				multipliedPoly[polyOrder] = multipliedPoly[polyOrder].add(cMultiplied);
			}
		}
		
		return new ComplexPolynomial(multipliedPoly);
	}
	
	/**
	 * Returns a new ComplexPolynomial that is the first derivation
	 * of the current polynomial
	 * 
	 * @return first derivation of current polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedPoly = new Complex[factors.length-1];
		
		for (int i = 1; i < factors.length; i++) {
			Complex der = factors[i];
			
			double derivedRe = der.getRe() * i;
			double derivedIm = der.getIm() * i;
			
			derivedPoly[i-1] = new Complex(derivedRe, derivedIm);
		}
		
		return new ComplexPolynomial(derivedPoly);
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
		
		Complex res = Complex.ZERO;
		
		for (int i = 0; i < factors.length; i++) {
			res = res.add(factors[i].multiply(z.power(i)));
		}
		
		return res;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(factors);
	}
}
