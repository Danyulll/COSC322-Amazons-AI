package ubc.cosc322;

import java.util.ArrayList;

public class TerritoryHeuristic {

	public ArrayList<int[]> WhiteQueenLocations(int[][] board) {
		ArrayList<int[]> WhiteQueensLocations = new ArrayList<>(4);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1) {
					for (int q = 0; q < 4; q++) {
						int[] value = new int[2];
						value[0] = i;
						value[1] = j;
						WhiteQueensLocations.add(value);
					}
				}
			}
		}
		/*
		System.out.println(
				"Location of Queen 1: " + WhiteQueensLocations.get(0)[0] + "," + WhiteQueensLocations.get(0)[1]);
		System.out
				.println("Location of Queen 2: " + WhiteQueensLocations.get(1)[0] + "," + WhiteQueensLocations.get(1)[1]);
		System.out
				.println("Location of Queen 3: " + WhiteQueensLocations.get(2)[0] + "," + WhiteQueensLocations.get(2)[1]);
		System.out
				.println("Location of Queen 4: " + WhiteQueensLocations.get(3)[0] + "," + WhiteQueensLocations.get(3)[1]);
*/
		return WhiteQueensLocations;
	}

	public ArrayList<int[]> BlackQueenLocations(int[][] board) {
		ArrayList<int[]> BlackQueensLocations = new ArrayList<>(4);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 2) {
					for (int q = 0; q < 4; q++) {
						int[] value = new int[2];
						value[0] = i;
						value[1] = j;
						BlackQueensLocations.add(value);
					}
				}
			}
		}
		return BlackQueensLocations;
	}

	public int[][] closestQueen(Board board1) {
		int[] white, black;
		int[][] board = board1.board;
		double dw = 1000;
		double db = 1000;
		int[][] owned = new int[10][10];

		ArrayList<int[]> WqueensLocations = WhiteQueenLocations(board);
		ArrayList<int[]> BqueensLocations = BlackQueenLocations(board);
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] == 0) {
					white = WqueensLocations.get(0);
					dw = Math.sqrt(Math.pow(white[0] - i, 2) + Math.pow(white[1] - j, 2));
					black = BqueensLocations.get(0);
					db = Math.sqrt(Math.pow(black[0] - i, 2) + Math.pow(black[1] - j, 2));

					for (int q = 1; q < 4; q++) {

						white = WqueensLocations.get(q);
						double z = Math.sqrt(Math.pow(white[0] - i, 2) + Math.pow(white[1] - j, 2));
						if (z < dw) {
							dw = z;

						}
					}
					for (int q = 1; q < 4; q++) {

						black = BqueensLocations.get(q);
						double z = Math.sqrt(Math.pow(black[0] - i, 2) + Math.pow(black[1] - j, 2));
						if (z < db) {
							db = z;
						}
					}
					if (db < dw) {
						owned[i][j] = 2; // square at i,j is owned by black
					} else if (db > dw) {
						owned[i][j] = 1; // square at i,j is owned by white
					} else {
						owned[i][j] = 0; // square at i,j is neutral
					}
				} else if (board[i][j] == 3) {

					owned[i][j] = 3;
				}
			}
		}
		 //printHeuristic(owned); // only for testing
		return owned;
	}

	public void printHeuristic(int[][] matrix) {
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

}
