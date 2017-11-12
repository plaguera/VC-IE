package pane.digitalization;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import image.Image;
import image.Pane;

@SuppressWarnings("serial")
public class DigitalizationPane extends Pane {
	
	private JSpinner sampleSpinner, colorSpinner;
	
	public DigitalizationPane(Image image) {
		super(image);
		
		setLayout(new BorderLayout());
		
		setSampleSpinner(new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)));
		getSampleSpinner().setBorder(BorderFactory.createTitledBorder("Samples"));
		((JSpinner.DefaultEditor)getSampleSpinner().getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);

		
		setColorSpinner(new JSpinner(new SpinnerNumberModel(8, 1, 8, 1)));
		getColorSpinner().setBorder(BorderFactory.createTitledBorder("Color"));
		((JSpinner.DefaultEditor)getColorSpinner().getEditor()).getTextField().setHorizontalAlignment(SwingConstants.CENTER);


		JPanel aux = new JPanel(new GridLayout(2,1));
		aux.add(getSampleSpinner());
		aux.add(getColorSpinner());
		add(aux, 	BorderLayout.CENTER);
		
		getSampleSpinner().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				getImage().downsample((Integer) getSampleSpinner().getValue());
			}
		});
		
		getColorSpinner().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				int bits = (Integer) getColorSpinner().getValue();
				getImage().changeColorDepth(bits);
			}
		});
	}

	public JSpinner getSampleSpinner() { return sampleSpinner; }
	public JSpinner getColorSpinner() { return colorSpinner; }
	public void setSampleSpinner(JSpinner sampleSpinner) { this.sampleSpinner = sampleSpinner; }
	public void setColorSpinner(JSpinner colorSpinner) { this.colorSpinner = colorSpinner; }

}
