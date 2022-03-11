package ubc.cosc322;

import java.util.ArrayList;
import java.util.Iterator;

public class Tree {

	// Attributes
	private Node root;

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

	public Tree generatePartialGameTree(Board curr, boolean white) {
		Tree partial = new Tree();

		// generate moves for white
		if (white) {
			// get queen
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			System.out.println("Getting white Queen locations");
			ArrayList<int[]> whiteQueenLocs = getQueens.WhiteQueenLocations(curr.board);
			System.out.println(whiteQueenLocs.toString());
			Queen Queen1 = new Queen(new Position(whiteQueenLocs.get(0)[0], whiteQueenLocs.get(0)[1]));
			System.out.println("Positiion of Queen 1: " + Queen1.getQueenCurrX() + "," + Queen1.getQueenCurrY());
			// Queen Queen2 = new Queen(new Position(whiteQueenLocs.get(1)[0],
			// whiteQueenLocs.get(1)[1]));
			// Queen Queen3 = new Queen(new Position(whiteQueenLocs.get(2)[0],
			// whiteQueenLocs.get(2)[1]));
			// Queen Queen4 = new Queen(new Position(whiteQueenLocs.get(3)[0],
			// whiteQueenLocs.get(3)[1]));
			// get legal moves
			LegalMove moveGetter = new LegalMove();
			ArrayList<Position> movesQueen1 = moveGetter.getLegalMove(Queen1, curr);
			System.out.println("Getting legal moves for Queen 1, ex: " + movesQueen1.get(0).getX() + ","
					+ movesQueen1.get(0).getY());
			// ArrayList<Position> movesQueen2 = moveGetter.getLegalMove(Queen2, curr);
			// ArrayList<Position> movesQueen3 = moveGetter.getLegalMove(Queen3, curr);
			// ArrayList<Position> movesQueen4 = moveGetter.getLegalMove(Queen4, curr);
			int counter = 0;
			for (Position position : movesQueen1) {
				System.out
						.println("For each move in the queen's move list generate a full list of possible arrow shots");
				ArrayList<Position> arrowMoves = moveGetter.getLegalMove(new Queen(position), curr);
				System.out.println("ex: " + arrowMoves.get(0).getX() + "," + arrowMoves.get(0).getY());

				for (Position position2 : arrowMoves) {
					counter++;
					System.out.println("For each arrow move position lets generate a board and add it to the tree");
					ArrayList<Integer> QueenPosCur = new ArrayList<>();
					ArrayList<Integer> QueenPosNew = new ArrayList<>();
					ArrayList<Integer> ArrowPos = new ArrayList<>();

					QueenPosCur.add(Queen1.getQueenCurrX());
					QueenPosCur.add(Queen1.getQueenCurrY());

					QueenPosNew.add(position.getX());
					QueenPosNew.add(position.getY());

					ArrowPos.add(position2.getX());
					ArrowPos.add(position2.getY());

					// clone the current board so we can add a updated version to the tree that
					// doesn't affect our current board
					System.out.println("curr before clone: ");
					curr.printBoard();
					System.out.println("tempbaord after clone but before update: ");
					Board tempBoard = (Board) curr.clone();
					tempBoard.printBoard();
					
					System.out.println("tempbaord after update");
					tempBoard.updateGameBoard(tempBoard, QueenPosCur, QueenPosNew, ArrowPos, false);
					tempBoard.printBoard();

					System.out.println("Cur after updating tempboard (should be unchanged))");
					curr.printBoard();
					//System.out.println("Find out what part is not cloning:\nBoards: currBoard- " + curr.board.toString() + "\ntempBaord- " + tempBoard.board.toString() + "\nWhite Pos curr- " + curr.WhitePos + "\n Temp whitepos- " + tempBoard.WhitePos + "\n Cur black pos- " + curr.BlackPos + "\n Temp Blackpos- " + tempBoard.BlackPos );
					Node tempNode = new Node(this.root, 0);
					tempNode.setBoard(tempBoard);
					partial.root.addChild(tempNode);
					
					if(counter==1)
						break;
					
				}
				counter++;
				System.out.println("tree generation done");
				if(counter==2)
					break;
			}
		} else { // generate moves for black

			// get queens
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			ArrayList<int[]> blackQueenLocs = getQueens.BlackQueenLocations(curr.board);
			Queen Queen1 = new Queen(new Position(blackQueenLocs.get(0)[0], blackQueenLocs.get(0)[1]));
			// Queen Queen2 = new Queen(new Position(blackQueenLocs.get(1)[0],
			// blackQueenLocs.get(1)[1]));
			// Queen Queen3 = new Queen(new Position(blackQueenLocs.get(2)[0],
			// blackQueenLocs.get(2)[1]));
			// Queen Queen4 = new Queen(new Position(blackQueenLocs.get(3)[0],
			// blackQueenLocs.get(3)[1]));

			// get legal moves for queens
			LegalMove moveGetter = new LegalMove();
			ArrayList<Position> movesQueen1 = moveGetter.getLegalMove(Queen1, curr);
			// ArrayList<Position> movesQueen2 = moveGetter.getLegalMove(Queen2, curr);
			// ArrayList<Position> movesQueen3 = moveGetter.getLegalMove(Queen3, curr);
			// ArrayList<Position> movesQueen4 = moveGetter.getLegalMove(Queen4, curr);

			// TODO - pass each possible queen position to movegetter to get possible arrow
			// positions, then get board states from this and add them to the tree
			// get board states for each move

			for (Position queenPos : movesQueen1) {
				ArrayList<Position> arrowMoves = moveGetter.getLegalMove(new Queen(queenPos), curr);

				for (Position arrowPos : arrowMoves) {
					ArrayList<Integer> QueenPosCur = new ArrayList<>();
					ArrayList<Integer> QueenPosNew = new ArrayList<>();
					ArrayList<Integer> ArrowPos = new ArrayList<>();

					QueenPosCur.add(Queen1.getQueenCurrX());
					QueenPosCur.add(Queen1.getQueenCurrY());

					QueenPosNew.add(queenPos.getX());
					QueenPosNew.add(queenPos.getY());

					ArrowPos.add(arrowPos.getX());
					ArrowPos.add(arrowPos.getY());

					Board tempBoard = (Board) curr.clone();

					tempBoard.updateGameBoard(curr, QueenPosCur, QueenPosNew, ArrowPos, false);

					Node tempNode = new Node(this.root, 0);
					tempNode.setBoard(tempBoard);
					partial.root.addChild(tempNode);
				}
			}

		}
		return partial;
	}

}

class Node {

	// Attributes
	private ArrayList<Node> children;
	private Node parent;
	private int value;
	private Board board;

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

	public Board getBoard() {
		return this.board;
	}

	public ArrayList<Node> getChildren() {
		return this.children;
	}

	// Setters
	public void setValue(int value) {
		this.value = value;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void addChild(Node node) {
		this.children.add(node);
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

}