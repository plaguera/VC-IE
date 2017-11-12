package image;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Pane extends JPanel {
	
	private Image image;
	
	public Pane(Image image) {
		setImage(image);
		setPreferredSize(new Dimension(200, 100));
	}

	public Image getImage() { return image; }
	public void setImage(Image image) { this.image = image; }

}
