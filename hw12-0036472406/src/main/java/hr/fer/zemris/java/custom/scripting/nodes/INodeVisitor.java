package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Specifies a visitor from the Visitor design pattern which knows how
 * to traverse {@linkplain TextNode}, {@linkplain ForLoopNode}, 
 * {@linkplain EchoNode} and {@linkplain DocumentNode} objects.
 * 
 * @author Vice Ivušić
 *
 */
public interface INodeVisitor {

	/**
	 * Method to be executed when visiting a TextNode object.
	 * 
	 * @param node the TextNode being visited
	 */
	public void visit(TextNode node);
	
	/**
	 * Method to be executed when visiting a ForLoopNode object.
	 * 
	 * @param node the ForLoopNode being visited
	 */

	public void visit(ForLoopNode node);
	
	/**
	 * Method to be executed when visiting an EchoNode object.
	 * 
	 * @param node the EchoNode being visited
	 */

	public void visit(EchoNode node);
	
	/**
	 * Method to be executed when visiting a DocumentNode object.
	 * 
	 * @param node the DocumentNode being visited
	 */

	public void visit(DocumentNode node);
}
