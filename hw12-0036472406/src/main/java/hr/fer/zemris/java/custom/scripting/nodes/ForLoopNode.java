package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Predstavlja čvor grafa koji sadrži informaciju o <code>ForLoop</code> tag-u.
 * 
 * @author Vice Ivušić
 *
 */
public class ForLoopNode extends Node {

	/** ime varijable FOR petlje **/
	private ElementVariable variable;
	/** početni izraz FOR petlje **/
	private Element startExpression;
	/** završni izraz FOR petlje **/
	private Element endExpression;
	/** izraz za korak FOR petlje **/
	private Element stepExpression;
	
	/**
	 * Stvara <code>ForLoopNode</code> s navedenom varijablom, početnim izrazom,
	 * završnim izrazom i korakom. Ime varijable mora biti <code>ElementVariable</code>;
	 * početni i završni izraz te izraz za korak moraju biti ili
	 * <code>ElementConstantInteger</code>, ili <code>ElementConstantDouble</code>, 
	 * ili <code>ElementString</code>. Izraz za korak dodatno može biti null.
	 * 
	 * @param variable ime varijale
	 * @param startExpression početni izraz
	 * @param endExpression završni izraz
	 * @param stepExpression izraz za korak
	 * @throws NullPointerException ako je varijable, početni ili završni izraz null
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		if (variable == null) {
			throw new NullPointerException("variable must not be null!");
		}
		if (startExpression == null) {
			throw new NullPointerException("startExpression must not be null!");
		}
		if (endExpression == null) {
			throw new NullPointerException("endExpression must not be null!");
		}
		
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Dohvaća varijablu ovog FOR čvora.
	 * 
	 * @return varijabla
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Dohvaća početni izraz FOR čvora.
	 * 
	 * @return početni izraz
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Dohvaća završni izraz FOR čvora.
	 * 
	 * @return završni izraz
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Vraća izraz za korak FOR čvora.
	 * 
	 * @return izraz za korak
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visit(this);
	}

}
