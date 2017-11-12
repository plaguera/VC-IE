package pane.linearTransformation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import utils.ImageUtils;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel implements Observer {

	private List<FunctionSegment> segments;

	public InfoPanel() {
		segments = new ArrayList<FunctionSegment>();
	}

	@Override
	protected void paintComponent(Graphics g) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof List<?>))
			return;
		segments = ImageUtils.returnSegments(((List<Node>) arg));
		removeAll();
		setLayout(new GridLayout(segments.size(), 1));
		for (FunctionSegment segment : segments) {
			// System.out.println(segment);
			JPanel aux = new JPanel(new GridLayout(1, 1));
			//JLabel lb1 = new JLabel(segment.stringFunction());
			JLabel lb2 = new JLabel(segment.stringPoints());
			//lb1.setHorizontalAlignment(SwingConstants.CENTER);
			lb2.setHorizontalAlignment(SwingConstants.CENTER);
			aux.setBorder(new LineBorder(Color.LIGHT_GRAY));
			//aux.add(lb1);
			aux.add(lb2);
			add(aux);
		}
		((JFrame) SwingUtilities.getWindowAncestor(this)).pack();
		repaint();
	}

	/**
	 * @return the segments
	 */
	public List<FunctionSegment> getSegments() {
		return segments;
	}

	/**
	 * @param segments the segments to set
	 */
	public void setSegments(List<FunctionSegment> segments) {
		this.segments = segments;
	}

}
