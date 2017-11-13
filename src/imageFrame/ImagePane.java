package imageFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import image.Image;
import image.Panel;
import listener.MousePixelListener;
import pane.difference.DifferencePane;
import pane.digitalization.DigitalizationPane;
import pane.gamma.GammaCorrectionPane;
import pane.linearAdjustment.LinearAdjustmentPane;
import pane.linearTransformation.LinearTranformationPane;

@SuppressWarnings("serial")
public class ImagePane extends Panel {
	
	private Image image;
	private JTabbedPane tabbedPane;
	private ImagePanel imagePanel;
	private PixelColorPanel pixelColorPanel;
	
	private JButton btnReset;
	
	public ImagePane(Image image) {
		setImage(image);
		setTabbedPane(new JTabbedPane(JTabbedPane.TOP));
		setImagePanel(this.new ImagePanel());
		setPixelColorPanel(new PixelColorPanel());
		setBtnReset(new JButton("Reset"));
		getImagePanel().addMouseMotionListener(new MousePixelListener(getPixelColorPanel(), getImage()));
		JPanel aux = new JPanel(new BorderLayout());
		aux.add(getTabbedPane(), BorderLayout.NORTH);
		aux.add(getPixelColorPanel(), BorderLayout.CENTER);
		aux.add(getBtnReset(), BorderLayout.SOUTH);
		add(getImagePanel(), BorderLayout.CENTER);
		add(aux, BorderLayout.EAST);
		getTabbedPane().addTab("Brightness / Contrast", new LinearAdjustmentPane(getImage()));
		getTabbedPane().addTab("Gamma Correction", new GammaCorrectionPane(getImage()));
		getTabbedPane().addTab("Digitalization Simulation", new DigitalizationPane(getImage()));
		getTabbedPane().addTab("Linear Transformation", new LinearTranformationPane(getImage()));
		getTabbedPane().addTab("Difference", new DifferencePane(getImage()));
		getTabbedPane().setFocusable(false);
		getBtnReset().setFocusable(false);
		
		getBtnReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().reset();
			}
		});
	}
	
	public Image 			getImage() 								{ return image; }
	public JTabbedPane 		getTabbedPane() 							{ return tabbedPane; }
	public ImagePanel 		getImagePanel() 							{ return imagePanel; }
	public PixelColorPanel 	getPixelColorPanel() 					{ return pixelColorPanel; }
	public JButton 			getBtnReset() 							{ return btnReset; }
	public void setImage(Image image) 								{ this.image = image; }
	public void setTabbedPane(JTabbedPane tabbedPane) 				{ this.tabbedPane = tabbedPane; }
	public void setImagePanel(ImagePanel imagePanel) 					{ this.imagePanel = imagePanel; }
	public void setPixelColorPanel(PixelColorPanel pixelColorPanel) 	{ this.pixelColorPanel = pixelColorPanel; }
	public void setBtnReset(JButton btnReset) 						{ this.btnReset = btnReset; }

	public class ImagePanel extends JPanel {
		public ImagePanel() {
			setPreferredSize(scaleBack());
		}
		
		private Dimension scaleBack() {
			int width = getImage().getWidth();
			int height = getImage().getHeight();
			int maxW = (int) ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.75);
			int maxH = (int) ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.75);
			int auxW = width, auxH = height;
			while(auxW > maxW || auxH > maxH) {
				auxW -= (int)(0.1*(double)width);
				auxH -= (int)(0.1*(double)height);
			}
			return new Dimension(auxW,auxH);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.get(), 0, 0, getWidth(), getHeight(), this);
		}

		public double getScale() {
			return (double) ((double) image.getWidth() / (double) getWidth());
		}
	}

}
