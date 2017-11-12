package imageFrame;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ColorPanel extends JPanel {
	
	private final static int MARGIN = 3;
	private Color color;
	
	public ColorPanel() { ; }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillRect(MARGIN, 0, getWidth()-MARGIN, getHeight());
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		repaint();
	}

}
