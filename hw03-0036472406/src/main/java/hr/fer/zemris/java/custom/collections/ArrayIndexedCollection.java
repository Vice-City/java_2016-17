package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;

/**
 * Implementacija razreda <code>Collection</code> koja koristi relokatibilno polje
 * za pohranu elemenata. Dopušta pohranu bilo kakvih objekata. <b>Null</b> 
 * reference ne smiju se pohranjivati. Svaki element ima svoj indeks
 * preko kojega ga je moguće dohvatiti. Dodatno, moguće je dohvatiti
 * element i na temelju njegovog sadržaja. 
 * <p>
 * Nudi metode za: dodavanje elemenata na kraj kolekcije; dodavanje
 * elemenata na bilo kojem mjestu unutar kolekcije; pretvorbu kolekcije u polje; 
 * brisanje elemenata na temelju indeksa te brisanje elemenata na temelju sadržaja;
 * brisanje svih elemenata iz kolekcije; vraćanje indeksa traženog elementa,
 * te za dodatne informacije o kolekciji, npr. je li prazna i koja joj je veličina,
 * <p>
 * Kolekcija ima kapacitet koji odgovara veličini unutarnjeg polja koje
 * se koristi za pohranu elemenata; kapacitet je uvijek veći ili jednak 
 * trenutnoj veličini kolekcije. Dodavanje elemenata na kraj kolekcije
 * izvodi se u O(1) složenosti, osim kad se unutarnje polje mora proširiti.
 * Dodavanje elemenata bilo gdje osim na kraj kolekcije izvodi se u 
 * O(n) složenosti. Brisanje elemenata s kraja kolekcije izvodi se u O(1)
 * složenosti, dok se brisanje elemenata s bilo kojeg mjesta osim kraja
 * izvodi u O(n) složenosti. Dohvat elemenata preko indeksa izvodi se u O(1)
 * složenosti. Dohvat elemenata na temelju sadržaja izvodi se u O(n) složenosti.

 * @author Vice Ivušić
 *
 */
public class ArrayIndexedCollection extends Collection {
	
	/**
	 * Pretpostavljeni početni kapacitet unutarnjeg polja za pohranjivanje elemenata.
	 */
	private final static int DEFAULT_CAPACITY = 16;

	/**
	 * Veličina kolekcije, tj. broj elemenata u njoj.
	 */
	private int size;
	
	/**
	 * Kapacitet kolekcije, tj. veličina unutarnjeg polja za pohranjivanje elemenata.
	 * Uvijek veći ili jednak veličini kolekcije.
	 */
	private int capacity;
	
	/**
	 * Unutarnje polje za pohranjivanje elemenata kolekcije. Pohranjuje bilo
	 * kakve objekte.
	 */
	private Object[] elements;
	
	/**
	 * Stvara novi <code>ArrayIndexedCollection</code> s elementima iz 
	 * druge navedene kolekcije tipa <code>Collection</code> te s navedenim početnim kapacitetom.
	 * Kapacitet kolekcije dodatno će se proširiti ako navedeni kapacitet
	 * nije dovoljan za pohranu svih elemenata navedene kolekcije.
	 * 
	 * @param other kolekcija čiji elementi postaju elementi nove kolekcije
	 * @param initialCapacity početni kapacitet kolekcije
	 * @throws IllegalArgumentException ako je početni kapacitet manji od jedan
	 * 		   te ako je predana null referenca za drugu kolekciju
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null) {
			throw new IllegalArgumentException("predana je null referenca za drugu kolekciju");
		}
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("predano je " + initialCapacity + " za početni kapacitet");
		}
		
		capacity = other.size() > initialCapacity ? other.size() : initialCapacity;
		elements = new Object[capacity];
		
		addAll(other);
	}
	
	/**
	 * Stvara novi <code>ArrayIndexedCollection</code> s elementima iz 
	 * druge navedene <code>Collection</code> kolekcije. Kapacitet nove
	 * kolekcije bit će barem 16; ako elementi navedene kolekcije ne bi
	 * stali u takvu kolekciju kapaciteta 16, kapacitet će se proširiti.
	 * 
	 * @param other kolekcija čiji elementi postaju elementi nove kolekcije
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Stvara novi <code>ArrayIndexedCollection</code> s navedenim 
	 * početnim kapacitetom.
	 * 
	 * @param initialCapacity početni kapacitet kolekcije
	 * @throws IllegalArgumentException ako je početni kapacitet manji od jedan
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("predano je " + initialCapacity + " za početni kapacitet");
		}
		
		capacity = initialCapacity;
		elements = new Object[capacity];
	}
	
	/**
	 * Stvara novi <code>ArrayIndexedCollection</code> s kapacitetom od 16.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("predana je null referenca kao argument za value");
		}
		
		insert(value, size);
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	/**
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	@Override
	public void forEach(Processor processor) {
		if (processor == null) {
			throw new IllegalArgumentException("predana je null referenca kao argument za procesor");
		}
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}
	
	@Override
	public boolean contains(Object value) {
		return indexOf(value) == -1 ? false : true;
	}
	
	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		
		if (index == -1) {
			return false;
		} else {
			remove(index);
			return true;
		}
	}
	
	/**
	 * Vraća element s navedenim indeksom iz kolekcije.
	 * 
	 * @param index indeks traženog elementa
	 * @return element s navedenim indeksom
	 * 
	 * @throws IndexOutOfBoundsException ako je indeks manji
	 * 		   od nule ili veći od broja elemenata unutar kolekcije
	 */
	public Object get(int index) {
		if (index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("predano je " + index + " za indeks");
		}
		
		return elements[index];
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}
	
	/**
	 * Dodaje element na navedeno mjesto unutar kolekcije, ispred
	 * elementa koji se prethodno nalazio na tom mjestu.
	 * 
	 * @param value element koji se dodaje u kolekciju
	 * @param position mjesto na kojemu se dodaje element u kolekciju
	 * 
	 * @throws IllegalArgumentException ako je predana <b>null</b> referenca
	 * @throws IndexOutOfBoundsException ako je navedeno mjesto manje
	 * 		   od nule ili veće od broja elemenata unutar kolekcije
	 */
	public void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("predana je null referenca kao argument za value");
		}
		
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException("predano je " + position + " za mjesto");
		}
		
		if (size == capacity) {
			capacity = capacity * 2;
			elements = Arrays.copyOf(elements, capacity);
		}
		
		// kapacitet je do ovog trenutka sigurno veći od veličine => indeksi su sigurni
		if (size > position) {
			for (int i = size-1; i >= position; i--) {
				elements[i+1] = elements[i];
			}
		}
		
		elements[position] = value;
		size++;
		
	}
	
	/**
	 * Vraća indeks navedenog elementa unutar kolekcije. Vraća -1 
	 * ako navedeni element ne postoji unutar kolekcije.
	 * 
	 * @param value element koji se traži
	 * @return indeks elementa unutar kolekcije, ili -1 ako element ne postoji
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}
		
		for (int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Briše element s navedenim indeksom iz kolekcije.
	 * 
	 * @param index indeks elementa koji se želi pobrisati
	 * @throws IndexOutOfBoundsException ako je indeks manji
	 * 		   od nule ili veći od broja elemenata unutar kolekcije
	 */
	public void remove(int index) {
		if (index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("predano je " + index + " za indeks");
		}
		
		//Pazi da indeks i+1 ne pristupa nečemu što ne postoji: i < size-1!
		for (int i = index; i < size-1; i++) {
			elements[i] = elements[i+1];
		}

		//pobriši zadnji element za garbage collection
		elements[size-1] = null; 
		size--;
	}
	
}
