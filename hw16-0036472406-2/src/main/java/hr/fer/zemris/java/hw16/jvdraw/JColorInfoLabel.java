package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw16.jvdraw.model.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.model.IColorProvider;

/**
 * A label component displaying the currently selected colors of the
 * configured components.
 * 
 * @author Vice Ivušić
 *
 */
public class JColorInfoLabel extends JLabel implements ColorChangeListener {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Provider of the foreground color component.
	 */
	private IColorProvider fgColorProvider;
	
	/**
	 * Provider of the background color component.
	 */
	private IColorProvider bgColorProvider;
	
	/**
	 * Creates a new JColorInfoLabel monitoring the specified color provider objects.
	 * 
	 * @param fgColorProvider provider of the foreground color component
	 * @param bgColorProvider provider of the background color component
	 */
	public JColorInfoLabel(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;
		
		
		bgColorProvider.addColorChangeListener(this);
		fgColorProvider.addColorChangeListener(this);

		updateLabelText();
	}
	
	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		updateLabelText();
	}
	
	/**
	 * Helper method for updating the label's content when a change occurs.
	 */
	private void updateLabelText() {
		setText(String.format(
			"Foreground color: (%d, %d, %d), background color: (%d, %d, %d).",
			fgColorProvider.getCurrentColor().getRed(),
			fgColorProvider.getCurrentColor().getGreen(),
			fgColorProvider.getCurrentColor().getBlue(),
			bgColorProvider.getCurrentColor().getRed(),
			bgColorProvider.getCurrentColor().getGreen(),
			bgColorProvider.getCurrentColor().getBlue()
		));
	}

}
