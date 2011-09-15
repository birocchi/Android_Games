package com.androidgames.framework;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Input.TouchEvent;

public abstract class Screen {
    protected final Game game;
    
    protected boolean debugBounds = false;
    
    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
    
    protected class Bounds {
    	public int x, y, width, height, clickEvent;
    	public Bounds(int clickEvent, int x, int y, int width, int height){
    		this.x = x;
    		this.y = y;
    		this.width = width;
    		this.height = height;
    		this.clickEvent = clickEvent;
    	}
    }
    
    protected int eventInBounds(List<Bounds> bounds, TouchEvent event){
    	for(Bounds b : bounds){
    		if(inBounds(event, b)==true){
    			return b.clickEvent;
    		}
    	}
    	return -1;
    }
    
    protected boolean inBounds(TouchEvent event, Bounds b) {
        if(event.x > b.x && event.x < b.x + b.width - 1 && 
           event.y > b.y && event.y < b.y + b.height - 1) 
            return true;
        else
            return false;
    }
    
    protected void drawDebugBounds(Graphics g, List<Bounds> Bounds){
    	for(Bounds b : Bounds){
    		g.drawRect(b.x, b.y, b.width, b.height, Color.GREEN, 130);
    		g.drawRect(b.x+1, b.y+1, b.width-2, b.height-2, Color.RED, 130);
    	}
    }
}
