package hr.fer.zemris.java.gui.charts;

/**
 * Models a pair of values.
 * 
 * @author Vice Ivušić
 *
 */
public class XYValue {

	/** x value */
	private int x;
	/** y value */
	private int y;
	
	/**
	 * Creates a new XYValue with the specified parameters.
	 * 
	 * @param x x value
	 * @param y y value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns current pair's x value.
	 * 
	 * @return current pair's x value
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns current pair's y value.
	 * 
	 * @return current pair's y value
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof XYValue))
			return false;
		XYValue other = (XYValue) obj;
		if (x != other.x)
			return false;
		return true;
	}
	
	
}
