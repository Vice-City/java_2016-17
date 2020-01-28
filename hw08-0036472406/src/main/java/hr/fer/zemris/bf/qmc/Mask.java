package hr.fer.zemris.bf.qmc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.utils.Util;

/*
 ************************************************************
 ********************                    ********************
 ********************       TASK2        ********************
 ********************                    ********************
 ************************************************************
 */

/**
 * Represents a product of boolean variables, which might also be
 * incompletely specified. Each of its objects also knows which
 * set of minterms it covers and whether it is a <i>don't care</i>
 * product or not. Each mask can also remember whether it has been
 * combined with another mask to create a whole new mask.
 * 
 * <p>For example, if working with a boolean function defined over
 * the variables A, B, C and D, the product ABC*D would correspond
 * to a mask with its values set to 1101. The product AD* defined
 * over the same variables would correspond to a mask with its
 * values set to 1220. It directly follows that a tautology would
 * have its values set to 2222 if represented as a mask.
 * 
 * <p>Offers constructors for directly constructing a mask with
 * its core structure, or for constructing a mask from the index
 * of a specified minterm. Offers methods for retrieving and setting
 * the combined flag, for retrieving its <i>don't care</i> flag, for
 * retrieving a set of indexes of minterms it represents, for
 * retrieving the number of 1 values it holds, for retrieving its
 * size, for retrieving the byte value at a specified position,
 * and for combining the current mask with another specified mask.
 * 
 * @author Vice Ivušić
 *
 */
public class Mask {

	/** array of bytes representing this mask */
	private byte[] values;
	/** indexes of the minterms this mask represents */
	private Set<Integer> indexes;
	/** flag indicating whether this mask represents a <i>don't care</i> product */
	private boolean dontCare;
	
	/** hash of the values array */
	private int valuesHash;
	
	/** flag indicating whether this mask has been combined with another maks */
	private boolean combined;
	
	/** */
	protected void ZADATAK2() {}
	
	/**
	 * Constructs a new Mask from the specified parameters. Note that the
	 * constructor will not check whether the specified values and specified
	 * set of minterm indexes are consistent with each other; that is up
	 * to the user to ensure.
	 * 
	 * @param values array of bytes representing this mask
	 * @param indexes indexes of the minterms represented by this mask
	 * @param dontCare flag indicating this mask represents a <i>don't care</i> product
	 * @throws IllegalArgumentException if any of the specified parameters is null,
	 * 		   or if the size of the values array of indexes set is zero, or if the
	 * 		   values array contains bytes other than 0, 1 and 2
	 */
	public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) {
		if (values == null || indexes == null) {
			throw new IllegalArgumentException("None of the arguments may be null!");
		}
		
		if (values.length == 0) {
			throw new IllegalArgumentException("Values array must contain at least one value!");
		}
		
		for (byte b : values) {
			if (b != (byte) 0 && b != (byte) 1 && b != (byte) 2) {
				throw new IllegalArgumentException("Byte values can only be 0, 1 and 2!");
			}
		}
		
		// TODO što ako set indeksa ima indekse manje od 0 ili veće od values.length?
		
		if (indexes.isEmpty()) {
			throw new IllegalArgumentException("Set of indexes cannot be empty!");
		}
		
		this.values = Arrays.copyOf(values, values.length);
		this.indexes = Collections.unmodifiableSortedSet(new TreeSet<>(indexes));
		this.dontCare = dontCare;
		
		valuesHash = Arrays.hashCode(values);
	}
	
	/**
	 * Constructs a new Mask representing the minterm with the specified
	 * index and for the specified number of variables. For example,
	 * for index=3 and numberOfVariables=3, the constructed mask will
	 * have its values set to 011.
	 * 
	 * @param index index of the desired minterm
	 * @param numberOfVariables number of variables the mask represents
	 * @param dontCare flag indicating this mask represents a <i>don't care</i> product
	 * @throws IllegalArgumentException if the specified minterm index is too large
	 * 		   for the specified number of variables
	 */
	public Mask(int index, int numberOfVariables, boolean dontCare) {
		if (index >= (1 << numberOfVariables)) {
			throw new IllegalArgumentException(
					"Minterm index is too large for specified number of variables!"
			);
		}
		
		values = Util.indexToByteArray(index, numberOfVariables);
		
		TreeSet<Integer> oneMemberSet = new TreeSet<>();
		oneMemberSet.add(index);
		indexes = Collections.unmodifiableSortedSet(oneMemberSet);
		
		this.dontCare = dontCare;
		
		valuesHash = Arrays.hashCode(values);
	}
	
	/**
	 * Returns true if the current mask has been configured as combined.
	 * 
	 * @return true iff the curent mask has been configured as combined
	 */
	public boolean isCombined() {
		return combined;
	}
	
	/**
	 * Sets this mask's combined flag to the specified flag.
	 * 
	 * @param combined desired value for this flag's combined flag
	 */
	public void setCombined(boolean combined) {
		this.combined = combined;
	}
	
	/**
	 * Returns true if this mask is flagged as a <i>don't care</i> product
	 * 
	 * @return true iff this mask is flagged as a <i>don't care</i> product
	 */
	public boolean isDontCare() {
		return dontCare;
	}
	
	/**
	 * Returns the set of minterm indexes this mask represents. The
	 * returned set is unmodifiable.
	 * 
	 * @return unmodifiable set of minterm indexes this mask represents
	 */
	public Set<Integer> getIndexes() {
		return indexes;
	}
	
	/**
	 * Returns the number of 1 values inside this mask's array of bytes.
	 * 
	 * @return number of 1 values inside this mask's array of bytes
	 */
	public int countOfOnes() {
		int count = 0;
		for (byte b : values) {
			if (b == (byte) 1) {
				count++;
			}
		}
		
		return count;
	}

	/**
	 * Returns the size of this mask's values array, i.e. the number
	 * of variables it represents.
	 * 
	 * @return size of this mask's values array
	 */
	public int size() {
		return values.length;
	}
	
	/**
	 * Returns the byte located at the specified index inside this
	 * mask's values array.
	 * 
	 * @param position index of the desired byte
	 * @return byte at the specified position inside this mask's values array
	 * @throws IndexOutOfBoundsException if the specified position is
	 * 		   out of bounds
	 */
	public byte getValueAt(int position) {
		if (position < 0 || position > values.length-1) {
			throw new IndexOutOfBoundsException(
					String.format("Index must be between %d and %d.", 0, values.length-1)
			);
		}
		
		return values[position];
	}
	
	/**
	 * Returns an Optional<Mask> object which will either be empty
	 * (if the specified mask cannot be combined with the current mask)
	 * or it will contain a new Mask object created by combining the
	 * specified mask with the current mask according to the theorem
	 * of simplification.
	 * 
	 * <p>Masks can be combined only if they are the same size and if their
	 * values arrays differ in only a single byte, but only if that byte
	 * is either 0 or 1. For example, the masks 0121 and 0120 can be
	 * combined into 0122, but the masks 0120 and 0111 cannot be combined.
	 * 
	 * @param other mask to attemt to combine with current mask
	 * @return an Optional object, which is either empty or contains
	 * 		   the newly combined mask
	 */
	public Optional<Mask> combineWith(Mask other) {
		if (other == null || this.size() != other.size()) {
			return Optional.empty();
		}
		
		boolean canCombine = false;
		for (int i = 0, n = values.length; i < n; i++) {
			// a bit complicated, but essentially checks whether both
			// values at a given position are 2 if one of the values is 2
			if (this.values[i] == (byte) 2 || other.values[i] == (byte) 2) {
				if (this.values[i] != (byte) 2 || other.values[i] != (byte) 2) {
					canCombine = false;
					break;
				}
			}
			
			// by this point I'm dealing with either 0s or 1s
			if (this.values[i] != other.values[i]) {
				if (canCombine) {
					canCombine = false;
					break;
				}
				
				canCombine = true;
			}
		}
		
		if (!canCombine) {
			return Optional.empty();
		}
		
		
		// by now, I know that the two values arrays differ in only one
		// value, and that value is certainly either 0 or 1
		byte[] combinedValues = new byte[values.length];
		for (int i = 0, n = values.length; i < n; i++) {
			if (this.values[i] != other.values[i]) {
				combinedValues[i] = (byte) 2;
				continue;
			}
			
			combinedValues[i] = values[i];
		}
		
		Set<Integer> combinedIndexes = new TreeSet<>(this.indexes);
		combinedIndexes.addAll(other.indexes);
		
		boolean combinedCare = this.dontCare && other.dontCare;
		
		return Optional.of(
			new Mask(combinedValues, combinedIndexes, combinedCare)
		);
	}

	@Override
	public int hashCode() {
		return valuesHash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Mask)) {
			return false;
		}
		
		Mask other = (Mask) obj;
		
		if (this.valuesHash != other.valuesHash) {
			return false;
		}
		
		if (!Arrays.equals(this.values, other.values)) {
			return false;
		}
		
		if (this.dontCare != other.dontCare) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (byte b : values) {
			sb.append(b == (byte) 2  ?  "-"  :  b);
		}
		
		sb.append(dontCare  ?  " D"  :  " .");
		sb.append(combined  ?  " *"  :  "  ");
		
		sb.append(" [");
		int i = 0;
		int n = indexes.size();
		for (int index : indexes) {
			if (i == n-1) {
				sb.append(index);
			} else {
				sb.append(index+", ");
			}
			i++;
		}
		sb.append("]");
		
		return sb.toString();
	}
}
