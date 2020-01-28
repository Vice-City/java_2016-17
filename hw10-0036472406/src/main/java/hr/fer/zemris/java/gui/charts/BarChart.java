package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Models a bar chart. Expects a list of XYValue objects to be passed on
 * as the main argument (these pairs are meant to be displayed in a coordinate system),
 * along with some helpful information such as the text to display along the
 * x and y axis, and the minimum, maximum and step of the y axis values.
 * 
 * @author Vice Ivušić
 *
 */
public class BarChart {

	/** list of XYValue objects */
	private List<XYValue> values;
	/** text to display along the x axis */
	private String xText;
	/** text to display along the y axis */
	private String yText;
	/** minimum y to display */
	private int yMin;
	/** maximum y to display */
	private int yMax;
	/** step of ys to iterate over */
	private int yStep;
	
	/**
	 * Constructs a new BarChart with the specified values. Assumes it will be passed
	 * on valid values (no null values) and doesn't check if yMin is smaller than yMax,
	 * or if the yStep is negative or not, and gives no warnings otherwise.
	 * 
	 * @param values list of XYValue objects
	 * @param xText text to display along the x axis
	 * @param yText text to display along the y axis
	 * @param yMin minimum y to display
	 * @param yMax maximum y to display
	 * @param yStep step of ys to iterate over
	 */
	public BarChart(List<XYValue> values, String xText, String yText, int yMin, int yMax, int yStep) {
		this.values = values;
		this.xText = xText;
		this.yText = yText;
		this.yMin = yMin;
		this.yMax = yMax;
		this.yStep = yStep;
	}
	
	/**
	 * Returns current bar chart's list of XYValues.
	 * 
	 * @return current bar chart's list of XYValues
	 */
	public List<XYValue> getValues() {
		return values;
	}
	
	/**
	 * Returns current bar chart's x axis text.
	 * 
	 * @return current bar chart's x axis text
	 */
	public String getXText() {
		return xText;
	}
	
	/**
	 * Returns current bar chart's y axis text.
	 * 
	 * @return current bar chart's y axis text
	 */
	public String getYText() {
		return yText;
	}
	
	/**
	 * Returns current bar chart's minimum y.
	 * 
	 * @return current bar chart's minimum y
	 */
	public int getYMin() {
		return yMin;
	}
	
	/**
	 * Returns current bar chart's maximum y.
	 * 
	 * @return current bar chart's maximum y
	 */
	public int getYMax() {
		return yMax;
	}
	
	/**
	 * Returns current bar chart's step y.
	 * 
	 * @return current bar chart's step y
	 */
	public int getYStep() {
		return yStep;
	}
	
}
