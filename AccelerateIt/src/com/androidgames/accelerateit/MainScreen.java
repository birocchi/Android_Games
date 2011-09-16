package com.androidgames.accelerateit;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;


public class MainScreen extends Screen {
	private final int RECT_X = 40;
	private final int RECT_Y = 400;
	private final int RECT_WIDTH = 400;
	private final int RECT_HEIGHT = 200;
	
	private final int MIN_PULSE = 22;
	
	float accelX, accelY, accelZ;
	double accelTotal, accelBest;
	int pulses;
	double accelPulse;
	double accelAnt=0;
	boolean pulsed = false;
	boolean graphPaused = false;
	
	ArrayList<Double> accels = new ArrayList<Double>();
	
    public MainScreen(Game game) {
        super(game);               
    }
    
    @Override
    public void update(float deltaTime) {
    	int len;
    	List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
    	
   		len = touchEvents.size();
   		for(int i = 0; i < len; i++) {
   			TouchEvent event = touchEvents.get(i);
   			if(event.type == TouchEvent.TOUCH_DOWN)
   				graphPaused = !graphPaused;
        }
    	
        
        
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
    	
    	if(graphPaused==false){
	    	if(accels.size()<RECT_WIDTH/2){
	    		accels.add(accelTotal);
	    	} else {
	    		accels.remove(0);
	    		accels.add(accelTotal);
	    	}
    	}
    	
    	if(accelTotal>accelBest)
    		accelBest = accelTotal;
    	
    	if(accelTotal> MIN_PULSE && accelTotal>accelAnt){
    		if (pulsed == false)
    			pulsed=true;
    		if(accelTotal> accelPulse)
    			accelPulse = accelTotal;
    	}
    	if(accelTotal<(0.7*accelPulse) && pulsed == true){
    		pulsed= false;
    		pulses++;
    		accelPulse=0;
    	}
    	accelAnt=accelTotal;
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
    	
    	g.drawText(" Pulses: "+String.format("%d",pulses), 20, 300, Color.WHITE, textSize);
    	
    	g.drawRect(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT, Color.DKGRAY);
    	
    	g.drawLine(RECT_X, RECT_Y+RECT_HEIGHT-2*MIN_PULSE, RECT_X+RECT_WIDTH, RECT_Y+RECT_HEIGHT-2*MIN_PULSE, Color.GRAY);
    	
    	for(int i=0; i<accels.size(); i++){
    		g.drawRect(RECT_X+2*i, RECT_Y+RECT_HEIGHT-(2*accels.get(i).intValue()), 2, 2, Color.RED);

    	}
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
