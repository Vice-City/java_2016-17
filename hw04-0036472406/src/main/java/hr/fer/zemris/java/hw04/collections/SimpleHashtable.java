package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a collection of [Key, Value] pairs. Each pair's
 * key is unique inside the hash table; there may not exist
 * multiple instances of pairs which share the same key.
 * 
 * <p>The collection offers methods for inserting new pairs,
 * for retrieving a pair based on its key (executed in O(1)
 * complexity), for retrieving a pair based on its value
 * (executed in O(n) complexity), for clearing the collection
 * of all its pairs, and various methods which offer information
 * about the collection; such as its size, and whether it contains
 * a particular key or value.
 * 
 * @author Vice Ivušić
 *
 * @param <K> the type of keys maintained by this hash table
 * @param <V> the type of mapped values
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	
	/**
	 * Represents a single [Key, Value] pair. The key cannot be
	 * null, while the value can. A pair's key cannot be changed
	 * once set, and it is advisable to use immutable objects
	 * as a pair's key.
	 * 
	 * <p>Has methods for retrieval of key, retrieval of value,
	 * setting the value and for generating a String representation
	 * of the pair.
	 * 
	 * @author Vice Ivušić
	 *
	 * @param <K> the type of key used for this pair
	 * @param <V> the type of value contained in this pair
	 */
	public static class TableEntry<K, V> {
		/** key of this pair **/
		private K key;
		/** value of this pair **/
		private V value;
		/** reference to the next pair in a particular hash table slot */
		private TableEntry<K, V> next;
		
		/**
		 * Constructs a new TableEntry with the specified parameters.
		 * 
		 * @param key key of the pair
		 * @param value value of the pair
		 * @param next reference to the next pair
		 * @throws IllegalArgumentException if key is null
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			if (key == null) {
				throw new IllegalArgumentException(
						"Key cannot be null!"
				);
			}
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Returns this pair's key.
		 * 
		 * @return this pair's key
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Returns this pair's value.
		 * 
		 * @return this pair's value
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Sets this pair's value to specified value.
		 * 
		 * @param value new value for pair
		 */
		public void setValue(V value) {
			this.value = value;
		}
	
		@Override
		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}

	/** amount of pairs stored in this hash table **/
	private int size;
	/** array used for storage of pairs **/
	private TableEntry<K, V>[] table;
	/** number of times a pair was added or removed from this hash table **/
	private int modificationCount;
	
	/** default amount of slots in the hash table's internal array **/
	private final static int DEFAULT_CAPACITY = 16;
	
	/**
	 * Constructs a hash table with the specified initial capacity.
	 * The initial capacity is rounded to the next biggest power of two.
	 * 
	 * @param capacity initial capacity (number of slots) of this hash table
	 * @throws IllegalArgumentException if the specified capacity is less
	 * 		   than one
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException(
					"Capacity must be at least 1! Received: " + capacity
			);
		}
		
		capacity = roundToNearestPowerOfTwo(capacity);
		
		table = (TableEntry<K, V>[]) new TableEntry[capacity];
	}
	
	/**
	 * Constructs a hash table with an initial capacity of 16 slots.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Helper method for calculating the next biggest or equal
	 * power of two for the specified number.
	 * 
	 * @param number number for which to calculate the power of two
	 * @return next biggest or equal power of two for the specified number
	 */
	private static int roundToNearestPowerOfTwo(int number) {
		// if number is already a power of two
		if (number == 1 || number/2*2 == number) {
			return number;
		}
	
		int power = 0;
		int remainder = number;
		
		while (remainder != 0) {
			remainder /= 2;
			power++;
		}
		
		return 1 << power;
	}

	/**
	 * Adds a pair with the specified key and value. The key may not
	 * be null.
	 * 
	 * @param key key of this pair 
	 * @param value value of this pair
	 * @throws IllegalArgumentException if the key is null
	 */
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException(
					"Key cannot be null!"
			);
		}

		checkAndSetCapacity();
		
		int slot = getSlot(key);
		if (table[slot] == null) {
			table[slot] = new TableEntry<>(key, value, null);
			size++;
			modificationCount++;
			return;
		}
		
		TableEntry<K, V> currentEntry = table[slot];
		while (true) {
			if (currentEntry.key.equals(key)) {
				currentEntry.setValue(value);
				return;
			}
			
			if (currentEntry.next == null) {
				currentEntry.next = new TableEntry<>(key, value, null);
				size++;
				modificationCount++;
				return;
			}
			
			currentEntry = currentEntry.next;
		}
	}
	
	/**
	 * Helper method for expanding the number of slots
	 * of the hash table. Does nothing if the number of 
	 * pairs stored in the table isn't close to the number
	 * of slots available in the hash table. Helps keep
	 * overflow to a minimum.
	 */
	@SuppressWarnings("unchecked")
	private void checkAndSetCapacity() {
		final double MAX_CAPACITY_FACTOR = 0.75;
		
		if (size < MAX_CAPACITY_FACTOR * table.length) {
			return;
		}
		
		TableEntry<K, V>[] oldTable = table;
		table = (TableEntry<K, V>[]) new TableEntry[2*oldTable.length];
		
		/*
		 * Goes over every reference in the old hash table and puts it in
		 * the appropriate place in the new hash table. No new objects
		 * are allocated.
		 */
		for (int i = 0; i < oldTable.length; i++) {
			TableEntry<K, V> currentOldEntry = oldTable[i];
			
			// goes over old entries in current slot in old table
			while (currentOldEntry != null) {
				int slot = getSlot(currentOldEntry.key);
				
				if (table[slot] == null) {
					table[slot] = currentOldEntry;
					currentOldEntry = currentOldEntry.next;
					table[slot].next = null;
					continue;
				}
				
				TableEntry<K, V> currentEntry = table[slot];
				while (true) {
					if (currentEntry.next == null) {
						currentEntry.next = currentOldEntry;
						currentOldEntry = currentOldEntry.next;
						currentEntry.next.next = null;
						break;
					}
					currentEntry = currentEntry.next;
				}
				
			}
		}
		
		// internal structure has changed => have to account for this
		modificationCount++;
	}

	/**
	 * Helper method for calculating the hash  table slot 
	 * for the specified key.
	 * 
	 * @param key key of the pair in question
	 * @return hash table slot index where the pair should be stored
	 */
	private int getSlot(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * Returns the value of the pair with the specified key.
	 * Returns null if the key is null or if the pair with the
	 * specified key does not exist in the hash table.
	 * 
	 * @param key key of the pair in question
	 * @return value of the pair in question
	 */
	public V get(Object key) {
		if (key == null) return null;
		
		int slot = getSlot(key);
		TableEntry<K, V> currentEntry = table[slot];
		
		while (currentEntry != null) {
			if (currentEntry.key.equals(key)) {
				return currentEntry.value;
			}
			currentEntry = currentEntry.next;
		}
		
		return null;
	}
	
	/**
	 * Returns the size of this hash table, i.e. the number
	 * of pairs currently stored in it.
	 * 
	 * @return size of this hash table
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns <b>true</b> if the hash table contains a pair
	 * with the specified key. Returns false if the key is null.
	 * 
	 * @param key key of the pair in question
	 * @return <b>true</b> iff the pair with the specified key 
	 * 		   exists within the table
	 */
	public boolean containsKey(Object key) {
		if (key == null) return false;
		
		int slot = getSlot(key);
		TableEntry<K, V> currentEntry= table[slot];
		
		while (currentEntry != null) {
			if (currentEntry.key.equals(key)) {
				return true;
			}
			currentEntry = currentEntry.next;
		}
		
		return false;
	}
	
	/**
	 * Returns <b>true</b> if the hash table contains a pair
	 * with the specified value. If the specified value is null,
	 * returns <b>true</b> if the hash table contains a pair 
	 * with its value set to null.
	 * 
	 * @param value value of the pair in question
	 * @return <b>true</b> iff a pair with the specified value 
	 * 		   exists within the table
	 */
	public boolean containsValue(V value) {
		for (int i = 0; i < table.length; i++) {
			TableEntry<K, V> currentEntry = table[i];
			
			while (currentEntry != null) {
				if (currentEntry.value == null) {
					if (value == null) {
						return true;
					}
					
					return false;
				}
				
				if (currentEntry.value.equals(value)) {
					return true;
				}
				currentEntry = currentEntry.next;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes the pair with the specified key from the
	 * hash table. Does nothing if the key is null or if
	 * the pair with the specified key does not exist.
	 * 
	 * @param key key of the pair in question
	 */
	public void remove(Object key) {
		if (key == null) return;
		
		int slot = getSlot(key);
		if (table[slot] == null) return;
		
		TableEntry<K, V> currentEntry = table[slot];
		
		if (currentEntry.key.equals(key)) {
			table[slot] = currentEntry.next;
			size--;
			modificationCount++;
			return;
		}
		
		while (true) {
			if (currentEntry.next == null) {
				return;
			}
			
			if (currentEntry.next.key.equals(key)) {
				currentEntry.next = currentEntry.next.next;
				size--;
				modificationCount++;
				return;
			}
			
			currentEntry = currentEntry.next;
		}
	}
	
	/**
	 * Returns <b>true</b> if the hash table doesn't have 
	 * any pairs stored within it.
	 * 
	 * @return <b>true</b> iff the hash table is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Removes all pairs from within this hash table.
	 */
	public void clear() {
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
		modificationCount++;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for (int i = 0, pairsRemaining = size; i < table.length; i++) {
			TableEntry<K, V> currentEntry = table[i];
			
			while (currentEntry != null) {
				sb.append(currentEntry);
				
				pairsRemaining--;
				if (pairsRemaining != 0) {
					sb.append(", ");
				}
				
				currentEntry = currentEntry.next;
			}
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * Returns the iterator over elements of type TableEntry<K, V>.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Represents an iterator of this hash table. Allows the user
	 * to remove pairs from the collection during iteration in a
	 * well defined manner.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private class IteratorImpl implements Iterator<TableEntry<K, V>> {
	
		/** the hash table slot which is currently being traversed **/
		private int currentlyVisitingSlot;
		/** number of pairs that have been processed so far **/
		private int numberOfVisitedElements;
		/** number of times this hash table has had a pair removed or added **/
		private int modificationCount;
		
		/** the pair which was last returned during iteration **/
		private TableEntry<K, V> lastEntry;
		/** flag which is set only if a pair has already been removed during current iteration **/
		private boolean removeAlreadyCalled;
		
		/**
		 * Constructs a new iterator.
		 */
		public IteratorImpl() {
			modificationCount = SimpleHashtable.this.modificationCount;
		}
		
		/**
		 * @throws ConcurrentModificationException if the hash table was 
		 * 		   modified from outside the iterator's API
		 */
		@Override
		public boolean hasNext() {
			if (modificationCount != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException(
						"Hashtable was modified from outside iterator!"
				);
			}
			
			return numberOfVisitedElements < size;
		}
	
		/**
		 * @throws ConcurrentModificationException if the hash table was 
		 * 		   modified from outside the iterator's API
		 * @throws NoSuchElementException if the method is called when
		 * 		   there are no more pairs to iterate over
		 */
		@Override
		public TableEntry<K, V> next() {
			if (modificationCount != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException(
						"Hashtable was modified from outside iterator!"
				);
			}
			
			if (numberOfVisitedElements == size) {
				throw new NoSuchElementException("no more elements to return!");
			}
			
			if (lastEntry != null) {
				if (lastEntry.next != null) {
					lastEntry = lastEntry.next;
					
					numberOfVisitedElements++;
					removeAlreadyCalled = false;
					
					return lastEntry;
				}
				
				// can't get next entry from last entry => visit next slot
				currentlyVisitingSlot++;
			}
			
			for (int i = currentlyVisitingSlot; i < table.length; i++) {
				if (table[i] == null) {
					continue;
				}
				
				lastEntry = table[i];
				currentlyVisitingSlot = i;
				
				numberOfVisitedElements++;
				removeAlreadyCalled = false;
				
				break;
			}
			
			return lastEntry;
		}
		
		/**
		 * @throws ConcurrentModificationException if the hash table was 
		 * 		   modified from outside the iterator's API
		 * @throws IllegalStateException if called before the iterator has 
		 * 		   completed at least one iteration, or if called twice
		 * 		   during same iteration
		 */
		@Override
		public void remove() {
			if (modificationCount != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException(
						"Hashtable was modified from outside iterator!"
				);
			}
			
			if (removeAlreadyCalled) {
				throw new IllegalStateException("Can call remove() only once per next()!");
			}
			
			if (lastEntry == null) {
				throw new IllegalStateException("Must call next() before remove() can be called!");
			}
			
			SimpleHashtable.this.remove(lastEntry.key);
			modificationCount++;
			numberOfVisitedElements--;
			removeAlreadyCalled = true;
		}
		
	}
}