package com.androidgames.framework;

/**
 * Defines a cartesian position in the 2D space
 */
public class Position {
	
	public int x, y;
	
	/**
	 * Create a new position set to the origin (0,0)
	 */
	public Position() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Create a new position set to (x,y)
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Checks if this Position has the same coordenates of another position P
	 */
	public boolean equals(Position p){
		if(this.x == p.x && this.y == p.y)
			return true;
		else
			return false;
	}
}