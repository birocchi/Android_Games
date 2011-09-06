package com.androidgames.accelerateit;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Screen;


public class MainScreen extends Screen {
	float accelX, accelY, accelZ;
	double accelTotal, accelBest;
	
    public MainScreen(Game game) {
        super(game);               
    }
    
    @Override
    public void update(float deltaTime) {
    	int len;

        List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK  && kevent.type == KeyEvent.KEY_UP)
	    		game.finish();
        }
        
    	accelX = game.getInput().getAccelX();
    	accelY = game.getInput().getAccelY();
    	accelZ = game.getInput().getAccelZ();
    	
    	accelTotal = Math.sqrt(Math.pow(accelX,2)+Math.pow(accelY,2)+Math.pow(accelZ,2));
    	if(accelTotal>accelBest)
    		accelBest = accelTotal;
    }
    
    @Override
    public void present(float deltaTime) {
    	Graphics g = game.getGraphics();
    	float textSize = 30;

    	g.clear(Color.BLACK);
    	g.drawText(" X: "+String.format("%.2f",accelX), 20, 40, Color.WHITE, textSize);
    	g.drawText(" Y: "+String.format("%.2f",accelY), 20, 80, Color.WHITE, textSize);
    	g.drawText(" Z: "+String.format("%.2f",accelZ), 20, 120, Color.WHITE, textSize);
    	
    	
    	g.drawText(" Total: "+String.format("%.2f",accelTotal), 20, 180, Color.WHITE, textSize);
    	
    	
    	g.drawText(" Best: "+String.format("%.2f",accelBest), 20, 240, Color.WHITE, textSize);
    }

    @Override
    public void pause() {        

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
    
}
