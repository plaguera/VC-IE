package utils;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;

public class Square {
	
	private Dimension dimension;
	private Point center;
	
	public Square(Dimension dimension, Point center) {
		setDimension(dimension);
		setCenter(center);
	}
	
	public Point translate(Point point, double angle) {
		Point min = min(angle);
		point = rotatePoint(point, angle);
		return new Point(point.x + Math.abs(min.x), point.y + Math.abs(min.y));
	}
	
	private Point rotatePoint(Point point, double angle) {
		int x = point.x, y = point.y;
		double radians = Math.toRadians(angle);
		int xº = (int) ((double)x * Math.cos(radians) - (double)y * Math.sin(radians));
		int yº = (int) ((double)x * Math.sin(radians) + (double)y * Math.cos(radians));
		return new Point(xº, yº);
	}
	
	private Point min(double angle) {
		int width = (int) dimension.getWidth(), height = (int) dimension.getHeight();
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
		int minY = Collections.min(Arrays.asList(y));
		return new Point(minX, minY);
	}
	
	public Dimension calculateDimension(double angle) {
		int width = (int) dimension.getWidth(), height = (int) dimension.getHeight();
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

	public Dimension getDimension() {
		return dimension;
	}

	public Point getCenter() {
		return center;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

}
