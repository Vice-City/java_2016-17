package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.model.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.model.IColorProvider;

/**
 * A component for displaying the currently chosen color and making
 * it possible to choose a new color by clicking on the component's area.
 * 
 * @author Vice Ivušić
 *
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Currently selected color.
	 */
	private Color selectedColor;
	
	/**
	 * List of registered ColorChangeListener objects.
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();

	/**
	 * Creates a new JColorArea with the specified color. Expects the
	 * user to pass on a non-null value.
	 * 
	 * @param selectedColor desired initial color value
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = selectedColor;
		
		setSize(getPreferredSize());
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Color color = JColorChooser.showDialog(JColorArea.this, "Choose new color", Color.WHITE);
				
				if (color == null) {
					return;
				}
				
				Color oldColor = JColorArea.this.selectedColor;
				JColorArea.this.selectedColor = color;
				
				repaint();
				
				for (ColorChangeListener l : listeners) {
					l.newColorSelected(JColorArea.this, oldColor, color);
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(selectedColor);
		
		g2d.fillRect(0, 0, getWidth()-1, getHeight()-1);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Color getCurrentColor() {
		// Color is immutable so this is okay
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		if (l == null || listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		if (l == null || !listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}
	
	
	
}
