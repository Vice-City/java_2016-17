package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JCheckBox configured meant to interact with a CalculatorModel model.
 * 
 * @author Vice Ivušić
 *
 */
public class InvertButton extends JCheckBox {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new InvertButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param model CalculatorModel the button should work with
	 */
	public InvertButton(CalculatorModel model) {
		setBackground(new Color(195, 195, 229));
		setBorder(BorderFactory.createLineBorder(new Color(65, 50, 102)));
		setBorderPainted(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		
		setFont(new Font(getFont().getFontName(), Font.BOLD, (int) ((int) getFont().getSize()*1.2)));
		setText("Inv");
		
		addActionListener(e -> {
			model.invert();
		});
	}
}
