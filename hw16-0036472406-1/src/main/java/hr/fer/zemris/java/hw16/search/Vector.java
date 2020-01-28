package hr.fer.zemris.java.hw16.search;

/**
 * Represents a multi-dimensional vector. Offers various methods
 * for vector algebra, such as calculating a vector's norm and
 * normalizing a vector.
 * 
 * @author Vice Ivušić
 *
 */
public class Vector {

	/**
	 * Array of components.
	 */
	private double[] components;

	/**
	 * Last calculated norm of the current vector. Will be null
	 * if out of date.
	 */
	private Double norm;
	
	/**
	 * Creates a new Vector with the specified size. Expects
	 * the user to pass on a size greater than zero and
	 * gives no warnings otherwise.
	 * 
	 * @param size desired size i.e. dimension
	 */
	public Vector(int size) {
		components = new double[size];
	}
	
	/**
	 * Returns the component at the specified index.
	 * 
	 * @param index index of the component in question
	 * @return value of the component at the specified index
	 * @throws IndexOutOfBoundsException if the index isn't between
	 * 		   zero and the vector's size
	 */
	public double getComponent(int index) {
		return components[index];
	}
	
	/**
	 * Sets the value of the component at the specified index to the
	 * specified value.
	 * 
	 * @param index index of the component in quesiton
	 * @param value new value for the component at the specified index
	 * @throws IndexOutOfBoundsException if the index isn't between
	 * 		   zero and the vector's size
	 */
	public void setComponent(int index, double value) {
		components[index] = value;
		norm = null;
	}

	/**
	 * Returns the current vector's norm, i.e. its 'length'.
	 * 
	 * @return current vector's norm
	 */
	public double norm() {
		if (norm != null) {
			return norm;
		}
		
		double squareSum = 0;
		for (double d : components) {
			squareSum += d*d;
		}
		
		norm = Math.sqrt(squareSum);
		return norm;
	}
	
	
	/**
	 * Returns the dot product of the current and specified vector.
	 * 
	 * @param other vector to calculate dot product with
	 * @return dot product of current and specified vector
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public double dot(Vector other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		if (components.length != other.components.length) {
			throw new IllegalArgumentException("Dimensions of vectors do not match!");
		}
		
		double dot = 0;
		for (int i = 0, n = components.length; i < n; i++) {
			dot += components[i] * other.components[i];
		}
		
		return dot;
	}
	
	/**
	 * Returns the cosine of the angle between the current
	 * and the specified vector. Returns Double.NaN if either
	 * of the vectors is a null vector, since in that case
	 * the cosine is not defined.
	 * 
	 * @param other vector to calculate cosine with
	 * @return cosine of the angle between the current and specified vector
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public double cosAngle(Vector other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		double norm1 = norm();
		double norm2 = other.norm();
		
		if (norm1 < 10e-9 || norm2 < 10e-9) {
			return Double.NaN;
		}
		
		return dot(other) / (norm1 * norm2);
	}
	
}
