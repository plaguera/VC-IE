package pane.histogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import utils.HistogramLayer;

@SuppressWarnings("serial")
public class HistogramPaneTab extends JPanel {

	protected static final int MIN_BAR_WIDTH = 2;
	
	private JPopupMenu popup;
	private List<HistogramLayer> layers;

	public HistogramPaneTab() {
		int width = (256 * MIN_BAR_WIDTH) + 11;
		setMinimumSize(new Dimension(width, 128));
		setPreferredSize(new Dimension(width, 256));
		popup = new JPopupMenu("Edit");
		layers = new ArrayList<HistogramLayer>();
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBorder(g);
		for (HistogramLayer layer : layers)
			if (layer.isVisible())
				drawHistogramLayer(g, layer);
	}

	private void drawBorder(Graphics g) {
		int xOffset = 5;
		int yOffset = 5;
		int width = getWidth() - 1 - (xOffset * 2);
		int height = getHeight() - 1 - (yOffset * 2);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(xOffset, yOffset, width, height);
	}

	public void newHistogramLayer(int[] values, Color color, boolean visible, String text) {
		HistogramLayer newLayer = new HistogramLayer(values, color, visible);
		JCheckBoxMenuItem newCheckBoxMenuItem = new JCheckBoxMenuItem(text, visible);
		newCheckBoxMenuItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				newLayer.setVisible(newCheckBoxMenuItem.getState());
				layers.add(layers.size()-1, layers.remove(layers.indexOf(newLayer)));
				repaint();
			}
		});
		popup.add(newCheckBoxMenuItem);
		layers.add(newLayer);
	}
	
	public void deleteHistogramLayer() {
		if(!getLayers().isEmpty())
			getLayers().remove(0);
	} 

	private void drawHistogramLayer(Graphics g, HistogramLayer layer) {
		if (layer != null) {
			int xOffset = 5;
			int yOffset = 5;
			int width = getWidth() - 1 - (xOffset * 2);
			int height = getHeight() - 1 - (yOffset * 2);
			Graphics2D g2d = (Graphics2D) g.create();
			int barWidth = Math.max(MIN_BAR_WIDTH, (int) Math.floor((float) width / (float) layer.getValues().length));
			int maxValue = 0;
			for (Integer value : layer.getValues()) {
				maxValue = Math.max(maxValue, value);
			}
			int xPos = xOffset;
			for (int i = 0; i < layer.getValues().length; i++) {
				int value = layer.getValues()[i];
				int barHeight = Math.round(((float) value / (float) maxValue) * height);
				g2d.setColor(new Color(i, i, i));
				int yPos = height + yOffset - barHeight;
				// Rectangle bar = new Rectangle(xPos, yPos, barWidth, barHeight);
				Rectangle2D bar = new Rectangle2D.Float(xPos, yPos, barWidth, barHeight);
				g2d.fill(bar);
				g2d.setColor(layer.getColor());

				g2d.draw(bar);
				xPos += barWidth;
			}
			g2d.dispose();
		}
	}

	public List<HistogramLayer> getLayers() {
		return layers;
	}

	public void setLayers(List<HistogramLayer> layers) {
		this.layers = layers;
	}

}
