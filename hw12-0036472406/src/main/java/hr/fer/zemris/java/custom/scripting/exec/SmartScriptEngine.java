package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

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
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * An engine meant to execute scripts parsed by {@linkplain SmartScriptParser}
 * into {@linkplain DocumentNode} objects. The engine has to be configured with
 * an appropriate DocumentNode object (containing the script) and a
 * {@linkplain RequestContext} object, which it will use to output its results
 * using the context's write method.
 * 
 * <p>In a smart script, everything outside of {$ $} tags is interpreted as regular
 * text. The tag can either be a for-loop tag or an echo tag. A for-loop tag is of the
 * following format:
 * 
 * <pre>
 * {$FOR variable startExpression endExpression stepExpression $}
 * ... other commands or text ...
 * {$END}
 * </pre>
 * 
 * Variable is a string, while each expression may be a string (representing a number),
 * integer or double. The step expression is optional and defaults to 1.
 * 
 * <p>An echo tag may contain a variable amount of arguments. Arguments can be
 * string, integer and double constants; function calls; operators (+, -, * and /) and
 * variables. Each operator expects two arguments to precede it; for example a variable
 * and an integer constant. The number of arguments for a function call depends on the
 * function being called. The following functions are supported:
 * 
 * <pre>
 * sin - expects one argument to precede it. Calculates the sine of the given argument.
 * 
 * decfmt - expects two arguments to precede it. First argument is a value to be formatted,
 * while the second argument is the format for the argument.
 * 
 * dup - expects one argument to precede it. Duplicates the given argument.
 * 
 * swap - expects two arguments to precede it. Swaps the order of the given arguments.
 * 
 * setMimeType - expects one argument to precede it. Sets the configured context's mime
 * type to the specified argument.
 * 
 * paramGet - expects two arguments to precede it. First argument is key to search for in
 * configured context's parameters map, while the second argument is the default
 * value to be used in case the parameter isn't found.
 * 
 * pparamGet - expects two arguments to precede it. First argument is key to search for in
 * configured context's persistentParameters map, while the second argument is the 
 * default value to be used in case the parameter isn't found.
 * 
 * pparamSet - expects two arguments to precede it. First argument is key for which to add the
 * second argument as a mapping in the configured context's persistentParameters map.
 * 
 * pparamDel - expects one argument to precede it. Removes the mapping with the specified
 * argument as key from the configured context's persistentParameters map.
 * 
 * tparamGet - expects two arguments to precede it. First argument is key to search for in
 * configured context's temporaryParameters map, while the second argument is the 
 * default value to be used in case the parameter isn't found.
 * 
 * tparamSet - expects two arguments to precede it. First argument is key for which to add the
 * second argument as a mapping in the configured context's temporaryParameters map.
 * 
 * tparamDel - expects one argument to precede it. Removes the mapping with the specified
 * argument as key from the configured context's temporaryParameters map.
 * </pre>
 * 
 * The format of an echo tag is as follows: <code>{$= arg1 arg2 ... argn $}</code>
 * 
 * <p>After the smart script engine has been configured, the execution is started by
 * calling its {@linkplain #execute} method.
 * 
 * @author Vice Ivušić
 *
 */
public class SmartScriptEngine {

	/** the parsed smart script */
	private DocumentNode documentNode;
	/** context to be used for parameters and output */
	private RequestContext requestContext;
	/** mappable stacks used for result calculation */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/** an instance of the INodeVisitor interface, used for executing functions */
	private INodeVisitor visitor = generateExecutionVisitor();

	/**
	 * Creates a new SmartScriptEngine from the specified parameters.
	 * 
	 * @param documentNode parsed smart script
	 * @param requestContext context to be used for parameters and output
	 * @throws NullPointerException if either of the arguments is null
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		if (documentNode == null || requestContext == null) {
			throw new NullPointerException("Arguments cannot be null!");
		}
		
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes the script parsed from the configured DocumentNode object and
	 * outputs the result using the configured context object.
	 * 
	 * @throws RuntimeException if any kind of exception occurs during
	 * 		   execution of script; most often it would be a wrapped
	 * 		   IOException
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
	
	/**
	 * Helper method for configuring and creating an instance of the
	 * INodeVisitor interface for traversing the configured DocumentNode
	 * object and executing the scripts contained within.
	 * 
	 * @return an implementantion of the INodeVisitor interface which knows
	 * 	       how to traverse and execute scripts
	 */
	private INodeVisitor generateExecutionVisitor() {
		return new INodeVisitor() {

			@Override
			public void visit(TextNode node) {
				try {
					requestContext.write(node.getText());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}

			@Override
			public void visit(ForLoopNode node) {
				ElementVariable var = node.getVariable();
				String varName = var.asText();
				
				multistack.push(
					varName,
					new ValueWrapper(node.getStartExpression().asText())
				);
				
				while (multistack.peek(varName).numCompare(node.getEndExpression().asText()) <= 0) {
					for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
						node.getChild(i).accept(visitor);
					}
					
					Element step = node.getStepExpression();
					
					multistack.peek(varName).add(step == null ? "1" : step.asText());
				}
				
				multistack.pop(varName);
			}

			@Override
			public void visit(EchoNode node) {
				Stack<ValueWrapper> stack = new Stack<>();
				
				for (Element elem : node.getElements()) {
					if (elem instanceof ElementString || 
						elem instanceof ElementConstantDouble ||
						elem instanceof ElementConstantInteger) {
						
						stack.push(new ValueWrapper(elem.asText()));
						continue;
					}
					
					if (elem instanceof ElementVariable) {
						ValueWrapper value = multistack.peek(elem.asText());
						stack.push(new ValueWrapper(value.getValue()));
						continue;
					}
					
					if (elem instanceof ElementOperator) {
						ValueWrapper arg1 = stack.pop();
						ValueWrapper arg2 = stack.pop();
						
						switch (elem.asText()) {
						case "+":
							arg2.add(arg1.getValue());
							break;
						case "-":
							arg2.subtract(arg1.getValue());
							break;
						case "*":
							arg2.multiply(arg1.getValue());
							break;
						case "/":
							arg2.divide(arg1.getValue());
							break;
						}
						
						stack.push(arg2);
						continue;
					}
					
					if (elem instanceof ElementFunction) {
						executeFunction(elem.asText(), stack);
					}
				}
				
				for (Object obj : stack) {
					try {
						requestContext.write(obj.toString());
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
				
			}

			@Override
			public void visit(DocumentNode node) {
				for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
					node.getChild(i).accept(visitor);
				}
			}

			private void executeFunction(String function, Stack<ValueWrapper> stack) {
					
					if (function.equals("sin")) {
						ValueWrapper value = stack.peek();
						value.sin();
						return;
					}
					
					if (function.equals("decfmt")) {
						DecimalFormat decFormat = new DecimalFormat((String) stack.pop().getValue());
						Object value = stack.pop().getValue();
						
						stack.push(new ValueWrapper(decFormat.format(value)));
						return;
					}
					
					if (function.equals("dup")) {
						stack.push(new ValueWrapper(stack.peek().getValue()));
						return;
					}
					
					if (function.equals("swap")) {
						ValueWrapper a = stack.pop();
						ValueWrapper b = stack.pop();
						
						stack.push(a);
						stack.push(b);
						return;
					}
					
					if (function.equals("setMimeType")) {
						String mimeType = (String) stack.pop().getValue();
						
						requestContext.setMimeType(mimeType);
						return;
					}
					
					if (function.equals("paramGet")) {
						Object dv = stack.pop().getValue();
						Object name = stack.pop().getValue();
						
						Object value = requestContext.getParameter(name.toString());
						
						stack.push(
							value == null 
							? new ValueWrapper(dv.toString()) 
							: new ValueWrapper(value.toString())
						);
						return;
					}
					
					if (function.equals("pparamGet")) {
						Object dv = stack.pop().getValue();
						Object name = stack.pop().getValue();
						
						Object value = requestContext.getPersistentParameter(name.toString());
						
						stack.push(
							value == null 
							? new ValueWrapper(dv.toString()) 
							: new ValueWrapper(value.toString())
						);
						return;
					}
					
					if (function.equals("pparamSet")) {
						Object name = stack.pop().getValue();
						Object value = stack.pop().getValue();
						
						requestContext.setPersistentParameter(name.toString(), value.toString());
					}
					
					if (function.equals("pparamDel")) {
						Object name = stack.pop().getValue();
						
						requestContext.removePersistentParameter(name.toString());
					}
					
					if (function.equals("tparamGet")) {
						Object dv = stack.pop().getValue();
						Object name = stack.pop().getValue();
						
						Object value = requestContext.getTemporaryParameter(name.toString());
						
						stack.push(
							value == null 
							? new ValueWrapper(dv.toString()) 
							: new ValueWrapper(value.toString())
						);
						return;
					}
					
					if (function.equals("tparamSet")) {
						Object name = stack.pop().getValue();
						Object value = stack.pop().getValue();
						
						requestContext.setTemporaryParameter(name.toString(), value.toString());
					}
					
					if (function.equals("tparamDel")) {
						Object name = stack.pop().getValue();
						
						requestContext.removeTemporaryParameter(name.toString());
					}
				}
		};
	}
}
