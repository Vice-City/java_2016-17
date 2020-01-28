package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.BinaryOperator;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JButton configured with two BinaryOperator strategies,
 * meant to interact with a CalculatorModel.
 * 
 * @author Vice Ivušić
 *
 */
public class BinaryInverseButton extends CoolButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new BinaryInverseButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param oper BinaryOperator strategy
	 * @param operInv inverted BinaryOperator strategy
	 * @param symbol string to display
	 * @param model CalculatorModel the specified strategies should work with
	 */
	public BinaryInverseButton(
			BinaryOperator<Double> oper, BinaryOperator<Double> operInv, 
			String symbol, CalculatorModel model) {
		setText(symbol);
		
		addActionListener(e -> {
			if (model.isInverted()) {
				model.calcBinary(operInv);
			} else {
				model.calcBinary(oper);
			}
		});
	}
}
