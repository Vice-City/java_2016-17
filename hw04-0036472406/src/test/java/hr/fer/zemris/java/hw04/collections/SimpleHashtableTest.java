package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;

public class SimpleHashtableTest {

	@Test(expected=IllegalArgumentException.class)
	public void testTableEntryConstructorNull() {
		new SimpleHashtable.TableEntry<String, Integer>(null, null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorLowCapacity() {
		new SimpleHashtable<String, Integer>(0);
	}
	
	@Test
	public void stressTest() {
		SimpleHashtable<Double, Integer> map = new SimpleHashtable<>(3);
		
		Random rand = new Random();
		for (int i = 0; i < 100_000; i++) {
			map.put(rand.nextDouble(), i);
		}
		
		assertTrue(map.size() > 99_500);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPutNull() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();
		
		map.put(null, null);
	}
	
	@Test
	public void testGet() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put("Ana", 5);
		map.put("Milka", null);
		
		assertTrue(map.get("Ana") == 5);
		assertTrue(map.get("Milka") == null);
		assertTrue(map.get("Štefica") == null);
	}
	
	@Test
	public void testContainsKey() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put("Ana", 5);
		map.put("Milka", null);
		
		assertTrue(map.containsKey("Ana"));
		assertTrue(map.containsKey("Milka"));
		assertFalse(map.containsKey("Štefica"));
		assertFalse(map.containsKey(null));
	}
	
	@Test
	public void testPut() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put("Ana", 1);
		assertTrue(map.size() == 1);
		assertTrue(map.containsKey("Ana"));
		assertTrue(map.containsValue(1));
		
		map.put("Milka", null);
		assertTrue(map.size() == 2);
		assertTrue(map.containsKey("Milka"));
		assertTrue(map.containsValue(null));
		
		map.put("Štef", 3);
		assertTrue(map.size() == 3);
		assertTrue(map.containsKey("Štef"));
		assertTrue(map.containsValue(3));
		
		map.put("Ana", 5);
		assertTrue(map.size() == 3);
		assertTrue(map.containsKey("Ana"));
		assertTrue(map.containsValue(5));
		assertFalse(map.containsValue(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void putNull() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put(null, 5);
	}
	
	@Test
	public void testContainsValue() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put("Ana", 5);
		map.put("Milka", null);
		
		assertTrue(map.containsValue(5));
		assertTrue(map.containsValue(null));
		assertFalse(map.containsValue(3));

	}
	
	@Test
	public void testRemove() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>();

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		
		map.remove("Štefica");
		assertTrue(map.size() == 5);
		
		map.remove(null);
		assertTrue(map.size() == 5);
		
		map.remove("Milka");
		map.remove("Kiki");
		assertTrue(map.size() == 3);
		
		assertTrue(map.containsKey("Ana"));
		assertFalse(map.containsKey("Milka"));
		assertFalse(map.containsKey("Kiki"));
	}
	
	@Test
	public void testIteratorRemove() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>(1);

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = map.iterator();
		while (iter.hasNext()) {
			if (iter.next().getKey().equals("Milka")) iter.remove();
		}
		
		assertTrue(map.size() == 4);
		assertFalse(map.containsKey("Milka"));
		
		iter = map.iterator();
		while (iter.hasNext()) {
			if (iter.next().getKey().equals("Ana")) iter.remove();
			if (iter.next().getKey().equals("Jasenka")) iter.remove();
		}
		
		assertTrue(map.size() == 2);
		assertFalse(map.containsKey("Ana"));
		assertFalse(map.containsKey("Jasenka"));
	}
	
	@Test(expected=ConcurrentModificationException.class)
	public void testElementRemovedOutsideIterator() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>(1);

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = map.iterator();
		while (iter.hasNext()) {
			map.remove("Štef");
		}
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testElementRemovedTwiceDuringSameIteration() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>(1);

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = map.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
			iter.remove();
		}
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testTryToRemoveElementBeforeCallingNext() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>(1);

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = map.iterator();
		while (iter.hasNext()) {
			iter.remove();
		}
		
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratingWithoutCheckingHasNext() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<>(1);

		map.put("Ana", 5);
		map.put("Milka", null);
		map.put("Štef", 3);
		map.put("Kiki", 1);
		map.put("Jasenka", 3);
		
		assertTrue(map.size() == 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = map.iterator();
		while (iter.next() != null);
		
	}
	
}
