package hr.fer.zemris.java.hw02;

import java.util.OptionalDouble;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Predstavlja kompleksni broj sa svojim realnim i imaginarnim dijelom.
 * 
 * Ima metode koje stvaraju novi kompleksni broj samo iz realnog dijela,
 * samo iz imaginarnog dijela, iz modula i kuta kompleksnog broja te iz
 * String-a koji predstavlja kompleksan broj.
 * 
 * Nudi metode za dohvat realnog dijela, dohvat imaginarnog dijela, dohvat
 * modula te dohvat kuta.
 * 
 * Također nudi metode koje rade operaciju nad postojećim kompleksnim brojem te
 * vraćaju rezultat operacije kao novi kompleksan broj. Te metode uključuju
 * metode za zbrajanje, oduzimanje, množenje, dijeljenje, potenciranje
 * te korjenovanje kompleksnih brojeva.
 * 
 * @author Vice Ivušić
 *
 */
public class ComplexNumber {

	/**
	 * Realna komponenta kompleksnog broja.
	 */
	private double real;
	
	/**
	 * Imaginarna komponenta kompleksnog broja.
	 */
	private double imaginary;
	
	/**
	 * Stvara novi kompleksan broj s navedenom realnom i 
	 * imaginarnom komponentom.
	 * 
	 * @param real realna komponenta kompleksnog broja
	 * @param imaginary imaginarna komponenta kompleksnog broja
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Vraća novi kompleksan broj s navedenon realnom komponentom
	 * te imaginarnom komponentom od nula.
	 * 
	 * @param real realna komponenta kompleksnog broja
	 * @return <code>ComplexNumber</code> s navedenom realnom
	 * 		   komponentom i imaginarnom komponentom od nula
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}
	
	/**
	 * Vraća novi kompleksan broj s navedenon imaginarnom komponentom
	 * te realnom komponentom od nula.
	 * 
	 * @param imaginary imaginarna komponenta kompleksnog broja
	 * @return <code>ComplexNumber</code> s navedenon imaginarnom
	 * 		   komponentom te realnom komponentom od nula
	 */
	public static ComplexNumber fromImaginary (double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}
	
	/**
	 * Vraća novi kompleksan broj s realnom i imaginarnom komponentom
	 * koje odgovaraju navedenom modulu i kutu izraženom u radijanima.
	 * 
	 * @param magnitude modul kompleksnog broja
	 * @param angle kut kompleksnog broja izražen u radijanima
	 * @return <code>ComplexNumber</code> s navedenim modulom i kutem u radijanima
	 * @throws IllegalArgumentException ako je navedeni modul manji od nule
	 */
	public static ComplexNumber fromMagnitudeAndAngle (double magnitude, double angle) {
		if (magnitude < 0.0) {
			throw new IllegalArgumentException("predano je " + magnitude + " za modul");
		}
		
		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);
		
		//ovo je da nema gluposti s pozitivnim i negativnim nulama
		if (Math.abs(real) < 10e-6) {
			real = Math.abs(real);
		}
		if (Math.abs(imaginary) < 10e-6) {
			imaginary = Math.abs(imaginary);
		}
		
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Stvara novi kompleksan broj koji odgovara sadržaju navedenog String-a.
	 * 
	 * Broj mora sadržavati barem jednu komponentu, ali ne smije sadržavati više
	 * od jedne realne i više od jedne imaginarne komponente te ne smije
	 * sadržavati praznine. Broj može biti zapisan bez ili sa predznakom. 
	 * Komponente mogu biti zapisane bilo kojim redoslijedom.
	 * 
	 * Primjeri pravilnih uporaba: "-i", "5.5+4i", "9i-1", "4", "i+1.619"
	 * 
	 * @param s String koji se parsira u kompleksan broj
	 * @return <code>ComplexNumber</code> koji odgovara sadržaju navedenog Stringa
	 * @throws IllegalArgumentException ako je predana null referanca
	 * @throws NumberFormatException ako navedeni String nije u pravilnom formatu
	 */
	public static ComplexNumber parse(String s) {
		if (s == null) {
			throw new IllegalArgumentException("predana je null referenca za parsiranje");
		}
		//regularni izraz za *jednu* komponentu kompleksnog broja
		String regex = "\\s*(?<value>[+-]?[0-9]*\\.?[0-9]*)(?<imaginary>i)?\\s*";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		
		OptionalDouble real = OptionalDouble.empty();
		OptionalDouble imaginary = OptionalDouble.empty();
		
		while (!matcher.hitEnd()) {
			matcher.find();
			
			String valueToken = matcher.group("value");
			boolean valueIsImaginary = matcher.group("imaginary") != null;
			
			if (valueIsImaginary) {
				if (imaginary.isPresent()) {
					throw new NumberFormatException();
				}
				
				// Ovo je tako da se "i", "+i" i "-i" dobro parsiraju
				if (valueToken.equals("+") || valueToken.equals("-") || valueToken.equals("")) {
					valueToken += "1";
				}
				
				double value = Double.parseDouble(valueToken);
				imaginary = OptionalDouble.of(value);
				
			} else {
				if (real.isPresent()) {
					throw new NumberFormatException();
				}
				
				double value = Double.parseDouble(valueToken);
				real = OptionalDouble.of(value);	
			} 
		}
		
		return new ComplexNumber(
				real.isPresent() ? real.getAsDouble() : 0.0, 
				imaginary.isPresent() ? imaginary.getAsDouble() : 0.0
		);
	}
	
	/**
	 * Vraća realnu komponentu trenutnog kompleksnog broja.
	 * @return realna komponenta broja
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Vraća imaginarnu komponentu trenutnog kompleksnog broja.
	 * @return imaginarna komponenta broja
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Vraća modul trenitmpg kompleksnog broja.
	 * 
	 * @return modul broja
	 */
	public double getMagnitude() {
		return Math.sqrt(real*real + imaginary*imaginary);
	}
	
	/**
	 * Vraća kut trenutnog kompleksnog broja u radijanima u intervalu (-Pi, Pi].
	 * 
	 * @return kut kompleksnog broja u intervalu (-Pi, Pi]
	 */
	public double getAngle() {
		return Math.atan2(imaginary, real);
	}
	
	/**
	 * Vraća novi kompleksni broj koji je rezultat operacije zbrajanja
	 * između trenutnog i navedenog kompleksnog broja.
	 * 
	 * @param c kompleksni broj s kojim se zbraja trenutni kompleksni broj
	 * @return rezultat operacije zbrajanja
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public ComplexNumber add(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("predana je null referenca za kompleksni broj");
		}
		
		return new ComplexNumber(real + c.real, imaginary + c.imaginary);
	}
	
	/**
	 * Vraća novi kompleksni broj koji je rezultat operacije oduzimanja
	 * između trenutnog i navedenog kompleksnog broja,
	 * 
	 * @param c kompleksni broj s kojim se oduzima trenutni kompleksni broj
	 * @return rezultat operacije oduzimanja
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public ComplexNumber sub(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("predana je null referenca za kompleksni broj");
		}
		
		return new ComplexNumber(real - c.real, imaginary - c.imaginary);
	}
	
	/**
	 * Vraća novi kompleksni broj koji je rezultat operacije množenja
	 * između trenutnog i navedenog kompleksnog broja.
	 * 
	 * @param c kompleksni broj s kojim se množi trenutni kompleksni broj
	 * @return rezultat operacije množenja
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public ComplexNumber mul(ComplexNumber c) {
		if (c == null) {
			throw new IllegalArgumentException("predana je null referenca za kompleksni broj");
		}
		
		return new ComplexNumber(
				this.real*c.real - this.imaginary*c.imaginary,
				this.real*c.imaginary + this.imaginary*c.real
		);
	}
	
	/**
	 * Vraća novi kompleksni broj koji je rezultat operacije dijeljenja
	 * između trenutnog i navedenog kompleksnog broja.
	 * 
	 * @param c kompleksni broj s kojim se dijeli trenutni kompleksni broj
	 * @return rezultat operacije dijeljenja
	 * @throws IllegalArgumentException ako je predana null referenca
	 * 		   ili ako je modul navedenog kompleksnog broja jednak nuli
	 */
	public ComplexNumber div(ComplexNumber c) {
		if (c == null || c.getMagnitude() == 0.0) {
			throw new IllegalArgumentException();
		}
		
		return fromMagnitudeAndAngle(
				this.getMagnitude() / c.getMagnitude(),
				this.getAngle() - c.getAngle()
		);
	}
	
	/**
	 * Vraća novi kompleksni broj koji je potenciran s navedenom potencijom.
	 * 
	 * @param n potencija
	 * @return rezultat operacije potenciranja
	 * @throws IllegalArgumentException ako je navedena potencija manja od nule.
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("predano je " + n + " za potenciranje");
		}
		return fromMagnitudeAndAngle(
				Math.pow(getMagnitude(), n),
				getAngle() * n
		);
	}
	
	/**
	 * Vraća polje kompleksnih brojeva dobivenih korjenovanjem trenutnog broja
	 * s navedenim stupnjem korijena.
	 * 
	 * @param n željeni stupanj korijena
	 * @return polje rezultata operacije korjenovanja
	 * @throws NumberFormatException ako je navedeni stupanj korijena
	 * 		   manji ili jednak nuli.
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("predano je " + n + " za korjenovanje");
		}
		
		ComplexNumber[] roots = new ComplexNumber[n];
		
		for (int k = 0; k < n; k++) {
			roots[k] = fromMagnitudeAndAngle(
						Math.pow(getMagnitude(), (double) 1/n),
						(getAngle() + 2*Math.PI*k) / n
			);
		}
		
		return roots;
	}
	
	
	/**
	 * Vraća <code>String</code> reprezentaciju trenutnog kompleksnog 
	 * broja. Komponente su prikazane na tri decimale.
	 * 
	 * @return <code>String</code> reprezentacija kompleksnog broja
	 */
	@Override
	public String toString() {
		return String.format("Re: %.3f, Im: %.3fi", real, imaginary);
	}
}
