package ubc.cosc322;

import java.util.ArrayList;

public class Tree {

	// Attributes
	private Node root;
	private Board board;

	// Constructors
	public Tree() {
		this.root = new Node(null, 0);
	}

	// Getters
	public Node getRoot() {
		return this.root;
	}

	// Methods
	public void insert(int value) {
		if (this.root.getValue() == 0) // temp value while we work with integers
			this.root.setValue(value);
		else
			this.root.getParent().add(value);
	}

}

class Node {

	// Attributes
	private ArrayList<Node> children;
	private Node parent;
	private int value;
	

	// Constructors
	public Node(Node parent, int value) {
		this.parent = parent;
		this.value = value;
		this.children = new ArrayList<Node>();
	}

	// Getters
	public Node getParent() {
		return this.parent;
	}

	public int getValue() {
		return this.value;
	}

	// Setters
	public void setValue(int value) {
		this.value = value;
	}

	// Methods
	public void add(int value) {
		if (this.children != null)
			this.children.add(new Node(this, value));
	}

	public int childrenCount() {
		int count = 0;
		for (int i = 0; i < this.children.size(); i++)
			if (this.children.get(i) != null)
				count++;

		return count;
	}
	
	/*public Board generatePartialGameTree(Board curr, boolean white) {
		
	}*/

}