package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

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

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * An HttpServlet which generates an Excel spreadsheet containing the
 * names of the voting options and the votes each option received.
 * This servlet expects to receive the poll ID of the poll options as 
 * its parameter; if none is received, it defaults to the first defined poll.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="excelRezultatiServlet", urlPatterns={"/servleti/glasanje-xls"})
public class ExcelRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pollIdParam = req.getParameter("pollId");
		long pollId = pollIdParam == null ? 1L : Long.parseLong(pollIdParam);
		
		List<PollOption> results;
		try {
			results = DAOProvider.getDao().getPollOptions(pollId);
		} catch (DAOException ex) {
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
			return;
		}
		
		results.sort((opt1, opt2) -> - Long.compare(opt1.getVotesCount(), opt2.getVotesCount()));
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("rezultati");
		
		int k = 0;
        for (PollOption opt : results) {
			Row row = sheet.createRow(k);
			
			Cell cellBand = row.createCell(0);
			Cell cellVotes = row.createCell(1);
			
			if (k == 0) {
				cellBand.setCellValue("Opcija");
				cellVotes.setCellValue("Glasovi");
				
				row = sheet.createRow(++k);
				cellBand = row.createCell(0);
				cellVotes = row.createCell(1);
			}
			
			cellBand.setCellValue(opt.getOptionTitle());
			cellVotes.setCellValue(opt.getVotesCount());
			k++;
        }
        
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"rezultati.xls\"");
        ServletOutputStream os = resp.getOutputStream();
        
        workbook.write(os);
        workbook.close();
	}
}
