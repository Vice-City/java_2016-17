package hr.fer.zemris.java.custom.collections;

public class Test {

	public static void main(String[] args) {
		
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		col.add("Ana");
		col.add("Bruno");
		col.add("Domagoj");
		col.insert("Cuki", 1);
		for (Object elem : col.toArray()) {
			System.out.println(elem);
		}
		System.out.println();
		col.insert("Esther", 0);
		for (Object elem : col.toArray()) {
			System.out.println(elem);
		}
		
	}
}
