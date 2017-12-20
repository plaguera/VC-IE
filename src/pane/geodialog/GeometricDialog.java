package pane.geodialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.AffineTransformOp;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import image.Image;

public abstract class GeometricDialog {

	public GeometricDialog() {}
	public abstract int[] launch(Image image);

	public static GeometricDialog SCALE_DIALOG() {
		return new GeometricDialog() {
			@Override
			public int[] launch(Image image) {
				ImageIcon icon = new ImageIcon("src/images/scale.png");
				JPanel panel = new JPanel(new BorderLayout());
				SpinnerNumberModel wModel = new SpinnerNumberModel(image.getWidth(), 0, image.getWidth()*4, 1);
				JSpinner spinner1 = new JSpinner(wModel);
				spinner1.setFont(new Font("SansSerif", Font.BOLD, 14));
				spinner1.setBorder(BorderFactory.createTitledBorder("Width (px)"));
				panel.add(spinner1, BorderLayout.NORTH);

				SpinnerNumberModel hModel = new SpinnerNumberModel(image.getHeight(), 0, image.getHeight()*4, 1);
				JSpinner spinner2 = new JSpinner(hModel);
				spinner2.setFont(new Font("SansSerif", Font.BOLD, 14));
				spinner2.setBorder(BorderFactory.createTitledBorder("Height (px)"));
				panel.add(spinner2, BorderLayout.CENTER);
				
				String[] algorithm = new String[] { "Bilinear Interpolation", "Nearest Neighbor"};
				JComboBox<String> algList = new JComboBox<>(algorithm);
				panel.add(algList, BorderLayout.SOUTH);

				UIManager.put("OptionPane.minimumSize", new Dimension(300, 120));
				JOptionPane.showMessageDialog(null, panel, "Rescale Image", JOptionPane.PLAIN_MESSAGE, icon);
				// System.out.println((Integer) spinner.getValue());
				return new int[] { 	(Integer) spinner1.getValue(),
									(Integer) spinner2.getValue(),
									((String) algList.getSelectedItem()).equals("Bilinear Interpolation") ? AffineTransformOp.TYPE_BILINEAR : AffineTransformOp.TYPE_NEAREST_NEIGHBOR};
			}

		};
	}

	public static GeometricDialog ROTATE_DIALOG() {
		return new GeometricDialog() {
			@Override
			public int[] launch(Image image) {
				ImageIcon icon = new ImageIcon("src/images/rotate.png");
				JPanel panel = new JPanel(new BorderLayout());

				String[] direction = new String[] { "Left", "Right"};
				JComboBox<String> dirList = new JComboBox<>(direction);
				panel.add(dirList, BorderLayout.NORTH);

				SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 360, 1);
				JSpinner spinner = new JSpinner(sModel);
				spinner.setFont(new Font("SansSerif", Font.BOLD, 14));
				spinner.setBorder(BorderFactory.createTitledBorder("Rotate (º)"));
				panel.add(spinner, BorderLayout.SOUTH);

				UIManager.put("OptionPane.minimumSize", new Dimension(300, 120));
				JOptionPane.showMessageDialog(null, panel, "Rotate Image", JOptionPane.PLAIN_MESSAGE, icon);
				return new int[] { 	((String) dirList.getSelectedItem()).equals("Left") ? -1 : 1,
									(Integer) spinner.getValue()};
			}

		};
	}

	/*
	 * public static GeometricDialog ROTATE_DIALOG() { return new GeometricDialog()
	 * {
	 * 
	 * @Override public double launch() { ImageIcon icon = new
	 * ImageIcon("src/images/rotate.png"); JPanel panel = new JPanel(new
	 * BorderLayout()); SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0,
	 * 3600, 90); JSpinner spinner = new JSpinner(sModel); spinner.setFont(new
	 * Font("SansSerif", Font.BOLD, 14));
	 * spinner.setBorder(BorderFactory.createTitledBorder("Rotate (º)"));
	 * panel.add(spinner, BorderLayout.CENTER);
	 * 
	 * UIManager.put("OptionPane.minimumSize",new Dimension(300, 120));
	 * JOptionPane.showMessageDialog(null, panel, "Rotate Image",
	 * JOptionPane.PLAIN_MESSAGE, icon); return (Integer) spinner.getValue(); }
	 * 
	 * }; }
	 */

}
