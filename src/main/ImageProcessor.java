package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import imageFrame.ImageFrame;
import utils.ImageUtils;

public class ImageProcessor {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ImageUtils.launchFrame(new ImageFrame("/Users/peter/Drive/VC/lena.png"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
