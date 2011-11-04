package com.androidgames.mrmunch;

import java.util.ArrayList;
import java.util.List;

public class Snake {
	
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    
    private static final int WORLD_HORIZONTAL_UPPER_LIMIT = 15;
    private static final int WORLD_HORIZONTAL_LOWER_LIMIT = 0;
    private static final int WORLD_VERTICAL_UPPER_LIMIT = 15;
    private static final int WORLD_VERTICAL_LOWER_LIMIT = 0;
    
    private static final int SNAKE_INITIAL_SIZE = 8; //MAX 8
    
    public List<SnakePart> parts = new ArrayList<SnakePart>();
    public int direction;
    public boolean already_turned;
    
    public Snake() {        
        direction = UP;
        for(int i=0; i<SNAKE_INITIAL_SIZE; i++){
        	parts.add(new SnakePart(8, 8+i));
        }
        already_turned = false;
    }
    
    public void turn(int turnDirection) {
    	switch(turnDirection){
    	case UP:
    		if(direction!=DOWN){
        		direction = UP;
                already_turned = true;	
    		}
            break;
    	case LEFT:
        	if(direction!=RIGHT){
        		direction = LEFT;
            	already_turned = true;
        	}
            break;
    	case DOWN:
        	if(direction!=UP){
        		direction = DOWN;
                already_turned = true;
        	}
            break;
    	case RIGHT:
        	if(direction!=LEFT){
        		direction = RIGHT;
                already_turned = true;
        	}
            break;
    	}
    }
    
    
    public void eat() {
        SnakePart end = parts.get(parts.size()-1); 
        parts.add(new SnakePart(end.x, end.y));
    }
    
    public void advance() {
        SnakePart head = parts.get(0);               
        
        int len = parts.size() - 1;
        for(int i = len; i > 0; i--) {
            SnakePart before = parts.get(i-1);
            SnakePart part = parts.get(i);
            part.x = before.x;
            part.y = before.y;
        }
        
        if(direction == UP)
            head.y -= 1;
        if(direction == LEFT)
            head.x -= 1;
        if(direction == DOWN)
            head.y += 1;
        if(direction == RIGHT)
            head.x += 1;
        
        if(head.x < WORLD_HORIZONTAL_LOWER_LIMIT)
            head.x = WORLD_HORIZONTAL_UPPER_LIMIT;
        if(head.x > WORLD_HORIZONTAL_UPPER_LIMIT)
            head.x = WORLD_HORIZONTAL_LOWER_LIMIT;
        if(head.y < WORLD_VERTICAL_LOWER_LIMIT)
            head.y = WORLD_VERTICAL_UPPER_LIMIT;
        if(head.y > WORLD_VERTICAL_UPPER_LIMIT)
            head.y = WORLD_VERTICAL_LOWER_LIMIT;
        
        already_turned = false;
    }
    
    public boolean isAlmostToDie(){
    	SnakePart next = new SnakePart(parts.get(0).x,parts.get(0).y);
        if(direction == UP)
            next.y -= 1;
        if(direction == LEFT)
            next.x -= 1;
        if(direction == DOWN)
            next.y += 1;
        if(direction == RIGHT)
            next.x += 1;
        
        if(next.x < WORLD_HORIZONTAL_LOWER_LIMIT)
            next.x = WORLD_HORIZONTAL_UPPER_LIMIT;
        if(next.x > WORLD_HORIZONTAL_UPPER_LIMIT)
            next.x = WORLD_HORIZONTAL_LOWER_LIMIT;
        if(next.y < WORLD_VERTICAL_LOWER_LIMIT)
            next.y = WORLD_VERTICAL_UPPER_LIMIT;
        if(next.y > WORLD_VERTICAL_UPPER_LIMIT)
            next.y = WORLD_VERTICAL_LOWER_LIMIT;
        
        int len = parts.size();
        for(int i = 1; i < len-1; i++) {
            SnakePart part = parts.get(i);
            if(part.x == next.x && part.y == next.y)
                return true;
        }        
        return false;
    }
    
    public boolean checkBitten() {
        int len = parts.size();
        SnakePart head = parts.get(0);
        for(int i = 1; i < len; i++) {
            SnakePart part = parts.get(i);
            if(part.x == head.x && part.y == head.y)
                return true;
        }        
        return false;
    }
    
    public void shrink() {
    	for (int i = 0; i < 5; i++) {
    		SnakePart end = parts.get(parts.size()-1);
    		parts.remove(end);
    	}
    }
}
