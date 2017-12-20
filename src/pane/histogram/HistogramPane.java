package pane.histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import image.Image;
import image.LUT;
import image.Pane;
import listener.MouseHistogramListener;

@SuppressWarnings("serial")
public class HistogramPane extends Pane implements Observer {

	private JTabbedPane tabbedPane;
	private HistogramPaneTab histRed, histGreen, histBlue, histGray, histCumul, histNorm;
	private JLabel lbColorValue, lbCount;

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
		
		getTabbedPane().addTab("Red", getHistRed());
		getTabbedPane().addTab("Green", getHistGreen());
		getTabbedPane().addTab("Blue", getHistBlue());
		getTabbedPane().addTab("Gray", getHistGray());
		getTabbedPane().addTab("Cumulative", getHistCumul());
		//getTabbedPane().addTab("Normalized", getHistNorm());
		
		JPanel labelPanel = new JPanel(new GridLayout(1, 2));
		
		setLbColorValue(new JLabel("0", SwingConstants.CENTER));
		getLbColorValue().setBorder(BorderFactory.createTitledBorder("Color Value"));
		labelPanel.add(getLbColorValue());

		setLbCount(new JLabel("0", SwingConstants.CENTER));
		getLbCount().setBorder(BorderFactory.createTitledBorder("Ocurrences"));
		labelPanel.add(getLbCount());
		
		getHistRed().addMouseMotionListener(new MouseHistogramListener(getHistRed(), getLbColorValue(), getLbCount()));
		getHistGreen().addMouseMotionListener(new MouseHistogramListener(getHistGreen(), getLbColorValue(), getLbCount()));
		getHistBlue().addMouseMotionListener(new MouseHistogramListener(getHistBlue(), getLbColorValue(), getLbCount()));
		getHistGray().addMouseMotionListener(new MouseHistogramListener(getHistGray(), getLbColorValue(), getLbCount()));
		getHistCumul().addMouseMotionListener(new MouseHistogramListener(getHistCumul(), getLbColorValue(), getLbCount()));
		getHistNorm().addMouseMotionListener(new MouseHistogramListener(getHistNorm(), getLbColorValue(), getLbCount()));
		
		setMinimumSize(getHistRed().getMinimumSize());
		setPreferredSize(getHistRed().getPreferredSize());
		
		add(getTabbedPane(), BorderLayout.CENTER);
		getLbColorValue().setPreferredSize(new Dimension(128, 10));
		getLbCount().setPreferredSize(new Dimension(128, 50));
		add(labelPanel, BorderLayout.SOUTH);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	}
	
	@Override
	public void update(Observable o, Object arg) {
		refreshDatasets();
	}
	
	private void refreshDatasets() {
		LUT lut = new LUT(getImage());
		
		getHistRed().deleteHistogramLayer();
		getHistGreen().deleteHistogramLayer();
		getHistBlue().deleteHistogramLayer();
		getHistGray().deleteHistogramLayer();
		getHistCumul().deleteHistogramLayer();
		getHistNorm().deleteHistogramLayer();
		
		getHistRed().newHistogramLayer(filterBlacks(lut.redCount()), Color.RED, true, "Red");
		getHistGreen().newHistogramLayer(filterBlacks(lut.greenCount()), Color.GREEN, true, "Green");
		getHistBlue().newHistogramLayer(filterBlacks(lut.blueCount()), Color.BLUE, true, "Blue");
		getHistGray().newHistogramLayer(filterBlacks(lut.grayCount()), Color.DARK_GRAY, true, "Gray");
		getHistCumul().newHistogramLayer(filterBlacks(lut.cumulativeCount()), Color.GRAY, true, "Cumulative");
		getHistNorm().newHistogramLayer(filterBlacks(lut.cumulativeCount()), Color.LIGHT_GRAY, true, "Normalized");
	}
	
	public int[] filterBlacks(int[] values) {
		values[0] = getImage().blacks;
		return values;
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

	public JLabel getLbColorValue() {
		return lbColorValue;
	}

	public JLabel getLbCount() {
		return lbCount;
	}

	public void setLbColorValue(JLabel lbColorValue) {
		this.lbColorValue = lbColorValue;
	}

	public void setLbCount(JLabel lbCount) {
		this.lbCount = lbCount;
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
