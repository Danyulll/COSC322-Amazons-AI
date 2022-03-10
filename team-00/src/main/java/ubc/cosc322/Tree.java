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

	public Tree generatePartialGameTree(Board curr, boolean white) {
		Tree partial = new Tree();

		// generate moves for white
		if (white) {
			// get queen
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			ArrayList<int[]> whiteQueenLocs = getQueens.WhiteQueenLocations(curr.board);

		} else { // generate moves for black
			// get queens
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			ArrayList<int[]> blackQueenLocs = getQueens.BlackQueenLocations(curr.board);
			Queen Queen1 = new Queen(new Position(blackQueenLocs.get(0)[0],blackQueenLocs.get(0)[1]));
			Queen Queen2 = new Queen(new Position(blackQueenLocs.get(1)[0],blackQueenLocs.get(1)[1]));
			Queen Queen3 = new Queen(new Position(blackQueenLocs.get(2)[0],blackQueenLocs.get(2)[1]));
			Queen Queen4 = new Queen(new Position(blackQueenLocs.get(3)[0],blackQueenLocs.get(3)[1]));
			
			//get legal moves
			LegalMove moveGetter = new LegalMove();
			ArrayList<Position> movesQueen1 = moveGetter.getLegalMove(Queen1, curr); 
			ArrayList<Position> movesQueen2 = moveGetter.getLegalMove(Queen2, curr);
			ArrayList<Position> movesQueen3 = moveGetter.getLegalMove(Queen3, curr);
			ArrayList<Position> movesQueen4 = moveGetter.getLegalMove(Queen4, curr); 
			
		}
		return partial;
	}

}