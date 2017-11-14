package imageFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import image.Panel;
import image.RGB;

@SuppressWarnings("serial")
public class PixelColorPanel extends Panel {

	private JLabel labelCoord, labelRGB, labelOthers;
	private ColorPanel panel;

	public PixelColorPanel() {
		setLabelCoord(new JLabel("", SwingConstants.CENTER));
		setLabelRGB(new JLabel("", SwingConstants.CENTER));
		setLabelOthers(new JLabel("", SwingConstants.CENTER));
		setPanel(new ColorPanel());
		setLayout(new GridLayout(2, 1));
		JPanel aux = new JPanel(new GridLayout(3, 1));
		aux.add(getLabelCoord());
		aux.add(getLabelRGB());
		aux.add(getLabelOthers());
		add(aux);
		add(getPanel());
		setPreferredSize(new Dimension(200, 100));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void setColor(int colorInt, int x, int y) {
		RGB color = new RGB(colorInt);
		getLabelCoord().setText("x = " + x + ", y = " + y);
		getLabelRGB().setText("<html>[<font color='red'>" + color.getRed() + "</font>, <font color='green'>"
				+ color.getGreen() + "</font>, <font color='blue'>" + color.getBlue() + "</font>]</html>");
		getLabelOthers()
				.setText("<html>" + String.format("(#%02x%02x%02x)", color.getRed(), color.getGreen(), color.getBlue())
						+ ", [<font color='gray'>" + color.gray() + "</font>]</html>");
		getPanel().setColor(new Color(colorInt));
	}

	public JLabel getLabelCoord() {
		return labelCoord;
	}

	public JLabel getLabelRGB() {
		return labelRGB;
	}

	public JLabel getLabelOthers() {
		return labelOthers;
	}

	public ColorPanel getPanel() {
		return panel;
	}

	public void setLabelCoord(JLabel labelCoord) {
		this.labelCoord = labelCoord;
	}

	public void setLabelRGB(JLabel labelRGB) {
		this.labelRGB = labelRGB;
	}

	public void setLabelOthers(JLabel labelOthers) {
		this.labelOthers = labelOthers;
	}

	public void setPanel(ColorPanel panel) {
		this.panel = panel;
	}

}
