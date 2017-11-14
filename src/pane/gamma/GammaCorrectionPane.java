package pane.gamma;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import image.Image;
import image.Pane;

@SuppressWarnings("serial")
public class GammaCorrectionPane extends Pane {

	public final static double MIN_GAMMA = 0.01;
	public final static double MAX_GAMMA = 40.00;

	private JSpinner spinnerGamma;
	private JLabel lbFormula;

	public GammaCorrectionPane(Image image) {
		super(image);

		setLayout(new BorderLayout());
		JPanel panel = new JPanel(new GridLayout(2, 1));

		setSpinnerGamma(new JSpinner(new SpinnerNumberModel(1.00, MIN_GAMMA, MAX_GAMMA, 0.01)));
		getSpinnerGamma().setBorder(BorderFactory.createTitledBorder("Gamma Correction"));
		((JSpinner.DefaultEditor) getSpinnerGamma().getEditor()).getTextField()
				.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(getSpinnerGamma());

		setLBFormula(new JLabel(
				"<html>I' = 255 x (<sup>I </sup>&frasl;<sub> 255</sub>)<sup><sup>1 </sup>&frasl;<sub> &gamma</sub></sup></html>",
				SwingConstants.CENTER));
		getLBFormula().setBorder(BorderFactory.createTitledBorder(""));
		getLBFormula().setHorizontalTextPosition(SwingConstants.CENTER);
		panel.add(getLBFormula());

		add(panel, BorderLayout.CENTER);

		getSpinnerGamma().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				double gamma = (Double) getSpinnerGamma().getValue();
				getImage().gamma(gamma);
			}
		});

	}

	public JLabel getLBFormula() {
		return lbFormula;
	}

	public void setLBFormula(JLabel lbFormula) {
		this.lbFormula = lbFormula;
	}

	public JSpinner getSpinnerGamma() {
		return spinnerGamma;
	}

	public void setSpinnerGamma(JSpinner spinnerGamma) {
		this.spinnerGamma = spinnerGamma;
	}

}
