package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;

/**
 * A simple text editing application. JNotepad++ offers basic data
 * data management utility, such as creating new documents, editing
 * documents and saving documents, as well as basic text editing
 * features such as changing selected text's case. Among the more
 * out-of-the-box features are its sorting line functions, which take
 * locale into account, and its unique lines function which removes
 * non-unique lines.
 * 
 * <p>The display language, along with locale, can be set through its
 * {@code Language} submenu. The program offers English, Croatian and
 * German as its display languages. Please note that German translations
 * are provided courtesy of Google Translate, so don't expect pristine
 * grammar. The default display language is English.
 * 
 * <p>All documents are loaded and saved using the UTF-8 character set.
 * 
 * @author Vice Ivušić
 *
 */
public class JNotepadPP extends JFrame {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	/** localization object to be used by components for setting display text */
	private FormLocalizationProvider flp = new FormLocalizationProvider(
		LocalizationProvider.getInstance(),
		this
	);
	
	/** the main container containing the tabbedPane and status bar */
	private JPanel mainContentPanel;
	/** the main component containing {@link JTextAreaPP} components */
	private JTabbedPane tabbedPane;
	
	/** the status bar shown at the bottom of the frame */
	private JPanel statusBar;
	/** displays currently opened document's length, i.e. amount of characters */
	private LocalizableJLabel lengthLabel = new LocalizableJLabel("lengthLabel", flp);
	/** displays currently opened document's caret position in relation to which line it resides in */
	private LocalizableJLabel lineLabel = new LocalizableJLabel("lineLabel", flp);
	/** displays currently opened document's caret position in relation to which column it resides in */
	private LocalizableJLabel columnLabel = new LocalizableJLabel("columnLabel", flp);
	/** displays currently opened document's amount of selected characters */
	private LocalizableJLabel selectedLabel = new LocalizableJLabel("selectedLabel", flp);
	/** displays current time */
	private JLabel timeLabel = new JLabel();
	
	/** flag which, when set, signals to time-keeping threads to terminate themselves */
	private boolean killTimeThread;

	/** keeps track of how many times the user has created a new file; used for naming purposes */
	private int numberOfCreatedFiles;
	
	/** keeps the last selected and copied/cut String for pasting functionality */
	private String copyBuffer;
	
	/** 
	 * flag indicating if the last selected option when trying to
	 * close an unsaved document was JOptionPane.CANCEL_OPTION,
	 * for purposes of aborting the exit application process
	 */
	private boolean lastSelectedOptionWasCancel;
	
	/** image icon used to indicate a document has unsaved changes */
	private ImageIcon unsavedIcon = loadIcon("icons/unsaved.png");
	/** image icon used to indicate a document doesn't have any unsaved changes */
	private ImageIcon savedIcon = loadIcon("icons/saved.png");

	/**
	 * Creates a new JNotepadPP window.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(750, 500);
		setLocationRelativeTo(null);
		setWindowTitle(null);
		
		// makes sure the user is prompted to save any unsaved documents
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitApplicationAction.actionPerformed(null);
			}
		});
		
		initGUI();
	}

	/**
	 * Initializes the program's graphical user interface.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		/*
		 * The tabbedPane and status bar will be going in here, so the
		 * toolbar is dockable on all sides of the main JFrame container.
		 */
		mainContentPanel = new JPanel(new BorderLayout());
		cp.add(mainContentPanel, BorderLayout.CENTER);
		
		initMenus();
		initToolBar();
		initTabbedPane();
		initStatusBar();
	}

	/**
	 * Initializies the GUI's menu bar.
	 */
	private void initMenus() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu(menuFileAction);
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(newFileAction));
		fileMenu.add(new JMenuItem(openFileAction));
		fileMenu.addSeparator();
		
		fileMenu.add(new JMenuItem(saveFileAction));
		fileMenu.add(new JMenuItem(saveFileAsAction));
		fileMenu.addSeparator();
		
		fileMenu.add(new JMenuItem(closeFileAction));
		fileMenu.add(new JMenuItem(exitApplicationAction));
		
		JMenu editMenu = new JMenu(menuEditAction);
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem(copyAction));
		editMenu.add(new JMenuItem(cutAction));
		editMenu.add(new JMenuItem(pasteAction));
		editMenu.addSeparator();
		
		JMenu changeCaseMenu = new JMenu(menuChangeCaseAction);
		editMenu.add(changeCaseMenu);
		
		changeCaseMenu.add(new JMenuItem(upperCaseAction));
		changeCaseMenu.add(new JMenuItem(lowerCaseAction));
		changeCaseMenu.add(new JMenuItem(invertCaseAction));
		editMenu.addSeparator();
		
		JMenu sortMenu = new JMenu(menuSortAction);
		editMenu.add(sortMenu);
		
		sortMenu.add(new JMenuItem(sortDescendingAction));
		sortMenu.add(new JMenuItem(sortAscendingAction));
		
		editMenu.add(new JMenuItem(uniqueAction));
		
		JMenu viewMenu = new JMenu(menuViewAction);
		menuBar.add(viewMenu);
		
		viewMenu.add(statsAction);
		JMenu languageMenu = new JMenu(menuLanguagesAction);
		viewMenu.add(languageMenu);
		
		//                              ( ͡° ͜ʖ ͡°)
		languageMenu.add(new JMenuItem(englishAction));
		languageMenu.add(new JMenuItem(croatianAction));
		languageMenu.add(new JMenuItem(germanAction));
	}
	
	/**
	 * Initializes the GUI's toolbar.
	 */
	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
		
		toolBar.add(new JButton(newFileAction));
		toolBar.add(new JButton(openFileAction));
		toolBar.add(new JButton(saveFileAction));
		toolBar.add(new JButton(saveFileAsAction));
		toolBar.add(new JButton(closeFileAction));
		toolBar.addSeparator();
		
		toolBar.add(new JButton(copyAction));
		toolBar.add(new JButton(cutAction));
		toolBar.add(new JButton(pasteAction));
		toolBar.addSeparator();
		
		toolBar.add(new JButton(upperCaseAction));
		toolBar.add(new JButton(lowerCaseAction));
		toolBar.add(new JButton(invertCaseAction));
		toolBar.addSeparator();
		
		toolBar.add(new JButton(sortDescendingAction));
		toolBar.add(new JButton(sortAscendingAction));
		toolBar.add(new JButton(uniqueAction));
	
	}

	/**
	 * Initializes the GUI's main editing panel, where all documents are displayed.
	 */
	private void initTabbedPane() {
		tabbedPane = new JTabbedPane();
		mainContentPanel.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// many actions are disabled if no document is open
				if (tabbedPane.getTabCount() == 0) {
					saveFileAction.setEnabled(false);
					saveFileAsAction.setEnabled(false);
					closeFileAction.setEnabled(false);
					
					statsAction.setEnabled(false);
					
					copyAction.setEnabled(false);
					cutAction.setEnabled(false);
					pasteAction.setEnabled(false);
					
					upperCaseAction.setEnabled(false);
					lowerCaseAction.setEnabled(false);
					invertCaseAction.setEnabled(false);
					
					sortAscendingAction.setEnabled(false);
					sortDescendingAction.setEnabled(false);
					uniqueAction.setEnabled(false);
					
					lengthLabel.setText(flp.getString("lengthLabel"));
					lineLabel.setText(flp.getString("lineLabel"));
					columnLabel.setText(flp.getString("columnLabel"));
					selectedLabel.setText(flp.getString("selectedLabel"));
					
					setWindowTitle(null);
					return;
				} 
				
				saveFileAsAction.setEnabled(true);
				closeFileAction.setEnabled(true);
				statsAction.setEnabled(true);
				
				if (copyBuffer != null) {
					pasteAction.setEnabled(true);
				}
				
				JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
				if (textArea.isUpdated()) {
					saveFileAction.setEnabled(false);
				} else {
					saveFileAction.setEnabled(true);
				}
				
				if (textArea.isSavedOnDisk()) {
					setWindowTitle(textArea.getSavePath().toString());
				} else {
					setWindowTitle(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
				}
				
				lengthLabel.setText(String.format(
					"%s:%d",
					flp.getString("lengthLabel"),
					textArea.getDocument().getLength()
				));		
				
				updateStatusBarRightLabels();
			}
		});
	}
	
	/**
	 * Initializes the GUI's status bar which displays helpful information and the current time.
	 */
	private void initStatusBar() {
		// the combination of JPanels and layouts is a bit complicated to ensure correct alignment
		statusBar = new JPanel(new GridLayout(1, 2));
		mainContentPanel.add(statusBar, BorderLayout.PAGE_END);
		
		JPanel leftInfoPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftInfoPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		statusBar.add(leftInfoPane);
		leftInfoPane.add(lengthLabel);
		
		JPanel rightInfoPane = new JPanel(new GridLayout(1, 2));
		rightInfoPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		statusBar.add(rightInfoPane);
		
		JPanel rightInfoPaneLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rightInfoPane.add(rightInfoPaneLeft);
		rightInfoPaneLeft.add(lineLabel);
		rightInfoPaneLeft.add(columnLabel);
		rightInfoPaneLeft.add(selectedLabel);
		
		JPanel rightInfoPaneRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightInfoPane.add(rightInfoPaneRight);
		rightInfoPaneRight.add(timeLabel);
		
		Thread timeThread = new Thread(() -> {
			final long UPDATE_TIME = 1000L;
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			while (!killTimeThread) {
				SwingUtilities.invokeLater(() -> {
					timeLabel.setText(sdf.format(new Date()));
				});
				
				try {
					Thread.sleep(UPDATE_TIME);
				} catch (InterruptedException ignorable) {}
			}
		});
		timeThread.setDaemon(true);
		timeThread.start();
	}
	
	/**
	 * Models the File menu bar menu.
	 */
	private Action menuFileAction = new LocalizableAction("menuFile", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
	};

	/**
	 * Helper method for adding a DocumentListener for the specified text area's
	 * document model. This DocumentListener is used for updating the save state,
	 * save icon and length information for the document.
	 * 
	 * @param textArea text area to add a DocumentListener to
	 */
	private void addDocumentListener(JTextAreaPP textArea) {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			private void update() {
				textArea.setUpdated(false);
				saveFileAction.setEnabled(true);
				
				int currentIndex = tabbedPane.getSelectedIndex();
				
				if (currentIndex == -1 || getTextAreaAt(currentIndex).isUpdated()) {
					return;
				}
				
				tabbedPane.setIconAt(currentIndex, unsavedIcon);
				
				lengthLabel.setText(String.format(
					"%s:%d",
					flp.getString("lengthLabel"),
					textArea.getDocument().getLength()
				));
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}
		});
	}

	/**
	 * Helper method for adding a CaretListener to the specified text area.
	 * This CaretListener is used for enabling and disabling the available
	 * actions at any given point, and for updating the status bar's information.
	 * 
	 * @param textArea text area to add a CaretListener to
	 */
	private void addCaretListener(JTextAreaPP textArea) {
		textArea.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				int selectedLength = Math.abs(e.getDot() - e.getMark());
				if (selectedLength == 0) {
					cutAction.setEnabled(false);
					copyAction.setEnabled(false);
					
					lowerCaseAction.setEnabled(false);
					upperCaseAction.setEnabled(false);
					invertCaseAction.setEnabled(false);
					
				} else {
					cutAction.setEnabled(true);
					copyAction.setEnabled(true);
					
					lowerCaseAction.setEnabled(true);
					upperCaseAction.setEnabled(true);
					invertCaseAction.setEnabled(true);
				}
				
				sortAscendingAction.setEnabled(true);
				sortDescendingAction.setEnabled(true);
				uniqueAction.setEnabled(true);
				
				updateStatusBarRightLabels();
			}
		});
	}

	/**
	 * Models the functionality of creating a new document.
	 */
	private Action newFileAction = new LocalizableAction("new", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = new JTextAreaPP();
			
			remapCopyCutPasteActions(textArea);
			
			addDocumentListener(textArea);
			
			tabbedPane.insertTab(
				flp.getString("emptyName")+(++numberOfCreatedFiles),
				unsavedIcon,
				new JScrollPane(textArea),
				flp.getString("emptyName")+numberOfCreatedFiles, 
				tabbedPane.getSelectedIndex()+1
			);
			
			addCaretListener(textArea);
			
			//ensures that the newly created document's tab gets focused
			if (tabbedPane.getTabCount() > 1) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);
			}
			
		}
	};
	
	/**
	 * Models the functionality of opening an existing document.
	 */
	private Action openFileAction = new LocalizableAction("open", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("openName"));
			
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			Path filePath = fc.getSelectedFile().toPath();
			
			for (int i = 0, n = tabbedPane.getTabCount(); i < n; i++) {
				JTextAreaPP currentTextArea = getTextAreaAt(i);
				
				if (filePath.toAbsolutePath().equals(currentTextArea.getSavePath())) {
					tabbedPane.setSelectedIndex(i);
					return;
				}
			}
			
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(
					JNotepadPP.this,
					flp.getString("fileOpenErrorDesc")+" "+filePath+".",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			byte[] data = null;
			try {
				data = Files.readAllBytes(filePath);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JNotepadPP.this,
					flp.getString("fileReadErrorDesc")+" "+filePath+".",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			String text = new String(data, StandardCharsets.UTF_8);
			JTextAreaPP textArea = new JTextAreaPP();

			remapCopyCutPasteActions(textArea);
			
			addDocumentListener(textArea);
			
			textArea.setText(text);
			textArea.setSavedPath(filePath.toAbsolutePath());
			textArea.setUpdated(true);
			
			tabbedPane.insertTab(
				filePath.getFileName().toString(),
				savedIcon,
				new JScrollPane(textArea),
				filePath.toAbsolutePath().toString(), 
				tabbedPane.getSelectedIndex()+1
			);
			
			addCaretListener(textArea);
			
			if (tabbedPane.getTabCount() > 1) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);
			}
		}
	};
	
	/**
	 * Helper method for remapping the default JTextArea's copy, cut and
	 * paste actions to this program's own implementations, for the specified
	 * text area.
	 * 
	 * @param textArea text area to remap actions for
	 */
	private void remapCopyCutPasteActions(JTextAreaPP textArea) {
		/*
		 * This may seem unneeded, but without it there was a case of 
		 * inconsistent behavior. For some reason, even though I've
		 * defined the shortcut keys for my own actions to the conventional
		 * ones (Ctrl-C/X/P), when using the shortcuts within the program,
		 * my own implementation of these actions would not be used.
		 * 
		 * This led to a possible situation where the user, e.g., uses Ctrl-C
		 * to copy a piece of text, but then uses the Paste function by
		 * selecting it from either the menu or the toolbar; the text pasted
		 * would not be the text the user previously copied, since my own
		 * implementation uses a different copying buffer. Because of this,
		 * I hard code my own actions into opened text area.
		 */
		ActionMap actionMap = textArea.getActionMap();
		actionMap.put(DefaultEditorKit.copyAction, copyAction);
		actionMap.put(DefaultEditorKit.cutAction, cutAction);
		actionMap.put(DefaultEditorKit.pasteAction, pasteAction);
	}

	/**
	 * Models the functionality of saving a document.
	 */
	private Action saveFileAction = new LocalizableAction("save", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			
			if (textArea.isUpdated()) {
				return;
			}
			
			// if the document hasn't been saved even once, prompt the user to save it somewhere
			if (!textArea.isSavedOnDisk()) {
				saveFileAsAction.actionPerformed(null);
				return;
			}
			
			Path savePath = textArea.getSavePath();
			byte[] data = textArea.getText().getBytes(StandardCharsets.UTF_8);
			
			try {
				Files.write(savePath, data);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JNotepadPP.this,
					flp.getString("saveErrorDesc")+" "+savePath+".",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
				);
				textArea.setSavedPath(null);
				return;
			}
			
			updateAfterSaving(textArea, savePath);
		}
	};
	
	/**
	 * Models the functionality of explicitly saving a document as another document.
	 */
	private Action saveFileAsAction = new LocalizableAction("saveAs", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Path savePath;
			
			outer:
			while (true) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(flp.getString("saveName"));
				
				if (fc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				savePath = fc.getSelectedFile().toPath();
				
				if (!Files.exists(savePath)) {
					break;
				}
				
				/*
				 * This for-loop goes through all the currently opened documents
				 * and determines whether the user wants to overwrite a document
				 * which is currently opened in the editor. I have disabled this
				 * behavior (except if the document to overwrite *is* the document
				 * being saved), since it could lead to cases where the editor
				 * has multiple opened documents, all of which are saved as the 
				 * same file. I didn't like this inconsistent behavior (which
				 * document represents the real state on disk?) so I disabled it
				 * altogether.
				 */
				for (int i = 0, n = tabbedPane.getTabCount(); i < n; i++) {
					if (i == tabbedPane.getSelectedIndex()) {
						continue;
					}
					
					JTextAreaPP currentTextArea = getTextAreaAt(i);
					
					if (savePath.toAbsolutePath().equals(currentTextArea.getSavePath())) {
						JOptionPane.showMessageDialog(
							JNotepadPP.this,
							flp.getString("saveAsExistsInWorkspace"),
							flp.getString("error"),
							JOptionPane.ERROR_MESSAGE
						);
						continue outer;
					}
				}
				
				int selected = JOptionPane.showConfirmDialog(
					JNotepadPP.this,
					flp.getString("saveAsExists"),
					flp.getString("saveName"),
					JOptionPane.YES_NO_OPTION
				);
				
				if (selected == JOptionPane.NO_OPTION) {
					continue;
				}
				
				break;
			}
			
			// by now I know that the selected document can be opened
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			byte[] data = textArea.getText().getBytes(StandardCharsets.UTF_8);

			try {
				Files.write(savePath, data);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(
					JNotepadPP.this,
					flp.getString("saveErrorDesc")+" "+savePath+".",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			updateAfterSaving(textArea, savePath);
		}
	};
	
	/**
	 * Helper method for updating save-state relevant information for the
	 * specified text area.
	 * 
	 * @param textArea text area to update
	 * @param savePath path to where the document was saved
	 */
	private void updateAfterSaving (JTextAreaPP textArea, Path savePath) {
		textArea.setSavedPath(savePath);
		textArea.setUpdated(true);
		
		int currentIndex = tabbedPane.getSelectedIndex();
		tabbedPane.setTitleAt(currentIndex, savePath.getFileName().toString());
		tabbedPane.setToolTipTextAt(currentIndex, savePath.toAbsolutePath().toString());
		tabbedPane.setIconAt(currentIndex, savedIcon);
		setWindowTitle(savePath.toAbsolutePath().toString());
		
		saveFileAction.setEnabled(false);
	}

	/**
	 * Models the functionality of closing the currently displayed document.
	 */
	private Action closeFileAction = new LocalizableAction("close", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F4"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			
			/*
			 * If there are unsaved changes, prompt the user to save them.
			 * The user's choice sets off the lastSelectedOptionWasCancel flag, so that
			 * the exitAction action can abort its exiting process if the user
			 * decides to cancel saving any one of the currently unsaved
			 * documents.
			 */
			if (!textArea.isUpdated()) {
				int selected = JOptionPane.showConfirmDialog(
					JNotepadPP.this,
					flp.getString("unsavedChangesDesc"),
					flp.getString("unsavedChanges")+" "+tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),
					JOptionPane.YES_NO_CANCEL_OPTION
				);
				
				if (selected == JOptionPane.CANCEL_OPTION) {
					lastSelectedOptionWasCancel = true;
					return;
				}
				
				if (selected == JOptionPane.YES_OPTION) {
					saveFileAction.actionPerformed(null);
				} 
				
				lastSelectedOptionWasCancel = false;
			}
			
			tabbedPane.remove(tabbedPane.getSelectedIndex());
		}
	};
	
	/**
	 * Models the functionality of exiting the application.
	 */
	private Action exitApplicationAction = new LocalizableAction("exit", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = tabbedPane.getTabCount()-1; i >= 0; i--) {
				tabbedPane.setSelectedIndex(i);
				closeFileAction.actionPerformed(null);
				
				if (lastSelectedOptionWasCancel) {
					return;
				}
			}
			
			killTimeThread = true;
			JNotepadPP.this.dispose();
		}
	};
	
	/**
	 * Models the Edit menu bar menu.
	 */
	private Action menuEditAction = new LocalizableAction("menuEdit", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
	};

	/**
	 * Models the functionality of copying selected text.
	 */
	private Action copyAction = new LocalizableAction("copy", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			Document doc = textArea.getDocument();
			
			int dot = textArea.getCaret().getDot();
			int mark = textArea.getCaret().getMark();
			
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			try {
				copyBuffer = doc.getText(offset, length);
				pasteAction.setEnabled(true);
			} catch (BadLocationException ignorable) {}
			
			// so the selected text stays selected
			textArea.getCaret().setDot(mark);
			textArea.getCaret().moveDot(dot);
		}
	};
	
	/**
	 * Models the functionality of cutting selected text.
	 */
	private Action cutAction = new LocalizableAction("cut", flp) {
		
		private static final long serialVersionUID = 1L;
	
		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			Document doc = textArea.getDocument();
			
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			try {
				copyBuffer = doc.getText(offset, length);
				doc.remove(offset, length);
				pasteAction.setEnabled(true);
			} catch (BadLocationException ignorable) { }
		}
	};

	/**
	 * Models the functionality of pasting previously copied/cut text.
	 */
	private Action pasteAction = new LocalizableAction("paste", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			Document doc = textArea.getDocument();
			
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			try {
				doc.remove(offset, length);
				doc.insertString(offset, copyBuffer, null);
			} catch (BadLocationException ignorable) {}
		}
	};
	
	/**
	 * Models the Change Case menu bar menu.
	 */
	private Action menuChangeCaseAction = new LocalizableAction("menuChangeCase", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_H);
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
		
	};

	/**
	 * Helper method containing shared logic for the change casing actions.
	 * 
	 * @param consumer Consumer object containing the logic specific to a change case action
	 */
	private void changeCase(Consumer<char[]> consumer) {
		JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
		Document doc = textArea.getDocument();
		
		int dot = textArea.getCaret().getDot();
		int mark = textArea.getCaret().getMark();
		
		int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
		int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
		
		String text = null;
		try {
			text = doc.getText(offset, length);
			
			char[] data = text.toCharArray();
			
			consumer.accept(data);
			
			doc.remove(offset, length);
			doc.insertString(offset, new String(data), null);
		} catch (BadLocationException ignorable) {}		
		
		textArea.getCaret().setDot(mark);
		textArea.getCaret().moveDot(dot);
	
	}
	
	/**
	 * Models the functionality of changing a selected text's casing to uppercase.
	 */
	private Action upperCaseAction = new LocalizableAction("upperCase", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCase(new Consumer<char[]>() {

				@Override
				public void accept(char[] data) {
					for (int i = 0; i < data.length; i++) {
						if (Character.isLowerCase(data[i])) {
							data[i] = Character.toUpperCase(data[i]);
						}
					}					
				}
			});
		}
	};
	
	/**
	 * Models the functionality of changing a selected text's case to lowercase.
	 */
	private Action lowerCaseAction = new LocalizableAction("lowerCase", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCase(new Consumer<char[]>() {

				@Override
				public void accept(char[] data) {
					for (int i = 0; i < data.length; i++) {
						if (Character.isUpperCase(data[i])) {
							data[i] = Character.toLowerCase(data[i]);
						}
					}
				}
				
			});
		}
		
	};

	/**
	 * Models the functionality of inverting the selected text's case.
	 */
	private Action invertCaseAction = new LocalizableAction("invertCase", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			changeCase(new Consumer<char[]>() {

				@Override
				public void accept(char[] data) {
					for (int i = 0; i < data.length; i++) {
						if (Character.isUpperCase(data[i])) {
							data[i] = Character.toLowerCase(data[i]);
						} else if (Character.isLowerCase(data[i])) {
							data[i] = Character.toUpperCase(data[i]);
						}
					}
				}
				
			});
		}
		
	};

	/**
	 * Models the Sort menu bar menu.
	 */
	private Action menuSortAction = new LocalizableAction("menuSort", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
	};

	/**
	 * Comparator implementing locale-sensitive string comparing logic.
	 */
	private Comparator<String> localeComparator = new Comparator<String>() {
		
		@Override
		public int compare(String str1, String str2) {
			Locale locale = new Locale(LocalizationProvider.getInstance().getCurrentLanguage());
			Collator collator = Collator.getInstance(locale);
			
			return collator.compare(str1, str2);
		}
	};

	/**
	 * Helper method containing shared logic for the sorting actions.
	 *  
	 * @param comparator comparator to use to sort lines
	 */
	private void sort(Comparator<String> comparator) {
		JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
		Document doc = textArea.getDocument();
		
		int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
		int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
		
		List<String> lines = new ArrayList<>();
		try {
			int startLine = textArea.getLineOfOffset(offset);
			int endLine = textArea.getLineOfOffset(offset+length);
			
			int startOffset = textArea.getLineStartOffset(startLine);
			int endOffset = textArea.getLineEndOffset(endLine);
			
			length = endOffset-startOffset;
			
			int lastLine = textArea.getLineOfOffset(doc.getLength());
			
			for (int i = startLine; i <= endLine; i++) {
				int ioff = textArea.getLineStartOffset(startLine);
				int ilen = textArea.getLineEndOffset(startLine) - ioff;
				
				String currentLine = doc.getText(ioff, ilen);
				doc.remove(ioff, ilen);
				
				lines.add(currentLine.replaceAll("\n", ""));
			}
			
			Collections.sort(lines, comparator);
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0, n = lines.size(); i < n; i++) {
				if (i == n-1 && endLine == lastLine) {
					sb.append(lines.get(i));
					continue;
				}
				sb.append(lines.get(i)+"\n");
				
			}
	
			doc.insertString(startOffset, sb.toString(), null);
			
			endOffset = endLine == lastLine ? endOffset : endOffset-1;
			
			textArea.getCaret().setDot(startOffset);
			textArea.getCaret().moveDot(endOffset);
		} catch (BadLocationException ignorable) {}
	
	}

	/**
	 * Models the functionality of sorting lines in descending order.
	 */
	private Action sortDescendingAction = new LocalizableAction("sortDescending", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift D"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
			
			setEnabled(false);
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			sort(localeComparator);
		}
	};
	
	/**
	 * Models the functionality of sorting lines in ascending order.
	 */
	private Action sortAscendingAction = new LocalizableAction("sortAscending", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift A"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
			
			setEnabled(false);
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			sort(Collections.reverseOrder(localeComparator));
		}
	};

	/**
	 * Models the functionality of removing non-unique lines from selected lines.
	 */
	private Action uniqueAction = new LocalizableAction("unique", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift Q"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
			
			setEnabled(false);
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
			Document doc = textArea.getDocument();
			
			int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
			int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
			
			LinkedHashSet<String> set = new LinkedHashSet<>();
			try {
				int startLine = textArea.getLineOfOffset(offset);
				int endLine = textArea.getLineOfOffset(offset+length);
				
				int startOffset = textArea.getLineStartOffset(startLine);
				int endOffset = textArea.getLineEndOffset(endLine);
				
				length = endOffset-startOffset;
				
				int lastLine = textArea.getLineOfOffset(doc.getLength());
				
				for (int i = startLine; i <= endLine; i++) {
					int ioff = textArea.getLineStartOffset(startLine);
					int ilen = textArea.getLineEndOffset(startLine) - ioff;
					
					String currentLine = doc.getText(ioff, ilen);
					doc.remove(ioff, ilen);
					
					set.add(currentLine.replaceAll("\n", ""));
					
				}
				
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String str : set) {
					if (i == set.size()-1 && endLine == lastLine) {
						sb.append(str);
						continue;
					}
					sb.append(str+"\n");
					i++;
				}
				
				doc.insertString(startOffset, sb.toString(), null);
				
				endOffset = endLine == lastLine ? endOffset : endOffset-1;
				
				textArea.getCaret().setDot(startOffset);
				textArea.getCaret().moveDot(endOffset);
				
			} catch (BadLocationException ignorable) {}
			
		}
	};

	/**
	 * Models the View menu bar menu.
	 */
	private Action menuViewAction = new LocalizableAction("menuView", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
	};

	/**
	 * Models the functionality of calculating and presenting the currently 
	 * displayed document's statistics, including the document's length,
	 * non-blank character count and line count.
	 */
	private Action statsAction = new LocalizableAction("stats", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
			
			setEnabled(false);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JTextAreaPP.Statistics stats = getTextAreaAt(tabbedPane.getSelectedIndex()).getStatistics();
			
			String info = String.format(
				"%s %d %s %d %s %d %s",
				flp.getString("stats1"),
				stats.getLength(),
				flp.getString("stats2"),
				stats.getNonWhitespaceChars(),
				flp.getString("stats3"),
				stats.getNumberOfLines(),
				flp.getString("stats4")
			);
			
			JOptionPane.showMessageDialog(
				JNotepadPP.this,
				info,
				flp.getString("statsName"),
				JOptionPane.INFORMATION_MESSAGE
			);
		}
	};
	
	/**
	 * Models the Languages menu bar menu.
	 */
	private Action menuLanguagesAction = new LocalizableAction("menuLanguages", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			return;
		}
	};

	/**
	 * Models the functionality of changing the program's display language to English.
	 */
	private Action englishAction = new LocalizableAction("english", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt shift E"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
		
	};
	
	/**
	 * Models the functionality of changing the program's display language to Croatian.
	 */
	private Action croatianAction = new LocalizableAction("croatian", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt shift H"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
		
	};
	
	/**
	 * Models the functionality of changing the program's display language to German.
	 */
	private Action germanAction = new LocalizableAction("german", flp) {
		
		private static final long serialVersionUID = 1L;

		{
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt shift D"));
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
		
	};
	
	/**
	 * Helper method for loading an icon resource into memory.
	 * 
	 * @param relativePathName relative path to the resource
	 * @return ImageIcon object constructed from the resource
	 */
	private ImageIcon loadIcon(String relativePathName) {
		byte[] data = null;
		
		try (InputStream is = this.getClass().getResourceAsStream(relativePathName);
			 ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			
			byte[] buffer = new byte[4096];
			int read;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			
			data = os.toByteArray();
			
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(
				JNotepadPP.this,
				flp.getString("errorLoadResource"),
				flp.getString("error"),
				JOptionPane.ERROR_MESSAGE
			);
			exitApplicationAction.actionPerformed(null);
		}
		
		return new ImageIcon(data);
	}

	/**
	 * Helper method for updating the Line, Column and Selected labels
	 * of the program's status bar based on current state.
	 */
	private void updateStatusBarRightLabels() {
		JTextAreaPP textArea = getTextAreaAt(tabbedPane.getSelectedIndex());
		
		int length = Math.abs(textArea.getCaret().getDot() - textArea.getCaret().getMark());
		int offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
		
		try {
			int line = textArea.getLineOfOffset(offset);
			lineLabel.setText(String.format(
				"%s:%d", 
				flp.getString("lineLabel"),
				line
			));
			
			columnLabel.setText(String.format(
				"%s:%d", 
				flp.getString("columnLabel"),
				offset - textArea.getLineStartOffset(line)
			));
			
			selectedLabel.setText(String.format(
				"%s:%d", 
				flp.getString("selectedLabel"),
				length
			));
			
		} catch (BadLocationException ignorable) {}
	}
	
	/**
	 * Helper method for setting the program's title bar to the specified title.
	 * Sets the program's title to "JNotepad++" if the specified title is null.
	 * 
	 * @param title title for the program, or null if "JNotepad++" is desired as the title
	 */
	private void setWindowTitle(String title) {
		if (title == null) {
			setTitle("JNotepad++");
			return;
		}
		
		setTitle(title + " - JNotepad++");
	}

	/**
	 * Helper method for retrieving the text area located in the tab with the
	 * specified index. Useful since each JTextAreaPP document is additionally
	 * wrapped inside a JScrollPane. Expects the user to always pass a valid
	 * index, i.e. between 0 and tabCount-1.
	 * 
	 * @param index index of the tab containing the requested text area
	 * @return JTextAreaPP text area contained in the tab with the specified index
	 */
	private JTextAreaPP getTextAreaAt(int index) {
		JScrollPane scrollPane = (JScrollPane) tabbedPane.getComponentAt(index);
		return (JTextAreaPP) scrollPane.getViewport().getView();
	}
	
	/**
	 * Starting point of the program,
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

}
