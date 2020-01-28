package hr.fer.zemris.java.hw01;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Program koji računa opseg i površinu pravokutnika
 * čija se širina i duljina zadaju preko naredbenog retka.
 * 
 * @author Vice
 *
 */
public class Rectangle {

	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti naredbenog retka
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args.length != 2) {
				System.out.println("Program se mora pokrenuti "
						+ "ili bez argumenata ili s točno dva argumenta!");
				return;
			}
			
			//koriste se Scanneri kako bi se uvažile lokalizacijske postavke računala
			double width;
			double length;
			Scanner sc1 = new Scanner(args[0]);
			Scanner sc2 = new Scanner(args[1]);
			try {
				width = sc1.nextDouble();
				length = sc2.nextDouble();
			} catch (InputMismatchException ex) {
				System.out.println("Barem jedan od predanih argumenata nije broj.");
				return;
			} finally {
				sc1.close();
				sc2.close();
			}
						
			if (width < 0.0f || length < 0.0f) {
				System.out.println("Predana je barem jedna negativna vrijednost.");
				return;
			}
			
			String info = generateInfo(width, length);
			System.out.println(info);
			return;
		}

		Scanner sc = new Scanner(System.in);
		double width = inputParameter("Unesite širinu > ", sc);
		double length = inputParameter("Unesite visinu > ", sc);
		
		String info = generateInfo(width, length);
		System.out.println(info);
		
		sc.close();
	}

	/**
	 * Vraća ne-negativni decimalni broj primljen preko naredbenog retka.
	 * 
	 * @param input informativni <code>String</code> koji se ispisuje na ekran
	 * @param sc <code>Scanner</code> objekt za upis podatka iz naredbenog retka
	 * @return <code>double</code> primljen preko naredbenog retka
	 */
	private static double inputParameter(String input, Scanner sc) {
		while (true) {
			System.out.print(input);
			
			double parameter;
			try {
				parameter = sc.nextDouble();
			} catch (InputMismatchException ex) {
				System.out.printf("'%s' se ne može protumačiti kao broj.%n", sc.next());
				continue;
			}
			
			if (parameter < 0.0f) {
				System.out.println("Unijeli ste negativnu vrijednost.");
				continue;
			}
			
			return parameter;
		}
	}

	/**
	 * Vraća podatke o pravokutniku kao <code>String</code>.
	 * Podatci uključuju širinu, duljinu, opseg i površinu pravokutnika.
	 * 
	 * @param width <code>double</code> širina pravokutnika
	 * @param length <code>double</code> duljina pravokutnika
	 * @return <code>String</code> s podacima o pravokutniku
	 */
	private static String generateInfo(double width, double length) {
		double perimeter = calculatePerimeter(width, length);
		double area = calculateArea(width, length);
		
		return String.format("Pravokutnik širine %.1f i visine %.1f ima opseg %.1f te površinu %.1f", 
					width, length, perimeter, area);
	}

	/**
	 * Vraća opseg pravokutnika. Za negativne vrijednosti parametara vraća -1.
	 * 
	 * @param width <code>double</code> širina pravokutnika
	 * @param length <code>double</code> duljina pravokutnika
	 * @return <code>double</code> opseg pravokutnika 
	 */
	public static double calculatePerimeter(double width, double length) {
		if (width < 0.0d || length < 0.0d) {
			return -1.0d;
		}
		return 2 * width + 2 * length;
	}
	
	/**
	 * Vraća površinu pravokutnika. Za negativne vrijednosti parametara vraća -1.
	 * 
	 * @param width <code>double</code> širina pravokutnika
	 * @param length <code>double</code> duljina pravokutnika
	 * @return <code>double</code> površina pravokutnika
	 */
	public static double calculateArea(double width, double length) {
		if (width < 0.0d || length < 0.0d) {
			return -1.0d;
		}
		return width * length;
	}

}
