package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * A simple program which prints out the content of a smart script
 * file after it has been parsed into a {@linkplain DocumentNode}.
 * Expects a single argument: path to smart script file.
 * 
 * @author Vice Ivušić
 *
 */
public class TreeWriter {

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; expects one argument:
	 * 		  path to smart script file
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Expected path to file with smart script!");
			return;
		}
		
		Path filePath;
		try {
			filePath = Paths.get(args[0]);
		} catch (InvalidPathException ex) {
			System.out.println("Path "+args[0]+" is not a valid path!");
			return;
		}
		
		if (Files.notExists(filePath)) {
			System.out.println("File "+filePath.toAbsolutePath()+" does not exist!");
			return;
		}
		
		if (Files.isDirectory(filePath)) {
			System.out.println("Specified path cannot point to directory!");
			return;
		}
		
		String fileData;
		try {
			fileData = new String(
				Files.readAllBytes(filePath),
				StandardCharsets.UTF_8
			);
		} catch (IOException ex1) {
			System.out.println("Could not read specified file!");
			return;
		} catch (OutOfMemoryError ex2) {
			System.out.println("Specified file is too large for current memory settings!");
			return;
		} catch (SecurityException ex3) {
			System.out.println("Insufficient security privileges for reading specified file!");
			return;
		}
		
		DocumentNode documentNode;
		try {
			documentNode = new SmartScriptParser(fileData).getDocumentNode();
		} catch (SmartScriptParserException ex) {
			System.out.println("Parsing error: "+ex.getMessage());
			return;
		}
		
		WriterVisitor writer = new WriterVisitor();
		writer.visit(documentNode);
	}
	
	/**
	 * An implementation of the {@linkplain INodeVisitor} interface which
	 * traverses a {@linkplain Node} object and prints out its contents
	 * to standard output.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visit(TextNode node) {
			for (char c : node.getText().toCharArray()) {
				if (c == '{' || c == '\\') {
					System.out.print("\\");
				}
				
				System.out.print(c);
			}
		}

		@Override
		public void visit(ForLoopNode node) {
			Element stepExpression = node.getStepExpression();
			
			System.out.print(String.format(
				"{$FOR %s %s %s %s$}",
				node.getVariable(),
				node.getStartExpression(),
				node.getEndExpression(),
				stepExpression == null ? "" : stepExpression + " "
			));
			
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
			
			System.out.print("{$END$}");
		}

		@Override
		public void visit(EchoNode node) {
			System.out.print("{$= ");
			for (Element elem : node.getElements()) {
				System.out.print(elem + " ");
			}
			System.out.print("$}");
		}

		@Override
		public void visit(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}
		
	}
}
