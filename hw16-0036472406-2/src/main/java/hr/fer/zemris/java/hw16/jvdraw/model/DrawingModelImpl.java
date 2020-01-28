package hr.fer.zemris.java.hw16.jvdraw.model;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Implementation of the DrawingModel interface.
 * 
 * @author Vice Ivušić
 *
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {

	/**
	 * List of registered drawing model listeners.
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();
	
	/**
	 * List of currently stored geometrical objects.
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(this);
		
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, objects.size()-1, objects.size()-1);
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if (l == null || listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		if (l == null || !listeners.contains(l)) {
			return;
		}
		
		listeners = new ArrayList<>(listeners);
		listeners.remove(l);
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject obj) {
		int index = objects.indexOf(obj);
		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		
		if (index == -1) {
			return;
		}

		object.removeGeometricalObjectListener(this);
		objects.remove(object);
		
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
		
	}
	
	@Override
	public void clear() {
		int size = objects.size();
		if (size == 0) {
			return;
		}
		
		for (GeometricalObject obj : objects) {
			obj.removeGeometricalObjectListener(this);
		}
		objects.clear();
		
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, 0, size);
		}

	}
	
}
