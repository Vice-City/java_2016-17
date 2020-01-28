package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * An HttpServlet which generates an Excel spreadsheet containing parsed
 * values and their values raised to a range of set powers. Prints
 * an error page when the parameters aren't compatible. If parameter
 * a is greater than parameter b, the parameters are switched.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="powersServlet", urlPatterns={"/powers2"})
public class PowersServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Helper class for easier rendering inside JSP pages.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class ErrorValues {
		/** name of the value */
		String name;
		/** value */
		Integer value;
		/** lowest allowed value */
		int lowestValue;
		/** highest allowed value */
		int highestValue;
		
		/**
		 * Creates a new ErrorValues with the specified arguments. Name
		 * and value may be null.
		 * 
		 * @param name name of the value
		 * @param value value
		 * @param lowestValue lowest allowed value
		 * @param highestValue highest allowed value
		 */
		public ErrorValues(String name, Integer value, int lowestValue, int highestValue) {
			this.name = name;
			this.value = value;
			this.lowestValue = lowestValue;
			this.highestValue = highestValue;
		}

		/**
		 * Returns the value's name.
		 * 
		 * @return the value's name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Returns the value's value.
		 * 
		 * @return the value's value
		 */
		public String getValue() {
			return value == null ? "null" : value.toString();
		}

		/**
		 * Returns the expected interval of values for current value.
		 * 
		 * @return the expected interval of values for current value
		 */
		public String getExpected() {
			return String.format("[%d, %d]", lowestValue, highestValue);
		}
	}
	
	/** minimum value for a and b parameter */
	private static final int VALUE_MIN = -100;
	/** maximum value for a and b parameter */
	private static final int VALUE_MAX = 100;
	/** minimum value for n parameter */
	private static final int N_MIN = 1;
	/** maximum value for n parameter */
	private static final int N_MAX = 5;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = null;
		Integer b = null;
		Integer n = null;
		
		// I am purposefully try/catching them individually for accurate error rendering
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch (NumberFormatException ignorable) {}
		
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch (NumberFormatException ignorable) {}
		
		try {
			n = Integer.parseInt(req.getParameter("n"));
		} catch (NumberFormatException ignorable) {}
		
		if (a == null || b == null || n == null ||
			a < VALUE_MIN || a > VALUE_MAX ||
			b < VALUE_MIN || b > VALUE_MAX ||
			n < N_MIN || n > N_MAX) {
			
			List<ErrorValues> errorValues = new ArrayList<>();
			errorValues.add(new ErrorValues("a", a, VALUE_MIN, VALUE_MAX));
			errorValues.add(new ErrorValues("b", b, VALUE_MIN, VALUE_MAX));
			errorValues.add(new ErrorValues("n", n, N_MIN, N_MAX));

			req.setAttribute("errorValues", errorValues);
			
			req.getRequestDispatcher("/WEB-INF/pages/powersError.jsp").forward(req, resp);;
			return;
		}
		
		if (a > b) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		for (int i = 0; i < n; i++) {
			HSSFSheet nSheet = workbook.createSheet("Power of "+(i+1));
			
			int k = 0;
			for (int j = a; j <= b; j++) {
				Row row = nSheet.createRow(k++);
				Cell cellNum = row.createCell(0);
				cellNum.setCellValue(j);
				
				Cell cellNPower = row.createCell(1);
				cellNPower.setCellValue(Math.pow(j, i+1));

			}
		}
		
        resp.setContentType("application/vnd.ms-excel");
        ServletOutputStream os = resp.getOutputStream();
        
        workbook.write(os);
        workbook.close();
	}
}
