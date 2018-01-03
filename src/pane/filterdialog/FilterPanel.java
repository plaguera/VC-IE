package pane.filterdialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.Kernel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel {
	
	private Font FONT = new Font("SansSerif", Font.BOLD, 14);
	private int width = 0, height = 0;
	private JSpinner[][] spinners;
	private JPanel dimensionPanel, dataPanel;
	
	public FilterPanel() {
		setKWidth(3);
		setKHeight(3);
		
		setLayout(new BorderLayout());
		dimensionPanel = new JPanel(new GridLayout(1,2));

		SpinnerNumberModel modelW = new SpinnerNumberModel(getKWidth(), 3, 21, 2);
		JSpinner spinnerW = new JSpinner(modelW);
		spinnerW.setFont(FONT);
		spinnerW.setBorder(BorderFactory.createTitledBorder("Width (px)"));
		dimensionPanel.add(spinnerW);

		SpinnerNumberModel modelH = new SpinnerNumberModel(getKHeight(), 3, 21, 2);
		JSpinner spinnerH = new JSpinner(modelH);
		spinnerH.setFont(FONT);
		spinnerH.setBorder(BorderFactory.createTitledBorder("Height (px)"));
		dimensionPanel.add(spinnerH);
		
		dimensionPanel.setBorder(BorderFactory.createTitledBorder("Kernel Dimensions"));
		
		spinnerW.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setKWidth((Integer) spinnerW.getValue());
				resetSpinners();
				repaint();
			}
		});
		spinnerH.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setKHeight((Integer) spinnerH.getValue());
				resetSpinners();
				repaint();
			}
		});

		dataPanel = new JPanel();
		resetSpinners();
		add(dimensionPanel, BorderLayout.NORTH);
		add(dataPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	private void resetSpinners() {
		if(getKWidth() == 0 && getKHeight() == 0)
			return;
		setSpinners(new JSpinner[getKWidth()][getKHeight()]);
		dataPanel.removeAll();
		dataPanel.setLayout(new GridLayout(getKHeight(), getKWidth()));
		for(int i = 0; i < spinners.length; i++) {
			for(int j = 0; j < spinners[i].length; j++) {
				getSpinners()[i][j] = new JSpinner(new SpinnerNumberModel(0, -10, 10, 1));
				dataPanel.add(getSpinners()[i][j]);
			}
		}
		repaint();
		validate();
	}
	
	public Kernel generateKernel() {
		float coefficient = 1.0f / 1.0f;
		int width = getKWidth(), height = getKHeight();
		float[] data = new float[width * height];
		int iterator = 0;
		for(int i = 0; i < spinners.length; i++) {
			for(int j = 0; j < spinners[i].length; j++) {
				int value = (Integer) getSpinners()[i][j].getValue();
				data[iterator++] = value * coefficient;
			}
		}
		return new Kernel(width, height, data);
	}
	
	public int getKWidth() { return width; }
	public int getKHeight() { return height; }
	public JSpinner[][] getSpinners() { return spinners; }
	public JPanel getDimensionPanel() { return dimensionPanel; }
	public JPanel getDataPanel() { return dataPanel; }
	public void setKWidth(int width) { this.width = width; }
	public void setKHeight(int height) { this.height = height; }
	public void setSpinners(JSpinner[][] spinners) { this.spinners = spinners; }
	public void setDimensionPanel(JPanel dimensionPanel) { this.dimensionPanel = dimensionPanel; }
	public void setDataPanel(JPanel dataPanel) { this.dataPanel = dataPanel; }

}
