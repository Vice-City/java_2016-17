package hr.fer.zemris.java.raytracer.model;

import hr.fer.zemris.math.Util;

/**
 * Represents a sphere modeled after the GraphicalObject class.
 * Each sphere knows how to find the intersection closest to
 * a given ray through its findClosestRayIntersection instance method.
 * 
 * @author Vice Ivušić
 *
 */
public class Sphere extends GraphicalObject {

	/** sphere center */
	private Point3D center;
	/** sphere radius */
	private double radius;
	
	/** coefficient for red diffuse component */
	private double kdr;
	/** coefficient for green diffuse component */
	private double kdg;
	/** coefficient for blue diffuse component */
	private double kdb;
	
	/** coefficient for red reflective component */
	private double krr;
	/** coefficient for green reflective component */
	private double krg;
	/** coefficient for blue reflective component */
	private double krb;
	
	/** coefficient for n reflective component */
	private double krn;
	
	/**
	 * Creates a new Sphere with the specified parameters. Assumes
	 * the user will pass on only valid values (no null values,
	 * positive radius, and coefficients between 0.0 and 1.0) and
	 * gives no warnings otherwise.
	 * 
	 * @param center sphere center
	 * @param radius sphere radius
	 * @param kdr coefficient for red diffuse component
	 * @param kdg coefficient for green diffuse component
	 * @param kdb coefficient for blue diffuse component
	 * @param krr coefficient for red reflective component
	 * @param krg coefficient for green reflective component
	 * @param krb coefficient for blue reflective component
	 * @param krn coefficient for n reflective component
	 */
	public Sphere(
			Point3D center, double radius, 
			double kdr, double kdg, double kdb, 
			double krr, double krg,	double krb, double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		if (ray == null) return null;
		
		/*
		 * IRG book page 44, chapter 2.7.2 (2016-03-02)
		 * C = center of sphere
		 * Ts = viewer's point of view
		 * d = normalized vector, pointing from Ts to C
		 * 
		 * breaking naming conventions on purpose so it's easier
		 * to check against the book!
		 */
		Point3D C = center;
		Point3D Ts = ray.start;
		Point3D d = ray.direction;
		
		// a is always 1
		double b = d.scalarMultiply(2).scalarProduct(Ts.sub(C));
		double c = Ts.sub(C).scalarProduct(Ts.sub(C)) - radius*radius;
		
		double discriminant = b*b - 4*c;
		// negative discriminant => no intersections!
		if (Util.compareDoubles(discriminant, 0) < 0) {
			return null;
		}
		
		double lambda1 = (-b + Math.sqrt(discriminant))/2;
		double lambda2 = (-b - Math.sqrt(discriminant))/2;
		
		// if lambda1 is greater than lambda2, switch them up
		lambda1 = Util.compareDoubles(lambda1, lambda2) > 0 ? lambda2 : lambda1;
		
		// formula in chapter 2.7
		// get intersection point by plugging in the appropriate lambda
		Point3D tLambda = Ts.add(d.scalarMultiply(lambda1));
		
		return new SphereIntersection(
			tLambda, Ts.sub(tLambda).norm(), true
		);
	}
	
	/**
	 * Represents a ray intersection for rays intersecting spheres.
	 * Each ray intersection knows how to calculate its normal and
	 * knows its lighting coefficients.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private class SphereIntersection extends RayIntersection {

		/**
		 * Creates a new SphereIntersection from the specified parameters.
		 * Assumes the user will pass on valid values (no null values,
		 * and positive distance) and gives no warnings otherwise.
		 * 
		 * @param point point of intersection between ray and sphere
		 * @param distance distance between start of ray and intersection
		 * @param outer true iff the intersection is outer, i.e. if the
		 * 			    ray is passing from outside the sphere to inside the sphere
		 */
		protected SphereIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public Point3D getNormal() {
			// sphere normal is IntersectionPoint - Center
			return super.getPoint().sub(center).normalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
		
	}
	
}
