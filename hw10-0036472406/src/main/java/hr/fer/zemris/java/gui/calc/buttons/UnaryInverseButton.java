package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.UnaryOperator;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JButton configured with two UnaryOperator strategies,
 * meant to interact with a CalculatorModel.
 * 
 * @author Vice Ivušić
 *
 */
public class UnaryInverseButton extends CoolButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new UnaryInverseButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param oper UnaryOperator strategy
	 * @param operInv inverted UnaryOperator strategy
	 * @param symbol string to display
	 * @param model CalculatorModel the specified strategies should work with
	 */
	public UnaryInverseButton(
			UnaryOperator<Double> oper, UnaryOperator<Double> operInv, 
			String symbol, CalculatorModel model) {
		setText(symbol);
		
		addActionListener(e -> {
			if (model.isInverted()) {
				model.calcUnary(operInv);
			} else {
				model.calcUnary(oper);
			}
		});
	}
}
