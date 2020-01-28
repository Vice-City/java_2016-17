package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 * Models a JLabel for displaying text.
 * 
 * @author Vice Ivušić
 *
 */
public class Display extends JLabel {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new Display.
	 * 
	 */
	public Display() {
		setBackground(new Color(241, 240, 255));
		
		Border prettyBorder = BorderFactory.createLineBorder(new Color(62, 50, 102));
		Border margins = new EmptyBorder(5, 5, 5, 5);
		setBorder(new CompoundBorder(prettyBorder, margins));
		
		setHorizontalAlignment(SwingConstants.RIGHT);
		
		setFont(new Font(getFont().getFontName(), Font.BOLD, getFont().getSize()*2));
		setText("0");
	}
}
