package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;

public class Board implements Cloneable {
	public static final int ROWS = 10;
	public static final int COLS = 10;

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	public static final int ARROW = 3;

	public int[][] board = new int[ROWS][COLS];
	ArrayList<Position> WhitePos = new ArrayList<>();
	ArrayList<Position> BlackPos = new ArrayList<>();

	public Board() {
		board[1 - 1][4 - 1] = BLACK;
		board[1 - 1][7 - 1] = BLACK;
		board[4 - 1][1 - 1] = BLACK;
		board[4 - 1][10 - 1] = BLACK;

		board[7 - 1][1 - 1] = WHITE;
		board[10 - 1][4 - 1] = WHITE;
		board[10 - 1][7 - 1] = WHITE;
		board[7 - 1][10 - 1] = WHITE;

		initialPos();
	}

	public void initialPos() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == WHITE) {
					WhitePos.add(new Position(i, j));
				} else if (board[i][j] == BLACK) {
					BlackPos.add(new Position(i, j));
				}
			}
		}
	}

	public void printBoard() {
		for (int i = 0; i < ROWS; i++) {
			System.out.print("| ");
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == WHITE) {
					System.out.print(" W ");
				} else if (board[i][j] == BLACK) {
					System.out.print(" B ");
				} else if (board[i][j] == ARROW) {
					System.out.print(" A ");
				} else {
					System.out.print(" . ");
				}
			}
			System.out.println(" |");
		}
		System.out.println(" ");
		System.out.println(" ");
	}

	public Board updateGameBoard(Board current, ArrayList<Integer> QueenPosCurEnemey,
			ArrayList<Integer> QueenPosNewEnemey, ArrayList<Integer> ArrowPosEnemey, boolean conversionNeeded) {
		if (conversionNeeded) {

			HashMap<ArrayList<Integer>, ArrayList<Integer>> map = this.makeHashTable();
			int WorB = this.board[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)];

			// replace old location of piece with 0
			this.board[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			this.board[map.get(QueenPosNewEnemey).get(0)][map.get(QueenPosNewEnemey).get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			this.board[map.get(ArrowPosEnemey).get(0)][map.get(ArrowPosEnemey).get(1)] = 3;
		} else {
			int WorB = this.board[QueenPosCurEnemey.get(0)][QueenPosCurEnemey.get(1)];

			// replace old location of piece with 0
			this.board[QueenPosCurEnemey.get(0)][QueenPosCurEnemey.get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			this.board[QueenPosNewEnemey.get(0)][QueenPosNewEnemey.get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			this.board[ArrowPosEnemey.get(0)][ArrowPosEnemey.get(1)] = 3;
		}
		return this;
	}

	public HashMap<ArrayList<Integer>, ArrayList<Integer>> makeHashTable() {
		HashMap<ArrayList<Integer>, ArrayList<Integer>> boardConversion = new HashMap<>();

		ArrayList<Integer> keys = new ArrayList<>();
		for (int row = 10; row > 0; row--) {
			for (int col = 1; col < 11; col++) {
				keys.add(row);
				keys.add(col);

			}
		}

		ArrayList<Integer> values = new ArrayList<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				values.add(row);
				values.add(col);

			}
		}

		int counter = 0, keyIndex = -1, valueIndex = -1;
		boolean done = false;
		while (!done) {
			ArrayList<Integer> keyTemp = new ArrayList<>();
			ArrayList<Integer> valueTemp = new ArrayList<>();
			keyTemp.add(keys.get(++keyIndex));
			keyTemp.add(keys.get(++keyIndex));

			valueTemp.add(values.get(++valueIndex));
			valueTemp.add(values.get(++valueIndex));

			boardConversion.put(keyTemp, valueTemp);
			counter++;
			if (counter == 100) {
				done = true;
			}

		}
		return boardConversion;
	}

	public Object clone() {
		Board clone = new Board();
		clone.board = this.board.clone();
		clone.WhitePos = (ArrayList<Position>) this.WhitePos.clone();
		clone.BlackPos = (ArrayList<Position>) this.BlackPos.clone();
		return clone;
	}

	// getter and setter
	public int getBoardPos(int x, int y) {
		return board[x][y];
	}

	public ArrayList<Position> getWhitePos() {
		return WhitePos;
	}

	public ArrayList<Position> getBlackPos() {
		return BlackPos;
	}

	public ArrayList<Position> setWhitePos() {
		return this.WhitePos = WhitePos;
	}

	public ArrayList<Position> setBlackPos() {
		return this.BlackPos = BlackPos;
	}

}
