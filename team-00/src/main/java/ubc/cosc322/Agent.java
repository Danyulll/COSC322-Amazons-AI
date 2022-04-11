package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;

import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;

public class Agent {
	// Attributes
	private boolean white;
	private ArrayList<Integer> QueenPosCurEnemey;
	private ArrayList<Integer> QueenPosNewEnemey;
	private ArrayList<Integer> ArrowPosEnemey;
	private Board board;
	private ArrayList<Integer> QueenPosCurSend;
	private ArrayList<Integer> QueenPosNewSend;
	private ArrayList<Integer> ArrowPosSend;
	private boolean outOfMoves;

	public boolean isOutOfMoves() {
		return outOfMoves;
	}

	public void setOutOfMoves(boolean outOfMoves) {
		this.outOfMoves = outOfMoves;
	}

	public ArrayList<Integer> getQueenPosCurSend() {
		return QueenPosCurSend;
	}

	public void setQueenPosCurSend(ArrayList<Integer> queenPosCurSend) {
		QueenPosCurSend = queenPosCurSend;
	}

	public ArrayList<Integer> getQueenPosNewSend() {
		return QueenPosNewSend;
	}

	public void setQueenPosNewSend(ArrayList<Integer> queenPosNewSend) {
		QueenPosNewSend = queenPosNewSend;
	}

	public ArrayList<Integer> getArrowPosSend() {
		return ArrowPosSend;
	}

	public void setArrowPosSend(ArrayList<Integer> arrowPosSend) {
		ArrowPosSend = arrowPosSend;
	}

	// Constructors
	public Agent(ArrayList<Integer> QueenPosCurEnemey, ArrayList<Integer> QueenPosNewEnemey,
			ArrayList<Integer> ArrowPosEnemey, Board board, boolean white) {
		this.setArrowPosEnemey(ArrowPosEnemey);
		this.setBoard(board);
		this.setQueenPosCurEnemey(QueenPosCurEnemey);
		this.setQueenPosNewEnemey(QueenPosNewEnemey);
		this.setWhite(white);
		this.QueenPosCurSend = new ArrayList<Integer>();
		this.QueenPosNewSend = new ArrayList<Integer>();
		this.ArrowPosSend = new ArrayList<Integer>();

	}

	public Agent(Board board, boolean white) {
		this.setBoard(board);
		this.setWhite(white);
		this.QueenPosCurSend = new ArrayList<Integer>();
		this.QueenPosNewSend = new ArrayList<Integer>();
		this.ArrowPosSend = new ArrayList<Integer>();
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	// Getters and Setters
	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public ArrayList<Integer> getQueenPosCurEnemey() {
		return QueenPosCurEnemey;
	}

	public void setQueenPosCurEnemey(ArrayList<Integer> queenPosCurEnemey) {
		QueenPosCurEnemey = queenPosCurEnemey;
	}

	public ArrayList<Integer> getQueenPosNewEnemey() {
		return QueenPosNewEnemey;
	}

	public void setQueenPosNewEnemey(ArrayList<Integer> queenPosNewEnemey) {
		QueenPosNewEnemey = queenPosNewEnemey;
	}

	public ArrayList<Integer> getArrowPosEnemey() {
		return ArrowPosEnemey;
	}

	public void setArrowPosEnemey(ArrayList<Integer> arrowPosEnemey) {
		ArrowPosEnemey = arrowPosEnemey;
	}

	public void legalityCheck(Board previous) {
		// check legaility of move
		HashMap<ArrayList<Integer>, ArrayList<Integer>> table = Board.makeHashTable();
		LegalMove moveGetter = new LegalMove();
		boolean illegalQueenMove = true;
		ArrayList<Position> moves = moveGetter.getLegalMove(
				new Queen(new Position(table.get(QueenPosCurEnemey).get(0), table.get(QueenPosCurEnemey).get(1))),
				previous);
		for (Position position : moves) {
			if (position.getX() == table.get(QueenPosNewEnemey).get(0)
					&& position.getY() == table.get(QueenPosNewEnemey).get(1)) {
				illegalQueenMove = false;
				break;
			}
		}

		boolean illegalArrowMove = true;
		LegalArrow arrowGetter = new LegalArrow();
		Board temp = (Board) previous.clone();
		temp.updateGameBoard(temp, QueenPosCurEnemey, QueenPosNewEnemey, true);
		ArrayList<Position> arrowMoves = arrowGetter.getLegalArrow(table.get(QueenPosNewEnemey).get(0),
				table.get(QueenPosNewEnemey).get(1), temp);

		for (Position position2 : arrowMoves) {
			if (position2.getX() == table.get(ArrowPosEnemey).get(0)
					&& position2.getY() == table.get(ArrowPosEnemey).get(1)) {
				illegalArrowMove = false;
				break;
			}
		}

		if (illegalQueenMove || illegalArrowMove) {
			System.out.println("opponenet made an illegal move");
			System.out.println(
					"*************************************************************************************************************************************************************************************************************");

			System.out.println(
					"Enmey Queen's old position: " + QueenPosCurEnemey.get(0) + "," + QueenPosCurEnemey.get(1));
			System.out.println(
					"Enmey Queen's New position: " + QueenPosNewEnemey.get(0) + "," + QueenPosNewEnemey.get(1));
			System.out.println("Enmey Arrow's New position: " + ArrowPosEnemey.get(0) + "," + ArrowPosEnemey.get(1));
			System.out.println("board before move");
			this.board.printBoard();
			System.out.println("opponent's queen move");
			Board temp4 = (Board) this.board.clone();
			temp4 = temp4.updateGameBoard(temp4, QueenPosCurEnemey, QueenPosNewEnemey, ArrowPosEnemey, true);
			temp4.printBoard();
		}

	}



	public int calculateDepth() {
		int ply = 1;
		int emptySpotCounter = 0;
		for (int i = 0; i < this.getBoard().board.length; i++) {

			for (int j = 0; j < this.getBoard().board.length; j++) {
				if (this.getBoard().board[i][j] == 0)
					emptySpotCounter++;

			}
		}
		int possible = emptySpotCounter * emptySpotCounter;
		if (possible > 3000)
			ply = 2;
		else if (possible <= 3000)
			ply = 3;
		

		System.out.println("ply chosen and possible count: (" + ply + "," + possible + ")");
		return ply;
	}

	public void makeMove() {
		Board boardBeforeMove = (Board) this.getBoard().clone();
		Tree partial = new Tree();
		partial.getRoot().setBoard(this.getBoard());
		int ply = this.calculateDepth();	
		partial.generatePartialGameTree(boardBeforeMove, this.isWhite(), ply, partial.getRoot());

		Board moveToMake = new Board();
		if (partial.getRoot().getChildren().size() == 0) {
			this.setOutOfMoves(true);
			return;
		}	
		moveToMake = minimax(partial.getRoot(), ply);
		this.setBoard(moveToMake);	
		// Get Move Coords

		int[] oldCoord = new int[2];
		int[] newCoord = new int[2];
		int[] newArrowcoord = new int[2];
		
		if (this.isWhite()) {
			for (int i = 0; i < boardBeforeMove.board.length; i++) {
				for (int j = 0; j < boardBeforeMove.board.length; j++) {

					if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
						newArrowcoord[0] = i;
						newArrowcoord[1] = j;
						
					}
					if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 1) {
						newCoord[0] = i;
						newCoord[1] = j;
						
					}

					if (moveToMake.board[i][j] != 1 && boardBeforeMove.board[i][j] == 1) {
					
						oldCoord[0] = i;
						oldCoord[1] = j;
					}

				}
			}
		} else if(!this.isWhite()) {

			for (int i = 0; i < boardBeforeMove.board.length; i++) {
				for (int j = 0; j < boardBeforeMove.board.length; j++) {

					if (moveToMake.board[i][j] == 3 && boardBeforeMove.board[i][j] != 3) {
						newArrowcoord[0] = i;
						newArrowcoord[1] = j;
					
					}
					if (boardBeforeMove.board[i][j] == 0 && moveToMake.board[i][j] == 2) {
						newCoord[0] = i;
						newCoord[1] = j;
					
					}

					if (moveToMake.board[i][j] != 2 && boardBeforeMove.board[i][j] == 2) {
				
						oldCoord[0] = i;
						oldCoord[1] = j;
					}

				}
			}
		}
		ArrayList<Integer> QueenNew = new ArrayList<Integer>();
		QueenNew.add(newCoord[0]);
		QueenNew.add(newCoord[1]);
		ArrayList<Integer> Arrow = new ArrayList<Integer>();
		Arrow.add(newArrowcoord[0]);
		Arrow.add(newArrowcoord[1]);
		ArrayList<Integer> QueenOld = new ArrayList<>();
		QueenOld.add(oldCoord[0]);
		QueenOld.add(oldCoord[1]);

		// Convert To sendable coords
		HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = Board.makeGaoTable();

		// Set move coordinates
		this.setQueenPosCurSend(gaoTable.get(QueenOld));	
		this.setQueenPosNewSend((gaoTable.get(QueenNew)));
		this.setArrowPosSend(gaoTable.get(Arrow));


	}

	public Board minimax(Node current, int depth) {
		Node move = MaxValue(current, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		return move.getBoard();
	}

	public Node MaxValue(Node current, int depth, double alpha, double beta) {
		if (depth == 0) {
			current.value = value(current.getBoard(), this.white);

			return current;
		}
		Node v = new Node(null);
		v.value = Double.NEGATIVE_INFINITY;
		for (Node node : current.getChildren()) {
			Node v2 = MinValue(node, depth - 1, alpha, beta);
			if (v2.value > v.value) {
				v.value = v2.value;
				v.setBoard(node.getBoard());
				alpha = Math.max(alpha, v.value);
			}
			if (v.value >= beta) {
				return v;
			}

		}

		return v;

	}

	public Node MinValue(Node current, int depth, double alpha, double beta) {
		if (depth == 0) {
			current.value = value(current.getBoard(), this.white);

			return current;
		}
		Node v = new Node(null);
		v.value = Double.POSITIVE_INFINITY;
		for (Node node : current.getChildren()) {
			Node v2 = MaxValue(node, depth - 1, alpha, beta);
			if (v2.value < v.value) {
				v.value = v2.value;
				v.setBoard(node.getBoard());
				beta = Math.min(beta, v.value);
			}
			if (v.value <= alpha) {
				return v;
			}

		}

		return v;

	}

	public double value(Board board, boolean max) {
		TerritoryHeuristic heur = new TerritoryHeuristic();
		int[][] scoreBoard = heur.closestQueen(board);

		double sum = 0;
		for (int i = 0; i < scoreBoard.length; i++) {
			for (int j = 0; j < scoreBoard[i].length; j++) {
				if (scoreBoard[i][j] == 1) {
					if (max) {
						sum += 1;
					} else {
						sum -= 1;
					}
				} else if (scoreBoard[i][j] == 2) {
					if (max) {
						sum -= 1;
					} else {
						sum += 1;
					}
				}
			}
		}
		return sum;
	}

}
