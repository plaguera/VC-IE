package pane.filterdialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.Kernel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import filter.Filter;

public abstract class FilterDialog {
	
	private static final Font FONT = new Font("SansSerif", Font.BOLD, 14);
	
	public FilterDialog() {}
	public abstract Kernel launch();
	
	public static FilterDialog MEAN_DIALOG() {
		return new FilterDialog() {
			@Override
			public Kernel launch() {
				//ImageIcon icon = new ImageIcon("src/images/picture.png");
				JPanel dimensionPanel = new JPanel(new GridLayout(1,1));

				SpinnerNumberModel modelS = new SpinnerNumberModel(3, 3, 99, 2);
				JSpinner spinnerS = new JSpinner(modelS);
				spinnerS.setFont(FONT);
				spinnerS.setBorder(BorderFactory.createTitledBorder("Side (px)"));
				dimensionPanel.add(spinnerS);
				
				UIManager.put("OptionPane.minimumSize", new Dimension(300, 120));
				JOptionPane.showMessageDialog(null, dimensionPanel, "Mean Filter", JOptionPane.PLAIN_MESSAGE, null);
				
				int side = (Integer)spinnerS.getValue();
				int size = side*side;
				float[] aux = new float[size];
				for(int i = 0; i < size; i++)
					aux[i] = 1.0f / (float)size;
				return new Kernel(side, side, aux);
			}

		};
	}
	
	public static FilterDialog GAUSS_DIALOG() {
		return new FilterDialog() {
			@Override
			public Kernel launch() {
				ImageIcon icon = new ImageIcon("src/images/gauss.png");
				JPanel panel = new JPanel(new GridLayout(1,2));

				SpinnerNumberModel model = new SpinnerNumberModel(1.0, 1, 25, 0.5);
				JSpinner spinner = new JSpinner(model);
				spinner.setFont(FONT);
				spinner.setBorder(BorderFactory.createTitledBorder("Theta"));
				panel.add(spinner);
				
				UIManager.put("OptionPane.minimumSize", new Dimension(300, 120));
				JOptionPane.showMessageDialog(null, panel, "Gauss Filter", JOptionPane.PLAIN_MESSAGE, icon);
				
				return Filter.gauss((Double)spinner.getValue(), true);
			}

		};
	}

	public static FilterDialog CONVOLVE_DIALOG() {
		return new FilterDialog() {
			@Override
			public Kernel launch() {
				//ImageIcon icon = new ImageIcon("src/images/picture.png");
				FilterPanel filterPanel = new FilterPanel();
				
				//UIManager.put("OptionPane.minimumSize", new Dimension(300, 120));
				JOptionPane.showMessageDialog(null, filterPanel, "Convolve", JOptionPane.PLAIN_MESSAGE);
				return filterPanel.generateKernel();
			}

		};
	}
	
}
