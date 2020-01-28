package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Implementation of the ListModel interface. This PrimListModel generates
 * consecutive prime numbers on demand.
 * 
 * @author Vice Ivušić
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/** list of prime numbers generated so far */
	private List<Integer> primes = new ArrayList<>();
	/** list of listeners */
	private List<ListDataListener> listeners = new ArrayList<>();

	/** first prime number */
	private static final int FIRST_PRIME = 2;
	
	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		if (listeners.contains(l)) return;
		
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		if (!listeners.contains(l)) return;
		
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}
	
	/**
	 * Generates the next prime number and updates state.
	 * 
	 * @return next prime number
	 */
	public int next() {
		int size = primes.size();
		
		if (size == 0) {
			primes.add(FIRST_PRIME);
			update();
			return FIRST_PRIME;
		}
		
		int potentialPrime = primes.get(size-1)+1;

		while (isPrime(potentialPrime) == false) {
			potentialPrime++;
		}
		
		primes.add(potentialPrime);
		update();
		return potentialPrime;
		
	}
	
	/**
	 * Helper method for determining a number's primality.
	 * 
	 * @param n number to determine primality for
	 * @return true if specified number is prime
	 */
	private boolean isPrime(int n) {
		if (n <= 1) return false;
		if (n <= 3) return true;
		
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		}
		
		int i = 5;
		while (i*i <= n) {
			if (n % i == 0 || n % (i + 2) == 0) {
				return false;
			}
			i += 6;
		}
		
		return true;
	}

	/**
	 * Helper method for notifying potential listeners of the change in state.
	 * 
	 */
	private void update() {
		ListDataEvent e = new ListDataEvent(
			this,
			ListDataEvent.INTERVAL_ADDED,
			primes.size(),
			primes.size()
		);
		
		for (ListDataListener l : listeners) {
			l.contentsChanged(e);
		}
	}

}
