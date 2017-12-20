package utils;

import java.awt.Color;

public class HistogramLayer {
	
	private int[] values;
	private Color color;
	private boolean isVisible;
	
	public HistogramLayer(int[] values, Color color, boolean visible) {
		setValues(values);
		setColor(color);
		setVisible(visible);
	}

	public int[] getValues() { return values; }

	public void setValues(int[] values) { this.values = values; }

	public Color getColor() { return color; }

	public void setColor(Color color) { this.color = color; }

	public boolean isVisible() { return isVisible; }

	public void setVisible(boolean isVisible) { this.isVisible = isVisible; }

}
