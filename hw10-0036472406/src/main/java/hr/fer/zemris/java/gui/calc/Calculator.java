package hr.fer.zemris.java.gui.calc;

import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.buttons.BinaryButton;
import hr.fer.zemris.java.gui.calc.buttons.BinaryInverseButton;
import hr.fer.zemris.java.gui.calc.buttons.GenericButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertButton;
import hr.fer.zemris.java.gui.calc.buttons.NumberButton;
import hr.fer.zemris.java.gui.calc.buttons.UnaryButton;
import hr.fer.zemris.java.gui.calc.buttons.UnaryInverseButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * A calculator with an accompanying GUI. This calculator tries
 * to emulate the functionality of Windows 10's stock calculator
 * application as much as possible.
 * 
 * <p>Offers all basic arithmetic functionality, including adding,
 * subtracting, multiplying, dividing, negating, finding reciprocal,
 * finding base 10 logarithm, finding natural logaritm, fidning 
 * exponential, finding sine, finding cosine, finding tangent and 
 * finding cotangent of numbers. The latter half of functionality 
 * can also be inverted by checking the Inv checkbox.
 * 
 * <p>Also offers clearing and resetting functionality. The clear
 * button only clears the current number in memory; it does not
 * clear the last specified operation. Because of that, it is also
 * possible to consecutively press the equals button to have the
 * calculator do the last operation using the last operand and result.
 * 
 * <p>This calculator offers pushing and popping functionality as well.
 * Every time the push button is pressed, the currently stored number
 * is stored and can be retrieved at a later time by pressing pop.
 * 
 * <p>Trigonometric functions calculate in radians.
 * 
 * @author Vice Ivušić
 *
 */
public class Calculator extends JFrame {

		
	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new Calculator.
	 * 
	 */
	private Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(20, 20, 500, 400);
		
		
		initGUI();
	}
	
	/**
	 * Initializes the calculator's graphical user interface.
	 * 
	 */
	private void initGUI() {
		Container cp = getContentPane();
		
		JPanel marginPanel = new JPanel();
		marginPanel.setLayout(new CalcLayout(5));
		marginPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		cp.add(marginPanel);
		
		CalculatorModel model = new CalculatorModel();
		Display display = new Display();
		model.setDisplay(display);
		
		marginPanel.add(display, new RCPosition(1, 1));
		
		marginPanel.add(new NumberButton(model, 0), new RCPosition(5, 3));
		marginPanel.add(new NumberButton(model, 1), new RCPosition(4, 3));
		marginPanel.add(new NumberButton(model, 2), new RCPosition(4, 4));
		marginPanel.add(new NumberButton(model, 3), new RCPosition(4, 5));
		marginPanel.add(new NumberButton(model, 4), new RCPosition(3, 3));
		marginPanel.add(new NumberButton(model, 5), new RCPosition(3, 4));
		marginPanel.add(new NumberButton(model, 6), new RCPosition(3, 5));
		marginPanel.add(new NumberButton(model, 7), new RCPosition(2, 3));
		marginPanel.add(new NumberButton(model, 8), new RCPosition(2, 4));
		marginPanel.add(new NumberButton(model, 9), new RCPosition(2, 5));
		
		marginPanel.add(new GenericButton(m -> m.negate(), "+/-", model), new RCPosition(5, 4));
		marginPanel.add(new GenericButton(m -> m.setDecimal(), ".", model), new RCPosition(5, 5));
		
		marginPanel.add(new BinaryButton((i, j) -> i/j, "/", model), new RCPosition(2, 6));
		marginPanel.add(new BinaryButton((i, j) -> i*j, "*", model), new RCPosition(3, 6));
		marginPanel.add(new BinaryButton((i, j) -> i-j, "-", model), new RCPosition(4, 6));
		marginPanel.add(new BinaryButton((i, j) -> i+j, "+", model), new RCPosition(5, 6));
		
		marginPanel.add(new UnaryButton(i -> 1.0/i, "1/x", model), new RCPosition(2, 1));
		marginPanel.add(
			new UnaryInverseButton(Math::log10, i -> Math.pow(10, i), "log", model), 
			new RCPosition(3, 1)
		);
		marginPanel.add(
			new UnaryInverseButton(Math::log, Math::exp, "ln", model), 
			new RCPosition(4, 1)
		);
		marginPanel.add(
			new BinaryInverseButton((i,j) -> Math.pow(i, j), (i, j) -> Math.pow(i, 1/j), "x^n", model), 
			new RCPosition(5, 1)
		);
		
		marginPanel.add(
			new UnaryInverseButton(Math::sin, Math::asin, "sin", model), 
			new RCPosition(2, 2)
		);
		marginPanel.add(
			new UnaryInverseButton(Math::cos, Math::acos, "cos", model), 
			new RCPosition(3, 2)
		);
		marginPanel.add(
			new UnaryInverseButton(Math::tan, Math::atan, "tan", model), 
			new RCPosition(4, 2)
		);
		marginPanel.add(
			new UnaryInverseButton(i -> 1/Math.tan(i), i -> Math.atan(1.0/i), "ctg", model), 
			new RCPosition(5, 2)
		);
		
		marginPanel.add(new GenericButton(m -> m.applyEqual(), "=", model), new RCPosition(1, 6));
		marginPanel.add(new GenericButton(m -> m.clear(), "clr", model), new RCPosition(1, 7));
		marginPanel.add(new GenericButton(m -> m.reset(), "res", model), new RCPosition(2, 7));
		marginPanel.add(new GenericButton(m -> m.push(), "push", model), new RCPosition(3, 7));
		marginPanel.add(new GenericButton(m -> m.pop(), "pop", model), new RCPosition(4, 7));
		marginPanel.add(new InvertButton(model), new RCPosition(5, 7));
	}
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input strings; not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}
