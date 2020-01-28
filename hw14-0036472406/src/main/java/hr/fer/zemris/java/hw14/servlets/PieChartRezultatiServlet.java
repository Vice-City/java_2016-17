package hr.fer.zemris.java.hw14.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * An HttpServlet which generates a pie chart graph containing the
 * names of options and the votes each option received. 
 * This servlet expects to receive the poll ID of the poll options as 
 * its parameter; if none is received, it defaults to the first defined poll.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="pieChartRezultatiServlet", urlPatterns={"/servleti/glasanje-grafika"})
public class PieChartRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		ServletOutputStream os = resp.getOutputStream();
		
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

        PieDataset dataset = createDataset(results);
        JFreeChart chart = createChart(dataset, null);

		BufferedImage img = chart.createBufferedImage(
			600, 
			400, 
			BufferedImage.TYPE_3BYTE_BGR,
			new ChartRenderingInfo()
		);
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(img, "png", output);
		output.flush();
		
		os.write(output.toByteArray());
	}
	

    /**
     * Creates a dataset according to the passed map mapping bands
     * to the amount of votes they've received.
     * 
     * @param results list of all poll options
     * @return dataset created from the specified parameters
     */
    private PieDataset createDataset(List<PollOption> results) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (PollOption opt : results) {
        	dataset.setValue(opt.getOptionTitle(), opt.getVotesCount());
        }
        
        return dataset;
    }

    /**
     * Helper method for creating a JFreeChart with the specified title
     * and dataset. Title may be null.
     * 
     * @param dataset the dataset that the chart is being created for
     * @param title the title for the chart
     * @return a JFreeChart object representing the dataset.
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
            title,
            dataset,
            true,
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;

    }
}
