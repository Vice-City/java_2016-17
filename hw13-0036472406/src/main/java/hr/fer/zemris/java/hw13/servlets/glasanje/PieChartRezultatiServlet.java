package hr.fer.zemris.java.hw13.servlets.glasanje;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

/**
 * An HttpServlet which generates a pie chart graph containing the
 * names of bands and the votes each band received. This servlet expects
 * the following text files in /WEB-INF/: glasanje-definicija.txt and
 * glasanje-rezultati.txt.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="pieChartRezultatiServlet", urlPatterns={"/glasanje-grafika"})
public class PieChartRezultatiServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		ServletOutputStream os = resp.getOutputStream();
		
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
		
        PieDataset dataset = createDataset(bandToVoteMap);
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
     * @param bandToVoteMap map mapping bands to the amount of votes
     * 		  they've received
     * @return dataset created from the specified parameters
     */
    private PieDataset createDataset(Map<BandInfo, Integer> bandToVoteMap) {
        DefaultPieDataset result = new DefaultPieDataset();
        
        for (Map.Entry<BandInfo, Integer> entry : bandToVoteMap.entrySet()) {
        	result.setValue(entry.getKey().getName(), entry.getValue());
        }
        
        return result;
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
