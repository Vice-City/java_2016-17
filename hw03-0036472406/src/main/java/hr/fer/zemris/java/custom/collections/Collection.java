package hr.fer.zemris.java.custom.collections;

/**
 * Predstavlja kostur općenite kolekcije elemenata tipa Object.
 * Neprikladan za pohranjivanje objekata sâm po sebi.
 * 
 * @author Vice Ivušić
 *
 */
public class Collection {

	/**
	 * Stvara novi <code>Collection</code> bez gotovo ikakve funkcionalnosti.
	 */
	protected Collection() {
		super();
	}
	
	/**
	 * Vraća <b>true</b> ako u kolekciji postoji barem
	 * jedan element, <b>false</b> inače.
	 * 
	 * @return <b>true</b> ako i samo ako u kolekciji postoji
	 * 		   barem jedan element
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Vraća broj elemenata koji se trenutno nalaze
	 * u kolekciji, tj. veličinu kolekcije.
	 * 
	 * @return broj elemenata u kolekciji
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Dodaje element na kraj kolekcije.
	 * 
	 * @param value element koji se želi dodati
	 */
	public void add(Object value) {
		return;
	}
	
	/**
	 * Vraća <b>true</b> ako se navedeni element nalazi unutar kolekcije, 
	 * <b>false</b> inače.
	 * 
	 * @param value traženi element
	 * @return <b>true</b> ako i samo ako navedeni element postoji
	 *		   unutar kolekcije
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Vraća <b>true</b> ako kolekcija izbriše jednu pojavu
	 * navedenog elementa iz kolekcije.
	 * 
	 * @param value element koji se želi izbrisati
	 * @return <b>true</b> ako i samo ako je navedeni element izbrisan iz kolekcije
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Vraća sadržaj kolekcije u obliku polja <b>Object</b> elemenata.
	 * 
	 * @return polje svih elemenata unutar kolekcije
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Poziva metodu <code>process</code> od navedenog <code>Processor</code> objekta
	 * za svaki element kolekcije.
	 * 
	 * @param processor objekt čija se metoda <code>process</code> poziva
	 * 					za svaki element kolekcije
	 */
	public void forEach(Processor processor) {
		return;
	}
	
	/**
	 * Dodaje sve elemente kolekcije <b>other</b> u trenutnu kolekciju.
	 * 
	 * @param other kolekcija čiji se elementi
	 * 		 	    dodaju u trenutnu kolekciju
	 * @throws IllegalArgumentException ako je predana null referenca
	 *		   ili referenca na trenutnu kolekciju
	 */
	public void addAll(Collection other) {
		if (other == null || other == this) {
			throw new IllegalArgumentException();
		}
		
		/**
		 * Predstavlja metodu koja prima bilo kakav objekt i dodaje taj
		 * isti objekt u trenutnu kolekciju.
		 * 
		 * @author Vice Ivušić
		 *
		 */
		class LocalProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Briše sve elemente iz kolekcije.
	 */
	public void clear() {
		return;
	}
}
