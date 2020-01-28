package hr.fer.zemris.java.hw13.servlets.glasanje;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * An HttpServlet which generates an Excel spreadsheet containing the
 * names of bands and the votes each band received. This servlet expects
 * the following text files in /WEB-INF/: glasanje-definicija.txt and
 * glasanje-rezultati.txt.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="excelRezultatiServlet", urlPatterns={"/glasanje-xls"})
public class ExcelRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bandsFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		String resultsName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		Map<String, Integer> results = null;
		List<BandInfo> bands = null;
		try {
			results = GlasanjeServlet.parseResults(resultsName);
			bands = GlasanjeServlet.parseBands(bandsFileName);
		} catch (IOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		Map<BandInfo, Integer> bandToVoteMap = GlasanjeServlet.createSortedBandToVoteMap(bands, results);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("rezultati");
		
		int k = 0;
        for (Map.Entry<BandInfo, Integer> entry : bandToVoteMap.entrySet()) {
			Row row = sheet.createRow(k);
			
			Cell cellBand = row.createCell(0);
			Cell cellVotes = row.createCell(1);
			
			if (k == 0) {
				cellBand.setCellValue("Bend");
				cellVotes.setCellValue("Glasovi");
				
				row = sheet.createRow(++k);
				cellBand = row.createCell(0);
				cellVotes = row.createCell(1);
			}
			
			cellBand.setCellValue(entry.getKey().getName());
			cellVotes.setCellValue(entry.getValue());
			k++;
        }
        
        resp.setContentType("application/vnd.ms-excel");
        ServletOutputStream os = resp.getOutputStream();
        
        workbook.write(os);
        workbook.close();
	}
}
