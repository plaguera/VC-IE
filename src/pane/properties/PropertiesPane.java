package pane.properties;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import image.Image;
import image.Pane;
import util.Color;

@SuppressWarnings("serial")
public class PropertiesPane extends Pane implements Observer {

	private JLabel categFormat, categResolution, categRange, categBrightness, categContrast, categDynRange,
			categEntropy;

	public PropertiesPane(Image image) {
		super(image);
		setLayout(new GridLayout(7, 1));
		setPreferredSize(new Dimension(200, 100));

		categFormat = new JLabel(getImage().getFormat());
		categFormat.setBorder(BorderFactory.createTitledBorder("Format"));
		categFormat.setHorizontalAlignment(SwingConstants.CENTER);
		categFormat.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categFormat);

		categResolution = new JLabel();
		categResolution.setBorder(BorderFactory.createTitledBorder("Resolution"));
		categResolution.setHorizontalAlignment(SwingConstants.CENTER);
		categResolution.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categResolution);

		categRange = new JLabel();
		categRange.setBorder(BorderFactory.createTitledBorder("Value Range"));
		categRange.setHorizontalAlignment(SwingConstants.CENTER);
		categRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categRange);

		categBrightness = new JLabel();
		categBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		categBrightness.setHorizontalAlignment(SwingConstants.CENTER);
		categBrightness.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categBrightness);

		categContrast = new JLabel();
		categContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		categContrast.setHorizontalAlignment(SwingConstants.CENTER);
		categContrast.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categContrast);

		categDynRange = new JLabel();
		categDynRange.setBorder(BorderFactory.createTitledBorder("Dynamic Range"));
		categDynRange.setHorizontalAlignment(SwingConstants.CENTER);
		categDynRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categDynRange);

		categEntropy = new JLabel();
		categEntropy.setBorder(BorderFactory.createTitledBorder("Entropy"));
		categEntropy.setHorizontalAlignment(SwingConstants.CENTER);
		categEntropy.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categEntropy);
		
		refresh();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isVisible())
			return;
		refresh();
	}
	
	private void refresh() {
		categResolution.setText(getImage().getResolution());
		Point range = Color.grayRange(getImage().get());
		categRange.setText("[" + range.x + " - " + range.y + "]");
		categBrightness.setText(String.valueOf(Color.brightness(getImage().get())));
		categContrast.setText(String.valueOf(Color.contrast(getImage().get())));
		categDynRange.setText(String.valueOf(Color.dynamicRange(getImage().get())));
		categEntropy.setText(String.valueOf(Color.shannonEntropy(getImage().get())));
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

}
