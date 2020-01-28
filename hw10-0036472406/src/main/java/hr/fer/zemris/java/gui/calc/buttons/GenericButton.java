package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.Consumer;

import hr.fer.zemris.java.gui.calc.CalculatorModel;

/**
 * Models a JButton configured with a Consumer strategy,
 * meant to interact with a CalculatorModel.
 * 
 * @author Vice Ivušić
 *
 */
public class GenericButton extends CoolButton {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new GenericButton with the specified parameters. Expects
	 * the user will pass on valid (not-null) values and gives no warnings otherwise.
	 * 
	 * @param consumer Consumer strategy for this button
	 * @param symbol string to display
	 * @param model CalculatorModel the specified strategy should work with
	 */
	public GenericButton(Consumer<CalculatorModel> consumer, String symbol, CalculatorModel model) {
		setText(symbol);
		
		addActionListener(e -> {
			consumer.accept(model);
		});

	}
	
}
