package hr.fer.zemris.java.custom.collections;

/**
 * Implementacija razreda <code>Collection</code> koja koristi dvostruko 
 * povezanu listu za pohranu elemenata. Dopušta pohranu bilo kakvih objekata. 
 * <b>Null</b> reference ne smiju se pohranjivati. Svaki element ima svoj indeks
 * preko kojega ga je moguće dohvatiti. Dodatno, moguće je dohvatiti
 * element i na temelju njegovog sadržaja. 
 * 
 * Nudi metode za: dodavanje elemenata na kraj kolekcije; dodavanje
 * elemenata na bilo kojem mjestu unutar kolekcije; pretvorbu kolekcije u polje; 
 * brisanje elemenata na temelju indeksa te brisanje elemenata na temelju sadržaja;
 * brisanje svih elemenata iz kolekcije; vraćanje indeksa traženog elementa,
 * te za dodatne informacije o kolekciji, npr. je li prazna i koja joj je veličina,
 * 
 * Dodavanje elemenata na početak i kraj kolekcije izvodi se u O(1) složenosti.
 * Dodavanje elemenata bilo gdje osim na kraj kolekcije izvodi se u 
 * O(n/2+1) složenosti. Brisanje elemenata ima ekvivalentne složenosti kao
 * i dodavanje elemenata. Dohvat elemenata preko indeksa izvodi se u O(n/2+1)
 * složenosti. Dohvat elemenata na temelju sadržaja izvodi se u O(n) složenosti.

 * @author Vice Ivušić
 *
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * Pomoćna struktura koja modelira element dvostruko povezane liste.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class ListNode {
		/**
		 * Referenca na sljedeći čvor liste, ili na null referencu ako se radi o repu.
		 */
		ListNode next;
		
		/**
		 * Referenca na sljedeći čvor liste, ili na null referencu ako se radi o glavi.
		 */
		ListNode previous;
		
		/**
		 * Bilo kakav objekt pohranjen kao element čvora liste.
		 */
		Object value;
	}
	
	/**
	 * Referenca na glavu kolekcije, tj. na prvi čvor u kolekciji.
	 */
	private ListNode first;
	
	/**
	 * Referenca na rep kolekcije, tj. na zadnji čvor u kolekciji.
	 */
	private ListNode last;
	
	/**
	 * Veličina kolekcije, tj. broj pohranjenih elemenata.
	 */
	private int size;
	
	/**
	 * Stvara novi <code>LinkedListIndexedCollection</code> s elementima iz 
	 * druge navedene kolekcija tipa <code>Collection</code>. 
	 * 
	 * @param other kolekcija čiji elementi postaju elementi nove kolekcije
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	public LinkedListIndexedCollection(Collection other) {
		if (other == null) {
			throw new IllegalArgumentException("predana je null referenca za drugu kolekciju");
		}
		
		addAll(other);
	}
	
	/**
	 * Stvara novi prazni <code>LinkedListIndexedCollection</code>.
	 */
	public LinkedListIndexedCollection() {
		super();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Pomoćna metoda koja vraća <code>ListNode</code> element s navedenim indeksom.
	 * 
	 * @param index indeks traženog elementa
	 * @return traženi element
	 * @throws IndexOutOfBoundsException ako je indeks manji
	 * 		   od nule ili veći od broja elemenata unutar kolekcije
	 */
	private ListNode getNode(int index) {
		if (index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("predano je " + index + " za indeks");
		}
		
		ListNode node;
		if (index <= size/2) {
			node = first;
			
			for (int i = 0; i < index; i++) {
				node = node.next;
			}
			
		} else {
			node = last;
			
			for (int i = size-1; i > index; i--) {
				node = node.previous;
			}
		}
		
		return node;
	}
	
	/**
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	@Override
	public void add(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("predana je null referenca za value");
		}
		
		insert(value, size);
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
	
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			array[i] = node.value;
			node = node.next;
		}
		
		return array;
	}
	
	/**
	 * @throws IllegalArgumentException ako je predana null referenca
	 */
	@Override
	public void forEach(Processor processor) {
		if (processor == null) {
			throw new IllegalArgumentException("predana je null referenca za procesor");
		}
		
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			processor.process(node.value);
			node = node.next;
		}/*
		while (node != null) {
			processor.process(node.value);
			node = node.next;
		}*/
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
		
		ListNode node = getNode(index);
		
		return node.value;
	}
	
	@Override
	public void clear() {
		first = last = null;
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
	 * @throws IndexOutOfBoundsException ako je indeks manji
	 * 		   od nule ili veći od broja elemenata unutar kolekcije
	 */
	void insert(Object value, int position) {
		if (value == null) {
			throw new IllegalArgumentException("predana je null referenca za value");
		}
		
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException("predano je " + position + " za mjesto");
		}
		
		ListNode node = new ListNode();
		node.value = value;
		if (size == 0) {
			first = last = node;
			
		} else if (position == size) {
			last.next = node;
			node.previous = last;
			last = node;
			
		} else if (position == 0) {
			first.previous = node;
			node.next = first;
			first = node;
			
		} else {
			ListNode existingNode = getNode(position);
			existingNode.previous.next = node;
			node.previous = existingNode.previous;
			existingNode.previous = node;
			node.next = existingNode;	
		}
		
		size++;
	}
	
	/**
	 * Vraća indeks navedenog elementa unutar kolekcije. Vraća -1 
	 * ako indeks ne postoji.
	 * 
	 * @param value element koji se traži
	 * @return indeks elementa unutar kolekcije, ili -1 ako element ne postoji
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}
		
		ListNode node = first;
		for (int i = 0; i < size; i++) {
			if (node.value.equals(value)) {
				return i;
			}
			node = node.next;
		}
		
		return -1;
	}
	
	/**
	 * Briše element s navedenim indeksom iz kolekcije.
	 * 
	 * @param index indeks elementa koji se želi pobrisati
	 * 
	 * @throws IndexOutOfBoundsException ako je indeks manji
	 * 		   od nule ili veći od broja elemenata unutar kolekcije
	 */
	public void remove(int index) {
		if (index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException("predano je " + index + " za indeks");
		}
		
		if (size == 1) {
			first = last = null;
			
		} else if (index == size-1) {
			last.previous.next = null;
			last = last.previous;
			
		} else if (index == 0) {
			first.next.previous = null;
			first = first.next;
			
		} else {
			ListNode node = getNode(index);
			node.previous.next = node.next;
			node.next.previous = node.previous;
		}
		
		size--;
	}
}
