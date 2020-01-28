package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;

/**
 * Models a component which knows how to display the results of a
 * BarChart object.
 * 
 * @author Vice Ivušić
 *
 */
public class BarChartComponent extends JComponent {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/** bar chart being used to represent information */
	private BarChart chart;
	 
	/** represents the gap between text and axis, and ending of axis and ending of arrow */
	private static final int GAP = 14;
	/**
	 * represents the inner margins for the bar chart; 
	 * independent of the component the chart resides in
	 */
	private static final int MARGINS = 6;
	
	/** represents the width of an axis arrow */
	private static final int ARROW_WIDTH = 4;
	/** represents the length of an axis arrow */
	private static final int ARROW_LENGTH = 8;
	
	/** represents how far the bar chart shadows are cast */
	private static final int SHADOW_CAST = 5;
	
	/** color used for the axii and arrows */
	private static final Color AXIS_COLOR = new Color(155, 154, 154);
	/** color used for the bars */
	private static final Color BAR_COLOR = new Color(244, 119, 72);
	/** color used for the bar shadows */
	private static final Color SHADOW_COLOR = new Color(0, 0, 0, 64);
	/** color used for the background mesh */
	private static final Color MESH_COLOR = new Color(243, 232, 210);
	
	/** stroke used for the axii and background mesh */
	private static final Stroke LINE_STROKE = new BasicStroke(2);
	
	/**
	 * Creates a new BarChartComponent from the specified chart. Assumes
	 * the char will be non-null and gives no warnings otherwise.
	 * 
	 * @param chart BarChart to display information for
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
		
		setPreferredSize(new Dimension(500, 400));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D gd = (Graphics2D) g;
		gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = getSize().width;
		int h = getSize().height;
		
		// colors background to white
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w-1, h-1);
		g.setColor(Color.BLACK);
		
		w = w - 2*MARGINS;
		h = h - 2*MARGINS;
		Rectangle bounds = new Rectangle(MARGINS, MARGINS, w, h);
		
		gd.setFont(new Font(gd.getFont().getFontName(), Font.PLAIN, (int) (gd.getFont().getSize()*1.1)));
		FontMetrics fm = gd.getFontMetrics();
		
		int fontHeight = fm.getHeight();
		String yAxisString = Integer.toString(chart.getYMax());
		int yAxisWidth = fm.stringWidth(yAxisString);
	
		// these two are important
		// ySpace is the space taken up by the left side of the chart
		int ySpace = fontHeight + 2*GAP + yAxisWidth;
		// xSpace is the space taken up by the lower part of the chart
		int xSpace = fontHeight * 2 + GAP*2;
		
		List<Integer> xValuesOrdered = 
			chart.getValues()
			.stream()
			.map(XYValue::getX)
			.sorted()
			.collect(Collectors.toList())
		;
		
		List<Integer> yValuesRevOrder = new ArrayList<>();
		for (int i = chart.getYMin(), n = chart.getYMax(); i < n + chart.getYStep(); i += chart.getYStep()) {
			yValuesRevOrder.add(i);
		}
		Collections.reverse(yValuesRevOrder);
		
		
		drawMesh(gd, new Rectangle(bounds.x+ySpace, bounds.y+GAP, w-ySpace-GAP*2, h-xSpace), xValuesOrdered, yValuesRevOrder, fm);
		drawBars(gd, new Rectangle(bounds.x+ySpace, bounds.y+GAP, w-ySpace-GAP*2, h-xSpace), xValuesOrdered, yValuesRevOrder, fm);
		
		drawYText(gd, new Rectangle(bounds.x, bounds.y+GAP, ySpace, h-xSpace), fm);
		drawYValues(gd, new Rectangle(bounds.x, bounds.y+GAP, ySpace, h-xSpace), yValuesRevOrder, fm);
		drawYAxis(gd, bounds.x+ySpace, bounds.y, bounds.x+ySpace, bounds.y+GAP+h-xSpace);
		drawYArrow(gd, bounds.x+fontHeight+yAxisWidth+2*GAP, bounds.y);
		
		drawXText(gd, new Rectangle(bounds.x+ySpace, bounds.y+GAP+h-xSpace, w-ySpace-GAP*2, xSpace-GAP), fm);
		drawXValues(gd, new Rectangle(bounds.x+ySpace, bounds.y+GAP+h-xSpace, w-ySpace-GAP*2, xSpace-GAP), xValuesOrdered, fm);
		drawXAxis(gd, bounds.x+ySpace, bounds.y+GAP+h-xSpace, bounds.x+w-1, bounds.y+GAP+h-xSpace);
		drawXArrow(gd, bounds.x+w-1, bounds.y+GAP+h-xSpace);
	}

	/**
	 * Helper method for drawing the bar chart's bars.
	 * 
	 * @param gd component's graphics object
	 * @param bounds rectangle within the component to work inside
	 * @param xValues list of x values
	 * @param yValues list of y values
	 * @param fm FontMetrics object for current font
	 */
	private void drawBars(Graphics2D gd, Rectangle bounds, List<Integer> xValues, List<Integer> yValues, FontMetrics fm) {
		Color oldColor = gd.getColor();
		
		List<XYValue> values = chart.getValues();
		
		// first for-loop is building shadows
		double intervalX = bounds.width / (double) xValues.size();
		double intervalY = bounds.height / (double) yValues.size();
		for (int i = 0, n = values.size(); i < n; i++) {
			int yValue = values.get(i).getY();
			if (yValue < chart.getYMin()) {
				continue;
			}
			
			// addedPadding helps align values which aren't multiples of yStep
			double addedPadding = 0;
			int indexOfXValue = xValues.indexOf(values.get(i).getX());
			int indexOfYValue = yValues.indexOf(yValue);
			
			if (indexOfYValue == -1) {
				int newYValue = values.get(i).getY() / chart.getYStep() * chart.getYStep() + chart.getYStep();
				indexOfYValue = yValues.indexOf(newYValue);
				addedPadding =  ((newYValue - yValue) / (double) chart.getYStep()) * intervalY;
			}
			
			gd.setColor(SHADOW_COLOR);
			gd.fillRect(
				(int) Math.ceil(bounds.x+indexOfXValue*intervalX+SHADOW_CAST),
				(int) Math.ceil(bounds.y+(indexOfYValue+1)*intervalY+addedPadding+SHADOW_CAST),
				(int) Math.ceil(intervalX-1),
				(int) Math.ceil(bounds.height-(indexOfYValue+1)*intervalY-addedPadding-SHADOW_CAST)
			);
		}
		
		// second for loop are actual bar charts and lines between them
		for (int i = 0, n = values.size(); i < n; i++) {
			int yValue = values.get(i).getY();
			if (yValue < chart.getYMin()) {
				continue;
			}
			
			double addedPadding = 0;
			int indexOfXValue = xValues.indexOf(values.get(i).getX());
			int indexOfYValue = yValues.indexOf(yValue);
			
			if (indexOfYValue == -1) {
				int newYValue = values.get(i).getY() / chart.getYStep() * chart.getYStep() + chart.getYStep();
				indexOfYValue = yValues.indexOf(newYValue);
				addedPadding =  ((newYValue - yValue) / (double) chart.getYStep()) * intervalY;
			}
			
			// these are the bars
			gd.setColor(BAR_COLOR);
			gd.fillRect(
				(int) (bounds.x+indexOfXValue*intervalX+1),
				(int) (bounds.y+(indexOfYValue+1)*intervalY+addedPadding),
				(int) Math.ceil(intervalX-1),
				(int) Math.ceil(bounds.height-(indexOfYValue+1)*intervalY-addedPadding)
			);
			
			// these are the lines between the bars
			gd.setColor(MESH_COLOR);
			gd.drawLine(
				(int) (bounds.x+(indexOfXValue+1)*intervalX),
				(int) (bounds.y+(indexOfYValue+1)*intervalY+addedPadding),
				(int) (bounds.x+(indexOfXValue+1)*intervalX),
				(int) (bounds.y+bounds.height)
			);
		}
		
		gd.setColor(oldColor);
	}

	/**
	 * Helper method for drawing the bar chart's background mesh.
	 * 
	 * @param gd component's graphics object
	 * @param bounds rectangle within the component to work inside
	 * @param yValues list of y values
	 * @param xValues list of x values
	 * @param fm FontMetrics object for current font
	 */
	private void drawMesh(Graphics2D gd, Rectangle bounds, List<Integer> xValues, List<Integer> yValues, FontMetrics fm) {
		Color oldColor = gd.getColor();
		Stroke oldStroke = gd.getStroke();
		gd.setStroke(LINE_STROKE);
		
		double intervalY = bounds.height / (double) yValues.size();
		for (int i = 1, n = yValues.size(); i <= n; i++) {
			gd.setColor(MESH_COLOR);
			gd.drawLine(
				bounds.x,
				(int) Math.ceil(bounds.y+i*intervalY),
				bounds.x+bounds.width+GAP,
				(int) Math.ceil(bounds.y+i*intervalY)
			);
			
			// these are the little indicators on the sides of the y axis
			gd.setColor(AXIS_COLOR);
			gd.drawLine(
					bounds.x-GAP/2,
					(int) Math.ceil(bounds.y+i*intervalY),
					bounds.x,
					(int) Math.ceil(bounds.y+i*intervalY)
					);
		}
		
		
		double intervalX = bounds.width / (double) xValues.size();
		for (int i = 0, n = xValues.size(); i <= n; i++) {
			gd.setColor(MESH_COLOR);
			gd.drawLine(
				(int) (bounds.x+i*intervalX),
				bounds.y,
				(int) (bounds.x+i*intervalX),
				bounds.y+bounds.height
			);
			
			// these are the little indicators on the lower side of the x axis
			gd.setColor(AXIS_COLOR);
			gd.drawLine(
				(int) (bounds.x+i*intervalX),
				bounds.y+bounds.height,
				(int) (bounds.x+i*intervalX),
				bounds.y+bounds.height+GAP/2
			);
		}	
		
		gd.setColor(oldColor);
		gd.setStroke(oldStroke);
	}

	/**
	 * Helper method for drawing the bar chart's x axis.
	 * 
	 * @param gd component's graphics object
	 * @param x1 starting x coordinate
	 * @param y1 starting y coordinate
	 * @param x2 ending x coordinate
	 * @param y2 ending y coordinate
	 */
	private void drawXAxis(Graphics2D gd, int x1, int y1, int x2, int y2) {
		Stroke oldStroke = gd.getStroke();
		gd.setStroke(LINE_STROKE);
		Color oldColor = gd.getColor();
		gd.setColor(AXIS_COLOR);
		
		gd.drawLine(x1, y1, x2-ARROW_LENGTH, y2);
		
		gd.setColor(oldColor);
		gd.setStroke(oldStroke);
	}

	/**
	 * Helper method for drawing the bar chart's x axis arrow.
	 * 
	 * @param gd component's graphics object
	 * @param x x coordinate of centerpoint
	 * @param y y coordinate of centerpoint
	 */
	private void drawXArrow(Graphics2D gd, int x, int y) {
		Color oldColor = gd.getColor();
		gd.setColor(AXIS_COLOR);
		
		gd.fillPolygon(
			new int[] {x-ARROW_LENGTH, x-ARROW_LENGTH, x},
			new int[] {y+ARROW_WIDTH, y-ARROW_WIDTH, y},
			3
		);
		
		gd.setColor(oldColor);
	}

	/**
	 * Helper method for drawing the bar chart's x axis values.
	 * 
	 * @param gd component's graphics object
	 * @param bounds rectangle within the component to work inside
	 * @param xValues list of x values
	 * @param fm FontMetrics object for current font
	 */
	private void drawXValues(Graphics2D gd, Rectangle bounds, List<Integer> xValues, FontMetrics fm) {
		List<XYValue> values = chart.getValues();
		int numberOfNumbers = values.size();
		double interval = bounds.width / (double) numberOfNumbers;
		int fontHeight = fm.getHeight();
		
		for (int i = 0, n = xValues.size(); i < n; i++) {
			int xValue = xValues.get(i);
			int xValueWidth = fm.stringWidth(Integer.toString(xValue));
			gd.drawString(
				Integer.toString(xValue),
				(int) (bounds.x+(i+1)*interval - (interval + xValueWidth)/2),
				bounds.y+fontHeight
			);
		}		
	}

	/**
	 * Helper method for drawing the bar chart's x axis text.
	 * 
	 * @param gd component's graphics object
	 * @param bounds rectangle within the component to work inside
	 * @param fm FontMetrics object for current font
	 */
	private void drawXText(Graphics2D gd, Rectangle bounds, FontMetrics fm) {
		String text = chart.getXText();
		int fontHeight = fm.getHeight();
		
		gd.drawString(
			text,
			bounds.x + (bounds.width-fm.stringWidth(text))/2, 
			bounds.y+fontHeight+GAP*2
		);

	}

	/**
	 * Helper method for drawing the bar chart's x axis.
	 * 
	 * @param gd component's graphics object
	 * @param x1 starting x coordinate
	 * @param y1 starting y coordinate
	 * @param x2 ending x coordinate
	 * @param y2 ending y coordinate
	 */
	private void drawYAxis(Graphics2D gd, int x1, int y1, int x2, int y2) {
		Stroke s = gd.getStroke();
		gd.setStroke(LINE_STROKE);
		Color oldColor = gd.getColor();
		gd.setColor(AXIS_COLOR);
		
		gd.drawLine(x1, y1+ARROW_LENGTH, x2, y2);
		
		gd.setStroke(s);
		gd.setColor(oldColor);
	}

	/**
	 * Helper method for drawing the bar chart's y axis arrow.
	 * 
	 * @param gd component's graphics object
	 * @param x x coordinate of centerpoint
	 * @param y y coordinate of centerpoint
	 */
	private void drawYArrow(Graphics2D gd, int x, int y) {
		Color oldColor = gd.getColor();
		gd.setColor(AXIS_COLOR);
		
		gd.fillPolygon(
			new int[] {x, x-ARROW_WIDTH, x+ARROW_WIDTH},
			new int[] {y, y+ARROW_LENGTH, y+ARROW_LENGTH},
			3
		);
		
		gd.setColor(oldColor);
	}

	/**
	 * Helper method for drawing the bar chart's y axis values.
	 * 
	 * @param gd component's graphics object
	 * @param bounds rectangle within the component to work inside
	 * @param yValues list of yValues
	 * @param fm FontMetrics object for current font
	 */
	private void drawYValues(Graphics2D gd, Rectangle bounds, List<Integer> yValues, FontMetrics fm) {
		int fontHeight = fm.getHeight();
		final char SINGLE_DIGIT_NUMBER = '0';
		
		double interval = bounds.height / (double) yValues.size();
		for (int i = 0, n = yValues.size(); i < n; i++) {
			int smallestValueSize = Integer.toString(yValues.get(i)).length();
			int largestValueSize = Integer.toString(chart.getYMax()).length();
			// this is for right-alignment
			int addedPadding = fm.charWidth(SINGLE_DIGIT_NUMBER) * (largestValueSize - smallestValueSize);
			
			gd.drawString(
				Integer.toString(yValues.get(i)),
				bounds.x+fontHeight+GAP+addedPadding,
				(int) (bounds.y+(i+1)*interval + fm.getDescent())
			);
		}
		
	}

	/**
		 * Helper method for drawing the bar chart's y axis text.
		 * 
		 * @param gd component's graphics object
		 * @param bounds rectangle within the component to work inside
		 * @param fm FontMetrics object for current font
		 */
		private void drawYText(Graphics2D gd, Rectangle bounds, FontMetrics fm) {
			AffineTransform saveAT = gd.getTransform();
			
			AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
			gd.setTransform(at);
			
			int textWidth = fm.stringWidth(chart.getYText());
			gd.drawString(
				chart.getYText(),
				-bounds.y-(bounds.height+textWidth)/2 - textWidth/2,
				bounds.x+fm.getHeight()
			);
			
			gd.setTransform(saveAT);		
		}

}
