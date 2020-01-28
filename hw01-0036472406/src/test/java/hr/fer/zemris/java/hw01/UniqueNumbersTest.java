package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;;

public class UniqueNumbersTest {

	private static UniqueNumbers.TreeNode initialize() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		
		return glava;
	}
	
	@Test
	public void jesuLiElementiStablaIzUputaDobroUpisani() {
		UniqueNumbers.TreeNode glava = initialize();
		
		int[] data = {
				glava.value,
				glava.left.value,
				glava.left.right.value,
				glava.right.value
		};
		
		int[] expected = {42, 21, 35, 76};
		assertArrayEquals(expected, data);
	}
	
	@Test
	public void velicinaStablaZaPodatkeIzUputa() {
		UniqueNumbers.TreeNode glava = initialize();
		
		int size = UniqueNumbers.treeSize(glava);	
		assertEquals(4, size);
	}
	
	@Test
	public void velicinaStablaZaPraznoStablo() {
		UniqueNumbers.TreeNode glava = null;
		
		int size = UniqueNumbers.treeSize(glava);	
		assertEquals(0, size);
	}	
	
	@Test
	public void sadrziLiStabloIzUputa76() {
		UniqueNumbers.TreeNode glava = initialize();
		
		boolean containsValue = UniqueNumbers.containsValue(glava, 76);	
		assertEquals(true, containsValue);
	}
	
	@Test
	public void sadrziLiStabloIzUputa144() {
		UniqueNumbers.TreeNode glava = initialize();
		
		boolean containsValue = UniqueNumbers.containsValue(glava, 144);	
		assertEquals(false, containsValue);
	}
	

}
