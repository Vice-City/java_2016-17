package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A GUI program for representing data as bar charts inside a coordinate system.
 * Data is loaded from a specified file, with the following format:
 * 
 * <pre>
 * X-Axis text
 * Y-Axis text
 * comma-separated pair values separated by spaces
 * minimum y value
 * maximum y value
 * y value step
 * 
 * </pre>
 * 
 * <p>Any lines after the first 6 are ignored. The comma separated pair values
 * should look something like this: <code>1,8 2,20 3,22 4,10 5,4</code>.
 * 
 * <p>The program takes one argument: path to file with data definition.
 * Examples of valid arguments:
 * <pre>
 * example.graph
 * wild.graph
 * </pre>
 * 
 * @author Vice Ivušić
 *
 */
public class BarChartDemo extends JFrame {
	
	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/** bar chart being displayed */
	private BarChart chart;
	/** path to bar chart's definition file */
	private Path path;

	/**
	 * Creates a new graphical representation of the input bar chart.
	 * 
	 * @param chart bar chart to display
	 * @param path path to bar chart's definition file
	 */
	public BarChartDemo(BarChart chart, Path path) {
		this.chart = chart;
		this.path = path;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(10, 10, 600, 500);
		
		initGUI();
	}
	
	/**
	 * Initializes the graphical elements of the program.
	 * 
	 */
	private void initGUI() {
		Container cp = getContentPane();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		cp.add(panel);
		
		JPanel pathPanel = new JPanel();
		pathPanel.setBackground(Color.WHITE);
		pathPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		pathPanel.add(new PathLabel(path));
		
		panel.add(pathPanel, BorderLayout.PAGE_START);
		panel.add(new BarChartComponent(chart), BorderLayout.CENTER);
	}

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Must input path to graph definition file!");
			return;
		}
		
		Path path;
		try {
			path = Paths.get(args[0]);
		} catch (InvalidPathException ex) {
			System.out.println("Given path is invalid!");
			return;
		}
		
		if (Files.isDirectory(path)) {
			System.out.println("Specified path is a directory! Must be a file!");
			return;
		}
		
		List<String> lines;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException ex) {
			System.out.println("Couldn't read specified file!");
			return;
		}
		
		BarChart chart;
		try {
			chart = parseChart(lines);
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(chart, path).setVisible(true);
		});
	}

	/**
	 * Helper method for parsing the bar chart definition file.
	 * 
	 * @param lines list of strings containing the definition file's lines.
	 * @return parsed BarChart object
	 * @throws IllegalArgumentException if the definition file is improperly formatted
	 */
	private static BarChart parseChart(List<String> lines) {
		if (lines.size() < 6) {
			throw new IllegalArgumentException(
				"Specified file must have at least 6 lines!"
			);
		}
		
		String xText = lines.get(0).trim();
		String yText = lines.get(1).trim();
		
		List<XYValue> values = parseValues(lines.get(2).trim());
		
		int yMin = Integer.parseInt(lines.get(3).trim());
		int yMax = Integer.parseInt(lines.get(4).trim());
		int yStep = Integer.parseInt(lines.get(5).trim());
		
		return new BarChart(values, xText, yText, yMin, yMax, yStep);
	}

	/**
	 * Helper method for parsing the XYValues listed in the definition file.
	 * 
	 * @param input line containing the XYValues
	 * @return list of parsed XYValues
	 * @throws IllegalArgumentException if the values are incorrectly formatted
	 */
	private static List<XYValue> parseValues(String input) {
		Pattern p = Pattern.compile("\\s*(-?[0-9]+),(-?[0-9]+)\\s*");
		Matcher m = p.matcher(input);
		
		List<XYValue> values = new ArrayList<>();
		while (!m.hitEnd()) {
			if (!m.find()) {
				throw new IllegalArgumentException("Incorrectly specified values!");
			}
			
			int value1 = Integer.parseInt(m.group(1));
			int value2 = Integer.parseInt(m.group(2));
			
			XYValue value = new XYValue(value1, value2);
			
			if (values.contains(value)) {
				throw new IllegalArgumentException("Cannot have multiple same x values!");
			}
			
			values.add(value);
		}
		
		return values;
	}
	
}
