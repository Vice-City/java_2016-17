package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class which implements {@linkplain IWebWorker}. Generates a table
 * representing the operands and result of an addition of two numbers.
 * The numbers may be set by setting parameter a and b; otherwise,
 * default values of 1 and 2 (respectively) are used.
 * 
 * @author Vice Ivušić
 *
 */
public class SumWorker implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) {
		
		String aToken = context.getParameter("a");
		int a = aToken == null ? 1 : Integer.parseInt(aToken);
		
		String bToken = context.getParameter("b");
		int b = bToken == null ? 2 : Integer.parseInt(bToken);
		
		int sum = a + b;
		String sumToken = Integer.toString(sum);
		aToken = Integer.toString(a);
		bToken = Integer.toString(b);
		
		context.setTemporaryParameter("a", aToken);
		context.setTemporaryParameter("b", bToken);
		context.setTemporaryParameter("sum", sumToken);
		
		try {
			context.getDispatcher().dispatchRequest("/private/calc.smscr");
		} catch (Exception e) {
			throw new RuntimeException("SumWorker could not write to output stream!");
		}
		
	}
}