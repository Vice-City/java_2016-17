package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Predstavlja parser dokumenta. Parser koristi tokenizator za izradu
 * tokena te provjerava jesu li primljeni tokeni generirani u dopuštenom
 * redoslijedu; ako jesu, gradi sintaksno stablo koje predstavlja
 * dokument, tj. provodi sintaksnu analizu.
 * 
 * <p>Nudi metodu za dohvat izrađenog sintaksnog stabla u obliku
 * <code>DocumentNode</code> objekta.
 * 
 * @author Vice Ivušić
 *
 */
public class SmartScriptParser {

	/** tokenizator izvornog teksta **/
	private Lexer lexer;
	/** sintaksno stablo dokumenta **/
	private DocumentNode documentNode;
	/** pomoćni stog za izgradnju sintaksnog stabla **/
	private ObjectStack stack;
	
	/**
	 * Stvara <code>SmartScriptParser</code> koji generira <code>DocumentNode</code>
	 * iz predanog teksta.
	 * 
	 * @param document tekst koji se parsira
	 * @throws IllegalArgumentException ako je predan null za tekst
	 * @throws SmartScriptParserException ako dođe do pogreške tijekom
	 * 			sintaksne analize teksta
	 */
	public SmartScriptParser(String document) {
		if (document == null) {
			throw new IllegalArgumentException (
					"Cannot create SmartScriptParser with document set to null!"
			);
		}
		lexer = new Lexer(document);
		stack = new ObjectStack();
		parse();
	}
	
	/**
	 * Dohvaća izrađeno sintaksno stablo u obliku <code>DocumentNode</code> objekta.
	 * 
	 * @return sintaksno stablo
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}

	/**
	 * Pomoćna metoda koja provodi sintaksnu analizu teksta.
	 */
	private void parse() {
		stack.push(new DocumentNode());
		getNextToken();
		
		while (true) {
			if (isTokenOfType(TokenType.EOF)) {
				break;
			}
			
			if (isTokenOfType(TokenType.TEXT)) {
				parseText();
				continue;
			}
			
			if (isTokenOfType(TokenType.TAG_START)) {
				parseTag();
				continue;
			}
			
			throw new SmartScriptParserException(
					addInfo("Expected either text or tag start!")
			);
		}
		
		if (stack.size() != 1) {
			throw new SmartScriptParserException(
					addInfo("One or more unclosed FOR tag!")
			);
		}
		
		documentNode = (DocumentNode) stack.pop();
	}
	
	/**
	 * Pomoćna metoda za parsiranje običnog teksta.
	 */
	private void parseText() {
		Node topNode = (Node) stack.peek();
		TextNode textNode = new TextNode((String) getTokenValue());
		topNode.addChildNode(textNode);
		
		getNextToken();
	}

	/**
	 * Pomoćna metoda za parsiranje tag-a.
	 */
	private void parseTag() {
		lexer.setState(LexerState.TAG);
		
		getNextToken();
		if (!isTokenOfType(TokenType.TAG_NAME)) {
			throw new SmartScriptParserException(
					addInfo("Expected tag name after tag opening!")
			);
		}
		
		switch (((String) getTokenValue()).toLowerCase()) {
		case "=":
			getNextToken();
			parseEcho();
			break;
		case "for":
			getNextToken();
			parseForLoop();
			break;
		case "end":
			getNextToken();
			parseEnd();
			break;
		default:
			throw new SmartScriptParserException(
					addInfo("The given tag name isn't defined!")
			);
		}
		
		if (!isTokenOfType(TokenType.TAG_END)) {
			throw new SmartScriptParserException(
					addInfo("Expected tag ending!")
			);
		}
		
		lexer.setState(LexerState.TEXT);
		getNextToken();		
	}

	/**
	 * Pomoćna metoda koja provjerava trenutni token u tokenizatoru
	 * te gradi prikladni izvedeni <code>Element</code> objekt na temelju
	 * vrste tokena.
	 * 
	 * @return prikladni element ili null ako vrsta tokena nema pripadajući
	 * 		   <code>Element</code> objekt
	 */
	private Element generateElement() {
		Element element;
		TokenType type = lexer.getToken().getType();
		
		switch (type) {
		case VARIABLE:
			element = new ElementVariable((String) getTokenValue());
			break;
		case OPERATOR:
			element = new ElementOperator((String) getTokenValue());
			break;
		case FUNCTION:
			element = new ElementFunction((String) getTokenValue());
			break;
		case STRING_VALUE:
			element = new ElementString((String) getTokenValue());
			break;
		case INT_VALUE:
			element = new ElementConstantInteger((Integer) getTokenValue());
			break;
		case DOUBLE_VALUE:
			element = new ElementConstantDouble((Double) getTokenValue());
			break;
		default:
			element = null;
		}
		
		return element;
	}

	/**
	 * Pomoćna metoda koja parsira i gradi <code>EchoNode</code>.
	 */
	private void parseEcho() {
		ArrayIndexedCollection elements = new ArrayIndexedCollection();
		
		while (!isTokenOfType(TokenType.TAG_END) && !isTokenOfType(TokenType.EOF)) {
			Element element = generateElement();
			
			if (element == null) {
				throw new SmartScriptParserException(
						addInfo("Echo tag has invalid member(s)!")
				);
			}
			
			elements.add(element);
			getNextToken();
		}
		
		Element[] elems = new Element[elements.size()];
		
		for (int i = 0; i < elements.size(); i++) {
			elems[i] = (Element) elements.get(i);
		}
		
		EchoNode echoNode = new EchoNode(elems);
		Node topNode = (Node) stack.peek();
		topNode.addChildNode(echoNode);
	}

	/**
	 * Pomoćna metoda koja parsira i gradi <code>ForLoopNode</code>.
	 */
	private void parseForLoop() {
		
		Element elementVariable = generateElement();
		if (elementVariable == null) {
			throw new SmartScriptParserException(
					addInfo("Expected FOR-LOOP variable name!")
			);
		}
		if (!isTokenOfType(TokenType.VARIABLE)) {
			throw new SmartScriptParserException(
					addInfo("FOR-LOOP variable name not in right format!")
			);
		}
		ElementVariable variable = (ElementVariable) elementVariable;
		getNextToken();

		Element startExpression = generateElement();
		if (startExpression == null) {
			throw new SmartScriptParserException(
					addInfo("Expected FOR-LOOP starting expression!")
			);
		}
		if (!isElementVariableNumberOrString(startExpression)) {
			throw new SmartScriptParserException(
					addInfo("FOR-LOOP starting expression must be a variable, number or string!")
			);
		}
		getNextToken();

		Element endExpression = generateElement();
		if (endExpression == null) {
			throw new SmartScriptParserException(
					addInfo("Expected FOR-LOOP ending expression!")
			);
		}
		if (!isElementVariableNumberOrString(endExpression)) {
			throw new SmartScriptParserException(
					addInfo("FOR-LOOP ending expression must be a variable, number or string!")
			);
		}
		getNextToken();

		Element stepExpression = generateElement();
		if (stepExpression != null) {
			if (!isElementVariableNumberOrString(stepExpression)) {
				throw new SmartScriptParserException(
						addInfo("FOR-LOOP step expression must be a variable, number or string!")
				);
			}
			getNextToken();
		}

		ForLoopNode forLoopNode = new ForLoopNode(
				variable, startExpression, endExpression, stepExpression
		);
		Node topNode = (Node) stack.peek();
		topNode.addChildNode(forLoopNode);
		stack.push(forLoopNode);

	}
	
	/**
	 * Pomoćna metoda koja parsira END tag.
	 */
	private void parseEnd() {
		stack.pop();
		
		if (stack.size() == 0) {
			throw new SmartScriptParserException(
					addInfo("One or more extra END tag!")
			);
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li navedeni element primjerak jedno od
	 * sljedećih razreda: <code>ElementVariable</code>, <code>ElementConstantInteger</code>,
	 * <code>ElementConstantDouble</code> te <code>ElementString</code>.
	 * 
	 * @param element element koji se provjerava
	 * @return <b>true</b> ako i samo ako je element primjerak jedno od navedenih razreda
	 */
	private boolean isElementVariableNumberOrString(Element element) {
		if (element instanceof ElementVariable) return true;
		if (element instanceof ElementConstantInteger) return true;
		if (element instanceof ElementConstantDouble) return true;
		if (element instanceof ElementString) return true;
		
		return false;
	}

	/**
	 * Pomoćna metoda koja provjerava je li token koji se trenutno
	 * nalazi u tokenizatoru navedenog tipa.
	 * 
	 * @param type vrsta tokena
	 * @return <b>true</b> ako i samo ako je trenutni token navedene vrste
	 */
	private boolean isTokenOfType(TokenType type) {
		return lexer.getToken().getType() == type;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća vrijednost trenutnog
	 * tokena iz tokenizatora. Ne generira sljedeći token!
	 *  
	 * @return vrijednost trenutnog tokena
	 */
	private Object getTokenValue() {
		return lexer.getToken().getValue();
	}
	
	/**
	 * Pomoćna metoda koja generira i dohvaća sljedeći token iz tokenizatora.
	 * Dodatno hvata <code>LexerException</code> iznimke koje se mogu dogoditi tijekom
	 * tokenizacije te omata njihovu poruku u <code>SmartScriptParserException</code> iznimku.
	 * 
	 * @return sljedeći generirani token
	 */
	private Token getNextToken() {
		try {
			Token token = lexer.nextToken();
			return token;
		} catch (LexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}

	/**
	 * Pomoćna metoda koja dodaje predanom Stringu informaciju
	 * o broju retka teksta na kojemu je zadnji token napravljen.
	 * 
	 * @param message poruka
	 * @return poruka s dodanim brojem retka
	 */
	private String addInfo(String message) {
		return String.format("Line %d: %s", lexer.getLine(), message);
	}
}
