package hr.fer.zemris.java.hw16.search;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a search engine that can work with text files. After the
 * search engine has been initialized with the path to a directory containing
 * text files to be searched, and the path to a stop word definition file,
 * queries can be performed by calling the {@link #search(String...)} method.
 * Results are retrived as a {@link SearchResults} object which can be
 * queried for further information.
 * 
 * @author Vice Ivušić
 *
 */
public class DocumentSearchEngine {

	/**
	 * Set of stop words as defined by the stop word definition file.
	 * These words are ignored during vocabulary building and searches.
	 */
	private Set<String> stopWords = new HashSet<>();
	
	/**
	 * Set of words the vocabulary consists of.
	 */
	private Set<String> vocabulary = new HashSet<>();
	
	/**
	 * Map mapping vocabulary words to the indexes under which they are
	 * stored in relevant vectors.
	 */
	private Map<String, Integer> wordIndex = new HashMap<>();
	
	/**
	 * List of documents the search engine is working with, as defined
	 * by the directory path.
	 */
	private List<Document> documents = new ArrayList<>();
	
	/**
	 * Inverse document frequency vector, used for building the TF-IDF vector.
	 */
	private Vector idfVector;
	
	/**
	 * Maximum amount of retrieved search results.
	 */
	private static final int MAX_SEARCH_RESULTS = 10;
	
	/**
	 * Creates a new DocumentSearchEngine, initializing itself using the files found
	 * under the given parameters. Expects the user to pass non-null values and
	 * gives no warnings otherwise.
	 * 
	 * @param directoryString path to directory containing text files to index and search
	 * @param stopWordsString path to stop word definition file
	 * @throws IOException if any kind of input/output error occurs
	 */
	public DocumentSearchEngine(String directoryString, String stopWordsString) throws IOException {
		Path directoryPath = Paths.get(directoryString);
		Path stopWordsPath = Paths.get(stopWordsString);
		
		loadStopWords(stopWordsPath);
		buildVocabulary(directoryPath);
		buildDocuments(directoryPath);
	}
	
	/**
	 * Helper method for initializing the stop word set of words.
	 * 
	 * @param stopWordsPath path to stop word definition file
	 * @throws IOException if any kind of input/output error occurs
	 */
	private void loadStopWords(Path stopWordsPath) throws IOException {
		List<String> lines = Files.readAllLines(stopWordsPath, StandardCharsets.UTF_8);
		// doing what the professor said on Ferko so vocabulary size is 10896 (without BOM marker)
		lines.forEach(word -> stopWords.add(word.replaceAll("(.*)[.]$", "$1")));
	}

	/**
	 * Helper method for initializing the set of vocabulary words.
	 * 
	 * @param directoryPath path to directory containing text files
	 * @throws IOException if any kind of input/output error occurs
	 */
	private void buildVocabulary(Path directoryPath) throws IOException {
		FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				List<String> words = loadWords(file);
				for (String word : words) {
					if (stopWords.contains(word)) {
						continue;
					}
					
					if (vocabulary.add(word)) {
						wordIndex.put(word, vocabulary.size()-1);
					}
				}
				
				return FileVisitResult.CONTINUE;
			}
		};
		
		Files.walkFileTree(directoryPath, visitor);
	}

	/**
	 * Helper method for loading a list of all the words contained in the
	 * specified text file.
	 * 
	 * @param file path to text file
	 * @return list of words the text file consists of
	 * @throws IOException if any kind of input/output error occurs
	 */
	private List<String> loadWords(Path file) throws IOException {
		String fileText = new String(
			Files.readAllBytes(file), 
			StandardCharsets.UTF_8
		);
		
		// the regular expression matches most European Unicode letters
		Pattern pattern = Pattern.compile("([a-zA-Z\u00C0-\u017F]+)");
		Matcher matcher = pattern.matcher(fileText);
		
		List<String> words = new ArrayList<>();
		while (matcher.find()) {
			words.add(matcher.group(1).toLowerCase());
		}
		
		return words;
	}

	/**
	 * Helper method for building the list of documents from the text files
	 * contained in the specified directory.
	 * 
	 * @param directoryPath path to directory containing text files
	 * @throws IOException if any kind of input/output error occurs
	 */
	private void buildDocuments(Path directoryPath) throws IOException {
		FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				List<String> words = loadWords(file);
				
				Document doc = new Document(file, vocabulary.size());
				buildBinaryAndFrequencyVector(doc, words.toArray(new String[words.size()]));
				documents.add(doc);

				return FileVisitResult.CONTINUE;
			}
		};		
		
		Files.walkFileTree(directoryPath, visitor);
		
		buildIdfVector();
		
		documents.forEach(doc -> buildTfIdfVector(doc));
		
	}
	
	/**
	 * Helper method for building the specified document's binary
	 * and frequency vectors t for the specified words.
	 * 
	 * @param doc document to build the vectors for
	 * @param words array of words to use for building the vectors
	 */
	private void buildBinaryAndFrequencyVector(Document doc, String ... words) {
		for (String word : words) {
			word = word.toLowerCase();
			
			if (stopWords.contains(word)) continue;
			if (!vocabulary.contains(word)) continue;
			
			int index = wordIndex.get(word);
			
			if (doc.bagOfWords.add(word)) {
				doc.binaryVector.setComponent(index, 1);
				doc.frequencyVector.setComponent(index, 1);
			} else {
				double oldValue = doc.frequencyVector.getComponent(index);
				doc.frequencyVector.setComponent(index, oldValue+1);
			}
		}
	}

	/**
	 * Helper method for building the inverse document frequency vector
	 * for the initialized documents.
	 */
	private void buildIdfVector() {
		idfVector = new Vector(vocabulary.size());
		int documentCount = documents.size();
		
		for (String word : vocabulary) {
			int index = wordIndex.get(word);
			
			int wordDocumentCount = 0;
			for (Document doc : documents) {
				if ((int) doc.binaryVector.getComponent(index) == 1) {
					wordDocumentCount++;
				}
			}
			
			idfVector.setComponent(
				index,
				Math.log((double) documentCount / wordDocumentCount)
			);
		}
		
	}

	/**
	 * Helper method for building the specified document's 
	 * Term Frequency - Inverse Document Frequency vector.
	 * 
	 * @param doc document to build the vector for
	 */
	private void buildTfIdfVector(Document doc) {
		for (String word : doc.bagOfWords) {
			int index = wordIndex.get(word);
			
			doc.tfIdfVector.setComponent(
				index,
				doc.frequencyVector.getComponent(index) * idfVector.getComponent(index)
			);
		}
	}
	
	/**
	 * Performs a search using the specified array of words as search terms.
	 * 
	 * @param words array of words to search for
	 * @return a SearchResults object which can be queried for further information
	 */
	public SearchResults search(String ... words) {
		Document query = new Document(null, vocabulary.size());
		buildBinaryAndFrequencyVector(query, words);
		buildTfIdfVector(query);
		
		List<Document> results = new ArrayList<>();
		Map<Document, Double> similarities = new HashMap<>();
		for (Document doc : documents) {
			if (results.size() >= MAX_SEARCH_RESULTS) {
				break;
			}
			
			double similarity = doc.tfIdfVector.cosAngle(query.tfIdfVector);
			similarities.put(doc, similarity);
			
			if (Math.abs(similarity) > 10e-4) {
				results.add(doc);
			}
		}
		
		Collections.sort(results, new Comparator<Document>() {
			
			@Override
			public int compare(Document doc1, Document doc2) {
				return -Double.compare(similarities.get(doc1), similarities.get(doc2));
			}
		});
		
		return new SearchResults(results, query, similarities);
	}

	/**
	 * Returns the size of the vocabulary of words the search engine is working with.
	 * 
	 * @return size of this search engine's vocabulary
	 */
	public int getVocabularySize() {
		return vocabulary.size();
	}

	/**
	 * Helper class for storing information relevant for a single document,
	 * including where its text file is saved on disk, its vectors and the
	 * words it consists of.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	private static class Document {
		
		/**
		 * Path where this document's text file is saved on disk.
		 */
		Path documentPath;
		
		/**
		 * This document's binary vector.
		 */
		Vector binaryVector;
		
		/**
		 * This document's frequency vector.
		 */
		Vector frequencyVector;
		
		/**
		 * This document's term frequency - inverse document frequency vector.
		 */
		Vector tfIdfVector;
		
		/**
		 * Set of words this document consists of.
		 */
		Set<String> bagOfWords = new LinkedHashSet<>();
		
		/**
		 * Creates a new Document from the text file located at the specified path
		 * and with the specified vector size. The vector size should match
		 * the vocabulary size.
		 * 
		 * @param documentPath path to text file
		 * @param vectorSize size of the frequency vectors
		 */
		Document(Path documentPath, int vectorSize) {
			this.documentPath = documentPath;
			
			binaryVector = new Vector(vectorSize);
			frequencyVector = new Vector(vectorSize);
			tfIdfVector = new Vector(vectorSize);
		}
		
	}
	
	/**
	 * Represents the search results of a performed query. This object
	 * can be queried for the amount of search results, for the path
	 * to the text file representing the documents, and for the similarity
	 * factor between the query and the search results.
	 * 
	 * @author Vice Ivušić
	 *
	 */
	public static class SearchResults {
		
		/**
		 * The query as a Document object.
		 */
		private Document query;
		
		/**
		 * List of documents the search results encompass.
		 */
		private List<Document> documents;
		
		/**
		 * Map mapping documents to their similarity factors.
		 */
		private Map<Document, Double> similarities;
		
		/**
		 * Creates a new SearchResults object from the specified parameters.
		 * 
		 * @param documents list of documents the search results encompass
		 * @param query the query as a Document object
		 * @param similarities map mapping documents to their similarity factors
		 */
		private SearchResults(List<Document> documents, Document query, Map<Document, Double> similarities) {
			this.documents = documents;
			this.query = query;
			this.similarities = similarities;
		}
		
		/**
		 * @return the amount of search results
		 */
		public int count() {
			return documents.size();
		}
		
		/**
		 * Returns the similarity factor between the query and the search result
		 * with the specified index.
		 * 
		 * @param index index of the search result in question
		 * @return similarity factor between query and specified search result
		 * @throws IndexOutOfBoundsException if the index isn't between zero
		 * 		   and the amount of search results
		 */
		public double getSimilarityFactor(int index) {
			return similarities.get(documents.get(index));
		}
		
		/**
		 * Returns the path to the text file of the document associated with
		 * the search results with the specified index.
		 * 
		 * @param index index of the search result in question
		 * @return Path to the text file of the document in question
		 */
		public Path getDocumentPath(int index) {
			return documents.get(index).documentPath.toAbsolutePath();
		}
		
		/**
		 * @return a set of words that were actually used to perform the query
		 */
		public Set<String> getQuery() {
			return Collections.unmodifiableSet(query.bagOfWords);
		}

	}
	
}
