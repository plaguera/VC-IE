package pane.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;

import image.Image;
import image.LUT;
import image.Pane;

@SuppressWarnings("serial")
public class HistogramPane extends Pane implements Observer {

	private ChartPanel red, green, blue, gray, cumulative, normalized;
	JTabbedPane tabbedPane;
	
	public HistogramPane(Image image) {
		super(image);
		LUT lut = new LUT(getImage());
		red = new ChartPanel(createChart(createDataset(int2double(lut.redCount2()), "Red"), Color.RED));
		green = new ChartPanel(createChart(createDataset(int2double(lut.greenCount2()), "Green"), Color.GREEN));
		blue = new ChartPanel(createChart(createDataset(int2double(lut.blueCount2()), "Blue"), Color.BLUE));
		gray = new ChartPanel(createChart(createDataset(int2double(lut.grayCount2()), "Gray"), Color.DARK_GRAY));
		cumulative = new ChartPanel(createChart(createDataset(int2double(lut.cumulativeCount2()), "Cumulative (Gray)"), Color.GRAY));
		normalized = new ChartPanel(createChart(createDataset(int2double(lut.grayCount2()), "Normalized (Gray)"), Color.GRAY));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Red", red);
		tabbedPane.addTab("Green", green);
		tabbedPane.addTab("Blue", blue);
		tabbedPane.addTab("Gray", gray);
		tabbedPane.addTab("Cumulative", cumulative);
		tabbedPane.addTab("Normalized", normalized);
		add(tabbedPane);
		
		setPreferredSize(new java.awt.Dimension(256, 256));
	}
	
	private void refreshDatasets() {
		LUT lut = new LUT(getImage());
		red.setChart(createChart(createDataset(int2double(lut.redCount2()), "Red"), Color.RED));
		green.setChart(createChart(createDataset(int2double(lut.greenCount2()), "Green"), Color.GREEN));
		blue.setChart(createChart(createDataset(int2double(lut.blueCount2()), "Blue"), Color.BLUE));
		gray.setChart(createChart(createDataset(int2double(lut.grayCount2()), "Gray"), Color.DARK_GRAY));
		cumulative.setChart(createChart(createDataset(int2double(lut.cumulativeCount2()), "Cumulative (Gray)"), Color.GRAY));
		normalized.setChart(createChart(createDataset(int2double(lut.grayCount2()), "Normalized (Gray)"), Color.GRAY));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}
	
	private IntervalXYDataset createDataset(double[] values, String title) {
		HistogramDataset dataset = new HistogramDataset();
		if(title.equals("Cumulative (Gray)"))
			dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		if(title.equals("Normalized (Gray)"))
			dataset.setType(HistogramType.SCALE_AREA_TO_1);
		dataset.addSeries(title, values, 256);
		return dataset;
	}
	
	private static double[] int2double(int[] values) {
		double[] aux = new double[values.length];
		for(int i = 0; i < values.length; i++)
			aux[i] = values[i];
		return aux;
	}	

	private static JFreeChart createChart(IntervalXYDataset dataset, Color color) {
		JFreeChart chart = ChartFactory.createHistogram("", null, null, dataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainPannable(true);
		plot.setRangePannable(true);
		plot.setForegroundAlpha(0.85f);
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);
		NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		// flat bars look best...
		renderer.setBarPainter(new StandardXYBarPainter());
		renderer.setShadowVisible(false);
		XYItemRenderer r = chart.getXYPlot().getRenderer();
		r.setSeriesPaint(0, color);
		return chart;
	}

	@Override
	public void update(Observable o, Object arg) {
		refreshDatasets();
	}

}
