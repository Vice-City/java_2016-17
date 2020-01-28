package hr.fer.zemris.java.custom.collections;

/**
 * Predstavlja stog koji dopušta pohranu svih mogućih objekata.
 * <p>
 * Nudi metode za: stavljanje elemenata na vrh stoga; dohvat
 * te skidanje elemenata s vrha stoga; informacije o veličini
 * stoga te metodu za brisanje svih elemenata stoga.
 * <p>
 * Sve operacije izvode se u O(1) složenosti, osim povremeno
 * kod dodavanja elementa na vrh stoga kad se izvodi u O(n) složenosti.
 * 
 * @author Vice Ivušić
 *
 */
public class ObjectStack {

	/**
	 * ArrayIndexedCollection koji se koristi za pohranu elemenata stoga.
	 */
	private ArrayIndexedCollection stack;
	
	/**
	 * Stvara novi prazni <code>ObjectStack</code> stog.
	 */
	public ObjectStack() {
		stack = new ArrayIndexedCollection();
	}
	
	/**
	 * Vraća <b>true</b> ako na stogu postoji barem
	 * jedan element, <b>false</b> inače.
	 * 
	 * @return <b>true</b> ako i samo ako na stogu postoji
	 * 		   barem jedan element
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Vraća broj elemenata trenutno na stogu, tj. veličinu stoga.
	 * 
	 * @return broj elemenata na stogu
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Stavlja navedeni element na vrh stoga.
	 * 
	 * @param value element koji se želi staviti na stog
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public void push(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("predana je null referenca za value");
		}
		
		stack.add(value);
	}
	
	/**
	 * Vraća element koji se nalazi na vrhu stoga.
	 * Ova metoda <b>ne mijenja</b> sadržaj stoga.
	 * 
	 * @return element koji se nalazi na vrhu stoga
	 * @throws EmptyStackException ako je metoda pozvana nad praznim stogom
	 */
	public Object peek() {
		if (isEmpty()) {
			throw new EmptyStackException("pozvana metoda peek() nad praznim stogom");
		}
		
		return stack.get(size()-1);
	}
	
	/**
	 * Skida i vraća element koji se nalazi na vrhu stoga.
	 * 
	 * @return element koji je skinut s vrha stoga
	 * @throws EmptyStackException ako je metoda pozvana nad praznim stogom
	 */
	public Object pop() {
		if (isEmpty()) {
			throw new EmptyStackException("pozvana metoda pop() nad praznim stogom");
		}
		
		Object element = peek();
		stack.remove(size()-1);
		
		return element;
	}
	
	/**
	 * Briše sadržaj stoga.
	 */
	public void clear() {
		stack.clear();
	}
}
