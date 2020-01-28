package hr.fer.zemris.java.hw13.servlets.glasanje;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An HttpServlet which prompts the user to vote for a predefined set
 * of bands. This servlet expects the file glasanje-definicja.txt to be
 * located in /WEB-INF/. Each line of the file has to have 3 tab separated
 * values: ID of the band, name of the band, and a link to the band's
 * popular song.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="glasanjeServlet", urlPatterns={"/glasanje"})
public class GlasanjeServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		
		List<BandInfo> bands;
		try {
			bands = parseBands(fileName);
		} catch (IOException ex) {
			req
				.getRequestDispatcher(req.getServletContext().getRealPath("/WEB-INF/pages/glasanjeError.jsp"))
				.forward(req, resp);
			return;
		}
		
		req.setAttribute("bandList", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
	/**
	 * Returns a list of bands parsed from the specified file.
	 * 
	 * @param fileName file path with the band definitions
	 * @return list of parsed bands
	 * @throws IOException if an I/O error occurs
	 */
	public static List<BandInfo> parseBands(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));

		List<BandInfo> bands = new ArrayList<>();
		for (String line : lines) {
			String[] lineTokens = line.split("\t");
			
			if (lineTokens.length != 3) {
				continue;
			}
			
			BandInfo info = new BandInfo(
				lineTokens[0],
				lineTokens[1],
				lineTokens[2]
			);
			
			bands.add(info);
		}
		
		return bands;
	}
	
	/**
	 * Returns a map mapping band IDs to the amount of votes they've received,
	 * parsed from the specified result defintion file.
	 * 
	 * @param fileName file path with the result definitions
	 * @return map mapping band IDs to the amount of votes they've received
	 * @throws IOException if an I/O error occurs
	 */
	public static Map<String, Integer> parseResults(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		
		Map<String, Integer> results = new LinkedHashMap<>();
		
		for (String line : lines) {
			String[] lineTokens = line.split("\t");
			
			if (lineTokens.length != 2) {
				continue;
			}
			
			results.put(lineTokens[0], Integer.parseInt(lineTokens[1]));
		}
		
		return results;
	}
	
	/**
	 * Creates a string according to the result definition file, which
	 * can then be written onto disk.
	 * 
	 * @param results map mapping band IDs to the amount of votes they've receive
	 * @return String representing the result definition file
	 */
	public static String createResults(Map<String, Integer> results) {
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<String, Integer> entry : results.entrySet()) {
			sb.append(entry.getKey() + '\t' + entry.getValue() + '\n');
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates an empty results file, taking band IDs from the band definition
	 * file.
	 * 
	 * @param fileName path to band definition file
	 * @return String representing an empty result definition file
	 * @throws IOException if an I/O error occurs
	 */
	public static String createEmptyResults(String fileName) throws IOException {
		List<BandInfo> bands = parseBands(fileName);
		
		StringBuilder sb = new StringBuilder();
		for (BandInfo info : bands) {
			sb.append(info.getId() + '\t' + "0" + '\n');
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates a map mapping bands to the amount of votes they've received,
	 * sorted by the amount of votes they've received (descendingly); must
	 * be iterated over with an iterator for the descending order to take
	 * effect. This method modifies the list of bands by sorting the bands
	 * descendingly according to the number of votes they've received
	 * 
	 * @param bands list of bands; will be modified after method call
	 * @param idToVoteMap map mapping band IDs to the amount of votes they've received
	 * @return a map mapping bands to the amount of votes they've received,
	 * 		   sorted by the amount of votes they've received
	 */
	public static Map<BandInfo, Integer> createSortedBandToVoteMap(List<BandInfo> bands, final Map<String, Integer> idToVoteMap) {
		bands.sort(new Comparator<BandInfo>() {

			@Override
			public int compare(BandInfo o1, BandInfo o2) {
				
				Integer votes1 = idToVoteMap.get(o1.getId());
				Integer votes2 = idToVoteMap.get(o2.getId());

				/*
				 * This is because of a weird exception that was happening when
				 * an empty results file had to be created, but only on Tomcat;
				 * on Jetty it worked fine even without it
				 */
				if (votes1 == null || votes2 == null) {
					return 0;
				}
				
				return votes1 < votes2 ? 1 : -1;
			}
		});
				
		Map<BandInfo, Integer> bandToVoteMap = new LinkedHashMap<>();
		for (BandInfo bandInfo : bands) {
			bandToVoteMap.put(bandInfo, idToVoteMap.get(bandInfo.getId()));
		}
				
		return bandToVoteMap;
	}
	
}
