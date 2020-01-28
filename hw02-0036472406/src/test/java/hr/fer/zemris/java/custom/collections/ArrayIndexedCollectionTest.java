package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayIndexedCollectionTest {
	
	private ArrayIndexedCollection initializeZeroOne() {
		ArrayIndexedCollection col = new ArrayIndexedCollection(2);
		col.add(new Integer(0));
		col.add(new Integer(1));
		
		return col;
	}
	
	@Test
	public void constructorTest() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(new Integer(0));
		col.add(new Integer(1));
		assertEquals(2, col.size());
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(1);
		col2.add(new Integer(2));
		col2.add(new Integer(3));
		assertEquals(2, col2.size());	
		
		col2 = new ArrayIndexedCollection(col);
		col2.add(new Integer(4));
		assertEquals(3, col2.size());
		assertEquals(Integer.valueOf(1), col.get(1));
		
		col2 = new ArrayIndexedCollection(col2, 2);
		assertEquals(3, col2.size());
		
		try {
			col2 = new ArrayIndexedCollection(null);
		} catch (IllegalArgumentException ex) { }
		assertEquals(3, col2.size());

		try {
			col2 = new ArrayIndexedCollection(0);
		} catch (IllegalArgumentException ex) { }
		assertEquals(3, col2.size());

		try {
			col2 = new ArrayIndexedCollection(null, 12);
		} catch (IllegalArgumentException ex) { }
		assertEquals(3, col2.size());		
	}
	
	@Test
	public void sizeTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(2, col.size());
		
		col.add(new Integer(1));
		assertEquals(3, col.size());
		
		col.remove(0);
		col.remove(0);
		assertEquals(1, col.size());
	}
	
	@Test
	public void addTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		
		col.add("bok");
		assertEquals("bok", col.get(2));
		
		col.add(new Double(1.6));
		assertEquals(Double.valueOf(1.6), col.get(3));
		
		try {
			col.add(null);
		} catch (IllegalArgumentException ex) { }
		assertEquals(4, col.size());
	}
	
	@Test
	public void toArrayTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		
		Object[] array = col.toArray();
		assertEquals(2, array.length);
		assertEquals(Integer.valueOf(0), array[0]);
		assertEquals(Integer.valueOf(1), array[1]);
		
		col.add("foo");
		array = col.toArray();
		assertEquals(3, array.length);
		assertEquals("foo", array[2]);
	}
	
	@Test
	public void forEachTest() {
		ArrayIndexedCollection col1 = initializeZeroOne();
		ArrayIndexedCollection col2 = new ArrayIndexedCollection();
		
		class TestProcessor extends Processor {
			@Override
			public void process(Object value) {
				col2.add(value);
			}
		}
		
		col1.forEach(new TestProcessor());
		assertEquals(Integer.valueOf(0), col2.get(0));
		assertEquals(Integer.valueOf(1), col2.get(1));
		
		try {
			col1.forEach(null);
		} catch (IllegalArgumentException ex) { }
		assertEquals(Integer.valueOf(0), col2.get(0));
		assertEquals(Integer.valueOf(1), col2.get(1));
	}
	
	@Test
	public void containsTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertTrue(col.contains(Integer.valueOf(1)));
		assertFalse(col.contains("foo"));
		assertFalse(col.contains(null));
	}
	
	@Test
	public void objectRemoveTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertTrue(col.remove(Integer.valueOf(0)));
		assertFalse(col.remove("foo"));
		assertFalse(col.remove(null));
	}
	
	@Test
	public void getTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(Integer.valueOf(1), col.get(1));
		assertEquals(Integer.valueOf(0), col.get(0));
		
		try {
			col.get(2);
		} catch (IndexOutOfBoundsException ex) {
			col.add("foo");
		}
		assertEquals("foo", col.get(2));
		
		try {
			col.get(-1);
		} catch (IndexOutOfBoundsException ex) {
			col.add("bar");
		}
		assertEquals("bar", col.get(3));
	}
	
	@Test
	public void clearTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(2, col.size());
		
		col.clear();
		assertEquals(0, col.size());
	}
	
	@Test
	public void insertTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		
		col.insert("foo", 0);
		assertEquals("foo", col.get(0));
		assertEquals(Integer.valueOf(0), col.get(1));
		assertEquals(Integer.valueOf(1), col.get(2));
		
		col.insert("bar", 2);
		assertEquals(Integer.valueOf(0), col.get(1));
		assertEquals("bar", col.get(2));
		assertEquals(Integer.valueOf(1), col.get(3));
		
		col.insert("foobar", 4);
		assertEquals(Integer.valueOf(1), col.get(3));
		assertEquals("foobar", col.get(4));
		
		try {
			col.insert("barfoo", -1);
		} catch (IndexOutOfBoundsException ex) { }
		assertEquals("foo", col.get(0));
		
		try {
			col.insert("foobar", 10);
		} catch (IndexOutOfBoundsException ex) { 
			col.insert("barfoo", 1);
		}
		assertEquals("barfoo", col.get(1));
	}
	
	@Test
	public void indexOfTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(0, col.indexOf(Integer.valueOf(0)));
		assertEquals(1, col.indexOf(Integer.valueOf(1)));
		assertEquals(-1, col.indexOf("foo"));
		assertEquals(-1, col.indexOf(null));
	}
	
	@Test
	public void indexRemoveTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(2, col.size());
		
		col.remove(1);
		assertEquals(1, col.size());
		
		col.remove(0);
		assertEquals(0, col.size());
		
		try {
			col.remove(0);
		} catch (IndexOutOfBoundsException ex) {
			col.add("foo");
		}
		assertEquals("foo", col.get(0));
		
		try {
			col.remove(2);
		} catch (IndexOutOfBoundsException ex) {
			col.remove("foo");
		}
		assertEquals(0, col.size());
	}
	
	@Test
	public void isEmptyTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertFalse(col.isEmpty());
		
		col.remove(0);
		assertFalse(col.isEmpty());
		
		col.remove(0);
		assertTrue(col.isEmpty());
	}
	
	@Test
	public void addAllTest() {
		ArrayIndexedCollection col = initializeZeroOne();
		assertEquals(2, col.size());
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection();
		assertEquals(0, col2.size());
		
		col.addAll(col2);
		assertEquals(2, col.size());
		
		try {
			col.addAll(col);
		} catch (IllegalArgumentException ex) { }
		assertEquals(2, col.size());
		
		col2.addAll(col);
		assertEquals(2, col2.size());
		
		try {
			col2.addAll(null);
		} catch (IllegalArgumentException ex) {
			col2.add("foo");
		}
		assertEquals(3, col2.size());
	}
}
