package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Represents a wrapper of any object. In addition to storing
 * the object, it offers multiple arithmetic operations if the
 * stored object is semantically a kind of a number, i.e. if 
 * the stored object is an Integer, Double or a String that
 * can be parsed into either one of those types.
 * 
 * <p>Offers methods for retrieving the currently stored value and
 * for setting a new value. Also offers methods for adding,
 * subtracting, multiplying and diving and comparing the current
 * value representing a number with another value representing
 * a number. 
 * 
 * <p>Note that these arithmetic operations <i>change</i>
 * the currently stored value of the current ValueWrapper object,
 * while leaving the argument object unchanged. Wrapped null
 * values are considered integers of zero for purposes of calculation.
 * 
 * @author Vice Ivušić
 *
 */
public class ValueWrapper {

	/** currently stored object **/
	private Object value;
	
	/**
	 * Creates a new ValueWrapper object with the specified
	 * Object value, which may also be null.
	 * 
	 * @param value Object value to be stored
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * Returns the currently stored Object value.
	 * 
	 * @return the currently stored Object value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Sets the currently stored Object value to the specified value.
	 * 
	 * @param value new value for the currently stored Object
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Modifies the currently stored number value by adding 
	 * the specified number value to it.
	 * 
	 * @param incValue number value to add to currently stored number
	 * @throws IllegalArgumentException if either the currently stored
	 * 		   value or the specified value aren't semantically numbers
	 */
	public void add(Object incValue) {
		value = applyFunction(value, incValue, (v1, v2) -> v1+v2);
	}
	
	/**
	 * Modifies the currently stored number value by subtracting 
	 * the specified number value from it.
	 * 
	 * @param decValue number value to subtract from the currently stored number
	 * @throws IllegalArgumentException if either the currently stored
	 * 		   value or the specified value aren't semantically numbers
	 */
	public void subtract(Object decValue) {
		value = applyFunction(value, decValue, (v1, v2) -> v1-v2);
	}
	
	/**
	 * Modifies the currently stored number value by multiplying 
	 * the specified number value with it.
	 * 
	 * @param mulValue number value to multiply the currently stored number with
	 * @throws IllegalArgumentException if either the currently stored
	 * 		   value or the specified value aren't semantically numbers
	 */
	public void multiply(Object mulValue) {
		value = applyFunction(value, mulValue, (v1, v2) -> v1*v2);
		}
	
	/**
	 * Modifies the currently stored number value by dividing 
	 * it with the specified number value.
	 * 
	 * @param divValue number value to divide the currently stored number with
	 * @throws IllegalArgumentException if either the currently stored
	 * 		   value or the specified value aren't semantically numbers
	 * 		   or if the specified value is zero
	 */
	public void divide(Object divValue) {
		BiFunction<Double, Double, Double> divisionFunction = (dividend, divisor) -> {
			if (Math.abs(divisor) < 10e-6) {
				throw new IllegalArgumentException("Can't divide by zero!");
			}
			
			return dividend / divisor;
		};
		
		value = applyFunction(value, divValue, divisionFunction);
	}
	
	/**
	 * Compares the currently stored number value with the specified
	 * number value. If both values are null, they are considered
	 * equal.
	 * 
	 * @param withValue number value to compare the currently stored number with
	 * @return -1 if first number is lesser than the second number;
	 * 			1 if the first number is greater than the second number;
	 * 			0 if they are equal
	 * @throws IllegalArgumentException if either the currently stored
	 * 		   value or the specified value aren't semantically numbers
	 */
	public int numCompare(Object withValue) {
		if (value == null && withValue == null) {
			return 0;
		}
		
		QuantumDouble arg1 = QuantumDouble.makeFrom(value);
		QuantumDouble arg2 = QuantumDouble.makeFrom(withValue);
		
		return arg1.compareTo(arg2);
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	/**
	 * Applies the specified function onto the specified objects
	 * representing numbers. If both objects are Integers (or null),
	 * the object returned will be an Integer. Otherwise, if at least
	 * one object is a Double, the object returned will be a Double.
	 * The specified objects may also be Strings representing either
	 * an Integer or Double.
	 * 
	 * @param obj1 first operand
	 * @param obj2 second operand
	 * @param operator a BiFunction object representing an arithmetic operation
	 * 		  on two numbers
	 * @return an Integer or Double object representing the result of the
	 * 		   specified operator's operation
	 * @throws IllegalArgumentException if either of the specified objects isn't an
	 * 	   	   Integer, Double or a parsable String, or if the specified operator is null
	 */
	private static Object applyFunction(Object obj1, 
										Object obj2, 
										BiFunction<Double, Double, Double> operator) {
		if (operator == null) {
			throw new IllegalArgumentException("Specified operator must not be null!");
		}
		
		QuantumDouble arg1 = QuantumDouble.makeFrom(obj1);
		QuantumDouble arg2 = QuantumDouble.makeFrom(obj2);
		
		return arg1.applyFunction(arg2, operator).value();
	}
	
}
