package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A program for calculating and rendering a predefined scene
 * containing two light sources (red and blue), two large foreground
 * spheres and lots of smaller background spheres.
 * Takes no input.
 * 
 * <p>This program employs parallelization, unlike RayCaster.
 * 
 * @author Vice Ivušić
 *
 */
public class RayCasterParallel {

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(
				new RayTracerProducer(),
				new Point3D(10, 0, 0),
				new Point3D(0, 0, 0),
				new Point3D(0, 0, 10),
				20, 20
			);	
	}
	
	/**
	 * Models a recursive job which calculates the given scene.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class RayTracerJob extends RecursiveAction {

		/** default serial version UID */
		private static final long serialVersionUID = 1L;
		
		/** horizontal dimension */
		private double horizontal;
		/** vertical dimension */
		private double vertical;
		/** screen width */
		private int width;
		/** screen height */
		private int height;
		
		/** smallest y to calculate for */
		private int yMin;
		/** largest y to calculate for */
		private int yMax;
		
		/** vector describing the x axis */
		private Point3D xAxis;
		/** vector describing the y axis */
		private Point3D yAxis;
		/** point located at upper-left screen corner */
		private Point3D screenCorner;
		/** point where the eye view is located */
		private Point3D eye;
		/** scene containing all the objects */
		private Scene scene;
		
		/** array of red color components */
		private short[] red;
		 /** array of red color components */
		private short[] green;
		/** array of red color components */
		private short[] blue;
		
		/** maximum threshold for recursively splitting jobs */
		private static final int THRESHOLD = 16;
		
		/**
		 * Creates a new RayTracerJob from the specified parameters.
		 * 
		 * @param horizontal horizontal dimension
		 * @param vertical vertical dimension
		 * @param width width of the screen
		 * @param height height of the screen
		 * @param yMin smallest y to calculate for
		 * @param yMax largest y to calculate for
		 * @param xAxis vector describing the x axis
		 * @param yAxis vector describing the y axis
		 * @param screenCorner point located at upper-left screen corner
		 * @param eye point where the eye view is located
		 * @param scene scene containing all the objects
		 * @param red array of red color components
		 * @param green array of green color components
		 * @param blue array of blue color components
		 */
		private RayTracerJob(
				double horizontal, double vertical, int width, int height,
				int yMin, int yMax,
				Point3D xAxis, Point3D yAxis, Point3D screenCorner, Point3D eye, 
				Scene scene, short[] red, short[] green, short[] blue) {
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.eye = eye;
			this.scene = scene;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		protected void compute() {
			if(yMax-yMin+1 <= THRESHOLD) {
				computeDirect();
				return;
			}
			
			invokeAll(
				new RayTracerJob(
					horizontal, vertical, width, height,
					yMin, yMin+(yMax-yMin)/2,
					xAxis, yAxis, screenCorner, eye,
					scene, red, green, blue),
				new RayTracerJob(
					horizontal, vertical, width, height,
					yMin+(yMax-yMin)/2+1, yMax,
					xAxis, yAxis, screenCorner, eye,
					scene, red, green, blue)
			);
		}
		
		/**
		 * Helper method which directly computes a job; it doesn't split it further.
		 */
		private void computeDirect() {
			RayCaster.calculate(
				horizontal, vertical, width, height,
				yMin, yMax,
				xAxis, yAxis, screenCorner, eye,
				scene, red, green, blue
			);
		}
	}
	
	/**
	 * Implementation of the IRayTracerProducer interface which employs parallelization.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class RayTracerProducer implements IRayTracerProducer {

		/** ForkJoinPool for current producer */
		private ForkJoinPool pool = new ForkJoinPool();
		
		@Override
		public void produce(
				Point3D eye, Point3D view, Point3D viewUp,
				double horizontal, double vertical, int width, int height,
				long requestNo, IRayTracerResultObserver observer) {
			System.out.println("Započinjem izračune...");
			short[] red = new short[width*height];
			short[] green = new short[width*height];
			short[] blue = new short[width*height];
			
			Point3D zAxis = view.sub(eye).normalize();
			Point3D yAxis = viewUp.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp))).normalize();
			Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
			
			Point3D screenCorner = 
				view
				.sub(xAxis.scalarMultiply(horizontal/2))
				.add(yAxis.scalarMultiply(vertical/2))
			;
			
			Scene scene = RayTracerViewer.createPredefinedScene();
			
			pool.invoke(new RayTracerJob(
				horizontal, vertical, width, height,
				0, height-1, 
				xAxis, yAxis, screenCorner, eye, 
				scene, red, green, blue
			));
			
			// I am not calling pool.shutdown() so that the screen can be resized
			
			System.out.println("Izračuni gotovi...");
			observer.acceptResult(red, green, blue, requestNo);
			System.out.println("Dojava gotova...");
		}
		
	}
}
