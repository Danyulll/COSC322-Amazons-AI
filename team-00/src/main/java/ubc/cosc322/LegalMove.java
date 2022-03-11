package ubc.cosc322;

import java.util.ArrayList;

public class LegalMove {

	public ArrayList<Position> getLegalMove(Queen queen, Board board) {
		ArrayList<Position> moves = new ArrayList<>();

		int currentRow = queen.getQueenCurrX();
		int currentCol = queen.getQueenCurrY();

		// Right
		for (int i = 1; currentCol + i < 10; i++) {
			if (board.getBoardPos(currentRow, currentCol + i) == 0) {
				moves.add(new Position(currentRow, currentCol + i));
			}
			if (board.getBoardPos(currentRow, currentCol + i) != 0) {
				break;
			}
		}

		// Left
		for (int i = 1; currentCol - i >= 0; i++) {
			if (board.getBoardPos(currentRow, currentCol - i) == 0) {
				moves.add(new Position(currentRow, currentCol - i));
			}
			if (board.getBoardPos(currentRow, currentCol - i) != 0) {
				break;
			}
		}

		// Up
		for (int i = 1; currentRow - i >= 0; i++) {
			if (board.getBoardPos(currentRow - i, currentCol) == 0) {
				moves.add(new Position(currentRow - i, currentCol));
			}

			if (board.getBoardPos(currentRow - i, currentCol) != 0) {
				break;
			}

		}

		// Down
		for (int i = 1; currentRow + i < 10; i++) {
			if (board.getBoardPos(currentRow + i, currentCol) == 0) {
				moves.add(new Position(currentRow + i, currentCol));
			}
			if (board.getBoardPos(currentRow + i, currentCol) != 0) {
				break;
			}
		}

		// Up-left
		for (int i = 1; currentRow - i >= 0 && currentCol - i >= 0; i++) {
			if (board.getBoardPos(currentRow - i, currentCol - i) == 0) {
				moves.add(new Position(currentRow - i, currentCol - i));
			}
			if (board.getBoardPos(currentRow - i, currentCol - i) != 0) {
				break;
			}
		}

		// Up-right
		for (int i = 1; currentRow - i >= 0 && currentCol + i < 10; i++) {
			if (board.getBoardPos(currentRow - i, currentCol + i) == 0) {
				moves.add(new Position(currentRow - i, currentCol + i));
			}

			if (board.getBoardPos(currentRow - i, currentCol + i) != 0) {
				break;
			}
		}

		// Down-right
		for (int i = 1; currentRow + i < 10 && currentCol + i < 10; i++) {
			if (board.getBoardPos(currentRow + i, currentCol + i) == 0) {
				moves.add(new Position(currentRow + i, currentCol + i));
			}
			if (board.getBoardPos(currentRow + i, currentCol + i) != 0) {
				break;
			}
		}

		// Down-left
		for (int i = 1; currentRow + i < 10 && currentCol - i >= 0; i++) {
			if (board.getBoardPos(currentRow + i, currentCol - i) == 0) {
				moves.add(new Position(currentRow + i, currentCol - i));
			}
			if (board.getBoardPos(currentRow + i, currentCol - i) != 0) {
				break;
			}
		}
		return moves;
	}

}
