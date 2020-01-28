package hr.fer.zemris.java.custom.collections.demo;

import java.util.Scanner;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program koji prima matematički izraz u postfiks notaciji te računa i
 * ispisuje rezultat. 
 * 
 * Program prima <b>samo jedan</b> argument pri pozivu programa. Argument
 * mora biti aritmetički izraz omeđen dvostrukim navodnicima. Primjer
 * korektnog argumenta: <code>"-1 8 2 / +"</code>. Operandi i operatori 
 * moraju biti odvojeni barem jednim razmakom. Podržani su samo cijeli
 * brojevi te sljedeće operacije: zbrajanje (+), oduzimanje (-), 
 * množenje (*), dijeljenje (/) te operacija modulo (%). Rezultati operacije
 * uvijek se zaokružuju na najmanji sljedeći cijeli broj, npr. 5/2 = 2.
 * 
 * Ako se program pokrene s manje ili više od jednog argumenta, ispisuje
 * se odgovarajuća poruka te se prekida rad programa. Program se jednako
 * ponaša u slučaju da je argument neispravnog formata ili kad bi izvođenjem
 * navedenih operacija došlo do nedozvoljene radnje, poput dijeljenja s nulom.
 * 
 * @author Vice Ivušić
 *
 */
public class StackDemo {

	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args polje ulaznih argumenata
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Program prima točno jedan argument!");
			return;
		}

		ObjectStack stack = new ObjectStack();

		Scanner sc = new Scanner(args[0]);
		while (sc.hasNext()) {
			if (sc.hasNextInt()) {
				stack.push(Integer.valueOf(sc.nextInt()));
				continue;
			}
			
			if (stack.size() < 2) {
				System.out.println("Unesen je prevelik broj operatora!");
				sc.close();
				return;
			}
			
			Integer second = (Integer) stack.pop();
			Integer first = (Integer) stack.pop();
			
			switch (sc.next()) {
			case "+":
				stack.push(first + second);
				break;
				
			case "-":
				stack.push(first - second);
				break;
				
			case "*":
				stack.push(first * second);
				break;
				
			case "/":
				if (second == 0) {
					System.out.println("Nije dozvoljeno dijeljenje s nulom!");
					sc.close();
					return;
				}
				stack.push(first / second);
				break;
				
			case "%":
				stack.push(first % second);
				break;
				
			default:
				System.out.println("Unesena je nedozvoljena operacija!");
				sc.close();
				return;
			}
		}
		
		sc.close();
		
		if (stack.size() != 1) {
			System.out.println("Unesen je prevelik broj operanada!");
			
		} else {
			System.out.println(stack.pop());
		}

	}
}
