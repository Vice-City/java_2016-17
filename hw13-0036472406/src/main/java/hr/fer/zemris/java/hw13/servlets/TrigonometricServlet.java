package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which calculates trigonometric values (sine and cosine)
 * of a range of values in degrees and outputs a table displaying the
 * results.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="trigonometricServlet", urlPatterns={"/trigonometric"})
public class TrigonometricServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Helper class for displaying the trigometric values in a JSP page.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class TrigonometricValues {
		/** value for which trigonometric values are being calculated, in degrees */
		private int value;
		/** sine of the value */
		private double sin;
		/** cosine of the value */
		private double cos;
		
		/**
		 * Creates a new TrigonometricValues from the specified value in degrees.
		 * 
		 * @param value value for which trigonometric values are being calculated, in degrees
		 */
		private TrigonometricValues(int value) {
			this.value = value;
			
			double radianValue = Math.toRadians(value);
			sin = Math.sin(radianValue);
			cos = Math.cos(radianValue);
		}
		
		/**
		 * Returns this object's value.
		 * 
		 * @return this object's value
		 */
		public int getValue() {
			return value;
		}
		
		/**
		 * Returns the sine of this object's value.
		 * 
		 * @return the sine of this object's value
		 */
		public double getSin() {
			return sin;
		}
		
		/**
		 * Returns the cosine of this object's value.
		 * 
		 * @return the cosine of this object's value
		 */

		public double getCos() {
			return cos;
		}
		
		/**
		 * Returns a string representation of this object's sine value,
		 * rounded to 6 decimals.
		 * 
		 * @return a string representation of this object's sine value,
		 * 		   rounded to 6 decimals.
		 */
		public String getRoundedSin() {
			return String.format("%.6f", sin);
		}
		
		/**
		 * Returns a string representation of this object's cosine value,
		 * rounded to 6 decimals.
		 * 
		 * @return a string representation of this object's cosine value,
		 * 		   rounded to 6 decimals.
		 */
		public String getRoundedCos() {
			return String.format("%.6f", cos);
		}
	}
	
	/** default value for the a parameter */
	private static final int A_DEFAULT = 0;
	/** default value for the b parameter */
	private static final int B_DEFAULT = 360;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = A_DEFAULT;
		int b = B_DEFAULT;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException ignorable) { }
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		if (b > a+720) {
			b = a+720;
		}
		
		List<TrigonometricValues> values = new ArrayList<>();
		for (int i = a; i <= b; i++) {
			values.add(new TrigonometricValues(i));
		}
		
		req.setAttribute("trigValues", values);
		
		req
			.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp")
			.forward(req, resp);
	}
}
