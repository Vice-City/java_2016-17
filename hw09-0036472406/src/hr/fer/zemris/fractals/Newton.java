package hr.fer.zemris.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * A program for calculating and rendering fractals derived
 * from the Newton-Raphson iteration. The user is asked to input
 * roots of a complex polynomial.
 * 
 * <p>Program is terminated by writing {@code done} after roots
 * have been input.
 * 
 * <p>Roots may be written in the form of {@code a+ib} or {@code a-ib}. 
 * Either a or b may be dropped, but not both, except when i is given by
 * itself, in which case b is 1. The imaginary component must
 * always come after the real component if both are present.
 * 
 * <p>Examples of valid input:
 * <pre>
 * 1 + i2
 * i
 * 1
 * 0 + 0i
 * -256.1 + i32.8
 * </pre>
 * 
 * @author Vice Ivušić
 *
 */
public class Newton {

	/**
	 * Starting point of the program.
	 * 
	 * @param args array of input arguments; not used
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.print("Please enter at least two roots, one root per line. ");
		System.out.println("Enter 'done' when done.");
		
		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();
		while (true) {
			System.out.printf("Root %d: ", roots.size()+1);
			String token = sc.nextLine().trim();
			
			if (token.equals("done")) {
				if (roots.size() < 2) {
					System.out.println("Must input at least two roots!");
					continue;
				}
				
				System.out.printf("Image of fractal will appear shortly. Thank you.%n%n");
				break;
			}
			
			try {
				roots.add(Complex.parse(token));
			} catch (NumberFormatException ex) {
				System.out.println(ex.getMessage());
				continue;
			}
		}
		sc.close();
		
		Complex[] rootsArray = new Complex[roots.size()];
		ComplexRootedPolynomial poly = new ComplexRootedPolynomial(roots.toArray(rootsArray));
		
		FractalViewer.show(new NewtonProducer(poly));
	}
	
	/**
	 * Helper class which implements the Callable<Void> interface. This
	 * class represents a single 'job' involved in the fractal calculating
	 * process and it calculates all the pixels for a given range of
	 * yMin to yMax, and all the accompanying x pixels.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class NewtonJob implements Callable<Void> {
		/** width of the screen */
		private int width;
		/** height of the screen */
		private int height;
		/** smallest y to calculate for */
		private int yMin;
		/** largest y to calculate for */
		private int yMax;
		
		/** smallest real component to account for */
		private double reMin;
		/** biggest real component to account for */
		private double reMax;
		/** smallest imaginary component to account for */
		private double imMin;
		/** largest imaginary component to account for */
		private double imMax;
		
		/** array to fill with appropriate indexes */
		private short[] data;
		
		/** polynomial the fractal is being calculated for */
		private ComplexRootedPolynomial poly;
		/** convergence threshold */
		private double convergenceThreshold;
		/** root threshold */
		private double rootThreshold;
		
		/** maximum number of iterations to go through */
		private static final int MAX_ITER = 16*16*16;
		
		/** 
		 * Constructs a new NewtonJob with the specified parameters.
		 * 
		 * @param width width of the screen
		 * @param height height of the screen
		 * @param yMin smallest y to calculate for
		 * @param yMax largest y to calculate for
		 * @param reMin smallest real component to account for
		 * @param reMax largest real component to account for
		 * @param imMin smallest imaginary component to account for
		 * @param imMax largest imaginary component to account for
		 * @param data array to fill with appropriate indexes
		 * @param poly polynomial the fractal is being calculated for
		 * @param convergenceThreshold convergence threshold
		 * @param rootThreshold root threshold
		 */
		private NewtonJob(
				int width, int height, int yMin, int yMax,
				double reMin, double reMax, double imMin, double imMax,
				short[] data, ComplexRootedPolynomial poly,
				double convergenceThreshold, double rootThreshold) {
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.data = data;
			this.poly = poly;
			this.convergenceThreshold = convergenceThreshold;
			this.rootThreshold = rootThreshold;
		}

		@Override
		public Void call() throws Exception {
			ComplexPolynomial polynomial = poly.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			int offset = width * yMin;
			
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Complex zn = mapToComplexPlane(x, y);
					
					int iter = 0;
					double module;
					
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						Complex zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						
						zn = zn1;	
						iter++;
					} while (module > convergenceThreshold && iter < MAX_ITER);
					
					int index = poly.indexOfClosestRootFor(zn, rootThreshold);
					data[offset++] = index == -1  ?  0  :  (short) index;
				}
			}
			
			return null;
		}

		/**
		 * Helper method for mapping the specified screen coordinates to the
		 * complex plane.
		 * 
		 * @param x x screen coordinate
		 * @param y y screen coordinate
		 * @return Complex plane mapping for specified coordinates
		 */
		private Complex mapToComplexPlane(int x, int y) {
			double cRe = x / (width-1.0) * (reMax - reMin) + reMin;
			double cIm = (height-1.0-y) / (height-1.0) * (imMax - imMin) + imMin;
			
			return new Complex(cRe, cIm);
		}
	}
	
	/**
	 * Implementation of the IFractalProducer interface for the Newton fractal.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class NewtonProducer implements IFractalProducer {

		/** polynomial the fractal is being calculated for */
		private ComplexRootedPolynomial poly;
		
		/** ExecutorService pool for current producer */
		private ExecutorService pool =
			 Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors(), 
				job -> {
					Thread t = new Thread(job);
					t.setDaemon(true);
					return t;
				}
			)
		;
		
		/**
		 * Creates a new NewtonProducer for the specified polynomial.
		 * 
		 * @param poly polynomial for which to build the fractal
		 */
		private NewtonProducer(ComplexRootedPolynomial poly) {
			this.poly = poly;
		}
					
		@Override
		public void produce(
				double reMin, double reMax, 
				double imMin, double imMax, 
				int width, int height,
				long requestNo, IFractalResultObserver observer) {
			
			short data[] = new short[width * height];
			final int trackNumber = 8 * Runtime.getRuntime().availableProcessors();
			int yPerTrack = height / trackNumber;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for (int i = 0; i < trackNumber; i++) {
				int yMin = i*yPerTrack;
				int yMax = (i+1)*yPerTrack-1;
				
				if (i == trackNumber-1) {
					yMax = height-1;
				}
				
				NewtonJob job = new NewtonJob(
					width, height, yMin, yMax, 
					reMin, reMax, imMin, imMax, 
					data, poly, 0.002, 0.001
				);
				
				results.add(pool.submit(job));
			}
			
			for(Future<Void> job : results) {
				while (!job.isDone()) {
					try {
						job.get();
					} catch (InterruptedException | ExecutionException ex) {
						continue;
					}
				}
			}
			
			// I am not calling pool.shutdown() so that the screen can be resized
			
			observer.acceptResult(data, (short) (poly.toComplexPolynom().order()+1), requestNo);
		}
		
	}
}
