package hr.fer.zemris.java.gui.prim;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrimListModelTest {

	@Test
	public void testPrimListModel() {
		PrimListModel model = new PrimListModel();
		
		assertTrue(model.next() == 2);
		assertTrue(model.next() == 3);
		assertTrue(model.next() == 5);
		assertTrue(model.next() == 7);
	}
	
	@Test
	public void testPrimListModelLarge() {
		PrimListModel model = new PrimListModel();
		
		for (int i = 0; i < 1_000_000; i++) {
			model.next();
		}
		
		assertTrue(model.next() == 15_485_867);
	}

}
