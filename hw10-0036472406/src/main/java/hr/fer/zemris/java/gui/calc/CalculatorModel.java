package hr.fer.zemris.java.gui.calc;

import java.util.Stack;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import javax.swing.JLabel;

/**
 * Models several arithmetic functions and can be used for building
 * a calculator. Offers methods through which other components can
 * configure the desired calculations with unary and binary operators,
 * as well as several non-configurable methods for working with the model.
 * 
 * <p>The results of this calculator's operations can only be retrieved
 * indirectly through a JLabel component. The component has to be registered 
 * through the model's setDisplay method.
 * 
 * @author Vice Ivušić
 *
 */
public class CalculatorModel {

	/** value currently in memory for use in both unary and binary operations */
	private double currentNumber;
	/** value that was last calculated, for use in binary operations */
	private double result;
	/** indicates whether there is at least some new input from an outside source */
	private boolean inputIsEmpty = true;
	
	/** label used for displaying the results of calculations */
	private JLabel display;
	/** string being currently displayed; always corresponds to the current number */
	private String currentInput = "";
	
	/** last binary operation which was used */
	private BinaryOperator<Double> lastBinaryOperation;
	/** indicates whether inverted operations should be used for supported operators */
	private boolean operationsAreInverted;
	
	/** used for temporary value storage */
	private Stack<Double> stack = new Stack<>();
	
	/**
	 * Sets this calculator's display to the specified JLabel component. Attempting
	 * any calculation without setting this component will result in values being
	 * calculated, but not being able to be seen anywhere.
	 * 
	 * @param display JLabel to be used as display
	 */
	public void setDisplay(JLabel display) {
		if (display == null) {
			throw new IllegalArgumentException("Argument display cannot be null!");
		}
		
		this.display = display;
	}
	
	/**
	 * Returns true if the calculator is in inverted mode.
	 * 
	 * @return true if calculator is in inverted mode
	 */
	public boolean isInverted() {
		return operationsAreInverted;
	}
	
	/**
	 * Inverts the calculator's mode. If non-inverted functions
	 * were being used, inverted functions will be used and vice versa.
	 * 
	 */
	public void invert() {
		operationsAreInverted ^= true;
	}
	
	/**
	 * Applies the specified strategy's binary operation onto the
	 * currently stored number and the last result.
	 * 
	 * @param binaryOper binary operation strategy
	 */
	public void calcBinary(BinaryOperator<Double> binaryOper) {
		if (lastBinaryOperation != null) {
			if (inputIsEmpty) {
				currentNumber = result;
				lastBinaryOperation = binaryOper;
				return;
			}
			
			result = lastBinaryOperation.apply(result, currentNumber);
			currentNumber = result;
			inputIsEmpty = true;
			
			lastBinaryOperation = binaryOper;
			updateResult(result);
			return;
		}
		
		lastBinaryOperation = binaryOper;
		result = currentNumber;
		currentNumber = result;
		inputIsEmpty = true;
		updateResult(result);
	}
	
	/**
	 * Applies the specified strategy's unary operation onto the
	 * currently stored number.
	 * 
	 * @param unaryOper unary operation strategy
	 */
	public void calcUnary(UnaryOperator<Double> unaryOper) {
		currentNumber = unaryOper.apply(currentNumber);
		result = currentNumber;
		
		inputIsEmpty = true;
		updateResult(result);
	}
	
	/**
	 * Negates the currently stored number.
	 */
	public void negate() {
		if (!inputIsEmpty) {
			currentNumber = -currentNumber;
			
			if (currentInput.charAt(0) == '-') {
				currentInput = currentInput.substring(1, currentInput.length());
			} else {
				currentInput = "-".concat(currentInput);
			}
			
			updateDisplay(currentInput);
			return;
		}
		
		result = -result;
		currentNumber = result;
		updateResult(result);
	}
	
	/**
	 * Adds the specified number to the currently stored number
	 * and updates the display.
	 * 
	 * @param n number to add
	 */
	public void addToCurrentNumber(int n) {
		if (inputIsEmpty) {
			inputIsEmpty = false;
			currentInput = "";
		}
		
		currentInput += n;
		currentNumber = Double.parseDouble(currentInput);
		
		updateDisplay(currentInput);
	}
	
	/**
	 * Adds the decimal dot to the currently stored number if it
	 * doesn't already have one.
	 */
	public void setDecimal() {
		if (inputIsEmpty) {
			inputIsEmpty = false;
			currentInput = "0";
		}
		
		if (currentInput.contains(".")) {
			return;
		}
		
		currentInput += ".";
		
		updateDisplay(currentInput);
	}

	/**
	 * Calculates the result of the operation between the last result
	 * and the currently stored number with the currently stored binary
	 * operator.
	 * 
	 */
	public void applyEqual() {
		if (lastBinaryOperation == null) {
			return;
		}
		
		result = lastBinaryOperation.apply(result, currentNumber);
		
		inputIsEmpty = true;
		updateResult(result);
	}
	
	/**
	 * Clears the currently stored number.
	 * 
	 */
	public void clear() {
		currentNumber = 0;
		inputIsEmpty = true;
		updateResult(currentNumber);
	}
	
	/**
	 * Reset's the calculator's state, save for the inverted flag.
	 * 
	 */
	public void reset() {
		inputIsEmpty = true;
		currentNumber = 0;
		result = 0;
		currentInput = "";
		lastBinaryOperation = null;
		updateResult(currentNumber);
	}
	
	/**
	 * Pushed currently stored value onto the stack.
	 * 
	 */
	public void push() {
		stack.push(currentNumber);
	}
	
	/**
	 * Pops the last stored number from the stack.
	 * 
	 */
	public void pop() {
		if (stack.isEmpty()) {
			updateDisplay("Stack is empty!");
			return;
		}
		
		currentNumber = stack.pop();
		updateResult(currentNumber);
	}
	
	/**
	 * Helper method for updating the display with the specified value.
	 * 
	 * @param output value to update the display with
	 */
	private void updateResult(double output) {
		if (!Double.isFinite(output)) {
			updateDisplay("Result is not defined!");
			inputIsEmpty = false;
			currentNumber = 0;
			lastBinaryOperation = null;
			currentInput = "";
			return;
		}
		
		if (Math.abs(output - Math.floor(output)) < 10e-15) {
			if (Math.abs(output) < 10e-15) {
				output = Math.abs(output);
			}
			
			String res = String.format("%.0f", output);
			updateDisplay(res);
			return;
		} 
		
		String res = Double.toString(output);
		updateDisplay(res);
	}

	/**
	 * Helper method which directly updates the display with the specified string.
	 * 
	 * @param text string to update the display with
	 */
	private void updateDisplay(String text) {
		if (display == null) {
			return;
		}
		
		/*
		 * I am aware that a model directly updating an outside component's
		 * content is bad practice, but considering how tightly coupled the
		 * current components are anyway (CalcLayout, Calculator and CalculatorModel),
		 * I thought it was OK to avoid further complicating things with
		 * listener interfaces, data event models etc.
		 */
		display.setText(text);
	}
}
