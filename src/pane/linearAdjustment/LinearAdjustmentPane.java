package pane.linearAdjustment;

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
public class LinearAdjustmentPane extends Pane {

	public final static int MIN_BRIGHTNESS = -255;
	public final static int MAX_BRIGHTNESS = 510;
	public final static int MIN_CONTRAST = -255;
	public final static int MAX_CONTRAST = 510;

	private JSpinner spinnerBrightness, spinerContrast;

	private double brightness, contrast;

	public LinearAdjustmentPane(Image image) {
		super(image);

		brightness = getImage().brightness();
		contrast = getImage().contrast();

		setLayout(new BorderLayout());
		JPanel panel = new JPanel(new GridLayout(2, 1));

		setSpinnerBrightness(new JSpinner(new SpinnerNumberModel(brightness, MIN_BRIGHTNESS, MAX_BRIGHTNESS, 1)));
		getSpinnerBrightness().setBorder(BorderFactory.createTitledBorder("Brightness"));
		((JSpinner.DefaultEditor) getSpinnerBrightness().getEditor()).getTextField()
				.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(spinnerBrightness);

		setSpinerContrast(new JSpinner(new SpinnerNumberModel(contrast, MIN_CONTRAST, MAX_CONTRAST, 1)));
		getSpinerContrast().setBorder(BorderFactory.createTitledBorder("Contrast"));
		((JSpinner.DefaultEditor) getSpinerContrast().getEditor()).getTextField()
				.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(spinerContrast);

		add(panel, BorderLayout.CENTER);

		getSpinnerBrightness().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});

		getSpinerContrast().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				refreshBrCn();
			}
		});

		// setFocusable(false);
		getSpinnerBrightness().setFocusable(false);
		getSpinerContrast().setFocusable(false);
	}

	private void refreshBrCn() {
		double contrast = (Double) spinerContrast.getValue();
		double offset = (Double) spinnerBrightness.getValue();
		getImage().adjustment(offset, contrast);
	}

	public JSpinner getSpinnerBrightness() {
		return spinnerBrightness;
	}

	public JSpinner getSpinerContrast() {
		return spinerContrast;
	}

	public void setSpinnerBrightness(JSpinner spinnerBrightness) {
		this.spinnerBrightness = spinnerBrightness;
	}

	public void setSpinerContrast(JSpinner spinerContrast) {
		this.spinerContrast = spinerContrast;
	}

}