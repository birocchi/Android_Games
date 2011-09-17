package com.androidgames.mrmunch;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Input.KeyEvent;
import com.androidgames.framework.Input.TouchEvent;
import com.androidgames.framework.Screen;

public class SettingsScreen extends Screen {
	
	private final int BUTTON_PREV_X = 0;
	private final int BUTTON_PREV_Y = 416;
	
	private final int BUTTON_RESET_X = 100;
	private final int BUTTON_RESET_Y = 416;
	
	private final int SETTINGS_IMAGE_X = 64;
	private final int SETTINGS_IMAGE_Y = 20;
	
    private List<Bounds> mBounds;
	
	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_BACK = 0;
	private final int CLICK_RESET = 1;
	
	public SettingsScreen(Game game) {
		super(game);
        Graphics g = game.getGraphics();
        
        //Defining the BOUNDS where some CLICK_EVENT should happen
        mBounds = new ArrayList<Bounds>();
        mBounds.add(new Bounds(CLICK_BACK, BUTTON_PREV_X, g.getHeight() - Assets.BUTTON_HEIGHT, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
        mBounds.add(new Bounds(CLICK_RESET, BUTTON_RESET_X, BUTTON_RESET_Y, 2*Assets.BUTTON_WIDTH, 2*Assets.BUTTON_HEIGHT));        
    }
	
	@Override
	public void update(float deltaTime) {
		int len, clickEvent;
		List<TouchEvent> events = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
		//Dealing with KEY EVENTS
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == KeyEvent.KEY_UP)
	    		game.setScreen(new MainMenuScreen(game));
        }
        
        //Dealing with TOUCH EVENTS
        Graphics g = game.getGraphics();
        len = events.size();
        for(int i=0; i<len; i++) {
        	TouchEvent event = events.get(i);
        	if (event.type == TouchEvent.TOUCH_UP) {
        		
        		clickEvent = eventInBounds(mBounds, event);
        		
        		switch(clickEvent){
            	case CLICK_BACK:
            		game.setScreen(new MainMenuScreen(game));
            		break;
            	case CLICK_RESET:
            		Settings.resetScore();
            		break;
        		}
        		//play sound if clicked a item and sound is enabled
            	if(clickEvent != CLICK_NO_EVENT && Settings.soundEnabled)
            		Assets.click.play(1);
            }
        	
        	int y = g.getHeight()/2;
        	
        	for (int j=1; j<=5; j++) {
	        	if (event.type == TouchEvent.TOUCH_UP) {
	        		int x = j*(g.getWidth()-30)/5 - Assets.NUMBER_WIDTH;
	                if (event.x > x - 6 && event.x < x + Assets.NUMBER_WIDTH + 6 && event.y > y - 4 && event.y < y + Assets.NUMBER_HEIGHT + 4) {
	                    if(Settings.soundEnabled)
	                        Assets.click.play(1);
	                    Settings.gameSpeed = j;
	                    return;
	                }
	            }
	        }
        }
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.mainMenu, SETTINGS_IMAGE_X, SETTINGS_IMAGE_Y, Assets.SETTINGS_SCRX, Assets.SETTINGS_SCRY, Assets.MENU_ITEM_WIDTH, Assets.MENU_ITEM_HEIGHT);
        g.drawPixmap(Assets.initialspeed, g.getWidth()/2 - Assets.initialspeed.getWidth()/2, g.getHeight()/2 - Assets.initialspeed.getHeight());
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.NUMBER_WIDTH - 6, g.getHeight()/2 - 4, Assets.NUMBER_WIDTH + 12, Assets.NUMBER_HEIGHT + 8, Color.WHITE);
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.NUMBER_WIDTH - 4, g.getHeight()/2 - 2, Assets.NUMBER_WIDTH + 8, Assets.NUMBER_HEIGHT + 4, Color.BLACK);
        for(int i = 1; i <= 5; i++) {
        	g.drawText(g, ""+i, i * (g.getWidth()-30)/5 - Assets.NUMBER_WIDTH , g.getHeight()/2);
        }
        g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_LEFT_SCRX, Assets.BUTTON_LEFT_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        g.drawPixmap(Assets.buttons, BUTTON_RESET_X, BUTTON_RESET_Y, Assets.BUTTON_RESET_SCRX, Assets.BUTTON_RESET_SCRY, 2*Assets.BUTTON_WIDTH, 2*Assets.BUTTON_HEIGHT);
        
        if(DEBUG_BOUNDS == true){
        	drawDebugBounds(g, mBounds);
        }
	}
	
	@Override
	public void pause() {
		Settings.save(game.getFileIO());

	}

	@Override
	public void resume() {

	}
	
	@Override
	public void dispose() {

	}

}
