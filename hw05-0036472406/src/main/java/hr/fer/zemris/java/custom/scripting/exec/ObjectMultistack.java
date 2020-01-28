package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a collection of stacks mapped to string values.
 * There may only be one stack per unique key. Each stack
 * contains ValueWrapper objects.
 * 
 * <p>Neither the keys nor the values, i.e. the ValueWrapper
 * objects, may be null. However, the stacks may contain
 * ValueWrapper objects which wrap null references.
 * 
 * <p>Offers methods for pushing a ValueWrapper object onto
 * the specified stack, for popping an object from the specified
 * stack as well for simply peeking into the current object
 * on top of the specified stack. Also offers a method for 
 * inquiring whether the specified stack is empty or not.
 * 
 * @author Vice Ivušić
 *
 */
public class ObjectMultistack {

	/** map with MultistackEntry nodes mapped to string keys **/
	private Map<String, MultistackEntry> stackMap;
	
	/**
	 * Creates a new empty ObjectMultistack.
	 */
	public ObjectMultistack() {
		stackMap = new HashMap<>();
	}
	
	/**
	 * Pushes the specified ValueWrapper object onto the top of the
	 * specified stack.
	 * 
	 * @param name mapped stack onto which the object is being pushed
	 * @param valueWrapper object being pushed on top of the stack
	 * @throws IllegalArgumentException if either of the specified
	 * 		   parameters is null
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (name == null) {
			throw new IllegalArgumentException("Argument name must not be null!");
		}
		
		if (valueWrapper == null) {
			throw new IllegalArgumentException("Argument valueWrapper must not be null!");
		}
		
		MultistackEntry newEntry = new MultistackEntry(valueWrapper, null);
		
		MultistackEntry headEntry = stackMap.get(name);
		if (headEntry != null) {
			newEntry.previous = headEntry;
		}
		
		stackMap.put(name, newEntry);
	}
	
	/**
	 * Removes and returns a ValueWrapper object from the top of
	 * the specified stack. 
	 * 
	 * @param name mapped stack from which the object is being removed
	 * @return object that has been removed from the stack
	 * @throws EmptyStackException if the specified stack is empty
	 */
	public ValueWrapper pop(String name) {
		if (isEmpty(name)) {
			throw new EmptyStackException();
		}
		
		MultistackEntry headEntry = stackMap.get(name);
		
		// just so the map doesn't accumulate keys mapped to empty stacks...
		if (headEntry.previous == null) {
			stackMap.remove(name);
		} else {
			stackMap.put(name, headEntry.previous);
		}
		
		return headEntry.valueWrapper;
	}
	
	/**
	 * Returns the ValueWrapper object from the top of the
	 * specified stack. Does <b>not</b> change the contents of
	 * the specified stack.
	 * 
	 * @param name mapped stack from which the object is being returned
	 * @return object that has been removed from the stack
	 * @throws EmptyStackException if the specified stack is empty
	 */
	public ValueWrapper peek(String name) {
		if (isEmpty(name)) {
			throw new EmptyStackException();
		}
		
		return stackMap.get(name).valueWrapper;
	}
	
	/**
	 * Checks whether the specified stack is empty, i.e. whether
	 * it has at least one element in it.
	 * 
	 * @param name mapped stack which is being checked 
	 * @return <b>true</b> iff the specified stack is empty
	 */
	public boolean isEmpty(String name) {
		return stackMap.get(name) == null;
	}
	
	/**
	 * Represents a node of a single ObjectMultistack stack. Contains
	 * the ValueWrapper object being pushed onto the stack.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class MultistackEntry {
		
		/** ValueWrapper object being pushed onto the stack **/
		private ValueWrapper valueWrapper;
		/** reference to the preceding MultistackEntry node within the same stack **/
		private MultistackEntry previous;
		
		/**
		 * Creates a new MultistackEntry object with the specified ValueWrapper
		 * object and the specified preceding MultistackEntry node.
		 * 
		 * @param valueWrapper the ValueWrapper object being pushed onto the stack
		 * @param previous the preceding MultistackEntry node within the same stack
		 * @throws IllegalArgumentException if the specified ValueWrapper object is null
		 */
		public MultistackEntry(ValueWrapper valueWrapper, MultistackEntry previous) {
			if (valueWrapper == null) {
				throw new IllegalArgumentException("Argument valueWrapper must not be null!");
			
			}
			this.valueWrapper = valueWrapper;
			this.previous = previous;
		}
		
		/**
		 * Returns this node's ValueWrapper object.
		 * 
		 * @return this node's ValueWrapper object
		 */
		public ValueWrapper getValueWrapper() {
			return valueWrapper;
		}
		
	}
}
