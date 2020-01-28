package hr.fer.zemris.java.gui.charts;

import java.awt.Font;
import java.nio.file.Path;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Models a label which displays a file path.
 * 
 * @author Vice Ivušić
 *
 */
public class PathLabel extends JLabel {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new PathLabel with the specified path. Assumes
	 * the user will pass a non-null path.
	 * 
	 * @param path path to display
	 */
	public PathLabel(Path path) {
		setText(path.toAbsolutePath().toString());
		setHorizontalAlignment(SwingConstants.CENTER);
		setFont(new Font("Consolas", Font.BOLD, getFont().getSize()));
	}

}
