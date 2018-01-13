package rotation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;

public abstract class Rotation {
	
	public static final int ROTATE_PAINT = 1;
	public static final int NEAREST_NEIGHBOUR = 2;
	public static final int BILINEAR_INTERPOLATION = 3;
	
	private Dimension dimension;
	private BufferedImage image;
	
	public Rotation(BufferedImage image) {
		setImage(image);
		setDimension(new Dimension(getImage().getWidth(), getImage().getHeight()));
	}
	
	public abstract BufferedImage rotate(double angle);
	
	protected Point rotatePoint(Point point, double angle) {
		int x = point.x, y = point.y;
		double radians = Math.toRadians(angle);
		int xº = (int) ((double)x * Math.cos(radians) - (double)y * Math.sin(radians));
		int yº = (int) ((double)x * Math.sin(radians) + (double)y * Math.cos(radians));
		return new Point(xº, yº);
	}
	
	protected Dimension calculateNewDimension(double angle) {
		int width = getWidth(), height = getHeight();
		Point p1 = new Point(0, 0);
		Point p2 = new Point(width, 0);
		Point p3 = new Point(0, height);
		Point p4 = new Point(width, height);
		p1 = rotatePoint(p1, angle);
		p2 = rotatePoint(p2, angle);
		p3 = rotatePoint(p3, angle);
		p4 = rotatePoint(p4, angle);
		Integer[] x = { p1.x, p2.x, p3.x, p4.x };
		Integer[] y = { p1.y, p2.y, p3.y, p4.y };
		int minX = Collections.min(Arrays.asList(x));
		int maxX = Collections.max(Arrays.asList(x));
		int minY = Collections.min(Arrays.asList(y));
		int maxY = Collections.max(Arrays.asList(y));
		width = maxX - minX;
		height = maxY - minY;
		return new Dimension(width, height);
	}

	public BufferedImage getImage() {
		return image;
	}

	protected void setImage(BufferedImage image) {
		this.image = image;
	}

	public Dimension getDimension() {
		return dimension;
	}

	protected void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	public int getWidth() {
		return (int) getDimension().getWidth();
	}
	
	public int getHeight() {
		return (int) getDimension().getHeight();
	}

}
