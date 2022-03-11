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
			// TerritoryHeuristic getQueens = new TerritoryHeuristic();
			// ArrayList<int[]> whiteQueenLocs = getQueens.WhiteQueenLocations(curr.board);
			ArrayList<int[]> whiteQueenLocs = new ArrayList<>();
			for (int i = 0; i < curr.board.length; i++) {
				for (int j = 0; j < curr.board.length; j++) {
					if (curr.board[i][j] == 1) {
						int[] temp = new int[2];
						temp[0] = i;
						temp[1] = j;
						whiteQueenLocs.add(temp);
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				Queen Queen = new Queen(new Position(whiteQueenLocs.get(i)[0], whiteQueenLocs.get(i)[1]));
				/*
				 * System.out.println("Queen1: " + whiteQueenLocs.get(0)[0] + "," +
				 * whiteQueenLocs.get(0)[1] + "\n" + "Queen2: " + whiteQueenLocs.get(1)[0] + ","
				 * + whiteQueenLocs.get(1)[1] + "\n" + "Queen3: " + whiteQueenLocs.get(2)[0] +
				 * "," + whiteQueenLocs.get(2)[1] + "\n" + "Queen4: " + whiteQueenLocs.get(3)[0]
				 * + "," + whiteQueenLocs.get(3)[1] + "\n");
				 */
				LegalMove moveGetter = new LegalMove();
				ArrayList<Position> movesQueen = moveGetter.getLegalMove(Queen, curr);
				for (Position position : movesQueen) {
					// remove new Queen(position) from curr and generate arrow moves off of this
					Board boardForArrowGeneration = (Board) curr.clone();
					ArrayList<Integer> currentQueenForUpdatingArrowBoard = new ArrayList<>();
					ArrayList<Integer> newQueenForUpdatingArrowBoard = new ArrayList<>();
					currentQueenForUpdatingArrowBoard.add(Queen.getQueenCurrX());
					currentQueenForUpdatingArrowBoard.add(Queen.getQueenCurrY());
					newQueenForUpdatingArrowBoard.add(position.getX());
					newQueenForUpdatingArrowBoard.add(position.getY());
					boardForArrowGeneration.updateGameBoard(boardForArrowGeneration, currentQueenForUpdatingArrowBoard,
							newQueenForUpdatingArrowBoard, false);
					ArrayList<Position> arrowMoves = moveGetter.getLegalMove(new Queen(position),
							boardForArrowGeneration);

					for (Position position2 : arrowMoves) {

						ArrayList<Integer> QueenPosCur = new ArrayList<>();
						ArrayList<Integer> QueenPosNew = new ArrayList<>();
						ArrayList<Integer> ArrowPos = new ArrayList<>();

						QueenPosCur.add(Queen.getQueenCurrX());
						QueenPosCur.add(Queen.getQueenCurrY());

						QueenPosNew.add(position.getX());
						QueenPosNew.add(position.getY());

						ArrowPos.add(position2.getX());
						ArrowPos.add(position2.getY());

						Board tempBoard = (Board) curr.clone();

						tempBoard.updateGameBoard(tempBoard, QueenPosCur, QueenPosNew, ArrowPos, false);

						Node tempNode = new Node(this.root, 0);
						tempNode.setBoard(tempBoard);
						partial.root.addChild(tempNode);

					}

				}

			}

		} else { // generate moves for black
			// get queen
			// TerritoryHeuristic getQueens = new TerritoryHeuristic();
			// ArrayList<int[]> blackQueenLocs = getQueens.BlackQueenLocations(curr.board);
			ArrayList<int[]> blackQueenLocs = new ArrayList<>();
			for (int i = 0; i < curr.board.length; i++) {
				for (int j = 0; j < curr.board.length; j++) {
					if (curr.board[i][j] == 2) {
						int[] temp = new int[2];
						temp[0] = i;
						temp[1] = j;
						blackQueenLocs.add(temp);
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				Queen Queen = new Queen(new Position(blackQueenLocs.get(i)[0], blackQueenLocs.get(i)[1]));
				/*
				 * System.out.println("Queen1: " + whiteQueenLocs.get(0)[0] + "," +
				 * whiteQueenLocs.get(0)[1] + "\n" + "Queen2: " + whiteQueenLocs.get(1)[0] + ","
				 * + whiteQueenLocs.get(1)[1] + "\n" + "Queen3: " + whiteQueenLocs.get(2)[0] +
				 * "," + whiteQueenLocs.get(2)[1] + "\n" + "Queen4: " + whiteQueenLocs.get(3)[0]
				 * + "," + whiteQueenLocs.get(3)[1] + "\n");
				 */
				LegalMove moveGetter = new LegalMove();
				ArrayList<Position> movesQueen = moveGetter.getLegalMove(Queen, curr);
				for (Position position : movesQueen) {
					// remove new Queen(position) from curr and generate arrow moves off of this
					/*Board boardForArrowGeneration = (Board) curr.clone();
					boardForArrowGeneration.board[Queen.getQueenCurrX()][Queen.getQueenCurrY()] = 0;
					ArrayList<Integer> currentQueenForUpdatingArrowBoard = new ArrayList<>();
					ArrayList<Integer> newQueenForUpdatingArrowBoard = new ArrayList<>();
					currentQueenForUpdatingArrowBoard.add(Queen.getQueenCurrX());
					currentQueenForUpdatingArrowBoard.add(Queen.getQueenCurrY());
					newQueenForUpdatingArrowBoard.add(position.getX());
					newQueenForUpdatingArrowBoard.add(position.getY());
					boardForArrowGeneration.updateGameBoard(boardForArrowGeneration, currentQueenForUpdatingArrowBoard,
							newQueenForUpdatingArrowBoard, false);
				*/
					ArrayList<Position> arrowMoves = moveGetter.getLegalMove(new Queen(position),
							curr);

					for (Position position2 : arrowMoves) {

						ArrayList<Integer> QueenPosCur = new ArrayList<>();
						ArrayList<Integer> QueenPosNew = new ArrayList<>();
						ArrayList<Integer> ArrowPos = new ArrayList<>();

						QueenPosCur.add(Queen.getQueenCurrX());
						QueenPosCur.add(Queen.getQueenCurrY());

						QueenPosNew.add(position.getX());
						QueenPosNew.add(position.getY());

						ArrowPos.add(position2.getX());
						ArrowPos.add(position2.getY());

						Board tempBoard = (Board) curr.clone();

						tempBoard.updateGameBoard(tempBoard, QueenPosCur, QueenPosNew, ArrowPos, false);

						Node tempNode = new Node(this.root, 0);
						tempNode.setBoard(tempBoard);
						partial.root.addChild(tempNode);

					}

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