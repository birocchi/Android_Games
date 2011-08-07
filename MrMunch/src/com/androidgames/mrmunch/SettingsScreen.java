package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class SettingsScreen extends Screen {
	
	private final int BUTTON_PREV_X = 0;
	private final int BUTTON_PREV_Y = 416;
	
	private final int SETTINGS_IMAGE_X = 64;
	private final int SETTINGS_IMAGE_Y = 20;
	
	public SettingsScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> events = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        Graphics g = game.getGraphics();
        int len = events.size();
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
	                if (event.x > x && event.x < x + Assets.NUMBER_WIDTH && event.y > y && event.y < y + Assets.NUMBER_HEIGHT) {
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
        for(int i = 1; i <= 5; i++) {
        	g.drawText(g, ""+i, i * (g.getWidth()-30)/5 - Assets.NUMBER_WIDTH , g.getHeight()/2);
        }
		g.drawCircle(Settings.gameSpeed * (g.getWidth()-30)/5 + 3, g.getHeight()/2 + Assets.NUMBER_HEIGHT/2, 4, Color.GREEN);
        g.drawPixmap(Assets.buttons, BUTTON_PREV_X, BUTTON_PREV_Y, Assets.BUTTON_PREV_SCRX, Assets.BUTTON_PREV_SCRY, Assets.BUTTON_WIDTH, Assets.BUTTON_HEIGHT);
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
