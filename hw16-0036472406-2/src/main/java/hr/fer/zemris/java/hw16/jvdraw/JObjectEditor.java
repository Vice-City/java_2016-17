package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw16.jvdraw.model.ICanvas;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectInfoExtractor;

/**
 * A component used to edit existing geometrical objects.
 * 
 * @author Vice Ivušić
 *
 */
public class JObjectEditor extends JPanel implements ObjectInfoExtractor {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An objecting representing a canvas with a width and height.
	 */
	private ICanvas canvas;
	
	/**
	 * Creates a new JObjectEditor using the specified ICanvas object to 
	 * receive relevant parameters.
	 * 
	 * @param canvas an ICanvas object
	 */
	public JObjectEditor(ICanvas canvas) {
		this.canvas = canvas;
		setLayout(new GridLayout(0, 1));
	}
	
	@Override
	public void extract(Line line) {
		JSlider x1 = addSlider("x1", 0, canvas.getWidth(), line.getP1().x);
		JSlider y1 = addSlider("y1", 0, canvas.getWidth(), line.getP1().y);
		JSlider x2 = addSlider("x2", 0, canvas.getWidth(), line.getP2().x);
		JSlider y2 = addSlider("y2", 0, canvas.getWidth(), line.getP2().y);
		JColorArea colorArea = addColorChooser("color", line.getColor());
		
		JButton applyButton = new JButton("Apply changes");
		add(applyButton);
		
		applyButton.addActionListener(e -> {
			line.setParameters(
				x1.getValue(), 
				y1.getValue(),
				x2.getValue(),
				y2.getValue(),
				colorArea.getCurrentColor()
			);
		});
	}

	@Override
	public void extract(Circle circle) {
		JSlider x = addSlider("x", 0, canvas.getWidth(), circle.getCenter().x);
		JSlider y = addSlider("y", 0, canvas.getWidth(), circle.getCenter().y);
		JSlider radius = addSlider("r", 0, canvas.getWidth()/2, (int)circle.getRadius());
		JColorArea colorArea = addColorChooser("color", circle.getColor());
		
		JButton applyButton = new JButton("Apply changes");
		applyButton.addActionListener(e -> {
			circle.setParameters(
				x.getValue(), 
				y.getValue(),
				radius.getValue(),
				colorArea.getCurrentColor()
			);
		});
		add(applyButton);
	}

	@Override
	public void extract(FilledCircle filledCircle) {
		JSlider x = addSlider("x", 0, canvas.getWidth(), filledCircle.getCenter().x);
		JSlider y = addSlider("y", 0, canvas.getWidth(), filledCircle.getCenter().y);
		JSlider radius = addSlider("r", 0, canvas.getWidth()/2, (int)filledCircle.getRadius());
		JColorArea outlineColorArea = addColorChooser("Outline color", filledCircle.getColor());
		JColorArea fillColorArea = addColorChooser("Fill color", filledCircle.getFillColor());
		
		JButton applyButton = new JButton("Apply changes");
		applyButton.addActionListener(e -> {
			filledCircle.setParameters(
				x.getValue(), 
				y.getValue(),
				radius.getValue(),
				outlineColorArea.getCurrentColor(),
				fillColorArea.getCurrentColor()
			);
		});
		add(applyButton);
	}

	/**
	 * Helper method for creating a JSlider with the specified parameters.
	 * 
	 * @param name name of the slider's value
	 * @param start starting value
	 * @param end ending value
	 * @param initialValue initial value
	 * @return reference to the created slider
	 */
	private JSlider addSlider(String name, int start, int end, int initialValue) {
		JPanel panel = new JPanel();
		add(panel);
		
		JLabel label = new JLabel(name);
		panel.add(label);
		
		JSlider slider = new JSlider(start, end, initialValue);
		panel.add(slider);
		
		slider.setMajorTickSpacing(end/5);
		slider.setPaintTicks(true);
		slider.setLabelTable(slider.createStandardLabels(end/5));
		slider.setPaintLabels(true);
		
		JLabel selectedValue = new JLabel(Integer.toString(slider.getValue()));
		panel.add(selectedValue);
		
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				selectedValue.setText(Integer.toString(slider.getValue()));
			}
		});
		
		return slider;
	}

	/**
	 * Helper method for creating a JColorArea from the specified parameters.
	 * 
	 * @param name name of the color value being edited
	 * @param color initial color value
	 * @return reference to the created JColorArea
	 */
	private JColorArea addColorChooser(String name, Color color) {
		JPanel panel = new JPanel();
		add(panel);
	
		JLabel label = new JLabel(name);
		panel.add(label);
	
		JColorArea colorArea = new JColorArea(color);
		panel.add(colorArea);
		
		return colorArea;
	}
	
}
