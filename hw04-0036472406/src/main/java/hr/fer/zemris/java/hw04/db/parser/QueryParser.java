package hr.fer.zemris.java.hw04.db.parser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.db.ComparisonOperators;
import hr.fer.zemris.java.hw04.db.ConditionalExpression;
import hr.fer.zemris.java.hw04.db.FieldValueGetters;
import hr.fer.zemris.java.hw04.db.IComparisonOperator;
import hr.fer.zemris.java.hw04.db.IFieldValueGetter;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw04.db.lexer.Token;
import hr.fer.zemris.java.hw04.db.lexer.TokenType;

/**
 * Represents a text parser. QueryParser uses a lexer which
 * tokenizes a piece of text so the parser can determine
 * whether the tokens received are generated in a well
 * defined manner. If they are, it builds a list of conditional
 * expressions the text is composed of. 
 * 
 * <p>Depending on the query that has been parsed, the parser
 * determines whether the query is a "direct query" or not.
 * 
 * <p>Offers methods for retrieving the list of conditional
 * expressions, for retrieving whether the query was a direct
 * query and, if it was, for retrieving the queried JMBAG.
 * 
 * @author Vice Ivušić
 *
 */
public class QueryParser {

	/** list of conditional expressions parsed from the query **/
	private List<ConditionalExpression> queries;
	/** lexer used for tokenizing the query **/
	private QueryLexer lexer;
	/** flag which indicates whether the query was a direct query or not **/
	private boolean directQuery;
	
	/**
	 * Creates a QueryParser which generates a list of conditional
	 * expressions from the specified query.
	 * 
	 * @param query query to be parsed
	 * @throws IllegalArgumentException if the specified query is null
	 * @throws QueryParserException if an error occurs during parsing
	 */
	public QueryParser(String query) {
		if (query == null) {
			throw new IllegalArgumentException("query text cannot be null!");
		}
		
		directQuery = true;
		queries = new ArrayList<>();
		
		lexer = new QueryLexer(query);
		parse();
	}
	
	/**
	 * Returns true if the parsed query is a direct query, i.e.
	 * if it's in the form of <code>jmbag="..."</code>.
	 * 
	 * @return true iff the parsed query is a direct query
	 */
	public boolean isDirectQuery() {
		return directQuery;
	}

	/**
	 * Returns the JMBAG value of a direct query.
	 * 
	 * @return JMBAG of the direct query
	 * @throws IllegalStateException if the method is called
	 * 		   when the last parsed query was not direct
	 */
	public String getQueriedJMBAG() {
		if (!directQuery) {
			throw new IllegalStateException("Query was not a direct query!");
		}
		
		return queries.get(0).getValue();
	}

	/**
	 * Returns a list of conditional expressions built from
	 * the parsed query. Always contains at least one expression.
	 * 
	 * @return list of conditional expressions
	 */
	public List<ConditionalExpression> getQuery() {
		return queries;
	}

	/**
	 * Helper method which parses the query.
	 */
	private void parse() {
		
		while(true) {
			getNextToken();
			if (!isTokenOfType(TokenType.ATTRIBUTE_NAME)) {
				throw new QueryParserException("Expected attribute name!");
			}
			IFieldValueGetter getter = getGetter();
			
			
			getNextToken();
			if (!isTokenOfType(TokenType.COMPARISON_OPERATOR)) {
				throw new QueryParserException("Expected operator!");
			}
			IComparisonOperator operator = getOperator();
			
			
			getNextToken();
			if (!isTokenOfType(TokenType.STRING_VALUE)) {
				throw new QueryParserException("Expected string comparison value!");
			}
			String comparisonValue = (String) getTokenValue();
			
			if (operator == ComparisonOperators.LIKE) {
				if (comparisonValue.indexOf('*') != comparisonValue.lastIndexOf('*')) {
					throw new QueryParserException(
							"Comparison value for LIKE can at most contain one wildcard (*) character!"
					);
				}
			}
			
			
			if (getter != FieldValueGetters.JMBAG || operator != ComparisonOperators.EQUALS) {
				directQuery = false;
			}
			
			queries.add(
					new ConditionalExpression(getter, comparisonValue, operator)
			);
			
			
			getNextToken();
			if (isTokenOfType(TokenType.EOF)) {
				break;
			}
			
			if (!isTokenOfType(TokenType.LOGICAL_OPERATOR)) {
				throw new QueryParserException("Expected either EOF or logical operator!");
			}
			if (!getTokenValue().equals("and")) {
				throw new QueryParserException("Logical operator can only be 'and'!");
			}
			
			directQuery = false;
		}
		
	}

	/**
	 * Helper method which checks an ATTRIBUTE_NAME token's value and 
	 * generates the appropriate IFieldValueGetter object.
	 * 
	 * @return IFieldValueGetter object for the current token
	 */
	private IFieldValueGetter getGetter() {
		String attributeName = (String) getTokenValue();
		IFieldValueGetter getter;
		
		switch (attributeName) {
		case "jmbag":
			getter = FieldValueGetters.JMBAG;
			break;
		case "firstName":
			getter = FieldValueGetters.FIRST_NAME;
			break;
		case "lastName":
			getter = FieldValueGetters.LAST_NAME;
			break;
		case "":
			throw new QueryParserException("Query contains illegal character(s)!");
		default:
			throw new QueryParserException(
					"Attribute name '"+attributeName+"' not an allowed value! Case sensitive!"
			);
		}
		
		return getter;
	}
	
	/**
	 * Helper method which checks a COMPARISON_OPERATOR token's value
	 * and generates the appropriate IComparisonOperator object.
	 * 
	 * @return IComparisonOperator object for current token
	 */
	private IComparisonOperator getOperator() {
		String operatorString = (String) getTokenValue();
		IComparisonOperator operator;
		
		switch (operatorString) {
		case "<":
			operator = ComparisonOperators.LESS;
			break;
		case "<=":
			operator = ComparisonOperators.LESS_OR_EQUALS;
			break;
		case "=":
			operator = ComparisonOperators.EQUALS;
			break;
		case ">=":
			operator = ComparisonOperators.GREATER_OR_EQUALS;
			break;
		case ">":
			operator = ComparisonOperators.GREATER;
			break;
		case "!=":
			operator = ComparisonOperators.NOT_EQUALS;
			break;
		case "LIKE":
			operator = ComparisonOperators.LIKE;
			break;
		default:
			throw new QueryParserException(
					"Operator '"+operatorString+"' is not allowed! Case sensitive!"
			);
		}
		
		return operator;
	}
	
	/**
	 * Helper method which checks if the currently generated token
	 * is of the specified type.
	 * 
	 * @param type the TokenType being checked
	 * @return true iff the current token is of the specified type
	 */
	private boolean isTokenOfType(TokenType type) {
		return lexer.getToken().getType() == type;
	}
	
	/**
	 * Helper method which returns the value of the current token.
	 * 
	 * @return value of the current token
	 */
	private Object getTokenValue() {
		return lexer.getToken().getValue();
	}
	
	/**
	 * Helper method which generates the next token and wraps
	 * any QueryLexerException that might have occured during
	 * tokenization into a QueryParserException.
	 * 
	 * @return the token generated by the lexer
	 */
	private Token getNextToken() {
		try {
			Token token = lexer.nextToken();
			return token;
		} catch (QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
	}
}
