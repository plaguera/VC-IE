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

		categResolution = new JLabel(getImage().getResolution());
		categResolution.setBorder(BorderFactory.createTitledBorder("Resolution"));
		categResolution.setHorizontalAlignment(SwingConstants.CENTER);
		categResolution.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categResolution);

		Point range = getImage().grayRange();
		categRange = new JLabel("[" + range.x + " - " + range.y + "]");
		categRange.setBorder(BorderFactory.createTitledBorder("Value Range"));
		categRange.setHorizontalAlignment(SwingConstants.CENTER);
		categRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categRange);

		categBrightness = new JLabel(String.valueOf(getImage().brightness()));
		categBrightness.setBorder(BorderFactory.createTitledBorder("Brightness"));
		categBrightness.setHorizontalAlignment(SwingConstants.CENTER);
		categBrightness.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categBrightness);

		categContrast = new JLabel(String.valueOf(getImage().contrast()));
		categContrast.setBorder(BorderFactory.createTitledBorder("Contrast"));
		categContrast.setHorizontalAlignment(SwingConstants.CENTER);
		categContrast.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categContrast);

		categDynRange = new JLabel(String.valueOf(getImage().dynamicRange()));
		categDynRange.setBorder(BorderFactory.createTitledBorder("Dynamic Range"));
		categDynRange.setHorizontalAlignment(SwingConstants.CENTER);
		categDynRange.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categDynRange);

		categEntropy = new JLabel(String.valueOf(getImage().shannonEntropy()));
		categEntropy.setBorder(BorderFactory.createTitledBorder("Entropy"));
		categEntropy.setHorizontalAlignment(SwingConstants.CENTER);
		categEntropy.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		add(categEntropy);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isVisible())
			return;
		// categFormat.setText(getImage().getFormat());

		categResolution.setText(getImage().getResolution());

		categRange.setText("[" + getImage().grayRange().x + " - " + getImage().grayRange().y + "]");

		categBrightness.setText(String.valueOf(getImage().brightness()));

		categContrast.setText(String.valueOf(getImage().contrast()));

		categDynRange.setText(String.valueOf(getImage().dynamicRange()));

		categEntropy.setText(String.valueOf(getImage().shannonEntropy()));
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

}
