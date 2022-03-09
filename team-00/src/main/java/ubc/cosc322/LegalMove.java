package ubc.cosc322;

import java.util.ArrayList;

public class LegalMove {
	
	public ArrayList<Position> getLegalMove (Queen queen, Board[][] board) {
		ArrayList<Position> moves = new ArrayList<>();

		int currentRow = queen.getQueenCurrX();
		int currentCol = queen.getQueenCurrY();
		
		// Right
		for (int i = 1; currentCol+i < 10; i++) {
			if(board[currentRow][currentCol+i] == null) {
				moves.add(new Position(currentRow, currentCol+i));
			}
		}
		
		// Left
		for (int i = 1; currentCol-i >= 0; i++) {
			if(board[currentRow][currentCol-i] == null) {
				moves.add(new Position(currentRow, currentCol-i));
			}
		}
		
		// Up
		for (int i = 1; currentRow-i >= 0; i++) {
			if(board[currentRow-i][currentCol] == null) {
				moves.add(new Position(currentRow-i, currentCol));
			}
		}
		
		// Down
		for (int i = 1; currentRow+i < 10; i++) {
			if(board[currentRow+i][currentCol] == null) {
				moves.add(new Position(currentRow+i, currentCol));
			}
		}
		
		// Up-left
		for (int i = 1; currentRow-i >= 0 && currentCol-i >= 0; i++) {
			if(board[currentRow-i][currentCol-i] == null) {
				moves.add(new Position(currentRow-i, currentCol-i));
			}
		}
		
		// Up-right
		for (int i = 1; currentRow-i >= 0 && currentCol+i < 10; i++) {
			if(board[currentRow-i][currentCol+i] == null) {
				moves.add(new Position(currentRow-i, currentCol+i));
			}
		}
		
		// Down-right
		for (int i = 1; currentRow+i < 10 && currentCol+i < 10; i++) {
			if(board[currentRow+i][currentCol+i] == null) {
				moves.add(new Position(currentRow+i, currentCol+i));
			}
		}
		
		// Down-left
		for (int i = 1; currentRow+i < 10 && currentCol-i >= 0; i++) {
			if(board[currentRow+i][currentCol-i] == null) {
				moves.add(new Position(currentRow+i, currentCol-i));
			}
		}
		return moves;
	}

}
