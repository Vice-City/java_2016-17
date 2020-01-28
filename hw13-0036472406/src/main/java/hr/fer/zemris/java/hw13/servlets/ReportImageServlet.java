package hr.fer.zemris.java.hw13.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
 * An HttpServlet which generates a pie chart image of a predefined
 * data set for desktop OS usage and market share.
 * 
 * @author Vice Ivušić
 *
 */
@WebServlet(name="reportImageServlet", urlPatterns={"/reportImage2"})
public class ReportImageServlet extends HttpServlet {

	/** default serial version UID */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		ServletOutputStream os = resp.getOutputStream();
		
        PieDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset, "OS usage statistics");

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
     * Helper method for creating a predefined dataset.
     * 
     * @return the created dataset
     */
    private PieDataset createDataset() {
        DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Linux", 4);
        result.setValue("Mac", 12);
        result.setValue("Windows", 84);
        
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
