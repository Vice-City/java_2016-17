package hr.fer.zemris.bf.qmc;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/*
 ************************************************************
 ********************                    ********************
 ********************   TASKS 4, 5, 6    ********************
 ********************                    ********************
 ************************************************************
 */

/**
 * Represents a minimizer of boolean functions. This minimizer uses
 * the Quine-McCluskey algorithm with the Pyne-McCluskey approach.
 * 
 * <p>Offers methods for retrieving a list of minimal forms as
 * sets of {@link Mask} objects, as a list of {@link Node} expressions, 
 * and as a list of string representations.
 * 
 * @author Vice Ivušić
 *
 */
public class Minimizer {

	/** logger object used for logging the minimization process */
	private static final Logger LOG = Logger.getLogger("hr.fer.zemris.bf.qmc");

	/** set of minterms for current boolean function */
	private Set<Integer> mintermSet;
	/** set of don't care products for current boolean function */
	private Set<Integer> dontCareSet;
	/** list of variables for current boolean function */
	private List<String> variables;

	/** list of sets of Mask objects representing the minimal forms */
	private List<Set<Mask>> minimalForms;
	
	/**
	 * Constructs a new Minimizer with the specified parameters.
	 * 
	 * @param mintermSet set of boolean function minterms
	 * @param dontCareSet set of boolean function <i>don't care</i> products
	 * @param variables set of variables the boolean function is defined over
	 * @throws IllegalArgumentException if any of the specified parameters is
	 * 		   null or if there are inconsistencies between the parameters, such
	 * 		   as unallowed indexes in the sets or overlapping indexes
	 */
	public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet, List<String> variables) {
		if (mintermSet == null || dontCareSet == null || variables == null) {
			throw new IllegalArgumentException("None of the arguments may be null!");
		}
		
		checkAllowedIndexes(mintermSet, dontCareSet, variables);
		checkOverlappingIndexes(mintermSet, dontCareSet);

		this.mintermSet = mintermSet;
		this.dontCareSet = dontCareSet;
		this.variables = variables;

		Set<Mask> primCover = findPrimaryImplicants();
		minimalForms = chooseMinimalCover(primCover);
	}

	/**
	 * Helper method for checking whether the specified sets of indexes
	 * have unallowed values according to the specified list of variables.
	 * 
	 * @param mintermSet set of boolean function minterms
	 * @param dontCareSet set of boolean function <i>don't care</i> products
	 * @param variables set of variables the boolean function is defined over
	 * @throws IllegalArgumentException if the specified sets of indexes
	 * 		   have unallowed values
	 */
	private void checkAllowedIndexes(Set<Integer> mintermSet, Set<Integer> dontCareSet, List<String> variables) {
		Integer biggestAllowedIndex = (1 << variables.size())-1;

		for (int index : mintermSet) {
			if (index < 0 || index > biggestAllowedIndex) {
				throw new IllegalArgumentException("Set of minterms contains illegal minterm!");
			}
		}
		
		for (int index : dontCareSet) {
			if (index < 0 || index > biggestAllowedIndex) {
				throw new IllegalArgumentException(
						"Set of don't cares contains illegal don't care product!"
				);
			}
		}
	}

	/**
	 * Helper method for checking whether the specified sets have
	 * overlapping indexes.
	 * 
	 * @param mintermSet set of boolean function minterms
	 * @param dontCareSet set of boolean function <i>don't care</i> products
	 * @throws IllegalArgumentException if the specified sets have
	 * 		   overlapping indexes
	 */
	private void checkOverlappingIndexes(Set<Integer> mintermSet, Set<Integer> dontCareSet) {
		if (mintermSet.removeAll(dontCareSet) == true) {
			throw new IllegalArgumentException(
					"Minterms and don't cares cannot have same indexes!"
			);
		}
	}

	/*
	 ************************************************************
	 ********************                    ********************
	 ********************    TASK4: PART1    ********************
	 ********************                    ********************
	 ************************************************************
	 */
	
	/**
	 * Helper method for finding the boolean function's primary implicants.
	 * 
	 * @return Set of primary implicants as their corresponding Mask representations
	 */
	private Set<Mask> findPrimaryImplicants() {
		Set<Mask> primaryImplicants = new LinkedHashSet<>();
	
		List<Set<Mask>> currentColumn = createFirstColumn();
		while (true) {
			boolean currentColumnIsEmpty = checkColumnIsEmpty(currentColumn);
			if (currentColumnIsEmpty) {
				break;
			}
	
			List<Set<Mask>> nextColumn = buildNextColumn(currentColumn);
			
			Set<Mask> columnPrimaryImplicants = findColumnPrimaries(currentColumn);
			primaryImplicants.addAll(columnPrimaryImplicants);
	
			logCurrentColumnAndPrimaries(currentColumn, columnPrimaryImplicants);
			currentColumn = nextColumn;
		}
	
		logAllPrimaryImplicants(primaryImplicants);
		return primaryImplicants;
	}

	/**
	 * Helper method for building the starting column for the first part
	 * of the Quine-McCluskey algorithm, i.e. implicants ordered ascendingly
	 * in groups according to the number of 1s in their Mask representations.
	 * The resulting list of sets of masks will e.g. have 0000 in the first
	 * set, 0001 and 0100 in the second set, 0101 and 0011 in the third set etc.,
	 * up to the fifth set. Each set may be empty, but is never null.
	 * 
	 * @return list of sets of masks representing the column with ordered groups
	 */
	private List<Set<Mask>> createFirstColumn() {
		int maskSlots = variables.size();
	
		List<Set<Mask>> listOfGroups = new ArrayList<>();
	
		for (int i = 0, n = variables.size(); i <= n; i++) {
			listOfGroups.add(new LinkedHashSet<>());
		}
	
		for (Integer i : mintermSet) {
			Mask mask = new Mask(i, maskSlots, false);
			Set<Mask> group = listOfGroups.get(mask.countOfOnes());
			group.add(mask);
		}
	
		for (Integer i : dontCareSet) {
			Mask mask = new Mask(i, maskSlots, true);
			Set<Mask> group = listOfGroups.get(mask.countOfOnes());
			group.add(mask);
		}
	
		return listOfGroups;
	}

	/**
	 * Returns true if the specified column is empty, i.e. if none of
	 * its groups contain a single implicant.
	 * 
	 * @param currentColumn column being checked
	 * @return true iff the specified column is empty
	 */
	private static boolean checkColumnIsEmpty(List<Set<Mask>> currentColumn) {
		for (Set<Mask> group : currentColumn) {
			if (!group.isEmpty()) {
				return false;
			}
		}
	
		return true;
	}

	/**
	 * Helper method for completing a single iteration of the
	 * Quine-McCluskey algorithm, i.e. combining each group
	 * with the next immediate group.
	 * 
	 * @param currentColumn column used for building the next column
	 * @return list of sets of Mask objects that the next column
	 * 		   consists of; may be empty if it wasn't possible to build
	 * 		   next column
	 */
	private static List<Set<Mask>> buildNextColumn(List<Set<Mask>> currentColumn) {
		List<Set<Mask>> nextColumn = null;
		
		// !! watch out here, important to set n to currentColumn.size()
		for (int i = 0, n = currentColumn.size()-1; i < n; i++) {
			Set<Mask> firstGroup = currentColumn.get(i);
			Set<Mask> secondGroup = currentColumn.get(i + 1);
	
			Set<Mask> newGroup = combineGroup(firstGroup, secondGroup);
			
			if (nextColumn == null) {
				nextColumn = new ArrayList<>();
			}
			nextColumn.add(newGroup);
		}
		
		return nextColumn == null
			   ? Collections.emptyList()
			   : nextColumn
		;
	}

	/**
	 * Helper method for a single part of the first step of the 
	 * Quine-McCluskey algorithm, i.e. combining one group of implicants
	 * with another group of implicants and building a new group from
	 * compatible implicants.
	 * 
	 * @param firstGroup first group being combined
	 * @param secondGroup second group being combined
	 * @return set of masks representing the new group made from the
	 * 		   specified groups; may be empty if no combinations were possible
	 */
	private static Set<Mask> combineGroup(Set<Mask> firstGroup, Set<Mask> secondGroup) {
		Set<Mask> newGroup = null;
	
		for (Mask first : firstGroup) {
			for (Mask second : secondGroup) {
				Optional<Mask> combinedMask = first.combineWith(second);
				if (!combinedMask.isPresent()) {
					continue;
				}
				
				first.setCombined(true);
				second.setCombined(true);
				
				if (newGroup == null) {
					newGroup = new LinkedHashSet<>();
				}
				newGroup.add(combinedMask.get());
			}
		}
		
		return newGroup == null
			   ? Collections.emptySet()
			   : newGroup
		;
	}

	/**
	 * Helper method for finding potential primary implicants during
	 * an iteration of the Quine-McCluskey algorithm, i.e. implicants
	 * which weren't used to create another column for the next iterations.
	 * 
	 * @param currentColumn column whose implicants are to be checked
	 * @return set of Mask objects representing the primary implicants;
	 * 		   may be empty if no such implicants were found
	 */
	private static Set<Mask> findColumnPrimaries(List<Set<Mask>> currentColumn) {
		Set<Mask> groupsPrimaryImplicants = null;
		
		for (Set<Mask> group : currentColumn) {
			for (Mask mask : group) {
				if (mask.isCombined() || mask.isDontCare()) {
					continue;
				}
				
				if (groupsPrimaryImplicants == null) {
					groupsPrimaryImplicants = new LinkedHashSet<>();
				}
				groupsPrimaryImplicants.add(mask);
			}
		}
		
		return groupsPrimaryImplicants == null
			   ? Collections.emptySet()
			   : groupsPrimaryImplicants
		;
	}

	/**
	 * Helper method for logging an iteration of the Quine-McCluskey
	 * algorithm.
	 * 
	 * @param currentColumn column being logged
	 * @param groupPrimaryImplicants group of potential primary implicants
	 */
	private static void logCurrentColumnAndPrimaries(List<Set<Mask>> currentColumn, Set<Mask> groupPrimaryImplicants) {
		LOG.log(Level.FINER, () -> "Stupac tablice:");
		LOG.log(Level.FINER, () -> "=================================");
	
		int size = currentColumn.size();
		int i = 0;
		for (Set<Mask> group : currentColumn) {
			if (group.isEmpty()) {
				i++;
				continue;
			}
			
			for (Mask mask : group) {
				LOG.log(Level.FINER, () -> mask.toString());
			}
	
			if (i++ < size-1) {
				LOG.log(Level.FINER, () -> "-------------------------------");
			}
		}
		LOG.log(Level.FINER, () -> "");
		
		if (groupPrimaryImplicants.isEmpty()) {
			return;
		}
	
		for (Mask mask : groupPrimaryImplicants) {
			LOG.log(Level.FINEST, () -> "Pronašao primarni implikant: " + mask);
		}
		LOG.log(Level.FINEST, () -> "");
	}

	/**
	 * Helper method for logging all of the potential primary implicants
	 * found after completing the Quine-McCluskey algorithm.
	 * 
	 * @param primaryImplicants group of potential primary implicants
	 */
	private static void logAllPrimaryImplicants(Set<Mask> primaryImplicants) {
		LOG.log(Level.FINE, () -> "");
		LOG.log(Level.FINE, () -> "Svi primarni implikanti:");
		
		for (Mask mask : primaryImplicants) {
			LOG.log(Level.FINE, () -> mask.toString());
		}		
	}
	
	/*
	 ************************************************************
	 ********************                    ********************
	 ********************    TASK4: PART2    ********************
	 ********************                    ********************
	 ************************************************************
	 */
	
	/**
	 * Helper method for calculating all minimal forms of a boolean function.
	 * A minimal form will always contain the function's important primary
	 * implicants, but may contain different combinations of unimportant
	 * primary implicants, which is why there are multiple equivalent
	 * minimal forms.
	 * 
	 * @param primCover set of primary implicants
	 * @return list of minimal forms as sets of Mask objects
	 */
	private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {
		Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
		Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);
	
		Map<Integer, Integer> mintermToColumnMap = new HashMap<>();
		for (int i = 0; i < minterms.length; i++) {
			Integer index = minterms[i];
			mintermToColumnMap.put(index, i);
		}
		
		boolean[][] table = buildCoverTable(implicants, minterms, mintermToColumnMap);
		
		boolean[] coveredMinterms = new boolean[minterms.length];
		
		Set<Mask> importantSet = selectImportantPrimaryImplicants(
				implicants, mintermToColumnMap, table, coveredMinterms
		);
		logImportantPrimaries(importantSet);
		
		List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);
		logRawPFunction(pFunction);
		
		Set<BitSet> minset = findMinimalSet(pFunction);
		logMinimizedAdditions(minset);
		
		if (minset.isEmpty()) {
			List<Set<Mask>> minimalForm = null;
			if (importantSet.isEmpty()) {
				minimalForm = Collections.emptyList();
			} else {
				minimalForm = new LinkedList<>();
				minimalForm.add(importantSet);
			}
			
			logMinimalForms(minimalForm);
			return minimalForm;
		}
		
		List<Set<Mask>> minimalForms = new ArrayList<>();
		for (BitSet bs : minset) {
			Set<Mask> set = new LinkedHashSet<>(importantSet);
			bs.stream().forEach(i -> set.add(implicants[i]));
			minimalForms.add(set);
		}
		
		logMinimalForms(minimalForms);
		
		return minimalForms;
	}

	/**
	 * Builds a table of boolean values indicating which implicant
	 * covers which minterm for the specified parameters. If implicant 
	 * i covers minterm j, then table[i][j] will be set to true.
	 * 
	 * @param implicants array of implicants
	 * @param minterms array of minterms
	 * @param mintermToColumnMap map mapping minterms to  the index 
	 * 		  where they are stored within the minterm array
	 * @return a double array of boolean values indicating which
	 * 		   implicant covers which minterm
	 */
	private static boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms,
			Map<Integer, Integer> mintermToColumnMap) {
		boolean[][] coverTable = new boolean[implicants.length][minterms.length];
		
		for (int i = 0, n = coverTable.length; i < n; i++) {
			Set<Integer> implicantIndexes = implicants[i].getIndexes();
			
			for (Integer implicantIndex : implicantIndexes) {
				// in case the implicant in question was combined with a 
				//dontCare, since the map won't contain its column index!
				if (!mintermToColumnMap.containsKey(implicantIndex)) {
					continue;
				}
				
				int columnIndex = mintermToColumnMap.get(implicantIndex);
				coverTable[i][columnIndex] = true;
			}
		}
		
		return coverTable;
	}

	/**
	 * Helper method which returns a set of important primary implicants
	 * for the specified parameters. Important primary implicants are the
	 * implicants which cover at least one minterm that isn't covered by
	 * any other primary implicant.
	 * 
	 * @param implicants array of implicants
	 * @param mintermToColumnMap map mapping minterms to  the index 
	 * 		  where they are stored within the minterm array
	 * @param table a double array of boolean values indicating which
	 * 		   implicant covers which minterm
	 * @param coveredMinterms array to fill with information about
	 * 		  whether a minterm is covered by a found important
	 * 		  primary implicant or not
	 * @return set of primary implicants as their mask representations
	 */
	private static Set<Mask> selectImportantPrimaryImplicants(
			Mask[] implicants, Map<Integer, Integer> mintermToColumnMap,
			boolean[][] table, boolean[] coveredMinterms) {
		Set<Mask> importantPrimaryImplicants = null;
		
		if (table.length == 0) {
			return Collections.emptySet();
		}
		
		for (int i = 0, n = table[i].length; i < n; i++) {
			Mask importantPrimary = null;
			
			boolean foundImportantPrimary = false;
			for (int j = 0, m = table.length; j < m; j++) {
				if (table[j][i] == true) {
					if (foundImportantPrimary) {
						foundImportantPrimary = false;
						break;
					}
					
					foundImportantPrimary = true;
					importantPrimary = implicants[j];
				}
			}
			
			if (!foundImportantPrimary) {
				continue;
			}
			
			for (Integer minterm : importantPrimary.getIndexes()) {
				if (!mintermToColumnMap.containsKey(minterm)) {
					continue;
				}
				
				int index = mintermToColumnMap.get(minterm);
				coveredMinterms[index] = true;
			}
			
			if (importantPrimaryImplicants == null) {
				importantPrimaryImplicants = new LinkedHashSet<>();
			}
			importantPrimaryImplicants.add(importantPrimary);
		}
		
		return importantPrimaryImplicants == null
			   ? Collections.emptySet()
			   : importantPrimaryImplicants
		;
	}

	/**
	 * Builds and returns a P-Function for the specified parameters.
	 * A P-Function is in the form of multiplying brackets containing
	 * sums of products, e.g. P(P0, P1, P2) = (P0 + P1)*(P0 + P2)*(P1 + P2).
	 * The products (P0, P1 and P2) are represented as a BitSet with a single
	 * bit (the one corresponding to the index) set to 1; the sums of products
	 * are represented as a Set of BitSets, while the whole product of brackets
	 * is the final List of Sets of Bitsets.
	 * 
	 * @param table a double array of boolean values indicating which
	 * 		   implicant covers which minterm
	 * @param coveredMinterms array of boolean values indicating which
	 * 	      minterms have been covered by an important primary implicant
	 * @return a List of Sets of Bitsets representing a product of brackets
	 * 		   containing sums of boolean variable products
	 */
	private List<Set<BitSet>> buildPFunction(boolean[][] table, boolean[] coveredMinterms) {
		if (table.length == 0) {
			return Collections.emptyList();
		}
		
		List<Set<BitSet>> listOfBrackets = new ArrayList<>();
		
		for (int i = 0, n = table[i].length; i < n; i++) {
			if (coveredMinterms[i] == true) {
				continue;
			}
			
			Set<BitSet> bracket = new LinkedHashSet<>();
			
			for (int j = 0, m = table.length; j < m; j++) {
				if (table[j][i] == false) {
					continue;
				}
				
				BitSet mask = new BitSet();
				mask.set(j);
				bracket.add(mask);
			}
			
			listOfBrackets.add(bracket);
		}
		
		return listOfBrackets;
	}

	/**
	 * Helper method which takes the specified P-Function, multiplies
	 * all of the products until a sum of products is obtained, and
	 * determines which products are equivalent for building a
	 * minimal form of a boolean function.
	 * 
	 * @param pFunction P-Function in its bracketed form
	 * @return set of products which may be used for building
	 * 		   a boolean function's minimal form
	 */
	private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
		if (pFunction.size() == 0) {
			return Collections.emptySet();
		}
		
		// if pFunction has only one bracket, choose a single
		// implicant since they're all equivalent anyway
		if (pFunction.size() == 1) {
			Set<BitSet> singleMemberSet = new LinkedHashSet<>();
			for (BitSet bs : pFunction.get(0)) {
				if (singleMemberSet.isEmpty()) {
					singleMemberSet.add(bs);
					break;
				}
			}
			return singleMemberSet;
		}
		
		Stack<Set<BitSet>> bracketStack = new Stack<>();
		for (int i = pFunction.size()-1; i >= 0; i--) {
			bracketStack.push(pFunction.get(i));
		}
	
		while (bracketStack.size() > 1) {
			Set<BitSet> firstBracket = bracketStack.pop();
			Set<BitSet> secondBracket = bracketStack.pop();
			
			Set<BitSet> newBracket = multiplyBrackets(firstBracket, secondBracket);
			bracketStack.push(newBracket);
		}
		
		Set<BitSet> allProducts = bracketStack.peek();
		logProductPFunction(bracketStack);
		
		Set<BitSet> minSet = new LinkedHashSet<>();
		
		OptionalInt lowestCardinality = OptionalInt.empty();
		for (BitSet bs : allProducts) {
			if (!lowestCardinality.isPresent()) {
				lowestCardinality = OptionalInt.of(bs.cardinality());
				continue;
			}
			
			if (bs.cardinality() < lowestCardinality.getAsInt()) {
				lowestCardinality = OptionalInt.of(bs.cardinality());
			}
		}
		
		if (!lowestCardinality.isPresent()) {
			return minSet;
		}
		
		for (BitSet bs : allProducts) {
			if (bs.cardinality() == lowestCardinality.getAsInt()) {
				minSet.add(bs);
			}
		}
		
		return minSet;
	}

	/**
	 * Helper method for multiplying two brackets containing sums of
	 * boolean variable products. 
	 * 
	 * @param firstBracket first bracket to multiply
	 * @param secondBracket second bracket to multiply
	 * @return resulting bracket
	 */
	private Set<BitSet> multiplyBrackets(Set<BitSet> firstBracket, Set<BitSet> secondBracket) {
		Set<BitSet> newBracket = new LinkedHashSet<>();
		
		for (BitSet product1 : firstBracket) {
			for (BitSet product2 : secondBracket) {
				BitSet result = new BitSet();
				result.or(product1);
				result.or(product2);
				
				newBracket.add(result);
			}
		}
		
		// if low number of variables, no need for further optimization
		if (variables.size() < 5) {
			return newBracket;
		}
		
		/*
		 * Otherwise, optimize the returning bracket by eliminating
		 * unnecessary products according to the theorem of simplification.
		 * This significantly speeds up the algorithm.
		 */
		Set<BitSet> optimizedBracket = new LinkedHashSet<>(newBracket);
		
		for (BitSet bs1 : newBracket) {
			if (!optimizedBracket.contains(bs1)) {
				continue;
			}
			
			for (BitSet bs2 : newBracket) {
				int card1 = bs1.cardinality();
				int card2 = bs2.cardinality();
				
				// if two products certainly differ in more than one variable...
				if ((int) Math.abs(card1 - card2) != 1) {
					continue;
				}
				
				BitSet bsCopy = new BitSet();
				bsCopy.or(bs1);
				bsCopy.xor(bs2);
				
				// if they don't differ for only one variable, continue
				if (bsCopy.cardinality() != 1) {
					continue;
				}
				
				// otherwise, remove the product with the extra variable
				if (card2 > card1) {
					optimizedBracket.remove(bs2);
				} else {
					optimizedBracket.remove(bs1);
				}
				
			}
		}
		
		return optimizedBracket;
	}

	/**
	 * Helper method for logging important primary implicants.
	 * 
	 * @param importantSet set of important primary implicants
	 */
	private static void logImportantPrimaries(Set<Mask> importantSet) {
		LOG.log(Level.FINE, () -> "");
		LOG.log(Level.FINE, () -> "Bitni primarni implikanti su:");
		for (Mask mask : importantSet) {
			LOG.log(Level.FINE, () -> mask.toString());
		}		
	}

	/**
	 * Helper method for logging a P-Function in its bracketed form.
	 * 
	 * @param pFunction P-Function in its bracketed form
	 */
	private static void logRawPFunction(List<Set<BitSet>> pFunction) {
		LOG.log(Level.FINER, () -> "");
		LOG.log(Level.FINER, () -> "p funkcija je:");
		LOG.log(Level.FINER, () -> pFunction.toString());
	}

	/**
	 * Helper method for logging a P-Function in its sum of products form.
	 * 
	 * @param bracketStack stack containing the P-Function in its
	 * 		  sum of products form
	 */
	private static void logProductPFunction(Stack<Set<BitSet>> bracketStack) {
		LOG.log(Level.FINER, () -> "");
		LOG.log(Level.FINER, () -> "Nakon pretvorbe p-funkcije u sumu produkata:");
		LOG.log(Level.FINER, () -> bracketStack.toString());
	}

	/**
	 * Helper method for logging the minimized additions of a function.
	 * 
	 * @param minset minimized additions of a function
	 */
	private static void logMinimizedAdditions(Set<BitSet> minset) {
		LOG.log(Level.FINER, () -> "");
		LOG.log(Level.FINER, () -> "Minimalna pokrivanja još trebaju:");
		LOG.log(Level.FINER, () -> minset.toString());
		
	}

	/**
	 * Helper method for logging all the minimal forms of a function.
	 * 
	 * @param minimalForms minimal forms of a function
	 */
	private static void logMinimalForms(List<Set<Mask>> minimalForms) {
		LOG.log(Level.FINE, () -> "");
		LOG.log(Level.FINE, () -> "Minimalni oblici funkcije su:");

		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = minimalForms.size(); i < n; i++) {
			sb.append(String.format("%d. ", i+1));
			sb.append(minimalForms.get(i));
			
			LOG.log(Level.FINE, () -> sb.toString());
			sb.setLength(0);
		}
	}

	/**
	 * Returns a list of the minimal forms of a function. Each minimal
	 * form is represented as a set of Mask objects, with each mask
	 * object representing a product of boolean variables.
	 * 
	 * @return list of minimal forms of a function
	 */
	public List<Set<Mask>> getMinimalForms() {
		return minimalForms;
	}

	/*
	 ************************************************************
	 ********************                    ********************
	 ********************       TASK5        ********************
	 ********************                    ********************
	 ************************************************************
	 */
	
	/**
	 * Returns a list of the minimal forms of a function as {@link Node}
	 * expressions.
	 * 
	 * @return list of minimal forms as Node expressions
	 */
	public List<Node> getMinimalFormsAsExpressions() {
		List<Node> topLevelNodes = new ArrayList<>();
		
		for (Set<Mask> minForm : minimalForms) {
			topLevelNodes.add(buildOrNode(minForm));
		}
		
		return topLevelNodes;
	}

	/**
	 * Helper method which builds a single potential OR node
	 * from the specified set of minimal forms.
	 * 
	 * @param minForms set of minimal forms
	 * @return a Node potentially representing an OR Node
	 */
	private Node buildOrNode(Set<Mask> minForms) {
		List<Node> children = new LinkedList<>();
		
		for (Mask mask : minForms) {
			children.add(buildAndNode(mask));
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		return new BinaryOperatorNode("or", children, Boolean::logicalOr);
	}

	/**
	 * Helper method which builds a single potential AND node
	 * from the specified Mask representing a product of variables.
	 * 
	 * @param mask mask representing a product of variables
	 * @return a Node potentially representing an AND Node
	 */
	private Node buildAndNode(Mask mask) {
		List<Node> children = new LinkedList<>();
		
		for (int i = 0, n = mask.size(); i < n; i++) {
			if (mask.getValueAt(i) == 2) {
				continue;
			}
			
			Node variableNode = new VariableNode(variables.get(i));
			if (mask.getValueAt(i) == 0) {
				variableNode = new UnaryOperatorNode("not", variableNode, bool -> !bool);
			}
			children.add(variableNode);
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		return new BinaryOperatorNode("and", children, Boolean::logicalAnd);
	}
	
	/*
	 ************************************************************
	 ********************                    ********************
	 ********************       TASK6        ********************
	 ********************                    ********************
	 ************************************************************
	 */
	
	/**
	 * Returns a list of the minimal forms of a function as their
	 * string representations.
	 * 
	 * @return list of minimal forms as their string representations
	 */
	public List<String> getMinimalFormsAsString() {
		List<String> topLevelNodes = new ArrayList<>();
		
		for (Set<Mask> minForms : minimalForms) {
			topLevelNodes.add(makeOrString(minForms));
		}
		
		if (topLevelNodes.isEmpty()) {
			topLevelNodes.add("FALSE");
		}
		
		return topLevelNodes;
	}

	/**
	 * Helper method which builds a string of a single
	 * potential OR from the specified set of minimal forms.
	 * 
	 * @param minForms set of minimal forms
	 * @return a Node potentially representing an OR
	 */
	private String makeOrString(Set<Mask> minForms) {
		List<String> children = new LinkedList<>();
		
		for (Mask mask : minForms) {
			children.add(makeAndString(mask));
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = children.size(); i < n;  i++) {
			if (i == n-1) {
				sb.append(children.get(i));
				continue;
			}
			
			sb.append(children.get(i) + " OR ");
		}
		
		return sb.toString();
	}

	/**
	 * Helper method which builds a string of a single potential AND
	 * from the specified Mask representing a product of variables.
	 * 
	 * @param mask mask representing a product of variables
	 * @return a Node potentially representing an AND
	 */
	private String makeAndString(Mask mask) {
		List<String> children = new LinkedList<>();
		
		boolean maskIsTautology = true;
		for (int i = 0, n = mask.size(); i < n; i++) {
			if (mask.getValueAt(i) == 2) {
				continue;
			}
			
			maskIsTautology = false;
			
			String child = "";
			
			if (mask.getValueAt(i) == 0) {
				child += "NOT ";
			}
			
			children.add(child += variables.get(i));
		}
		
		if (maskIsTautology) {
			return "TRUE";
		}
		
		if (children.size() == 1) {
			return children.get(0);
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = children.size(); i < n; i++) {
			if (i == n-1) {
				sb.append(children.get(i));
				continue;
			}
			
			sb.append(children.get(i) + " AND ");
		}
		
		return sb.toString();
	}

}
