package hr.fer.zemris.bf.qmc;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

public class MaskTest {

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructorNull() {
		new Mask(null, null, true);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructorValuesLength() {
		new Mask(new byte[0], new HashSet<>(Arrays.asList(1, 2)), false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructorIndexesSize() {
		new Mask(new byte[] {1, 0} , Collections.emptySet(), false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructorIllegalValue() {
		new Mask(new byte[] {1, 0, 4} , new HashSet<>(Arrays.asList(1, 2)), false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidConstructorIllegalIndex() {
		new Mask(30, 3, false);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testModifyReturnedSetOfIndexes() {
		Mask mask = new Mask(1, 3, false);
		Set<Integer> set = mask.getIndexes();
		
		set.add(2);
	}
	
	@Test
	public void testCountOfOnes() {
		Mask mask = new Mask(6, 4, false);
		assertTrue(mask.countOfOnes() == 2);
		
		mask = new Mask(15, 4, false);
		assertTrue(mask.countOfOnes() == 4);
	}
	
	@Test
	public void testGetValueAt() {
		Mask mask = new Mask(6, 4, false);
		assertTrue(mask.getValueAt(0) == 0);
		assertTrue(mask.getValueAt(1) == 1);
		assertTrue(mask.getValueAt(2) == 1);
		assertTrue(mask.getValueAt(3) == 0);
		
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetValueAtIllegalPosition() {
		new Mask(6, 4, false).getValueAt(4);
	}
	
	@Test
	public void testCombineWithNoTwos() {
		Mask mask1 = new Mask(
				new byte[] {0, 1, 0, 1} , new HashSet<>(Arrays.asList(5)), false
		);
		
		Mask mask2 = new Mask(
				new byte[] {0, 1, 0, 0} , new HashSet<>(Arrays.asList(4)), true
		);
		
		Mask mask = mask1.combineWith(mask2).get();
		Mask maskExpected = new Mask( 
				new byte[] {0, 1, 0, 2} , new HashSet<>(Arrays.asList(4,5)), false
		);
		
		assertTrue(mask.equals(maskExpected));
	}
	
	@Test
	public void testCombineWithNoTwosIllegal() {
		Mask mask1 = new Mask(
				new byte[] {1, 1, 0, 1} , new HashSet<>(Arrays.asList(13)), false
				);
		
		Mask mask2 = new Mask(
				new byte[] {0, 1, 0, 0} , new HashSet<>(Arrays.asList(4)), true
		);
		
		Optional<Mask> opt = mask1.combineWith(mask2);
		assertFalse(opt.isPresent());
	}
	
	@Test
	public void testCombineWithTwos() {
		Mask mask1 = new Mask(
				new byte[] {1, 0, 2, 1} , new HashSet<>(Arrays.asList(9,11)), true
				);
		
		Mask mask2 = new Mask(
				new byte[] {1, 1, 2, 1} , new HashSet<>(Arrays.asList(13, 15)), true
				);
		
		Mask mask = mask1.combineWith(mask2).get();
		Mask maskExpected = new Mask( 
				new byte[] {1, 2, 2, 1} , new HashSet<>(Arrays.asList(9, 11, 13, 15)), true
		);
		
		assertTrue(mask.equals(maskExpected));
	}

}
