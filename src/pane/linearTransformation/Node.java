package pane.linearTransformation;

import java.awt.Point;

public	 class Node implements Comparable<Node> {
	private int x, y;
	private boolean isSelected, isMoveable, isDeleteable;

	public Node(int x, int y, boolean selected, boolean moveable, boolean deleteable) {
		setCoordinates(x, y);
		setSelected(selected);
		setMoveable(moveable);
		setDeleteable(deleteable);
	}

	public Node(int x, int y) {
		this(x, y, true, true, true);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isMoveable() {
		return isMoveable;
	}

	public void setMoveable(boolean isMoveable) {
		this.isMoveable = isMoveable;
	}

	public boolean isDeleteable() {
		return isDeleteable;
	}

	public void setDeleteable(boolean isDeleteable) {
		this.isDeleteable = isDeleteable;
	}

	public void setCoordinates(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public Point getCoordinates() {
		return new Point(getX(), getY());
	}
	
	public String toString() {
		return "Node (" + this.hashCode() + ") = (" + getX() + ", " + getY() + ")";
	}

	@Override
	public int compareTo(Node arg0) {
		return this.getX() - arg0.getX();
	}

}
