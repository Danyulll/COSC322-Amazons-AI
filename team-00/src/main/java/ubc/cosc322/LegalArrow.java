package ubc.cosc322;

import java.util.ArrayList;

public class LegalArrow {
	
	//x: row of the current queen
	//y: column of the current queen
	//return ArrayList<Position> of all possible moves from the current queen
	//if there's other arrows/queens in the way, break
	public ArrayList<Position> getLegalArrow (int x, int y, Board board) {
		ArrayList<Position> legalArrowMoves = new ArrayList<>();
		int currentRow = x;
		int currentCol = y;
		
		// Right
		for (int i = 1; currentCol+i < 10; i++) {
			if(board.getBoardPos(currentRow, currentCol+i) == 0) {
				legalArrowMoves.add(new Position(currentRow, currentCol+i));
			} else {
				break;
			}
		}
			
		// Left
		for (int i = 1; currentCol-i >= 0; i++) {
			if(board.getBoardPos(currentRow, currentCol-i) == 0) {
				legalArrowMoves.add(new Position(currentRow, currentCol-i));
			} else {
				break;
			}
		}
				
		// Up
		for (int i = 1; currentRow-i >= 0; i++) {
			if(board.getBoardPos(currentRow-i, currentCol) == 0) {
				legalArrowMoves.add(new Position(currentRow-i, currentCol));
			} else {
				break;
			}
		}
				
		// Down
		for (int i = 1; currentRow+i < 10; i++) {
			if(board.getBoardPos(currentRow+i, currentCol) == 0) {
				legalArrowMoves.add(new Position(currentRow+i, currentCol));
			} else {
				break;
			}
		}
				
		// Up-left
		for (int i = 1; currentRow-i >= 0 && currentCol-i >= 0; i++) {
			if(board.getBoardPos(currentRow-i, currentCol-i) == 0) {
				legalArrowMoves.add(new Position(currentRow-i, currentCol-i));
			} else {
				break;
			}
		}
				
		// Up-right
		for (int i = 1; currentRow-i >= 0 && currentCol+i < 10; i++) {
			if(board.getBoardPos(currentRow-i, currentCol+i) == 0) {
				legalArrowMoves.add(new Position(currentRow-i, currentCol+i));
			} else {
				break;
			}
		}
				
		// Down-right
		for (int i = 1; currentRow+i < 10 && currentCol+i < 10; i++) {
			if(board.getBoardPos(currentRow+i, currentCol+i) == 0) {
				legalArrowMoves.add(new Position(currentRow+i, currentCol+i));
			} else {
				break;
			}
		}
			
		// Down-left
		for (int i = 1; currentRow+i < 10 && currentCol-i >= 0; i++) {
			if(board.getBoardPos(currentRow+i, currentCol-i) == 0) {
				legalArrowMoves.add(new Position(currentRow+i, currentCol-i));
			} else {
				break;
			}
		}
		return legalArrowMoves;
	}

}
