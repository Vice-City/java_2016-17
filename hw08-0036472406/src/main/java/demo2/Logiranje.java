package demo2;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 ************************************************************
 ********************                    ********************
 ********************       TASK3        ********************
 ********************                    ********************
 ************************************************************
 */

/**
 * Demo program for trying out Java's Logger class. Prints out
 * a message for each of the default logging levels.
 * 
 * <p>Note that the JVM must be configured properly when running
 * this program by specifying the following flag:
 * <br>{@code -Djava.util.logging.config.file=./logging.properties}
 * 
 * <p>The logging.properties file has to be located at the root
 * directory of this program's source code.
 * 
 * @author Vice Ivušić
 *
 */
public class Logiranje {
	
	/** logger object used for logging purposes in this program */
	private static final Logger LOG = Logger.getLogger("demo2");

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		Level[] levels = new Level[] {
			Level.SEVERE,
			Level.WARNING,
			Level.INFO,
			Level.CONFIG,
			Level.FINE,
			Level.FINER,
			Level.FINEST
		};
		
		for (Level l : levels) {
			LOG.log(l, "Ovo je poruka " + l + " razine.");
		}
	}
}
