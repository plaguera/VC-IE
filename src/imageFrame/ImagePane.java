package imageFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import image.Image;
import image.Panel;
import image.RGB;
import listener.MousePixelListener;
import pane.difference.DifferencePane;
import pane.digitalization.DigitalizationPane;
import pane.gamma.GammaCorrectionPane;
import pane.linearAdjustment.LinearAdjustmentPane;
import pane.linearTransformation.FunctionSegment;
import pane.linearTransformation.LinearTranformationPane;

@SuppressWarnings("serial")
public class ImagePane extends Panel {

	private final static Color ROI_COLOR = new Color(211,211,211,127);
	
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
		// getTabbedPane().setFocusable(true);
		getBtnReset().setFocusable(false);
		getBtnReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getImage().reset();
			}
		});
		
	}

	public Image getImage() { return image; }
	public JTabbedPane getTabbedPane() { return tabbedPane; }
	public ImagePanel getImagePanel() { return imagePanel; }
	public PixelColorPanel getPixelColorPanel() { return pixelColorPanel; }
	public JButton getBtnReset() { return btnReset; }
	public void setImage(Image image) { this.image = image; }
	public void setTabbedPane(JTabbedPane tabbedPane) { this.tabbedPane = tabbedPane; }
	public void setImagePanel(ImagePanel imagePanel) { this.imagePanel = imagePanel; }
	public void setPixelColorPanel(PixelColorPanel pixelColorPanel) { this.pixelColorPanel = pixelColorPanel; }
	public void setBtnReset(JButton btnReset) { this.btnReset = btnReset; }
	
	public class ImagePanel extends JPanel {
		private Point rP1, rP2;
		private boolean isDrag, isROI, isCS;
		public ImagePanel() {
			setPreferredSize(scaleBack());
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					if(isROI() || isCS())
						setrP1(e.getPoint());
				} 

				@Override
				public void mouseReleased(MouseEvent e) {
					if((isROI() || isCS()) && isDrag()) {
						setrP2(e.getPoint());
						int w = Math.abs(getrP1().x - getrP2().x);
						int h = Math.abs(getrP1().y - getrP2().y);
						int x = Math.min(getrP1().x, getrP2().x);
						int y = Math.min(getrP1().y, getrP2().y);
						if(isROI()) {
							getImage().subimage(x, y, w, h);
							setROI(false);
						}
						if(isCS()) {
							FunctionSegment aux = new FunctionSegment(getrP1(), getrP2());
							int minX = Math.min(getrP1().x, getrP2().x);
							int maxX = Math.max(getrP1().x, getrP2().x);
							RGB[] pixels = new RGB[maxX - minX];
							int k = 0;
							for (x = minX; x < maxX; x++) {
								y = (int) aux.f(x);
								pixels[k++] = new RGB(image.get().getRGB(x, y));
							}
							BufferedImage image = new BufferedImage(maxX-minX+1, 1, BufferedImage.TYPE_INT_RGB);
							for(int i = 0; i < pixels.length; i++)
								image.setRGB(i, 0, pixels[i].toInt());
							getImage().getDll().add(image);
							getImage().changed();
							setCS(false);
						}
						setrP1(null);
						setrP2(null);
						setPreferredSize(scaleBack());
						repaint();
					}
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if(isDrag())
						setrP2(e.getPoint());
					repaint();
				}
			});
		}

		private Dimension scaleBack() {
			int width = getImage().getWidth();
			int height = getImage().getHeight();
			int maxW = (int) ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.75);
			int maxH = (int) ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.75);
			int auxW = width, auxH = height;
			while (auxW > maxW || auxH > maxH) {
				auxW -= (int) (0.1 * (double) width);
				auxH -= (int) (0.1 * (double) height);
			}
			return new Dimension(auxW, auxH);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.get(), 0, 0, getWidth(), getHeight(), this);
			if (isROI() && isDrag()) {
				g.setColor(ROI_COLOR);
				int w = Math.abs(getrP1().x - getrP2().x);
				int h = Math.abs(getrP1().y - getrP2().y);
				int x = Math.min(getrP1().x, getrP2().x);
				int y = Math.min(getrP1().y, getrP2().y);
				g.fillRect(x, y, w, h);
			}
			
			if (isCS() && isDrag()) {
				g.setColor(Color.GREEN);
				g.drawLine(getrP1().x, getrP1().y, getrP2().x, getrP2().y);
				int side = 6;
				g.setColor(Color.BLUE);
				g.fillOval(getrP1().x - side/2, getrP1().y - side/2, side, side);
				g.fillOval(getrP2().x - side/2, getrP2().y - side/2, side, side);
			}
		}

		public Point 	getrP1() { return rP1; }
		public Point 	getrP2() { return rP2; }
		public boolean 	isDrag() { return isDrag; }
		public boolean 	isROI() { return isROI; }
		public boolean 	isCS() { return isCS; }
		public void setrP1(Point rP1) 		{ this.rP1 = rP1; }
		public void setrP2(Point rP2) 		{ this.rP2 = rP2; }
		public void setDrag(boolean isDrag) 	{ this.isDrag = isDrag; }
		public void setROI(boolean isROI) 	{ this.isROI = isROI; setDrag(isROI); }
		public void setCS(boolean isCS) 		{ this.isCS = isCS; setDrag(isCS); }

	}

}
