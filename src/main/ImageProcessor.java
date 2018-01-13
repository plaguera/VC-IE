package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import imageFrame.ImageFrame;
import util.ImageUtils;

public class ImageProcessor {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ImageUtils.launchFrame(new ImageFrame(startWindow()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static String startWindow() {
		ImageIcon icon = new ImageIcon("src/images/picture.png");
		JPanel panel      = new JPanel();
		JPanel outerPanel = new JPanel();
		panel.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 0) );
		JTextField textField = new JTextField(10);
		textField.setBorder(BorderFactory.createTitledBorder("Path to Image"));
		panel.add(textField);
		JButton button = new JButton("Open...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ImageUtils.openImage();
				if (selectedFile == null)
					return;
				textField.setText(selectedFile.getAbsolutePath());
			}
		});
		panel.add(button);
		panel.setBackground( textField.getBackground() );
		panel.setBorder( textField.getBorder() );
		textField.setBorder(null);
		//textField.setText("/Users/peter/Drive/VC/marbles.bmp");
		textField.setText("/Users/peter/Desktop/aux1.bmp");
		outerPanel.add(panel);
		
        UIManager.put("OptionPane.minimumSize",new Dimension(300, 120));
        JOptionPane.showMessageDialog(null, outerPanel, "Open Image", JOptionPane.PLAIN_MESSAGE, icon);
        
        return textField.getText();
	}
}
