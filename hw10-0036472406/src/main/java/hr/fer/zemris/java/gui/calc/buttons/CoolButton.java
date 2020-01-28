package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Models a JButton with cool and hip coloring and borders.
 * 
 * @author Vice Ivušić
 *
 */
public class CoolButton extends JButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new CoolButton.
	 * 
	 */
	public CoolButton() {
		setBackground(new Color(195, 195, 229));
		setBorder(BorderFactory.createLineBorder(new Color(65, 50, 102)));
		setFont(new Font(getFont().getFontName(), Font.BOLD, (int) ((int) getFont().getSize()*1.2)));
	}
	
}
