package pane.linearTransformation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JScrollPane;

import image.Image;
import image.Pane;
import utils.ImageUtils;

@SuppressWarnings("serial")
public class LinearTranformationPane extends Pane {

	private LinearTransformationPanel panel;
	private InfoPanel info;

	public LinearTranformationPane(Image image) {
		super(image);
		info = new InfoPanel();
		setLayout(new BorderLayout());
		setPanel(new LinearTransformationPanel());
		getPanel().getNodes().addObserver(info);

		//setFocusable(true);
		add(getPanel(), BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(info);
		scrollPane.setPreferredSize(new Dimension(200,100));
		add(scrollPane, BorderLayout.SOUTH);
		/*getPanel().*/addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					getImage().linearTransform(ImageUtils.returnSegments(panel.getNodes().getList()));
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getPanel().getPreferredSize();
	}

	public LinearTransformationPanel getPanel() {
		return panel;
	}

	public InfoPanel getInfo() {
		return info;
	}

	public void setPanel(LinearTransformationPanel panel) {
		this.panel = panel;
	}

	public void setInfo(InfoPanel info) {
		this.info = info;
	}
	

}
