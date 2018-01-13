package pane.difference;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import image.Image;
import image.Pane;
import imageFrame.ImageFrame;
import util.ImageUtils;

@SuppressWarnings("serial")
public class DifferencePane extends Pane {

	public final static int MIN_THRESHOLD = 0;
	public final static int MAX_THRESHOLD = 255;

	private JSpinner spinnerThreshold;

	private JTextField tfFilePath;
	private JButton btnFile, btnDifference, btnChangeMap;

	public DifferencePane(Image image) {
		super(image);

		JPanel textFieldWithButtonsPanel = new JPanel(new FlowLayout(SwingConstants.LEADING, 5, 1));
		setTFFilePath(new JTextField(10));
		textFieldWithButtonsPanel.add(getTFFilePath());

		setBtnFile(new JButton("Open..."));
		setBtnDifference(new JButton("Difference"));
		setBtnChangeMap(new JButton("Change Map"));

		getBtnFile().setContentAreaFilled(false);
		// getBtnFile().setBorderPainted(false);
		getBtnFile().setMargin(new Insets(0, 0, 0, 0));
		textFieldWithButtonsPanel.add(getBtnFile());

		textFieldWithButtonsPanel.setBackground(getTFFilePath().getBackground());
		textFieldWithButtonsPanel.setBorder(getTFFilePath().getBorder());
		getTFFilePath().setBorder(null);

		getBtnFile().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if (selectedFile == null)
					return;
				getTFFilePath().setText(selectedFile.getAbsolutePath());
			}
		});
		getBtnDifference().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getTFFilePath().getText() == null || getTFFilePath().getText().equals(""))
					return;
				ImageUtils.launchFrame(
						new ImageFrame(getImage().difference(ImageUtils.readImage(getTFFilePath().getText()))));
			}
		});
		getBtnChangeMap().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getTFFilePath().getText() == null || getTFFilePath().getText().equals(""))
					return;
				int threshold = (int) getSpinnerThreshold().getValue();
				ImageUtils.launchFrame(new ImageFrame(getImage().colorChangeMap(threshold)));
			}
		});

		setSpinnerThreshold(new JSpinner(new SpinnerNumberModel(128, MIN_THRESHOLD, MAX_THRESHOLD, 1)));
		getSpinnerThreshold().setBorder(BorderFactory.createTitledBorder("Threshold"));
		getSpinnerThreshold().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
			}
		});

		// END WARNING:
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.add(getSpinnerThreshold());
		panel.add(getBtnDifference());
		panel.add(getBtnChangeMap());
		add(textFieldWithButtonsPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);

	}

	public JSpinner getSpinnerThreshold() {
		return spinnerThreshold;
	}

	public void setSpinnerThreshold(JSpinner spinnerThreshold) {
		this.spinnerThreshold = spinnerThreshold;
	}

	public JTextField getTFFilePath() {
		return tfFilePath;
	}

	public void setTFFilePath(JTextField tfFilePath) {
		this.tfFilePath = tfFilePath;
	}

	public JButton getBtnFile() {
		return btnFile;
	}

	public void setBtnFile(JButton btnFile) {
		this.btnFile = btnFile;
	}

	public JButton getBtnDifference() {
		return btnDifference;
	}

	public void setBtnDifference(JButton btnDifference) {
		this.btnDifference = btnDifference;
	}

	public JButton getBtnChangeMap() {
		return btnChangeMap;
	}

	public void setBtnChangeMap(JButton btnChangeMap) {
		this.btnChangeMap = btnChangeMap;
	}

}
