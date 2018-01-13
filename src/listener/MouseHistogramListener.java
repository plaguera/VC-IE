package listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import pane.histogram.Histogram;

public class MouseHistogramListener implements MouseMotionListener {
	
	private Histogram pane;
	private JLabel lbValue, lbCount;
	
	public MouseHistogramListener(Histogram pane, JLabel value, JLabel count) {
		setPane(pane);
		setLbValue(value);
		setLbCount(count);
	}

	@Override
	public void mouseDragged(MouseEvent e) {;}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		if(y <= Histogram.MARGIN || y > (pane.getHeight() - Histogram.MARGIN))
			return;
		if(x > Histogram.MARGIN && x < (pane.getWidth() - Histogram.MARGIN)) {
			x -= Histogram.MARGIN;
			int width = pane.getWidth() - (Histogram.MARGIN * 2);
			double barWidth = (double) width / 256d;
			x = (int) (x / barWidth);
			int value = pane.getValues()[x];
			getLbValue().setText(String.valueOf(x));
			getLbCount().setText(String.valueOf(value));
			//System.out.println((x / barWidth) + " = " + value);
		}
	}

	public Histogram getPane() {
		return pane;
	}

	public void setPane(Histogram pane) {
		this.pane = pane;
	}
	
	public JLabel getLbValue() {
		return lbValue;
	}

	public void setLbValue(JLabel lbValue) {
		this.lbValue = lbValue;
	}

	public JLabel getLbCount() {
		return lbCount;
	}

	public void setLbCount(JLabel lbCount) {
		this.lbCount = lbCount;
	}

}
