package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A CalcLayout object lays out a container roughly in a 5 by 7 grid, with 
 * the first cell of the grid (at position 1,1) taking up 5 cells in total.
 * Each cell can (but doesn't have to) contain a component. All components
 * will be resized according to scale as the container using CalcLayout
 * changes its size.
 * 
 * <p>A gap between the components may be optionally specified. Components
 * must be added along with a valid {@link RCPosition} constraint object.
 * Valid positions range from 1 to 5 for the horizontal components, i.e.
 * the row, and from 1 to 7 for the vertical components, i.e. columns.
 * The constraint may also be specified with a string which would correspond
 * to an RCPosition object, with the format of {@code "value,value"}.
 * 
 * <p>CalcLayout's default layout makes it especially convenient for creating
 * a custom calculator.
 * 
 * @author Vice Ivušić
 *
 */
public class CalcLayout implements LayoutManager2 {

	/** map mapping components to their RCPosition constraints */
	private Map<Component, RCPosition> positions = new HashMap<>();
	/** size of the gap between components */
	private int gapSize;
	
	/** number of rows */
	private static final int ROWS = 5;
	/** number of columns */
	private static final int COLUMNS = 7;
	
	/**
	 * Creates a new CalcLayout layout manager with the specified gap size.
	 * The gap size specifies how much space is left between each component.
	 * Cannot be smaller than zero.
	 * 
	 * @param gapSize gap between components
	 */
	public CalcLayout(int gapSize) {
		if (gapSize < 0) {
			gapSize = 0;
		}
		
		this.gapSize = gapSize;
	}
	
	/**
	 * @throws IllegalArgumentException if the specified constraint is not valid
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, name);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		positions.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calcDimension(parent, Component::getPreferredSize);
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calcDimension(parent, Component::getMinimumSize);
	}
	
	@Override
	public void layoutContainer(Container parent) {
		Insets parentInsets = parent.getInsets();
		
		int availableWidth = parent.getWidth() - parentInsets.left - parentInsets.right;
		int availableHeight = parent.getHeight() - parentInsets.top - parentInsets.bottom;
		
		int compWidth = (availableWidth - (COLUMNS-1) * gapSize) / COLUMNS;
		int compHeight = (availableHeight - (ROWS-1) * gapSize) / ROWS;
		
		int leftoverWidth = (availableWidth - (COLUMNS-1) * gapSize) % COLUMNS;
		int leftoverHeight = (availableHeight - (ROWS-1) * gapSize) % ROWS;
		
		for (Entry<Component, RCPosition> entry : positions.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			
			if (pos.isFirstPosition()) {
				comp.setBounds(
					parentInsets.left, 
					parentInsets.top, 
					compWidth*ROWS+gapSize*(ROWS-1), 
					compHeight
				);
				continue;
			}

			int col = (pos.getColumn()-1);
			int row = (pos.getRow()-1);

			comp.setBounds(
				col*compWidth + col*gapSize + parentInsets.left,
				row*compHeight + row*gapSize + parentInsets.top,
				compWidth + (col == COLUMNS-1 ? leftoverWidth : 0),
				compHeight + (row == ROWS-1 ? leftoverHeight : 0)
			);
		}

	}

	/**
	 * @throws IllegalArgumentException if the specified constraint is not valid
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints instanceof String) {
			constraints = parseConstraint((String) constraints);
		}
		
		if (!(constraints instanceof RCPosition)) {
			throw new IllegalArgumentException("Argument constraints must be RCPosition object!");
		}
		
		RCPosition constraint = (RCPosition) constraints;
		
		if (positions.containsValue(constraint)) {
			throw new IllegalArgumentException("Specified constraint is already in use by a component!");
		}
		
		if (positions.containsKey(comp)) {
			throw new IllegalArgumentException("Specified component already has a set constraint!");
		}
		
		checkConstraint(constraint);
		
		positions.put(comp, constraint);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
		return;
	}

	/**
	 * Helper method for calculating the maximum dimensions of all the
	 * children components of the specified parent container, with the 
	 * specified strategy governing which dimension (e.g. minimum,
	 * maximum, preferred) is being taken into account.
	 * 
	 * @param parent container with children components
	 * @param getter {@link SizeGetter} strategy object specifying which dimension
	 * 				 is being calculated
	 * @return calculated dimension
	 */
	private Dimension calcDimension(Container parent, SizeGetter getter) {
		int n = parent.getComponentCount();
		
		Dimension dim = new Dimension(0, 0);
		
		for (int i = 0; i < n; i++) {
			Component child = parent.getComponent(i);
			
			if (positions.get(child).isFirstPosition()) {
				Dimension childDim = getter.getSize(child);
				
				if (childDim != null) {
					dim.width = Math.max(dim.width, (childDim.width-4*gapSize)/5);
					dim.height = Math.max(dim.height, childDim.height);
				}
				continue;
			}
			
			Dimension childDim = getter.getSize(child);
			
			if (childDim != null) {
				dim.width = Math.max(dim.width, childDim.width);
				dim.height = Math.max(dim.height, childDim.height);
			}
		}
		
		dim.width = dim.width * COLUMNS + gapSize * (COLUMNS-1);
		dim.height = dim.height * ROWS + gapSize * (ROWS-1);
		
		Insets parentInsets = parent.getInsets();
		dim.width += parentInsets.left + parentInsets.right;
		dim.height += parentInsets.top + parentInsets.bottom;
		
		return dim;
	}

	/**
	 * Helper method for checking if the specified constraint is valid.
	 * 
	 * @param constraint constraint being checked
	 * @throws IllegalArgumentException if the specified constraint is not valid
	 */
	private void checkConstraint(RCPosition constraint) {
		int row = constraint.getRow();
		int col = constraint.getColumn();
		
		if (row<1 || row>ROWS || col<1 || col>COLUMNS) {
			throw new IllegalArgumentException(
				"Minimum value for either row or column is 1; maximum for row is 5, for column it's 7."
			);
		}
		
		if (row == 1 && (col==2 || col == 3 || col==4 || col==5))  {
			throw new IllegalArgumentException(
				"Constraints (1,2), (1,3), (1,4) and (1,5) are not allowed!"
			);
		}
		
	}

	/**
	 * Helper method which parses the content of the specified input
	 * into an RCPosition object. Valid format of input strings is
	 * {@code "value,value"}.
	 * 
	 * @param input input to be parsed
	 * @return parsed RCPosition object
	 * @throws IllegalArgumentException if the input is not in a valid format
	 */
	private RCPosition parseConstraint(String input) {
		String constraint = input.replaceAll("\\s", "");
		Pattern p = Pattern.compile("([0-9]+),([0-9]+)");
		Matcher m = p.matcher(constraint);
		
		if (!m.matches()) {
			throw new IllegalArgumentException("Constraint must be given in form VALUE,VALUE!");
		}
		
		int row = Integer.parseInt(m.group(1));
		int column = Integer.parseInt(m.group(2));
		
		return new RCPosition(row, column);
	}

	/**
	 * Helper interface specifying a single method which takes a component
	 * and returns one of its dimensions.
	 * @author Vice Ivušić
	 *
	 */
	private interface SizeGetter {
		/**
		 * Returns a dimension for the specified component.
		 * 
		 * @param comp component object
		 * @return a dimension for the specified component
		 */
		Dimension getSize(Component comp);
	}

}
