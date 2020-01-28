package hr.fer.zemris.java.hw01;


import java.util.Scanner;

/**
 * Program koji računa faktorijele brojeva zadanih preko naredbenog retka.
 * 
 * @author Vice
 *
 */
public class Factorial {

	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args ne koristi se
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while (true) {
			System.out.print("Unesite broj > ");
			
			String token = sc.next();		
			if (token.equals("kraj")) {
				System.out.println("Doviđenja.");
				break;
			}
			
			int n;
			try {
				n = Integer.parseInt(token);
			} catch (NumberFormatException ex) {
				System.out.printf("'%s' nije cijeli broj.%n", token);
				continue;
			}
			
			if (n < 1 || n > 20) {
				System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", n);
				continue;
			}
			
			long factorial = calculateFactorial(n);	
			System.out.printf("%d! = %d%n", n, factorial);
		}
		
		sc.close();
	}

	/**
	 * Vraća faktorijel predanog broja. Radi samo za interval 
	 * prirodnih brojeva [1, 20]; za brojeve izvan tog intervala vraća -1.
	 * 
	 * @param n broj čiji se faktorijel računa
	 * @return faktorijel predanog broja kao <code>long</code>
	 */
	public static long calculateFactorial(int n) {
		if (n < 1 || n > 20) {
			return -1L;
		}
		if (n == 1) {
			return 1L;
		}
		return n * calculateFactorial(n-1);
	}

}
