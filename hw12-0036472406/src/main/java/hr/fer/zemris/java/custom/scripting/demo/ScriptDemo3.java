package hr.fer.zemris.java.custom.scripting.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Simple program which loads and executes the script brojPoziva.smscr,
 * and prints out the results to standard output.
 * Expects the script to be stored (relative root folder) in 
 * {@code webroot/scripts/brojPoziva.smscr}.
 * 
 * @author Vice Ivušić
 *
 */
public class ScriptDemo3 {

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		String documentBody = DemoUtilities.readFromDisk("webroot/scripts/brojPoziva.smscr");
		
		if (documentBody == null) {
			System.err.println("Could not load script from expected location!");
			return;
		}
		
		Map<String, String> parameters = new HashMap<>();
		Map<String, String> persistentParameters = new HashMap<>();
		
		List<RCCookie> cookies = new ArrayList<>();
		
		persistentParameters.put("brojPoziva", "3");
		
		RequestContext rc = new RequestContext(
			System.out,
			parameters,
			persistentParameters,
			cookies
		);
		
		SmartScriptEngine engine;
		try {
			engine = new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				rc
			);
		} catch (SmartScriptParserException ex) {
			System.err.println("Parsing error occured: "+ex.getMessage());
			return;
		}
		
		try {
			engine.execute();
		} catch (RuntimeException ex) {
			System.err.println("Execution error occured: "+ex.getMessage());
		}
		
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}

}

