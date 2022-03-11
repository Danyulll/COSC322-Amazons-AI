package ubc.cosc322;

import java.util.ArrayList;

public class Position implements Cloneable {
	public int row, col;
	
	public Position(int x, int y) {
		this.row = x;
		this.col = y;
	}
	
	public int getX() {
		return row;
	}
	
	public int getY() {
		return col;
	}
	
	public ArrayList<Integer> getXY() {
		ArrayList<Integer> xy = new ArrayList<Integer>();
		xy.add(row);
		xy.add(col);
		return xy;
	}
	
	public String toString() {
		return row+","+col;
	}

	public Object clone() {
		Position clone = new Position(this.row,this.col);
		return clone;
	}
	
}
