package pane.geodialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

public abstract class GeometricDialog {
	
	public GeometricDialog() { }
	public abstract double launch();
	
	public static final GeometricDialog Z = new GeometricDialog() {

		@Override
		public double launch() {
			return 0;
			// TODO Auto-generated method stub
			
		}
	};
	
	public static GeometricDialog SCALE_DIALOG() {
		return new GeometricDialog() {
			@Override
			public double launch() {
				ImageIcon icon = new ImageIcon("src/images/scale.png");
		        JPanel panel = new JPanel(new BorderLayout());
		        SpinnerNumberModel sModel = new SpinnerNumberModel(100, 0, 500, 1);
				JSpinner spinner = new JSpinner(sModel);
				spinner.setFont(new Font("SansSerif", Font.BOLD, 14));
				spinner.setBorder(BorderFactory.createTitledBorder("Rescale (%)"));
				panel.add(spinner, BorderLayout.CENTER);

		        UIManager.put("OptionPane.minimumSize",new Dimension(300, 120));
		        JOptionPane.showMessageDialog(null, panel, "Rescale Image", JOptionPane.PLAIN_MESSAGE, icon);
		        //System.out.println((Integer) spinner.getValue());
				return (double)((Integer) spinner.getValue() / 100.0d);
			}
			
		};
	}
	
	public static GeometricDialog ROTATE_DIALOG() {
		return new GeometricDialog() {
			@Override
			public double launch() {
				ImageIcon icon = new ImageIcon("src/images/rotate.png");
		        JPanel panel = new JPanel(new BorderLayout());
		        SpinnerNumberModel sModel = new SpinnerNumberModel(0, 0, 3600, 90);
				JSpinner spinner = new JSpinner(sModel);
				spinner.setFont(new Font("SansSerif", Font.BOLD, 14));
				spinner.setBorder(BorderFactory.createTitledBorder("Rotate (º)"));
				panel.add(spinner, BorderLayout.CENTER);

		        UIManager.put("OptionPane.minimumSize",new Dimension(300, 120));
		        JOptionPane.showMessageDialog(null, panel, "Rotate Image", JOptionPane.PLAIN_MESSAGE, icon);
				return (Integer) spinner.getValue();
			}
			
		};
	}

}
