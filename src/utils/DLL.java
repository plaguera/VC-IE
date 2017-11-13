package utils;

public class DLL<Type> {
	
	private Node<Type> head, tail, current;
	
	public DLL() {
		setHead(null);
		setTail(null);
		setCurrent(null);
	}
	
	public DLL(Type value) {
		Node<Type> node = new Node<Type>(null, null, value);
		node.setCommit(true);
		setHead(node);
		setTail(node);
		setCurrent(node);
	}
	
	public void commit() {
		getCurrent().setCommit(true);
	}
	
	public Node<Type> lastCommit() {
		Node<Type> aux = getCurrent();
		while(!aux.isCommit()) {
			aux = aux.getPrev();
		}
		return aux;
	}
	
	public void add(Type value) {
		addAfter(getCurrent(), value);
	}
	
	public void addAfter(Node<Type> after, Type value) {
		if(after == null)
			return;
		// NEXT IS  NULL ALL PREVIOUS VERSIONS AFTER THE NEW ONE WILL BE DELETED
		Node<Type> node = new Node<Type>(after, null, value);
		after.setNext(node);
		setCurrent(node);
		if(after == getTail())
			setTail(node);
	}
	
	public Node<Type> toBeginning() {
		setCurrent(getHead());
		return getCurrent();
	}
	
	public Node<Type> forward() {
		if(getCurrent() == getTail())
			return getCurrent();
		setCurrent(getCurrent().getNext());
		return getCurrent();
	}
	
	public Node<Type> backward() {
		if(getCurrent() == getHead())
			return getCurrent();
		setCurrent(getCurrent().getPrev());
		return getCurrent();
	}
	
	public boolean isEmpty() {
		return getHead() == null && getTail() == null && getCurrent() == null;
	}

	public Node<Type> getHead() { return head; }
	public Node<Type> getTail() { return tail; }
	public Node<Type> getCurrent() { return current; }
	public void setHead(Node<Type> head) { this.head = head; }
	public void setTail(Node<Type> tail) { this.tail = tail; }
	public void setCurrent(Node<Type> current) { this.current = current; }
	
	public String toString() {
		String out = "";
		if(getCurrent() == getHead())
			out += "[" + String.valueOf(getCurrent().getValue().hashCode()) + "]";
		else
			out += String.valueOf(getHead().getValue().hashCode());
		Node<Type> aux = getHead().getNext();
		while(aux != null) {
			if(aux == getCurrent())
				out += " --> [" + String.valueOf(aux.getValue().hashCode()) + "]";
			else
				out += " --> " + String.valueOf(aux.getValue().hashCode());
			aux = aux.getNext();
		}
		return out;
	}
}
