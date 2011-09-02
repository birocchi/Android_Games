package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;


public class HelpScreens extends Screen {  
	private int screenNumber = 1;
	
	private final int BUTTON_PREV_X = 0;
	private final int BUTTON_PREV_Y = 416;
	
	private final int BUTTON_NEXT_X = 256;
	private final int BUTTON_NEXT_Y = 416;
	
	private final int BUTTON_CANCEL_ESQ_X = 0;
	private final int BUTTON_CANCEL_ESQ_Y = 416;
	
	private final int BUTTON_CANCEL_DIR_X = 256;
	private final int BUTTON_CANCEL_DIR_Y = 416;
	
	private final int HELP_IMAGE_X = 64;
	private final int HELP_IMAGE_Y = 100;
	
    public HelpScreens(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
    	int len;
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == kevent.KEY_UP)
	        	screenNumber--;
	        	if(screenNumber<1)
	        		game.setScreen(new MainMenuScreen(game));
        }
        
        Graphics g = game.getGraphics();
        len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                	            	
                if(event.x > g.getWidth() - Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT ) {
                	screenNumber++;
                	if(screenNumber>3){
                		game.setScreen(new MainMenuScreen(game));
                	}
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(event.x < Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT ) {
                	screenNumber--;
                	if(screenNumber<1){
                		game.setScreen(new MainMenuScreen(game));
                	}
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.clear(Color.BLACK);
        
    	switch(screenNumber){
    	case 1:
    		g.drawPixmap(Assets.help1, HELP_IMAGE_X, HELP_IMAGE_Y);
            g.drawPixmap(Assets.buttons, BUTTON_NEXT_X, BUTTON_NEXT_Y, Assets.BUTTON_RIGHT_SCRX, Assets.BUTTON_RIGHT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
            g.drawPixmap(Assets.buttons, BUTTON_CANCEL_ESQ_X, BUTTON_CANCEL_ESQ_Y, Assets.BUTTON_CANCEL_SCRX, Assets.BUTTON_CANCEL_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    		break;
    	case 2:
    		g.drawPixmap(Assets.help2, HELP_IMAGE_X, HELP_IMAGE_Y);
            g.drawPixmap(Assets.buttons, BUTTON_NEXT_X, BUTTON_NEXT_Y, Assets.BUTTON_RIGHT_SCRX, Assets.BUTTON_RIGHT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
            g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    		break;
    	case 3:
    		g.drawPixmap(Assets.help3, HELP_IMAGE_X, HELP_IMAGE_Y);
            g.drawPixmap(Assets.buttons, BUTTON_CANCEL_DIR_X, BUTTON_CANCEL_DIR_Y, Assets.BUTTON_CANCEL_SCRX, Assets.BUTTON_CANCEL_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
            g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
    		break;
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