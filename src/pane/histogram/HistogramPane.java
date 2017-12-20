package pane.histogram;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JTabbedPane;

import image.Image;
import image.LUT;
import image.Pane;

@SuppressWarnings("serial")
public class HistogramPane extends Pane {

	private JTabbedPane tabbedPane;
	private HistogramPaneTab histRed, histGreen, histBlue, histGray, histCumul, histNorm;

	public HistogramPane(Image image) {
		super(image);
		LUT lut = new LUT(image);
		setTabbedPane(new JTabbedPane(JTabbedPane.TOP));
		setHistRed(new HistogramPaneTab());
		setHistGreen(new HistogramPaneTab());
		setHistBlue(new HistogramPaneTab());
		setHistGray(new HistogramPaneTab());
		setHistCumul(new HistogramPaneTab());
		setHistNorm(new HistogramPaneTab());
		
		getHistRed().newHistogramLayer(lut.redCount(), Color.RED, true, "Red");
		getHistGreen().newHistogramLayer(lut.greenCount(), Color.GREEN, true, "Green");
		getHistBlue().newHistogramLayer(lut.blueCount(), Color.BLUE, true, "Blue");
		getHistGray().newHistogramLayer(lut.grayCount(), Color.DARK_GRAY, true, "Gray");
		getHistCumul().newHistogramLayer(lut.cumulativeCount(), Color.GRAY, true, "Cumulative");
		getHistNorm().newHistogramLayer(lut.cumulativeCount(), Color.LIGHT_GRAY, true, "Normalized");
		
		getTabbedPane().addTab("Red", getHistRed());
		getTabbedPane().addTab("Green", getHistGreen());
		getTabbedPane().addTab("Blue", getHistBlue());
		getTabbedPane().addTab("Gray", getHistGray());
		getTabbedPane().addTab("Cumulative", getHistCumul());
		//getTabbedPane().addTab("Normalized", getHistNorm());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public HistogramPaneTab getHistRed() {
		return histRed;
	}

	public HistogramPaneTab getHistBlue() {
		return histBlue;
	}

	public HistogramPaneTab getHistGreen() {
		return histGreen;
	}

	public HistogramPaneTab getHistGray() {
		return histGray;
	}

	public HistogramPaneTab getHistCumul() {
		return histCumul;
	}

	public HistogramPaneTab getHistNorm() {
		return histNorm;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public void setHistRed(HistogramPaneTab histRed) {
		this.histRed = histRed;
	}

	public void setHistBlue(HistogramPaneTab histBlue) {
		this.histBlue = histBlue;
	}

	public void setHistGreen(HistogramPaneTab histGreen) {
		this.histGreen = histGreen;
	}

	public void setHistGray(HistogramPaneTab histGray) {
		this.histGray = histGray;
	}

	public void setHistCumul(HistogramPaneTab histCumul) {
		this.histCumul = histCumul;
	}

	public void setHistNorm(HistogramPaneTab histNorm) {
		this.histNorm = histNorm;
	}

}
