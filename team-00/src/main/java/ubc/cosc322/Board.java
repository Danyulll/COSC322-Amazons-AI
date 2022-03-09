package ubc.cosc322;

import java.util.ArrayList;

public class Board {
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
		board[1-1][4-1] = BLACK;
		board[1-1][7-1] = BLACK;
		board[4-1][1-1] = BLACK;
		board[4-1][10-1] = BLACK;
		
		board[7-1][1-1] = WHITE;
		board[10-1][4-1] = WHITE;
		board[10-1][7-1] = WHITE;
		board[7-1][10-1] = WHITE;
		
		initialPos();
	}
	
	public void initialPos() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if(board[i][j] == WHITE) {
					WhitePos.add(new Position(i,j));
				} else if (board[i][j] == BLACK) {
					BlackPos.add(new Position(i,j));
				}
			}
		}
	}
	
	public void printBoard() {
		for (int i = 0; i < ROWS; i++) {
			System.out.print("| ");
			for (int j = 0; j < COLS; j++) {
				if(board[i][j] == WHITE) {
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
	
	public void printBoard2() {
		int[][] matrix = this.board;
		System.out.println("**********");
		int counter = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				counter++;
				System.out.print(matrix[i][j]);
				if (counter == 10) {
					System.out.println();

					counter = 0;
				}

			}
		}
		System.out.println("**********");
	}
	
	// getter and setter
	public int getBoardPos(int x, int y) {
		return board[x][y];
	}
	
	public ArrayList<Position> getWhitePos(){
		return WhitePos;
	}
	
	public ArrayList<Position> getBlackPos(){
		return BlackPos;
	}
	
	public ArrayList<Position> setWhitePos(){
		return this.WhitePos = WhitePos;
	}
	
	public ArrayList<Position> setBlackPos(){
		return this.BlackPos = BlackPos;
	}

}
