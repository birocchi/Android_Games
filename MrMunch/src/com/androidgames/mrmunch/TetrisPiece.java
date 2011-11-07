package com.androidgames.mrmunch;

import java.util.ArrayList;

import com.androidgames.framework.Position;

public class TetrisPiece {
	
	public static final int SQUARE = 0;
	public static final int LINE = 1;
	public static final int L = 2;
	public static final int S = 3;
	public static final int T = 4;
	
	public static boolean match(int pieceType, ArrayList<Position> nodes){

		Position startPos = nodes.get(0);
		
		switch(pieceType){
		
		case SQUARE:
			if( alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&		//□ □
				alreadyHave(new Position(startPos.x-1,startPos.y-1), nodes) &&		//□ ■
				alreadyHave(new Position(startPos.x,startPos.y-1), nodes) )			//
				return true;
			else
				return false;
			
		case LINE:																	//□
			if( alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&		//□
				alreadyHave(new Position(startPos.x,startPos.y-2), nodes) &&		//■
				alreadyHave(new Position(startPos.x,startPos.y+1), nodes) )			//□
				return true;
			else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//
					alreadyHave(new Position(startPos.x-2,startPos.y), nodes) &&	//□ □ ■ □
					alreadyHave(new Position(startPos.x+1,startPos.y), nodes) )		//
				return true;
			else
				return false;

		case L:
			if( alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&			//□
				alreadyHave(new Position(startPos.x,startPos.y+1), nodes) &&			//■
				alreadyHave(new Position(startPos.x+1,startPos.y+1), nodes) )			//□ □
					return true;
				else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//    □
						alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//□ ■ □
						alreadyHave(new Position(startPos.x+1,startPos.y-1), nodes) )	//  
					return true;
				else if(alreadyHave(new Position(startPos.x,startPos.y+1), nodes) &&	//□ □
						alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//  ■
						alreadyHave(new Position(startPos.x-1,startPos.y-1), nodes) )	//  □
					return true;
				else if(alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//□ ■ □
						alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□
						alreadyHave(new Position(startPos.x-1,startPos.y+1), nodes) )	//
					return true;
			    //Inverted L
				else if( alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//  □
						alreadyHave(new Position(startPos.x,startPos.y+1), nodes) &&	//  ■
						alreadyHave(new Position(startPos.x-1,startPos.y+1), nodes) )	//□ □
					return true;
				else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□ ■ □
						alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//    □
						alreadyHave(new Position(startPos.x+1,startPos.y+1), nodes) )	//  
					return true;
				else if(alreadyHave(new Position(startPos.x,startPos.y+1), nodes) &&	//□ □
						alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//■
						alreadyHave(new Position(startPos.x+1,startPos.y-1), nodes) )	//□
					return true;
				else if(alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//□
						alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□ ■ □
						alreadyHave(new Position(startPos.x-1,startPos.y-1), nodes) )	//
					return true;
				else
					return false;
			
		case S:
			if( alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&		//  □ □
				alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&		//□ ■
				alreadyHave(new Position(startPos.x+1,startPos.y-1), nodes) )		//
				return true;
			else if(alreadyHave(new Position(startPos.x,startPos.y+1), nodes) &&	//□
					alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□ ■
					alreadyHave(new Position(startPos.x-1,startPos.y-1), nodes) )	//  □
				return true;
			//Inverted S
			else if(alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//□ □
					alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//  ■ □
					alreadyHave(new Position(startPos.x-1,startPos.y-1), nodes) )	//
				return true;
			else if(alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//  □
					alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□ ■
					alreadyHave(new Position(startPos.x-1,startPos.y+1), nodes) )	//□
				return true;
			else
				return false;

		case T:
			if( alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&		//□
				alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&		//■ □
				alreadyHave(new Position(startPos.x,startPos.y+1), nodes) )			//□
				return true;
			else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//  □
					alreadyHave(new Position(startPos.x,startPos.y-1), nodes) &&	//□ ■
					alreadyHave(new Position(startPos.x,startPos.y+1), nodes) )		//  □
				return true;
			else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//  □
					alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//□ ■ □
					alreadyHave(new Position(startPos.x,startPos.y-1), nodes) )		//
				return true;
			else if(alreadyHave(new Position(startPos.x-1,startPos.y), nodes) &&	//□ ■ □
					alreadyHave(new Position(startPos.x+1,startPos.y), nodes) &&	//  □
					alreadyHave(new Position(startPos.x,startPos.y+1), nodes) )		//
				return true;
			else
				return false;
			
		default:
			return false;
		}

	}
	
	private static boolean alreadyHave(Position p, ArrayList<Position> nodes){
		for(int i=0; i < nodes.size(); i++){
			if(p.equals(nodes.get(i))) 
				return true;
		}
		return false;		
	}

}
