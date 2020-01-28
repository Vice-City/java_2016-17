package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

public class SmartScriptParserTest {

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNull() {
		new SmartScriptParser(null);
	}
	
	@Test
	public void testEmptyDocument() {
		SmartScriptParser parser = new SmartScriptParser("");
		assertEquals(0, parser.getDocumentNode().numberOfChildren());
	}
	
	@Test
	public void testTextNode() {
		SmartScriptParser parser = new SmartScriptParser("\\\\Foobar.\\{");
		DocumentNode doc = parser.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		
		TextNode actual = (TextNode) doc.getChild(0); 
		TextNode expected = new TextNode("\\Foobar.{");
		
		assertTrue(textNodesAreEqual(actual, expected));
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testInvalidTextNode() {
		new SmartScriptParser("Foobar.\\");
	}
	
	@Test 
	public void testEchoNode() {
		SmartScriptParser parser = new SmartScriptParser(
				"{$     = i*j_-32@foo\"bar\\\"\\\\\"0.2$}"
		);
		DocumentNode doc = parser.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		
		EchoNode actual = (EchoNode) doc.getChild(0);
		EchoNode expected = new EchoNode(
				new Element[] {
					new ElementVariable("i"),
					new ElementOperator("*"),
					new ElementVariable("j_"),
					new ElementConstantInteger(-32),
					new ElementFunction("foo"),
					new ElementString("bar\"\\"),
					new ElementConstantDouble(0.2d)
				}				
		);
		
		assertTrue(echoNodesAreEqual(actual, expected));
	}
	
	@Test 
	public void testEmptyEchoNode() {
		SmartScriptParser parser = new SmartScriptParser(
				"{$=$}"
		);
		DocumentNode doc = parser.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		
		EchoNode actual = (EchoNode) doc.getChild(0);
		EchoNode expected = new EchoNode(new Element[0]);
		
		assertTrue(echoNodesAreEqual(actual, expected));
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testInvalidEchoNodeVarName() {
		new SmartScriptParser("{$=_i");
	}

	@Test(expected=SmartScriptParserException.class)
	public void testInvalidStringEscape() {
		new SmartScriptParser("{$= \"foo\\bar\"");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testInvalidStringEscapeAtEnd() {
		new SmartScriptParser("{$= \"foobar\\\"");
	}

	@Test(expected=SmartScriptParserException.class)
	public void testInvalidTagName() {
		new SmartScriptParser("{$FORR i j k $}{$END$}");
	}
	
	@Test
	public void testForLoopNode() {
		SmartScriptParser parser = new SmartScriptParser("{$FOR i j 10.2 \"k\" $}{$END$}");
		DocumentNode doc = parser.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		
		ForLoopNode actual = (ForLoopNode) doc.getChild(0);
		ForLoopNode expected = new ForLoopNode(
				new ElementVariable("i"),
				new ElementVariable("j"),
				new ElementConstantDouble(10.2d),
				new ElementString("k")
		);
		
		assertTrue(forLoopNodesAreEqual(actual, expected));
	}
	
	@Test
	public void testForLoopNodeNoStepExpression() {
		SmartScriptParser parser = new SmartScriptParser("{$FOR i j 10.2 $}{$END$}");
		DocumentNode doc = parser.getDocumentNode();
		
		assertEquals(1, doc.numberOfChildren());
		
		ForLoopNode actual = (ForLoopNode) doc.getChild(0);
		ForLoopNode expected = new ForLoopNode(
				new ElementVariable("i"),
				new ElementVariable("j"),
				new ElementConstantDouble(10.2d),
				null
		);
		
		assertTrue(forLoopNodesAreEqual(actual, expected));
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testUnclosedForLoop() {
		new SmartScriptParser("{$FOR i j k $}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testTooManyEndTags() {
		new SmartScriptParser("{$FOR i j k $}{$END}{$END$}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testForLoopTooFewArguments() {
		new SmartScriptParser("{$FOR i j $}{$END$}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testForLoopNoArgumentsAtAll() {
		new SmartScriptParser("{$FOR$}{$END$}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testForLoopTooManyArguments() {
		new SmartScriptParser("{$FOR i j k l m $}{$END$}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testForLoopInvalidVariableType() {
		new SmartScriptParser("{$FOR 10 j k $}{$END$}");
	}
	
	@Test(expected=SmartScriptParserException.class)
	public void testForLoopInvalidExpression() {
		new SmartScriptParser("{$FOR i j k @Func $}{$END$}");
	}
	
	@Test
	public void testMixedInput() {
		SmartScriptParser parser = new SmartScriptParser(
				"This is FOR:\n"
				+ "{$FOR i 10 2.71 \"10.00\" $}\n"
				+ "Printing {$= i @echo $}\n"
				+ "{$END$}\n"
				+ "Done printing."
		);
		DocumentNode doc = parser.getDocumentNode();
		assertEquals(3, doc.numberOfChildren());
		
		TextNode t1Actual = (TextNode) doc.getChild(0);
		TextNode t1Expected = new TextNode("This is FOR:\n");
		assertTrue(textNodesAreEqual(t1Actual, t1Expected));
		
		ForLoopNode f1Actual = (ForLoopNode) doc.getChild(1);
		assertEquals(3, f1Actual.numberOfChildren());
		ForLoopNode f1Expected = new ForLoopNode(
				new ElementVariable("i"),
				new ElementConstantInteger(10),
				new ElementConstantDouble(2.71),
				new ElementString("10.00")
		);
		assertTrue(forLoopNodesAreEqual(f1Actual, f1Expected));
		
		
		TextNode t2Actual = (TextNode) f1Actual.getChild(0);
		TextNode t2Expected = new TextNode("\nPrinting ");
		assertTrue(textNodesAreEqual(t2Actual, t2Expected));
		
		EchoNode e1Actual = (EchoNode) f1Actual.getChild(1);
		EchoNode e1Expected = new EchoNode(
				new Element[] {
						new ElementVariable("i"),
						new ElementFunction("echo")
				}
		);
		assertTrue(echoNodesAreEqual(e1Actual, e1Expected));
		
		TextNode t3Actual = (TextNode) f1Actual.getChild(2);
		TextNode t3Expected = new TextNode("\n");
		assertTrue(textNodesAreEqual(t3Actual, t3Expected));
		
		TextNode t4Actual = (TextNode) doc.getChild(2);
		TextNode t4Expected = new TextNode("\nDone printing.");
		assertTrue(textNodesAreEqual(t4Actual, t4Expected));
	}
	
	
	
	@Test(expected=SmartScriptParserException.class)
	public void testImproperlyClosedTag() {
		new SmartScriptParser("{$FOR i j k}{$END$}");
	}

	@Test
	public void testHelperMethods() {
		Element d1 = new ElementConstantDouble(2.5);
		Element d2 = new ElementConstantDouble(3.5);
		Element d3 = new ElementConstantDouble(2.5);
		Element i1 = new ElementConstantInteger(1);
		
		assertFalse(d1.equals(d2));
		assertTrue(d1.equals(d3));
		assertFalse(d1.equals(i1));
		
		TextNode t1 = new TextNode("foo");
		TextNode t2 = new TextNode("foo");
		TextNode t3 = new TextNode("bar");
		
		assertTrue(textNodesAreEqual(t1, t2));
		assertFalse(textNodesAreEqual(t1, t3));
		
		ForLoopNode f1 = new ForLoopNode(
				new ElementVariable("pikachu"),
				new ElementConstantInteger(1),
				new ElementConstantDouble(2.4),
				new ElementString("charizard")
		);
		ForLoopNode f2 = new ForLoopNode(
				new ElementVariable("pikachu"),
				new ElementConstantInteger(1),
				new ElementConstantDouble(2.4),
				new ElementString("charizard")
		);
		ForLoopNode f3 = new ForLoopNode(
				new ElementVariable("bulbasaur"),
				new ElementConstantInteger(1),
				new ElementConstantDouble(2.4),
				new ElementString("charizard")
		);
		
		assertTrue(forLoopNodesAreEqual(f1, f2));
		assertFalse(forLoopNodesAreEqual(f1, f3));
		
	}

	private boolean textNodesAreEqual(TextNode node1, TextNode node2) {
		return node1.getText().equals(node2.getText());
	}
	
	private boolean echoNodesAreEqual(EchoNode node1, EchoNode node2) {
		Element[] elements1 = node1.getElements();
		Element[] elements2 = node2.getElements();
		
		if (elements1.length != elements2.length) {
			return false;
		}
		
		for (int i = 0, n = elements1.length; i < n; i++) {
			Element elem1 = elements1[i];
			Element elem2 = elements2[i];
			
			if (!elem1.equals(elem2)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean forLoopNodesAreEqual(ForLoopNode node1, ForLoopNode node2) {
		
		if (!node1.getVariable().equals(node2.getVariable())) return false;
		if (!node1.getStartExpression().equals(node2.getStartExpression())) return false;
		if (!node1.getEndExpression().equals(node2.getEndExpression())) return false;
		
		if (node1.getStepExpression() != null) {
			if (!node1.getStepExpression().equals(node2.getStepExpression())) {
				return false;
			}
		} else if (node2.getStepExpression() != null) {
			return false;
		}
		
		return true;
	}
}