package demo;

import java.util.Arrays;

import hr.fer.zemris.bf.utils.Util;

/**
 * Demo program for checking the results of Task 4.
 * 
 * @author Marko Čupić
 *
 */
public class ForEachDemo1 {

	/**
	 * The program starts by executing this method.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		Util.forEach(
				Arrays.asList("A","B","C"), 
				values -> 
					System.out.println(
						Arrays.toString(values)
							.replaceAll("true", "1")
							.replaceAll("false", "0")
					)
		);
	}
	
}
