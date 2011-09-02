package com.badlogic.androidgames.mrnom;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class HelpScreens extends Screen {
	private int screenNumber = 1;
	
    public HelpScreens(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 256 && event.y > 416 ) {
                	screenNumber++;
                	if(screenNumber>3){
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
    	g.drawPixmap(Assets.background, 0, 0);
    	switch(screenNumber){
    	case 1:
    		g.drawPixmap(Assets.help1, 64, 100);
    		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    		break;
    	case 2:
    		g.drawPixmap(Assets.help2, 64, 100);
    		g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    		break;
    	case 3:
    		g.drawPixmap(Assets.help3, 64, 100);
    		g.drawPixmap(Assets.buttons, 256, 416, 0, 128, 64, 64);
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