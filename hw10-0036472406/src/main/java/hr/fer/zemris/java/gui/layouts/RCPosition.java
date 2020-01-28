package hr.fer.zemris.java.gui.layouts;

/**
 * Models a pair of integer values. Meant to be created and
 * passed on as a constraint when adding components using
 * the {@link CalcLayout} layout manager.
 * 
 * @author Vice Ivušić
 *
 */
public class RCPosition {

	/** row value */
	private int row;
	/** column value */
	private int column;
	
	/** indicates whether current RCPosition is in position (1,1) */
	private boolean firstPosition;
	
	/**
	 * Creates a new RCPosition with the specified parameters.
	 * 
	 * @param row row value
	 * @param column column value
	 * @throws IllegalArgumentException if the specified parameters are 
	 * 		   smaller than 1
	 */
	public RCPosition(int row, int column) {
		if (row < 1 || column < 1) {
			throw new IllegalArgumentException("Row and column have to be at least 1!");
		}
		
		this.row = row;
		this.column = column;
		
		if (row == 1 && column == 1) {
			firstPosition = true;
		}
	}
	
	/**
	 * Returns current position's row value.
	 * 
	 * @return current position's row value
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns current position's column value.
	 * 
	 * @return current position's column value
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Returns true if current position is in position (1,1).
	 * 
	 * @return true if current position is in position (1,1)
	 */
	public boolean isFirstPosition() {
		return firstPosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	
	
}
