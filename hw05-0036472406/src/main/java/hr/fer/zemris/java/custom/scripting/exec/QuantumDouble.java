package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Represents a container of an object which semantically represents a number,
 * i.e. an Integer, Double or a String representing either an Integer
 * or Double. It might be a double, or it might not; you don't really
 * know until you look. For the purposes of calculation and modeling 
 * functions, it is fine to pretend this class holds a double value.
 * 
 * <p>Offers instance methods for applying a binary function onto
 * the current QuantumDouble object, as well as for comparing them. 
 * Offers a method which retrieves the currently stored value, 
 * which is either a Double or an Integer.
 * 
 * <p>Instances of this class are created using the provided factory method.
 * 
 * @author Vice Ivušić
 *
 */
public class QuantumDouble {
	
	/** currently stored value **/
	private double quantumValue;
	/** flag indicating that the currently stored value is actually an integer **/
	private boolean isInteger;
	
	/** default value for objects which are null **/
	private static final int DEFAULT_NULL_VALUE = 0;
	
	/**
	 * Creates a new QuantumDouble object with the specified parameters.
	 * 
	 * @param quantumValue value of the number
	 * @param isInteger flag indicating the number is an integer
	 */
	private QuantumDouble(double quantumValue, boolean isInteger) {
		this.quantumValue = quantumValue;
		this.isInteger = isInteger;
	}

	/**
	 * Creates a new QuantumDouble object from the specified object.
	 * The specified object must be either an Integer, Double or a
	 * String representing a parsable Integer or Double.
	 *
	 * <p>If the specified object is null, the created QuantumDouble object
	 * is analogous to the reference returned by Integer.valueOf(0).
	 * Otherwise, the QuantumDouble object is analogous to the reference
	 * returned by Double.valueOf(obj.doubleValue()) if the specified
	 * object is a Double, or Integer.valueOf(obj.intValue()) if the 
	 * specified object is an Integer.
	 * 
	 * @param obj object representing a number
	 * @return a QuantumDouble object constructed from the specified object
	 * @throws IllegalArgumentException if the specified object isn't an
	 * 	   	   Integer, Double or a parsable String
	 */
	public static QuantumDouble makeFrom(Object obj) {
		if (obj == null) {
			return new QuantumDouble(DEFAULT_NULL_VALUE, true);
		}
		
		if (!(obj instanceof Integer) && 
			!(obj instanceof Double) && 
			!(obj instanceof String)) {
			throw new IllegalArgumentException(
					"Argument value is neither an integer, double nor string!"
			);
		}
		
		if (obj instanceof Integer) {
			double value = ((Integer) obj).doubleValue();
			return new QuantumDouble(value, true);
		}
		
		if (obj instanceof Double) {
			double value = ((Double) obj).doubleValue();
			return new QuantumDouble(value, false);
		}
		
		String numberToken = (String) obj;
		boolean isInteger = !numberToken.contains(".") && 
							!numberToken.toUpperCase().contains("E")
		;
		
		try {
			double value = Double.parseDouble(numberToken);
			return new QuantumDouble(value, isInteger);
			
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException(
					"Argument string cannot be parsed into integer or double!"
			);
		}
		
	}
	
	/**
	 * Returns the Object value of the number stored in this
	 * QuantumDouble object. The returned value will be castable
	 * into either an Integer or a Double.
	 * 
	 * @return Integer or Double value stored in this object
	 */
	public Object value() {
		if (isInteger) {
			return Integer.valueOf((int) quantumValue);
		} else {
			return Double.valueOf(quantumValue);
		}
	}
	
	/**
	 * Returns <b>true</b> if the current object holds an
	 * integer value.
	 * 
	 * @return <b>true</b> iff the current object holds an integer value
	 */
	public boolean isInteger() {
		return isInteger;
	}

	/**
	 * Returns a new QuantumDouble object with the specified operation
	 * applied to it.
	 * 
	 * @param operand the second operand in the operation
	 * @param operator the object defining the operation that needs to be done
	 * @return new QuantumDouble object with the applied operation
	 * @throws IllegalArgumentException if the specified operator is null, or
	 * 		   if the result of the operation is undefined for the specified operand
	 */
	public QuantumDouble applyFunction(QuantumDouble operand, 
									   BiFunction<Double, Double, Double> operator) {
		if (operator == null) {
			throw new IllegalArgumentException("Specified operator must not be null!");
		}
		
		double result = operator.apply(quantumValue, operand.quantumValue);
		
		if (this.isInteger && operand.isInteger) {
			return new QuantumDouble(result, true);
		} else {
			return new QuantumDouble(result, false);
		}
	}

	/**
	 * Compares the current QuantumDouble object with the specified one.
	 * 
	 * @param other the QuantumDouble object this one is being compared with
	 * @return -1 if first number is lesser than the second number;
	 * 			1 if the first number is greater than the second number;
	 * 			0 if they are equal
	 */
	public int compareTo(QuantumDouble other) {
		if (Math.abs(quantumValue - other.quantumValue) < 10e-6) {
			return 0;
		}
		
		return Double.compare(quantumValue, other.quantumValue);
	}
}