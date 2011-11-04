package com.androidgames.framework;

public class Position {
	
	public int x, y;
	
	public Position() {
		x = 0;
		y = 0;
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Position p){
		if(this.x == p.x && this.y == p.y)
			return true;
		else
			return false;
	}
}