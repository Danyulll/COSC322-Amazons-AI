package ubc.cosc322;

import java.util.ArrayList;

public class Queen {
	public Position queenCurr;
	public Position queenNext;
	public Position arrow;
	
	public Queen (Position queenCurr, Position queenNext, Position arrow) {
		this.queenCurr = queenCurr;
		this.queenNext = queenNext;
		this.arrow = arrow;
	}
	//this is for when just need a position for generating legal moves
	public Queen (Position queenCurr) {
		this.queenCurr = queenCurr;
	}
	
	public int getQueenCurrX() {
		return queenCurr.getX();
	}
	public int getQueenCurrY() {
		return queenCurr.getY();
	}
	public ArrayList<Integer> getQueenCurr(){
		return queenCurr.getXY();
	}
	
	public int getQueenNextX() {
		return queenNext.getX();
	}
	public int getQueenNextY() {
		return queenNext.getY();
	}
	public ArrayList<Integer> getQueenNext(){
		return queenNext.getXY();
	}
	
	public int getArrowX() {
		return arrow.getX();
	}
	public int getArrowY() {
		return arrow.getY();
	}
	public ArrayList<Integer> getArrow(){
		return arrow.getXY();
	}
}
