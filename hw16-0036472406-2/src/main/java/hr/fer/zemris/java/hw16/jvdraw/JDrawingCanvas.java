package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.nio.file.Path;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.model.ICanvas;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.ObjectProvider;
import hr.fer.zemris.java.hw16.jvdraw.objects.util.ObjectPainter;

/**
 * A component used for drawing geometrical objects onto its surface. Additionally
 * stores data related to the canvas's state i.e. whether it is updated or stored
 * onto the disk.
 * 
 * @author Vice Ivušić
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener, ICanvas {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to a geometrical object in construction. May be null if no object is being added.
	 */
	private GeometricalObject objectBeingAdded;
	
	/**
	 * Path to where this canvas is saved on the disk. May be null.
	 */
	private Path savePath;
	
	/**
	 * Flag indicating whether any changes have occured since the last time the canvas was saved.
	 */
	private boolean isUpdated = true;
	
	/**
	 * Reference to a DrawingModel object being used to store this canvas's geometrical objects.
	 */
	private DrawingModel drawingModel;
	
	/**
	 * Creates a new JDrawingCanvas using the specified parameters.
	 * 
	 * @param provider an object capable of producing geometrical objects
	 * @param drawingModel reference to a DrawingModel object used to store this canvas's objects
	 */
	public JDrawingCanvas(ObjectProvider provider, DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		
		drawingModel.addDrawingModelListener(this);
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				System.out.println("Mouse clicked at "+e.getPoint());
				if (objectBeingAdded == null) {
					objectBeingAdded = provider.getObject(e.getPoint());
					repaint();
					return;
				}
				
				drawingModel.add(objectBeingAdded);
				objectBeingAdded = null;
			}
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if (objectBeingAdded != null) {
					objectBeingAdded.scaleTo(e.getPoint());
					repaint();
				}
			}
			
		});
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		ObjectPainter painter = new ObjectPainter(g2d);
		
//		g2d.setColor(Color.WHITE);
//		g2d.fillRect(0, 0, getWidth()-1, getHeight()-1);
		
		for (int i = 0, n = drawingModel.getSize(); i < n; i++) {
			GeometricalObject obj = drawingModel.getObject(i);
			obj.accept(painter);
		}
		
		if (objectBeingAdded != null) {
//			System.out.println("rendering object");
			objectBeingAdded.accept(painter);
		}
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		update();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		update();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		update();
	}
	
	/**
	 * Helper method for updating this canvas's state after a change occurs in
	 * the associated drawing model.
	 */
	private void update() {
		isUpdated = false;
		repaint();
	}

	/**
	 * @return true if the no changes have occured since the last time this canvas was saved
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * @param value flag indicating whether or this canvas is up to date
	 */
	public void setUpdated(boolean value) {
		isUpdated = value;
	}

	/**
	 * @return true if this canvas has been saved to disk
	 */
	public boolean isSavedOnDisk() {
		return savePath != null;
	}

	/**
	 * @param savePath the path where this canvas is saved
	 */
	public void setSavePath(Path savePath) {
		this.savePath = savePath;
	}

	/**
	 * @return the path where this canvas is saved
	 */
	public Path getSavePath() {
		return savePath;
	}
	
}
