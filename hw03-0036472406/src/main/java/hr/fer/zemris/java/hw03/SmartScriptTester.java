package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program koji prima putanju do tekstualne datoteke na
 * standardni ulaz i provodi sintaksnu analizu tog teksta.
 * Tekst se ispisuje prije i poslije provedene sintaksne analize.
 * Moguće je da se raspored riječi promijeni zbog uklanjanja
 * ili dodavanja razmaka u tekstu.
 * 
 * Primjer ispravnog argumenta: "src/test/resources/test1"
 * 
 * Datoteke na raspolaganju su: test1, test2, test3, test4 i test5.
 * 
 * @author Vice Ivušić
 *
 */
public class SmartScriptTester {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti programa
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Expected a single argument with path to file!");
			System.exit(-1);
		}
		
		String docBody = null;
		try {
			String path = args[0];
			docBody = new String(
					Files.readAllBytes(Paths.get(path)),
					StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			System.out.println("Couldn't read file from given path!");
			System.exit(-1);
		}
		
		System.out.println();
		System.out.println("/******************** ORIGINAL TEXT ********************/");
		System.out.println(docBody);
		System.out.println();
		
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		} catch(Exception ex) {
			System.out.println("If this ever executes, you have failed this class!");
			System.exit(-1);
		}
		
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		
		System.out.println("/********* TEXT RECONSTRUCTED FROM PARSED BITS *********/");
		System.out.println(originalDocumentBody);
		
	}
	
	/**
	 * Pomoćna metoda koja rekonstruira sadržaj originalnog dokumenta iz
	 * <code>DocumentNode</code> sintaksnog stabla.
	 * 
	 * @param document sintaksno stablo
	 * @return rekonstruirani dokument
	 */
	private static String createOriginalDocumentBody(DocumentNode document) {
		return document.toString();
	}
	
}