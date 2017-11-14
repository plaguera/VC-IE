package pane.linearTransformation;

import java.awt.Point;

public class FunctionSegment implements Comparable<FunctionSegment> {
	Point p1, p2;

	public FunctionSegment(Point a, Point b) {
		setP1(a);
		setP2(b);
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public double getM() {
		return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
	}

	public double getB() {
		return p1.getY() - getM() * p1.getX();
	}

	public double f(double x) {
		return getM() * x + getB();
	}

	public String stringFunction() {
		return "y = " + String.format("%.2f", getM()) + "x + " + String.format("%.2f", getB());
	}

	public String stringPoints() {
		return "[(" + p1.x + ", " + p1.y + ") --> (" + p2.x + ", " + p2.y + ")]";
	}

	public String toString() {
		return /*
				 * "y = " + String.format("%.2f", getM()) + "x + " + String.format("%.2f",
				 * getB()) +
				 */"[(" + p1.x + ", " + p1.y + ") --> (" + p2.x + ", " + p2.y + ")]";
	}

	public boolean contains(int x) {
		return x >= getP1().x && x <= getP2().x;
	}
	
	@Override
	public int compareTo(FunctionSegment arg0) {
		return (int) (getP1().getX() - arg0.getP1().getX());
	}
}
