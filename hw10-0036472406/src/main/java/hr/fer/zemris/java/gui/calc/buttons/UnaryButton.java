package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.UnaryOperator;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JButton configured with a UnaryOperator strategy,
 * meant to interact with a CalculatorModel.
 * 
 * @author Vice Ivušić
 *
 */
public class UnaryButton extends CoolButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new UnaryButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param oper UnaryOperator strategy
	 * @param symbol string to display
	 * @param model CalculatorModel the specified strategy should work with
	 */	
	public UnaryButton(UnaryOperator<Double> oper, String symbol, CalculatorModel model) {
		setText(symbol);
		
		addActionListener(e -> {
			model.calcUnary(oper);
		});
	}
}
