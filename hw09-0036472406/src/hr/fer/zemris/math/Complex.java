package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a complex number, with its real and imaginary components.
 * Offers various methods for algebra in the complex domain, such as
 * calculating a complex number's module; adding, subtracting, multiplying
 * and diving complex numbers; calculating the n-th power and n-th roots
 * of a complex number; and a method for parsing a complex number.
 * 
 * <p>Methods which result in new complex numbers return new objects instead
 * of modifying existing ones, so multiple methods may be chained one after 
 * the other.
 * 
 * <p>This class also offers a number of static final complex number objects
 * which are commonly used, such as 0, 1, -1, i and -i.
 * 
 * @author Vice Ivušić
 *
 */
public class Complex {

	/** real component of this complex number */
	private double re;
	/** imaginary component of this complex number */
	private double im;
	
	/** complex number representing 0 */
	public static final Complex ZERO = new Complex(0, 0);
	/** complex number representing 1 */
	public static final Complex ONE = new Complex(1, 0);
	/** complex number representing -1 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/** complex number representing i */
	public static final Complex IM = new Complex(0, 1);
	/** complex number representing -i */
	public static final Complex IM_NEG = new Complex(0, -1);
	
	/**
	 * Creates a new Complex number with its components set to 0,
	 * i.e. the number zero.
	 */
	public Complex() {
		super();
	}
	
	/**
	 * Creates a new Complex number with the specified real and
	 * imaginary components.
	 * 
	 * @param re real component of a complex number
	 * @param im imaginary component of a complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * Returns current complex number's real component.
	 * 
	 * @return current complex number's real component
	 */
	public double getRe() {
		return re;
	}
	
	/**
	 * Returns current complex number's imaginary component.
	 * 
	 * @return current complex number's imaginary component
	 */
	public double getIm() {
		return im;
	}
	
	/**
	 * Returns current complex number's module.
	 * 
	 * @return current complex number's module.
	 */
	public double module() {
		return Math.sqrt(re*re + im*im);
	}
	
	/**
	 * Returns a new complex number made by adding the current and
	 * specified complex number.
	 * 
	 * @param c complex number to add
	 * @return complex number made by adding current and specified complex number
	 * @throws IllegalArgumentException if the specified complex number is null
	 */
	public Complex add(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Argument c cannot be null!");
		}
		
		return new Complex(
			re + c.re,
			im + c.im
		);
	}

	/**
	 * Returns a new complex number made by subtracting the specified complex
	 * number from the current complex number.
	 * 
	 * @param c complex number to subtract with
	 * @return complex number made by subtracting current and specified complex number
	 * @throws IllegalArgumentException if the specified complex number is null
	 */
	public Complex sub(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Argument c cannot be null!");
		}
		
		return new Complex(
			re - c.re,
			im - c.im
		);
	}

	/**
	 * Returns a new complex number made by multiplying the current and
	 * specified complex number.
	 * 
	 * @param c complex number to multiply with
	 * @return complex number made by multiplying current and specified complex number
	 * @throws IllegalArgumentException if the specified complex number is null
	 */
	public Complex multiply(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Argument c cannot be null!");
		}
		
		return new Complex(
			re*c.re - im*c.im,
			im*c.re + re*c.im
		);
	}
	
	/**
	 * Returns a new complex number made by adding the current and
	 * specified complex number. Note that the resulting complex
	 * number may contain Double.INFINITY or Double.NaN values if 
	 * the denominator is zero.
	 * 
	 * @param c complex number to divide with
	 * @return complex number made by dividing current and specified complex number
	 * @throws IllegalArgumentException if the specified complex number is null
	 */
	public Complex divide(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Argument c cannot be null!");
		}
		
		double denominator = c.re*c.re + c.im*c.im;
		
		return new Complex(
			(re*c.re + im*c.im) / denominator,
			(im*c.re - re*c.im) / denominator
		);
	}
	
	/**
	 * Returns a new complex number made by negating the current
	 * complex number.
	 * 
	 * @return complex number made by negating the current complex number
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}
	
	/**
	 * Returns a new complex number made by calculating the n-th
	 * power of the current complex number.
	 * 
	 * @param n power to calculate for
	 * @return complex number made by calculating the n-th power of the current number
	 * @throws IllegalArgumentException if n is lesser than 0
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Argument n has to be at least 0!");
		}
		
		double modN = Math.pow(module(), n);
		double angleN = n * angle();
		
		return new Complex(
			modN * Math.cos(angleN),
			modN * Math.sin(angleN)
		);
	}
	
	/**
	 * Returns a list of all the n-th roots of the current complex number.
	 * 
	 * @param n power of the roots being calculated
	 * @return list of all the n-th roots of the current complex number
	 * @throws IllegalArgumentException if n is less than 1
	 */
	public List<Complex> root(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Argument n has to be at least 1!");
		}
		
		double modN = Math.pow(module(), 1.0/n);
		double angle = angle();
		
		List<Complex> roots = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			Complex root = new Complex(
				modN * Math.cos((angle + 2*Math.PI*i) / n),
				modN * Math.sin((angle + 2*Math.PI*i) / n)
			);
			
			roots.add(root);
		}
		
		return roots;		
	}
	
	/**
	 * Helper method for calculating the angle of the current complex number.
	 * 
	 * @return angle of the current complex number in radians
	 */
	private double angle() {
		return Math.atan2(im, re);
	}
	
	@Override
	public String toString() {
		return String.format("(%f, %fi)", re, im);
	}


	/**
	 * Returns a new Complex number parsed from the input string.
	 * General syntax of the input strings is {@code a+ib} or
	 * {@code a-ib}. Either a or b may be dropped, but not both, 
	 * except when i is given by itself, in which case b is 1.
	 * The imaginary component must always come after the real 
	 * component if both are present.
	 * 
	 * @param input input to parse
	 * @return parsed Complex number
	 * @throws NumberFormatException if the input string is in incorrect format
	 */
	public static Complex parse(String input) {
		String regexBothComponents = 
			"\\s*([+|-]?\\s*[0-9]+\\.?[0-9]*)\\s*([+|-]?\\s*i[0-9]*\\.?[0-9]*)\\s*"
		;
		
		Pattern pattern = Pattern.compile(regexBothComponents);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			String reToken = matcher.group(1).replaceAll("\\s", "");
			String imToken = matcher.group(2).replaceAll("[\\s|i]", "");
			
			if (imToken.equals("+") || imToken.equals("-") || imToken.equals("")) {
				imToken += "1";
			}
			
			return new Complex(
				Double.parseDouble(reToken),
				Double.parseDouble(imToken)
			);
		}
		
		String regexSingleComponent = "\\s*([+|-]?\\s*(i)?[0-9]*\\.?[0-9]*)\\s*";
		pattern = Pattern.compile(regexSingleComponent);
		matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			boolean isImaginary = matcher.group(2) != null;
			String valueToken = matcher.group(1).replaceAll("[\\s|i]", "");
			
			double re, im;
			
			if (isImaginary) {
				re = 0;
				
				if (valueToken.equals("") || valueToken.equals("+") || valueToken.equals("-")) {
					valueToken += "1";
				}
				im = Double.parseDouble(valueToken);
				
			} else {
				im = 0;
				
				if (valueToken.equals("")) {
					throw new NumberFormatException("Cannot parse empty string!");
				}
				re = Double.parseDouble(valueToken);
			}
			
			return new Complex(re, im);
		}
		
		throw new NumberFormatException("Illegal input!");
	}
}
