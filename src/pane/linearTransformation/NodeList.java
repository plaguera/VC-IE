package pane.linearTransformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class NodeList extends Observable {

	private List<Node> list;
	
	public NodeList() {
		setList(new ArrayList<Node>());
	}
	
	public void add(Node node) {
		setChanged();
		getList().add(node);
		notifyObservers(list);
	}
	
	public void remove(Node node) {
		setChanged();
		getList().remove(node);
		notifyObservers(list);
	}
	
	public void manualNotify() {
		setChanged();
		notifyObservers(list);
	}
	
	public void sort() {
		Collections.sort(list);
	}
	
	public Node get(int i) {
		return list.get(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * @return the list
	 */
	public List<Node> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<Node> list) {
		this.list = list;
	}

}
