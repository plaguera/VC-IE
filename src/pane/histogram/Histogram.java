package pane.histogram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;

import image.Image;
import image.Pane;

@SuppressWarnings("serial")
public class Histogram extends Pane implements Observer {

	public static final int STANDARD = 1;
	public static final int CUMULATIVE = 2;
	public static final int NORMALIZED = 3;
	public static final int MARGIN = 10;

	private int[][] data;
	private int[] values;
	private int channel;
	private JPopupMenu popup;
	private int mode = 1;
	private Color color;


	public Histogram(Image image, int channel, Color color) {
		super(image);
		getImage().addObserver(this);
		setChannel(channel);
		setData(utils.Color.matrix(getImage().get(), getChannel()));
		setColor(color);
		setPopup(new JPopupMenu("Edit"));
		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Standard");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				setMode(STANDARD);
				repaint();
			}
		});
		menuItem.setSelected(true);
		group.add(menuItem);
		getPopup().add(menuItem);

		menuItem = new JRadioButtonMenuItem("Cumulative");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				setMode(CUMULATIVE);
				repaint();
			}
		});
		group.add(menuItem);
		getPopup().add(menuItem);

		menuItem = new JRadioButtonMenuItem("Normalized");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				setMode(NORMALIZED);
				repaint();
			}
		});
		group.add(menuItem);
		getPopup().add(menuItem);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
					getPopup().show(e.getComponent(), e.getX(), e.getY());
			}
        });
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBars(g, getValues(getMode()));
		paintMargin(g);
	}

	private void paintBars(Graphics g, int[] data) {
		int width = getWidth() - (MARGIN * 2);
		int height = getHeight() - (MARGIN * 2);
		Graphics2D g2d = (Graphics2D) g.create();
		double barWidth = (double) width / 256d;
		int maxValue = 0;
		for (Integer value : data)
			maxValue = Math.max(maxValue, value);
		double xPos = MARGIN;
		for (int i = 0; i < data.length; i++) {
			g2d.setColor(new Color(i, i, i));
			int value = data[i];
			double barHeight = ((double) value / (double) maxValue) * (double) height;
			double yPos = height + MARGIN - barHeight;
			// Rectangle bar = new Rectangle(xPos, yPos, barWidth, barHeight);
			Rectangle2D bar = new Rectangle2D.Float((int) xPos, (int) yPos, (int) barWidth, (int) barHeight);
			g2d.fill(bar);
			g2d.setColor(getColor());

			g2d.draw(bar);
			xPos += barWidth;
		}
		g2d.dispose();
	}

	private int[] getValues(int mode) {
		int[] values = new int[256];
		for (int i = 0; i < values.length; i++)
			values[i] = 0;

		for (int[] row : getData())
			for (int i : row)
				values[i] = values[i] + 1;
		
		if (mode == CUMULATIVE)
			for (int i = 1; i < 256; i++)
				values[i] += values[i - 1];
		else if (mode == NORMALIZED) {
			int sum = 0;
			for (int i : values)
				sum += i;
			for (int i = 0; i < values.length; i++) {
				double norm = ((double) values[i] / (double) sum) * 1E6d;
				values[i] = (int)norm;
			}
		}
		this.values = values;
		return values;
	}

	private void paintMargin(Graphics g) {
		int width = getWidth() - (MARGIN * 2);
		int height = getHeight() - (MARGIN * 2);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawRect(MARGIN, MARGIN, width, height);
	}

	public int[][] getData() { return data; }
	public JPopupMenu getPopup() { return popup; }
	public int getMode() { return mode; }
	public Color getColor() { return color; }
	public void setData(int[][] data) { this.data = data; }
	public void setPopup(JPopupMenu popup) { this.popup = popup; }
	public void setMode(int mode) { this.mode = mode; }
	public void setColor(Color color) { this.color = color; }

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(o == getImage()) {
			setData(utils.Color.matrix(((Image)o).get(), channel));
		}
	}

}
