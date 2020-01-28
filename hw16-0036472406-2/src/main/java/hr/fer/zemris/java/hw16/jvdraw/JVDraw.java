package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelImpl;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingObjectListModel;
import hr.fer.zemris.java.hw16.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.Line;
import hr.fer.zemris.java.hw16.jvdraw.objects.ObjectProvider;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.BoundingBoxShifter;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectParserUtil;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectSaver;

/**
 * A simple vector graphics GUI application. Offers tools to draw
 * lines, circles and filled circles as well as save the current
 * workspace and export the content of the workspace as an image
 * in the JPEG, PNG and GIF formats.
 * 
 * @author Vice Ivušić
 *
 */
public class JVDraw extends JFrame implements ObjectProvider {
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to the drawing model being used.
	 */
	private DrawingModel drawingModel = new DrawingModelImpl();
	
	/**
	 * Reference to the foreground color picker.
	 */
	private JColorArea fgColorPicker;
	
	/**
	 * Reference to the background color picker.
	 */
	private JColorArea bgColorPicker;
	
	/**
	 * A function which generates geometrical objects. Set by the Line, Circle and FCircle actions.
	 */
	private Function<Point, GeometricalObject> objectFactory;
	
	/**
	 * Reference to the drawing canvas being used.
	 */
	private JDrawingCanvas drawingCanvas;
	
	/**
	 * Creates a new JVDraw frame and initializes everything necessary for the
	 * application to run.
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(750, 500);
		setLocationRelativeTo(null);
		setTitle("JVDraw");

		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				exitAction.actionPerformed(null);
			}
			
		});
		
		initGUI();
	}

	/**
	 * Helper method for initializing the GUI.
	 */
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		add(mainPanel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(openAction));
		fileMenu.add(new JMenuItem(saveAction));
		fileMenu.add(new JMenuItem(saveAsAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exportAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		
		toolBar.add(new JLabel("Foreground: "));
		fgColorPicker = new JColorArea(Color.GREEN);
		toolBar.add(fgColorPicker);
		toolBar.addSeparator();
		
		toolBar.add(new JLabel("Background: "));
		bgColorPicker = new JColorArea(Color.RED);
		toolBar.add(bgColorPicker);
		toolBar.addSeparator();
		
		ButtonGroup drawingButtonGroup = new ButtonGroup();
		
		JToggleButton lineButton = new JToggleButton(lineAction);
		drawingButtonGroup.add(lineButton);
		toolBar.add(lineButton);
		
		JToggleButton circleButton = new JToggleButton(circleAction);
		drawingButtonGroup.add(circleButton);
		toolBar.add(circleButton);

		JToggleButton filledCircleButton = new JToggleButton(filledCircleAction);
		drawingButtonGroup.add(filledCircleButton);
		toolBar.add(filledCircleButton);
		
		// so the Line tool is initially chosen without the user having to click it
		drawingButtonGroup.setSelected(lineButton.getModel(), true);
		lineAction.actionPerformed(null);
		
		JPanel colorInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(colorInfoPanel, BorderLayout.PAGE_END);
		
		JColorInfoLabel colorInfoLabel = new JColorInfoLabel(fgColorPicker, bgColorPicker);
		colorInfoPanel.add(colorInfoLabel);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.add(splitPane, BorderLayout.CENTER);
		
		drawingCanvas = new JDrawingCanvas(this, drawingModel);
		splitPane.add(drawingCanvas);
		
		JList<GeometricalObject> objectList = new JList<>(new DrawingObjectListModel(drawingModel));
		objectList.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_DELETE) {
					return;
				}
				
				GeometricalObject selected = objectList.getSelectedValue();
				if (selected == null) {
					return;
				}
				
				drawingModel.remove(selected);			
			}

		});
		splitPane.add(objectList);
		splitPane.setDividerLocation((int) (getWidth() * 0.75));
		splitPane.setResizeWeight(1);
		
		addListListeners(objectList);
		
	}
	
	/**
	 * Helper method for adding listeners relevant to the list of currently
	 * drawn geometrical objects.
	 * 
	 * @param objectList list of currently drawn objects
	 */
	private void addListListeners(JList<GeometricalObject> objectList) {
		objectList.addMouseListener(new MouseAdapter() {
			
			Timer timer = new Timer();
			boolean clickedOnce;
			GeometricalObject previouslySel;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (clickedOnce) {
					clickedOnce = false;
					GeometricalObject selected = objectList.getSelectedValue();
					
					if (selected == null || selected != previouslySel) {
						previouslySel = null;
						return;
					}
					
					JObjectEditor editor = new JObjectEditor(drawingCanvas);
					selected.accept(editor);
					JOptionPane.showMessageDialog(
						JVDraw.this,
						editor,
						"Edit "+selected.toString(),
						JOptionPane.PLAIN_MESSAGE
					);
				}
				
				clickedOnce = true;
				previouslySel = objectList.getSelectedValue();
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						SwingUtilities.invokeLater(() -> clickedOnce = false);
						
					}
				}, 500L
				);
			}
			
		});
		
	}
	
	@Override
	public GeometricalObject getObject(Point p) {
		return objectFactory.apply(p);
	}

	/**
	 * Action representing the Line drawing tool.
	 */
	private Action lineAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
			putValue(NAME, "Line");
			putValue(SHORT_DESCRIPTION, "Creates a line with the chosen foreground color");

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			objectFactory = p -> new Line(p, p, fgColorPicker.getCurrentColor());
		}
		
	};
	
	/**
	 * Action representing the circle drawing tool.
	 */
	private Action circleAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			putValue(NAME, "Circle");
			putValue(SHORT_DESCRIPTION, "Creates a circle with the chosen foreground color");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			objectFactory = p -> new Circle(p, 0, fgColorPicker.getCurrentColor());
		}
		
	};
	
	/**
	 * Action representing the FilledCircle drawing tool.
	 */
	private Action filledCircleAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
			putValue(NAME, "FilledCircle");
			putValue(SHORT_DESCRIPTION, "Creates a circle with the chosen foreground color and background color fill");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			objectFactory = p -> new FilledCircle(p, 0, fgColorPicker.getCurrentColor(), bgColorPicker.getCurrentColor());
		}
		
	};
	
	/**
	 * Action representing the open canvas function.
	 */
	private Action openAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
			putValue(NAME, "Open...");
			putValue(SHORT_DESCRIPTION, "Opens existing canvas");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Path savePath;
			
			while (true) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(null, "jvd"));
				fc.setDialogTitle("Open JVD file");
				
				if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				savePath = fc.getSelectedFile().toPath();
				if (!savePath.toString().endsWith(".jvd")) {
					JOptionPane.showMessageDialog(
						JVDraw.this,
						"Selected file isn't a JVD file!",
						"Error!",
						JOptionPane.ERROR_MESSAGE
					);
					continue;
				}
				
				break;				
			}
			
			if (!drawingCanvas.isUpdated()) {
				int selected = JOptionPane.showConfirmDialog(
					JVDraw.this,
					"There are unsaved changes! Save?",
					"Unsaved changes",
					JOptionPane.YES_NO_CANCEL_OPTION
				);
				
				if (selected == JOptionPane.CANCEL_OPTION) {
					return;
				}
				
				if (selected == JOptionPane.YES_OPTION) {
					saveAction.actionPerformed(null);
				}
			}
			
			List<String> lines;
			try {
				lines = Files.readAllLines(savePath, StandardCharsets.UTF_8);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JVDraw.this,
					"Could not open "+savePath.toAbsolutePath(),
					"Error!",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			List<GeometricalObject> objects = new ArrayList<>();
			try {
				for (String line : lines) {
					objects.add(ObjectParserUtil.parse(line));
				}
			} catch (RuntimeException ex) {
				JOptionPane.showMessageDialog(
					JVDraw.this,
					"Could not parse "+savePath.toAbsolutePath(),
					"Error!",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			drawingModel.clear();
			
			for (GeometricalObject obj : objects) {
				drawingModel.add(obj);
			}
			
			drawingCanvas.setUpdated(true);
			drawingCanvas.setSavePath(savePath);
			setTitle("JVDraw - "+ savePath.toAbsolutePath().toString());
		}
		
	};
	
	/**
	 * Action representing the save canvas function..
	 */
	private Action saveAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
			putValue(NAME, "Save");
			putValue(SHORT_DESCRIPTION, "Saves the canvas");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (drawingCanvas.isUpdated()) {
				return;
			}
			
			if (!drawingCanvas.isSavedOnDisk()) {
				saveAsAction.actionPerformed(null);
				return;
			}
			
			Path savePath = drawingCanvas.getSavePath();
			ObjectSaver os = new ObjectSaver();
			for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
				drawingModel.getObject(i).accept(os);
			}
			
			byte[] data = os.computeStringData().getBytes(StandardCharsets.UTF_8);
			
			try {
				Files.write(savePath, data);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JVDraw.this,
					"Could not save to "+savePath.toAbsolutePath(),
					"Error!",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			drawingCanvas.setSavePath(savePath);
			drawingCanvas.setUpdated(true);
		}
		
	};
	
	/**
	 * Action representing the save canvas as new file option.
	 */
	private Action saveAsAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
			putValue(NAME, "Save As...");
			putValue(SHORT_DESCRIPTION, "Saves the canvas to a new file");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Path savePath;
			
			while (true) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(null, "jvd"));
				fc.setDialogTitle("Save as JVD file");
				
				if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				savePath = fc.getSelectedFile().toPath();
				if (!savePath.toString().endsWith(".jvd")) {
					savePath = Paths.get(savePath.toString()+".jvd");
				}
				
				if (Files.notExists(savePath)) {
					break;
				}
				
				int selected = JOptionPane.showConfirmDialog(
					JVDraw.this,
					"The specified file already exists! Overwrite?",
					"Overwrite?",
					JOptionPane.YES_NO_OPTION
				);
				
				if (selected == JOptionPane.NO_OPTION) {
					continue;
				}
				
				break;
			}
			
			ObjectSaver os = new ObjectSaver();
			for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
				drawingModel.getObject(i).accept(os);
			}
			
			byte[] data = os.computeStringData().getBytes(StandardCharsets.UTF_8);
			
			try {
				Files.write(savePath, data);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JVDraw.this,
					"Could not save to "+savePath.toAbsolutePath(),
					"Error!",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			drawingCanvas.setSavePath(savePath);
			drawingCanvas.setUpdated(true);
			setTitle("JVDraw - "+savePath.toAbsolutePath().toString());
			
		}
		
	};
	
	/**
	 * Action representing the export canvas as image function.
	 */
	private Action exportAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
			putValue(NAME, "Export...");
			putValue(SHORT_DESCRIPTION, "Exports the canvas as an image file");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Path savePath;
			
			String extension = (String) JOptionPane.showInputDialog(
				JVDraw.this,
				"Please select the desired output format.",
				"Output format", 
				JOptionPane.PLAIN_MESSAGE,
				null,
				new String[] {"jpg", "png", "gif"},
				"png"
			);
			
			
			while (true) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(null, extension));
				fc.setDialogTitle("Export image");
				
				if (fc.showSaveDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				savePath = fc.getSelectedFile().toPath();
				if (!savePath.toString().endsWith("."+extension)) {
					savePath = Paths.get(savePath.toString()+"."+extension);
				}
				
				if (Files.notExists(savePath)) {
					break;
				}
				
				int selected = JOptionPane.showConfirmDialog(
					JVDraw.this,
					"The specified file already exists! Overwrite?",
					"Overwrite?",
					JOptionPane.YES_NO_OPTION
				);
				
				if (selected == JOptionPane.NO_OPTION) {
					continue;
				}
				
				break;
			}
			
			List<GeometricalObject> objects = new ArrayList<>();
			for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
				objects.add(drawingModel.getObject(i));
			}
			
			Rectangle boundingBox = BoundingBoxShifter.computeBoundingBox(objects);
			
			BufferedImage image = new BufferedImage(
				boundingBox.width,
				boundingBox.height,
				BufferedImage.TYPE_3BYTE_BGR
			);
			Graphics2D g2d = image.createGraphics();
			
			BoundingBoxShifter bbs = new BoundingBoxShifter(boundingBox, g2d);
			
			for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
				drawingModel.getObject(i).accept(bbs);
			}
			g2d.dispose();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			try {
				ImageIO.write(image, extension, bos);
				byte[] data = bos.toByteArray();
				Files.write(savePath, data);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JVDraw.this,
					"Could not export image to "+savePath.toAbsolutePath(),
					"Error!",
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
		}
		
	};
	
	/**
	 * Action representing the exit application functionality.
	 */
	private Action exitAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Exits the application");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!drawingCanvas.isUpdated()) {
				int selected = JOptionPane.showConfirmDialog(
					JVDraw.this,
					"There are unsaved changes! Save?",
					"Unsaved changes",
					JOptionPane.YES_NO_CANCEL_OPTION
				);
				
				if (selected == JOptionPane.CANCEL_OPTION) {
					return;
				}
				
				if (selected == JOptionPane.YES_OPTION) {
					saveAction.actionPerformed(null);
				}
			}
			
			System.exit(1);
		}
		
	};


	/**
	 * Starting point of the program,
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw().setVisible(true);
		});
	}

}
