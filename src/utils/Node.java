package utils;

public class Node<Type> {

	private Type value;
	private Node<Type> prev, next;
	private boolean isCommit;

	public Node(Node<Type> prev, Node<Type> next, Type value) {
		setPrev(prev);
		setNext(next);
		setValue(value);
	}

	public Type getValue() {
		return value;
	}

	public Node<Type> getPrev() {
		return prev;
	}

	public Node<Type> getNext() {
		return next;
	}

	public boolean isCommit() {
		return isCommit;
	}

	public void setValue(Type value) {
		this.value = value;
	}

	public void setPrev(Node<Type> prev) {
		this.prev = prev;
	}

	public void setNext(Node<Type> next) {
		this.next = next;
	}

	public void setCommit(boolean isCommit) {
		this.isCommit = isCommit;
	}

	public String toString() {
		return getValue().toString();
	}

}
