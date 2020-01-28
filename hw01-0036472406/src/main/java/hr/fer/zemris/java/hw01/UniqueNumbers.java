package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program koji pamti cijele brojeve predane preko naredbenog retka
 * i ispisuje ih sortirane na ekran.
 * 
 * @author Vice
 *
 */
public class UniqueNumbers {

	/**
	 * Pomoćna struktura koja modelira čvor u binarnom stablu.
	 * @author Vice
	 *
	 */
	static class TreeNode {
		TreeNode left;
		TreeNode right;
		int value;
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args ne koristi se
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TreeNode glava = null;
		
		while (true) {
			System.out.print("Unesite broj > ");
			String token = sc.next();
			if (token.equals("kraj")) {
				break;
			}
			
			int value;
			try {
				value = Integer.parseInt(token);
			} catch (NumberFormatException ex) {
				System.out.printf("'%s' nije cijeli broj.%n", token);
				continue;
			}
			
			boolean valueAlreadyExists = containsValue(glava, value);
			if (valueAlreadyExists) {
				System.out.println("Broj već postoji. Preskačem.");
			} else {
				glava = addNode(glava, value);
				System.out.println("Dodano.");
			}
		}
		
		StringBuilder sb = new StringBuilder();
		generateInfo(glava, true, sb);
		System.out.printf("Ispis od najmanjeg: %s%n", sb.toString());
		
		//pražnjenje buffera od stringbuilder-a
		sb.setLength(0);
		
		generateInfo(glava, false, sb);
		System.out.printf("Ispis od najvećeg: %s%n", sb.toString());
		
		sc.close();
	}

	/**
	 * Dodaje cijeli broj u binarno stablo i vraća njegov korijen.
	 * Cijeli brojevi koji već postoje u stablu se ne dodaju.
	 * 
	 * @param glava <code>TreeNode</code> korijen binarnog stabla
	 * @param value	vrijednost koja se dodaje
	 * @return <code>TreeNode</code> korijen binarnog stabla
	 */
	public static TreeNode addNode(TreeNode glava, int value) {
		if (glava == null) {
			TreeNode newNode = new TreeNode();
			newNode.value = value;
			return newNode;
		}
		
		if (value < glava.value) {
			glava.left = addNode(glava.left, value);
		} else if (value == glava.value) {
			return glava;
		} else {
			glava.right = addNode(glava.right, value);
		}
		
		return glava;
		
	}

	/**
	 * Provjerava postoji li tražena vrijednost već u stablu.
	 * 
	 * @param glava <code>TreeNode</code> korijen binarnog stabla
	 * @param value tražena vrijednost
	 * @return <code>true</code> ako i samo ako tražena vrijednost postoji u stablu
	 */
	public static boolean containsValue(TreeNode glava, int value) {
		if (glava == null) {
			return false;
		}
		if (value == glava.value) {
			return true;
		}
		
		if (value < glava.value) {
			return containsValue(glava.left, value);
		} else {
			return containsValue(glava.right, value);
		}
	}

	/**
	 * Vraća veličinu binarnog stabla, tj. broj pohranjenih elemenata.
	 * 
	 * @param glava <code>TreeNode</code> korijen binarnog stabla
	 * @return veličina stabla
	 */
	public static int treeSize(TreeNode glava) {
		if (glava == null) {
			return 0;
		}
		return 1 + treeSize(glava.left) + treeSize(glava.right);
	}
	
	/**
	 * Sortira i puni predani <code>StringBuilder</code> objekt s elementima binarnog stabla.
	 * Uzlazno ili silazno sortiranje zadaje se sa zastavicom <code>sortAscending</code>.
	 * 
	 * @param glava <code>TreeNode</code> korijen binarnog stabla
	 * @param sortAscending <code>true</code> ako je željeno uzlazno sortiranje podataka
	 * @param sb <code>StringBuilder</code> objekt u koji će se stavljati elementi binarnog stabla
	 */
	private static void generateInfo(TreeNode glava, boolean sortAscending, StringBuilder sb) {
		if (glava == null) {
			return;
		}
		
		if (sortAscending == true) {
			generateInfo(glava.left, sortAscending, sb);
			sb.append(String.format("%d ", glava.value));
			generateInfo(glava.right, sortAscending, sb);			
		} else {
			generateInfo(glava.right, sortAscending, sb);
			sb.append(String.format("%d ", glava.value));
			generateInfo(glava.left, sortAscending, sb);		
		}
	}
	
}
