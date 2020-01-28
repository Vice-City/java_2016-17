package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;
import hr.fer.zemris.math.Util;

/**
 * A program for calculating and rendering a predefined scene
 * containing two light sources (red and blue), two large foreground
 * spheres and lots of smaller background spheres.
 * Takes no input.
 * 
 * <p>Also contains a static method for calculating the color values
 * for a given scene and given set of parameters.
 * 
 * @author Vice Ivušić
 *
 */
public class RayCaster {

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
	 * Calculates and fills the specified red, green and blue
	 * arrays with the respective color values for each pixel
	 * containing yMin to yMax, for the specified scene and other
	 * parameters. Assumes the user will pass on valid arguments
	 * (no null values, no negative values) and gives no warnings
	 * otherwise.
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
	public static void calculate(
			double horizontal, double vertical, int width, int height, 
			int yMin, int yMax,
			Point3D xAxis, Point3D yAxis, Point3D screenCorner, Point3D eye,
			Scene scene, short[] red, short[] green, short[] blue) {
		short[] rgb = new short[3];
		int offset = width * yMin;
		
		for(int y = yMin; y <= yMax; y++) {
			for(int x = 0; x < width; x++) {
				Point3D screenPoint = 
					screenCorner
					.add(xAxis.scalarMultiply(x/(width-1.0)*horizontal)
					.sub(yAxis.scalarMultiply(y/(height-1.0)*vertical)))
				;
				Ray eyeToScreen = Ray.fromPoints(eye, screenPoint);
				
				tracer(scene, eyeToScreen, rgb);
				
				red[offset] = rgb[0] > 255 ? 255 : rgb[0];
				green[offset] = rgb[1] > 255 ? 255 : rgb[1];
				blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
				
				offset++;
			}
		}
	}

	/**
	 * Helper method for calculating the color values for the specified
	 * ray for the given scene.
	 * 
	 * @param scene scene containing all the objects
	 * @param eyeToScreen ray pointing from the eye view to the screen
	 * @param rgb array of rgb values for current ray
	 */
	private static void tracer(Scene scene, Ray eyeToScreen, short[] rgb) {
		RayIntersection intersection = findClosestIntersection(scene, eyeToScreen);
		
		if (intersection == null) {
			rgb[0] = rgb[1] = rgb[2] = 0;
			return;
		}
		
		determineColorFor(scene, intersection, eyeToScreen, rgb);
	}

	/**
	 * Helper method for finding the closest ray intersection with
	 * any of the objects contained in the given scene for the specified ray.
	 * Returns null if no such intersection exists.
	 * 
	 * @param scene scene containing all the objects
	 * @param ray ray for which to find closest intersection
	 * @return closest ray intersection or null if no intersection exists
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection closestInter = null;
		
		for (GraphicalObject gr : scene.getObjects()) {
			RayIntersection currentInter = gr.findClosestRayIntersection(ray);
			
			if (currentInter == null) {
				continue;
			}
			
			if (closestInter == null) {
				closestInter = currentInter;
			}
			
			double comparison = Util.compareDoubles(
					currentInter.getDistance(), closestInter.getDistance()
			);
			boolean currentInterIsCloser = comparison < 0;
			if (currentInterIsCloser) {
				closestInter = currentInter;
			}
		}
		
		return closestInter;
	}

	/**
	 * Helper method for determining the color values for light sources in
	 * given scene and the specified intersection.
	 * 
	 * @param scene scene containing all the objects
	 * @param viewInter intersection for which to find color values
	 * @param eyeToScreen ray pointing from the eye view to the screen
	 * @param rgb array of rgb values for the current intersection
	 */
	private static void determineColorFor(
			Scene scene,
			RayIntersection viewInter, Ray eyeToScreen,
			short[] rgb) {
		rgb[0] = rgb[1] = rgb[2] = 15;

		for (LightSource ls : scene.getLights()) {
			Ray viewInterToSource = Ray.fromPoints(ls.getPoint(), viewInter.getPoint());
			double viewInterDistToSource = ls.getPoint().sub(viewInter.getPoint()).norm();
			
			RayIntersection sourceInter = findClosestIntersection(scene, viewInterToSource);
			double sourceInterDistToSource = sourceInter.getDistance();
			
			int comparison = Util.compareDoubles(sourceInterDistToSource, viewInterDistToSource);
			boolean objectIsBetweenSourceAndInter = comparison < 0;
			if (objectIsBetweenSourceAndInter) {
				continue;
			}
			
			Point3D interToSource = ls.getPoint().sub(viewInter.getPoint()).modifyNormalize();
			Point3D normal = viewInter.getNormal();
			// IRG book page 22, chapter 2.2 (2016-03-02)
			Point3D reflectedViewToSource = 
				normal
				.scalarMultiply(2 * interToSource.scalarProduct(normal))
				.modifySub(interToSource)
				.modifyNormalize()
			;
			Point3D interToView = 
				eyeToScreen.start
				.sub(viewInter.getPoint())
				.modifyNormalize()
			;
			
			rgb[0] += calculateComponents(
				ls.getR(), viewInter.getKdr(), viewInter.getKrr(), viewInter.getKrn(),
				interToSource, normal, reflectedViewToSource, interToView
			);
			rgb[1] += calculateComponents(
				ls.getG(), viewInter.getKdg(), viewInter.getKrg(), viewInter.getKrn(),
				interToSource, normal, reflectedViewToSource, interToView
			);
			rgb[2] += calculateComponents(
				ls.getB(), viewInter.getKdb(), viewInter.getKrb(), viewInter.getKrn(),
				interToSource, normal, reflectedViewToSource, interToView
			);
			
		}
	}

	/**
	 * Helper method for calculating the diffuse and specular components
	 * for the specified parameters.
	 * 
	 * @param i intensity of one color
	 * @param kd diffuse coefficient of one color
	 * @param ks specular coefficient of one color
	 * @param kn surface coefficient
	 * @param l normalized intersection to light source vector
	 * @param n normalized normal vector for current intersection
	 * @param r normalized reflected l vector
	 * @param v normalized intersection to view vector
	 * @return color value component
	 */
	private static double calculateComponents(
			int i, double kd, double ks, double kn,
			Point3D l, Point3D n, Point3D r, Point3D v) {
		/*
		 * IRG book page 234, chapter 9.2.5 (2016-03-02)
		 * 
		 * i = intensity of one color
		 * kd = diffuse coefficient of one color
		 * ks = specular coefficient of one color
		 * kn = surface coefficient
		 * l = normalized intersection to light source vector
		 * n = normalized normal vector for current intersection
		 * r = normalized reflected l vector
		 * v = normalized intersection to view vector
		 */
		
		double diffuse = diffuseComp(kd, l, n);
		double specular = specularComp(ks, kn, r, v);
		return i * (diffuse + specular);
	}

	/**
	 * Helper method for calculating the diffuse component for
	 * the specified parameters.
	 * 
	 * @param kd diffuse coefficient of one color
	 * @param l normalized intersection to light source vector
	 * @param n normalized normal vector for current intersection
	 * @return diffuse color value component
	 */
	private static double diffuseComp(double kd, Point3D l, Point3D n) {
		double product = Math.max(l.scalarProduct(n), 0);
		return kd * product;
	}

	/**
	 * Helper method for calculating the diffuse component for
	 * the specified parameters.
	 * 
	 * @param ks specular coefficient of one color
	 * @param kn surface coefficient
	 * @param r normalized reflected l vector
	 * @param v normalized intersection to view vector
	 * @return specular color value component
	 */
	private static double specularComp(double ks, double kn, Point3D r, Point3D v) {
		double product = Math.max(r.scalarProduct(v), 0);
		return ks * Math.pow(product, kn);
	}

	/**
	 * Implementation of the IRayTracerProducer interface.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class RayTracerProducer implements IRayTracerProducer {
	
		@Override
		public void produce(
				Point3D eye, Point3D view, Point3D viewUp,
				double horizontal, double vertical, int width, int height,
				long requestNo, IRayTracerResultObserver observer) {
			System.out.println("Započinjem izračune...");
			
			short[] red = new short[width*height];
			short[] green = new short[width*height];
			short[] blue = new short[width*height];
			
			Point3D zAxis = view.sub(eye).modifyNormalize();
			Point3D yAxis = 
				viewUp
				.sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp)))
				.modifyNormalize();
			Point3D xAxis = zAxis.vectorProduct(yAxis).modifyNormalize();
			
			Point3D screenCorner = 
				view
				.sub(xAxis.scalarMultiply(horizontal/2))
				.modifyAdd(yAxis.scalarMultiply(vertical/2))
			;
			
			Scene scene = RayTracerViewer.createPredefinedScene();
	
			calculate(
				horizontal, vertical, width, height,
				0, height-1,
				xAxis, yAxis, screenCorner, eye, 
				scene, red, green, blue
			);
	
			System.out.println("Izračuni gotovi...");
			observer.acceptResult(red, green, blue, requestNo);
			System.out.println("Dojava gotova...");
		}
		
	}
}
