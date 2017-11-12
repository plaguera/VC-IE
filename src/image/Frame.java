package image;

import javax.swing.JFrame;

import imageFrame.ImageFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	public ImageFrame parent;

	public Frame(ImageFrame parent) {
		setParent(parent);
		//setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		if(getParent() != null)
			setTitle(getParent().getTitle());
	}

	@Override
	public ImageFrame getParent() { return parent; }
	public void setParent(ImageFrame parent) { this.parent = parent; }
	public void showParent() { parent.setVisible(true); }
	
	
}
