package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
		board[0][3] = BLACK;
		board[0][6] = BLACK;
		board[3][0] = BLACK;
		board[3][9] = BLACK;

		board[6][0] = WHITE;
		board[9][3] = WHITE;
		board[9][6] = WHITE;
		board[6][9] = WHITE;

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
			int WorB = current.board[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)];

			// replace old location of piece with 0
			current.board[map.get(QueenPosCurEnemey).get(0)][map.get(QueenPosCurEnemey).get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			current.board[map.get(QueenPosNewEnemey).get(0)][map.get(QueenPosNewEnemey).get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			current.board[map.get(ArrowPosEnemey).get(0)][map.get(ArrowPosEnemey).get(1)] = 3;
		} else {
			int WorB = this.board[QueenPosCurEnemey.get(0)][QueenPosCurEnemey.get(1)];

			// replace old location of piece with 0
			current.board[QueenPosCurEnemey.get(0)][QueenPosCurEnemey.get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			current.board[QueenPosNewEnemey.get(0)][QueenPosNewEnemey.get(1)] = (WorB == 1) ? 1 : 2;

			// Update arrow location
			current.board[ArrowPosEnemey.get(0)][ArrowPosEnemey.get(1)] = 3;
		}
		return current;
	}

	public Board updateGameBoard(Board boardForArrowGeneration, ArrayList<Integer> QueenCur,
			ArrayList<Integer> QueenNew, boolean conversionNeeded) {
		if (conversionNeeded) {

			HashMap<ArrayList<Integer>, ArrayList<Integer>> map = this.makeHashTable();
			int WorB = boardForArrowGeneration.board[map.get(QueenCur).get(0)][map.get(QueenCur).get(1)];

			// replace old location of piece with 0
			boardForArrowGeneration.board[map.get(QueenCur).get(0)][map.get(QueenCur).get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			boardForArrowGeneration.board[map.get(QueenNew).get(0)][map.get(QueenNew).get(1)] = (WorB == 1) ? 1 : 2;

		} else {
			int WorB = boardForArrowGeneration.board[QueenCur.get(0)][QueenCur.get(1)];

			// replace old location of piece with 0
			boardForArrowGeneration.board[QueenCur.get(0)][QueenCur.get(1)] = 0;

			// if moving piece is white put 1 at coord else put 2
			boardForArrowGeneration.board[QueenNew.get(0)][QueenNew.get(1)] = (WorB == 1) ? 1 : 2;

		}
		return boardForArrowGeneration;
	}

	public static HashMap<ArrayList<Integer>, ArrayList<Integer>> makeHashTable() {
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
		clone.board = new int[10][10];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				clone.board[i][j] = this.board[i][j];
			}
		}

		clone.WhitePos = new ArrayList<Position>();
		int counter = 0;
		for (Position position : this.WhitePos) {
			Position temp = new Position(this.WhitePos.get(counter).getX(), this.WhitePos.get(counter).getY());
			clone.WhitePos.add(temp);
			counter++;
		}
		counter = 0;
		clone.BlackPos = new ArrayList<Position>();
		for (Position position : this.BlackPos) {
			Position temp = new Position(this.BlackPos.get(counter).getX(), this.BlackPos.get(counter).getY());
			clone.BlackPos.add(temp);
			counter++;
		}
		return clone;
	}

	public static HashMap<ArrayList<Integer>, ArrayList<Integer>> makeGaoTable() {
		HashMap<ArrayList<Integer>, ArrayList<Integer>> gaoTable = new HashMap<>();
		ArrayList<Integer> keys = new ArrayList<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				keys.add(row);
				keys.add(col);

			}
		}

		ArrayList<Integer> values = new ArrayList<>();
		for (int row = 10; row > 0; row--) {
			for (int col = 1; col < 11; col++) {
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

			gaoTable.put(keyTemp, valueTemp);
			counter++;
			if (counter == 100) {
				done = true;
			}

		}

		return gaoTable;
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
