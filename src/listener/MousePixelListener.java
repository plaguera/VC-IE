package listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import image.Image;
import imageFrame.PixelColorPanel;

public class MousePixelListener implements MouseMotionListener {

	private PixelColorPanel panel;
	private Image image;

	public MousePixelListener(PixelColorPanel panel, Image image) {
		setPanel(panel);
		setImage(image);
	}
	
	public MousePixelListener(MousePixelListener mousePixelListener) {
		setPanel(mousePixelListener.getPanel());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(x > image.getWidth() || x < 0 || y > image.getHeight() || y < 0)
			return;
		getPanel().setColor(image.get().getRGB(x, y), x, y);
		//getPanel().repaint();
	}

	public PixelColorPanel getPanel() {
		return panel;
	}

	public void setPanel(PixelColorPanel panel) {
		this.panel = panel;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

}
