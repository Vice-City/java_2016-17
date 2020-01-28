package hr.fer.zemris.java.hw05.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a collection of sequential prime numbers, from the
 * first prime number beyond. The only way to access the members 
 * of this collection is by iterating through all of its members 
 * using the Iterator available through one of this collection's methods. 
 * 
 * @author Vice Ivušić
 *
 */
public class PrimesCollection implements Iterable<Integer> {

	/** amount of sequential prime numbers this collection holds **/
	private int primeAmount;
	
	/**
	 * Creates a new PrimesCollection object with the specified amount
	 * of sequential prime numbers.
	 * 
	 * @param primeAmount amount of prime numbers this collection is to hold
	 * @throws IllegalArgumentException if the specified prime amount is less
	 * 		   than zero
	 */
	public PrimesCollection(int primeAmount) {
		if (primeAmount < 0) {
			throw new IllegalArgumentException("Argument primeAmount must be at least 0!");
		}
		this.primeAmount = primeAmount;
	}
	
	/**
	 * Returns an iterator over elements of type Integer.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new PrimeIterator();
	}

	/**
	 * Represents an iterator over integer prime numbers. Offers
	 * methods for determining whether there is still a prime number
	 * to retrieve and a method for retrieving the next prime number.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private class PrimeIterator implements Iterator<Integer> {

		/** amount of primes already returned by the iterator **/
		private int processedPrimes;
		/** the last prime number returned by the iterator **/
		private int lastPrime;
		
		/** the first prime number used for calculation **/
		private static final int FIRST_PRIME = 2;
		
		@Override
		public boolean hasNext() {
			return processedPrimes < primeAmount;
		}

		@Override
		public Integer next() {
			if (processedPrimes == primeAmount) {
				throw new NoSuchElementException("There are no more elements to iterate over!");
			}
			
			if (processedPrimes == 0) {
				lastPrime = FIRST_PRIME;
				processedPrimes++;
				return lastPrime;
			}
			
			int potentialPrime = lastPrime+1;
			int divisor = FIRST_PRIME;
			while (true) {
				// found the next prime!
				if (potentialPrime == divisor) {
					break;
				}
				
				// potentialPrime may still be prime
				if (potentialPrime % divisor != 0) {
					divisor++;
					continue;
				}
				
				// potentialPrime definitely isn't prime => next candidate!
				potentialPrime++;
				divisor = FIRST_PRIME;
			}
			
			lastPrime = potentialPrime;
			processedPrimes++;
			return lastPrime;
			
		}
		
	}
	
	
}
