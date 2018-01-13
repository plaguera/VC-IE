package pane.histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import image.Image;
import listener.MouseHistogramListener;

@SuppressWarnings("serial")
public class HistogramPane extends JPanel  {

	private JTabbedPane tabbedPane;
	private Histogram histRed, histGreen, histBlue, histGray;
	private JLabel lbColorValue, lbCount;

	public HistogramPane(Image image) {
		setTabbedPane(new JTabbedPane());
		setHistRed(new Histogram(image, utils.Color.RED, Color.RED));
		setHistGreen(new Histogram(image, utils.Color.GREEN, Color.GREEN));
		setHistBlue(new Histogram(image, utils.Color.BLUE, Color.BLUE));
		setHistGray(new Histogram(image, utils.Color.GRAY, Color.DARK_GRAY));
		setLayout(new BorderLayout());
		
		add(getTabbedPane(), BorderLayout.CENTER);
		getTabbedPane().add("Red", getHistRed());
		getTabbedPane().add("Green", getHistGreen());
		getTabbedPane().add("Blue", getHistBlue());
		getTabbedPane().add("Gray", getHistGray());
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		setLbColorValue(new JLabel("0"));
		setLbCount(new JLabel("0"));
		getLbColorValue().setBorder(BorderFactory.createTitledBorder("Value"));
		getLbCount().setBorder(BorderFactory.createTitledBorder("Ocurrences"));
		panel.add(getLbColorValue());
		panel.add(getLbCount());
		add(panel, BorderLayout.SOUTH);
		
		getHistRed().addMouseMotionListener(new MouseHistogramListener(getHistRed(), getLbColorValue(), getLbCount()));
		getHistGreen().addMouseMotionListener(new MouseHistogramListener(getHistGreen(), getLbColorValue(), getLbCount()));
		getHistBlue().addMouseMotionListener(new MouseHistogramListener(getHistBlue(), getLbColorValue(), getLbCount()));
		getHistGray().addMouseMotionListener(new MouseHistogramListener(getHistGray(), getLbColorValue(), getLbCount()));
		
		setBackground(Color.LIGHT_GRAY);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public Histogram getHistRed() {
		return histRed;
	}

	public Histogram getHistGreen() {
		return histGreen;
	}

	public Histogram getHistBlue() {
		return histBlue;
	}

	public Histogram getHistGray() {
		return histGray;
	}

	public JLabel getLbColorValue() {
		return lbColorValue;
	}

	public JLabel getLbCount() {
		return lbCount;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public void setHistRed(Histogram histRed) {
		this.histRed = histRed;
	}

	public void setHistGreen(Histogram histGreen) {
		this.histGreen = histGreen;
	}

	public void setHistBlue(Histogram histBlue) {
		this.histBlue = histBlue;
	}

	public void setHistGray(Histogram histGray) {
		this.histGray = histGray;
	}

	public void setLbColorValue(JLabel lbColorValue) {
		this.lbColorValue = lbColorValue;
	}

	public void setLbCount(JLabel lbCount) {
		this.lbCount = lbCount;
	}

}
