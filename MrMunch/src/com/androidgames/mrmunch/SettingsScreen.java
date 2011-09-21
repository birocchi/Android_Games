package com.androidgames.mrmunch;

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
	
	public SettingsScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(float deltaTime) {
		int len;
		List<TouchEvent> events = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
        
        len = keyEvents.size();
        for(int i=0; i<len; i++){
        	KeyEvent kevent = keyEvents.get(i);
	        if(kevent.keyCode == android.view.KeyEvent.KEYCODE_BACK && kevent.type == KeyEvent.KEY_UP)
	    		game.setScreen(new MainMenuScreen(game));
        }
        
        Graphics g = game.getGraphics();
        len = events.size();
        for(int i=0; i<len; i++) {
        	TouchEvent event = events.get(i);
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < Assets.BUTTON_WIDTH && event.y > g.getHeight() - Assets.BUTTON_HEIGHT ) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
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
        g.drawPixmap(Assets.buttons, BUTTON_OK_X, BUTTON_OK_Y, Assets.BUTTON_OK_SCRX, Assets.BUTTON_OK_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
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
