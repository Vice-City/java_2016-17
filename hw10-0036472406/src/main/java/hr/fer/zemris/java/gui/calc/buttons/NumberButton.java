package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JButton configured with a number,
 * meant to interact with a CalculatorModel.
 * 
 * @author Vice Ivušić
 *
 */
public class NumberButton extends CoolButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new NumberButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param model CalculatorModel the specified strategy should work with
	 * @param number the number to be used both for the CalculatorModel and as display
	 */
	public NumberButton(CalculatorModel model, int number) {
		setText(Integer.toString(number));
		
		addActionListener(e -> {
			model.addToCurrentNumber(number);
		});

	}
	
}
