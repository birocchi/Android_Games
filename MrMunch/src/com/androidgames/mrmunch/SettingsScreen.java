package com.androidgames.mrmunch;

import java.util.List;

import android.graphics.Color;

import com.androidgames.framework.Game;
import com.androidgames.framework.Graphics;
import com.androidgames.framework.Screen;
import com.androidgames.framework.Input.TouchEvent;

public class SettingsScreen extends Screen {
	
	public SettingsScreen(Game game) {
		super(game);
	}
	
	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> events = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = events.size();
        for(int i=0; i<len; i++) {
        	TouchEvent event = events.get(i);
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y > 416) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        	
        	int y = g.getHeight()/2;
        	if (event.type == TouchEvent.TOUCH_UP) {
        		int x = 1*(g.getWidth()-30)/5 - 20;
                if (event.x > x && event.x < x+20 && event.y > y && event.y < y+32) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    Settings.gameSpeed = 1;
                    return;
                }
            }
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
        		int x = 2*(g.getWidth()-30)/5 - 20;
        		if (event.x > x && event.x < x+20 && event.y > y && event.y < y+32) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    Settings.gameSpeed = 2;
                    return;
                }
            }
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
        		int x = 3*(g.getWidth()-30)/5 - 20;
        		if (event.x > x && event.x < x+20 && event.y > y && event.y < y+32) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    Settings.gameSpeed = 3;
                    return;
                }
            }
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
        		int x = 4*(g.getWidth()-30)/5 - 20;
        		if (event.x > x && event.x < x+20 && event.y > y && event.y < y+32) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    Settings.gameSpeed = 4;
                    return;
                }
            }
        	
        	if (event.type == TouchEvent.TOUCH_UP) {
        		int x = 5*(g.getWidth()-30)/5 - 20;
        		if (event.x > x && event.x < x+20 && event.y > y && event.y < y+32) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    Settings.gameSpeed = 5;
                    return;
                }
            }
        }
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.mainMenu, 64, 20, 0, 86, 196, 43);
        g.drawPixmap(Assets.initialspeed, g.getWidth()/2 - Assets.initialspeed.getWidth()/2, g.getHeight()/2 - Assets.initialspeed.getHeight());
        for(int i = 1; i <= 5; i++) {
        	if(Settings.gameSpeed == i){
        		g.drawText(g, ""+i, i * (g.getWidth()-30)/5 - 20 , g.getHeight()/2);
        		g.drawRect(i * (g.getWidth()-20)/5, g.getHeight()/2+13, 6, 6, Color.GREEN);
        	}
        	else
        		g.drawText(g, ""+i, i * (g.getWidth()-30)/5 - 20 , g.getHeight()/2);
        }
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
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
