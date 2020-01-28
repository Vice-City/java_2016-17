package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A GUI program for calculating consecutive prime numbers. Prime numbers
 * are added to two lists each time the user presses the next button.
 * 
 * @author Vice Ivušić
 *
 */
public class PrimDemo extends JFrame {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new graphical interface for the program.
	 * 
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(10, 10, 500, 400);
		
		initGUI();
	}

	/**
	 * Initializes the programs graphical user interface.
	 * 
	 */
	private void initGUI() {
		Container cp = getContentPane();
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		cp.add(contentPanel);
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 2));
		contentPanel.add(listPanel, BorderLayout.CENTER);
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		listPanel.add(new JScrollPane(list1), BorderLayout.LINE_START);
		listPanel.add(new JScrollPane(list2), BorderLayout.LINE_END);
		
		JButton addButton = new JButton();
		addButton.setText("Next prime number");
		addButton.addActionListener(e -> {
			model.next();
		});
		contentPanel.add(addButton, BorderLayout.PAGE_END);
	}
	
	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
		});
	}
}
