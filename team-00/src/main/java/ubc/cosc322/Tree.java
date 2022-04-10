package ubc.cosc322;

import java.util.ArrayList;
import java.util.Iterator;

public class Tree {

	// Attributes
	private Node root;

	// Constructors
	public Tree() {
		this.root = new Node(null);
	}

	// Getters
	public Node getRoot() {
		return this.root;
	}

	// Methods

	public void setRoot(Node node) {
		this.root = node;
	}

	public void generatePartialGameTree(Board curr, boolean white, int depth, Node root) {
		Tree partial = new Tree();
		if (depth == 0) {
			return;
		}
		// if you are white generate your potential moves and then move on to generating
		// blacks potential moves
		if (white) {
			// get the white queens
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			ArrayList<int[]> whiteQueenLocs = getQueens.WhiteQueenLocations(curr.board);
			// for all four white queens generate their potential moves by adding their
			// nodes into the array list of the parent
			for (int i = 0; i < whiteQueenLocs.size(); i++) {
				// select your queen
				Queen Queen = new Queen(new Position(whiteQueenLocs.get(i)[0], whiteQueenLocs.get(i)[1]));
				// construct legal move generator
				LegalMove moveGetter = new LegalMove();
				// get all the ways the queen can move

				ArrayList<Position> movesQueen = moveGetter.getLegalMove(Queen, curr);
				// for every possible move a queen make generate its possible arrow shots
				for (Position position : movesQueen) {
					// get possible arrow shots for each queen position

					
					LegalArrow arrowGetter = new LegalArrow();
					Board temp = (Board) curr.clone();
					temp.updateGameBoard(temp, Queen.getQueenCurr(), position.getXY(), false);
					ArrayList<Position> arrowMoves = arrowGetter.getLegalArrow(position.getX(), position.getY(), temp);

					// for each possible arrow shot and queen position make a node with the
					// corresponding state and add it to the tree

					for (Position position2 : arrowMoves) {
						// make arrays to hold coordinates
						ArrayList<Integer> QueenPosCur = new ArrayList<>();
						ArrayList<Integer> QueenPosNew = new ArrayList<>();
						ArrayList<Integer> ArrowPos = new ArrayList<>();

						// add coordinates
						QueenPosCur.add(Queen.getQueenCurrX());
						QueenPosCur.add(Queen.getQueenCurrY());

						QueenPosNew.add(position.getX());
						QueenPosNew.add(position.getY());

						ArrowPos.add(position2.getX());
						ArrowPos.add(position2.getY());

						// create game state
						Board tempBoard = (Board) curr.clone();
						tempBoard.updateGameBoard(tempBoard, QueenPosCur, QueenPosNew, ArrowPos, false);

						// create temp node that will be added to tree
						Node tempNode = new Node(root);

						// set the game state for the board
						tempNode.setBoard(tempBoard);

						// add this node to the tree
						root.getChildren().add(tempNode);
					}

				}
			}
			int count = 0;
			for (Node child : root.getChildren()) {
				this.generatePartialGameTree(child.getBoard(), !white, depth - 1, child);
				count++;
				//if (count > 10)
					//break;
			}

		} else {
			// get the black queens
			TerritoryHeuristic getQueens = new TerritoryHeuristic();
			ArrayList<int[]> blackQueenLocs = getQueens.BlackQueenLocations(curr.board);
			// for all four black queens generate their potential moves by adding their
			// nodes into the array list of the parent
			for (int i = 0; i < blackQueenLocs.size(); i++) {
				// select your queen
				Queen Queen = new Queen(new Position(blackQueenLocs.get(i)[0], blackQueenLocs.get(i)[1]));
				// construct legal move generator
				LegalMove moveGetter = new LegalMove();
				// get all the ways the queen can move
				ArrayList<Position> movesQueen = moveGetter.getLegalMove(Queen, curr);
				// for every possible move a queen make generate its possible arrow shots
				for (Position position : movesQueen) {
					// get possible arrow shots for each queen position

					// TODO right now the possible arrow moves are found using the board where the
					// queen hasn't moved. This means the tree won't have a state for when a queen
					// moves and shoots an arrow where it just was

					LegalArrow arrowGetter = new LegalArrow();
					Board temp = (Board) curr.clone();
					temp.updateGameBoard(temp, Queen.getQueenCurr(), position.getXY(), false);
					ArrayList<Position> arrowMoves = arrowGetter.getLegalArrow(position.getX(), position.getY(), temp);

					// for each possible arrow shot and queen position make a node with the
					// corresponding state and add it to the tree

					for (Position position2 : arrowMoves) {
						// make arrays to hold coordinates
						ArrayList<Integer> QueenPosCur = new ArrayList<>();
						ArrayList<Integer> QueenPosNew = new ArrayList<>();
						ArrayList<Integer> ArrowPos = new ArrayList<>();

						// add coordinates
						QueenPosCur.add(Queen.getQueenCurrX());
						QueenPosCur.add(Queen.getQueenCurrY());

						QueenPosNew.add(position.getX());
						QueenPosNew.add(position.getY());

						ArrowPos.add(position2.getX());
						ArrowPos.add(position2.getY());

						// create game state
						Board tempBoard = (Board) curr.clone();
						tempBoard.updateGameBoard(tempBoard, QueenPosCur, QueenPosNew, ArrowPos, false);

						// create temp node that will be added to tree
						Node tempNode = new Node(root);

						// set the game state for the board
						tempNode.setBoard(tempBoard);

						// add this node to the tree
						root.getChildren().add(tempNode);
					}

				}
			}
			int count = 0;
			for (Node child : root.getChildren()) {
				this.generatePartialGameTree(child.getBoard(), !white, depth - 1, child);
				count++;
				//if (count > 10)
					//break;
			}

		}

	}
	


	
}