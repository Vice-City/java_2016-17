package hr.fer.zemris.math;

/**
 * Represents a three-dimensional vector. Offers various methods
 * for vector algebra, such as calculating a vector's norm,
 * normalizing a vector, adding, subtracting and scaling vectors,
 * calculating dot and cross products and retrieving each of the
 * vector's x, y or z components.
 * 
 * <p>Methods which result in new vectors return new objects
 * instead of modifying existing ones, so multiple methods
 * may be chained one after the other.
 * 
 * @author Vice Ivušić
 *
 */
public class Vector3 {

	/** vector's x component */
	private double x;
	/** vector's y component */
	private double y;
	/** vector's z component */
	private double z;
	
	/**
	 * Constructs a new Vector3 with the specified components.
	 * 
	 * @param x vector's x component
	 * @param y vector's y component
	 * @param z vector's z component
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Returns current vector's x component.
	 * 
	 * @return current vector's x component
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns current vector's y component.
	 * 
	 * @return current vector's y component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns current vector's z component.
	 * 
	 * @return current vector's z component
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns the current vector's norm, i.e. its 'length'.
	 * 
	 * @return current vector's norm
	 */
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Returns a normalized version of the current vector. Returns
	 * a null vector if current vector is also a null vector.
	 * 
	 * @return normalized version of current vector
	 */
	public Vector3 normalized() {
		double norm = norm();
		
		if (norm < 10e-9) {
			return new Vector3(0, 0, 0);
		}
		
		return new Vector3(
			x/norm,
			y/norm,
			z/norm
		);
	}
	
	/**
	 * Returns a new vector made by adding the current and
	 * specified vector.
	 * 
	 * @param other vector to add
	 * @return vector made by adding current and specified vector
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public Vector3 add(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		return new Vector3(
			x + other.x,
			y + other.y,
			z + other.z
		);
	}
	
	/**
	 * Returns a new vector made by subtracting the specified vector
	 * from the current vector.
	 * 
	 * @param other vector to subtract with
	 * @return vector made by subtracting the specified vector
	 *	       from the current vector.
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public Vector3 sub(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		return new Vector3(
			x - other.x,
			y - other.y,
			z - other.z
		);
	}
	
	/**
	 * Returns the dot product of the current and specified vector.
	 * 
	 * @param other vector to calculate dot product with
	 * @return dot product of current and specified vector
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public double dot(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		return x*other.x + y*other.y + z*other.z;
	}
	
	/**
	 * Returns a new vector made by calculating the cross product
	 * of the current and specified vector.
	 * 
	 * @param other vector to calculate cross product with
	 * @return cross product of current and specified vector
	 * @throws IllegalArgumentException if the specified vector is null
	 */
	public Vector3 cross(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		return new Vector3(
			y*other.z - z*other.y,
			z*other.x - x*other.z,
			x*other.y - y*other.x
		);
	}
	
	/**
	 * Returns a new vector made by multiplying all of the
	 * current vector's values with the specified scalar value.
	 * 
	 * @param s scalar value to multiply with
	 * @return new vector made by multiplying all of the current 
	 * 		   vector's values with the specified scalar value
	 */
	public Vector3 scale(double s) {
		return new Vector3(
			x*s,
			y*s,
			z*s
		);
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
	public double cosAngle(Vector3 other) {
		if (other == null) {
			throw new IllegalArgumentException("Argument other cannot be null!");
		}
		
		double norm1 = norm();
		double norm2 = other.norm();
		
		if (norm1 < 10e-9 || norm2 < 10e-9) {
			return Double.NaN;
		}
		
		return dot(other) / (norm() * other.norm());
	}
	
	/**
	 * Returns current vector's components as an array of double values.
	 * 
	 * @return current vector's components as an array of double values
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}
	
}
