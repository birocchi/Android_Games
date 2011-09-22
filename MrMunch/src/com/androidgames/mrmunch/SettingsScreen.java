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
	
	private final int BUTTON_OK_X = 0;
	private final int BUTTON_OK_Y = 416;

	private final int SETTINGS_IMAGE_X = 64;
	private final int SETTINGS_IMAGE_Y = 20;

	private List<Bounds> mBounds;

	private final int CLICK_NO_EVENT = -1;
	private final int CLICK_1 = 1;
	private final int CLICK_2 = 2;
	private final int CLICK_3 = 3;
	private final int CLICK_4 = 4;
	private final int CLICK_5 = 5;
	private final int CLICK_OK = 6;

	public SettingsScreen(Game game) {
		super(game);
		
		//Defining the BOUNDS where some CLICK_EVENT should happen
		Graphics g = game.getGraphics();
        mBounds = new ArrayList<Bounds>();
        for(int i = 1; i <= 5; i++) {
        	mBounds.add(new Bounds(i,
        			               i * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 6,
        			               g.getHeight()/2 - 4,
        			               Assets.CHARACTER_WIDTH + 12,
        			               Assets.CHARACTER_HEIGHT + 8
        			               ));
        }
        mBounds.add(new Bounds(CLICK_OK, BUTTON_OK_X, BUTTON_OK_Y, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT));
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
        len = events.size();
        for(int i=0; i<len; i++) {
        	TouchEvent event = events.get(i);
        	if (event.type == TouchEvent.TOUCH_UP) {
        		
        		clickEvent = eventInBounds(mBounds, event);

            	//play sound if clicked a item and sound is enabled
            	if(clickEvent != CLICK_NO_EVENT && Settings.soundEnabled)
            		Assets.click.play(1);

            	switch(clickEvent){
            	case CLICK_1:
            		Settings.gameSpeed = 1;
            		break;
            	case CLICK_2:
            		Settings.gameSpeed = 2;
            		break;
            	case CLICK_3:
            		Settings.gameSpeed = 3;
            		break;
            	case CLICK_4:
            		Settings.gameSpeed = 4;
            		break;
            	case CLICK_5:
            		Settings.gameSpeed = 5;
            		break;
            	case CLICK_OK:
            		game.setScreen(new MainMenuScreen(game));
            		break;
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
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 6, g.getHeight()/2 - 4, Assets.CHARACTER_WIDTH + 12, Assets.CHARACTER_HEIGHT + 8, Color.WHITE);
        g.drawRect(Settings.gameSpeed * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH - 4, g.getHeight()/2 - 2, Assets.CHARACTER_WIDTH + 8, Assets.CHARACTER_HEIGHT + 4, Color.BLACK);
        for(int i = 1; i <= 5; i++) {
        	g.drawText(g, Assets.characters, ""+i, i * (g.getWidth()-30)/5 - Assets.CHARACTER_WIDTH , g.getHeight()/2);
        }
        g.drawPixmap(Assets.buttons, BUTTON_OK_X, BUTTON_OK_Y, Assets.BUTTON_OK_SCRX, Assets.BUTTON_OK_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
        
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
