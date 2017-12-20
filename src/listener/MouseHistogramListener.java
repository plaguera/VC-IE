package listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;

import pane.histogram.HistogramPaneTab;
import utils.HistogramLayer;

public class MouseHistogramListener implements MouseMotionListener {
	
	private HistogramPaneTab pane;
	private JLabel lbValue, lbCount;
	
	public MouseHistogramListener(HistogramPaneTab pane, JLabel value, JLabel count) {
		setPane(pane);
		setLbValue(value);
		setLbCount(count);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {;}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		HistogramLayer aux = getPane().getLayers().get(0);
		int[] values = aux.getValues();
		int bar_width = 2, value = x;
		if(x >= 5 && x < (256 * bar_width) + 5) {
			value = x - 6;
			value /= bar_width;
			if(value < 0 || value > 255)
				return;
			getLbValue().setText(String.valueOf(value));
			getLbCount().setText(String.valueOf(values[value]));
		}
		
	}

	public HistogramPaneTab getPane() {
		return pane;
	}

	public void setPane(HistogramPaneTab pane) {
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
