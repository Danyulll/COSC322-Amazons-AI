package ubc.cosc322;

import java.util.ArrayList;


class Node {

	// Attributes
	private ArrayList<Node> children;
	private Node parent;
	public double value;
	private Board board;

	// Constructors
	public Node(Node parent) {

		this.parent = parent;
		// this.value = value;
		this.children = new ArrayList<Node>();
		this.board = new Board();

	}

	// Getters
	public Node getParent() {
		return this.parent;
	}

	public double getValue() {
		return this.value;
	}

	public Board getBoard() {
		return this.board;
	}

	public ArrayList<Node> getChildren() {
		return this.children;
	}

	// Setters
	public void setValue(double value) {
		this.value = value;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void addChild(Node node) {
		this.children.add(node);
	}

	public void setParent(Node node) {
		this.parent = node;
	}

	public int childrenCount() {
		int count = 0;
		for (int i = 0; i < this.children.size(); i++)
			if (this.children.get(i) != null)
				count++;

		return count;
	}

}